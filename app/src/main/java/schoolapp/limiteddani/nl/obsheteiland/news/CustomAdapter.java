package schoolapp.limiteddani.nl.obsheteiland.news;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import schoolapp.limiteddani.nl.obsheteiland.MainActivity;
import schoolapp.limiteddani.nl.obsheteiland.R;
import schoolapp.limiteddani.nl.obsheteiland.activity.Article;
import schoolapp.limiteddani.nl.obsheteiland.activity.News;

/**
 * Created by daniq on 7-4-2017.
 */

public class CustomAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener {

    private ArrayList<DataModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtTitle;
        TextView txtDescription;
        ImageView txtImage;
        CardView txtCard;
        TextView txtPDF;
    }

    public CustomAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public void onClick(View v) {

        int position = (Integer) v.getTag();
        Object object = getItem(position);
        DataModel dataModel = (DataModel) object;
        //Open het nieuws
        Intent intent = new Intent(v.getContext(), Article.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("pdf", dataModel.getPDF());
        intent.putExtra("title", dataModel.getTitle());
        v.getContext().startActivity(intent);
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.news_title);
            viewHolder.txtDescription = (TextView) convertView.findViewById(R.id.news_description);
            viewHolder.txtImage = (ImageView) convertView.findViewById(R.id.news_image);
            viewHolder.txtCard = (CardView) convertView.findViewById(R.id.news_card);
            viewHolder.txtPDF = (TextView) convertView.findViewById(R.id.news_pdf);
            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtTitle.setText(dataModel.getTitle());
        viewHolder.txtDescription.setText(dataModel.getDescription());
        Picasso.with(mContext).load(dataModel.getImage()).into(viewHolder.txtImage);
        viewHolder.txtPDF.setText(dataModel.getPDF());

        viewHolder.txtCard.setOnClickListener(this);
        viewHolder.txtCard.setTag(position);
        return convertView;
    }
}