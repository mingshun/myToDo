package com.gutspot.apps.android.mytodo.adapter;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.gutspot.apps.android.mytodo.R;
import com.gutspot.apps.android.mytodo.dao.ToDoDAO;
import com.gutspot.apps.android.mytodo.model.ToDo;

public class ToDoAdapter extends BaseAdapter {

    private Context context;
    private List<ToDoItem> toDoItems;

    public ToDoAdapter(Context context, List<ToDoItem> toDoItems) {
        this.context = context;
        this.toDoItems = toDoItems;
    }

    @Override
    public int getCount() {
        return toDoItems.size();
    }

    @Override
    public Object getItem(int position) {
        return toDoItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_row_to_do, parent, false);
            holder = new ViewHolder();
            holder.finishCheckBox = (CheckBox) convertView.findViewById(R.id.checkbox_finish);
            holder.digestTextView = (TextView) convertView.findViewById(R.id.text_todo_digest);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ToDoItem item = toDoItems.get(position);
        final CheckBox finishCheckBox = holder.finishCheckBox;
        if (item.finished == null) {
            finishCheckBox.setChecked(false);
        } else {
            finishCheckBox.setChecked(true);
        }

        finishCheckBox.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                finishCheckBox.setEnabled(false);
                ToDoDAO toDoDAO = new ToDoDAO(context);
                ToDo toDo = toDoDAO.findById(item.id);
                if (toDo.getFinished() == null) {
                    toDo.setFinished(new Date());
                    finishCheckBox.setChecked(true);
                } else {
                    toDo.setFinished(null);
                    finishCheckBox.setChecked(false);
                }
                toDoDAO.update(toDo);
                finishCheckBox.setEnabled(true);
            }
        });

        TextView digestTextView = holder.digestTextView;
        digestTextView.setText(item.digest);

        return convertView;
    }

    static class ViewHolder {
        public CheckBox finishCheckBox;
        public TextView digestTextView;
    }

    public static class ToDoItem {
        public long id;
        public Date created;
        public Date finished;
        public String digest;
    }
}
