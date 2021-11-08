package org.comppress.customnewsapi.repository;

import org.comppress.customnewsapi.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity findByUsername(String username);
    Boolean existsByEmail(String email);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByOtpAndEmailAndIsOtpUsedFalse(String otp, String email);

}
