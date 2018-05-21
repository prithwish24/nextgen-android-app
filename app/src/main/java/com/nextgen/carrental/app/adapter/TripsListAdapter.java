package com.nextgen.carrental.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nextgen.carrental.app.R;
import com.nextgen.carrental.app.model.BookingData;
import com.nextgen.carrental.app.model.CarClassEnum;
import com.nextgen.carrental.app.util.Utils;

import java.util.List;

public class TripsListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private List<BookingData> mDataSource;

    public TripsListAdapter(Context mContext, List<BookingData> tripsList) {
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

    public void setItemList(List<BookingData> itemList) {
        this.mDataSource = itemList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TripsListAdapter.ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_show_trips, parent, false);

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

        final BookingData trip = (BookingData) getItem(position);
        holder.carTypeTextView.setText(trip.carType);
        holder.pickUpTextView.setText(trip.pickupLoc);
        holder.pickUpTimeTextView.setText(Utils.fmtTime(trip.pickupDateTime, Utils.LONG_DATE_TIME));
        holder.returnTimeTextView.setText(Utils.fmtTime(trip.pickupDateTime, Utils.LONG_DATE_TIME));
        final CarClassEnum imgId = CarClassEnum.find(trip.carType);
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
