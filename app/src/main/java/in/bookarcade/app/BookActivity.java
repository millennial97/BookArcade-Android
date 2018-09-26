package in.bookarcade.app;

import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import at.blogc.android.views.ExpandableTextView;
import de.hdodenhof.circleimageview.CircleImageView;
import in.bookarcade.app.utils.UniversalImageLoader;

public class BookActivity extends AppCompatActivity {

    //Java built-in types
    private DecimalFormat decimalFormat = new DecimalFormat("###.##");
    private String title;
    private String author;
    private String book_id;
    private String s_image_url;
    private double mrp, price, goodreads_rating;

    //Android widgets
    private ImageView img_book;
    private TextView tv_book_title;
    private TextView tv_book_author;
    private TextView tv_book_publisher;
    private TextView tv_book_release_date;
    private TextView tv_book_pages;
    private TextView tv_book_isbn;
    private TextView tv_book_language;
    private TextView tv_mrp;
    private TextView tv_about_author;
    private ExpandableTextView tv_about;
    private Button btn_buy, btn_rent, btn_about_expand;
    private ImageButton btn_cart, btn_wishlist;
    private Intent intent;
    private RelativeLayout mainLayout;
    private ProgressBar progressBar;

    //External types
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser mUser;
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
        mUser = mAuth.getCurrentUser();

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
        tv_about = findViewById(R.id.tv_about);
        tv_about_author = findViewById(R.id.tv_about_author);

        //Buttons
        btn_buy = findViewById(R.id.btn_buy);
        btn_rent = findViewById(R.id.btn_rent);
        btn_cart = findViewById(R.id.btn_cart);
        btn_about_expand = findViewById(R.id.btn_about_expand);
        btn_wishlist = findViewById(R.id.btn_wishlist);

        //Listeners
        btn_about_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_about.toggle();
                btn_about_expand.setBackgroundResource(tv_about.isExpanded() ? R.drawable.ic_expand : R.drawable.ic_collapse);
            }
        });

        tv_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_about.toggle();
                btn_about_expand.setBackgroundResource(tv_about.isExpanded() ? R.drawable.ic_expand : R.drawable.ic_collapse);
            }
        });
    }

    private void mainInit() {

        //Initialize cart
        db.collection("users").document(Objects.requireNonNull(mUser.getEmail())).collection("cart").document(intent.getStringExtra("book_id"))
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    btn_cart.setBackgroundResource(R.drawable.ic_cart_down);
                }
                btn_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Objects.equals(btn_cart.getBackground().getConstantState(), getResources().getDrawable(R.drawable.ic_cart_plus).getConstantState())) {
                            btn_cart.setBackgroundResource(R.drawable.ic_cart_down);
                            Snackbar.make(mainLayout, "Added to cart", Snackbar.LENGTH_SHORT)
                                    .setAction("Cart", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivity(new Intent(BookActivity.this, CartActivity.class));
                                        }
                                    }).show();
                            Map<String, Object> cartBook = new HashMap<>();
                            cartBook.put("title", title);
                            cartBook.put("book_id", book_id);
                            cartBook.put("author", author);
                            cartBook.put("mrp", mrp);
                            cartBook.put("price", price);
                            cartBook.put("s_image_url", s_image_url);
                            cartBook.put("goodreads_rating", goodreads_rating);

                            db.collection("users").document(mUser.getEmail()).collection("cart").document(book_id).set(cartBook);
                        } else {
                            btn_cart.setBackgroundResource(R.drawable.ic_cart_plus);
                            Snackbar.make(mainLayout, "Removed from cart", Snackbar.LENGTH_SHORT).show();
                            db.collection("users").document(mUser.getEmail()).collection("cart").document(book_id).delete();
                        }
                    }
                });
            }
        });

        //Initialize wishlist
        db.collection("users").document(mUser.getEmail()).collection("wishlist").document(intent.getStringExtra("book_id"))
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    btn_wishlist.setBackgroundResource(R.drawable.ic_wishlist_added);
                }
                btn_wishlist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Objects.equals(btn_wishlist.getBackground().getConstantState(), getResources().getDrawable(R.drawable.ic_wishlist).getConstantState())) {
                            btn_wishlist.setBackgroundResource(R.drawable.ic_wishlist_added);
                            Snackbar.make(mainLayout, "Added to wishlist", Snackbar.LENGTH_SHORT).show();

                            Map<String, Object> wishlistBook = new HashMap<>();
                            wishlistBook.put("title", title);
                            wishlistBook.put("book_id", book_id);
                            wishlistBook.put("author", author);
                            wishlistBook.put("mrp", mrp);
                            wishlistBook.put("price", price);
                            wishlistBook.put("s_image_url", s_image_url);
                            wishlistBook.put("goodreads_rating", goodreads_rating);

                            db.collection("users").document(mUser.getEmail()).collection("wishlist").document(book_id).set(wishlistBook);
                        } else {
                            btn_wishlist.setBackgroundResource(R.drawable.ic_wishlist);
                            Snackbar.make(mainLayout, "Removed from wishlist", Snackbar.LENGTH_SHORT).show();
                            db.collection("users").document(mUser.getEmail()).collection("wishlist").document(book_id).delete();
                        }
                    }
                });
            }
        });

        //Set author details
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

        //Set book details
        db.collection("master_books").document(intent.getStringExtra("book_id")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    final Map<String, Object> book = task.getResult().getData();
                    if (book != null) {
                        UniversalImageLoader.setImage(book.get("m_image_url").toString(), img_book, null);

                        book_id = intent.getStringExtra("book_id");
                        s_image_url = book.get("s_image_url").toString();
                        goodreads_rating = Double.parseDouble(book.get("goodreads_rating").toString());

                        title = book.get("title").toString();
                        tv_book_title.setText(title);

                        author = book.get("author").toString();
                        tv_book_author.setText(author);

                        tv_book_language.setText(getString(R.string.language_) + " " + book.get("language").toString());
                        tv_book_isbn.setText(getString(R.string.isbn_) + " " + book.get("isbn13").toString());
                        tv_book_publisher.setText(book.get("publisher").toString());
                        tv_book_release_date.setText(book.get("release_date").toString());
                        tv_book_pages.setText(Integer.parseInt(book.get("pages").toString()) + " " + getString(R.string._pages));

                        mrp = Double.parseDouble(book.get("mrp").toString());
                        tv_mrp.setText(getString(R.string.rupee_symbol) + Double.parseDouble(book.get("mrp").toString()));
                        tv_mrp.setPaintFlags(tv_mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        tv_about.setText(Html.fromHtml(book.get("long_description").toString()));

                        price = Double.parseDouble(book.get("price").toString());

                        btn_buy.setText("Purchase - " + getString(R.string.rupee_symbol) + book.get("price").toString());
                        btn_buy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(BookActivity.this, PurchaseActivity.class);
                                i.putExtra("book_id", book_id);
                                i.putExtra("title", title);
                                i.putExtra("publisher", book.get("publisher").toString());
                                i.putExtra("price", price);
                                i.putExtra("mrp", mrp);
                                i.putExtra("author", book.get("author").toString());
                                i.putExtra("image_url", book.get("m_image_url").toString());
                                startActivity(i);
                            }
                        });

                        btn_rent.setText("Rent - " + getString(R.string.rupee_symbol) + decimalFormat.format(Double.parseDouble(book.get("price").toString()) * 0.40));
                        btn_rent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
//                        btn_rent.setText("Rent - " + getString(R.string.rupee_symbol) + (Math.round(Double.parseDouble(book.get("price").toString())) * 0.4 * 100.0/100.0));

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
