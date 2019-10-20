package com.codencolors.ciphon.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.codencolors.ciphon.dao.EncryptedFileDao;
import com.codencolors.ciphon.dto.EncryptedFile;

@Database(entities = {EncryptedFile.class}, version = 1, exportSchema = false)
public abstract class EncryptedFileDatabase extends RoomDatabase {
    private static EncryptedFileDatabase instance;

    public abstract EncryptedFileDao dao();

    public static synchronized EncryptedFileDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    EncryptedFileDatabase.class,
                    "encrypted_file_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
