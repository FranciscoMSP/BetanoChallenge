package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ListItemSportBinding;
import com.example.myapplication.models.Event;
import com.example.myapplication.models.Sport;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SportsAdapter extends RecyclerView.Adapter<SportsAdapter.SportViewHolder> {
    private List<Sport> sports;
    private Context context;
    private ArrowClick mListener;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    public SportsAdapter(List<Sport> sports, Context context) {
        this.sports = sports;
        this.context = context;
    }

    @NonNull
    @Override
    public SportsAdapter.SportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemSportBinding sportBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_sport, parent, false);
        return new SportsAdapter.SportViewHolder(sportBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SportsAdapter.SportViewHolder holder, int position) {
        holder.bind(sports.get(position), position);
    }

    @Override
    public int getItemCount() {
        return sports.size();
    }

    public interface ArrowClick { void onItemClicked(int position);}

    public void setArrowClickListener(ArrowClick listener) {
        mListener = listener;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int pos = (int) view.getTag();

            if(mListener != null) {
                mListener.onItemClicked(pos);
            }
        }
    };

    class SportViewHolder extends RecyclerView.ViewHolder {
        private ListItemSportBinding binding;

        SportViewHolder(@NonNull ListItemSportBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.ivArrow.setOnClickListener(mOnClickListener);
        }

        void bind(Sport s, int pos) {
            binding.setSport(s);
            binding.ivArrow.setTag(pos);

            EventsAdapter eventsAdapter = new EventsAdapter(s.getE());

            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    binding.getRoot().getContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
            );
            layoutManager.setInitialPrefetchItemCount(s.getE().size());

            binding.rvEvents.setLayoutManager(layoutManager);
            binding.rvEvents.setAdapter(eventsAdapter);
            //binding.rvEvents.setRecycledViewPool(viewPool);
            eventsAdapter.setFavoriteClickListener(position -> {
                sports.get(pos).getE().get(position).setFavorite(!sports.get(pos).getE().get(position).isFavorite());
                Collections.sort(sports.get(pos).getE(), (event, t1) -> Boolean.compare(t1.isFavorite(), event.isFavorite()));
                eventsAdapter.notifyDataSetChanged();
            });
        }
    }
}
