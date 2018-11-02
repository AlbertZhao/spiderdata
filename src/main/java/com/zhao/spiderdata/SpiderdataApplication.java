package com.zhao.spiderdata;

import com.zhao.spiderdata.utils.ExcelUtils;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class SpiderdataApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SpiderdataApplication.class, args);
		String city = null;
		if (args.length>0) {
            city = args[0];
        }
        String crawlStorageFolder = "/Users/zhaoshijie/Downloads/proxy/";
        int numberOfCrawlers = 3;

        CrawlConfig config = new CrawlConfig();
//        config.setMaxDepthOfCrawling(2);
//        config.setMaxPagesToFetch(1000);
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setUserAgentString("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36");

        File file = new File(crawlStorageFolder+"proxy.txt");
        List<ProxyObj> proxyObjs = null;
        if (file.isFile() && file.exists()) {
            proxyObjs = new ArrayList<ProxyObj>();
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String proxyAddress = null;

            while ((proxyAddress=br.readLine()) != null) {
                String proxyIP = proxyAddress.split("\\|")[0];
                String proxyPort = proxyAddress.split("\\|")[1];
                ProxyObj proxyObj = new ProxyObj();
                proxyObj.setProxyIP(proxyIP);
                proxyObj.setProxyPort(proxyPort);
                proxyObjs.add(proxyObj);
            }

            br.close();
        }


        for (int i=1; i<=100; i++) {
            if (proxyObjs != null && proxyObjs.size() >10) {
                Random random = new Random();
                ProxyObj proxyObj = proxyObjs.get(random.nextInt(10));
                config.setProxyHost(proxyObj.getProxyIP());
                config.setProxyPort(Integer.valueOf(proxyObj.getProxyPort()));
            }
            PageFetcher pageFetcher = new PageFetcher(config);
            RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
            RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
            CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
            if (city != null){
                controller.addSeed("http://" + city + ".baixing.com/huodong/?page=" + i);
            } else {
                controller.addSeed("http://shanghai.baixing.com/huodong/?page=" + i);
            }
            controller.start(MyCrawler.class, numberOfCrawlers);


            try {
                ExcelUtils.generateExcelFile(MyCrawler.list,"/Users/zhaoshijie/Downloads/");
            } catch (IOException e) {
                System.out.println("generate excel failed!");
                e.printStackTrace();
            } finally {
                MyCrawler.list.clear();
            }
        }


	}
}
