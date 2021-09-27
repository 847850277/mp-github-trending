package com.farm.openproject.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhengpeng
 * @date 2021-09-27 11:36
 **/
@Configuration
public class PropertiesConfig {

    private static String emailUsername;

    private static String emailPassword;

    private static String webdriverChromeDriver;

    private static String webdriverChromeBin;

    @Value("${email.username}")
    public  void setEmailUsername(String emailUsername) {
        PropertiesConfig.emailUsername = emailUsername;
    }

    @Value("${email.password}")
    public  void setEmailPassword(String emailPassword) {
        PropertiesConfig.emailPassword = emailPassword;
    }

    @Value("${selenium.driverPath}")
    public  void setWebdriverChromeDriver(String webdriverChromeDriver) {
        PropertiesConfig.webdriverChromeDriver = webdriverChromeDriver;
    }


    @Value("${selenium.chrome}")
    public  void setWebdriverChromeBin(String webdriverChromeBin) {
        PropertiesConfig.webdriverChromeBin = webdriverChromeBin;
    }


    public static String getEmailUsername() {
        return emailUsername;
    }



    public static String getEmailPassword() {
        return emailPassword;
    }

    public static String getWebdriverChromeDriver() {
        return webdriverChromeDriver;
    }

    public static String getWebdriverChromeBin() {
        return webdriverChromeBin;
    }

}
