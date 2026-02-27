package com.example.futurebase.backend.repository;

import com.example.futurebase.backend.entity.OtpToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpToken, Long> {

  // 最新の未使用OTPを取得
  @Query("""
      SELECT o FROM OtpToken o
      WHERE o.user.id = :userId
        AND o.consumedAt IS NULL
      ORDER BY o.createdAt DESC
      LIMIT 1
      """)
  Optional<OtpToken> findLatestActiveByUserId(@Param("userId") Long userId);

  // 古いOTPを削除（新しいOTP発行前にクリーンアップ）
  @Modifying
  @Query("DELETE FROM OtpToken o WHERE o.user.id = :userId AND o.consumedAt IS NULL")
  void deleteActiveByUserId(@Param("userId") Long userId);
}
