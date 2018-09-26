package sk.upjs.Pinball;

import sk.upjs.jpaz2.ImageTurtleShape;
import sk.upjs.jpaz2.Pane;
import sk.upjs.jpaz2.Turtle;

public class Button extends Turtle {
	
	public Button(Pane pane, String picture, double x, double y, double scale) {
		makeButton(pane, picture, x, y, scale);
	}
	
	public void makeButton(Pane pane, String picture, double x, double y, double scale) {
		setShape(new ImageTurtleShape(picture));
		pane.add(this);
		setPosition(x, y);
		setScale(scale);
	}

	public boolean isMouseOver(int x, int y) {
		return containsInShape(x, y);
	}


}
