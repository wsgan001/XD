package ClassifierModel;


import java.lang.*;
import java.util.*;
import java.io.*;



public class Check_Input_Data{


	String data_path = "";
	String patient_no = "";
	String[] flist;
	String[] disease_list;
	String[] data_path_list;
	
	

	public Check_Input_Data(String data_path, String patient_no){
		
		try{
		
			// �]�w�Ѽ�
			this.data_path = data_path;
			this.patient_no = patient_no;
		
			print();		
		
		
			// �T�{�u�bpred_input��Ƨ��̡v���ɦW
			check_files_list();
		
		
						
			// �T�{�b�upred_input��Ƨ��̬O�_�S�������ɮצs�b�I�v
			if(flist.length==0){
				System.out.println(" Please check your input data directionary again!!! (no files)");
				return;	
			}
		
		
		
			// �q�upred_input��Ƨ��ɮײM����X���w�����e�f�����v
			retrieve_prediction_disease_list();
		
		
		
		
		}catch(Exception e){
			System.out.println(" Error about checking the prediction models (in Check_Prediction_Models.java):"+e.toString());	
		}
		
	}


//-----------------------------------------------
//-----------------------------------------------
//-----------------------------------------------


	public String[] getDataFileList(){	// �^�ǡu/pred_input��Ƨ��̪��Ҧ��ɮצW�ٲM��v
		return 	flist;
	}


	public String[] getDataPathList(){	// �^�ǡu/pred_input��Ƨ��̪��Ҧ��ɮצW�ٲM��v
		return 	data_path_list;
	}




	public String[] getDiseaseList(){
		return disease_list;
	}


//-----------------------------------------------
//-----------------------------------------------
//-----------------------------------------------



	public void retrieve_prediction_disease_list(){
		
		try{
			Vector<String> temp_disease_list = new Vector<String>();
			
				
			for(int i=0;i<flist.length;i++){
				String[] tmp = flist[i].split("_");
				
				// �p�G���O�Ө����̪���ơA�h���U�@���ɮצW��!!!
				if(tmp[0].compareTo(patient_no)!=0){
					continue;						
				}
				
				// �s�W�u�Ө����̪��w���e�f�W�١v
				temp_disease_list.add(tmp[1]);																
			}
		
		
		
			disease_list = new String[temp_disease_list.size()];
		
			for(int i=0;i<temp_disease_list.size();i++){
				String str =  temp_disease_list.get(i);
				
				disease_list[i]="";	// ����l��!!!
				
				
				// �����X�u���ɦW�v
				for(int j=0;j<str.length();j++){
					if(str.charAt(j)=='.')
						break;
					disease_list[i] += str.charAt(j);
				}
					
				System.out.println(" disease list["+i+"] = "+disease_list[i] );
			}
		
		
		
		}catch(Exception e){
			System.out.println(" Error about retrieving the disease list from file list (in Check_Prediction_Models.java): "+e.toString());	
		}
				
		
	}




	public void check_files_list(){
		
		try{
				
			File f = new File(data_path);			

			
						
			if(!f.isDirectory()){	// �T�w�����|�O�_���@�ӡu��Ƨ��v			
				System.out.println(" Please set the correct the data path!!!");
				return;							
			}
			
			// ���o�u�Ӹ�Ƨ��̪��Ҧ����زM��v
			flist =  f.list();
				
				
			data_path_list = new String[flist.length];	
				
			// �C�L��T
			for(int i=0;i<flist.length;i++){
				data_path_list[i] = data_path+"/"+flist[i];
				//System.out.println(" data_path_list["+i+"] = "+data_path_list[i]);
				//System.out.println(" file name["+i+"] = "+flist[i]);
			}

		
		}catch(Exception e){
			System.out.println(" Error about checking the files list (in Check_Prediction_Models.java): "+e.toString());	
		}
		
	}



	public void print(){
		
		
		try{
				
			// �C�L�Ѽ�
			System.out.println(" this.data_path = "+this.data_path);	
			System.out.println(" this.patient_no = "+this.patient_no);
	
		}catch(Exception e){
			System.out.println(" Error about printing the files list (in Check_Prediction_Models.java): "+e.toString());	
		}
		
	}



}