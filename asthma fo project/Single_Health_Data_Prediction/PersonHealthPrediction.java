package Single_Health_Data_Prediction;

import java.lang.*;
import java.util.*;
import java.io.*;


public class PersonHealthPrediction{



	String[] input_data_list;		// 記錄「 欲進行預測的 data file list 」
	String[] data_path_list;		// 記錄「 欲進行預測的 data path list 」
	String[] pred_disease_list;		// 記錄「 欲進行預測的 pred_disease_list」
	
	String[] models_path_list;		// 記錄「 目前所有的預測模組檔案路徑清單 」
	String[] models_name_list;		// 記錄「 目前所有的預測模組名稱清單」


	Vector<String> output_result = new Vector<String>();
	public String[] Rank_Results;





	Vector<String> HealthRiskConf  = new Vector<String>();
	Vector<String> HealthRiskName  = new Vector<String>();

	public Vector<String> RankHealthRiskConf  = new Vector<String>();
	public Vector<String> RankHealthRiskName  = new Vector<String>();


	public PersonHealthPrediction(String data_path, String patient_no){
		
		
		System.out.println(" data_path = " + data_path);
		
		
		//------------------------------------		
		// 01. 
		// 先檢查pred_input資料夾裡是否有存在「資料」
		// 如果沒有的話，則終止程式!!!
		
		Check_Input_Data cid = new Check_Input_Data(data_path, patient_no);			
			input_data_list = cid.getDataFileList();
			data_path_list = cid.getDataPathList();								
			
			/*
			for(int i=0;i<input_data_list.length;i++){
				System.out.println(" input_data_list = "+input_data_list[i]);	
			}
			*/
			
			//------------------------------------		
			// 01-01. 
			// 確定該受測者在pred_input資料夾裡的所有檔名
			// 由「檔名來確定要預測的慢性疾病的種類」		
			pred_disease_list = cid.getDiseaseList();
			//System.out.println(" pred_disease_list.length = "+pred_disease_list.length);
					
			
			// 確認在「pred_input資料夾裡是否沒有任何檔案存在！」
			if(input_data_list.length==0){			
				return;	
			}
			

		//------------------------------------		
		// 02. 		
		// 確定「目前預測模組的所有種類」
		// 以便可得知「哪些慢性疾病的種類是無法進行預測」
		
		Check_Prediction_Models	cpm = new Check_Prediction_Models("./PredictionModels");
			models_name_list = cpm.getModelsNameList();
			models_path_list = cpm.getModelsPathList();
		
		
										
		//------------------------------------		
		// 04. 		
		// 進行讀入該受測者(p1)的檔案
		// 進行該受測者的各個疾病的預測
		perform_prediction_process(data_path, patient_no);
			
		
		
		
		//------------------------------------		
		// 06.
		// 將所有預測的結果，依據可信度(Confidence Degree)進行排序
		Rank_Results r2 = new Rank_Results(output_result);
			Rank_Results = r2.getRankResults();
			RankHealthRiskConf  = r2.getRankHealthRiskConf();
			RankHealthRiskName  = r2.getRankHealthRiskName();

		
		
		for(int i=0;i<HealthRiskName.size();i++){
			System.out.println(" health name ["+i+"] = "+HealthRiskName.get(i));	
			System.out.println(" health risk confidence ["+i+"] = "+HealthRiskConf.get(i));
		}
		
		for(int i=0;i<output_result.size();i++)
			System.out.println("OR"+i+":"+output_result.get(i));
				
		for(int i=0;i<Rank_Results.length;i++)		
			System.out.println("RR"+i+":"+Rank_Results[i]);
		
//		for(int i=0;i<RankHealthRiskConf.size();i++)
//		{
//			System.out.println(RankHealthRiskName.get(i)+":"+RankHealthRiskConf.get(i));
//		}
		
		
	}



//-----------------------------------------------
//-----------------------------------------------
//-----------------------------------------------

	public String[] getRankResults(){		
		return Rank_Results;	
	}

	public Vector<String> getHealthRiskConfidence(){	// 回傳「尚未排序的每個慢性疾病預測的健康風險信賴度」
		return HealthRiskConf;	
	}

	public Vector<String> getHealthRiskName(){		// 回傳「尚未排序的每個慢性疾病名稱」
		return HealthRiskName;	
	}


	public Vector<String> getRankHealthRiskConfidence(){	// 回傳「排序後的每個慢性疾病預測的健康風險信賴度」
		return RankHealthRiskConf;	
	}

	public Vector<String> getRankHealthRiskName(){		// 回傳「排序後的每個慢性疾病名稱」
		return RankHealthRiskName;	
	}

	
//-----------------------------------------------
//-----------------------------------------------
//-----------------------------------------------



	public void perform_prediction_process(String data_path, String patient_no){
		
		try{	
					
			//System.out.println(" pred_disease_list.length = "+pred_disease_list.length);
			for(int i=0;i<pred_disease_list.length;i++){
				
				//System.out.println("pred_disease_list[i]="+pred_disease_list[i]+"!");

			
				for(int j=0;j<models_name_list.length;j++){
					//System.out.println("models_name_list[j]="+models_name_list[j]+"!");
					
					if(pred_disease_list[i].compareTo(models_name_list[j])==0){								
						
						System.out.println(" 開始進行 "+pred_disease_list[i]+" 預測!!!");
					
						String[] para = new String[2];
						para[0] = models_path_list[j]; 
						//para[1] = data_path_list[i];
						para[1] = data_path+"/"+patient_no+"_"+ models_name_list[j]+".arff";
						
						System.out.println("regenerate model....");
						PerformPredictionProcess p3 = new PerformPredictionProcess(para[0], para[1], models_name_list[j]);
							
						
						System.out.println("get prediction result....");
					 	String result_str = "";
					 	//result_str += pred_disease_list[i]+"_"+p3.getClassifyResult()+"_"+p3.getClassifyConfidence();
					 	result_str += pred_disease_list[i]+"_"+p3.getPredictHealthRiskConfidence();
						
						
						
						HealthRiskConf.add(String.valueOf(p3.getPredictHealthRiskConfidence()));
						HealthRiskName.add(pred_disease_list[i]);
						
						
						
						// 將「預測結果」放至「output_result」裡
						output_result.add(result_str);												
												
						p3 = null;
						
						break;		
					}
				}
						
			}
						
		
		
		}catch(Exception e){
			System.out.println(" Error about performing the prediction process (in PresonHealthPrediction.java): "+e.toString());	
		}
	}


	

	// 主方法!!!
	
	public static void main(String[] args){		
		
		if(args.length!=0){
			new PersonHealthPrediction(args[0], args[1]);
		}else{
			new PersonHealthPrediction("./pred_input", "2");
		}	
	}
	
}