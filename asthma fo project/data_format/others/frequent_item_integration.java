/**
	This is for preprocessing of formal sequential pattern mining
	It transforms all frequent item-sets into single symbol(number).
	And remove all non-frequent items
	
	Input: the sequence data with format<file_type>:	1. [uid]:[class]:[itemset]  ex. user512:class2:beer,diaper,
								2. [class]:[sequence using "()" to mark time points]
	Processing: If <file_type=1> to change it into <file_typ=2>, discover frequent the item or the itemset, which maybe replaced with new item name
	* 			remove all non-frequent ones and save all into a file.
	Output: the data which just consists of frequent itemset
		and all itemsets are replaced with symbols(numbers)
		ex. format [class]:[symbol sequence]
**/

import java.io.*;

public class frequent_item_integration
{
	String INPUT_FILENAME="";
	String MATCHING_TABLE_FILENAME="";
	String OUTPUT_FILENAME="";
	HashMap<String,Integer> frequency_table=new HashMap<String,Integer>();
	// Just user mode now
	int file_type=1;	//file_type:1 or 2
	
	
	public frequent_item_integration(String f)
	{
		INPUT_FILENAME=f;
		MATCHING_TABLE_FILENAME=(new File(f)).getParentFile()+"matching_table_"+(new File(f)).getName();
		OUTPUT_FILENAME=(new File(f)).getParentFile()+"processed_"+(new File(f)).getName();		
	}
	
	public void do()
	{
		try
		{
			String temp_filename=preprocessing_for_format();	//combine transactions whichh are belong to the same user.
			frequent_discover();
			create_matching_table();
			output_into_file();
		}
		catch(Exception e)
		{
			System.out.println("frequent_item_integration do exception:"+e);
		}		
		frequency_scan(temp_filename);
		remove_nonfrequent();
		make_result_file(OUTPUT_FILENAME);		
	}
	
	private Vector<String> get_all_user_class(String f)
	{
		Vector<String> result=new Vector<String>();
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(f));
			String buffer="";
			while((buffer=br.readLine())!=null)
			{
				buffer=buffer.substring(0,buffer.lastIndexOf(":"));
				if(!result.contains(buffer))
					result.add(buffer);
			}
			br.close();
		}
		catch(Exception e)
		{
			SYstem.out.println("frequent_item_integration get_all_user_class exception:"+e);
		}
		return result;
	}
	
	private String preprocessing_for_format()
	{
		String result_filename="";
		try
		{
			//If there is no tmp directort, create it!!
			File temp_dir=new File("/tmp");
			if(!temp_dir.isDirectory())
				temp_dir.createNewFile();
			
			//check format
			BufferedReader br=new BufferedReader(new FileReader(INPUT_FILENAME));
			String buffer="";
			buffer=br.readLine();
			if(buffer.indexOf("(")!=-1 and buffer.indexOf(")")!=-1)
			{
				//file_type=2
				return INPUT_FILENAME;
			}
			br.close();
			
			Vector<String> all_user_class=get_all_user_class(INPUT_FILENAME); //if the same user with different class, they are different
			
			HashMap<String,BufferedWriter> user_class_set=new HashMap<String,BufferedWriter>();
			HashMap<String,String> filename_set=new HashMap<String,String>();
			br=new BufferedReader(new FileReader(sorted_file));
			while((buffer=br.readLine())!=null)			
			{
				String uc=buffer.substring(0,buffer.lastIndexOf(":"));
				BufferedWriter temp_bw;
				if(!user_class_set.containsKey(uc))
				{
					File temp_filename=File.createTempFile(uc.substring(0,uc.indexOf(":")),uc.substring(uc.indexOf(":")+1)+".tmp",temp_dir);
					filename_set.put(uc,temp_filename.getName());
					temp_bw=new BufferedReader(new FileReader(temp_filename));
				}
				else
					temp_bw=(BufferedWriter)user_class_set.get(uc);					
				temp_bw.write(buffer.substring(buffer.indexOf(":")+1));
				user_class_set.put(uc.temp_bw);
			}
			
			Iterator ir=user_class_set.getKeySet().iterator();
			while(ir.hasNext())
			{
				((BufferedWriter)user_class_set.get((String)ir.next())).close();
			}
			br.close();
			
			result_filename="tmp/"+(File.createTempFile("user_based",".tmp",temp_dir)).getName();
			
			BufferedWriter temp_bw=new BufferedWriter(new FileReader(result_filename));
			while(ir.hasNext())
			{
				String uc=(String)ir.next();
				temp_bw.write(uc+":");
				br=new BufferedReader(new FileReader((String)filename_set.get(uc)));
				String buffer="";
				while((buffer=br.readLine())!=null)
				{
					temp_bw.write("("+buffer+"),");
				}
				temp_bw.write("\n");
				br.close();
			}
			temp_bw.close();
			
		}
		catch(Exception e)
		{
			System.out.println("frequemt_item_integration preprocessing_for_format exception:"+e);
			return INPUT_FILENAME;
		}
		return result_filename;
	}
	
	public void frequent_scan(String f)
	{
		try
		{
			
			BufferedReader br=new BufferedReader(new FileReader(f));
			String buffer="";
			while((buffer.br.readLine())!=null)
			{
				transaction temp_transaction=transaction_parser.user_class_sequence_parser(buffer);
				for(int i=0;i<temp_transaciton.ITEM_SET.size();i++)
				{
					String temp_item=(String)temp_transaction.ITEM_SET.get(i);
					Set<String> no_repeat_item_set=new Set<String>();
					no_repeat_item_set.addAll(temp_transaction.ITEM_SET);
					Iterator ir=no_repeart_item_set.iterator();
					while(ir.hasNext())
					{
						String item_name=(String)ir.next();
						if(frequency_table.containsKey(item_name))
						{
							int temp_count=((Integer)frequency_table.get(item_name)).intValue()+1;
							frequency_table.put(item_name,new Integer(temp_count));
						}
					}
				}
			}
			br.close();
		}
		catch(Exception e)
		{
			System.out.println("frequent_item_integration frequent_scan exception:"+e);
		}
	}
	
	public void remove_nonfrequent()
	{
		try
		{
		}
		catch(Exception e)
		{
			System.out.println("frequent_item_integration remove_nonfrequent exception:"+e);
		}
	}
	
	public String make_result_file(String f)
	{
		try
		{
		}
		catch(Exception e)
		{
			System.out.println("frequent_item_integration make_result_file exception:"+e);
		}
	}
}
