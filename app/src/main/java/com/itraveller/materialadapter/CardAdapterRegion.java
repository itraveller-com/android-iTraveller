package com.itraveller.materialadapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.itraveller.R;
import com.itraveller.model.TestModelData;
import com.itraveller.volley.AppController;
import java.util.List;

/**
 * Created by I TRAVELLES on 30-10-2015.
 */
public class CardAdapterRegion extends RecyclerView.Adapter<CardAdapterRegion.ViewHolder> {

    List<TestModelData> mItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CardAdapterRegion(){
        super();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.material_region_place_card_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        TestModelData nature = mItems.get(i);
        viewHolder.tvTitle.setText(nature.getTitle());
        viewHolder.tvNoOfNights.setText(nature.getNoOfNights());
        viewHolder.tvDestination.setText(nature.getDestination());
        viewHolder.imgThumbnail.setImageResource(nature.getThumbnail());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgThumbnail;
        public TextView tvTitle;
        public TextView tvNoOfNights;
        public TextView tvDestination;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView)itemView.findViewById(R.id.thumbnail);
            tvTitle = (TextView)itemView.findViewById(R.id.title);
            tvNoOfNights=(TextView) itemView.findViewById(R.id.no_of_nights);
            tvDestination=(TextView) itemView.findViewById(R.id.destination);
            //tvDesNature = (TextView)itemView.findViewById(R.id.tv_des_nature);
        }
    }
}
