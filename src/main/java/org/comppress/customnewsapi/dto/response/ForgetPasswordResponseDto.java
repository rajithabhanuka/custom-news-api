package org.comppress.customnewsapi.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ForgetPasswordResponseDto implements ResponseDto {
    private String message;
}