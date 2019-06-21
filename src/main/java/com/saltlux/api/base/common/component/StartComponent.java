package com.saltlux.api.base.common.component;

import javax.annotation.PostConstruct;

import com.saltlux.api.base.lotto.service.LottoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 시작시 초기화 할 컴포넌트
 */
@Component
public class StartComponent{

  @Autowired
  private LottoService lottoService;

  @PostConstruct
  public void init(){
    
    lottoService.getCheckLotto();

  }

}
