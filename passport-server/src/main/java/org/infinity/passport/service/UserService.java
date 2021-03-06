package org.infinity.passport.service;

import org.infinity.passport.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

public interface UserService {

    void changePassword(String userName, String newRawPassword);

    User insert(String userName, String rawPassword, String firstName, String lastName, String email, String mobileNo,
                String activationKey, Boolean activated, Boolean enabled, String remarks, String resetKey,
                Instant resetTime, Set<String> authorityNames);

    void update(String userName, String firstName, String lastName, String email, String mobileNo, String modifiedBy,
                Boolean activated, Boolean enabled, String remarks, Set<String> authorityNames);

    Optional<User> findOneByUserName(String userName);

    Optional<User> findOneByEmail(String email);

    Optional<User> findOneByMobileNo(String mobileNo);

    Optional<User> findOneByLogin(String login);

    Page<User> findByLogin(Pageable pageable, String login);

    Optional<User> activateRegistration(String activationKey);

    Optional<User> requestPasswordReset(String email, String resetKey);

    Optional<User> completePasswordReset(String newRawPassword, String resetKey);

    boolean checkValidPasswordLength(String password);

}