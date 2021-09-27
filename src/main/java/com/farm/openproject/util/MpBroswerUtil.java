package com.farm.openproject.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.farm.openproject.pojo.bo.GitTrendingBo;
import io.github.biezhi.ome.OhMyEmail;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author zhengpeng
 * @date 2021-09-21 15:03
 **/
@Slf4j
public class MpBroswerUtil {

    /**
     * 微信公众号点击新的创作
     * @param broser
     */
    public static void doNewCreation(WebDriver broser,Integer retryTimes) throws InterruptedException {

        try{
            log.info("微信公众号点击新的创作");
            WebElement element = broser.findElement(By.xpath("//*[@id='js_main']/div[3]/div[2]/div/div/div/div[1]/div/div[1]/div[1]"));
            log.info("微信公众号点击新的创作:" + element);
            if(Objects.nonNull(element)){
                element.click();
                WebElement writeElement = broser.findElement(By.xpath("//*[@id='js_main']/div[3]/div[2]/div/div/div/div[1]/div/div[1]/div[2]/ul/li[1]"));
                log.info("点击新的创作：" + writeElement);
                writeElement.click();
                log.info("点击新的创作成功" );
            }
        }catch (Exception e){
            e.printStackTrace();
            if(retryTimes <= 3){
                TimeUnit.SECONDS.sleep(1);
                doNewCreation(broser,retryTimes + 1);
            }

        }


    }

    /**
     * 微信公众号点击图文素材
     * @param broser
     */
    public static void doImgAndTxtMaterial(WebDriver broser,Integer retryTimes) throws InterruptedException {
        try{

            WebElement element = broser.findElement( By.xpath("//*[@id=\"js_mp_sidemenu\"]/div/div/ul/li[2]/ul/li[1]/ul/li[1]/a"));
            log.info("微信公众号点击图文素材：" + element);
            Thread.sleep(1000);
            if(Objects.nonNull(element)){
                element.click();
                log.info("微信公众号点击图文素材:成功");
            }
        }catch (Exception e){
            e.printStackTrace();
            if(retryTimes <= 3){
                TimeUnit.SECONDS.sleep(1);
                doImgAndTxtMaterial(broser,retryTimes + 1);
            }
        }

    }

    /**
     * 设置标题
     * @param broser
     * @param retryTimes
     * @param title
     */
    public static void writeTitle(WebDriver broser, int retryTimes,String title) throws InterruptedException {
        try {
            WebElement element = broser.findElement(By.xpath("//*[@id='title']"));
            log.info("匹配写标题元素:" + element);
            element.sendKeys(title);
            log.info("写标题成功");
        }catch (Exception e){
            e.printStackTrace();
            if(retryTimes <= 3){
                TimeUnit.SECONDS.sleep(1);
                writeTitle(broser,retryTimes + 1,title);
            }
        }


    }

    /**
     * 设置作者
     * @param broser
     * @param retryTimes
     * @param author
     */
    public static void writeAuthor(WebDriver broser, int retryTimes, String author) throws InterruptedException {

        try {
            WebElement element = broser.findElement(By.xpath("//*[@id='author']"));
            log.info("匹配写作者元素：" + element);
            element.sendKeys(author);
            log.info("写作者成功");
        }catch (Exception e){
            e.printStackTrace();
            if(retryTimes <= 3){
                TimeUnit.SECONDS.sleep(1);
                writeAuthor(broser,retryTimes + 1,author);
            }
        }
    }

    /**
     * 写内容
     * @param broser
     * @param retryTimes
     * @param content
     */
    public static void writeContent(WebDriver broser, int retryTimes, String content) throws InterruptedException {

        try {

            String js = "document.querySelector('#ueditor_0').contentDocument.querySelector('body').innerHTML='"+content+"'";
            ((JavascriptExecutor)broser).executeScript(js);
        }catch (Exception e){
            e.printStackTrace();
            if(retryTimes <= 3){
                TimeUnit.SECONDS.sleep(1);
                writeContent(broser,retryTimes + 1,content);
            }
        }
    }

    public static void doScrollTop(WebDriver broser, int retryTimes) throws InterruptedException {

        try {

            String js = "var q=document.documentElement.scrollTop=50000";
            ((JavascriptExecutor)broser).executeScript(js);
        }catch (Exception e){
            e.printStackTrace();
            if(retryTimes <= 3){
                TimeUnit.SECONDS.sleep(1);
                doScrollTop(broser,retryTimes + 1);
            }
        }
    }

