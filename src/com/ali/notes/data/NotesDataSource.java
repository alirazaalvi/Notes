package com.ali.notes.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import com.ali.notes.R.string;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NotesDataSource extends SQLiteOpenHelper {

	private static final String PREFKEY = "notes";
	private static final String DB_NAME = "notes_db";
	public NotesDataSource(Context context) {
		super(context, DB_NAME, null, 1);
	}

	public List<NoteItem> findAll() {
		String sql = "Select OID,title,description,note_date,color_scheme,is_checked from notes";
		List<NoteItem> notesList = new ArrayList<NoteItem>();
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.rawQuery(sql, null);

		if (cursor.moveToFirst()) {
			do {
				NoteItem noteItem = new NoteItem();
				noteItem.setOID(cursor.getInt(0));
				noteItem.setTitle(cursor.getString(1));
				noteItem.setDescription(cursor.getString(2));
				noteItem.setNoteDate(cursor.getString(3));
				noteItem.setColorScheme(cursor.getString(4));
				noteItem.setIsChecked(cursor.getInt(5));
				notesList.add(noteItem);
			} while (cursor.moveToNext());

		}
		db.close();

		return notesList;

	}

	@SuppressLint("NewApi")
	public boolean insert(NoteItem noteItem) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		if(noteItem.getTitle() == null || noteItem.getTitle().isEmpty())
		{
			values.put(noteItem.FIELD_TITLE, this.getCurrentDate("MMM d"));
		}
		else
		{
			values.put(noteItem.FIELD_TITLE, noteItem.getTitle());
		}
		values.put(noteItem.FIELD_DESCRIPTION, noteItem.getDescription());
		values.put(noteItem.FIELD_NOTE_DATE, this.getCurrentDate());
		values.put(noteItem.FIELD_COLOR_SCHEME, noteItem.getColorScheme());
		values.put(noteItem.FIELD_IS_CHECKED, 0);
		db.insert(noteItem.NOTES_TABLE, null, values);
		db.close();
		return true;
	}
	
	public boolean update(NoteItem noteItem) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(NoteItem.FIELD_TITLE, noteItem.getTitle());
		values.put(noteItem.FIELD_DESCRIPTION, noteItem.getDescription());
		values.put(noteItem.FIELD_COLOR_SCHEME, noteItem.getColorScheme());
		values.put(noteItem.FIELD_IS_CHECKED, noteItem.getIsChecked());
		db.update(noteItem.NOTES_TABLE, values, noteItem.FIELD_OID + "= ?",
				new String[] { String.valueOf(noteItem.getOID()) });
		db.close();

		return true;
	}

	public boolean remove(NoteItem noteItem) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(noteItem.NOTES_TABLE, " OID = ? ",
				new String[] { String.valueOf(noteItem.getOID()) });
		db.close();
		return true;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS " + NoteItem.NOTES_TABLE + "(title VARCHAR, description TEXT, color_scheme VARCHAR, note_date TEXT, is_checked INTEGER);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP " + NoteItem.NOTES_TABLE + " IF EXISTS notes");
        onCreate(db);

	}

	public String getCurrentDate() {
		String pattern = "yyyy-MM-dd HH:mm:ss Z";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(new Date());
	}
	
	public String getCurrentDate(String pattern) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(new Date());
	}
	
	/*public void initializeDb()
	{
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("CREATE TABLE IF NOT EXISTS "+ NoteItem.NOTES_TABLE + "(title VARCHAR,description text, note_date text);");
		db.close();
	}*/
}
