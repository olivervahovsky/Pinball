package sk.upjs.Pinball;

import java.awt.Color;
import java.awt.Font;
import java.awt.RenderingHints.Key;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sk.upjs.jpaz2.*;
import sk.upjs.jpaz2.theater.*;

public class ClassicTable extends Scene {

	public static final String NAME = "Main";

	private Painter painter = new Painter();
	private List<Bumper> bumpers;
	private List<Slingshot> slingshots = new ArrayList<>();
	private List<Wormhole> wormholes = new ArrayList<>();

	private Ball ball = new Ball();
	private Flipper lFlip = new Flipper(-1);
	private Flipper rFlip = new Flipper(1);
	private int lostBalls = 3;

	private int spaceOfDeath = 20;
	private int flipperEndAltitude = 50;

	private double l = lFlip.getFlipperLength();
	private double xSL = (getWidth() - 35) / 2 - spaceOfDeath / 2 - l / 2;
	private double xSR = (getWidth() - 35) / 2 + spaceOfDeath / 2 + l / 2;
	private double yS = getHeight() - flipperEndAltitude - l;
	private double flipperEndL = xSL - l / 2;
	private double flipperEndR = xSR + l / 2;

	private boolean readyToGo = false;
	private double shotSpeed = 0;
	private boolean pullingString = false;
	private boolean fallenBall = false;
	private int powerBarNorm = 204;

	private boolean pause = false;
	private Button pauseButton;
	private Button returnButton;
	private Score score;
	private Score ballsLeft;
	private int lastScore;
	private boolean[] missionTypes = new boolean[3];
	private int pointsForMission = 150;
	private int timer = 0;

	// mission time as number of seconds
	private int missionTime = 10;
	private Pane missionInfo;
	private Turtle infoWriter;

	private int gifTimer = 0;

	public ClassicTable(Stage stage) {
		super(stage);
		buildTable();
	}

	private void buildTable() {
		// paint background
		Painter.background(this, "/images/space.jpg");
		// set repeat period for left and right keys (vyriesilo lagnutie po
		// stlaceni a zvysilo plynulost, vyriesilo sucasne stlacenie klaves)
		setKeyRepeatPeriod(KeyEvent.VK_LEFT, 10);
		setKeyRepeatPeriod(KeyEvent.VK_RIGHT, 10);
		// simple walls
		simpleWalls();
		// power bar
		powerBar();
		// text ohladom misii
		missionInfo();
		// buttons
		pauseButton = new Button(this, "/images/pauseButton.png", 60, 790, 0.35);
		returnButton = new Button(this, "/images/returnButton.png", 60, 860, 0.35);
		// slingshots
		slingshots
				.add(new Slingshot(this, 40, yS - (flipperEndL + lFlip.getFlipperWidth() / Math.sqrt(2)) - 25, 150, 1));
		slingshots.add(new Slingshot(this, getWidth() - 40 - 35,
				yS - (flipperEndL + lFlip.getFlipperWidth() / Math.sqrt(2)) - 25, 150, -1));
		// wormholes
		wormholes.add(new Wormhole(this, 65, 240));
		wormholes.add(new Wormhole(this, 435, 240));
		wormholes.add(new Wormhole(this, (getWidth()-35) / 2, getHeight() / 2));
		// wings
		Painter.picture(this, 423, 500, "/images/rightWing.png");
		Painter.picture(this, 77, 500, "/images/leftWing.png");
		Painter.picture(this, 25, 25, "/images/upjs_logo_mini.png");
		// create and configure actors
		addBumpers();

		add(lFlip);
		add(rFlip);

		lFlip.setPosition(xSL, yS);
		rFlip.setPosition(xSR, yS);
	}

	private void missionInfo() {
		missionInfo = new Pane(220, 75);
		add(missionInfo);
		missionInfo.setTransparentBackground(true);
		missionInfo.setBorderWidth(0);
		missionInfo.setPosition((getWidth() - 35) / 2 - missionInfo.getWidth() / 2,
				getHeight() - missionInfo.getHeight());
		infoWriter = new Turtle();
		missionInfo.add(infoWriter);
		infoWriter.setTransparency(1);
		infoWriter.penUp();
		infoWriter.setPenColor(Color.WHITE);
		infoWriter.setFont(new Font("Lucida Sans", Font.BOLD, 20));
		infoWriter.setPosition(missionInfo.getWidth() / 2, missionInfo.getHeight() / 4 - 8);
		infoWriter.setDirection(90);
		infoWriter.printCenter(MissionControl.printMission(missionTypes));
	}

