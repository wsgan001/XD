package Health_Sys;

import java.lang.*;
import java.util.*;
import java.io.*;

import sequential_pattern.*;		// ���J�uSequential Pattern Mining ����k�v
import ConvertArffFiles.*;		// ���J�u�i�N��l time series data�ഫ���@��ID3�i���檺 arff�ɡv
import IntegratedTwoArffs.*;		// �N�u���arff�ɦX���@�� integrated.arff�ɡI�v
import Gen_training_testing_data.*;	// �N�u���arff�ɦX���@�� integrated.arff�ɡI�v
import Classifier.*;			// ���J�u�������v
import health_exam.*;


public class HAS{

	
	static Vector<String> all_parameters;
	
	
	// unbalance training data information
	static double unbalance_accuracy = 0d;
	static double unbalance_precision = 0d;
	static double unbalance_recall = 0d;
	
	// balance training data information
	static double balance_accuracy = 0d;
	static double balance_precision = 0d;	
	static double balance_recall = 0d;
	
	String disease_name="";
	
	
	public static void main(String[] args){
		
		try{
		
			String[] para = new String[9];
			
			
			if(args.length==9)
			{
				// �U���Ѽƿ�J			
				para[0] = args[0];	// �s��u��Ӹ�Ʈw�������̪��򥻸�ƻP��������ɡv�����|�P�ɦW	
				para[1] = args[1];	// �s��u���ո�Ƹ��|�v
				para[2] = args[2];	// �����uŲ�w���ءv
				para[3] = args[3];	// �]�w�u���d�϶��� lower bound value�v
				para[4] = args[4];	// �]�w�u���d�϶��� upper bound value�v
				para[5] = args[5];	// �]�w�u���d�϶��˦������e�ȡv
				para[6] = args[6];	// �]�w�u���d�϶��˦����ɦW�v
				para[7] = args[7];	// �]�w�u�V�m��ƶ�����v�v
				para[8] = args[8];	// �ӯe�f�W��(�Ҧp:�}���f)
			}
			else
			{
				para[0] = "C://Documents and Settings//yuyen//�ୱ//Integrated_HealthAnalysisSys//PersonData//sqlfile_outputfile_�򥻸�ƻP�������.txt";	// �s��u��Ӹ�Ʈw�������̪��򥻸�ƻP��������ɡv�����|�P�ɦW	
				para[1] = "C://Documents and Settings//yuyen//�ୱ//Integrated_HealthAnalysisSys//20090930_���ո��";		// �s��u���ո�Ƹ��|�v
				para[2] = "sqlfile_output_�}���f�z��_�Ÿ���}_timeseries.txt";		// �����uŲ�w���ءv
				para[3] = "0";								// �]�w�u���d�϶��� lower bound value�v
				para[4] = "100";							// �]�w�u���d�϶��� upper bound value�v
				para[5] = "0.04";							// �]�w�u���d�϶��˦������e�ȡv
				para[6] = "�}���f_seq.arff";						// �]�w�u���d�϶��˦����ɦW�v
				para[7] = "0.7";							// �]�w�u�V�m��ƶ�����v�v
				para[8] = "�}���f";							// �ӯe�f�W��(�Ҧp:�}���f)			
			}
									
			
			
			new HAS(para[0], para[1], para[2], para[3], para[4], 
				para[5], para[6], para[7], para[8]);
			
		
		
		}catch(Exception e){
			System.out.println(" Error in main function (HAS.java):"+e.toString());	
		}
	}

	
	

	
	public HAS(String para1, String para2, String para3, String para4, String para5,
	           String para6, String para7, String para8, String para9){
	
		
						
		try{
			all_parameters = new Vector<String>();
			
			/*
			//--------------------------------
			// 01
			// Ū���U���Ѽ�			
			load_parameters load_para = new load_parameters();
				all_parameters = load_para.getParameters();
				Print();				
			*/

									
			all_parameters.add(para1);
			all_parameters.add(para2);
			all_parameters.add(para3);
			all_parameters.add(para4);
			all_parameters.add(para5);
			all_parameters.add(para6);
			all_parameters.add(para7);
			all_parameters.add(para8);
			all_parameters.add(para9);
					
			
			Print();				
			
			disease_name = para9;	// Ū���u�e�f�W�� disease name�v			
			
			
			
			if(all_parameters.size()<2){
				System.out.println(" Please check your input parameters!!!");
				System.out.println(" Thank you for your cooperation!!!");
				return;				
			}
			
			
															

			//--------------------------------
			// 02
			// �i��u�C�Ӱ��˶��ت� time series �ഫ�榡�B�z�v			
						
			String[] format_extraction = new String[2];
			format_extraction[0] = all_parameters.get(0);	// ��J�u���w�� �����������̸�� �v
			format_extraction[1] = all_parameters.get(1);	// ��J�u���w�� input ��Ƨ��v
			
			
			del_files df = new del_files(format_extraction[1]+"/tmp");	// ���M�šu���w�ؿ����̪�tmp��Ƨ��v
			my_format_extraction a = new my_format_extraction(format_extraction[0], format_extraction[1]);
			BufferedWriter temp_bw=new BufferedWriter(new FileWriter("log/"+disease_name+"_patient_order_log"));
			for(int i=0;i<a.all_patient_id.size();i++)
			{
				temp_bw.write(a.all_patient_id.get(i)+"\n");
			}
			temp_bw.close();
			
						

			//--------------------------------
			// 03
			// �i��u�C�Ӱ��˶��ت� time series �[�J target class value�v	
					
			String[] data_transform = new String[5];
			data_transform[0] = all_parameters.get(2);			// ��J�u�Ÿ���}.txt �� �ɦW�v
			data_transform[1] = all_parameters.get(3);			// ��J�urange���d�� - lower bound value�v
			data_transform[2] = all_parameters.get(4);			// ��J�urange���d�� - upper bound value�v
			data_transform[3] = format_extraction[1]+"\\tmp\\";		// ��J�uŪ���g�LStep1�ഫ�᪺ time series ��Ƨ��v
			data_transform[4] = format_extraction[1]+"\\classinstall\\";	// ��X�u�w�s�Jtarget class value ���Ҧ� time series data �ܫ��w����Ƨ��v
			
						
			del_files df1 = new del_files(format_extraction[1]+"\\classinstall\\");	// ���M�šu���w�ؿ����̪�classinstall��Ƨ��v
			
															
			// �I�s����k!!!
			class_install agent = new class_install(format_extraction[1]+"\\"+data_transform[0], Double.parseDouble(data_transform[2]), Double.parseDouble(data_transform[1]), data_transform[3], data_transform[4]);	
			
		
      //after class_install the target_file show be removed (ex. �Ÿ���}.txt)	
      File target_file=new File(format_extraction[1]+"/classinstall/"+data_transform[0]);
      if(target_file.exists())
        target_file.delete();


	
		
			//--------------------------------
			// 04
			// �i��u�C�Ӱ��˶��ت� time series �� Sequential Patterns Mining�v
			String[] sp_para = new String[3];		
			sp_para[0] = format_extraction[1]+"\\classinstall\\";			
			sp_para[1] = all_parameters.get(5);
			sp_para[2] = format_extraction[1]+"\\seq_patterns_arffs\\";		
					
			del_files df2 = new del_files(format_extraction[1]+"\\seq_patterns_arffs\\");	// ���M�šu���w�ؿ����̪�classinstall��Ƨ��v		
											
			class_sequential_pattern_miner sp_mine = new class_sequential_pattern_miner( "-d", sp_para[0], Double.parseDouble(sp_para[1]), sp_para[2]);
			temp_bw=new BufferedWriter(new FileWriter("log/"+disease_name+"_file_logs"));
			temp_bw.write(sp_mine.file_logs);
			temp_bw.close();
			
		
						

			//--------------------------------
			// 05
			// ��X�u������sequential patterns arff �ɡv���u�@�ӷs��X��arff�v
			String[] at_para = new String[2];
			at_para[0] = format_extraction[1]+"\\seq_patterns_arffs\\";
			at_para[1] = format_extraction[1]+"\\output_arffs\\"+all_parameters.get(6);
			
			
			del_files df3 = new del_files(format_extraction[1]+"\\output_arffs\\");	// ���M�šu���w�ؿ����̪�classinstall��Ƨ��v					
			arff_together at = new arff_together(at_para[0], at_para[1]);
			temp_bw=new BufferedWriter(new FileWriter("log/"+disease_name+"_order_logs"));
			temp_bw.write(at.order_log);
			temp_bw.close();
			
			order_log_change("log/"+all_parameters.get(8)+"_file_logs","log/"+disease_name+"_order_logs");
			
			pattern_mapping_info_generation("log/"+disease_name+"_file_logs","log/"+disease_name+"_order_logs");
	

			//--------------------------------
			// 06
			// ��z�u�R�A��ƪ�arff�ɡv
			String[] transform_para = new String[4];
			transform_para[0] = all_parameters.get(1);	// Ū���u�������g�L�ഫ�� time series ��Ƨ��v
			transform_para[1] = all_parameters.get(2);	// Ū���utarget class �� timeseries �ɡv
			transform_para[2] = all_parameters.get(3);	// Ū���u���d�϶��� Lower Bound�v
			transform_para[3] = all_parameters.get(4);	// Ū���u���d�϶��� Upper Bound�v

			//�o�ӭn��@�U �]���p�G�Ψ�~�֤��s���� �C�s�N�����O�������������o~ 
			//TransformArffFile taf = new TransformArffFile(transform_para[0], transform_para[1], Float.parseFloat(transform_para[2]), Float.parseFloat(transform_para[3]),disease_name);
			//�ҥH�ڧ��o �U���h�[�@�ӰѼ� �]�N�O��personal data�L�h (40���H�U�N�|��40���H�U�ۤv��personal data)
			TransformArffFile taf = new TransformArffFile(all_parameters.get(0),transform_para[0], transform_para[1], Float.parseFloat(transform_para[2]), Float.parseFloat(transform_para[3]),disease_name);
			
	
			//--------------------------------
			// 07
			// ��X�u�ʺA���(sequential patterns attribute arff)�P�R�A���(id3.arff)�ɡv			
			
			CombinationArff ca = new CombinationArff(format_extraction[1]+"\\output_arffs\\", disease_name);

		
		

	
	
			//--------------------------------
			// 08
			// �N�u��X��Ƥ��� Training data �M Testing data�v
			String[] split_arff_para = new String[4];
			String training_data_name="";
			String testing_data_name="";
			
			split_arff_para[0] = format_extraction[1]+"\\output_arffs\\";
			split_arff_para[1] = disease_name+"_combined_arff.arff";
			split_arff_para[2] = all_parameters.get(7);
			split_arff_para[3] = format_extraction[1]+"\\output_arffs\\";
			
			
			SplitArffFile srf = new SplitArffFile(split_arff_para[0], split_arff_para[1], Double.parseDouble(split_arff_para[2]), split_arff_para[3]);
				training_data_name = srf.getTrainingDataName();	
				testing_data_name = srf.getTestingDataName();
	
	
	
			System.out.println(" training_data_name = "+training_data_name);
			System.out.println(" testing_data_name = "+testing_data_name);
		
			/*

			//--------------------------------
			// 09
			// �N�i������{��!!!
			String[] decision_tree_para = new String[3];
			decision_tree_para[0] = format_extraction[1]+"\\output_arffs\\";
			decision_tree_para[1] = training_data_name;
			decision_tree_para[2] = testing_data_name;
						
			MyDecisionTree mdt = new MyDecisionTree(decision_tree_para[0], decision_tree_para[1], decision_tree_para[2]);
			*/
		
		
		
			//--------------------------------
			// 09
			// �N�i��ubalance training data �P unbalance training data �������v�{��!!!
			//"C:\jdk1.6.0_03\bin\2009_11���\20091103_01_���˸��_���Ť�v\input_data" disease_name+"_combined_arff.arff"
			String[] decision_tree_para = new String[3];
			decision_tree_para[0] = format_extraction[1]+"\\output_arffs\\";	// ��X�u��X����ɡv
			decision_tree_para[1] = disease_name+"_combined_arff.arff";				// �g�L����ഫ�᪺�u��X����ɡv
			decision_tree_para[2] = all_parameters.get(7);	// training data �P testing data ����v
			
			
			ClassifierMechanism  cm = new ClassifierMechanism(decision_tree_para[0], decision_tree_para[1],  Double.parseDouble(decision_tree_para[2]));
				balance_accuracy = cm.getBalanceAccuracy();
				balance_precision = cm.getBalancePrecision();
				balance_recall = cm.getBalanceRecall();
				
				unbalance_accuracy = cm.getUnBalanceAccuracy();
				unbalance_precision = cm.getUnBalancePrecision();
				unbalance_recall = cm.getUnBalanceRecall();


				System.out.println(" balance_accuracy = "+balance_accuracy);
				System.out.println(" balance_precision = "+balance_precision);
				System.out.println(" balance_recall = "+balance_recall);
				System.out.println(" unbalance_accuracy = "+unbalance_accuracy);
				System.out.println(" unbalance_precision = "+unbalance_precision);
				System.out.println(" unbalance_recall = "+unbalance_recall);



		
		}catch(Exception e){
			System.out.println(" Error in main function (in HAS.java): "+e.toString());	
			
		}
				
	}
	
	
	
