package com.zhao.spiderdata;

import com.zhao.spiderdata.utils.ExcelUtils;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.message.BasicHeader;
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
		String city = "china";
		boolean enableProxy = false;
		String proxyIP = null;
		String proxyPort = null;
		if (args.length>0) {
            city = args[0];
        }
        String crawlStorageFolder = "C:\\data\\";
//        String crawlStorageFolder = "/Users/zhaoshijie/Downloads/data/";
        int numberOfCrawlers = 3;

        CrawlConfig config = new CrawlConfig();
        config.setMaxDepthOfCrawling(1);
//        config.setMaxPagesToFetch(1000);
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setUserAgentString("Opera/9.80 (Macintosh; Intel Mac OS X 10.14.1) Presto/2.12.388 Version/12.16");
        config.setPolitenessDelay(new Random().nextInt(3) * 1000);


        //Set proxy ip
        File file = new File(crawlStorageFolder+"proxy.txt");
        List<ProxyObj> proxyObjs = null;
        if (file.isFile() && file.exists()) {
            proxyObjs = new ArrayList<ProxyObj>();
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String proxyAddress = null;

            while ((proxyAddress=br.readLine()) != null) {
                String proxyIPFromList = proxyAddress.split("\\|")[0];
                String proxyPortFromList = proxyAddress.split("\\|")[1];
                ProxyObj proxyObj = new ProxyObj();
                proxyObj.setProxyIP(proxyIPFromList);
                proxyObj.setProxyPort(proxyPortFromList);
                proxyObjs.add(proxyObj);
            }

            br.close();
        }

        //Set user agent string
        File agentFile = new File(crawlStorageFolder+"agent.txt");
        List<AgentObj> agentObjs = null;
        if (agentFile.isFile() && agentFile.exists()) {
            agentObjs = new ArrayList<AgentObj>();
            InputStreamReader isr = new InputStreamReader(new FileInputStream(agentFile), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String agentString = null;

            while ((agentString=br.readLine()) != null) {
                AgentObj agentObj = new AgentObj();
                agentObj.setAgentString(agentString);
                agentObjs.add(agentObj);
            }

            br.close();
        }


        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        ArrayList<Header> headers = new ArrayList<Header>();
//        headers.add(new BasicHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"));
//        headers.add(new BasicHeader("Cookie","__trackId=154131415226368; __city=shanghai; __s=vuitkp806us3kiqqiucbcghds3; _ga=GA1.2.986150485.1541314224; _gid=GA1.2.161395943.1541314224; Hm_lvt_5a727f1b4acc5725516637e03b07d3d2=1541314225; _auth_redirect=http%3A%2F%2Fshanghai.baixing.com%2Fhuodong%2F%3Fpage%3D1; Hm_lpvt_5a727f1b4acc5725516637e03b07d3d2=1541321600"));
//        headers.add(new BasicHeader("Connection", "keep-alive"));
//        headers.add(new BasicHeader("Remote Address", "127.0.0.1:1080"));
        //Create Crawler controller
        for (int i=1; i<=100; i++) {
            if (enableProxy) {
                if (proxyIP !=null && proxyPort !=null) {
                    config.setProxyHost(proxyIP);
                    config.setProxyPort(Integer.valueOf(proxyPort));
                    Header header = new BasicHeader("X-Forwarded-For", proxyIP);
                    Header xRealIP= new BasicHeader("X-Real-IP", proxyIP);
                    headers.add(header);
                    headers.add(xRealIP);
                    System.out.println("Proxy ip is =======================> " + config.getProxyHost() + ":" + config.getProxyPort());
                } else if (proxyObjs != null && proxyObjs.size()>0) {
                    Random random = new Random();
                    ProxyObj proxyObj = proxyObjs.get(random.nextInt(proxyObjs.size()));
                    Header reffer = new BasicHeader(HttpHeaders.REFERER,proxyObj.getProxyIP());
                    headers.add(reffer);
                    System.out.println("Proxy ip is ========================> " + proxyObj.getProxyIP() + ":" + proxyObj.getProxyPort());
                }
            }



            if (agentObjs != null && agentObjs.size()>0) {
                Random random = new Random();
                AgentObj agentObj = agentObjs.get(random.nextInt(agentObjs.size()));
                config.setUserAgentString(agentObj.getAgentString());
                Header header = new BasicHeader(HttpHeaders.USER_AGENT, agentObj.getAgentString());
                headers.add(header);
                System.out.println("Set User agent as ========================> " + config.getUserAgentString());
            }

            config.setDefaultHeaders(headers);
            PageFetcher pageFetcher = new PageFetcher(config);
            RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
            CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
            if (city != null){
                System.out.println("From City " + city + "start!" );
                controller.addSeed("http://" + city + ".baixing.com/huodong/?page=" + i);
            } else {
                controller.addSeed("http://shanghai.baixing.com/huodong/?page=" + i);
            }
            controller.start(MyCrawler.class, numberOfCrawlers);


            try {
                ExcelUtils.generateExcelFile(MyCrawler.list,crawlStorageFolder);
            } catch (IOException e) {
                System.out.println("generate excel failed!");
                e.printStackTrace();
            } finally {
                MyCrawler.list.clear();
            }
        }


	}
}
