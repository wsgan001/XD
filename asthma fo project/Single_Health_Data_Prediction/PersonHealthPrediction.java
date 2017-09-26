package Single_Health_Data_Prediction;

import java.lang.*;
import java.util.*;
import java.io.*;


public class PersonHealthPrediction{



	String[] input_data_list;		// �O���u ���i��w���� data file list �v
	String[] data_path_list;		// �O���u ���i��w���� data path list �v
	String[] pred_disease_list;		// �O���u ���i��w���� pred_disease_list�v
	
	String[] models_path_list;		// �O���u �ثe�Ҧ����w���Ҳ��ɮ׸��|�M�� �v
	String[] models_name_list;		// �O���u �ثe�Ҧ����w���ҲզW�ٲM��v


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
		// ���ˬdpred_input��Ƨ��̬O�_���s�b�u��ơv
		// �p�G�S�����ܡA�h�פ�{��!!!
		
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
			// �T�w�Ө����̦bpred_input��Ƨ��̪��Ҧ��ɦW
			// �ѡu�ɦW�ӽT�w�n�w�����C�ʯe�f�������v		
			pred_disease_list = cid.getDiseaseList();
			//System.out.println(" pred_disease_list.length = "+pred_disease_list.length);
					
			
			// �T�{�b�upred_input��Ƨ��̬O�_�S�������ɮצs�b�I�v
			if(input_data_list.length==0){			
				return;	
			}
			

		//------------------------------------		
		// 02. 		
		// �T�w�u�ثe�w���Ҳժ��Ҧ������v
		// �H�K�i�o���u���ǺC�ʯe�f�������O�L�k�i��w���v
		
		Check_Prediction_Models	cpm = new Check_Prediction_Models("./PredictionModels");
			models_name_list = cpm.getModelsNameList();
			models_path_list = cpm.getModelsPathList();
		
		
										
		//------------------------------------		
		// 04. 		
		// �i��Ū�J�Ө�����(p1)���ɮ�
		// �i��Ө����̪��U�ӯe�f���w��
		perform_prediction_process(data_path, patient_no);
			
		
		
		
		//------------------------------------		
		// 06.
		// �N�Ҧ��w�������G�A�̾ڥi�H��(Confidence Degree)�i��Ƨ�
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

	public Vector<String> getHealthRiskConfidence(){	// �^�ǡu�|���ƧǪ��C�ӺC�ʯe�f�w�������d���I�H��סv
		return HealthRiskConf;	
	}

	public Vector<String> getHealthRiskName(){		// �^�ǡu�|���ƧǪ��C�ӺC�ʯe�f�W�١v
		return HealthRiskName;	
	}


	public Vector<String> getRankHealthRiskConfidence(){	// �^�ǡu�Ƨǫ᪺�C�ӺC�ʯe�f�w�������d���I�H��סv
		return RankHealthRiskConf;	
	}

	public Vector<String> getRankHealthRiskName(){		// �^�ǡu�Ƨǫ᪺�C�ӺC�ʯe�f�W�١v
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
						
						System.out.println(" �}�l�i�� "+pred_disease_list[i]+" �w��!!!");
					
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
						
						
						
						// �N�u�w�����G�v��ܡuoutput_result�v��
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


	

	// �D��k!!!
	
	public static void main(String[] args){		
		
		if(args.length!=0){
			new PersonHealthPrediction(args[0], args[1]);
		}else{
			new PersonHealthPrediction("./pred_input", "2");
		}	
	}
	
}