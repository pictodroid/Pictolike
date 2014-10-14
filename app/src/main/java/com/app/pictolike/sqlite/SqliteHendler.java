package com.app.pictolike.sqlite;

import java.util.ArrayList;

import com.app.pictolike.data.Constant;
import com.app.pictolike.data.PictoFile;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteHendler extends SQLiteOpenHelper{

	public static String DB_NAME="PictoLike.db"; 
	public static String TABLE_NAME="picto";
	public SqliteHendler(Context context) {
		super(context, DB_NAME, null, 1);
		System.out.println("database created");
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		 System.out.println("on create");
		 db.execSQL("create table " + TABLE_NAME + "(id integer primary key autoincrement, username text not null, filename text not null, datecreated text not null,noOfLikes integer not null,noOfViews integer not null,text_added text not null,firstpictolikepicture text not null)");
		 System.out.println("table craeted..");
		 
	}
	
	public void insertDataToPicto(ArrayList<PictoFile> pictoArray)
	{
		System.out.println("insert data....");
		
		SQLiteDatabase db=this.getWritableDatabase();
		for(int i=0;i<pictoArray.size();i++)
		{
			ContentValues contentValues=new ContentValues();
			contentValues.put("username", pictoArray.get(i).username);
			contentValues.put("filename", pictoArray.get(i).username);
			contentValues.put("datecreated", pictoArray.get(i).username);
			contentValues.put("noOfLikes", pictoArray.get(i).username);
			contentValues.put("noOfViews", pictoArray.get(i).username);
			contentValues.put("text_added", pictoArray.get(i).username);
			contentValues.put("firstpictolikepicture", pictoArray.get(i).username);
			db.insert(TABLE_NAME, null, contentValues);
			System.out.println("data insertewd//.//////");
		}
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
