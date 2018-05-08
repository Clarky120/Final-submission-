package com.example.liam.opendayfinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressBar pbar;
    EditText editusername, editpword;
    private FirebaseAuth mAuth;

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitysignup);

        editusername = findViewById(R.id.editTextEmail);
        editpword = findViewById(R.id.editTextPassword);
        pbar = findViewById(R.id.progressbar);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        findViewById(R.id.buttonSignUp).setOnClickListener(this);
        findViewById(R.id.textViewLogin).setOnClickListener(this);

    }

    private void  RegisternewUser()
    {
        final String username = editusername.getText().toString().trim();
        String pword = editpword.getText().toString().trim();

        if (username.isEmpty())
        {
            editusername.setError("Please enter a Email");
            editusername.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(username).matches())
        {
            editusername.setError("Email is not valid!");
            editusername.requestFocus();
            return;
        }
        if(pword.isEmpty())
        {
            editpword.setError("Please enter a Password");
            editpword.requestFocus();
            return;
        }
        if (pword.length() < 6)
        {
            editpword.setError("Passwords must be at least 6 letters/numbers long");
            editpword.requestFocus();
            return;
        }
        pbar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(username, pword).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pbar.setVisibility(View.GONE);
                        if (task.isSuccessful())
                        {
                            final DatabaseReference adduser = mDatabase;
                            String datauser = username.replace(".",",");

                            Map test = new HashMap();
                            test.put("username", username);
                            test.put("location", "");
                            test.put("admin", "false");
                            adduser.child(datauser).setValue(test);

                            final DatabaseReference tsign = FirebaseDatabase.getInstance().getReference("usernames");
                            tsign.child(datauser).setValue(username);



                            Toast.makeText(getApplicationContext(), "User Signed Up!",Toast.LENGTH_SHORT).show();
                            Intent mainmenu = new Intent(SignUpActivity.this, MenuActivity.class);
                            mainmenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(mainmenu);
                        }
                        else
                        {
                            if(task.getException() instanceof FirebaseAuthUserCollisionException)
                            {
                                Toast.makeText(getApplicationContext(), "This Email is already registered!", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });


    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case  R.id.buttonSignUp:
            RegisternewUser();
            break;

            case R.id.textViewLogin:

                startActivity(new Intent(this, MainActivity.class));

                break;
        }
    }

}
