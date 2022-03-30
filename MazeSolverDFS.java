//COMP2230 ASSIGNMENT 1, name: Jacob Metcalfe, student number: c3305509
//Maze solver class used to solved the randomly generated  n x m maze using a depth-first-search
//with stack approach. 

import java.util.*;
import java.io.*;

public class MazeSolverDFS {

    private int n;
    private int m;
    private int size;
    private int start;
    private int end;
    private int endX;
    private int endY;
    private int[] maze;
    private int x;
    private int y;

    private int steps;
    private int[] path;
    private List<Integer> pathX;
    private List<Integer> pathY;

    private boolean[][] left;
    private boolean[][] right;
    private boolean[][] up;
    private boolean[][] down;
    private boolean[][] visited;

    private long start_time;
    private long end_time;

    // Read from the file
    public void readFile(String fileName) {

        // Read file depending on the file name inputted
        String line = "";
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(fileName));
            line = br.readLine();
        } catch (IOException e) {
        }

        // Get individual variables from the file
        String[] split = line.split(":");
        String[] sizeString = split[0].split(",");
        n = Integer.valueOf(sizeString[0]);
        m = Integer.valueOf(sizeString[1]);
        start = Integer.valueOf(split[1]);
        end = Integer.valueOf(split[2]);

        // Get size of maze
        size = n * m;

        maze = new int[size];

        // Get each number and put it into an array
        char[] ch = split[3].toCharArray();
        for (int i = 0; i < size; i++) {
            maze[i] = Character.getNumericValue(ch[i]);
        }

    }

    public void readMaze() {
        // Set start x and y
        int[] coords = new int[2];
        coords = getLocation(start);
        x = coords[0];
        y = coords[1];

        // Set end x and y
        coords = getLocation(end);
        endX = coords[0];
        endY = coords[1];

        // Initialize
        steps = 0;

        pathX = new ArrayList<Integer>();
        pathY = new ArrayList<Integer>();

        left = new boolean[n + 2][m + 2];
        right = new boolean[n + 2][m + 2];
        up = new boolean[n + 2][m + 2];
        down = new boolean[n + 2][m + 2];
        visited = new boolean[n + 2][m + 2];

        for (int i = 0; i < n + 2; i++) {
            for (int j = 0; j < m + 2; j++) {
                // Set all cell openness values to false (no cells opened)
                left[i][j] = false;
                right[i][j] = false;
                up[i][j] = false;
                down[i][j] = false;
                // Set all cells to unvisited
                visited[i][j] = false;
            }
        }

        // Left to Right
        for (int x = 0; x < n + 2; x++) {
            // Sets the top most outer wall
            visited[x][0] = true;

            // Sets the bottom most outer wall
            visited[x][m + 1] = true;
        }

        // Top to Bottom
        for (int y = 0; y < m + 2; y++) {
            // Sets the left most outer wall
            visited[0][y] = true;

            // Sets the right most outer wall
            visited[n + 1][y] = true;
        }

        int count = 0;
        // Sets the walls depending on the file input values
        for (int y = 1; y < m + 1; y++) {
            for (int x = 1; x < n + 1; x++) {
                if (maze[count] == 0) {
                    right[x][y] = false;
                    down[x][y] = false;
                } else if (maze[count] == 1) {
                    right[x][y] = true;
                    left[x + 1][y] = true;
                } else if (maze[count] == 2) {
                    down[x][y] = true;
                    up[x][y + 1] = true;
                } else if (maze[count] == 3) {
                    right[x][y] = true;
                    left[x + 1][y] = true;
                    down[x][y] = true;
                    up[x][y + 1] = true;
                }
                count++;
            }
        }

    }

    public void DFSsolver(int x, int y) {

        Stack<Integer> stackX = new Stack<Integer>();
        Stack<Integer> stackY = new Stack<Integer>();

        // While the solver location is not at the end point location
        while (!visited[endX][endY]) {

            // Keeps track of paths taken and number of steps taken by the DFS
            pathX.add(x);
            pathY.add(y);
            steps++;

            // Mark the current cell as visited
            visited[x][y] = true;

            while (true) {

                // Move Up
                if (up[x][y] && !visited[x][y - 1]) {
                    // Add to stack
                    stackX.push(x);
                    stackY.push(y);
                    // Move DFS Up
                    y--;
                    break;
                }
                // Move Right
                if (right[x][y] && !visited[x + 1][y]) {
                    // Add to stack
                    stackX.push(x);
                    stackY.push(y);
                    // Move DFS Right
                    x++;
                    break;
                }
                // Move Down
                if (down[x][y] && !visited[x][y + 1]) {
                    // Add to stack
                    stackX.push(x);
                    stackY.push(y);
                    // Move DFS Down
                    y++;
                    break;
                }
                // Move Left
                if (left[x][y] && !visited[x - 1][y]) {
                    // Add to stack
                    stackX.push(x);
                    stackY.push(y);
                    // Move DFS Left
                    x--;
                    break;
                }

                // If DFS cannot go in any direction, pop the stack
                // Lets the DFS solver backtrack to a previously visited cell in a different
                // direction
                x = (int) stackX.pop();
                y = (int) stackY.pop();
                break;

            }

        }

        // Finish time of DFS solver
        end_time = System.currentTimeMillis();
    }

    // Simple function to convert inputted start and end location to coordinates
    // (x,y)
    public int[] getLocation(int cell) {
        int[] coords = new int[2];
        // Get start point of maze
        int remaining = 0;
        remaining = cell % n;
        int a = 0;
        int b = 0;
        if (remaining == 0) {
            a = n;
            b = cell / n;
        } else if (cell > n && remaining != 0) {
            a = remaining;
            b = (cell + (n - remaining)) / n;
        } else if (cell <= n) {
            a = cell;
            b = 1;
        }

        coords[0] = a;
        coords[1] = b;

        return coords;
    }

    // Print out output of the DFS solver
    public void output() {
        path = new int[steps];

        // Print out the paths taken by the solver
        System.out.print("(");
        for (int i = 0; i < steps; i++) {
            path[i] = (pathY.get(i) * n) + pathX.get(i) - n;
            // Print out the cells seperated by commas
            if (i < steps - 1)
                System.out.print(path[i] + ",");
            // Do not print out comma on the last cell
            else
                System.out.print(path[i]);
        }
        System.out.println(")");

        // Print out the number of steps taken
        System.out.println(steps);

        // Get time taken
        long time_taken = end_time - start_time;
        // Print out the amount of time taken to solve the maze in miliseconds
        System.out.println(time_taken);
    }

    public static void main(String[] args) {
        // Get file name from console input
        String fileName = args[0];

        MazeSolverDFS ms = new MazeSolverDFS();
        // Read file
        ms.readFile(fileName);
        // Create maze from file
        ms.readMaze();

        // Start time of DFS solver
        ms.start_time = System.currentTimeMillis();
        // Start solving maze
        ms.DFSsolver(ms.x, ms.y);

        // Output the maze path
        ms.output();

    }
}
