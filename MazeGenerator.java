//COMP2230 ASSIGNMENT 1, name: Jacob Metcalfe, student number: c3305509
//Maze generation Class used to randomly generate an n x m maze using a depth-first-search
//with recursive backtracking. 

import java.util.*;
import java.io.FileOutputStream;

public class MazeGenerator {

    private int n;
    private int m;

    private boolean[][] visited;
    private boolean[][] left;
    private boolean[][] right;
    private boolean[][] up;
    private boolean[][] down;

    private int start;
    private int endY;
    private int endX;

    // function used to initialize the maze
    public void init() {

        // Create the arrays bigger than the actual maze to store the outer cell walls
        visited = new boolean[n + 2][m + 2];
        left = new boolean[n + 2][m + 2];
        right = new boolean[n + 2][m + 2];
        up = new boolean[n + 2][m + 2];
        down = new boolean[n + 2][m + 2];

        for (int i = 0; i < n + 2; i++) {
            for (int j = 0; j < m + 2; j++) {
                // Set all cell openness values to false (no cells opened)
                left[i][j] = false;
                right[i][j] = false;
                up[i][j] = false;
                down[i][j] = false;
                // Set all cells to not visited
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
            // Set the left most outer wall
            visited[0][y] = true;

            // Set the right most outer wall
            visited[n + 1][y] = true;
        }

    }

    // recursive function used to generate the random maze using DFS
    public void generate(int x, int y) {

        // Mark the current cell as visited
        visited[x][y] = true;

        // Keep updating the end x and y values until the maze is fully generated
        endX = x;
        endY = y;

        // While an unvisited neighbor exists
        while (!visited[x - 1][y] || !visited[x + 1][y] || !visited[x][y + 1] || !visited[x][y - 1]) {
            while (true) {
                // Get a random direction
                Random rand = new Random();
                // Get value from 0 - 3
                int r = rand.nextInt(4);
                // 0 = Up, 1 = Right, 2 = Down, 3 = Left

                // (0) Move Up
                if (r == 0 && !visited[x][y - 1]) {
                    // Sets the current cell's top wall to open
                    up[x][y] = true;
                    // Sets the cell's bottom wall of the cell to the top to open
                    down[x][y - 1] = true;
                    // Call the function recursively with the new updated x and y values
                    generate(x, y - 1);
                    break;
                }

                // (1) Move Right
                else if (r == 1 && !visited[x + 1][y]) {
                    // Sets the current cell's right wall to open
                    right[x][y] = true;
                    // Sets the cell's left wall of the cell to the right to open
                    left[x + 1][y] = true;

                    // Call the function recursively with the new updated x and y values
                    generate(x + 1, y);

                    break;
                }

                // (2) Move Down
                else if (r == 2 && !visited[x][y + 1]) {
                    // Sets the current cell's bottom wall to open
                    down[x][y] = true;
                    // Sets the cell's top wall of the cell to the bottom to open
                    up[x][y + 1] = true;

                    // Call the function recursively with the new updated x and y values
                    generate(x, y + 1);

                    break;
                }

                // (3) Move Left
                else if (r == 3 && !visited[x - 1][y]) {
                    // Sets the current cell's left wall to open
                    left[x][y] = true;
                    // Sets the cell's right wall of the cell to the left to open
                    right[x - 1][y] = true;

                    // Call the function recursively with the new updated x and y values
                    generate(x - 1, y);

                    break;
                }
            }
        }

    }

    public void beginGenerate() {
        // Get size of the maze
        int size = n * m;

        // Randomizes a start point
        Random rand = new Random();
        // Ensure that the start point starts at 1
        start = rand.nextInt(size) + 1;

        int x = 0;
        int y = 0;

        // Calculates where the x and y of the start point will be
        int remaining = start % n;
        // If no remainder then,
        if (remaining == 0) {
            x = n;
            y = start / n;
        }
        // If there is a remainder and the starting cell is greater than the maximum x
        // value (which is n) then,
        else if (start > n && remaining != 0) {
            x = remaining;
            y = (start + (n - remaining)) / n;
        }
        // If the maximum x value (n) is greater then the starting cell then,
        else if (start <= n) {
            x = start;
            y = 1;
        }

        // Begin generating the maze
        generate(x, y);
    }

    public String output() {

        int[][] cell_list = new int[n + 2][m + 2];

        // Loop through cells from left to right
        // To identify the cell openness value
        for (int j = 1; j < m + 1; j++) {

            for (int i = 1; i < n + 1; i++) {

                if (!right[i][j] && !down[i][j]) { // both closed
                    cell_list[i][j] = 0;

                } else if (right[i][j] && !down[i][j]) { // only right open
                    cell_list[i][j] = 1;

                } else if (!right[i][j] && down[i][j]) { // down only open
                    cell_list[i][j] = 2;

                } else if (right[i][j] && down[i][j]) { // both open
                    cell_list[i][j] = 3;
                }

            }

        }

        // Calculate the cell location of the end point using the end point's x and y
        // value
        int end = (endY * n) + endX - n;

        // Combine everything into one string
        String textline = n + "," + m + ":" + start + ":" + end + ":";

        // Put cell wall values in a string from left to right
        // Starts from 1 and end at the last bottom-right most cell that is not an outer
        // wall as it will exclude the outer walls
        for (int j = 1; j < m + 1; j++) {
            for (int i = 1; i < n + 1; i++) {
                textline += cell_list[i][j];
            }
        }

        return textline;

    }

    // Create output file with the output String line
    public void outputFile(String fileName, String textline) {
        try {
            FileOutputStream out = new FileOutputStream(fileName);

            out.write(textline.getBytes());
            out.close();
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) {

        MazeGenerator mg = new MazeGenerator();

        // Get maze n and m from console input
        mg.n = Integer.parseInt(args[0]);
        mg.m = Integer.parseInt(args[1]);
        // Get file name from console input
        String fileName = args[2];

        // Initialize the variables
        mg.init();
        // Begin generating the maze
        mg.beginGenerate();
        // Create output file
        mg.outputFile(fileName, mg.output());
    }
}
