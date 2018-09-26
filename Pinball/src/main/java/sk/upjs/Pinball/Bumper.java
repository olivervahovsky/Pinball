package sk.upjs.Pinball;

import java.awt.Color;

import sk.upjs.jpaz2.ImageTurtleShape;
import sk.upjs.jpaz2.Pane;
import sk.upjs.jpaz2.Turtle;

public class Bumper extends Turtle {

	private double bumperRadius;

	public Bumper(Pane table, double x, double y, double radius, boolean visible) {
		super();
		bumperRadius = radius;
		table.add(this);
		setPosition(x, y);
		if (visible) {
			setShape(new ImageTurtleShape("/images/bumper.gif"));
		} else {
			setFillColor(Color.gray);
			dot(radius);
			setTransparency(1);
		}
	}

	public double getBumperRadius() {
		return bumperRadius;
	}

	public void setBumperRadius(double bumperRadius) {
		this.bumperRadius = bumperRadius;
	}

	// test ci je lopticka v interakcnej oblasti bumpera
	public boolean closeEncounter(Ball ball) {
		return (distanceTo(ball.getX() + ball.getVelocityX(), ball.getY() + ball.getVelocityY()) < ball.getBallRadius()
				+ getBumperRadius());
	}

	// interakcia s bumperom
	public void bumperInteraction(Ball ball, Pane table, double bumperPower) {
		double nx = ball.getX() - getX();
		double ny = ball.getY() - getY();
		double nxContainer = nx;
		nx = nx / Math.sqrt(nx * nx + ny * ny);
		ny = ny / Math.sqrt(nxContainer * nxContainer + ny * ny);
		double vx = ball.getVelocityX();
		double vy = ball.getVelocityY();
		ball.setVelocityX((vx - 2 * (vx * nx + vy * ny) * nx) * bumperPower);
		ball.setVelocityY((vy - 2 * (vx * nx + vy * ny) * ny) * bumperPower);
	}

}
