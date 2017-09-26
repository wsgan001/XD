

package Gen_training_testing_data;


	
import java.lang.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.io.*;

public class SplitArffFile{



	
	static String[] attribute;			// ���X�u����ݩʦW�١v
	static String[][] data;				// ���X�u��Ʈw���e�v
	static String TableName;			// ���X�u��ƦW�١v
	static FastVector attri_lines;			// ���X�u���㪺attribute information�v
	static FastVector data_lines;			// ���X�u���㪺 data information�v
			

	static String[][] combined_data;	// �O���ucombined �� data�v
	
	static String training_data_name="";
	static String testing_data_name="";




	public SplitArffFile(String source_folder, String arff_file_name, double training_data_rate, String output_folder){
	//public static void main(String[] args){
		
		
		try{		
			
			String[] load_data_para = new String[2];
			load_data_para[0] = source_folder;	
			load_data_para[1] = arff_file_name;
			
			
			
			
			
			//load_combined_arff_file cf1 = new load_combined_arff_file(args[0]+"\\"+args[1]);
			load_combined_arff_file cf1 = new load_combined_arff_file(load_data_para[0]+"\\"+load_data_para[1]);
				attribute = cf1.getAttribute();
				data = cf1.getData();
				TableName = cf1.getTableName();
				attri_lines = cf1.getAttriLines();	// �O���uall the attribute information �v
				data_lines = cf1.getDataLines();	// �O���uall the data information�v
			
			
			
			
			
			System.out.println(" attribute = "+attribute.length);
			System.out.println(" data = "+data.length);
			System.out.println(" TableName = "+TableName);
			System.out.println(" attri_lines = "+attri_lines.size());





			//String data_output_folder = args[0];
			//double training_data_rate = Double.parseDouble(args[2]);			
			
			String data_output_folder = output_folder;
			
			
			
			
			
			
			System.out.println(" data_output_folder = "+data_output_folder);
				gen_training_data gtd = new gen_training_data(data_output_folder, training_data_rate, TableName, attri_lines, data_lines);
					training_data_name = gtd.getTrainingDataName();
					testing_data_name = gtd.getTestingDataName();
					
					
					System.out.println(" gtd.getTrainingDataName() = "+gtd.getTrainingDataName());
					System.out.println(" gtd.getTestingDataName() = "+gtd.getTestingDataName());
				


		
		}catch(Exception e){
			System.out.println(" Error in main function ( in SplitArffFile.java): "+e.toString());	
		}		
	}
	
	
	
	
	
	public String getTrainingDataName(){
		return  training_data_name;	
	}
	
	public String getTestingDataName(){
		return  testing_data_name;	
	}
	
	
	
	
	/*
	public static String getTrainingDataName(){
		return  training_data_name;	
	}
	
	public static String getTestingDataName(){
		return  testing_data_name;	
	}
	*/
	
}