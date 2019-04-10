package com.sk.logs.stream;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.concurrent.TimeUnit;

public class LogFileReader {

    private static Logger logger = LoggerFactory.getLogger(LogFileReader.class);

    private static PublishSubject<String> subject = PublishSubject.create();

    public static void startStreaming(final String filePath){

        Flowable.interval(1, TimeUnit.SECONDS)
                .flatMap(aLong -> getStreamingObservable(filePath))
                .subscribe(logLine -> subject.onNext(logLine),throwable -> {
                    logger.error("Exception occurred during file reading");
                });

    }

    public static Flowable<String> getStreamingObservable(final String filePath){

        return Flowable.using(
                () -> new BufferedReader(new FileReader(filePath)),
                reader -> Flowable.fromIterable(() -> reader.lines().iterator()),
                reader -> reader.close()
        );
    }

    public static Observable<String> observeStream(){
        return subject;
    }
}
