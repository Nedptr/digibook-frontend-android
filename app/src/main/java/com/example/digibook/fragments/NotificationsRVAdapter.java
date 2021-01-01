package com.example.digibook.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digibook.R;
import com.example.digibook.models.Notification;

import org.w3c.dom.Text;

import java.util.List;

public class NotificationsRVAdapter extends RecyclerView.Adapter<NotificationsRVAdapter.MyViewHolder> {

    Context context;
    List<Notification> notifData;

    public NotificationsRVAdapter(Context ct, List<Notification> Data) {
        context = ct;
        notifData = Data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.notification_rv_item, parent, false);
        return new NotificationsRVAdapter.MyViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.action.setText(notifData.get(position).getAction());
        holder.name.setText(notifData.get(position).getName());
        holder.post.setText(notifData.get(position).getNotificationid());
        // set image however the fk u like holder.image.setImageBitmap();.......
    }

    @Override
    public int getItemCount() {
        return notifData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name,action,post;
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.notifName);
            action = itemView.findViewById(R.id.notifAction);
            post = itemView.findViewById(R.id.notifpost);
            image = itemView.findViewById(R.id.notifImage);

        }
    }
}
