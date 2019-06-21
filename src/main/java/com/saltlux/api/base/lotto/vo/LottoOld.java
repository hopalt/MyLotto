package com.saltlux.api.base.lotto.vo;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "old_lotto")
public class LottoOld {

    /**
     * 횟차
     */
    @Id
    @Column(name = "drw_no", unique = true, nullable = false)
    private int drwNo;

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

    /**
     * 보너스 당첨 번호
     */
    @Column(name = "bnus_no", nullable = true)
    private int bnusNo;
}
