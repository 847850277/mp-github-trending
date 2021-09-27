package com.farm.openproject.config;

import io.github.biezhi.ome.OhMyEmail;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author zhengpeng
 * @date 2021-09-14 09:48
 **/
@Component
@Configuration
@Slf4j
public class BeanInitializer implements ApplicationRunner {


    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.setProperty("webdriver.chrome.driver", PropertiesConfig.getWebdriverChromeDriver());
        if(StringUtils.isNotBlank(PropertiesConfig.getWebdriverChromeBin())){
            System.setProperty("webdriver.chrome.bin", PropertiesConfig.getWebdriverChromeBin());
        }
        //邮箱配置
        Properties properties = OhMyEmail.SMTP_163(true);
        properties.put("mail.smtp.starttls.enable",true);
        properties.put("mail.smtp.socketFactory.port",465);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        OhMyEmail.config(properties, PropertiesConfig.getEmailUsername(), PropertiesConfig.getEmailPassword());
        log.info("初始化邮箱配置成功");
        ChromeOptions chromeOptions = new ChromeOptions();
        // 禁用沙箱
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--window-size=1920,1080");
        chromeOptions.addArguments("--start-maximized");
        WebDriver broser = new ChromeDriver(chromeOptions);
        broser.get("https://www.baidu.com");
        log.info("谷歌浏览器打开成功:" + broser);
        broser.close();
        log.info("启动关闭成功");

    }
}