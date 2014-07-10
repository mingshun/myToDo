package com.gutspot.apps.android.mytodo.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;

import com.gutspot.apps.android.mytodo.MainActivity;
import com.gutspot.apps.android.mytodo.ToDoActivity;
import com.gutspot.apps.android.mytodo.dao.MemoDAO;
import com.gutspot.apps.android.mytodo.dao.ToDoDAO;
import com.gutspot.apps.android.mytodo.utils.AlertUtil;

public class ToDoOptionsDialog extends DialogFragment implements OnClickListener {
    private static final String[] options = new String[] { "查看", "删除" };

    private Context context;
    private long toDoId;

    public static ToDoOptionsDialog newInstance(Context context, long toDoId) {
        ToDoOptionsDialog dialog = new ToDoOptionsDialog();
        dialog.context = context;
        dialog.toDoId = toDoId;
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
            viewToDo();
            break;

        case 1:
            deleteToDo();
            break;

        default:

        }
    }

    private void viewToDo() {
        Intent intent = new Intent(context, ToDoActivity.class);
        intent.putExtra("todo_id", toDoId);
        context.startActivity(intent);
    }

    private void deleteToDo() {
        String message = "是否删除ToDo？";

        String yesLabel = "是";
        DialogInterface.OnClickListener yesListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToDoDAO toDoDAO = new ToDoDAO(context);
                toDoDAO.remove(toDoId);
                MemoDAO memoDAO = new MemoDAO(context);
                memoDAO.removeByToDoId(toDoId);
                // TODO: Remove all related notice
                ((MainActivity) context).updateToDoListView();
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