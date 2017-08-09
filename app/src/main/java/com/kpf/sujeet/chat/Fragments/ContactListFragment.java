package com.kpf.sujeet.chat.Fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kpf.sujeet.chat.Adapter.ContactListRecyclerAdapter;
import com.kpf.sujeet.chat.Models.User;
import com.kpf.sujeet.chat.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactListFragment extends Fragment {


    RecyclerView recyclerview;

    List<User> userList;
    List<User> phoneContactList;
    List<User> filterUserList;

    public ContactListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        phoneContactList = new ArrayList<User>();
        userList = new ArrayList<User>();
        filterUserList = new ArrayList<User>();
        // Inflate the layout for this fragment
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
        phoneContactList.clear();
        filterUserList.clear();
        Cursor cursor = getActivity().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, "upper("+ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+") ASC");
        //Cursor cursor= getActivity().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
        if(cursor.moveToFirst()) {

            do {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? ", new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String contactName = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                        User user = new User();
                        user.name = contactName + " -> " + contactNumber;
                        user.mobno = contactNumber;
                        phoneContactList.add(user);
                        break;

                    }
                    pCur.close();

                }

            } while (cursor.moveToNext());
        }


        Query query= FirebaseDatabase.getInstance().getReference();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator iterator=dataSnapshot.getChildren().iterator();

                while (iterator.hasNext()){

                    DataSnapshot dataSnapshot2=(DataSnapshot)iterator.next();
                    if (dataSnapshot2.getKey().equals("users")){
                        Iterator iterator1 = dataSnapshot2.getChildren().iterator();
                        while (iterator1.hasNext()){
                            DataSnapshot dataSnapshot3 = (DataSnapshot) iterator1.next();
                            Iterator iterator2 = dataSnapshot3.getChildren().iterator();
                            User user=new User();
                            user.uid = dataSnapshot3.getKey();
                            while (iterator2.hasNext()){
                                DataSnapshot  snapshot = (DataSnapshot) iterator2.next();
                                if (snapshot.getKey().equals("name")){
                                    user.name = snapshot.getValue().toString();
                                }
                                if (snapshot.getKey().equals("photoUrl")){
                                    user.photoUrl = snapshot.getValue().toString();
                                }
                                if(snapshot.getKey().equals("email")){
                                    user.email = snapshot.getValue().toString();
                                }
                                if(snapshot.getKey().equals("contact_no")){
                                    user.mobno = snapshot.getValue().toString();
                                }
                                if(snapshot.getKey().equals("country")){
                                    user.country = snapshot.getValue().toString();
                                }
                            }
                            userList.add(user);
                        }
                        Log.d("out_of_onstart", userList.toString()+" "+phoneContactList.toString());

                        for (User firebaseuser:userList){
                            for(User contactuser:phoneContactList) {

                                String contact_no = contactuser.mobno;
                                contact_no = contact_no.replaceAll(" ", "");
                                if (contact_no.length() >= 10) {
                                    contact_no = contact_no.substring(contact_no.length() - 10);
                                    if (firebaseuser.mobno.equals(contact_no) && !firebaseuser.mobno.equals("")) {
                                        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(firebaseuser.uid)){

                                        }else{
                                            filterUserList.add(firebaseuser);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                        ContactListRecyclerAdapter chatListRecyclerAdapter = new ContactListRecyclerAdapter(getActivity(), filterUserList);
                        recyclerview.setAdapter(chatListRecyclerAdapter);

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

}











