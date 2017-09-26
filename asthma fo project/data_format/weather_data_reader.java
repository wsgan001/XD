package data_format;
import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.HashMap;
import java.util.StringTokenizer;

public class weather_data_reader
{
	String FILENAME="";
	Vector<String> COLUMN_NAMES=new Vector<String>(); // all item names
	HashMap<String,Vector<String>> DATA=new HashMap<String,Vector<String>>();	// date, value vector order by column_names
	boolean READY=true;
	public weather_data_reader(String f)
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
			String temp_year="";
			String temp_item_name="";
			boolean record_start=false;
			int column_index=0;
			while((buffer=br.readLine())!=null)
			{
				if(buffer.indexOf("´ú¯¸:")==0)
				{
					String[] items=buffer.substring(buffer.indexOf(":")+1).split(" ");
					temp_year=items[0];
				}
				else if(buffer.indexOf("D\\M")==0)
				{
					record_start=true;
				}
				else if(buffer.indexOf("¶µ¥Ø:")==0)
				{
					temp_item_name=buffer.substring(buffer.indexOf(":")+1);
					if(!COLUMN_NAMES.contains(temp_item_name))
					{
						COLUMN_NAMES.add(temp_item_name);
						column_index=COLUMN_NAMES.size()-1;
					}
					else
					{
						column_index=COLUMN_NAMES.indexOf(temp_item_name);
					}
					//System.out.println("CI:"+column_index);
				}
				else if(buffer.indexOf("AVG")==0 || buffer.indexOf("MAX/DATE")==0)
				{
					record_start=false;
				}
				else
				{
					if(record_start)
					{
						StringTokenizer temp_st=new StringTokenizer(buffer," ");
						Vector<String> temp_values=new Vector<String>();
						while(temp_st.hasMoreElements())
							temp_values.add((String)temp_st.nextElement());						
						String temp_date=temp_values.get(0);
						if(temp_values.size()!=12)
						{
							if(temp_date.equals("29") && temp_values.size()==11)
								temp_values.insertElementAt("",2);
							if(temp_date.equals("30") && temp_values.size()==11)
								temp_values.insertElementAt("",2);
							if(temp_date.equals("31"))
							{
								temp_values.insertElementAt("",2);
								temp_values.insertElementAt("",4);
								temp_values.insertElementAt("",6);
								temp_values.insertElementAt("",9);
								temp_values.insertElementAt("",11);
							}
								
						}
						for(int i=1;i<temp_values.size();i++)
						{
							if(!temp_values.get(i).equals(""))
							{
								String date_string=temp_year+"/"+i+"/"+temp_date;
								Vector<String> temp_vector=null;
								if(!DATA.containsKey(date_string))
								{
									temp_vector=new Vector<String>();
									temp_vector.add(temp_values.get(i));
									while(temp_vector.size()<column_index+1)
										temp_vector.add("");
									temp_vector.set(column_index,temp_values.get(i));
								}
								else
								{
									temp_vector=DATA.get(date_string);
									while(temp_vector.size()<column_index+1)
										temp_vector.add("");
									temp_vector.set(column_index,temp_values.get(i));									
								}
								//System.out.print(temp_vector.size()+",");								
								DATA.put(date_string,temp_vector);
							}
						}
					}
				}
			}
			br.close();
		}
		catch(Exception e)
		{
			System.out.println("waether_data_reader data_read exception:"+e);
			READY=false;
		}
		READY=true;
	}
	
	public Vector<String> get_weather_chain(String t)
	{
		Vector<String> result=new Vector<String>();
		try
		{
			for(int i=0;i<COLUMN_NAMES.size();i++)
			{
				//System.out.print(COLUMN_NAMES.get(i)+"\t");
				//System.out.println();
				result.add("");				
			}
			String time_points[]=t.split(",");
			for(int i=0;i<time_points.length;i++)
			{
				String date_string=get_date_string(Long.parseLong(time_points[i])*1000);  //it just second , not millisecond				
				if(DATA.get(date_string)!=null)
				{
					Vector<String> temp_vector=DATA.get(date_string);
					if(temp_vector.size()!=result.size())
					{
						System.out.println("Error====="+temp_vector.size()+" =/= "+result.size()+"===========");
						for(int ci=0;ci<temp_vector.size();ci++)
							System.out.print("["+temp_vector.get(ci)+"] ");
							System.out.println();
					}
					else
					{
						for(int ci=0;ci<temp_vector.size();ci++)
						{
							String temp_value=temp_vector.get(ci);
							if(temp_value.indexOf(":")>=0 && temp_value.indexOf("/")>=0)
							{
								temp_value=temp_value.substring(0,temp_value.indexOf("/"));
								//System.out.println(temp_value);
							}
							if(!((String)result.get(ci)).trim().equals(""))
								result.set(ci,result.get(ci)+","+temp_value);
							else
								result.set(ci,temp_value);
						}
					}
				}
				else
				{
					for(int ci=0;ci<result.size();ci++)
						result.set(ci,result.get(ci)+","+"");
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("weather_data_reader get_weather_chain exception:"+e);
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
	
	public static void main(String args[])
	{
		weather_data_reader a=new weather_data_reader(args[0]);
		Vector<String> temp_v=a.get_weather_chain("1114997890,1117620535,1117626430,1117626828,1117626926,1117627466,1117627691,1117628007,1120490232,1120495219,1120613148,1120613176,1120657481,1120675083,1122307902");
		for(int i=0;i<temp_v.size();i++)
			System.out.println(temp_v.get(i));
	}
}
