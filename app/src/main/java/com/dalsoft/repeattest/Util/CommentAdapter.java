package com.dalsoft.repeattest.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dalsoft.repeattest.R;
import com.dalsoft.repeattest.Util.Dto.Comments;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Comments> commentList;

    public CommentAdapter(ArrayList<Comments> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.recycler_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comments comments = commentList.get(position);

        holder.textView_postComment.setText(String.format(context.getResources().getString(R.string.post_comment), comments.getPostId(), comments.getId()));
        holder.textView_name.setText(String.format(context.getResources().getString(R.string.writer), comments.getName()));
        holder.textView_email.setText(String.format(context.getResources().getString(R.string.email), comments.getEmail()));
    }

    @Override
    public int getItemCount() {
        if (commentList != null) {
            return commentList.size();
        }

        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView_postComment;
        TextView textView_name;
        TextView textView_email;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView_postComment = itemView.findViewById(R.id.textView_postComment);
            textView_name        = itemView.findViewById(R.id.textView_name);
            textView_email       = itemView.findViewById(R.id.textView_email);
        }
    }
}