	// paint simple walls
	private void simpleWalls() {
		// vystrelovacia trubka
		Painter.line(this, getWidth() - 35, 50, getWidth() - 35, 900, Color.GRAY, 3);
		// lavy lievik
		Painter.line(this, flipperEndL + lFlip.getFlipperWidth() / Math.sqrt(2), yS, 0,
				yS - (flipperEndL + lFlip.getFlipperWidth() / Math.sqrt(2)), Color.gray, 3);
		Painter.picture(this, 70, 750, "/images/mars.png");
		// pravy lievik
		Painter.line(this, flipperEndR - rFlip.getFlipperWidth() / Math.sqrt(2), yS, getWidth() - 35,
				yS - (getWidth() - 35 - flipperEndR + rFlip.getFlipperWidth() / Math.sqrt(2)), Color.gray, 3);
		Painter.picture(this, 430, 750, "/images/moon.png");
		// vystrelovac
		Painter.line(this, getWidth() - 35, yS, getWidth(), yS, Color.gray, 3);
		// steny diery
		// Painter.line(this, flipperEndL + lFlip.getFlipperWidth() /
		// Math.sqrt(2), yS,
		// flipperEndL + lFlip.getFlipperWidth() / Math.sqrt(2), getHeight(),
		// Color.gray, 3);
		Painter.line(this, flipperEndR - rFlip.getFlipperWidth() / Math.sqrt(2), yS,
				flipperEndR - rFlip.getFlipperWidth() / Math.sqrt(2), getHeight(), Color.gray, 3);
		// odrazac vystrelenej lopticky
		Painter.line(this, getWidth() - 35, 0, getWidth(), 35, Color.gray, 3);
	}

	// ukazovatel skore
	private void scoreBoard() {
		Turtle writer = new Turtle();
		add(writer);
		writer.setPosition(xSR + 85, yS + 95);
		writer.setVisible(false);
		writer.setDirection(90);
		writer.setPenColor(Color.yellow);
		writer.setFont(new Font("Lucida Sans", Font.BOLD, 25));
		writer.print("Score");
		score = new Score(0);
		add(score);
		score.setPosition(xSR + 70, yS + 100);
	}

	// ukazovatel zvysneho poctu lopticiek
	private void ballsLeftBoard() {
		Turtle writer = new Turtle();
		add(writer);
		writer.setPosition(xSR + 70, yS + 10);
		writer.setVisible(false);
		writer.setDirection(90);
		writer.setPenColor(Color.yellow);
		writer.setFont(new Font("Lucida Sans", Font.BOLD, 20));
		writer.print("Balls Left");
		ballsLeft = new Score(3);
		add(ballsLeft);
		ballsLeft.setPosition(xSR + 70, yS + 15);
	}

	private void addBall() {
		add(ball);
		ball.setPosition(getWidth() - 35 / 2.0, 35);
		// ball.setPosition(175, 200);
		ball.setVelocityX(0);
		ball.setVelocityY(0);
	}

	// pridanie bumperov do stola
	private void addBumpers() {
		bumpers = new ArrayList<>();
		Turtle positioner = new Turtle();
		positioner.setTransparency(1);
		positioner.penUp();
		add(positioner);
		positioner.setPosition(getWidth() / 2, getHeight() / 5);
		Bumper bumper = new Bumper(this, positioner.getX(), positioner.getY(), 18, true);
		bumpers.add(bumper);
		positioner.turn(-60);
		for (int i = 0; i < 3; i++) {
			positioner.step(75);
			bumper = new Bumper(this, positioner.getX(), positioner.getY(), 18, true);
			bumpers.add(bumper);
			positioner.step(-75);
			positioner.turn(120);
		}
		bumper = new Bumper(this, 75, 75, 18, true);
		bumpers.add(bumper);
		bumper = new Bumper(this, 50, 100, 18, true);
		bumpers.add(bumper);
		remove(positioner);
	}

	// interakcia s bumpermi cez interakcnu metodu triedy bumper
	private void bumpersInteraction() {
		for (Bumper bumper : bumpers) {
			if (bumper.closeEncounter(ball)) {
				bumper.bumperInteraction(ball, this, 1.2);
				if (missionTypes[0] == true)
					score.increaseScore(5 * 10);
				else
					score.increaseScore(10);
			}
		}
	}

