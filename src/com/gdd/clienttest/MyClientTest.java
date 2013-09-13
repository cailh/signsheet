package com.gdd.clienttest;

import java.awt.BorderLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class MyClientTest extends JFrame implements ActionListener{
	
	private static TextArea showServerLog = null;
	private JButton sendMSG = null;
	
	public MyClientTest(){
		this.setLayout(new BorderLayout());
		this.setBounds(60, 60, 500, 500);
		this.setTitle("ǩ��΢�Ϳͻ���");
		
		showServerLog = new TextArea(10, 10);
		sendMSG = new JButton("������Ϣ");
		sendMSG.addActionListener(this);
		this.add(showServerLog, BorderLayout.CENTER);		
		this.add(sendMSG, BorderLayout.NORTH);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton sourceBtn = (JButton) e.getSource();
		if(sourceBtn.getText().equals("������Ϣ")){
			StringBuffer sb = new StringBuffer();
			sb.append(sourceBtn.getText());
		}
	}
	
	
	
	public static void main(String[] args){
		new MyClientTest();
	}
}
