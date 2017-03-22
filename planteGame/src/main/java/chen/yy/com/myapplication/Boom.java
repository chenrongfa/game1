package chen.yy.com.myapplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Game
 * Created by chenrongfa on 2017/3/22
 * email:18720979339@163.com
 * qq:952786280
 * company:yy
 */

public class Boom {

	public int getBoomX() {
		return boomX;
	}

	public void setBoomX(int boomX) {
		this.boomX = boomX;
	}

	public int getBoomY() {
		return boomY;
	}

	public void setBoomY(int boomY) {
		this.boomY = boomY;
	}

	public int getTotalFrame() {
		return totalFrame;
	}

	public void setTotalFrame(int totalFrame) {
		this.totalFrame = totalFrame;
	}

	public int getFrameW() {
		return frameW;
	}

	public void setFrameW(int frameW) {
		this.frameW = frameW;
	}

	public int getFrameH() {
		return frameH;
	}

	public void setFrameH(int frameH) {
		this.frameH = frameH;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public boolean isPlayEnd() {
		return isPlayEnd;
	}

	public void setPlayEnd(boolean playEnd) {
		isPlayEnd = playEnd;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	private int boomX,boomY;
	private int totalFrame;
	private  int frameW,frameH;
	private Bitmap bitmap;//爆炸图片
	private boolean isPlayEnd;
	private int currentIndex;


	public Boom(Bitmap bitmap ,int boomX,int boomY,int totalFrame){
		this.bitmap=bitmap;
		this.boomX=boomX;
		this.boomY=boomY;
		this.totalFrame=totalFrame;
		frameW=bitmap.getWidth()/totalFrame;
		frameH=bitmap.getHeight();


	}
	public void draw(Canvas canvas, Paint paint){
		canvas.save();
		canvas.clipRect(boomX,boomY,boomX+frameW,frameH+boomY);
		canvas.drawBitmap(bitmap,boomX-currentIndex*frameW,boomY,paint);
		canvas.restore();

	}
	public void run(){
		if (currentIndex>=totalFrame){
			isPlayEnd=true;
			currentIndex=0;
		}else {
			currentIndex++;
		}


	}


}
