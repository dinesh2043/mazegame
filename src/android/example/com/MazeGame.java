package android.example.com;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MazeGame extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get instance of Vibrator from current Context
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		// Vibrate for 300 milliseconds
		v.vibrate(50);
		setContentView(R.layout.main);
		Button newGame = (Button) findViewById(R.id.bNew);
		Button exit = (Button) findViewById(R.id.bExit);
		newGame.setOnClickListener(this);
		exit.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {

		// Get instance of Vibrator from current Context
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		// Vibrate for 300 milliseconds
		v.vibrate(50);
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.bExit:
			finish();
			break;

		case R.id.bNew:

			String[] levels = { "Maze 1", "Maze 2", "Maze 3" };
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getString(R.string.levelSelect));
			builder.setItems(levels, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					MediaPlayer mp = MediaPlayer.create(getBaseContext(),
							R.raw.music);
					mp.start();
					// Get instance of Vibrator from current Context
					Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

					// Vibrate for 300 milliseconds
					v.vibrate(50);
					// TODO Auto-generated method stub
					Intent game = new Intent(MazeGame.this, Game.class);
					Maze maze = MazeCreator.getMaze(item + 1);
					game.putExtra("maze", maze);
					startActivity(game);
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		}
	}
}