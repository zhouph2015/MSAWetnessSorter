package com.evariant.platform.msawetness.process;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.evariant.platform.msawetness.helpers.MSAProcessUtils;
import com.evariant.platform.msawetness.model.County;
import com.evariant.platform.msawetness.model.CountyKey;
import com.evariant.platform.msawetness.model.MSA;
import com.evariant.platform.msawetness.model.MSAWetness;
import com.evariant.platform.msawetness.model.PrecipitationRecord;
import com.evariant.platform.msawetness.model.WBANStation;

import com.opencsv.CSVParser;

/**
 * This MSAWetness class is used to retrieve information from input text files
 * and calculate & sort the Wetness of each Metropolitan Statistical Areas
 * (MSAs). The definition of Wetness
 * is the total population of MSA multiplied by the average amount of rain
 * received in 2015 May.
 * 
 * @author Peter Zhou
 */

public class MSAWetnessProcess {

    private static Logger logger = LoggerFactory.getLogger(MSAWetnessProcess.class);

    public static ArrayList<MSAWetness> calculateAndSort(String statesFile, String countyToMSAFile, String wbanMasterListFile,
            String precipitationFile) throws Exception {

        // create the state and its Abbreviation for further process
        Map<String, String> statesToAbbrMap = readStateShort(statesFile);

        // create the msaMap and countyMap
        Map<String, MSA> msaMap = new HashMap<String, MSA>();
        Map<CountyKey, County> countyMap = readCountyAndMSA(countyToMSAFile, statesToAbbrMap, msaMap);

        // create the wbanMap
        Map<String, WBANStation> wbanMap = readWBANStation(wbanMasterListFile, countyMap);

        // calculate the total precipitation per WBANStation
        Map<WBANStation, Double> totalPrecipPereWBANMap = calculatePrecipitation(precipitationFile, wbanMap);

        // calculate the average precipitation amount per county
        Map<CountyKey, Double> avgPrecipAmountPerCountyMap = calculateAvgPrecipAmountPerCounty(totalPrecipPereWBANMap);

        // calculate and sort the wetness of each MSA
        ArrayList<MSAWetness> msaWetnessList = calculateAndSortWetnessPerMSA(avgPrecipAmountPerCountyMap, msaMap, countyMap);
        
        // sort the msaWetnessList
        Collections.sort(msaWetnessList, Collections.reverseOrder());

        return msaWetnessList;
    }

    /**
     * Creates a state with its abbreviation HashMap with the provided
     * information.
     * 
     * @param a text file path store the state name and it's abbreviation.
     * @return the created hashMap with state and its abbreviation
     */
    static HashMap<String, String> readStateShort(String stateFilePath) throws IOException {
        logger.info("Read State and its abbrivation process start");
        CSVParser csvParser = new CSVParser(',');
        HashMap<String, String> stateMap = new HashMap<String, String>();
        BufferedReader br = getBufferedReaderFromFile(stateFilePath);
        String line;
        String[] fields;
        while ((line = br.readLine()) != null) {
            fields = csvParser.parseLine(line);
            stateMap.put(fields[0].toLowerCase(), fields[1].toLowerCase());
        }
        br.close();
        logger.info("Read State and its abbrivation process end");
        return stateMap;
    }

    /**
     * Creates a CountyKey and County HashMap with the provided
     * information.
     * 
     * @param a text file path store county and MSA information
     * @param a hashMap with state and its abbreviation
     * @param a hashMap will store the MSA name with MSA
     * @return the created hashMap with CountyKey and County
     */

    static Map<CountyKey, County> readCountyAndMSA(String CountyToMSApathFile, Map<String, String> stateToAbbreMap,
            Map<String, MSA> MSAMap) throws IOException, ParseException {
        logger.info("Read County and MSA information process start");
        CSVParser csvParser = new CSVParser(',');
        Map<CountyKey, County> countyMap = new HashMap<>();
        BufferedReader br = getBufferedReaderFromFile(CountyToMSApathFile);
        String line;
        String[] fields;
        while ((line = br.readLine()) != null) {

            fields = csvParser.parseLine(line);
            if (fields.length != 7) {
                throw new ParseException("can not parse: " + line, 0);
            }
            String name = fields[1].replace(" County", "").toLowerCase();
            String state = fields[2].toLowerCase();
            int population = Integer.parseInt(fields[4].replace(",", ""));
            String msaName = fields[5];
            // Check if this county belong a MSA, then update the MSAHap
            if (!msaName.isEmpty()) {

                County county = new County(new CountyKey(name, stateToAbbreMap.get(state)), population, msaName);
                if (MSAMap.containsKey(msaName)) {
                    MSA msa = MSAMap.get(msaName);
                    msa.addCounty(county.getCountyKey());
                    MSAMap.put(msaName, msa);
                } else {
                    MSA msa = new MSA(msaName);
                    msa.addCounty(county.getCountyKey());
                    MSAMap.put(msa.getName(), msa);
                }
                countyMap.put(county.getCountyKey(), county);
            }
        }
        br.close();
        logger.info("Read County and MSA information process end");
        return countyMap;
    }

    /**
     * Creates a wbanId and WBANStation HashMap with the provided
     * information.
     * 
     * @param a text file path store WBANStation information
     * @param a hashMap with with CountyKey and County information
     * @return the created hashMap with wbanID and WBANStation
     */

