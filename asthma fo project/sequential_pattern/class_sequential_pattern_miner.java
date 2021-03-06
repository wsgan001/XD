package sequential_pattern;
import java.io.*;
import java.util.HashMap;
import java.util.Vector;
import java.util.Iterator;

import data_format.*;

public class class_sequential_pattern_miner
{
	public boolean ITEM_REPEAT=true;
	public String result_filename="";
	public String target_dir="tmp";
	public Vector<String> results=new Vector<String>();
	public String file_logs="";
	public class_sequential_pattern_miner()
	{		
	}
	
	public void file_mining(String f, double ms)   //return : [result_file] [pattern_sn_file] [feature-ized file] [arff_file]
	{
		//Vector<String> result=new Vector<String>();
		
		System.out.println(" file path: "+f);		
		file_logs+="[file path] "+f+"\n";
		try
		{
			HashMap<String,String> class_files=separate_with_class(f);
			Iterator ir=class_files.keySet().iterator();
			Vector<String> each_result_files=new Vector<String>();
			while(ir.hasNext())
			{
				String temp_class=(String)ir.next();
				//System.out.println("Class "+temp_class+"EEEE");
				String temp_filename=class_files.get(temp_class);
				sequential_pattern temp_miner=new sequential_pattern(temp_filename,ms);
				temp_miner.ITEM_REPEAT=this.ITEM_REPEAT;
				temp_miner.start();
				each_result_files.add(temp_miner.result_filename);
				//System.out.println(temp_miner.result_filename);
			}
			String temp_pattern_filename=pattern_combine(each_result_files);
			this.result_filename=temp_pattern_filename;
			results.add(temp_pattern_filename);
			file_logs+="[class pattern file] "+temp_pattern_filename+"\n";
			
			String rname=(new File(f)).getName();
			if(rname.lastIndexOf(".")>0)
				rname=rname.substring(0,rname.indexOf("."));
			File table_file=File.createTempFile(rname,"table.tmp",new File(target_dir));
			File pattern_sn_file=File.createTempFile(rname,"pattern_sn.tmp",new File(target_dir));
			File arff_file=File.createTempFile(rname,"table.arff",new File(target_dir));
			table_file.deleteOnExit();
			//pattern_sn_file.deleteOnExit();
				
			String table_filename=table_file.getAbsolutePath();
			String pattern_sn_filename=pattern_sn_file.getAbsolutePath();
			String arff_filename=arff_file.getAbsolutePath();
			file_logs+="[table format file] "+table_filename+"\n";
			file_logs+="[pattern_sn_filename] "+pattern_sn_filename+"\n";
			file_logs+="[arff filename] "+arff_filename+"\n\n";

			pattern_replace replacer=new pattern_replace(temp_pattern_filename,f,table_filename,pattern_sn_filename);
			pattern_replace.table_format(table_filename,arff_filename);
		}
		catch(Exception e)
		{
			System.out.println("class_sequential_pattern_miner mining exception:"+e);
		}
	}
	
	public String pattern_combine(Vector<String > af)
	{
		String result="";		
		try
		{
			Vector<String> all_patterns=new Vector<String>();
			Vector<String> repeat_patterns=new Vector<String>();
			for(int i=0;i<af.size();i++)
			{
				Vector<String> temp_vector=new Vector<String>();
				BufferedReader temp_br=new BufferedReader(new FileReader(af.get(i)));
				String buffer="";
				boolean read_flg=false;
				while((buffer=temp_br.readLine())!=null)
				{
					if(buffer.indexOf("#")==0)
					{
						if(buffer.indexOf("#Large")==0)
							read_flg=true;
						else if(buffer.indexOf("#Candidate")==0)
							read_flg=false;
					}
					else
					{
						if(read_flg)
						{
							buffer=buffer.substring(0,buffer.indexOf(":"));
							if(!all_patterns.contains(buffer))
								all_patterns.add(buffer);
							temp_vector.add(buffer);
						}
					}
				}
				temp_br.close();
				if(i==0)
					repeat_patterns.addAll(temp_vector);
				else
				{
					//if(!repeat_patterns.retainAll(temp_vector))
					//	throw new Exception("Intersection Error!!!");
					repeat_patterns=intersection(repeat_patterns,temp_vector);
				}
			}			
			
			HashMap<Integer,Vector<String>> kind=new HashMap<Integer,Vector<String>>();
			for(int i=0;i<all_patterns.size();i++)
			{
				String temp_pattern=all_patterns.get(i);
				if(repeat_patterns.contains(temp_pattern))
					continue;				
				String[] items=temp_pattern.split(",");
				Vector<String> temp_vector=null;
				if(kind.get(new Integer(items.length))!=null)
				{
					temp_vector=kind.get(new Integer(items.length));
				}
				else
				{
					temp_vector=new Vector<String>();
					kind.put(new Integer(items.length),temp_vector);
				}
				temp_vector.add(temp_pattern);
			}
			
			File result_file=File.createTempFile("combine_pattern",".tmp",new File(target_dir));
			//result_file.deleteOnExit();
			result=result_file.getAbsolutePath();
			BufferedWriter bw=new BufferedWriter(new FileWriter(result));			
			for(int i=1;i<kind.size();i++)
			{
				bw.write("#Large "+i+"\n");
				if(kind.get(new Integer(i))!=null)
				{
					Vector<String> temp_vector=kind.get(new Integer(i));
					for(int j=0;j<temp_vector.size();j++)
					{
						bw.write(temp_vector.get(j)+":0\n");
					}
				}				
			}
			bw.write("#Class Repeat Part:\n"+repeat_patterns.toString());
			bw.close();
		}
		catch(Exception e)
		{
			System.out.println("class_sequential_pattern_miner pattern_combine exception:"+e);
		}
		return result;
	}
	
