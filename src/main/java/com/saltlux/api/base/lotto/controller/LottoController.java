package com.saltlux.api.base.lotto.controller;


import com.saltlux.api.base.common.service.ResponseService;
import com.saltlux.api.base.common.vo.ListResult;
import com.saltlux.api.base.common.vo.SingleResult;
import com.saltlux.api.base.lotto.repo.LottoOldRepo;
import com.saltlux.api.base.lotto.service.LottoService;
import com.saltlux.api.base.lotto.vo.LottoBuyHistory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.java.Log;

@RestController
@RequestMapping("/lotto")
@Log
public class LottoController {

  @Autowired
  private LottoService lottoService;

  @Autowired
  private LottoOldRepo lottoOldRepo;

  @Autowired
  private ResponseService responseService;

  // @GetMapping("/getLottoList")
  // public CommonResult getLottoList() throws ClientProtocolException, IOException {

  //   for(int i = 800; i <=862; i++){
  //     //for(int i = 1; i <=862; i++){
  //       lottoService.getLottoNumber(i);
  //   }

  //   return responseService.getSuccessResult();
  // }

  @GetMapping("/getOldLotto")
  public SingleResult<Integer> getOldLotto(){

    int maxNo = lottoOldRepo.findMaxDrwNo();

    return responseService.getSingleResult(maxNo);
  }

  @GetMapping("/analysisLotto")
  public SingleResult<Boolean> analysisLotto(){

    lottoService.analysisLotto();

    return responseService.getSingleResult(true);
  }

  /**
   * 금주의 로또 번호 리스트
   */
  @GetMapping("/getMyLottoNumber")
  public ListResult<LottoBuyHistory> getMyLottoNumber(){

    return responseService.getListResult(lottoService.getMyLottoNumber());
  }

  @GetMapping("/checkMyLottoNumber/{seq}")
  public SingleResult<Boolean> checkMyLotto(@PathVariable("seq") int seq){

    lottoService.checkMyLottoNumber(seq);
    return responseService.getSingleResult(true);
  }
}