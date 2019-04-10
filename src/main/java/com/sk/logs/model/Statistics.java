package com.sk.logs.model;

import java.util.HashMap;
import java.util.Map;

public class Statistics {

    Map<String,Integer> hits = new HashMap<>();

    public void addHit(String hostName){
        hits.put(hostName,hits.getOrDefault(hostName,0) + 1);
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "hits=" + hits +
                '}';
    }
}
