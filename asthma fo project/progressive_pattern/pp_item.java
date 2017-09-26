package progressive_pattern;
import java.io.*;

public class pp_item
{
	public String NAME;
	public double[] TIME_POINTS;
	public pp_item(String content) throws Exception
	{
		if(content.indexOf("(")>=0)
		{
			NAME=content.substring(0,content.indexOf("("));
			String time_segment=content.substring(content.indexOf("(")+1,content.indexOf(")"));
			String[] all_time_points=time_segment.split("~");
			TIME_POINTS=new double[all_time_points.length];
			for(int i=0;i<all_time_points.length;i++)
				TIME_POINTS[i]=Double.parseDouble(all_time_points[i]);
		}
		else
			throw (new Exception("Format Error"));
	}

	public static void main(String args[])
	{
		try
		{
			pp_item a=new pp_item("t1v15(6~),");
			System.out.println("NAME:"+a.NAME);
			System.out.println("TIME_POINTS:"+a.TIME_POINTS[0]);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}
