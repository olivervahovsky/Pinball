package sk.upjs.Pinball;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import sk.upjs.jpaz2.*;
import sk.upjs.jpaz2.theater.*;

public class IntroScene extends Scene {

	
	public static final String NAME = "Intro";

	private Button startButton;
	private Button exitButton;

	public IntroScene(Stage stage) {
		super(stage);
		prepareScreen();
		getStage().setMutedMusic(true);
	}

	private void prepareScreen() {
		setBorderWidth(0);

		// paint background
		Painter.gifBackground(this, "/images/turtle.gif");

		// menu buttons
		startButton = new Button(this, "/images/startButton.png", getWidth() / 2, getHeight() / 2 - 70, 1);
		exitButton = new Button(this, "/images/exitButton.png", getWidth() / 2, getHeight() / 2 + 70, 1);
	}

	@Override
	public void start() {

	}

	@Override
	public void stop() {

	}

	@Override
	protected void onMousePressed(int x, int y, MouseEvent detail) {
		if (detail.getButton() == MouseEvent.BUTTON1) {
			if (startButton.isMouseOver(x, y)) {
				getStage().changeScene(ClassicTable.NAME, TransitionEffect.MOVE_RIGHT, 1);
				return;
			}

			if (exitButton.isMouseOver(x, y)) {
				System.exit(0);
			}
		}
	}
	
	protected void onKeyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
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
		if(e.getKeyCode() == KeyEvent.VK_PRINTSCREEN) {
			savePicture("picture.png");
		}
	}

	@Override
	protected boolean onCanClick(int x, int y) {
		return super.onCanClick(x, y) || startButton.isMouseOver(x, y) || exitButton.isMouseOver(x, y);
	}
}