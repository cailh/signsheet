package com.gdd.net;

import java.awt.BorderLayout;
import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import com.gdd.db.MyDatabaseConnection;
import com.gdd.utils.SignLoginBusinese;

public class MyServerSingle extends JFrame {
	private static TextArea showServerLog = null;
	private ServerSocket mServer = null;
	String[] userinfo = new String[5];   
	String method = null;
	String username = null;
	String password = null;
	String signUser = null;
	
    Map<String, String> mParameter = null;
    ResultSet rs = null;
	MyDatabaseConnection mydatabaseconnection = null;
	SignLoginBusinese threadSignBusi = null;
	
	boolean flag = false;
    BufferedReader in = null;  
    PrintWriter out = null;  
	
	public MyServerSingle(){	
		
		this.setLayout(new BorderLayout());
		this.setBounds(60, 60, 500, 500);
		this.setTitle("签到系统控制台");
		
		showServerLog = new TextArea(10, 10);
		this.add(showServerLog, BorderLayout.CENTER);		
		
		this.setVisible(true);
		
		
		try {
			mServer = new ServerSocket(9000);		

			while (true) { 
		        Socket socket = mServer.accept(); 
		        mydatabaseconnection = new MyDatabaseConnection();
	            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
	            out = new PrintWriter(socket.getOutputStream());  
	            mParameter = new HashMap<String, String>();
	            String msg0 = in.readLine();
	            StringBuffer sb0 = new StringBuffer();
	            sb0.append(showServerLog.getText());
	            if(showServerLog.getText() != null && !showServerLog.getText().equals(""))
	            	sb0.append('\n');
	            sb0.append(msg0);     
	            
	            showServerLog.setText(sb0.toString());
	            
	            userinfo = msg0.split(";");
	            method = userinfo[0];
	            username = userinfo[1];
	            password = userinfo[2];
	            
	            mParameter.put("username", username);
	            mParameter.put("password", password);
	            
				rs = mydatabaseconnection.executesql(mParameter, 1);
				try {
					if(rs.next()){
						flag = true;
						try {
							threadSignBusi = new SignLoginBusinese(username);
							threadSignBusi.signCheck();
						} catch (Exception e) {
							e.printStackTrace();
						}
	                    out.println("LOGINSUCCESS");  
	                    out.flush(); 
					} else {
						out.println("LOGINFAIL");  
						out.flush(); 
				}
				
			} catch (SQLException e) {
//需  要  输  出  数  据  库  出  错  的  问  题  ！！！
				System.out.println("出错了！");
				e.printStackTrace();
			}
			
			 }
		} catch (IOException e) {
			showServerLog.setText("StrartServer Error!" + "\n");
			e.printStackTrace();
		}		
		finally {  
            try {  
                in.close();  
            } catch (Exception e) {}  
            try {  
                out.close();  
            } catch (Exception e) {}  
            try {
            	rs.close();
            } catch(Exception e){}
            try {
            	mydatabaseconnection.close();
            } catch(Exception e){}
        }  
	}	
	
	
	public static void main(String args[]){
		new MyServerSingle();
	}
}
