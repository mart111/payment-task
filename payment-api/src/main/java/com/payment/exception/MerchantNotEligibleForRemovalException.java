package com.payment.exception;

public class MerchantNotEligibleForRemovalException extends RuntimeException {

    public MerchantNotEligibleForRemovalException(String msg) {
        super(msg);
    }
}
