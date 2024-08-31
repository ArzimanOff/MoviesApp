package com.arziman_off.moviesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
        TextView reviewDate = view.findViewById(R.id.reviewData);
        TextView reviewType = view.findViewById(R.id.reviewType);
        TextView reviewGoodReaction = view.findViewById(R.id.reviewGoodReaction);
        TextView reviewBadReaction = view.findViewById(R.id.reviewBadReaction);


        ImageView closeReviewWindowBtn = view.findViewById(R.id.closeReviewWindowBtn);
        LinearLayout reviewAuthorBox = view.findViewById(R.id.reviewAuthorBox);
        closeReviewWindowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        reviewAuthorBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.kinopoisk.ru/user/" + review.getAuthorId()));
                startActivity(intent);
            }
        });

        if (review != null){
            reviewAuthor.setText(review.getAuthor());
            reviewTitle.setText(review.getTitle());
            reviewText.setText(review.getText());
            reviewDate.setText(parseDate(review.getDate()));
            reviewType.setText(review.getType() + " отзыв");
            setStyles(reviewType, review.getType());
            reviewGoodReaction.setText(String.valueOf(review.getReviewLikes()));
            reviewBadReaction.setText(String.valueOf(review.getReviewDislikes()));
        }
        return view;
    }

    public void setStyles(TextView reviewType, String type){
        if (type.equals("Позитивный")){
            reviewType.setBackgroundResource(R.drawable.rating_box_bg_high);
            reviewType.setTextColor(
                    reviewType.getContext()
                            .getResources()
                            .getColor(R.color.high_rating_color)
            );
        } else if (type.equals("Нейтральный")) {
            reviewType.setBackgroundResource(R.drawable.rating_box_bg_medium);
            reviewType.setTextColor(
                    reviewType.getContext()
                            .getResources()
                            .getColor(R.color.medium_rating_color)
            );
        } else if (type.equals("Негативный")) {
            reviewType.setBackgroundResource(R.drawable.rating_box_bg_low);
            reviewType.setTextColor(
                    reviewType.getContext()
                            .getResources()
                            .getColor(R.color.low_rating_color)
            );
        }
    }

    public static String parseDate(String dateString){
        // Преобразование строки в объект ZonedDateTime
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateString);

        // Создание формата для нужного вывода
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        // Преобразование в строку с нужным форматом
        return zonedDateTime.format(formatter);
    }
}
