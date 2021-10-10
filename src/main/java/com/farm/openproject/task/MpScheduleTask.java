package com.farm.openproject.task;

import com.farm.openproject.util.MpBroswerUtil;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * @author zhengpeng
 * @date 2021-09-25 16:10
 **/
@Configuration
@EnableScheduling
@Slf4j
public class MpScheduleTask {




    /**
     * //每天8点执行
     * @throws Exception
     */
    @Scheduled(cron = "0 0 18 * * ?")
    //@Scheduled(cron = "0 */10 * * * ?")
    private void configureTask() throws Exception{
        log.info("开始执行定时任务");
        // 禁用沙箱
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--window-size=1920,1080");
        chromeOptions.addArguments("--start-maximized");
        WebDriver broser = new ChromeDriver(chromeOptions);
        //全局等待10s
        broser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        broser.get("https://mp.weixin.qq.com/");
        //登陆区域
        broser.findElement(By.xpath("//*[@id='header']/div[2]/div/div")).click();
        Thread.sleep(2000);
        //截图
        File loginScreenShotFile = ((TakesScreenshot) broser).getScreenshotAs(OutputType.FILE);
        // 登陆图片的base64码
        String base64ImgStr = MpBroswerUtil.getLoginBase64ImgStr(loginScreenShotFile);
        MpBroswerUtil.sendEmail(base64ImgStr,0);
        //登陆成功后，写图文素材
        Integer counter = 0;
        // 监听登陆成功
        while (counter < 180){
            counter++;
            log.info("等待登陆中");
            if(MpBroswerUtil.successLogin(broser)){
                counter = 200;
            }
        }
        if(counter >= 180 && counter < 200){
            log.info("登陆等待超时：" + counter);
            broser.close();
            log.info("关闭浏览器成功");
            return;
        }
        if(MpBroswerUtil.successLogin(broser)){
            log.info("登陆成功");
            //点击图文素材
            MpBroswerUtil.doImgAndTxtMaterial(broser,0);
            MpBroswerUtil.doNewCreation(broser,0);
            //切换窗口
            MpBroswerUtil.switchWindow(broser);
            //设置标题
            String title = MpBroswerUtil.dateFormat("yyyy-MM-dd") + "的github趋势榜单";
            String author = "farm机器人";
            String content = MpBroswerUtil.crawlingContent().replaceAll("'","`");
            log.info("content:" + content);
            MpBroswerUtil.writeTitle(broser,0, title);
            MpBroswerUtil.writeAuthor(broser,0, author);
            MpBroswerUtil.writeContent(broser,0, content);
            MpBroswerUtil.doScrollTop(broser,0);
            //点击插图片
            broser.findElement(By.xpath("//div[@id='js_cover_area']/div/i")).click();
            broser.findElement(By.xpath("//div[@id='js_cover_null']/ul/li[2]/a")).click();
            //选择第一张
            broser.findElement(By.xpath("//div[@id='vue_app']/div[2]/div/div/div[2]/div/div/div[2]/div/div[3]/div/div/i")).click();
            broser.findElement(By.xpath("//div[@id='vue_app']/div[2]/div/div/div[3]/div/button")).click();
            broser.findElement(By.xpath("//div[@id='vue_app']/div[2]/div/div/div[3]/div[2]/button")).click();
            //点击群发
            Thread.sleep(2000);
            broser.findElement(By.xpath("//span[@id='js_send']/button")).click();
            //确定群发
            broser.findElement(By.xpath("//*[@id='vue_app']/div[2]/div[1]/div[1]/div/div[3]/div/div[3]/div/button")).click();
            Thread.sleep(2000);
            //继续群发
            broser.findElement(By.xpath("//*[@id='vue_app']/div[2]/div[2]/div[1]/div/div[3]/div/div[1]/button")).click();
            Thread.sleep(2000);
            //截图
            File groupSendScreenShotFile = ((TakesScreenshot) broser).getScreenshotAs(OutputType.FILE);
            //发送群发截图邮件
            String grouSendBase64ImgStr = MpBroswerUtil.getGroupSendBase64ImgStr(groupSendScreenShotFile);
            MpBroswerUtil.sendEmail(grouSendBase64ImgStr,0);

        }
        //等待半个小时后关闭
        Thread.sleep(3600 * 1000);
        //关闭浏览器 释放资源
        broser.close();
        log.info("关闭浏览器成功");

    }
}
