package com.example.notes;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainViewModel extends AndroidViewModel {

    private static NotesDatabase database;
    private LiveData<List<Note>> notes;

    public MainViewModel(@NonNull Application application) {
        super(application);
        database = NotesDatabase.getInstance(getApplication());
        notes = database.notesDao().getAllNotes();
    }


    public LiveData<List<Note>> getNotes() {
        return notes;
    }

    public void insertNote(Note note) {
        new InsertTask().execute(note);
    }

    public void deleteNote(Note note) {
        new DeleteTask().execute(note);
    }

    public void deleteAllTasks() {
        new DeleteAllTasks().execute();
    }

    public Note getNote(int id) {

        try {
            return new GetNote().execute(id).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateNotes(Note... notes) {

        new UpdateNotes().execute(notes);

    }

    private static class InsertTask extends AsyncTask<Note, Void, Void> {

        @Override
        protected Void doInBackground(Note... notes) {
            if (notes != null && notes.length > 0) {
                database.notesDao().insertNote(notes[0]);
            }

            return null;
        }
    }

    private static class DeleteTask extends AsyncTask<Note, Void, Void> {

        @Override
        protected Void doInBackground(Note... notes) {
            if (notes != null && notes.length > 0) {
                database.notesDao().delete(notes[0]);
            }

            return null;
        }
    }

    private static class DeleteAllTasks extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... notes) {
            database.notesDao().deleteAllNotes();

            return null;
        }
    }

    private static class GetNote extends AsyncTask<Integer, Void, Note> {

        @Override
        protected Note doInBackground(Integer... integers) {

            return database.notesDao().getNote(integers[0]);

        }
    }

    private static class UpdateNotes extends AsyncTask<Note, Void, Void> {
        @Override
        protected Void doInBackground(Note... notes) {

            database.notesDao().updateNotes(notes);

            return null;
        }
    }

}
