package org.comppress.customnewsapi.controller;

import org.comppress.customnewsapi.dto.UserPreferenceDto;
import org.springframework.http.ResponseEntity;

public interface HomeService {
    ResponseEntity<UserPreferenceDto> getUserPreference();
}
