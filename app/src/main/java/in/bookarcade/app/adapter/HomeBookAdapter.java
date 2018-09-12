package in.bookarcade.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import in.bookarcade.app.R;
import in.bookarcade.app.model.HomeBook;
import in.bookarcade.app.model.Section;
import in.bookarcade.app.utils.UniversalImageLoader;

public class HomeBookAdapter extends RecyclerView.Adapter<HomeBookAdapter.ViewHolder> {

    private Context context;
    private List<HomeBook> books;

    public HomeBookAdapter(Context context) {
        this.context = context;
        this.books = new ArrayList<>();
    }

    public void setBooks(List<HomeBook> books) {
        this.books = books;
        notifyDataSetChanged();
        ImageLoader.getInstance().init(new UniversalImageLoader(context).getDefaultConfig());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_book_title;
        private TextView tv_book_author;
        private ImageView img_book;
        private TextView tv_mrp;
        private TextView tv_price;

        public ViewHolder(View itemView) {
            super(itemView);

            this.tv_book_title = itemView.findViewById(R.id.tv_book_title);
            this.tv_book_author = itemView.findViewById(R.id.tv_book_author);
            this.tv_mrp = itemView.findViewById(R.id.tv_mrp);
            this.tv_price = itemView.findViewById(R.id.tv_price);
            this.img_book = itemView.findViewById(R.id.img_book);
        }

        public void bind(HomeBook book) {
            UniversalImageLoader.setImage(book.getImageUrl(), this.img_book, null);
            this.tv_book_title.setText(book.getBookTitle());
            this.tv_book_author.setText(book.getBookAuthor());
            this.tv_mrp.setText(String.valueOf(book.getBookMrp()));
            this.tv_price.setText(String.valueOf(book.getBookPrice()));
        }
    }

    @NonNull
    @Override
    public HomeBookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.card_home_book, parent, false);
        return new HomeBookAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeBookAdapter.ViewHolder holder, int position) {
        holder.bind(this.books.get(position));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
}
