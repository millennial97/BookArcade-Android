package in.bookarcade.app.auth;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import in.bookarcade.app.HomeActivity;
import in.bookarcade.app.R;

public class LoginActivity extends AppCompatActivity {

    // Java built-in types


    // Android widgets
    private Button btn_login;
    private Button facebookLogin;
    private Button btn_signup;
    private TextInputEditText email;
    private TextInputEditText password;
    private ProgressBar progressBar;
    private TextView tv_resetPassword;
    private RelativeLayout layout;

    // External types
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preInit();
        initViews();
        mainInit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
    }

    private void preInit() {
        mAuth = FirebaseAuth.getInstance();

    }

    private void mainInit() {
        initializeFacebookLogin();
    }

    private void initViews() {
        layout = findViewById(R.id.activity_login);

        // Buttons
        btn_signup = findViewById(R.id.btn_register);
        btn_login = findViewById(R.id.btn_login);
        facebookLogin = findViewById(R.id.btn_facebook);

        // ProgressBar
        progressBar = findViewById(R.id.progress_bar);

        // Text Widgets
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        tv_resetPassword = findViewById(R.id.tv_resetPassword);

        // Listeners
        tv_resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_id = email.getText().toString();
                if (email_id.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(layout, "Enter a valid email address", Snackbar.LENGTH_SHORT);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.WHITE);
                    TextView textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                    snackbar.show();
                } else {
                    Toast.makeText(LoginActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_login.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                login();
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                i.putExtra(Intent.EXTRA_TEXT, "register");
                startActivity(i);
            }
        });
    }

    private void login() {
        try {
            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                progressBar.setVisibility(View.GONE);
                                mUser = mAuth.getCurrentUser();
                                if (mUser != null) {
                                    mUser.reload();
                                    if (mUser.isEmailVerified()) {
                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Please verify your email to continue", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            progressBar.setVisibility(View.INVISIBLE);
                            btn_login.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBar.setVisibility(View.VISIBLE);
        } catch (IllegalArgumentException e) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBar.setVisibility(View.INVISIBLE);
            btn_login.setVisibility(View.VISIBLE);
            Toast.makeText(LoginActivity.this, "Missing credentials", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Facebook Authentication Handler
     */
    private void initializeFacebookLogin() {
        loginButton = new LoginButton(this);
        loginButton.setReadPermissions(Arrays.asList("public_profile,email"));

        callbackManager = CallbackManager.Factory.create();

        facebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.performClick();
            }
        });

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Callback: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            boolean newUser = task.getResult().getAdditionalUserInfo().isNewUser();
                            mUser = mAuth.getCurrentUser();
                            if (newUser && mUser != null) {
                                String email = mUser.getEmail();
                                String firstName = mUser.getDisplayName().split(" ")[0];
                                String lastName = mUser.getDisplayName().split(" ")[1];
                                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                                i.putExtra(Intent.EXTRA_EMAIL, email);
                                i.putExtra(Intent.EXTRA_TEXT, "facebook");
                                i.putExtra("first_name", firstName);
                                i.putExtra("last_name", lastName);
                                startActivity(i);
                                finish();
                            } else {
                                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(i);
                                finish();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
