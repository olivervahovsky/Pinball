package sk.upjs.Pinball;

import java.awt.Color;
import java.awt.Font;

import sk.upjs.jpaz2.ImageTurtleShape;
import sk.upjs.jpaz2.Pane;
import sk.upjs.jpaz2.Turtle;

public class Score extends Pane {

	private int score;

	private Turtle painter;
	private Turtle background;

	// trieda vytvorena po hlbkovom studiu snowballCatchera (ctrl c, ctrl v + male upravy)
	public Score(int startingScore) {
		super(100, 56);
		setBorderWidth(0);
		background = new Turtle();
		background.setShape(new ImageTurtleShape("/images/screen.png"));
		add(background);
		background.center();
		background.stamp();
		remove(background);
		painter = new Turtle();
		painter.setVisible(false);
		painter.setDirection(90);
		painter.setPenColor(Color.yellow);
		painter.setFont(new Font("Lucida Sans", Font.BOLD, 20));
		add(painter);
		resetScore(startingScore);
	}

	public void resetScore(int startingScore) {
		score = startingScore;
		repaintScore();
	}

	public void increaseScore(int points) {
		this.score += points;
		repaintScore();
	}
	
	public void decreaseScore(int points) {
		this.score -= points;
		repaintScore();
	}

	public int getScore() {
		return score;
	}

	private void repaintScore() {
		add(background);
		background.stamp();
		remove(background);
		painter.printCenter(Integer.toString(score));
	}
}
