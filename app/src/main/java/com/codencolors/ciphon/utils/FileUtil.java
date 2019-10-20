package com.codencolors.ciphon.utils;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import kotlin.jvm.internal.PropertyReference0Impl;

public class FileUtil {

    private static final String USER_ID_ATTRIBUTE = "user:objectid";
    private static final String USER_ID = "objectid";

    private static final String TAG = "FileUtil";

    public static void moveFileToEncryptedFilesDirectory(File source, File target) {
        new MoveFileAsyncTask().execute(source, target);
    }

    /**
     * Set Unique id to file.
     *
     * @param path is the file path
     * @return Optional.empty() on failure condition, set an identifier to file.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Optional<String> setId(Path path) {
        String fileId = generateFileId();
        return setId(path, fileId);
    }

    /**
     * get the id associated with the file.
     *
     * @param path is the file path
     * @return Optional.empty(), if no id associated with given file, else
     *         return the id.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Optional<String> getId(Path path) {
        UserDefinedFileAttributeView fileAttributeView = Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);
        try {
            List<String> userAttributes = fileAttributeView.list();

            if (userAttributes.contains(USER_ID)) {
                byte[] b = (byte[]) Files.getAttribute(path, USER_ID_ATTRIBUTE);
                String objectId = new String(b, StandardCharsets.UTF_8);
                return Optional.of(objectId);
            }
        } catch (IOException e) {
            return Optional.empty();
        }

        return Optional.empty();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Optional<String> setId(Path path, String fileId) {
        try {
            Files.setAttribute(path, USER_ID_ATTRIBUTE, fileId.getBytes(StandardCharsets.UTF_8));
            return Optional.of(fileId);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public static String generateFileId() {
        return UUID.randomUUID().toString();
    }

    private static class MoveFileAsyncTask extends AsyncTask<File, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostMovingTask: " + s);
        }

        @Override
        protected String doInBackground(File... files) {
            try {
                Files.move(files[0].toPath(), files[1].toPath(), StandardCopyOption.REPLACE_EXISTING);
                return "file moved";
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "moveFileToEncryptedFileDirectory: " + e.getLocalizedMessage());
                return "error moving file";
            }
        }
    }
}
