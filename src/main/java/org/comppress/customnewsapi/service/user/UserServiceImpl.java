package org.comppress.customnewsapi.service.user;

import org.comppress.customnewsapi.dto.UserDto;
import org.comppress.customnewsapi.entity.UserEntity;
import org.comppress.customnewsapi.exceptions.DuplicateEntryException;
import org.comppress.customnewsapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

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

    public ResponseEntity<UserDto> saveUser(UserDto userDto) throws DuplicateEntryException {

        UserEntity entity = userDto.toEntity(UserEntity.class);
        entity.setPassword(bcryptEncoder.encode(userDto.getPassword()));
        try {
            entity = userRepository.save(entity);
        }catch (DataIntegrityViolationException ex){
            throw new DuplicateEntryException("Duplicate record found",String.format("%s,%s",entity.getUsername(),entity.getEmail()));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(entity.toDto(UserDto.class));
    }
}
