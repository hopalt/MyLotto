package com.saltlux.api.base.common.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResult{
  
  /** 응답 성공 여부 : true/false */
  private boolean success;

  /** 응답 코드 : code > 0 정상, code < 0 비정상*/
  private int code;

  /** 응답 메시지*/
  private String msg;

}