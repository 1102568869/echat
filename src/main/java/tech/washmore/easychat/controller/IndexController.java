package tech.washmore.easychat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tech.washmore.easychat.common.Constants;
import tech.washmore.easychat.common.uc.LoginUserToken;
import tech.washmore.easychat.common.uc.TokenManager;

/**
 * @author Washmore
 * @version V1.0
 * @summary TODO
 * @Copyright (c) 2018, Lianjia Group All Rights Reserved.
 * @since 2018/4/9
 */
@Controller
public class IndexController {
    @Autowired
    private TokenManager tokenManager;

    @GetMapping({"/", ""})
    public String welcome() {
        return "/index";
    }

    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestBody LoginUserToken loginUser) {
        return tokenManager.generateToken4WebPc(loginUser);
    }

    @GetMapping("/verifyToken")
    @ResponseBody
    public String verifyToken(@CookieValue(Constants.COOKIE_TOKEN_KEY) String token) {
        LoginUserToken loginUserToken = tokenManager.getLoginUserToken(token);
        if (loginUserToken == null) {
            return null;
        }
        return loginUserToken.getAccount();
    }
}
