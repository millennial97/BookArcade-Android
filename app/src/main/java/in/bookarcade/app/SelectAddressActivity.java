package in.bookarcade.app;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import in.bookarcade.app.adapter.AddressAdapter;
import in.bookarcade.app.adapter.SelectAddressAdapter;
import in.bookarcade.app.model.Address;

public class SelectAddressActivity extends AppCompatActivity {

    //Java built-in types
    private List<Address> addresses;

    //Android Widgets
    private RecyclerView rv_addresses;
    private SelectAddressAdapter addressAdapter;

    //External types
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);

        preInit();
        initViews();
        mainInit();
    }

    private void preInit() {
        ActionBar actionBar = this.getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_up);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();

        addresses = new ArrayList<>();
        addressAdapter = new SelectAddressAdapter(this);
    }

    private void initViews() {

        rv_addresses = findViewById(R.id.rv_addresses);
        rv_addresses.setAdapter(addressAdapter);
    }

    private void mainInit() {
        db.collection("users").document(mUser.getEmail()).collection("addresses").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                    for (DocumentSnapshot document : documents) {
                        Map<String, Object> address = document.getData();
                        if (address != null) {
                            addresses.add(new Address(document.getId(), address.get("name").toString(), address.get("address1").toString(), address.get("address2").toString(),
                                    address.get("landmark").toString(), address.get("phone").toString(), address.get("pincode").toString(), address.get("city").toString(),
                                    address.get("state").toString()));
                        }
                    }
                    addressAdapter.setAddresses(addresses);
                }
            }
        });
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
