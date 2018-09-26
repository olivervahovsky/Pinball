package sk.upjs.Pinball;

import java.awt.Color;

import sk.upjs.jpaz2.ImageTurtleShape;
import sk.upjs.jpaz2.Pane;
import sk.upjs.jpaz2.Turtle;

public class Painter extends Turtle {

	// metody na kreslenie jednoduchych ciar
	public void line(double x1, double y1, double x2, double y2) {
		setTransparency(1);
		setPosition(x1, y1);
		moveTo(x2, y2);
	}

	public static void line(Pane pane, double x1, double y1, double x2, double y2, Color color, double width) {
		Painter painter = new Painter();
		painter.setPenColor(color);
		painter.setPenWidth(width);
		pane.add(painter);
		painter.setPosition(x1, y1);
		painter.moveTo(x2, y2);
		pane.remove(painter);
	}

	// tvorba pozadia
	public static void background(Pane pane, String picture) {
		Painter background = new Painter();
		background.setShape(new ImageTurtleShape(picture));
		pane.add(background);
		background.center();
		background.stamp();
		pane.remove(background);
	}

	// tvorba animovaneho pozadia
	public static void gifBackground(Pane pane, String picture) {
		Painter background = new Painter();
		background.setShape(new ImageTurtleShape(picture));
		pane.add(background);
		background.center();
	}

	// lepenie obrazkov
	public static void picture(Pane pane, double x, double y, String cesta) {
		Painter painter = new Painter();
		painter.setShape(new ImageTurtleShape(cesta));
		pane.add(painter);
		painter.setPosition(x, y);
		painter.stamp();
		pane.remove(painter);
	}

}