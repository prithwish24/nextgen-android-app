package com.nextgen.carrental.app.android;

import android.app.Fragment;
import android.content.Context;
import android.location.Address;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nextgen.carrental.app.R;
import com.nextgen.carrental.app.constants.GlobalConstants;
import com.nextgen.carrental.app.model.BookingData;
import com.nextgen.carrental.app.model.CarClassEnum;
import com.nextgen.carrental.app.model.WeatherForecast;
import com.nextgen.carrental.app.model.WeekdayEnum;
import com.nextgen.carrental.app.service.RestClient;
import com.nextgen.carrental.app.service.RestParameter;
import com.nextgen.carrental.app.util.SessionManager;
import com.nextgen.carrental.app.util.Utils;

import org.springframework.core.ParameterizedTypeReference;

import java.lang.ref.WeakReference;
import java.util.Date;

/**
 * Confirmation Page
 * @author Prithwish
 *
 */

public class FragmentConfirm extends Fragment {
    public static final String TAG = FragmentConfirm.class.getName();
    private View view;
    private BookingData bookingData;
    private Address address;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_confirmation, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (bookingData != null) {
            ((TextView)view.findViewById(R.id.tv_confNumber)).setText(bookingData.confNum);
            ((TextView)view.findViewById(R.id.tv_pickup_point)).setText(bookingData.pickupLoc);
            ((TextView)view.findViewById(R.id.tv_pickup_date_time)).setText(
                    Utils.fmtTime(bookingData.pickupDateTime, Utils.LONG_DATE_TIME));

            final CarClassEnum carClass = CarClassEnum.find(bookingData.carType);
            if (carClass == null) {
                ((TextView) view.findViewById(R.id.tv_car_type)).setText(bookingData.carType);

            } else {
                ((ImageView) view.findViewById(R.id.iv_carType_thumbnail)).setImageResource(carClass.getImgId());
                ((TextView) view.findViewById(R.id.tv_car_type)).setText(carClass.name());
            }

            getActivity().findViewById(R.id.aiButtonContainer).setVisibility(View.GONE);
            getActivity().findViewById(R.id.aiButtonContainer_topBorder).setVisibility(View.GONE);


            new GetWeatherForecast(getActivity().getApplicationContext(),
                    view, address, bookingData.pickupDateTime).execute();

        } else {
            Toast.makeText(getContext(), "There is a technical difficulties to confirm your booking. " +
                    "Please try again later later or call 800-xxx-xxx for assistance.", Toast.LENGTH_LONG).show();

        }

    }

    public void bindBookingData(final BookingData bookingData) {
        this.bookingData = bookingData;
    }

    public void bindGpsLocation (final Address address) {
        this.address = address;
    }


    private static class GetWeatherForecast extends AsyncTask<Void, Void, WeatherForecast> {
        private WeakReference<Context> contextRef;
        private WeakReference<View> viewRef;
        private Address address;
        private Date pickupDate;

        GetWeatherForecast(final Context context, final View view, final Address address, final Date pickupDate) {
            this.contextRef = new WeakReference<>(context);
            this.viewRef = new WeakReference<>(view);
            this.address = address;
            this.pickupDate = pickupDate;
        }

        @Override
        protected WeatherForecast doInBackground(Void... voids) {
            try {
                final String svcURL = Utils.getServiceURL(contextRef.get(),
                        GlobalConstants.Services.GET_WEATHER_FORECAST);
                final String sessionID = new SessionManager(contextRef.get()).getLoggedInSessionID();

                final RestParameter<Object> param = new RestParameter<>();
                param.addPathParam("sessionId", sessionID);
                if (address != null) {
                    if (address.getLocality() != null) {
                        param.addQueryParam("city", address.getLocality());
                    } else if (address.getAdminArea() != null) {
                        param.addQueryParam("city", address.getAdminArea());
                    }
                }

                ParameterizedTypeReference<WeatherForecast> responseType = new ParameterizedTypeReference<WeatherForecast>() {
                };

                return RestClient.INSTANCE.GET(svcURL, param, responseType);

            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(WeatherForecast wf) {
            super.onPostExecute(wf);
            final View view = viewRef.get();
            final char degreeSymbol = (char) 0x00B0;
            final String dateStr = Utils.fmtTime(pickupDate, Utils.YAHOO_WEATHER_DATE);
            final String curDateStr = Utils.fmtTime(new Date(), Utils.YAHOO_WEATHER_DATE);
            final boolean isBookingToday = TextUtils.equals(dateStr, curDateStr);
            final String tempUnit = wf.getUnits().getTemperature();

            final StringBuilder str = new StringBuilder();

            for (WeatherForecast.Forecast f : wf.getForecastList()) {
                if ( TextUtils.equals(f.getDate().toUpperCase(), dateStr.toUpperCase()) ) {
                    str.append(f.getDate()).append(" ,").append(WeekdayEnum.valueOf(f.getDay()).name());
                    ((TextView) view.findViewById(R.id.tv_weather_date)).setText(str.toString());

                    str.setLength(0);
                    str.append(wf.getLocation().getCity()).append(", ").append(wf.getLocation().getCountry());
                    ((TextView) view.findViewById(R.id.tv_weather_city)).setText(str.toString());

                    ((TextView) view.findViewById(R.id.tv_weather_text)).setText(f.getText());


                    if (isBookingToday) {
                        view.findViewById(R.id.layout_current_temp).setVisibility(View.VISIBLE);

                        str.setLength(0);
                        str.append(wf.getCondition().getTemp()).append(degreeSymbol).append(" ").append(tempUnit);
                        ((TextView) view.findViewById(R.id.tv_weather_temperature)).setText(str.toString());

                        str.setLength(0);
                        str.append("High/Low:   ").append(f.getHigh()).append(degreeSymbol).append("/")
                                .append(f.getLow()).append(degreeSymbol).append(" ").append(tempUnit);
                        ((TextView) view.findViewById(R.id.tv_weather_high_low)).setText(str.toString());

                    } else {
                        view.findViewById(R.id.layout_future_temp).setVisibility(View.VISIBLE);

                        str.setLength(0);
                        str.append("High: ").append(f.getHigh()).append(degreeSymbol).append(" ").append(tempUnit);
                        ((TextView) view.findViewById(R.id.tv_weather_temp_high)).setText(str.toString());

                        str.setLength(0);
                        str.append("Low:  ").append(f.getLow()).append(degreeSymbol).append(" ").append(tempUnit);
                        ((TextView) view.findViewById(R.id.tv_weather_high_low)).setText(str.toString());
                    }

                    break;
                }
            }

        }
    }

}
