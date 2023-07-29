package com.payment.model.response;

import java.util.List;

public record MerchantListResponse (List<MerchantResponse> merchantResponseList) {
    public static final MerchantListResponse EMPTY = new MerchantListResponse(List.of());
}
