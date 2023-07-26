package com.payment.model;

import java.util.Date;

public record AuthenticationToken(String authToken, Date expiresIn) {
}
