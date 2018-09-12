package in.bookarcade.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import in.bookarcade.app.R;
import in.bookarcade.app.model.Section;
import in.bookarcade.app.utils.UniversalImageLoader;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.ViewHolder> {

    private Context context;
    private List<Section> sections;

    public SectionAdapter(Context context) {
        this.context = context;
        this.sections = new ArrayList<>();
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_section_name;
        private TextView tv_section_more;
        private RecyclerView rv_books;

        public ViewHolder(View itemView) {
            super(itemView);

            this.tv_section_name = itemView.findViewById(R.id.tv_section_name);
            this.tv_section_more = itemView.findViewById(R.id.tv_section_more);
            this.rv_books = itemView.findViewById(R.id.rv_books);
        }

        public void bind(Section section) {
            tv_section_name.setText(section.getSectionName());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.card_section, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(this.sections.get(position));
    }

    @Override
    public int getItemCount() {
        return sections.size();
    }
}
