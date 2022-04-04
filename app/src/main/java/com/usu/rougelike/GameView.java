package com.usu.rougelike;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.Choreographer;
import android.view.MotionEvent;
import android.view.View;

import com.usu.rougelike.game.RougeLike;
import com.usu.rougelike.game.gameengine.Game;
import com.usu.rougelike.game.gameengine.Input;

public class GameView extends View implements Choreographer.FrameCallback {
    Game game;
    Paint paint = new Paint();
    long time;
    public GameView(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (game == null) {
            time = System.nanoTime();
            game = new RougeLike(getWidth(), getHeight());
            setOnTouchListener((view, motionEvent) -> {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    game.enqueueInput(Input.Type.Touch, motionEvent.getX(), motionEvent.getY());
                    return true;
                }
                return false;
            });
            Choreographer.getInstance().postFrameCallback(this);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        game.render(canvas, paint);
    }

    @Override
    public void doFrame(long l) {
        long deltaT = l - time;
        time = l;

        game.doFrame(deltaT);
        Choreographer.getInstance().postFrameCallback(this);
        invalidate();
    }
}
