package com.example.liam.opendayfinal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.security.KeyStore;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    //defining objects
    private static FirebaseAuth firebaseAuth;
    EditText editusername, editpword;
    ProgressBar pbar;
    private static FirebaseAuth.AuthStateListener mAuthListener;

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Initialise
        firebaseAuth = FirebaseAuth.getInstance();

        //Check If user is logged in
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                if (firebaseAuth.getCurrentUser() != null)
                {
                    Toast.makeText(MainActivity.this, "Now you are logged In as " + firebaseAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        };


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.textViewSignup).setOnClickListener(this);
        findViewById(R.id.buttonLogin).setOnClickListener(this);
        pbar = (ProgressBar)findViewById(R.id.progressbar);
        editusername = (EditText)findViewById(R.id.editTextEmail);
        editpword = (EditText)findViewById(R.id.editTextPassword);

        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    //Logs in the user
    private void Userlogin()
    {
        {
            String username = editusername.getText().toString().trim();
            String pword = editpword.getText().toString().trim();

            if (username.isEmpty()) {
                editusername.setError("Please enter a Email");
                editusername.requestFocus();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
                editusername.setError("Email is not valid!");
                editusername.requestFocus();
                return;
            }
            if (pword.isEmpty()) {
                editpword.setError("Please enter a Password");
                editpword.requestFocus();
                return;
            }
            if (pword.length() < 6) {
                editpword.setError("Passwords must be at least 6 letters/numbers long");
                editpword.requestFocus();
                return;
            }
            pbar.setVisibility(View.VISIBLE);

            firebaseAuth.signInWithEmailAndPassword(username, pword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    pbar.setVisibility(View.GONE);
                    if (task.isSuccessful())
                    {
                        Intent mainmenu = new Intent(MainActivity.this, MenuActivity.class);
                        mainmenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainmenu);

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    //Onclick for the buttons
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.textViewSignup:

            startActivity(new Intent(this, SignUpActivity.class));

            break;

            case R.id.buttonLogin:

                Userlogin();

                break;


        }
    }
}


