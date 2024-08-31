package com.arziman_off.moviesapp;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ReviewsBigAdapter extends RecyclerView.Adapter<ReviewsBigAdapter.ReviewsBigViewHolder> {
    private List<MovieReview> bigReviewsList = new ArrayList<>();

    private OnReviewItemClickListener onReviewItemClickListener;

    public void setBigReviewsList(List<MovieReview> bigReviewsList) {
        this.bigReviewsList = bigReviewsList;
        notifyDataSetChanged();
    }

    public void setOnReviewItemClickListener(OnReviewItemClickListener onReviewItemClickListener) {
        this.onReviewItemClickListener = onReviewItemClickListener;
    }

    @NonNull
    @Override
    public ReviewsBigViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.review_big_item_view,
                        parent,
                        false
                );
        return new ReviewsBigViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsBigViewHolder holder, int position) {
        MovieReview review = bigReviewsList.get(position);

        holder.reviewAuthor.setText(review.getAuthor());
        holder.reviewTitle.setText(review.getTitle());
        holder.reviewText.setText(review.getText());
        holder.reviewType.setText(review.getType());
        setReviewTypeStyles(holder.reviewType);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onReviewItemClickListener != null){
                    onReviewItemClickListener.onReviewClick(review);
                }
            }
        });
    }

    public static void setReviewTypeStyles(TextView reviewType) {
        String type = reviewType.getText().toString();
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

    @Override
    public int getItemCount() {
        return bigReviewsList.size();
    }

    interface OnReviewItemClickListener{
        void onReviewClick(MovieReview movieReview);
    }

    static class ReviewsBigViewHolder extends RecyclerView.ViewHolder {
        private final TextView reviewAuthor;
        private final TextView reviewType;
        private final TextView reviewTitle;
        private final TextView reviewText;
        public ReviewsBigViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewAuthor = itemView.findViewById(R.id.review_author);
            reviewType = itemView.findViewById(R.id.review_type);
            reviewTitle = itemView.findViewById(R.id.review_title);
            reviewText = itemView.findViewById(R.id.review_text);
        }
    }
}
