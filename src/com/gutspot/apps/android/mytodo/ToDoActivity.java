package com.gutspot.apps.android.mytodo;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import com.gutspot.apps.android.mytodo.model.Memo;
import com.gutspot.apps.android.mytodo.utils.AlertUtil;

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

        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void updateMemoListView() {
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

    public static class MemoOptionsDialog extends DialogFragment implements OnClickListener {
        private static final String[] options = new String[] { "编辑", "删除" };

        private Context context;
        private long toDoId;
        private long memoId;

        public static MemoOptionsDialog newInstance(Context context, long toDoId, long memoId) {
            MemoOptionsDialog dialog = new MemoOptionsDialog();
            dialog.context = context;
            dialog.toDoId = toDoId;
            dialog.memoId = memoId;
            return dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("提示");
            builder.setItems(options, this);
            return builder.create();
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
            case 0:
                viewMemo();
                break;

            case 1:
                deleteMemo();
                break;

            default:

            }
        }

        private void viewMemo() {
            Intent intent = new Intent(context, MemoActivity.class);
            intent.putExtra("type", 3);
            intent.putExtra("todo_id", toDoId);
            intent.putExtra("memo_id", memoId);
            context.startActivity(intent);
        }

        private void deleteMemo() {
            String message = "是否删除Memo？";

            String yesLabel = "是";
            DialogInterface.OnClickListener yesListener = new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MemoDAO memoDAO = new MemoDAO(context);
                    memoDAO.remove(memoId);
                    ((ToDoActivity) context).updateMemoListView();
                }
            };

            String noLabel = "否";
            DialogInterface.OnClickListener noListener = new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            };

            AlertUtil.show(context, message, yesLabel, yesListener, noLabel, noListener);
        }
    }

}
