package com.meetup.ballbounce.android;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;


public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
    }

    public class MyView extends View {
        int screenWidth;
        int screenHeight;
        Point ball;
        Point ballVelocity;
        int ballRadius;
        int gravity;
        int friction;

        Paint backgroundPaint;
        Paint ballPaint;
        double totalElapsedTime;

        public MyView(Context context) {
            super(context);

            backgroundPaint = new Paint();
            backgroundPaint.setColor(Color.BLACK);

            ballPaint = new Paint();
            ballPaint.setColor(Color.GREEN);

        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh)
        {
            super.onSizeChanged(w, h, oldw, oldh);
            screenWidth = w;
            screenHeight = h;

            // Initialize Ball info
            if (screenWidth < screenHeight)
                ballRadius = (int)(screenWidth * .05);
            else
                ballRadius = (int)(screenHeight * .05);

            updatePosition();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);
            canvas.drawCircle(ball.x, ball.y, ballRadius, ballPaint);

            Point currLoc = new Point();
            currLoc.x = ball.x;
            currLoc.y = ball.y;

            if (ballVelocity.x > 0)
                ballVelocity.x -= friction;
            else
                ballVelocity.x += friction;
            ball.x += ballVelocity.x;

            ballVelocity.y += gravity;
            if (ballVelocity.y > 0)
                ballVelocity.y -= friction;
            else
                ballVelocity.y += friction;
            ball.y += ballVelocity.y;

            if (ball.x + ballRadius > screenWidth || ball.x - ballRadius < 0) {
                ball.x = currLoc.x;
                ballVelocity.x *= -1;
            }
            if (ball.y + ballRadius > screenHeight || ball.y - ballRadius < 0) {
                ball.y = currLoc.y;
                ballVelocity.y *= -1;
            }

            invalidate();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            updatePosition();
            invalidate();
            return super.onTouchEvent(event);
        }

        public void updatePosition() {
            ball = new Point();
            ball.x = screenWidth / 2;
            ball.y = screenHeight / 2;

            // Random velocity for each direction from -15 to +15
            Random r = new Random();
            ballVelocity = new Point();
            ballVelocity.x = r.nextInt(30) - 15;
            ballVelocity.y = r.nextInt(30) - 15;
            gravity = 2;
            friction = 0;
        }
    }
}


