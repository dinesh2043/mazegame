package android.example.com;

import java.io.Serializable;

public class Maze implements Serializable {

	private static final long serialVersionUID = 1L;
	public boolean[][] verticalLines;
	public boolean[][] horizontalLines;
	public int sizeX, sizeY; // stores the width and height of the maze
	float currentX = 0, currentY = 0; // stores the current location of the ball
	private int finalX, finalY; // stores the ending point of the maze
	private boolean gameComplete;

	// setters and getters
	// to get maze size
	public int getMazeWidth() {
		return sizeX;
	}

	// to get maze height
	public int getMazeHeight() {
		return sizeY;
	}

	boolean moved = false;
	

	// to update x direction movement of the marble
	public boolean updateMarbleX(float newX) {
		// boundary checking and horizontal line checking, don't want the marble
		// rolling
		// off-screen beyond the horizontal line
		if (newX > 0) {
			if (currentX < sizeX - 1
					&& !verticalLines[(int) currentY][(int) currentX]) {
				currentX += newX;

				moved = true;
			}
		} else if (newX < 0) {

			if (currentX >= 1
					&& !verticalLines[(int) currentY][(int) (currentX - 1)]) {
				currentX += newX;
				moved = true;
			}
		}

		if (moved) {
			if (currentX == finalX && currentY == finalY) {
				gameComplete = true;
			}
		}
		return moved;

	}

	public boolean updateMarbleY(float newY) {

		// boundary checking and horizontal line checking, don't want the marble
		// rolling
		// off-screen beyond the horizontal line
		if (newY < 0) {
			if (currentY < sizeY - 1
					&& !horizontalLines[(int) currentY][(int) currentX]) {
				currentY -= newY;
				moved = true;
			}
		} else if (newY > 0) {

			if (currentY >= 1
					&& !horizontalLines[(int) (currentY - 1)][(int) currentX]) {
				currentY -= newY;
				moved = true;
			}
		}

		if (moved) {
			if ((int) currentX == finalX && (int) currentY == finalY) {
				gameComplete = true;
			}

		}
		return moved;

	}

	// to return boolean game complete
	public boolean isGameComplete() {
		return gameComplete;
	}

	// to set the start position
	public void setStartPosition(int x, int y) {
		currentX = x;
		currentY = y;
	}

	// to get finalX position
	public int getFinalX() {
		return finalX;
	}

	// to get finalY position
	public int getFinalY() {
		return finalY;
	}

	// set final position
	public void setFinalPosition(int x, int y) {
		finalX = x;
		finalY = y;
	}

	// get current positionX
	public int getCurrentX() {
		return (int) currentX;
	}

	// get current positionY
	public int getCurrentY() {
		return (int) currentY;
	}

	// get horizontal lines
	public boolean[][] getHorizontalLines() {
		return horizontalLines;
	}

	public void setHorizontalLines(boolean[][] lines) {
		horizontalLines = lines;
		sizeX = horizontalLines[0].length;
	}

	public boolean[][] getVerticalLines() {
		return verticalLines;
	}

	public void setVerticalLines(boolean[][] lines) {
		verticalLines = lines;
		sizeY = verticalLines.length;
	}

}
