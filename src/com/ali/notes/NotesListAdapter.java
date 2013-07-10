package com.ali.notes;

import java.util.List;

import com.ali.notes.data.NoteItem;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NotesListAdapter extends ArrayAdapter<NoteItem> {

	Context context;
	int layoutResourceId;
	List<NoteItem> data = null;
	ColorScheme colorScheme = new ColorScheme();
	
	public NotesListAdapter(Context context, int layoutResourceId, List<NoteItem> data)
	{
		super(context, layoutResourceId, data);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.data = data;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		noteHolder holder = null;
		if(row == null)
		{
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
	           
            holder = new noteHolder();
            holder.txtTitle = (TextView)row.findViewById(R.id.txtNoteListTitle);
            holder.txtColorBlock = (TextView)row.findViewById(R.id.txtColorBlock);
           
            row.setTag(holder);
		}
		else
		{
			holder = (noteHolder) row.getTag();
		}
		
		NoteItem noteRow = data.get(position);
		holder.txtTitle.setText(noteRow.getTitle());
		holder.txtTitle.setBackgroundColor(Color.parseColor(colorScheme.colorCodes.get(noteRow.getColorScheme()).get("title")));
		holder.txtColorBlock.setBackgroundColor(Color.parseColor(colorScheme.colorCodes.get(noteRow.getColorScheme()).get("description")));
		
		return row;
	}
	
	
	static class noteHolder
    {
        TextView txtColorBlock;
		TextView txtTitle;
    }
}
