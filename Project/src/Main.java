import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Main {
    private static int currentLine;
    private static int linesCount;
    public static void main(String[] args) {
        JFrame frame = new JFrame("Программа");
        JButton startButton = new JButton("Начать");
        JButton exitButton = new JButton("Выйти");

        ImageIcon icon = new ImageIcon("kvaa.png");
        Image image = icon.getImage().getScaledInstance(500, 400, Image.SCALE_SMOOTH);
        Icon scaledIcon = new ImageIcon(image);

        JLabel imageLabel = new JLabel(scaledIcon);
        frame.add(imageLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(exitButton);
        frame.add(buttonPanel, BorderLayout.CENTER);

        JPanel startButtonPanel = new JPanel(new GridLayout(1, 2));
        startButtonPanel.add(startButton);
        frame.add(startButtonPanel, BorderLayout.SOUTH);

        JLabel cardLabel = new JLabel("", SwingConstants.CENTER);
        cardLabel.setVisible(false);
        frame.add(cardLabel, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3));
        JButton yesButton = new JButton("Да");
        panel.add(yesButton);          // Добавили новую кнопку
        JButton checkButton = new JButton("Проверить");

        try {
            File soundFile = new File("ost.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        checkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    FileReader fileReader = new FileReader("slova.txt");
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    String line = bufferedReader.readLine();
                    for (int i = 0; i < currentLine; i++) {  // Пропускаем строки до текущей позиции
                        line = bufferedReader.readLine();
                    }
                    String[] parts = line.split("-");
                    String firstPart = parts[0];
                    String secondPart = parts[1];

                    if (cardLabel.getText().equals(firstPart)) {
                        cardLabel.setText(secondPart);
                    } else {
                        cardLabel.setText(firstPart);
                    }

                    bufferedReader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        panel.add(checkButton);
        JButton noButton = new JButton("Нет");  // Добавили новую кнопку
        noButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    FileReader fileReader = new FileReader("slova.txt");
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    FileWriter fileWriter = new FileWriter("povtor.txt", true);  // Создаем объект FileWriter
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);  // Создаем объект BufferedWriter
                    String line = bufferedReader.readLine();
                    for (int i = 0; i < currentLine; i++) {  // Пропускаем строки до текущей позиции
                        line = bufferedReader.readLine();
                    }
                    String[] parts = line.split("-");
                    String firstPart = parts[0];
                    String secondPart = parts[1];
                    bufferedWriter.write(firstPart + "-" + secondPart);  // Записываем пару слов в файл
                    bufferedWriter.newLine();  // Переходим на новую строку
                    bufferedWriter.close();
                    bufferedReader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                currentLine++;
                if (currentLine >= linesCount) {
                    currentLine = 0;
                    Object[] options = {"Повторить?", "Выйти"};
                    int choice = JOptionPane.showOptionDialog(frame, "Файл был полностью прочитан!", "Конец файла",
                            JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                    if (choice == JOptionPane.YES_OPTION) {
                        // Пользователь выбрал "Повторить?"
                        checkButton.doClick();
                    } else {
                        // Пользователь выбрал "Выйти"
                        System.exit(0);
                    }
                }
                checkButton.doClick();  // Вызываем проверку текущей строки
            }
        });
        panel.add(noButton);
        yesButton.addActionListener(new ActionListener() {  //Добавили обработчик для новой кнопки
            public void actionPerformed(ActionEvent e) {
                currentLine++;
                if (currentLine >= linesCount) {
                    currentLine = 0;
                    Object[] options = {"Повторить?", "Выйти"};
                    int choice = JOptionPane.showOptionDialog(frame, "Файл был полностью прочитан!", "Конец файла",
                            JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                    if (choice == JOptionPane.YES_OPTION) {
                        // Пользователь выбрал "Повторить?"
                        checkButton.doClick();
                    } else {
                        // Пользователь выбрал "Выйти"
                        System.exit(0);
                    }
                }
                checkButton.doClick();  // Вызываем проверку текущей строки
            }
        });

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                imageLabel.setIcon(null);
                panel.setVisible(true);
                cardLabel.setVisible(true);
                startButton.setVisible(false);
                exitButton.setVisible(false);
                try {
                    FileReader fileReader = new FileReader("slova.txt");
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    String line = bufferedReader.readLine();
                    linesCount = 0;
                    while (line != null) {  // Считаем количество строк в файле
                        linesCount++;
                        line = bufferedReader.readLine();
                    }
                    bufferedReader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                currentLine = 0;
                checkButton.doClick();  // Вызываем проверку первой строки
                frame.revalidate();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        Border border = BorderFactory.createLineBorder(new Color(0, 100, 0, 165),7);
        Border border2 = BorderFactory.createLineBorder(new Color(255, 255, 115, 147),5);
        frame.add(panel, BorderLayout.SOUTH);
        panel.setVisible(false);
        frame.add(startButton, BorderLayout.WEST);
        frame.add(exitButton, BorderLayout.EAST);
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(235, 255, 84));// не работает лмао
        exitButton.setBackground(new Color(255, 255, 170)); exitButton.setForeground(new Color(0, 100, 0));
        exitButton.setBorder(border);
        exitButton.setPreferredSize(new Dimension(246, 30));
        exitButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.setBackground(new Color(255, 255, 170)); startButton.setForeground(new Color(0, 100, 0));
        startButton.setBorder(border);
        startButton.setPreferredSize(new Dimension(246, 30));
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        yesButton.setBackground(new Color(157, 206, 58)); yesButton.setForeground(new Color(255, 250, 250));
        yesButton.setBorder(border2);
        yesButton.setPreferredSize(new Dimension(150, 50));
        yesButton.setFont(new Font("Arial", Font.BOLD, 20));
        noButton.setBackground(new Color(157, 206, 58)); noButton.setForeground(new Color(255, 250, 250));
        noButton.setBorder(border2);
        noButton.setFont(new Font("Arial", Font.BOLD, 20));
        checkButton.setBackground(new Color(255, 255, 115)); checkButton.setForeground(new Color(0, 100, 0));
        checkButton.setBorder(border);
        checkButton.setFont(new Font("Arial", Font.BOLD, 20));
        cardLabel.setFont(new Font("Arial", Font.BOLD, 25));
        cardLabel.setForeground(new Color(0, 100, 0));
    }
}