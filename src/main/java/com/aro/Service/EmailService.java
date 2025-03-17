package com.aro.Service;

import com.aro.Entity.OTP;
import com.aro.Entity.Users;
import com.aro.Repositorys.OTPRepo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.Transient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailService {
    private Random random = new Random();

    @Value("${passhub.email}")
    private String passhubEmail;

    private final JavaMailSender mailSender;

    private JwtService jwtService;

    private UserService userService;

    private OTPRepo otpRepo;


    public EmailService(JavaMailSender mailSender, JwtService jwtService,
                        OTPRepo otpRepo, UserService userService) {
        this.mailSender = mailSender;
        this.jwtService = jwtService;
        this.otpRepo = otpRepo;
        this.userService = userService;
    }

    // now we could use this to generate the Opt and saved it in the data base
    public String generateOtp() {
        return String.valueOf(100000 + random.nextInt(900000));
    }

    @Transient
    private void saveOtp(String token, String email, String otp) {
        Users user = userService.getUserByEmail(email);
        otpRepo.save(new OTP(user, otp));
    }

    public void sendEmailOtp(String to, String token) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        String otp = generateOtp();

        saveOtp(token, to, otp);

        String htmlContent = getOtpEmailHtml(otp);

        helper.setFrom(passhubEmail);
        helper.setTo(to);
        helper.setSubject("Your otp code");
        helper.setText(htmlContent, true); // Enable HTML content

        mailSender.send(mimeMessage);
    }

    private String getOtpEmailHtml(String otp) {
        return String.format("""
        <!DOCTYPE html>
        <html>
        <head>
            <title>OTP Verification</title>
            <style>
                .container {
                    font-family: Arial, sans-serif;
                    max-width: 500px;
                    margin: auto;
                    padding: 20px;
                    border: 1px solid #ddd;
                    border-radius: 10px;
                    text-align: center;
                    background-color: #f9f9f9;
                }
                .otp {
                    font-size: 24px;
                    font-weight: bold;
                    color: #ff5722;
                    margin: 20px 0;
                }
                .footer {
                    font-size: 12px;
                    color: #777;
                    margin-top: 20px;
                }
            </style>
        </head>
        <body>
            <div class="container">
                <h2>OTP Verification</h2>
                <p>Your One-Time Password (OTP) for verification is:</p>
                <div class="otp">%s</div>
                <p>This OTP is valid for only 5 minutes.</p>
                <p>If you didn't request this, please ignore this email.</p>
                <div class="footer">
                    &copy; 2024 PassHub.com. All rights reserved.
                </div>
            </div>
        </body>
        </html>
    """, otp);
    }

}
