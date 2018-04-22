package com.nextgen.carrental.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nextgen.carrental.app.R;
import com.nextgen.carrental.app.model.Reservation;

import java.util.HashMap;
import java.util.List;

/**
 * Reservation List Adapter
 * @author Prithwish
 */

public class ReservationListAdapter extends BaseAdapter {

    //public static final String TAG = ReservationListAdapter.class.getSimpleName();
    private static final HashMap <String, Integer> CAR_IMAGES = new HashMap <String, Integer> () {
        {
            put("COMPACT", R.drawable.ct_compact);
            put("STANDARD", R.drawable.ct_standard);
            put("PREMIUM", R.drawable.ct_premium);
        }
    };

    //private Context mContext;
    private LayoutInflater mInflater;
    private List<Reservation> mDataSource;

    public ReservationListAdapter(Context mContext, List<Reservation> resList) {
        //this.mContext = mContext;
        this.mDataSource = resList;
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
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.show_reservation_item, parent, false);

            holder = new ViewHolder();
            holder.thumbnailImageView = convertView.findViewById(R.id.res_list_thumbnail);
            holder.statusTextView = convertView.findViewById(R.id.res_list_status);
            holder.carTypeTextView  = convertView.findViewById(R.id.res_list_car_type);
            holder.pickUpTextView = convertView.findViewById(R.id.res_list_pickup);
            holder.pickUpTimeTextView = convertView.findViewById(R.id.res_list_pickup_time);
            holder.returnTimeTextView = convertView.findViewById(R.id.res_list_return_time);

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        final Reservation res = (Reservation) getItem(position);
        holder.statusTextView.setText(res.getStatus());
        holder.carTypeTextView.setText(res.getCarType());
        holder.pickUpTextView.setText(res.getPickUpPoint());
        holder.pickUpTimeTextView.setText(res.getPickUpTime());
        holder.returnTimeTextView.setText(res.getDropOffTime());
        final Integer imgId = CAR_IMAGES.get(res.getCarType().toUpperCase());
        if (imgId != null) {
            holder.thumbnailImageView.setImageResource(imgId);
        }

        return convertView;
    }

    private class ViewHolder {
        ImageView thumbnailImageView;
        TextView statusTextView;
        TextView carTypeTextView;
        TextView pickUpTextView;
        TextView pickUpTimeTextView;
        TextView returnTimeTextView;
    }

}
