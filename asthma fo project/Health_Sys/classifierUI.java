package Health_Sys;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import javax.swing.event.*;
import java.text.SimpleDateFormat;


public class classifierUI extends JInternalFrame {
	JPanel contentPane;
	JTabbedPane JTab=new JTabbedPane();
	JPanel filePanel = new JPanel();     //檔案設定頁面
	SpringLayout springLayout1 = new SpringLayout();
	JPanel profilePanel = new JPanel();   //檔案設定-基本資料1
	SpringLayout profileLayout = new SpringLayout();
	JPanel testPanel = new JPanel();	//檔案設定-測試資料夾2
	SpringLayout testLayout = new SpringLayout();
	
	JScrollPane jScrollPane = new JScrollPane();	//分析檔案選項
   	JPanel checkPanel = new JPanel();
   	//JCheckBox[] check = new JCheckBox[17];
   	JCheckBox[] check = null;
   	JButton selectBtn = new JButton("選擇全部");
	JButton cancelBtn = new JButton("取消選擇");
   	SpringLayout checkLayout = new SpringLayout();
   	
	JPanel targetPanel = new JPanel();	//檔案設定-目標項目3
	SpringLayout targetLayout = new SpringLayout();

	JPanel parameterPanel = new JPanel();   //參數輸入頁面
	SpringLayout springLayout2 = new SpringLayout();
	JPanel regionPanel = new JPanel();	//參數輸入-健康區間1
	SpringLayout regionLayout = new SpringLayout();
	JPanel thresholdPanel = new JPanel();	//參數輸入-門檻值2
	SpringLayout thresholdLayout = new SpringLayout();
	
	JLabel titleLabel1 = new JLabel("基本資料與身體評估檔");	//titleLabel1~3 檔案設定  4~8參數設定
	JLabel titleLabel2 = new JLabel("測試資料路徑");
	JLabel titleLabel3 = new JLabel("目標疾病");
	JLabel titleLabel4 = new JLabel("下限值");
	JLabel titleLabel5 = new JLabel("上限值");
	JLabel titleLabel6 = new JLabel("門檻值");
	JLabel titleLabel7 = new JLabel("訓練資料集的比例");
	JLabel titleLabel8 = new JLabel("疾病名稱");
	JTextField browseTextField1 = new JTextField();
	JTextField browseTextField2 = new JTextField();
	JTextField browseTextField3 = new JTextField();
	JTextField parameter1 = new JTextField();  //parameter1~5  參數設定頁面
	JTextField parameter2 = new JTextField();
	JTextField parameter3 = new JTextField();
	JTextField parameter5 = new JTextField();
	JButton browseBtn1 = new JButton("選擇檔案");
	JButton browseBtn2 = new JButton("選擇路徑");
	JButton browseBtn3 = new JButton("選擇疾病");
	JFileChooser jFileChooser1 = new JFileChooser("./");	
	JFileChooser jFileChooser2 = new JFileChooser("./");	
	JFileChooser jFileChooser3 = new JFileChooser("./");	
	String fileNameStr1 = "";
	String fileNameStr2 = "";
	String fileNameStr3 = "";
	JButton returnBtn1 = new JButton("關閉視窗");
	JButton returnBtn2 = new JButton("關閉視窗");
	JButton submitBtn = new JButton("執行");
	JSlider trainSlider = new JSlider(0, 100, 70);
	JLabel trainLabel1 = new JLabel("0");
    JLabel trainLabel2 = new JLabel("100");
	JLabel trainLabel3 = new JLabel("70");
    JLabel trainLabel4 = new JLabel("%");
    
	private int exams_count=0;
    
    
    JFrame menuStart;


	public classifierUI(JFrame menuStart) {
		super("建構預測模組",true,true,true,true);
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try {
			this.menuStart = menuStart;
			jbInit();
		} 
		catch(Exception e)  { 
			e.printStackTrace(); 
		}
	}
	
