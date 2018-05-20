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

/**
 * Reservation List Adapter
 * @author Prithwish
 */

public class ReservationListAdapter extends BaseAdapter {
    public static final String TAG = ReservationListAdapter.class.getSimpleName();

    private Context mContext;
    private List<BookingData> mDataSource;

    public ReservationListAdapter(Context context, List<BookingData> resList) {
        this.mDataSource = resList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public BookingData getItem(int position) {
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
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        // Need to place logic for zero element

        final BookingData data = getItem(position);
        holder.statusTextView.setText(data.status);
        holder.carTypeTextView.setText(data.carType);
        holder.pickUpTextView.setText(data.pickupLoc);
        holder.pickUpTimeTextView.setText(Utils.fmtTime(data.pickupDateTime, Utils.LONG_DATE_TIME));
        holder.returnTimeTextView.setText(Utils.fmtTime(data.returnDateTime, Utils.LONG_DATE_TIME));
        final CarClassEnum carClass = CarClassEnum.find(data.carType);
        if (carClass != null)
            holder.thumbnailImageView.setImageResource(carClass.getImgId());

        return convertView;
    }

    public void setItemList(List<BookingData> itemList) {
        this.mDataSource = itemList;
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
