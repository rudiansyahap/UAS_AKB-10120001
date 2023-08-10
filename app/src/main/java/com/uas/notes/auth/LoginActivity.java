/**
 * NIM : 10120001
 * NAMA : Rudiansyah Adi Prasetyo
 * KELAS : IF-1
 */
package com.uas.notes.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uas.notes.Config;
import com.uas.notes.MainActivity;
import com.uas.notes.R;
import com.uas.notes.helper.DBHelper;
import com.uas.notes.helper.StringHelper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference DB;
    private FirebaseAuth Auth;
    private EditText fEmail;
    private EditText fPassword;
    private Button bSignin;
    private Button bSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Create instance firebase
        DB = FirebaseDatabase.getInstance(Config.getDB_URL()).getReference();
        Auth = FirebaseAuth.getInstance();

        // Set component
        fEmail = findViewById(R.id.login_email);
        fPassword = findViewById(R.id.login_password);
        bSignin = findViewById(R.id.login_submit_btn);
        bSignup = findViewById(R.id.login_signup_btn);

        // Btn on click action
        bSignup.setOnClickListener(this);
        bSignin.setOnClickListener(this);

        // Check if user is logged in
        if (Auth.getCurrentUser() != null) {
            Toast.makeText(LoginActivity.this, "Already logged in",
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    // On click action override
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.login_submit_btn) {
            signIn();
        } else if (i == R.id.login_signup_btn) {
            signUp();
        }
    }

    // Text Input Vallidation
    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(fEmail.getText().toString())) {
            fEmail.setError("Required");
            result = false;
        } else {
            fEmail.setError(null);
        }

        if (TextUtils.isEmpty(fPassword.getText().toString())) {
            fPassword.setError("Required");
            result = false;
        } else {
            fPassword.setError(null);
        }

        return result;
    }


    // Sign In action
    private void signIn() {
        if (!validateForm()) return;

        String email = fEmail.getText().toString();
        String password = fPassword.getText().toString();

        Auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        onAuthSuccess(task.getResult().getUser());
                    } else {
                        Toast.makeText(LoginActivity.this, "Sign In Failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Sign Up action
    private void signUp() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    // Auth Success
    private void onAuthSuccess(FirebaseUser user) {
        String name = StringHelper.usernameFromEmail(user.getEmail());

        // Create User If Not Exist
        DBHelper.saveUser(DB, user.getUid(), name, user.getEmail());

        // Make alert
        Toast.makeText(LoginActivity.this, "Sign In Success !",
                Toast.LENGTH_SHORT).show();

        // Move to Main Activity
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }


}