	private void jbInit() throws Exception  { 
	
		File[] all_exams=(new File("Data")).listFiles();
		check=new JCheckBox[all_exams.length];
		exams_count=all_exams.length;
		for(int i=0;i<exams_count;i++)
		{
			check[i]=new JCheckBox(all_exams[i].getName());
		}
	   	/*check[0] = new JCheckBox("B型肝炎檢查");    
	   	check[1] = new JCheckBox("C型肝炎檢查");
	   	check[2] = new JCheckBox("甲狀腺功能檢查");
	   	check[3] = new JCheckBox("血脂肪");
	   	check[4] = new JCheckBox("肝臟功能");
	   	check[5] = new JCheckBox("肥胖度檢查");
	   	check[6] = new JCheckBox("青光眼及視網膜檢查");
	   	check[7] = new JCheckBox("骨質疏鬆症篩檢");    
	   	check[8] = new JCheckBox("高血壓篩檢");
	   	check[9] = new JCheckBox("基本資料與身體評估");
	   	check[10] = new JCheckBox("貧血及血癌篩檢");
	   	check[11] = new JCheckBox("痛風篩檢");
	   	check[12] = new JCheckBox("發炎指標");
	   	check[13] = new JCheckBox("腎臟功能");
	   	check[14] = new JCheckBox("腹部腫瘤篩檢");
	   	check[15] = new JCheckBox("電解質平衡");
	   	check[16] = new JCheckBox("糖尿病篩檢");*/
	   	
		this.setSize(510,515);
		this.setTitle("建構預測模組");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();   //frame location
		Dimension frameSize = this.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
      
		this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		this.setVisible(true);
		
		
		final classifierUI adaptee = this;
				 	
		browseBtn1.addActionListener(new java.awt.event.ActionListener() {		//選檔
			public void actionPerformed(ActionEvent e) {
		   	if(jFileChooser1.APPROVE_OPTION == jFileChooser1.showOpenDialog(adaptee)){
		     		fileNameStr1 = jFileChooser1.getSelectedFile().getPath();
        			browseTextField1.setText(fileNameStr1);
			}
			}
		}); 	
		
		/*
		browseBtn2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
			jFileChooser2.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);	
			if(jFileChooser2.APPROVE_OPTION == jFileChooser2.showOpenDialog(adaptee)){
		     	fileNameStr2 = jFileChooser2.getSelectedFile().getPath();
        		browseTextField2.setText(fileNameStr2);
			}
			}   
		}); 	
		*/
		
		browseBtn3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
			if(parameter5.getText().length() > 0)	
				copyFiles();
			else{
				JOptionPane.showMessageDialog(null,"請輸入疾病名稱","警示視窗", JOptionPane.ERROR_MESSAGE);
				return;
			}
        		
        			jFileChooser3.setFileSelectionMode(JFileChooser.FILES_ONLY);
        			jFileChooser3.setCurrentDirectory(new File("./"+parameter5.getText()));
			if(jFileChooser3.APPROVE_OPTION == jFileChooser3.showOpenDialog(adaptee)){
		     		fileNameStr3 = jFileChooser3.getSelectedFile().getName();
        				browseTextField3.setText(fileNameStr3);
			}
			}
		}); 
		
		returnBtn1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
			   	dispose();
			    menuStart.setVisible(true);
			}
		}); 
		
		returnBtn2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
			   	dispose();
			    menuStart.setVisible(true);
			}
		}); 
		
		submitBtn.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String path = "PredictionModels";
				String dstDir = parameter5.getText();
				
				File createFile = new File(path,dstDir);
				createFile.mkdir();
			   	
			   	writeFileWhenSubmit();
			   	
				copyFile();		//複製arff檔
			}
		}); 						
		
		contentPane = (JPanel) this.getContentPane();
		contentPane.add(JTab, BorderLayout.CENTER);
		JTab.addTab("檔案設定", filePanel);
		JTab.addTab("參數輸入", parameterPanel);	
		fileLayout();
		parameterLayout();
	}
  	   
    private void writeFileWhenSubmit() {     //寫檔
    	try { 
    		//File file = new File("C:/Integrated_HealthAnalysisSys/input_parameters.ini");
    		//FileWriter fw = new FileWriter(file);
    		//BufferedWriter bw = new BufferedWriter(fw);
    		Double threshold = new Double(trainSlider.getValue());
    		threshold = threshold / 100;
    		
    		/*bw.write(browseTextField1.getText() + "\r\n");
    		bw.write(browseTextField2.getText() + "\r\n");
    		bw.write(browseTextField3.getText() + "\r\n");
    		bw.write(parameter1.getText() + "\r\n");
    		bw.write(parameter2.getText() + "\r\n");
    		bw.write(parameter3.getText() + "\r\n");
    		bw.write(parameter5.getText() + "_seq.arff\r\n");
    		bw.write(threshold + "\r\n");
    		bw.write(parameter5.getText());
    		
    		bw.close();
    		fw.close();
    		file = null;*/
    		
    		HAS has = new HAS(browseTextField1.getText(), 
    						  "./"+parameter5.getText(),
    						  browseTextField3.getText(),
    						  parameter1.getText(),
    						  parameter2.getText(),
    						  parameter3.getText(),
    						  parameter5.getText() + "_seq.arff",
    						  threshold+"",
    						  parameter5.getText());
    				 
/*			new moduleResult(has.unbalance_accuracy,
					has.unbalance_precision,
					has.unbalance_recall,
					has.balance_accuracy,
					has.balance_precision,
					has.balance_recall);			
*/		
		BufferedWriter bw=new BufferedWriter(new FileWriter(parameter5.getText()+"/output_arffs/" + parameter5.getText() + ".info"));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z");
		bw.write("["+parameter5.getText()+"]疾病預測模組 (建立時間:"+formatter.format(Calendar.getInstance().getTime())+")\n");
		bw.write("基本測試報告==============================\n");
		bw.write("原始資料 準確度(Accuracy):"+has.unbalance_accuracy+"\n");
		bw.write("原始資料 針對疾病發作的精確度(Precision):"+has.unbalance_precision+"\n");
		bw.write("原始資料 針對疾病發作的召回率(Recall):"+has.unbalance_recall+"\n");
		bw.write("[較客觀]平均取樣後資料 準確度(Accuracy):"+has.balance_accuracy+"\n");
		bw.write("[較客觀]平均取樣後資料 針對疾病發作的精確度(Precision):"+has.balance_precision+"\n");
		bw.write("[較客觀]平均取樣後資料 針對疾病發作的召回率(Recall):"+has.balance_recall+"\n");
		bw.close();
		
		new moduleResult(parameter5.getText()+"/output_arffs/" + parameter5.getText() + ".info");
    			  
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    private void copyFile(){
    		copyFileTo(parameter5.getText()+"/output_arffs/" + parameter5.getText() + "_combined_arff.arff",
    				"PredictionModels/" + parameter5.getText()+"/"+ parameter5.getText() + "_combined_arff.arff");    		
    		copyFileTo(parameter5.getText()+"/output_arffs/j48.model",
    				"PredictionModels/" + parameter5.getText()+"/"+ parameter5.getText() + ".model");    		
    		copyFileTo(parameter5.getText()+"/output_arffs/"+ parameter5.getText()+".info",
    				"PredictionModels/" + parameter5.getText()+"/"+ parameter5.getText() + ".info");
    		copyFileTo("log/"+ parameter5.getText()+"_patient_order_log",
    				"PredictionModels/" + parameter5.getText()+"/"+ parameter5.getText() + "_patient_order_log");
    		copyFileTo("log/"+ parameter5.getText()+"_file_logs",
    				"PredictionModels/" + parameter5.getText()+"/"+ parameter5.getText() + "_file_logs");
    		copyFileTo("log/"+ parameter5.getText()+"_order_logs",
    				"PredictionModels/" + parameter5.getText()+"/"+ parameter5.getText() + "_order_logs");


		copyFileTo(parameter5.getText()+"/output_arffs/j48.txt",
    				"PredictionModels/" + parameter5.getText()+"/J48.tree");
    		copyFileTo(parameter5.getText()+"/output_arffs/"+ parameter5.getText()+"_MappingInfo.txt",
    				"PredictionModels/" + parameter5.getText()+"/"+ parameter5.getText() + "_MappingInfo.txt");
    		copyFileTo("log/"+ parameter5.getText()+"_pattern_mapping_info_log",
    				"PredictionModels/" + parameter5.getText()+"/"+ parameter5.getText() + "_pattern_mapping_info_log");    		
    				    		
    		text_tree_rebuild(parameter5.getText()+"/output_arffs/J48.txt",
    					parameter5.getText()+"/output_arffs/"+ parameter5.getText()+"_MappingInfo.txt",
    					"log/"+ parameter5.getText()+"_pattern_mapping_info_log",
    					"PredictionModels/" + parameter5.getText()+"/"+ parameter5.getText() + ".tree");
    		
    		//File data_dir=new File("PredictionModels/"+parameter5.getText()+"/data");
    		//data_dir.mkdir();
    		//move_data_folder(parameter5.getText());
  }
  	
  	private void move_data_folder(String disease_name)
  	{
  		try
  		{
  			File dir_f=new File(disease_name);
  			File[] file_list=dir_f.listFiles();
  			for(int i=0;i<file_list.length;i++)
  			{
  				if(file_list[i].isFile())
  					copyFileTo(disease_name+"/"+file_list[i].getName(), "PredictionModels/"+disease_name+"/data/"+file_list[i].getName());
  			}
  				
  			delete_dir(new File(disease_name));
  		}
  		catch(Exception e)
  		{
  			System.out.println("classifierUI move_data_folder exception:"+e);
  		}
  	}
  	
  	private void delete_dir(File p)
  	{
  		try
  		{
  			if(p.isDirectory())
  			{
  				File[] f_list=p.listFiles();
  				for(int i=0;i<f_list.length;i++)
  					delete_dir(f_list[i]);
  			}
  			else
  				p.delete();
  		}
  		catch(Exception e)
  		{
  			System.out.println("classifierUI delete_dir exception:"+e);
  		}
  	}
  	//==============================================================Lobby 201001=======tree transform====start
	
	private void  text_tree_rebuild(String itree, String static_info, String pattern_info, String otree)
	{
		try
		{
			HashMap<String,String> mapping_table=load_mapping_table(static_info,pattern_info);
			HashMap<String,String> node_name_map=new HashMap<String,String>();
			BufferedWriter bw=new BufferedWriter(new FileWriter(otree));
			BufferedReader br=new BufferedReader(new FileReader(itree));
			String buffer="";
			while((buffer=br.readLine())!=null)
			{
				System.out.println(buffer);
				if(buffer.indexOf("[")>=0 && buffer.indexOf("->")>=0)
				{
					//is a relation
					System.out.println("Relation");
					String pre_node_symbol=buffer.substring(0,buffer.indexOf("->")).trim();
					String value_string=fetch_attribute("label",buffer);
					if(value_string.indexOf(" ")>=0)
						value_string=value_string.substring(value_string.indexOf(" ")+1);
					if(node_name_map.containsKey(pre_node_symbol))
					{
						String real_name=(String)node_name_map.get(pre_node_symbol);						
						String key_string=real_name+":"+value_string;
						System.out.println(pre_node_symbol+" <"+key_string+">");
						if(mapping_table.containsKey(key_string))
						{
							String real_name_and_value=real_name+":"+(String)mapping_table.get(key_string);
							buffer=buffer.replaceFirst(value_string,real_name_and_value);
							System.out.println("==>"+real_name_and_value);
						}
					}
					else
						System.out.println("No node:"+pre_node_symbol);
				}
				else if(buffer.indexOf("[")>=0)
				{
					// is a node
					System.out.println("Node");
					String node_symbol=buffer.substring(0,buffer.indexOf("[")).trim();
					String temp_symbol=fetch_attribute("label",buffer);
					if(mapping_table.containsKey(temp_symbol))
					{
						String real_name=(String)mapping_table.get(temp_symbol);
						buffer=buffer.replaceFirst(temp_symbol,real_name);
						System.out.println(temp_symbol+"==>"+real_name);
						node_name_map.put(node_symbol,real_name);
						System.out.println(node_symbol+" saved "+real_name);
					}					
				}
				bw.write(buffer+"\n");
			}
			bw.close();
			br.close();
		}
		catch(Exception e)
		{
			System.out.println("classifierUI text_tree_rebuild exception:"+e);
		}
	}
	
	private String fetch_attribute(String n,String c)
	{
		String result="";
		try
		{
			if(c.indexOf(n)>=0)
			{
				result=c.substring(c.indexOf("=",c.indexOf(n))+1);
				result=result.trim();
				if(result.charAt(0)=='\"')
				{
					int temp=result.indexOf('\"',1);
					result=result.substring(1,temp);
				}
				else
				{
					if(result.indexOf(" ")>=0)
						result=result.substring(0,result.indexOf(" "));
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("tree_parser fetch_attribute exception:"+e);
		}
		return result;
	}
	
	private HashMap<String,String> load_mapping_table(String static_info,String pattern_info)
	{
		HashMap<String,String> mapping_table=new HashMap<String,String>();
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(static_info));
			String buffer="";
			while((buffer=br.readLine())!=null)
			{
				//System.out.println(buffer);
				if(buffer.indexOf("@attribute")==0)
				{
					String temp_key=buffer.substring(buffer.indexOf(" "), buffer.indexOf("--->")).trim();
					String temp_value=buffer.substring(buffer.indexOf("actual name: "),buffer.indexOf("{"));
					temp_value=temp_value.substring(temp_value.indexOf(":")+1).trim();
					mapping_table.put(temp_key,temp_value);
				}
				else
				{
					//different
					String real_value=buffer.substring(0,buffer.indexOf("--->")).trim();
					String item_name=real_value.substring(0,real_value.lastIndexOf("_")).trim();
					real_value=real_value.substring(real_value.lastIndexOf("_")+1).trim();
					String transformed_value=buffer.substring(buffer.lastIndexOf(":")+1).trim();
					
					mapping_table.put(item_name+":"+transformed_value,real_value);
				}
			}
			br.close();
			System.out.println("load static mapping information.");
			
			br=new BufferedReader(new FileReader(pattern_info));
			while((buffer=br.readLine())!=null)
			{
				//System.out.println(buffer);
				String temp_key=buffer.substring(0,buffer.indexOf(":")).trim();
				String temp_value=buffer.substring(buffer.indexOf(":")+1).trim();
				mapping_table.put(temp_key,temp_value);
			}
			br.close();
			System.out.println("load dynamic mapping information.");
		}
		catch(Exception e)
		{
			System.out.println("classifierUI load_mapping_table exception:"+e);
		}
		return mapping_table;
	}
	
	//==============================================================Lobby 201001=======tree transform====end

	private void copyFileTo(String in, String out)
	{	
		try
		{		
			FileInputStream fis  = new FileInputStream(new File(in));
			FileOutputStream fos = new FileOutputStream(new File(out));
			byte[] buf = new byte[1024];
			int i = 0;
			while ((i = fis.read(buf)) != -1)
			{
				fos.write(buf, 0, i);
			}
			if (fis != null) fis.close();
			if (fos != null) fos.close();
		} 
		catch (Exception e)
		{
			System.out.println("classifierUI copyFileTo exception:"+e);
		}
	}  		
  		
  	private void fileLayout() throws Exception  {
  		
  		//profilePanel testPanel targetPanel returnBtn1排版
  		filePanel.setLayout(springLayout1); 
  		browseTextField1.setPreferredSize(new Dimension(330, 25));
		browseTextField2.setPreferredSize(new Dimension(330, 25));
		browseTextField3.setPreferredSize(new Dimension(330, 25));
  		browseTextField1.setEditable(false);
  		browseTextField3.setEditable(false);
		
		filePanel.add(profilePanel);
		filePanel.add(testPanel);
		filePanel.add(targetPanel);
		filePanel.add(returnBtn1);
		springLayout1.putConstraint(SpringLayout.WEST, profilePanel, 10, SpringLayout.WEST, filePanel);
		springLayout1.putConstraint(SpringLayout.NORTH, profilePanel,10, SpringLayout.NORTH, filePanel);
		springLayout1.putConstraint(SpringLayout.WEST, testPanel, 10, SpringLayout.WEST, filePanel);
		springLayout1.putConstraint(SpringLayout.NORTH, testPanel,15, SpringLayout.SOUTH, profilePanel);
		springLayout1.putConstraint(SpringLayout.WEST, targetPanel, 10, SpringLayout.WEST, filePanel);
		springLayout1.putConstraint(SpringLayout.NORTH, targetPanel,15, SpringLayout.SOUTH, testPanel);
		springLayout1.putConstraint(SpringLayout.EAST, returnBtn1, -20, SpringLayout.EAST, filePanel);
		springLayout1.putConstraint(SpringLayout.SOUTH, returnBtn1, -13, SpringLayout.SOUTH, filePanel);
		
		//profilePanel
		profilePanel.setLayout(profileLayout); 
		profilePanel.add(titleLabel1);
		profilePanel.add(browseTextField1);
		profilePanel.add(browseBtn1);
		profilePanel.setPreferredSize(new Dimension(480,90));
		profilePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE,1),"基本資料"));
		profileLayout.putConstraint(SpringLayout.WEST, titleLabel1, 20, SpringLayout.WEST, profilePanel);
		profileLayout.putConstraint(SpringLayout.NORTH, titleLabel1,5, SpringLayout.NORTH, profilePanel);	
		profileLayout.putConstraint(SpringLayout.WEST, browseTextField1, 20, SpringLayout.WEST, profilePanel);
		profileLayout.putConstraint(SpringLayout.NORTH, browseTextField1,5, SpringLayout.SOUTH, titleLabel1); 
		profileLayout.putConstraint(SpringLayout.WEST, browseBtn1, 15, SpringLayout.EAST, browseTextField1);
		profileLayout.putConstraint(SpringLayout.NORTH, browseBtn1, 5, SpringLayout.SOUTH, titleLabel1);
	
		//testPanel
		testPanel.setLayout(testLayout);
			//testPanel.add(titleLabel2);
			//testPanel.add(browseTextField2);
			//testPanel.add(browseBtn2);
		testPanel.add(titleLabel8);
		testPanel.add(parameter5);
		testPanel.add(jScrollPane);
		testPanel.add(selectBtn);
		testPanel.add(cancelBtn);
		testPanel.setPreferredSize(new Dimension(480,180));
		testPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.ORANGE,1),"測試資料夾"));
		/*
		testLayout.putConstraint(SpringLayout.WEST, titleLabel2, 20, SpringLayout.WEST, testPanel);
		testLayout.putConstraint(SpringLayout.NORTH, titleLabel2, 5, SpringLayout.NORTH, testPanel);
		testLayout.putConstraint(SpringLayout.WEST, browseTextField2, 20, SpringLayout.WEST, testPanel);
		testLayout.putConstraint(SpringLayout.NORTH, browseTextField2, 5, SpringLayout.SOUTH, titleLabel2); 
		testLayout.putConstraint(SpringLayout.WEST, browseBtn2, 15, SpringLayout.EAST, browseTextField2);
		testLayout.putConstraint(SpringLayout.NORTH, browseBtn2, 5, SpringLayout.SOUTH, titleLabel2);
		*/
		testLayout.putConstraint(SpringLayout.WEST, titleLabel8, 20, SpringLayout.WEST, testPanel);
		testLayout.putConstraint(SpringLayout.NORTH, titleLabel8, 7,SpringLayout. NORTH, testPanel);
		testLayout.putConstraint(SpringLayout.WEST, parameter5, 85, SpringLayout.WEST, testPanel);
		testLayout.putConstraint(SpringLayout.NORTH, parameter5, 5, SpringLayout.NORTH, testPanel);
		testLayout.putConstraint(SpringLayout.WEST, jScrollPane, 20, SpringLayout.EAST, parameter5);
		testLayout.putConstraint(SpringLayout.NORTH, jScrollPane, 3, SpringLayout.NORTH, testPanel);  
  		testLayout.putConstraint(SpringLayout.WEST, selectBtn, 90, SpringLayout.WEST, testPanel);
		testLayout.putConstraint(SpringLayout.NORTH, selectBtn, 20, SpringLayout.SOUTH, titleLabel8);  
  		testLayout.putConstraint(SpringLayout.WEST, cancelBtn, 90, SpringLayout.WEST, testPanel);
		testLayout.putConstraint(SpringLayout.NORTH, cancelBtn, 10, SpringLayout.SOUTH, selectBtn);  
  		checkitems();
  		
  		
		//targetPanel
		targetPanel.setLayout(targetLayout);
		targetPanel.add(titleLabel3);	
		targetPanel.add(browseTextField3);
		targetPanel.add(browseBtn3);
		targetPanel.setPreferredSize(new Dimension(480,90));
		targetPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GREEN,1),"目標疾病"));
		targetLayout.putConstraint(SpringLayout.WEST, titleLabel3, 20, SpringLayout.WEST, targetPanel);
		targetLayout.putConstraint(SpringLayout.NORTH, titleLabel3, 5, SpringLayout.NORTH, targetPanel);
		targetLayout.putConstraint(SpringLayout.WEST, browseTextField3,20, SpringLayout.WEST, targetPanel);
		targetLayout.putConstraint(SpringLayout.NORTH, browseTextField3, 5, SpringLayout.SOUTH, titleLabel3); 
		targetLayout.putConstraint(SpringLayout.WEST, browseBtn3, 15, SpringLayout.EAST, browseTextField3);
		targetLayout.putConstraint(SpringLayout.NORTH, browseBtn3, 5, SpringLayout.SOUTH, titleLabel3);
  	}
  	
  	private void checkitems() throws Exception  {
		   	
		selectBtn.addActionListener(new java.awt.event.ActionListener() {	//全選
			public void actionPerformed(ActionEvent e) {
			   	for(int count=0 ; count<exams_count ; count++){
			   		check[count].setSelected(true);
			   	}
			}
		}); 

		cancelBtn.addActionListener(new java.awt.event.ActionListener() {	//取消選擇
			public void actionPerformed(ActionEvent e) {
			   	for(int count=0 ; count<exams_count ; count++){
			   		check[count].setSelected(false);
			   	}
			}
		}); 		
		
		jScrollPane.setPreferredSize(new Dimension(250, 145));		//checkPanel大小
		jScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1), "欲分析項目"));
		jScrollPane.getViewport().add(checkPanel);
		
		checkPanel.setPreferredSize(new Dimension(200, 35*exams_count));
		checkPanel.setLayout(checkLayout);
		//modulePanel.add(sbV);

	   	for(int count=0 ; count<exams_count ; count++){
	   		checkPanel.add(check[count]);
	   	}
	   	for(int count=0 ; count<exams_count ; count++){	   	
	   		checkLayout.putConstraint(SpringLayout.WEST, check[count], 20, SpringLayout.WEST, checkPanel);
	   		
	   		if(count==0)
	   			checkLayout.putConstraint(SpringLayout.NORTH, check[count], 5, SpringLayout.NORTH, checkPanel);
	   		else	   		
				checkLayout.putConstraint(SpringLayout.NORTH, check[count], 7, SpringLayout.SOUTH, check[count-1]);
	   	}		
