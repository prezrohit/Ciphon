package com.codencolors.ciphon.activities;

import android.Manifest;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codencolors.ciphon.R;
import com.codencolors.ciphon.adapter.EncryptedFilesAdapter;
import com.codencolors.ciphon.dto.EncryptedFile;
import com.codencolors.ciphon.ui.EncryptedFileViewModel;
import com.codencolors.ciphon.utils.AppUtils;
import com.codencolors.ciphon.utils.FileUtil;
import com.kimcy929.simple_file_chooser.FileChooserActivity;
import com.pvryan.easycrypt.ECResultListener;
import com.pvryan.easycrypt.symmetric.ECSymmetric;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.codencolors.ciphon.utils.AppUtils.ENCRYPTED_FILE_DIRECTORY;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.rv_encrypted_files)
    RecyclerView rvEncryptedFiles;

    @BindView(R.id.lbl_no_files)
    TextView lblNoFiles;

    private String encryptedFileName;
    private EncryptedFileViewModel viewModel;

    private static final int FILE_CHOOSER_REQUEST_CODE = 101;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 100;
    private static final String TAG = MainActivity.class.getSimpleName();

    private final File exportFilesDirectory = new File(ENCRYPTED_FILE_DIRECTORY);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        rvEncryptedFiles.setLayoutManager(new LinearLayoutManager(this));
        rvEncryptedFiles.setHasFixedSize(true);

        EncryptedFilesAdapter adapter = new EncryptedFilesAdapter();
        rvEncryptedFiles.setAdapter(adapter);
        rvEncryptedFiles.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        viewModel = ViewModelProviders.of(this).get(EncryptedFileViewModel.class);
        viewModel.getAllFiles().observe(this, encryptedFiles -> {

            if (encryptedFiles == null || encryptedFiles.size() == 0) {
                rvEncryptedFiles.setVisibility(View.GONE);
                lblNoFiles.setVisibility(View.VISIBLE);

            } else {
                adapter.setNotes(encryptedFiles);
                lblNoFiles.setVisibility(View.GONE);
            }
        });

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST_CODE);
    }

    @OnClick(R.id.fab_add)
    public void onClickAddFiles() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent fileIntent = new Intent(this, FileChooserActivity.class);
            fileIntent.putExtra(FileChooserActivity.CHOOSE_FILE_EXTRA, true);
            startActivityForResult(fileIntent, FILE_CHOOSER_REQUEST_CODE);

        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
                if (exportFilesDirectory.exists()) {
                    Log.d(TAG, "is Directory exists: true");

                } else {
                    Log.d(TAG, "is Directory exists: false");
                    boolean isDirectoryCreated = exportFilesDirectory.mkdir();
                    if (isDirectoryCreated) {
                        Log.d(TAG, "is Directory created: true");

                    } else {
                        Log.d(TAG, "is Directory created: false");
                    }
                }

            } else {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_CHOOSER_REQUEST_CODE) {
            if (resultCode == FileChooserActivity.RESULT_CODE_FILE_SELECTED) {
                assert data != null : "file selection intent data empty";
                String filePath = data.getStringExtra(FileChooserActivity.RESULT_FILE_EXTRA);
                Log.d(TAG, "onActivityResult: " + filePath);
                if (filePath != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Enter a Password");

                    final EditText input = new EditText(this);
                    input.setHint("Password");
                    input.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);
                    builder.setView(input);

                    builder.setPositiveButton("Encrypt", (dialog, which) -> {
                        String password = input.getText().toString().trim();
                        if (password.isEmpty()) {
                            Toast.makeText(this, "password cannot be empty", Toast.LENGTH_SHORT).show();

                        } else {
                            ECSymmetric ecSymmetric = new ECSymmetric();
                            File inputFile = new File(filePath);
                            encryptedFileName = inputFile.getName();
                            ecSymmetric.encrypt(inputFile, password, new ECResultListener() {
                                @Override
                                public void onProgress(int i, long l, long l1) {
                                    runOnUiThread(() -> {
                                        progressBar.setIndeterminate(false);
                                        progressBar.setMax((int) l1);
                                        progressBar.setProgress((int) l);
                                        progressBar.setVisibility(View.VISIBLE);
                                    });
                                }

                                @Override
                                public <T> void onSuccess(T t) {

                                    runOnUiThread(() -> progressBar.setVisibility(View.GONE));

                                    Log.d(TAG, "onSuccess: File Encrypted: " + t.toString());

                                    File encryptedOutputFile = (File) t;

                                    EncryptedFile encryptedFile = new EncryptedFile(encryptedFileName, password, AppUtils.getFileSizeMegaBytes(encryptedOutputFile), new Date());
                                    viewModel.insert(encryptedFile);

                                    // TODO: decrypt button
                                    // TODO: the following files were either deleted or moved or renamed.
                                    FileUtil.moveFileToEncryptedFilesDirectory(encryptedOutputFile, new File(ENCRYPTED_FILE_DIRECTORY + File.separator + encryptedFileName + ".ecrypt"));

                                    boolean isDeleted = inputFile.delete();
                                    if (isDeleted) {
                                        Log.d(TAG, "onSuccess: original file deleted!!!");

                                    } else {
                                        Log.d(TAG, "onSuccess: couldn't delete file");
                                    }
                                }

                                @Override
                                public void onFailure(@NotNull String s, @NotNull Exception e) {
                                    Log.d(TAG, "onFailure: " + s);
                                    Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                                    runOnUiThread(() -> {
                                        progressBar.setVisibility(View.GONE);
                                    });
                                }
                            });
                        }
                    });

                    builder.setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.cancel();
                        Log.d(TAG, "Password Dialog: couldn't encrypt, password not entered");
                    });

                    builder.show();

                } else {
                    Log.d(TAG, "getting the path of file: PATH NULL");
                    Toast.makeText(this, "error accessing the path of file", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
