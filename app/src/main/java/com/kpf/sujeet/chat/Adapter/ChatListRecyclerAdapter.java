package com.kpf.sujeet.chat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.kpf.sujeet.chat.Activity.ChatActivity;
import com.kpf.sujeet.chat.Models.Chat;
import com.kpf.sujeet.chat.Models.User;
import com.kpf.sujeet.chat.R;
import com.kpf.sujeet.chat.Utils.AppController;

import java.util.List;

/**
 * Created by SUJEET on 1/7/2017.
 */

public class ChatListRecyclerAdapter extends RecyclerView.Adapter<ChatListRecyclerAdapter.MyViewHolder> {

    ImageLoader imageLoader;
    Context context;
    List<Chat> chatList;
    public ChatListRecyclerAdapter(Context contex, List<Chat> chatList){
        this.context = contex;
        this.chatList=chatList;
        imageLoader = AppController.getInstance().getImageLoader();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        NetworkImageView network_chat_image;
        TextView txt_chat_name;
        TextView textview_message;

        public MyViewHolder(View itemView) {
            super(itemView);
            network_chat_image = (NetworkImageView)itemView.findViewById(R.id.network_chat_image);
            txt_chat_name = (TextView)itemView.findViewById(R.id.txt_chat_user_name);
            textview_message=(TextView)itemView.findViewById(R.id.textview_message);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            Chat chat = chatList.get(position);
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("other_user_uid",chat.author);
            intent.putExtra("name",chat.name);
            context.startActivity(intent);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Chat chat = chatList.get(position);

        if(chatList.get(position).photo_url.equals("")) {
            holder.network_chat_image.setDefaultImageResId(R.drawable.defaultimg);
        }else{
            holder.network_chat_image.setImageUrl(chatList.get(position).photo_url, imageLoader);
        }
        holder.txt_chat_name.setText(chat.name);
        holder.textview_message.setText(chat.message);
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

}
