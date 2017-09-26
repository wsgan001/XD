package data_format;
import java.io.*;
import java.util.Vector;
import java.util.Properties;

//This record one transaction of multivariate time series data.
public class mv_transaction
{
	//This vector record MTS part
	public Vector<Vector<String>> ITEM_SET=null;
	//Basic attributes
	public String CLASS_LABEL="";
	public String USER_ID="";
	public double SCORE=0.0;
	
	//other attributes
	public Properties ATTRIBUTES=null;
	
	public mv_transaction()
	{
		ITEM_SET=new Vector<Vector<String>>();
		CLASS_LABEL="";
	}
	
	public mv_transaction(Vector<Vector<String>> i)
	{
		ITEM_SET=i;
	}
	
	public mv_transaction(Vector<Vector<String>> i,String c)
	{
		ITEM_SET=i;
		CLASS_LABEL=c;
	}
	
	public mv_transaction(Vector<Vector<String>> i,double s)
	{
		ITEM_SET=i;
		SCORE=s;
	}
	
	public mv_transaction(Vector<Vector<String>> i,String c,double s)
	{
		ITEM_SET=i;
		CLASS_LABEL=c;
		SCORE=s;
	}
	
	public String get_attribute(String at_name)
	{
		try
		{
			if(ATTRIBUTES!=null && ATTRIBUTES.getProperty(at_name)!=null)
			{
				return (String)ATTRIBUTES.getProperty(at_name);
			}
		}
		catch(Exception e)
		{
			System.out.println("mv_transaction get_attribute exception:"+e);
		}
		return null;
	}
	
	public String to_string(String tp)
	{
		String result="";
		try
		{
			for(int i=0;i<ITEM_SET.size();i++)
			{
				Vector<String> temp_variable=(Vector<String>)ITEM_SET.get(i);
				for(int j=0;j<temp_variable.size();j++)				
					result+=ITEM_SET.get(i)+",";
				result+=";";
			}
			if(tp.equals("ss")||tp.equals("SS"))
				result=result+":"+Double.toString(SCORE);
			else if(tp.equals("cs")||tp.equals("CS"))
				result=CLASS_LABEL+":"+result;
			else if(tp.equals("css")||tp.equals("CSS"))
				result=CLASS_LABEL+":"+result+":"+Double.toString(SCORE);
		}
		catch(Exception e)
		{
			System.out.println("transaction to_string Exception:"+e);
		}
		return result;
	}
}
