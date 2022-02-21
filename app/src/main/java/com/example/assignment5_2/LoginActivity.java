package com.example.assignment5_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmailEt, loginPasswordEt;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;
    public final static String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();
        loginEmailEt = findViewById(R.id.loginEmailEt);
        loginPasswordEt = findViewById(R.id.loginPasswordEt);
        progressBar = findViewById(R.id.progressBar2);

        authProfile = FirebaseAuth.getInstance();

        ImageView showHidePwdImage = findViewById(R.id.showHidePwdImage);
        showHidePwdImage.setImageResource(R.drawable.ic_baseline_visibility_24);
        showHidePwdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginPasswordEt.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    loginPasswordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showHidePwdImage.setImageResource(R.drawable.ic_baseline_visibility_24);
                }
                else {
                    loginPasswordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showHidePwdImage.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                }
                }


        });

        Button buttonLogin = findViewById(R.id.login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = loginEmailEt.getText().toString();
                String userPassword = loginPasswordEt.getText().toString();

                if (TextUtils.isEmpty(userEmail)){
                    Toast.makeText(LoginActivity.this, getString(R.string.enter_email), Toast.LENGTH_SHORT).show();
                    loginEmailEt.setError(getString(R.string.email_required));
                    loginEmailEt.requestFocus();
                }

                else if (TextUtils.isEmpty(userPassword)){
                    Toast.makeText(LoginActivity.this, getString(R.string.enter_password), Toast.LENGTH_SHORT).show();
                    loginPasswordEt.setError(getString(R.string.password_required));
                    loginPasswordEt.requestFocus();
            }else {
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser(userEmail, userPassword);
                }

            }
        });

    }

    private void loginUser(String email, String password) {
        authProfile.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    FirebaseUser firebaseUser = authProfile.getCurrentUser();
                    if (firebaseUser.isEmailVerified()){
                        Toast.makeText(LoginActivity.this, getString(R.string.loggedin), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, UserProfileActivity.class));
                        finish();

                    }else {
                        firebaseUser.sendEmailVerification();
                        authProfile.signOut();
                        showAlertDialog();
                    }
                }

                else {
                    Toast.makeText(LoginActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                    try {
                        throw task.getException();

                    } catch (FirebaseAuthInvalidUserException e) {
                        loginEmailEt.setError(getString(R.string.user_doesnt_exist));
                        loginEmailEt.requestFocus();
                    }catch(FirebaseAuthInvalidCredentialsException e){
                        loginEmailEt.setError(getString(R.string.invalid_credential));
                        loginEmailEt.requestFocus();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle(R.string.email_not_verified);
        builder.setMessage(R.string.email_verification_required);
                builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (authProfile.getCurrentUser() != null){
            Toast.makeText(LoginActivity.this, getString(R.string.already_loggedin), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, UserProfileActivity.class));
            finish();
        }
        else {
            Toast.makeText(LoginActivity.this, getString(R.string.can_login), Toast.LENGTH_SHORT).show();
        }
    }
}