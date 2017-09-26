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
		
			// 設定參數
			this.models_path = models_path;

		
			print();		
		
		
			// 確認「在pred_input資料夾裡」的檔名
			check_models_list();
		
		
						
			// 確認在「pred_input資料夾裡是否沒有任何檔案存在！」
			if(directionary_list.length==0){
				System.out.println(" Please check your prediction models !!! (no models)");
				return;	
			}
		
		
	
			// 從「 PredictionModels 資料夾檔案清單取出欲預測的疾病種類」
			check_prediction_disease_models();
	
		
		
		
		}catch(Exception e){
			System.out.println(" Error about checking the prediction models (in Check_Prediction_Models.java):"+e.toString());	
		}
		
	}


//-----------------------------------------------
//-----------------------------------------------
//-----------------------------------------------


	public String[] getModelsNameList(){	// 回傳「PredictionModel 資料夾裡的所有名稱清單」
		return 	directionary_list;
	}


	public String[] getModelsPathList(){	// 回傳「PredictionMOdel 資料夾裡的所有名稱路徑清單」
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
				
				
				
				if(!f.exists()){	// 確定此預測模組的檔案是否存在!
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

			
						
			if(!f.isDirectory()){	// 確定此路徑是否為一個「資料夾」			
				System.out.println(" Please set the correct the data path!!!");
				return;							
			}
			
			// 取得「該資料夾裡的所有項目清單」
			directionary_list =  f.list();
				
			// 列印資訊
			for(int i=0;i<directionary_list.length;i++){
				System.out.println(" Model name["+i+"] = "+directionary_list[i]);				
			}			

		
		}catch(Exception e){
			System.out.println(" Error about checking the files list (in Check_Prediction_Models.java): "+e.toString());	
		}
		
	}



	public void print(){
		
		
		try{
				
			// 列印參數
			System.out.println(" this.models_path = "+this.models_path);	

	
		}catch(Exception e){
			System.out.println(" Error about printing the files list (in Check_Prediction_Models.java): "+e.toString());	
		}
		
	}



}