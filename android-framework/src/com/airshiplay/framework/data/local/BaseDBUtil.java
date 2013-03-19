/**
 * 
 */
package com.airshiplay.framework.data.local;

import com.airshiplay.framework.application.FWApplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author airshiplay
 * @Create Date 2013-3-9
 * @version 1.0
 * @since 1.0
 */
public abstract class BaseDBUtil {
	public static final String DATABASE_NAME = "framework.db";
	public static final int DEFAULT_VERSION = 1;
	protected Context mContext;
	protected DBOpenHelper myDBOpenHelper;
	protected SQLiteDatabase readableDatabase;
	protected SQLiteDatabase writableDatabase;

	public BaseDBUtil(Context context) {
		FWApplication application = FWApplication.getInstance();
		if (application == null)
			this.mContext = context.getApplicationContext();
		else {
			this.mContext = application.getApplicationContext();
		}
		this.myDBOpenHelper = new DBOpenHelper(mContext, DATABASE_NAME, DEFAULT_VERSION);
	}

	public void closeReadableDB() {
		if (this.readableDatabase != null)
			this.readableDatabase.close();
	}

	public void closeWriteableDB() {
		if (this.writableDatabase != null)
			this.writableDatabase.close();
	}

	public void openReadableDB() {
		try {
			this.readableDatabase = this.myDBOpenHelper.getReadableDatabase();
		} finally {
		}
	}

	public void openWriteableDB() {
		try {
			this.writableDatabase = this.myDBOpenHelper.getWritableDatabase();
		} finally {
		}
	}

	public boolean tabbleIsExist(SQLiteDatabase sQLiteDatabase, String tableName) {
		if (tableName == null)
			return false;
		Cursor cur = null;
		try {
			cur = sQLiteDatabase.rawQuery(
					"select count(*) as c from Sqlite_master  where type ='table' and name ='"
							+ tableName.trim() + "' ", null);
			boolean exist = cur.moveToNext();
			if (exist) {
				if (cur.getInt(0) > 0)
					return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		} finally {
			if (cur != null)
				cur.close();
		}
	}
}
