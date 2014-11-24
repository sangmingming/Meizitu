package me.isming.meizitu.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import me.isming.meizitu.app.R;
import me.isming.meizitu.data.ImageCacheManager;
import me.isming.meizitu.model.Feed;


/**
 * Created by Sam on 14-3-26.
 */
public class FeedsAdapter extends CursorAdapter {
    private LayoutInflater mLayoutInflater;

    private ListView mListView;

    private Drawable mDefaultImageDrawable = new ColorDrawable(Color.argb(255, 201, 201, 201));

    public FeedsAdapter(Context context, ListView listView) {
        super(context, null, false);
        mLayoutInflater = ((Activity) context).getLayoutInflater();
        mListView = listView;
    }

    @Override
    public Feed getItem(int position) {
        if (mCursor == null || mCursor.getCount() <= position) {
            return null;
        }
        mCursor.moveToPosition(position);
        return Feed.fromCursor(mCursor);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view =  mLayoutInflater.inflate(R.layout.listitem_feed, null);
        Holder holder = new Holder();
        holder.caption = (TextView) view.findViewById(R.id.tv_caption);
        holder.image = (ImageView) view.findViewById(R.id.iv_normal);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Holder holder = (Holder) view.getTag();
        if (holder.imageRequest != null) {
            holder.imageRequest.cancelRequest();
        }

        view.setEnabled(!mListView.isItemChecked(cursor.getPosition()
                + mListView.getHeaderViewsCount()));

        Feed feed = Feed.fromCursor(cursor);
        if(!feed.getImgs().isEmpty()) {
            holder.imageRequest = ImageCacheManager.loadImage(feed.getImgs().get(0), ImageCacheManager
                    .getImageListener(holder.image, mDefaultImageDrawable, mDefaultImageDrawable));
        }
        holder.caption.setText(feed.getName());
    }

    static class Holder {
        ImageView image;

        TextView caption;

        public ImageLoader.ImageContainer imageRequest;
    }
}
