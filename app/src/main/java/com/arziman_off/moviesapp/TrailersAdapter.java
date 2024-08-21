package com.arziman_off.moviesapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersViewHolder> {
    private List<MovieTrailer> trailers = new ArrayList<>();

    public void setTrailers(List<MovieTrailer> trailers) {
        this.trailers = trailers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TrailersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.trailer_item_view,
                        parent,
                        false
                );
        return new TrailersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailersViewHolder holder, int position) {
        MovieTrailer movieTrailer = trailers.get(position);
        holder.tvTrailerName.setText(movieTrailer.getName());
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    static class TrailersViewHolder extends RecyclerView.ViewHolder{
        private final TextView tvTrailerName;
        public TrailersViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTrailerName = itemView.findViewById(R.id.trailerName);
        }
    }
}
