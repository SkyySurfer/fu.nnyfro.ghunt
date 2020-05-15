package fu.nnyfro.ghunt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import fu.nnyfro.ghunt.Models.Direction;

public class MActivity extends AppCompatActivity {
    ImageView fly;
    ImageView frog;
    ImageView chameleon;

    RelativeLayout lay;

    CountDownTimer countDownTimer;
    TextView balanceView;
    TextView timerView;
    LinearLayout timerLayout;

    CountDownTimer timerChameleon;
    CountDownTimer timerChameleon1;
    CountDownTimer timerHide;

    Button hideButt;

    Direction direction = Direction.LEFT;

    float place = 2000f;
    int game = 0;
    int balance = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
        lay = findViewById(R.id.lay);
        fly = findViewById(R.id.fly);
        balanceView = findViewById(R.id.balanceView);
        frog = findViewById(R.id.frog);
        chameleon = findViewById(R.id.chameleon);
        hideButt = findViewById(R.id.hide_but);
        timerLayout = findViewById(R.id.underwaterTime);
        timerView = findViewById(R.id.timerView);

        fly.setVisibility(View.VISIBLE);

        timerChameleon = new CountDownTimer(40000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                chameleon.setVisibility(View.VISIBLE);
                hideButt.setVisibility(View.VISIBLE);
                timerChameleon1.start();
                hideButt.setVisibility(View.VISIBLE);

            }
        };

        timerChameleon.start();

        hideButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (frog.getVisibility() == View.VISIBLE){
                    frog.setVisibility(View.GONE);
                    timerLayout.setVisibility(View.VISIBLE);
                    timerHide.start();
                }
                else {
                    frog.setVisibility(View.VISIBLE);
                    hideButt.setVisibility(View.GONE);
                }

            }
        });

        timerChameleon1 = new CountDownTimer(10000, 3000) {
            @Override
            public void onTick(long l) {

                if (frog.getVisibility() == View.VISIBLE){
                    balance -= 1;
                    balanceView.setText(String.valueOf(balance));
                }


            }

            @Override
            public void onFinish() {
                chameleon.setVisibility(View.GONE);
                timerChameleon.start();
                //hideButt.setVisibility(View.GONE);
               // hideButt.setVisibility(View.VISIBLE);
            }
        };

        timerHide = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {

                timerView.setText(String.valueOf(l/1000));
            }

            @Override
            public void onFinish() {
                if (frog.getVisibility() == View.GONE){
                    frog.setVisibility(View.GONE);
                    countDownTimer.cancel();
                    timerChameleon.cancel();
                    timerChameleon1.cancel();
                    chameleon.setVisibility(View.GONE);
                    hideButt.setVisibility(View.GONE);
                    fly.setVisibility(View.GONE);
                    balanceView.setText(String.valueOf(0));
                    timerLayout.setVisibility(View.GONE);

                    Snackbar.make(lay, "You've been under water too long", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Try Again",
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(MActivity.this, MActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    })
                    .show();
                }
                timerLayout.setVisibility(View.GONE);
            }
        };


        Log.d("sdsdsd", String.valueOf(fly.getX()));

        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (game == 1){
                    balance += 1;
                    balanceView.setText(String.valueOf(balance));
                    game = 0;
                    fly.setVisibility(View.GONE);
                    frog.setImageDrawable(getResources().getDrawable(R.drawable.frog_2));
                    CountDownTimer countDownTimer1 = new CountDownTimer(500, 100) {
                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            frog.setImageDrawable(getResources().getDrawable(R.drawable.frog_1));
                        }
                    };
                    countDownTimer1.start();
                }
            }
        };

        frog.setOnClickListener(listener);
        game = 1;
        countDownTimer = new CountDownTimer(5000, 10) {


            @Override
            public void onTick(long l) {
                if (direction == Direction.LEFT){
                    place -=10f;
                    fly.setX(place - 10f);
                    if (game == 0){
                        frog.setOnClickListener(null);
                    }
                }
                if (direction == Direction.RIGHT){
                    fly.setX(fly.getX() + 10f);
                    if (game == 0){
                        frog.setOnClickListener(null);
                    }
                }

                Log.d("sdsdsd", String.valueOf(fly.getX()));
            }

            @Override
            public void onFinish() {
                fly.setVisibility(View.VISIBLE);
                game = 1;
                place = 2000f;
                if (direction == Direction.LEFT){
                    direction = Direction.RIGHT;
                    fly.setScaleX(-1);
                    frog.setScaleX(-1);
                    //fly.setRotation(180);
                }
                else {
                    direction = Direction.LEFT;
                    fly.setScaleX(1);
                    frog.setScaleX(1);
                    //fly.setRotation(180);
                }
                countDownTimer.start();
                frog.setOnClickListener(listener);
            }
        };
        countDownTimer.start();
    }
}
