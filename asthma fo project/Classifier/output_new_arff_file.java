package Classifier;


import java.lang.*;
import java.util.*;
import java.sql.*;
import java.io.*;


public class output_new_arff_file{


		
	String TableName;			// 取出「資料名稱」
	FastVector attri_lines;			// 取出「完整的attribute information」
	FastVector data_lines;			// 取出「完整的 data information」				
	


	
	Vector<String> data;


	String output_data_path="";
	String output_data_name="";




	
	public output_new_arff_file(String output_data_path, String output_data_name, String TableName,FastVector attri_lines,Vector<String> data){
		
		try{
		
			// 設定初始值
			this.output_data_path = output_data_path;
			this.output_data_name = output_data_name;
			this.TableName = TableName;
			this.attri_lines = attri_lines;
			this.data = data;
			
		
			/*
			System.out.println(" output_data_path = "+this.output_data_path);
			System.out.println(" output_data_name = "+this.output_data_name);
			System.out.println(" TableName = "+this.TableName);
			System.out.println(" attri_lines.size() = "+this.attri_lines.size());			
			System.out.println(" data.size() = "+this.data.size());
			*/			
		
			output_new_arff_file();
		
		
		}catch(Exception e){
			System.out.println(" Error about outputing the new arff file (in output_new_arff_file.java): "+e.toString());	
		}
						
	}


	public void output_new_arff_file(){
		
		try{
				
				
				BufferedWriter bw = new BufferedWriter(new FileWriter(output_data_path+"/"+output_data_name));
		
		
				//輸出arff檔名								
				bw.write("@relation "+TableName);
 				bw.newLine(); 
 				bw.newLine(); 
 				bw.newLine(); 
		
				
				//輸出屬性名稱
				for(int i=0;i<attri_lines.size();i++){
					bw.write(attri_lines.elementAt(i));
					bw.newLine(); 					
				}
		
 				bw.newLine(); 
 				bw.write("@data"); 
  				bw.newLine(); 
  				bw.newLine(); 				
  				
 				
 				// 輸出data部份			
 				for(int i=0;i<data.size();i++){
					bw.write(data.get(i));
					bw.newLine(); 					
				} 				
 				
 				
 				bw.close();					
		

		}catch(Exception e){
			System.out.println(" Error about outputing the training data (in output_new_arff_file.java): "+e.toString());	
		}
		
	}	





}