package com.itraveller.adapter;

        import android.app.Activity;
        import android.content.Context;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.Filter;
        import android.widget.Filterable;
        import android.widget.TextView;

        import com.android.volley.toolbox.ImageLoader;

        import java.util.ArrayList;
        import java.util.List;

        import com.itraveller.R;
        import com.itraveller.model.PortAndLocModel;
        import com.itraveller.volley.AppController;

public class PortAndLocAdapter extends BaseAdapter implements Filterable {

    private Activity mActivity;
    private LayoutInflater mLayoutInflater;
    public static List<PortAndLocModel> sPortandLocItems;
    public List<PortAndLocModel> portandLocList;
    private List<PortAndLocModel> mFilterPortandLocItems;
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public PortAndLocAdapter(Activity mActivity, List<PortAndLocModel> sPortandLocItems) {
        this.mActivity = mActivity;
        this.sPortandLocItems = sPortandLocItems;
        mFilterPortandLocItems = sPortandLocItems;
    }

    @Override
    public int getCount() {
        return sPortandLocItems.size();
    }

    @Override
    public Object getItem(int location) {
        return sPortandLocItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        if (mLayoutInflater == null)
            mLayoutInflater = (LayoutInflater) mActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = mLayoutInflater.inflate(R.layout.airport_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        TextView title = (TextView) convertView.findViewById(R.id.title);

        // getting data for the row
        PortAndLocModel m = sPortandLocItems.get(position);

        // title
        title.setText(m.getValue());

        return convertView;
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                Log.i("ChangeText", " " + constraint);
                FilterResults results = new FilterResults();

                if (constraint != null && constraint.length() > 0) {
                    portandLocList = new ArrayList<PortAndLocModel>();

                    int filter_loc_items_size=mFilterPortandLocItems.size();
                    for (int i = 0; i < filter_loc_items_size; i++) {
                        if ((mFilterPortandLocItems.get(i).getValue().toUpperCase())
                                .contains(constraint.toString().toUpperCase())) {

                            PortAndLocModel am = new PortAndLocModel();
                            am.setValue(mFilterPortandLocItems.get(i).getValue());
                            am.setKey(mFilterPortandLocItems.get(i).getKey());
                            portandLocList.add(am);
                        }
                    }
                    results.count = portandLocList.size();
                    results.values = portandLocList;
                } else {
                    results.count = mFilterPortandLocItems.size();
                    results.values = mFilterPortandLocItems;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults results) {
                sPortandLocItems = (ArrayList<PortAndLocModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
