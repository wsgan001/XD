package data_format;
import java.io.*;
import java.util.Vector;
import java.util.HashMap;
import java.util.Random;
import java.util.Calendar;
import java.util.Iterator;

//
public class data_separator
{
	String filename;
	public boolean data_repeat=false;
	private file_info my_file;
	public data_separator(String f)
	{
		filename=f;
		my_file=new file_info(f);
	}

	public Vector separate_arff_tt(double training_rate)
	{
		//must balance for class label
		Vector<String> result_files=new Vector<String>();
		try
		{
			Random ran_gen=new Random(Calendar.getInstance().getTimeInMillis());
			String training_filename=filename+"_train";
			String testing_filename=filename+"_test";
			
//			System.out.println("1");
			HashMap<String,Integer> remain_count=new HashMap<String,Integer>();
			Iterator ir=my_file.CLASS_COUNT_SET.keySet().iterator();
			while(ir.hasNext())
			{
				String temp_class_name=(String)ir.next();
				int temp_class_data_count=((Integer)my_file.CLASS_COUNT_SET.get(temp_class_name)).intValue();
				int test_count=(int)(temp_class_data_count*(1.0-training_rate));
				int train_count=(int)(temp_class_data_count-test_count);
				remain_count.put(temp_class_name+"_train",new Integer(train_count));
				remain_count.put(temp_class_name+"_test",new Integer(test_count));
			}
			
//			System.out.println("2");
			BufferedReader br=new BufferedReader(new FileReader(filename));
			BufferedWriter bw_train=new BufferedWriter(new FileWriter(training_filename));
			BufferedWriter bw_test=new BufferedWriter(new FileWriter(testing_filename));
			String buffer="";
			while((buffer=br.readLine())!=null)
			{
				if(buffer.indexOf("@")==0 || buffer.trim().length()==0)
				{
					bw_train.write(buffer+"\n");
					bw_test.write(buffer+"\n");
				}
				else
				{
					transaction temp_tran=transaction_parser.auto_parser(buffer);
					String temp_class=(String)temp_tran.ITEM_SET.get(temp_tran.ITEM_SET.size()-1);
					int train_remain=((Integer)remain_count.get(temp_class+"_train")).intValue();
					int test_remain=((Integer)remain_count.get(temp_class+"_test")).intValue();
					double r=ran_gen.nextDouble();
					boolean train_flg=true;
					if(r>training_rate)
					{
						if(train_remain<=0 && test_remain<=0)
							continue;
						if(test_remain>0)
							train_flg=false;
					}
					
					if(train_flg)
					{
						train_remain-=1;
						bw_train.write(buffer+"\n");
					}
					else
					{
						test_remain-=1;
						bw_test.write(buffer+"\n");
					}
					remain_count.put(temp_class+"_train",new Integer(train_remain));
					remain_count.put(temp_class+"_test",new Integer(test_remain));
				}
			}
//			System.out.println("3");
			br.close();
			bw_train.close();
			bw_test.close();
			result_files.add(training_filename);
			result_files.add(testing_filename);
		}
		catch(Exception e)
		{
			System.out.println("data_separator separate_arff_tt exception:"+e);
		}
		return result_files;
	}	
	
