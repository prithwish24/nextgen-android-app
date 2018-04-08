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
            put("midsize", R.mipmap.ctyp_compact);
            put("standard", R.mipmap.ctyp_standard);
            put("premium", R.mipmap.ctyp_premium);
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
            convertView = mInflater.inflate(R.layout.listview_show_reservation, parent, false);

            holder = new ViewHolder();
            holder.thumbnailImageView = convertView.findViewById(R.id.res_list_thumbnail);
            holder.numberTextView = convertView.findViewById(R.id.res_list_number);
            holder.carTypeTextView  = convertView.findViewById(R.id.res_list_cartype);
            holder.pickUpTextView = convertView.findViewById(R.id.res_list_pickup);
            holder.dropOffTextView = convertView.findViewById(R.id.res_list_dropoff);

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        final Reservation res = (Reservation) getItem(position);
        holder.numberTextView.setText("Res# " + res.getNumber());
        holder.carTypeTextView.setText("Type: " + res.getCarType());
        holder.pickUpTextView.setText("From: "+res.getPickUpPoint()+" @ "+res.getPickUpTime());
        holder.dropOffTextView.setText("To: "+res.getDropOffPoint()+" @ "+res.getDropOffTime());
        final Integer imgId = CAR_IMAGES.get(res.getCarType().toLowerCase());
        if (imgId != null) {
            holder.thumbnailImageView.setImageResource(imgId);
        }

        return convertView;
    }

    private class ViewHolder {
        ImageView thumbnailImageView;
        TextView numberTextView;
        TextView carTypeTextView;
        TextView pickUpTextView;
        TextView dropOffTextView;
    }

}
