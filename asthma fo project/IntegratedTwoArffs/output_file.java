
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
	

	// ��X�u��r�ɡv
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
		
					
			// �̾�sort�᪺ valid attributes �ӿ�X
			output();
		

			// �T�{�O�_���o�ӥؿ��s�b
			createDirectory(prediction_model_directionary);
			createDirectory(prediction_model_directionary+"/"+disease_name);
			
			
			// �Y�� disease name �ؿ��s�b�ɡA�Y�w�� intgrated_arff �ɮצb���Y�A�h�����R��!
			deleteFiles(prediction_model_directionary+"/"+disease_name);


			// �N�u��X��ơv�s�J�uPredictionModels��Ƨ��v
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
			
	
			// ��X�u��ƪ�W�١v
			output.write("@relation disease_combined_arff");			// ��X���W��
		      	output.newLine();						// ����    	
		      	output.newLine();						// ����    	
		      	
		      	
		      	for(int i=0;i<attri_lines1.size()-1;i++){
		      		output.write(attri_lines1.elementAt(i));		// ��X���W��
		      		output.newLine();					// ����	
		      	}
		      	
		      	for(int i=0;i<attri_lines2.size();i++){
		      		output.write(attri_lines2.elementAt(i));		// ��X���W��	
		      		output.newLine();					// ����	
		      	}
		      	
		      	
		      	
			// ��X�u����T�v
			/*
		      	for(int i=0;i<valid_attribute.length;i++){		      												
				output.write(AttriLines.elementAt(valid_attribute[i]));		// ��X���W��
		      		output.newLine();						// ����	
		      	}
		      	// �ɤWclass����T
		      	output.write(AttriLines.elementAt(AttriLines.size()-1));		// ��X���W��		      	
		     	output.newLine();						// ����	
		      	*/
		      	
		      			      			      		      	

			output.newLine();					// ����    	
			output.write("@data ");					// ��X���W��
			output.newLine();
			output.newLine();
			
			
			for(int i=0;i<combined_data.length;i++){
				
				String str="";
				
				for(int j=0;j<combined_data[i].length-1;j++){
					str+=combined_data[i][j]+",";
				}
				str+=combined_data[i][combined_data[i].length-1];
				
				output.write(str);				// ��X	
				output.newLine();				// ����										
			}
			
			
			// ������X�y
			output.close();		      	
			
			System.out.println(" ��ƿ�X����!");
			System.out.println("Combined_Arff.arff");
			
	
	
	
	
	
	
		}catch(Exception e){
			System.out.println(" Error about outputing file to the prediction model directionary (in output_file.java): "+e.toString());	
		}		
		
	}



	// ���͸�ƥؿ�
	public void createDirectory(String new_directionary_path){
		
		try{
			

			File make_dir = new File(new_directionary_path);
			
			if(!make_dir.exists()){
				make_dir.mkdirs();
			
				//make_dir.mkdir();
				System.out.println(" �إߤ@�ӷs��Ƨ� ");
				//System.out.println(" �s��Ƨ����|"+data_path+"/new_timeseries_data");		
				System.out.println(" �s��Ƨ����|"+new_directionary_path);
			}


		}catch(Exception e){
			System.out.println(" Error about making the new directory (in output_file.java):"+e.toString());	
		}
		
	}



	// ���͸�ƥؿ�
	public void deleteFiles(String new_directionary_path){
		
		try{
		
			
			File tmp_file = new File(new_directionary_path);
		
			String[] del_file_names = tmp_file.list();
		
			if(del_file_names.length!=0){
				for(int i=0;i<del_file_names.length;i++){
					File del = new File(new_directionary_path+"/"+del_file_names[i]);	
					System.out.println(" �R��:�@"+del_file_names[i]);
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
			
	
			// ��X�u��ƪ�W�١v
			output.write("@relation disease_combined_arff");			// ��X���W��
		      	output.newLine();						// ����    	
		      	output.newLine();						// ����    	
		      	
		      	
		      	for(int i=0;i<attri_lines1.size()-1;i++){
		      		output.write(attri_lines1.elementAt(i));		// ��X���W��
		      		output.newLine();					// ����	
		      	}
		      	
		      	for(int i=0;i<attri_lines2.size();i++){
		      		output.write(attri_lines2.elementAt(i));		// ��X���W��	
		      		output.newLine();					// ����	
		      	}
		      	
		      	
		      	
			// ��X�u����T�v
			/*
		      	for(int i=0;i<valid_attribute.length;i++){		      												
				output.write(AttriLines.elementAt(valid_attribute[i]));		// ��X���W��
		      		output.newLine();						// ����	
		      	}
		      	// �ɤWclass����T
		      	output.write(AttriLines.elementAt(AttriLines.size()-1));		// ��X���W��		      	
		     	output.newLine();						// ����	
		      	*/
		      	
		      			      			      		      	

			output.newLine();					// ����    	
			output.write("@data ");					// ��X���W��
			output.newLine();
			output.newLine();
			
			
			for(int i=0;i<combined_data.length;i++){
				
				String str="";
				
				for(int j=0;j<combined_data[i].length-1;j++){
					str+=combined_data[i][j]+",";
				}
				str+=combined_data[i][combined_data[i].length-1];
				
				output.write(str);				// ��X	
				output.newLine();				// ����										
			}
			
			
			// ������X�y
			output.close();		      	
			
			System.out.println(" ��ƿ�X����!");
			System.out.println("Combined_Arff.arff");
		
		
		
		}catch(Exception e){
			System.out.println(" Error about outputing new arff file (in output_file.java): "+e.toString());	
		}
		
	}
	

	
}



