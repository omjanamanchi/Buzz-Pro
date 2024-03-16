package com.example.buzzpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccountActivity extends AppCompatActivity
{
    EditText email_edit_text, password_edit_text, confirm_password_edit_text;
    Button create_account_button;
    ProgressBar progressBar;
    TextView login_text_view_button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        email_edit_text = findViewById(R.id.email_edit_text);
        password_edit_text = findViewById(R.id.password_edit_text);
        confirm_password_edit_text = findViewById(R.id.confirm_password_edit_text);
        create_account_button = findViewById(R.id.create_account_button);
        progressBar = findViewById(R.id.progress_bar);
        login_text_view_button = findViewById(R.id.login_text_view_button);


        create_account_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

        login_text_view_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    void createAccount()
    {
        String email = email_edit_text.getText().toString();
        String password = password_edit_text.getText().toString();
        String confirmPassword = confirm_password_edit_text.getText().toString();

        boolean isValidated = validateData(email, password, confirmPassword);
        if(!isValidated) { return; }

        createAccountInFirebase(email, password);
    }

    void createAccountInFirebase(String email, String password)
    {
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if(task.isSuccessful())
                {
                    Utility.showToast(CreateAccountActivity.this, "Successfully create account, Check email to verify");
                    firebaseAuth.signOut();
                    finish();
                }
                else
                {
                    Utility.showToast(CreateAccountActivity.this, task.getException().getLocalizedMessage());
                }
            }
        });

    }

    void changeInProgress(boolean inProgress)
    {
        if(inProgress)
        {
            progressBar.setVisibility(View.VISIBLE);
            create_account_button.setVisibility(View.GONE);
        }
        else
        {
            progressBar.setVisibility(View.GONE);
            create_account_button.setVisibility(View.VISIBLE);
        }

    }


    boolean validateData(String email, String password, String confirmPassword)
    {
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            email_edit_text.setError("Email is invalid");
            return false;
        }

        if(password.length() < 6)
        {
            password_edit_text.setError("Password length is invalid");
            return false;
        }

        if(!password.equals(confirmPassword))
        {
            confirm_password_edit_text.setError("Password not matched");
            return false;
        }
        return true;
    }


}

