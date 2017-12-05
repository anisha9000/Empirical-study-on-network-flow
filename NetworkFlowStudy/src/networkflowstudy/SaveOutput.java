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
//import apache.commons.csv.CSVFormat;
//import apache.commons.csv.CSVPrinter;
/**
 *
 * @author anisha
 */
public class SaveOutput {
    private static final String OUTPUT_CSV_FILE = new File("").getAbsolutePath()
            + "/output_capacity.csv";
    public static void writeToCSV(String graphType, String algorithmName, int numberOfVertices, 
            long runningTime, int maxFlow) throws IOException {
        
        FileWriter fileWriter = new FileWriter(OUTPUT_CSV_FILE, true);
        
        String row = graphType + "," + algorithmName + "," + numberOfVertices +
                ","+ runningTime + "," + maxFlow;
        //System.out.println(row);
        fileWriter.append(row);
        fileWriter.append("\n");
        fileWriter.flush();
        fileWriter.close();
    }
    
    public static void writeToCSV(String graphType, String algorithmName, int numberOfVertices, 
            long runningTime, int maxFlow, int capacity) throws IOException {
        
        FileWriter fileWriter = new FileWriter(OUTPUT_CSV_FILE, true);
        
        String row = graphType + "," + algorithmName + "," + numberOfVertices +
                ","+ runningTime + "," + maxFlow+ "," + capacity;
        //System.out.println(row);
        fileWriter.append(row);
        fileWriter.append("\n");
        fileWriter.flush();
        fileWriter.close();
    }
    
    
}
