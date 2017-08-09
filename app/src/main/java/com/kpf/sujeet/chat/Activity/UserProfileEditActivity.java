package com.kpf.sujeet.chat.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kpf.sujeet.chat.R;

import java.util.Iterator;

public class UserProfileEditActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtxt_update_name;
    EditText edtxt_update_email;
    EditText edtxt_update_contact_no;
    EditText edtxt_update_country;
    Button btn_update_submit;
    String name, email, contact_no, country;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_edit);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        edtxt_update_name = (EditText) findViewById(R.id.edtxt_update_name);
        edtxt_update_email = (EditText) findViewById(R.id.edtxt_update_email);
        edtxt_update_contact_no = (EditText) findViewById(R.id.edtxt_update_contact_no);
        edtxt_update_country = (EditText) findViewById(R.id.edtxt_update_country);
        btn_update_submit = (Button) findViewById(R.id.btn_update_submit);

        btn_update_submit.setOnClickListener(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Query query = databaseReference.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(UserProfileEditActivity.this, ""+dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()) {
                    DataSnapshot dataSnapshot1 = (DataSnapshot) iterator.next();
                    if (dataSnapshot1.getKey().equals("name")) {
                        edtxt_update_name.setText(dataSnapshot1.getValue().toString());
                    }
                    if (dataSnapshot1.getKey().equals("email")) {
                        edtxt_update_email.setText(dataSnapshot1.getValue().toString());
                    }
                    if (dataSnapshot1.getKey().equals("contact_no")) {
                        edtxt_update_contact_no.setText(dataSnapshot1.getValue().toString());
                    }
                    if (dataSnapshot1.getKey().equals("country")) {
                        edtxt_update_country.setText(dataSnapshot1.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        edtxt_update_email.setEnabled(false);
    }


    public void onClick(View view) {
        name = edtxt_update_name.getText().toString().trim();
        email = edtxt_update_email.getText().toString().trim();
        contact_no = edtxt_update_contact_no.getText().toString().trim();
        country = edtxt_update_country.getText().toString().trim();

        switch (view.getId()) {
            case R.id.btn_update_submit:
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());

                if (!name.equals("")) {
                    databaseReference.child("name").setValue(name);
                }
                if (!email.equals("")) {
                    databaseReference.child("email").setValue(email);
                }
                if (!contact_no.equals("")) {
                    databaseReference.child("contact_no").setValue(contact_no);
                }
                if (!country.equals("")) {
                    databaseReference.child("country").setValue(country);

                    Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                    finish();
                    break;

                }
        }
    }
}



