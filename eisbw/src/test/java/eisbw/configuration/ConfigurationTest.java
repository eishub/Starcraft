
package eisbw.configuration;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import eis.eis2java.exception.NoTranslatorException;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Translator;
import eis.iilang.Identifier;
import eis.iilang.Parameter;
import eisbw.translators.BooleanStringTranslator;
import eisbw.translators.ParamEnumTranslator;
import eisbw.translators.RaceStringTranslator;

public class ConfigurationTest {

	/**
	 * register Translators.
	 */
	@Before
	public void start() {
		Translator.getInstance().registerParameter2JavaTranslator(new ParamEnumTranslator());
		Translator.getInstance().registerParameter2JavaTranslator(new BooleanStringTranslator());
		Translator.getInstance().registerParameter2JavaTranslator(new RaceStringTranslator());
	}

	@Test(expected = TranslationException.class)
	public void exception_test() throws NoTranslatorException, TranslationException {
		Map<String, Parameter> parameters = new HashMap<>();
		parameters.put("false_input", new Identifier("scdir"));
		new Configuration(parameters);
	}

	@Test
	public void noException_test() throws NoTranslatorException, TranslationException {
		Map<String, Parameter> parameters = new HashMap<>();
		parameters.put("debug", new Identifier("true"));
		parameters.put("own_race", new Identifier("terran"));
		parameters.put("enemy_race", new Identifier("zerg"));
		parameters.put("map", new Identifier("map"));
		parameters.put("starcraft_location", new Identifier("scdir"));
		parameters.put("auto_menu", new Identifier("Single_Player"));
		Configuration config = new Configuration(parameters);
		assertEquals(true, config.getDebug());
		assertEquals("terran", config.getOwnRace());
		assertEquals("zerg", config.getEnemyRace());
		assertEquals("map", config.getMap());
		assertEquals("scdir", config.getScDir());
		assertEquals("SINGLE_PLAYER", config.getAutoMenu());
	}

}
