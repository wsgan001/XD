package Health_Sys;

import java.lang.*;
import java.util.*;
import java.io.*;

import sequential_pattern.*;		// 載入「Sequential Pattern Mining 的方法」
import ConvertArffFiles.*;		// 載入「可將原始 time series data轉換成一個ID3可執行的 arff檔」
import IntegratedTwoArffs.*;		// 將「兩個arff檔合成一個 integrated.arff檔！」
import Gen_training_testing_data.*;	// 將「兩個arff檔合成一個 integrated.arff檔！」
import Classifier.*;			// 載入「分類器」
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
				// 各項參數輸入			
				para[0] = args[0];	// 存放「整個資料庫的受測者的基本資料與身體評估檔」的路徑與檔名	
				para[1] = args[1];	// 存放「測試資料路徑」
				para[2] = args[2];	// 指明「鑑定項目」
				para[3] = args[3];	// 設定「健康區間的 lower bound value」
				para[4] = args[4];	// 設定「健康區間的 upper bound value」
				para[5] = args[5];	// 設定「健康區間樣式的門檻值」
				para[6] = args[6];	// 設定「健康區間樣式的檔名」
				para[7] = args[7];	// 設定「訓練資料集的比率」
				para[8] = args[8];	// 該疾病名稱(例如:糖尿病)
			}
			else
			{
				para[0] = "C://Documents and Settings//yuyen//桌面//Integrated_HealthAnalysisSys//PersonData//sqlfile_outputfile_基本資料與身體評估.txt";	// 存放「整個資料庫的受測者的基本資料與身體評估檔」的路徑與檔名	
				para[1] = "C://Documents and Settings//yuyen//桌面//Integrated_HealthAnalysisSys//20090930_測試資料";		// 存放「測試資料路徑」
				para[2] = "sqlfile_output_糖尿病篩檢_空腹血糖_timeseries.txt";		// 指明「鑑定項目」
				para[3] = "0";								// 設定「健康區間的 lower bound value」
				para[4] = "100";							// 設定「健康區間的 upper bound value」
				para[5] = "0.04";							// 設定「健康區間樣式的門檻值」
				para[6] = "糖尿病_seq.arff";						// 設定「健康區間樣式的檔名」
				para[7] = "0.7";							// 設定「訓練資料集的比率」
				para[8] = "糖尿病";							// 該疾病名稱(例如:糖尿病)			
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
			// 讀取各項參數			
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
			
			disease_name = para9;	// 讀取「疾病名稱 disease name」			
			
			
			
			if(all_parameters.size()<2){
				System.out.println(" Please check your input parameters!!!");
				System.out.println(" Thank you for your cooperation!!!");
				return;				
			}
			
			
															

			//--------------------------------
			// 02
			// 進行「每個健檢項目的 time series 轉換格式處理」			
						
			String[] format_extraction = new String[2];
			format_extraction[0] = all_parameters.get(0);	// 輸入「指定的 全部的受測者資料 」
			format_extraction[1] = all_parameters.get(1);	// 輸入「指定的 input 資料夾」
			
			
			del_files df = new del_files(format_extraction[1]+"/tmp");	// 先清空「指定目錄夾裡的tmp資料夾」
			my_format_extraction a = new my_format_extraction(format_extraction[0], format_extraction[1]);
			BufferedWriter temp_bw=new BufferedWriter(new FileWriter("log/"+disease_name+"_patient_order_log"));
			for(int i=0;i<a.all_patient_id.size();i++)
			{
				temp_bw.write(a.all_patient_id.get(i)+"\n");
			}
			temp_bw.close();
			
						

			//--------------------------------
			// 03
			// 進行「每個健檢項目的 time series 加入 target class value」	
					
			String[] data_transform = new String[5];
			data_transform[0] = all_parameters.get(2);			// 輸入「空腹血糖.txt 的 檔名」
			data_transform[1] = all_parameters.get(3);			// 輸入「range的範圍 - lower bound value」
			data_transform[2] = all_parameters.get(4);			// 輸入「range的範圍 - upper bound value」
			data_transform[3] = format_extraction[1]+"\\tmp\\";		// 輸入「讀取經過Step1轉換後的 time series 資料夾」
			data_transform[4] = format_extraction[1]+"\\classinstall\\";	// 輸出「已存入target class value 的所有 time series data 至指定的資料夾」
			
						
			del_files df1 = new del_files(format_extraction[1]+"\\classinstall\\");	// 先清空「指定目錄夾裡的classinstall資料夾」
			
															
			// 呼叫此方法!!!
			class_install agent = new class_install(format_extraction[1]+"\\"+data_transform[0], Double.parseDouble(data_transform[2]), Double.parseDouble(data_transform[1]), data_transform[3], data_transform[4]);	
			
		
      //after class_install the target_file show be removed (ex. 空腹血糖.txt)	
      File target_file=new File(format_extraction[1]+"/classinstall/"+data_transform[0]);
      if(target_file.exists())
        target_file.delete();


	
		
			//--------------------------------
			// 04
			// 進行「每個健檢項目的 time series 之 Sequential Patterns Mining」
			String[] sp_para = new String[3];		
			sp_para[0] = format_extraction[1]+"\\classinstall\\";			
			sp_para[1] = all_parameters.get(5);
			sp_para[2] = format_extraction[1]+"\\seq_patterns_arffs\\";		
					
			del_files df2 = new del_files(format_extraction[1]+"\\seq_patterns_arffs\\");	// 先清空「指定目錄夾裡的classinstall資料夾」		
											
			class_sequential_pattern_miner sp_mine = new class_sequential_pattern_miner( "-d", sp_para[0], Double.parseDouble(sp_para[1]), sp_para[2]);
			temp_bw=new BufferedWriter(new FileWriter("log/"+disease_name+"_file_logs"));
			temp_bw.write(sp_mine.file_logs);
			temp_bw.close();
			
		
						

			//--------------------------------
			// 05
			// 整合「全部的sequential patterns arff 檔」為「一個新整合檔arff」
			String[] at_para = new String[2];
			at_para[0] = format_extraction[1]+"\\seq_patterns_arffs\\";
			at_para[1] = format_extraction[1]+"\\output_arffs\\"+all_parameters.get(6);
			
			
			del_files df3 = new del_files(format_extraction[1]+"\\output_arffs\\");	// 先清空「指定目錄夾裡的classinstall資料夾」					
			arff_together at = new arff_together(at_para[0], at_para[1]);
			temp_bw=new BufferedWriter(new FileWriter("log/"+disease_name+"_order_logs"));
			temp_bw.write(at.order_log);
			temp_bw.close();
			
			order_log_change("log/"+all_parameters.get(8)+"_file_logs","log/"+disease_name+"_order_logs");
			
			pattern_mapping_info_generation("log/"+disease_name+"_file_logs","log/"+disease_name+"_order_logs");
	

			//--------------------------------
			// 06
			// 整理「靜態資料的arff檔」
			String[] transform_para = new String[4];
			transform_para[0] = all_parameters.get(1);	// 讀取「全部未經過轉換的 time series 資料夾」
			transform_para[1] = all_parameters.get(2);	// 讀取「target class 的 timeseries 檔」
			transform_para[2] = all_parameters.get(3);	// 讀取「健康區間的 Lower Bound」
			transform_para[3] = all_parameters.get(4);	// 讀取「健康區間的 Upper Bound」

			//這個要改一下 因為如果用到年齡分群的話 每群就都不是全部的受測者囉~ 
			//TransformArffFile taf = new TransformArffFile(transform_para[0], transform_para[1], Float.parseFloat(transform_para[2]), Float.parseFloat(transform_para[3]),disease_name);
			//所以我改囉 下面多加一個參數 也就是傳personal data過去 (40歲以下就會有40歲以下自己的personal data)
			TransformArffFile taf = new TransformArffFile(all_parameters.get(0),transform_para[0], transform_para[1], Float.parseFloat(transform_para[2]), Float.parseFloat(transform_para[3]),disease_name);
			
	
			//--------------------------------
			// 07
			// 整合「動態資料(sequential patterns attribute arff)與靜態資料(id3.arff)檔」			
			
			CombinationArff ca = new CombinationArff(format_extraction[1]+"\\output_arffs\\", disease_name);

		
		

	
	
			//--------------------------------
			// 08
			// 將「整合資料分為 Training data 和 Testing data」
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
			// 將進行分類程序!!!
			String[] decision_tree_para = new String[3];
			decision_tree_para[0] = format_extraction[1]+"\\output_arffs\\";
			decision_tree_para[1] = training_data_name;
			decision_tree_para[2] = testing_data_name;
						
			MyDecisionTree mdt = new MyDecisionTree(decision_tree_para[0], decision_tree_para[1], decision_tree_para[2]);
			*/
		
		
		
			//--------------------------------
			// 09
			// 將進行「balance training data 與 unbalance training data 的分類」程序!!!
			//"C:\jdk1.6.0_03\bin\2009_11月份\20091103_01_健檢資料_平衡比率\input_data" disease_name+"_combined_arff.arff"
			String[] decision_tree_para = new String[3];
			decision_tree_para[0] = format_extraction[1]+"\\output_arffs\\";	// 輸出「整合資料檔」
			decision_tree_para[1] = disease_name+"_combined_arff.arff";				// 經過資料轉換後的「整合資料檔」
			decision_tree_para[2] = all_parameters.get(7);	// training data 與 testing data 的比率
			
			
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
	

	
	
	
	
	
	
	
	//generate pattern collection Ex. F1_A0 = sqlfile_output_基本資料與身體評估_BMI_timeseries.txt : 正常,正常,
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