package com.evariant.platform.msawetness.process;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

import com.evariant.platform.msawetness.model.County;
import com.evariant.platform.msawetness.model.CountyKey;
import com.evariant.platform.msawetness.model.MSA;
import com.evariant.platform.msawetness.model.MSAWetness;
import com.evariant.platform.msawetness.model.WBANStation;

/**
 * Test class for MSAWetnessProcessTest
 * @ Created by Peter Zhou
 */

public class MSAWetnessProcessTest {

    final String statesInfoPath = "src/test/resource/states.txt";
    final String countyMSAPath = "src/test/resource/CountyMSA.txt";
    final String wbanListPath = "src/test/resource/wbanmasterlist.psv";
    final String percipitationPath = "src/test/resource/201505precip.txt";

    @Test
    public void testReadStateShort() throws Exception {

        HashMap<String, String> stateMap = MSAWetnessProcess.readStateShort(statesInfoPath);

        assertEquals("Check stateMap size", 51, stateMap.size());
        assertEquals("Check the Abbreviation for alabam", "al", stateMap.get("alabama"));
    }

    @Test
    public void testReadCountyAndMSA() throws Exception {

        HashMap<String, String> stateMap = MSAWetnessProcess.readStateShort(statesInfoPath);

        assertEquals("Check stateMap size", 51, stateMap.size());
        assertEquals("Check the Abbreviation for alabam", "al", stateMap.get("alabama"));

        Map<String, MSA> msaMap = new HashMap<String, MSA>();

        // read counties of MSAs
        Map<CountyKey, County> countyMap = MSAWetnessProcess.readCountyAndMSA(countyMSAPath, stateMap, msaMap);
        
        assertEquals("Check the number of counties in Birmingham-Hoover MSA",
                msaMap.get("Birmingham-Hoover, AL Metropolitan Statistical Area").getListOfCounty().size(), 7);
    }

    @Test
    public void testReadWBANStation() throws Exception {

        HashMap<String, String> stateMap = MSAWetnessProcess.readStateShort(statesInfoPath);

        Map<String, MSA> msaMap = new HashMap<String, MSA>();
        // read counties of MSAs
        Map<CountyKey, County> countyMap = MSAWetnessProcess.readCountyAndMSA(countyMSAPath, stateMap, msaMap);
        System.out.println(countyMap.size());
        Map<String, WBANStation> wbanMap = MSAWetnessProcess.readWBANStation(wbanListPath, countyMap);
        System.out.println(wbanMap.size());
        
    }

    @Test
    public void testCalculateAndSort() throws Exception {

        ArrayList<MSAWetness> calculate = MSAWetnessProcess.calculateAndSort(statesInfoPath, countyMSAPath, wbanListPath,
                percipitationPath);
        System.out.println(calculate.size());
        for (MSAWetness msaWetness : calculate) {
            System.out.println(msaWetness.toString());
        }
    }
}
