package com.codencolors.ciphon.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.codencolors.ciphon.dto.EncryptedFile;
import com.codencolors.ciphon.repository.EncryptedFileRepository;

import java.util.List;

public class EncryptedFileViewModel extends AndroidViewModel {
    private EncryptedFileRepository repository;
    private LiveData<List<EncryptedFile>> allFiles;

    public EncryptedFileViewModel(@NonNull Application application) {
        super(application);
        repository = new EncryptedFileRepository(application);
        allFiles = repository.getAllFiles();
    }

    public void insert(EncryptedFile encryptedFile) {
        repository.insert(encryptedFile);
    }

    public void delete(EncryptedFile encryptedFile) {
        repository.delete(encryptedFile);
    }

    public LiveData<EncryptedFile> getFileByName(String name) {
        return repository.getFileByName(name);
    }

    public LiveData<List<EncryptedFile>> getAllFiles() {
        return allFiles;
    }
}
