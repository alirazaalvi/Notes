package com.ali.notes;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;

import com.ali.notes.data.NoteItem;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
//import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View.OnClickListener;



@SuppressLint("NewApi")
public class NoteEditorActivity extends Activity implements OnClickListener{
	
	private NoteItem note;
	private ColorScheme colorScheme;
	//private String colorScheme;
	
	
	
	public NoteEditorActivity()
	{
		//settings the color codes Map
		this.colorScheme = new ColorScheme();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_editor);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		

		note = new NoteItem();
		Intent intent = getIntent();
		note.setOID(intent.getIntExtra("OID",0));
		note.setTitle(intent.getStringExtra("title"));
		note.setDescription(intent.getStringExtra("description"));
		note.setColorScheme(intent.getStringExtra("colorScheme"));
		
		
		
		EditText txtTitle = (EditText) findViewById(R.id.noteTitle);
		EditText txtDescription = (EditText) findViewById(R.id.noteDescription);
		
		txtTitle.setText(note.getTitle());
		txtDescription.setText(note.getDescription());
		
		txtTitle.setSelection(txtTitle.length());
		//txtDescription.setSelection(txtDescription.length());
		
		setEditorBackgroundColor(note.getColorScheme());
		
		ImageButton btnChangeColor = (ImageButton) findViewById(R.id.btnChangeColor);
		btnChangeColor.setOnClickListener(this);
	}
	
	public void saveAndFinish()
	{
		Intent intent = new Intent();
		TextView txtTitle = (TextView) findViewById(R.id.noteTitle);
		TextView txtDescription = (TextView) findViewById(R.id.noteDescription);

		intent.putExtra("OID", this.note.getOID());
		intent.putExtra("title", txtTitle.getText().toString());
		intent.putExtra("description", txtDescription.getText().toString());
		intent.putExtra("colorScheme", this.colorScheme.getColorScheme());
		
		setResult(RESULT_OK, intent);
		finish();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home)
		{
			saveAndFinish();
		}
		return false;
	}
	
	@Override
	public void onBackPressed() {
		saveAndFinish();
	}
	
	//onclick listeners
	public void onClick(View v)
	{
		if(v.getId() == R.id.btnChangeColor)
		{
			//Intent intent = new Intent(this, PickColorActivity.class);
			//startActivityForResult(intent, MainActivity.COLOR_PICKER);
			final Dialog dialog = new Dialog(this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.activity_pick_color);
			//dialog.setTitle("Set note color");
			dialog.show();
			
			Window window = dialog.getWindow();
			window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E8E8E8")));
			//window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			
			
			Button btnRed = (Button) dialog.findViewById(R.id.btnRed);
			Button btnOrange = (Button) dialog.findViewById(R.id.btnOrange);
			Button btnYellow = (Button) dialog.findViewById(R.id.btnYellow);
			Button btnPink = (Button) dialog.findViewById(R.id.btnPurple);
			
			Button btnGreen = (Button) dialog.findViewById(R.id.btnGreen);
			Button btnBlue = (Button) dialog.findViewById(R.id.btnBlue);
			Button btnGray = (Button) dialog.findViewById(R.id.btnGray);
			Button btnWhite = (Button) dialog.findViewById(R.id.btnWhite);
			
			
			btnRed.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setEditorBackgroundColor("red");
					dialog.dismiss();
				}
			});
			
			btnOrange.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setEditorBackgroundColor("orange");
					dialog.dismiss();
				}
			});
			
			btnYellow.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setEditorBackgroundColor("yellow");
					dialog.dismiss();
				}
			});
			
			btnPink.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setEditorBackgroundColor("purple");
					dialog.dismiss();
				}
			});
			
			btnGreen.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setEditorBackgroundColor("green");
					dialog.dismiss();
				}
			});
			
			btnBlue.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setEditorBackgroundColor("blue");
					dialog.dismiss();
				}
			});
			
			btnGray.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					setEditorBackgroundColor("gray");
					dialog.dismiss();
				}
			});
			
			btnWhite.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					setEditorBackgroundColor("white");
					dialog.dismiss();
				}
			});
			
			
		}
	}
	
	public void setEditorBackgroundColor(String scheme)
	{
		TextView noteTitle = (TextView) findViewById(R.id.noteTitle);
		TextView txtNoteTopBorder = (TextView) findViewById(R.id.txtNoteTopBorder);
		//TextView noteDescription = (TextView) findViewById(R.id.noteDescription);
		ImageButton btnChangeColor = (ImageButton) findViewById(R.id.btnChangeColor);
		
		if(scheme != null && !scheme.isEmpty())
		{
			noteTitle.setBackgroundColor(Color.parseColor(this.colorScheme.colorCodes.get(scheme).get("title")));
			//noteDescription.setBackgroundColor(Color.parseColor(this.colorScheme.colorCodes.get(scheme).get("description")));
			txtNoteTopBorder.setBackgroundColor(Color.parseColor(this.colorScheme.colorCodes.get(scheme).get("description")));
			
			btnChangeColor.setBackgroundColor(Color.parseColor(this.colorScheme.colorCodes.get(scheme).get("title")));
			this.colorScheme.setColorScheme(scheme);
		}
		else
		{
			noteTitle.setBackgroundColor(Color.parseColor(this.colorScheme.colorCodes.get(this.colorScheme.defaultColorScheme).get("title")));
			//noteDescription.setBackgroundColor(Color.parseColor(this.colorScheme.colorCodes.get(this.colorScheme.defaultColorScheme).get("description")));
			txtNoteTopBorder.setBackgroundColor(Color.parseColor(this.colorScheme.colorCodes.get(this.colorScheme.defaultColorScheme).get("description")));
			btnChangeColor.setBackgroundColor(Color.parseColor(this.colorScheme.colorCodes.get(this.colorScheme.defaultColorScheme).get("title")));
			this.colorScheme.setColorScheme(scheme);
		}
	}
}
