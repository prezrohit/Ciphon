package com.codencolors.ciphon.ui;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codencolors.ciphon.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EncryptedFileViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.lbl_encrypted_file_name)
    TextView lblEncryptedFileName;

    @BindView(R.id.lbl_encryption_date)
    TextView lblEncryptionDate;

    @BindView(R.id.lbl_encrypted_file_size)
    TextView lblEncryptionFileSize;

    @BindView(R.id.btn_decrypt_file)
    Button btnDecryptFile;

    public EncryptedFileViewHolder(@NonNull View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void setEncryptedFileName(String fileName) {
        lblEncryptedFileName.setText(fileName);
    }

    public void setEncryptionDate(String fileDate) {
        lblEncryptionDate.setText(fileDate);
    }

    public void setEncryptedFileSize(String size) {
        lblEncryptionFileSize.setText(size);
    }

    public void setButtonClickListener(View.OnClickListener clickListener) {
        btnDecryptFile.setOnClickListener(clickListener);
    }
}
