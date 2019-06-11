package com.saltlux.api.base.lotto.service;

import java.io.IOException;

import com.google.gson.Gson;
import com.saltlux.api.base.common.util.HttpSendUtil;
import com.saltlux.api.base.lotto.repo.LottoOldRepo;
import com.saltlux.api.base.lotto.vo.LottoOld;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.java.Log;

/** 이전 로또 서비스 */
@Log
@Service
public class LottoOldService {

  private final String API_URL = "https://www.nlotto.co.kr/common.do?method=getLottoNumber&drwNo=";

  private Gson gson = new Gson();
  @Autowired
  private LottoOldRepo lottoOldRepo;

    
    public boolean getLottoNumber(final int drwNo) throws ClientProtocolException, IOException {

        String result = HttpSendUtil.doGet(API_URL+drwNo, null);

        LottoOld lottoOld = gson.fromJson(result, LottoOld.class);

        lottoOldRepo.save(lottoOld);
        
        log.info(lottoOld.toString());
        return true;
    }

}
