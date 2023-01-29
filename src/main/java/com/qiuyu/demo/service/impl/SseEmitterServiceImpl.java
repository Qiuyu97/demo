package com.qiuyu.demo.service.impl;

import com.qiuyu.demo.common.constant.SseEmitterConstant;
import com.qiuyu.demo.dto.SseEmitterResultVO;
import com.qiuyu.demo.service.SseEmitterService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @Description: SseEmitterServiceImpl
 * @Author: qiuyu
 * @Date: 2022/4/25
 **/
@Slf4j
@Service
public class SseEmitterServiceImpl implements SseEmitterService {

    /**
     * 容器，保存连接，用于输出返回
     */
    private static final Map<String, SseEmitter> sseCache = new ConcurrentHashMap<>();

    @Override
    public SseEmitter createSseConnect(String clientId)  {
        // 设置超时时间，0表示不过期。默认30秒，超过时间未完成会抛出异常：AsyncRequestTimeoutException
        SseEmitter sseEmitter = new SseEmitter(1000*60*60L);//超时时间1小时
        // 是否需要给客户端推送ID
        if (StringUtils.isBlank(clientId)) {
            clientId = UUID.randomUUID().toString();
        }
        // 注册回调
        sseEmitter.onCompletion(completionCallBack(clientId));
        sseCache.put(clientId, sseEmitter);
        log.info("创建新的sse连接，当前用户：{}", clientId);

        try {
            sseEmitter.send(SseEmitter.event().id(String.valueOf(System.currentTimeMillis())).data(clientId));
        } catch (IOException e) {
            log.error("SseEmitterServiceImpl[createSseConnect]: 创建长链接异常，客户端ID:{}", clientId, e);
            throw new RuntimeException("创建连接异常！", e);
        }
        return sseEmitter;
    }

    @Override
    public void closeSseConnect(String clientId) {
        SseEmitter sseEmitter = sseCache.get(clientId);
        if (sseEmitter != null) {
            sseEmitter.complete();
            removeUser(clientId);
        }
    }

    // 根据客户端id获取SseEmitter对象
    @Override
    public SseEmitter getSseEmitterByClientId(String clientId) {
        return sseCache.get(clientId);
    }

    // 推送消息到客户端，此处结合业务代码，业务中需要推送消息处调用即可向客户端主动推送消息
    @Override
    public void sendMsgToClient(List<SseEmitterResultVO> sseEmitterResultVOList) {
        if (CollectionUtils.isEmpty(sseCache)) {
            return;
        }
        for (Map.Entry<String, SseEmitter> entry : sseCache.entrySet()) {
            sendMsgToClientByClientId(entry.getKey(), sseEmitterResultVOList, entry.getValue());
        }
    }

    @Override
    public void sendMsgToClient(String clientId, List<SseEmitterResultVO> sseEmitterResultVOList) {
        if (CollectionUtils.isEmpty(sseCache)) {
            return;
        }
        sendMsgToClientByClientId(clientId,sseEmitterResultVOList,sseCache.get(clientId));
    }

    /**
     * 推送消息到客户端
     * 此处做了推送失败后，重试推送机制，可根据自己业务进行修改
     *
     * @param clientId               客户端ID
     * @param sseEmitterResultVOList 推送信息，此处结合具体业务，定义自己的返回值即可
     * @author re
     * @date 2022/3/30
     **/
    private void sendMsgToClientByClientId(String clientId, List<SseEmitterResultVO> sseEmitterResultVOList, SseEmitter sseEmitter) {
        if (sseEmitter == null) {
            log.error("SseEmitterServiceImpl[sendMsgToClient]: 推送消息失败：客户端{}未创建长链接,失败消息:{}",
                    clientId, sseEmitterResultVOList.toString());
            return;
        }

        SseEmitter.SseEventBuilder sendData = SseEmitter.event().id(String.valueOf(System.currentTimeMillis())).data(sseEmitterResultVOList, MediaType.APPLICATION_JSON);
        try {
            sseEmitter.send(sendData);
        } catch (IOException e) {
            // 推送消息失败，记录错误日志，进行重推
            log.error("SseEmitterServiceImpl[sendMsgToClient]: 推送消息失败：{},尝试进行重推", sseEmitterResultVOList.toString(), e);
            boolean isSuccess = true;
            // 推送消息失败后，每隔10s推送一次，推送5次
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(10000);
                    sseEmitter = sseCache.get(clientId);
                    if (sseEmitter == null) {
                        log.error("SseEmitterServiceImpl[sendMsgToClient]：{}的第{}次消息重推失败，未创建长链接", clientId, i + 1);
                        continue;
                    }
                    sseEmitter.send(sendData);
                } catch (Exception ex) {
                    log.error("SseEmitterServiceImpl[sendMsgToClient]：{}的第{}次消息重推失败", clientId, i + 1, ex);
                    continue;
                }
                log.info("SseEmitterServiceImpl[sendMsgToClient]：{}的第{}次消息重推成功,{}", clientId, i + 1, sseEmitterResultVOList.toString());
                return;
            }
            removeUser(clientId);
        }
    }

    /**
     * 长链接完成后回调接口(即关闭连接时调用)
     *
     * @param clientId 客户端ID
     * @return java.lang.Runnable
     * @author re
     * @date 2021/12/14
     **/
    private Runnable completionCallBack(String clientId) {
        return () -> {
            log.info("结束连接：{}", clientId);
            removeUser(clientId);
        };
    }

    /**
     * 连接超时时调用
     *
     * @param clientId 客户端ID
     * @return java.lang.Runnable
     * @author re
     * @date 2021/12/14
     **/
    private Runnable timeoutCallBack(String clientId) {
        return () -> {
            log.info("连接超时：{}", clientId);
            removeUser(clientId);
        };
    }

    /**
     * 推送消息异常时，回调方法
     *
     * @param clientId 客户端ID
     * @return java.util.function.Consumer<java.lang.Throwable>
     * @author re
     * @date 2021/12/14
     **/
    private Consumer<Throwable> errorCallBack(String clientId) {
        return throwable -> {
            log.error("SseEmitterServiceImpl[errorCallBack]：连接异常,客户端ID:{}", clientId);

            // 推送消息失败后，每隔10s推送一次，推送5次
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(10000);
                    SseEmitter sseEmitter = sseCache.get(clientId);
                    if (sseEmitter == null) {
                        log.error("SseEmitterServiceImpl[errorCallBack]：第{}次消息重推失败,未获取到 {} 对应的长链接", i + 1, clientId);
                        continue;
                    }
                    sseEmitter.send("失败后重新推送");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * 移除用户连接
     *
     * @param clientId 客户端ID
     * @author re
     * @date 2021/12/14
     **/
    private void removeUser(String clientId) {
        sseCache.remove(clientId);
        log.info("SseEmitterServiceImpl[removeUser]:移除用户：{}", clientId);
    }
}
