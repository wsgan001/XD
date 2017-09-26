package data_format;
import java.io.*;

public class add_comma
{
	public static void main(String args[])
	{
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(args[0]));
			BufferedWriter bw=new BufferedWriter(new FileWriter(args[1]));
			String buffer="";
			while((buffer=br.readLine())!=null)
			{
				if(!buffer.trim().equals(""))
				{
					if(buffer.lastIndexOf(",")!=buffer.length()-1)
						bw.write(buffer+",\n");
					else
						bw.write(buffer+"\n");
				}
			}
			bw.close();
			br.close();
		}
		catch(Exception e)
		{
			System.out.println("add_comma exception:"+e);
		}
	}
}
