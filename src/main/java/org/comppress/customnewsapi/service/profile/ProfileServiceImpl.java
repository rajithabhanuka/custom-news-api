package org.comppress.customnewsapi.service.profile;

import org.comppress.customnewsapi.dto.ForgetPasswordDto;
import org.comppress.customnewsapi.dto.UpdatePasswordDto;
import org.comppress.customnewsapi.entity.UserEntity;
import org.comppress.customnewsapi.exceptions.EmailAlreadyExistsException;
import org.comppress.customnewsapi.exceptions.EmailSenderException;
import org.comppress.customnewsapi.exceptions.PasswordNotMatchException;
import org.comppress.customnewsapi.exceptions.RecordNotFoundException;
import org.comppress.customnewsapi.repository.UserRepository;
import org.comppress.customnewsapi.service.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Properties;
import java.util.Random;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder bcryptEncoder;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    public ProfileServiceImpl(UserRepository userRepository, EmailService emailService, PasswordEncoder bcryptEncoder) {
        this.bcryptEncoder = bcryptEncoder;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    @Override
    public void sendOtp(ForgetPasswordDto forgetPasswordDto) throws EmailSenderException, EmailAlreadyExistsException {
        UserEntity user = userRepository.findByEmail(forgetPasswordDto.getEmail()).
                orElseThrow(() -> new RecordNotFoundException("User not found",forgetPasswordDto.getEmail()));

        String otp = String.format("%06d", new Random().nextInt(999999));

        user.setOtp(otp);
        user.setOtpUsed(false);
        userRepository.save(user);

        Properties properties = new Properties();

        properties.setProperty("to", forgetPasswordDto.getEmail());
        properties.setProperty("from", fromEmail);
        properties.setProperty("subject", "Forget password OTP");
        properties.setProperty("template_name", "email_template");
        properties.setProperty("otp", otp);
        properties.setProperty("date", new Date() + "");

        emailService.sendAutomatedEmailWithTemplate(properties);

    }

    @Override
    public ResponseEntity<UpdatePasswordDto> updatePassword(UpdatePasswordDto updatePasswordDto) throws EmailSenderException, EmailAlreadyExistsException {
        UserEntity user = userRepository.findByOtpAndEmailAndIsOtpUsedFalse(updatePasswordDto.getOtp(),updatePasswordDto.getEmail()).
                orElseThrow(() -> new RecordNotFoundException("User not found for given otp and email",updatePasswordDto.getEmail()));

        if (!updatePasswordDto.getNewPassword().equals(updatePasswordDto.getConfirmPassword())) {
            throw new PasswordNotMatchException("New Passwords are Not Matching", "Please check");
        }

        user.setPassword(encryptPassword(updatePasswordDto.getNewPassword()));
        user.setOtpUsed(true);
        userRepository.save(user);

        Properties properties = new Properties();

        properties.setProperty("to", updatePasswordDto.getEmail());
        properties.setProperty("from", fromEmail);
        properties.setProperty("subject", "Password has been updated successfully");
        properties.setProperty("template_name", "update_password_template");

        emailService.sendAutomatedEmailWithTemplate(properties);

        return ResponseEntity.ok(updatePasswordDto);
    }

    private String encryptPassword(String password) {

        return bcryptEncoder.encode(password);
    }
}
