package com.example.imagesearch;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imagesearch.model.Image;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {

    private final String TAG = "TAG";
    private final List<Image> images;
    private final LayoutInflater inflater;

    public ImagesAdapter(Context context, List<Image> images) {
        this.images = images;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Image image = images.get(position);

        holder.imageView.setOnClickListener(v -> {
            Log.d(TAG, "onClick: " + image.getLink());
            MainActivity.recyclerView.setVisibility(View.GONE);
            MainActivity.bigImage.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(image.getLink())
                    .error(R.drawable.error_drawable)
                    .into(MainActivity.bigImage);
        });

        Picasso.get()
                .load(image.getLink())
                .error(R.drawable.error_drawable)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView imageView;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.item_image);
        }

    }
}
