package com.zhao.spiderdata;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpiderdataApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SpiderdataApplication.class, args);

        String crawlStorageFolder = "C:\\Spec\\dev_codes\\spiderdata\\data";
        int numberOfCrawlers = 3;

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);

        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        controller.addSeed("https://10.229.201.21:8463/ofx/ssomock");
        controller.addSeed("https://10.229.201.21:8463/ofx/sso/15D0C107-3ED1-171D-0FDE-3DD11E587DCD");

        controller.start(MyCrawler.class, numberOfCrawlers);
	}
}
