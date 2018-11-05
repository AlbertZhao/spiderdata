package com.zhao.spiderdata;

import java.util.Random;
import java.util.regex.Pattern;

public class CsiCrawlerCrawlerControllerFactory  {
//
//    Map<String, String> metadata;
//    SqlRepository repository;
//
//    public CsiCrawlerCrawlerControllerFactory(Map<String, String> metadata, SqlRepository repository) {
//        this.metadata = metadata;
//        this.repository = repository;
//    }
//
//    @Override
//    public WebCrawler newInstance() throws Exception {
//        return new MyCrawler(metadata, repository);
//    }

    public static void main(String[] args) {
        Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
                + "|png|mp3|mp4|zip|gz))$");
        Pattern URLFILTERS = Pattern.compile("(http://)\\S*\\.baixing.com\\/\\S+\\/\\ba\\d+\\.html\\?from=regular");
        Pattern URL_P1 = Pattern.compile("(http://)\\S*\\.baixing.com\\/nanzhaov\\/\\S*");
        Pattern URL_P2 = Pattern.compile("(http://)\\S*\\.baixing.com\\/juhui\\/\\S*");
        Pattern URL_P3 = Pattern.compile("(http://)\\S*\\.baixing.com\\/zhenghun\\/\\S*");
        String href ="http://beijing.baixing.com/huodong/?page=1";
        System.out.println(!FILTERS.matcher(href).matches()
                && URLFILTERS.matcher(href).matches() && (URL_P1.matcher(href).matches()
                || URL_P2.matcher(href).matches()
                || URL_P3.matcher(href).matches()));
        Random random = new Random();
        System.out.println(random.nextInt(10));

    }
}
