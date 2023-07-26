package com.payment.converter;

import com.payment.model.Role;
import org.springframework.core.convert.converter.Converter;

public class RoleStringConverter implements Converter<String, Role> {

    @Override
    public Role convert(String source) {
        return Role.valueOf(source.toUpperCase());
    }
}
