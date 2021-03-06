package ClassifierModel;

import java.io.*;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.Id3;
import weka.core.*;
import java.util.*;



public class Rank_Results{


	String[] rank_result;


	public Rank_Results(Vector<String> classify_results){
		
		
		try{


			rank_result = new String[classify_results.size()];

			String[] disease_name = new String[classify_results.size()];
			String[] classify_target = new String[classify_results.size()];
			double[] classify_confidence = new double[classify_results.size()];
			
			
			// 拆解「每筆分類的結果」
			for(int i=0;i<classify_results.size();i++){
				
				String[] tmp = classify_results.get(i).split("_");	
				
				disease_name[i] = tmp[0];
				classify_target[i] = tmp[1];
				classify_confidence[i] = Double.parseDouble(tmp[2]);
								
			}
			
			
			
			
			// 使用「氣泡排序法」
			for(int i=0;i<classify_target.length;i++){
				for(int j=i+1;j<classify_target.length;j++){
					
					if(classify_confidence[i]<classify_confidence[j]){
						
						String tmp_disease = disease_name[i];
						String tmp_target =  classify_target[i];
						double tmp_confidence = classify_confidence[i];
						
						
						disease_name[i] = disease_name[j];
						classify_target[i] = classify_target[j];
						classify_confidence[i] = classify_confidence[j];
							
							
						disease_name[j]	= tmp_disease;
						classify_target[j] = tmp_target;
						classify_confidence[j] = tmp_confidence;
														
					}
				}
			}



			for(int i=0;i<disease_name.length;i++){
				System.out.println(" disease name : "+disease_name[i] );					
				System.out.println(" classify_target : "+classify_target[i]);					
				System.out.println(" classify_confidence: "+classify_confidence[i]);					
			}

			System.out.println();
			System.out.println();
			System.out.println("******************************************");

			for(int i=0;i<rank_result.length;i++){
				rank_result[i]="";
				rank_result[i]+=disease_name[i]+"_"+classify_target[i]+"_"+classify_confidence[i];		
				System.out.println(" rank_result["+i+"]="+rank_result[i]);
			}
			System.out.println("******************************************");

		}catch(Exception e){
			System.out.println(" Error about ranking all the prediction results (in Rank_Results.java): "+e.toString());	
		}

	}


//-----------------------------------------------
//-----------------------------------------------
//-----------------------------------------------


	
	public String[] getRankResults(){
		return rank_result;		
	}



//-----------------------------------------------
//-----------------------------------------------
//-----------------------------------------------



}