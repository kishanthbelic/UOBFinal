package com.example.kishanthprab.uobfinal;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    MaterialButton btn_SignIn;
    TextView txtV_Register;
    RelativeLayout rootLayout;


    FirebaseAuth FireAuth;
    FirebaseDatabase FireDB;
    FirebaseUser FireUser;
    DatabaseReference users;

    LinearLayout LinearLayout_signin;
    MaterialEditText editT_signIn_Email, editT_signIn_Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);
        btn_SignIn = (MaterialButton) findViewById(R.id.btn_signIn);
        txtV_Register = (TextView) findViewById(R.id.txtV_register);

        LinearLayout_signin = (LinearLayout) findViewById(R.id.LinearLayout_signin);
        editT_signIn_Email = (MaterialEditText) findViewById(R.id.editT_signIn_email);
        editT_signIn_Password = (MaterialEditText) findViewById(R.id.editT_signIn_password);

        LinearLayout_signin.setTranslationY(-1000f);
        LinearLayout_signin.animate().translationYBy(1000f).setDuration(700);


        btn_SignIn.setTranslationY(1000f);
        btn_SignIn.animate().translationYBy(-1000f).setDuration(900);

        txtV_Register.setTranslationY(1000f);
        txtV_Register.animate().translationYBy(-1000f).setDuration(1000);


        //Firebase Initialization
        FireAuth = FirebaseAuth.getInstance();
        FireDB = FirebaseDatabase.getInstance();
        FireUser = FireAuth.getCurrentUser();
        users = FireDB.getReference("users");

        if (FireUser!=null){
            Log.i("tags", "user id : " + FireUser.getUid());
            goTOWelcomeActivity();
        }



        btn_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validateRegisterUser()) {

                    LoginUser();
                }

            }
        });


        txtV_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);


            }
        });


    }


    private boolean validateRegisterUser() {
        if (TextUtils.isEmpty(editT_signIn_Email.getText().toString())) {

            Snackbar.make(rootLayout, "Please enter Email", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(editT_signIn_Password.getText().toString())) {

            Snackbar.make(rootLayout, "Please enter Password", Snackbar.LENGTH_SHORT).show();

            return false;
        }


        return true;
    }


    private void LoginUser() {



        FireAuth.signInWithEmailAndPassword(editT_signIn_Email.getText().toString(), editT_signIn_Password.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        FireUser = FireAuth.getCurrentUser();

                        Log.i("tags", "uid : " + FirebaseAuth.getInstance().getCurrentUser().getUid());
                        Snackbar.make(rootLayout, "Registered successfully!", Snackbar.LENGTH_LONG).show();

                        goTOWelcomeActivity();


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(rootLayout, "creating account Failed! " + e.getMessage(), Snackbar.LENGTH_LONG).show();
                Log.i("tags", "error create account : " + e);
            }
        });


    }

    private void goTOWelcomeActivity() {
        //Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        //startActivity(intent);
    }
}
