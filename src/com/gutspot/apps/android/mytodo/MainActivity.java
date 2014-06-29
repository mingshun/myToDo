package com.gutspot.apps.android.mytodo;

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
import android.widget.Toast;

public class MainActivity extends Activity {

    private int pressedCategoryButton = R.id.button_unfinished_todo;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setCategoryButtonListener();
        initPressedCategoryButton();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initPressedCategoryButton();
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
            Intent addToDoIntent = new Intent(this, AddToDoActivity.class);
            startActivity(addToDoIntent);
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
