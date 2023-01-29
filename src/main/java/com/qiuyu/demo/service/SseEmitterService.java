package com.qiuyu.demo.service;

import com.qiuyu.demo.dto.SseEmitterResultVO;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * @Description: SseEmitterService
 * @Author: qiuyu
 * @Date: 2022/4/25
 **/
public interface SseEmitterService {

    SseEmitter createSseConnect(String clientId);

    void closeSseConnect(String clientId);

    SseEmitter getSseEmitterByClientId(String clientId);

    void sendMsgToClient(List<SseEmitterResultVO> sseEmitterResultVOList);

    void sendMsgToClient(String clientId,List<SseEmitterResultVO> sseEmitterResultVOList);



}
