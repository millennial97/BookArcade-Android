package in.bookarcade.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import in.bookarcade.app.R;
import in.bookarcade.app.model.CarouselItem;
import in.bookarcade.app.utils.UniversalImageLoader;

public class CarouselViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater layoutInflater;
    private List<CarouselItem> carouselImg;
    private ImageLoader imageLoader;

    public CarouselViewPagerAdapter(Context mContext, List<CarouselItem> carouselImg) {
        this.mContext = mContext;
        this.carouselImg = carouselImg;
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getDefaultConfig());
    }

    @Override
    public int getCount() {
        return carouselImg.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.card_carousel_item, null);

        CarouselItem utils = carouselImg.get(position);

        ImageView imageView = view.findViewById(R.id.imageView);

        UniversalImageLoader.setImage(utils.getCarouselImageUrl(), imageView, null);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Image " + position + " clicked", Toast.LENGTH_SHORT).show();
            }
        });

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}
