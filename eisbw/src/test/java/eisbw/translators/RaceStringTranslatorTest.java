package eisbw.translators;

import static org.junit.Assert.assertEquals;

import eis.eis2java.exception.TranslationException;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eisbw.configuration.RaceString;

import org.junit.Before;
import org.junit.Test;

public class RaceStringTranslatorTest {
  private RaceStringTranslator translator;

  @Before
  public void start() {
    translator = new RaceStringTranslator();
  }

  @Test(expected = TranslationException.class)
  public void translateException_test() throws TranslationException {
    translator.translate(new Numeral(0));
  }

  @Test(expected = TranslationException.class)
  public void translateExceptionNotFound_test() throws TranslationException {
    translator.translate(new Identifier("notFound"));
  }

  @Test
  public void translate_test() throws TranslationException {
    assertEquals(new RaceString("zerg").getData(),
        translator.translate(new Identifier("zerg")).getData());
  }

  @Test
  public void translatesTo_test() {
    assertEquals(RaceString.class, translator.translatesTo());
  }
}
