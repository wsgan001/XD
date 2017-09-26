package data_format;
import java.io.*;

public class result_list
{
	public result_list(String d)
	{
		try
		{
			File dir=new File(d);
			if(dir.isDirectory())
			{
				File[] files=dir.listFiles();
				for(int i=0;i<files.length;i++)
				{
					String info_string=get_info(files[i]);
					System.out.println(files[i].getName()+" "+info_string);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("result_list exception:"+e);
		}
	}

	public String get_info(File f)
	{
		String result="";
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(f));
			String buffer="";
			while((buffer=br.readLine())!=null)
			{
				if(buffer.indexOf("Accuracy:")>=0)
				{
					result=buffer.substring(0,buffer.indexOf(" "))+" "+buffer.substring(buffer.indexOf("Class1:"),buffer.indexOf(",",buffer.indexOf("Class1:")));
					break;
				}
			}
			br.close();
		}
		catch(Exception e)
		{
			System.out.println("result_list get_info exception:"+e);
		}
		return result;
	}

	public static void main(String args[])
	{
		result_list a=new result_list(args[0]);
	}
}
