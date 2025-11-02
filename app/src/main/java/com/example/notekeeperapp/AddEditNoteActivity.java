package com.example.notekeeperapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class AddEditNoteActivity extends AppCompatActivity {

    private TextInputEditText editTextTitle, editTextContent;
    private Button buttonSave;
    private DatabaseHelper db;
    private Note existingNote;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextContent = findViewById(R.id.editTextContent);
        buttonSave = findViewById(R.id.buttonSave);
        db = new DatabaseHelper(this);

        if (getIntent().hasExtra("NOTE_ID")) {
            isEditMode = true;
            long noteId = getIntent().getLongExtra("NOTE_ID", -1);
            existingNote = db.getNote(noteId);

            if (existingNote != null) {
                editTextTitle.setText(existingNote.getTitle());
                editTextContent.setText(existingNote.getContent());
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle("Edit Note");
                }
            } else {
                Toast.makeText(this, "Error: Note not found.", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity if the note is not found
                return;
            }
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Add Note");
            }
        }

        buttonSave.setOnClickListener(v -> saveNote());
    }

    private void saveNote() {
        String title = editTextTitle.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Please enter title and content", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isEditMode) {
            if (existingNote != null) {
                existingNote.setTitle(title);
                existingNote.setContent(content);
                db.updateNote(existingNote);
            }
        } else {
            Note newNote = new Note();
            newNote.setTitle(title);
            newNote.setContent(content);
            db.addNote(newNote);
        }
        finish(); // Go back to MainActivity
    }
}
