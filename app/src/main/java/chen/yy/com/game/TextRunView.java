package chen.yy.com.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

/**
 * Game
 * Created by chenrongfa on 2017/3/20
 * email:18720979339@163.com
 * qq:952786280
 * company:yy
 */

public class TextRunView extends View {
	public TextRunView(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.GREEN);
		paint.setTextSize(20);
		Path path=new Path();
		canvas.save();
		canvas.rotate(45,getWidth()/2,getHeight()/2);
		path.addCircle(getWidth()/2,getHeight()/2,50, Path.Direction.CW);
		canvas.drawTextOnPath("woshicherongfa",path,10,20,paint);
		super.onDraw(canvas);
		paint.setTextAlign(Paint.Align.CENTER);
		Paint paint1=new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.GREEN);
		paint.setTextSize(20);
		paint1.setTextAlign(Paint.Align.CENTER);
		canvas.restore();
		canvas.drawText("miaodian",100,100 ,paint);
		canvas.save();

		canvas.drawText("miaodian",100,100 ,paint1);


	}
}
