package in.bookarcade.app.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.bookarcade.app.R;
import in.bookarcade.app.model.HomeAuthor;
import in.bookarcade.app.model.HomeBook;
import in.bookarcade.app.utils.UniversalImageLoader;

public class HomeAuthorAdapter extends RecyclerView.Adapter<HomeAuthorAdapter.ViewHolder> {

    private Context context;
    private List<HomeAuthor> authors;

    public HomeAuthorAdapter(Context context) {
        this.context = context;
        this.authors = new ArrayList<>();
    }

    public void setAuthors(List<HomeAuthor> authors) {
        this.authors = authors;
        notifyDataSetChanged();
        ImageLoader.getInstance().init(new UniversalImageLoader(context).getDefaultConfig());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_author_name;
        private CircleImageView img_author;
        private ProgressBar progressBar;

        ViewHolder(View itemView) {
            super(itemView);

            this.tv_author_name = itemView.findViewById(R.id.tv_author_name);
            this.img_author = itemView.findViewById(R.id.img_author);
            this.progressBar = itemView.findViewById(R.id.progress_bar);
        }

        void bind(HomeAuthor author) {
            UniversalImageLoader.setImage(author.getImageUrl(), this.img_author, this.progressBar);
            this.tv_author_name.setText(author.getAuthorName());
        }
    }

    @NonNull
    @Override
    public HomeAuthorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.card_home_author, parent, false);
        return new HomeAuthorAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAuthorAdapter.ViewHolder holder, int position) {
        holder.bind(this.authors.get(position));
    }

    @Override
    public int getItemCount() {
        return authors.size();
    }
}
