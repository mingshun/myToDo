package com.gutspot.apps.android.mytodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ToDoActivity extends Activity {

    private long toDoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        Intent intent = this.getIntent();
        toDoId = intent.getLongExtra("todo_id", -1);
        if (toDoId == -1) {
            Toast.makeText(this, "无效参数todo_id", Toast.LENGTH_SHORT).show();
            this.finish();
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.to_do, menu);
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

        case R.id.action_add_memo:
            Intent addMemoIntent = new Intent(this, AddToDoMemoActivity.class);
            addMemoIntent.putExtra("type", 2);
            addMemoIntent.putExtra("todo_id", toDoId);
            startActivity(addMemoIntent);
            return true;

        default:
            return super.onOptionsItemSelected(item);
        }
    }

}
