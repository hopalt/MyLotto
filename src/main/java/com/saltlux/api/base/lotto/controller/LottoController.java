package com.saltlux.api.base.lotto.controller;


import java.io.IOException;

import com.saltlux.api.base.common.service.ResponseService;
import com.saltlux.api.base.common.vo.CommonResult;
import com.saltlux.api.base.common.vo.SingleResult;
import com.saltlux.api.base.lotto.repo.LottoOldRepo;
import com.saltlux.api.base.lotto.service.LottoOldService;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.java.Log;

@RestController
@RequestMapping("/lotto")
@Log
public class LottoController {

  @Autowired
  private LottoOldService lottoOldService;

  @Autowired
  private LottoOldRepo lottoOldRepo;

  @Autowired
  private ResponseService responseService;

  @GetMapping("/getLottoList")
  public CommonResult getLottoList() throws ClientProtocolException, IOException {

    for(int i = 800; i <=862; i++){
      //for(int i = 1; i <=862; i++){
      lottoOldService.getLottoNumber(i);
    }

    return responseService.getSuccessResult();
  }

  @GetMapping("/getOldLotto")
  public SingleResult<Integer> getOldLotto(){

    int maxNo = lottoOldRepo.findMaxDrwNo();

    return responseService.getSingleResult(maxNo);
  }


}