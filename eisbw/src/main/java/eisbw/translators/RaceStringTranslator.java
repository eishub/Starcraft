package eisbw.translators;

import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Parameter2Java;
import eis.iilang.Identifier;
import eis.iilang.Parameter;
import eisbw.configuration.RaceString;
import eisbw.constants.Races;

public class RaceStringTranslator implements Parameter2Java<RaceString> {

  @Override
  public RaceString translate(Parameter param) throws TranslationException {
    if (!(param instanceof Identifier)) {
      throw new TranslationException("Invalid parameter " + param + ", must be a string");
    }
    String id = ((Identifier) param).getValue().toLowerCase();

    if (Races.getRaceList().contains(id)) {
      return new RaceString(id);
    }

    throw new TranslationException("Parameter " + id + " should be a race.");
  }

  @Override
  public Class<RaceString> translatesTo() {
    return RaceString.class;
  }

}
