package data_format;
import java.io.*;
import java.util.HashMap;
import java.util.Vector;
import sequential_pattern.*;

public class get_health_data
{
	String data_directory="./";
	String data_filename="";
	public get_health_data(String dir)
	{
		data_directory=dir;
	}
		
	public String get_one_patient(String  chk_no)
	{
		String result="";  //output_filename
		try
		{
			result="tmp/"+chk_no+".data";
			File dir=new File(data_directory);
			BufferedWriter bw=new BufferedWriter(new FileWriter(result));
			get_dir_data(dir,chk_no,bw);
			bw.close();
			data_filename=result;
		}
		catch(Exception e)
		{
			System.out.println("get_health_data get_one_patient exception:"+e);
		}
		return result;
	}
	
	private void get_dir_data(File f,String chk_no,BufferedWriter bw)
	{
		try
		{
			if(f.isDirectory())
			{
				File[] all_files=f.listFiles();
				for(int i=0;i<all_files.length;i++)
					get_data(all_files[i],chk_no,bw);
			}
		}
		catch(Exception e)
		{
			System.out.println("get_health_data get_dir_data exception:"+e);
		}
	}
	
	//get all data of chk_no from rf and write to of
	private void get_data(File f,String chk_no,BufferedWriter bw)
	{
		try
		{
			if(f.isDirectory())
			{
				//File[] all_files=f.listFiles();
				//for(int i=0;i<all_files.length;i++)
				//	get_data(all_files[i],chk_no,bw);
				//no more				
			}
			else if(f.isFile())
			{
				if(f.getName().indexOf("timeseries")>0 || f.getName().indexOf("timseries")>0)
				{
					String item_time_series=get_ts(f,chk_no);
					if(!item_time_series.trim().equals(""))
						bw.write(f.getName()+":"+item_time_series+"\n");
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("get_health_data get_data exception:"+e);
		}
	}
	
	private String get_ts(File f,String chk_no)
	{
		String result="";
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(f));
			String buffer="";
			while((buffer=br.readLine())!=null)
			{
				if(buffer.indexOf(chk_no)==0)
				{
					result=buffer.substring(buffer.indexOf(",")+1);
				}
			}
			br.close();
		}
		catch(Exception e)
		{
			System.out.println("get_health_data get_ts exception:"+e);
		}
		return result;
	}
		
	public String transform_into_arff(String disease_name,String output_filename)
	{
		try
		{
			String r=transform_into_arff(disease_name);
			(new File(r)).renameTo(new File(output_filename));
		}
		catch(Exception e)
		{
			System.out.println("get_health_data transform_into_arff exception:"+e);
		}
		return output_filename;
	}
	
	public String transform_into_arff(String disease_name)
	{
		String result="";
		try
		{
			person_rule_match a=new person_rule_match("PredictionModels/"+disease_name+"/"+disease_name+"_file_logs");
			String seq_filename=a.get_match_result("PredictionModels/"+disease_name+"/"+disease_name+"_order_logs",data_filename);
			String static_filename=get_static_part(disease_name,"PredictionModels/"+disease_name+"/"+disease_name+"_MappingInfo.txt",data_filename);
			System.out.println(static_filename);
			result=arff_combine(disease_name,seq_filename,static_filename);
		}
		catch(Exception e)
		{
			System.out.println("get_health_data transform_into_arff exception:"+e);
		}
		return result;
	}
	
	private String arff_combine(String disease_name,String seq_f,String sta_s)
	{
		String result="tmp/temp_combined.arff";
		try
		{
			BufferedReader br=new BufferedReader(new FileReader("PredictionModels/"+disease_name+"/"+disease_name+"_combined_arff.arff"));
			System.out.println(seq_f);
			BufferedWriter bw=new BufferedWriter(new FileWriter(result));
			String buffer="";
			while((buffer=br.readLine())!=null)
			{
				if(buffer.trim().equals("@data") || buffer.trim().equals("@Data") || buffer.trim().equals("@DATA"))
					break;
				else
					bw.write(buffer+"\n");
			}
			bw.write("@data\n");
			br.close();
			System.out.println("Kerker...");
			br=new BufferedReader(new FileReader(seq_f));
			while((buffer=br.readLine())!=null)
			{
				if(buffer.equals("@data"))
				{
					while((buffer=br.readLine()).trim().equals(""));
					break;
				}
			}			
			bw.write(buffer.substring(0,buffer.lastIndexOf(",")+1)+sta_s+buffer.substring(buffer.lastIndexOf(",")+1)+"?\n");
			br.close();
			bw.close();			
		}
		catch(Exception e)
		{
			System.out.println("get_health_data_transform arff_combine exception:"+e);
		}
		return result;
	}
	
