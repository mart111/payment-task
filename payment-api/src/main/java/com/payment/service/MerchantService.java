package com.payment.service;

import com.payment.model.entity.Merchant;
import com.payment.model.response.MerchantListResponse;
import com.payment.model.response.MerchantResponse;
import com.payment.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MerchantService {

    private final MerchantRepository merchantRepository;

    public MerchantListResponse getAllMerchants() {
        return Optional.of(merchantRepository.findAll())
                .map(this::convertToMerchantResponse)
                .orElse(MerchantListResponse.EMPTY);
    }

    private MerchantListResponse convertToMerchantResponse(List<Merchant> merchants) {
        return new MerchantListResponse(merchants
                .stream()
                .map(merchant -> new MerchantResponse(merchant.getName(),
                        merchant.getEmail(),
                        merchant.getTotalTransactionSum(),
                        merchant.getDescription()))
                .collect(Collectors.toList()));
    }
}
