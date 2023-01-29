package com.qiuyu.demo.resource;

import com.qiuyu.demo.dto.SseEmitterResultVO;
import com.qiuyu.demo.service.SseEmitterService;
import com.qiuyu.demo.utils.Result;
import com.qiuyu.demo.utils.ResultEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.Executor;

/**
 * @Description: 长链接SSE学习
 * @Author: qiuyu
 * @Date: 2021/4/7
 **/
@RestController
@RequestMapping("sse")
public class ServerSentEventsController {

    @Autowired
    private SseEmitterService sseEmitterService;

    @Qualifier("taskExecutor")
    @Autowired
    private Executor taskExecutor;


    /**
     * 创建SSE长链接
     *
     * @param clientId   客户端唯一ID(如果为空，则由后端生成并返回给前端)
     * @return org.springframework.web.servlet.mvc.method.annotation.SseEmitter
     * @author re
     * @date 2021/12/12
     **/
    @GetMapping("/CreateSseConnect")
    public SseEmitter createSseConnect(@RequestParam(name = "clientId", required = false) String clientId) {
        return sseEmitterService.createSseConnect(clientId);
    }

    /**
     * 关闭SSE连接
     *
     * @param clientId 客户端ID
     * @author re
     * @date 2021/12/13
     **/
    @GetMapping("/CloseSseConnect")
    public Result closeSseConnect(String clientId) {
        sseEmitterService.closeSseConnect(clientId);
        return new Result(ResultEnum.OK).ok();
    }


    @GetMapping("/sendTest/{text}")
    public void sendTest(@PathVariable String text) {
        sseEmitterService.sendMsgToClient(Collections.singletonList(new SseEmitterResultVO(1L, text)));
    }

    @GetMapping("/download")
    public SseEmitter download(@RequestParam(name = "clientId", required = false) String clientId) {
        String id = StringUtils.isNotBlank(clientId) ? clientId : UUID.randomUUID().toString();
        SseEmitter sseConnect = sseEmitterService.createSseConnect(id);
        taskExecutor.execute(()->{
            try {
                Thread.sleep(10000L);  // 模拟异步下载
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sseEmitterService.sendMsgToClient(id,Collections.singletonList(new SseEmitterResultVO(8317327131L, "报表生成完成，请下载")));
        });
        return sseConnect;
    }


}
