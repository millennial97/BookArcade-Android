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
import in.bookarcade.app.model.Address;
import in.bookarcade.app.model.HomeBook;
import in.bookarcade.app.utils.UniversalImageLoader;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private Context context;
    private List<Address> addresses;

    public AddressAdapter(Context context) {
        this.context = context;
        this.addresses = new ArrayList<>();
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_display_name;
        private TextView tv_address1;
        private TextView tv_address2;
        private TextView tv_phone;
        private TextView tv_pincode;
        private TextView tv_landmark;
        private TextView tv_city;
        private ImageButton btn_edit, btn_delete;

        ViewHolder(View itemView) {
            super(itemView);

            this.tv_display_name = itemView.findViewById(R.id.tv_display_name);
            this.tv_address1 = itemView.findViewById(R.id.tv_address1);
            this.tv_address2 = itemView.findViewById(R.id.tv_address2);
            this.tv_phone = itemView.findViewById(R.id.tv_phone);
            this.tv_pincode = itemView.findViewById(R.id.tv_pincode);
            this.tv_landmark = itemView.findViewById(R.id.tv_landmark);
            this.tv_city = itemView.findViewById(R.id.tv_city);
            this.btn_delete = itemView.findViewById(R.id.btn_delete);
            this.btn_edit = itemView.findViewById(R.id.btn_edit);

            this.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            this.btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }

        void bind(Address address) {
            this.tv_display_name.setText(address.getName());
            this.tv_address1.setText(address.getAddress1());
            if (!address.getAddress2().isEmpty())
                this.tv_address2.setText(address.getAddress2());
            if (!address.getLandmark().isEmpty())
                this.tv_landmark.setText(address.getLandmark());
            this.tv_phone.setText(address.getPhone());
            this.tv_pincode.setText(address.getPincode());
            this.tv_city.setText(R.string.bangalore);
        }
    }

    @NonNull
    @Override
    public AddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.card_address, parent, false);
        return new AddressAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.ViewHolder holder, int position) {
        holder.bind(this.addresses.get(position));
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }
}
