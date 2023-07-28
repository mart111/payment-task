package com.payment.model.response;

import com.payment.model.TransactionStatus;

import java.math.BigDecimal;

public record TransactionResponse(BigDecimal amount,
                                  TransactionStatus status,
                                  String customerPhone,
                                  String customerEmail,
                                  String uuid,
                                  String referenceId) {

}
