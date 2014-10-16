package com.app.pictolike.sqlite;

import java.util.ArrayList;
import java.util.List;

import com.app.pictolike.data.Constant;
import com.app.pictolike.data.PictoFile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteHandler extends SQLiteOpenHelper{

    public static final int VERSION = 1;
    public static String DB_NAME="PictoLike.db";
	public static String TABLE_NAME="picto";
	public static String SAVED_PICTOS="saved_pictos";
	public SqliteHandler(Context context) {
		super(context, DB_NAME, null, VERSION);
		System.out.println("database created");
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		 System.out.println("on create");
		 db.execSQL("create table " + TABLE_NAME + "(id integer primary key autoincrement, username text not null, filename text not null, datecreated text not null,noOfLikes integer not null,noOfViews integer not null,text_added text not null,firstpictolikepicture text not null)");
		 db.execSQL("create table " + SAVED_PICTOS + "(id integer primary key autoincrement," +
                 "filename text not null," +
                 "datecreated text not null," +
                 "noOfLikes integer not null," +
                 "noOfViews integer not null" +
                 ")");
		 System.out.println("table craeted..");
		 
	}
	public void savePicto(PictoFile pPictoFile){
        SQLiteDatabase lDatabase = getWritableDatabase();
        ContentValues lValues = new ContentValues();
        bind(lValues,pPictoFile);
        lDatabase.insert(SAVED_PICTOS,null,lValues);
    }

    private void bind(final ContentValues pValues, final PictoFile pPictoFile) {
        pValues.put("filename",pPictoFile.filename);
        pValues.put("datecreated",pPictoFile.dateCreated);
        pValues.put("noOfLikes",pPictoFile.noOfLikes);
        pValues.put("noOfViews",pPictoFile.noOfViews);
    }

    public void insertDataToPicto(ArrayList<PictoFile> pictoArray)
	{
		System.out.println("insert data....");
		
		SQLiteDatabase db=this.getWritableDatabase();
		for(int i=0;i<pictoArray.size();i++)
		{
			ContentValues contentValues=new ContentValues();
			contentValues.put("username", pictoArray.get(i).username);
			contentValues.put("filename", pictoArray.get(i).filename);
			contentValues.put("datecreated", pictoArray.get(i).dateCreated);
//			contentValues.put("noOfLikes", pictoArray.get(i).n);
//			contentValues.put("noOfViews", pictoArray.get(i).username);
//			contentValues.put("text_added", pictoArray.get(i).te);
//			contentValues.put("firstpictolikepicture", pictoArray.get(i).username);
			db.insert(TABLE_NAME, null, contentValues);
			System.out.println("data insertewd//.//////");
		}
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

    public List<PictoFile> loadSavedPictos() {
        List<PictoFile> lPictoFiles = new ArrayList<PictoFile>();
        Cursor c = null;
        SQLiteDatabase lDatabase = getReadableDatabase();
        try{
            c = lDatabase.query(SAVED_PICTOS,null,null,null,null,null,null);
            while (c.moveToNext()){
                PictoFile lPictoFile = createPicto(c);
                if (lPictoFile!=null){
                    lPictoFiles.add(lPictoFile);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (c!=null){
                c.close();
            }
        }
        return lPictoFiles;
    }

    private PictoFile createPicto(final Cursor pC) {
        PictoFile lPictoFile = new PictoFile();
        lPictoFile.noOfLikes = pC.getLong(pC.getColumnIndex("noOfLikes"));
        lPictoFile.noOfViews = pC.getLong(pC.getColumnIndex("noOfViews"));
        lPictoFile.filename = pC.getString(pC.getColumnIndex("filename"));
        return lPictoFile;
    }
}
