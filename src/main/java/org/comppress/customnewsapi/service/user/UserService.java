package org.comppress.customnewsapi.service.user;


import org.comppress.customnewsapi.dto.UserDto;
import org.comppress.customnewsapi.exceptions.DuplicateEntryException;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<UserDto> saveUser(UserDto userDto) throws DuplicateEntryException;
}
