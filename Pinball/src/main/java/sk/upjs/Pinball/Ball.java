package sk.upjs.Pinball;

import sk.upjs.jpaz2.ImageTurtleShape;
import sk.upjs.jpaz2.Pane;
import sk.upjs.jpaz2.Turtle;
import sk.upjs.jpaz2.theater.Actor;

public class Ball extends Actor {

	// x-ova suradnica rychlosti
	private double velocityX;

	// y-ova suradnica rychlosti
	private double velocityY;

	// maximalna rychlost
	private double maximumVelocity = 10;

	private int ballRadius = 9;
	
	// koeficient trenia
	private double frictionCoeff = 0.7;

	public Ball() {
		setShape(new ImageTurtleShape("/images/ball.png"));
		penUp();
	}

	public int getBallRadius() {
		return ballRadius;
	}

	public double getMaximumVelocity() {
		return maximumVelocity;
	}
	
	public void setMaximumVelocity(double maximumVelocity) {
		this.maximumVelocity = maximumVelocity;
	}

	// uprava y-ovej suradnice rychlosti
	public void setVelocityY(double velocityY) {
		double velocity = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
		if (velocity > maximumVelocity) {
			double coeff = maximumVelocity / velocity;
			velocityX = velocityX * coeff;
			this.velocityY = velocityY * coeff;
		} else {
			this.velocityY = velocityY;
		}
	}

	public double getVelocityY() {
		return velocityY;
	}

	// uprava x-ovej suradnice rychlosti
	public void setVelocityX(double velocityX) {
		double velocity = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
		if (velocity > maximumVelocity) {
			double coeff = maximumVelocity / velocity;
			velocityY = velocityY * coeff;
			this.velocityX = velocityX * coeff;
		} else {
			this.velocityX = velocityX;
		}
	}

	public double getVelocityX() {
		return velocityX;
	}

	// na kontrolu korektnosti buducej pozicie korytnacky v niektorych pripadoch
	public Turtle futurePosition(Pane pane) {
		Turtle future = new Turtle();
		pane.add(future);
		future.setTransparency(0);
		future.setPosition(getX() + getVelocityX(), getY() + getVelocityY());
		return future;
	}

	// korekcie rychlosti za uvazenia trenia
	public void bounceAndFriction() {
		setVelocityY(getVelocityY() * frictionCoeff);
		setVelocityX(getVelocityX() * frictionCoeff);
	}

	// zakon odrazu pre rovne plochy
	public void lawOfReflection(double angle) {
		double velocityXContainer = getVelocityX();
		setVelocityX(getVelocityX() * Math.cos(2 * angle) + getVelocityY() * Math.sin(2 * angle));
		setVelocityY(getVelocityY() * Math.cos(2 * angle) + velocityXContainer * Math.sin(2 * angle));
		bounceAndFriction();
	}

	// odraz danou rychlostou
	public void controlledReflection(double velocityX, double velocityY, double angle) {
		setVelocityX(velocityX * Math.sin(Math.toRadians(angle)));
		setVelocityY(velocityY * Math.cos(Math.toRadians(angle)));
	}

	public void reverseVelocity() {
		double velocityContainer = getVelocityX();
		setVelocityX(getVelocityY());
		setVelocityY(velocityContainer);
	}

	// zakon odrazu vo vektorovej forme pre odraz od kruhovych objektov
	public void advancedReflection(double nx, double ny, double power) {
		double nxContainer = nx;
		nx = nx / Math.sqrt(nx * nx + ny * ny);
		ny = ny / Math.sqrt(nxContainer * nxContainer + ny * ny);
		double vx = getVelocityX();
		double vy = getVelocityY();
		setVelocityX((vx - 2 * (vx * nx + vy * ny) * nx) * 1.2);
		setVelocityY((vy - 2 * (vx * nx + vy * ny) * ny) * 1.2);
	}

	// pohyb gulicky
	public void roll() {
		setPosition(getX() + getVelocityX(), getY() + getVelocityY());
	}

}