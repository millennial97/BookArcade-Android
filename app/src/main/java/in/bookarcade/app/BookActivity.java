package in.bookarcade.app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import in.bookarcade.app.utils.UniversalImageLoader;

public class BookActivity extends AppCompatActivity {

    //Java built-in types


    //Android widgets
    private ImageView img_book;
    private TextView tv_book_title, tv_book_author, tv_book_publisher, tv_book_release_date, tv_book_pages, tv_book_isbn, tv_book_language, tv_mrp, tv_read_more, tv_about, tv_about_author;
    private Button btn_buy, btn_rent;
    private ImageButton btn_cart, btn_wishlist;
    private Intent intent;
    private RelativeLayout mainLayout;
    private ProgressBar progressBar;

    //External types
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CircleImageView img_author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        preInit();
        initViews();
        mainInit();

    }

    private void preInit() {
        ActionBar actionBar = this.getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_up);

        intent = getIntent();
        actionBar.setTitle(intent.getStringExtra("title"));

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

    }

    private void initViews() {
        mainLayout = findViewById(R.id.mainLayout);
        progressBar = findViewById(R.id.progress_bar);

        //ImageViews
        img_author = findViewById(R.id.img_author);
        img_book = findViewById(R.id.img_book);

        //TextViews
        tv_book_title = findViewById(R.id.tv_book_title);
        tv_book_author = findViewById(R.id.tv_book_author);
        tv_book_language = findViewById(R.id.tv_book_language);
        tv_book_isbn = findViewById(R.id.tv_book_isbn);
        tv_book_publisher = findViewById(R.id.tv_book_publisher);
        tv_book_release_date = findViewById(R.id.tv_book_release_date);
        tv_book_pages = findViewById(R.id.tv_book_pages);
        tv_mrp = findViewById(R.id.tv_mrp);
        tv_read_more = findViewById(R.id.tv_read_more);
        tv_about = findViewById(R.id.tv_about);
        tv_about_author = findViewById(R.id.tv_about_author);

        //Buttons
        btn_buy = findViewById(R.id.btn_buy);
        btn_rent = findViewById(R.id.btn_rent);
        btn_cart = findViewById(R.id.btn_cart);
        btn_wishlist = findViewById(R.id.btn_wishlist);

        //Listeners
        btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void mainInit() {

        /*
        Map<String, Object> book = new HashMap<>();
        book.put("author", "Ravish Kumar");
        book.put("title", "The Free Voice: On Democracy, Culture and the Nation");
        book.put("s_image_url", "https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1520184445i/38929045._UY200_.jpg");
        book.put("book_id", "THE_FREE_VOICE-9387164780");
        book.put("mrp", 499);
        book.put("price", 335);
        db.collection("android_v1_0_0").document("section1").collection("books").document("THE_FREE_VOICE-9387164780").set(book);

        Map<String, Object> author = new HashMap<>();
        author.put("name", "Ravish Kumar");
        author.put("image_url", "https://i0.wp.com/news.fawnconsulting.in/wp-content/uploads/2018/01/Ravish-kumar.jpg?fit=576%2C432");
        author.put("about", "Ravish Kumar, writer, journalist and social commentator, is Senior Executive Editor with NDTV India. He is also the author of Ishq Mein Shahar Hona, Dekhte Rahiye and Ravishpanti.");
        db.collection("master_authors").document("Ravish Kumar").set(author);
        */


        db.collection("master_authors").document(intent.getStringExtra("author")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Map<String, Object> author = task.getResult().getData();
                if (author != null) {
                    UniversalImageLoader.setImage(author.get("image_url").toString(), img_author, null);
                    tv_about_author.setText(author.get("about").toString());
                }
            }
        });

        db.collection("master_books").document(intent.getStringExtra("book_id")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    Map<String, Object> book = task.getResult().getData();
                    if (book != null) {
                        UniversalImageLoader.setImage(book.get("m_image_url").toString(), img_book, null);

                        tv_book_title.setText(book.get("title").toString());
                        tv_book_author.setText(book.get("author").toString());
                        tv_book_language.setText(getString(R.string.language_) + " " + book.get("language").toString());
                        tv_book_isbn.setText(getString(R.string.isbn_) + " " + book.get("isbn13").toString());
                        tv_book_publisher.setText(book.get("publisher").toString());
                        tv_book_release_date.setText(book.get("release_date").toString());
                        tv_book_pages.setText(Integer.parseInt(book.get("pages").toString()) + " " + getString(R.string._pages));
                        tv_mrp.setText(getString(R.string.mrp_rupee) + Double.parseDouble(book.get("mrp").toString()));
                        tv_about.setText(book.get("short_description").toString());

                        progressBar.setVisibility(View.GONE);
                        mainLayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(BookActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
