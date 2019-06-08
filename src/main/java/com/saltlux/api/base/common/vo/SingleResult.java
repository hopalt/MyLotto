package com.saltlux.api.base.common.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleResult<T> extends CommonResult{
  private T data;

}