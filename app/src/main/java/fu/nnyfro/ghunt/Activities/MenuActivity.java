package fu.nnyfro.ghunt.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fu.nnyfro.ghunt.MActivity;
import fu.nnyfro.ghunt.R;

public class MenuActivity extends AppCompatActivity {

    Button startButton;
    Button rulesButton;
    TextView rulesLay;

    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        startButton = findViewById(R.id.start_button);
        rulesButton = findViewById(R.id.rules_button);
        rulesLay = findViewById(R.id.rules_lay);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, MActivity.class);
                startActivity(intent);
            }
        });

        rulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startButton.setVisibility(View.GONE);
                rulesButton.setVisibility(View.GONE);
                rulesLay.setVisibility(View.VISIBLE);
                timer.start();
            }
        });

        timer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                rulesButton.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.VISIBLE);
                rulesLay.setVisibility(View.GONE);
            }
        };



    }
}
