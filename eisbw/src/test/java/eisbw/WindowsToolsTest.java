package eisbw;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class WindowsToolsTest {
	@Test
	public void test() {
		assertNotNull(WindowsTools.getIniFile("race", "map", "OFF", "MELEE", "random"));
	}
}
