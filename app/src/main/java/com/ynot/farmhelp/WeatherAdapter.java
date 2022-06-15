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

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private Context context;
    private final ArrayList<WeatherModel> weatherModelArrayList;

    public WeatherAdapter(Context context, ArrayList<WeatherModel> weatherModelArrayList) {
        this.context = context;
        this.weatherModelArrayList = weatherModelArrayList;
    }

    @NonNull
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_item,parent,false);
        return new ViewHolder(view);
    }

    //to fetch data from api
    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.ViewHolder holder, int position) {

//            int images[] = {R.drawable.morning2, R.drawable.night3};
//
//            if ( position >= 6 && position <= 17 ) {
//                holder.itemView.setBackgroundResource(images[0]);
//            }
//            else {
//                holder.itemView.setBackgroundResource(images[1]);
//            }


        WeatherModel modal = weatherModelArrayList.get(position);
        holder.temp.setText(modal.getTemperature() + "℃");
        Picasso.get().load("http:".concat(modal.getIcon())).into(holder.conditions);
        holder.wind.setText(modal.getWindSpeed() + "km/hr");
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat output = new SimpleDateFormat("hh:mm aa");
        try {
            Date t = input.parse(modal.getTime());
            holder.time.setText(output.format(t));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //to return size of arrayList
    @Override
    public int getItemCount() {
        return weatherModelArrayList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private TextView time, temp, wind;
        private ImageView conditions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            temp = itemView.findViewById(R.id.temp);
            wind = itemView.findViewById(R.id.windSpeed);
            conditions = itemView.findViewById(R.id.conditions);
        }
    }
}

