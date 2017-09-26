package data_format;
import java.io.*;
import java.util.Vector;
import org.apache.commons.math.distribution.NormalDistributionImpl;

public class sax
{

	NormalDistributionImpl nd_model=null;
	public sax()
	{
	}
	
	public sax(double mean, double std)
	{
		nd_model=new NormalDistributionImpl(mean,std);
	}

	public void build_model(String filename)
	{
		
		File target=new File(filename);
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(target));
			String buffer="";
			double sum=0.0;
			double std=0.0;
			int count=0;
			while(true)
			{
				buffer=br.readLine();				
				if(buffer==null || buffer.trim().equals(""))
				{
					if(buffer==null)
						break;
				}
				else
				{
					transaction temp_tran=transaction_parser.auto_parser(buffer);
					if(temp_tran.ITEM_SET.size()!=0)
					{
						for(int i=0;i<temp_tran.ITEM_SET.size();i++)
						{
							double temp_value=0.0;
							try
							{
								String temp_s=temp_tran.ITEM_SET.get(i);
								if(temp_s.indexOf("*")>0)
									temp_s=temp_s.substring(0,temp_s.indexOf("*"));
								temp_value=Double.parseDouble(temp_s);
							}
							catch(Exception e)
							{
								System.out.print("FE("+temp_tran.ITEM_SET.get(i)+")");
							}
							sum+=temp_value;
							count++;
						}
					}
				}
			}
			br.close();
			double mean=sum/(double)count;
			br=new BufferedReader(new FileReader(target));
			while(true)
			{
				buffer=br.readLine();				
				if(buffer==null || buffer.trim().equals(""))
				{
					if(buffer==null)
						break;
				}
				else
				{
					transaction temp_tran=transaction_parser.auto_parser(buffer);
					if(temp_tran.ITEM_SET.size()!=0)
					{
						for(int i=0;i<temp_tran.ITEM_SET.size();i++)
						{
							double temp_value=0.0;
							try
							{
								String temp_s=temp_tran.ITEM_SET.get(i);
								if(temp_s.indexOf("*")>0)
									temp_s=temp_s.substring(0,temp_s.indexOf("*"));
								temp_value=Double.parseDouble(temp_s);
							}
							catch(Exception e)
							{
								System.out.print("FE("+temp_tran.ITEM_SET.get(i)+")");
							}
							temp_value=temp_value-mean;
							std+=temp_value*temp_value;
						}
					}
				}
			}
			br.close();
			std=Math.pow(std/(double)count,0.5);
			System.out.println("MEAN:"+mean+"\n STD:"+std+"\n");
			nd_model=new NormalDistributionImpl(mean,std);
		}
		catch(Exception e)
		{
			System.out.println("sax sax_file exception:"+e);
		}
	}

	public String sax_file(String filename,int level)
	{
		if(nd_model==null)
			build_model(filename);

		File target=new File(filename);
		File result_file=new File(target.getParentFile(),target.getName()+"_saxed");
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(target));
			BufferedWriter bw=new BufferedWriter(new FileWriter(result_file));
			String buffer="";
			while(true)
			{
				buffer=br.readLine();				
				if(buffer==null || buffer.trim().equals(""))
				{
					if(buffer==null)
						break;
					bw.write(buffer+"\n");
				}
				else
				{
					transaction temp_tran=transaction_parser.auto_parser(buffer);
					String type="s";
					if(buffer.indexOf(":")>0)
					{
						if(buffer.indexOf(":")!=buffer.lastIndexOf(":"))
							type="css";
						else
							type="cs";
					}
					else
					{
						type="s";
					}
					if(temp_tran.ITEM_SET.size()!=0)
					{
						for(int i=0;i<temp_tran.ITEM_SET.size();i++)
						{
							double temp_value=0.0;
							boolean t_flg=true;
							try
							{
								String temp_s=temp_tran.ITEM_SET.get(i);
								if(temp_s.indexOf("*")>0)
									temp_s=temp_s.substring(0,temp_s.indexOf("*"));
								temp_value=Double.parseDouble(temp_s);
							}
							catch(Exception e)
							{
								t_flg=false;
							}
							if(t_flg)
							{
								temp_value=nd_model.cumulativeProbability(temp_value);
								temp_value=temp_value*level;
								temp_tran.ITEM_SET.setElementAt(Integer.toString((int)Math.ceil(temp_value)),i);
							}
						}
						bw.write(temp_tran.to_string(type));
					}
					bw.write("\n");
				}
			}
			br.close();
			bw.close();
		}
		catch(Exception e)
		{
			System.out.println("sax sax_file exception:"+e);
		}
		return result_file.getAbsolutePath();
	}

	public static void main(String args[])
	{
		//sax a=new sax(20.0,10.0);
		sax a=new sax();
		System.out.println(a.sax_file(args[0],Integer.parseInt(args[1])));
	}
}
