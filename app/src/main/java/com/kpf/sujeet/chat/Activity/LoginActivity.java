package com.kpf.sujeet.chat.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kpf.sujeet.chat.R;

import java.util.Arrays;

import static android.R.attr.country;
import static android.R.attr.data;
import static android.R.attr.name;
import static android.R.attr.theme;

/**
 * Created by user pc on 1/2/2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int GOOGLE_SIGN_IN =9001 ;
    private static final int FB_SIGN_IN =64206 ;

    //login page fields
    EditText edt_login_username;
    EditText edt_login_password;
    Button btn_login_button;
    Button btn_signup_button;
    TextView txtv_forgotpassword_textview;
    Button btn_sign_in_facebook;
    SignInButton sign_in_button; //predefined google button

    String login_email,login_password;

    private CallbackManager mCallbackManager;
    ProgressDialog progressDialog;

    FirebaseAuth mauth;
    FirebaseAuth.AuthStateListener mAuthListener;

    //google
    GoogleApiClient mGoogleApiClient;

    //fb data retrieve create local variable.(Similar to saveUserDataToDatabase of normal SignUp)




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //facebook initialization
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        mCallbackManager=CallbackManager.Factory.create();
        if(AccessToken.getCurrentAccessToken()!=null){
            LoginManager.getInstance().logOut();
        }



        setContentView(R.layout.activity_login);

        //firebase
        mauth=FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };


        //login page fields
        edt_login_username = (EditText) findViewById(R.id.edt_login_username);
        edt_login_password = (EditText) findViewById(R.id.edt_login_password);
        btn_login_button = (Button) findViewById(R.id.btn_login_button);
        btn_signup_button = (Button) findViewById(R.id.btn_signup_button);
        txtv_forgotpassword_textview = (TextView) findViewById(R.id.txtv_forgotpassword_textview);
        btn_sign_in_facebook = (Button) findViewById(R.id.btn_sign_in_facebook);
        sign_in_button=(SignInButton)findViewById(R.id.sign_in_button);  //predefined google button

        btn_login_button.setOnClickListener(this);
        btn_signup_button.setOnClickListener(this);
        txtv_forgotpassword_textview.setOnClickListener(this);
        btn_sign_in_facebook.setOnClickListener(this);
        sign_in_button.setOnClickListener(this);  //predefined google button

        //google initialization
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Processing...");




    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onClick(View view) {
        login_email=edt_login_username.getText().toString().trim();
        login_password=edt_login_password.getText().toString().trim();
        switch (view.getId())
        {
            case R.id.btn_login_button:
                Toast.makeText(this, "normal login", Toast.LENGTH_SHORT).show();
                if(login_email.equals("") & login_password.equals(""))
                    Toast.makeText(this, "First enter both fields", Toast.LENGTH_SHORT).show();
                else
                    progressDialog.show();
                    normalLogin();

                break;
            case R.id.btn_signup_button:
                progressDialog.show();
                startActivity(new Intent(this,SignupActivity.class));
                Toast.makeText(this, "normal signup", Toast.LENGTH_SHORT).show();

                break;
            case R.id.btn_sign_in_facebook:
                Toast.makeText(this, "fb signin", Toast.LENGTH_SHORT).show();
                progressDialog.show();
                fbLogin();
                break;
            case R.id.sign_in_button:
                Toast.makeText(this, "google signin", Toast.LENGTH_SHORT).show();
                progressDialog.show();
                signIn();
                break;

        }

    }


    //normal login
    public void normalLogin(){
        mauth.signInWithEmailAndPassword(login_email, login_password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("normal_login", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("normal_login", "signInWithEmail:failed", task.getException());
                            progressDialog.dismiss();//dismiss on task failure
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Successful login",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                            progressDialog.dismiss();//dismiss on task completion
                            finish();
                        }

                    }
                });

    }


    //facebook method
    public void fbLogin() {
        // Initialize Facebook Login button
//        mCallbackManager = CallbackManager.Factory.create();
//        LoginButton loginButton = (LoginButton) findViewById(R.id.button_facebook_login);
//        loginButton.setReadPermissions("email", "public_profile");
//        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email"));
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                //  Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                //Log.d(TAG, "facebook:onCancel");
                Toast.makeText(LoginActivity.this, "User cancels", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onError(FacebookException error) {
                //Log.d(TAG, "facebook:onError", error);
                Log.d("error:", error.toString());
                Toast.makeText(LoginActivity.this, "User gets an error", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }

        //[Authenticating with facebook]
        private void handleFacebookAccessToken(AccessToken token) {
        Log.d("Reached_handler", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Reached_handler", "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("Reached_handler", "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                        else
                        {

                            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();//initialization
                            final DatabaseReference databaseReference =firebaseDatabase.getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            databaseReference.child("name").setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                  if(task.isSuccessful()){
                                      databaseReference.child("email").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());
//                                      databaseReference.child("contact_no").setValue("");
//                                      databaseReference.child("country").setValue("");
                                  }
                                }
                            });

                            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                            Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            finish();
                        }

                    }
                });
    }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Differentiate fb and google on the basis of requestCode
        if(requestCode ==GOOGLE_SIGN_IN) {
            //for google
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
            else if(requestCode==FB_SIGN_IN){
            //for fb
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
        }





    //GOOGLE INTEGRATION
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == GOOGLE_SIGN_IN) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            handleSignInResult(result);
//        }
//    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("google handled", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            firebaseAuthWithGoogle(acct);
//            mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
//            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }
    }



    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("Google auth", "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        //showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("google auth", "signInWithCredential:onComplete:" + task.isSuccessful());
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        final DatabaseReference databaseReference = firebaseDatabase.getReference().child("users").child(uid);

                        databaseReference.child("name").setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    //databaseReference.child("Contact").setValue(contact_no);
                                    //databaseReference.child("photoUrl").setValue(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl());
                                    databaseReference.child("email").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                    task.getResult();
                                    Toast.makeText(LoginActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
//                                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                                } else {
                                    Toast.makeText(LoginActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        if (!task.isSuccessful()) {
//                            Log.v();
                            Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Successfully login", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                            finish();
                        }
                    }
                });
    }




    





    //google mGoogleApiClient implements this method
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



}