	//------------------------------------------
	//------------------------------------------
	//------------------------------------------		
	public static double getBalanceAccuracy(){	// return the accuracy of balance training data
		return balance_accuracy;	
	}

	public static double getBalancePrecision(){	// return the precision of balance training data
		return balance_precision;	
	}

	public static double getBalanceRecall(){	// return the recall of balance training data
		return balance_recall;	
	}


	public static double getUnBalanceAccuracy(){	// return the accuracy of unbalance training data
		return unbalance_accuracy;	
	}

	public static double getUnBalancePrecision(){	// return the precision of unbalance training data
		return unbalance_precision;	
	}

	public static double getUnBalanceRecall(){	// return the recall of unbalance training data
		return unbalance_recall;	
	}
	

	
	
	
	
	
	
	
	//generate pattern collection Ex. F1_A0 = sqlfile_output_�򥻸�ƻP�������_BMI_timeseries.txt : ���`,���`,
	public void pattern_mapping_info_generation(String fl,String ol)
	{
		try
		{
			HashMap match_table=load_pattern_sn_file_table(fl);
			Vector<String> ts_file_list=load_arff_file_list(ol); //this file list have been "timeseries" filename list already.
			BufferedWriter bw=new BufferedWriter(new FileWriter("log/"+disease_name+"_pattern_mapping_info_log"));
			int file_index=0;
			for(int i=0;i<ts_file_list.size();i++)
			{
				if( match_table.get(ts_file_list.get(i))!=null )
				{
					String temp_pattern_sn_filename=(String)match_table.get(ts_file_list.get(i));
					BufferedReader br=new BufferedReader(new FileReader(temp_pattern_sn_filename));
					String buffer="";
					int pattern_count=0;
					while((buffer=br.readLine())!=null)
					{
						buffer=buffer.substring(buffer.indexOf(":")+1,buffer.lastIndexOf(":"));
						bw.write("F"+file_index+"_A"+pattern_count+": ["+temp_pattern_sn_filename.substring(0,temp_pattern_sn_filename.indexOf("_timeseries"))+"_pattern] "+buffer+"\n");
						pattern_count++;
					}
					//if(pattern_count>0)
						file_index++;
					br.close();
				}
			}
			bw.close();
		}
		catch(Exception e)
		{
			System.out.println("HAS pattern_mapping_info_generation exception:"+e);
		}
	}
	
