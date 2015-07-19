package eisbw.percepts;

import eis.iilang.Numeral;
import eis.iilang.Percept;

public class VespeneGeyserPercept extends Percept {

    public VespeneGeyserPercept(int id, int resources, int resourceGroup, int x, int y) {
        super(Percepts.VespeneGeyser, new Numeral(id),new Numeral(resources), new Numeral(resourceGroup), new Numeral(x), new Numeral(y));
    }
}
