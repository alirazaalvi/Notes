package com.ali.notes.data;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.ali.notes.ColorScheme;

public class NoteItem {

	private Integer OID;
	private String title;
	private String description;
	private String noteDate;
	private String colorScheme;
	
	public static final String NOTES_TABLE = "notes";
	public static final String FIELD_OID = "OID";
	public static final String FIELD_TITLE = "title";
	public static final String FIELD_DESCRIPTION = "description";
	public static final String FIELD_NOTE_DATE = "note_date";
	public static final String FIELD_COLOR_SCHEME = "color_scheme";
	
	
	public Integer getOID()
	{
		return this.OID;
	}
	
	public void setOID(Integer OID)
	{
		this.OID = OID; 
	}
	
	
	public String getTitle()
	{
		return this.title;
	}
	
	public void setTitle(String key)
	{
		this.title =key;
	}
	
	
	public String getDescription()
	{
		return this.description;
	}
	
	public void setDescription(String text)
	{
		this.description = text;
	}
	
	
	public String getNoteDate()
	{
		return this.noteDate;
	}
	
	public void setNoteDate(String noteDate)
	{
		this.noteDate = noteDate; 
	}
	
	public String getColorScheme()
	{
		return this.colorScheme;
	}
	
	public void setColorScheme(String colorScheme)
	{
		this.colorScheme = colorScheme; 
	}
	
	
		
	
	
	@SuppressLint("SimpleDateFormat")
	public static NoteItem getNew()
	{
		Locale locale = new Locale("en-US");
		Locale.setDefault(locale);
		
		/*String pattern = "yyyy-MM-dd HH:mm:ss Z";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		String noteDate = formatter.format(new Date());*/
		
		NoteItem noteItem = new NoteItem();
		noteItem.setOID(0);
		noteItem.setTitle("");
		noteItem.setDescription("");
		noteItem.setNoteDate("");
		noteItem.setColorScheme(ColorScheme.defaultColorScheme);
		
		return noteItem;
	}
	
	@Override 
	public String toString()
	{
		return this.getTitle();
	}

}
