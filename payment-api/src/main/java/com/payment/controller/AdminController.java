package com.payment.controller;

import com.payment.model.GenericErrorResponse;
import com.payment.model.request.MerchantEditRequest;
import com.payment.model.response.MerchantListResponse;
import com.payment.service.AdminService;
import com.payment.service.MerchantService;
import com.payment.service.PaymentTransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.payment.model.GenericErrorResponse.withError;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;

@RestController
@RequestMapping("/api/v1/merchants")
@RequiredArgsConstructor
public class AdminController {

    private final PaymentTransactionService paymentTransactionService;
    private final MerchantService merchantService;
    private final AdminService adminService;

    @GetMapping
    public ResponseEntity<MerchantListResponse> fetchAllMerchants() {
        return ResponseEntity.ok(merchantService.getAllMerchants());
    }

    @GetMapping("transactions")
    public ResponseEntity<?> getAllTransactionsOfMerchant(@RequestParam String merchantEmail) {
        return hasText(merchantEmail) ?
                ResponseEntity.ok(paymentTransactionService.getAllTransactionsOfMerchant(merchantEmail)) :
                ResponseEntity.badRequest()
                        .body(GenericErrorResponse.withError("Merchant's email should be provided."));
    }

    @PostMapping(value = "import",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> importMerchants(@RequestParam("file") MultipartFile csvFile) throws IOException {
        final var fileAsBytes = csvFile.getBytes();
        return fileAsBytes.length > 0 ? ResponseEntity.ok(adminService.importFromCsv(fileAsBytes))
                : ResponseEntity.badRequest().body(withError("Provided CSV file is empty."));
    }

    @PutMapping("/{merchantId}")
    public ResponseEntity<?> updateMerchant(@PathVariable long merchantId,
                                            @RequestBody @Valid MerchantEditRequest merchantEditRequest) {
        final var merchantResponse = adminService.updateMerchant(merchantId, merchantEditRequest);
        return nonNull(merchantResponse) ?
                ResponseEntity.ok(merchantResponse) :
                ResponseEntity.badRequest()
                        .body(withError(String.format("Failed to update merchant with email '%s'",
                                merchantEditRequest.username())));
    }
}
