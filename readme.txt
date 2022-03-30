#readme for DFSMazeGeneratorAndSolver


**ABOUT**
The aim of this project is to first create a random n x m sized maze using the common depth first search maze generation method. The generator will create a .dat file which will contain (size of the maze e.g 5,5, start point, End point, and finally a cell list e.g 1311030312322302201201110 which the solver will read to determine 
which walls are open in each cell.

  **Cell wall explanation
  0: Both closed
  1: Right only open
  2: Down only open
  3: both open
  
  the MazeSolverDFS will then read this .dat file and solve the maze once again using a depth first search algorithm.
  the results will be an ordered list of the cells of the maze the DFS algorithm visited while it attempted to solve the maze
  it will then list the total number of cells it took to reach the finish and the total time it took to complete
  

**INSTRUCTIONS**

1. Open the DFSMazeGeneratorAndSolver folder and paste any required datafiles needed for testing

2. Open cmd in the folder DFSMazeGeneratorAndSolver

3. Type javac MazeGenerator.java

4. Type javac MazeSolver.java

5. Type java MazeGenerator n m *maze file name* (n and m are the dimensions of the maze)

6. Type java MazeSolverDFS *maze file name*



example

javac MazeGenerator.java
javac MazeSolverDFS.java

java MazeGenerator 5 5 maze.dat
java MazeSolverDFS maze.dat

