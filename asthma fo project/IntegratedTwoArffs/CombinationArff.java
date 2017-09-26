

package IntegratedTwoArffs;


	
import java.lang.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.io.*;

public class CombinationArff{



	String[] attribute1;			// 取出「欄位屬性名稱」
	String[][] data1;			// 取出「資料庫內容」
	String TableName1;			// 取出「資料名稱」
	FastVector attri_lines1;			// 取出「完整的attribute information」
		

	String[] attribute2;			// 取出「欄位屬性名稱」
	String[][] data2;			// 取出「資料庫內容」
	String TableName2;			// 取出「資料名稱」
	FastVector attri_lines2;			// 取出「完整的attribute information」
		


	String[] arffs;		// 記錄「arff」個數！
	String[] arffs_paths;	// 記錄「arff」的路徑！	


	String[][] combined_data;	// 記錄「combined 的 data」


	public CombinationArff(String source_folder, String disease_name){
	//public static void main(String[] args){
		
		
		try{
		

				
		
				
				System.out.println(" source_folder = "+source_folder);
				System.out.println("");
			
			
//================================================marked by lobby==============
//			check_arff_list cal = new check_arff_list(source_folder);
//				arffs = cal.getArffFiles();
//				arffs_paths = cal.getArffFiles_Paths();
//			
//			System.out.println();
//			System.out.println(" arffs.length = "+arffs.length );
//			System.out.println(" arffs_paths.length = "+arffs_paths.length );
//			System.out.println();
//================================================marked by lobby==============end
//Lobby=================
			String seq_filename=(new File(source_folder,disease_name+"_seq.arff")).getAbsolutePath();
			String static_filename=(new File(source_folder,disease_name+"_J48_static.arff")).getAbsolutePath();
//lobby=================end
				
			//connectionfile cf1 = new connectionfile(args[0]);
			//connectionfile cf1 = new connectionfile(arffs_paths[0]);
			connectionfile cf1 = new connectionfile(seq_filename);
				attribute1 = cf1.getAttribute();
				data1 = cf1.getData();
				TableName1 = cf1.getTableName();
				attri_lines1 = cf1.getAttriLines();	// 記錄「屬性的整筆記錄」
				
			
			//connectionfile cf2 = new connectionfile(args[1]);
			//connectionfile cf2 = new connectionfile(arffs_paths[1]);	
			connectionfile cf2 = new connectionfile(static_filename);
			
				attribute2 = cf2.getAttribute();
				data2 = cf2.getData();
				TableName2 = cf2.getTableName();
				attri_lines2 = cf2.getAttriLines();	// 記錄「屬性的整筆記錄」
				
				
/*
				System.out.println("===========================================================");	
				System.out.println(" cf1.getAttribute() = "+ cf1.getAttribute().length);
				System.out.println(" cf1.getData() = " + cf1.getData().length);
				System.out.println(" cf1.getTableName() =" + cf1.getTableName());
				System.out.println(" cf1.getAttriLines() = "+ cf1.getAttriLines().size());	
				System.out.println("===========================================================");	
				System.out.println(" cf2.getAttribute() = "+ cf2.getAttribute().length);
				System.out.println(" cf2.getData() = " + cf2.getData().length);
				System.out.println(" cf2.getTableName() =" + cf2.getTableName());
				System.out.println(" cf2.getAttriLines() = "+ cf2.getAttriLines().size());	
				System.out.println("===========================================================");	
*/		
			
			//(attribute1, data1, attri_lines1, attribute2, data2, attri_lines2)
			
			//combine_2arffs combine2 = new combine_2arffs(args[0], TableName1, TableName2, attri_lines1, attri_lines2);
			
			
			
			combine_2arffs combine2 = new combine_2arffs(attribute1, data1, attri_lines1, attribute2, data2, attri_lines2);
				combined_data = combine2.getCombinedData();
				System.out.println("===========================================================");	
				System.out.println(" combined_data.length = "+combined_data.length);
				System.out.println("===========================================================");	
		
		
			//output_file of = new output_file( args[0],TableName1, TableName2, attri_lines1, attri_lines2, combined_data);
			output_file of = new output_file( source_folder, TableName1, TableName2, attri_lines1, attri_lines2, combined_data, disease_name);
		
		
		
		
		
		}catch(Exception e){
			System.out.println(" Error in main function ( in CombinationArff.java): "+e.toString());	
		}		
	}
	
}