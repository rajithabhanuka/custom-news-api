package org.comppress.customnewsapi.service.user;

import org.comppress.customnewsapi.dto.UserDto;
import org.comppress.customnewsapi.entity.UserEntity;
import org.comppress.customnewsapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder bcryptEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder bcryptEncoder) {
        this.userRepository = userRepository;
        this.bcryptEncoder = bcryptEncoder;
    }

    public ResponseEntity<UserDto> saveUser(UserDto userDto){

        UserEntity entity = userDto.toEntity(UserEntity.class);
        entity.setPassword(bcryptEncoder.encode(userDto.getPassword()));

        UserEntity saved = userRepository.save(entity);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved.toDto(UserDto.class));

    }
}
