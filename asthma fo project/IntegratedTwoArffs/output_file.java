
package IntegratedTwoArffs;


import java.lang.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.io.*;

public class output_file{
	

	String FileName;	
	FastVector attri_lines1;
	FastVector attri_lines2;	
	String TableName1="";
	String TableName2="";
	String[][] combined_data;
	
	
	String prediction_model_directionary="./PredictionModels";
	
	String disease_name="";
	

	// 輸出「文字檔」
	public output_file(String FileName, String TableName1, String TableName2, FastVector attri_lines1, FastVector attri_lines2, String[][] combined_data, String disease_name){
		
		try{
		
		
			this.FileName = FileName;			
			this.TableName1 = TableName1;
			this.TableName2 = TableName2;
			this.attri_lines1 = attri_lines1;
			this.attri_lines2 = attri_lines2;
			this.combined_data = combined_data;
			this.disease_name = disease_name;			
		 	
		
		
		
			System.out.println("=================================");
			System.out.println(" FileName = "+FileName);
			System.out.println(" attri_lines1.size() = "+attri_lines1.size());					
			System.out.println(" attri_lines2.size() = "+attri_lines2.size());
			System.out.println(" combined_data = "+combined_data);
			System.out.println(" TableName1 = "+TableName1);
			System.out.println(" TableName2 = "+TableName2);
			System.out.println(" disease_name = "+this.disease_name);
			System.out.println("=================================");
		
					
			// 依據sort後的 valid attributes 來輸出
			output();
		

			// 確認是否有這個目錄存在
			createDirectory(prediction_model_directionary);
			createDirectory(prediction_model_directionary+"/"+disease_name);
			
			
			// 若有 disease name 目錄存在時，若已有 intgrated_arff 檔案在裡頭，則直接刪除!
			deleteFiles(prediction_model_directionary+"/"+disease_name);


			// 將「整合資料」存入「PredictionModels資料夾」
			output_of_integrated_arff_file_of_prediction_model();



		
		}catch(Exception e){
			System.out.println(" Error about outputing file (in output_file.java): "+e.toString());	
		}
						
	}



	//=========================================================
	//=========================================================
	//=========================================================
	

	public void output_of_integrated_arff_file_of_prediction_model(){
		
		try{
	
			
			BufferedWriter output = new BufferedWriter(new FileWriter(prediction_model_directionary+"/"+disease_name+"/"+disease_name+"_combined_arff.arff"));
			
	
			// 輸出「資料表名稱」
			output.write("@relation disease_combined_arff");			// 輸出欄位名稱
		      	output.newLine();						// 換行    	
		      	output.newLine();						// 換行    	
		      	
		      	
		      	for(int i=0;i<attri_lines1.size()-1;i++){
		      		output.write(attri_lines1.elementAt(i));		// 輸出欄位名稱
		      		output.newLine();					// 換行	
		      	}
		      	
		      	for(int i=0;i<attri_lines2.size();i++){
		      		output.write(attri_lines2.elementAt(i));		// 輸出欄位名稱	
		      		output.newLine();					// 換行	
		      	}
		      	
		      	
		      	
			// 輸出「欄位資訊」
			/*
		      	for(int i=0;i<valid_attribute.length;i++){		      												
				output.write(AttriLines.elementAt(valid_attribute[i]));		// 輸出欄位名稱
		      		output.newLine();						// 換行	
		      	}
		      	// 補上class欄位資訊
		      	output.write(AttriLines.elementAt(AttriLines.size()-1));		// 輸出欄位名稱		      	
		     	output.newLine();						// 換行	
		      	*/
		      	
		      			      			      		      	

			output.newLine();					// 換行    	
			output.write("@data ");					// 輸出欄位名稱
			output.newLine();
			output.newLine();
			
			
			for(int i=0;i<combined_data.length;i++){
				
				String str="";
				
				for(int j=0;j<combined_data[i].length-1;j++){
					str+=combined_data[i][j]+",";
				}
				str+=combined_data[i][combined_data[i].length-1];
				
				output.write(str);				// 輸出	
				output.newLine();				// 換行										
			}
			
			
			// 關閉輸出流
			output.close();		      	
			
			System.out.println(" 資料輸出完成!");
			System.out.println("Combined_Arff.arff");
			
	
	
	
	
	
	
		}catch(Exception e){
			System.out.println(" Error about outputing file to the prediction model directionary (in output_file.java): "+e.toString());	
		}		
		
	}



