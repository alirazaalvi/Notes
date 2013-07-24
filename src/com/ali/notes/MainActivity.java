package com.ali.notes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

import com.ali.notes.data.NoteItem;
import com.ali.notes.data.NotesDataSource;
import com.ali.utility.Utility;

public class MainActivity extends ListActivity {

	private static final int EDITOR_ACTIVITY_REQUEST = 1001;
	private static final int MENU_DELETE_ID = 1002;
	public static final int COLOR_PICKER = 1003;
	private static final int MENU_CHECK_ID = 1004;
	private static final int MENU_BACKUP_ID = 1005;

	public static String currentPackageName;

	// private GestureDetector gestureDetector;
	final SwipeDetector swipeDetector = new SwipeDetector();

	private int currentNoteId;
	private NotesDataSource dataSource;
	private List<NoteItem> notesList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// initializeDb();
		registerForContextMenu(getListView());
		dataSource = new NotesDataSource(this);
		refreshDisplay();

		final ListView lv = getListView();
		lv.setOnTouchListener(this.swipeDetector);
		// this.gestureDetector = new GestureDetector(this, new
		// customGestureListener());
		this.currentPackageName = getApplicationContext().getPackageName();

		/*try {
			InputStream instream = new FileInputStream(Environment
					.getExternalStorageDirectory().toString()
					+ "/"
					+ this.currentPackageName + "/" + "Jul-18-13-05-43-01.db");
			InputStreamReader inputreader = new InputStreamReader(instream);
			BufferedReader buffreader = new BufferedReader(inputreader);
			StringBuilder sb = new StringBuilder();
			String line;
			try {
				while ((line = buffreader.readLine()) != null) {
					sb.append(line);
				}
			} catch (IOException e) {

			}
			Log.i("ali", sb.toString());
		} catch (FileNotFoundException e) {
			Log.i("ali", e.toString());
		}*/

	}

	private void refreshDisplay() {

		notesList = dataSource.findAll();
		NotesListAdapter adapter = new NotesListAdapter(this,
				R.layout.list_item_layout, notesList);
		// ArrayAdapter<NoteItem> adapter = new ArrayAdapter<NoteItem>(this,
		// R.layout.list_item_layout, notesList);
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
		if (item.getItemId() == R.id.action_create) {
			createNote();
		}
		return super.onOptionsItemSelected(item);
	}

	public void createNote() {
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

		if (swipeDetector.swipeDetected()) {
			if (note.getIsChecked() == 1) {
				note.setIsChecked(0);
			} else {
				note.setIsChecked(1);
			}
			dataSource.update(note);
			refreshDisplay();
		} else {
			Intent intent = new Intent(this, NoteEditorActivity.class);
			intent.putExtra("OID", note.getOID());
			intent.putExtra("title", note.getTitle());
			intent.putExtra("description", note.getDescription());
			intent.putExtra("colorScheme", note.getColorScheme());
			startActivityForResult(intent, EDITOR_ACTIVITY_REQUEST);
		}

	}

	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == EDITOR_ACTIVITY_REQUEST && resultCode == RESULT_OK) {
			NoteItem note = new NoteItem();
			Integer OID = data.getIntExtra("OID", 0);
			note.setOID(OID);

			note.setTitle(data.getStringExtra("title"));
			note.setDescription(data.getStringExtra("description"));
			note.setColorScheme(data.getStringExtra("colorScheme"));
			if (OID == 0) {
				if (note.getDescription() != null
						&& !note.getDescription().isEmpty()) {
					dataSource.insert(note);
					showStatusToast("Note inserted successfully");
				}
			} else {
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
		currentNoteId = (int) info.id;
		menu.setHeaderTitle(R.string.note_list_operations);
		menu.add(0, MENU_DELETE_ID, 0, "Delete");
		menu.add(0, MENU_CHECK_ID, 0, "Check");
		menu.add(0, MENU_BACKUP_ID, 0, "Backup");
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == MENU_DELETE_ID) {
			NoteItem note = notesList.get(currentNoteId);
			dataSource.remove(note);
			showStatusToast("Note removed successfully");
			refreshDisplay();
		} else if (item.getItemId() == MENU_CHECK_ID) {
			NoteItem note = notesList.get(currentNoteId);
			if (note.getIsChecked() == 1) {
				note.setIsChecked(0);
			} else {
				note.setIsChecked(1);
			}
			dataSource.update(note);
			refreshDisplay();
		} else if (item.getItemId() == MENU_BACKUP_ID) {

			/*
			//sd to system
			Utility.copyFile(
					"Jul-18-13-09-24-35",
					NotesDataSource.DB_NAME,
					
					this.currentPackageName
					+ "/"
					+ "Jul-18-13-09-24-35",
					"//data//" + this.currentPackageName
							+ "//databases//notes_db",
					
									this.currentPackageName,
									this.currentPackageName,
									false
									);
			
			//system to sd
			Utility.copyFileSysToSd(
					NotesDataSource.DB_NAME,
					NotesDataSource.getCurrentDate("MMM-d-yy-HH-mm-ss"),
					"//data//" + this.currentPackageName
							+ "//databases//notes_db",
					this.currentPackageName
							+ "/"
							+ NotesDataSource
									.getCurrentDate("MMM-d-yy-HH-mm-ss"),
									this.currentPackageName,
									this.currentPackageName,
									true
									);
			refreshDisplay();
			*/
			
		}
		return super.onContextItemSelected(item);
	}

	public void showStatusToast(String message) {
		Context context = getApplicationContext();
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		toast.show();
	}

	public void initializeDb() {
		// SQLiteDatabase db = openOrCreateDatabase("notes_db", MODE_PRIVATE,
		// null);
		// db.execSQL("ALTER TABLE notes ADD COLUMN color_scheme VARCHAR;");
		// db.execSQL("ALTER TABLE notes ADD COLUMN is_checked INTEGER;");
		// db.execSQL("CREATE TABLE IF NOT EXISTS notes(title VARCHAR,description text, note_date text);");
		// db.execSQL("INSERT INTO notes(title,description,note_date) values('note3','this is my third note','7-6-2013');");
		// db.execSQL("INSERT INTO notes(title,description,note_date) values('note4','this is my fourth note','7-6-2013');");
		// db.close();
	}

}