	private Vector<String> intersection(Vector<String> a, Vector<String> b)
	{
		Vector<String> result=new Vector<String>();
		try
		{
			for(int i=0;i<a.size();i++)
			{
				if(b.contains(a.get(i)))
					result.add(a.get(i));
			}
		}
		catch(Exception e)
		{
			System.out.println("class_sequential_pattern_miner intersection exception:"+e);
		}
		return result;
	}
	
	public void dir_mining(String f,double ms)
	{
		try
		{
			results.clear();
			File d=new File(f);
			File[] files=d.listFiles();
			for(int i=0;i<files.length;i++)
			{
				System.out.println(files[i]+", min_sup:"+ms);
				file_mining(files[i].getAbsolutePath(), ms);
				System.out.println(" files[i].getAbsolutePath() = "+files[i].getAbsolutePath());
				
			}
			
			
			// 20091025
			// 寫出檔案！			
			del_files df1 = new del_files(d.getParent()+"\\SP_features\\");	// 先清空「指定目錄夾裡」								
			BufferedWriter bw = new BufferedWriter(new FileWriter(d.getParent()+"\\SP_features\\feature_id.txt"));
			String str="";
			for(int i=0;i<files.length;i++){
				// 輸出「字串內容至文字檔」
				// Feature ID是從0開始編號
				str = " Feature ID: "+(i)+" ||  File Path = "+files[i].getName();
				bw.write(str); 
				bw.newLine(); 
			}			
			
			bw.close();
			
		}
		catch(Exception e)
		{
			System.out.println("class_sequential_pattern_miner dir_mining exception:"+e);
		}
	}
	
	public HashMap<String,String> separate_with_class(String f)
	{
		HashMap<String,String> result=new HashMap<String,String>();
		try
		{
			
			HashMap<String, BufferedWriter> bw_set=new HashMap<String,BufferedWriter>();
			BufferedReader br=new BufferedReader(new FileReader(f));
			
			BufferedWriter temp_bw=null;
			String buffer="";
			
		
			while((buffer=br.readLine())!=null)
			{
				if(buffer.trim().equals(""))
					continue;
		
			
				transaction temp_tran=transaction_parser.class_sequence_parser(buffer);
				if(bw_set.get(temp_tran.CLASS_LABEL)==null)
				{	
					//System.out.println(1);
					File temp_file=File.createTempFile("subclass_",".tmp",new File(target_dir));
					//System.out.println(2);
					temp_file.deleteOnExit();
					
					
					//System.out.println(3);
					String temp_filename=temp_file.getAbsolutePath();
					temp_bw=new BufferedWriter(new FileWriter(temp_filename));
					//System.out.println(4);
					bw_set.put(temp_tran.CLASS_LABEL,temp_bw);
					result.put(temp_tran.CLASS_LABEL,temp_filename);
				}
				else
					temp_bw=bw_set.get(temp_tran.CLASS_LABEL);
				
					
				temp_bw.write(buffer+"\n");
			}
			br.close();
											
			Iterator ir=bw_set.keySet().iterator();
			while(ir.hasNext())
			{
				String temp_class=(String)ir.next();
				((BufferedWriter)bw_set.get(temp_class)).close();
			}
		
		}
		catch(Exception e)
		{
			System.out.println("class_sequential_pattern_miner separate_with_class Exception:"+e);
		}
		return result;
	}
	
	
	String source_folder="";
	
	public class_sequential_pattern_miner(String check_d, String source_folder, Double min_sup, String output_folder){
		
		try{
			
			//a.target_dir=args[3];
			this.target_dir = output_folder;
			this.source_folder = source_folder;
			//a.dir_mining(args[1],Double.parseDouble(args[2]));
			dir_mining(source_folder,min_sup);
			
			System.out.println("All pattern files are save in directory "+target_dir);
		
		}catch(Exception e){
			System.out.println(" Error about printing the file in class_sequential_pattern_miner function (in class_sequential_pattern_miner.java): "+e.toString());	
		}
		
	}
	
	
	
	//for testing
	public static void main(String args[])
	{		
		class_sequential_pattern_miner a=new class_sequential_pattern_miner();
		if(args[0].equals("-d"))
		{
			if(args.length>3)
				a.target_dir=args[3];
			a.dir_mining(args[1],Double.parseDouble(args[2]));
			System.out.println("All pattern files are save in directory "+a.target_dir);
		}
		else	
		{	
			a.file_mining(args[0],Double.parseDouble(args[1]));
			System.out.println(a.results.toString());
		}
	}
}