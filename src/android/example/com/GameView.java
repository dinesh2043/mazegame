package android.example.com;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.view.LayoutInflater;
import android.view.View;

@SuppressWarnings("deprecation")
public class GameView extends View {

	// width and height of the whole maze and width of lines which
	// make the walls
	private int width, height, lineWidth;
	// size of the maze i.e. number of cells in it
	private int mazeSizeX, mazeSizeY;

	// width and height of cells in the maze
	float cellWidth, cellHeight;
	// the following store result of cellWidth+lineWidth
	// and cellHeight+lineWidth respectively
	float totalCellWidth, totalCellHeight;
	// the finishing point of the maze
	private int mazeFinishX, mazeFinishY;
	private Maze maze;
	private Activity context;

	// sensor manager used to control the accelerometer sensor.
	private SensorManager mSensorManager;
	// accelerometer sensor values.
	private float mAccelX = 0;
	private float mAccelY = 0;
	// private float mAccelZ = 0;
	// private Vibrator vibrator;

	private float mSensorBuffer = 0;

	private final SensorListener mSensorAccelerometer = new SensorListener() {

		// method called whenever new sensor values are reported.
		public void onSensorChanged(int sensor, float[] values) {
			
			// grab the values required to respond to user movement.
			mAccelX = (float) (0.2 * values[0]);
			mAccelY = (float) (0.2 * values[1]);
			// mAccelZ = values[2];
			invalidate();
		}

		public void onAccuracyChanged(int sensor, int accuracy) {
		}
	};

	// gathering all the required information about the game view
	private Paint line, red, green, blue, background;

	public GameView(Context context, Maze maze) {
		super(context);
		this.context = (Activity) context;
		// vibrator =
		// (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
		// setup accelerometer sensor manager.
		mSensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);

		mSensorManager.registerListener(mSensorAccelerometer,
				SensorManager.SENSOR_ACCELEROMETER,
				SensorManager.SENSOR_DELAY_GAME);
		this.maze = maze;
		// mMarble = new Marble(this);

		// getting the size and finish point of the game
		mazeFinishX = maze.getFinalX();
		mazeFinishY = maze.getFinalY();
		mazeSizeX = maze.getMazeWidth();
		mazeSizeY = maze.getMazeHeight();

		// getting values stored in colors.xml in current view
		line = new Paint();
		line.setColor(getResources().getColor(R.color.line));
		red = new Paint();
		red.setColor(getResources().getColor(R.color.position));
		green = new Paint();
		green.setColor(getResources().getColor(R.color.Green));
		blue = new Paint();
		blue.setColor(getResources().getColor(R.color.Blue));
		background = new Paint();
		background.setColor(getResources().getColor(R.color.game_bg));
		setFocusable(true);
		this.setFocusableInTouchMode(true);

	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		width = (w < h) ? w : h;
		height = (h > w) ? h : w;
		// height = width; //for now square mazes
		lineWidth = 1; // for now 1 pixel wide walls
		cellWidth = (width - ((float) mazeSizeX * lineWidth)) / mazeSizeX;
		totalCellWidth = cellWidth + lineWidth;
		cellHeight = (height - ((float) mazeSizeY * lineWidth)) / mazeSizeY;
		totalCellHeight = cellHeight + lineWidth;
		red.setTextSize(cellHeight * 0.75f);
		super.onSizeChanged(w, h, oldw, oldh);
	}

	public void onDraw(Canvas canvas) {
		// fill in the background
		canvas.drawRect(0, 0, width, height, background);

		boolean[][] hLines = maze.getHorizontalLines();
		boolean[][] vLines = maze.getVerticalLines();
		// iterate over the boolean arrays to draw walls
		for (int i = 0; i < mazeSizeX; i++) {
			for (int j = 0; j < mazeSizeY; j++) {
				float x = j * totalCellWidth;
				float y = i * totalCellHeight;
				if (j < mazeSizeX - 1 && vLines[i][j]) {
					// we'll draw a vertical line
					canvas.drawLine(x + cellWidth, // start X
							y, // start Y
							x + cellWidth, // stop X
							y + cellHeight, // stop Y
							line);
				}
				if (i < mazeSizeY - 1 && hLines[i][j]) {
					// we'll draw a horizontal line
					canvas.drawLine(x, // startX
							y + cellHeight, // startY
							x + cellWidth, // stopX
							y + cellHeight, // stopY
							line);
				}
			}
		}

		// draw the finishing point indicator
		canvas.drawText("F", (mazeFinishX * totalCellWidth)
				+ (cellWidth * 0.25f), (mazeFinishY * totalCellHeight)
				+ (cellHeight * 0.75f), red);

		int currentX = maze.getCurrentX(), currentY = maze.getCurrentY();
		// draw the ball in a specified location between the middle of cell
		canvas.drawCircle((currentX*totalCellWidth) + (cellWidth / 2), // x of center
				(currentY*totalCellHeight) + (cellWidth / 2), // y of center
				(cellWidth * 0.4f), // radius
				red);

//		canvas.drawCircle((currentX * totalCellWidth) + (cellWidth / 2), // x of center
//				(currentY * totalCellHeight) + (cellWidth / 2), // y of center
//				(cellWidth * 0.4f), // radius
//				red);

		 //maze.draw(canvas, line);
		
		updateMarble();
		//invalidate();
	}

	boolean moved = false;

	// this is called to update the position of the marble
	public boolean updateMarble() {

		// we CAN give ourselves a buffer to stop the marble from rolling even
		// though we think the device is "flat".
		// to get the tilted position of the phone
		if (mAccelX > mSensorBuffer || mAccelX < -mSensorBuffer) {

			moved = maze.updateMarbleX(mAccelX);

		}
		// to get the position of the phone
		if (mAccelY > mSensorBuffer || mAccelY < -mSensorBuffer) {

			moved = maze.updateMarbleY(mAccelY);

		}

		// check weather the marble have changed its position or not checking
		// the bool value
		if (moved) {
			
			// check weather the game is complete
			if (maze.isGameComplete()) {
				// vibrator.vibrate(50);
				// to show alert dailog after game is complete
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle(context.getText(R.string.finished_title));
				LayoutInflater inflater = context.getLayoutInflater();
				View view = inflater.inflate(R.layout.finish, null);
				builder.setView(view);
				View closeButton = view.findViewById(R.id.closeGame);

				closeButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View clicked) {

						if (clicked.getId() == R.id.closeGame) {
							context.finish();
						}
					}
				});
				AlertDialog finishDialog = builder.create();
				finishDialog.show();

			}
			// vibrator.cancel();
		}
		return true;

	}

}
