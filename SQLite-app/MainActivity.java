package com.example.helloworld;

import static android.R.*;
import static android.R.layout.simple_list_item_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText text;
    private Button insert_but;
    private ListView db_view;

    private TextView modified;
    private Database db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.insert_text);
        insert_but = findViewById(R.id.insert_button);
        db_view = findViewById(R.id.listview0);
        modified = findViewById(R.id.modified);
        db = new Database(getApplicationContext());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, db.getNames());
        db_view.setAdapter(adapter);
        modified.setText(db.getLastModifiedDate());
        insert_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.insertText(String.valueOf(text.getText()));
                adapter.clear();
                adapter.addAll(db.getNames());
                adapter.notifyDataSetChanged();
                db.updateLastModifiedDate();
            }
        });
    }
}
