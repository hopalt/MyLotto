package com.saltlux.api.base.lotto.repo;

import java.util.List;

import com.saltlux.api.base.lotto.vo.LottoBuyHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


/** 로또 이전 당첨 번호 레파지토리 */
public interface LottoBuyHistoryRepo extends JpaRepository<LottoBuyHistory, Integer> {

    /** 저장된 마지막 횟차 조회 */
    @Query("select max(lotto.drwNo) from LottoBuyHistory lotto")
    Integer findMaxDrwNo();

    /**
     * 
     * @param seq 회차 번호
     * @return 회차 번호에 생성된 리스트
     */
    List<LottoBuyHistory> findByDrwNo(int seq);

    
    // int countByDrwtNo1AndDrwtNo2AndDrwtNo3AndDrwtNo4AndDrwtNo5AndDrwtNo6(int no1, int no2, int no3, int no4, int no5, int no6);
}
