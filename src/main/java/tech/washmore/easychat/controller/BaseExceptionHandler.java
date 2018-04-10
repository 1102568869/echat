package tech.washmore.easychat.controller;

import com.google.common.base.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.washmore.easychat.common.UnLoginException;
import tech.washmore.easychat.common.util.SpringContextUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class BaseExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(BaseExceptionHandler.class);

    @ExceptionHandler(value = UnLoginException.class)
    @ResponseBody
    public void handleBussnessException(UnLoginException e) throws IOException {
        logger.error(String.format("handleBussnessException:%s", e.getMessage()), e);
        HttpServletResponse response = SpringContextUtil.getHttpResponse();
        response.setCharacterEncoding(Charsets.UTF_8.toString());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8.toString());
        response.getWriter().print(e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public void handleException(Exception e) throws IOException {
        logger.error(String.format("handleException:%s", e.getMessage()), e);
        HttpServletResponse response = SpringContextUtil.getHttpResponse();
        response.setCharacterEncoding(Charsets.UTF_8.toString());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8.toString());
        response.getWriter().print(e.getMessage());
    }
}

