package eisbw.translators;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import eis.eis2java.exception.TranslationException;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eisbw.configuration.ParamEnum;

public class ParamEnumTranslatorTest {

	private ParamEnumTranslator translator;

	@Before
	public void start() {
		this.translator = new ParamEnumTranslator();
	}

	@Test(expected = TranslationException.class)
	public void translateException_test() throws TranslationException {
		this.translator.translate(new Numeral(0));
	}

	@Test(expected = TranslationException.class)
	public void translateExceptionNotFound_test() throws TranslationException {
		this.translator.translate(new Identifier("notFound"));
	}

	@Test
	public void translate_test() throws TranslationException {
		assertEquals(ParamEnum.MAP, this.translator.translate(new Identifier("map")));
	}

	@Test
	public void translatesTo_test() {
		assertEquals(ParamEnum.class, this.translator.translatesTo());
	}

}
