package in.bookarcade.app.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import in.bookarcade.app.R;
import in.bookarcade.app.utils.UniversalImageLoader;
import in.bookarcade.app.wallet.WalletActivity;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private CardView menu_orders, menu_addresses, menu_wallet, menu_wishlist, menu_about, menu_donate, menu_faq;
    private TextView tv_display_name, tv_email, tv_address;
    private CircleImageView img_user;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        tv_address = view.findViewById(R.id.tv_address);
        tv_display_name = view.findViewById(R.id.tv_display_name);
        tv_email = view.findViewById(R.id.tv_email);

        menu_orders = view.findViewById(R.id.menu1);
        menu_addresses = view.findViewById(R.id.menu2);
        menu_wallet = view.findViewById(R.id.menu3);
        menu_wishlist = view.findViewById(R.id.menu4);
        menu_about = view.findViewById(R.id.menu5);
        menu_faq = view.findViewById(R.id.menu6);
        menu_donate = view.findViewById(R.id.menu7);

        img_user = view.findViewById(R.id.img_user);

        if (Objects.requireNonNull(mUser.getProviders()).contains("facebook.com")) {
            UniversalImageLoader.setImage(String.valueOf(mUser.getPhotoUrl()) + "?height=200", img_user, null);
        } else {
            TextDrawable textDrawable = TextDrawable.builder()
                    .beginConfig().
                            textColor(getResources().getColor(R.color.colorPrimary)).
                            withBorder(4)
                    .endConfig().buildRound(String.valueOf(Objects.requireNonNull(mUser.getDisplayName()).charAt(0)),
                            getResources().getColor(R.color.colorLight));
            img_user.setBackground(textDrawable);
        }

        tv_display_name.setText(mUser.getDisplayName());
        tv_email.setText(mUser.getEmail());

        menu_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), WalletActivity.class));
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
