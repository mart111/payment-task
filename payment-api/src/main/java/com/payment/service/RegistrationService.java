package com.payment.service;

import com.payment.exception.DuplicateUsernameException;
import com.payment.model.Status;
import com.payment.model.entity.Merchant;
import com.payment.model.entity.User;
import com.payment.model.request.UserRegistrationRequest;
import com.payment.model.response.UserRegistrationResponse;
import com.payment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(isolation = Isolation.REPEATABLE_READ,
            propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class)
    public UserRegistrationResponse register(UserRegistrationRequest userRegistrationRequest) {

        if (usernameAlreadyExists(userRegistrationRequest.username())) {
            throw new DuplicateUsernameException(String.format("Username '%s' already exists",
                    userRegistrationRequest.username()));
        }

        final var user = convertUserRegistrationRequest(userRegistrationRequest);
        user.setPassword(passwordEncoder.encode(userRegistrationRequest.password()));
        user.setStatus(Status.ACTIVE);
        final var entity = userRepository.save(user);
        return convertToUserRegistrationResponse(entity);
    }

    private boolean usernameAlreadyExists(String username) {
        return userRepository.existsByUsername(username);
    }

    private UserRegistrationResponse convertToUserRegistrationResponse(User user) {
        return new UserRegistrationResponse(
                user.getName(),
                user.getDescription(),
                user.getEmail(),
                user.getRole()
        );
    }

    private User convertUserRegistrationRequest(UserRegistrationRequest registrationRequest) {
        switch (registrationRequest.role()) {
            case ADMIN -> {
                return convertToUser(new User(), registrationRequest);
            }
            case MERCHANT -> {
                return convertToUser(new Merchant(), registrationRequest);
            }
        }
        return null;
    }

    private static User convertToUser(User user, UserRegistrationRequest registrationRequest) {
        user.setName(registrationRequest.name());
        user.setEmail(registrationRequest.username());
        user.setPassword(registrationRequest.password());
        user.setRole(registrationRequest.role());
        user.setDescription(registrationRequest.description());
        return user;
    }
}