	//for training and testing
	public Vector<String> separate_tt(double training_rate)
	{
		//must balance for class label
		Vector<String> result_files=new Vector<String>();
		try
		{
			Random ran_gen=new Random(Calendar.getInstance().getTimeInMillis());
			String training_filename=filename+"_train";
			String testing_filename=filename+"_test";
			
//			System.out.println("1");
			HashMap<String,Integer> remain_count=new HashMap<String,Integer>();
			Iterator ir=my_file.CLASS_COUNT_SET.keySet().iterator();
			while(ir.hasNext())
			{
				String temp_class_name=(String)ir.next();
				int temp_class_data_count=((Integer)my_file.CLASS_COUNT_SET.get(temp_class_name)).intValue();
				int test_count=(int)(temp_class_data_count*(1.0-training_rate));
				int train_count=(int)(temp_class_data_count-test_count);
				remain_count.put(temp_class_name+"_train",new Integer(train_count));
				remain_count.put(temp_class_name+"_test",new Integer(test_count));
			}
			
//			System.out.println("2");
			BufferedReader br=new BufferedReader(new FileReader(filename));
			BufferedWriter bw_train=new BufferedWriter(new FileWriter(training_filename));
			BufferedWriter bw_test=new BufferedWriter(new FileWriter(testing_filename));
			String buffer="";
			while((buffer=br.readLine())!=null)
			{
				transaction temp_tran=transaction_parser.auto_parser(buffer);
				String temp_class=temp_tran.CLASS_LABEL;
				int train_remain=((Integer)remain_count.get(temp_class+"_train")).intValue();
				int test_remain=((Integer)remain_count.get(temp_class+"_test")).intValue();
				double r=ran_gen.nextDouble();
				boolean train_flg=true;
				if(r>training_rate)
				{
					if(train_remain<=0 && test_remain<=0)
						continue;
					if(test_remain>0)
						train_flg=false;
				}
				
				if(train_flg)
				{
					train_remain-=1;
					bw_train.write(buffer+"\n");
				}
				else
				{
					test_remain-=1;
					bw_test.write(buffer+"\n");
				}
				remain_count.put(temp_class+"_train",new Integer(train_remain));
				remain_count.put(temp_class+"_test",new Integer(test_remain));
			}
//			System.out.println("3");
			br.close();
			bw_train.close();
			bw_test.close();
			result_files.add(training_filename);
			result_files.add(testing_filename);
		}
		catch(Exception e)
		{
			System.out.println("data_separator separate_tt exception:"+e);
		}
		return result_files;
	}
	
	class file_info
	{
		public int LINE_COUNT=0;
		public int CLASS_COUNT=0;
		public HashMap<String,Integer> CLASS_COUNT_SET=new HashMap<String,Integer>();
//		public Vector<String> ITEM_SET=new Vector<String>();
		public file_info(String f)
		{
			try
			{
				BufferedReader br=new BufferedReader(new FileReader(f));
				String buffer="";
				int lc=0;
				//int cc=0;
				//System.out.println("1.1");
				if(br.readLine().indexOf("@")==0)
				{
					while((buffer=br.readLine())!=null)
					{
						if(buffer.indexOf("@")==0 || buffer.trim().length()==0)
						{
						}
						else
						{							
							transaction temp_tran=transaction_parser.auto_parser(buffer);
							String temp_class_label=(String)temp_tran.ITEM_SET.get(temp_tran.ITEM_SET.size()-1);
							if(CLASS_COUNT_SET.get(temp_class_label)!=null)
							{
								int temp=((Integer)CLASS_COUNT_SET.get(temp_class_label)).intValue();
								CLASS_COUNT_SET.put(temp_class_label,new Integer(temp+1));
							}
							else
							{
								CLASS_COUNT_SET.put(temp_class_label,new Integer(1));
							}
							lc++;
						}
					}
				}
				else
				{
					while((buffer=br.readLine())!=null)
					{
						transaction temp_tran=transaction_parser.auto_parser(buffer);
						if(CLASS_COUNT_SET.get(temp_tran.CLASS_LABEL)!=null)
						{
							int temp=((Integer)CLASS_COUNT_SET.get(temp_tran.CLASS_LABEL)).intValue();
							CLASS_COUNT_SET.put(temp_tran.CLASS_LABEL,new Integer(temp+1));
						}
						else
						{
							CLASS_COUNT_SET.put(temp_tran.CLASS_LABEL,new Integer(1));
						}
						lc++;			
					}
				}
				//System.out.println("1.2");
				br.close();
				LINE_COUNT=lc;
				CLASS_COUNT=CLASS_COUNT_SET.size();
			}
			catch(Exception e)
			{
				System.out.println("data_separator file_info exception:"+e);
			}			
		}
	}
	
	//for test
	public static void main(String args[])
	{
		if(args[0].equals("-test"))
		{
			data_separator a=new data_separator("all_test.txt");
			Vector xx=a.separate_tt(0.7);
			System.out.println(xx);
		}
		else if(args[0].equals("-m")) //my format
		{
			data_separator a=new data_separator(args[1]);
			Vector r=a.separate_tt(Double.parseDouble(args[2]));
			System.out.println("Separate File["+args[1]+"] by "+args[2]+" into Files");
			System.out.println(r);
		}
		else if(args[0].equals("-a")) //arff format
		{
			data_separator a=new data_separator(args[1]);
			Vector r=a.separate_arff_tt(Double.parseDouble(args[2]));
			System.out.println("Separate File["+args[1]+"] by "+args[2]+" into Files");
			System.out.println(r);
		}
	}
}