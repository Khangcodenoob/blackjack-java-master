import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class BlackJack {

    private class Card {
        String value;
        String type;
        boolean isFaceUp;

        Card(String value, String type) {
            this.value = value;
            this.type = type;
            this.isFaceUp = false;
        }

        public String toString() {
            return value + "-" + type;
        }

        public int getValue() {
            if ("AJQK".contains(value)) { //A J Q K
                if (value.equals("A")) {
                    return 11;
                }
                return 10;
            }
            return Integer.parseInt(value); //2-10
        }

        public boolean isAce() {
            return value.equals("A");
        }

        public String getImagePath() {
            return "./cards/" + toString() + ".png";
        }

        public void flip() {
            isFaceUp = !isFaceUp;
        }
    }

    ArrayList<Card> deck;
    Random random = new Random(); //shuffle deck

    //player1
    ArrayList<Card> player1Hand;
    int player1Sum;
    int player1AceCount;
    int player1Money = 100000;

    //player2
    ArrayList<Card> player2Hand;
    int player2Sum;
    int player2AceCount;
    int player2Money = 100000;

    //player3
    ArrayList<Card> player3Hand;
    int player3Sum;
    int player3AceCount;
    int player3Money = 100000;

    //player4
    ArrayList<Card> player4Hand;
    int player4Sum;
    int player4AceCount;
    int player4Money = 100000;

    //window
    int boardWidth = 1000;
    int boardHeight = 900;

    int cardWidth = 80; //ratio should 1/1.4
    int cardHeight = 140;

    JFrame frame = new JFrame("BLACKJACK - Xì Dzách");
    JPanel gamePanel = new JPanel() {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            try {
                //draw deck image
                Image deckImg = new ImageIcon(getClass().getResource("./cards/BACK.png")).getImage();
                g.drawImage(deckImg, boardWidth / 2 - cardWidth / 2, boardHeight / 2 - cardHeight / 2, cardWidth, cardHeight, null);

                //draw player1's hand
                for (int i = 0; i < player1Hand.size(); i++) {
                    Card card = player1Hand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.isFaceUp ? card.getImagePath() : "./cards/BACK.png")).getImage();
                    g.drawImage(cardImg, boardWidth / 2 - cardWidth / 2 - (cardWidth + 5) * (player1Hand.size() / 2) + (cardWidth + 5) * i, 20, cardWidth, cardHeight, null);
                }

                //draw player2's hand
                for (int i = 0; i < player2Hand.size(); i++) {
                    Card card = player2Hand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.isFaceUp ? card.getImagePath() : "./cards/BACK.png")).getImage();
                    g.drawImage(cardImg, boardWidth - cardWidth - 20 - (cardWidth + 5) * i, boardHeight / 2 - cardHeight / 2, cardWidth, cardHeight, null);
                }

                //draw player3's hand
                for (int i = 0; i < player3Hand.size(); i++) {
                    Card card = player3Hand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.isFaceUp ? card.getImagePath() : "./cards/BACK.png")).getImage();
                    g.drawImage(cardImg, boardWidth / 2 - cardWidth / 2 - (cardWidth + 5) * (player3Hand.size() / 2) + (cardWidth + 5) * i, boardHeight - cardHeight - 80, cardWidth, cardHeight, null);
                }

                //draw player4's hand
                for (int i = 0; i < player4Hand.size(); i++) {
                    Card card = player4Hand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.isFaceUp ? card.getImagePath() : "./cards/BACK.png")).getImage();
                    g.drawImage(cardImg, 20 + (cardWidth + 5) * i, boardHeight / 2 - cardHeight / 2, cardWidth, cardHeight, null);
                }

                g.setFont(new Font("Time New Roman", Font.PLAIN, 30));
                g.setColor(Color.white);

                if (!hitButton1.isEnabled() && !stayButton1.isEnabled()
                        && !hitButton2.isEnabled() && !stayButton2.isEnabled()
                        && !hitButton3.isEnabled() && !stayButton3.isEnabled()
                        && !hitButton4.isEnabled() && !stayButton4.isEnabled()) {
                    showResults(g);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void showResults(Graphics g) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'showResults'");
        }
    };

    JButton againButton = new JButton("VÁN MỚI!!");
    JButton hitButton1 = new JButton("Player 1 RÚT!");
    JButton stayButton1 = new JButton("Player 1 DẰN!");
    JButton hitButton2 = new JButton("Player 2 RÚT!");
    JButton stayButton2 = new JButton("Player 2 DẰN!");
    JButton hitButton3 = new JButton("Player 3 RÚT!");
    JButton stayButton3 = new JButton("Player 3 DẰN!");
    JButton hitButton4 = new JButton("Player 4 RÚT!");
    JButton stayButton4 = new JButton("Player 4 DẰN!");
    JLabel resultLabel1 = new JLabel("");
    JLabel resultLabel2 = new JLabel("");
    JLabel resultLabel3 = new JLabel("");
    JLabel resultLabel4 = new JLabel("");

    BlackJack() {
        startGame();

        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel.setLayout(null);
        gamePanel.setBackground(new Color(53, 101, 77));
        frame.add(gamePanel);

        // Player 1 components
        hitButton1.setBounds(boardWidth / 2 - 110, 200, 120, 40);
        stayButton1.setBounds(boardWidth / 2, 200, 120, 40);
        resultLabel1.setBounds(boardWidth / 2 - 60, 250, 175, 100);
        resultLabel1.setForeground(Color.black);
        gamePanel.add(hitButton1);
        gamePanel.add(stayButton1);
        gamePanel.add(resultLabel1);

        // Player 2 components
        hitButton2.setBounds(boardWidth - 250, boardHeight / 2 - 125, 110, 40);
        stayButton2.setBounds(boardWidth - 150, boardHeight / 2 - 125, 110, 40);
        resultLabel2.setBounds(boardWidth - 250, boardHeight / 2 - 50, 200, 40);
        resultLabel2.setForeground(Color.black);
        gamePanel.add(hitButton2);
        gamePanel.add(stayButton2);
        gamePanel.add(resultLabel2);

        // Player 3 components
        hitButton3.setBounds(boardWidth / 2 - 110, boardHeight - 300, 120, 40);
        stayButton3.setBounds(boardWidth / 2, boardHeight - 300, 120, 40);
        resultLabel3.setBounds(boardWidth / 2 - 60, boardHeight - 260, 200, 30);
        resultLabel3.setForeground(Color.black);
        gamePanel.add(hitButton3);
        gamePanel.add(stayButton3);
        gamePanel.add(resultLabel3);

        // Player 4 components
        hitButton4.setBounds(90, boardHeight / 2 - 125, 110, 40);
        stayButton4.setBounds(190, boardHeight / 2 - 125, 110, 40);
        resultLabel4.setBounds(250, boardHeight / 2 - 20, 100, 40);
        resultLabel4.setForeground(Color.black);
        gamePanel.add(hitButton4);
        gamePanel.add(stayButton4);
        gamePanel.add(resultLabel4);

        againButton.setBounds(boardWidth / 2 - 100, boardHeight / 2 + 100, 200, 30);
        againButton.setVisible(false);
        gamePanel.add(againButton);

        againButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                player1Hand.clear();
                player1Sum = 0;
                player1AceCount = 0;

                player2Hand.clear();
                player2Sum = 0;
                player2AceCount = 0;

                player3Hand.clear();
                player3Sum = 0;
                player3AceCount = 0;

                player4Hand.clear();
                player4Sum = 0;
                player4AceCount = 0;

                resultLabel1.setText("");
                resultLabel2.setText("");
                resultLabel3.setText("");
                resultLabel4.setText("");

                buildDeck();
                shuffleDeck();

                startGame();

                hitButton1.setEnabled(true);
                stayButton1.setEnabled(true);
                hitButton2.setEnabled(true);
                stayButton2.setEnabled(true);
                hitButton3.setEnabled(true);
                stayButton3.setEnabled(true);
                hitButton4.setEnabled(true);
                stayButton4.setEnabled(true);
                againButton.setVisible(false);

                gamePanel.repaint();
            }
        });

        hitButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Card card = deck.remove(deck.size() - 1);
                player1Sum += card.getValue();
                player1AceCount += card.isAce() ? 1 : 0;
                player1Hand.add(card);
                if (reducePlayer1Ace() > 21) {
                    hitButton1.setEnabled(false);
                    checkEndGame();
                }
                gamePanel.repaint();
            }
        });

        stayButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hitButton1.setEnabled(false);
                stayButton1.setEnabled(false);
                checkEndGame();
            }
        });

        hitButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Card card = deck.remove(deck.size() - 1);
                player2Sum += card.getValue();
                player2AceCount += card.isAce() ? 1 : 0;
                player2Hand.add(card);
                if (reducePlayer2Ace() > 21) {
                    hitButton2.setEnabled(false);
                    checkEndGame();
                }
                gamePanel.repaint();
            }
        });

        stayButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hitButton2.setEnabled(false);
                stayButton2.setEnabled(false);
                checkEndGame();
            }
        });

        hitButton3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Card card = deck.remove(deck.size() - 1);
                player3Sum += card.getValue();
                player3AceCount += card.isAce() ? 1 : 0;
                player3Hand.add(card);
                if (reducePlayer3Ace() > 21) {
                    hitButton3.setEnabled(false);
                    checkEndGame();
                }
                gamePanel.repaint();
            }
        });

        stayButton3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hitButton3.setEnabled(false);
                stayButton3.setEnabled(false);
                checkEndGame();
            }
        });

        hitButton4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Card card = deck.remove(deck.size() - 1);
                player4Sum += card.getValue();
                player4AceCount += card.isAce() ? 1 : 0;
                player4Hand.add(card);
                if (reducePlayer4Ace() > 21) {
                    hitButton4.setEnabled(false);
                    checkEndGame();
                }
                gamePanel.repaint();
            }
        });

        stayButton4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hitButton4.setEnabled(false);
                stayButton4.setEnabled(false);
                checkEndGame();
            }
        });

        gamePanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int yPlayer1 = 20;
                int yPlayer2 = boardHeight / 2 - cardHeight / 2;
                int yPlayer3 = boardHeight - cardHeight - 100;
                int yPlayer4 = boardHeight / 2 - cardHeight / 2;

                for (int i = 0; i < player1Hand.size(); i++) {
                    Rectangle cardRect = new Rectangle(boardWidth / 2 - cardWidth / 2 - (cardWidth + 5) * (player1Hand.size() / 2) + (cardWidth + 5) * i, yPlayer1, cardWidth, cardHeight);
                    if (cardRect.contains(e.getPoint())) {
                        player1Hand.get(i).flip();
                    }
                }

                for (int i = 0; i < player2Hand.size(); i++) {
                    Rectangle cardRect = new Rectangle(boardWidth - cardWidth - 20 - (cardWidth + 5) * i, yPlayer2, cardWidth, cardHeight);
                    if (cardRect.contains(e.getPoint())) {
                        player2Hand.get(i).flip();
                    }
                }

                for (int i = 0; i < player3Hand.size(); i++) {
                    Rectangle cardRect = new Rectangle(boardWidth / 2 - cardWidth / 2 - (cardWidth + 5) * (player3Hand.size() / 2) + (cardWidth + 5) * i, yPlayer3, cardWidth, cardHeight);
                    if (cardRect.contains(e.getPoint())) {
                        player3Hand.get(i).flip();
                    }
                }

                for (int i = 0; i < player4Hand.size(); i++) {
                    Rectangle cardRect = new Rectangle(20 + (cardWidth + 5) * i, yPlayer4, cardWidth, cardHeight);
                    if (cardRect.contains(e.getPoint())) {
                        player4Hand.get(i).flip();
                    }
                }

                Rectangle deckRect = new Rectangle(boardWidth / 2 - cardWidth / 2, boardHeight / 2 - cardHeight / 2, cardWidth, cardHeight);
                if (deckRect.contains(e.getPoint())) {
                    startGame();
                }

                gamePanel.repaint();
            }
        });

        gamePanel.repaint();
    }

    public void checkEndGame() {
        if (!hitButton1.isEnabled() && !stayButton1.isEnabled()
                && !hitButton2.isEnabled() && !stayButton2.isEnabled()
                && !hitButton3.isEnabled() && !stayButton3.isEnabled()
                && !hitButton4.isEnabled() && !stayButton4.isEnabled()) {
            player1Sum = reducePlayer1Ace();
            player2Sum = reducePlayer2Ace();
            player3Sum = reducePlayer3Ace();
            player4Sum = reducePlayer4Ace();

            int[] playerSums = {player1Sum, player2Sum, player3Sum, player4Sum};
            int maxSum = 0;
            for (int sum : playerSums) {
                if (sum <= 21 && sum > maxSum) {
                    maxSum = sum;
                }
            }

            resultLabel1.setText(getResult(player1Sum, maxSum));
            resultLabel2.setText(getResult(player2Sum, maxSum));
            resultLabel3.setText(getResult(player3Sum, maxSum));
            resultLabel4.setText(getResult(player4Sum, maxSum));

            againButton.setVisible(true);
        }
    }

    public String getResult(int playerSum, int maxSum) {
        if (playerSum > 21) {
            return "Thua!";
        } else if (playerSum == maxSum) {
            return "Thắng!";
        } else {
            return "Thua!";
        }
        
    }

    public void startGame() {
        //deck
        buildDeck();
        shuffleDeck();

        //player1
        player1Hand = new ArrayList<Card>();
        player1Sum = 0;
        player1AceCount = 0;

        for (int i = 0; i < 2; i++) {
            Card card = deck.remove(deck.size() - 1);
            player1Sum += card.getValue();
            player1AceCount += card.isAce() ? 1 : 0;
            player1Hand.add(card);
        }

        //player2
        player2Hand = new ArrayList<Card>();
        player2Sum = 0;
        player2AceCount = 0;

        for (int i = 0; i < 2; i++) {
            Card card = deck.remove(deck.size() - 1);
            player2Sum += card.getValue();
            player2AceCount += card.isAce() ? 1 : 0;
            player2Hand.add(card);
        }

        //player3
        player3Hand = new ArrayList<Card>();
        player3Sum = 0;
        player3AceCount = 0;

        for (int i = 0; i < 2; i++) {
            Card card = deck.remove(deck.size() - 1);
            player3Sum += card.getValue();
            player3AceCount += card.isAce() ? 1 : 0;
            player3Hand.add(card);
        }

        //player4
        player4Hand = new ArrayList<Card>();
        player4Sum = 0;
        player4AceCount = 0;

        for (int i = 0; i < 2; i++) {
            Card card = deck.remove(deck.size() - 1);
            player4Sum += card.getValue();
            player4AceCount += card.isAce() ? 1 : 0;
            player4Hand.add(card);
        }
    }

    public void buildDeck() {
        deck = new ArrayList<Card>();
        String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        String[] types = {"C", "D", "H", "S"};

        for (int i = 0; i < types.length; i++) {
            for (int j = 0; j < values.length; j++) {
                Card card = new Card(values[j], types[i]);
                deck.add(card);
            }
        }
    }

    public void shuffleDeck() {
        for (int i = 0; i < deck.size(); i++) {
            int j = random.nextInt(deck.size());
            Card currCard = deck.get(i);
            Card randomCard = deck.get(j);
            deck.set(i, randomCard);
            deck.set(j, currCard);
        }
    }

    public int reducePlayer1Ace() {
        while (player1Sum > 21 && player1AceCount > 0) {
            player1Sum -= 10;
            player1AceCount -= 1;
        }
        return player1Sum;
    }

    public int reducePlayer2Ace() {
        while (player2Sum > 21 && player2AceCount > 0) {
            player2Sum -= 10;
            player2AceCount -= 1;
        }
        return player2Sum;
    }

    public int reducePlayer3Ace() {
        while (player3Sum > 21 && player3AceCount > 0) {
            player3Sum -= 10;
            player3AceCount -= 1;
        }
        return player3Sum;
    }

    public int reducePlayer4Ace() {
        while (player4Sum > 21 && player4AceCount > 0) {
            player4Sum -= 10;
            player4AceCount -= 1;
        }
        return player4Sum;
    }

    public static void main(String[] args) {
        new BlackJack();
    }
}
