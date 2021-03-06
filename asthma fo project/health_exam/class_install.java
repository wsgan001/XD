package health_exam;
import java.io.*;
import java.util.HashMap;
import java.util.Vector;
import java.util.Iterator;

public class class_install
{
	public HashMap<String,String> id_class=new HashMap<String,String>();
	public Vector<String> class_set=new Vector<String>();
	public HashMap<String,String> match_table=new HashMap<String,String>();
	
	public class_install()
	{}
	
	public class_install(String cf, double u, double l)
	{
		set_class_file(cf,-1);
		set_a_health_range(u,l);
		//File class_target_file=new File(cf);
		//if(class_target_file.exists())
		  //class_target_file.delete();
	}
	
	public class_install(String cf, double u, double l,String f,String of)
	{
//		System.out.println("TEST111!!!");
		set_class_file(cf,-1);
//		System.out.println("TEST222!!!");
		set_a_health_range(u,l);
//		System.out.println("TEST333!!!");
		install(f,of);
		//File class_target_file=new File(cf);
		//if(class_target_file.exists())
		  //class_target_file.delete();
	}
	
	//for check
	public int set_class_file(String cf,int target_index) //0: means first, -1 means last, # means real index number
	{
		try
		{
			System.out.println(" cf = "+cf);
			
			id_class.clear();
			class_set.clear();
			BufferedReader br=new BufferedReader(new FileReader(cf));
			String buffer="";
			while((buffer=br.readLine())!=null)
			{
				String[] items=buffer.split(",");
				String temp_id=items[0];
				String temp_value="";
				if(target_index<0)
				{
					if(items.length-1!=0)
						temp_value=items[items.length-1];
				}
				else if((target_index+1)<items.length)
				{
					temp_value=items[target_index+1];
				}
				if(!class_set.contains(temp_value))
					class_set.add(temp_value);
				id_class.put(temp_id,temp_value);
			}
			br.close();
			System.out.println("# of Class:"+class_set.size());
			//System.out.println(class_set);
		}
		catch(Exception e)
		{
			System.out.println("class_install set_class_file");
		}
		return class_set.size();
	}
	
	
	
	
	public void set_a_health_range(double upper,double lower)
	{
		//for numerical data
		
		System.out.println(" upper value: "+ upper);
		System.out.println(" lower value: "+ lower);
		
		match_table.clear();
		try
		{
			//There must be three class {Low, Health, High};
			Vector<String> new_class_set=new Vector<String>();
			for(int i=0;i<class_set.size();i++)
			{
				try
				{
					double temp_value=Double.parseDouble(class_set.get(i));
					String temp_label="";
					if(temp_value>=upper)
						temp_label="1";
					else if(temp_value<upper && temp_value>=lower)
						temp_label="2";
					else
						temp_label="3";
					match_table.put(class_set.get(i),temp_label);
					if(!new_class_set.contains(temp_label))
						new_class_set.add(temp_label);
				}
				catch(Exception e)
				{
					if(!class_set.get(i).equals("?"))
					{						
						System.out.println("Not categorical");
						match_table.clear();
						return;
					}
				}
			}
			//class label
			Iterator ir=id_class.keySet().iterator();
			while(ir.hasNext())
			{
				String temp_id=(String)ir.next();
				String temp_value=id_class.get(temp_id);
				if(match_table.get(temp_value)!=null)
				{
					id_class.put(temp_id,match_table.get(temp_value));
				}
				else
				{
					if(!new_class_set.contains(temp_value))
						new_class_set.add(temp_value);
				}
			}
			
			class_set.clear();
			class_set.addAll(new_class_set);
			
			
			System.out.println(" new class_set.size() = "+ class_set.size());
			
			
		}
		catch(Exception e)
		{
			System.out.println("class_install set_a_health_range exception:"+e);
		}
	}
	
	public void install(String f,String of)
	{
		try
		{
			System.out.println(" f input  folder = "+f);
			System.out.println(" output folder = "+of);
			
			
			File tmp_file = new File(f);		
			String[] process_file_names = tmp_file.list();
			
			
			for(int i=0;i<process_file_names.length;i++){
				
				
				BufferedWriter bw = new BufferedWriter(new FileWriter(of+"/"+process_file_names[i]));
				BufferedReader br = new BufferedReader(new FileReader(f+"/"+process_file_names[i]));
				String buffer="";
			
				while((buffer=br.readLine())!=null)
				{
					String[] items=buffer.split(",");
					String temp_id=items[0];
					String temp_class="?";
				
				
					if(id_class.get(temp_id)!=null)
					{
						temp_class=id_class.get(temp_id);
					}
					bw.write(temp_class+":");
					for(int j=1;j<items.length;j++)
					{
						bw.write(items[j]+",");
					}
					bw.write("\n");
				
				}
			
				br.close();
				bw.close();
				
				
				/*
				BufferedReader br = new BufferedReader(new FileReader(f+"/"+process_file_names[i]));
				String buffer="";
				while((buffer=br.readLine())!=null)
				{
					String[] items=buffer.split(",");
					String temp_id=items[0];
					String temp_class="?";
				}
				br.close();
				*/
			}
			
			
			
			
			/*
			BufferedWriter bw = new BufferedWriter(new FileWriter(of));
			BufferedReader br = new BufferedReader(new FileReader(f));
			String buffer="";
			
			while((buffer=br.readLine())!=null)
			{
				String[] items=buffer.split(",");
				String temp_id=items[0];
				String temp_class="?";
				
				
				if(id_class.get(temp_id)!=null)
				{
					temp_class=id_class.get(temp_id);
				}
				bw.write(temp_class+":");
				for(int i=1;i<items.length;i++)
				{
					bw.write(items[i]+",");
				}
				bw.write("\n");
				
			}
			
			br.close();
			bw.close();
			*/
			
		}
		
		catch(Exception e)
		{
			System.out.println("class_install install exception:"+e);
		}
		
	}
	
	public static void main(String args[])
	{
		if(args.length==5)
		{
			class_install a=new class_install(args[0],Double.parseDouble(args[1]),Double.parseDouble(args[2]),args[3],args[4]);
		}
		else
		{
			class_install a=new class_install();
			a.set_class_file(args[0],-1);
		}
	}
}