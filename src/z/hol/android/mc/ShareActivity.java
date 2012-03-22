package z.hol.android.mc;

import java.util.List;

import z.hol.android.mc.utils.ShareManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;


public class ShareActivity extends BaseActivity{
	
	private Gallery galIcon;
	private GridView gridIcon;
	private IconAdapter mIconAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//galIcon.set
		ShareManager share = new ShareManager(this);
		mIconAdapter = new IconAdapter(share.getShareAppIconsList(), thisInstance, true);
		galIcon.setAdapter(mIconAdapter);
		//galIcon.getse
		IconAdapter gridAdapter = new IconAdapter(share.getShareAppIconsList(), thisInstance, false);
		gridIcon.setNumColumns(gridAdapter.getCount());
		gridIcon.setColumnWidth(81);
		gridIcon.setAdapter(gridAdapter);
	}

	@Override
	protected void setLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.share_layout);
	}

	@Override
	protected void findView() {
		// TODO Auto-generated method stub
		galIcon = (Gallery) findViewById(R.id.share_gallery);
		gridIcon = (GridView) findViewById(R.id.share_grid);
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		
	}
	
	private class IconAdapter extends BaseAdapter{
		private List<Drawable> mIconList;
		private Context mContext;
		private boolean mIsGallery = false;
		
		public IconAdapter(List<Drawable> icons, Context context, boolean isGallery){
			mIconList = icons;
			mContext = context;
			mIsGallery = isGallery;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mIconList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mIconList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (convertView == null){
				ImageView image = new ImageView(mContext);
				image.setAdjustViewBounds(true);
				image.setScaleType(ScaleType.FIT_XY);
				//image.setLayoutParams(new Gallery.LayoutParams(Gallery.LayoutParams.WRAP_CONTENT, Gallery.LayoutParams.WRAP_CONTENT));
				if (mIsGallery){
					image.setLayoutParams(new Gallery.LayoutParams(48, 48));
				}else{
					image.setLayoutParams(new GridView.LayoutParams(48, 48));
				}
				convertView = image;
				holder = new ViewHolder();
				holder.icon = image;
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			Drawable icon = mIconList.get(position);
			holder.icon.setImageDrawable(icon);
			return convertView;
		}
		
		private class ViewHolder{
			ImageView icon;
		}
		
	}
	
	
}
