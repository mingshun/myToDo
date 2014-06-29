package com.gutspot.apps.android.mytodo;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class MainActivity extends Activity {

    private int pressedCategoryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);

        setCategoryButtonListener();
        initPressedCategoryButton();
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
        pressedCategoryButton = R.id.button_unfinished_todo;
        Button buttons = (Button) this.findViewById(pressedCategoryButton);
        buttons.setPressed(true);
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
            return true;

        case R.id.action_search_todo:
            return true;

        default:
            return super.onOptionsItemSelected(item);
        }

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
