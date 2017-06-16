package com.evariant.platform.msawetness;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.evariant.platform.msawetness.model.MSAWetness;
import com.evariant.platform.msawetness.process.MSAWetnessProcess;

/**
 * This Main class is the main class for the MSAWetnessSorter program. It will
 * invoke the MSAWetnessProces class and save the output as result.text file.
 * 
 * @author Peter Zhou
 */

public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {

        if (args.length != 4) {
            logger.error("the program need four text files as argurments");
            throw new IllegalArgumentException();

        }
        logger.info("MSAWetness process start");
        List<MSAWetness> resultList = MSAWetnessProcess.calculateAndSort(args[0], args[1], args[2], args[3]);
        output(resultList);
        logger.info("MSAWetness process end");

    }

    public static void output(List<MSAWetness> resultList) throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter("output.txt", true), true);
        for (MSAWetness msaWetness : resultList) {
            out.write(msaWetness.toString() + "\n");
        }
        out.close();
    }

}
