package in.bookarcade.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import in.bookarcade.app.utils.CartItemTouchHelper;

public class CartActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, CartItemTouchHelper.CartItemTouchListener {

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
    private ItemTouchHelper.SimpleCallback simpleCallback;

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

        simpleCallback = new CartItemTouchHelper(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(rv_books);

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
                        cart_total += Double.parseDouble(Objects.requireNonNull(book).get("price").toString());
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

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartBookAdapter.ViewHolder) {
            String itemName = books.get(viewHolder.getAdapterPosition()).getBookTitle();
            String itemId = books.get(viewHolder.getAdapterPosition()).getBookId();

            final CartBook deletedBook = books.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            Snackbar snackbar = Snackbar.make(mainLayout, itemName + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            snackbar.setActionTextColor(getResources().getColor(R.color.colorAccent));
            snackbar.show();
        }
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
