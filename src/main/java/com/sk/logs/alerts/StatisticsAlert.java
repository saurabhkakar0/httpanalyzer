package com.sk.logs.alerts;

import com.sk.logs.model.Statistics;
import com.sk.logs.parser.LogEntry;
import com.sk.logs.parser.LogParser;
import com.sk.logs.stream.LogFileReader;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class StatisticsAlert implements Alert{

    private static Logger logger = LoggerFactory.getLogger(StatisticsAlert.class);

    private Executor exceutor = Executors.newSingleThreadExecutor();

    public void startListening(){

        final Observable<String> streamObservable = LogFileReader.observeStream();
        streamObservable
                .window(5, TimeUnit.SECONDS)
                .flatMap(stringObservable -> stringObservable.toList().toObservable())
                .map(logLines -> {
                    logger.info("This window emitted {} items",logLines.size());
                    Statistics statistics = new Statistics();
                    for(final String logLine : logLines){
                        enrichStatistics(logLine,statistics);
                    }
                    return statistics;
                })
                .onErrorReturn(throwable -> {
                    logger.error("Exception occurred during parsing",throwable);
                    return new Statistics();
                })
                .subscribeOn(Schedulers.from(exceutor))

                .subscribe(statistics -> logger.info("Statistics from {} to {}",statistics));

    }

    private void enrichStatistics(String logLine, Statistics statistics) throws ParseException {

        LogEntry logEntry = LogParser.convert(logLine);
        String hostName = logEntry.getIp();
        statistics.addHit(hostName);

    }

}
