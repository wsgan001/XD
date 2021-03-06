package health_exam;
import java.io.*;
import java.util.Vector;
import java.util.HashMap;

//Assumption: The data in Each arff is ordered by user_id and keeps the same quantity of transactions.
//Do: Add all data of each user together. 
public class arff_together
{
	public HashMap<String,String> name_match_table=new HashMap<String,String>();
	public String order_log="";
	public String order_log2="";

	public arff_together()
	{
		order_log="";
		order_log2="";
	}

	public arff_together(String source_folder, String summary_arffs_file_name)
	{
		
		//arff_together a=new arff_together();
		//join_dir(args[0],args[1]);
		order_log="";
		order_log2="";
		join_dir(source_folder, summary_arffs_file_name);
		
	}
	
	public void join_dir(String d, String of)
	{
		try
		{
			File dir=new File(d);
			File[] files=dir.listFiles();
			Vector<String> all_files=new Vector<String>();
			for(int i=0;i<files.length;i++)
			{
				if(files[i].getName().indexOf(".arff")>=0)
					all_files.add(files[i].getAbsolutePath());
			}
			System.out.println(all_files.toString());
			join(all_files,of);
		}
		catch(Exception e)
		{
			System.out.println("arff together join_dir exception:"+e);
		}
	}
	
	public void join(Vector<String> fi, String fo)
	{
		try
		{
			//Prepare outputfile
			BufferedWriter bw=new BufferedWriter(new FileWriter(fo));
			bw.write("@relation Mix-Relations\n");	
			Vector<BufferedReader> all_br=new Vector<BufferedReader>();
			String header_string="";
			String class_string="";
			for(int i=0;i<fi.size();i++)
			{
				//Ready each file to @data
				BufferedReader br=new BufferedReader(new FileReader(fi.get(i)));
				order_log+=fi.get(i)+"\n";
				order_log2+=fi.get(i)+" has ";
				int att_count=0;
				String buffer="";
				String relation_name="";
				HashMap<String,String> attribute_set=new HashMap<String,String>();
				while((buffer=br.readLine())!=null)
				{
					if(buffer.indexOf("%")==0)
						continue;
					else if(buffer.indexOf("@relation")==0)
					{
						relation_name=buffer.substring(buffer.indexOf(" ")+1);
						if(name_match_table.get(relation_name)!=null)
							relation_name=name_match_table.get(relation_name);
						else
						{
							name_match_table.put(relation_name,"F"+Integer.toString(i));
							relation_name="F"+Integer.toString(i);
						}
					}
					else if(buffer.indexOf("@attribute")==0)
					{
						String[] items=buffer.split(" ");
						if(!items[1].equals("CLASS") && !items[1].equals("class") && !items[1].equals("Class"))
						{
							for(int j=0;j<items.length;j++)
							{
								if(j==1)
								{
									header_string+=relation_name+"_"+items[1]+" ";
								}
								else
									header_string+=items[j]+" ";
							}
							header_string+="\n";
							att_count++;
						}
						else
						{
							if(class_string.equals(""))
								class_string=buffer;
						}
					}
					else if(buffer.indexOf("@data")==0)
					{
						all_br.add(br);
						break;
					}
				}
				order_log2+=(new Integer(att_count)).toString()+" features\n";
			}
			//write header
			bw.write(header_string);
			//System.out.println(header_string);
			bw.write(class_string+"\n\n");
			//System.out.println(class_string+"\n\n");
			bw.write("@data\n\n");
			//System.out.println("@data\n\n");
			
			Vector<String> temp_buffer=new Vector<String>();
			BufferedReader temp_br=null;
			int transaction_count=0;
			while(true)
			{
				String temp_transaction="";
				String buffer="";
				boolean check_flg=true;
				for(int i=0;i<all_br.size();i++)
				{
					temp_br=all_br.get(i);					
					if((buffer=temp_br.readLine())!=null)
					{
						if(i==(all_br.size()-1))
							temp_transaction+=buffer+"\n";
						else
						{
							if(buffer.indexOf(",")>=0)
								temp_transaction+=buffer.substring(0,buffer.lastIndexOf(",")+1);
						}
					}
					else
						check_flg=false;
				}
				if(check_flg)
				{
					bw.write(temp_transaction);
					transaction_count++;
					if(transaction_count%1000==0)
						System.out.print(".");
						
					if(transaction_count>500000)
						break;
				}
				else
					break;
			}
			
			for(int i=0;i<all_br.size();i++)
				(all_br.get(i)).close();
			bw.close();
		}
		catch(Exception e)
		{
			System.out.println("arff_together join exception:"+e);
		}
	}
	
	
	public static void main(String args[])
	{
		arff_together a=new arff_together();
		a.join_dir(args[0],args[1]);
	}
	
}

