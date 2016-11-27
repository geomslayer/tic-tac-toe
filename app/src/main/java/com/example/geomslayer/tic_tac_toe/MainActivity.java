package com.example.geomslayer.tic_tac_toe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.example.geomslayer.tic_tac_toe.R.id.result_txt;

public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = this.getClass().getSimpleName();
    final int SIZE = 3;
    final int CROSSES = 0;
    final int ZEROES = 1;
    final int EMPTY = -1;

    int activePlayer;
    int[][] field = new int[SIZE][SIZE];
    LinearLayout retryLayout;
    GridLayout itemFieldLayout;
    ImageView activePlayerImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retryLayout = (LinearLayout) findViewById(R.id.retry_layout);
        itemFieldLayout = (GridLayout) findViewById(R.id.item_field_layout);
        activePlayerImg = (ImageView) findViewById(R.id.active_player_img);
        prepareField();
    }

    private void prepareField() {
        activePlayer = CROSSES;
        retryLayout.setVisibility(View.INVISIBLE);

        for (int i = 0; i < SIZE; ++i) {
            for (int k = 0; k < SIZE; ++k) {
                field[i][k] = EMPTY;
            }
        }

        for (int i = 0; i < itemFieldLayout.getChildCount(); ++i) {
            ((ImageView) itemFieldLayout.getChildAt(i)).setImageResource(0);
        }
        setEnabledAll(itemFieldLayout, true);
        setActivePlayer();
    }

    public void dropItem(View view) {
        ImageView item = (ImageView) view;
        int num = Integer.parseInt(item.getTag().toString());
        int i = num / SIZE;
        int k = num % SIZE;

        if (field[i][k] != EMPTY) {
            return;
        }

        field[i][k] = activePlayer;
        showItem(item);

        String msg = check();
        if (!msg.equals("")) {
            retryLayout.setVisibility(View.VISIBLE);
            ((TextView) findViewById(result_txt)).setText(msg);
            setEnabledAll(itemFieldLayout, false);
        }
        else {
            activePlayer ^= 1;
            setActivePlayer();
        }

    }

    public void playAgain(View view) {
        prepareField();
    }

    private void setActivePlayer() {
        if (activePlayer == CROSSES) {
            activePlayerImg.setImageResource(R.drawable.cross);
        }
        else {
            activePlayerImg.setImageResource(R.drawable.zero);
        }
    }

    private void setEnabledAll(GridLayout layout, boolean mode) {
        layout.setEnabled(mode);
        for (int i = 0; i < layout.getChildCount(); ++i) {
            layout.getChildAt(i).setEnabled(mode);
        }
    }

    private void showItem(ImageView image) {
        if (activePlayer == CROSSES) {
            image.setImageResource(R.drawable.cross);
        } else {
            image.setImageResource(R.drawable.zero);
        }
    }

    private boolean draw() {
        boolean res = true;
        for (int i = 0; i < SIZE; ++i) {
            for (int k = 0; k < SIZE; ++k) {
                res &= (field[i][k] != EMPTY);
            }
        }
        return res;
    }

    private String check() {
        boolean res = false;
        for (int i = 0; i < SIZE; ++i) {
            boolean ok = (field[i][0] != EMPTY);
            for (int k = 1; k < SIZE; ++k) {
                if (field[i][0] != field[i][k]) {
                    ok = false;
                }
            }
            res |= ok;
        }
        for (int k = 0; k < SIZE; ++k) {
            boolean ok = (field[0][k] != EMPTY);
            for (int i = 1; i < SIZE; ++i) {
                if (field[0][k] != field[i][k]) {
                    ok = false;
                }
            }
            res |= ok;
        }
        boolean diag = (field[0][0] != EMPTY);
        for (int i = 1; i < SIZE; ++i) {
            if (field[i][i] != field[0][0]) {
                diag = false;
            }
        }
        res |= diag;
        diag = (field[0][SIZE - 1] != EMPTY);
        for (int i = 1, k = SIZE - 2; i < SIZE; ++i, --k) {
            if (field[i][k] != field[0][SIZE - 1]) {
                diag = false;
            }
        }
        res |= diag;
        if (res) {
            return (activePlayer == CROSSES ? "Crosses " : "Zeroes ") + getString(R.string.winner);
        }
        if (draw()) {
            return getString(R.string.draw);
        }
        return "";
    }

}