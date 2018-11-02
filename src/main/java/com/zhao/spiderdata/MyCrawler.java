package com.zhao.spiderdata;

import com.zhao.spiderdata.pojo.TestBeans;
import com.zhao.spiderdata.utils.ExcelUtils;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class MyCrawler extends WebCrawler {
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp4|zip|gz))$");

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches()
                && href.startsWith("https://XXXXXXXX/");
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
            Document doc = Jsoup.parse(html);

            List<TestBeans> list = new ArrayList<TestBeans>();
            TestBeans test = new TestBeans();
            test.setAid(doc.select("input[name=aid]").val());
            test.setUserID(doc.select("input[name=userId]").val());
            test.setCid(doc.select("input[name=cid]").val());
            test.setCountry(doc.select("select[name=country]").select("option[selected]").text());
            list.add(test);
            list.add(test);
            list.add(test);
            list.add(test);
            list.add(test);
            list.add(test);
            list.add(test);
            list.add(test);
            list.add(test);
            list.add(test);


            System.out.println("Aid: " + doc.select("input[name=aid]").val());
            System.out.println("USERID: " + doc.select("input[name=userId]").val());
            System.out.println("CID: " + doc.select("input[name=cid]").val());
            System.out.println("COUNTRY: " + doc.select("select[name=country]").select("option[selected]").text());

            try {
                ExcelUtils.generateExcelFile(list);
            } catch (IOException e) {
                System.out.println("generate excel failed!");
                e.printStackTrace();
            }
        }
    }
}
