package eisbw.actions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import eisbw.BwapiUtility;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;

public class SendTextTest {
    private SendText action;
    private LinkedList<Parameter> params;

    @Mock
    private JNIBWAPI bwapi;
    @Mock
    private Action act;
    @Mock
    private Unit unit;
    @Mock
    private UnitType unitType;

    /**
     * Initialize mocks.
     */
    @Before
    public void start() {
        MockitoAnnotations.initMocks(this);
        this.action = new SendText(this.bwapi);

        this.params = new LinkedList<>();
        this.params.add(new Identifier("Working"));

        when(this.unit.isBeingConstructed()).thenReturn(false);
        when(this.act.getParameters()).thenReturn(this.params);
        when(this.unit.getType()).thenReturn(this.unitType);
        BwapiUtility.clearCache(0);
    }

    @Test
    public void isValid_test_with_numeral() {
        this.params.set(0, new Numeral(1));
        assertFalse(this.action.isValid(this.act));
    }

    @Test
    public void isValid_test() {
        this.params.set(0, new Identifier("Working"));
        assertTrue(this.action.isValid(this.act));
    }

    @Test
    public void isValidChatString_too_long() {
        String too_long = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        assertFalse(this.action.isValidChatString(too_long));
    }

    @Test
    public void isValidChatString_accepted() {
        String accepted_string = "Test string!";
        assertTrue(this.action.isValidChatString(accepted_string));
    }

    @Test
    public void isValidChatString_containsRaceName() {
        String terran = "TeRrAn";
        assertFalse(this.action.isValidChatString(terran));
        String zerg = "zerG";
        assertFalse(this.action.isValidChatString(zerg));
        String protoss = "Protoss";
        assertFalse(this.action.isValidChatString(protoss));
    }

    @Test
    public void isValidChatString_containsSlash() {
        String slash = "/command";
        assertFalse(this.action.isValidChatString(slash));
    }

    @Test
    public void isValidChatString_isCheat() {
        String cheat = "there is no cow level";
        assertFalse(this.action.isValidChatString(cheat));
    }

    @Test
    public void isValidChatString_checkRateLimit() {
        String message = "Test";
        assertTrue(this.action.isValidChatString(message));
        this.action.execute(unit, act);
        assertFalse(this.action.isValidChatString(message));
        this.action.execute(unit, act);
    }

    @Test
    public void canExecute_is_true() {
        assertTrue(this.action.canExecute(this.unitType, this.act));
    }

}
