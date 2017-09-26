package progressive_pattern;
import java.io.*;
import java.util.Vector;
import data_format.*;

public class CBMPS
{
	public double MIN_SUP=0.1;
	public double MIN_CONF1=0.3;
	public double MIN_CONF2=0.5;
	public String TRAINING_FILENAME="";
	private String TRANSFORMED_TRAINING_FILENAME="";
	public composite_mpp pattern_miner=null;
	public CBMPS_classifier classifier=null;
	public prediction_result EVALUATION_RESULT=null;

	public CBMPS(String f,double ms,double mc1,double mc2)
	{
		train(f,ms,mc1,mc2);
//		TRAINING_FILENAME=f;
//		MIN_SUP=ms;
//		MIN_CONF1=mc1;
//		MIN_CONF2=mc2;

//		pattern_miner=new composite_mpp();
//		pattern_miner.train(TRAINING_FILENAME,MIN_SUP,MIN_CONF1,MIN_CONF2);
//		TRANSFORMED_TRAINING_FILENAME=pattern_miner.get_feature_transformed_file();
//		classifier=new CBMPS_classifier(pattern_miner.MPP_FILENAME);
//		EVALUATION_RESULT=classifier.batch_prediction(TRANSFORMED_TRAINING_FILENAME);
	}

	public CBMPS()
	{}
	
	public void train(String f,double ms,double mc1,double mc2)
	{
		TRAINING_FILENAME=f;
		MIN_SUP=ms;
		MIN_CONF1=mc1;
		MIN_CONF2=mc2;

		pattern_miner=new composite_mpp();
		pattern_miner.train(TRAINING_FILENAME,MIN_SUP,MIN_CONF1,MIN_CONF2);
		TRANSFORMED_TRAINING_FILENAME=pattern_miner.get_feature_transformed_file();
		classifier=new CBMPS_classifier(pattern_miner.MPP_FILENAME);
		classifier.DETAIL_DISPLAY=false;
//Do not evaluate training data
//		EVALUATION_RESULT=classifier.batch_prediction(TRANSFORMED_TRAINING_FILENAME);
	}

	public prediction_result predict(String f)
	{
		prediction_result result=null;
		try
		{
			String temp_transformed_filename=pattern_miner.get_feature_transformed_file(f);
			result=classifier.batch_prediction(temp_transformed_filename);
		}
		catch(Exception e)
		{
			System.out.println("CBMPS predict exception:"+e);
		}
		return result;
	}

	public static void main(String args[])
	{
		long start_time=System.currentTimeMillis();
		//Parameters: File, min_sup, min_conf1, min_conf2(no use now)
		CBMPS a=new CBMPS(args[0],Double.parseDouble(args[1]),Double.parseDouble(args[2]),Double.parseDouble(args[3]));
		System.out.println("MPP_File:"+a.pattern_miner.MPP_FILENAME);
		System.out.println("Training Time:"+(System.currentTimeMillis()-start_time));
		long testing_start_time=System.currentTimeMillis();
//		System.out.println(a.EVALUATION_RESULT.toString());
		for(int i=4;i<args.length;i++)
			System.out.println(a.predict(args[i]).toString());
		long end_time=System.currentTimeMillis();
		System.out.println("Testing Time:"+(end_time-testing_start_time));
		System.out.println("Execution Time:"+(end_time-start_time));
	}
}
