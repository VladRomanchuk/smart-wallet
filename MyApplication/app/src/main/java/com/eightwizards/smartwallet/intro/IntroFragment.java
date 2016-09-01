package com.eightwizards.smartwallet.intro;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eightwizards.smartwallet.R;

public class IntroFragment extends Fragment {

    private TextView  titleTextView;
    private TextView  descriptionTitleTextView;
    private ImageView logoImageView;

    public static IntroFragment newInstance(String title, String description, int resources) {
        IntroFragment fragment = new IntroFragment();

        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("description", description);
        args.putInt("resources", resources);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_intro, container, false);

        defineView(view);
        setText();

        return view;
    }

    private void defineView(View v){
        titleTextView = (TextView) v.findViewById(R.id.title_intro);
        descriptionTitleTextView = (TextView) v.findViewById(R.id.description_intro);
        logoImageView = (ImageView) v.findViewById(R.id.logo_intro);
    }

    private void setText(){
        titleTextView.setText(getArguments().getString("title"));
        descriptionTitleTextView.setText(getArguments().getString("description"));
        logoImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), getArguments().getInt("resources")));
    }
}
