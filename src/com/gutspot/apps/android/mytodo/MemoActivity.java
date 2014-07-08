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
import android.widget.ScrollView;
import android.widget.Toast;

import com.gutspot.apps.android.mytodo.dao.MemoDAO;
import com.gutspot.apps.android.mytodo.dao.ToDoDAO;
import com.gutspot.apps.android.mytodo.model.Memo;
import com.gutspot.apps.android.mytodo.model.ToDo;
import com.gutspot.apps.android.mytodo.utils.AlertUtil;

public class MemoActivity extends Activity {

    private static final int[] textColorButtonIds = new int[] { R.id.button_text_color_black,
            R.id.button_text_color_red, R.id.button_text_color_green, R.id.button_text_color_blue };
    private static final int[] backgroundButtonIds = new int[] { R.id.button_background_color_yellow,
            R.id.button_background_color_orage, R.id.button_background_color_red,
            R.id.button_background_color_green, R.id.button_background_color_blue };

    private int pressedTextColorButtonId = R.id.button_text_color_black;
    private int pressedBackgroundButtonId = R.id.button_background_color_yellow;

    private int type;
    private String title;
    private long toDoId;
    private long memoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = this.getIntent();
        type = intent.getIntExtra("type", -1);
        switch (type) {
        case 1:
            title = "添加ToDo";
            toDoId = -1;
            break;

        case 2:
            title = "添加Memo";
            toDoId = intent.getLongExtra("todo_id", -1);
            break;

        case 3:
            title = "编辑Memo";
            toDoId = intent.getLongExtra("todo_id", -1);
            memoId = intent.getLongExtra("memo_id", -1);
            break;

        default:
            Toast.makeText(this, "无效参数type", Toast.LENGTH_SHORT).show();
            this.finish();
            return;
        }

        this.setTitle(title);

        setColorButtonListeners();

        switch (type) {
        case 1:
        case 2:
            initPressedColorButton();
            break;

        case 3:
            findView();
            break;

        default:
        }

    }

    @Override
    public void onBackPressed() {
        // Check if the memo has been modified.
        // If not, skip displaying the confirm dialog.
        if (type == 3) {
            MemoDAO memoDAO = new MemoDAO(this);
            Memo memo = memoDAO.findById(memoId);
            EditText edit = (EditText) this.findViewById(R.id.edit_content);
            if (memo.getContent().equals(edit.getText().toString())) {
                this.finish();
                return;
            }
        }

        String message = "是否放弃保存？";

        String yesLabel = "是";
        DialogInterface.OnClickListener yesListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                MemoActivity.this.finish();
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
        getMenuInflater().inflate(R.menu.add_to_do_memo, menu);
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
        OnClickListener changeTextColorListener = new ChangeTextColorListener(textColorButtonIds);
        for (int textColorButtonId : textColorButtonIds) {
            ImageButton textColorButton = (ImageButton) this.findViewById(textColorButtonId);
            textColorButton.setOnClickListener(changeTextColorListener);
        }

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

        EditText edit = (EditText) MemoActivity.this.findViewById(R.id.edit_content);
        edit.setTextColor(textColor.getColor());
        ScrollView editView = (ScrollView) this.findViewById(R.id.view_content);
        editView.setBackgroundColor(backgroundColor.getColor());
    }

    private void findView() {
        MemoDAO memoDAO = new MemoDAO(this);
        Memo memo = memoDAO.findById(memoId);

        EditText edit = (EditText) this.findViewById(R.id.edit_content);
        edit.setText(memo.getContent());
        edit.setTextColor(memo.getTextColor());
        ScrollView editView = (ScrollView) this.findViewById(R.id.view_content);
        editView.setBackgroundColor(memo.getBackgroundColor());

        for (int buttonId : textColorButtonIds) {
            ImageButton button = (ImageButton) this.findViewById(buttonId);
            ColorDrawable color = (ColorDrawable) button.getBackground();
            if (color.getColor() == memo.getTextColor()) {
                button.setImageResource(R.drawable.ic_action_accept);
            } else {
                button.setImageResource(android.R.color.transparent);
            }
        }

        for (int buttonId : backgroundButtonIds) {
            ImageButton button = (ImageButton) this.findViewById(buttonId);
            ColorDrawable color = (ColorDrawable) button.getBackground();
            if (color.getColor() == memo.getBackgroundColor()) {
                button.setImageResource(R.drawable.ic_action_accept_light);
            } else {
                button.setImageResource(android.R.color.transparent);
            }
        }
    }

    private void saveMemo() {
        EditText edit = (EditText) this.findViewById(R.id.edit_content);
        ScrollView editView = (ScrollView) this.findViewById(R.id.view_content);
        ColorDrawable backgroundDrawable = (ColorDrawable) editView.getBackground();

        MemoDAO memoDAO = new MemoDAO(this);
        Memo memo = null;
        if (type == 3) {
            memo = memoDAO.findById(memoId);
        } else {
            memo = new Memo();
            memo.setCreated(new Date());
        }
        memo.setToDoId(toDoId);
        memo.setContent(edit.getText().toString());
        memo.setTextColor(edit.getCurrentTextColor());
        memo.setBackgroundColor(backgroundDrawable.getColor());
        long result = -1;
        if (type == 3) {
            result = memoDAO.update(memo);
        } else {
            result = memoDAO.create(memo);
        }
        if (result == -1) {
            Toast.makeText(this, "保存Memo失败", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
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
        case 3:
            saveMemo();

        default:

        }

        finish();
        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ToDoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("todo_id", toDoId);
        this.startActivity(intent);
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
                    ImageButton b = (ImageButton) MemoActivity.this.findViewById(buttonId);
                    b.setImageResource(android.R.color.transparent);
                }
            }

            ColorDrawable color = (ColorDrawable) button.getBackground();
            EditText edit = (EditText) MemoActivity.this.findViewById(R.id.edit_content);
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
                    ImageButton b = (ImageButton) MemoActivity.this.findViewById(buttonId);
                    b.setImageResource(android.R.color.transparent);
                }
            }

            ColorDrawable color = (ColorDrawable) button.getBackground();
            ScrollView editView = (ScrollView) MemoActivity.this.findViewById(R.id.view_content);
            editView.setBackgroundColor(color.getColor());
            pressedBackgroundButtonId = button.getId();
        }

    }

}
