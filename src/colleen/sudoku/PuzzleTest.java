package colleen.sudoku;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import org.junit.Test;
import junit.framework.TestCase;
import static org.junit.Assert.*;

@SuppressWarnings("unused")
public class PuzzleTest extends TestCase
{
	public static Puzzle tester;
	
	protected void setUp() throws Exception
	{
		super.setUp();
		int[][] placeholder = new int[9][9];
		tester = new Puzzle(placeholder);
	}

	@Test
	public void testUpdateGroup1()
	{
		@SuppressWarnings("unchecked")
		LinkedList<Integer>[] testList = new LinkedList[9];
		for(int i = 0; i < testList.length; i++)
		{
			testList[i] = new LinkedList<Integer>();
			if(i == 0)
				for(int j = 1; j <= 9; j++)
					testList[i].add(j);
			else
				testList[i].add(i+1);
		}
		
		assertTrue(tester.updateGroup(testList));
		assertEquals(1, testList[0].size());
		assertEquals(Integer.valueOf(1), testList[0].get(0));
	}
	
	@Test
	public void testUpdateGroup2()
	{
		@SuppressWarnings("unchecked")
		List<Integer>[] testList = new LinkedList[9];
		for(int i = 0; i < testList.length; i++)
		{
			testList[i] = new LinkedList<Integer>();
			for(int j = 1; j <= 9; j++)
				testList[i].add(j);
		}
		
		assertFalse(tester.updateGroup(testList));
		for(List<Integer> item : testList)
			assertEquals(9, item.size());
	}
	
	@Test
	public void testUpdateGroup3()
	{
		@SuppressWarnings("unchecked")
		List<Integer>[] testList = new List[9];
		for(int i = 0; i < testList.length; i++)
		{
			testList[i] = new ArrayList<Integer>();
			for(int j = 2; j <= 9; j++)
				testList[i].add(j);
		}
		testList[0].add(1);
		
		assertTrue(tester.updateGroup(testList));
		assertEquals(1, testList[0].size());
		assertEquals(Integer.valueOf(1), testList[0].get(0));
	
	}
	
	public void testSweepOnly1()
	{
		int[][] init = new int[9][9];
		
		init[0][0] = 3;
		init[1][0] = 6;
		init[2][0] = 1;
		init[2][1] = 2;
		init[1][2] = 4;
		
		init[5][1] = 6;
		
		init[6][2] = 6;
		init[7][2] = 3;
		
		init[1][3] = 8;
		init[2][4] = 9;
		init[1][5] = 5;
		
		init[3][3] = 6;
		init[4][4] = 4;
		init[4][5] = 9;
		
		init[7][3] = 7;
		init[8][3] = 2;
		init[8][4] = 3;
		init[8][5] = 1;
		
		init[0][8] = 8;
		init[1][8] = 3;
		
		init[5][6] = 1;
		init[3][7] = 9;
		init[3][8] = 5;
		init[4][8] = 2;
		
		init[7][6] = 8;
		init[6][7] = 4;
		init[8][8] = 9;
		
		for(int y = 0; y < init.length; y++)
		{
			for(int x = 0; x < init.length; x++)
			{
				System.out.print(init[x][y] + ",");
			}
			System.out.println();
		}
		
		Puzzle test = new Puzzle(init);
		test.solve();
		System.out.println();
		
		int[][] solution = test.outputSoln();
		
		for(int y = 0; y < init.length; y++)
		{
			for(int x = 0; x < init.length; x++)
			{
				System.out.print(solution[x][y] + ",");
			}
			System.out.println();
		}
	}
	
	public void testSolve1()
	{
		int[][] init = new int[9][9];
		
		init[1][0] = 5;
		init[1][1] = 8;
		init[1][2] = 9;
		
		init[5][1] = 9;
		
		init[6][1] = 4;
		init[6][2] = 5;
		
		init[0][3] = 9;
		init[1][4] = 1;
		init[2][4] = 2;
		
		init[3][3] = 1;
		init[3][5] = 2;
		init[4][4] = 8;
		
		init[8][3] = 5;
		init[8][4] = 6;
		init[7][4] = 4;
		init[6][5] = 7;
		
		init[0][7] = 8;
		init[1][8] = 3;
		init[2][6] = 1;
		
		init[3][6] = 7;
		init[3][8] = 9;
		init[4][7] = 1;
		init[5][8] = 2;
		
		init[6][6] = 2;
		init[6][8] = 6;
		init[8][8] = 8;
		
		for(int y = 0; y < init.length; y++)
		{
			for(int x = 0; x < init.length; x++)
			{
				System.out.print(init[x][y] + ",");
			}
			System.out.println();
		}
		
		Puzzle test = new Puzzle(init);
		test.solve();
		System.out.println();
		
		int[][] solution = test.outputSoln();
		
		for(int y = 0; y < init.length; y++)
		{
			for(int x = 0; x < init.length; x++)
			{
				System.out.print(solution[x][y] + ",");
			}
			System.out.println();
		}
		System.out.println();
	}
	
}