	private HashMap<String,String> load_pattern_sn_file_table(String f)
	{
		HashMap<String,String> result=new HashMap<String,String>();
		try
		{			
			BufferedReader br=new BufferedReader(new FileReader(f));
			String buffer="";
			String temp_filename="";
			String temp_pattern_sn_filename="";
			while((buffer=br.readLine())!=null)
			{
				if(!buffer.trim().equals(""))
				{
					if(buffer.indexOf("file path")>0)
					{
						temp_filename=buffer.substring(buffer.indexOf("]")+1).trim();
					}
					else if(buffer.indexOf("pattern_sn_filename")>0)
					{	
						temp_pattern_sn_filename=buffer.substring(buffer.indexOf("]")+1).trim();
					}
				}
				else
				{
					if(!temp_filename.equals("") && !temp_pattern_sn_filename.equals(""))
					{
						temp_filename=(new File(temp_filename)).getName();
						//temp_pattern_sn_filename=(new File(temp_pattern_sn_filename)).getName();  // I need real file path
						result.put(temp_filename,temp_pattern_sn_filename); // filename=>pattern_sn_file
						temp_filename="";
						temp_pattern_sn_filename="";
					}
				}
			}
			br.close();
		}
		catch(Exception e)
		{
			System.out.println("HAS load_pattern_sn_file_table exception:"+e);
		}
		return result;
	}
	
