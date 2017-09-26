package data_format;
import java.io.*;
import java.util.Properties;
import java.util.Vector;

public class preprocessor
{	
	String setting_filename="";
	public double TRAINING_RATE=0.7;
	public String TRANSACTION_MODE="CS"; //S, CS , CSS
	public int TRANSACTION_COUNT=0;
//	public String DISCRETIZED_DATA_FILENAME="";
	public int DISCRETIZATION_MODE=0; //-1 not need discretize 0 VALUE BASE   1  DIFFERENCE_BASE_WITH GENERAL LEVEL  2 DIFFERENCE BASE WITH ANGLE BASE
	public String DATA_FILENAME="";
	public String TRAINING_FILENAME="";
	public String TESTING_FILENAME="";
	public int DISCRETIZE_LEVEL=10;
	public Vector<String> CLASS_SET=new Vector<String>();
//	double MAX=-9999;
//	double MIN=9999;

	public preprocessor()
	{
	}
	
	public preprocessor(String sf)
	{
		feed_data(sf);
	}
	
	public void feed_data(String sf)
	{
		setting_filename=sf;
		if(file_parser())		
			action();		
	}
	
	private boolean file_parser()
	{
		try
		{
			FileInputStream fis=new FileInputStream(setting_filename);
			Properties pro=new Properties();
			pro.load(fis);
			if(pro.getProperty("DATA_FILENAME")!=null)
				DATA_FILENAME=pro.getProperty("DATA_FILENAME");
			if(pro.getProperty("TRAINING_RATE")!=null)
				TRAINING_RATE=Double.parseDouble(pro.getProperty("TRAINING_RATE"));
			if(pro.getProperty("DISCRETIZE_LEVEL")!=null)
				DISCRETIZE_LEVEL=Integer.parseInt(pro.getProperty("DISCRETIZE_LEVEL"));
/*			if(pro.getProperty("TARGET_FILENAME")!=null)			
				TARGET_FILENAME=pro.getProperty("TARGET_FILENAME");
			else
				TARGET_FILENAME="preprocessed_"+DATA_FILENAME;*/				
			if(pro.getProperty("DISCRETIZATION_MODE")!=null)
			{
				String tmp_str=pro.getProperty("DISCRETIZATION_MODE");
				if(tmp_str.equals("VALUE"))
					DISCRETIZATION_MODE=0;
				else if(tmp_str.equals("DIF"))
					DISCRETIZATION_MODE=1;
				else if(tmp_str.equals("DIF_ANG"))
					DISCRETIZATION_MODE=2;
				else if(tmp_str.equals("SAX"))
					DISCRETIZATION_MODE=3;
				else if(tmp_str.equals("NONE"))
					DISCRETIZATION_MODE=-1;
			}
			if(pro.getProperty("TRANSACTION_MODE")!=null)
				TRANSACTION_MODE=pro.getProperty("TRANSACTION_MODE");
			
			if(pro.getProperty("TRAINING_FILENAME")!=null)
				TRAINING_FILENAME=pro.getProperty("TRAINING_FILENAME");
			else
				TRAINING_FILENAME="train_"+DATA_FILENAME;
			if(pro.getProperty("TESTING_FILENAME")!=null)
				TESTING_FILENAME=pro.getProperty("TESTING_FILENAME");
			else
				TRAINING_FILENAME="test_"+DATA_FILENAME;
			fis.close();
		}
		catch(Exception e)
		{
			System.out.println("preprocessor file_parser exception:"+e);
			return false;
		}
		return true;
	}
	
	private String discretize_data(String f)
	{
		return discretize_data(f,DISCRETIZATION_MODE);
	}
	
	//f: input filename , dm: {0:by value, 1:by difference, 2:difference_angle, 3:by SAX}
	public String discretize_data(String f,int dm)
	{
		String DISCRETIZED_DATA_FILENAME="";
		try
		{
			if(DISCRETIZATION_MODE!=-1)
			{
				//create a processed_file_name
				File tmp_dir=new File("tmp");
				if(tmp_dir.isDirectory())
					DISCRETIZED_DATA_FILENAME=File.createTempFile("processed_",".tmp",tmp_dir).getAbsolutePath();
				else
					DISCRETIZED_DATA_FILENAME=File.createTempFile("processed_",".tmp").getAbsolutePath();
			}
			else
				return f;
				
			if(dm==0)
				discretize_by_value(f,DISCRETIZED_DATA_FILENAME);
			else if(dm==1)
				discretize_by_difference(f,DISCRETIZED_DATA_FILENAME);
			else if(dm==2)
				discretize_by_difference_angle(f,DISCRETIZED_DATA_FILENAME);
			else if(dm==3)
				discretize_by_SAX(f,DISCRETIZED_DATA_FILENAME);
		}
		catch(Exception e)
		{
			System.out.println("preprocessor discretize_data exception:"+e);
		}
		return DISCRETIZED_DATA_FILENAME;
	}
	
