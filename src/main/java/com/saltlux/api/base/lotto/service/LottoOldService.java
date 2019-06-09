package com.saltlux.api.base.lotto.service;

import com.saltlux.api.base.common.util.HttpSendUtil;
import com.saltlux.api.base.lotto.repo.LottoOldRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** 이전 로또 서비스 */
@Service
public class LottoOldService {

    private final String API_URL = "https://www.nlotto.co.kr/common.do?method=getLottoNumber&drwNo=";

    @Autowired
    private LottoOldRepo lottoOldRepo;

    private boolean getLottoNumber(final int drwNo){

        String result = HttpSendUtil.doGet(API_URL+drwNo);

        return true;
    }

}
