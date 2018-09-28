package in.bookarcade.app.auth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import in.bookarcade.app.R;

public class SignupActivity extends AppCompatActivity {

    // Java built-in types
    private String email;
    private String first_name;
    private String last_name;
    private String password;

    // Android widgets
    private EditText firstName;
    private EditText lastName;
    private EditText emailId;
    private EditText password1, password2;
    private ProgressBar progressBar;
    private Button sign_up;
    private Intent i;
    private ActionBar actionBar;

    // External types
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        preInit();
        initViews();
        mainInit();
    }

    private void preInit() {
        actionBar = this.getSupportActionBar();
        if (actionBar != null)
            actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        i = getIntent();
    }

    private void initViews() {
        // ProgressBar
        progressBar = findViewById(R.id.progress_bar);

        // Text Widgets
        firstName = findViewById(R.id.firstname);
        lastName = findViewById(R.id.lastname);
        emailId = findViewById(R.id.email);
        password1 = findViewById(R.id.password);
        password2 = findViewById(R.id.password2);

        // Buttons
        sign_up = findViewById(R.id.btn_signup);

        // Listeners
        if (i.getStringExtra(Intent.EXTRA_TEXT).equals("facebook")) {
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setHomeButtonEnabled(false);
                actionBar.setDisplayShowHomeEnabled(false);
            }


            fillFields();
            sign_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        sign_up.setVisibility(View.INVISIBLE);
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBar.setVisibility(View.VISIBLE);
                        if (i.getStringExtra(Intent.EXTRA_TEXT).equals("facebook")) {
                            password = password1.getText().toString();
                            AuthCredential authCredential = EmailAuthProvider.getCredential(email, password);
                            if (mAuth.getCurrentUser() != null)
                                mAuth.getCurrentUser().linkWithCredential(authCredential)
                                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                    mUser = mAuth.getCurrentUser();
                                                    mUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(SignupActivity.this, "Verification mail sent.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                                    Map<String, Object> newUser = new HashMap<>();
                                                    newUser.put("uid", mUser.getUid());
                                                    newUser.put("dob", "NA");
                                                    newUser.put("cart", "NA");
                                                    newUser.put("wallet", 0.0D);
                                                    newUser.put("wishlist", "NA");
                                                    newUser.put("email", email);
                                                    newUser.put("first_name", first_name);
                                                    newUser.put("last_name", last_name);
                                                    newUser.put("gender", "NA");
                                                    newUser.put("order_limit", "NA");
                                                    newUser.put("order_price_limit", "NA");
                                                    newUser.put("role", "GENERAL");
                                                    newUser.put("delivery_address", "NA");
                                                    newUser.put("subscription_plan", "NA");
                                                    newUser.put("subscription_start_date", "NA");
                                                    newUser.put("subscription_end_date", "NA");
                                                    db.collection("users").document(email).set(newUser)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    progressBar.setVisibility(View.GONE);

                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                                                                    builder.setTitle("Welcome to BookBird")
                                                                            .setMessage("Verify your email to proceed")
                                                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                                    if (mAuth.getCurrentUser() != null) {
                                                                                        mAuth.signOut();
                                                                                        LoginManager.getInstance().logOut();
                                                                                    }
                                                                                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                                                                    finish();
                                                                                }
                                                                            }).show();
                                                                }
                                                            });
                                                }
                                            }
                                        });

                        }
                    } catch (IllegalArgumentException e) {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignupActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if (i.getStringExtra(Intent.EXTRA_TEXT).equals("register")) {
            sign_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        sign_up.setVisibility(View.INVISIBLE);
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBar.setVisibility(View.VISIBLE);
                        password = password1.getText().toString();
                        email = emailId.getText().toString();
                        first_name = firstName.getText().toString();
                        last_name = lastName.getText().toString();

                        if ((TextUtils.isEmpty(first_name) || TextUtils.isEmpty(last_name))) {
                            throw new NullPointerException("Missing name fields");
                        } else if (!password.equals(password2.getText().toString())) {
                            throw new NullPointerException("Passwords do not match");
                        }

                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(first_name + " " + last_name)
                                                    .build();

                                            if (user != null) {
                                                user.updateProfile(profileUpdates)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                                Toast.makeText(SignupActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                                                progressBar.setVisibility(View.GONE);
                                                            }
                                                        });

                                                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(SignupActivity.this, "Verification mail sent.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            Map<String, Object> newUser = new HashMap<>();
                                            newUser.put("dob", "NA");
                                            newUser.put("cart", "NA");
                                            newUser.put("wallet", 0.0D);
                                            newUser.put("wishlist", "NA");
                                            newUser.put("email", email);
                                            newUser.put("first_name", first_name);
                                            newUser.put("last_name", last_name);
                                            newUser.put("gender", "NA");
                                            newUser.put("order_limit", "NA");
                                            newUser.put("order_price_limit", "NA");
                                            newUser.put("role", "GENERAL");
                                            newUser.put("shipping_address", "NA");
                                            newUser.put("subscription_plan", "NA");
                                            newUser.put("subscription_start_date", "NA");
                                            newUser.put("subscription_end_date", "NA");
                                            db.collection("users").document(email).set(newUser);

                                            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                                            builder.setTitle("Welcome to BookBird")
                                                    .setMessage("Verify your email to proceed")
                                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            finish();
                                                        }
                                                    }).show();
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SignupActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                    } catch (IllegalArgumentException e) {
                        Toast.makeText(SignupActivity.this, "Missing necessary fields", Toast.LENGTH_SHORT).show();
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBar.setVisibility(View.GONE);
                        sign_up.setVisibility(View.VISIBLE);
                    } catch (NullPointerException e) {
                        Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBar.setVisibility(View.GONE);
                        sign_up.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    private void fillFields() {
        try {
            email = i.getStringExtra(Intent.EXTRA_EMAIL);
            emailId.setText(email);
            first_name = i.getStringExtra("first_name");
            firstName.setText(first_name);
            last_name = i.getStringExtra("last_name");
            lastName.setText(last_name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mainInit() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            super.onBackPressed();
        }
        return false;
    }
}
