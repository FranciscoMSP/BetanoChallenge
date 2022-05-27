package com.example.myapplication;

import android.os.Handler;
import android.widget.TextView;

public class CountdownRunnable implements Runnable {
    public long millisUntilFinished;
    public TextView holder;
    Handler handler;

    public CountdownRunnable(Handler handler, TextView holder, long millisUntilFinished) {
        this.handler = handler;
        this.holder = holder;
        this.millisUntilFinished = millisUntilFinished;
    }

    @Override
    public void run() {
        /* do what you need to do */
//        long seconds = millisUntilFinished / 1000;
//        long minutes = seconds / 60;
//        long hours = minutes / 60;
//        long days = hours / 24;
//        String time = days + " " + "days" + " :" + hours % 24 + ":" + minutes % 60 + ":" + seconds % 60;
//        holder.setText(time);

        long diffSeconds;
        long diffMinutes;
        long diffHours;
        diffSeconds = millisUntilFinished / 1000 % 60;
        diffMinutes = millisUntilFinished / (60 * 1000) % 60;
        diffHours = millisUntilFinished / (60 * 60 * 1000) % 24;
        holder.setText(String.format("%04d:%02d:%02d", diffHours,   diffMinutes, diffSeconds));

        millisUntilFinished -= 1000;

        handler.postDelayed(this, 1000);
    }
}