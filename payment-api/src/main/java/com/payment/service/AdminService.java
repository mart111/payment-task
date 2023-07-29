package com.payment.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.payment.model.request.UserRegistrationRequest;
import com.payment.model.response.UserRegistrationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final RegistrationService registrationService;

    @Transactional
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
}
