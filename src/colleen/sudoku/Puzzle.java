package colleen.sudoku;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Colleen
 *
 */
@SuppressWarnings("unchecked")
public class Puzzle
{
	private List<Integer>[][] possibles = new LinkedList[9][9];
	
	/* Uses numbers in init to generate an initial puzzle.
	 * Argument must be 9 x 9 int array; if not, will throw IllegalArgumentException 
	 * Values 1-9 will be treated as puzzle initial known values
	 * All other values will be interpreted as blank spaces
	 */
	public Puzzle(int[][] init) throws IllegalArgumentException
	{
		// Checks for proper x dimension
		if(init.length != 9) throw new IllegalArgumentException("Improper puzzle dimensions");
		
		// Fills out possibles
		for(int x = 0; x < init.length; x++)
		{
			// Checks for proper y dimension
			if(init[x].length != 9) throw new IllegalArgumentException("Improper puzzle dimensions");
			for(int y = 0; y < init[x].length; y++)
			{
				if(init[x][y] >= 1 && init[x][y] <= 9)	
				{
					possibles[x][y] = new LinkedList<Integer>();
					possibles[x][y].add(init[x][y]);
				}
				else
				{
					possibles[x][y] = new LinkedList<Integer>();
					for(int i = 1; i <= 9; i++)
						possibles[x][y].add(i);
				}
			}
		}
	}

	/* Takes in a precomputed possibles array and copies
	 * it. If not a 9x9 grid, or contains values outside 
	 * 1-9, throws IllegalArgumentException.
	 */
	public Puzzle(List<Integer>[][] init)
	{
		// Checks for proper x dimension
		if(init.length != 9) throw new IllegalArgumentException("Improper puzzle dimensions");	
		
		for(int x = 0; x < init.length; x++)
		{
			// Checks for proper y dimension
			if(init[x].length != 9) throw new IllegalArgumentException("Improper puzzle dimensions");
			for(int y = 0; y < init[x].length; y++)
			{
				for(Integer item : init[x][y])
				{
					if(item < 1 || item > 9) 
						throw new IllegalArgumentException("Must use numbers 1-9");
					else 
						possibles[x][y] = new LinkedList<Integer>(init[x][y]);
				}
			}
		}	
	}
	
	/* Attempts to modify the puzzle to a solved state.
	 * If it successfully achieves a solved state, returns true
	 * and can output the solved puzzle
	 * If the puzzle is somehow unsolvable, returns false
	 */
	public boolean solve()
	{
		
		SolvingLoop:
		do
		{
			if(sweep() == -1) return false;
			
			for(int y = 0; y < possibles.length; y++)
			{
				for(int x = 0; x < possibles[y].length; x++)
				{
					if(possibles[x][y].size() > 1)
					{
						Puzzle aGuess = guess(x, y, possibles[x][y].get(0));
						if(aGuess.solve())
						{
							possibles = aGuess.possibles;
							return true;
						}
						else
						{
							possibles[x][y].remove(0);
							continue SolvingLoop;
						}
					}
				}
			}
		}while(!isSolved());
		
		return false;
	}
	
	/* Returns whether the puzzle is in a solved state 
	 * ie, whether each possibles list has exactly 1 element.
	 */
	public boolean isSolved()
	{
		boolean flag = true;
		for(List<Integer>[] col : possibles)
		{
			for(List<Integer> space : col)
				flag = flag && (space.size() == 1);
		}
		return flag;
	}
	
	/* Returns a puzzle that is a copy of this puzzle, except
	 * that coordinate described is made to be a known value
	 * containing guessedNum.
	 */
	Puzzle guess(int xCoord, int yCoord, int guessedNum)
	{
		Puzzle puzz = new Puzzle(possibles);
		puzz.possibles[xCoord][yCoord].clear();
		puzz.possibles[xCoord][yCoord].add(guessedNum);
		return puzz;
	}
	
	/* Returns an array with the first element from each possibles list.
	 * If the puzzle is in a solved state, this will be the solution.
	 */
	public int[][] outputSoln()
	{
		int[][] output = new int[9][9];
		
		for(int x = 0; x < possibles.length; x++)
		{
			for(int y = 0; y < possibles[x].length; y++)
				output[x][y] = possibles[x][y].get(0);
		}
		
		return output;
	}
	
	/* Edits the possibles array to account for constraints from known spaces
	 * if the possibles array is modified in this process, returns 1; if it
	 * is unchanged, returns 0. If the puzzle is deemed unsolvable, returns -1.
	 */
	int sweep()
	{
		boolean retFlag = false; // for tracking return value
		boolean loopFlag = false;
		
		do
		{
			loopFlag = false;
			
			int colVal = sweepColumns();
			if(colVal == -1) return -1;
			int rowVal = sweepRows();
			if(rowVal == -1) return -1;
			int boxVal = sweepBoxes();
			if(boxVal == -1) return -1;
			
			if(colVal == 1 || rowVal == 1 || boxVal == 1)
			{
				retFlag = true;
				loopFlag = true;
			}
			
		} while(loopFlag);
		
		if(retFlag) return 1;
		return 0;
	}
	
