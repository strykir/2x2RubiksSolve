import java.util.LinkedList;

public class Cube {	
	private class StringCubePair{
		Cube cube;
		String permut;
		
		public StringCubePair(String permut, Cube cube) {
			this.cube = cube;
			this.permut = permut;
		}
	}
	
	private static final int TOP = 0, 
					      BOTTOM = 1, 
					        LEFT = 2, 
					       FRONT = 3, 
						   RIGHT = 4, 
					        BACK = 5, 
						   
				       UPPERLEFT = 0, 
				      UPPERRIGHT = 1, 
				      LOWERRIGHT = 2, 
				       LOWERLEFT = 3;
	
	private final char[][] SOLVEDCUBE = {  // each face goes top left, top right, bottom right, bottom left (CW around center)
											{'W', 'W', 'W', 'W'}, // top
											{'Y', 'Y', 'Y', 'Y'}, // bottom
											{'G', 'G', 'G', 'G'}, // left
											{'R', 'R', 'R', 'R'}, // front
											{'B', 'B', 'B', 'B'}, // right
											{'O', 'O', 'O', 'O'}, // back
	};
	
	private char[][] cubeState;
	
	public Cube() {
		this.cubeState = cloneState(SOLVEDCUBE);
	}
	
	public Cube(char[][] inputCube) throws InvalidCubeException {
		if (inputCube.length != 6) throw new InvalidCubeException("Cube has more/less than 6 sides");
		for (int i = 0; i < inputCube.length; i++) {
			if (inputCube[i].length != 4) throw new InvalidCubeException("Cube has a face with more/less than 4 segments");
			for (int j = 0; j < inputCube[i].length; j++) {
				if (invalidColor(inputCube[i][j])) throw new InvalidCubeException("Cube has an invalid color (not W,R,G,O,B or Y)");
			}
		}
		this.cubeState = cloneState(inputCube);
	}
	
	public Cube(char[] top, char[] bottom, char[] left, char[] front, char[] right, char[] back) throws InvalidCubeException {
		if (top.length != 4) throw new InvalidCubeException("top face has more/less than 4 segments");
		if (bottom.length != 4) throw new InvalidCubeException("bottom face has more/less than 4 segments");
		if (left.length != 4) throw new InvalidCubeException("left face has more/less than 4 segments");
		if (front.length != 4) throw new InvalidCubeException("front face has more/less than 4 segments");
		if (right.length != 4) throw new InvalidCubeException("right face has more/less than 4 segments");
		if (back.length != 4) throw new InvalidCubeException("back face has more/less than 4 segments");
		
		char[][] checkCube = new char[6][4];
		checkCube[0] = top;
		checkCube[1] = bottom;
		checkCube[2] = left;
		checkCube[3] = front;
		checkCube[4] = right;
		checkCube[5] = back;
		
		for (int i = 0; i < 6; i++) 
			for (int j = 0; j < 4; j++)
				if (invalidColor(checkCube[i][j])) throw new InvalidCubeException("Cube has an invalid color (not W,R,G,O,B or Y)");
		
		this.cubeState = cloneState(checkCube);
	}
	
	public Cube(Cube cube) {
		this.cubeState = cloneState(cube.getCubeState());
	}
	
	public char[][] getCubeState(){
		return this.cubeState;
	}
	
	private boolean invalidColor(char color) {
		if (color == 'W') return false;
		if (color == 'R') return false;
		if (color == 'G') return false;
		if (color == 'O') return false;
		if (color == 'B') return false;
		if (color == 'Y') return false;
		return true;
	}
	
	public boolean solved() {
		for (int i = 0; i < 6; i++) {
			char faceColor = cubeState[i][0];
			for (int j = 1; j < 4; j++) {
				if (cubeState[i][j] != faceColor) return false;
			}
		}
		return true;
	}
	
	class InvalidCubeException extends Exception{
		public InvalidCubeException() {
			super();
		}
		public InvalidCubeException(String msg) {
			super(msg);
		}
	}
	
