package com.kpf.sujeet.chat.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kpf.sujeet.chat.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

    public class SplashScreenActivity extends AppCompatActivity {
        FirebaseUser firebaseUser;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash_screen);
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (firebaseUser != null) {
                        startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                        finish();
                    }

                }
            }, 3000);
        }
    }

// for generating hash key
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.kpf.sujeet.chat",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }