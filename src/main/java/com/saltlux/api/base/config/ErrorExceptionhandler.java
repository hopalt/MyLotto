package com.saltlux.api.base.config;

import javax.servlet.http.HttpServletRequest;

import com.saltlux.api.base.common.service.ResponseService;
import com.saltlux.api.base.common.vo.CommonResult;
import com.saltlux.api.base.config.exception.LoginFailedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/**
 * 에러 처리 헨들러
 * https://github.com/brunocleite/spring-boot-exception-handling/blob/master/src/main/java/com/example/springbootexceptionhandling/RestExceptionHandler.java
 * 참고
 *
 */
@ControllerAdvice
public class ErrorExceptionhandler extends ResponseEntityExceptionHandler {

  @Autowired
  private ResponseService responseService;

  @Autowired
  private MessageSource messageSource;

  //로그인 에러 처리
  @ExceptionHandler(LoginFailedException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  protected CommonResult loginFailed( HttpServletRequest req, LoginFailedException e) {
    return responseService.getFailResult(Integer.valueOf(getMessage("logininFailed.code")),getMessage("logininFailed.msg"));
  }

  private String getMessage(String code){
    return getMessage(code, null);
  }

  private String getMessage(String code, Object[] args){
    return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
  }
}
