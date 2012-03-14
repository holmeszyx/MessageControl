package z.hol.android.mc.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MessageControlerDatabaseHelper extends SQLiteOpenHelper{
	public static final int VERSION = 1;
	public static final String DATABASE_NAME = "mc.db";
	

	public MessageControlerDatabaseHelper(Context context){
		this(context, DATABASE_NAME, null, VERSION);
	}
	
	public MessageControlerDatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
