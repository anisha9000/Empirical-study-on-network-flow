/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkflowstudy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
/**
 *
 * @author anisha
 */
public class SaveOutput {
    private static final String OUTPUT_CSV_FILE = new File("").getAbsolutePath()
            + "/src/output.csv";

    private static final String OUTPUT_FOLDER =  new File("").getAbsolutePath() 
            + "/src/output/";
    
    public static void writeToCSV(String graphType, String algorithmName, int numberOfVertices, 
            long runningTime, int maxFlow) throws IOException {
        
        FileWriter fileWriter = new FileWriter(OUTPUT_CSV_FILE, true);
        
        String row = graphType + "," + algorithmName + "," + numberOfVertices +
                ","+ runningTime + "," + maxFlow;
        System.out.println(row);
        fileWriter.append(row);
        fileWriter.append("\n");
        fileWriter.flush();
        fileWriter.close();
    }
    
    
}