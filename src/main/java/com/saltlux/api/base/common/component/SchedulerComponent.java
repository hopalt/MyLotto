package com.saltlux.api.base.common.component;

import com.saltlux.api.base.lotto.service.LottoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 스케쥴러 컴포넌트
 */
@Component
public class SchedulerComponent{

  @Autowired
  private LottoService lottoService;

  @Scheduled(cron = "0 30 2 * * SUN")
  public void getNewLottoNumber(){
    lottoService.checkMyLottoNumber();
  }

  /**
   * 이때까지 안돌렸으면 자동으로 돌리기
   */
  @Scheduled(cron = "0 30 17 * * FRI")
  public void analysisLottoNumber(){
    lottoService.analysisLotto();
  }
}