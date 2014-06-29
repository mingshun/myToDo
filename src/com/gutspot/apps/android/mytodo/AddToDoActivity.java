package com.gutspot.apps.android.mytodo;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddToDoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        int[] textColorButtonIds = new int[] { R.id.button_text_color_black, R.id.button_text_color_red,
                R.id.button_text_color_green, R.id.button_text_color_blue };
        OnClickListener changeTextColorListener = new ChangeTextColorListener();
        for (int textColorButtonId : textColorButtonIds) {
            Button textColorButton = (Button) this.findViewById(textColorButtonId);
            textColorButton.setOnClickListener(changeTextColorListener);
        }

        int[] backgroundButtonIds = new int[] { R.id.button_background_color_yellow,
                R.id.button_background_color_orage, R.id.button_background_color_red,
                R.id.button_background_color_green, R.id.button_background_color_blue };
        OnClickListener changeBackgroundColorListener = new ChangeBackgroundColorListener();
        for (int backgroundButtonId : backgroundButtonIds) {
            Button backgroundButton = (Button) this.findViewById(backgroundButtonId);
            backgroundButton.setOnClickListener(changeBackgroundColorListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_to_do, menu);
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

        case R.id.action_save_todo:
            return true;

        default:
            return super.onOptionsItemSelected(item);
        }

    }

    class ChangeTextColorListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            Button button = (Button) v;
            ColorDrawable color = (ColorDrawable) button.getBackground();
            EditText edit = (EditText) AddToDoActivity.this.findViewById(R.id.edit_content);
            edit.setTextColor(color.getColor());
        }
    }

    class ChangeBackgroundColorListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            Button button = (Button) v;
            ColorDrawable color = (ColorDrawable) button.getBackground();
            EditText edit = (EditText) AddToDoActivity.this.findViewById(R.id.edit_content);
            edit.setBackgroundColor(color.getColor());
        }

    }

}
