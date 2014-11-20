package com.ali.notes;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.List;

public class MainActivity extends ListActivity {

    private static final int EDITOR_ACTIVITY_REQUEST = 1001;
    private static final int MENU_DELETE_ID = 1002;
    public static final int COLOR_PICKER = 1003;
    private static final int MENU_CHECK_ID = 1004;
    private static final int MENU_BACKUP_ID = 1005;

    private static String currentPackageName;

    // private GestureDetector gestureDetector;
    final SwipeDetector swipeDetector;

    private int currentNoteId;
    private NotesDataSource dataSource;
    private List<NoteItem> notesList;

    public MainActivity() {
        swipeDetector = new SwipeDetector();
    }

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
        this.currentPackageName = getApplicationContext().getPackageName();
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


        }
        return super.onContextItemSelected(item);
    }

    public void showStatusToast(String message) {
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }


}
