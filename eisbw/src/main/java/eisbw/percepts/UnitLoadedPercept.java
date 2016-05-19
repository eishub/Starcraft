package eisbw.percepts;

import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Percept;

public class UnitLoadedPercept extends Percept {

  private static final long serialVersionUID = 1L;

  public UnitLoadedPercept(int id, String type) {
    super(Percepts.UNITLOADED, new Numeral(id), new Identifier(type));
  }
}
