package com.payment.service;

import com.payment.model.Role;
import com.payment.model.Status;
import com.payment.model.request.MerchantEditRequest;
import com.payment.model.response.UserRegistrationResponse;
import lombok.Cleanup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    private static final List<UserRegistrationResponse> USERS_REGISTRATION_RESPONSE =
            List.of(
                    new UserRegistrationResponse("Merchant1", "Test merchant Description",
                            "merchant1@gmail.com", Role.MERCHANT),
                    new UserRegistrationResponse("Merchant2", "Test merchant Description",
                            "merchant2@gmail.com", Role.MERCHANT)
            );

    @Mock
    RegistrationService registrationService;

    @Mock
    MerchantService merchantService;

    @InjectMocks
    AdminService testSubject;

    @Test
    void importFromCsv() throws IOException {
        when(registrationService.registerAll(anyList()))
                .thenReturn(USERS_REGISTRATION_RESPONSE);

        @Cleanup final var resourceAsStream = AdminServiceTest.class
                .getClassLoader()
                .getResourceAsStream("csv/users.csv");
        final var fileAsBytes = resourceAsStream.readAllBytes();
        final var responseList = testSubject.importFromCsv(fileAsBytes);
        assertAll(() -> assertEquals(2, responseList.size()),
                () -> assertEquals(USERS_REGISTRATION_RESPONSE, responseList));

        verify(registrationService)
                .registerAll(anyList());
    }

    @Test
    void importFromCsv_RequiredHeaderFieldIsMissing() throws IOException {
        @Cleanup final var resourceAsStream = AdminServiceTest.class
                .getClassLoader()
                .getResourceAsStream("csv/users-missing-mandatory-field.csv");
        final var fileAsBytes = resourceAsStream.readAllBytes();
        assertThrows(Exception.class, () ->
                testSubject.importFromCsv(fileAsBytes));
        verify(registrationService, never())
                .registerAll(anyList());
    }

    @Test
    void updateMerchant() {
        MerchantEditRequest merchantEditRequest
                = new MerchantEditRequest("John",
                "Description",
                "john@gmail.com",
                "password1",
                Status.ACTIVE);

        testSubject.updateMerchant(1L, merchantEditRequest);

        verify(merchantService)
                .updateMerchant(anyLong(), any(MerchantEditRequest.class));
    }

    @Test
    void deleteMerchant() {
        testSubject.deleteMerchant(1L);

        verify(merchantService)
                .delete(anyLong());
    }
}