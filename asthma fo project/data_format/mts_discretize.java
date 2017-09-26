package data_format;
import java.io.*;
import java.util.Vector;
import java.util.StringTokenizer;

public class mts_discretize
{	
	Vector<var_information> vars=new Vector<var_information>();//input level is the level of first variable.
	public int VAR_NUM=0;
	public boolean SAX_MODE=false;
	public double STEP_VALUE=10.0;
	public String MTS_FILE="";
	
	public class var_information
	{
		public double MAX=100.0;
		public double MIN=0.0;
		public double SUM=0.0;
		public int COUNT=0;
		public int LEVEL=20;		
		public var_information(){}
	}
	
	public mts_discretize(String input_file,int level,String output_file)
	{	
		MTS_FILE=input_file;
		check_level_for_each_var(level);
		discretize_file(output_file);
	}
	
	public void discretize_file(String output_file)
	{
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(MTS_FILE));
			BufferedWriter bw=new BufferedWriter(new FileWriter(output_file));
			String buffer="";
			Vector<String> temp_mts=new Vector<String>();
			while(true)
			{
				buffer=br.readLine();
				if(buffer==null || buffer.equals(""))
				{
					if(temp_mts.size()!=0)
					{
						for(int i=0;i<temp_mts.size();i++)
						{
							if(vars.get(i)!=null)
							{
								var_information temp_var_information=vars.get(i);
								StringTokenizer st=new StringTokenizer(temp_mts.get(i),",");
								while(st.hasMoreTokens())
								{
									bw.write((int)(Math.ceil((Double.parseDouble(st.nextToken())-temp_var_information.MIN)/STEP_VALUE))+",");
								}
								bw.write("\n");
							}
							else
								bw.write(temp_mts.get(i)+"\n");
						}
						bw.write("\n");
					}
					
					if(buffer==null)
						break;
					temp_mts.clear();
				}
				else
				{
					if(buffer.indexOf(":")>0)
					{
						bw.write(buffer.substring(0,buffer.indexOf(":")+1)+"\n");
						if(!buffer.trim().substring(buffer.indexOf(":")+1).equals(""))
							temp_mts.add(buffer.substring(buffer.indexOf(":")+1));
					}
					else
						temp_mts.add(buffer);
				}
			}
			
			bw.close();
			br.close();
		}
		catch(Exception e)
		{
			System.out.println("mts_discretize discretize_file exception:"+e);
		}
	}
	
	public void check_level_for_each_var(int level)
	{
		try
		{
			vars.clear();
			Vector<String> temp_mts=new Vector<String>();
			BufferedReader br=new BufferedReader(new FileReader(MTS_FILE));
			String buffer="";
			String temp_class="";
			while(true)
			{
				buffer=br.readLine();
				if(buffer==null || buffer.equals(""))
				{
					if(vars.size()==0)
					{
						for(int i=0;i<temp_mts.size();i++)
							vars.add(new var_information());
					}
					for(int i=0;i<temp_mts.size();i++)
					{
						StringTokenizer st=new StringTokenizer(temp_mts.get(i),",");
						var_information temp_var=vars.get(i);
						while(st.hasMoreTokens())
						{
							double temp_value=Double.parseDouble(st.nextToken());
							temp_var.SUM+=temp_value;
							temp_var.COUNT++;
							if(temp_value>temp_var.MAX)
								temp_var.MAX=temp_value;
							if(temp_value<temp_var.MIN)
								temp_var.MIN=temp_value;
						}
					}
					if(buffer==null)
						break;
					temp_mts.clear();
				}
				else
				{
					if(buffer.indexOf(":")<0)
						temp_mts.add(buffer);
					else
					{
						temp_class=buffer.substring(0,buffer.indexOf(":"));
						if(!buffer.trim().substring(buffer.indexOf(":")+1).equals(""))
							temp_mts.add(buffer.substring(buffer.indexOf(":")+1));						
					}
				}
			}
			br.close();
			
			for(int i=0;i<vars.size();i++)
			{
				var_information temp_var_information=vars.get(i);
				if(i==0)
				{
					STEP_VALUE=(temp_var_information.MAX-temp_var_information.MIN)/(double)level;
					temp_var_information.LEVEL=level;
				}
				else				
					temp_var_information.LEVEL=(int)Math.ceil((temp_var_information.MAX-temp_var_information.MIN)/STEP_VALUE);
			}
		}
		catch(Exception e)
		{
			System.out.println("mts_discretize check_level_for_each_var exception:"+e);
		}
	}
	
	public static void main(String args[])
	{
		mts_discretize a=new mts_discretize(args[0],Integer.parseInt(args[1]),args[2]);
	}
}
