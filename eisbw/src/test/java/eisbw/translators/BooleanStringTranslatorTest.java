package eisbw.translators;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import eis.eis2java.exception.TranslationException;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eisbw.configuration.BooleanString;

public class BooleanStringTranslatorTest {
	private BooleanStringTranslator translator;

	@Before
	public void start() {
		this.translator = new BooleanStringTranslator();
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
		assertEquals(new BooleanString("false").getData(),
				this.translator.translate(new Identifier("false")).getData());
		assertEquals(new BooleanString("true").getData(), this.translator.translate(new Identifier("true")).getData());
	}

	@Test
	public void translatesTo_test() {
		assertEquals(BooleanString.class, this.translator.translatesTo());
	}

}
