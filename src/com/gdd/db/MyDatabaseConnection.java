package com.gdd.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MyDatabaseConnection {

	final static int CHECKUSER = 1;
	final static int INSERTSHEET = 2;
	
	public String sql = null;
	
	public String dbDriver = null;
	public String url = null;
	public String username = null;
	public String password = null;
	
	public String headName = null;
	public String headValue = null;
	public Map<String,String> mParameter = null;
	public Set<String> setHead;
	
	
	private Connection mConnection;
	private Statement mStatement;
	private ResultSet mResultSet;
	
	
	public MyDatabaseConnection(){

		dbDriver = "com.mysql.jdbc.Driver";
		url = "jdbc:mysql://localhost/signsheet";
		username = "root";
		password = "gdd759";
		
		mConnection = null;
		mStatement = null;
		mResultSet = null;
		
	}
	
	public Connection sqlcon(){		
		try {
			Class.forName(this.dbDriver).newInstance();
			this.mConnection = DriverManager.getConnection(url, username, password);
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return null;
		}	
		return mConnection;				
	}
	
	public Statement getstate(){		
		try {
			this.mStatement = this.sqlcon().createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}		
		return mStatement;			
	}
	
	public ResultSet executesql(Map<String,String> mParameter ,int flag){		
		this.mParameter = mParameter;
		switch(flag){
		case CHECKUSER:
			setHead = mParameter.keySet();
			Iterator<String> iteratorHeadCheckUser = setHead.iterator();
			StringBuffer sbCheckuser = new StringBuffer();
			sbCheckuser.append("select * from User where ");
			while(iteratorHeadCheckUser.hasNext()){
				headName = iteratorHeadCheckUser.next();
				headValue = (String)mParameter.get(headName);
				sbCheckuser.append(headName + "=" + "'" + headValue + "' " + "and ");
			}
			sql = sbCheckuser.toString();
			int i = sql.lastIndexOf("and");
			sql = sql.substring(0, i - 1);			
			break;
		case INSERTSHEET:
			StringBuffer sb = new StringBuffer();
			sb.append("insert into signsheet values('',");
			sb.append("'" + mParameter.get("username") + "',");
			sb.append("'" + mParameter.get("day") + "',");
			sb.append("'" + mParameter.get("workstart") + "',");
			sb.append("'" + mParameter.get("workfinish") + "',");
			sb.append("'" + mParameter.get("worklast") + "',");
			sb.append("'" + mParameter.get("timesum") + "')");			
			sql = sb.toString();			
			break;
		}
		
		try {
			
			switch(flag){
			case CHECKUSER:
				this.mResultSet = this.getstate().executeQuery(sql);
				break;
			case INSERTSHEET:
				this.getstate().executeUpdate(sql);
				this.mResultSet = null;
				break;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}		
		return this.mResultSet;		
	}

	public void close() {
		
		try {
			mResultSet.close();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		
		try {
			mStatement.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		try {
			mConnection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
