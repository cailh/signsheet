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

public class MyServerSocket extends JFrame{

	private static TextArea showServerLog = null;
	private ServerSocket mServer = null;

	
	public MyServerSocket(){	
		
		this.setLayout(new BorderLayout());
		this.setBounds(60, 60, 500, 500);
		this.setTitle("ǩ��ϵͳ����̨");
		
		showServerLog = new TextArea(10, 10);
		this.add(showServerLog, BorderLayout.CENTER);		
		
		this.setVisible(true);
		
		
		try {
			mServer = new ServerSocket(9000);
			
	        while (true) {  
	            Socket socket = mServer.accept();  
	            invoke(socket);  
	        }  
		} catch (IOException e) {
			showServerLog.setText("StrartServer Error!" + "\n");
			e.printStackTrace();
		}			
	}	
	
    private static void invoke(final Socket client) throws IOException {  
        new Thread(new Runnable() {   
        	
        	String[] userinfo = new String[5];   
        	String method = null;
        	String username = null;
        	String password = null;
        	String signUser = null;
        	
            Map<String, String> mParameter = null;
            ResultSet rs = null;
        	MyDatabaseConnection mydatabaseconnection = null;
        	SignLoginBusinese threadSignBusi = null;
            public void run() {              	
            	boolean flag = false;
                BufferedReader in = null;  
                PrintWriter out = null;  
                try { 
                	mydatabaseconnection = new MyDatabaseConnection();
                    in = new BufferedReader(new InputStreamReader(client.getInputStream()));  
                    out = new PrintWriter(client.getOutputStream());  
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
							mydatabaseconnection.close();
							
//							signloginbusinese = new SignLoginBusinese();
							try {
								threadSignBusi = new SignLoginBusinese(username);
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
//   ��  Ҫ  ��  ��  ��  ��  ��  ��  ��  ��  ��  ��  ������
						System.out.println("�����ˣ�");
						e.printStackTrace();
					}
                    while (flag) {  
                        String msg = in.readLine();
                        StringBuffer sb = new StringBuffer();
                        sb.append(showServerLog.getText());
                        sb.append('\n');
                        sb.append(msg);                        
                        showServerLog.setText(sb.toString());
                        out.println("SUCCESS");  
                        out.flush(); 
                        
                        readmessage(msg);
                        
                        if (msg.equals("bye")) {  
                            break;  
                        }  
                    }  
                } catch(IOException ex) {  
                    ex.printStackTrace();  
                } finally {  
                    try {  
                        in.close();  
                    } catch (Exception e) {}  
                    try {  
                        out.close();  
                    } catch (Exception e) {}  
                    try {  
                        client.close();  
                    } catch (Exception e) {}  
                    try {
                    	rs.close();
                    } catch(Exception e){}
                    try {
                    	mydatabaseconnection.close();
                    } catch(Exception e){}
                }  
                

            }
			//��ȡsocket�ļ����浽���ݿ���
			private void readmessage(String msg) {
				userinfo = new String[5];
				userinfo = msg.split(";");
				try {
					method = userinfo[0];
					signUser = userinfo[1];
					threadSignBusi.signCheck();
				} catch (Exception e) {
//   ��  Ҫ  ��  ��  ��  ��  ��  ��  ��  ��  ��  ��
					e.printStackTrace();
				}
				
			} 
 
        }).start();  
    } 
    

	public static void main(String args[]){
		new MyServerSocket();
	}
	
}
