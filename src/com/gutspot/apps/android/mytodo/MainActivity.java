package com.gutspot.apps.android.mytodo;

import java.util.ArrayList;
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
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.gutspot.apps.android.mytodo.adapter.ToDoAdapter;
import com.gutspot.apps.android.mytodo.adapter.ToDoAdapter.ToDoItem;
import com.gutspot.apps.android.mytodo.dao.MemoDAO;
import com.gutspot.apps.android.mytodo.dao.ToDoDAO;
import com.gutspot.apps.android.mytodo.model.Memo;
import com.gutspot.apps.android.mytodo.model.ToDo;
import com.gutspot.apps.android.mytodo.utils.AlertUtil;

public class MainActivity extends Activity implements OnTouchListener {

    private static final int[] categoryButtonIds = new int[] { R.id.button_unfinished_todo, R.id.button_finished_todo,
            R.id.button_all_todo };
    private int pressedCategoryButtonId = categoryButtonIds[0];
    private boolean doubleBackToExitPressedOnce = false;

    private ListView toDoListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.util.Log.d("MainActivity", this.toString());

        if (savedInstanceState != null) {
            pressedCategoryButtonId = savedInstanceState.getInt("pressed_category_button");
            android.util.Log.d("MainActivity", "pressedCategoryButton: " + pressedCategoryButtonId);
        }

        setCategoryButtonListener();
        setPressedCategoryButton();

        toDoListView = (ListView) this.findViewById(R.id.list_todo);
        updateToDoListView();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        pressedCategoryButtonId = savedInstanceState.getInt("pressed_category_button");
        android.util.Log.d("MainActivity", "pressedCategoryButton: " + pressedCategoryButtonId);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("pressed_category_button", pressedCategoryButtonId);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        setPressedCategoryButton();
        if (toDoListView != null) {
            updateToDoListView();
        }

        android.util.Log.d("MainActivity.onRestart", this.toString());
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "再按一次退出myToDo", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
        case R.id.action_add_todo:
            Intent addToDoMemoIntent = new Intent(this, MemoActivity.class);
            addToDoMemoIntent.putExtra("type", 1);
            startActivity(addToDoMemoIntent);
            return true;

        case R.id.action_search_todo:
            return true;

        default:
            return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        for (int buttonId : categoryButtonIds) {
            if (view.getId() != buttonId) {
                Button button = (Button) this.findViewById(buttonId);
                button.setPressed(false);
            }
        }
        ((Button) view).setPressed(true);
        pressedCategoryButtonId = view.getId();
        updateToDoListView();
        return true;
    }

    private void setCategoryButtonListener() {
        for (int buttonId : categoryButtonIds) {
            Button button = (Button) this.findViewById(buttonId);
            button.setOnTouchListener(this);
        }
    }

    private void setPressedCategoryButton() {
        Button button = (Button) this.findViewById(pressedCategoryButtonId);
        button.setPressed(true);
    }

    private void updateToDoListView() {
        List<ToDoItem> toDoItems = new ArrayList<ToDoItem>();
        ToDoDAO toDoDAO = new ToDoDAO(this);
        MemoDAO memoDAO = new MemoDAO(this);

        List<ToDo> toDos = new ArrayList<ToDo>();
        switch (pressedCategoryButtonId) {
        case R.id.button_unfinished_todo:
            toDos = toDoDAO.findUnfinishedOrderByCreated();
            break;
        case R.id.button_finished_todo:
            toDos = toDoDAO.findFinishedOrderByCreated();
            break;
        case R.id.button_all_todo:
            toDos = toDoDAO.findOrderByCreated();
            break;
        default:
        }

        for (ToDo toDo : toDos) {
            ToDoItem toDoItem = new ToDoItem();
            toDoItem.id = toDo.getId();
            toDoItem.created = toDo.getCreated();
            toDoItem.finished = toDo.getFinished();
            Memo memo = memoDAO.findFirstByToDoId(toDoItem.id);
            toDoItem.digest = memo.getContent();
            toDoItems.add(toDoItem);
        }

        final ToDoAdapter adapter = new ToDoAdapter(this, toDoItems);
        toDoListView.setAdapter(adapter);
        toDoListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Activity activity = MainActivity.this;
                ToDoItem item = (ToDoItem) adapter.getItem(position);
                Intent intent = new Intent(activity, ToDoActivity.class);
                intent.putExtra("todo_id", item.id);
                activity.startActivity(intent);
            }
        });
        toDoListView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ToDoItem item = (ToDoItem) adapter.getItem(position);
                DialogFragment optionsDialog = ToDoOptionsDialog.newInstance(MainActivity.this, item.id);
                optionsDialog.show(MainActivity.this.getFragmentManager(), "dialog");
                return false;
            }

        });
    }

    public static class ToDoOptionsDialog extends DialogFragment implements OnClickListener {
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
}
