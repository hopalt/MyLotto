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
import com.saltlux.api.base.lotto.repo.LottoBuyHistoryRepo;
import com.saltlux.api.base.lotto.repo.LottoOldRepo;
import com.saltlux.api.base.lotto.repo.LottoResultRepo;
import com.saltlux.api.base.lotto.vo.LottoBuyHistory;
import com.saltlux.api.base.lotto.vo.LottoOld;
import com.saltlux.api.base.lotto.vo.LottoResult;
import com.saltlux.api.base.lotto.vo.LottoBuyHistory.ChoiceType;
import com.saltlux.api.base.lotto.vo.LottoResult.ResultType;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.java.Log;

/** 이전 로또 서비스 */
@Log
@Service
public class LottoService {

  private final String API_URL = "https://www.nlotto.co.kr/common.do?method=getLottoNumber&drwNo=";

  private Gson gson = new Gson();

  @Autowired
  private LottoOldRepo lottoOldRepo;

  @Autowired
  private LottoBuyHistoryRepo lottoBuyRepo;

  @Autowired
  private LottoResultRepo lottoResultRepo;


    /**
     * 로또 번호 생성 메소드
     */
    public void analysisLotto(){

      int maxDrwNo = lottoOldRepo.findMaxDrwNo();


      int buyCount = (int) lottoBuyRepo.count();

      if(buyCount > 0){
        int buyDrwNo = lottoBuyRepo.findMaxDrwNo();

        //한주에 한번 했으면 끝
        if(maxDrwNo < buyDrwNo){
          return;
        }
      }


      maxDrwNo -= 100;

      List<LottoOld> lottoList = lottoOldRepo.findByDrwNoAfter(maxDrwNo);

      log.info("로또 리스트 : "+ lottoList.size());

      Map<Integer, Integer> lottoNumberBase = new HashMap<>();
      //통계치? 초기화
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

      lottoNumberBase.keySet().forEach(key ->{
        log.info(key + " : "+lottoNumberBase.get(key));
      });
      
      // 645개

      //최대치에서 뽑기
      List<Integer> maxBaseNumber = new ArrayList<>();

      lottoNumberBase.keySet().forEach(key ->{
        int loopSize = lottoNumberBase.get(key);
        for(int i = 0; i < loopSize; i++){
          maxBaseNumber.add(key);
        }
      });
      
      
      List<Integer> analysisList = getMyNumber(maxBaseNumber);

      setBuyLottoNumber(analysisList, ChoiceType.MAX);

      
      //최소치에서 뽑기
      int baseMax = lottoNumberBase.values().stream().max(Integer::compare).orElse(1);
      
      List<Integer> minBaseNumber = new ArrayList<>();
      
      lottoNumberBase.keySet().forEach(key ->{

        int loopSize = baseMax - lottoNumberBase.get(key) +1;
        for(int i = 0; i < loopSize; i++){
          minBaseNumber.add(key);
        }

      });

      analysisList = getMyNumber(minBaseNumber);

      setBuyLottoNumber(analysisList, ChoiceType.MIN);

      //순수 랜덤에서 뽑기?
      List<Integer> randomBaseNumber = new ArrayList<>();

      for(int i = 1; i<=45; i++){
        int random = (int)Math.random() * baseMax;
        for(int j = 0; j <= random; j++){
          randomBaseNumber.add(i);
        }
      }

      analysisList = getMyNumber(randomBaseNumber);

      setBuyLottoNumber(analysisList, ChoiceType.RANDOM);

      //고정 구매 번호
      analysisList = new ArrayList<>();
      analysisList.add(5);
      analysisList.add(12);
      analysisList.add(18);
      analysisList.add(20);
      analysisList.add(24);
      analysisList.add(45);
      setBuyLottoNumber(analysisList, ChoiceType.MANUAL);

      analysisList = new ArrayList<>();
      analysisList.add(5);
      analysisList.add(11);
      analysisList.add(17);
      analysisList.add(25);
      analysisList.add(30);
      analysisList.add(40);
      setBuyLottoNumber(analysisList, ChoiceType.MANUAL);
    }



    /**
     * 신규 당첨 번호가 있는지 조회 메소드
     */
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
    
    /**
     * 로또 당첨 번호 조회 메소드
     * 
     * @param drwNo 데이터를 가져올 횟차 번호
     * @return 성공여부
     * @throws ClientProtocolException
     * @throws IOException
     */
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

