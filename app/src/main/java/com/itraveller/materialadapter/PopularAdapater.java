package com.itraveller.materialadapter;

/**
 * Created by iTraveller on 10/28/2015.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.itraveller.R;
import com.itraveller.activity.MaterialRegionPlace;
import com.itraveller.model.LandingModel;
import com.itraveller.volley.AppController;
import java.util.List;


public class PopularAdapater extends RecyclerView.Adapter<PopularAdapater.ViewHolder> {

    List<LandingModel> mItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    Activity activity;
    SharedPreferences.Editor editor;

    public PopularAdapater (List<LandingModel> LandingItems, Activity activity) {
        super();
        this.mItems = LandingItems;
        this.activity = activity;
        SharedPreferences sharedpreferences = activity.getSharedPreferences("Itinerary", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
          View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.searchbar_popular, viewGroup, false);
          return new ViewHolder(v);
    }
    //    need to override this method
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final LandingModel nature = mItems.get(i);
        viewHolder.tvNature.setText(nature.getRegion_Name());
        viewHolder.tvimageIcon.setImageResource(R.drawable.ic_star);
        viewHolder.tvNature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent i = new Intent(activity, MaterialRegionPlace.class);
                i.putExtra("RegionID", nature.getRegion_Id());
                i.putExtra("RegionName", nature.getRegion_Name());
                editor.putString("FlightBit", "" + nature.getHome_Page());
                editor.commit();
                activity.startActivity(i);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvNature;
        public ImageView tvimageIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNature = (TextView) itemView.findViewById(R.id.edt_tool_search);
            tvimageIcon = (ImageView) itemView.findViewById(R.id.img_tool_back);
            //tvDesNature = (TextView)itemView.findViewById(R.id.tv_des_nature);
        }
    }

}
