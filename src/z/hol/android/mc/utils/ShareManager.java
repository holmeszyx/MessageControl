package z.hol.android.mc.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

public class ShareManager {

	private Context mContext;
	
	public ShareManager(Context context){
		mContext = context;
	}
	
	public List<ResolveInfo> getShareAppList(){
		PackageManager pm = mContext.getPackageManager();
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/plain");
		List<ResolveInfo> shareAppList =  pm.queryIntentActivities(i, PackageManager.GET_INTENT_FILTERS);
		return shareAppList;
	}
	
	public List<Drawable> getAppIcons(List<ResolveInfo> appList, PackageManager pm){
		List<Drawable> iconList = null;
		if (appList != null){
			iconList = new ArrayList<Drawable>();
			for (int i = 0; i < appList.size(); i ++){
				Drawable icon = appList.get(i).loadIcon(pm);
				iconList.add(icon);
			}
		}
		return iconList;
	}
	
	public List<Drawable> getShareAppIconsList(){
		return getAppIcons(getShareAppList(), mContext.getPackageManager());
	}
}
