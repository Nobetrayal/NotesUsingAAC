package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNotes;
    private final List<Note> notes = new ArrayList<>();
    private NotesAdapter adapter;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        getData();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);

//        adapter = new NotesAdapter(notes);
        adapter = new NotesAdapter(notes);
        adapter.setOnNoteClickListener(new NotesAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(int position) {

                Note note = adapter.getNotes().get(position);

                Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
                intent.putExtra("edit", true);
                intent.putExtra("id", note.getId());
                startActivity(intent);

            }

            @Override
            public void onLongNoteClick(int position) {

                remove(position);

            }
        });

        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNotes.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                remove(viewHolder.getAdapterPosition());

            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerViewNotes);

    }

    private void remove(int position) {
        Note note = adapter.getNotes().get(position);
        viewModel.deleteNote(note);


    }

    public void onClickAddNote(View view) {

        Intent intent = new Intent(this, AddNoteActivity.class);
        startActivity(intent);

    }

    private void getData() {
        // observe
        LiveData<List<Note>> notesFromDB = viewModel.getNotes();
        notesFromDB.observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notesFromLiveData) {
                adapter.setNotes(notesFromLiveData);
            }
        });


    }

}