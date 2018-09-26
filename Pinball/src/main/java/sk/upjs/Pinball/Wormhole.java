package sk.upjs.Pinball;

import sk.upjs.jpaz2.ImageTurtleShape;
import sk.upjs.jpaz2.Pane;
import sk.upjs.jpaz2.theater.Actor;

public class Wormhole extends Actor {
	
	public Wormhole(Pane pane, double x, double y) {
		setShape(new ImageTurtleShape("/images/wormhole.gif"));
		setScale(0.07);
		pane.add(this);
		setPosition(x, y);
	}

}
