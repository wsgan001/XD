package Single_Health_Data_Prediction;


import java.lang.*;
import java.util.*;
import java.io.*;



public class Check_Prediction_Models{


	String models_path = "";
	String[] directionary_list;
	String[] models_path_list;



	public Check_Prediction_Models(String models_path){
		
		try{
		
			// �]�w�Ѽ�
			this.models_path = models_path;

		
			print();		
		
		
			// �T�{�u�bpred_input��Ƨ��̡v���ɦW
			check_models_list();
		
		
						
			// �T�{�b�upred_input��Ƨ��̬O�_�S�������ɮצs�b�I�v
			if(directionary_list.length==0){
				System.out.println(" Please check your prediction models !!! (no models)");
				return;	
			}
		
		
	
			// �q�u PredictionModels ��Ƨ��ɮײM����X���w�����e�f�����v
			check_prediction_disease_models();
	
		
		
		
		}catch(Exception e){
			System.out.println(" Error about checking the prediction models (in Check_Prediction_Models.java):"+e.toString());	
		}
		
	}


//-----------------------------------------------
//-----------------------------------------------
//-----------------------------------------------


	public String[] getModelsNameList(){	// �^�ǡuPredictionModel ��Ƨ��̪��Ҧ��W�ٲM��v
		return 	directionary_list;
	}


	public String[] getModelsPathList(){	// �^�ǡuPredictionMOdel ��Ƨ��̪��Ҧ��W�ٸ��|�M��v
		return models_path_list;
	}


//-----------------------------------------------
//-----------------------------------------------
//-----------------------------------------------

	public void check_prediction_disease_models(){
		
		try{
		
		
			models_path_list = new String[directionary_list.length];
			
			
			for(int i=0;i<directionary_list.length;i++){
				File f  = new File(models_path+"/"+directionary_list[i]+"/"+directionary_list[i]+"_combined_arff.arff");		
				//System.out.println(" Prediction Model Path : "+models_path+"/"+directionary_list[i]+"/Combined_Arff.arff");
				
				
				
				if(!f.exists()){	// �T�w���w���Ҳժ��ɮ׬O�_�s�b!
					System.out.println(" Please check the "+directionary_list[i]+" prediction model!!!");
					return;							
				}
				
				
				//models_path_list[i] = models_path+"/"+directionary_list[i]+"/Combined_Arff.arff";
				//models_path_list[i] = models_path+"/"+directionary_list[i]+"/"+directionary_list[i]+"_combined_arff.arff";		
				models_path_list[i] = models_path+"/"+directionary_list[i]+"/"+directionary_list[i]+".model";
				System.out.println(" models_path_list["+i+"] = " +models_path_list[i]);
				//System.out.println(" the file exist? " + f.exists());
			}
				
	
		
		
				
		}catch(Exception e){
			System.out.println(" Error about checking the prediction models in the prediction model directionary (in Check_Prediction_Models.java): "+e.toString());	
		}			
	}



	public void check_models_list(){
		
		try{
				
			File f = new File(models_path);			

			
						
			if(!f.isDirectory()){	// �T�w�����|�O�_���@�ӡu��Ƨ��v			
				System.out.println(" Please set the correct the data path!!!");
				return;							
			}
			
			// ���o�u�Ӹ�Ƨ��̪��Ҧ����زM��v
			directionary_list =  f.list();
				
			// �C�L��T
			for(int i=0;i<directionary_list.length;i++){
				System.out.println(" Model name["+i+"] = "+directionary_list[i]);				
			}			

		
		}catch(Exception e){
			System.out.println(" Error about checking the files list (in Check_Prediction_Models.java): "+e.toString());	
		}
		
	}



	public void print(){
		
		
		try{
				
			// �C�L�Ѽ�
			System.out.println(" this.models_path = "+this.models_path);	

	
		}catch(Exception e){
			System.out.println(" Error about printing the files list (in Check_Prediction_Models.java): "+e.toString());	
		}
		
	}



}