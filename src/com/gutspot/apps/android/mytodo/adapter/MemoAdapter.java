package com.gutspot.apps.android.mytodo.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gutspot.apps.android.mytodo.R;
import com.gutspot.apps.android.mytodo.model.Memo;

public class MemoAdapter extends BaseAdapter {

    private Context context;
    private List<Memo> memos;

    public MemoAdapter(Context context, List<Memo> memos) {
        this.context = context;
        this.memos = memos;
    }

    @Override
    public int getCount() {
        return memos.size();
    }

    @Override
    public Object getItem(int position) {
        return memos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_row_memo, parent, false);
            holder = new ViewHolder();
            holder.memoTextView = (TextView) convertView.findViewById(R.id.text_memo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Memo memo = memos.get(position);
        TextView memoTextView = holder.memoTextView;
        memoTextView.setText(memo.getContent());
        memoTextView.setTextColor(memo.getTextColor());
        memoTextView.setBackgroundColor(memo.getBackgroundColor());

        return convertView;
    }

    static class ViewHolder {
        public TextView memoTextView;
    }
}