/*	
		checkLayout.putConstraint(SpringLayout.WEST, check[0], 20, SpringLayout.WEST, checkPanel);
		checkLayout.putConstraint(SpringLayout.NORTH, check[0], 5, SpringLayout.NORTH, checkPanel);
		checkLayout.putConstraint(SpringLayout.WEST, check[1], 20, SpringLayout.WEST, checkPanel);
		checkLayout.putConstraint(SpringLayout.NORTH, check[1], 7, SpringLayout.SOUTH, check[0]);
		checkLayout.putConstraint(SpringLayout.WEST, check[2], 20, SpringLayout.WEST, checkPanel);
		checkLayout.putConstraint(SpringLayout.NORTH, check[2], 7, SpringLayout.SOUTH, check[1]);
		checkLayout.putConstraint(SpringLayout.WEST, check[3], 20, SpringLayout.WEST, checkPanel);
		checkLayout.putConstraint(SpringLayout.NORTH, check[3], 7, SpringLayout.SOUTH, check[2]);
		checkLayout.putConstraint(SpringLayout.WEST, check[4], 20, SpringLayout.WEST, checkPanel);
		checkLayout.putConstraint(SpringLayout.NORTH, check[4], 7, SpringLayout.SOUTH, check[3]);
		checkLayout.putConstraint(SpringLayout.WEST, check[5], 20, SpringLayout.WEST, checkPanel);
		checkLayout.putConstraint(SpringLayout.NORTH, check[5], 7, SpringLayout.SOUTH, check[4]);
		checkLayout.putConstraint(SpringLayout.WEST, check[6], 20, SpringLayout.WEST, checkPanel);
		checkLayout.putConstraint(SpringLayout.NORTH, check[6], 7, SpringLayout.SOUTH, check[5]);	  
		checkLayout.putConstraint(SpringLayout.WEST, check[7], 20, SpringLayout.WEST, checkPanel);
		checkLayout.putConstraint(SpringLayout.NORTH, check[7], 7, SpringLayout.SOUTH, check[6]);	
		checkLayout.putConstraint(SpringLayout.WEST, check[8], 20, SpringLayout.WEST, checkPanel);
		checkLayout.putConstraint(SpringLayout.NORTH, check[8],7, SpringLayout.SOUTH, check[7]);	
		checkLayout.putConstraint(SpringLayout.WEST, check[9], 20, SpringLayout.WEST, checkPanel);
		checkLayout.putConstraint(SpringLayout.NORTH, check[9], 7, SpringLayout.SOUTH, check[8]);	
		checkLayout.putConstraint(SpringLayout.WEST, check[10], 20, SpringLayout.WEST, checkPanel);
		checkLayout.putConstraint(SpringLayout.NORTH, check[10], 7, SpringLayout.SOUTH, check[9]);	
		checkLayout.putConstraint(SpringLayout.WEST, check[11], 20, SpringLayout.WEST, checkPanel);
		checkLayout.putConstraint(SpringLayout.NORTH, check[11], 7, SpringLayout.SOUTH, check[10]);	
		checkLayout.putConstraint(SpringLayout.WEST, check[12], 20, SpringLayout.WEST, checkPanel);
		checkLayout.putConstraint(SpringLayout.NORTH, check[12], 7, SpringLayout.SOUTH, check[11]);	
		checkLayout.putConstraint(SpringLayout.WEST, check[13], 20, SpringLayout.WEST, checkPanel);
		checkLayout.putConstraint(SpringLayout.NORTH, check[13], 7, SpringLayout.SOUTH, check[12]);	
		checkLayout.putConstraint(SpringLayout.WEST, check[14], 20, SpringLayout.WEST, checkPanel);
		checkLayout.putConstraint(SpringLayout.NORTH, check[14], 7, SpringLayout.SOUTH, check[13]);	
		checkLayout.putConstraint(SpringLayout.WEST, check[15], 20, SpringLayout.WEST, checkPanel);
		checkLayout.putConstraint(SpringLayout.NORTH, check[15], 7, SpringLayout.SOUTH, check[14]);	
		checkLayout.putConstraint(SpringLayout.WEST, check[16], 20, SpringLayout.WEST, checkPanel);
		checkLayout.putConstraint(SpringLayout.NORTH, check[16], 7, SpringLayout.SOUTH, check[15]);	
*/
	}
	
  	
  	private void parameterLayout() throws Exception  {
  		
  		//regionPanel thresholdPanel datasetPanel returnBtn2 submitBtn排版
  		parameterPanel.setLayout(springLayout2); 
  		parameter1.setPreferredSize(new Dimension(100, 25));
		parameter2.setPreferredSize(new Dimension(100, 25));
		parameter3.setPreferredSize(new Dimension(100, 25));
		parameter5.setPreferredSize(new Dimension(100, 25));
		
		parameterPanel.add(regionPanel);
		parameterPanel.add(thresholdPanel);
		parameterPanel.add(returnBtn2);
		parameterPanel.add(submitBtn);
		springLayout2.putConstraint(SpringLayout.WEST, regionPanel, 10, SpringLayout.WEST, parameterPanel);
		springLayout2.putConstraint(SpringLayout.NORTH, regionPanel,10, SpringLayout.NORTH, parameterPanel);	
		springLayout2.putConstraint(SpringLayout.WEST, thresholdPanel, 10, SpringLayout.WEST, parameterPanel);
		springLayout2.putConstraint(SpringLayout.NORTH, thresholdPanel, 25, SpringLayout.SOUTH, regionPanel); 	
		springLayout2.putConstraint(SpringLayout.EAST, returnBtn2, -20, SpringLayout.EAST, parameterPanel);
		springLayout2.putConstraint(SpringLayout.SOUTH, returnBtn2, -13, SpringLayout.SOUTH, parameterPanel);
  		springLayout2.putConstraint(SpringLayout.EAST, submitBtn, -130, SpringLayout.EAST, parameterPanel);
		springLayout2.putConstraint(SpringLayout.SOUTH, submitBtn, -13, SpringLayout.SOUTH, parameterPanel);	
		
		//regionPanel
		regionPanel.setLayout(regionLayout);
		regionPanel.add(titleLabel4);
		regionPanel.add(titleLabel5);
		regionPanel.add(parameter1);
		regionPanel.add(parameter2);
		regionPanel.setPreferredSize(new Dimension(480,140));
		regionPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE,1),"此項目之健康區間"));
		regionLayout.putConstraint(SpringLayout.WEST, titleLabel4, 90, SpringLayout.WEST, regionPanel);
		regionLayout.putConstraint(SpringLayout.NORTH, titleLabel4, 12, SpringLayout.NORTH, regionPanel);
		regionLayout.putConstraint(SpringLayout.WEST, parameter1, 150, SpringLayout.WEST, regionPanel);
		regionLayout.putConstraint(SpringLayout.NORTH, parameter1, 10, SpringLayout.NORTH, regionPanel); 
	   	regionLayout.putConstraint(SpringLayout.WEST, titleLabel5, 90, SpringLayout.WEST, regionPanel);
		regionLayout.putConstraint(SpringLayout.NORTH, titleLabel5, 30, SpringLayout.SOUTH, titleLabel4);
		regionLayout.putConstraint(SpringLayout.WEST, parameter2, 150, SpringLayout.WEST, regionPanel);
		regionLayout.putConstraint(SpringLayout.NORTH, parameter2, 25, SpringLayout.SOUTH, parameter1); 
		
		//thresholdPanel
		thresholdPanel.setLayout(thresholdLayout);
		thresholdPanel.add(titleLabel6);	//門檻值
		thresholdPanel.add(parameter3);
		thresholdPanel.add(trainSlider);	//比例
		thresholdPanel.add(trainLabel1);
		thresholdPanel.add(trainLabel2);
		thresholdPanel.add(trainLabel3);
		thresholdPanel.add(trainLabel4);
		thresholdPanel.add(titleLabel7);
		thresholdPanel.setPreferredSize(new Dimension(480,140));
		thresholdPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.ORANGE,1),"門檻&比例"));
		thresholdLayout.putConstraint(SpringLayout.WEST, titleLabel6, 90, SpringLayout.WEST, thresholdPanel);
		thresholdLayout.putConstraint(SpringLayout.NORTH, titleLabel6, 12, SpringLayout.NORTH, thresholdPanel);
		thresholdLayout.putConstraint(SpringLayout.WEST, parameter3, 150, SpringLayout.WEST, thresholdPanel);
		thresholdLayout.putConstraint(SpringLayout.NORTH, parameter3, 10, SpringLayout.NORTH, thresholdPanel);
		
		thresholdLayout.putConstraint(SpringLayout.WEST, titleLabel7, 30, SpringLayout.WEST, thresholdPanel);
		thresholdLayout.putConstraint(SpringLayout.NORTH, titleLabel7, 72, SpringLayout.NORTH, thresholdPanel);
 		thresholdLayout.putConstraint(SpringLayout.WEST, trainLabel1, 150, SpringLayout.WEST, thresholdPanel);
		thresholdLayout.putConstraint(SpringLayout.NORTH, trainLabel1, 70, SpringLayout.NORTH, thresholdPanel); 
  		thresholdLayout.putConstraint(SpringLayout.WEST, trainSlider, 0, SpringLayout.EAST, trainLabel1);
		thresholdLayout.putConstraint(SpringLayout.NORTH, trainSlider, 70, SpringLayout.NORTH, thresholdPanel); 
  		thresholdLayout.putConstraint(SpringLayout.WEST, trainLabel2, 0, SpringLayout.EAST, trainSlider);
		thresholdLayout.putConstraint(SpringLayout.NORTH, trainLabel2, 70, SpringLayout.NORTH, thresholdPanel); 
   		thresholdLayout.putConstraint(SpringLayout.WEST, trainLabel3, 25, SpringLayout.EAST, trainLabel2);
		thresholdLayout.putConstraint(SpringLayout.NORTH, trainLabel3, 70, SpringLayout.NORTH, thresholdPanel); 
   		thresholdLayout.putConstraint(SpringLayout.WEST, trainLabel4, 5, SpringLayout.EAST, trainLabel3);
		thresholdLayout.putConstraint(SpringLayout.NORTH, trainLabel4, 70, SpringLayout.NORTH, thresholdPanel); 
	
		//Action Listener
     	trainSlider.addChangeListener(new javax.swing.event.ChangeListener() {
        	public void stateChanged(ChangeEvent e) {
        		Double valueD = Double.parseDouble(trainSlider.getValue() + "");
        		String valueStr = "";
        		//valueD = valueD / 10;
        		valueStr = Double.toString(valueD);
        		        		
           	trainLabel3.setText(valueStr + "");
        	}
    	});

	
		
    }
    
    void copyFiles() {
    	try{
			String path = "./";
			String dstDir = parameter5.getText();
			String[] fileList;
			
			File createFile = new File(path,dstDir);
			createFile.mkdir(); 
			createFile = null;
			
			for(int count=0 ; count<exams_count ; count++){
				if(check[count].isSelected()){
					createFile = new File("Data/" + check[count].getText() + "/");
					fileList = createFile.list();
					for(int count1=0 ; count1<fileList.length ; count1++){
										
					String srcfile = "Data/" + check[count].getText() + "/" + fileList[count1];
					String dstpath = parameter5.getText()+"/"+fileList[count1];
						
					copyFileTo(srcfile,dstpath);
	    				
	    				/*File inputFile = new File(srcfile);
	    				File outputFile = new File(dstpath);
	
	   					FileReader in = new FileReader(inputFile);
						FileWriter out = new FileWriter(outputFile);
	   					int c;
	
						while ((c = in.read()) != -1)
	   						out.write(c);
	
	   					in.close();
	   					out.close();*/
   					}
					
				}
			}   		
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	
    }
    		
/*	protected void processWindowEvent(WindowEvent e) {		//關閉視窗
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			int reply = -1;
			
			reply = JOptionPane.showConfirmDialog(null,"確定要離開此程式嗎?","確認視窗", JOptionPane.YES_NO_OPTION);
			if(reply == 0){
				System.exit(0);	
			}
			else{
				this.setVisible(true);
			}			
		}
	}*/
}