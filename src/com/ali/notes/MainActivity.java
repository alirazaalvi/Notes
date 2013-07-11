package com.ali.notes;

import java.util.List;


import com.ali.notes.data.NoteItem;
import com.ali.notes.data.NotesDataSource;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;

public class MainActivity extends ListActivity{

	private static final int EDITOR_ACTIVITY_REQUEST = 1001;
	private static final int MENU_DELETE_ID = 1002;
	public static final int COLOR_PICKER = 1003;
	
	private int currentNoteId;
	private NotesDataSource dataSource;
	private List<NoteItem> notesList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		registerForContextMenu(getListView());
		dataSource = new NotesDataSource(this);
		refreshDisplay();
	}

	private void refreshDisplay() {
		
		notesList = dataSource.findAll();
		NotesListAdapter adapter = new NotesListAdapter(this, R.layout.list_item_layout, notesList);
		//ArrayAdapter<NoteItem> adapter = new ArrayAdapter<NoteItem>(this, R.layout.list_item_layout, notesList);
		setListAdapter(adapter);
		
		
		
	}
	
		@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.action_create)
		{
			createNote();
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void createNote()
	{
		NoteItem note = NoteItem.getNew();
		Intent intent = new Intent(this, NoteEditorActivity.class);
		intent.putExtra("title", note.getTitle());
		intent.putExtra("description", note.getDescription());
		intent.putExtra("colorScheme", note.getColorScheme());
		startActivityForResult(intent, EDITOR_ACTIVITY_REQUEST);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		NoteItem note = notesList.get(position);
		Intent intent = new Intent(this, NoteEditorActivity.class);
		intent.putExtra("OID", note.getOID());
		intent.putExtra("title", note.getTitle());
		intent.putExtra("description", note.getDescription());
		intent.putExtra("colorScheme", note.getColorScheme());
		startActivityForResult(intent, EDITOR_ACTIVITY_REQUEST);
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == EDITOR_ACTIVITY_REQUEST && resultCode == RESULT_OK)
		{
			NoteItem note = new NoteItem();
			Integer OID = data.getIntExtra("OID", 0);
			note.setOID(OID);
			
			note.setTitle(data.getStringExtra("title"));
			note.setDescription(data.getStringExtra("description"));
			note.setColorScheme(data.getStringExtra("colorScheme"));
			if(OID == 0)
			{
				dataSource.insert(note);
				showStatusToast("Note inserted successfully");
			}
			else
			{
				dataSource.update(note);
				showStatusToast("Note updated successfully");
			}
			refreshDisplay();
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		currentNoteId = (int)info.id;
		menu.setHeaderTitle(R.string.note_list_operations);
		menu.add(0, MENU_DELETE_ID, 0, "Delete");
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if(item.getItemId() == MENU_DELETE_ID)
		{
			NoteItem note = notesList.get(currentNoteId);
			dataSource.remove(note);
			showStatusToast("Note removed successfully");
			refreshDisplay();
		}
		return super.onContextItemSelected(item);
	}
	
	
	
	public void showStatusToast(String message)
	{
		Context context = getApplicationContext();
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		toast.show();
	}
	
	
	public void initializeDb()
	{
		//SQLiteDatabase db = openOrCreateDatabase("notes_db", MODE_PRIVATE, null);
		//db.execSQL("ALTER TABLE notes ADD COLUMN color_scheme VARCHAR;");
		//db.execSQL("CREATE TABLE IF NOT EXISTS notes(title VARCHAR,description text, note_date text);");
		//db.execSQL("INSERT INTO notes(title,description,note_date) values('note3','this is my third note','7-6-2013');");
		//db.execSQL("INSERT INTO notes(title,description,note_date) values('note4','this is my fourth note','7-6-2013');");
		//db.close();
	}
	
	


}