	// metody na interakciu so slingshotmi
	private void slingshotsInteractionLong() {
		for (Slingshot slingshot : slingshots) {
			double r = ball.getBallRadius();
			if (ball.getY() > slingshot.getY() && ball.getY() + ball.getVelocityY() - r < slingshot.getY()) {
				ball.setVelocityY(-ball.getVelocityY());
			} else if (ball.getY() < slingshot.getY()) {
				double a1 = (slingshot.getVertexYU() - slingshot.getVertexYD())
						/ (slingshot.getVertexXU() - slingshot.getVertexXD());
				double b1 = slingshot.getVertexYU() - a1 * slingshot.getVertexXU();
				if (ball.getY() + ball.getVelocityY() + r > a1 * (ball.getX() + ball.getVelocityX()) + b1) {
					ball.advancedReflection(slingshot.getHeelX() - slingshot.getX(),
							slingshot.getHeelY() - slingshot.getY(), 1.2);
					if (missionTypes[1] == true)
						score.increaseScore(5 * 15);
					else
						score.increaseScore(15);
				}
			}
		}
	}

	private void slingshotsInteraction() {
		double r = ball.getBallRadius();
		for (Slingshot slingshot : slingshots) {
			for (Bumper bumper : slingshot.getBumpers()) {
				if (bumper.closeEncounter(ball)) {
					bumper.bumperInteraction(ball, this, 1);
				}
			}
			if (slingshot.getSign() == 1) {
				if (ball.getX() < slingshot.getX() && ball.getY() < slingshot.getY()
						&& ball.getY() > slingshot.getY() - slingshot.getLength()) {
					if (ball.getX() + ball.getVelocityX() + r > slingshot.getX()) {
						ball.setVelocityX(-ball.getVelocityX());
					}
				}
				if ((ball.getX() > slingshot.getX() && ball.getX() < slingshot.getVertexXD())
						|| (ball.getX() + ball.getVelocityX() > slingshot.getX()
								&& ball.getX() + ball.getVelocityX() < slingshot.getVertexXD())) {
					slingshotsInteractionLong();
				}
			} else {
				if (ball.getX() > slingshot.getX() && ball.getX() < 500 && ball.getY() < slingshot.getY()
						&& ball.getY() > slingshot.getY() - slingshot.getLength()) {
					if (ball.getX() + ball.getVelocityX() - r < slingshot.getX()) {
						ball.setVelocityX(-ball.getVelocityX());
					}
				}
				if ((ball.getX() < slingshot.getX() && ball.getX() > slingshot.getVertexXD())
						|| (ball.getX() + ball.getVelocityX() > slingshot.getX()
								&& ball.getX() + ball.getVelocityX() < slingshot.getVertexXD())) {
					slingshotsInteractionLong();
				}
			}
		}
	}

	// interakcia s cervymi dierami
	private void wormholeInteraction() {
		for (Wormhole wormhole : wormholes) {
			if (wormhole.containsInShape(ball.getX(), ball.getY())) {
				if (missionTypes[2] == true)
					score.increaseScore(5 * 15);
				else
					score.increaseScore(15);
				double direction = ball.directionTowards(wormhole.getPosition());
				JPAZUtilities.delay(150);
				List<Wormhole> otherWormholes = new ArrayList<>();
				for (Wormhole otherWormhole : wormholes) {
					if (otherWormhole != wormhole) {
						otherWormholes.add(otherWormhole);
					}
				}
				Wormhole randomWormhole = otherWormholes.get((int)(2*Math.random()));
				ball.setPosition(randomWormhole.getX(), randomWormhole.getY());
				ball.setDirection(direction);
				ball.step(26);
			}
		}
	}

	// powerbar a jeho funkcia
	private void powerBar() {
		Pane powerBar = new Pane(35, 163);
		add(powerBar);
		powerBar.setPosition(getWidth() - 35, yS);
		Painter.line(powerBar, 0, powerBar.getHeight() / 2, powerBar.getWidth(), powerBar.getHeight() / 2, Color.gray,
				3);
		powerBar.add(painter);
		painter.setPenWidth(1);
		painter.setPosition(0, powerBar.getHeight());
	}

