package Classifier;


import java.io.*;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.Id3;
import weka.core.*;
import java.util.*;


public class MyDecisionTree {


	double accuracy =0d;
	

	MyDecisionTree(String data_path, String training_data_name, String testing_data_name){

		try{


    			FileReader training_reader = new FileReader(data_path+"/"+training_data_name);
    			Instances training_instances = new Instances(training_reader);
    			
    			
    			FileReader testing_reader = new FileReader(data_path+"/"+testing_data_name);
    			Instances testing_instances = new Instances(testing_reader);




			// Make the last attribute be the class
    			training_instances.setClassIndex(training_instances.numAttributes() - 1);
    			testing_instances.setClassIndex(testing_instances.numAttributes() - 1);
    			
    			
    			
    			J48 tree = new J48();
    			tree.buildClassifier(training_instances);
    			
    			/*
    			System.out.println("The third animal is classified as: " + tree.classifyInstance(training_instances.instance(0)));
    			System.out.println(training_instances.classAttribute().value((int)tree.classifyInstance(training_instances.instance(0))));
    			System.out.println("instances.instance(0).classValue() is " + training_instances.instance(0).classValue());
    
    			System.out.println("=============================================================");
    			// instances.instance(1) 指第二筆記錄，因為是從0開始計數
    			System.out.println(training_instances.classAttribute().value((int)tree.classifyInstance(training_instances.instance(1))));
    
    			System.out.println("=============================================================");
			*/



    			double right=0;
    			int total_num=0;
    			//FileWriter wt=new FileWriter("result.data");
    			
    			for(int i=0;i<testing_instances.numInstances();i++)
    			{
				int result=(int)tree.classifyInstance(testing_instances.instance(i));
				//wt.write(""+result+"\n");
				if(result==testing_instances.instance(i).classValue())
	    				right++;
		
				total_num++;
	
		    	}
    			//wt.close();
    			training_reader.close();
    			testing_reader.close();
    
    
    			accuracy = (right/(double)total_num);
    			
    
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public double getAccuracy(){
		return accuracy;	
	}



	public static void main(String args[]){
		new MyDecisionTree(args[0], args[1], args[2]);
	 }
}



