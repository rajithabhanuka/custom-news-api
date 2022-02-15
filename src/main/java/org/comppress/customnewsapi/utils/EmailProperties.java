package org.comppress.customnewsapi.utils;

import java.util.Date;
import java.util.Properties;

public class EmailProperties {

    public static Properties getEmailProperties(){

        Properties properties = new Properties();

        // dynamic in the future, "to" should be set to personal email
        properties.setProperty("to", "lucasstocksmeier@gmail.com");
        properties.setProperty("from", "automated@noreply");
        properties.setProperty("subject", "Forget password OTP");
        properties.setProperty("template_name", "email_template");

        properties.setProperty("otp", "12345");
        properties.setProperty("date", new Date() + "");

        return properties;
    }

}
