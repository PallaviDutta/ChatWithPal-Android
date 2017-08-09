package com.kpf.sujeet.chat.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kpf.sujeet.chat.Adapter.ChatListRecyclerAdapter;
import com.kpf.sujeet.chat.Adapter.ContactListRecyclerAdapter;
import com.kpf.sujeet.chat.Models.Chat;
import com.kpf.sujeet.chat.Models.User;
import com.kpf.sujeet.chat.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 */
public class ChatListFragment extends Fragment {
    RecyclerView recyclerview;
    List<User> userList;
    List<Chat> chatRoomList;


    public ChatListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        userList = new ArrayList<User>();
        chatRoomList = new ArrayList<Chat>();

        recyclerview = (RecyclerView) inflater.inflate(R.layout.fragment_chatlist, container, false);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        return recyclerview;
    }




    @Override
    public void onStart() {
        super.onStart();
        userList.clear();
        chatRoomList.clear();

        Query query= FirebaseDatabase.getInstance().getReference();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator iterator=dataSnapshot.getChildren().iterator();

                while (iterator.hasNext()){

                    DataSnapshot chatSnapshot=(DataSnapshot)iterator.next();
                    if(chatSnapshot.getKey().equals("chat")){
                        Iterator chatRoomIterator = chatSnapshot.getChildren().iterator();
                        while (chatRoomIterator.hasNext()){
                            DataSnapshot chatRoomSnapshot = (DataSnapshot)chatRoomIterator.next();

                            Chat chat = new Chat();

                            String str = chatRoomSnapshot.getKey().toString();
                            String[] separated = str.split(":");
                            String user_id_1 = separated[0];
                            String user_id_2 = separated[1];
                            if(user_id_1.equals(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())){

                                chat.author = user_id_2;

                                Iterator chatIterator = chatRoomSnapshot.getChildren().iterator();
                                while (chatIterator.hasNext()){
                                    DataSnapshot snapshot = (DataSnapshot)chatIterator.next();

                                    if(user_id_2.equals(snapshot.child("author").getValue().toString())){
                                        chat.message = snapshot.child("message").getValue().toString();
                                    }else{
                                        chat.message = snapshot.child("message").getValue().toString();
                                    }
                                }
                                chatRoomList.add(chat);
                            }else if (user_id_2.equals(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())){
                                chat.author = user_id_1;
                                Iterator chatIterator = chatRoomSnapshot.getChildren().iterator();
                                while (chatIterator.hasNext()){
                                    DataSnapshot snapshot = (DataSnapshot)chatIterator.next();

                                    if(user_id_2.equals(snapshot.child("author").getValue().toString())){
                                        chat.message = snapshot.child("message").getValue().toString();
                                    }else{
                                        chat.message = snapshot.child("message").getValue().toString();
                                    }
                                }
                                chatRoomList.add(chat);
                            }
                        }

                    }
                    if (chatSnapshot.getKey().equals("users")){
                        Iterator iterator1 = chatSnapshot.getChildren().iterator();
                        while (iterator1.hasNext()){
                            DataSnapshot dataSnapshot2 = (DataSnapshot) iterator1.next();
                            Iterator iterator2 = dataSnapshot2.getChildren().iterator();
                            User user=new User();
                            user.uid = dataSnapshot2.getKey();
                            while (iterator2.hasNext()){
                                DataSnapshot  dataSnapshot3 = (DataSnapshot) iterator2.next();
                                if (dataSnapshot3.getKey().equals("name")){
                                    user.name = dataSnapshot3.getValue().toString();
                                }
                                if (dataSnapshot3.getKey().equals("photoUrl")){
                                    user.photoUrl = dataSnapshot3.getValue().toString();
                                }
                                if(dataSnapshot3.getKey().equals("email")){
                                    user.email = dataSnapshot3.getValue().toString();
                                }
                                if(dataSnapshot3.getKey().equals("contact_no")){
                                    user.mobno = dataSnapshot3.getValue().toString();
                                }
                                if(dataSnapshot3.getKey().equals("country")){
                                    user.country = dataSnapshot3.getValue().toString();
                                }
                            }
                            userList.add(user);
                        }

                    }
                }

                for(User user : userList){

                    for (Chat chat : chatRoomList){
                        if(chat.author.equals(user.uid)){
                            chat.name = user.name;
                            chat.photo_url = user.photoUrl;
                        }

                    }
                }

                ChatListRecyclerAdapter chatListRecyclerAdapter = new ChatListRecyclerAdapter(getActivity(),chatRoomList);
                recyclerview.setAdapter(chatListRecyclerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}





