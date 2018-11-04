package com.zhao.spiderdata;

import com.zhao.spiderdata.pojo.PersonInfo;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

public class MyCrawler extends WebCrawler {
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp4|zip|gz))$");

    Pattern URLFILTERS = Pattern.compile("(http://)\\S*\\.baixing.com\\/\\S+\\/\\ba\\d+\\.html\\?from=regular");
    public static List<PersonInfo> list = new ArrayList<PersonInfo>();
    public static String city="shanghai";

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        System.out.println(href);
        return !FILTERS.matcher(href).matches()
                && URLFILTERS.matcher(href).matches() && (href.startsWith("http://"+city+".baixing.com/zhenghun/")
                || href.startsWith("http://"+city+".baixing.com/nanzhaonv/")
                || href.startsWith("http://"+city+".baixing.com/juhui/"));
    }

    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        System.out.println("URL: " + url);

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();
            System.out.println("Links counts: " + links.size());

            Document doc = Jsoup.parse(html);
            PersonInfo personInfo = new PersonInfo();
            personInfo.setName(doc.getElementsByClass("viewad-title").text());
            System.out.println("name:" + doc.getElementsByClass("viewad-title").text());
            if (doc.getElementById("mobileNumber") != null) {
                String mobile = doc.getElementById("mobileNumber").childNodes().get(0).childNodes().get(0).toString();
                personInfo.setMobileNumber(mobile);
                System.out.println("mobile:" + mobile);
            }
            System.out.println("publishDate: " + doc.getElementsByClass("viewad-actions").select("span").get(0).text());
            personInfo.setPublishDate(doc.getElementsByClass("viewad-actions").select("span").get(0).text());
            if (doc.getElementsByClass("viewad-meta").select("li") != null) {
                Elements details = doc.getElementsByClass("viewad-meta").select("li");
                for (int i=0; i< details.size(); i++) {
                    if (details.get(i).select("label").text().indexOf("性别") != -1) {
                        personInfo.setSex(details.get(i).select("span").text());
                        System.out.println("sex: " + details.get(i).select("span").text());
                    }

                    if (details.get(i).select("label").text().indexOf("年龄") != -1) {
                        personInfo.setAge(details.get(i).select("span").text());
                        System.out.println("age: " + details.get(i).select("span").text());
                    }

                    if (details.get(i).select("label").text().indexOf("职业") != -1) {
                        personInfo.setJob(details.get(i).select("span").text());
                        System.out.println("job: " + details.get(i).select("span").text());
                    }

                    if (details.get(i).select("label").text().indexOf("地址") != -1) {
                        personInfo.setAddress(details.get(i).select("span").text());
                        System.out.println("address: " + details.get(i).select("span").text());
                    }
                }
            }
            list.add(personInfo);
        }
    }
}
