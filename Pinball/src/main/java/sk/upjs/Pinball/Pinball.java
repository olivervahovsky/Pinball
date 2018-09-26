package sk.upjs.Pinball;

import sk.upjs.jpaz2.AudioClip;
import sk.upjs.jpaz2.theater.*;

public class Pinball extends Stage {

	public Pinball() {
		super("Pinball", 535, 900);
	}

	@Override
	protected void initialize() {
		// initialize stage and add scenes
		AudioClip clip = new AudioClip("images", "neverending.mid", true);
		clip.setVolume(0.5);
		setBackgroundMusic(clip);
		addScene(IntroScene.NAME, new IntroScene(this));
		addScene(ClassicTable.NAME, new ClassicTable(this));
		addScene(GameOverScene.NAME, new GameOverScene(this));
	}
	
	public static void main(String[] args) {
		// create demo stage and start a show
		Pinball pinball = new Pinball();
		pinball.run(IntroScene.NAME);
	}
}