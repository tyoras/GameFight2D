package yoan.game.gf2d.world.model;

import org.junit.Test;

import yoan.game.gf2d.world.model.champions.Champion;
import yoan.game.gf2d.world.model.champions.Champion.State;
import static org.fest.assertions.api.Assertions.assertThat;

public class ChampionTest {
	
	private class TestChampionImpl extends Champion {
		public TestChampionImpl(float x, float y, float width, float heigth){
			super(x, y, width, heigth);
		}
	}
	
	@Test
	public void should_be_initialized_with_state_FALL() {
		//given
		Champion justInitializedChamp = new TestChampionImpl(42, 42, 42, 42);
		
		//when
		State state = justInitializedChamp.state;
		
		//then
		assertThat(state).as("initial state").isEqualTo(State.FALL);
	}
	
	
}
