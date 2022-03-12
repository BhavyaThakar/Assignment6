package com.example.assignment5_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {
    private TextView userNameTv, userContactTv, userMailTv,
            userCountryTv, userAddressTv, userGenderTv,
            userDobTv, userHobbiesTv, userStd1Tv, userStd1PerTv,
            userStd2Tv, userStd2PerTv, userDegreeTv, userDegreePerTv;

    String  userName, userContact, userMail,
            userCountry, userAddress, userGender,
            userDob, userHobbies, userStd1, userStd1Per,
            userStd2, userStd2Per, userDegree, userDegreePer;

    private ImageView userProfile;
    private FirebaseAuth authProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pforile);

//        getSupportActionBar().hide();

                userNameTv = findViewById(R.id.userNameTv);
                userContactTv = findViewById(R.id.userContactTv);
                userMailTv = findViewById(R.id.userMailTv);
                userCountryTv = findViewById(R.id.userCountryTv);
                userAddressTv = findViewById(R.id.userAddressTv);
                userGenderTv = findViewById(R.id.userGenderTv);
                userDobTv = findViewById(R.id.userDobTv);
                userHobbiesTv = findViewById(R.id.userHobiesTv);
                userStd1Tv = findViewById(R.id.userStd1Tv);
                userStd1PerTv = findViewById(R.id.userStd1PerTv);
                userStd2Tv = findViewById(R.id.userStd2Tv);
                userStd2PerTv = findViewById(R.id.userStd2PerTv);
                userDegreeTv = findViewById(R.id.userDegreeTv);
                userDegreePerTv = findViewById(R.id.userDegreePerTv);
                authProfile = FirebaseAuth.getInstance();

        FirebaseUser firebaseUser = authProfile.getCurrentUser();
                
                if (firebaseUser == null){
                    Toast.makeText(UserProfileActivity.this, getString(R.string.user_details_unavailable), Toast.LENGTH_SHORT).show();

                }
                else{
                    checkIfEmailVerified(firebaseUser);
                    showUserProfile(firebaseUser);
                }

        Button button =findViewById(R.id.button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        authProfile.getInstance().signOut();
                        Toast.makeText(UserProfileActivity.this, getString(R.string.user_signed_out), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

    }

    private void checkIfEmailVerified(FirebaseUser firebaseUser) {
        if (!firebaseUser.isEmailVerified()){
        }
    }



    private void showUserProfile(FirebaseUser firebaseUser) {
        String userId = firebaseUser.getUid();
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if(readUserDetails != null){
                    userName = readUserDetails.userName;
                    userContact = readUserDetails.userPhone;
                    userMail = readUserDetails.userMail;
                    userCountry = readUserDetails.userCountry;
                    userAddress = readUserDetails.userAddress;
                    userGender = readUserDetails.userGender;
                    userDob = readUserDetails.userDob;
                    userHobbies = readUserDetails.userHobey;
                    userStd1 = readUserDetails.userStd1;
                    userStd1Per = readUserDetails.userPerStd1;
                    userStd2 = readUserDetails.userStd2;
                    userStd2Per = readUserDetails.userPerStd2;
                    userDegree = readUserDetails.userDegree;
                    userDegreePer = readUserDetails.userPerDegree;


                    userNameTv.setText(userName);
                    userContactTv.setText(userContact);
                    userMailTv.setText(userMail);
                    userCountryTv.setText(userCountry);
                    userAddressTv.setText(userAddress);
                    userGenderTv.setText(userGender);
                    userDobTv.setText(userDob);
                    userHobbiesTv.setText(userHobbies);
                    userStd1Tv.setText(userStd1);
                    userStd1PerTv.setText(userStd1Per);
                    userStd2Tv.setText(userStd2);
                    userStd2PerTv.setText(userStd2Per);
                    userDegreeTv.setText(userDegree);
                    userDegreePerTv.setText(userDegreePer);




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = R.id.menu_refresh;
        if (id == R.id.menu_refresh){
            startActivity(getIntent());
            finish();
        }





        
        else {
            Toast.makeText(UserProfileActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

        }

        return super.onOptionsItemSelected(item);
    }
}