	//original order file records the order of arff, we change them into the order of timeseries filename.
	public void order_log_change(String fl,String ol)
	{
		try
		{
			//file and its arff filename , but we record as <arff_filename,original_filename>
			HashMap<String,String> match_table=load_arff_file_table(fl);
			Vector<String> arff_file_list=load_arff_file_list(ol);
			
			BufferedWriter bw=new BufferedWriter(new FileWriter(ol));
			for(int i=0;i<arff_file_list.size();i++)
			{
				if( match_table.get(arff_file_list.get(i))!=null )
				{
					bw.write(match_table.get(arff_file_list.get(i))+"\n");
				}
			}
			bw.close();
		}
		catch(Exception e)
		{
			System.out.println("HAS order_log_change exception:"+e);
		}
	}
	
	
	private HashMap<String,String> load_arff_file_table(String f)
	{
		HashMap<String,String> result=new HashMap<String,String>();
		try
		{			
			BufferedReader br=new BufferedReader(new FileReader(f));
			String buffer="";
			String temp_filename="";
			String temp_arff_filename="";
			while((buffer=br.readLine())!=null)
			{
				if(!buffer.trim().equals(""))
				{
					if(buffer.indexOf("file path")>0)
					{
						temp_filename=buffer.substring(buffer.indexOf("]")+1).trim();
					}
					else if(buffer.indexOf("arff filename")>0)
					{	
						temp_arff_filename=buffer.substring(buffer.indexOf("]")+1).trim();
					}
				}
				else
				{
					if(!temp_filename.equals("") && !temp_arff_filename.equals(""))
					{
						temp_filename=(new File(temp_filename)).getName();
						temp_arff_filename=(new File(temp_arff_filename)).getName();
						result.put(temp_arff_filename,temp_filename);
						temp_filename="";
						temp_arff_filename="";
					}
				}
			}
			br.close();
		}
		catch(Exception e)
		{
			System.out.println("HAS load_arff_file_table exception:"+e);
		}
		return result;
	}
	
