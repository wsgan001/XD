package Classifier;


import java.lang.*;
import java.util.*;
import java.io.*;


public class del_files{

	String data_path="";

	public del_files(String data_path){
		
		try{
			
			this.data_path = data_path;
			
			
			createDirectory();
			deleteFiles();
			
			
		}catch(Exception e){
			System.out.println(" Error about deleting the files in the assigned floder (in del_files.java): "+e.toString());	
		}
	}





	// ���͸�ƥؿ�
	public void createDirectory(){
		
		try{
			
			//File make_dir = new File(data_path+"/new_timeseries_data");	
			File make_dir = new File(data_path);
			
			if(!make_dir.exists()){
				make_dir.mkdirs();
			
				//make_dir.mkdir();
				System.out.println(" �إߤ@�ӷs��Ƨ� ");
				//System.out.println(" �s��Ƨ����|"+data_path+"/new_timeseries_data");		
				System.out.println(" �s��Ƨ����|"+data_path);
			}


		}catch(Exception e){
			System.out.println(" Error about making the new directory (in del_files.java):"+e.toString());	
		}
		
	}

	// ���͸�ƥؿ�
	public void deleteFiles(){
		
		try{
		
			//File tmp_file = new File(data_path+"/new_timeseries_data");	
			File tmp_file = new File(data_path);
		
			String[] del_file_names = tmp_file.list();
		
			if(del_file_names.length!=0){
				for(int i=0;i<del_file_names.length;i++){
					File del = new File(data_path+"/"+del_file_names[i]);	
					System.out.println(" �R��:�@"+del_file_names[i]);
					del.delete();
				}			
			}
				
					
		}catch(Exception e){
			System.out.println(" Error about deleting the old files in the new directory (in del_files.java):"+e.toString());	
		}		
	}
	
}