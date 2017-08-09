package com.kpf.sujeet.chat.Activity;



import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kpf.sujeet.chat.R;



public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtxt_signup_name;
    EditText edtxt_signup_email;
    EditText edtxt_signup_contact_no;
    EditText edtxt_signup_country;
    EditText edtxt_signup_password;
    EditText edtxt_signup_confirm_password;
    Button btn_signup_submit;

    String name;
    String email;
    String contact_no;
    String country;
    String password;
    String confirm_password;
    String submit;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
//    private FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);
        mAuth=FirebaseAuth.getInstance();

        edtxt_signup_name = (EditText)findViewById(R.id.edtxt_signup_name);
        edtxt_signup_email = (EditText)findViewById(R.id.edtxt_signup_email);
        edtxt_signup_contact_no=(EditText)findViewById(R.id.edtxt_signup_contact_no);
        edtxt_signup_country=(EditText)findViewById(R.id.edtxt_signup_country);
        edtxt_signup_password = (EditText)findViewById(R.id.edtxt_signup_password);
        edtxt_signup_confirm_password = (EditText)findViewById(R.id.edtxt_signup_confirm_password);
        btn_signup_submit = (Button)findViewById(R.id.btn_signup_submit);

        btn_signup_submit.setOnClickListener(this);

//        mAuth=FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //Log.d("", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    //Log.d("", "onAuthStateChanged:signed_out");
                }
            }
        };

    }

    public void onStart()
    {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    public void onStop()
    {
        super.onStop();
        if (mAuthListener != null)
        {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }



    @Override
    public void onClick(View view) {

        name=edtxt_signup_name.getText().toString().trim();
        email=edtxt_signup_email.getText().toString().trim();
        contact_no=edtxt_signup_contact_no.getText().toString().trim();
        country=edtxt_signup_country.getText().toString().trim();
        password=edtxt_signup_password.getText().toString().trim();
        confirm_password=edtxt_signup_confirm_password.getText().toString().trim();
        submit=btn_signup_submit.getText().toString().trim();

        switch (view.getId())
        {
            case R.id.btn_signup_submit:
                if(name.equals(""))
                    edtxt_signup_name.setError("Empty field");
                else if(email.equals(""))
                    edtxt_signup_email.setError("Empty field");
                else if(contact_no.equals(""))
                    edtxt_signup_contact_no.setError("Empty field");
                else if(country.equals(""))
                    edtxt_signup_country.setError("Empty field");
                else if(password.equals(""))
                    edtxt_signup_password.setError("Empty field");
                else if(confirm_password.equals(""))
                    edtxt_signup_confirm_password.setError("Empty field");
                else
                    if(!password.equals(confirm_password))
                    Toast.makeText(this,"Password doesn't match",Toast.LENGTH_LONG).show();
                else {
                    createUser(email,password);
                    Toast.makeText(this, "Registering!!!", Toast.LENGTH_LONG).show();
                    }
                break;
        }
    }



    private void createUser(final String email, String password)
    {
       mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task)
                   {
                       if(task.isSuccessful())
                       {
                           Toast.makeText(SignupActivity.this, "Signup successful", Toast.LENGTH_SHORT).show();
                           saveUserDataToDatabase(task.getResult().getUser(),name,email,contact_no,country);
                       }
                       else
                       {
                           Toast.makeText(SignupActivity.this, "Signup not successful", Toast.LENGTH_SHORT).show();
                       }

                   }
               }
       );

    }


    public void saveUserDataToDatabase(FirebaseUser firebaseUser,final String name, final String email,final String contact_no,final String country) {

        String uid = firebaseUser.getUid();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();//initialization
        final DatabaseReference databaseReference = firebaseDatabase.getReference().child("users").child(uid);

        databaseReference.child("name").setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    databaseReference.child("email").setValue(email);
                    databaseReference.child("contact_no").setValue(contact_no);
                    databaseReference.child("country").setValue(country);
                    Toast.makeText(SignupActivity.this, "Data Inserted after signup", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                } else
                    Toast.makeText(SignupActivity.this, "Data insertion failed after signup", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}





