package org.comppress.customnewsapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
public class UpdatePasswordDto {

    @Pattern(regexp = "^([a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6})*$", message = "Invalid Email")
    @NotBlank(message = "Email can't be empty")
    private String email;

    @NotBlank(message = "Otp can't be empty")
    private String otp;

    @Size(min = 8, message = "New password must be at least 8 characters")
    @Pattern.List({
            @Pattern(regexp = "(?=.*[0-9]).+"),
            @Pattern(regexp = "(?=.*[a-z]).+"),
            @Pattern(regexp = "(?=.*[A-Z]).+"),
            //@Pattern(regexp = "(?=.[!@#\\$%\\^&\\*]).+"),
            @Pattern(regexp = "(?=\\S+$).+")
    })
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, value = "new_password")
    private String newPassword;

    @Size(min = 8, message = "Confirm password must be at least 8 characters")
    @Pattern.List({
            @Pattern(regexp = "(?=.*[0-9]).+"),
            @Pattern(regexp = "(?=.*[a-z]).+"),
            @Pattern(regexp = "(?=.*[A-Z]).+"),
            //@Pattern(regexp = "(?=.[!@#\\$%\\^&\\*]).+"),
            @Pattern(regexp = "(?=\\S+$).+")
    })
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, value = "confirm_new_password")
    private String confirmPassword;

}
