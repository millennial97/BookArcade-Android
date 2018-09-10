package in.bookarcade.app;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import in.bookarcade.app.adapter.CarouselViewPagerAdapter;
import in.bookarcade.app.model.CarouselItem;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private int dotsCount;
    private ViewPager viewPager;
    private LinearLayout carouselDotsPanel;
    private ImageView[] dots;
    private List<CarouselItem> carouselImg;
    private CarouselViewPagerAdapter adapter;

    private OnFragmentInteractionListener mListener;

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

        carouselImg = new ArrayList<>();

        viewPager = view.findViewById(R.id.viewPager);
        carouselDotsPanel = view.findViewById(R.id.carousel_dots);

        carouselImg.add(new CarouselItem("https://www.pugh.co.uk/wp-content/uploads/2018/03/Sophos-Intercept-X--770x377.jpg"));
        carouselImg.add(new CarouselItem("http://www.emilylistman.com/wp-content/uploads/2018/06/home-improvement-770x377.jpeg"));
        carouselImg.add(new CarouselItem("http://beautifultrouble.org/wp-content/themes/beautifultrouble/img/BT_Banners4.jpg"));
        carouselImg.add(new CarouselItem("http://beautifultrouble.org/wp-content/themes/beautifultrouble/img/BT_Banners4.jpg"));

        adapter = new CarouselViewPagerAdapter(getContext(), carouselImg);
        viewPager.setAdapter(adapter);

        dotsCount = adapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(getContext());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dot_inactive));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);
            carouselDotsPanel.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dot_active));


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


        return view;
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
