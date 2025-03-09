package vn.iotstar.sqlite.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import vn.iotstar.sqlite.Activity.MainActivity;
import vn.iotstar.sqlite.Model.NotesModel;
import vn.iotstar.sqlite.R;

public class NotesAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<NotesModel> notesList;

    public NotesAdapter(Context context, int layout, List<NotesModel> notesList) {
        this.context = context;
        this.layout = layout;
        this.notesList = notesList;
    }

    @Override
    public int getCount() {
        return notesList.size();
    }

    @Override
    public Object getItem(int position) {
        return notesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            viewHolder.textViewNote = (TextView) convertView.findViewById(R.id.textViewNameNote);
            viewHolder.imageViewDelete=(ImageView) convertView.findViewById(R.id.imageViewDelete);
            viewHolder.imageViewEdit=(ImageView) convertView.findViewById(R.id.imageViewEdit);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        NotesModel notes = notesList.get(position);
        viewHolder.textViewNote.setText(notes.getNameNote());
        viewHolder.imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Cập nhật " + notes.getNameNote(), Toast.LENGTH_SHORT).show();
                if (context instanceof MainActivity) {
                    ((MainActivity) context).DialogCapNhatNotes(notes.getNameNote(), notes.getIdNote());
                }
            }
        });

        viewHolder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) context).DialogDelete(notes.getNameNote(), notes.getIdNote());
            }
        });
        return convertView;
    }
    private class ViewHolder{
        TextView textViewNote;
        ImageView imageViewEdit;
        ImageView imageViewDelete;
    }
}
