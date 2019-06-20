package com.saltlux.api.base.lotto.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.saltlux.api.base.common.util.HttpSendUtil;
import com.saltlux.api.base.lotto.repo.LottoOldRepo;
import com.saltlux.api.base.lotto.vo.LottoOld;

import org.apache.http.client.ClientProtocolException;
import org.assertj.core.util.Arrays;
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


    public void analysisLotto(){

      int maxDrwNo = lottoOldRepo.findMaxDrwNo();
      maxDrwNo -= 100;

      List<LottoOld> lottoList = lottoOldRepo.findByDrwNoAfter(maxDrwNo);

      log.info("로또 리스트 : "+ lottoList.size());

      Map<Integer, Integer> lottoNumberBase = new HashMap<>();
      //통게치? 초기화
      for(int i = 1; i <= 45; i++){
        lottoNumberBase.put(i, 1);
      }

      lottoList.forEach(item ->{
        lottoNumberBase.put(item.getDrwtNo1(), lottoNumberBase.get(item.getDrwtNo1())+1);
        lottoNumberBase.put(item.getDrwtNo2(), lottoNumberBase.get(item.getDrwtNo2())+1);
        lottoNumberBase.put(item.getDrwtNo3(), lottoNumberBase.get(item.getDrwtNo3())+1);
        lottoNumberBase.put(item.getDrwtNo4(), lottoNumberBase.get(item.getDrwtNo4())+1);
        lottoNumberBase.put(item.getDrwtNo5(), lottoNumberBase.get(item.getDrwtNo5())+1);
        lottoNumberBase.put(item.getDrwtNo6(), lottoNumberBase.get(item.getDrwtNo6())+1);
      });

      // lottoNumberBase.keySet().forEach(key ->{
      //   log.info(key + " : "+lottoNumberBase.get(key));
      // });
      
      // 645개

      List<Integer> maxBaseNumber = new ArrayList<>();

      lottoNumberBase.keySet().forEach(key ->{
        int loopSize = lottoNumberBase.get(key);
        for(int i = 0; i < loopSize; i++){
          maxBaseNumber.add(key);
        }
      });


      for(int i=0 ; i <=5; i++){
        getMyNumber(maxBaseNumber);
      }
      

      // log.info(maxBaseNumber.toString());
      // log.info(maxBaseNumber.size() + "");


    }



    public void getCheckLotto() {

      int maxDrwNo = lottoOldRepo.findMaxDrwNo();

      try {
        while (getLottoNumber(++maxDrwNo)) {
          log.info("maxDrwNo : " + maxDrwNo);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
      

    }
    
    public boolean getLottoNumber(final int drwNo) throws ClientProtocolException, IOException {

        String result = HttpSendUtil.doGet(API_URL+drwNo, null);
        try{
          LottoOld lottoOld = gson.fromJson(result, LottoOld.class);

          if(lottoOld.getDrwNo() == 0){
            return false;
          }

          lottoOldRepo.save(lottoOld);
          
          log.info(lottoOld.toString());
        }catch(Exception e){
          return false;
        }
        return true;
    }

    private List<Integer> getMyNumber(List<Integer> baseNumber){

      List<Integer> myNumberList = new ArrayList<>();

      List<Integer> base = baseNumber.stream().collect(Collectors.toList());

      while(myNumberList.size() <= 6){
        long seed = System.nanoTime();
        Collections.shuffle(base, new Random(seed));
        
        int idx = (int)(Math.random() * base.size()) +1;

        int number = base.get(idx);

        myNumberList.add(number);

        base = base.stream().filter(item -> {return item != number;}).collect(Collectors.toList());
      }

      Collections.sort(myNumberList);
      

      log.info(myNumberList+"");

      int idx = 0;

      int checkCount = lottoOldRepo.countByDrwtNo1AndDrwtNo2AndDrwtNo3AndDrwtNo4AndDrwtNo5AndDrwtNo6(myNumberList.get(idx++), myNumberList.get(idx++), myNumberList.get(idx++), myNumberList.get(idx++), myNumberList.get(idx++), myNumberList.get(idx++));
      log.info("동일한 갯수 : "+ checkCount);

      
      return myNumberList;
    }

}
