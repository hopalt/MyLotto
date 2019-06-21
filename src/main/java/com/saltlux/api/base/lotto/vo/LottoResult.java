package com.saltlux.api.base.lotto.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lotto_result")
public class LottoResult{

  public enum ResultType{
    /** 꽝 */
    ZERO,
    /** 5등 */
    FIVE,
    /** 4등 */
    FOUR,
    /** 3등 */
    THREE,
    /** 2등 */
    TWO,
    /** 1등 */
    ONE
  }

  @Id
  @Column(name = "lotto_result_seq", nullable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int lottoResultSeq;

  @OneToOne
  @JoinColumn(name="lotto_buy_seq", nullable = false)  
  private LottoBuyHistory lotto;

  @Enumerated(EnumType.STRING)
  @Column(name="result", nullable = false, length = 20)
  private ResultType result;

}