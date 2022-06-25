package com.ynot.farmhelp;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherAdapter1 extends RecyclerView.Adapter<WeatherAdapter1.ViewHolder> {

    private Context context;
    private final ArrayList<WeatherModel1> weatherModelArrayList1;

    public WeatherAdapter1(Context context, ArrayList<WeatherModel1> weatherModelArrayList1) {
        this.context = context;
        this.weatherModelArrayList1 = weatherModelArrayList1;
    }

    @NonNull
    @Override
    public WeatherAdapter1.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_item2,parent,false);
        return new ViewHolder(view);
    }

    //to fetch data from api
    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter1.ViewHolder holder, int position) {

//            int images[] = {R.drawable.morning2, R.drawable.night3};
//
//            if ( position >= 6 && position <= 17 ) {
//                holder.itemView.setBackgroundResource(images[0]);
//            }
//            else {
//                holder.itemView.setBackgroundResource(images[1]);
//            }


        WeatherModel1 modal = weatherModelArrayList1.get(position);
        holder.temperature.setText(modal.getTemperature() + "℃");
        holder.description.setText(modal.getDescription());
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yy");
        try {
            Date d = input.parse(modal.getDate());
            holder.date.setText(output.format(d));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat input1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat output1 = new SimpleDateFormat("hh:mm");
        String s = modal.getTime();
        String h = s.substring(11,13);
        int hour = Integer.parseInt(h);

        if ( hour >= 12 ) {
            try {
                Date t = input1.parse(modal.getTime());
                holder.time.setText(output1.format(t)+ " PM");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                Date t = input1.parse(modal.getTime());
                holder.time.setText(output1.format(t)+ " AM");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

//        if (  hour >= 12) {
//            output1 = new SimpleDateFormat("hh:mm");
//            try {
//                Date t = input1.parse(modal.getTime());
//                holder.time.setText(output1.format(t) + " PM");
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//        else {
//            output1 = new SimpleDateFormat("hh:mm");
//            try {
//                Date t = input1.parse(modal.getTime());
//                holder.time.setText(output1.format(t) + " AM");
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }


        holder.humidity.setText(modal.getHumidity() + "%");
        holder.clouds.setText((modal.getClouds()+ "%"));
        holder.windSpeed.setText(modal.getWindSpeed() + " kph");
        holder.isRain.setText(modal.getIsRain());
    }

    //to return size of arrayList
    @Override
    public int getItemCount() {
        return weatherModelArrayList1.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private TextView date, time, temperature, description, humidity, clouds, windSpeed, isRain;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            temperature = itemView.findViewById(R.id.temperature);
            description = itemView.findViewById(R.id.description);
            humidity = itemView.findViewById(R.id.humidity);
            clouds = itemView.findViewById(R.id.clouds);
            windSpeed = itemView.findViewById(R.id.wind_speed);
            isRain = itemView.findViewById(R.id.is_rain);
        }
    }
}

