package Classifier;


import java.lang.*;
import java.util.*;
import java.io.*;
import weka.classifiers.trees.J48;


public class ClassifierMechanism{

	
	
	String[] attribute;			// ���X�u����ݩʦW�١v
	String[][] data;				// ���X�u��Ʈw���e�v
	String TableName;			// ���X�u��ƦW�١v
	FastVector attri_lines;			// ���X�u���㪺attribute information�v
	FastVector data_lines;			// ���X�u���㪺 data information�v
	FastVector dbData;			
	


	Vector<String> health_data = new Vector<String>();			
	
	
	Vector<String> balance_training_data = new Vector<String>();
	Vector<String> unbalance_training_data = new Vector<String>();
	Vector<String> unbalance_testing_data = new Vector<String>();
	//Vector<String> balance_testing_data = new Vector<String>();
	
	
	// unbalance training data information
	double mt_accuracy = 0d;
	double mt_precision = 0d;
	double mt_recall = 0d;
	
	// balance training data information
	double mt2_accuracy = 0d;
	double mt2_precision = 0d;	
	double mt2_recall = 0d;
	
	private Vector<String> health_label_set;
	private Vector<String> unhealth_label_set;
	public J48 classifier=null;
	
	public ClassifierMechanism(String input_data_path, String input_data_name, double training_rate){
		Vector<String> hls=new Vector<String>();
		hls.add("C2");
		Vector<String> uhls=new Vector<String>();
		uhls.add("C1");
		uhls.add("C3");
		initial(input_data_path,input_data_name, training_rate);
	}

	public ClassifierMechanism(String input_data_path, String input_data_name, double training_rate,Vector<String> hls,Vector<String> uhls)
	{
		health_label_set=hls;
		unhealth_label_set=uhls;
		initial(input_data_path,input_data_name, training_rate);
	}
	
