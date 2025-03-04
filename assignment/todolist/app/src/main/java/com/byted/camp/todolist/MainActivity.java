package com.byted.camp.todolist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.byted.camp.todolist.beans.Note;
import com.byted.camp.todolist.beans.State;
import com.byted.camp.todolist.db.TodoContract;
import com.byted.camp.todolist.db.TodoDbHelper;
import com.byted.camp.todolist.debug.DebugActivity;
import com.byted.camp.todolist.ui.NoteListAdapter;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD = 1002;

    private RecyclerView recyclerView;
    private NoteListAdapter notesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(
                        new Intent(MainActivity.this, NoteActivity.class),
                        REQUEST_CODE_ADD);
            }
        });

        recyclerView = findViewById(R.id.list_todo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        notesAdapter = new NoteListAdapter(new NoteOperator() {
            @Override
            public void deleteNote(Note note) {
                MainActivity.this.deleteNote(note);
            }

            @Override
            public void updateNote(Note note) {
                MainActivity.this.updateNode(note);
            }
        });
        recyclerView.setAdapter(notesAdapter);

        notesAdapter.refresh(loadNotesFromDatabase());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_debug:
                startActivity(new Intent(this, DebugActivity.class));
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD
                && resultCode == Activity.RESULT_OK) {
            notesAdapter.refresh(loadNotesFromDatabase());
        }
    }

    private List<Note> loadNotesFromDatabase() {
        // TODO 从数据库中查询数据，并转换成 JavaBeans
        TodoDbHelper mDbHelper = new TodoDbHelper(MainActivity.this);

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        if (db == null) {
            return Collections.emptyList();
        }

        List<Note> result = new LinkedList<>();
        Cursor cursor = null;

        String[] projection = {
                TodoContract.TodoNotes._ID,
                TodoContract.TodoNotes.COLUMN_DATE,
                TodoContract.TodoNotes.COLUMN_STATE,
                TodoContract.TodoNotes.COLUMN_CONTENT,
                TodoContract.TodoNotes.COLUMN_PRIORITY
        };
        try {
            cursor = db.query(
                    TodoContract.TodoNotes.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    TodoContract.TodoNotes.COLUMN_PRIORITY + " ASC"
            );

            while (cursor.moveToNext()) {
                String content = cursor.getString(cursor.getColumnIndex(TodoContract.TodoNotes.COLUMN_CONTENT));
                long dataMs = cursor.getLong(cursor.getColumnIndex(TodoContract.TodoNotes.COLUMN_DATE));
                int intState = cursor.getInt(cursor.getColumnIndex(TodoContract.TodoNotes.COLUMN_STATE));
                int id = cursor.getInt(cursor.getColumnIndex(TodoContract.TodoNotes._ID));
                int priority = cursor.getInt(cursor.getColumnIndex(TodoContract.TodoNotes.COLUMN_PRIORITY));

                Note note = new Note(id);
                note.setContent(content);
                note.setDate(new Date(dataMs));
                note.setState(State.from(intState));
                note.setPriority(priority);

                result.add(note);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }



        return result;
    }

    private void deleteNote(Note note) {
        // TODO 删除数据

        String selection = TodoContract.TodoNotes._ID + " = ?";
        String[] selectionArgs = {String.valueOf(note.id)};

        TodoDbHelper dbHelper = new TodoDbHelper(MainActivity.this);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int deleteRows = db.delete(TodoContract.TodoNotes.TABLE_NAME, selection, selectionArgs);

        if (deleteRows > 0) {
            notesAdapter.refresh(loadNotesFromDatabase());
        }
    }

    private void updateNode(Note note) {
        // TODO 更新数据
        TodoDbHelper dbHelper = new TodoDbHelper(MainActivity.this);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TodoContract.TodoNotes.COLUMN_CONTENT, note.getContent());
        values.put(TodoContract.TodoNotes.COLUMN_DATE, note.getDate().getTime());
        values.put(TodoContract.TodoNotes.COLUMN_STATE, note.getState().intValue);
        values.put(TodoContract.TodoNotes.COLUMN_PRIORITY, note.getPriority());

        String selection = TodoContract.TodoNotes._ID + " = ?";
        String selectionArgs[] = {
                String.valueOf(note.id)
        };

        db.update(TodoContract.TodoNotes.TABLE_NAME, values, selection, selectionArgs);
    }

}
