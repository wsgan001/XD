package Classifier;

import java.io.*;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.Id3;
import weka.core.*;
import java.util.*;


public class MyDecisionTree {


	
	
	int C2_right_num = 0;
    	int C2_wrong_num = 0;
    	int C1C3_right_num = 0;
    	int C1C3_wrong_num = 0;
	
	double accuracy =0d;
	double precise = 0d;
    	double recall = 0d;
    	
	public J48 classifier=null;

	MyDecisionTree(String data_path, String training_data_name, String testing_data_name){

		try{


    			FileReader training_reader = new FileReader(data_path+"/"+training_data_name);
    			Instances training_instances = new Instances(training_reader);
    			
    			
    			FileReader testing_reader = new FileReader(data_path+"/"+testing_data_name);
    			Instances testing_instances = new Instances(testing_reader);




			// Make the last attribute be the class
    			training_instances.setClassIndex(training_instances.numAttributes() - 1);
    			testing_instances.setClassIndex(testing_instances.numAttributes() - 1);
    			
    			
    			
    			classifier = new J48();
    			classifier.buildClassifier(training_instances);
    			
    			//save txt(graph)
    			//ObjectOutputStream oostxt = new ObjectOutputStream(new FileOutputStream(new File(new File(data_path).getParentFile(),"j48.txt")));
 			//oostxt.writeObject(classifier.graph());
 			BufferedWriter bw=new BufferedWriter(new FileWriter( new File(new File(data_path).getParentFile(),"j48.txt") ));
 			bw.write(classifier.graph());
 			bw.close();
    			//oostxt.flush();
 			//oostxt.close();
   
 				
 			//save model
 			ObjectOutputStream oosModel = new ObjectOutputStream(new FileOutputStream(new File(new File(data_path).getParentFile(),"j48.model")));
 			oosModel.writeObject(classifier);
 			oosModel.flush();
 			oosModel.close();
    			
    			/*
    			System.out.println("The third animal is classified as: " + classifier.classifyInstance(training_instances.instance(0)));
    			System.out.println(training_instances.classAttribute().value((int)classifier.classifyInstance(training_instances.instance(0))));
    			System.out.println("instances.instance(0).classValue() is " + training_instances.instance(0).classValue());
    
    			System.out.println("=============================================================");
    			// instances.instance(1) ���ĤG���O���A�]���O�q0�}�l�p��
    			System.out.println(training_instances.classAttribute().value((int)classifier.classifyInstance(training_instances.instance(1))));    			    
    			System.out.println("=============================================================");
			*/
    			System.out.println("ddddddddd" + data_path);
    			/*ObjectInputStream ois=new ObjectInputStream(new FileInputStream(new File(new File(data_path).getParentFile(),"j48.model")));
				classifier=(J48)ois.readObject();
				ois.close();*/

    			double right=0;
    			int total_num=0;
    			
    			System.out.println("debug: test instance");
    			System.out.println("numClasses: " + testing_instances.numClasses());

    			for(int i=0;i<testing_instances.numInstances();i++)
    			{
				int result=(int)classifier.classifyInstance(testing_instances.instance(i));
				double[] result_probabilities = classifier.distributionForInstance(testing_instances.instance(i));
				/*for(int it=0;it<result_probabilities.length;it++)
					System.out.println(result_probabilities[it]);
				System.out.println("");*/
				if(result==testing_instances.instance(i).classValue())
	    				right++;
		
				
				// ����u���d�w������T�v
				if(testing_instances.instance(i).classValue()==1){
					if(result==testing_instances.instance(i).classValue()){
						C2_right_num++;	// a
					}else{
						C2_wrong_num++;	// b
					}
				}
				
				
				// ����u�D���d�w������T�v
				if(testing_instances.instance(i).classValue()==0 || testing_instances.instance(i).classValue()==2 ){
					if(result==testing_instances.instance(i).classValue()){
						C1C3_right_num++;	// d
					}else{
						C1C3_wrong_num++;	// c
					}
				}				
				
				
		
				total_num++;
	
		    	}
    			
    			training_reader.close();
    			testing_reader.close();
    
        
    			accuracy = (right/(double)total_num);
    			
    			//System.out.println(" accuracy = "+accuracy);
    			
    			
			
    			System.out.println("===================================");
    			System.out.println("	�H�u���d�v���D");
    			precise = (double)(C2_right_num)/(double)(C2_right_num+C2_wrong_num);
    			recall = (double)(C2_right_num)/(double)(C2_right_num+C1C3_wrong_num);
    			accuracy = (double)(C2_right_num+C1C3_right_num)/(double)(C2_right_num+C2_wrong_num+C1C3_right_num+C1C3_wrong_num);
    			
    			
    			System.out.println(" Precise = " + precise);
    			System.out.println(" recall = " + recall);
    			//System.out.println(" accuracy = " + accuracy);
    			System.out.println("===================================");
    			
    			
    			
    			System.out.println("===================================");
    			System.out.println("	�H�u�D���d�v���D");
    			precise = (double)(C1C3_right_num)/(double)(C1C3_right_num+C1C3_wrong_num);
    			recall = (double)(C1C3_right_num)/(double)(C1C3_right_num+C2_wrong_num);
    			accuracy = (double)(C2_right_num+C1C3_right_num)/(double)(C2_right_num+C2_wrong_num+C1C3_right_num+C1C3_wrong_num);
    			
    			
    			System.out.println(" Precise = " + precise);
    			System.out.println(" recall = " + recall);
    			//System.out.println(" accuracy = " + accuracy);
    			System.out.println("===================================");
    			    			    			
    
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public double getAccuracy(){	//  return the accuracy value
		return accuracy;	
	}

	public double getPrecision(){	// return the precision value for target class, unhealth
		return precise;	
	}

	public double getRecall(){	// return the recall value for  target class, unhealth
		return recall;	
	}
	
	


	public int getC2RightNum(){		// ����O�u�ӵ��O����class�O"���d", �w���]�O"���d"�v
		return C2_right_num;			
	}

	public int getC2WrongNum(){		// ����O�u�ӵ��O����class�O"���d", �w���o�O"�D���d"�v
		return C2_wrong_num;			
	}

	public int getC1C3RightNum(){		// ����O�u�ӵ��O����class�O"�����d", �w���]�O"�����d"�v
		return C1C3_right_num;			
	}

	public int getC1C3WrongNum(){		// ����O�u�ӵ��O����class�O"�����d", �w���o�O"���d"�v
		return C1C3_wrong_num;			
	}


	public static void main(String args[]){
		new MyDecisionTree(args[0], args[1], args[2]);
	 }
}



