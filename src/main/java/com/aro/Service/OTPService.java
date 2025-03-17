package com.aro.Service;

import com.aro.Entity.OTP;
import com.aro.Repositorys.OTPRepo;
import org.springframework.stereotype.Service;

@Service
public class OTPService {

    private final OTPRepo otpRepo;

    public OTPService(OTPRepo otpRepo) {
        this.otpRepo = otpRepo;
    }

    // this method is going to be use for saving the otp inside the db
    public String saveOtp(OTP otp) {
        if (otp == null) {
            return "Failed to save the otp";
        }

        otpRepo.save(otp);
        return "saved the otp successfully";
    }

    public String getOtpByUserId(Long userId) {
        OTP otp = otpRepo.findTopByUserUserIdOrderByCreatedAtDesc(userId);

        if (otp == null) {
            return null;
        }

        return otp.getOtp();
    }
}



