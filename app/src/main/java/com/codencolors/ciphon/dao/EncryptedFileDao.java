package com.codencolors.ciphon.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.codencolors.ciphon.dto.EncryptedFile;

import java.util.List;

@Dao
public interface EncryptedFileDao {

    @Insert
    void insert(EncryptedFile file);

    @Delete
    void delete(EncryptedFile file);

    @Query("SELECT * FROM encrypted_files_table WHERE name = :name")
    LiveData<EncryptedFile> getFileByName(String name);

    @Query("SELECT * FROM encrypted_files_table")
    LiveData<List<EncryptedFile>> getAllFiles();
}
