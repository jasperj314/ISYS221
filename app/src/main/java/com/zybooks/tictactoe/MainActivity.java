package com.zybooks.peerproject2;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private GameModel game;
    private Button[] buttons = new Button[9];
    private TextView turnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Keep Android Studio's template code
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Setup
        game = new GameModel();
        turnTextView = findViewById(R.id.turnTextView);

        // Link 9 buttons
        for (int i = 0; i < 9; i++) {
            int resID = getResources().getIdentifier("button" + i, "id", getPackageName());
            buttons[i] = findViewById(resID);
            final int index = i;
            buttons[i].setOnClickListener(v -> handleMove(index));
        }

        resetBoardUI();
    }

    private void handleMove(int index) {
        if (game.makeMove(index)) {
            buttons[index].setText(String.valueOf(game.getCurrentPlayer()));

            if (game.checkWin()) {
                disableBoard();
                showNewGameDialog(game.getCurrentPlayer() + " Wins!");
                return;
            }

            if (game.isBoardFull()) {
                showNewGameDialog("It's a tie!");
                return;
            }

            game.switchPlayer();
            turnTextView.setText(game.getCurrentPlayer() + "'s Turn");
        }
    }

    private void disableBoard() {
        for (Button b : buttons) b.setEnabled(false);
    }

    private void resetBoardUI() {
        // Ask player which symbol they want to start with
        new AlertDialog.Builder(this)
                .setTitle("Choose your symbol")
                .setMessage("Do you want to be X or O?")
                .setPositiveButton("X", (dialog, which) -> {
                    game.reset();
                    game.setCurrentPlayer('X');
                    updateBoardUI();
                })
                .setNegativeButton("O", (dialog, which) -> {
                    game.reset();
                    game.setCurrentPlayer('O');
                    updateBoardUI();
                })
                .setCancelable(false)
                .show();
    }

    private void updateBoardUI() {
        for (Button b : buttons) {
            b.setText("");
            b.setEnabled(true);
        }
        turnTextView.setText(game.getCurrentPlayer() + "'s Turn");
    }

    private void showNewGameDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage(message + "\nStart a new game?")
                .setPositiveButton("Yes", (dialog, which) -> resetBoardUI())
                .setNegativeButton("No", null)
                .show();
    }

    // Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_new_game) {
            resetBoardUI();
            return true;
        } else if (item.getItemId() == R.id.menu_quit) {
            showQuitDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showQuitDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Quit")
                .setMessage("Are you sure you want to quit?")
                .setPositiveButton("Yes", (dialog, which) -> finish())
                .setNegativeButton("No", null)
                .show();
    }
}
