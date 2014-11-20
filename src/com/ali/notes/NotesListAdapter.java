package com.ali.notes;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.ali.notes.data.NoteItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class NotesListAdapter extends ArrayAdapter<NoteItem> {

    Context context;
    int layoutResourceId;
    List<NoteItem> data = null;
    ColorScheme colorScheme = new ColorScheme();

    public NotesListAdapter(Context context, int layoutResourceId, List<NoteItem> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        noteHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new noteHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.txtNoteListTitle);
            holder.txtDate = (TextView) row.findViewById(R.id.txtNoteListDate);
            holder.txtColorBlock = (TextView) row.findViewById(R.id.txtColorBlock);


            row.setTag(holder);
        } else {
            holder = (noteHolder) row.getTag();
        }

        NoteItem noteItem = data.get(position);
        holder.txtTitle.setText(noteItem.getTitle());
        holder.txtTitle.setBackgroundColor(Color.parseColor(colorScheme.colorCodes.get(noteItem.getColorScheme()).get("title")));

        if (noteItem.getIsChecked() == 1) {
            holder.txtTitle.setPaintFlags(holder.txtTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }


        holder.txtDate.setBackgroundColor(Color.parseColor(colorScheme.colorCodes.get(noteItem.getColorScheme()).get("title")));

        String noteDate = "";

        SimpleDateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //please notice the    capital M
        SimpleDateFormat outputFormatter = new SimpleDateFormat("MMM d");
        try {
            java.util.Date date = (java.util.Date) inputFormatter.parse(noteItem.getNoteDate());
            noteDate = outputFormatter.format(date).toString() + "  ";
        } catch (ParseException e) {
            //e.printStackTrace();
        }

        holder.txtDate.setText(noteDate);
        holder.txtColorBlock.setBackgroundColor(Color.parseColor(colorScheme.colorCodes.get(noteItem.getColorScheme()).get("description")));


        return row;
    }


    static class noteHolder {
        TextView txtColorBlock;
        TextView txtTitle;
        TextView txtDate;
    }

}
