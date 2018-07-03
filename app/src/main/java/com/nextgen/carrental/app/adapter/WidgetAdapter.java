package com.nextgen.carrental.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.nextgen.carrental.app.model.BaseWidget;

import java.util.List;

/**
 * Adapter for Widgets
 * @author Prithwish
 */

public class WidgetAdapter extends RecyclerView.Adapter<WidgetAdapter.ViewHolder> {
    private List<BaseWidget> mWidgets;

    public WidgetAdapter(List<BaseWidget> widgets) {
        this.mWidgets = widgets;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public static final class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }

        void bind(BaseWidget baseWidget) {
            
        }
    }
}
