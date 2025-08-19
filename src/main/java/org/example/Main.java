package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        try {
            int[][] matrixA = readMatrix("src/main/resources/A.txt");
            int[][] matrixB = readMatrix("src/main/resources/B.txt");

            int rowsA = matrixA.length;
            int colsA = (rowsA > 0) ? matrixA[0].length : 0;
            int rowsB = matrixB.length;
            int colsB = (rowsB > 0) ? matrixB[0].length : 0;


            if (colsA != rowsB) {
                System.err.println("Columns of Matrix A must be equal to Rows of Matrix B .");
                return;
            }

            int[][] result = new int[rowsA][colsB];
            ExecutorService executor = Executors.newFixedThreadPool(rowsA);

            for (int i = 0; i < rowsA; i++) {
                final int row = i;
                executor.submit(() -> {
                    for (int j = 0; j < colsB; j++) {
                        int sum = 0;
                        for (int k = 0; k < colsA; k++) {
                            sum += matrixA[row][k] * matrixB[k][j];
                        }
                        result[row][j] = sum;
                    }
                });
            }

            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.SECONDS);

            System.out.println("Result:");
            printMatrix(result);

        } catch (FileNotFoundException e) {
            System.err.println("Error: Input file not found. " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Error: Threads were interrupted. " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public static int[][] readMatrix(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        Scanner sc = new Scanner(file);
        List<List<Integer>> matrixList = new ArrayList<>();

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            List<Integer> rowList = new ArrayList<>();
            Scanner lineScanner = new Scanner(line);
            while (lineScanner.hasNextInt()) {
                rowList.add(lineScanner.nextInt());
            }
            matrixList.add(rowList);
            lineScanner.close();
        }
        sc.close();

        if (matrixList.isEmpty()) {
            return new int[0][0];
        }

        int rows = matrixList.size();
        int cols = matrixList.get(0).size();
        int[][] matrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = matrixList.get(i).get(j);
            }
        }
        return matrix;
    }

    public static void printMatrix(int[][] matrix) {
        if (matrix == null || matrix.length == 0) {
            System.out.println("[]");
            return;
        }
        for (int[] row : matrix) {
            for (int val : row) {
                System.out.print(val + "\t");
            }
            System.out.println();
        }
    }
}