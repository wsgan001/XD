package data_format;
import java.io.*;
import java.util.Vector;
import java.util.HashMap;
import java.util.Random;
import java.util.Calendar;
import java.util.Iterator;

// separate mts data(with class) into training and testing part
public class mts_data_separator
{
	String filename;
	public boolean data_repeat=false;
	private mts_file_info my_file;
	public mts_data_separator(String f)
	{
		filename=f;
		my_file=new mts_file_info(f);
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
			
			System.out.println("1");
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
			System.out.println(remain_count);
			System.out.println("2");
			BufferedReader br=new BufferedReader(new FileReader(filename));
			System.out.println(filename);
			BufferedWriter bw_train=new BufferedWriter(new FileWriter(training_filename));
			System.out.println(training_filename);
			BufferedWriter bw_test=new BufferedWriter(new FileWriter(testing_filename));
			System.out.println(testing_filename);
			String buffer="";
			Vector<String> temp_mts=new Vector<String>();
			String temp_class="";
			while(true)
			{
				buffer=br.readLine();
				if(buffer==null || buffer.equals(""))
				{
					if(temp_mts.size()!=0)
					{
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
							bw_train.write(temp_class+":\n");
							for(int i=0;i<temp_mts.size();i++)
								bw_train.write(temp_mts.get(i)+"\n");
							bw_train.write("\n");
						}
						else
						{
							test_remain-=1;
							bw_test.write(temp_class+":\n");
							for(int i=0;i<temp_mts.size();i++)
								bw_test.write(temp_mts.get(i)+"\n");
							bw_test.write("\n");
						}
						remain_count.put(temp_class+"_train",new Integer(train_remain));
						remain_count.put(temp_class+"_test",new Integer(test_remain));
					}

					if(buffer==null)
						break;
					temp_class="";
					temp_mts.clear();
				}
				else
				{
					if(buffer.indexOf(":")>=0)
					{
						temp_class=buffer.substring(0,buffer.indexOf(":"));
						if(!buffer.substring(buffer.indexOf(":")+1).trim().equals(""))
							temp_mts.add(buffer.substring(buffer.indexOf(":")+1));
					}
					else
						temp_mts.add(buffer);
				}
			}
			System.out.println("3");
			br.close();
			bw_train.close();
			bw_test.close();
			result_files.add(training_filename);
			result_files.add(testing_filename);
		}
		catch(Exception e)
		{
			System.out.println("mts_data_separator separate_tt exception:"+e);
		}
		return result_files;
	}
	
	class mts_file_info
	{
		public int LINE_COUNT=0;
		public int CLASS_COUNT=0;
		public HashMap<String,Integer> CLASS_COUNT_SET=new HashMap<String,Integer>();
//		public Vector<String> ITEM_SET=new Vector<String>();
//
		public mts_file_info(String f)
		{
			try
			{
				BufferedReader br=new BufferedReader(new FileReader(f));
				String buffer="";
				String temp_class="";
				int lc=0;
				while(true)
				{
					buffer=br.readLine();
					if(buffer==null || buffer.equals(""))
					{
						if(CLASS_COUNT_SET.get(temp_class)!=null)
						{
							int temp=((Integer)CLASS_COUNT_SET.get(temp_class)).intValue();
							CLASS_COUNT_SET.put(temp_class,new Integer(temp+1));
						}
						else
						{
							CLASS_COUNT_SET.put(temp_class,new Integer(1));
						}
						lc++;

						if(buffer==null)
							break;
					}
					else
					{
						if(buffer.indexOf(":")>=0)
						{
							temp_class=buffer.substring(0,buffer.indexOf(":"));
						}
					}
				}
				//System.out.println("1.2");
				br.close();
				LINE_COUNT=lc;
				CLASS_COUNT=CLASS_COUNT_SET.size();
			}
			catch(Exception e)
			{
				System.out.println("mts_data_separator mts_file_info exception:"+e);
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
		else if(args[0].equals("-mts"))
		{
			mts_data_separator a=new mts_data_separator(args[1]);
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
		else
		{
			System.out.println("Please follow this command: mts_data_separator [-test/-m/-mts/-a] [input_filename] [training_rate]");
		}
	}
}
