package data_format;
import java.io.*;

public class sim_data_modify
{
	public static void main(String args[])
	{
		try
		{
			if(args.length>1)
			{
				return ;
			}
			else
			{
				File target_file=new File(args[0]);				
				BufferedReader br=new BufferedReader(new FileReader(target_file));
				String tmp_filename=target_file.getParent()+"/tmp.tmp";
				BufferedWriter bw=new BufferedWriter(new FileWriter(tmp_filename));
				String buffer="";
				while((buffer=br.readLine())!=null)
				{
					transaction temp_tran=transaction_parser.auto_parser(buffer);
					bw.write(temp_tran.CLASS_LABEL+":");
					if(temp_tran.CLASS_LABEL.equals("0"))
					{
						temp_tran.ITEM_SET.insertElementAt("500", 10);
						temp_tran.ITEM_SET.insertElementAt("501", 11);
						temp_tran.ITEM_SET.insertElementAt("502", 12);
					}
					for(int i=0;i<temp_tran.ITEM_SET.size();i++)
						bw.write(temp_tran.ITEM_SET.get(i)+",");
				}
				File new_file=new File(tmp_filename);
				target_file.delete();
				new_file.renameTo(new File(args[0]));
				bw.close();
				br.close();
			}
		}
		catch(Exception e)
		{
			System.out.println("sim_data_modify exception:"+e);
		}
	}
}
