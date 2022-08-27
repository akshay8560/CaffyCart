package com.example.caffycart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BucketAdapter extends RecyclerView.Adapter<BucketAdapter.BucketViewHolder> {

    private final CoffeeBucket[] bucket;

    public BucketAdapter(CoffeeBucket[] bucket) {
        this.bucket = bucket;
    }

    @Override
    public int getItemCount() {
        return bucket.length;
    }

    @NonNull
    @Override
    public BucketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coffee_list_activity, parent, false);
        return new BucketViewHolder(view) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull BucketViewHolder holder, int position) {
        holder.bind(bucket[position], position);
    }

    static class BucketViewHolder extends RecyclerView.ViewHolder {

        private final ImageView thingImage;
        private final TextView thingTitle, thingDescription;

        public BucketViewHolder(@NonNull View itemView) {
            super(itemView);
            thingImage = itemView.findViewById(R.id.image_view_project_icon);
            thingTitle = itemView.findViewById(R.id.text_view_coffee_name);
            thingDescription = itemView.findViewById(R.id.text_view_coffee_description);
        }

        public void bind(CoffeeBucket bucketElement, int position) {
            String heading = (position + 1) + ". " + bucketElement.name;
            thingTitle.setText(heading);
            thingDescription.setText(bucketElement.description);
            thingImage.setImageResource(bucketElement.image);
        }
    }
}