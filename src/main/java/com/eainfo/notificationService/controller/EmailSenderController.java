package com.eainfo.notificationService.controller;

import com.eainfo.notificationService.dto.EmailRequest;
import com.eainfo.notificationService.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailSenderController {

    private final EmailSenderService emailSenderService;

    @PostMapping("/send-email")
    public void sendEmail(@RequestBody EmailRequest request) {
        emailSenderService.sendEmail(request.getToEmail(), request.getTemplateName(), request.getVariables());
    }
}
