package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {

        int rowsA = 2;
        int colsA = 5;
        int rowsB = 5;
        int colsB = 3;

        int[][] matrixA = readMatrix("src/main/resources/A.txt", rowsA, colsA);
        int[][] matrixB = readMatrix("src/main/resources/B.txt", rowsB, colsB);
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


        System.out.println("Result: ");
        printMatrix(result);
    }

    public static int[][] readMatrix(String filePath, int rows, int cols) throws FileNotFoundException {
        int[][] matrix = new int[rows][cols];
        Scanner sc = new Scanner(new File(filePath));
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (sc.hasNextInt()) {
                    matrix[i][j] = sc.nextInt();
                }
            }
        }
        sc.close();
        return matrix;
    }


    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int val : row) {
                System.out.print(val + " ");
            }
            System.out.println();
        }
    }
}

