package progressive_pattern;
import java.io.*;
import java.util.Vector;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collections;

public class mp_transaction
{	
	public String CLASS_LABEL;
	public Vector<Double> HAPPEN_TIMES=new Vector<Double>();
	public HashMap<Double,Vector<pp_item>> ITEM_SET=new HashMap<Double,Vector<pp_item>>();
	
	public mp_transaction(String d) throws Exception
	{
		parse(d);
	}
	
	private void parse(String content) throws Exception
	{
		try
		{
			CLASS_LABEL=content.substring(0,content.indexOf(":"));
			String items[]=content.substring(content.indexOf(":")+1).split(",");
			for(int i=0;i<items.length;i++)
			{
				//do not process empty data.
				if(items[i].equals(""))
					continue;
				pp_item a=new pp_item(items[i]);
				Double start_time=new Double(a.TIME_POINTS[0]);
				Vector<pp_item> temp_pp_item_set=null;
				if(ITEM_SET.get(start_time)!=null)
					temp_pp_item_set=(Vector<pp_item>)ITEM_SET.get(start_time);
				else
					temp_pp_item_set=new Vector<pp_item>();
				temp_pp_item_set.add(a);
				ITEM_SET.put(start_time,temp_pp_item_set);
			}
			if(ITEM_SET.size()!=0)
			{
				Iterator ir=ITEM_SET.keySet().iterator();
				while(ir.hasNext())
					HAPPEN_TIMES.add((Double)ir.next());
				Collections.sort(HAPPEN_TIMES);
			}
		}
		catch(Exception e)
		{
			System.out.println("mp_transaction parse exception:"+e);
			throw (new Exception("Format Error! "+content));
		}
	}

	public int size()
	{
		//return length of time period.
		return HAPPEN_TIMES.size();
	}
	
	//get all item happened at start_time
	public Vector<pp_item> get_item_at_time_point(int time_point_index)
	{
		return (Vector<pp_item>)ITEM_SET.get(HAPPEN_TIMES.get(time_point_index));
	}

	public static void main(String args[])
	{
		try
		{
			String buffer=" 0:t0v70(0~),t0v14(1~),t0v64(4~),t0v18(5~),t0v82(7~),t0v30(8~),t0v40(9~),t0v51(10~),t0v5(12~),t0v16(13~),t0v27(14~),t0v60(15~),t0v73(17~),t1v80(0~),t1v54(1~),t1v91(2~),t1v58(3~),t1v74(4~),t1v3(5~),t1v15(6~),t1v0(8~),t1v75(9~),t1v65(12~),t1v14(15~),t1v28(16~),t1v9(17~),t1v83(18~),t2v59(0~),t2v43(1~),t2v68(2~),t2v85(3~),t2v22(4~),t2v34(6~),t2v78(7~),t2v36(9~),t2v28(10~),t2v63(11~),t2v75(12~),t2v44(14~),t2v52(15~),t2v47(16~),t2v29(18~),";
			mp_transaction a=new mp_transaction(buffer);
			System.out.println("HAPPEN_TIMES:"+a.HAPPEN_TIMES);
			System.out.println("ITEM_SET:"+a.ITEM_SET);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}