	private void powerBarAnimation() {
		if (pullingString) {
			if (shotSpeed < ball.getMaximumVelocity()) {
				shotSpeed += ball.getMaximumVelocity() / 250;
			}
			if (powerBarNorm > 1) {
				powerBarNorm -= 1;
			}
			if (painter.getY() > 0) {
				if (!(painter.getY() > 80 && painter.getY() < 83)) {
					painter.setPenColor(new Color(255, powerBarNorm, powerBarNorm));
					painter.line(painter.getX(), painter.getY(), 35, painter.getY());
					painter.setPosition(0, painter.getY() - 0.7);
				} else {
					painter.setPosition(0, painter.getY() - 0.7);
				}
			}
		} else {
			if (shotSpeed > 0) {
				shotSpeed -= ball.getMaximumVelocity() / 250;
			}
			if (powerBarNorm < 204) {
				powerBarNorm += 1;
			}
			if (painter.getY() < 163) {
				if (!(painter.getY() > 80 && painter.getY() < 83)) {
					painter.setPenColor(Color.white);
					painter.line(painter.getX(), painter.getY(), 35, painter.getY());
					painter.setPosition(0, painter.getY() + 0.7);
				} else {
					painter.setPosition(0, painter.getY() + 0.7);
				}
			}
		}
	}

	@Override
	public void start() {
		// start scene updates - periodically update the scene
		setTickPeriod(8);
		lastScore = 0;
		lostBalls = 0;
		addBall();
		fallenBall = false;
		// scoreboard
		scoreBoard();
		ballsLeftBoard();
	}

	@Override
	public void stop() {
		setTickPeriod(0);
		getStage().setMutedMusic(true);
	}

	protected boolean onCanClick(int x, int y) {
		return super.onCanClick(x, y) || pauseButton.isMouseOver(x, y) || returnButton.isMouseOver(x, y);
	}

	@Override
	protected void onMousePressed(int x, int y, MouseEvent detail) {
		if (returnButton.isMouseOver(x, y)) {
			stopMission();
			getStage().changeScene(IntroScene.NAME, TransitionEffect.MOVE_RIGHT, 1);
		}
		if (pauseButton.isMouseOver(x, y)) {
			pause();
		}
	}

