package ClassifierModel;

import java.io.*;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.Id3;
import weka.core.*;
import java.util.*;



public class PerformPredictionProcess {
	
	
	
	String training_data_path ="";
	String testing_data_path ="";
	
	
	String classify_result="";
	double classify_confidence = 0;
	String disease_name="";
	
	
	
	public PerformPredictionProcess(String training_data_path, String testing_data_path, String disease_name){
		
		
		try{
		
			this.training_data_path = training_data_path;
			this.testing_data_path = testing_data_path;
			this.disease_name = disease_name;
			
			System.out.println(" training data path : "+training_data_path);
			System.out.println(" testing data path : "+testing_data_path);	
			System.out.println(" disease_name : "+this.disease_name);	
	
	
			// 設定路徑 與 資料讀取			
		    	FileReader reader_training_data = new FileReader(training_data_path);
    			Instances training_instances = new Instances(reader_training_data);
    			
    			FileReader reader_testing_data = new FileReader(testing_data_path);
    			Instances testing_instances = new Instances(reader_testing_data);






			// Make the last attribute be the class
    			training_instances.setClassIndex(training_instances.numAttributes() - 1);    			    			    			
    			testing_instances.setClassIndex(testing_instances.numAttributes() - 1);
    			
    			
    			
    			
    			J48 pred_model = new J48();
    			pred_model.buildClassifier(training_instances);
    			    			    			
    			
    			String pred_class_index = String.valueOf(pred_model.classifyInstance(testing_instances.instance(0)));
    			String pred_class_name = testing_instances.classAttribute().value((int)pred_model.classifyInstance(testing_instances.instance(0)));
    			
    			
    			System.out.println("The disease is classified as: " + pred_model.classifyInstance(testing_instances.instance(0)));
    			System.out.println("The disease is classified as: " + testing_instances.classAttribute().value((int)pred_model.classifyInstance(testing_instances.instance(0))));
			
			
			//System.out.println("The answer is : " + testing_instances.instance(0).classValue());
			//System.out.println("The answer is : " + testing_instances.classAttribute().value((int)testing_instances.instance(0).classValue()));
			System.out.println(" The original answer value of this record is : " + testing_instances.instance(0).classValue());
			System.out.println(" The original answer name of this record is :  " + testing_instances.classAttribute().value((int)testing_instances.instance(0).classValue()));

			
			
	    		// 列印出「該筆記錄對於每個 target class 的可信度」
	    		// 這筆資料至classifier tree裡，對每一個target class的信賴度 (confidence)
	    		double [] distributionForInstance = pred_model.distributionForInstance(testing_instances.instance(0));
	    		double max_distribution =0;
	    		int max_distribution_index =0;
	    		
    			for(int j=0;j<distributionForInstance.length;j++){
    				
    				if(max_distribution<distributionForInstance[j]){
    					max_distribution=distributionForInstance[j];
    					max_distribution_index = j;
    				}
    					
    				System.out.println("distributionForInstance["+j+"] ="+distributionForInstance[j]);	
    				
   			}System.out.println();
  
  
  
  
  
  
  
  
  			classify_result =  testing_instances.classAttribute().value((int)pred_model.classifyInstance(testing_instances.instance(0)));
  			classify_confidence = distributionForInstance[max_distribution_index];
    			
  
  			System.out.println("*************************************************");
  			System.out.println("*************************************************");
			System.out.println("*************************************************");
			System.out.println(" Disease Name: "+disease_name);
			System.out.println(" The known answer name = " + testing_instances.classAttribute().value((int)testing_instances.instance(0).classValue()));
  			System.out.println(" classify_result = "+classify_result);
  			System.out.println(" classify_confidence = "+classify_confidence);
  			System.out.println("*************************************************");
  			System.out.println("*************************************************");
			System.out.println("*************************************************");
  			
  			
  			
                
		} catch(Exception ex){
			System.out.println(" Error aobut performing the predicton process (in PerformPredictionProcess.java)"+ex.toString());
		}
	}
	


//-----------------------------------------------
//-----------------------------------------------
//-----------------------------------------------



	public String getClassifyResult(){
		return classify_result;	
	}

	public double getClassifyConfidence(){
		return classify_confidence;
	}
	

//-----------------------------------------------
//-----------------------------------------------
//-----------------------------------------------



	
	

	public static void main(String args[]){
		
		new PerformPredictionProcess(args[0], args[1], args[2]);
		
	 }
}



