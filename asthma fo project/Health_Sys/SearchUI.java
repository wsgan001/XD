package Health_Sys;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import javax.swing.event.*;


public class SearchUI extends JFrame {
	JPanel contentPane;
	JPanel inputPanel = new JPanel();
	JPanel resultPanel = new JPanel();
	JButton returnBtn = new JButton("回主畫面");
	SpringLayout springLayout1 = new SpringLayout();
   	
	JFileChooser fileChooser = new JFileChooser();
	JLabel fileLabel = new JLabel("健檢項目檔案");
	JTextField fileTextField = new JTextField("");
	JButton fileBtn = new JButton("選擇檔案");
	String fileNameStr = "";
	JLabel inputLabel = new JLabel("輸入欲查詢之病歷號");
	JTextField inputTextField = new JTextField("");
   	JButton searchBtn = new JButton("確定");
   	SpringLayout inputLayout = new SpringLayout();
   	
	//JButton drawBtn = new JButton("繪圖");
	SpringLayout resultLayout = new SpringLayout();
	
	JFrame menuStart;
	JPanel tablePanel = new JPanel();
	TextArea txa = new TextArea();
	JTable dataTable;
	JScrollPane jScrollPane = new JScrollPane();
	int rowNum, columnNum;

		
	public SearchUI(JFrame menuStart) {		
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try {
			this.menuStart = menuStart;
			jbInit();
		}  catch(Exception e)  { e.printStackTrace(); }
	}
   
