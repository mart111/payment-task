package com.payment.service;

import com.payment.exception.MerchantNotEligibleForRemovalException;
import com.payment.model.entity.Merchant;
import com.payment.model.entity.User;
import com.payment.model.request.MerchantEditRequest;
import com.payment.model.response.MerchantListResponse;
import com.payment.model.response.MerchantResponse;
import com.payment.repository.MerchantRepository;
import com.payment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Service
public class MerchantService {

    private final MerchantRepository merchantRepository;
    private final UserRepository userRepository;
    private final PaymentTransactionService paymentTransactionService;

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

    @Transactional(isolation = Isolation.REPEATABLE_READ,
            rollbackFor = Exception.class)
    void delete(Long merchantId) {
        merchantRepository.findById(merchantId)
                .map(Merchant::getTransactions)
                .filter(CollectionUtils::isEmpty)
                .ifPresentOrElse(__ -> merchantRepository.deleteById(merchantId),
                        () -> {
                            throw new MerchantNotEligibleForRemovalException("Merchant has transaction history.");
                        });
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
        actualUser.setName(hasText(merchantEditRequest.name())
                ? merchantEditRequest.name()
                : actualUser.getName());
        actualUser.setDescription(hasText(merchantEditRequest.description())
                ? merchantEditRequest.description()
                : actualUser.getDescription());
        actualUser.setEmail(hasText(merchantEditRequest.username())
                ? merchantEditRequest.username()
                : actualUser.getEmail());
        actualUser.setStatus(merchantEditRequest.status() != null
                ? merchantEditRequest.status()
                : actualUser.getStatus());
        actualUser.setPassword(hasText(merchantEditRequest.password())
                ? merchantEditRequest.password()
                : actualUser.getPassword());

        return (Merchant) actualUser;
    }
}
