package com.codencolors.ciphon.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.codencolors.ciphon.dao.EncryptedFileDao;
import com.codencolors.ciphon.db.EncryptedFileDatabase;
import com.codencolors.ciphon.dto.EncryptedFile;

import java.util.List;

public class EncryptedFileRepository {
    private EncryptedFileDao dao;

    private static final String TAG = "EncryptedFileRepository";

    public EncryptedFileRepository(Application application) {
        EncryptedFileDatabase database = EncryptedFileDatabase.getInstance(application);
        dao = database.dao();
    }

    public void insert(EncryptedFile encryptedFile) {
        new InsertFileTask(dao).execute(encryptedFile);
    }

    public void delete(EncryptedFile encryptedFile) {
        new DeleteFileTask(dao).execute(encryptedFile);
    }

    public LiveData<List<EncryptedFile>> getAllFiles() {
        return dao.getAllFiles();
    }

    public LiveData<EncryptedFile> getFileByName(String name) {
        return dao.getFileByName(name);
    }

    private static class InsertFileTask extends AsyncTask<EncryptedFile, Void, String> {

        private EncryptedFileDao dao;

        private InsertFileTask(EncryptedFileDao dao) {
            this.dao = dao;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d(TAG, "onPostInsertion: " + s);
        }

        @Override
        protected String doInBackground(EncryptedFile... encryptedFiles) {
            dao.insert(encryptedFiles[0]);
            return "file inserted into DB";
        }
    }

    private static class DeleteFileTask extends AsyncTask<EncryptedFile, Void, String> {

        private EncryptedFileDao dao;

        private DeleteFileTask(EncryptedFileDao dao) {
            this.dao = dao;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d(TAG, "onPostDeletion: " + s);
        }

        @Override
        protected String doInBackground(EncryptedFile... encryptedFiles) {
            dao.delete(encryptedFiles[0]);
            return "file deleted fromDB";
        }
    }
}
