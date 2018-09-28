package in.bookarcade.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import in.bookarcade.app.BookActivity;
import in.bookarcade.app.R;
import in.bookarcade.app.model.HomeBook;
import in.bookarcade.app.utils.UniversalImageLoader;

public class HomeBookAdapter extends RecyclerView.Adapter<HomeBookAdapter.ViewHolder> {

    private Context context;
    private List<HomeBook> books;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser mUser;

    public HomeBookAdapter(Context context) {
        this.context = context;
        this.books = new ArrayList<>();
    }

    public void setBooks(List<HomeBook> books) {
        this.books = books;
        this.mAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
        this.mUser = mAuth.getCurrentUser();
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
                    HomeBook book = books.get(getAdapterPosition());
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
                    PopupMenu popupMenu = new PopupMenu(context, btn_options);
                    popupMenu.inflate(R.menu.home_book_menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu_cart:
                                    Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show();

                                    HomeBook clickedBook = books.get(getAdapterPosition());

                                    Map<String, Object> cartBook = new HashMap<>();
                                    cartBook.put("title", clickedBook.getBookTitle());
                                    cartBook.put("book_id", clickedBook.getBookId());
                                    cartBook.put("author", clickedBook.getBookAuthor());
                                    cartBook.put("mrp", clickedBook.getBookMrp());
                                    cartBook.put("price", clickedBook.getBookPrice());
                                    cartBook.put("s_image_url", clickedBook.getSImageUrl());

                                    db.collection("users").document(Objects.requireNonNull(mUser.getEmail())).collection("cart").document(clickedBook.getBookId()).set(cartBook);
                                    return true;
                                case R.id.menu_wishlist:
                                    Toast.makeText(context, "Added to Wishlist", Toast.LENGTH_SHORT).show();

                                    HomeBook clickedBook2 = books.get(getAdapterPosition());

                                    Map<String, Object> cartBook2 = new HashMap<>();
                                    cartBook2.put("title", clickedBook2.getBookTitle());
                                    cartBook2.put("book_id", clickedBook2.getBookId());
                                    cartBook2.put("author", clickedBook2.getBookAuthor());
                                    cartBook2.put("mrp", clickedBook2.getBookMrp());
                                    cartBook2.put("price", clickedBook2.getBookPrice());
                                    cartBook2.put("s_image_url", clickedBook2.getSImageUrl());

                                    db.collection("users").document(Objects.requireNonNull(mUser.getEmail())).collection("wishlist").document(clickedBook2.getBookId()).set(cartBook2);
                                    return true;

                                default:
                                    return false;
                            }
                        }
                    });
                    popupMenu.show();
                }
            });
        }

        void bind(HomeBook book) {
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
