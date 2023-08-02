package com.payment.listener;

import com.payment.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CreateDefaultAdminOnStartup {

    private final AdminService adminService;

    @Value("classpath:/import-admin.csv")
    private Resource csvFile;

    @EventListener(ApplicationReadyEvent.class)
    public void createAdminOnStartup() throws IOException {
        final var content = csvFile.getContentAsByteArray();
        adminService.importFromCsv(content);
    }
}
