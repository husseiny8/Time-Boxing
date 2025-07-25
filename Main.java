import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static Scanner scanner = new Scanner(System.in);
    public static Random random = new Random();
    final public static int ston = 50;
    final public static int radif = 20;
    public static int[][] greed = new int[radif][ston];
    public static int rman = radif / 2 - 1;
    public static int sman = ston / 2 - 1;
    private static int score = 0;

    public static void main(String[] args) throws IOException {

        for (int i = 0; i < radif; i++) {
            for (int j = 0; j < ston; j++) {
                greed[i][j] = random.nextInt(9) + 1;
            }
        }

        while (true) {

            if (valid() == false) {
                System.out.println("GAME OVER!");
                System.out.println("Score: " + score);
                break;
            }

            greed[rman][sman] = -1;

            for (int i = 0; i < radif; i++) {
                for (int j = 0; j < ston; j++) {
                    if (greed[i][j] == -1) {
                        System.out.print("\u001B[32m@\u001B[0m ");
                    } else if (greed[i][j] == 0) {
                        System.out.print("  ");
                    } else {
                        System.out.print(greed[i][j] + " ");
                    }
                }
                System.out.println();
            }
            System.out.println("Directions: [W] up, [S] down, [A] left, [D] right, [Q] Show scores");
            System.out.println("Score:" + score);
            char D = scanner.next().charAt(0);

            switch (D) {
                case 'w':
                case 'W': {
                    moveup();
                    break;
                }
                case 's':
                case 'S': {
                    movedown();
                    break;
                }
                case 'a':
                case 'A': {
                    moveleft();
                    break;
                }
                case 'D':
                case 'd': {
                    moveright();
                    break;
                }
                case 'q':
                case 'Q':{
                    break;
                }
                default: {
                    System.out.println("Invalid direction. Try again.");
                }
            }
        }
    }

    public static boolean valid() {
        boolean moveu;
        boolean moved;
        boolean mover;
        boolean movel;

        if (rman != 0) {
            int eatu = greed[rman - 1][sman];
            if (rman - eatu < 0 || eatu == 0 || greed[rman - eatu][sman] == 0)
                moveu = false;
            else
                moveu = true;
        } else
            moveu = false;

        if (rman < 19) {
            int eatd = greed[rman + 1][sman];
            if (rman + eatd >= radif || eatd == 0 || greed[rman + eatd][sman] == 0)
                moved = false;
            else
                moved = true;
        } else
            moved = false;

        if (sman != 0) {
            int eatl = greed[rman][sman - 1];
            if (sman - eatl < 0 || eatl == 0 || greed[rman][sman - eatl] == 0)
                movel = false;
            else
                movel = true;
        } else
            movel = false;

        if (sman < 49) {
            int eatr = greed[rman][sman + 1];
            if (sman + eatr >= ston || eatr == 0 || greed[rman][sman + eatr] == 0)
                mover = false;
            else
                mover = true;
        } else
            mover = false;

        boolean b = moved || moveu || movel || mover;
        return b;
    }

    public static void moveup() {
        if (rman != 0) {
            int eatu = greed[rman - 1][sman];
            if (rman - eatu >= 0 && eatu != 0 && greed[rman - eatu][sman] != 0) {
                for (int i = 0; i < eatu; i++) {
                    if (greed[rman - i][sman] != 0) {
                        score++;
                    }
                    greed[rman - i][sman] = 0;
                }
                rman = rman - eatu;
            } else {
                System.out.println("Wrong Move!");
            }
        } else
            System.out.println("Wrong Move!");
    }

    public static void movedown() {
        if (rman != 19) {
            int eat = greed[rman + 1][sman];
            if (rman + eat < radif && eat != 0 && greed[rman + eat][sman] != 0) {
                for (int i = 0; i < eat; i++) {
                    if (greed[rman + i][sman] != 0) {
                        score++;
                    }
                    greed[rman + i][sman] = 0;
                }
                rman = rman + eat;
            } else {
                System.out.println("Wrong Move!");
            }
        } else
            System.out.println("Wrong Move!");
    }

    public static void moveright() {
        if (sman != 49) {
            int eatr = greed[rman][sman + 1];
            if (sman + eatr < ston && eatr != 0 && greed[rman][sman + eatr] != 0) {
                for (int i = 0; i < eatr; i++) {
                    if (greed[rman][sman + i] != 0) {
                        score++;
                    }
                    greed[rman][sman + i] = 0;
                }
                sman = sman + eatr;
            } else {
                System.out.println("Wrong Move!");
            }
        } else
            System.out.println("Wrong Move!");
    }

    public static void moveleft() {
        if (sman != 0) {
            int eatl = greed[rman][sman - 1];
            if (sman - eatl >= 0 && eatl != 0 && greed[rman][sman - eatl] != 0) {
                for (int i = 0; i < eatl; i++) {
                    if (greed[rman][sman - i] != 0) {
                        score++;
                    }
                    greed[rman][sman - i] = 0;
                }
                sman = sman - eatl;
            } else {
                System.out.println("Wrong Move!");
            }
        } else
            System.out.println("Wrong Move!");
    }
}
