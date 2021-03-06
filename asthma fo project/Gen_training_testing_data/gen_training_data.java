
package Gen_training_testing_data;


import java.lang.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.io.*;



public class gen_training_data{


		
  		String line;
  		
	
		//------------------------------
		// 取得資料庫內容
	
		String[] attribute;			// 取出「欄位屬性名稱」		
		String TableName;			// 取出「資料名稱」
		FastVector attri_lines;			// 取出「完整的attribute information」
	
		int data_num;
		double training_data_rate;
		String data_output_folder;
		FastVector data_lines;			// 取出「完整的attribute information」
	
		String training_data_name ="";
		String testing_data_name ="";
		
		
		
		
	public gen_training_data(String data_output_folder, double training_data_rate, String TableName, FastVector attri_lines, FastVector data_lines){
		
		try{
		
			//-------------------------------------------
			// 01. 參數設定
			this.data_output_folder	= data_output_folder;
			this.training_data_rate = training_data_rate;
			this.TableName = TableName;
			this.attri_lines = attri_lines;
			this.data_lines = data_lines;
			
			
			
			
			
			//-------------------------------------------
			// 02. 產生訓練資料
			data_num = (int)(training_data_rate*data_lines.size());
			System.out.println(" training data size: "+data_num);
			gen_training_data();
		
		
		
		
			//-------------------------------------------
			// 03. 產生測試資料
			
			System.out.println(" testing data size: "+(data_lines.size()-data_num));			
			gen_testing_data();
		
		
		
		
		}catch(Exception e){
			System.out.println(" Error about generating the training data (in gen_training_data.java): "+e.toString());	
		}
		
		
	}



//--------------------------------------------------------------
//--------------------------------------------------------------
//--------------------------------------------------------------


	public String getTrainingDataName(){		
		return training_data_name;					
	}
	
	public String getTestingDataName(){		
		return testing_data_name;					
	}
			
		

//--------------------------------------------------------------
//--------------------------------------------------------------
//--------------------------------------------------------------




	public void gen_training_data(){
		
		try{





			BufferedWriter output = new BufferedWriter(new FileWriter(data_output_folder+"\\"+TableName+"_"+training_data_rate+".arff"));
			
	
			// 輸出「資料表名稱」
			output.write("@relation "+TableName+"_"+training_data_rate);			// 輸出欄位名稱
		      	output.newLine();						// 換行    	
		      	output.newLine();						// 換行    	
		      	
		      	
		      	// 輸出「屬性」
		      	for(int i=0;i<attri_lines.size();i++){
		      		output.write(attri_lines.elementAt(i));			// 輸出欄位名稱
		      		output.newLine();					// 換行	
		      	}
		      	output.newLine();						// 換行    	
		      	
		      	
			// 輸出「data」			
			output.write("@data ");					// 輸出欄位名稱
			output.newLine();
			output.newLine();
			

		
			for(int i=0;i<data_num;i++){										
				output.write(data_lines.elementAt(i));				// 輸出	
				output.newLine();					// 換行										
			}
			
			
			// 關閉輸出流
			output.close();		      	
			
			System.out.println(" 完成 Training data 輸出!!!");
			System.out.println( TableName+"_"+training_data_rate+".arff");
		 	training_data_name = TableName+"_"+training_data_rate+".arff";
		
		
		}catch(Exception e){
			System.out.println(" Error about generating the training data (in gen_training_data.java): "+e.toString());	
		}
		
	}

	public void gen_testing_data(){
		
		try{

			int tmp_rate =(int)( (1-training_data_rate)*(double)1000);
			double testing_data_rate = (double)tmp_rate/(double)1000;
			


			BufferedWriter output = new BufferedWriter(new FileWriter(data_output_folder+"\\"+TableName+"_"+(testing_data_rate)+".arff"));
			
	
			// 輸出「資料表名稱」
			output.write("@relation "+TableName+"_"+testing_data_rate);			// 輸出欄位名稱
		      	output.newLine();						// 換行    	
		      	output.newLine();						// 換行    	
		      	
		      	
		      	// 輸出「屬性」
		      	for(int i=0;i<attri_lines.size();i++){
		      		output.write(attri_lines.elementAt(i));			// 輸出欄位名稱
		      		output.newLine();					// 換行	
		      	}
		      	output.newLine();						// 換行    	
		      	
		      	
			// 輸出「data」			
			output.write("@data ");					// 輸出欄位名稱
			output.newLine();
			output.newLine();
			

		
			for(int i=data_num;i<data_lines.size();i++){										
				output.write(data_lines.elementAt(i));				// 輸出	
				output.newLine();					// 換行										
			}
			
			
			// 關閉輸出流
			output.close();		      	
			
			System.out.println(" 完成 Training data 輸出!!!");
			System.out.println( TableName+"_"+testing_data_rate+".arff");
			
			testing_data_name = TableName+"_"+testing_data_rate+".arff";
		
		
		}catch(Exception e){
			System.out.println(" Error about generating the training data (in gen_training_data.java): "+e.toString());	
		}
		
	}

}