    /**
     * 成功登陆
     * @param broser
     * @return
     */
    public static boolean successLogin(WebDriver broser){

        try {
            WebElement element = broser.findElement(By.xpath("//*[@id=\"js_mp_sidemenu\"]/div/div/ul/li[2]/ul/li[1]/ul/li[1]/a"));
            log.info("登陆查找的element：" + element);
            if(Objects.nonNull(element)){
                return true;
            }
        }catch (Exception e){
            //继续等待
            return false;
        }
        return false;
    }



    /**
     *  切新的窗口
     */
    public static void switchWindow(WebDriver broser) {
        log.info("切换窗口");
        Set<String> windowHandles = broser.getWindowHandles();
        String current = broser.getWindowHandle();
        windowHandles.remove(current);
        String next = windowHandles.iterator().next();
        broser.switchTo().window(next);
        log.info("切换窗口成功");

    }


    /**
     * 今日git ranking集合
     * @return
     */
    public static List<GitTrendingBo> todayRankings(){
        ArrayList<GitTrendingBo> objects = new ArrayList<GitTrendingBo>();
        String html = crawling(0);
        Document document = Jsoup.parse(html);
        Elements blocks = document.select("#js-pjax-container > div.position-relative.container-lg.p-responsive.pt-6 > div > div > article");

        for (Element block : blocks) {
            objects.add(extract(block.html()));
        }

        return objects;

    }

    /**
     * 持续爬取直到跑到为止
     * @param i
     * @return
     */
    private static String crawling(int i) {
        try{
            HttpResponse<String> response = Unirest.get("https://github.com/trending")
                    .header("cache-control", "no-cache")
                    .asString();
            String html = response.getBody();
            return html;
        }catch (Exception e){
            e.printStackTrace();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            return crawling(i++);
        }
    }

    /**
     * 提取 封装成对象
     * @param block
     * @return
     */
    private static GitTrendingBo extract(String block) {

        /**
         * jsoup筛选
         */
        Document document = Jsoup.parse(block);
        Elements urlElement = document.select("h1 > a");
        Elements languageElement = document.select("div.f6.color-text-secondary.mt-2 > span.d-inline-block.ml-0.mr-3 > span:nth-child(2)");
        Elements starsElement = document.select("div.f6.color-text-secondary.mt-2 > a:nth-child(2)");
        Elements forksElement = document.select("div.f6.color-text-secondary.mt-2 > a:nth-child(3)");
        Elements todayStarsElement = document.select("div.f6.color-text-secondary.mt-2 > span.d-inline-block.float-sm-right");
        Elements descriptionElement = document.select("p");

        String url = urlElement.text().replaceAll(" ","");
        String language = languageElement.text();
        String stars = starsElement.text();
        String forks = forksElement.text();
        String todayStars = todayStarsElement.text();
        String description = descriptionElement.text();

        GitTrendingBo trendingBo = GitTrendingBo.builder()
                .url(url)
                .language(language)
                .starts(stars)
                .forks(forks)
                .todayStars(todayStars)
                .description(description)
                .build();
        return trendingBo;
    }

    public static String dateFormat(String format){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDateTime now = LocalDateTime.now();
        return now.format(formatter);

    }

