package Classifier;


import java.lang.*;
import java.util.*;
import java.io.*;

public class Load_File{


	String input_data_path="";
	
 	String line;
  	BufferedReader br;	

	





	public Load_File(String input_data_path){
		
		try{
		
			this.input_data_path=input_data_path;
			load_combined_arff_file();
		
		
		
		
		}catch(Exception e){
			System.out.println(" Error about loading the combin_arff.arff file (in Load_File.java): "+e.toString());	
		}
		
	}


	public void load_combined_arff_file(){
		
		try{	
		
			br = new BufferedReader(new FileReader(input_data_path));


         		while ((line=br.readLine())!=null){
 				
 				//String[] temp_info = line.split(",");
 				
 			
 			}
 				
 			br.close();
 			
 					
			
		
		
		
		}catch(Exception e){
			System.out.println(" Error about loading the combin_arff.arff file (in Load_File.java): "+e.toString());	
		}
		
	}


}