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

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(isolation = Isolation.REPEATABLE_READ,
            propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class)
    public UserRegistrationResponse register(UserRegistrationRequest userRegistrationRequest) {

        if (usernameAlreadyExists(userRegistrationRequest.getUsername())) {
            throw new DuplicateUsernameException(String.format("Username '%s' already exists",
                    userRegistrationRequest.getUsername()));
        }

        final var user = convertUserRegistrationRequest(userRegistrationRequest);
        user.setPassword(passwordEncoder.encode(userRegistrationRequest.getPassword()));
        user.setStatus(Status.ACTIVE);
        final var entity = userRepository.save(user);
        return convertToUserRegistrationResponse(entity);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ,
            rollbackFor = Exception.class)
    public List<UserRegistrationResponse> registerAll(List<UserRegistrationRequest> registrationRequests) {

        // prefer to use register method, as email uniqueness should be checked.
        return registrationRequests
                .stream()
                .map(this::register)
                .toList();
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
        switch (registrationRequest.getRole()) {
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
        user.setName(registrationRequest.getName());
        user.setEmail(registrationRequest.getUsername());
        user.setPassword(registrationRequest.getPassword());
        user.setRole(registrationRequest.getRole());
        user.setDescription(registrationRequest.getDescription());
        return user;
    }
}
