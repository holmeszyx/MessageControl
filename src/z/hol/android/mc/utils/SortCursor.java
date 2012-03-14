package z.hol.android.mc.utils;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;

import com.pinyin4android.PinyinUtil;

public class SortCursor extends CursorWrapper{
	Cursor mCursor;
	ArrayList<SortEntry> sortList = new ArrayList<SortEntry>();
	int mPos = -1;
	
	public class SortEntry {
		public String key;
		public int order;
	}
	
	//直接初始化,加快比较速度,在G3上从3s->0.2s
	@SuppressWarnings("rawtypes")
	private Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);
	
	@SuppressWarnings("unchecked")	
	public Comparator<SortEntry> comparator = new Comparator<SortEntry>(){		
		@Override
		public int compare(SortEntry entry1, SortEntry entry2) {			
			return cmp.compare(entry1.key, entry2.key);       
		}	
	};

	public SortCursor(Context context, Cursor cursor,String columnName) {		
		this(context, cursor, cursor.getColumnIndexOrThrow(columnName));
	}
	
	public SortCursor(Context context, Cursor cursor,int columnIndex){
		super(cursor);
		mCursor = cursor;
		if(mCursor != null && mCursor.getCount() > 0) {
			int i = 0;
			int column = columnIndex;
			for(mCursor.moveToFirst();!mCursor.isAfterLast();mCursor.moveToNext(),i++){
				SortEntry sortKey = new SortEntry();
				//sortKey.key = cursor.getString(column);
				sortKey.key = PinyinUtil.toPinyin(context, cursor.getString(column));
				sortKey.order = i;
				sortList.add(sortKey);
			}
		}
		
		//排序
		Collections.sort(sortList,comparator);
	}
	
    public boolean moveToPosition(int position)
    {
    	if(position >= 0 && position < sortList.size()){
    		mPos = position;
    		int order = sortList.get(position).order;
    		return mCursor.moveToPosition(order);
    	}
    	if(position < 0){
    		mPos = -1;
    		return false;
    	}
    	if(position >= sortList.size()){
    		mPos = sortList.size();
    		return false;
    	}
    	if (position == mPos){
    		return true;
    	}
    	return mCursor.moveToPosition(position);        
    }
    
    public boolean moveToFirst() {    	
        return moveToPosition(0);
    }
    
    public boolean moveToLast(){
    	return moveToPosition(getCount() - 1);
    }
    
    public boolean moveToNext() {    	    	
        return moveToPosition(mPos+1);
    }
    
    public boolean moveToPrevious() {    	
        return moveToPosition(mPos-1);
    }
	
    public boolean move(int offset) {    	
        return moveToPosition(mPos + offset);
    }
    
    public int getPosition() {    	
    	return mPos;
    }
}