	public String toString() {
		String returnString = "";
		returnString += String.format("  %c%c\n", cubeState[TOP][UPPERLEFT], cubeState[TOP][UPPERRIGHT]);
		returnString += String.format("  %c%c\n", cubeState[TOP][LOWERLEFT], cubeState[TOP][LOWERRIGHT]);
		
		for (int face = 2; face < 6; face++) 
			returnString += String.format("%c%c", cubeState[face][UPPERLEFT], cubeState[face][UPPERRIGHT]);
		
		returnString += "\n";
		
		for (int face = 2; face < 6; face++) 
			returnString += String.format("%c%c", cubeState[face][LOWERLEFT], cubeState[face][LOWERRIGHT]);
		
		returnString += "\n";
		
		returnString += String.format("  %c%c\n", cubeState[BOTTOM][UPPERLEFT], cubeState[BOTTOM][UPPERRIGHT]);
		returnString += String.format("  %c%c"  , cubeState[BOTTOM][LOWERLEFT], cubeState[BOTTOM][LOWERRIGHT]);

		return returnString;
	}
	
	private static char[][] cloneState(char[][] state) {
		char[][] newState = new char[6][4];
		for (int i = 0; i < 6; i++) 
			for (int j = 0; j < 4; j++)
				newState[i][j] = state[i][j];
		
		return newState;
	}
	
	private static void rotateFaceCW(char[][] state, int face) {
		char temp = state[face][UPPERLEFT];
		state[face][UPPERLEFT]  = state[face][LOWERLEFT];
		state[face][LOWERLEFT]  = state[face][LOWERRIGHT];
		state[face][LOWERRIGHT] = state[face][UPPERRIGHT];
		state[face][UPPERRIGHT] = temp;
	}
	
	private static void rotateFaceCCW(char[][] state, int face) {
		char temp = state[face][UPPERLEFT];
		state[face][UPPERLEFT] = state[face][UPPERRIGHT];
		state[face][UPPERRIGHT] = state[face][LOWERRIGHT];
		state[face][LOWERRIGHT] = state[face][LOWERLEFT];
		state[face][LOWERLEFT] = temp;
	}
	
