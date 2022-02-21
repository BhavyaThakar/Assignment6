package com.example.assignment5_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    EditText nameEt, phoneEt, mailEt,addressEt, hobeyEt, passwordEt, confirmPasswordEt, percentageStd1Et, percentageStd2Et, percentageDegreeEt;
    TextView dobTv;
    Spinner countriesNames, schoolSpinner1, schoolSpinner2, collegeSpinner2;
    RadioGroup radioGroup;
    RadioButton radioButton;
    private DatePickerDialog.OnDateSetListener setListener;
    private static final String TAG = "RegisterActivity";
    String date;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        Toast.makeText(RegisterActivity.this, getString(R.string.register_from_here), Toast.LENGTH_LONG).show();

        nameEt = findViewById(R.id.nameEt);
        phoneEt = findViewById(R.id.phoneEt);
        mailEt = findViewById(R.id.mailEt);
        addressEt = findViewById(R.id.addressEt);
        hobeyEt = findViewById(R.id.hobeyEt);
        countriesNames = findViewById(R.id.countriesNames);
        schoolSpinner1 = findViewById(R.id.schoolSpinner1);
        schoolSpinner2 = findViewById(R.id.schoolSpinner2);
        collegeSpinner2 = findViewById(R.id.collegeSpinner2);
        passwordEt = findViewById(R.id.passwordEt);
        confirmPasswordEt = findViewById(R.id.confirmPassword);
        percentageStd1Et = findViewById(R.id.percentage1);
        percentageStd2Et = findViewById(R.id.percentage2);
        percentageDegreeEt = findViewById(R.id.collegePercentage2);

        ArrayAdapter<CharSequence> adapterForCountries = ArrayAdapter.createFromResource(this, R.array.countries, android.R.layout.simple_spinner_item);
        adapterForCountries.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countriesNames.setAdapter(adapterForCountries);

        ArrayAdapter<CharSequence> adapterForSchool1 = ArrayAdapter.createFromResource(this, R.array.schoolArray, android.R.layout.simple_spinner_item);
        adapterForSchool1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        schoolSpinner1.setAdapter(adapterForSchool1);

        ArrayAdapter<CharSequence> adapterForSchool2 = ArrayAdapter.createFromResource(this, R.array.schoolArray, android.R.layout.simple_spinner_item);
        adapterForSchool2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        schoolSpinner2.setAdapter(adapterForSchool2);

        ArrayAdapter<CharSequence> adapterForCollage2 = ArrayAdapter.createFromResource(this, R.array.collegeArray, android.R.layout.simple_spinner_item);
        adapterForCollage2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        collegeSpinner2.setAdapter(adapterForCollage2);

        dobTv = findViewById(R.id.dobTv);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        dobTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        RegisterActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                date = year+ " / "+month+" / "+day;
                dobTv.setText(date);
            }
        };

        Button register = findViewById(R.id.button_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioGroup = findViewById(R.id.radioGroup);
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);


                String userName = nameEt.getText().toString();
                String userPhone = phoneEt.getText().toString();
                String userMail = mailEt.getText().toString();
                String userAddress = addressEt.getText().toString();
                String userHobey = hobeyEt.getText().toString();
                String userCountry = countriesNames.getSelectedItem().toString();
                String userStd1 = schoolSpinner1.getSelectedItem().toString();
                String userStd2 = schoolSpinner2.getSelectedItem().toString();
                String userDegree = collegeSpinner2.getSelectedItem().toString();
                String userDob = dobTv.getText().toString();
                String userGender = radioButton.getText().toString();
                String userPassword = passwordEt.getText().toString();
                String userConfirmPassword = confirmPasswordEt.getText().toString();
                String userPerStd1 = percentageStd1Et.getText().toString();
                String userPerStd2 = percentageStd2Et.getText().toString();
                String userPerDegree = percentageDegreeEt.getText().toString();

                if (TextUtils.isEmpty(userName)){
                    Toast.makeText(RegisterActivity.this, getString(R.string.enter_name), Toast.LENGTH_LONG).show();
                    nameEt.setError(getString(R.string.full_name_required));
                    nameEt.requestFocus();
                }
                else if (TextUtils.isEmpty(userPhone)){
                    Toast.makeText(RegisterActivity.this, getString(R.string.pls_enter_number), Toast.LENGTH_LONG).show();
                    phoneEt.setError(getString(R.string.number_is_required));
                    phoneEt.requestFocus();
                }
                else if (userPhone.length() != 10){
                    Toast.makeText(RegisterActivity.this, getString(R.string.reenter_number), Toast.LENGTH_SHORT).show();
                    phoneEt.setError(getString(R.string.number_should_be_10));
                    phoneEt.requestFocus();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(userMail).matches()){
                    Toast.makeText(RegisterActivity.this, getString(R.string.reenter_email), Toast.LENGTH_LONG).show();
                    mailEt.setError(getString(R.string.valid_email_required));
                    mailEt.requestFocus();
                }

                else if (TextUtils.isEmpty(userAddress)){
                    Toast.makeText(RegisterActivity.this, getString(R.string.enter_address), Toast.LENGTH_LONG).show();
                    addressEt.setError(getString(R.string.address_required));
                    addressEt.requestFocus();
                }
                else if (TextUtils.isEmpty(userHobey)){
                    Toast.makeText(RegisterActivity.this, getString(R.string.enter_hobbies), Toast.LENGTH_LONG).show();
                    addressEt.setError(getString(R.string.hobbies_required));
                    addressEt.requestFocus();
                }
                else if (TextUtils.isEmpty(userDob)){
                    Toast.makeText(RegisterActivity.this, getString(R.string.enter_dob), Toast.LENGTH_LONG).show();
                    dobTv.setError(getString(R.string.dob_required));
                    dobTv.requestFocus();
                }
                else if (TextUtils.isEmpty(userPassword)){
                    Toast.makeText(RegisterActivity.this, getString(R.string.pls_enter_password), Toast.LENGTH_SHORT).show();
                    passwordEt.setError(getString(R.string.password_required));
                    passwordEt.requestFocus();

                }
                else if (TextUtils.isEmpty(userConfirmPassword)){
                    Toast.makeText(RegisterActivity.this, getString(R.string.pls_confirm_password), Toast.LENGTH_SHORT).show();
                    confirmPasswordEt.setError(getString(R.string.confirm_password_required));
                    confirmPasswordEt.requestFocus();

                }
                else if (!userPassword.equals(userConfirmPassword)){
                    Toast.makeText(RegisterActivity.this, getString(R.string.enter_same_password), Toast.LENGTH_SHORT).show();
                    confirmPasswordEt.setError(getString(R.string.confirm_password_required));
                    confirmPasswordEt.requestFocus();

                    passwordEt.clearComposingText();
                    confirmPasswordEt.clearComposingText();

                }
                else if (radioGroup.getCheckedRadioButtonId() == -1){
                    Toast.makeText(RegisterActivity.this, getString(R.string.select_gender), Toast.LENGTH_SHORT).show();
                    radioButton.setError(getString(R.string.gender_required));
                    radioButton.requestFocus();
                }

                else userGender = radioButton.getText().toString();
                progressBar = findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
                registerUser(userName, userPhone, userMail, userCountry, userAddress, userGender, userDob, userHobey, userStd1, userStd2, userDegree, userPerStd1, userPerStd2, userPerDegree, userPassword);

            }
        });

    }

    public  void checkedRadioButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        Toast.makeText(this, getString(R.string.selected_gender) + radioButton.getText(), Toast.LENGTH_SHORT).show();

    }

    private void registerUser(String userName, String userPhone, String userMail, String userCountry, String userAddress, String userGender,String userDob,String userHobey,String userStd1,String userStd2,String userDegree,String userPerStd1,String userPerStd2,String userPerDegree, String userPassword) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(userMail,userPassword).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser firebaseUser = auth.getCurrentUser();




                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(userName, userPhone, userMail, userCountry, userAddress, userGender, userDob, userHobey, userStd1, userStd2, userDegree, userPerStd1, userPerStd2, userPerDegree, userPassword);
                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");

                    referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){

                                firebaseUser.sendEmailVerification();
                                Toast.makeText(RegisterActivity.this, getString(R.string.registration_successful), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, UserProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                                   | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, getString(R.string.registration_failed), Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);

                        }
                    });
                }
                else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        passwordEt.setError(getString(R.string.weak_password));
                        passwordEt.requestFocus();
                    }
                    catch (FirebaseAuthInvalidCredentialsException e) {
                        mailEt.setError(getString(R.string.email_already_in_use));
                        mailEt.requestFocus();
                    }
                    catch (FirebaseAuthUserCollisionException e) {
                        mailEt.setError(getString(R.string.existing_email));
                        mailEt.requestFocus();
                    }

                    catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                    progressBar.setVisibility(View.GONE);

                }

            }
        });
    }
}