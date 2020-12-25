package com.lglf77.splashscreen2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    int counter;
    LinearLayout containerInfo, screen, containerHide;
    Animation bottomAnimation;
    private long timerStart = 9000;
    private Context context;
    private CountDownTimer countDownTimer;
    private TextView counttime;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        context = SplashScreen.this;
        screen = findViewById(R.id.screen);
        containerInfo = findViewById(R.id.transition_container);
        containerHide = findViewById(R.id.screen);

        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        containerHide.setAnimation(bottomAnimation);

        counttime = findViewById(R.id.counttime);

        screen.setOnClickListener(new View.OnClickListener() {
            boolean visible;
            ImageView icon = findViewById(R.id.icon);

            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(containerInfo);
                visible = !visible;
                containerInfo.setVisibility(visible ? View.VISIBLE : View.GONE);
                icon.setEnabled(!icon.isEnabled());
                if (containerInfo.getVisibility() == View.VISIBLE) {
                    stopTimer();
                } else {
                    startTimer(timerStart);
                }
            }
        });
    }

    private void startTimer(long timerStartFrom) {
        countDownTimer = new CountDownTimer(timerStartFrom, 1000) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTick(long millisUntilFinished) {
                // sempre vamos definir o valor atual da contagem na veriavel timeStart, pois se pararmos o
                // contador, e dps iniciar, vamos iniciá-lo de onde parou
                timerStart = millisUntilFinished;
                Log.d("time: ", String.valueOf(timerStart));

                NumberFormat f = new DecimalFormat("0");
                long sec = (millisUntilFinished / 1000) % 60;
                counttime.setText(f.format(sec));
                // counttime.setText(String.valueOf(counter));
                counter++;
            }

            @Override
            public void onFinish() {
                counttime.setText(""); // Opcional, definir mensagem para encerramento do contador
                startNewActivity();
            }
        }.start();
    }

    private void stopTimer() {
        countDownTimer.cancel();
    }

    private void startNewActivity() {
        // COÓDIGO RESPONSÁVEL PELA TÉRMINO DO SPLASH E REMANEJAR PARA MAIN ACTIVITY
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            // Handler e prepareMainLooper descontinuado na API 30, inserir Looper.getMainLooper dentro de colchetes do Hadnler
            @Override
            public void run() {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                finish();
            }

        }, 1000); // atualizei para 1 segundo
    }

    @Override
    public void onBackPressed() {
        // botão BackHandler está desabilitado (requer contructor vazio)
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(getApplicationContext(), "onStart called", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // vamos iniciar aqui depois pois o onCreate já está ok, mas pode iniciar nele tbm
        startTimer(timerStart);
    }

    @Override
    protected void onPause() {
        super.onPause();
        super.onResume();
        Toast.makeText(getApplicationContext(), "onPause called", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // aqui é opcional, se vc quiser que o contador pare se o app ficar em recentes,
        // ou alguma outra coisa sobrepor esta atividade...
        stopTimer();
        // aí quando a atividade retornar para a tela, voce pode começar de onde parou,
        // ou vc pode redefinir o startTimer para iniciar do primeiro segundo...
        timerStart = 9000;
        // quando a atividade retornar pra tela, ele reinicia a contagem de 8s no onResume
    }
}