	private void jbInit() throws Exception  {
		this.setSize(500,500);
		this.setTitle("病歷號搜尋");	
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
		
		final SearchUI adaptee = this;
		returnBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
			   	dispose();
			    menuStart.setVisible(true);
			}
		}); 
				
		contentPane = (JPanel) this.getContentPane();
		contentPane.setLayout(springLayout1);
      
		inputLayout();
		resultLayout();

		contentPane.add(inputPanel);
		contentPane.add(resultPanel);
		contentPane.add(returnBtn);
		springLayout1.putConstraint(SpringLayout.WEST, inputPanel, 10, SpringLayout.WEST, contentPane);
		springLayout1.putConstraint(SpringLayout.NORTH, inputPanel, 10, SpringLayout.NORTH, contentPane);
		springLayout1.putConstraint(SpringLayout.WEST, resultPanel, 10, SpringLayout.WEST, contentPane);
		springLayout1.putConstraint(SpringLayout.NORTH, resultPanel, 15, SpringLayout.SOUTH, inputPanel);
		springLayout1.putConstraint(SpringLayout.EAST, returnBtn, -20, SpringLayout.EAST, contentPane);
		springLayout1.putConstraint(SpringLayout.NORTH, returnBtn, 15, SpringLayout.SOUTH, resultPanel);		
	}
	
	protected void inputLayout(){
		final SearchUI adaptee = this;
		
		fileBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
		   	if(fileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(adaptee)){
		    	fileNameStr = fileChooser.getSelectedFile().getPath();
        		fileTextField.setText(fileNameStr);
			}
		}
		}); 
		
		searchBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Vector matchVec = doSearch();
				showSearchResult(matchVec);
			}
		});
		
		
		inputPanel.setLayout(inputLayout);
   		inputPanel.add(fileLabel);
		inputPanel.add(fileTextField);
		inputPanel.add(fileBtn);
		inputPanel.add(inputLabel);
		inputPanel.add(inputTextField);
		inputPanel.add(searchBtn);
   		fileTextField.setPreferredSize(new Dimension(250, 25));
   		inputTextField.setPreferredSize(new Dimension(150, 25));
		
		inputPanel.setPreferredSize(new Dimension(470, 120));
		inputPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.green, 1), "輸入"));
		inputLayout.putConstraint(SpringLayout.WEST, fileLabel, 10, SpringLayout.WEST, inputPanel);
		inputLayout.putConstraint(SpringLayout.NORTH, fileLabel, 10, SpringLayout.NORTH, inputPanel);
		inputLayout.putConstraint(SpringLayout.WEST, fileTextField, 10, SpringLayout.EAST, fileLabel);
		inputLayout.putConstraint(SpringLayout.NORTH, fileTextField, 5, SpringLayout.NORTH, inputPanel);
		inputLayout.putConstraint(SpringLayout.WEST, fileBtn, 10, SpringLayout.EAST, fileTextField);
		inputLayout.putConstraint(SpringLayout.NORTH, fileBtn, 5, SpringLayout.NORTH, inputPanel);
		inputLayout.putConstraint(SpringLayout.WEST, inputLabel, 10, SpringLayout.WEST, inputPanel);
		inputLayout.putConstraint(SpringLayout.NORTH, inputLabel, 50, SpringLayout.NORTH, inputPanel);
		inputLayout.putConstraint(SpringLayout.WEST, inputTextField, 10, SpringLayout.EAST, inputLabel);
		inputLayout.putConstraint(SpringLayout.NORTH, inputTextField, 50, SpringLayout.NORTH, inputPanel);
		inputLayout.putConstraint(SpringLayout.WEST, searchBtn, 10, SpringLayout.EAST, inputTextField);
		inputLayout.putConstraint(SpringLayout.NORTH, searchBtn, 50, SpringLayout.NORTH, inputPanel);
	}	
		
	protected void resultLayout(){
		//dataTable = new JTable();
		tablePanel.setLayout(new BorderLayout());
		tablePanel.setPreferredSize(new Dimension(445, 170));
		tablePanel.add(txa, BorderLayout.CENTER);
		
		resultPanel.setLayout(resultLayout);
   		resultPanel.add(tablePanel);
		//resultPanel.add(drawBtn);

		resultPanel.setPreferredSize(new Dimension(470,260));
		resultPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.orange, 1), "搜尋結果"));
		resultLayout.putConstraint(SpringLayout.WEST, tablePanel, 10, SpringLayout.WEST, resultPanel);
		resultLayout.putConstraint(SpringLayout.NORTH, tablePanel, 10, SpringLayout.NORTH, resultPanel);
		//resultLayout.putConstraint(SpringLayout.WEST, drawBtn, 30, SpringLayout.WEST, resultPanel);
		//resultLayout.putConstraint(SpringLayout.NORTH, drawBtn, 15, SpringLayout.SOUTH, tablePanel);
	}
	
	protected Vector doSearch(){
		Vector matchVec = new Vector();
		
		try{
			File file = new File(fileNameStr);
			FileReader fr = new FileReader(file);	
			BufferedReader br = new BufferedReader(fr);
			String str;
			StringTokenizer st;
			int max = 0;
			
			rowNum = columnNum = 0;
						
			while(br.ready()){
				str = br.readLine();
				st = new StringTokenizer(str, ",");
				
				if(st.hasMoreTokens()){
					if(st.nextToken().contains( inputTextField.getText() )){
					//if(st.nextToken().equals( inputTextField.getText() )){
						matchVec.add(str);//System.out.println(str);
						rowNum++;
						if(max < st.countTokens()+1){
							max = st.countTokens() + 1;
						}						
					}
				}
			}	
			
			columnNum = max;
			
			br.close();
			fr.close();
			file = null;
		}
		catch(IOException e){
			e.getMessage();
			e.printStackTrace();
		}
		
		return matchVec;		
	}
	
	protected void showSearchResult(Vector matchVec){
		String[] title = new String[columnNum];
		String[][] data = new String[rowNum][columnNum];
		String str;
		StringTokenizer st;
		int columnCount;
		
		title[0] = "病歷號";
		for(int count=1 ; count<columnNum ; count++){
			title[count] = "第" + count + "次";
		}
		
		for(int count=0 ; count<rowNum ; count++){
			str = matchVec.remove(0).toString();
			
			columnCount = 0;
			st = new StringTokenizer(str, ",");
			while(st.hasMoreTokens()){
				data[count][columnCount++] = st.nextToken();
			}
		}		
		
		tablePanel.removeAll();
		jScrollPane.getViewport().removeAll();
		dataTable = null;
		txa = null;
		dataTable = new JTable(data, title);
		dataTable.setAutoResizeMode(0);
		jScrollPane.getViewport().add(dataTable);
		tablePanel.add(jScrollPane, BorderLayout.CENTER);
		tablePanel.updateUI();
	}
	
	protected void processWindowEvent(WindowEvent e) {		//關閉視窗
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
	}
}