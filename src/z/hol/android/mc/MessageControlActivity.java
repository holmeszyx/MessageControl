package z.hol.android.mc;

import z.hol.android.mc.service.ServiceHelper;
import z.hol.android.mc.utils.SettingManager;
import z.hol.android.mc.widget.NumericWheelAdapter;
import z.hol.android.mc.widget.WheelView;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class MessageControlActivity extends Activity implements ServiceHelper.OnBindDoneCallBack, OnClickListener{
	
	private ServiceHelper mServiceHelper;
	private Boolean mIsSetpass = false;
	private PopupWindow passWindow;
	private WheelView wheelView1;
	private WheelView wheelView2;
	private WheelView wheelView3;	
	private View vRoot;
	private TextView txtBack;
	private Activity thisInstance;
	private View vSetPass;
	private View vLog;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisInstance = this;
        setContentView(R.layout.main);
        findView();
        setListener();
       mServiceHelper = new ServiceHelper(this, this); 
       mServiceHelper.startService();
       mServiceHelper.bindService();
       new Handler().postDelayed(new Runnable() {
		
			@Override
			public void run() {
				// TODO Auto-generated method stub
				showPassPopup(false);
			}
		}, 100);
    }
    
    private void findView(){
    	vRoot = findViewById(R.id.root);
    	txtBack = (TextView) findViewById(R.id.back);
    	vSetPass = findViewById(R.id.set_pass);
    	vLog = findViewById(R.id.set_log);
    }
    
    private void setListener(){
    	vSetPass.setOnClickListener(this);
    	vLog.setOnClickListener(this);
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
		//finish();
	}
	
	private void showPassPopup(boolean setpass){
		mIsSetpass = setpass;
		if (passWindow == null){
			View passView = LayoutInflater.from(this).inflate(R.layout.pass_layout, null);
			passWindow = new PopupWindow(passView, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			//passWindow.setContentView(passView);
			wheelView1 = (WheelView) passView.findViewById(R.id.pass1);
			wheelView2 = (WheelView) passView.findViewById(R.id.pass2);
			wheelView3 = (WheelView) passView.findViewById(R.id.pass3);		
			Button btnAction = (Button) passView.findViewById(R.id.pass_action);
			btnAction.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String pass = getPass(wheelView1, wheelView2, wheelView3);
					if (mIsSetpass){
						setPass(pass);
					}else{
						verifyPass(pass);
					}
					//passWindow.dismiss();
				}

			});
			passWindow.setOnDismissListener(new PopupWindow.OnDismissListener(){

				@Override
				public void onDismiss() {
					// TODO Auto-generated method stub
					//txtBack.setVisibility(View.GONE);
					/*
					Animation fadeOut = AnimationUtils.loadAnimation(thisInstance, R.anim.fade_out);
					fadeOut.setAnimationListener(new Animation.AnimationListener(){

						@Override
						public void onAnimationEnd(Animation animation) {
							// TODO Auto-generated method stub
							txtBack.setVisibility(View.GONE);
						}

						@Override
						public void onAnimationRepeat(Animation animation) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onAnimationStart(Animation animation) {
							// TODO Auto-generated method stub
							
						}
						
					});
					txtBack.startAnimation(fadeOut);
					*/
					txtBack.setVisibility(View.GONE);
				}
				
			});
			passWindow.setFocusable(true);
			passWindow.setOutsideTouchable(false);
		}
		
		passWindow.setBackgroundDrawable((setpass) ? new ColorDrawable(Color.TRANSPARENT) : null);
		
		initWheelView(wheelView1);
		initWheelView(wheelView2);
		initWheelView(wheelView3);

		passWindow.showAtLocation(vRoot, Gravity.CENTER, 0, 0);
		txtBack.setVisibility(View.VISIBLE);
		Animation fadeIn = AnimationUtils.loadAnimation(thisInstance, R.anim.fade_in);
		fadeIn.setAnimationListener(new Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				txtBack.setVisibility(View.VISIBLE);
			}
		});
		txtBack.startAnimation(fadeIn);
	}
	
	private void initWheelView(WheelView wheelView){
		if (wheelView.getAdapter() == null){
			wheelView.setAdapter(new NumericWheelAdapter(0, 9));
		}
		wheelView.setVisibleItems(5);
		wheelView.setCyclic(true);
		wheelView.setCurrentItem(0, true);
	}
	
	private void setPass(String pass) {
		// TODO Auto-generated method stub
		SettingManager.get(thisInstance).setPass(pass);
		passWindow.dismiss();
		Toast.makeText(thisInstance, "口令已设置", Toast.LENGTH_SHORT).show();
	}
	
	private void verifyPass(String pass){
		if (SettingManager.get(thisInstance).getPass().equals(pass)){
			passWindow.dismiss();
		}else{
			Toast.makeText(thisInstance, "错误口令", Toast.LENGTH_SHORT).show();
		}
	}
	
	private String getPass(WheelView pass1, WheelView pass2, WheelView pass3){
		int p1 = pass1.getCurrentItem();
		int p2 = pass2.getCurrentItem();
		int p3 = pass3.getCurrentItem();
		return String.format("%d%d%d", p1, p2, p3);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(vSetPass)){
			showPassPopup(true);
			return;
		}
		
		if (v.equals(vLog)){
			Toast.makeText(thisInstance, "还没有做", Toast.LENGTH_SHORT).show();
			return;
		}
	}
}