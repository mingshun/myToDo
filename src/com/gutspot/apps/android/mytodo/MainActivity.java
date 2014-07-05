package com.gutspot.apps.android.mytodo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.gutspot.apps.android.mytodo.adapter.ToDoAdapter;
import com.gutspot.apps.android.mytodo.adapter.ToDoAdapter.ToDoItem;
import com.gutspot.apps.android.mytodo.dao.MemoDAO;
import com.gutspot.apps.android.mytodo.dao.ToDoDAO;
import com.gutspot.apps.android.mytodo.model.Memo;
import com.gutspot.apps.android.mytodo.model.ToDo;

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
            Intent addToDoMemoIntent = new Intent(this, AddToDoMemoActivity.class);
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
                Activity mainActivity = MainActivity.this;
                ToDoItem item = (ToDoItem) adapter.getItem(position);
                Intent intent = new Intent(mainActivity, ToDoActivity.class);
                intent.putExtra("todo_id", item.id);
                mainActivity.startActivity(intent);
            }
        });
    }

}