	/* Traverses possibles by columns, updating for knowns. If puzzle is
	 * modified, returns 1. If puzzle is deemed unsolvable, returns -1.
	 * Otherwise, returns 0.
	 */
	int sweepColumns()
	{
		boolean modFlag = false; // Tracks whether this method has done modifications, for return purposes
		
		for(int x = 0; x < possibles.length; x++)
		{
			List<Integer>[] column = getColumn(x);
			if(updateGroup(column))
			{
				if(!validateGroup(column)) return -1;
				modFlag = true;
			}
		}
		
		if(modFlag) return 1;
		return 0;
	}
	
	/* Traverses possibles by rows, updating for knowns. If puzzle is
	 * modified, returns 1. If puzzle is deemed unsolvable, returns -1.
	 * Otherwise, returns 0.
	 */
	int sweepRows()
	{
		boolean modFlag = false; // Tracks whether this method has done modifications, for return purposes
		
		for(int y = 0; y < possibles.length; y++)
		{
			List<Integer>[] row = getRow(y);
			if(updateGroup(row))
			{
				if(!validateGroup(row)) return -1;
				modFlag = true;
			}
		}
		
		if(modFlag) return 1;
		return 0;
	}
	
	/* Traverses possibles by boxes, updating for knowns. If puzzle is
	 * modified, returns 1. If puzzle is deemed unsolvable, returns -1.
	 * Otherwise, returns 0.
	 */
	int sweepBoxes()
	{
		boolean modFlag = false; // Tracks whether this method has done modifications, for return purposes
		
		for(int x = 0; x < 3; x++)
		{
			for(int y = 0; y < 3; y++)
			{
				List<Integer>[] box = getBox(x, y);
				if(updateGroup(box))
				{
					if(!validateGroup(box)) 
						return -1;
					modFlag = true;
				}
			}
		}
		
		if(modFlag) return 1;
		return 0;
	}
	
	/* Edits each possibles space in accordance with known spaces in group, once.
	 * group represents a set of spaces that share constraints with each other
	 * that is, a single row, column, or box of 9 spaces. Returns true if caused
	 * modifications, false otherwise. If group is not a 9-element array, throws
	 * IllegalArgumentException.
	 */
	boolean updateGroup(List<Integer>[] group) throws IllegalArgumentException
	{
		if(group.length != 9) throw new IllegalArgumentException("Improper argument dimensions");
		
		boolean modFlag = false; // variable to track modifications
		
		ArrayList<Integer> knowns = new ArrayList<Integer>();
		for(List<Integer> space : group)
			if (space.size() == 1) knowns.add(space.get(0));
		
		for(List<Integer> space : group)
			if(space.size() != 1)	modFlag = modFlag | space.removeAll(knowns);
		
		outer:
		for(int i = 1; i <= 9; i++)
		{
			List<Integer> firstInstance = null;
			for(List<Integer> space : group)
			{
				if(space.contains(i))
				{
					if(firstInstance != null || space.size() == 1) continue outer;
					firstInstance = space;
				}
			}
			
			if(firstInstance == null) continue;
			firstInstance.clear();
			firstInstance.add(i);
			modFlag = true;
		}
		
		return modFlag;
	}
	
	/* Checks whether the given group of related space clues 
	 * has conflicting ie, a space that has no allowed numbers. 
	 * If there is a conflict, returns false; if the given group 
	 * arrangement can obey sudoku rules, return true.
	 * DOES NOT reduce/modify clues!
	 */
	boolean validateGroup(List<Integer>[] group)
	{
		for(List<Integer> item : group)
			if(item.size() == 0) return false;
		
		return true;
	}
	
	/*	Extracts all entries from the possibles matrix
	 *	that reside in the xth column, using 0-biased counting. 
	 *	If x < 0 or x > 8, throws IllegalArgumentException.
	 */
	private List<Integer>[] getColumn(int x)
	{
		if(!(x <= 8 && x >= 0)) throw new IllegalArgumentException("Improper argument dimensions");
		List<Integer>[] forReturn = new List[9];
		for(int i = 0; i < forReturn.length; i++)
			forReturn[i] = possibles[x][i];
		return forReturn;
	}
	
	/*	Extracts all entries from the possibles matrix
	 *	that reside in the yth row, using 0-biased counting. 
	 *	If x < 0 or x > 8, throws IllegalArgumentException.
	 */
	private List<Integer>[] getRow(int y)
	{
		if(!(y <= 8 && y >= 0)) throw new IllegalArgumentException("Improper argument dimensions");
		List<Integer>[] forReturn = new List[9];
		for(int i = 0; i < forReturn.length; i++)
			forReturn[i] = possibles[i][y];
		return forReturn;
	}
	
	/*	Extracts all entries from the possibles matrix
	 *	that reside in the box described by a and y, 
	 *	using 0-biased counting. If x < 0 or x > 2, 
	 *	or y < 0 or y > 2 throws IllegalArgumentException.
	 */
	private List<Integer>[] getBox(int x, int y)
	{
		if(x < 0 || x > 2) throw new IllegalArgumentException("Improper argument dimensions");
		if(y < 0 || y > 2) throw new IllegalArgumentException("Improper argument dimensions");
		List<Integer>[] forReturn = new List[9];
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				forReturn[(i*3) + j] = (possibles[(x*3) + i][(y*3) + j]);
			}
		}
		
		return forReturn;
	}
}
