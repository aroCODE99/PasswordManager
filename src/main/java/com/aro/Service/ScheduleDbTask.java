package com.aro.Service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduleDbTask {
    private JdbcTemplate jdbcTemplate;

    public ScheduleDbTask(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Scheduled(fixedRate = 60000)
    public void deleteExpireOtp() {
        String sql = "DELETE FROM otp WHERE expires_at < NOW()";
        int rowAffected = jdbcTemplate.update(sql);

        if (rowAffected > 0) {
            System.out.println("Deleted the expired OTP");
        }
    }
}