	private void initial(String input_data_path, String input_data_name, double training_rate){
		
		try{
	
			System.out.println(" input_data_path = "+input_data_path);
			
			
			// 01.
			// Ū�J�uCombined_Arff.arff�v��
			
			load_combined_arff_file cf1 = new load_combined_arff_file(input_data_path+"/"+input_data_name);
				attribute = cf1.getAttribute();
				data = cf1.getData();
				TableName = cf1.getTableName();
				attri_lines = cf1.getAttriLines();	// �O���uall the attribute information �v
				data_lines = cf1.getDataLines();	// �O���uall the data information�v
				dbData = cf1.dbData;
			
			
			System.out.println(" attribute = "+attribute.length);
			System.out.println(" data = "+data.length);
			System.out.println(" TableName = "+TableName);
			System.out.println(" attri_lines = "+attri_lines.size());

				
				
				
			
			// 02.
			// �N�uCombined_Arff.arff�v�ɤ��Φ��u���d�M�D���d�v
			// data:�w�g�H�u�r���v�ϧO
			// dbData:�O�O�d�uarff�ɸ̪��浧�O�����e�v
			divid_data_into_groups ddig = new divid_data_into_groups(data, dbData, training_rate,health_label_set, unhealth_label_set);
				balance_training_data = ddig.getBalanceTrainingData();
				unbalance_training_data = ddig.getUnbalanceTrainingData();
				unbalance_testing_data = ddig.getTestingData();
				//balance_testing_data  = ddig.getBalanceTestingData();
				
				System.out.println(" unbalance_training_data.size() = "+unbalance_training_data.size());
				System.out.println(" balance_training_data.size() = "+balance_training_data.size());
				System.out.println(" unbalance testing_data.size() = "+unbalance_testing_data.size());
				//System.out.println(" balance testing_data.size() = "+balance_testing_data.size());
			
			
			System.out.println(" input_data_path = "+input_data_path);
			
			// �إߡu��X�ؿ���Ƨ��v
			del_files df = new del_files(input_data_path+"/"+"output_data");
			
			
			
			// 03.
			// ���ͷs�� training arff��
			String[] para_training = new String[2];
			//para_training[0] = "./output_data";			
			para_training[0] = input_data_path+"/"+"output_data";
			para_training[1] =  "unbalance_training_"+input_data_name;
			output_new_arff_file onaf1 = new output_new_arff_file(para_training[0], para_training[1], TableName, attri_lines, unbalance_training_data);			
			
			System.out.println(" finish generating the training data!!! ");
			
			
			// 04.
			// ���ͷs�� unbalance testing arff��
			String[] para_training2 = new String[2];
			//para_training2[0] = "./output_data";			
			para_training2[0] = input_data_path+"/"+"output_data";		
			para_training2[1] = "balance_training_"+input_data_name;
			output_new_arff_file onaf2 = new output_new_arff_file(para_training2[0], para_training2[1], TableName, attri_lines, balance_training_data);
			
			
			System.out.println(" finish generating the balance training data!!! ");
			
			
			// 04.
			// ���ͷs�� balance testing arff��
			String[] para_testing = new String[2];
			//para_testing[0] = "./output_data";			
			para_testing[0] = input_data_path+"/"+"output_data";			
			para_testing[1] = "unbalance_testing_"+input_data_name;
			output_new_arff_file onaf3 = new output_new_arff_file(para_testing[0], para_testing[1], TableName, attri_lines, unbalance_testing_data);
			
			
			
			System.out.println(" finish generating the unbalance testing data!!! ");
			
			
			
			
			
			
			
			// 05.
			// Ū�J�uunbalance_training_data.arff�v�H�غc�u�w���Ҳաv�öi��u�w���{�ǡv
			// �̫�A�H�D�o�u�w���ǽT�׵��G�v
			
			String[] para_dt = new String[3];
			//para_dt[0] = "./output_data";			
			para_dt[0] = input_data_path+"/"+"output_data";		
			para_dt[1] = "unbalance_training_"+input_data_name;
			para_dt[2] = "unbalance_testing_"+input_data_name;
			MyDecisionTree  mt = new MyDecisionTree(para_dt[0], para_dt[1], para_dt[2]);
				mt_accuracy = mt.getAccuracy();
				mt_precision = mt.getPrecision();
				mt_recall = mt.getRecall();
				
			
			
    			System.out.println("************************************************");
    			System.out.println(" Unbalance Training/ Unbalance Testing data ( for target class unhealth)");
    			System.out.println( " Accuracy  = "+mt.getAccuracy());
    			System.out.println( " Precision = "+mt.getPrecision());
    			System.out.println( " Recall = "+mt.getRecall());
    			System.out.println("************************************************");
          			
			
			
			// 06.
			// �ubalance_training_data.arff�v
			// ���ͷs�� testing arff��			
			String[] para_dt2 = new String[3];
			//para_dt2[0] = "./output_data";			
			para_dt2[0] = input_data_path+"/"+"output_data";		
			para_dt2[1] = "balance_training_"+input_data_name;
			para_dt2[2] = "unbalance_testing_"+input_data_name;
			MyDecisionTree  mt2 = new MyDecisionTree(para_dt2[0], para_dt2[1], para_dt2[2]);			
				mt2_accuracy = mt2.getAccuracy();
				mt2_precision = mt2.getPrecision();
				mt2_recall = mt2.getRecall();
			
			this.classifier=mt2.classifier;	
			
     			System.out.println("************************************************");
    			System.out.println(" Balance Training/ Unbalance Testing data ( for target class unhealth)");
    			System.out.println( " Accuracy Rate = "+mt2.getAccuracy());
    			System.out.println( " Precision = "+mt2.getPrecision());
    			System.out.println( " Recall = "+mt2.getRecall());
    			System.out.println("************************************************");
			
		}catch(Exception e){
			System.out.println(" Error about loading the file (in Balance.java): "+e.toString());
		}
	}

	//------------------------------------------
	//------------------------------------------
	//------------------------------------------
	
	public double getBalanceAccuracy(){	// return the accuracy of balance training data
		return mt2_accuracy;	
	}

	public double getBalancePrecision(){	// return the precision of balance training data
		return mt2_precision;	
	}

	public double getBalanceRecall(){	// return the recall of balance training data
		return mt2_recall;	
	}


	public double getUnBalanceAccuracy(){	// return the accuracy of unbalance training data
		return mt_accuracy;	
	}

	public double getUnBalancePrecision(){	// return the precision of unbalance training data
		return mt_precision;	
	}

	public double getUnBalanceRecall(){	// return the recall of unbalance training data
		return mt_recall;	
	}




	public static void main(String[] args){
		
		try{
			new ClassifierMechanism(args[0], args[1],  Double.parseDouble(args[2]));	
			
			
		}catch(Exception e){
			System.out.println(" Error in main function (in Balance.java):"+e.toString());	
		}
		
	}

}