	public void discretize_by_value(String fi,String fo)
	{
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(fi));
			String buffer="";
			double max=-9999.9;
			double min=9999.9;
			while((buffer=br.readLine())!=null)
			{
				transaction t=transaction_parser.basic_parser(buffer,TRANSACTION_MODE);
				for(int i=0;i<t.ITEM_SET.size();i++)
				{
					double tmp=0.0;
					if(((String)t.ITEM_SET.get(i)).equals(""))
						tmp=0.0;
					else
						tmp=Double.parseDouble((String)t.ITEM_SET.get(i));
					if(tmp>max)
						max=tmp;
					if(tmp<min)
						min=tmp;
				}
			}
			br.close();			
			System.out.println(min+":"+max);
			double interval_value=(max-min)/(double)DISCRETIZE_LEVEL;
			System.out.println(interval_value);
			
			br=new BufferedReader(new FileReader(fi));
			BufferedWriter bw=new BufferedWriter(new FileWriter(fo));
			while((buffer=br.readLine())!=null)
			{
				transaction t=transaction_parser.basic_parser(buffer,TRANSACTION_MODE);
				if(TRANSACTION_MODE.equals("CS") || TRANSACTION_MODE.equals("CSS"))
					bw.write(t.CLASS_LABEL+":");
				
				for(int i=0;i<t.ITEM_SET.size();i++)
				{
					int tmp_value=0;
					if(((String)t.ITEM_SET.get(i)).equals(""))
						tmp_value=(int)((0.0-min)/interval_value);
					else				
						tmp_value=(int)(((Double.parseDouble((String)t.ITEM_SET.get(i)))-min)/interval_value);
					bw.write(tmp_value+",");
				}
				
				if(TRANSACTION_MODE.equals("CSS"))
					bw.write(":"+t.SCORE);
				bw.write("\n");
			}
			bw.close();
			br.close();			
		}
		catch(Exception e)
		{
			System.out.println("preprocessor discretize_by_value exception:"+e);
		}
	}
	
	public void discretize_by_difference(String fi,String fo)
	{
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(fi));
			String buffer="";
			double max=-9999.9;
			double min=9999.9;
			double pre_value=0.0;
			while((buffer=br.readLine())!=null)
			{
				transaction t=transaction_parser.basic_parser(buffer,TRANSACTION_MODE);
				for(int i=0;i<t.ITEM_SET.size();i++)
				{
					double tmp=0.0;
					if(((String)t.ITEM_SET.get(i)).equals(""))
						tmp=0.0;
					else
						tmp=Double.parseDouble((String)t.ITEM_SET.get(i));					
					if(i==0)
					{
						pre_value=tmp;
						continue;
					}
					else
					{
						double local_tmp=tmp;
						tmp=tmp-pre_value;
						pre_value=local_tmp;
					}
					if(tmp>max)
						max=tmp;
					if(tmp<min)
						min=tmp;
				}
			}
			br.close();			
			
			double interval_value=(max-min)/(double)DISCRETIZE_LEVEL;
			
			br=new BufferedReader(new FileReader(fi));
			BufferedWriter bw=new BufferedWriter(new FileWriter(fo));
			while((buffer=br.readLine())!=null)
			{
				transaction t=transaction_parser.basic_parser(buffer,TRANSACTION_MODE);
				if(TRANSACTION_MODE.equals("CS") || TRANSACTION_MODE.equals("CSS"))
					bw.write(t.CLASS_LABEL+":");
				double now_value=0.0;
				for(int i=0;i<t.ITEM_SET.size();i++)
				{
					int tmp_value=0;
					now_value=0.0;
					if(!((String)t.ITEM_SET.get(i)).equals(""))
						now_value=Double.parseDouble((String)t.ITEM_SET.get(i));
					if(i==0)
					{
						pre_value=now_value;
						continue;
					}
					else
					{
						double local_tmp=now_value;
						now_value=now_value-pre_value;
						pre_value=local_tmp;
					}
					tmp_value=(int)((now_value-min)/interval_value);
					bw.write(tmp_value+",");
				}
				
				if(TRANSACTION_MODE.equals("CSS"))
					bw.write(":"+t.SCORE);
				bw.write("\n");
			}
			bw.close();
			br.close();
		}
		catch(Exception e)
		{
			System.out.println("preprocessor discretize_by_difference exception:"+e);
		}
	}	

	public void discretize_by_difference_angle(String fi,String fo)
	{
		try
		{
		}
		catch(Exception e)
		{
			System.out.println("preprocessor discretize_by_difference exception:"+e);
		}
	}
	
	private void discretize_by_SAX(String fi,String fo)
	{
		try
		{
		}
		catch(Exception e)
		{
			System.out.println("preprocessor discretize_by_difference exception:"+e);
		}
	}	
	
	private void action()
	{
		try
		{
			String df=discretize_data(DATA_FILENAME);
			
			BufferedReader br=new BufferedReader(new FileReader(df));
			String buffer="";
						
			BufferedWriter bw1=new BufferedWriter(new FileWriter(TRAINING_FILENAME));
			BufferedWriter bw2=new BufferedWriter(new FileWriter(TESTING_FILENAME));
			while((buffer=br.readLine())!=null)
			{
				if(buffer.indexOf(":")>=0)
				{
					String temp_class_label=buffer.substring(0,buffer.indexOf(":"));
					if(!CLASS_SET.contains(temp_class_label))
						CLASS_SET.add(temp_class_label);
				}
				if(Math.random()<=TRAINING_RATE)
					bw1.write(buffer+"\n");
				else
					bw2.write(buffer+"\n");
			}
			br.close();
			bw1.close();
			bw2.close();
		}
		catch(Exception e)
		{
			System.out.println("preprocessor action exception:"+e);
		}
	}
}