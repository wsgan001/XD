
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
		// ���o��Ʈw���e
	
		String[] attribute;			// ���X�u����ݩʦW�١v		
		String TableName;			// ���X�u��ƦW�١v
		FastVector attri_lines;			// ���X�u���㪺attribute information�v
	
		int data_num;
		double training_data_rate;
		String data_output_folder;
		FastVector data_lines;			// ���X�u���㪺attribute information�v
	
		String training_data_name ="";
		String testing_data_name ="";
		
		
		
		
	public gen_training_data(String data_output_folder, double training_data_rate, String TableName, FastVector attri_lines, FastVector data_lines){
		
		try{
		
			//-------------------------------------------
			// 01. �ѼƳ]�w
			this.data_output_folder	= data_output_folder;
			this.training_data_rate = training_data_rate;
			this.TableName = TableName;
			this.attri_lines = attri_lines;
			this.data_lines = data_lines;
			
			
			
			
			
			//-------------------------------------------
			// 02. ���ͰV�m���
			data_num = (int)(training_data_rate*data_lines.size());
			System.out.println(" training data size: "+data_num);
			gen_training_data();
		
		
		
		
			//-------------------------------------------
			// 03. ���ʹ��ո��
			
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
			
	
			// ��X�u��ƪ��W�١v
			output.write("@relation "+TableName+"_"+training_data_rate);			// ��X���W��
		      	output.newLine();						// ����    	
		      	output.newLine();						// ����    	
		      	
		      	
		      	// ��X�u�ݩʡv
		      	for(int i=0;i<attri_lines.size();i++){
		      		output.write(attri_lines.elementAt(i));			// ��X���W��
		      		output.newLine();					// ����	
		      	}
		      	output.newLine();						// ����    	
		      	
		      	
			// ��X�udata�v			
			output.write("@data ");					// ��X���W��
			output.newLine();
			output.newLine();
			

		
			for(int i=0;i<data_num;i++){										
				output.write(data_lines.elementAt(i));				// ��X	
				output.newLine();					// ����										
			}
			
			
			// ������X�y
			output.close();		      	
			
			System.out.println(" ���� Training data ��X!!!");
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
			
	
			// ��X�u��ƪ��W�١v
			output.write("@relation "+TableName+"_"+testing_data_rate);			// ��X���W��
		      	output.newLine();						// ����    	
		      	output.newLine();						// ����    	
		      	
		      	
		      	// ��X�u�ݩʡv
		      	for(int i=0;i<attri_lines.size();i++){
		      		output.write(attri_lines.elementAt(i));			// ��X���W��
		      		output.newLine();					// ����	
		      	}
		      	output.newLine();						// ����    	
		      	
		      	
			// ��X�udata�v			
			output.write("@data ");					// ��X���W��
			output.newLine();
			output.newLine();
			

		
			for(int i=data_num;i<data_lines.size();i++){										
				output.write(data_lines.elementAt(i));				// ��X	
				output.newLine();					// ����										
			}
			
			
			// ������X�y
			output.close();		      	
			
			System.out.println(" ���� Training data ��X!!!");
			System.out.println( TableName+"_"+testing_data_rate+".arff");
			
			testing_data_name = TableName+"_"+testing_data_rate+".arff";
		
		
		}catch(Exception e){
			System.out.println(" Error about generating the training data (in gen_training_data.java): "+e.toString());	
		}
		
	}

}