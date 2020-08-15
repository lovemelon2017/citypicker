package com.hanchao.citypicker.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanchao.citypicker.R;
import com.hanchao.citypicker.bean.CityModel;

import java.util.List;

/**
 * @author :小豆豆打飞机
 * @date: 2020/8/15
 * @motto: A good beginning is half the battle
 */
public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {

    private Context context;
    private List<CityModel> data;
    ItemCityClick click;

    public void setClick(ItemCityClick click) {
        this.click = click;
    }

    public void setData(List<CityModel> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public CityAdapter(Context context, List<CityModel> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_city_choose, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String city = data.get(position).getCity();
        holder.name.setText(city);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click != null) {
                    click.cityName(city);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_city_name);

        }
    }

   public interface ItemCityClick {
        void cityName(String name);
    }

}
