package z.hol.android.mc;

import android.app.Activity;
import android.os.Bundle;

public abstract class BaseActivity extends Activity{
	
	protected Activity thisInstance;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		thisInstance = this;
		if (!beforeSetLayout()){
			return;
		}
		setLayout();
		findView();
		setListener();
	}
	
	protected boolean beforeSetLayout(){
		return true;
	}
	
	protected abstract void setLayout();
	
	protected abstract void findView();
	
	protected abstract void setListener();
}
