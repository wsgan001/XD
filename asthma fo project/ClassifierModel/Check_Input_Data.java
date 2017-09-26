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
		
			// 設定參數
			this.data_path = data_path;
			this.patient_no = patient_no;
		
			print();		
		
		
			// 確認「在pred_input資料夾裡」的檔名
			check_files_list();
		
		
						
			// 確認在「pred_input資料夾裡是否沒有任何檔案存在！」
			if(flist.length==0){
				System.out.println(" Please check your input data directionary again!!! (no files)");
				return;	
			}
		
		
		
			// 從「pred_input資料夾檔案清單取出欲預測的疾病種類」
			retrieve_prediction_disease_list();
		
		
		
		
		}catch(Exception e){
			System.out.println(" Error about checking the prediction models (in Check_Prediction_Models.java):"+e.toString());	
		}
		
	}


//-----------------------------------------------
//-----------------------------------------------
//-----------------------------------------------


	public String[] getDataFileList(){	// 回傳「/pred_input資料夾裡的所有檔案名稱清單」
		return 	flist;
	}


	public String[] getDataPathList(){	// 回傳「/pred_input資料夾裡的所有檔案名稱清單」
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
				
				// 如果不是該受測者的資料，則換下一個檔案名稱!!!
				if(tmp[0].compareTo(patient_no)!=0){
					continue;						
				}
				
				// 新增「該受測者的預測疾病名稱」
				temp_disease_list.add(tmp[1]);																
			}
		
		
		
			disease_list = new String[temp_disease_list.size()];
		
			for(int i=0;i<temp_disease_list.size();i++){
				String str =  temp_disease_list.get(i);
				
				disease_list[i]="";	// 先初始化!!!
				
				
				// 不取出「副檔名」
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

			
						
			if(!f.isDirectory()){	// 確定此路徑是否為一個「資料夾」			
				System.out.println(" Please set the correct the data path!!!");
				return;							
			}
			
			// 取得「該資料夾裡的所有項目清單」
			flist =  f.list();
				
				
			data_path_list = new String[flist.length];	
				
			// 列印資訊
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
				
			// 列印參數
			System.out.println(" this.data_path = "+this.data_path);	
			System.out.println(" this.patient_no = "+this.patient_no);
	
		}catch(Exception e){
			System.out.println(" Error about printing the files list (in Check_Prediction_Models.java): "+e.toString());	
		}
		
	}



}