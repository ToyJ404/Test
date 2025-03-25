package TicTacToe;

import java.util.InputMismatchException;
import java.util.Scanner;

public class TicTacToe {
    private static final char PLAYER_X = 'X', PLAYER_O = 'O', EMPTY = ' ';
    private static char currentPlayer = PLAYER_X;
    private static final int SIZE = 3;
    private static final char[][] BOARD = new char[SIZE][SIZE];
    private static int gameMode = 2;

    public static void main(String[] args) {
        System.out.println("\nАвтор: Nyanpasu\n________________________________");

        boolean again = true;
        SelectionGameMode();
        initialization();
        while (again) {
            Scanner scanAgain = new Scanner(System.in);
            gameProcess();
            String answer;
            while (true) {
                System.out.print("Хотите сыграть еще раз? (y/n): ");
                answer = scanAgain.nextLine().trim();
                if (answer.equalsIgnoreCase("y")) {
                    again = true;
                    break;
                } else if (answer.equalsIgnoreCase("n")) {
                    again = false;
                    break;
                } else System.out.println("Ввод не корректный можно ввести только (\"y\" и \"n\").");
            }
        }

        System.out.println("До встречи!");
    }

    private static void initialization() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                BOARD[i][j] = EMPTY;
            }
        }
    }

    private static void gameProcess() {
        if (gameMode == 1) {
            GameWithComputer();
        } else {
            GameWithFriend();
        }
    }

    private static void printBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(BOARD[i][j]);
                if (j < SIZE - 1) {
                    System.out.print(" | ");
                }
            }
            System.out.println();
            if (i < SIZE - 1) {
                System.out.println("--+---+--");
            }
        }
    }

    private static void playerMove() {

        try {
            Scanner scan = new Scanner(System.in);
            int row, col;
            while (true) {
                System.out.println();
                System.out.print("Игрок " + currentPlayer + " введите строку (1 - 3) и столбец (1 - 3): ");

                row = scan.nextInt() - 1;
                col = scan.nextInt() - 1;
                if (row >= 0 && row < SIZE && col >= 0 && col < SIZE && BOARD[row][col] == EMPTY) {
                    BOARD[row][col] = currentPlayer;
                    break;
                } else {
                    System.out.println();
                    System.out.println("Некоретный ввод попробуйте снова");
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Можно ввести только цифры от 1-3 строка и столбик");
        }

    }

    private static boolean checkingWinner() {
        for (int i = 0; i < SIZE; i++) { // Сравнение по вертикали и горизонтали
            boolean rowWin = true, colWin = true;
            for (int j = 0; j < SIZE; j++) {
                if (currentPlayer != BOARD[i][j]) rowWin = false; // Проверка строк
                if (currentPlayer != BOARD[j][i]) colWin = false; // Проверка столбцов
            }
            if (rowWin || colWin) return true;
        }
        boolean diagonalLeftRightWin = true, diagonalRightLeftWin = true; // C верхнего левого угла до нижнего правого // C верхнего правого угла до нижнего левого
        for (int i = 0; i < SIZE; i++) {
            if (currentPlayer != BOARD[i][SIZE - 1 - i]) diagonalRightLeftWin = false;
            if (currentPlayer != BOARD[i][i]) diagonalLeftRightWin = false;
        }
        return diagonalLeftRightWin || diagonalRightLeftWin;
    }

    private static boolean drawGame() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (BOARD[i][j] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void switchPlayer() {
        currentPlayer = currentPlayer == PLAYER_X ? PLAYER_O : PLAYER_X;
    }

    private static void SelectionGameMode() {
        Scanner scanMode = new Scanner(System.in);
        while (true) {
            System.out.println("Выбери режим игры перед началом:\n1 - играть с компьютером.\n2 - играть с другом.");
            System.out.print("Выбор режима: ");
            if (scanMode.hasNextInt()) {
                gameMode = scanMode.nextInt();
                scanMode.nextLine();
                if (gameMode == 1 || gameMode == 2) {
                    break;
                } else {
                    System.out.println("Введите 1 или 2 для выбора режима");
                    scanMode.nextLine();
                }
            }
        }
        if (gameMode == 1) {
            System.out.println("________________________________\nВы выбрали режим игры с компьютером\nПриятной игры!!!");
        } else {
            System.out.println("________________________________\nВы выбрали режим игры с другом\nПриятной игры!!!");
        }
    }

    private static void GameWithFriend() {
        System.out.println("Запуск игры с другом...");
        boolean gameOver = false;
        while (!gameOver) {
            printBoard();
            playerMove();
            if (checkingWinner()) {
                printBoard();
                System.out.println("Игрок " + currentPlayer + " одержал победу!");
                gameOver = true;
            } else if (drawGame()) {
                printBoard();
                System.out.println("Ничья!");
                gameOver = true;
            } else switchPlayer();
        }
    }

    private static void GameWithComputer() {
        System.out.println("Запуск игры с компьютером...");
        initialization();
        boolean gameOver = false;
        while (!gameOver) {
            printBoard();
            System.out.println();
            if (currentPlayer == PLAYER_X) {
                playerMove();
            } else computerMove();
            if (checkingWinner()) {
                printBoard();
                System.out.println("Игрок " + currentPlayer + " одержал победу!");
                gameOver = true;
            } else if (drawGame()) {
                printBoard();
                System.out.println("Ничья!");
                gameOver = true;
            } else switchPlayer();
        }
    }

    private static void computerMove() {
        int bestMove = Integer.MIN_VALUE;
        int bestRow = -1, bestCol = -1;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (BOARD[i][j] == EMPTY) {
                    BOARD[i][j] = PLAYER_O;
                    int findBetterMove = minimax(false, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    BOARD[i][j] = EMPTY;
                    if (findBetterMove > bestMove) {
                        bestMove = findBetterMove;
                        bestRow = i;
                        bestCol = j;
                    }
                }
            }
        }
        if (bestCol != -1 && bestRow != -1) {
            BOARD[bestRow][bestCol] = PLAYER_O;
        }
    }

    private static int minimax(boolean isMinimax, int alpha, int beta) {
        int score = evaluateBoard();
        if (score == -1 || score == 1 || drawGame()) {
            return score;
        }
        if (isMinimax) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (BOARD[i][j] == EMPTY) {
                        BOARD[i][j] = PLAYER_O;
                        bestScore = Math.max(bestScore, minimax(false, alpha, beta));
                        BOARD[i][j] = EMPTY;
                        alpha = Math.max(alpha, bestScore);
                        if (beta <= alpha) break;
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (BOARD[i][j] == EMPTY) {
                        BOARD[i][j] = PLAYER_X;
                        bestScore = Math.min(bestScore, minimax(true, alpha, beta));
                        BOARD[i][j] = EMPTY;
                        beta = Math.min(beta, bestScore);
                        if (beta <= alpha) break;
                    }
                }
            }
            return bestScore;
        }
    }

    private static int evaluateBoard() {
        for (int i = 0; i < SIZE; i++) {
            if (checkLine(BOARD[0][i], BOARD[1][i], BOARD[2][i])) return getScore(BOARD[0][i]);
            if (checkLine(BOARD[i][0], BOARD[i][1], BOARD[i][2])) return getScore(BOARD[i][0]);
        }
        if (checkLine(BOARD[0][0], BOARD[1][1], BOARD[2][2])) return BOARD[0][0]; // слева в право
        if (checkLine(BOARD[0][2], BOARD[1][1], BOARD[2][0])) return BOARD[2][0]; // с право в лево
        return 0;
    }

    private static boolean checkLine(char a, char b, char c) {
        return a == b && b == c && a != EMPTY;
    }

    private static int getScore(char player) {
        if (PLAYER_X == player) return -1;
        if (PLAYER_O == player) return 1;
        return 0;
    }
}