	@Override
	protected void onKeyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			// true = je stlacena lava sipka, teda lavy flipper nepada nadol
			lFlip.setLeft(true);
		}

		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			// true = je stlacena prava sipka, teda pravy flipper nepada nadol
			rFlip.setRight(true);
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			pullingString = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			stopMission();
			getStage().changeScene(IntroScene.NAME, TransitionEffect.MOVE_RIGHT, 1);
		}
		if (e.getKeyCode() == KeyEvent.VK_P) {
			pause();
		}
		// na testovanie
		if (e.getKeyCode() == KeyEvent.VK_A) {
			addBall();
		}
		if (e.getKeyCode() == KeyEvent.VK_R) {
			remove(ball);
		}
	}

	public void pause() {
		if (!pause) {
			setTickPeriod(0);
			pause = !pause;
		} else {
			pause = !pause;
			setTickPeriod(8);
		}
	}

	protected void onKeyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			// pustili sme lavu sipku, lavy flipper moze padat nadol
			lFlip.setLeft(false);
		}

		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			// pustili sme pravu sipku, pravy flipper moze padat nadol
			rFlip.setRight(false);
		}

		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			if (readyToGo) {
				ball.setVelocityY(-shotSpeed);
				readyToGo = false;
			}
			pullingString = false;
		}
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

	// odpalovac
	private void plunger() {
		double r = ball.getBallRadius();
		// v odpalovaci
		if (ball.getX() > getWidth() - 35 && ball.getX() < getWidth()) {
			ball.setMaximumVelocity(20);
			// odraz od stien odpalovaca
			if (ball.getY() > 50) {
				if (ball.getX() + ball.getVelocityX() - r < getWidth() - 35
						|| ball.getX() + ball.getVelocityX() + r > getWidth()) {
					ball.setVelocityX(-ball.getVelocityX());
				}
			} else {
				// odrazac nad odpalovacom
				if (ball.getY() + ball.getVelocityY() - r * 1.5 < 35 / 2.0) {
					ball.setPosition(ball.getX(), 35 / 2.0 + r * 1.5);
					ball.reverseVelocity();
				}
			}
			// odraz od spodku odpalovaca
			if (ball.getY() + ball.getVelocityY() + r > yS) {
				ball.setVelocityY(-ball.getVelocityY());
				ball.setVelocityY(ball.getVelocityY() * 0.5);
				ball.setVelocityX(ball.getVelocityX() * 0.5);
			}
			// statie na spodku odpalovaca
			if (ball.getY() > yS - r - 0.1 && ball.getY() < yS + r + 0.1 && ball.getVelocityY() < 0.1) {
				readyToGo = true;
				ball.setPosition(ball.getX(), ball.getY() - 0.1);
			}
			// roh steny odpalovaca
			Turtle corner = new Turtle();
			corner.setTransparency(1);
			add(corner);
			corner.setPosition(ball.getX() + ball.getVelocityX(), ball.getY() + ball.getVelocityY());
			if (corner.distanceTo(getWidth() - 35, 50) < r) {
				ball.reverseVelocity();
			}
			remove(corner);
		} else {
			ball.setMaximumVelocity(10);
		}
	}

	// odraz a zmensenie rychlosti od krajov
	private void tableBordersInteraction() {
		double r = ball.getBallRadius();
		// odraz a zmensenie rychlosti od krajov
		if (ball.getX() < getWidth() - 35 - r
				&& (ball.getX() + ball.getVelocityX() < r || ball.getX() + ball.getVelocityX() > getWidth() - r - 35)) {
			ball.setVelocityX(-ball.getVelocityX());
			ball.bounceAndFriction();
		}
		if (ball.getY() + ball.getVelocityY() < r) {
			ball.setVelocityY(-ball.getVelocityY());
			ball.bounceAndFriction();
		}
		// lava strana lieviku smerom k packam
		double lL = flipperEndL + lFlip.getFlipperWidth() / Math.sqrt(2);
		if (ball.getX() + ball.getVelocityX() - r <= lL
				&& ball.getY() + ball.getVelocityY() + r * Math.sqrt(2) >= yS - lL + (ball.getX() + ball.getVelocityX())
				&& ball.getY() < yS) {
			// proti lepeniu a prepadavaniu
			ball.setY(yS - lL + (ball.getX() + ball.getVelocityX()) - ball.getVelocityY() - r * Math.sqrt(2) - 2);
			ball.setX(ball.getX() + 2);
			ball.lawOfReflection(Math.toRadians(45));
		}
		// prava strana lieviku smerom k packam
		double lR = getWidth() - 35 - flipperEndR + rFlip.getFlipperWidth() / Math.sqrt(2);
		if (ball.getX() + ball.getVelocityX() + r >= flipperEndR
				&& ball.getX() + ball.getVelocityX() + r <= getWidth() - 35
				&& ball.getY() + ball.getVelocityY() + r + 1 >= yS - lR
						+ (getWidth() - 35 - ball.getX() - ball.getVelocityX())
				&& ball.getY() + ball.getVelocityY() < yS + 10) {
			// proti lepeniu a prepadavaniu
			ball.setY(
					yS - lR + (getWidth() - 35 - ball.getX() - ball.getVelocityX()) - ball.getVelocityY() - r - 1 - 2);
			ball.setX(ball.getX() - 2);
			ball.lawOfReflection(Math.toRadians(315));
		}
		// v diere
		if (ball.getY() + ball.getVelocityY() + r > yS + 10 && (ball.getX() + ball.getVelocityX() + r < flipperEndL
				|| ball.getX() + ball.getVelocityX() + r > flipperEndR)) {
			ball.setVelocityX(-ball.getVelocityX());
			ball.bounceAndFriction();
		}
		if (ball.getY() + ball.getVelocityY() >= getHeight()) {
			remove(ball);
			lostBalls++;
			fallenBall = true;
		}
	}

	public void closeToBorder() {
		// odraz a zmensenie rychlosti od krajov
		tableBordersInteraction();
		plunger();
	}

	// interakcia s flippermi
	public double flipperSurfaceEquation(Flipper flipper, double flipperEnd) {
		return yS - flipper.getFlipperWidth() / (2 * Math.cos(Math.toRadians(Math.abs(flipper.getDirection()))))
				+ l * Math.sin(Math.toRadians(Math.abs(flipper.getDirection())))
				- Math.tan(Math.toRadians(Math.abs(flipper.getDirection())))
						* (l * Math.cos(Math.toRadians(Math.abs(flipper.getDirection())))
								- (ball.getX() + ball.getVelocityX() - flipperEnd));
	}

	public void leftFlipperInteraction() {
		double r = ball.getBallRadius();
		double vL = ball.distanceTo(xSL - l / 2, yS) * 0.2;
		if (ball.getX() + ball.getVelocityX() > flipperEndL && ball.getX() + ball.getVelocityX() < flipperEndL
				+ l * Math.cos(Math.toRadians(lFlip.getDirection()))) {
			double lFlipSurfaceEq = flipperSurfaceEquation(lFlip, flipperEndL);
			Turtle future = ball.futurePosition(this);
			double angle = lFlip.getDirection() - future.directionTowards(flipperEndL, yS);
			double angle3 = 360 + lFlip.getDirection() - future.directionTowards(flipperEndL, yS);
			if (ball.getY() + ball.getVelocityY() + r * Math.sqrt(2) > lFlipSurfaceEq) {
				if (lFlip.getDirection() < 45 && angle3 > 90) {
					if (!lFlip.isLeft()) {
						ball.controlledReflection(-vL, vL, lFlip.getDirection());
					} else {
						ball.controlledReflection(vL, -vL, lFlip.getDirection());
					}
				} else if (lFlip.getDirection() > 315 && angle > 90) {
					if (!lFlip.isLeft()) {
						ball.controlledReflection(vL, vL, lFlip.getDirection() - 360);
					} else {
						ball.controlledReflection(vL, -vL, lFlip.getDirection());
					}
				} else if (lFlip.getDirection() == 45 && angle3 > 90) {
					ball.setY(lFlipSurfaceEq - ball.getVelocityY() - r * Math.sqrt(2) - 2);
					ball.setX(ball.getX() + 2);
					ball.lawOfReflection(Math.toRadians(45));
				} else if (lFlip.getDirection() == 315 && angle > 90) {
					if (yS - ball.getY() < r + 15) {
						ball.setPosition(ball.getX(), ball.getY() - 5);
					} else {
						ball.setY(lFlipSurfaceEq - ball.getVelocityY() - r * Math.sqrt(2) - 2);
						ball.setX(ball.getX() - 2);
						ball.lawOfReflection(Math.toRadians(315));
					}
				}
			}
			remove(future);
		}
	}

	public void rightflipperInteraction() {
		double r = ball.getBallRadius();
		double vR = ball.distanceTo(xSR + l / 2, yS) * 0.2;
		if (ball.getX() + ball.getVelocityX() < flipperEndR && ball.getX() + ball.getVelocityX() > flipperEndR
				- l * Math.cos(Math.toRadians(rFlip.getDirection()))) {
			double rFlipSurfaceEq = flipperSurfaceEquation(rFlip, flipperEndR);
			Turtle future = ball.futurePosition(this);
			double angle = 360 - rFlip.getDirection() + future.directionTowards(flipperEndR, yS);
			double angle2 = future.directionTowards(flipperEndR, yS) - rFlip.getDirection();
			if (ball.getY() + ball.getVelocityY() + r * Math.sqrt(2) > rFlipSurfaceEq) {
				if (rFlip.getDirection() > 315 && angle > 90) {
					if (!rFlip.isRight()) {
						ball.controlledReflection(vR, vR, rFlip.getDirection() - 360);
					} else {
						ball.controlledReflection(vR, -vR, rFlip.getDirection());
					}
				} else if (rFlip.getDirection() < 45 && angle2 > 90) {
					if (!rFlip.isRight()) {
						ball.controlledReflection(-vR, vR, rFlip.getDirection());
					} else {
						ball.controlledReflection(vR, -vR, rFlip.getDirection());
					}
				} else if (rFlip.getDirection() == 45 && angle2 > 90) {
					if (ball.distanceTo(flipperEndR, yS) < r + 15) {
						ball.setPosition(ball.getX(), ball.getY() - 5);
					}
					ball.setY(rFlipSurfaceEq - ball.getVelocityY() - r * Math.sqrt(2) - 2);
					ball.setX(ball.getX() + 2);
					ball.lawOfReflection(Math.toRadians(45));
				} else if (rFlip.getDirection() == 315 && angle > 90) {
					// proti lepeniu a prepadavaniu
					ball.setY(rFlipSurfaceEq - ball.getVelocityY() - r * Math.sqrt(2) - 2);
					ball.setX(ball.getX() - 2);
					ball.lawOfReflection(Math.toRadians(315));
				}
			}
			remove(future);
		}
	}

	// kontrola ci ma hra bezat a zapis skore pre GameOverScene
	private void checkGame() {
		if (lostBalls == 3) {
			try (PrintWriter pw = new PrintWriter(new File("score.txt"))) {
				pw.print(score.getScore());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ballsLeft.decreaseScore(1);
			stopMission();
			JPAZUtilities.delay(500);
			getStage().changeScene(GameOverScene.NAME, TransitionEffect.FADE_OUT_FADE_IN, 1500);
		} else {
			if (fallenBall) {
				stopMission();
				addBall();
				ballsLeft.decreaseScore(1);
				fallenBall = false;
			}
		}
	}

	private void stopMission() {
		MissionControl.stopMission(missionTypes);
		lastScore = score.getScore();
		timer = 0;
		missionInfo.clear();
		MissionControl.writeMissionStatus(infoWriter, missionInfo, missionTypes);
	}

	// metoda na riadenie misii - ak je misia spustena, kolko casu este ostava,
	// zastavenie misie, ak nie je spustena, kolko bodov chyba k spusteniu, riadenie
	// spustenia misie (kontrola, ci su splnene podmienky)
	private void missionControl() {
		if (timer > 0 && timer <= (1 / (getTickPeriod() / 1000.0)) * missionTime) {
			if(ball.getX() < 50 && ball.getY() < 50) {
				timer = 1;
			}
			lastScore = score.getScore();
			missionInfo.clear();
			MissionControl.writeMissionStatus(infoWriter, missionInfo, missionTypes);
			infoWriter.setPosition(missionInfo.getWidth() / 2, 3 * missionInfo.getHeight() / 4 - 8);
			infoWriter.step(-75);
			infoWriter.print(
					"Time left: " + Math.round((missionTime - timer / (1 / (getTickPeriod() / 1000.0))) * 10d) / 10d);
		}
		if (timer > (1 / (getTickPeriod() / 1000.0)) * missionTime) {
			stopMission();
		}
		if (timer == 0) {
			missionInfo.clear();
			MissionControl.writeMissionStatus(infoWriter, missionInfo, missionTypes);
			infoWriter.setPosition(missionInfo.getWidth() / 2, 3 * missionInfo.getHeight() / 4 - 8);
			infoWriter.step(-100);
			infoWriter.print("Score " + (pointsForMission - (score.getScore() - lastScore)) + " more pts!");
		}
		if (MissionControl.checkMissionStatus(score.getScore(), lastScore, pointsForMission)) {
			MissionControl.startMission(missionTypes);
			MissionControl.writeMissionStatus(infoWriter, missionInfo, missionTypes);
			infoWriter.setPosition(missionInfo.getWidth() / 2, 3 * missionInfo.getHeight() / 4 - 8);
			infoWriter.step(-75);
			infoWriter.print("Time left: " + missionTime);
		}
		timer = MissionControl.timer(timer, missionTypes, missionTime, getTickPeriod());
	}

	// metoda na ukladanie kazdeho n-teho snimku obrazovky, kde n = gifTimer, sluzi
	// na robenie gifov z danych obrazkov
	public void makeAGif(String fileName) {
		if (gifTimer % 10 == 0) {
			savePicture(fileName);
		}
	}

	@Override
	protected void onTick() {
		gifTimer++;
		powerBarAnimation();
		if (lFlip.isLeft()) {
			lFlip.rotate(l / 2, 5, xSL, yS);
		}
		if (rFlip.isRight()) {
			rFlip.rotate(l / 2, 5, xSR, yS);
		}
		// samovolny pohyb flipperov
		if (!lFlip.isLeft()) {
			lFlip.rotate(l / 2, -5, xSL, yS);
		}
		if (!rFlip.isRight()) {
			rFlip.rotate(l / 2, -5, xSR, yS);
		}
		// kontrola ci nenastal odraz
		closeToBorder();
		// nastavenie rychlosti a spomalenie
		double accelDown = -9.81 * Math.sin(10 * Math.PI / 180) * 0.5 * 0.07;
		ball.setVelocityY(ball.getVelocityY() - accelDown);
		// pohyb
		leftFlipperInteraction();
		rightflipperInteraction();
		bumpersInteraction();
		slingshotsInteraction();
		wormholeInteraction();
		ball.roll();
		missionControl();
		checkGame();
		//makeAGif("gameplayGif" + gifTimer/10 + ".png");
	}
}