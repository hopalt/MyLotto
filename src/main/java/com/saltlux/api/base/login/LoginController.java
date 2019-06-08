package com.saltlux.api.base.login;

import java.util.Collections;

import com.saltlux.api.base.common.service.ResponseService;
import com.saltlux.api.base.common.vo.CommonResult;
import com.saltlux.api.base.common.vo.SingleResult;
import com.saltlux.api.base.config.exception.LoginFailedException;
import com.saltlux.api.base.config.security.JwtTokenProvider;
import com.saltlux.api.base.user.UserRepo;
import com.saltlux.api.base.user.UserVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.java.Log;

@RestController
@RequestMapping(value="/login")
@Log
public class LoginController {

  @Autowired
  private UserRepo userRepo;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  private ResponseService responseService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @GetMapping(value = "/login")
  private SingleResult<String> login(@RequestParam String id, @RequestParam String password){
    log.info("login");
    UserVO user = userRepo.findById(id).orElseThrow(LoginFailedException::new);
    
    if(!passwordEncoder.matches(password, user.getPassword())){
      throw new LoginFailedException();
    }

    return responseService.getSingleResult(jwtTokenProvider.createToken(user.getUsername(), user.getRoles()));
  }


  @GetMapping(value = "/signup")
  public CommonResult signup(@RequestParam String id, @RequestParam String password, @RequestParam String name){
    log.info("signup");
    UserVO user = UserVO.builder()
      .userId(id)
      .password(passwordEncoder.encode(password))
      .name(name)
      .roles(Collections.singletonList("ROLE_USER"))
      .build();
    userRepo.save(user);

    return responseService.getSuccessResult();
  }
}
