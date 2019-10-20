package com.codencolors.ciphon.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codencolors.ciphon.R;
import com.codencolors.ciphon.dto.EncryptedFile;
import com.codencolors.ciphon.ui.EncryptedFileViewHolder;
import com.codencolors.ciphon.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class EncryptedFilesAdapter extends RecyclerView.Adapter<EncryptedFileViewHolder> {

    private List<EncryptedFile> filesList;

    public EncryptedFilesAdapter() {
        filesList = new ArrayList<>();
    }

    @NonNull
    @Override
    public EncryptedFileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_encrypted_file, parent, false);
        return new EncryptedFileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EncryptedFileViewHolder holder, int position) {
        EncryptedFile encryptedFile = filesList.get(position);
        holder.setEncryptedFileName(encryptedFile.getName());
        holder.setEncryptedFileSize(encryptedFile.getSize());
        holder.setEncryptionDate(AppUtils.convertDateIntoString(encryptedFile.getCreationDate()));
    }

    @Override
    public int getItemCount() {
        return filesList.size();
    }

    public void setNotes(List<EncryptedFile> filesList) {
        this.filesList = filesList;
        notifyDataSetChanged();
    }
}
