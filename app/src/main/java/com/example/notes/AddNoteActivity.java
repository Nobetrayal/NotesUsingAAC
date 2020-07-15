package com.example.notes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddNoteActivity extends AppCompatActivity {

    private TextView editTextTitle;
    private TextView editTextDescription;
    private Spinner spinnerDayOfWeek;
    private RadioGroup radioGroupPriority;
    private MainViewModel viewModel;

    private boolean isEditing = false;
    private int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }


        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        spinnerDayOfWeek = findViewById(R.id.spinnerDayOfWeek);
        radioGroupPriority = findViewById(R.id.radioGroupPriority);

        Intent intent = getIntent();
        fillSavedValues(intent);

    }

    private void fillSavedValues(Intent intent) {

        isEditing = intent.getBooleanExtra("edit", false);

        if (isEditing) {

            noteId = intent.getIntExtra("id", 0);
            Note note = viewModel.getNote(noteId);

            editTextTitle.setText(note.getTitle());
            editTextDescription.setText(note.getDescription());
            spinnerDayOfWeek.setSelection(note.getDayOfWeek() - 1);

            int radioButtonID;

            switch (note.getPriority()) {

                case 1:
                    radioButtonID = R.id.radioButton1;
                    break;
                case 2:
                    radioButtonID = R.id.radioButton2;
                    break;
                default:
                    radioButtonID = R.id.radioButton3;

            }

            RadioButton radioButtonPriority = findViewById(radioButtonID);
            radioButtonPriority.setChecked(true);

        }

    }

    public void onClickSaveNote(View view) {

        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        int dayOfWeek = spinnerDayOfWeek.getSelectedItemPosition() + 1;

        int radioButtonId = radioGroupPriority.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(radioButtonId);
        int priority = Integer.parseInt(radioButton.getText().toString());

        if (isFilled(title, description)) {

            if (isEditing){
                Note note = new Note(noteId, title, description, dayOfWeek, priority);
                viewModel.updateNotes(note);
            } else {
                Note note = new Note(title, description, dayOfWeek, priority);
                viewModel.insertNote(note);
            }



            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        } else {


            Toast.makeText(this, R.string.filling_note_warning, Toast.LENGTH_SHORT).show();


        }


    }

    private boolean isFilled(String title, String description) {

        return !title.isEmpty() && !description.isEmpty();

    }
}