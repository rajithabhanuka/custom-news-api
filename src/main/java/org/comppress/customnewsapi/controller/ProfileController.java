package org.comppress.customnewsapi.controller;

import org.comppress.customnewsapi.dto.ForgetPasswordDto;
import org.comppress.customnewsapi.dto.UpdatePasswordDto;
import org.comppress.customnewsapi.exceptions.EmailAlreadyExistsException;
import org.comppress.customnewsapi.exceptions.EmailSenderException;
import org.comppress.customnewsapi.service.profile.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping(value = "/forgot-password")
    public void sendOtp(@RequestBody @Valid ForgetPasswordDto forgetPasswordDto) throws EmailSenderException, EmailAlreadyExistsException {
        profileService.sendOtp(forgetPasswordDto);
    }

    @PostMapping(value = "/update-password")
    public ResponseEntity<UpdatePasswordDto> updatePassword(@RequestBody @Valid UpdatePasswordDto updatePasswordDto) throws EmailSenderException, EmailAlreadyExistsException {
        return profileService.updatePassword(updatePasswordDto);
    }
}
