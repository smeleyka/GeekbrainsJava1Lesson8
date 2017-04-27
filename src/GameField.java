
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by admin on 20.04.2017.
 */
public class GameField extends JPanel {
    private final char EMPTY = '*';
    private final char PLAYER_DOT = 'X';
    private final char AI_DOT = 'O';
    private final int SIZE = 5;
    private int cellWidth;
    private int cellHeight;
    private char[][] map;
    private final int DOT_TO_WIN = 3;
    private int turnCount;

    public GameField() {
        setBackground(Color.white);
        map = new char[SIZE][SIZE];
        initMap();
        addMouseListener(new MouseAdapter() {
            @Override

            public void mouseReleased(MouseEvent e) {

                while (humanTurn(e.getX(), e.getY())) {
                    if (winCheck(PLAYER_DOT)) {
                        JOptionPane.showOptionDialog(null, "Победил Игрок", "Крестики нолики", JOptionPane.DEFAULT_OPTION,
                                JOptionPane.INFORMATION_MESSAGE, null, null, null);
                        initMap();
                        return;
                    }
                    else if (isMapFull()){
                        JOptionPane.showOptionDialog(null, "Ничья", "Крестики нолики", JOptionPane.DEFAULT_OPTION,
                                JOptionPane.INFORMATION_MESSAGE, null, null, null);
                        initMap();
                        return;
                    }
                    aiTurn();
                    if (winCheck(AI_DOT)) {
                        JOptionPane.showOptionDialog(null, "Победил компьютер", "Крестики нолики", JOptionPane.DEFAULT_OPTION,
                                JOptionPane.INFORMATION_MESSAGE, null, null, null);
                        initMap();
                        return;
                    }
                    else if (isMapFull()){
                        JOptionPane.showOptionDialog(null, "Ничья", "Крестики нолики", JOptionPane.DEFAULT_OPTION,
                                JOptionPane.INFORMATION_MESSAGE, null, null, null);
                        initMap();
                        return;
                    }
                }
            }
        });
    }

    public void initMap() {
        turnCount = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                map[i][j] = EMPTY;
            }
        }
        repaint();
    }

    public boolean isValidDot(int x, int y) {
        return (map[x][y] == EMPTY);
    }

    public boolean humanTurn(int x, int y) {
        int cellX = x / cellWidth;
        int cellY = y / cellHeight;
        if (isValidDot(cellX, cellY)) {
            map[cellX][cellY] = PLAYER_DOT;
            repaint();
            turnCount++;
            return true;
        }
        return false;
    }

    public boolean blockPlayer() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (isValidDot(i, j)) {
                    map[i][j] = PLAYER_DOT;
                    if (winCheck(PLAYER_DOT)) {
                        map[i][j] = AI_DOT;
                        return true;
                    }
                    else map[i][j] = EMPTY;
                }
            }
        }
        return false;
    }

    public void aiTurn() {
        int randX, randY;
        if (!blockPlayer()) {
            do {
                randX = (int) (Math.random() * SIZE);
                randY = (int) (Math.random() * SIZE);
            } while (!isValidDot(randX, randY));
            map[randX][randY] = AI_DOT;
        }
        repaint();
        turnCount++;
    }

    public void gameStart() {
        initMap();
        repaint();
    }

    public boolean winCheck(char player_dot) {
        int x = 0;
        int y = 0;
        for (int i = 0; i < SIZE; i++, x = 0, y = 0) {
            for (int j = 0; j < SIZE; j++) {
                if (map[i][j] == player_dot) {
                    x++;
                    if (x == DOT_TO_WIN) {
                        return true;
                    }
                } else x = 0;
                if (map[j][i] == player_dot) {
                    y++;
                    if (y == DOT_TO_WIN) {
                        return true;
                    }
                } else y = 0;
            }
        }
        if (diagonalCheck(player_dot)) return true;
        return false;
    }

    public boolean diagonalCheck(char dot) {
        int x = 0;
        int f;
        for (int i = 0; i < SIZE; i++) {
            f = i;
            x = 0;
            for (int j = 0; f < SIZE; j++, f++) {
                if (map[j][f] == dot) {
                    x++;
                    if (x == DOT_TO_WIN) {
                        return true;
                    }
                } else x = 0;
            }
            f = i;
            x = 0;
            for (int j = 0; f < SIZE; j++, f++) {
                if (map[f][j] == dot) {
                    x++;
                    if (x == DOT_TO_WIN) {
                        return true;
                    }
                } else x = 0;
            }
        }
        for (int i = 0; i < SIZE; i++) {
            x = 0;
            f = i;
            for (int j = 0; f >= 0; j++, f--) {
                if (map[j][f] == dot) {
                    x++;
                    if (x == DOT_TO_WIN) {
                        return true;
                    }
                } else x = 0;
            }
            for (int j = 0; f >= 0; j++, f--) {
                if (map[f][j] == dot) {
                    x++;
                    if (x == DOT_TO_WIN) {
                        return true;
                    }
                } else x = 0;
            }
        }
        return false;
    }

    public boolean isMapFull() {
        return turnCount == (SIZE * SIZE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        cellWidth = getWidth() / SIZE;
        cellHeight = getHeight() / SIZE;
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(Color.black);
        for (int i = 0; i < SIZE; i++) {
            g2d.drawLine(cellWidth * i, 0, cellWidth * i, SIZE * cellHeight);
            g2d.drawLine(0, cellHeight * i, SIZE * cellWidth, cellHeight * i);
        }
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (map[i][j] == PLAYER_DOT) {
                    g2d.setColor(Color.red);
                    g2d.drawOval(i * cellWidth + 12, j * cellHeight + 12, cellWidth - 24, cellHeight - 24);
                }
            }
        }
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (map[i][j] == AI_DOT) {
                    g2d.setColor(Color.green);
                    g2d.drawOval(i * cellWidth + 12, j * cellHeight + 12, cellWidth - 24, cellHeight - 24);
                }
            }
        }
    }
}
