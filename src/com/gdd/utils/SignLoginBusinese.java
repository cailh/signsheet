package com.gdd.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.gdd.db.MyDatabaseConnection;

public class SignLoginBusinese {

	private static int signCount = 0;
	
	private String lastsign = null;
	private String currentsign = null;
	private String currenttime = null;
	private String username = null;
	
	//不必要去处理构造时已经处理
	private String currentDay = null;	
	private int globallasthour = 0;
	private int globallastminute = 0;
	
	//不用处理只要在构造时处理就可以
	private int myday = 0;
	
	//不知道的东西。
	private MyDatabaseConnection busiConnection = null;

	private Map<String, String> mParameter = null;

	//构造方法
	public SignLoginBusinese(String username){
		
		this.username = username;
		busiConnection = new MyDatabaseConnection();
		Date date=new Date();
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		
		int curDay = cal.get(Calendar.DAY_OF_MONTH);
		int curHour = cal.get(Calendar.HOUR_OF_DAY);
		int curMinute = cal.get(Calendar.MINUTE);
		
		myday = curDay;
		
		globallasthour = curHour;
		globallastminute = curMinute;
		
		this.lastsign = String.valueOf(curHour) + ":" + String.valueOf(curMinute);
		this.currentsign = lastsign;
		this.currenttime = lastsign;
		this.currentDay = String.valueOf(curDay);
		
		mParameter.put("username", getUsername());
		mParameter.put("day", String.valueOf(myday));
		mParameter.put("workstart", currentsign);
		mParameter.put("workfinish", currentsign);
		mParameter.put("workfinish", "0");
		mParameter.put("timesum", signtimesum());
		
//		databaseConnection.executesql(mParameter , 2);
		
	}
	


	public void signCheck(){
		
		Date date=new Date();
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		

		int curHour = cal.get(Calendar.HOUR_OF_DAY);
		int curMinute = cal.get(Calendar.MINUTE);
		
		if((curHour - globallasthour) > 1){
			
			//重新进入计时状态
			//保存当前的状态
			savecurrent(curHour , curMinute);			
			//设置上一次状态。
			setlast(curHour , curMinute);
			
		}
		else if((curHour - globallasthour) == 1){
			if(((60 - globallastminute) + curMinute)<8){
				//记录签到
				savesign(curHour , curMinute);
				setlast(curHour , curMinute);
			}
			else{
				//重新进入计时状态。
				//保存当前的状态
				savecurrent(curHour , curMinute);			
				//设置上一次状态。
				setlast(curHour , curMinute);
			}
		}
		else{
			if((curMinute - globallastminute)<8){
				//记录签到事件
				savesign(curHour , curMinute);
				setlast(curHour , curMinute);
			}
			else{
				//重新进入计时状态。
				savecurrent(curHour , curMinute);			
				//设置上一次状态。
				setlast(curHour , curMinute);
			}
		}
		
	}
	
	private void savesign(int curHour, int curMinute) {
		signCount++;
		currentsign = String.valueOf(curHour) + ":" + String.valueOf(curMinute);
		currenttime = currentsign;
		
		mParameter.put("username", getUsername());
		mParameter.put("day", String.valueOf(myday));
		mParameter.put("workstart", currentsign);
		mParameter.put("workfinish", lastsign);
		mParameter.put("workfinish", "5");
		mParameter.put("timesum", signtimesum());
		
		busiConnection.executesql(mParameter , 2);
	}

	private void savecurrent(int curHour, int curMinute) {
		
		
		currentsign = String.valueOf(curHour) + ":" + String.valueOf(curMinute);
		currenttime = currentsign;
		
		mParameter.put("username", getUsername());
		mParameter.put("day", String.valueOf(myday));
		mParameter.put("workstart", currentsign);
		mParameter.put("workfinish", currentsign);
		mParameter.put("workfinish", "0");
		mParameter.put("timesum", signtimesum());
		
		busiConnection.executesql(mParameter , 2);
		
	}
	
	private String signtimesum() {
		
		String timesum_str = null;
		int timesum = 0;
		int signhour = 0;
		int signminute = 0;
		timesum = signCount*5;
		signhour = timesum/60;
		signminute = timesum%60;
		timesum_str = String.valueOf(signhour) + ":"
		+ String.valueOf(signminute);		
		return timesum_str;
		
	}

	private void setlast(int curHour, int curMinute){
	
		lastsign = String.valueOf(curHour) + ":" + String.valueOf(curMinute);
		globallasthour = curHour;
		globallastminute = curMinute;
		
	}
		
	//set方法
	public void setLastsign(String lastsign){
		this.lastsign = lastsign;
	}
	public void setCurrentsign(String currentsign){
		this.currentsign = currentsign;
	}
	public void setCurrenttime(String currenttime){
		this.currenttime = currenttime;
	}
	public void setCurrentDay(String currentDay){
		this.currentDay = currentDay;
	}
	public void setUsername(String username){
		this.username = username;
	}
	
	//get方法
	public String getLastsign(){
		return this.currentsign;
	}
	public String getCurrentsign(){
		return this.currentsign;
	}
	public String getCurrenttime(){
		return this.currenttime;
	}
	public String getCurrentDay(){
		return this.currentDay;
	}
	public String getUsername(){
		return this.username;
	}
		
}
