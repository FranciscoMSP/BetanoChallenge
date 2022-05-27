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
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {
    private List<Event> events;
    private EventsAdapter.FavoriteClick mListener;
    private Handler handler = new Handler();
    //private ScheduledFuture updateFuture;

    public EventsAdapter(List<Event> events) {
        this.events = events;
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

    public void clearAll() {
        handler.removeCallbacksAndMessages(null);
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        private ListItemEventBinding binding;
        CountdownRunnable countdownRunnable;

        EventViewHolder(@NonNull ListItemEventBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            countdownRunnable = new CountdownRunnable(handler, binding.tvTimer,10000);
        }

        void bind(Event e, int pos) {
            binding.setEvent(e);
            binding.ivFavorite.setTag(pos);
            binding.ivFavorite.setOnClickListener(mOnClickListener);

            //updateTimeRemaining(e.getTt(), binding.tvTimer);

  /*          handler.removeCallbacks(countdownRunnable);
            countdownRunnable.holder = binding.tvTimer;
            countdownRunnable.millisUntilFinished = e.getTt(); //because i want all timers run separately.
            handler.postDelayed(countdownRunnable, 100);*/

//            if (updateFuture == null) {
//                final Handler mainHandler = new Handler(Looper.getMainLooper());
//                updateFuture = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
//                    @Override
//                    public void run() {
//                        setDefferinceTimer(binding , e.getTt());
//                        notifyDataSetChanged();
//                        mainHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                notifyDataSetChanged();
//                            }
//                        });
//                    }
//                }, 0, 1000, TimeUnit.MILLISECONDS);
//            }

//            if (timer != null) {
//                timer.cancel();
//            }
//
//            timer = new CountDownTimer(e.getTt(), 1000) {
//                public void onTick(long millisUntilFinished) {
////                    final long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
////                    final long minute = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
////                    final long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
//
//                    int hours = (int) (millisUntilFinished / (60 * 60 * 1000));
//                    int minutes = (int) (millisUntilFinished / (60 * 1000)) % 60;
//                    int seconds = (int) (millisUntilFinished / 1000) % 60;
//
//                    SimpleDateFormat sdf = new SimpleDateFormat("HH:MM:SS");
//
//// Edit: setting the UTC time zone
////                    TimeZone utc = TimeZone.getTimeZone("UTC");
////                    sdf.setTimeZone(utc);
//
//                    Date date = new Date(millisUntilFinished);
//                    //System.out.println(sdf.format(date));
//
//                    long diffSeconds;
//                    long diffMinutes;
//                    long diffHours;
//                    diffSeconds = millisUntilFinished / 1000 % 60;
//                    diffMinutes = millisUntilFinished / (60 * 1000) % 60;
//                    diffHours = millisUntilFinished / (60 * 60 * 1000) % 24;
//                    binding.tvTimer.setText(sdf.format(date)); //String.format("%02d:%02d:%02d", hours, minutes, seconds)
//                }
//
//                public void onFinish() {
//                    binding.tvTimer.setText("00:00:00");
//                }
//            }.start();

            handler.postDelayed(new UpdateTimerThread(binding, e),0);
        }
    }

    private void updateTimeRemaining(long endTime, TextView yourTextView) {

        long timeDiff = endTime - System.currentTimeMillis();
        if (timeDiff > 0) {
            int seconds = (int) (timeDiff / 1000) % 60;
            int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
            int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);

            yourTextView.setText(MessageFormat.format("{0}:{1}:{2}", hours, minutes, seconds));
        } else {
            yourTextView.setText("00:00:00");
        }
    }

    class UpdateTimerThread implements Runnable{
        //Handler handler;
        ListItemEventBinding binding;
        Event event;

    public UpdateTimerThread(ListItemEventBinding binding, Event event) { // , Handler handler
            this.binding = binding;
            this.event = event;
            //this.handler = handler;
        }
        @Override
        public void run() {
            long countdown = event.getTt() - System.currentTimeMillis();
            long diffSeconds;
            long diffMinutes;
            long diffHours;
            diffSeconds = (int) (countdown / 1000) % 60; //countdown / 1000 % 60;
            diffMinutes = (int) ((countdown / (1000 * 60)) % 60); //countdown / (60 * 1000) % 60;
            diffHours = (int) ((countdown / (1000 * 60 * 60)) % 24); //countdown / (60 * 60 * 1000) % 24;
            binding.tvTimer.setText(String.format("%04d:%02d:%02d", diffHours,   diffMinutes, diffSeconds));
            handler.postDelayed(this,1000);
        }
    }
}
