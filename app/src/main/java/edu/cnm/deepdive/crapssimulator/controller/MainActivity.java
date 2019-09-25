package edu.cnm.deepdive.crapssimulator.controller;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import edu.cnm.deepdive.craps.model.Game;
import edu.cnm.deepdive.craps.model.Game.Round;
import edu.cnm.deepdive.crapssimulator.R;
import edu.cnm.deepdive.crapssimulator.view.RoundAdapter;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

  private Game game;
  private Random rng;
  private RoundAdapter adapter;
  private TextView tally;
  private ListView rolls;
  private boolean running;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    tally = findViewById(R.id.tally);
    rolls = findViewById(R.id.rolls);
    adapter = new RoundAdapter(this);
    rolls.setAdapter(adapter);
    rng = new Random();
    resetGame();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.options, menu);
    return true;
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);
    menu.findItem(R.id.play_one).setVisible(!running);
    menu.findItem(R.id.fast_forward).setVisible(!running);
    menu.findItem(R.id.pause).setVisible(running);
    menu.findItem(R.id.reset).setEnabled(!running);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    boolean handled = true;
    switch (item.getItemId()) {
      case R.id.play_one:
        updateDisplay(game.play());
        break;
      case R.id.fast_forward:
        running = true;
        invalidateOptionsMenu();
        break;
      case R.id.pause:
        running = false;
        invalidateOptionsMenu(); // FIXME Move this somewhere else.
        break;
      case R.id.reset:
        resetGame();
        break;
      default:
        handled = super.onOptionsItemSelected(item);
    }
    return handled;
  }

  private void updateDisplay(Round round) {
    adapter.add(round);
    tally.setText(getString(R.string.tally_format,
        game.getWins(), game.getPlays(), 100 * game.getPercentage()));
  }

  private void resetGame() {
    game = new Game(rng);
    updateDisplay(null);
  }

}
