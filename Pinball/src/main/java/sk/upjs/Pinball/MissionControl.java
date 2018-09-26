package sk.upjs.Pinball;

import java.awt.Color;
import java.awt.Font;

import sk.upjs.jpaz2.Pane;
import sk.upjs.jpaz2.Turtle;

public class MissionControl {

	// kontrola podmienky pre spustenie misie
	public static boolean checkMissionStatus(int score, int lastScore, int starter) {
		return score - lastScore >= starter;
	}

	// spustenie nahodneho typu misie
	public static void startMission(boolean[] missionTypes) {
		int missionType = (int) (missionTypes.length * Math.random());
		missionTypes[missionType] = true;
	}

	// vrati String pre vypisanie stavu misii (ze nie je spustena ziadna alebo ak
	// ano, tak ktora)
	public static String printMission(boolean[] missions) {
		if (missions[0] == true)
			return "Bumper = x5 pts!";
		else if (missions[1] == true)
			return "Slingshot = x5 pts!";
		else if (missions[2] == true)
			return "Wormhole = x5 pts!";
		return "No active mission";
	}

	// zastavenie misie
	public static void stopMission(boolean[] missionTypes) {
		for (int i = 0; i < missionTypes.length; i++) {
			missionTypes[i] = false;
		}
	}

	// zvysovanie timera urcujuceho trvanie misie
	public static int timer(int timer, boolean[] missionTypes, int missionTime, double tickPeriod) {
		for (int i = 0; i < missionTypes.length; i++) {
			if (missionTypes[i] == true) {
				return ++timer;
			}
		}
		return timer;
	}

	// vypisanie stavu misii
	public static void writeMissionStatus(Turtle infoWriter, Pane missionInfo, boolean[] missionTypes) {
		infoWriter.setPosition(missionInfo.getWidth() / 2, missionInfo.getHeight() / 4 - 8);
		infoWriter.setDirection(90);
		infoWriter.printCenter(MissionControl.printMission(missionTypes));
	}

}
