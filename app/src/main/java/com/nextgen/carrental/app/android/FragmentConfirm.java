package com.nextgen.carrental.app.android;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.nextgen.carrental.app.R;
import com.nextgen.carrental.app.bo.BaseResponse;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nextgen.carrental.app.constants.GlobalConstants.DEGREE_SYMBOL;
import static com.nextgen.carrental.app.constants.GlobalConstants.Services;

/**
 * Confirmation Page
 * @author Prithwish
 *
 */

public class FragmentConfirm extends Fragment implements View.OnClickListener {
    public static final String TAG = FragmentConfirm.class.getName();
    private View view;
    private BookingData bookingData;

    private Map<String, View> widgetViews = new HashMap<>();
    private Dialog activeDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_confirmation, container, false);
        //register events
        view.findViewById(R.id.widget_promotion).setOnClickListener(this);
        view.findViewById(R.id.widget_weather).setOnClickListener(this);
        view.findViewById(R.id.widget_fuel).setOnClickListener(this);
        view.findViewById(R.id.widget_hotels).setOnClickListener(this);
        view.findViewById(R.id.widget_places).setOnClickListener(this);

        //final LayoutInflater layoutInflater = LayoutInflater.from(getActivity().getApplicationContext());
        final View weatherView = inflater.inflate(R.layout.widget_weather, container, false);
        widgetViews.put("weather", weatherView);
        weatherView.findViewById(R.id.widget_weather_close).setOnClickListener(this);

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
                    view, widgetViews, bookingData).execute();

        } else {
            Toast.makeText(getContext(), "There is a technical difficulties to confirm your booking. " +
                    "Please try again later later or call 800-xxx-xxx for assistance.", Toast.LENGTH_LONG).show();

            /*new GetWeatherForecast(getActivity().getApplicationContext(),
                    view, gpsAddress, bookingData).execute();*/

        }

    }

    public void bindBookingData(final BookingData bookingData) {
        this.bookingData = bookingData;
    }

    @Override
    public void onClick(View v) {
        final int itemId = v.getId();
        if (itemId == R.id.widget_promotion) {
            Toast.makeText(getContext(), "Promotion clicked", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.widget_weather) {
            Toast.makeText(getContext(), "Weather details clicked", Toast.LENGTH_SHORT).show();
            activeDialog = new Dialog(view.getContext());
            activeDialog.setContentView(widgetViews.get("weather"));
            activeDialog.setTitle("Weather Forecast");
            activeDialog.show();

        } else if (itemId == R.id.widget_fuel) {
            Toast.makeText(getContext(), "Fuel info clicked", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.widget_hotels) {
            Toast.makeText(getContext(), "Hotel deals clicked", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.widget_places) {
            Toast.makeText(getContext(), "Attraction clicked", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.widget_weather_close) {
            if (activeDialog != null) activeDialog.dismiss();
        }

    }


    private static class GetWeatherForecast extends AsyncTask<Void, Void, WeatherForecast> {
        private WeakReference<Context> contextRef;
        private WeakReference<View> viewRef;
        private Map<String, View> widgetViews;
        private BookingData bookingData;

        private String serviceURL;
        private RestParameter<Object> param;
        private ParameterizedTypeReference<BaseResponse<WeatherForecast>> responseType;

        private WeatherForecast weatherForecastData;


        private GetWeatherForecast(final Context context, final View view, Map<String, View> widgetViews, final BookingData bookingData) {
            this.contextRef = new WeakReference<>(context);
            this.viewRef = new WeakReference<>(view);
            this.widgetViews = widgetViews;
            this.bookingData = bookingData;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            final String sessionID = new SessionManager(contextRef.get()).getLoggedInSessionID();
            serviceURL = Utils.getServiceURL(contextRef.get(), Services.GET_WEATHER_FORECAST);
            param = new RestParameter<>();
            responseType = new ParameterizedTypeReference<BaseResponse<WeatherForecast>>() {
            };

            // prepare request parameters
            param.addPathParam("sessionId", sessionID);

            if (bookingData != null)
                param.addQueryParam("location", bookingData.pickupLoc);
            else
                param.addQueryParam("city", "St. Louis");
        }

        @Override
        protected WeatherForecast doInBackground(Void... voids) {
            try {
                final BaseResponse<WeatherForecast> baseResponse = RestClient.INSTANCE.GET(serviceURL, param, responseType);
                if (baseResponse.isSuccess()) {
                    return baseResponse.getResponse();
                } else {
                    Log.e(TAG, baseResponse.getError().toString());
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(WeatherForecast wf) {
            super.onPostExecute(wf);
            if (wf != null) {
                this.weatherForecastData = wf;
                final View view = viewRef.get();

                Date dateToMatch;
                if (bookingData != null)
                    dateToMatch = bookingData.pickupDateTime;
                else
                    dateToMatch = new Date();
                final String dateStr = Utils.fmtTime(dateToMatch, Utils.YAHOO_WEATHER_DATE);
                final String curDateStr = Utils.fmtTime(new Date(), Utils.YAHOO_WEATHER_DATE);
                final boolean isBookingToday = TextUtils.equals(dateStr, curDateStr);
                final String tempUnit = wf.getUnits().getTemperature();

                final StringBuilder str = new StringBuilder();

                final WeatherForecast.Forecast forecast = wf.getForecastList().get(0);
                //((ImageView)view.findViewById(R.id.widget_weather_icon)).setImageResource();
                ((TextView) view.findViewById(R.id.widget_weather_text)).setText(wf.getCondition().getText());

                str.setLength(0);
                str.append(wf.getCondition().getTemp()).append(DEGREE_SYMBOL)
                        .append(" ").append(wf.getUnits().getTemperature());
                ((TextView) view.findViewById(R.id.widget_weather_temp)).setText(str);

                str.setLength(0);
                str.append("Min: ").append(forecast.getLow()).append(DEGREE_SYMBOL).append(" ").append(tempUnit);
                ((TextView) view.findViewById(R.id.widget_weather_min)).setText(str);

                str.setLength(0);
                str.append("Max: ").append(forecast.getHigh()).append(DEGREE_SYMBOL).append(" ").append(tempUnit);
                ((TextView) view.findViewById(R.id.widget_weather_max)).setText(str);

//                final View weatherView = widgetViews.get("weather");
//                buildCurrentTemperatureLayout (weatherView, wf.getCondition(), forecast);
//                buildAndPopulateTableData (weatherView, weatherForecastData.getForecastList());

                /*for (WeatherForecast.Forecast f : wf.getForecastList()) {
                    if (TextUtils.equals(f.getDate().toUpperCase(), dateStr.toUpperCase())) {
                        final WeekdayEnum weekday = WeekdayEnum.find(f.getDay());
                        str.append(f.getDate()).append(", ").append(weekday!=null?weekday.name():"");
                        ((TextView) view.findViewById(R.id.tv_weather_date)).setText(str.toString());

                        str.setLength(0);
                        str.append(wf.getLocation().getCity()).append(", ").append(wf.getLocation().getCountry());
                        ((TextView) view.findViewById(R.id.tv_weather_city)).setText(str.toString());

                        ((TextView) view.findViewById(R.id.tv_weather_text)).setText(f.getText());


                        if (isBookingToday) {
                            view.findViewById(R.id.layout_future_temp).setVisibility(View.GONE);
                            view.findViewById(R.id.layout_current_temp).setVisibility(View.VISIBLE);

                            str.setLength(0);
                            str.append(wf.getCondition().getTemp()).append(degreeSymbol).append(" ").append(tempUnit);
                            ((TextView) view.findViewById(R.id.tv_weather_current_temp)).setText(str.toString());

                            str.setLength(0);
                            str.append("High: ").append(f.getHigh()).append(degreeSymbol).append(" ").append(tempUnit);
                            ((TextView) view.findViewById(R.id.tv_weather_current_high)).setText(str.toString());

                            str.setLength(0);
                            str.append("Low : ").append(f.getLow()).append(degreeSymbol).append(" ").append(tempUnit);
                            ((TextView) view.findViewById(R.id.tv_weather_current_low)).setText(str.toString());

                        } else {
                            view.findViewById(R.id.layout_current_temp).setVisibility(View.GONE);
                            view.findViewById(R.id.layout_future_temp).setVisibility(View.VISIBLE);

                            str.setLength(0);
                            str.append("High: ").append(f.getHigh()).append(degreeSymbol).append(" ").append(tempUnit);
                            ((TextView) view.findViewById(R.id.tv_weather_future_high)).setText(str.toString());

                            str.setLength(0);
                            str.append("Low : ").append(f.getLow()).append(degreeSymbol).append(" ").append(tempUnit);
                            ((TextView) view.findViewById(R.id.tv_weather_future_low)).setText(str.toString());
                        }

                        break;
                    }
                }*/


            }

        }

        private void buildCurrentTemperatureLayout(View weatherView, WeatherForecast.Condition condition, WeatherForecast.Forecast forecast) {
            /*final LinearLayout layout = viewRef.get().findViewById(R.id.weather_frame_current);
            final Resources resources = viewRef.get().getResources();

            TextView tv_weather_text = new TextView(contextRef.get());
            tv_weather_text.setText(condition.getText());
            tv_weather_text.setTextSize(16);
            tv_weather_text.setTextColor(resources.getColor(R.color.orange5, null));
            layout.addView(tv_weather_text, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            TextView tv_weather_current_high_low = new TextView(contextRef.get());
            tv_weather_current_high_low.setText(condition.getText());
            tv_weather_current_high_low.setTextSize(14);
            tv_weather_current_high_low.setTextColor(resources.getColor(R.color.darkGray, null));
            layout.addView(tv_weather_current_high_low, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));*/


            ((TextView)weatherView.findViewById(R.id.tv_weather_text)).setText(condition.getText());
            ((TextView)weatherView.findViewById(R.id.tv_weather_current_low)).setText(condition.getText()+DEGREE_SYMBOL);
            ((TextView)weatherView.findViewById(R.id.tv_weather_current_high)).setText(condition.getText()+DEGREE_SYMBOL);
            ((TextView)weatherView.findViewById(R.id.tv_weather_current_temp)).setText(condition.getTemp()+DEGREE_SYMBOL);

        }

        private void buildAndPopulateTableData(View weatherView, List<WeatherForecast.Forecast> fList) {
            final TableLayout tabLayout = weatherView.findViewById(R.id.weather_table_current);
            TableRow trDay;
            TextView tv;

            for (WeatherForecast.Forecast f: fList) {
                final WeekdayEnum weekday = WeekdayEnum.find(f.getDay());
                final String tempMinMax = f.getLow() + DEGREE_SYMBOL
                        + "  " + f.getHigh() + DEGREE_SYMBOL;

                trDay = new TableRow(contextRef.get());

                tv = new TextView(contextRef.get());
                tv.setText(weekday==null?"":weekday.getDay());
                trDay.addView(tv);

                tv = new TextView(contextRef.get());
                tv.setText(f.getText());
                tv.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                trDay.addView(tv);

                tv = new TextView(contextRef.get());
                tv.setText(tempMinMax);
                tv.setTextAlignment(TextView.TEXT_ALIGNMENT_TEXT_END);
                trDay.addView(tv);

                tabLayout.addView(trDay, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

            }

        }
        
    }

}