    /**
     * 번호 생성 메소드
     */
    private List<Integer> getMyNumber(List<Integer> baseNumber){

      List<Integer> myNumberList = new ArrayList<>();

      List<Integer> base = baseNumber.stream().collect(Collectors.toList());

      int checkCount = -1;

      while(checkCount != 0){
        while(myNumberList.size() < 6){
          long seed = System.nanoTime();
          Collections.shuffle(base, new Random(seed));
          
          int idx = (int)(Math.random() * base.size());

          int number = base.get(idx);

          myNumberList.add(number);

          base = base.stream().filter(item -> {return item != number;}).collect(Collectors.toList());
        }

        Collections.sort(myNumberList);
        

        log.info(myNumberList+"");

        int idx = 0;
        checkCount = lottoOldRepo.countByDrwtNo1AndDrwtNo2AndDrwtNo3AndDrwtNo4AndDrwtNo5AndDrwtNo6(myNumberList.get(idx++), myNumberList.get(idx++), myNumberList.get(idx++), myNumberList.get(idx++), myNumberList.get(idx++), myNumberList.get(idx++));
        
      }
      
      return myNumberList;
    }


    /**
     *
     * @param lottoNumber 저장할 번호 리스트
     * @param type 생성 타입
     */
    private void setBuyLottoNumber(List<Integer> lottoNumber, ChoiceType type){

      int idx = 0;

      LottoBuyHistory buyLotto = LottoBuyHistory.builder()
      .drwNo(lottoOldRepo.findMaxDrwNo()+1)
      .choiceType(type)
      .drwtNo1(lottoNumber.get(idx++))
      .drwtNo2(lottoNumber.get(idx++))
      .drwtNo3(lottoNumber.get(idx++))
      .drwtNo4(lottoNumber.get(idx++))
      .drwtNo5(lottoNumber.get(idx++))
      .drwtNo6(lottoNumber.get(idx++))
      .build();

      lottoBuyRepo.save(buyLotto);

    }


    /**
     * 
     * @return 금주의 로또 번호 리스트
     */
    public List<LottoBuyHistory> getMyLottoNumber(){
      int maxDrwNo = lottoOldRepo.findMaxDrwNo();
      
      int buyCount = (int) lottoBuyRepo.count();

      if(buyCount > 0){
        analysisLotto();
      }

      int buyDrwNo = lottoBuyRepo.findMaxDrwNo();

      //분석 된게 없으면 분석
      if(maxDrwNo >= buyDrwNo){
        analysisLotto();
      }

      buyDrwNo = lottoBuyRepo.findMaxDrwNo();

      return lottoBuyRepo.findByDrwNo(buyDrwNo);

    }

    /**
     * 스케쥴러에서 일요일에 정답 가져오기 및 정답 확인할 메소드
     */
    public void checkMyLottoNumber(){
      //신규 번호 가져오기
      getCheckLotto();
      //최대값 구하기
      int maxDrwNo = lottoOldRepo.findMaxDrwNo();
      //확인
      checkMyLottoNumber(maxDrwNo);
    }

    /**
     * 로또 결과 확인 메소드
     */
    public void checkMyLottoNumber(int drwNo){


      if(lottoResultRepo.countDrwNo(drwNo) > 0){
        return;
      }

      LottoOld winLotto = lottoOldRepo.findById(drwNo).orElseGet(null);

      List<LottoBuyHistory> myLottoList = lottoBuyRepo.findByDrwNo(drwNo);

      if(winLotto == null || myLottoList == null || myLottoList.size() == 0){
        return;
      }

      //비교를 위한 변환
      List<Integer> winList = new ArrayList<>();
      winList.add(winLotto.getDrwtNo1());
      winList.add(winLotto.getDrwtNo2());
      winList.add(winLotto.getDrwtNo3());
      winList.add(winLotto.getDrwtNo4());
      winList.add(winLotto.getDrwtNo5());
      winList.add(winLotto.getDrwtNo6());

      myLottoList.forEach(item ->{
        LottoResult result = new LottoResult();
        result.setLotto(item);

        int score = 0;
        if(winList.contains(item.getDrwtNo1())){
          score++;
        }
        if(winList.contains(item.getDrwtNo2())){
          score++;
        }
        if(winList.contains(item.getDrwtNo3())){
          score++;
        }
        if(winList.contains(item.getDrwtNo4())){
          score++;
        }
        if(winList.contains(item.getDrwtNo5())){
          score++;
        }
        if(winList.contains(item.getDrwtNo6())){
          score++;
        }

        //등수 조정
        switch(score){
          case 6:
            result.setResult(ResultType.ONE);
          break;
          case 5:
            result.setResult(ResultType.THREE);
          break;
          case 4:
            result.setResult(ResultType.THREE);
          break;
          case 3:
            result.setResult(ResultType.FIVE);
          break;
          
          default:
            result.setResult(ResultType.ZERO);
          break;
        }

        if(score == 5){
          //보너스 3등일때만 보너스 번호 확인
          if(item.checkBonus(winLotto.getBnusNo())){
            result.setResult(ResultType.TWO);
          }
          
        }

        lottoResultRepo.save(result);

      });



    }
}
