package com.gutspot.apps.android.mytodo;

import java.util.List;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.gutspot.apps.android.mytodo.adapter.MemoAdapter;
import com.gutspot.apps.android.mytodo.dao.MemoDAO;
import com.gutspot.apps.android.mytodo.dialog.DateTimeDialog;
import com.gutspot.apps.android.mytodo.dialog.MemoOptionsDialog;
import com.gutspot.apps.android.mytodo.model.Memo;

public class ToDoActivity extends Activity {

    private long toDoId;

    private ListView memoListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        Intent intent = this.getIntent();
        toDoId = intent.getLongExtra("todo_id", -1);
        if (toDoId == -1) {
            Toast.makeText(this, "无效参数todo_id", Toast.LENGTH_SHORT).show();
            this.finish();
            return;
        }

        memoListView = (ListView) this.findViewById(R.id.list_memo);
        updateMemoListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.to_do, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
        case android.R.id.home:
            onBackPressed();
            return true;

        case R.id.action_add_memo:
            Intent addMemoIntent = new Intent(this, MemoActivity.class);
            addMemoIntent.putExtra("type", 2);
            addMemoIntent.putExtra("todo_id", toDoId);
            startActivity(addMemoIntent);
            return true;

        case R.id.action_alert:
            DialogFragment dateTimeDialog = DateTimeDialog.newInstance(this);
            dateTimeDialog.show(this.getFragmentManager(), "dialog");
            return true;

        default:
            return super.onOptionsItemSelected(item);
        }
    }

    public void updateMemoListView() {
        MemoDAO memoDAO = new MemoDAO(this);
        List<Memo> memos = memoDAO.findByToDoId(toDoId);
        final MemoAdapter adapter = new MemoAdapter(this, memos);

        memoListView.setAdapter(adapter);
        memoListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Activity activity = ToDoActivity.this;
                Memo memo = (Memo) adapter.getItem(position);
                Intent intent = new Intent(activity, MemoActivity.class);
                intent.putExtra("type", 3);
                intent.putExtra("todo_id", toDoId);
                intent.putExtra("memo_id", memo.getId());
                activity.startActivity(intent);
            }
        });
        memoListView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Memo memo = (Memo) adapter.getItem(position);
                DialogFragment optionsDialog = MemoOptionsDialog.newInstance(ToDoActivity.this, toDoId, memo.getId());
                optionsDialog.show(ToDoActivity.this.getFragmentManager(), "dialog");
                return false;
            }
        });
    }

}
