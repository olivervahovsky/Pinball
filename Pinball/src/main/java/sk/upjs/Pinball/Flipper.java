package sk.upjs.Pinball;

import sk.upjs.jpaz2.ImageTurtleShape;
import sk.upjs.jpaz2.theater.Actor;

public class Flipper extends Actor {

	private int flipperLength = 113;
	private int flipperWidth = 15;
	// smer otacania laveho resp. praveho flippera
	private int smer;
	// ci je pravy resp. lavy flipper aktivny (stlacena prislusna sipka)
	private boolean left;
	private boolean right;
	private double angle = 0;

	// pri vytvoreni flippera smerom urcime, aky flipper ideme vytvorit, podla
	// toho sa vyberie obrazok a urci jeho smer otacania
	public Flipper(int smer) {
		if (smer == -1) {
			setShape(new ImageTurtleShape("/images/flipper3.jpg"));
		} else {
			setShape(new ImageTurtleShape("/images/flipper4.jpg"));
		}
		this.smer = smer;
		penUp();
	}

	public int getFlipperLength() {
		return flipperLength;
	}

	public int getFlipperWidth() {
		return flipperWidth;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	/**
	 * Turns left flipper to left and right flipper to right
	 */
	public void rotate(double radius, double angle, double xS, double yS) {
		if (!(getDirection() + angle * smer >= 315 || getDirection() + angle * smer <= 45)) {
			return;
		}
		this.angle += angle;
		turn(angle * smer);
		setPosition(xS + smer * radius * (1 - Math.cos(Math.toRadians(this.angle))),
				yS - radius * Math.sin(Math.toRadians(this.angle)));
	}

}