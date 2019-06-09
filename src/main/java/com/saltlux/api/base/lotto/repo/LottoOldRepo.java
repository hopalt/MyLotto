package com.saltlux.api.base.lotto.repo;

import com.saltlux.api.base.lotto.vo.LottoOld;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/** 로또 이전 당첨 번호 레파지토리 */
public interface LottoOldRepo extends JpaRepository<LottoOld, Integer> {

    /** 저장된 마지막 횟차 조회 */
    @Query("select max(lotto.drwNo) from LottoOld lotto")
    Integer findMaxDrwNo();
}
