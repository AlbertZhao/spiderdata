//package com.zhao.spiderdata;
//
//import edu.uci.ics.crawler4j.crawler.CrawlController;
//import edu.uci.ics.crawler4j.crawler.WebCrawler;
//
//import java.util.Map;
//
//public class CsiCrawlerCrawlerControllerFactory implements CrawlController.WebCrawlerFactory {
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
//}
