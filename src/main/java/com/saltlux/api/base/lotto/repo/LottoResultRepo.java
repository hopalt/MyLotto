package com.saltlux.api.base.lotto.repo;

import com.saltlux.api.base.lotto.vo.LottoResult;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


/** 로또 결과 레파지토리 */
public interface LottoResultRepo extends JpaRepository<LottoResult, Integer> {

  @Query("select count(r) from LottoResult r where r.lotto.drwNo = :drwNo")
  public int countDrwNo(@Param("drwNo") int drwNo);
}