	public String get_static_part(String disease_name,String ref_file,String data_file)
	{
		String result="";
		try
		{
			//get header for numeric check
			Vector<String> numeric_set=new Vector<String>();
			BufferedReader br=new BufferedReader(new FileReader("PredictionModels/"+disease_name+"/"+disease_name+"_combined_arff.arff"));
			String buffer="";
			while((buffer=br.readLine())!=null)
			{
				if(buffer.trim().equals("@data") || buffer.trim().equals("@Data") || buffer.trim().equals("@DATA"))
					break;
				else if(!buffer.trim().equals("") && buffer.indexOf("@attribute")==0)
				{
					String tmp_string_set[]=buffer.split(" ");
					if(tmp_string_set[2].equals("numeric"))
					{
						numeric_set.add(tmp_string_set[1]); //ex. attri_9
					}
				}
			}
			br.close();
			//fetching
			HashMap<String, String> last_values=new HashMap<String,String>();
			//BufferedReader br=new BufferedReader(new FileReader(data_file));
			br=new BufferedReader(new FileReader(data_file));			
			while((buffer=br.readLine())!=null)
			{
				if(buffer.indexOf(":")>0)
				{
					String temp_item_filename=buffer.substring(0,buffer.indexOf(":"));					
					String temp_item_sequence=buffer.substring(buffer.indexOf(":")+1);					
					if(buffer.indexOf(",")>=0)
					{
						String all_items[]=temp_item_sequence.split(",");
						if(all_items.length>1)
							last_values.put(temp_item_filename,all_items[all_items.length-2]);
						else
							last_values.put(temp_item_filename,"?");
					}
					else
						last_values.put(temp_item_filename,"?");
				}
			}
			br.close();			
			HashMap<String,String> value_matching_table=new HashMap<String,String>(); //ex. "sqlfile_output_基本資料與身體評估_BMI_timeseries.txt_30.8" , "91"
			br=new BufferedReader(new FileReader(ref_file));
			while((buffer=br.readLine())!=null)
			{
				if(buffer.indexOf("---> actual name:")>0)
				{
					String attribute_sn=buffer.substring(0,buffer.indexOf("---> actual name:")).trim();
					attribute_sn=attribute_sn.substring(attribute_sn.indexOf(" ")+1);
					String attribute_filename=buffer.substring(buffer.indexOf(":")+1,buffer.indexOf("{")).trim();
					if(numeric_set.contains(attribute_sn))
					{
						numeric_set.remove(attribute_sn);
						numeric_set.add(attribute_filename);
					}
				}
				else if(buffer.indexOf("---> mapping value:")>0)
				{
					value_matching_table.put(	buffer.substring(0,buffer.indexOf(" ")).trim(),
								buffer.substring(buffer.indexOf(":")+1).trim()
					);
				}
			}
			br.close();
			//matching in order[in while loop] and matching for value [in third-depth if block]!						
			br=new BufferedReader(new FileReader(ref_file));
			while((buffer=br.readLine())!=null)
			{
				if(buffer.indexOf("@")==0 && buffer.indexOf("@attribute class")<0)
				{
					String temp_item=buffer.substring(buffer.indexOf(":")+1,buffer.indexOf("{")).trim();
					if(last_values.get(temp_item)!=null)
					{
						if(numeric_set.contains(temp_item))
						{
							System.out.println("N "+temp_item+"-->"+last_values.get(temp_item));
							temp_item=last_values.get(temp_item);							
						}
						else
						{
							String for_matching_string=temp_item+"_"+(String)last_values.get(temp_item);
							if(value_matching_table.get(for_matching_string)!=null)
							{
								temp_item=(String)value_matching_table.get(for_matching_string);
								System.out.println(for_matching_string+"-->"+temp_item);
							}
							else
							{
								System.out.println("* "+temp_item+" -->"+last_values.get(temp_item));
								//temp_item=last_values.get(temp_item);
								temp_item="?";		//no match
							}
						}
					}
					else
						temp_item="?";
					result+=temp_item+",";
				}
			}
			br.close();
		}
		catch(Exception e)
		{
			System.out.println("get_health_data get_static_part exception:"+e);
		}
		return result;
	}
	
	public static void main(String args[])
	{
		//[data_dir] [patient_chkno] [disease_name]
		get_health_data a=new get_health_data(args[0]);
		a.get_one_patient(args[1]);
		System.out.println(a.transform_into_arff(args[2],"tmp/"+args[1]+"_"+args[2]+".arff"));
	}
}