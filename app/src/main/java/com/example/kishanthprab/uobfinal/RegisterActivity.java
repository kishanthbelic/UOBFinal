package com.example.kishanthprab.uobfinal;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

public class RegisterActivity extends AppCompatActivity {

    RelativeLayout rootLayout;
    MaterialButton btn_SignUp;
    TextView txtV_signIn;
    MaterialEditText editT_Register_Email,editT_Register_Password,editT_Register_CPassword,editT_Register_Name;

    FirebaseAuth FireAuth;
    FirebaseDatabase FireDB;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        rootLayout = (RelativeLayout)findViewById(R.id.rootLayout);
        btn_SignUp = (MaterialButton)findViewById(R.id.btn_signUp);
        txtV_signIn =(TextView)findViewById(R.id.txtV_signIn);

        //editText
        editT_Register_Email = (MaterialEditText)findViewById(R.id.editT_Register_email);
        editT_Register_Password = (MaterialEditText)findViewById(R.id.editT_Register_password);
        editT_Register_CPassword = (MaterialEditText)findViewById(R.id.editT_Register_CPassword);
        editT_Register_Name = (MaterialEditText)findViewById(R.id.editT_Register_name);

        //ui animation
        animateFrontUI();


        //Firebase Initialization
        FireAuth = FirebaseAuth.getInstance();
        FireDB = FirebaseDatabase.getInstance();

        users = FireDB.getReference("users");



        btn_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validateRegisterUser()) {

                    RegisterNewUser();
                }


            }
        });

        txtV_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                goToLogin();

            }
        });

    }



    private void goToLogin() {

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);

    }


    private boolean validateRegisterUser() {
        if (TextUtils.isEmpty(editT_Register_Email.getText().toString())){

            Snackbar.make(rootLayout,"Please enter Email",Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(editT_Register_Password.getText().toString())){

            Snackbar.make(rootLayout,"Please enter Password",Snackbar.LENGTH_SHORT).show();

            return false;
        }
        if (TextUtils.isEmpty(editT_Register_Name.getText().toString())){

            Snackbar.make(rootLayout,"Please enter name",Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if (!editT_Register_Password.getText().toString().equals(editT_Register_CPassword.getText().toString())){

            Snackbar.make(rootLayout,"Password not matching",Snackbar.LENGTH_SHORT).show();
            editT_Register_Password.setText("");
            editT_Register_CPassword.setText("");
            return false ;
        }
        if (editT_Register_Password.getText().toString().length() < 6){

            Snackbar.make(rootLayout,"Password should be atleast 6 characters long",Snackbar.LENGTH_SHORT).show();
            editT_Register_Password.setText("");
            editT_Register_CPassword.setText("");
            return false;
        }
        return true;
    }


    private void RegisterNewUser() {

        FireAuth.createUserWithEmailAndPassword(editT_Register_Email.getText().toString(),editT_Register_Password.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        user newUser = new user(editT_Register_Email.getText().toString(),
                                editT_Register_Password.getText().toString(),
                                editT_Register_Name.getText().toString()
                        );


                        Log.i("tags","uid : "+ FirebaseAuth.getInstance().getCurrentUser().getUid());

                        users.child(FireAuth.getCurrentUser().getUid())
                                .setValue(newUser)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Snackbar.make(rootLayout,"Registered successfully!",Snackbar.LENGTH_LONG).show();
                                        goToLogin();
                                    }

                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(rootLayout,"Error DB "+e.getMessage(),Snackbar.LENGTH_LONG).show();
                                Log.i("tags","error DB : "+e);
                            }
                        });



                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(rootLayout,"creating account Failed! "+e.getMessage(),Snackbar.LENGTH_LONG).show();
                Log.i("tags","error create account : "+e);
            }
        });




    }

    private void animateFrontUI() {
        editT_Register_Email.setTranslationY(-1000f);
        editT_Register_Password.setTranslationY(-1000f);
        editT_Register_CPassword.setTranslationY(-1000f);
        editT_Register_Name.setTranslationY(-1000f);

        btn_SignUp.setTranslationY(1000f);
        txtV_signIn.setTranslationY(1000f);

        editT_Register_Email.animate().translationYBy(1000f).setDuration(600);
        editT_Register_Name.animate().translationYBy(1000f).setDuration(700);
        editT_Register_Password.animate().translationYBy(1000f).setDuration(800);
        editT_Register_CPassword.animate().translationYBy(1000f).setDuration(900);

        btn_SignUp.animate().translationYBy(-1000f).setDuration(800);
        txtV_signIn.animate().translationYBy(-1000f).setDuration(1000);
    }


}
