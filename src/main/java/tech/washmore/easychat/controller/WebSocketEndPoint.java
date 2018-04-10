package tech.washmore.easychat.controller;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tech.washmore.easychat.common.Constants;
import tech.washmore.easychat.common.MsgData;
import tech.washmore.easychat.common.uc.LoginUserToken;
import tech.washmore.easychat.common.uc.TokenManager;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Washmore
 * @version V1.0
 * @summary TODO
 * @Copyright (c) 2018, Lianjia Group All Rights Reserved.
 * @since 2018/4/10
 */
@ServerEndpoint("/webSocket")
@Component
public class WebSocketEndPoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketEndPoint.class);
    public static Map<String, Session> connectClients = new HashMap<>();

    @OnMessage
    public void onMessage(String message, Session session) {
        LOGGER.info("Received msg from {}:{}", session.getId(), message);

        MsgData msgData = JSON.parseObject(message, MsgData.class);
        if (msgData.type == 90) {
            MsgData msg = new MsgData();
            LoginUserToken loginUserToken = TokenManager.memoryTokens.get(msgData.content);
            if (loginUserToken == null) {
                return;
            }

            if (loginUserToken.getSessionId() == null) {
                msg.type = 101;
                msg.account = loginUserToken.getAccount();
                msg.content = "天空一声巨响,老子闪亮登场!";
                connectClients.forEach((k, v) -> {
                    if (!k.equals(session.getId())) {
                        v.getAsyncRemote().sendText(JSON.toJSONString(msg));
                    }
                });
            }

            msg.type = 91;
            msg.account = loginUserToken.getAccount();
            msg.content = JSON.toJSONString(TokenManager.memoryTokens.values().stream().map(LoginUserToken::getAccount).collect(Collectors.toList()));
            session.getAsyncRemote().sendText(JSON.toJSONString(msg));
            loginUserToken.setSessionId(session.getId());

        } else if (msgData.type == 100) {
            MsgData msg = new MsgData();
            msg.type = 101;
            msg.account = msgData.account;
            LoginUserToken loginUserToken = TokenManager.memoryTokens.get(msgData.content);
            if (loginUserToken != null) {
                loginUserToken.setExpire(System.currentTimeMillis() + Constants.EXPIRE_HALF_HOUR);
            }
            msg.content = msgData.content;
            connectClients.forEach((k, v) -> {
                v.getAsyncRemote().sendText(JSON.toJSONString(msg));
            });
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        MsgData msg = new MsgData();
        msg.content = JSON.toJSONString(TokenManager.memoryTokens.values().stream().map(LoginUserToken::getAccount).collect(Collectors.toList()));
        msg.type = 92;
        connectClients.forEach((k, v) -> {
            if (!k.equals(session.getId())) {
                v.getAsyncRemote().sendText(JSON.toJSONString(msg));
            }
        });
        connectClients.put(session.getId(), session);

        LOGGER.info("Client connected-id:{}", session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        connectClients.remove(session.getId());
        connectClients.forEach((k, v) -> {
            //  v.getAsyncRemote().sendText(JSON.toJSONString(ImmutableMap.of("userCode", 80000, "content", String.format("%s 伤心的离开了我们!", session.getId()))));
        });

        LOGGER.info("Connection closed-id:{}", session.getId());
    }
}

