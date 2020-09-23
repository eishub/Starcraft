package eisbw.translators;

import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Parameter2Java;
import eis.iilang.Identifier;
import eis.iilang.Parameter;
import eisbw.configuration.BooleanString;

/**
 * @author Danny & Harm - The translator which translates strings to booleans.
 */
public class BooleanStringTranslator implements Parameter2Java<BooleanString> {
	@Override
	public BooleanString translate(final Parameter param) throws TranslationException {
		if (!(param instanceof Identifier)) {
			throw new TranslationException("Invalid parameter " + param + ", must be a string");
		}

		final String id = ((Identifier) param).getValue();
		if ("false".equals(id) || "true".equals(id)) {
			return new BooleanString(id);
		} else {
			throw new TranslationException("Parameter " + param + " should be either true or false.");
		}
	}

	@Override
	public Class<BooleanString> translatesTo() {
		return BooleanString.class;
	}
}
