package org.comppress.customnewsapi.service;

import org.comppress.customnewsapi.exceptions.EmailSenderException;

import java.util.Properties;

public interface EmailService {

   String sendAutomatedEmailWithTemplate(Properties properties) throws EmailSenderException;
}
