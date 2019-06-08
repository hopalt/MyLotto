package com.saltlux.api.base.user;

import com.saltlux.api.base.common.service.ResponseService;
import com.saltlux.api.base.common.service.SessionService;
import com.saltlux.api.base.common.vo.ListResult;
import com.saltlux.api.base.common.vo.SingleResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.java.Log;

@RestController
@RequestMapping("/user")
@Log
public class UserController {

  @Autowired
  private SessionService sessionService;

  @Autowired
  private ResponseService responseService;

  @Autowired
  private UserRepo userRepo;


  @GetMapping("/users")
  public ListResult<UserVO> findAllUsers(){
    log.info("/users");
    return responseService.getListResult(userRepo.findAll());
  }

  @GetMapping("/myInfo")
  public SingleResult<UserVO> findByUser(){

    String id = sessionService.getUserId();
    log.info(id);

    UserVO user = userRepo.findById(id).orElse(null);

    return responseService.getSingleResult(user);
  }


}
