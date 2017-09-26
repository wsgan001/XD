package Health_Sys;

import java.lang.*;
import java.util.*;
import java.io.*;

import sequential_pattern.*;
import ConvertArffFiles.*;
import IntegratedTwoArffs.*;
import Gen_training_testing_data.*;
import Classifier.*;
import health_exam.*;
import weka.classifiers.trees.J48;
import weka.core.*;
import java.text.SimpleDateFormat;

public class basic_mts_classifier
{
	public String FILENAME="";
	public String project_name="segmented3_train_project";
	public double minimum_support=0.3;
	
	private J48 classifier=null;
	
	//All model should be put under "PredictionModels"
	public basic_mts_classifier(String pn)
	{
		//File folder_file=new File(pn);
		//project_name=(folder_file).getName();
		try{
			System.out.println(new File(pn).getParentFile());
			ObjectInputStream ois=new ObjectInputStream(new FileInputStream(new File(new File(pn).getParentFile(),"j48.model")));
			classifier=(J48)ois.readObject();
			//System.out.println("XDDDDD");
			ois.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		//load_model((new File(project_name)).getAbsolutePath());
	}
	
	public basic_mts_classifier(String f,double ms)
	{
		FILENAME=f;
		minimum_support=ms;
		project_name=(new File(FILENAME)).getName()+"_project";
/*		if(!(new File(project_name)).isDirectory())
		{
			File temp_dir=new File(project_name);
			//System.out.println(temp_dir.getAbsolutePath());
			(temp_dir).mkdir();
		}*/		
	}
	
	//model_file_path should be a folder with j48 model
	public boolean load_model(String model_file_path)
	{		
		boolean result=true;
		try
		{
			//if(FILENAME.equals("") && project_name.equals(""))
			//{
				//project name can be the name of the folder
				project_name=(new File(model_file_path)).getName();
				//FILENAME should be the filepath of j48 model
				if((new File(model_file_path,"output_arffs")).isDirectory())
					FILENAME=(new File(new File(model_file_path),"output_arffs/j48.model")).getAbsolutePath();
				else
					FILENAME=(new File(new File(model_file_path),project_name+".model")).getAbsolutePath();
			//}
			System.out.println("Loading File: "+FILENAME);
			ObjectInputStream ois=new ObjectInputStream(new FileInputStream(FILENAME));
			classifier=(J48)ois.readObject();
			ois.close();
		}
		catch(Exception e)
		{
			System.out.println("basic_mts_classifier load_model exception:"+e);
			result=false;
		}
		return result;
	}
	
	//basic mts format
	public String predict(String data_filename)
	{
		String result="";
		try
		{
			String temp_arff_filename=transform_into_arff(data_filename);
			replace_arff_header(temp_arff_filename);
			Instances all_instances=new Instances(new FileReader(temp_arff_filename));	
			
			System.out.println(temp_arff_filename);
			all_instances.setClassIndex(all_instances.numAttributes()-1);
			result=Double.toString(classifier.classifyInstance(all_instances.firstInstance()));
			double[] result_probabilities=classifier.distributionForInstance(all_instances.firstInstance());
			
    		System.out.println("debug: class attribute");
    		System.out.println("attr: " + all_instances.firstInstance().classAttribute().value(0));

			System.out.println("Probability:");
			for(int i=0;i<result_probabilities.length;i++)
				System.out.println(result_probabilities[i]);
		}
		catch(Exception e)
		{
			System.out.println("basic_mts_classifier predict exception:"+e);
		}
		return result;
	}
	
	private void replace_arff_header(String f)
	{
		try
		{
			String temp_filename=(File.createTempFile("arff_",".arff",new File("tmp"))).getAbsolutePath();
			BufferedWriter temp_bw=new BufferedWriter(new FileWriter(temp_filename));
			BufferedReader training_br=new BufferedReader(new FileReader("PredictionModels/" + project_name+"/"+ project_name + "_seq.arff"));
			BufferedReader single_br=new BufferedReader(new FileReader(f));
			
			String buffer="";
			String default_class_label="?";
			while((buffer=training_br.readLine())!=null)
			{
				temp_bw.write(buffer+"\n");
				String items[]=buffer.split(" ");
				if(items.length>1 && (items[1].equals("class") || items[1].equals("Class") || items[1].equals("CLASS")))
				{
					String class_items[]=items[2].substring(items[2].indexOf("{")+1,items[2].indexOf("}")).split(",");
					default_class_label=class_items[0];
				}
				if(buffer.trim().equals("@data"))
					break;
			}
			boolean write_flg=false;
			while(true)
			{
				buffer=single_br.readLine();
				if(buffer==null)
					break;
				if(write_flg)
				{
					if(!buffer.trim().equals(""))
					{
						temp_bw.write(buffer.substring(0,buffer.lastIndexOf(","))+","+default_class_label+"\n");
						//temp_bw.write(buffer+"0\n");
					}
				}
				if(buffer.trim().equals("@data"))
					write_flg=true;
			}
			
			temp_bw.close();
			training_br.close();
			single_br.close();
			copyFileTo(f, f+"wtf");
			copyFileTo(temp_filename,f);
		}
		catch(Exception e)
		{
			System.out.println("basic_mts_classifier replace_arff_header exception:"+e);
		}
	}
	
	public String transform_into_arff(String data_filename)
	{
		String result="";
		try
		{
			//person_rule_match a=new person_rule_match("PredictionModels/"+project_name+"/"+project_name+"_file_logs");
			//result=a.get_match_result("PredictionModels/"+project_name+"/"+project_name+"_order_logs",data_filename);
			basic_mts_rule_match a=new basic_mts_rule_match("PredictionModels/"+project_name+"/"+project_name+"_file_logs");
			result=a.get_match_result("PredictionModels/"+project_name+"/"+project_name+"_order_logs",data_filename);
			//String seq_filename=a.get_match_result("PredictionModels/"+disease_name+"/"+disease_name+"_order_logs",data_filename);
			//String static_filename=get_static_part(disease_name,"PredictionModels/"+disease_name+"/"+disease_name+"_MappingInfo.txt",data_filename);
			//result=arff_combine(disease_name,seq_filename,static_filename);
		}
		catch(Exception e)
		{
			System.out.println("basic_mts_classifier transform_into_arff exception:"+e);
		}
		return result;
	}
	
	public String predict_arff(String data_file_path)
	{
		String result="";
		try
		{
			if(classifier!=null)
			{				
				Instances testing_instances = new Instances(new FileReader(data_file_path));
				testing_instances.setClassIndex(testing_instances.numAttributes() - 1);
				double r=classifier.classifyInstance(testing_instances.firstInstance());
				result="Class:"+Double.toString(r);
			}
		}
		catch(Exception e)
		{
			System.out.println("basic_mts_predictor predict exception:"+e);
		}
		return result;
	}
	
	public void train()
	{
		try
		{
			//01 create a folder with input filename, and separate mts_file into ts_files(with class) in [project_name]/class_install
			(new File(project_name)).mkdir();
			copyFileTo((new File(FILENAME)).getAbsolutePath(),(new File(new File(project_name),FILENAME)).getAbsolutePath());
			prepare_ts_files();
			
			
			//--------------------------------
			// 04
			// 進行「每個健檢項目的 time series 之 Sequential Patterns Mining」			
			String ts_folder = project_name+"/class_install/";
			String sp_folder = project_name+"/seq_patterns_arffs/";
			if(!(new File(sp_folder)).isDirectory())
			{
				(new File(sp_folder)).mkdir();
			}
					
			del_files df2 = new del_files(project_name+"/seq_patterns_arffs/");	// 先清空「指定目錄夾裡的classinstall資料夾」		
											
			class_sequential_pattern_miner sp_mine = new class_sequential_pattern_miner( "-d", ts_folder,minimum_support, sp_folder);
			BufferedWriter temp_bw=new BufferedWriter(new FileWriter("log/"+project_name+"_file_logs"));
			temp_bw.write(sp_mine.file_logs);
			temp_bw.close();
			
		
						

			//--------------------------------
			// 05
			// 整合「全部的sequential patterns arff 檔」為「一個新整合檔arff」
			String output_file = project_name+"/output_arffs/"+project_name+"_seq.arff";
			
			
			del_files df3 = new del_files(project_name+"/output_arffs/");	// 先清空「指定目錄夾裡的output_arff資料夾」					
			arff_together at = new arff_together(sp_folder, output_file);
			temp_bw=new BufferedWriter(new FileWriter("log/"+project_name+"_order_logs"));
			temp_bw.write(at.order_log);
			temp_bw.close();
			
			temp_bw=new BufferedWriter(new FileWriter("log/"+project_name+"_order_logs2"));
			temp_bw.write(at.order_log2);
			temp_bw.close();
			
			copyFileTo(project_name+"/output_arffs/"+project_name+"_seq.arff",project_name+"/output_arffs/"+project_name+"_seq.arff_TTTT");
			
			order_log_change("log/"+project_name+"_file_logs","log/"+project_name+"_order_logs");
			pattern_mapping_info_generation("log/"+project_name+"_file_logs","log/"+project_name+"_order_logs");
	

			//--------------------------------
			// 06
			// 整理「靜態資料的arff檔」
//			String[] transform_para = new String[4];
//			transform_para[0] = all_parameters.get(1);	// 讀取「全部未經過轉換的 time series 資料夾」
//			transform_para[1] = all_parameters.get(2);	// 讀取「target class 的 timeseries 檔」
//			transform_para[2] = all_parameters.get(3);	// 讀取「健康區間的 Lower Bound」
//			transform_para[3] = all_parameters.get(4);	// 讀取「健康區間的 Upper Bound」

			
//			TransformArffFile taf = new TransformArffFile(transform_para[0], transform_para[1], Float.parseFloat(transform_para[2]), Float.parseFloat(transform_para[3]),disease_name);
			
	
			//--------------------------------
			// 07
			// 整合「動態資料(sequential patterns attribute arff)與靜態資料(id3.arff)檔」			
			
//			CombinationArff ca = new CombinationArff(format_extraction[1]+"\\output_arffs\\", disease_name);

		
			/*
	
			//--------------------------------
			// 08
			// 將「整合資料分為 Training data 和 Testing data」
			
			String input_arff_folder = project_name+"/output_arffs/";
			String input_arff = project_name+"_seq.arff";
			String output_folder = project_name]+"/output_arffs/";
			
			SplitArffFile srf = new SplitArffFile(input_arff_folder, input_arff, 0.7, output_folder);
			training_data_name = srf.getTrainingDataName();	
			testing_data_name = srf.getTestingDataName();
	
			System.out.println(" training_data_name = "+training_data_name);
			System.out.println(" testing_data_name = "+testing_data_name);
		

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
			decision_tree_para[0] = project_name+"/output_arffs/";	// 輸出「整合資料檔」
			decision_tree_para[1] = project_name+"_seq.arff";				// 經過資料轉換後的「整合資料檔」
			decision_tree_para[2] = "0.7";	// training data 與 testing data 的比率
			
			Vector<String> health_label_set=new Vector<String>();
			Vector<String> unhealth_label_set=new Vector<String>();
			health_label_set.add("C0");
			unhealth_label_set.add("C1");
			
			ClassifierMechanism  cm = new ClassifierMechanism(decision_tree_para[0], decision_tree_para[1],  Double.parseDouble(decision_tree_para[2]), health_label_set, unhealth_label_set);

			double balance_accuracy = cm.getBalanceAccuracy();
			double balance_precision = cm.getBalancePrecision();
			double balance_recall = cm.getBalanceRecall();
			double unbalance_accuracy = cm.getUnBalanceAccuracy();
			double unbalance_precision = cm.getUnBalancePrecision();
			double unbalance_recall = cm.getUnBalanceRecall();


			System.out.println(" balance_accuracy = "+balance_accuracy);
			System.out.println(" balance_precision = "+balance_precision);
			System.out.println(" balance_recall = "+balance_recall);
			System.out.println(" unbalance_accuracy = "+unbalance_accuracy);
			System.out.println(" unbalance_precision = "+unbalance_precision);
			System.out.println(" unbalance_recall = "+unbalance_recall);
			
			BufferedWriter bw=new BufferedWriter(new FileWriter(project_name+"/output_arffs/" + project_name + ".info"));
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z");
			bw.write("["+project_name+"]疾病預測模組 (建立時間:"+formatter.format(Calendar.getInstance().getTime())+")\n");
			bw.write("FileName:"+FILENAME+" Minimum support:"+minimum_support+"\n");
			bw.write("基本測試報告==============================\n");
			bw.write("原始資料 準確度(Accuracy):"+unbalance_accuracy+"\n");
			bw.write("原始資料 針對疾病發作的精確度(Precision):"+unbalance_precision+"\n");
			bw.write("原始資料 針對疾病發作的召回率(Recall):"+unbalance_recall+"\n");
			bw.write("[較客觀]平均取樣後資料 準確度(Accuracy):"+balance_accuracy+"\n");
			bw.write("[較客觀]平均取樣後資料 針對疾病發作的精確度(Precision):"+balance_precision+"\n");
			bw.write("[較客觀]平均取樣後資料 針對疾病發作的召回率(Recall):"+balance_recall+"\n");
			bw.close();
			
			copyFile();
			this.classifier=cm.classifier;
		}
		catch(Exception e)
		{
			System.out.println("basic_mts_classifier train exception:"+e);
		}
	}
	
	private void copyFile()
	{
		(new File("PredictionModels/"+project_name)).mkdir();
		copyFileTo(project_name+"/output_arffs/" + project_name + "_seq.arff",
    				"PredictionModels/" + project_name+"/"+ project_name + "_seq.arff");
    		copyFileTo(project_name+"/output_arffs/j48.model",
    				"PredictionModels/" + project_name+"/"+ project_name + ".model");    		
    		copyFileTo(project_name+"/output_arffs/"+ project_name+".info",
    				"PredictionModels/" + project_name+"/"+ project_name + ".info");
    		//copyFileTo("log/"+ project_name+"_patient_order_log",
    		//		"PredictionModels/" + project_name+"/"+ project_name + "_patient_order_log");
    		copyFileTo("log/"+ project_name+"_file_logs",
    				"PredictionModels/" + project_name+"/"+ project_name + "_file_logs");
    		copyFileTo("log/"+ project_name+"_order_logs",
    				"PredictionModels/" + project_name+"/"+ project_name + "_order_logs");


		copyFileTo(project_name+"/output_arffs/j48.txt",
    				"PredictionModels/" + project_name+"/J48.tree");
    		//copyFileTo(project_name+"/output_arffs/"+ project_name+"_MappingInfo.txt",
    		//		"PredictionModels/" + project_name+"/"+ project_name + "_MappingInfo.txt");
    		copyFileTo("log/"+ project_name+"_pattern_mapping_info_log",
    				"PredictionModels/" + project_name+"/"+ project_name + "_pattern_mapping_info_log");    		
    				    		
    		//text_tree_rebuild(project_name+"/output_arffs/J48.txt",
    		//			project_name+"/output_arffs/"+ project_name+"_MappingInfo.txt",
    		//			"log/"+ project_name+"_pattern_mapping_info_log",
    		//			"PredictionModels/" + project_name+"/"+ project_name + ".tree");
    		
    		//File data_dir=new File("PredictionModels/"+project_name+"/data");
    		//data_dir.mkdir();
    		//move_data_folder(project_name);
    	}	

	private void copyFileTo(String in, String out)
	{	
		try
		{		
			FileInputStream fis  = new FileInputStream(new File(in));
			FileOutputStream fos = new FileOutputStream(new File(out));
			byte[] buf = new byte[1024];
			int i = 0;
			while ((i = fis.read(buf)) != -1)
			{
				fos.write(buf, 0, i);
			}
			if (fis != null) fis.close();
			if (fos != null) fos.close();
		} 
		catch (Exception e)
		{
			System.out.println("basic_mts_classifier copyFileTo exception:"+e);
		}
	}
	
	private void prepare_ts_files()
	{
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(FILENAME));
			(new File(project_name+"/class_install")).mkdir();
			String buffer="";
			String temp_instance="";
			String temp_class_label="";
			Vector<String> temp_tss=new Vector<String>();
			Vector<BufferedWriter> bw_set=new Vector<BufferedWriter>();
			while(true)
			{
				buffer=br.readLine();
				if(buffer==null || buffer.equals(""))
				{
					if(bw_set.size()==0)
					{
						for(int i=0;i<temp_tss.size();i++)
						{
							bw_set.add(new BufferedWriter(new FileWriter(project_name+"/class_install/ts"+i)));
						}
					}
					else
					{
						for(int i=0;i<temp_tss.size() && i<bw_set.size();i++)
						{
							BufferedWriter temp_bw=(BufferedWriter)bw_set.get(i);
							temp_bw.write(temp_class_label+temp_tss.get(i)+"\n");
						}
					}
					if(buffer==null)
						break;
					temp_tss.clear();
				}
				else
				{
					if(buffer.indexOf(":")>=0)
					{
						temp_class_label=buffer;
					}
					else
					{
						temp_tss.add(buffer);
					}
				}
			}			
			br.close();
			for(int i=0;i<bw_set.size();i++)
			{
				((BufferedWriter)bw_set.get(i)).close();
			}
		}
		catch(Exception e)
		{
			System.out.println("basic_mts_classifier prepare_ts_files exception:"+e);
		}
	}

	//generate pattern collection Ex. F1_A0 = sqlfile_output_基本資料與身體評估_BMI_timeseries.txt : 正常,正常,
	public void pattern_mapping_info_generation(String fl,String ol)
	{
		try
		{
			HashMap match_table=load_pattern_sn_file_table(fl);
			Vector<String> ts_file_list=load_arff_file_list(ol); //this file list have been "timeseries" filename list already.
			BufferedWriter bw=new BufferedWriter(new FileWriter("log/"+project_name+"_pattern_mapping_info_log"));
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
						bw.write("F"+file_index+"_A"+pattern_count+": ["+ts_file_list.get(i)+"_pattern] "+buffer+"\n");
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
			System.out.println("basic_mts_classifier pattern_mapping_info_generation exception:"+e);
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
			System.out.println("basic_mts_classifier load_pattern_sn_file_table exception:"+e);
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
			System.out.println("basic_mts_classifier order_log_change exception:"+e);
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
			System.out.println("basic_mts_classifier load_arff_file_table exception:"+e);
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
			System.out.println("basic_mts_classifier load_arff_file_list exception:"+e);
		}
		return result;
	}
	
	public static void main(String args[])
	{
		if(args.length==2 || args.length==3)
		{
			boolean train_flg=true; //train or load classifier
			
			basic_mts_classifier classifier=null;
			try
			{
				Double.parseDouble(args[1]);
			}
			catch(Exception e)
			{
				train_flg=false;
			}
			if(train_flg)
			{
				classifier=new basic_mts_classifier(args[0],Double.parseDouble(args[1]));		
				classifier.train();
			}
			else
			{
				//classifier=new basic_mts_classifier("D:/project/III/2009/Integrated_HealthAnalysisSys/氣喘.txt_folder");
				System.out.println("Load model ..."+args[0]);
				classifier=new basic_mts_classifier(args[0]);
				System.out.println("分類結果:"+classifier.predict(args[1]));
			}
			
			if(args.length==3)
				System.out.println("分類結果:"+classifier.predict(args[2]));
		}
		else
		{
			System.out.println("Three possible inputs:");
			System.out.println("java basic_mts_classifier [training filename] [# minimum support]");
			System.out.println("java basic_mts_classifier [training filename] [# minimum support] [testing_filename]");
			System.out.println("java basic_mts_classifier [tree model filename] [testing_filename]");
			System.exit(0);
		}				
	}
}