package com.codencolors.ciphon.dto;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.codencolors.ciphon.utils.TimestampConverter;

import java.util.Date;

@Entity(tableName = "encrypted_files_table")
public class EncryptedFile {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "size")
    private String size;

    @ColumnInfo(name = "created_at")
    @TypeConverters({TimestampConverter.class})
    private Date creationDate;

    public EncryptedFile(String name, String password, String size, Date creationDate) {
        this.name = name;
        this.password = password;
        this.size = size;
        this.creationDate = creationDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getSize() {
        return size;
    }

    public Date getCreationDate() {
        return creationDate;
    }
}
