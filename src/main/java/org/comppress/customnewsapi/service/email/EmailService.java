package org.comppress.customnewsapi.service.email;

import org.comppress.customnewsapi.dto.ForgetPasswordDto;
import org.comppress.customnewsapi.exceptions.EmailAlreadyExistsException;
import org.comppress.customnewsapi.exceptions.EmailSenderException;

import java.util.Properties;

public interface EmailService {

    String sendAutomatedEmailWithTemplate(Properties properties) throws EmailSenderException, EmailAlreadyExistsException;

}
