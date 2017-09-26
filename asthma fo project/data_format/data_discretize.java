package data_format;
import java.io.*;

public class data_discretize {

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub		
		if(args.length==4)
		{
			int d_mode=-1;
			if(args[0].equals("-v"))
			{
				d_mode=0;
			}
			else if(args[0].equals("-D"))
			{
				d_mode=1;
			}
			preprocessor pre=new preprocessor();
                        pre.DISCRETIZE_LEVEL=Integer.parseInt(args[2]);
                        String f_tmp=pre.discretize_data(args[1],d_mode);
                        try
                        {
                        	FileReader in = new FileReader(new File(f_tmp));
                                FileWriter out = new FileWriter(new File(args[3]));
                                int c;

                                while ((c = in.read()) != -1)
                                    out.write(c);

                                in.close();
                                out.close();
                        }
                        catch(Exception e)
                        {
                                System.out.println("data_discretize f_copy_to exception:"+e);
                        }

		}
	}
}
