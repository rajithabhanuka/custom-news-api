package org.comppress.customnewsapi.service.user;


import org.comppress.customnewsapi.dto.UserDto;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<UserDto> saveUser(UserDto userDto);
}
