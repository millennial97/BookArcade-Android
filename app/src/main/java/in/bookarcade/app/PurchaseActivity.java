package in.bookarcade.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import in.bookarcade.app.orders.PurchaseOrderDetailsActivity;
import in.bookarcade.app.utils.UniversalImageLoader;

public class PurchaseActivity extends AppCompatActivity {

    //Java built-in types
    private static final int ADD_ADDRESS = 1;
    private static final int CHANGE_ADDRESS = 2;
    private double total, cart_total, shipping, discount, mrp, price;
    private String name, address1, address2, city = "Bangalore", pincode, landmark, phone, state = "Karnataka";
    private String book_id, title, author, publisher, image_url;

    //Android widgets
    private Button btn_add, btn_change, btn_proceed;
    private TextView tv_eta, tv_cart_total, tv_shipping, tv_discount, tv_total, tv_mrp;
    private TextView tv_name, tv_address1, tv_address2, tv_landmark, tv_phone, tv_city, tv_pincode;
    private TextView tv_book_title, tv_book_author, tv_book_publisher;
    private ImageView img_book;
    private Intent intent;
    private CardView addressLayout;
    private ProgressBar progressBar;
    private RelativeLayout mainLayout;

    //External types
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        preInit();
        initViews();
        mainInit();
    }

    private void preInit() {
        ActionBar actionBar = this.getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_up);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        intent = getIntent();
        actionBar.setTitle("Buy " + intent.getStringExtra("title"));
        cart_total = intent.getDoubleExtra("price", 0.0);
        mrp = intent.getDoubleExtra("mrp", 0.0);
        mrp = intent.getDoubleExtra("price", 0.0);
        book_id = intent.getStringExtra("book_id");
        title = intent.getStringExtra("title");
        author = intent.getStringExtra("author");
        publisher = intent.getStringExtra("publisher");
        image_url = intent.getStringExtra("image_url");
    }

    private void initViews() {

        progressBar = findViewById(R.id.progress_bar);

        //Buttons
        btn_add = findViewById(R.id.btn_add);
        btn_change = findViewById(R.id.btn_change);
        btn_proceed = findViewById(R.id.btn_proceed);

        //Layouts
        addressLayout = findViewById(R.id.layout_address);
        mainLayout = findViewById(R.id.mainLayout);

        //TextViews
        tv_eta = findViewById(R.id.tv_eta);
        tv_mrp = findViewById(R.id.tv_mrp);
        tv_cart_total = findViewById(R.id.tv_cart_total);
        tv_shipping = findViewById(R.id.tv_shipping);
        tv_discount = findViewById(R.id.tv_discount);
        tv_total = findViewById(R.id.tv_total);
        tv_name = findViewById(R.id.tv_display_name);
        tv_address1 = findViewById(R.id.tv_address1);
        tv_address2 = findViewById(R.id.tv_address2);
        tv_landmark = findViewById(R.id.tv_landmark);
        tv_phone = findViewById(R.id.tv_phone);
        tv_pincode = findViewById(R.id.tv_pincode);
        tv_city = findViewById(R.id.tv_city);
        tv_book_title = findViewById(R.id.tv_book_title);
        tv_book_author = findViewById(R.id.tv_book_author);
        tv_book_publisher = findViewById(R.id.tv_book_publisher);

        //ImageViews
        img_book = findViewById(R.id.img_book);

        //Listeners
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(PurchaseActivity.this, AddNewAddressActivity.class), ADD_ADDRESS);
            }
        });

        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(PurchaseActivity.this, SelectAddressActivity.class), CHANGE_ADDRESS);
            }
        });

        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PurchaseActivity.this);
                builder.setTitle("Confirm Order Details");
                builder.setMessage("Order total: " + getString(R.string.rupee_symbol) + total);
                builder.setNegativeButton("Cancel", null);
                builder.setPositiveButton("Place Order", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

//                        OrderAddress order_address = new OrderAddress(name, address1, address2, landmark, phone, pincode, city, state);
                        Map<String, String> order_address = new HashMap<>();
                        order_address.put("name", name);
                        order_address.put("address1", address1);
                        order_address.put("address2", address2);
                        order_address.put("landmark", landmark);
                        order_address.put("phone", phone);
                        order_address.put("pincode", pincode);
                        order_address.put("city", city);
                        order_address.put("state", state);

                        final String order_id = new java.sql.Timestamp(System.currentTimeMillis()).getTime() + "_" + mUser.getUid();

                        Map<String, Object> order = new HashMap<>();
                        order.put("email", mUser.getEmail());
                        order.put("uid", mUser.getUid());
                        order.put("status", "PENDING");
                        order.put("payment_method", "COD");
                        order.put("shipping_address", order_address);
                        order.put("shipping_charges", shipping);
                        order.put("order_total", total);
                        order.put("ordered_at", Timestamp.now());
                        order.put("order_id", order_id);

                        final Map<String, Object> book = new HashMap<>();
                        book.put("book_id", book_id);
                        book.put("title", title);
                        book.put("author", author);
                        book.put("publisher", publisher);
                        book.put("m_image_url", image_url);
                        book.put("quantity", 1);
                        book.put("mrp", mrp);
                        book.put("price", price);

                        db.collection("buy_orders").document(order_id).set(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                db.collection("buy_orders").document(order_id).collection("books").document(book_id).set(book)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Intent i = new Intent(PurchaseActivity.this, PurchaseOrderDetailsActivity.class);
                                                i.putExtra("order_id", order_id);
                                                startActivity(i);
                                                finish();
                                            }
                                        });

                            }
                        });

                    }
                });
                builder.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_ADDRESS) {
            if (resultCode == RESULT_OK) {

                tv_name.setVisibility(View.VISIBLE);
                tv_address2.setVisibility(View.VISIBLE);
                tv_address1.setVisibility(View.VISIBLE);
                tv_city.setVisibility(View.VISIBLE);
                tv_phone.setVisibility(View.VISIBLE);
                tv_pincode.setVisibility(View.VISIBLE);
                tv_landmark.setVisibility(View.VISIBLE);
                btn_change.setVisibility(View.VISIBLE);

                name = data.getStringExtra("name");
                address1 = data.getStringExtra("address1");
                address2 = data.getStringExtra("address2");
                city = data.getStringExtra("city");
                phone = data.getStringExtra("phone");
                pincode = data.getStringExtra("pincode");
                landmark = data.getStringExtra("landmark");

                tv_name.setText(name);
                tv_address1.setText(address1);

                if (!TextUtils.isEmpty(address2))
                    tv_address2.setText(address2);
                else
                    tv_address2.setVisibility(View.GONE);

                tv_city.setText(city);
                tv_pincode.setText(pincode);
                tv_phone.setText("+91 " + phone);

                if (!TextUtils.isEmpty(landmark))
                    tv_landmark.setText("Landmark: " + landmark);
                else
                    tv_landmark.setVisibility(View.GONE);
            }
        } else if (requestCode == CHANGE_ADDRESS) {
            if (resultCode == RESULT_OK) {

                tv_name.setVisibility(View.VISIBLE);
                tv_address2.setVisibility(View.VISIBLE);
                tv_address1.setVisibility(View.VISIBLE);
                tv_city.setVisibility(View.VISIBLE);
                tv_phone.setVisibility(View.VISIBLE);
                tv_pincode.setVisibility(View.VISIBLE);
                tv_landmark.setVisibility(View.VISIBLE);
                btn_change.setVisibility(View.VISIBLE);

                name = data.getStringExtra("name");
                address1 = data.getStringExtra("address1");
                address2 = data.getStringExtra("address2");
                city = data.getStringExtra("city");
                phone = data.getStringExtra("phone");
                pincode = data.getStringExtra("pincode");
                landmark = data.getStringExtra("landmark");

                tv_name.setText(name);
                tv_address1.setText(address1);

                if (!TextUtils.isEmpty(address2))
                    tv_address2.setText(address2);
                else
                    tv_address2.setVisibility(View.GONE);

                tv_city.setText(city);
                tv_pincode.setText(pincode);
                tv_phone.setText("+91 " + phone);

                if (!TextUtils.isEmpty(landmark))
                    tv_landmark.setText("Landmark: " + landmark);
                else
                    tv_landmark.setVisibility(View.GONE);
            }
        }
    }

    private void mainInit() {

        tv_book_title.setText(title);
        tv_book_author.setText(author);
        tv_book_publisher.setText(publisher);
        tv_mrp.setText(getString(R.string.rupee_symbol) + mrp);
        UniversalImageLoader.setImage(image_url, img_book, null);

        db.collection("android_v1_0_0").document("values").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Map<String, Object> values = task.getResult().getData();
                if (values != null) {
                    tv_eta.setText(values.get("eta").toString());
                    shipping = Double.parseDouble(values.get("shipping_charges").toString());
                    tv_shipping.setText(getString(R.string.rupee_symbol) + shipping);

                    total = cart_total + shipping;
                    tv_total.setText(getString(R.string.rupee_symbol) + total);
                }
            }
        });

        db.collection("users").document(Objects.requireNonNull(mUser.getEmail())).collection("addresses").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> documents = task.getResult().getDocuments();
                if (documents.size() > 0) {
                    Map<String, Object> address = documents.get(0).getData();
                    if (address != null) {
                        name = address.get("name").toString();
                        address1 = address.get("address1").toString();
                        address2 = address.get("address2").toString();
                        city = address.get("city").toString();
                        pincode = address.get("pincode").toString();
                        phone = address.get("phone").toString();
                        landmark = address.get("landmark").toString();
                    }

                    tv_name.setText(name);
                    tv_address1.setText(address1);

                    if (!TextUtils.isEmpty(address2))
                        tv_address2.setText(address2);
                    else
                        tv_address2.setVisibility(View.GONE);

                    tv_city.setText(city);
                    tv_pincode.setText(pincode);
                    tv_phone.setText("+91 " + phone);

                    if (!TextUtils.isEmpty(landmark))
                        tv_landmark.setText("Landmark: " + landmark);
                    else
                        tv_landmark.setVisibility(View.GONE);
                } else {
                    btn_change.setVisibility(View.INVISIBLE);
                    tv_name.setVisibility(View.INVISIBLE);
                    tv_city.setVisibility(View.INVISIBLE);
                    tv_pincode.setVisibility(View.INVISIBLE);
                    tv_landmark.setVisibility(View.INVISIBLE);
                    tv_phone.setVisibility(View.INVISIBLE);
                    tv_address1.setVisibility(View.INVISIBLE);
                    tv_address2.setText("Click below to add a new address.");
                }

                mainLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });

        discount = mrp - cart_total;
        tv_cart_total.setText(getString(R.string.rupee_symbol) + cart_total);
        tv_discount.setText(getString(R.string.rupee_symbol) + discount);
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
