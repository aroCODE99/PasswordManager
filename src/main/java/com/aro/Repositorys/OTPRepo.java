package com.aro.Repositorys;

import com.aro.Entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OTPRepo extends JpaRepository<OTP, Long> {
    OTP findTopByUserUserIdOrderByCreatedAtDesc(Long userId);
}

// Reason: Failed to create query for method public abstract com.aro.Entity.OTP com.aro.Repositorys.OTPRepo.findByOtpUserId(java.lang.Long);
// No property 'userId' found for type 'String'; Traversed path: OTP.otp
