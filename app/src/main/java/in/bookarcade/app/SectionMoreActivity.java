package in.bookarcade.app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

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

import in.bookarcade.app.adapter.HomeBookAdapter;
import in.bookarcade.app.model.HomeBook;
import in.bookarcade.app.utils.ColumnQty;

public class SectionMoreActivity extends AppCompatActivity {

    //Java built-in types
    private List<HomeBook> books;

    //Android widgets
    private RecyclerView rv_books;
    private HomeBookAdapter bookAdapter;
    private Intent intent;
    private ProgressBar progressBar;

    //External types
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_more);

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
        actionBar.setTitle(intent.getStringExtra("title"));

        books = new ArrayList<>();
        bookAdapter = new HomeBookAdapter(this);
    }

    private void initViews() {
        rv_books = findViewById(R.id.rv_books);
        progressBar = findViewById(R.id.progress_bar);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int n = (int) (dpWidth / 180);

        rv_books.setLayoutManager(new GridLayoutManager(this, new ColumnQty(this, R.layout.card_home_book).calculateNoOfColumns()));
        rv_books.setAdapter(bookAdapter);
    }

    private void mainInit() {
        db.collection("android_v1_0_0").document("section1").collection("books").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> documents = task.getResult().getDocuments();
                for (DocumentSnapshot document : documents) {
                    Map<String, Object> book = document.getData();
                    if (book != null) {
                        books.add(new HomeBook(book.get("title").toString(), book.get("book_id").toString(),
                                book.get("m_image_url").toString(), book.get("author").toString(), Double.parseDouble(book.get("mrp").toString()),
                                Double.parseDouble(book.get("price").toString())));
                    }

                }
                progressBar.setVisibility(View.GONE);
                bookAdapter.setBooks(books);
                rv_books.setAdapter(bookAdapter);
                rv_books.setVisibility(View.VISIBLE);
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
