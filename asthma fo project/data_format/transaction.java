package data_format;
import java.io.*;
import java.util.Vector;

public class transaction
{
	public Vector<String> ITEM_SET=null;
	public String CLASS_LABEL="";
	public String USER_ID="";
	public double SCORE=0.0;
	
	public transaction()
	{
		ITEM_SET=new Vector<String>();
		CLASS_LABEL="";
	}
	
	public transaction(Vector<String> i)
	{
		ITEM_SET=i;
	}
	
	public transaction(Vector<String> i,String c)
	{
		ITEM_SET=i;
		CLASS_LABEL=c;
	}
	
	public transaction(Vector<String> i,double s)
	{
		ITEM_SET=i;
		SCORE=s;
	}
	
	public transaction(Vector<String> i,String c,double s)
	{
		ITEM_SET=i;
		CLASS_LABEL=c;
		SCORE=s;
	}
	
	public String to_string(String tp)
	{
		String result="";
		try
		{
			for(int i=0;i<ITEM_SET.size();i++)
				result+=ITEM_SET.get(i)+",";
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