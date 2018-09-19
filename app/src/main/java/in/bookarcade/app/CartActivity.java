package in.bookarcade.app;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import in.bookarcade.app.adapter.CartBookAdapter;
import in.bookarcade.app.model.CartBook;

public class CartActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    //Java built-in types
    double cart_total = 0.0;

    //Android widgets
    private RecyclerView rv_books;
    private CartBookAdapter bookAdapter;
    private List<CartBook> books;
    private TextView tv_cart_empty, tv_head_total, tv_total;
    private ProgressBar progressBar;
    private RelativeLayout mainLayout;
    private SwipeRefreshLayout refreshLayout;

    //External types
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

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

        books = new ArrayList<>();
        bookAdapter = new CartBookAdapter(this);
    }

    private void initViews() {
        rv_books = findViewById(R.id.rv_books);
        progressBar = findViewById(R.id.progress_bar);
        mainLayout = findViewById(R.id.mainLayout);
        refreshLayout = findViewById(R.id.refreshLayout);

        //TextViews
        tv_cart_empty = findViewById(R.id.tv_cart_empty);
        tv_head_total = findViewById(R.id.tv_head_total);
        tv_total = findViewById(R.id.tv_total);

        rv_books.setAdapter(bookAdapter);
    }

    private void mainInit() {
        db.collection("users").document(Objects.requireNonNull(mUser.getEmail())).collection("cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> documents = task.getResult().getDocuments();

                if (documents.size() != 0) {
                    tv_cart_empty.setVisibility(View.GONE);
                    for (DocumentSnapshot document : documents) {
                        Map<String, Object> book = document.getData();
                        cart_total += Double.parseDouble(book.get("price").toString());
                        books.add(new CartBook(Objects.requireNonNull(book).get("title").toString(), document.getId(), book.get("s_image_url").toString(), book.get("author").toString(),
                                Double.parseDouble(book.get("mrp").toString()), Double.parseDouble(book.get("price").toString())));
                    }

                    bookAdapter.setBooks(books);
                    mainLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    refreshLayout.setOnRefreshListener(CartActivity.this);
                    if (refreshLayout.isRefreshing())
                        refreshLayout.setRefreshing(false);
                } else {
                    refreshLayout.setOnRefreshListener(CartActivity.this);
                    tv_cart_empty.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    refreshLayout.setRefreshing(false);
                }

            }
        });
    }

    private void refreshList() {
        books.clear();
        progressBar.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.GONE);
        db.collection("users").document(Objects.requireNonNull(mUser.getEmail())).collection("cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> documents = task.getResult().getDocuments();

                if (documents.size() != 0) {
                    tv_cart_empty.setVisibility(View.GONE);
                    for (DocumentSnapshot document : documents) {
                        Map<String, Object> book = document.getData();
                        books.add(new CartBook(Objects.requireNonNull(book).get("title").toString(), document.getId(), book.get("s_image_url").toString(), book.get("author").toString(),
                                Double.parseDouble(book.get("mrp").toString()), Double.parseDouble(book.get("price").toString())));
                    }
                    bookAdapter.setBooks(books);
                    mainLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    refreshLayout.setOnRefreshListener(CartActivity.this);
                    if (refreshLayout.isRefreshing())
                        refreshLayout.setRefreshing(false);
                } else {
                    tv_cart_empty.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    if (refreshLayout.isRefreshing())
                        refreshLayout.setRefreshing(false);
                }

            }
        });
    }

    @Override
    public void onRefresh() {
        refreshList();
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
