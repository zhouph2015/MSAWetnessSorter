package com.evariant.platform.msawetness.model;

import java.io.IOException;
import java.text.ParseException;

import com.opencsv.CSVParser;

/**
 * Created by Peter Zhou on 06/12/2017.
 */

public class WBANStation {

    private String wbanId;
    private CountyKey countyKey;

    public WBANStation(String wbanId, CountyKey county) {
        this.wbanId = wbanId;
        this.countyKey = county;
    }

    public static WBANStation parse(String row, CSVParser csvParser) throws ParseException, IOException {
        String[] fields = csvParser.parseLine(row);
        if (fields.length != 19) {
            throw new ParseException("can not parse: " + row, 0);
        }
        String wbanId = fields[1];
        String state = fields[3].toLowerCase();
        String countyName = fields[4].toLowerCase();
        return new WBANStation(wbanId, new CountyKey(countyName, state));
    }

    public String getWbanId() {
        return wbanId;
    }

    public CountyKey getCountyKey() {
        return countyKey;
    }

    @Override
    public String toString() {
        return "WBANStation{" + "wbanId='" + wbanId + '\'' + ", countyKey=" + countyKey + '}';
    }
}
