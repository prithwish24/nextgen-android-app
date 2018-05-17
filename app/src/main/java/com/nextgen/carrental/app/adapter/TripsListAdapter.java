package com.nextgen.carrental.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nextgen.carrental.app.R;
import com.nextgen.carrental.app.bo.TripsResponse;
import com.nextgen.carrental.app.model.CarClassEnum;

import java.util.List;


public class TripsListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<TripsResponse> mDataSource;

    public TripsListAdapter(Context mContext, List<TripsResponse> tripsList) {
        //this.mContext = mContext;
        this.mDataSource = tripsList;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TripsListAdapter.ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.fragment_home, parent, false);

            holder = new TripsListAdapter.ViewHolder();
            holder.thumbnailImageView = convertView.findViewById(R.id.car_image_home);
            holder.carTypeTextView  = convertView.findViewById(R.id.car_type_home);
            holder.pickUpTextView = convertView.findViewById(R.id.pickup_point_home);
            holder.pickUpTimeTextView = convertView.findViewById(R.id.pickup_time_home);
            holder.returnTimeTextView = convertView.findViewById(R.id.drop_time_home);

            convertView.setTag(holder);

        } else {

            holder = (TripsListAdapter.ViewHolder) convertView.getTag();
        }

        final TripsResponse trip = (TripsResponse) getItem(position);
        holder.carTypeTextView.setText(trip.getCarType());
        holder.pickUpTextView.setText(trip.getPickupPoint());
        holder.pickUpTimeTextView.setText(trip.getPickupDateTime());
        holder.returnTimeTextView.setText(trip.getDropoffDateTime());
        final CarClassEnum imgId = CarClassEnum.find(trip.getCarType());
        if (imgId != null) {
            holder.thumbnailImageView.setImageResource(imgId.getImgId());
        }

        return convertView;
    }

    private class ViewHolder {
        ImageView thumbnailImageView;
        TextView carTypeTextView;
        TextView pickUpTextView;
        TextView pickUpTimeTextView;
        TextView returnTimeTextView;
    }
}
