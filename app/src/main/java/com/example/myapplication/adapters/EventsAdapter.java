package com.example.myapplication.adapters;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.CountdownRunnable;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ListItemEventBinding;
import com.example.myapplication.models.Event;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {
    private List<Event> events;
    private final List<EventViewHolder> lstHolders;
    private EventsAdapter.FavoriteClick mListener;
    private Handler mHandler = new Handler();
    private Runnable updateRemainingTimeRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (lstHolders) {
                long currentTime = System.currentTimeMillis();
                for (EventViewHolder holder : lstHolders) {
                    holder.updateTimeRemaining(currentTime);
                }
            }
        }
    };

    public EventsAdapter(List<Event> events) {
        this.events = events;
        lstHolders = new ArrayList<>();
        startUpdateTimer();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemEventBinding historyBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_event, parent, false);
        return new EventViewHolder(historyBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        holder.bind(events.get(position), position);
        synchronized (lstHolders) {
            lstHolders.add(holder);
        }
        holder.updateTimeRemaining(System.currentTimeMillis());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public interface FavoriteClick { void onItemClicked(int position);}

    public void setFavoriteClickListener(EventsAdapter.FavoriteClick listener) {
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

    class EventViewHolder extends RecyclerView.ViewHolder {
        private ListItemEventBinding binding;
        private Event currentEvent;

        EventViewHolder(@NonNull ListItemEventBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Event e, int pos) {
            binding.setEvent(e);
            currentEvent = e;
            binding.ivFavorite.setTag(pos);
            binding.ivFavorite.setOnClickListener(mOnClickListener);
            updateTimeRemaining(System.currentTimeMillis());
        }

        public void updateTimeRemaining(long currentTime) {
            long timeDiff = currentEvent.getTt() * 1000L - (currentTime);
            if (timeDiff > 0) {
                long secondsInMilli = 1000;
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;

                long elapsedHours = timeDiff / hoursInMilli;
                timeDiff = timeDiff % hoursInMilli;

                long elapsedMinutes = timeDiff / minutesInMilli;
                timeDiff = timeDiff % minutesInMilli;

                long elapsedSeconds = timeDiff / secondsInMilli;

                binding.tvTimer.setText(String.format(Locale.ROOT, "%d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds));
            } else {
                binding.tvTimer.setText(R.string.timer_zero);
            }
        }
    }

    private void startUpdateTimer() {
        Timer tmr = new Timer();
        tmr.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(updateRemainingTimeRunnable);
            }
        }, 1000, 1000);
    }
}