    static Map<String, WBANStation> readWBANStation(String wbanMasterListFile, Map<CountyKey, County> msaCountyMap)
            throws IOException, ParseException {
        logger.info("Read WBANStation information process start");
        Map<String, WBANStation> wbanMap = new HashMap<String, WBANStation>();
        BufferedReader br = getBufferedReaderFromFile(wbanMasterListFile);

        String line;
        while ((line = br.readLine()) != null) {
            WBANStation wbanStation = MSAProcessUtils.wbanStationparse(line);
            if (wbanStation != null) {
                wbanMap.put(wbanStation.getWbanId(), wbanStation);
            }
        }
        logger.info("Read WBANStation information process end");
        return wbanMap;
    }

    /**
     * Creates a WBANStation and precipitation amount HashMap with the provided
     * information.
     * 
     * @param a text file path store precipitation for all WBANSation over the
     *            U.S.
     * @param a hashMap with with wbanStationId and WBANStation
     * @return the created hashMap with WBANStation and precipitation amount
     */

    private static Map<WBANStation, Double> calculatePrecipitation(String precipitationFilePath, Map<String, WBANStation> wbanMap)
            throws IOException, ParseException {
        logger.info("Calculate precipitation process start");
        Map<WBANStation, Double> wbanIdTotalPrecipMap = new HashMap<>();
        BufferedReader br = getBufferedReaderFromFile(precipitationFilePath);

        String line;
        while ((line = br.readLine()) != null) {
            PrecipitationRecord precipRecord = MSAProcessUtils.preciptationRecordParse(line);
            if (precipRecord.isDay() && precipRecord.rained() && wbanMap.containsKey(precipRecord.getWban())) {

                if (!wbanIdTotalPrecipMap.containsKey(wbanMap.get(precipRecord.getWban()))) {
                    wbanIdTotalPrecipMap.put(wbanMap.get(precipRecord.getWban()), new Double(0D));
                } else {
                    Double precipAmount = wbanIdTotalPrecipMap.get(wbanMap.get(precipRecord.getWban()))
                            + precipRecord.getPrecipitation();
                    wbanIdTotalPrecipMap.put(wbanMap.get(precipRecord.getWban()), precipAmount);
                }
            }
        }
        br.close();
        logger.info("Calculate precipitation process end");
        return wbanIdTotalPrecipMap;
    }

    /**
     * Creates a CountyKey and average precipitation amount inside the County
     * HashMap with the provided
     * information.
     * 
     * @param a hashMap with WBANStation and precipitation amount
     * @return the created hashMap with CountyKey and average precipitation
     *         amount inside the County
     */

    private static Map<CountyKey, Double> calculateAvgPrecipAmountPerCounty(Map<WBANStation, Double> wbanIdTotalPrecipMap) {
        logger.info("Calculate average precipitation amount per county process start");
        Map<CountyKey, Double> avgPrecipAmountPerCountyMap = new HashMap<>();
        for (Map.Entry<WBANStation, Double> entry : wbanIdTotalPrecipMap.entrySet()) {
            CountyKey key = entry.getKey().getCountyKey();
            Double averageAmount = entry.getValue();
            // calculate the average amount of WBANStation locates in the county
            if (avgPrecipAmountPerCountyMap.containsKey(key)) {
                averageAmount = (avgPrecipAmountPerCountyMap.get(key) + averageAmount) / 2;
            }
            avgPrecipAmountPerCountyMap.put(key, averageAmount);
        }
        logger.info("Calculate average precipitation amount per county process end");
        return avgPrecipAmountPerCountyMap;
    }

    /**
     * Calculate and sort MSAWetness with the provided
     * information.
     * 
     * @param a hashMap with WBANStation and precipitation amount
     * @param a hashMap with MSA name and MSA
     * @param a hashMap with CountyKey and County
     * @return the created ArrayList of MSAWetness
     */

    private static ArrayList<MSAWetness> calculateAndSortWetnessPerMSA(Map<CountyKey, Double> avgPrecipAmountPerCountyMap,
            Map<String, MSA> msaMap, Map<CountyKey, County> countyMap) {
        logger.info("Calculate and sort MSAWetness process start");
        ArrayList<MSAWetness> msaWetnessList = new ArrayList<MSAWetness>();

        for (Map.Entry<String, MSA> entry : msaMap.entrySet()) {
            String msaName = entry.getKey();
            MSA msa = entry.getValue();
            int msaPopulation = 0;
            Double msaPercipitation = 0.0;
            int countyNum = 0;
            for (CountyKey countyKey : msa.getListOfCounty()) {
                msaPopulation += countyMap.get(countyKey).getPopulation();
                if (avgPrecipAmountPerCountyMap.get(countyKey) != null) {
                    msaPercipitation += avgPrecipAmountPerCountyMap.get(countyKey);
                }
                countyNum++;
            }
            Double wetness = (msaPercipitation / countyNum) * msaPopulation;
            MSAWetness msaWetness = new MSAWetness(msaName, wetness);
            msaWetnessList.add(msaWetness);
        }
        logger.info("Calculate and sort MSAWetness process end");
        return msaWetnessList;
    }

    /**
     * a helper function to read BufferReader from input file path
     * information.
     * 
     * @param a text file path
     * @return a BufferReader with skipping of the first line metadata
     */

    private static BufferedReader getBufferedReaderFromFile(String path) throws IOException {
        InputStream inputStream = new FileInputStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        // to skip the metadata line of the text data file
        br.readLine();
        return br;
    }
}
