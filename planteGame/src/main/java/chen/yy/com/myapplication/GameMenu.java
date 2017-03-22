package chen.yy.com.myapplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Region;
import android.util.Log;
import android.view.MotionEvent;

import static android.content.ContentValues.TAG;

/**
 * Game
 * Created by chenrongfa on 2017/3/21
 * email:18720979339@163.com
 * qq:952786280
 * company:yy
 */

public class GameMenu {
	private  boolean isPress;
	private Bitmap bgBtn;
	private Bitmap bgBtn_press;
	private Bitmap bgMenu;
	private int btnX;
	private int btnY;
	private Paint paint;

	private Region region;


	public GameMenu(Bitmap bgMenu,Bitmap bgBtn,Bitmap bgBtn_press){
		this.bgBtn=bgBtn;
		this.bgBtn_press=bgBtn_press;
		this.bgMenu=bgMenu;
		btnX=bgMenu.getWidth()/2-bgBtn.getWidth()/2;
		btnY=bgMenu.getHeight()-bgBtn.getHeight();
		paint=new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.FILL);

		region=new Region();
		region.set(btnX,btnY,btnX+bgBtn.getWidth(),btnY+bgBtn.getHeight());
		isPress=false;
	}
	public void draw(Canvas canvas){

		canvas.drawBitmap(bgMenu,0,0,paint);

		 if (isPress){
			 canvas.drawBitmap(bgBtn_press,btnX,btnY,paint);
		 }else{
			 canvas.drawBitmap(bgBtn,btnX,btnY,paint);
		 }



	}
   public void ontouch(MotionEvent ev){
	   int eventX= (int) ev.getX();
	   int eventY= (int) ev.getY();
	   if(ev.getAction()==MotionEvent.ACTION_DOWN) {
		   if (region.contains(eventX, eventY)) {
			   isPress = true;
			   Log.e(TAG, "ontouch:11 ");

			   PlaneGameSurface.gameState = PlaneGameSurface.GAME_PLAYING;
		   } else {
			   isPress = false;
			   Log.e(TAG, "ontouch: 12");
		   }

	   }else if (ev.getAction()==MotionEvent.ACTION_MOVE){
		   if (region.contains(eventX, eventY)) {
			   isPress = true;
			   Log.e(TAG, "ontouch:11 ");
			   PlaneGameSurface.gameState = PlaneGameSurface.GAME_PLAYING;
		   } else {
			   isPress = false;
			   Log.e(TAG, "ontouch: 12");
		   }
	   }



   }

}
