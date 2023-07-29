package com.payment.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.payment.model.request.MerchantEditRequest;
import com.payment.model.request.UserRegistrationRequest;
import com.payment.model.response.MerchantResponse;
import com.payment.model.response.UserRegistrationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final RegistrationService registrationService;
    private final MerchantService merchantService;

    @Transactional(isolation = Isolation.REPEATABLE_READ,
            rollbackFor = Exception.class)
    public List<UserRegistrationResponse> importFromCsv(byte[] fileAsBytes) {
        final var reader = new InputStreamReader(new ByteArrayInputStream(fileAsBytes));
        final CsvToBean<UserRegistrationRequest> csvToBean = new CsvToBeanBuilder<UserRegistrationRequest>(reader)
                .withType(UserRegistrationRequest.class)
                .withIgnoreLeadingWhiteSpace(true)
                .withIgnoreEmptyLine(true)
                .build();
        return Optional.of(csvToBean.stream().toList())
                .map(registrationService::registerAll)
                .orElse(List.of());

    }

    @Transactional(isolation = Isolation.REPEATABLE_READ,
            rollbackFor = Exception.class)
    public MerchantResponse updateMerchant(Long merchantId, MerchantEditRequest merchantEditRequest) {
        return merchantService.updateMerchant(merchantId, merchantEditRequest);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ,
            rollbackFor = Exception.class)
    public void deleteMerchant(Long merchantId) {
        merchantService.delete(merchantId);
    }
}
