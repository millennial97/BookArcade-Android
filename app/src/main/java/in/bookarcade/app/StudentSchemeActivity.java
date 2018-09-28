package in.bookarcade.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.Objects;

import in.bookarcade.app.student.ExistingStudentFragment;
import in.bookarcade.app.student.NewStudentFragment;
import in.bookarcade.app.student.RegisteredStudentFragment;

public class StudentSchemeActivity extends AppCompatActivity {

    //Java built-in types


    //Android widgets
    private FrameLayout container;
    private Fragment fragment;
    private ProgressBar progressBar;

    //External types
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_scheme);

        preInit();
        initViews();
        mainInit();
    }

    private void preInit() {
        ActionBar actionBar = this.getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_up);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
    }

    private void initViews() {
        container = findViewById(R.id.container);
        progressBar = findViewById(R.id.progress_bar);

        db.collection("students").document(Objects.requireNonNull(mUser.getEmail())).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    Map<String, Object> student = task.getResult().getData();
                    if (Objects.requireNonNull(student).get("status").toString().equals("REGISTERED")) {
                        fragment = new RegisteredStudentFragment();
                    } else {
                        fragment = new ExistingStudentFragment();
                    }
                } else {
                    fragment = new NewStudentFragment();
                }

                progressBar.setVisibility(View.GONE);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.container, fragment);
                ft.commit();
            }
        });


    }

    private void mainInit() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
