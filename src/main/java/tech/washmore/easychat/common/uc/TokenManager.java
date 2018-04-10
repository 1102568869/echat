package tech.washmore.easychat.common.uc;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tech.washmore.easychat.common.Constants;
import tech.washmore.easychat.common.MsgData;
import tech.washmore.easychat.common.UnLoginException;
import tech.washmore.easychat.controller.WebSocketEndPoint;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Washmore
 * @version V1.0
 * @summary TODO
 * @Copyright (c) 2018, Lianjia Group All Rights Reserved.
 * @since 2018/4/9
 */
@Component
public class TokenManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenManager.class);

    public static final Map<String, LoginUserToken> memoryTokens = new HashMap<>();

    public static final String SOURCE_WX_XCX = "wx_xcx";
    public static final String SOURCE_WEB_PC = "web_pc";

    public String generateToken4WebPc(LoginUserToken user) {
        return this.generateToken(user, SOURCE_WEB_PC);
    }

    private String generateToken(LoginUserToken user, String source) {
        if (memoryTokens.values().stream().anyMatch(ut -> ut.getAccount().equals(user.getAccount()))) {
            throw new UnLoginException(String.format("已存在账户为[%s]的在线用户!", user.getAccount()));
        }
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        user.setExpire(System.currentTimeMillis() + Constants.EXPIRE_HALF_HOUR);
        memoryTokens.put(token, user);
        return token;
    }

    public LoginUserToken getLoginUserToken(String token) {
        return memoryTokens.get(token);
    }

    @Scheduled(cron = "0 0/5 * * * ?")
    public void checkExpireMembers() {
        memoryTokens.values().stream().filter(m -> System.currentTimeMillis() > m.getExpire()).map(LoginUserToken::getToken).collect(Collectors.toList()).forEach(token -> {
            LOGGER.info("MemeryTokenManger-checkExpireMembers:token[{}]已过期,被移除!对应成员[{}]", token, memoryTokens.get(token).getAccount());
            memoryTokens.remove(token);
        });
        MsgData msg = new MsgData();
        msg.content = JSON.toJSONString(TokenManager.memoryTokens.values().stream().map(LoginUserToken::getAccount).collect(Collectors.toList()));
        msg.type = 92;
        WebSocketEndPoint.connectClients.forEach((k, v) -> {
            v.getAsyncRemote().sendText(JSON.toJSONString(msg));
        });
    }

}
