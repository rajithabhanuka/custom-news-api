package org.comppress.customnewsapi.controller;

import org.comppress.customnewsapi.exceptions.EmailSenderException;
import org.comppress.customnewsapi.service.EmailService;
import org.comppress.customnewsapi.utils.EmailProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping(value = "/sendemail")
    public String sendEmail() throws EmailSenderException {

        return emailService.sendAutomatedEmailWithTemplate(EmailProperties.getEmailProperties());
    }

}
