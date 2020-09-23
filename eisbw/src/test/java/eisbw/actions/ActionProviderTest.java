package eisbw.actions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eis.iilang.Action;
import eis.iilang.Numeral;

public class ActionProviderTest {
	private ActionProvider actionProvider;

	@Test
	public void test() {
		this.actionProvider = new ActionProvider();
		this.actionProvider.loadActions(null, null);
		final Action action = new Action("attack", new Numeral(0));
		assertEquals(new Attack(null).toString(), this.actionProvider.getAction(action).toString());
	}
}
