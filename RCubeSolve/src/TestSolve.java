
public class TestSolve {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		char[] top = {'O', 'G', 'R', 'W'};
		char[] bottom = {'O', 'R', 'B', 'O'};
		char[] left = {'Y', 'R', 'G', 'G'};
		char[] front = {'B', 'Y', 'Y', 'Y'};
		char[] right = {'B', 'R', 'W', 'G'};
		char[] back = {'W', 'B', 'W', 'O'};
		
//		char[] top = {'W', 'O', 'O', 'W'}; //
//		char[] bottom = {'Y', 'Y', 'R', 'R'}; //
//		char[] left = {'G', 'G', 'O', 'Y'}; // 
//		char[] front = {'R', 'W', 'G', 'G'};  //
//		char[] right = {'B', 'B', 'W', 'R'}; 
//		char[] back = {'Y', 'O', 'B', 'B'}; 
		
		Cube cube1 = null;
		
		try {
			cube1 = new Cube(top, bottom, left, front, right, back);
		} catch (Cube.InvalidCubeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(cube1.solve());
		
		Cube cube2 = new Cube();
		
		cube2 = Cube.dRotation(cube2);
		cube2 = Cube.dPrimeRotation(cube2);
		
		System.out.println(cube2.solve());
	}

}
