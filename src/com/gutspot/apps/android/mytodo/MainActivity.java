package com.gutspot.apps.android.mytodo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.gutspot.apps.android.mytodo.adapter.ToDoAdapter;
import com.gutspot.apps.android.mytodo.adapter.ToDoAdapter.ToDoItem;
import com.gutspot.apps.android.mytodo.dao.MemoDAO;
import com.gutspot.apps.android.mytodo.dao.ToDoDAO;
import com.gutspot.apps.android.mytodo.dialog.ToDoOptionsDialog;
import com.gutspot.apps.android.mytodo.model.Memo;
import com.gutspot.apps.android.mytodo.model.ToDo;

public class MainActivity extends Activity {

    private static final String[] categories = new String[] { "未完成", "已完成", "全部" };
    private static final String[] sortMethods = new String[] { "创建时间 - 升序", "创建时间 - 降序", "完成时间 - 升序", "完成时间 - 降序" };

    private int currentCategory = 0;
    private int currentSortMethod = 0;

    private boolean doubleBackToExitPressedOnce = false;

    private ListView toDoListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.util.Log.d("MainActivity", this.toString());

        if (savedInstanceState != null) {
            currentCategory = savedInstanceState.getInt("current_category");
            currentSortMethod = savedInstanceState.getInt("current_sort_method");
        }

        Spinner categorySpinner = (Spinner) this.findViewById(R.id.spinner_category);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this, R.layout.item_spinner, categories);
        categoryAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentCategory = position;
                updateToDoListView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        Spinner sortMethodSpinner = (Spinner) this.findViewById(R.id.spinner_sort_method);
        ArrayAdapter<String> sortMethodAdapter = new ArrayAdapter<String>(this, R.layout.item_spinner, sortMethods);
        sortMethodAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        sortMethodSpinner.setAdapter(sortMethodAdapter);
        sortMethodSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentSortMethod = position;
                updateToDoListView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        toDoListView = (ListView) this.findViewById(R.id.list_todo);
        updateToDoListView();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentCategory = savedInstanceState.getInt("current_category");
        currentSortMethod = savedInstanceState.getInt("current_sort_method");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("current_category", currentCategory);
        outState.putInt("current_sort_method", currentSortMethod);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

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

    public void updateToDoListView() {
        List<ToDoItem> toDoItems = new ArrayList<ToDoItem>();
        ToDoDAO toDoDAO = new ToDoDAO(this);
        MemoDAO memoDAO = new MemoDAO(this);

        List<ToDo> toDos = new ArrayList<ToDo>();
        switch (currentCategory) {
        case 0:
            toDos = toDoDAO.findUnfinishedOrderByCreated();
            break;
        case 1:
            toDos = toDoDAO.findFinishedOrderByCreated();
            break;
        case 2:
            toDos = toDoDAO.findOrderByCreated();
            break;
        default:
        }

        for (ToDo toDo : toDos) {
            ToDoItem toDoItem = new ToDoItem();
            toDoItem.toDo = toDo;
            Memo memo = memoDAO.findFirstByToDoId(toDo.getId());
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
                intent.putExtra("todo_id", item.toDo.getId());
                activity.startActivity(intent);
            }
        });
        toDoListView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ToDoItem item = (ToDoItem) adapter.getItem(position);
                DialogFragment optionsDialog = ToDoOptionsDialog.newInstance(MainActivity.this, item.toDo.getId());
                optionsDialog.show(MainActivity.this.getFragmentManager(), "dialog");
                return false;
            }

        });
    }

}
