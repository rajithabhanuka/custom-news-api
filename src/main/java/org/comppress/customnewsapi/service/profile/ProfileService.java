package org.comppress.customnewsapi.service.profile;

import org.comppress.customnewsapi.dto.ForgetPasswordDto;
import org.comppress.customnewsapi.dto.PreferenceDto;
import org.comppress.customnewsapi.dto.UpdatePasswordDto;
import org.comppress.customnewsapi.dto.UserDto;
import org.comppress.customnewsapi.dto.response.ForgetPasswordResponseDto;
import org.comppress.customnewsapi.dto.response.ResponseDto;
import org.comppress.customnewsapi.exceptions.EmailAlreadyExistsException;
import org.comppress.customnewsapi.exceptions.EmailSenderException;
import org.springframework.http.ResponseEntity;

public interface ProfileService {

    ResponseEntity<ResponseDto> sendOtp(ForgetPasswordDto forgetPasswordDto) throws EmailSenderException, EmailAlreadyExistsException;

    ResponseEntity<UpdatePasswordDto> updatePassword(UpdatePasswordDto updatePasswordDto) throws EmailSenderException, EmailAlreadyExistsException;

    ResponseEntity<UserDto> updateCategoryAndPublisherPreference(PreferenceDto preferenceDto);
}
