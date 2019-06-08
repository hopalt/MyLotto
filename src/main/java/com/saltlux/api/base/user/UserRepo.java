package com.saltlux.api.base.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<UserVO, String> {
  Optional<UserVO> findByUserId(String userId);
}
