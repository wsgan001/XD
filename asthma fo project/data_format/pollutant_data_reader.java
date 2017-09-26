package data_format;
import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.HashMap;

public class pollutant_data_reader
{
	String FILENAME="";
	Vector<String> COLUMN_NAMES=new Vector<String>();
	HashMap<String,Vector<String>> DATA=new HashMap<String,Vector<String>>();
	boolean READY=true;
	public pollutant_data_reader(String f)
	{
		FILENAME=f;
		data_read();
	}
	
	private void data_read()
	{
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(FILENAME));
			String buffer="";
			get_column_names(br.readLine());
			Vector<String> temp_vector=null;
			while((buffer=br.readLine())!=null)
			{
				String[] items=buffer.split("\t");
				temp_vector=new Vector<String>();
				for(int i=0;i<items.length;i++)
					temp_vector.add(items[i]);
				DATA.put(items[0],(Vector<String>)temp_vector.clone());
			}
			br.close();			
		}
		catch(Exception e)
		{
			System.out.println("pollutant_data_reader data_read exception:"+e);
			READY=false;
		}
		READY=true;
	}
	
	public Vector<String> get_pollutant_chain(String time_series)
	{
		Vector<String> result=new Vector<String>();
		try
		{
			String[] time_points=time_series.split(",");
			for(int i=0;i<time_points.length;i++)
			{
				Vector<String> temp_v=get_vector_with_column_order(time_points[i]);
				if(i==0)
					result.addAll(temp_v);
				else
				{
					for(int x=0;x<result.size();x++)
						result.set(x,(String)result.get(x)+","+(String)temp_v.get(x));
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("pollutant_data_reader get_pollutant_chain exceptin:"+e);
		}
		return result;
	}
	
	public Vector<String> get_vector_with_column_order(String t)
	{
		Vector<String> result=new Vector<String>();
		try
		{
			for(int i=0;i<COLUMN_NAMES.size();i++)
				result.add("");
			String[] time_points=t.split(",");
			for(int i=0;i<time_points.length;i++)
			{
				String date_string=get_date_string(Long.parseLong(time_points[i])*1000);  //it just second , not millisecond
				//System.out.println(date_string);
				if(DATA.get(date_string)!=null)
				{
					Vector<String> temp_vector=DATA.get(date_string);
					for(int ci=0;ci<temp_vector.size();ci++)
					{
						if( !(((String)result.get(ci)).trim()).equals("") )
							result.set(ci,result.get(ci)+"~"+temp_vector.get(ci));
						else
							result.set(ci,temp_vector.get(ci));
					}
				}
				else
				{
					for(int ci=0;ci<result.size();ci++)
						result.set(ci,result.get(ci)+"~"+"");
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("pollutant_data_reader get_vector_with_column_order exception:"+e);
		}
		return result;
	}
	
	private String get_date_string(long t)
	{
		String result="";
		try
		{
			SimpleDateFormat formatter=new SimpleDateFormat("yyyy/M/d");
			result=formatter.format(new Date(t));
			//System.out.println(t+"::"+result);
		}
		catch(Exception e)
		{
			System.out.println("pollutant_data_reader get_date_string exception:"+e);
		}
		return result;
	}
	
	private void get_column_names(String s)
	{
		try
		{
			String items[]=s.split("\t");
			COLUMN_NAMES.clear();
			for(int i=0;i<items.length;i++)
				COLUMN_NAMES.add(items[i]);
		}
		catch(Exception e)
		{
			System.out.println("pollutant_data_reader get_column_names exception:"+e);
		}
	}
	
	public  Date date_parse(String d)
	{
		try
		{
			SimpleDateFormat formatter=new SimpleDateFormat("yyyy/MM/dd");
			return formatter.parse(d);
		}
		catch(Exception e)
		{
			System.out.println("pollutant_data_reader date_parse exception:"+e);
		}
		return null;
	}
	
	public static void main(String[] args)
	{
		pollutant_data_reader a=new pollutant_data_reader(args[0]);
		//System.out.println((a.date_parse("2010/12/11")).toString());
		Vector<String> temp=a.get_pollutant_chain("1114997890,1117620535,1117626430,1117626828,1117626926,1117627466,1117627691,1117628007,1120490232,1120495219,1120613148,1120613176,1120657481,1120675083,1122307902");
		for(int i=0;i<temp.size();i++)
			System.out.println(temp.get(i));
	}
}