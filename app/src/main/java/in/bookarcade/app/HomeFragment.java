package in.bookarcade.app;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import in.bookarcade.app.adapter.CarouselViewPagerAdapter;
import in.bookarcade.app.adapter.HomeBookAdapter;
import in.bookarcade.app.adapter.SectionAdapter;
import in.bookarcade.app.model.CarouselItem;
import in.bookarcade.app.model.HomeBook;
import in.bookarcade.app.model.Section;


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
    private List<CarouselItem> carouselImg;
    private CarouselViewPagerAdapter adapter;
    private RecyclerView rv_books, rv_books2, rv_books3, rv_books4, rv_books5, rv_books6, rv_books7, rv_books8, rv_books9, rv_books10;
    private List<HomeBook> books, books2, books3, books4, books5, books6, books7, books8, books9, books10;

    private OnFragmentInteractionListener mListener;

    //External types
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
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

        carouselImg.add(new CarouselItem("https://www.pugh.co.uk/wp-content/uploads/2018/03/Sophos-Intercept-X--770x377.jpg"));
        carouselImg.add(new CarouselItem("http://www.emilylistman.com/wp-content/uploads/2018/06/home-improvement-770x377.jpeg"));
        carouselImg.add(new CarouselItem("http://beautifultrouble.org/wp-content/themes/beautifultrouble/img/BT_Banners4.jpg"));
        carouselImg.add(new CarouselItem("http://beautifultrouble.org/wp-content/themes/beautifultrouble/img/BT_Banners4.jpg"));

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

        dotsCount = adapter.getCount();
        dots = new ImageView[dotsCount];

    }

    private void initViews(View view) {

        viewPager = view.findViewById(R.id.viewPager);
        carouselDotsPanel = view.findViewById(R.id.carousel_dots);

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

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(getContext());
            dots[i].setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.dot_inactive));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);
            carouselDotsPanel.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.dot_active));

        //Listeners
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotsCount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dot_inactive));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dot_active));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void mainInit() {
        viewPager.setAdapter(adapter);

        books.add(new HomeBook("Half Girlfriend", "HALFGIRLFRIEND",
                "https://images-na.ssl-images-amazon.com/images/I/51A4JAd%2BXsL._SX323_BO1,204,203,200_.jpg",
                "Chetan Bhagat", 200.0, 176.0));
        books.add(new HomeBook("Half Girlfriend", "HALFGIRLFRIEND",
                "https://images-na.ssl-images-amazon.com/images/I/51IpHUkHttL._SX326_BO1,204,203,200_.jpg",
                "Chetan Bhagat", 200.0, 176.0));
        books.add(new HomeBook("Half Girlfriend", "HALFGIRLFRIEND",
                "https://images-na.ssl-images-amazon.com/images/I/51IpHUkHttL._SX326_BO1,204,203,200_.jpg",
                "Chetan Bhagat", 200.0, 176.0));
        books.add(new HomeBook("Half Girlfriend", "HALFGIRLFRIEND",
                "https://images-na.ssl-images-amazon.com/images/I/51IpHUkHttL._SX326_BO1,204,203,200_.jpg",
                "Chetan Bhagat", 200.0, 176.0));
        books.add(new HomeBook("Half Girlfriend", "HALFGIRLFRIEND",
                "https://images-na.ssl-images-amazon.com/images/I/51IpHUkHttL._SX326_BO1,204,203,200_.jpg",
                "Chetan Bhagat", 200.0, 176.0));
        books.add(new HomeBook("Half Girlfriend", "HALFGIRLFRIEND",
                "https://images-na.ssl-images-amazon.com/images/I/51IpHUkHttL._SX326_BO1,204,203,200_.jpg",
                "Chetan Bhagat", 200.0, 176.0));
        books.add(new HomeBook("Half Girlfriend", "HALFGIRLFRIEND",
                "https://images-na.ssl-images-amazon.com/images/I/51IpHUkHttL._SX326_BO1,204,203,200_.jpg",
                "Chetan Bhagat", 200.0, 176.0));
        bookAdapter.setBooks(books);
        rv_books.setAdapter(bookAdapter);

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
