package com.byted.camp.todolist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.byted.camp.todolist.beans.Note;
import com.byted.camp.todolist.db.TodoContract;
import com.byted.camp.todolist.db.TodoDbHelper;

import java.util.Date;

public class NoteActivity extends AppCompatActivity {

    private EditText editText;
    private Button addBtn;
    private RadioGroup priorityRadioGroup;
    public static final String EXTRA_PRIORITY_LEVEL = "priority";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        setTitle(R.string.take_a_note);

        editText = findViewById(R.id.edit_text);
        editText.setFocusable(true);
        editText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.showSoftInput(editText, 0);
        }

        priorityRadioGroup = findViewById(R.id.bt_priority_group);
        priorityRadioGroup.check(R.id.bt_low_priority);

        addBtn = findViewById(R.id.btn_add);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence content = editText.getText();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(NoteActivity.this,
                            "No content to add", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean succeed = saveNote2Database(content.toString().trim());
                if (succeed) {
                    Toast.makeText(NoteActivity.this,
                            "Note added", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent();
//                    intent.putExtra(EXTRA_PRIORITY_LEVEL, priorityRadioGroup.getCheckedRadioButtonId());
//                    setResult(Activity.RESULT_OK, intent);
                    setResult(Activity.RESULT_OK);
                } else {
                    Toast.makeText(NoteActivity.this,
                            "Error", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private boolean saveNote2Database(String content) {
        // TODO 插入一条新数据，返回是否插入成功
        TodoDbHelper mDbHelper = new TodoDbHelper(NoteActivity.this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TodoContract.TodoNotes.COLUMN_DATE, System.currentTimeMillis());
        values.put(TodoContract.TodoNotes.COLUMN_STATE, false);
        values.put(TodoContract.TodoNotes.COLUMN_CONTENT, content);
        int priority;
        priority = Note.getPriorityFromId(priorityRadioGroup.getCheckedRadioButtonId());
        values.put(TodoContract.TodoNotes.COLUMN_PRIORITY, priority);

        long newRowId = db.insert(TodoContract.TodoNotes.TABLE_NAME, null, values);
        if (newRowId == -1) {
            return false;
        } else {
            return true;
        }
    }
}
