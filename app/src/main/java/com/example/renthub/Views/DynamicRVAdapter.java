package com.example.renthub.Views;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.renthub.R;
import com.example.renthub.Views.DRVinterface.LoadMore;
import com.example.renthub.Views.Models.DynamicRVModel;

import java.util.List;

class LoadingViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar progressBar;

    public LoadingViewHolder(@NonNull View itemView) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.progress_bar);
    }
}

class ItemViewHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public TextView price;
    public ImageView image;


    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        price = itemView.findViewById(R.id.price);
        image = itemView.findViewById(R.id.imageView);

    }
}

public class DynamicRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;
    LoadMore loadMore;
    boolean isLoading;
    Activity activity;
    List<DynamicRVModel> items;
    int visibleThreshold = 5;
    int lastVisibleItem, totalItemCount;
    private RecyclerView recyclerView;

    public DynamicRVAdapter(RecyclerView recyclerView, Activity activity, List<DynamicRVModel> items) {
        this.activity = activity;
        this.items = items;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (loadMore != null)
                        loadMore.onLoadMore();
                    isLoading = true;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setLoadMore(LoadMore loadMore) {
        this.loadMore = loadMore;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity).inflate(R.layout.dynamic_rv_item_layout, parent, false);
            final ItemViewHolder itemViewHolder = new ItemViewHolder(view);

            // Set click listener for the item
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = itemViewHolder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        DynamicRVModel clickedItem = items.get(position);
                        // Show Toast with the content of the clicked item
                        Intent j = new Intent(activity, RentCarActivity.class);
                        j.putExtra("DocumentUID", clickedItem.getDocumentUID());
                        activity.startActivity(j);
                    }
                }
            });

            return itemViewHolder;
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(activity).inflate(R.layout.dynamic_rv_progress_bar, parent, false);
            return new LoadingViewHolder(view);
        }

        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            DynamicRVModel item = items.get(position);
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            viewHolder.name.setText(items.get(position).getName());
            viewHolder.price.setText(items.get(position).getPrice());
            viewHolder.image.setImageResource(R.drawable.rent);
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (com.example.renthub.Views.LoadingViewHolder) holder;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }



}

