package com.payment.service;

import com.payment.model.entity.Merchant;
import com.payment.model.entity.User;
import com.payment.model.request.MerchantEditRequest;
import com.payment.model.response.MerchantListResponse;
import com.payment.model.response.MerchantResponse;
import com.payment.repository.MerchantRepository;
import com.payment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MerchantService {

    private final MerchantRepository merchantRepository;
    private final UserRepository userRepository;

    public MerchantListResponse getAllMerchants() {
        return Optional.of(merchantRepository.findAll())
                .map(this::convertToMerchantResponse)
                .orElse(MerchantListResponse.EMPTY);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ,
            rollbackFor = Exception.class)
    MerchantResponse updateMerchant(Long merchantId, MerchantEditRequest merchantEditRequest) {
        return userRepository.findById(merchantId)
                .map(actualUser -> updateUser(actualUser, merchantEditRequest))
                .map(this::convertToMerchantResponse)
                .orElse(null);
    }

    public boolean usernameAlreadyExists(String username) {
        return userRepository.existsByUsername(username);
    }

    private MerchantListResponse convertToMerchantResponse(List<Merchant> merchants) {
        return new MerchantListResponse(merchants
                .stream()
                .map(merchant -> new MerchantResponse(
                        merchant.getId(),
                        merchant.getName(),
                        merchant.getEmail(),
                        merchant.getStatus(),
                        merchant.getTotalTransactionSum(),
                        merchant.getDescription()))
                .collect(Collectors.toList()));
    }

    private MerchantResponse convertToMerchantResponse(Merchant merchant) {
        return new MerchantResponse(
                merchant.getId(),
                merchant.getName(),
                merchant.getEmail(),
                merchant.getStatus(),
                merchant.getTotalTransactionSum(),
                merchant.getDescription()
        );
    }

    private Merchant updateUser(User actualUser, MerchantEditRequest merchantEditRequest) {
        actualUser.setName(merchantEditRequest.name());
        actualUser.setDescription(merchantEditRequest.description());
        actualUser.setEmail(merchantEditRequest.username());
        actualUser.setStatus(merchantEditRequest.status());
        actualUser.setPassword(merchantEditRequest.password());

        return (Merchant) actualUser;
    }
}
