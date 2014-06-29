package com.gutspot.apps.android.mytodo;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

public class AddToDoActivity extends Activity {

    private int pressedTextColorButtonId = R.id.button_text_color_black;
    private int pressedBackgroundButtonId = R.id.button_background_color_yellow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setColorButtonListeners();
        initPressedColorButton();
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

    private void setColorButtonListeners() {
        int[] textColorButtonIds = new int[] { R.id.button_text_color_black, R.id.button_text_color_red,
                R.id.button_text_color_green, R.id.button_text_color_blue };
        OnClickListener changeTextColorListener = new ChangeTextColorListener(textColorButtonIds);
        for (int textColorButtonId : textColorButtonIds) {
            ImageButton textColorButton = (ImageButton) this.findViewById(textColorButtonId);
            textColorButton.setOnClickListener(changeTextColorListener);
        }

        int[] backgroundButtonIds = new int[] { R.id.button_background_color_yellow,
                R.id.button_background_color_orage, R.id.button_background_color_red,
                R.id.button_background_color_green, R.id.button_background_color_blue };
        OnClickListener changeBackgroundColorListener = new ChangeBackgroundColorListener(backgroundButtonIds);
        for (int backgroundButtonId : backgroundButtonIds) {
            ImageButton backgroundButton = (ImageButton) this.findViewById(backgroundButtonId);
            backgroundButton.setOnClickListener(changeBackgroundColorListener);
        }
    }

    private void initPressedColorButton() {
        ImageButton pressedTextColorButton = (ImageButton) this.findViewById(pressedTextColorButtonId);
        pressedTextColorButton.setImageResource(R.drawable.ic_action_accept);
        ColorDrawable textColor = (ColorDrawable) pressedTextColorButton.getBackground();

        ImageButton pressedBackgroundColorButton = (ImageButton) this.findViewById(pressedBackgroundButtonId);
        pressedBackgroundColorButton.setImageResource(R.drawable.ic_action_accept_light);
        ColorDrawable backgroundColor = (ColorDrawable) pressedBackgroundColorButton.getBackground();

        EditText edit = (EditText) AddToDoActivity.this.findViewById(R.id.edit_content);
        edit.setTextColor(textColor.getColor());
        edit.setBackgroundColor(backgroundColor.getColor());
    }

    class ChangeTextColorListener implements OnClickListener {

        private int[] buttonIds;

        public ChangeTextColorListener(int[] buttonIds) {
            this.buttonIds = buttonIds;
        }

        @Override
        public void onClick(View v) {
            ImageButton button = (ImageButton) v;
            button.setImageResource(R.drawable.ic_action_accept);
            for (int buttonId : buttonIds) {
                if (button.getId() != buttonId) {
                    ImageButton b = (ImageButton) AddToDoActivity.this.findViewById(buttonId);
                    b.setImageResource(android.R.color.transparent);
                }
            }

            ColorDrawable color = (ColorDrawable) button.getBackground();
            EditText edit = (EditText) AddToDoActivity.this.findViewById(R.id.edit_content);
            edit.setTextColor(color.getColor());
            pressedTextColorButtonId = button.getId();
        }
    }

    class ChangeBackgroundColorListener implements OnClickListener {

        private int[] buttonIds;

        public ChangeBackgroundColorListener(int[] buttonIds) {
            this.buttonIds = buttonIds;
        }

        @Override
        public void onClick(View v) {
            ImageButton button = (ImageButton) v;
            button.setImageResource(R.drawable.ic_action_accept_light);
            for (int buttonId : buttonIds) {
                if (button.getId() != buttonId) {
                    ImageButton b = (ImageButton) AddToDoActivity.this.findViewById(buttonId);
                    b.setImageResource(android.R.color.transparent);
                }
            }
            
            ColorDrawable color = (ColorDrawable) button.getBackground();
            EditText edit = (EditText) AddToDoActivity.this.findViewById(R.id.edit_content);
            edit.setBackgroundColor(color.getColor());
            pressedBackgroundButtonId = button.getId();
        }

    }

}