    /**
     * 爬取正文
     * @return
     */
    public static String crawlingContent() {

        StringBuilder sb = new StringBuilder();
        String yyyyMMdd = dateFormat("yyyy-MM-dd");
        List<GitTrendingBo> gitTrendingBos = todayRankings();
        if(CollectionUtils.isNotEmpty(gitTrendingBos)){
            sb.append(" ").append(yyyyMMdd).append("日的github trending🌍。</br></br></br>");

            sb.append("<p>链接:</p>");
            sb.append("<p>语言:</p>");
            sb.append("<p>星星🌟:</p>");
            sb.append("<p>今日星星🌟:</p>");
            sb.append("<p>forks:</p>");
            sb.append("<p>Description:</p>");
            sb.append("<p>一句话介绍:</p>");
            sb.append("</br>");
            for (GitTrendingBo gitTrendingBo : gitTrendingBos) {
                sb.append("<p>" + "https://www.github.com/" + Optional.ofNullable(gitTrendingBo.getUrl()).orElse("小尾巴未找到") + "</p>");
                sb.append("<p>" + Optional.ofNullable(gitTrendingBo.getLanguage()).orElse("暂为找到语言") + "</p>");
                sb.append("<p>" + Optional.ofNullable(gitTrendingBo.getStarts()).orElse("暂无获取到") + "🌟🌟🌟" + "</p>");
                sb.append("<p>" + Optional.ofNullable(gitTrendingBo.getTodayStars()).orElse("小尾巴未找到") + "🌟🌟🌟" + "</p>");
                sb.append("<p>" + Optional.ofNullable(gitTrendingBo.getForks()).orElse("暂未获取到") + "forks" +  "</p>");
                sb.append("<p> " + Optional.ofNullable(gitTrendingBo.getDescription()).orElse("Description is empty") + "</p>");
                sb.append("<p> " + translateContent(Optional.ofNullable(gitTrendingBo.getDescription()).orElse("小尾巴未找到")) + "</p>");
                sb.append("</br>");
            }
        }
        return sb.toString();
    }


    /**
     * 翻译文本内容
     * @param content
     * @return
     */
    public static String translateContent(String content) {

        try{
            //有道翻译
            String url = "http://fanyi.youdao.com/translate?doctype=json&type=AUTO&i=" + URLEncoder.encode(content,"UTF-8");
            HttpResponse<String> response = Unirest.get(url)
                    .header("cache-control", "no-cache")
                    .asString();
            StringBuilder sb = new StringBuilder();
            JSONObject responseObj = JSONObject.parseObject(response.getBody());
            JSONArray translateResult = responseObj.getJSONArray("translateResult");
            for (Object result : translateResult) {
                JSONArray resultObj = (JSONArray) result;
                for (Object o : resultObj) {
                    JSONObject iter = (JSONObject) o;
                    sb.append(iter.getString("tgt"));
                }
            }
            return sb.toString();
        }catch (Exception e){
            e.printStackTrace();
            return "尝试翻译内容失败";
        }
    }

    /**
     * 获取登陆的二维码截图
     * @param snapshotFile
     * @return
     */
    public static String getLoginBase64ImgStr(File snapshotFile) throws Exception{
        OutputStream outputStream = new ByteArrayOutputStream();
        //裁剪图片工具类
        Thumbnails.of(snapshotFile).sourceRegion(Positions.TOP_CENTER, 1920,1080).size(1920, 1080).keepAspectRatio(false).toOutputStream(outputStream);
        return getBase64ImgStr(ConvertUtil.parse(outputStream));

    }

    /**
     *  获取群发的二维码截图
     * @param snapshotFile
     * @return
     * @throws Exception
     */
    public static String getGroupSendBase64ImgStr(File snapshotFile) throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        //裁剪图片工具类
        Thumbnails.of(snapshotFile).sourceRegion(Positions.CENTER, 1000,1000).size(1000, 1000).keepAspectRatio(false).toOutputStream(outputStream);
        return getBase64ImgStr(ConvertUtil.parse(outputStream));

    }


    /**
     * 将图片转换成Base64编码
     * @return
     */
    public static String getBase64ImgStr(InputStream inputStream) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理

        byte[] data = null;
        // 读取图片字节数组
        try {
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeBase64String(data);
    }

    /**
     * 将图片转换成Base64编码
     * @param imgFile
     * @return
     */
    public static String getBase64ImgStr(String imgFile) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理

        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeBase64String(data);
    }


    public static void sendEmail(String base64ImgStr) throws Exception {
        log.info("base64ImgStr:" + base64ImgStr);
        OhMyEmail.subject("farm微信公众号操作提醒邮件")
                .from("farm笔记的邮箱")
                .to("847850277@qq.com")
                .html("<h1 font=red>登陆二维码</h1><img src='data:image/jpg;base64,"+base64ImgStr+"' alt = 'login.jpg'/>")
                .send();
    }

    public static boolean hasContinueSend(WebDriver broser) {

        WebElement element = broser.findElement(By.xpath("//*[@id='vue_app']/div[2]/div[2]/div[1]/div/div[3]/div/div[1]/button"));
        if(Objects.nonNull(element)){
            return true;
        }else {
            return false;
        }
    }
}
