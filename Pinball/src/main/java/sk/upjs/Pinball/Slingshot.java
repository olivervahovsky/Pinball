package sk.upjs.Pinball;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import sk.upjs.jpaz2.Pane;
import sk.upjs.jpaz2.Turtle;

public class Slingshot extends Object {

	private double x;
	private double y;
	private double vertexXD;
	private double vertexYD;
	private double vertexXU;
	private double vertexYU;
	private double heelX;
	private double heelY;
	private double length;
	private int sign;
	private List<Bumper> bumpers;

	public Slingshot(Pane pane, double x, double y, double length, int sign) {
		super();
		this.x = x;
		this.y = y;
		this.length = length;
		this.sign = sign;
		addSlingshot(pane);
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getVertexXD() {
		return vertexXD;
	}

	public void setVertexXD(double vertexXD) {
		this.vertexXD = vertexXD;
	}

	public double getVertexYD() {
		return vertexYD;
	}

	public void setVertexYD(double vertexYD) {
		this.vertexYD = vertexYD;
	}

	public double getVertexXU() {
		return vertexXU;
	}

	public void setVertexXU(double vertexXU) {
		this.vertexXU = vertexXU;
	}

	public double getVertexYU() {
		return vertexYU;
	}

	public void setVertexYU(double vertexYU) {
		this.vertexYU = vertexYU;
	}

	public double getHeelX() {
		return heelX;
	}

	public void setHeelX(double heelX) {
		this.heelX = heelX;
	}

	public double getHeelY() {
		return heelY;
	}

	public void setHeelY(double heelY) {
		this.heelY = heelY;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public int getSign() {
		return sign;
	}

	public void setSign(int sign) {
		this.sign = sign;
	}

	public List<Bumper> getBumpers() {
		return bumpers;
	}

	public void setBumpers(List<Bumper> bumpers) {
		this.bumpers = bumpers;
	}

	public void addSlingshot(Pane pane) {
		bumpers = new ArrayList<>();
		Turtle painter = new Turtle();
		pane.add(painter);
		bumpers.add(new Bumper(pane, x, y, 6, false));
		painter.setPosition(x, y);
		painter.turn(sign * 63.435);
		painter.step(1);
		heelX = painter.getX();
		heelY = painter.getY();
		painter.step(-1);
		painter.turn(-63.435 * sign);
		painter.setPenColor(Color.gray);
		painter.setPenWidth(3);
		painter.penDown();
		painter.step(length);
		vertexXU = painter.getX();
		vertexYU = painter.getY();
		bumpers.add(new Bumper(pane, vertexXU, vertexYU, 6, false));
		painter.step(-length);
		painter.turn(sign * 90);
		painter.step(length / 2);
		vertexXD = painter.getX();
		vertexYD = painter.getY();
		bumpers.add(new Bumper(pane, vertexXD, vertexYD, 6, false));
		painter.setPenWidth(5);
		painter.setPenColor(Color.cyan);
		painter.moveTo(vertexXU, vertexYU);
		pane.remove(painter);
	}
}
