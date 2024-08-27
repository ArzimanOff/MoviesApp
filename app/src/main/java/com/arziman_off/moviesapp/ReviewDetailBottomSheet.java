package com.arziman_off.moviesapp;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ReviewDetailBottomSheet extends BottomSheetDialogFragment {
    private static final String ARG_REVIEW = "movieReview";

    private MovieReview review;

    public static ReviewDetailBottomSheet newInstance(MovieReview movieReview){
        ReviewDetailBottomSheet fragment = new ReviewDetailBottomSheet();
        Bundle args = new Bundle();
        args.putSerializable(ARG_REVIEW, movieReview);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                review = getArguments().getSerializable(ARG_REVIEW, MovieReview.class);
            } else {
                review = (MovieReview) getArguments().getSerializable(ARG_REVIEW);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_review_details,
                container,
                false
        );
        TextView reviewAuthor = view.findViewById(R.id.reviewAuthor);
        TextView reviewTitle = view.findViewById(R.id.reviewTitle);
        TextView reviewText = view.findViewById(R.id.reviewText);

        if (review != null){
            reviewAuthor.setText(review.getAuthor());
            reviewTitle.setText(review.getTitle());
            reviewText.setText(review.getText());
        }
        return view;
    }
}
