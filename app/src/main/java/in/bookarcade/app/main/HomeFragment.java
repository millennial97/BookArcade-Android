package in.bookarcade.app.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import in.bookarcade.app.R;
import in.bookarcade.app.SectionMoreActivity;
import in.bookarcade.app.adapter.CarouselViewPagerAdapter;
import in.bookarcade.app.adapter.HomeAuthorAdapter;
import in.bookarcade.app.adapter.HomeBookAdapter;
import in.bookarcade.app.model.CarouselItem;
import in.bookarcade.app.model.HomeAuthor;
import in.bookarcade.app.model.HomeBook;
import in.bookarcade.app.utils.UniversalImageLoader;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    //Java built-in types
    private int dotsCount;

    //Android Widgets
    private ViewPager viewPager;
    private LinearLayout carouselDotsPanel;
    private ImageView[] dots;
    private CardView card_featured, card_spotlight;
    private ImageView img_footer, img_featured, img_spotlight;
    private List<CarouselItem> carouselImg;
    private CarouselViewPagerAdapter adapter;
    private RecyclerView rv_books, rv_books2, rv_books3, rv_books4, rv_books5, rv_books6, rv_books7, rv_books8, rv_books9, rv_books10;
    private RecyclerView rv_authors;
    private List<HomeBook> books, books2, books3, books4, books5, books6, books7, books8, books9, books10;
    private List<HomeAuthor> authors;
    private TextView tv_section1, tv_section2, tv_section3, tv_section4, tv_section5, tv_section6, tv_section7, tv_section8, tv_section9, tv_section10, tv_featured, tv_spotlight;
    private TextView tv_more1, tv_more2, tv_more3, tv_more4, tv_more5, tv_more6, tv_more7, tv_more8, tv_more9, tv_more10;
    private TextView tv_featured_book_title, tv_featured_book_author, tv_featured_book_description, tv_featured_price, tv_featured_mrp;
    private TextView tv_spotlight_book_title, tv_spotlight_book_author, tv_spotlight_book_description, tv_spotlight_price, tv_spotlight_mrp;

    private OnFragmentInteractionListener mListener;

    //External types
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private HomeAuthorAdapter authorAdapter;
    private HomeBookAdapter bookAdapter, bookAdapter2, bookAdapter3, bookAdapter4, bookAdapter5, bookAdapter6, bookAdapter7, bookAdapter8, bookAdapter9, bookAdapter10;

    public HomeFragment() {

    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        preInit();
        initViews(view);
        mainInit();

        return view;
    }

    private void preInit() {

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        carouselImg = new ArrayList<>();
        books = new ArrayList<>();
        books2 = new ArrayList<>();
        books3 = new ArrayList<>();
        books4 = new ArrayList<>();
        books5 = new ArrayList<>();
        books6 = new ArrayList<>();
        books7 = new ArrayList<>();
        books8 = new ArrayList<>();
        books9 = new ArrayList<>();
        books10 = new ArrayList<>();
        authors = new ArrayList<>();

//        carouselImg.add(new CarouselItem("https://images-eu.ssl-images-amazon.com/images/G/31/img18/Books/082018/ChildrenBookshelf/Ingress/1134387_BooksHomepage-Tile3_750x486_1536207908.jpg"));
//        carouselImg.add(new CarouselItem("https://images-eu.ssl-images-amazon.com/images/G/31/img18/Books/Editorials/750x486_BXHPTile_TravellersStore_2.jpg"));
//        carouselImg.add(new CarouselItem("https://images-eu.ssl-images-amazon.com/images/G/31/img18/Books/Editorials/1101443_750x486_HPTile_3.jpg"));

        adapter = new CarouselViewPagerAdapter(getContext(), carouselImg);
        bookAdapter = new HomeBookAdapter(getContext());
        bookAdapter2 = new HomeBookAdapter(getContext());
        bookAdapter3 = new HomeBookAdapter(getContext());
        bookAdapter4 = new HomeBookAdapter(getContext());
        bookAdapter5 = new HomeBookAdapter(getContext());
        bookAdapter6 = new HomeBookAdapter(getContext());
        bookAdapter7 = new HomeBookAdapter(getContext());
        bookAdapter8 = new HomeBookAdapter(getContext());
        bookAdapter9 = new HomeBookAdapter(getContext());
        bookAdapter10 = new HomeBookAdapter(getContext());
        authorAdapter = new HomeAuthorAdapter(getContext());

    }

    private void initViews(View view) {

        viewPager = view.findViewById(R.id.viewPager);
        carouselDotsPanel = view.findViewById(R.id.carousel_dots);

        //RecyclerViews
        rv_books = view.findViewById(R.id.rv_books);
        rv_books2 = view.findViewById(R.id.rv_books2);
        rv_books3 = view.findViewById(R.id.rv_books3);
        rv_books4 = view.findViewById(R.id.rv_books4);
        rv_books5 = view.findViewById(R.id.rv_books5);
        rv_books6 = view.findViewById(R.id.rv_books6);
        rv_books7 = view.findViewById(R.id.rv_books7);
        rv_books8 = view.findViewById(R.id.rv_books8);
        rv_books9 = view.findViewById(R.id.rv_books9);
        rv_books10 = view.findViewById(R.id.rv_books10);
        rv_authors = view.findViewById(R.id.rv_authors);

        //TextViews
        tv_more1 = view.findViewById(R.id.tv_more1);
        tv_more2 = view.findViewById(R.id.tv_more2);
        tv_more3 = view.findViewById(R.id.tv_more3);
        tv_more4 = view.findViewById(R.id.tv_more4);
        tv_more5 = view.findViewById(R.id.tv_more5);
        tv_more6 = view.findViewById(R.id.tv_more6);
        tv_more7 = view.findViewById(R.id.tv_more7);
        tv_more8 = view.findViewById(R.id.tv_more8);
        tv_more9 = view.findViewById(R.id.tv_more9);
        tv_more10 = view.findViewById(R.id.tv_more10);

        tv_featured = view.findViewById(R.id.tv_head_featured);
        tv_featured_book_title = view.findViewById(R.id.tv_featured_book_title);
        tv_featured_book_author = view.findViewById(R.id.tv_featured_book_author);
        tv_featured_book_description = view.findViewById(R.id.tv_featured_book_description);
        tv_featured_price = view.findViewById(R.id.tv_featured_price);
        tv_featured_mrp = view.findViewById(R.id.tv_featured_mrp);

        tv_spotlight = view.findViewById(R.id.tv_head_spotlight);
        tv_spotlight_book_title = view.findViewById(R.id.tv_spotlight_book_title);
        tv_spotlight_book_author = view.findViewById(R.id.tv_spotlight_book_author);
        tv_spotlight_book_description = view.findViewById(R.id.tv_spotlight_book_description);
        tv_spotlight_price = view.findViewById(R.id.tv_spotlight_price);
        tv_spotlight_mrp = view.findViewById(R.id.tv_spotlight_mrp);

        tv_section1 = view.findViewById(R.id.tv_section1);
        tv_section2 = view.findViewById(R.id.tv_section2);
        tv_section3 = view.findViewById(R.id.tv_section3);
        tv_section4 = view.findViewById(R.id.tv_section4);
        tv_section5 = view.findViewById(R.id.tv_section5);
        tv_section6 = view.findViewById(R.id.tv_section6);
        tv_section7 = view.findViewById(R.id.tv_section7);
        tv_section8 = view.findViewById(R.id.tv_section8);
        tv_section9 = view.findViewById(R.id.tv_section9);
        tv_section10 = view.findViewById(R.id.tv_section10);

        //CardViews/Layouts
        card_featured = view.findViewById(R.id.card_featured);


        img_footer = view.findViewById(R.id.img_footer);
        img_featured = view.findViewById(R.id.img_featured_book);
        img_spotlight = view.findViewById(R.id.img_spotlight_book);

        //Listeners


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotsCount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.dot_inactive));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.dot_active));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void mainInit() {
/*
        db.collection("books").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> documents = task.getResult().getDocuments();
                for (DocumentSnapshot document : documents) {
                    Map<String, Object> book = document.getData();
                    Map<String, Object> master_book = new HashMap<>();
                    master_book.put("book_sku", book.get("book_sku").toString());
                    master_book.put("author", book.get("author").toString());
                    master_book.put("category", book.get("category").toString());
                    master_book.put("category_tags", book.get("category_tags"));
                    master_book.put("condition", "New");
                    master_book.put("goodreads_rating", Double.parseDouble(book.get("goodreads_rating").toString()));
                    master_book.put("mrp", Double.parseDouble(book.get("mrp").toString()));
                    master_book.put("price", Double.parseDouble(book.get("price").toString()));
                    master_book.put("pages", Integer.parseInt(book.get("pages").toString()));
                    master_book.put("isbn10", book.get("isbn10").toString());
                    master_book.put("isbn13", book.get("isbn13").toString());
                    master_book.put("l_image_url", book.get("l_image_url").toString());
                    master_book.put("s_image_url", book.get("s_image_url").toString());
                    master_book.put("m_image_url", book.get("m_image_url").toString());
                    master_book.put("language", book.get("language").toString());
                    master_book.put("long_description", book.get("long_description").toString());
                    master_book.put("publisher", book.get("publisher").toString());
                    master_book.put("release_date", book.get("release_date").toString());
                    master_book.put("short_description", book.get("short_description").toString());
                    master_book.put("sub_category", book.get("sub_category").toString());
                    master_book.put("title", book.get("title").toString());
                    master_book.put("stock_available", true);


                    db.collection("master_books").document(book.get("book_sku").toString()).set(master_book);
                }
            }
        });*/

      /*  db.collection("master_books").document("BAAZ-9352644123").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Map<String, Object> master_book = task.getResult().getData();
                Map<String, Object> book = new HashMap<>();
                book.put("author", master_book.get("author").toString());
                book.put("title", master_book.get("title").toString());
                book.put("m_image_url", master_book.get("m_image_url").toString());
                book.put("book_id", master_book.get("book_sku").toString());
                book.put("mrp", Double.parseDouble(master_book.get("mrp").toString()));
                book.put("price", Double.parseDouble(master_book.get("price").toString()));
                db.collection("android_v1_0_0").document("section3").collection("books").document(master_book.get("book_sku").toString()).set(book);
            }
        });

        Map<String, Object> author = new HashMap<>();
        author.put("name", "Anuja Chauhan");
        author.put("image_url", "https://images.indianexpress.com/2015/05/anuja-main.jpg");
        author.put("about", "Anuja Chauhan is an Indian author, advertiser and screenwriter. She worked in the advertising agency, JWT India, for over 17 years, eventually becoming vice-president and executive creative director, before resigning in 2010 to pursue a full-time literary career.");
        db.collection("master_authors").document("Anuja Chauhan").set(author);*/


        db.collection("android_v1_0_0").document("carousel_home").collection("items").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> documents = task.getResult().getDocuments();
                for (DocumentSnapshot document : documents) {
                    Map<String, Object> item = document.getData();
                    if (item != null) {
                        carouselImg.add(new CarouselItem(item.get("image_url").toString(), item.get("item_type").toString(), item.get("path").toString(),
                                item.get("author").toString(), item.get("book_id").toString(), item.get("m_image_url").toString(), item.get("title").toString(),
                                Double.parseDouble(item.get("mrp").toString()), Double.parseDouble(item.get("price").toString())));
                    }
                }
                adapter.notifyDataSetChanged();

                dotsCount = adapter.getCount();
                dots = new ImageView[dotsCount];

                for (int i = 0; i < dotsCount; i++) {
                    dots[i] = new ImageView(getContext());
                    dots[i].setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.dot_inactive));

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    params.setMargins(8, 0, 8, 0);
                    carouselDotsPanel.addView(dots[i], params);
                }

                dots[0].setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.dot_active));
            }
        });

        viewPager.setAdapter(adapter);

        UniversalImageLoader.setImage("https://funologist.org/wp-content/uploads/2017/11/donate-button.gif", img_footer, null);

        setSectionBooks();

        authors.add(new HomeAuthor("Chetan Bhagat", "https://images.indianexpress.com/2016/06/chetan-bhagat-lead.jpg", "CHETAN_BHAGAT"));
        authors.add(new HomeAuthor("Chetan Bhagat", "https://i.gadgets360cdn.com/large/chetan_bhagat_facebook_full_1524833671640.jpg?", "CHETAN_BHAGAT"));
        authors.add(new HomeAuthor("Arthur Conan Doyle", "https://www.thefamouspeople.com/profiles/images/sir-arthur-conan-doyle-18.jpg", "CHETAN_BHAGAT"));
        authors.add(new HomeAuthor("JK Rowling", "https://static.timesofisrael.com/www/uploads/2018/04/AP_16267553017862-e1524077573580.jpg", "CHETAN_BHAGAT"));
        authors.add(new HomeAuthor("Steve Jobs", "https://cdn.vox-cdn.com/thumbor/DVN7eqE1o8HeBOP-jg15YHTsiLY=/0x0:640x427/1200x800/filters:focal(0x0:640x427)/cdn.vox-cdn.com/assets/1496753/stevejobs.jpg", "CHETAN_BHAGAT"));
        authors.add(new HomeAuthor("Chetan Bhagat", "https://images.indianexpress.com/2016/06/chetan-bhagat-lead.jpg", "CHETAN_BHAGAT"));
        authors.add(new HomeAuthor("Chetan Bhagat", "https://images.indianexpress.com/2016/06/chetan-bhagat-lead.jpg", "CHETAN_BHAGAT"));
        authors.add(new HomeAuthor("Chetan Bhagat", "https://images.indianexpress.com/2016/06/chetan-bhagat-lead.jpg", "CHETAN_BHAGAT"));

        authorAdapter.setAuthors(authors);
        rv_authors.setAdapter(authorAdapter);

    }

    private void setSectionBooks() {

        //Featured
        db.collection("android_v1_0_0").document("featured").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Map<String, Object> featuredBook = task.getResult().getData();
                if (featuredBook != null) {
                    UniversalImageLoader.setImage(featuredBook.get("s_image_url").toString(), img_featured, null);
                    tv_featured.setText(featuredBook.get("section_name").toString());
                    tv_featured_book_title.setText(featuredBook.get("title").toString());
                    tv_featured_book_author.setText(getString(R.string.hyphen) + featuredBook.get("author").toString());
                    tv_featured_book_description.setText(featuredBook.get("description").toString());
                    tv_featured_mrp.setText(getString(R.string.rupee_symbol) + String.valueOf(Double.parseDouble(featuredBook.get("mrp").toString())));
                    tv_featured_mrp.setPaintFlags(tv_featured_mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    tv_featured_price.setText(getString(R.string.rupee_symbol) + String.valueOf(Double.parseDouble(featuredBook.get("price").toString())));


                }
            }
        });

        //Spotlight
        db.collection("android_v1_0_0").document("spotlight").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Map<String, Object> spotlightBook = task.getResult().getData();
                if (spotlightBook != null) {
                    UniversalImageLoader.setImage(spotlightBook.get("s_image_url").toString(), img_spotlight, null);
                    tv_spotlight.setText(spotlightBook.get("section_name").toString());
                    tv_spotlight_book_title.setText(spotlightBook.get("title").toString());
                    tv_spotlight_book_author.setText(getString(R.string.hyphen) + spotlightBook.get("author").toString());
                    tv_spotlight_book_description.setText(spotlightBook.get("description").toString());
                    tv_spotlight_mrp.setText(getString(R.string.rupee_symbol) + String.valueOf(Double.parseDouble(spotlightBook.get("mrp").toString())));
                    tv_spotlight_mrp.setPaintFlags(tv_spotlight_mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    tv_spotlight_price.setText(getString(R.string.rupee_symbol) + String.valueOf(Double.parseDouble(spotlightBook.get("price").toString())));


                }
            }
        });

        //Section 1
        db.collection("android_v1_0_0").document("section1").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                tv_section1.setText(Objects.requireNonNull(task.getResult().getData()).get("section_name").toString());
                tv_more1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getContext(), SectionMoreActivity.class);
                        i.putExtra("section_id", "section1");
                        i.putExtra("title", tv_section1.getText().toString());
                        startActivity(i);
                    }
                });
            }
        });
        db.collection("android_v1_0_0").document("section1").collection("books").limit(10).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                        bookAdapter.setBooks(books);
                        rv_books.setAdapter(bookAdapter);
                    }
                });

        //Section 2
        db.collection("android_v1_0_0").document("section2").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                tv_section2.setText(Objects.requireNonNull(task.getResult().getData()).get("section_name").toString());
                tv_more2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getContext(), SectionMoreActivity.class);
                        i.putExtra("title", tv_section2.getText().toString());
                        i.putExtra("section_id", "section2");
                        startActivity(i);
                    }
                });
            }
        });
        db.collection("android_v1_0_0").document("section2").collection("books").limit(10).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                        for (DocumentSnapshot document : documents) {
                            Map<String, Object> book = document.getData();
                            if (book != null) {
                                books2.add(new HomeBook(book.get("title").toString(), book.get("book_id").toString(),
                                        book.get("m_image_url").toString(), book.get("author").toString(), Double.parseDouble(book.get("mrp").toString()),
                                        Double.parseDouble(book.get("price").toString())));
                            }
                        }
                        bookAdapter2.setBooks(books2);
                        rv_books2.setAdapter(bookAdapter2);
                    }
                });

        //Section 3
        db.collection("android_v1_0_0").document("section3").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                tv_section3.setText(Objects.requireNonNull(task.getResult().getData()).get("section_name").toString());
                tv_more3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getContext(), SectionMoreActivity.class);
                        i.putExtra("title", tv_section3.getText().toString());
                        i.putExtra("section_id", "section3");
                        startActivity(i);
                    }
                });
            }
        });
        db.collection("android_v1_0_0").document("section3").collection("books").limit(10).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                        for (DocumentSnapshot document : documents) {
                            Map<String, Object> book = document.getData();
                            if (book != null) {
                                books3.add(new HomeBook(book.get("title").toString(), book.get("book_id").toString(),
                                        book.get("m_image_url").toString(), book.get("author").toString(), Double.parseDouble(book.get("mrp").toString()),
                                        Double.parseDouble(book.get("price").toString())));
                            }
                        }
                        bookAdapter3.setBooks(books3);
                        rv_books3.setAdapter(bookAdapter3);
                    }
                });

    }

    private void setSectionNames() {

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
