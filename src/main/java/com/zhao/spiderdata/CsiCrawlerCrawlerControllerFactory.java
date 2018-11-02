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
        Pattern URLFILTERS = Pattern.compile("(http://)\\S*\\.baixing.com\\/\\S+\\/\\ba\\d+\\.html\\?from=regular");
        String href ="http://shanghai.baixing.com/jiaoyouqun/a888.html?from=regular";
        System.out.println(URLFILTERS.matcher(href).matches());
        Random random = new Random();
        System.out.println(random.nextInt(10));

    }
}
