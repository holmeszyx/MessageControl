package z.hol.android.mc;

import z.hol.android.mc.service.ServiceHelper;
import android.app.Activity;
import android.os.Bundle;

public class MessageControlActivity extends Activity implements ServiceHelper.OnBindDoneCallBack{
	
	private ServiceHelper mServiceHelper;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
       mServiceHelper = new ServiceHelper(this, this); 
       mServiceHelper.startService();
       mServiceHelper.bindService();
    }
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	mServiceHelper.unbindService();
    }

	@Override
	public void onDone() {
		// TODO Auto-generated method stub
		finish();
	}
}