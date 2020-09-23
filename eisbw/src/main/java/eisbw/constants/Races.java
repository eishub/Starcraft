package eisbw.constants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Danny & Harm - The data class with all race definition input.
 */
public class Races {
	private static List<String> raceNames;

	private Races() {
		// Static class.
	}

	/**
	 * Get a list from all racenames
	 *
	 * @return racenames.
	 */
	public static List<String> getRaceList() {
		if (raceNames == null) {
			raceNames = new ArrayList<>(8);
			raceNames.add("terran");
			raceNames.add("protoss");
			raceNames.add("zerg");
			raceNames.add("random");
			raceNames.add("randomtp");
			raceNames.add("randomtz");
			raceNames.add("randompz");
			raceNames.add("test");
		}
		return raceNames;
	}
}
