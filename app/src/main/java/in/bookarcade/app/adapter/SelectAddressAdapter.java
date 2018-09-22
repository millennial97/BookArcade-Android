package in.bookarcade.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.bookarcade.app.R;
import in.bookarcade.app.model.Address;

public class SelectAddressAdapter extends RecyclerView.Adapter<SelectAddressAdapter.ViewHolder> {

    private Context context;
    private List<Address> addresses;

    public SelectAddressAdapter(Context context) {
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent returnIntent = new Intent();
                    Address address = addresses.get(getAdapterPosition());
                    returnIntent.putExtra("name", address.getName());
                    returnIntent.putExtra("address1", address.getAddress1());
                    returnIntent.putExtra("address2", address.getAddress2());
                    returnIntent.putExtra("phone", address.getPhone());
                    returnIntent.putExtra("pincode", address.getPincode());
                    returnIntent.putExtra("landmark", address.getLandmark());
                    returnIntent.putExtra("city", address.getCity());

                    ((Activity) context).setResult(Activity.RESULT_OK, returnIntent);
                    ((Activity) context).finish();
                }
            });

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
            else
                this.tv_address2.setVisibility(View.GONE);
            if (!address.getLandmark().isEmpty())
                this.tv_landmark.setText(context.getString(R.string.landmark_) + " " + address.getLandmark());
            else
                this.tv_landmark.setVisibility(View.GONE);
            this.tv_phone.setText(context.getString(R.string.ph_) + " " + address.getPhone());
            this.tv_pincode.setText(address.getPincode());
            this.tv_city.setText(R.string.bangalore);
        }
    }

    @NonNull
    @Override
    public SelectAddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.card_select_address, parent, false);
        return new SelectAddressAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectAddressAdapter.ViewHolder holder, int position) {
        holder.bind(this.addresses.get(position));
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }
}
