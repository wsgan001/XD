
package IntegratedTwoArffs;

import java.lang.*;
import java.util.*;
import java.io.*;



public class check_arff_list{


	String[] arffs;		// �O���uarff�v�ӼơI
	String[] arffs_paths;	// �O���uarff�v�����|�I


	public check_arff_list(String Source_Path){
		
		try{
		
		
			File filelist = new File(Source_Path);
     		
     			String[] flist = filelist.list();
     		
     		
     			System.out.println(filelist.getPath());
     			File[] files = filelist.listFiles();
     			
     			
     			// �q�ӷ���Ƨ�Ū�X�Ҧ������ɦW��arff���ɮ�!!!
     			Retrieving_ArffFiles(flist, files);
			
		
  		}catch(Exception e){
  			System.out.println(" Error about checking the list of arff files (in check_arff_list.java): "+e.toString());
  		}			
		
	}


//=======================================================================================
//=======================================================================================
//=======================================================================================


	public String[] getArffFiles(){	// �^�ǡu��z�᪺ timeseries_��T�v
		return arffs;
	}

	public String[] getArffFiles_Paths(){	// �^�ǡu��z�᪺ timeseries_��T�v
		return arffs_paths;	
	}

//=======================================================================================
//=======================================================================================
//=======================================================================================



	
	public void Retrieving_ArffFiles(String[] flist, File[] files){
	
		try{
			int valid_num=0;
		
			// 01
			// �T�{"_timseries.arff"��r�ɪ��ƶq
			System.out.println("====================================");
			
			
			
			for(int i=0;i<flist.length;i++){
				
				String check_str ="";	
				if(flist[i].length()<5)continue;		
				for(int j=flist[i].length()-5;j<flist[i].length();j++){
					check_str+=flist[i].charAt(j);
				}
				if(check_str.compareTo(".arff")==0){
					//System.out.println(" flist["+i+"]= "+flist[i]);	
					valid_num++;					
				}				
			}
			
			
			arffs = new String[valid_num];
			arffs_paths = new String[valid_num];
			valid_num=0;
			
			
			for(int i=0;i<flist.length;i++){
				
				String check_str ="";	
				if(flist[i].length()<5)continue;		
				for(int j=flist[i].length()-5;j<flist[i].length();j++){
					check_str+=flist[i].charAt(j);
				}
				if(check_str.compareTo(".arff")==0){
					
					arffs[valid_num]=flist[i];
					arffs_paths[valid_num] = files[i].getPath();					
					valid_num++;	
					
					
				}				
			}
					
					
			for(int i=0;i<arffs.length;i++){
				System.out.println("=================================");
				System.out.println(" arff file["+i+"]= "+arffs[i]);	
				System.out.println(" arff file_paths["+i+"]= "+arffs_paths[i]);						
				System.out.println("=================================");
			}
								
			System.out.println(" arffs_paths.length = "+arffs_paths.length);
			
			
		
		}catch(Exception e){
			System.out.println(" Error about getting time series text file (in LoadFileList.java):"+e.toString());	
		}
		
		
	}




}