package com.gutspot.apps.android.mytodo;

import java.util.Date;

import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.gutspot.apps.android.mytodo.dao.MemoDAO;
import com.gutspot.apps.android.mytodo.dao.ToDoDAO;
import com.gutspot.apps.android.mytodo.model.Memo;
import com.gutspot.apps.android.mytodo.model.ToDo;
import com.gutspot.apps.android.mytodo.utils.AlertUtil;

public class AddToDoMemoActivity extends Activity {

    private int pressedTextColorButtonId = R.id.button_text_color_black;
    private int pressedBackgroundButtonId = R.id.button_background_color_yellow;

    private int type;
    private String name;
    private Long toDoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do_memo);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = this.getIntent();
        type = intent.getIntExtra("type", -1);
        switch (type) {
        case 1:
            name = "ToDo";
            toDoId = null;
            break;
        case 2:
            name = "Memo";
            toDoId = intent.getLongExtra("todo_id", -1);
            break;

        default:
            Toast.makeText(this, "无效参数type", Toast.LENGTH_SHORT).show();
            this.finish();
            return;
        }

        this.setTitle("添加" + name);

        setColorButtonListeners();
        initPressedColorButton();
    }

    @Override
    public void onBackPressed() {
        String message = "是否放弃保存" + name + "？";

        String yesLabel = "是";
        DialogInterface.OnClickListener yesListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                AddToDoMemoActivity.this.finish();
            }
        };

        String noLabel = "否";
        DialogInterface.OnClickListener noListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        };

        AlertUtil.show(this, message, yesLabel, yesListener, noLabel, noListener);
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
            saveData();
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

        EditText edit = (EditText) AddToDoMemoActivity.this.findViewById(R.id.edit_content);
        edit.setTextColor(textColor.getColor());
        edit.setBackgroundColor(backgroundColor.getColor());
    }

    private void saveData() {
        switch (type) {
        case 1:
            ToDo toDo = new ToDo();
            toDo.setCreated(new Date());
            ToDoDAO toDoDAO = new ToDoDAO(this);
            toDoId = toDoDAO.create(toDo);
            if (toDoId == -1) {
                Toast.makeText(this, "保存ToDo失败", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

        case 2:
            EditText edit = (EditText) this.findViewById(R.id.edit_content);
            ColorDrawable backgroundDrawable = (ColorDrawable) edit.getBackground();
            Memo memo = new Memo();
            memo.setToDoId(toDoId);
            memo.setContent(edit.getText().toString());
            memo.setTextColor(edit.getCurrentTextColor());
            memo.setBackgroundColor(backgroundDrawable.getColor());
            memo.setCreated(new Date());
            MemoDAO memoDAO = new MemoDAO(this);
            long memoId = memoDAO.create(memo);
            if (memoId == -1) {
                Toast.makeText(this, "保存Memo失败", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

        default:

        }

        Toast.makeText(this, "保存" + name + "成功", Toast.LENGTH_SHORT).show();
        finish();
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
                    ImageButton b = (ImageButton) AddToDoMemoActivity.this.findViewById(buttonId);
                    b.setImageResource(android.R.color.transparent);
                }
            }

            ColorDrawable color = (ColorDrawable) button.getBackground();
            EditText edit = (EditText) AddToDoMemoActivity.this.findViewById(R.id.edit_content);
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
                    ImageButton b = (ImageButton) AddToDoMemoActivity.this.findViewById(buttonId);
                    b.setImageResource(android.R.color.transparent);
                }
            }

            ColorDrawable color = (ColorDrawable) button.getBackground();
            EditText edit = (EditText) AddToDoMemoActivity.this.findViewById(R.id.edit_content);
            edit.setBackgroundColor(color.getColor());
            pressedBackgroundButtonId = button.getId();
        }

    }

}
