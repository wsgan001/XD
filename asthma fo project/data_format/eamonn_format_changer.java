package data_format;
import java.io.*;
import java.util.StringTokenizer;

public class eamonn_format_changer
{
	public eamonn_format_changer()
	{}
	public boolean change_format_to(String file_in,String file_out)
	{
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(file_in));
			BufferedWriter bw=new BufferedWriter(new FileWriter(file_out));
			String buffer="";
			while((buffer=br.readLine())!=null)
			{
				StringTokenizer st=new StringTokenizer(buffer," ");
				boolean x=true;
				while(st.hasMoreElements())
				{
					Double tempd=new Double((String)st.nextElement());
					if(x)
					{
						bw.write(Integer.toString(tempd.intValue()));
						bw.write(":");
						x=!x;
					}
					else
						bw.write(tempd.doubleValue()+",");
				}
				bw.write("\n");
			}
			br.close();
			bw.close();
		}
		catch(Exception e)
		{
			System.out.println("eamonn_format_changer change_format_to exception:"+e);
			return false;
		}
		return true;
	}
	public static void main(String args[])
	{
		eamonn_format_changer changer=new eamonn_format_changer();
		changer.change_format_to(args[0],args[1]);
	}
}