	private Vector<String> load_arff_file_list(String f)
	{
		Vector<String> result=new Vector<String>();
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(f));
			String buffer="";
			while((buffer=br.readLine())!=null)
			{
				result.add( (new File(buffer)).getName() );
			}
			br.close();
		}
		catch(Exception e)
		{
			System.out.println("HAS load_arff_file_list exception:"+e);
		}
		return result;
	}
	
	
	public static void load_parameters(){
		
		try{	
		
			BufferedReader br = new BufferedReader(new FileReader("./input_parameters.ini"));
  			String line="";
  			
  			
        		while ((line=br.readLine())!=null){
     					
     				all_parameters.add(line);
             		}

         		br.close();				
			
			Print();
		
		}catch(Exception e){
			System.out.println(" Error about loading parameters (in HAS.java): "+e.toString());
		}
		
	}	

	public static void Print(){
		
		try{
		
			System.out.println("=======================================");
			System.out.println("		Parameters Print");
			
			for(int i=0;i<all_parameters.size();i++)
			{
				String para =  all_parameters.get(i);
				System.out.println(" Parameter "+(i+1)+": "+para);
			}
		
			System.out.println("=======================================");
		
		
		}catch(Exception e){
			System.out.println(" Error about printing all the parameters (in HAS.java): "+e.toString());
		}
		
		
	}	
	
	
}