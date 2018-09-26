package sk.upjs.Pinball;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import sk.upjs.jpaz2.TransitionEffect;
import sk.upjs.jpaz2.Turtle;
import sk.upjs.jpaz2.theater.Scene;
import sk.upjs.jpaz2.theater.Stage;

public class GameOverScene extends Scene {

	public static final String NAME = "GameOver";

	private Button playAgainButton;
	private Button menuButton;
	private Button exitButton;

	public GameOverScene(Stage stage) {
		super(stage);
		// TODO Auto-generated constructor stub
	}

	public void prepareScreen() {
		// TODO Auto-generated method stub
		int score = 0;
		try (Scanner sc = new Scanner(new File("score.txt"))) {
			score = sc.nextInt();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Painter.background(this, "/images/doge.jpg");
		Turtle painter = new Turtle();
		painter.setVisible(false);
		painter.setDirection(90);
		painter.setPenColor(Color.white);
		painter.setFont(new Font("Lucida Sans", Font.BOLD, 90));
		add(painter);
		painter.setPosition(10, 350);
		painter.print("Game Over");
		painter.setPosition(20, 430);
		painter.setFont(new Font("Lucida Sans", Font.BOLD, 50));
		painter.print("Score :" + score);
		playAgainButton = new Button(this, "/images/playAgainButton.png", getWidth() / 2, 500, 1);
		menuButton = new Button(this, "/images/menuButton.png", getWidth() / 2, 600, 1);
		exitButton = new Button(this, "/images/exitButton.png", getWidth() / 2, 700, 1);
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		prepareScreen();
	}

	@Override
	protected void onMousePressed(int x, int y, MouseEvent detail) {
		if (detail.getButton() == MouseEvent.BUTTON1) {
			if (playAgainButton.isMouseOver(x, y)) {
				getStage().changeScene(ClassicTable.NAME, TransitionEffect.MOVE_RIGHT, 1);
				return;
			}

			if (menuButton.isMouseOver(x, y)) {
				getStage().changeScene(IntroScene.NAME, TransitionEffect.MOVE_RIGHT, 1);
				return;
			}

			if (exitButton.isMouseOver(x, y)) {
				System.exit(0);
			}
		}
	}

	protected void onKeyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			getStage().changeScene(IntroScene.NAME, TransitionEffect.MOVE_RIGHT, 1);
		}
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			getStage().changeScene(ClassicTable.NAME, TransitionEffect.MOVE_RIGHT, 1);
		}
	}

	protected void onKeyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_M) {
			if (getStage().isMutedMusic())
				getStage().setMutedMusic(false);
			else
				getStage().setMutedMusic(true);
		}
		if (e.getKeyCode() == KeyEvent.VK_PRINTSCREEN) {
			savePicture("picture.png");
		}
	}

	@Override
	protected boolean onCanClick(int x, int y) {
		return super.onCanClick(x, y) || playAgainButton.isMouseOver(x, y) || menuButton.isMouseOver(x, y)
				|| exitButton.isMouseOver(x, y);
	}

}
