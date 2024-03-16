package com.example.buzzpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.time.ZonedDateTime;
import java.util.Date;

public class NoteDetailsActivity extends AppCompatActivity
{
    EditText notes_title_text, notes_content_text;
    ImageButton save_note_button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        notes_title_text = findViewById(R.id.notes_title_text);
        notes_content_text = findViewById(R.id.notes_content_text);
        save_note_button = findViewById(R.id.save_note_button);

        save_note_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });


    }

    void saveNote()
    {
        String noteTitle = notes_title_text.getText().toString();
        String noteContent = notes_content_text.getText().toString();
        if(noteTitle == null || noteTitle.isEmpty())
        {
            notes_title_text.setError("Title is required");
            return;
        }

        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());
        saveNoteToFirebase(note);

    }

    void saveNoteToFirebase(Note note)
    {
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForNotes().document();
        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Utility.showToast(NoteDetailsActivity.this, "Note added successfully");
                    finish();
                }
                else
                {
                    Utility.showToast(NoteDetailsActivity.this, "Failed while adding note");
                }
            }
        });
    }
}