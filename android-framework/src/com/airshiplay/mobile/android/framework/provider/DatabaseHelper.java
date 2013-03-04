/**
 * 
 */
package com.airshiplay.mobile.android.framework.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author airshiplay
 * @Create Date 2013-2-6
 * @version 1.0
 * @since 1.0
 */
public class DatabaseHelper extends SQLiteOpenHelper{

	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}


}
