package test.utils;

import org.junit.Test;
import main.utils.ATTACKRESULT;
import main.utils.WhiteAttackTable;
import static org.junit.Assert.*;

public class WhiteAttackTableTest {
	//mhAttack()
	@Test
	public void rollOfOneShouldReturnMiss(){
		WhiteAttackTable at = new WhiteAttackTable(80, 300, 300, 300, 1);
		assertEquals(at.mhAttack(), ATTACKRESULT.MISS);
		
		
	}
	
	//ohAttack()
}
