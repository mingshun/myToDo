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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.gutspot.apps.android.mytodo.adapter.ToDoAdapter;
import com.gutspot.apps.android.mytodo.adapter.ToDoAdapter.ToDoItem;
import com.gutspot.apps.android.mytodo.dao.MemoDAO;
import com.gutspot.apps.android.mytodo.dao.ToDoDAO;
import com.gutspot.apps.android.mytodo.model.Memo;
import com.gutspot.apps.android.mytodo.model.ToDo;

public class MainActivity extends Activity {

    private int pressedCategoryButton = R.id.button_unfinished_todo;
    private boolean doubleBackToExitPressedOnce = false;

    private ListView toDoListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.util.Log.d("MainActivity", this.toString());

        if (savedInstanceState != null) {
            pressedCategoryButton = savedInstanceState.getInt("pressed_category_button");
            android.util.Log.d("MainActivity", "pressedCategoryButton: " + pressedCategoryButton);
        }

        setCategoryButtonListener();
        initPressedCategoryButton();

        initToDoList();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        pressedCategoryButton = savedInstanceState.getInt("pressed_category_button");
        android.util.Log.d("MainActivity", "pressedCategoryButton: " + pressedCategoryButton);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("pressed_category_button", pressedCategoryButton);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initPressedCategoryButton();
        if (toDoListView != null) {
            List<ToDoItem> data = loadData();
            ToDoAdapter adapter = new ToDoAdapter(this, data);
            toDoListView.setAdapter(adapter);
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

    private void setCategoryButtonListener() {
        int[] buttonIds = new int[] { R.id.button_unfinished_todo, R.id.button_finished_todo, R.id.button_all_todo };
        OnTouchListener listener = new CategoryButtonsClickListener(buttonIds);
        for (int buttonId : buttonIds) {
            Button button = (Button) this.findViewById(buttonId);
            button.setOnTouchListener(listener);
        }
    }

    private void initPressedCategoryButton() {
        Button button = (Button) this.findViewById(pressedCategoryButton);
        button.setPressed(true);
    }

    private List<ToDoItem> loadData() {
        List<ToDoItem> toDoItems = new ArrayList<ToDoItem>();
        ToDoDAO toDoDAO = new ToDoDAO(this);
        List<ToDo> toDos = toDoDAO.findOrderByCreated();
        MemoDAO memoDAO = new MemoDAO(this);
        for (ToDo toDo : toDos) {
            ToDoItem toDoItem = new ToDoItem();
            toDoItem.id = toDo.getId();
            toDoItem.created = toDo.getCreated();
            toDoItem.finished = toDo.getFinished();
            Memo memo = memoDAO.findFirstByToDoId(toDoItem.id);
            toDoItem.digest = memo.getContent();
            toDoItems.add(toDoItem);
        }
        return toDoItems;
    }

    private void initToDoList() {
        List<ToDoItem> data = loadData();
        ToDoAdapter adapter = new ToDoAdapter(this, data);
        toDoListView = (ListView) this.findViewById(R.id.list_todo);
        toDoListView.setAdapter(adapter);
    }

    class CategoryButtonsClickListener implements OnTouchListener {

        private int[] buttonIds;

        public CategoryButtonsClickListener(int[] buttonIds) {
            this.buttonIds = buttonIds;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            for (int buttonId : buttonIds) {
                if (v.getId() != buttonId) {
                    Button button = (Button) MainActivity.this.findViewById(buttonId);
                    button.setPressed(false);
                }
            }
            ((Button) v).setPressed(true);
            pressedCategoryButton = v.getId();
            return true;
        }
    }

}