	public static Cube fRotation(Cube cube) {
		char[][] state = cloneState(cube.getCubeState());
		
		rotateFaceCW(state, FRONT);
		
		// rotate adjacent edges
		char temp1 = state[TOP][LOWERLEFT];
		char temp2 = state[TOP][LOWERRIGHT];
		state[TOP]   [LOWERLEFT]  = state[LEFT]  [LOWERRIGHT];
		state[TOP]   [LOWERRIGHT] = state[LEFT]  [UPPERRIGHT];
		state[LEFT]  [UPPERRIGHT] = state[BOTTOM][UPPERLEFT];
		state[LEFT]  [LOWERRIGHT] = state[BOTTOM][UPPERRIGHT];
		state[BOTTOM][UPPERLEFT]  = state[RIGHT] [LOWERLEFT];
		state[BOTTOM][UPPERRIGHT] = state[RIGHT] [UPPERLEFT];
		state[RIGHT] [LOWERLEFT]  = temp2;
		state[RIGHT] [UPPERLEFT]  = temp1;
		
		try {
			return new Cube(state);
		} catch (InvalidCubeException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Cube fPrimeRotation(Cube cube) {
		char[][] state = cloneState(cube.getCubeState());
		
		rotateFaceCCW(state, FRONT);
		
		char temp1 = state[TOP][LOWERLEFT];
		char temp2 = state[TOP][LOWERRIGHT];
		state[TOP][LOWERLEFT] = state[RIGHT][UPPERLEFT];
		state[TOP][LOWERRIGHT] = state[RIGHT][LOWERLEFT];
		state[RIGHT][UPPERLEFT] = state[BOTTOM][UPPERRIGHT];
		state[RIGHT][LOWERLEFT] = state[BOTTOM][UPPERLEFT];
		state[BOTTOM][UPPERRIGHT] = state[LEFT][LOWERRIGHT];
		state[BOTTOM][UPPERLEFT] = state[LEFT][UPPERRIGHT];
		state[LEFT][LOWERRIGHT] = temp1;
		state[LEFT][UPPERRIGHT] = temp2;
		
		try {
			return new Cube(state);
		} catch (InvalidCubeException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Cube bRotation(Cube cube) {
		char[][] state = cloneState(cube.getCubeState());
		
		rotateFaceCW(state, BACK);
			
		// rotate adjacent edges
		char temp1 = state[TOP][UPPERLEFT];
		char temp2 = state[TOP][UPPERRIGHT];
		state[TOP][UPPERLEFT] = state[RIGHT][UPPERRIGHT];
		state[TOP][UPPERRIGHT] = state[RIGHT][LOWERRIGHT];
		state[RIGHT][UPPERRIGHT] = state[BOTTOM][LOWERRIGHT];
		state[RIGHT][LOWERRIGHT] = state[BOTTOM][LOWERLEFT];
		state[BOTTOM][LOWERRIGHT] = state[LEFT][LOWERLEFT];
		state[BOTTOM][LOWERLEFT] = state[LEFT][UPPERLEFT];
		state[LEFT][LOWERLEFT] = temp1;
		state[LEFT][UPPERLEFT] = temp2;
		
		try {
			return new Cube(state);
		} catch (Cube.InvalidCubeException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Cube bPrimeRotation(Cube cube) {
		char[][] state = cloneState(cube.getCubeState());
		
		rotateFaceCCW(state, BACK);
		
		char temp1 = state[TOP][UPPERLEFT];
		char temp2 = state[TOP][UPPERRIGHT];
		state[TOP][UPPERLEFT] = state[LEFT][LOWERLEFT];
		state[TOP][UPPERRIGHT] = state[LEFT][UPPERLEFT];
		state[LEFT][LOWERLEFT] = state[BOTTOM][LOWERRIGHT];
		state[LEFT][UPPERLEFT] = state[BOTTOM][LOWERLEFT];
		state[BOTTOM][LOWERRIGHT] = state[RIGHT][UPPERRIGHT];
		state[BOTTOM][LOWERLEFT] = state[RIGHT][LOWERRIGHT];
		state[RIGHT][UPPERRIGHT] = temp1;
		state[RIGHT][LOWERRIGHT] = temp2;
		
		try {
			return new Cube(state);
		} catch (Cube.InvalidCubeException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Cube rRotation(Cube cube) {
		char[][] state = cloneState(cube.getCubeState());
		
		rotateFaceCW(state, RIGHT);
		
		// rotate adjacent edges
		char temp1 = state[TOP][UPPERRIGHT];
		char temp2 = state[TOP][LOWERRIGHT];
		state[TOP][UPPERRIGHT] = state[FRONT][UPPERRIGHT];
		state[TOP][LOWERRIGHT] = state[FRONT][LOWERRIGHT];
		state[FRONT][UPPERRIGHT] = state[BOTTOM][UPPERRIGHT];
		state[FRONT][LOWERRIGHT] = state[BOTTOM][LOWERRIGHT];
		state[BOTTOM][UPPERRIGHT] = state[BACK][LOWERLEFT];
		state[BOTTOM][LOWERRIGHT] = state[BACK][UPPERLEFT];
		state[BACK][LOWERLEFT] = temp1;
		state[BACK][UPPERLEFT] = temp2;
		
		try {
			return new Cube(state);
		} catch (Cube.InvalidCubeException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Cube rPrimeRotation(Cube cube) {
		char[][] state = cloneState(cube.getCubeState());
		
		rotateFaceCCW(state, RIGHT);
		
		char temp1 = state[TOP][UPPERRIGHT];
		char temp2 = state[TOP][LOWERRIGHT];
		state[TOP][UPPERRIGHT] = state[BACK][LOWERLEFT];
		state[TOP][LOWERRIGHT] = state[BACK][UPPERLEFT];
		state[BACK][LOWERLEFT] = state[BOTTOM][UPPERRIGHT];
		state[BACK][UPPERLEFT] = state[BOTTOM][LOWERRIGHT];
		state[BOTTOM][UPPERRIGHT] = state[FRONT][UPPERRIGHT];
		state[BOTTOM][LOWERRIGHT] = state[FRONT][LOWERRIGHT];
		state[FRONT][UPPERRIGHT] = temp1;
		state[FRONT][LOWERRIGHT] = temp2;
		
		try {
			return new Cube(state);
		} catch (Cube.InvalidCubeException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Cube lRotation(Cube cube) {
		char[][] state = cloneState(cube.getCubeState());
		
		rotateFaceCW(state, LEFT);
		
		// rotate adjacent edges
		char temp1 = state[TOP][UPPERLEFT];
		char temp2 = state[TOP][LOWERLEFT];
		state[TOP][UPPERLEFT] = state[BACK][UPPERRIGHT];
		state[TOP][LOWERLEFT] = state[BACK][LOWERRIGHT];
		state[BACK][UPPERRIGHT] = state[BOTTOM][LOWERLEFT];
		state[BACK][LOWERRIGHT] = state[BOTTOM][UPPERLEFT];
		state[BOTTOM][UPPERLEFT] = state[FRONT][UPPERLEFT];
		state[BOTTOM][LOWERLEFT] = state[FRONT][LOWERLEFT];
		state[FRONT][UPPERLEFT] = temp1;
		state[FRONT][LOWERLEFT] = temp2;
		
		try {
			return new Cube(state);
		} catch (Cube.InvalidCubeException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Cube lPrimeRotation(Cube cube) {
		char[][] state = cloneState(cube.getCubeState());
		
		rotateFaceCCW(state, LEFT);
		
		char temp1 = state[TOP][UPPERLEFT];
		char temp2 = state[TOP][LOWERLEFT];
		state[TOP][UPPERLEFT] = state[FRONT][UPPERLEFT];
		state[TOP][LOWERLEFT] = state[FRONT][LOWERLEFT];
		state[FRONT][UPPERLEFT] = state[BOTTOM][UPPERLEFT];
		state[FRONT][LOWERLEFT] = state[BOTTOM][LOWERLEFT];
		state[BOTTOM][UPPERLEFT] = state[BACK][LOWERRIGHT];
		state[BOTTOM][LOWERLEFT] = state[BACK][UPPERRIGHT];
		state[BACK][LOWERRIGHT] = temp1;
		state[BACK][UPPERRIGHT] = temp2;
		
		try {
			return new Cube(state);
		} catch (Cube.InvalidCubeException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Cube uRotation(Cube cube) {
		char[][] state = cloneState(cube.getCubeState());
		
		rotateFaceCW(state, TOP);
		
		char temp1 = state[LEFT][UPPERLEFT];
		char temp2 = state[LEFT][UPPERRIGHT];
		state[LEFT][UPPERLEFT] = state[FRONT][UPPERLEFT];
		state[LEFT][UPPERRIGHT] = state[FRONT][UPPERRIGHT];
		state[FRONT][UPPERLEFT] = state[RIGHT][UPPERLEFT];
		state[FRONT][UPPERRIGHT] = state[RIGHT][UPPERRIGHT];
		state[RIGHT][UPPERLEFT] = state[BACK][UPPERLEFT];
		state[RIGHT][UPPERRIGHT] = state[BACK][UPPERRIGHT];
		state[BACK][UPPERLEFT] = temp1;
		state[BACK][UPPERRIGHT] = temp2;
		
		try {
			return new Cube(state);
		} catch (Cube.InvalidCubeException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Cube uPrimeRotation(Cube cube) {
		char[][] state = cloneState(cube.getCubeState());
		
		rotateFaceCCW(state, TOP);
		
		char temp1 = state[LEFT][UPPERLEFT];
		char temp2 = state[LEFT][UPPERRIGHT];
		state[LEFT][UPPERLEFT] = state[BACK][UPPERLEFT];
		state[LEFT][UPPERRIGHT] = state[BACK][UPPERRIGHT];
		state[BACK][UPPERLEFT] = state[RIGHT][UPPERLEFT];
		state[BACK][UPPERRIGHT] = state[RIGHT][UPPERRIGHT];
		state[RIGHT][UPPERLEFT] = state[FRONT][UPPERLEFT];
		state[RIGHT][UPPERRIGHT] = state[FRONT][UPPERRIGHT];
		state[FRONT][UPPERLEFT] = temp1;
		state[FRONT][UPPERRIGHT] = temp2;
		
		try {
			return new Cube(state);
		} catch (Cube.InvalidCubeException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Cube dRotation(Cube cube) {
		char[][] state = cloneState(cube.getCubeState());
		
		rotateFaceCW(state, BOTTOM);
		
		char temp1 = state[BACK][LOWERLEFT];
		char temp2 = state[BACK][LOWERRIGHT];
		state[BACK][LOWERLEFT] = state[RIGHT][LOWERLEFT];
		state[BACK][LOWERRIGHT] = state[RIGHT][LOWERRIGHT];
		state[RIGHT][LOWERLEFT] = state[FRONT][LOWERLEFT];
		state[RIGHT][LOWERRIGHT] = state[FRONT][LOWERRIGHT];
		state[FRONT][LOWERLEFT] = state[LEFT][LOWERLEFT];
		state[FRONT][LOWERRIGHT] = state[LEFT][LOWERRIGHT];
		state[LEFT][LOWERLEFT] = temp1;
		state[LEFT][LOWERRIGHT] = temp2;
		
		try {
			return new Cube(state);
		} catch (Cube.InvalidCubeException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Cube dPrimeRotation(Cube cube) {
		char[][] state = cloneState(cube.getCubeState());
		
		rotateFaceCCW(state, BOTTOM);
		
		char temp1 = state[BACK][LOWERLEFT];
		char temp2 = state[BACK][LOWERRIGHT];
		state[BACK][LOWERLEFT] = state[LEFT][LOWERLEFT];
		state[BACK][LOWERRIGHT] = state[LEFT][LOWERRIGHT];
		state[LEFT][LOWERLEFT] = state[FRONT][LOWERLEFT];
		state[LEFT][LOWERRIGHT] = state[FRONT][LOWERRIGHT];
		state[FRONT][LOWERLEFT] = state[RIGHT][LOWERLEFT];
		state[FRONT][LOWERRIGHT] = state[RIGHT][LOWERRIGHT];
		state[RIGHT][LOWERLEFT] = temp1;
		state[RIGHT][LOWERRIGHT] = temp2;
		
		try {
			return new Cube(state);
		} catch (Cube.InvalidCubeException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String solve() {
		// make string cube pair
		// run all different actions on cube and then add the corresponding action to the string of the pair
		// check if the cube is solved
		// continue until a cube is found
		
		int minNumOfMoves = 0;
		StringCubePair input = new StringCubePair("", this);
		LinkedList<StringCubePair> queue = new LinkedList<>();
		queue.add(input);
		
		while (true) {
			input = queue.poll(); // pop cube
			
			if (input.permut.replaceAll("[A-Z]", "").length() > minNumOfMoves) {
				minNumOfMoves++;
				System.out.println("Min num of moves is now " + minNumOfMoves);
			}
			
			if (input.cube.solved()) break;
			
			queue.add(new StringCubePair(
					input.permut + "F", 
					Cube.fRotation(new Cube(input.cube))
				)
			);
			
			queue.add(new StringCubePair(
					input.permut + "F'", 
					Cube.fPrimeRotation(new Cube(input.cube))
				)
			);
			
			queue.add(new StringCubePair(
					input.permut + "B", 
					Cube.bRotation(new Cube(input.cube))
				)
			);
			
			queue.add(new StringCubePair(
					input.permut + "B'", 
					Cube.bPrimeRotation(new Cube(input.cube))
				)
			);
			
			queue.add(new StringCubePair(
					input.permut + "R", 
					Cube.rRotation(new Cube(input.cube))
				)
			);
			
			queue.add(new StringCubePair(
					input.permut + "R'", 
					Cube.rPrimeRotation(new Cube(input.cube))
				)
			);
			
			queue.add(new StringCubePair(
					input.permut + "L", 
					Cube.lRotation(new Cube(input.cube))
				)
			);
			
			queue.add(new StringCubePair(
					input.permut + "L'", 
					Cube.lPrimeRotation(new Cube(input.cube))
				)
			);
			
			queue.add(new StringCubePair(
					input.permut + "U", 
					Cube.uRotation(new Cube(input.cube))
				)
			);
			
			queue.add(new StringCubePair(
					input.permut + "U'", 
					Cube.uPrimeRotation(new Cube(input.cube))
				)
			);
			
			queue.add(new StringCubePair(
					input.permut + "D", 
					Cube.dRotation(new Cube(input.cube))
				)
			);
			
			queue.add(new StringCubePair(
					input.permut + "D'", 
					Cube.dPrimeRotation(new Cube(input.cube))
				)
			);
		}
		
		if (input.permut.equals("")) return "Already Solved!";

		return input.permut;
	}
}
