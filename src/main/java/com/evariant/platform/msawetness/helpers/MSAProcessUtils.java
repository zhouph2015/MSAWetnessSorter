package com.evariant.platform.msawetness.helpers;

import java.io.IOException;
import java.text.ParseException;

import com.evariant.platform.msawetness.model.CountyKey;
import com.evariant.platform.msawetness.model.PrecipitationRecord;
import com.evariant.platform.msawetness.model.WBANStation;
import com.opencsv.CSVParser;

/**
 * a MSAProcessUtils class to provide the parse method to parse text lines
 * 
 * Created by Peter Zhou on 06/12/2017.
 */

public class MSAProcessUtils {

    /**
     * to pare the precipitation record
     * 
     * @param a text line contains precipitation record information
     * @return a precipitation object
     */

    public static PrecipitationRecord preciptationRecordParse(String row) throws ParseException {

        String SEPARATOR = ",";
        String[] fields = row.split(SEPARATOR);
        if (fields.length != 5) {
            throw new ParseException("can not parse: " + row, 0);
        }
        String precipitation = fields[3].trim();
        if (precipitation.equals("T") || precipitation.isEmpty()) {
            precipitation = "0.0";
        }
        double precipAmount = Double.parseDouble(precipitation);
        return new PrecipitationRecord(fields[0].trim(), fields[1].trim(), Integer.parseInt(fields[2].trim()), precipAmount,
                fields[4].trim());
    }

    /**
     * to pare the WBANStation
     * 
     * @param a text line contains WBANStation information
     * @return a WBANStation object
     */

    public static WBANStation wbanStationparse(String row) throws ParseException, IOException {

        CSVParser csvParser = new CSVParser('|', '"', '\n', false, false);
        String[] fields = csvParser.parseLine(row);
        if (fields.length != 19) {
            throw new ParseException("can not parse: " + row, 0);
        }
        String wbanId = fields[1];
        String state = fields[3].toLowerCase();
        String countyName = fields[4].toLowerCase();
        if (!countyName.isEmpty()) {
            return new WBANStation(wbanId, new CountyKey(countyName, state));
        }
        return null;
    }

}
