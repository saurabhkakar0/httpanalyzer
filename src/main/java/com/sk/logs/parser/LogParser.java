package com.sk.logs.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class LogParser {

    static final String DATE_FORMAT = "dd/MMM/yyyy:hh:mm:ss Z";

    static SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);


    static final int MAX_OPERATION_COUNT=1000;


    public static LogEntry convert(String line) throws ParseException {

        LogEntry log = new LogEntry();
        int pos = 0;
        // до первого пробела - IP адрес клиента
        int i = line.indexOf(' ');
        pos = i;
        log.setIp(line.substring(0, i));
        //  далее имя авторизованного пользователя (если есть)

        i = line.indexOf(' ', pos);

        pos = line.indexOf(' ', i + 1);

        log.setUserName(line.substring(i + 1, pos));
        pos = line.indexOf('[', pos);;
        i = line.indexOf(']', pos);

        String timeStr = line.substring(pos + 1, i);

        log.setDateTime(dateFormat.parse(timeStr));
        pos = i + 1;

        i = line.indexOf("\"", pos);
        if (i < 0) {
            i = line.indexOf("\"POST", pos);
        }
        pos = i + 1;

        i = line.indexOf("HTTP");

        log.setResourceUrl(line.substring(pos, i-1));
        // код HTTP ответа
        pos = line.indexOf('\"', pos) + 2;

        log.setStatus(Integer.parseInt(line.substring(pos, pos + 3)));
        pos = pos + 4;

        /*i = line.indexOf(' ', pos);
        log.setResponseSize(Integer.parseInt(line.substring(pos, i-1)));
        pos = i; // тут пробел*/

//        pos = line.indexOf('\"', pos);
//        pos = line.indexOf('\"', pos);
//        pos = line.indexOf('\"', pos); // перед user_agent;
//
//        i = line.indexOf('\"', pos + 1);
//
//        log.setUserAgent(line.substring(pos + 1, i));

        return log;
    }
}
