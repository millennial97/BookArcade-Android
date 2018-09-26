package in.bookarcade.app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.bookarcade.app.model.Address;

public class AddNewAddressActivity extends AppCompatActivity {

    //Java built-in types
    private static final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
    private static Pattern pattern;
    private Matcher matcher;
    private String name, address1, address2 = "", landmark = "", phone, pincode;

    //Android Widgets
    private TextInputEditText et_name, et_address1, et_address2, et_landmark, et_phone, et_pincode;
    private TextInputLayout til_name, til_address1, til_address2, til_landmark, til_phone, til_pincode;
    private Button btn_save;
    private ProgressBar progressBar;

    //External types
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_address);

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

        progressBar = findViewById(R.id.progress_bar);

        //Buttons
        btn_save = findViewById(R.id.btn_save);

        //TextInputLayouts and TextInputEditTexts
        til_name = findViewById(R.id.til_name);
        til_address1 = findViewById(R.id.til_address1);
        til_address2 = findViewById(R.id.til_address2);
        til_landmark = findViewById(R.id.til_landmark);
        til_phone = findViewById(R.id.til_phone);
        til_pincode = findViewById(R.id.til_pincode);

        et_name = findViewById(R.id.et_name);
        et_address1 = findViewById(R.id.et_address1);
        et_address2 = findViewById(R.id.et_address2);
        et_landmark = findViewById(R.id.et_landmark);
        et_phone = findViewById(R.id.et_phone);
        et_pincode = findViewById(R.id.et_pincode);

        //Listeners
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_save.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                til_name.setErrorEnabled(false);
                til_address1.setErrorEnabled(false);
                til_phone.setErrorEnabled(false);
                til_pincode.setErrorEnabled(false);

                if (validateForm()) {
                    Map<String, Object> addressDocument = new HashMap<>();
                    addressDocument.put("name", name);
                    addressDocument.put("address1", address1);
                    addressDocument.put("address2", address2);
                    addressDocument.put("landmark", landmark);
                    addressDocument.put("phone", phone);
                    addressDocument.put("pincode", pincode);
                    addressDocument.put("city", "Bangalore");
                    addressDocument.put("state", "Karnataka");
                    db.collection("users").document(Objects.requireNonNull(mUser.getEmail())).collection("addresses").add(addressDocument)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if (task.isSuccessful()) {
                                        Intent returnIntent = new Intent();
                                        returnIntent.putExtra("name", name);
                                        returnIntent.putExtra("address1", address1);
                                        returnIntent.putExtra("address2", address2);
                                        returnIntent.putExtra("landmark", landmark);
                                        returnIntent.putExtra("phone", phone);
                                        returnIntent.putExtra("pincode", pincode);
                                        returnIntent.putExtra("city", "Bangalore");
                                        returnIntent.putExtra("state", "Karnataka");

                                        setResult(RESULT_OK, returnIntent);
                                        finish();
                                    } else {
                                        Toast.makeText(AddNewAddressActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                        btn_save.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                }
            }
        });
    }

    private void mainInit() {

    }

    private boolean validateForm() {
        name = et_name.getText().toString();
        address1 = et_address1.getText().toString();
        address2 = et_address2.getText().toString();
        landmark = et_landmark.getText().toString();
        phone = et_phone.getText().toString();
        pincode = et_pincode.getText().toString();

        if (TextUtils.isEmpty(name)) {
            til_name.setError("Name cannot be empty.");
            et_name.requestFocus();
            progressBar.setVisibility(View.INVISIBLE);
            btn_save.setVisibility(View.VISIBLE);
            return false;
        } else if (TextUtils.isEmpty(address1)) {
            til_address1.setError("Address Line 1 is required.");
            et_address1.requestFocus();
            progressBar.setVisibility(View.INVISIBLE);
            btn_save.setVisibility(View.VISIBLE);
            return false;
        } else if (TextUtils.isEmpty(phone)) {
            til_phone.setError("Mobile number is required.");
            et_phone.requestFocus();
            progressBar.setVisibility(View.INVISIBLE);
            btn_save.setVisibility(View.VISIBLE);
            return false;
        } else if (TextUtils.isEmpty(pincode)) {
            til_pincode.setError("Pincode cannot be empty.");
            et_pincode.requestFocus();
            progressBar.setVisibility(View.INVISIBLE);
            btn_save.setVisibility(View.VISIBLE);
            return false;
        }

        if (phone.length() != 10) {
            til_phone.setError("Mobile number should have 10 digits.");
            progressBar.setVisibility(View.INVISIBLE);
            btn_save.setVisibility(View.VISIBLE);
            return false;
        }

        return true;
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
