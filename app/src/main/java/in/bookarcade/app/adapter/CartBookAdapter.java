package in.bookarcade.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import in.bookarcade.app.BookActivity;
import in.bookarcade.app.R;
import in.bookarcade.app.model.CartBook;
import in.bookarcade.app.model.HomeBook;
import in.bookarcade.app.utils.UniversalImageLoader;

public class CartBookAdapter extends RecyclerView.Adapter<CartBookAdapter.ViewHolder> {

    private Context context;
    private List<CartBook> books;

    public CartBookAdapter(Context context) {
        this.context = context;
        this.books = new ArrayList<>();
    }

    public void setBooks(List<CartBook> books) {
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
        private ProgressBar progressBar;
        private ImageButton btn_options;

        ViewHolder(View itemView) {
            super(itemView);

            this.tv_book_title = itemView.findViewById(R.id.tv_book_title);
            this.tv_book_author = itemView.findViewById(R.id.tv_book_author);
            this.tv_mrp = itemView.findViewById(R.id.tv_mrp);
            this.tv_price = itemView.findViewById(R.id.tv_price);
            this.img_book = itemView.findViewById(R.id.img_book);
            this.progressBar = itemView.findViewById(R.id.progress_bar);
            this.btn_options = itemView.findViewById(R.id.btn_options);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CartBook book = books.get(getAdapterPosition());
                    Intent i = new Intent(context, BookActivity.class);
                    i.putExtra("title", book.getBookTitle());
                    i.putExtra("book_id", book.getBookId());
                    i.putExtra("image_url", book.getImageUrl());
                    i.putExtra("author", book.getBookAuthor());
                    i.putExtra("mrp", book.getBookMrp());
                    i.putExtra("price", book.getBookPrice());
                    context.startActivity(i);
                }
            });

            this.btn_options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Options for " + books.get(getAdapterPosition()).getBookTitle(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        void bind(CartBook book) {
            UniversalImageLoader.setImage(book.getImageUrl(), this.img_book, this.progressBar);
            this.tv_book_title.setText(book.getBookTitle());
            this.tv_book_author.setText(book.getBookAuthor());
            this.tv_mrp.setText(String.valueOf(book.getBookMrp()));
            this.tv_mrp.setPaintFlags(tv_mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            this.tv_price.setText(String.valueOf(book.getBookPrice()));
        }
    }

    @NonNull
    @Override
    public CartBookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.card_cart_book, parent, false);
        return new CartBookAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartBookAdapter.ViewHolder holder, int position) {
        holder.bind(this.books.get(position));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
}
