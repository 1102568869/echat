package tech.washmore.easychat.common.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import tech.washmore.easychat.common.Constants;
import tech.washmore.easychat.common.uc.LoginUserToken;
import tech.washmore.easychat.common.uc.TokenManager;
import tech.washmore.easychat.common.util.CookieUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Washmore
 * @version V1.0
 * @summary TODO
 * @Copyright (c) 2018, Lianjia Group All Rights Reserved.
 * @since 2018/4/10
 */
public class UserLoginInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginInterceptor.class);
    @Autowired
    private TokenManager tokenManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String remoteIP = request.getHeader(Constants.X_REAL_IP);
        if (StringUtils.isBlank(remoteIP)) {
            remoteIP = request.getRemoteAddr();
        }
        Cookie tokenCookie = CookieUtil.getCurrentCookieByName(Constants.COOKIE_TOKEN_KEY);

        if (tokenCookie == null) {
            return false;
        }
        String token = tokenCookie.getValue();
        if (StringUtils.isEmpty(token)) {
            return false;
        }

        LoginUserToken loginUserToken = tokenManager.getLoginUserToken(token);
        if (loginUserToken == null) {
            return false;
        }

        LOGGER.info("--login check success:[{}],ip:[{}],account:[{}]", request.getRequestURI(), remoteIP, loginUserToken.getAccount());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
