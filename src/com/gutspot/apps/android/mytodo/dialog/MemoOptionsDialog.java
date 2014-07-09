package com.gutspot.apps.android.mytodo.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;

import com.gutspot.apps.android.mytodo.MemoActivity;
import com.gutspot.apps.android.mytodo.ToDoActivity;
import com.gutspot.apps.android.mytodo.dao.MemoDAO;
import com.gutspot.apps.android.mytodo.utils.AlertUtil;

public class MemoOptionsDialog extends DialogFragment implements OnClickListener {
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