	// 產生資料目錄
	public void createDirectory(String new_directionary_path){
		
		try{
			

			File make_dir = new File(new_directionary_path);
			
			if(!make_dir.exists()){
				make_dir.mkdirs();
			
				//make_dir.mkdir();
				System.out.println(" 建立一個新資料夾 ");
				//System.out.println(" 新資料夾路徑"+data_path+"/new_timeseries_data");		
				System.out.println(" 新資料夾路徑"+new_directionary_path);
			}


		}catch(Exception e){
			System.out.println(" Error about making the new directory (in output_file.java):"+e.toString());	
		}
		
	}



	// 產生資料目錄
	public void deleteFiles(String new_directionary_path){
		
		try{
		
			
			File tmp_file = new File(new_directionary_path);
		
			String[] del_file_names = tmp_file.list();
		
			if(del_file_names.length!=0){
				for(int i=0;i<del_file_names.length;i++){
					File del = new File(new_directionary_path+"/"+del_file_names[i]);	
					System.out.println(" 刪除:　"+del_file_names[i]);
					del.delete();
				}			
			}
				
					
		}catch(Exception e){
			System.out.println(" Error about deleting the old files in the new directory (in output_file.java):"+e.toString());	
		}		
	}









	public void output(){
		
		try{
	
			BufferedWriter output = new BufferedWriter(new FileWriter(FileName+"//"+disease_name+"_combined_arff.arff"));
			
	
			// 輸出「資料表名稱」
			output.write("@relation disease_combined_arff");			// 輸出欄位名稱
		      	output.newLine();						// 換行    	
		      	output.newLine();						// 換行    	
		      	
		      	
		      	for(int i=0;i<attri_lines1.size()-1;i++){
		      		output.write(attri_lines1.elementAt(i));		// 輸出欄位名稱
		      		output.newLine();					// 換行	
		      	}
		      	
		      	for(int i=0;i<attri_lines2.size();i++){
		      		output.write(attri_lines2.elementAt(i));		// 輸出欄位名稱	
		      		output.newLine();					// 換行	
		      	}
		      	
		      	
		      	
			// 輸出「欄位資訊」
			/*
		      	for(int i=0;i<valid_attribute.length;i++){		      												
				output.write(AttriLines.elementAt(valid_attribute[i]));		// 輸出欄位名稱
		      		output.newLine();						// 換行	
		      	}
		      	// 補上class欄位資訊
		      	output.write(AttriLines.elementAt(AttriLines.size()-1));		// 輸出欄位名稱		      	
		     	output.newLine();						// 換行	
		      	*/
		      	
		      			      			      		      	

			output.newLine();					// 換行    	
			output.write("@data ");					// 輸出欄位名稱
			output.newLine();
			output.newLine();
			
			
			for(int i=0;i<combined_data.length;i++){
				
				String str="";
				
				for(int j=0;j<combined_data[i].length-1;j++){
					str+=combined_data[i][j]+",";
				}
				str+=combined_data[i][combined_data[i].length-1];
				
				output.write(str);				// 輸出	
				output.newLine();				// 換行										
			}
			
			
			// 關閉輸出流
			output.close();		      	
			
			System.out.println(" 資料輸出完成!");
			System.out.println("Combined_Arff.arff");
		
		
		
		}catch(Exception e){
			System.out.println(" Error about outputing new arff file (in output_file.java): "+e.toString());	
		}
		
	}
	

	
}



