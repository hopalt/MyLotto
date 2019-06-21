package com.saltlux.api.base.lotto.vo;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lotto_buy_history")
public class LottoBuyHistory {

    public enum ChoiceType{

      /** 수동 */
      MANUAL,
      /** 자동 최대 */
      MAX,
      /** 자동 최소 */
      MIN,
      /** 자동 */
      RANDOM
    };


    @Id
    @Column(name = "lotto_buy_seq", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int lottoBuySeq;

    /**
     * 횟차
     */
    @Column(name = "drw_no", nullable = false)
    private int drwNo;

    /** 번호 생성 방법 */
    @Enumerated(EnumType.STRING)
    @Column(name="choice_type", nullable = false, length = 20)
    private ChoiceType choiceType;

    /**
     * 번호 1
     */
    @Column(name = "drwt_no_1", nullable = false)
    private int drwtNo1;


    /**
     * 번호 2
     */
    @Column(name = "drwt_no_2", nullable = false)
    private int drwtNo2;


    /**
     * 번호 3
     */
    @Column(name = "drwt_no_3", nullable = false)
    private int drwtNo3;


    /**
     * 번호 4
     */
    @Column(name = "drwt_no_4", nullable = false)
    private int drwtNo4;


    /**
     * 번호 5
     */
    @Column(name = "drwt_no_5", nullable = false)
    private int drwtNo5;


    /**
     * 번호 6
     */
    @Column(name = "drwt_no_6", nullable = false)
    private int drwtNo6;


    public boolean checkBonus(int bonusNum){
      if(drwtNo1 == bonusNum){
        return true;
      }
      if(drwtNo2 == bonusNum){
        return true;
      }
      if(drwtNo3 == bonusNum){
        return true;
      }
      if(drwtNo4 == bonusNum){
        return true;
      }
      if(drwtNo5 == bonusNum){
        return true;
      }
      if(drwtNo6 == bonusNum){
        return true;
      }
      return false;
    }
}
