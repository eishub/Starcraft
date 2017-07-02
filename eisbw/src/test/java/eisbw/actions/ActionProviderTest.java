package eisbw.actions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ActionProviderTest {
	private ActionProvider actionProvider;

	@Test
	public void test() {
		this.actionProvider = new ActionProvider();
		this.actionProvider.loadActions(null);
		assertEquals(new Attack(null).toString(), this.actionProvider.getAction("attack/1").toString());
	}
}
