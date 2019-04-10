package com.sk;

import com.sk.logs.alerts.StatisticsAlert;
import com.sk.logs.stream.LogFileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 */
public class Starter
{
    static Logger logger = LoggerFactory.getLogger(Starter.class);

    public static void main( String[] args ) throws InterruptedException {

        logger.info("Starting Statistics Alerting");
        StatisticsAlert statisticsAlert = new StatisticsAlert();
        statisticsAlert.startListening();

        LogFileReader.startStreaming("/Users/saurabh/IdeaProjects/httploganalyzer/src/main/java/logs/common.log");

        System.out.println( "Hello World!" );
    }
}
