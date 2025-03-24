package com.aro.Controller;

import com.aro.Entity.OTP;
import com.aro.Service.EmailService;
import com.aro.Service.JwtService;
import com.aro.Service.OTPService;
import com.aro.Service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class EmailController {

    // so i think @Value does not inject the application.properties values into the Controller annotated class
    private final String devOtp = String.valueOf(234123);
    // final keyword is used to restrict modifications to variables, methods, and classes

    private EmailService emailService;

    private JwtService jwtService;

    private OTPService otpService;

    private UserService userService;

    public EmailController(EmailService emailService, JwtService jwtService,
                           OTPService otpService, UserService userService) {
        this.emailService = emailService;
        this.jwtService = jwtService;
        this.otpService = otpService;
        this.userService = userService;
    }

    @PostMapping("/generateOtp")
    public ResponseEntity<String> getOtp(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            return ResponseEntity.badRequest().body("Invalid Authorization header");
        }

        String token = authHeader.substring(7);
        String email = jwtService.extractSubject(token);


        try {
            emailService.sendEmailOtp(email, token);
            return ResponseEntity.ok("Successfully sent the email");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/getOtpByUserId")
    public ResponseEntity<String> getOtpByUserId(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            return ResponseEntity.badRequest().body("Invalid Authorization header");
        }

        String token = authHeader.substring(7);

        Claims claims = jwtService.extractClaims(token);
        Long userId = Long.decode(claims.get("userId").toString());

        if (userId == null) {
            return ResponseEntity.badRequest().body("UserId not present");
        }

        return ResponseEntity.ok(otpService.getOtpByUserId(userId));
    }

    // so this endpoint validate the otp and generates the token and passes it to the user
    // so how will the workflow be :
    // the user will click on the moreAction and it will generate the otp and save it in the db
    // then after that the otp form validation will open up and it will validate the otp and send that otp at this endpoint
    // SIMPLE
    @PostMapping("/validateTheOtp")
    public ResponseEntity<String> validateTheOtp(@RequestHeader("Authorization") String authHeader, @RequestBody OTP userOtp) {
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            return ResponseEntity.badRequest().body("Invalid Authorization header");
        }

        String token = authHeader.substring(7);

        // now here first we should get the otp and then do the next step
        Claims claims = jwtService.extractClaims(token);
        Long userId = Long.decode(claims.get("userId").toString());

        if (userId == null) {
            return  ResponseEntity.badRequest().body("Unable to validate the otp");
        }

        String dbOtp = otpService.getOtpByUserId(userId);

        // now we validate the otp if they are similar we return the token or else we just throw the error
        if (!userOtp.getOtp().equals(dbOtp)) {
            return ResponseEntity.badRequest().body("Invalid Otp, Please regenerate the otp");
        }

        // now generate the token and send it to the client with the claim 2FA_VALID = "true"
        String otpToken = jwtService.generateTheToken(userService.getUserById(userId));
        if (otpToken == null) {
            return ResponseEntity.badRequest().body("Error in generating the token for the 2fa");
        }

        return ResponseEntity.ok(otpToken);
    }

    // i think this is not worth api
    @PostMapping("/validateThe2FaToken")
    public ResponseEntity<String> validateThe2FaToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            return ResponseEntity.badRequest().body("Invalid Authorization header");
        }

        String token = authHeader.substring(7);
        Claims claims = jwtService.extractClaims(token);

        return ResponseEntity.ok(claims.get("IS_2FA_VALID").toString());
    }

    @PostMapping("/testValidateOtp")
    public ResponseEntity<String> testValidateOtp(@RequestHeader("Authorization") String authHeader, @RequestBody OTP otp) {
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            return ResponseEntity.badRequest().body("Invalid Authorization header");
        }

        String token = authHeader.substring(7);
        Claims claims = jwtService.extractClaims(token);
        Long userId = Long.decode(claims.get("userId").toString());

        if (!otp.getOtp().equals(devOtp)) {
            return ResponseEntity.badRequest().body("Error Please enter the otp again");
        }

        // now generate the token and send it to the client with the claim 2FA_VALID = "true"
        String otpToken = jwtService.generateTheToken(userService.getUserById(userId));
        if (otpToken == null) {
            return ResponseEntity.badRequest().body("Error in generating the token for the 2fa");
        }

        return ResponseEntity.ok(otpToken);
    }

}

// now this will be use for checking the user if he is 2FA_Validated or not now the real question is do we need the main
// token also to validate the user we only need this token
// or we could just use the this token to act as the main token but just with few extra claims

// now let's make the test validateOtp controller