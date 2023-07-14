import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class Main {
    private static int currentLine;
    private static int linesCount;
    public static void main(String[] args) {
        JFrame frame = new JFrame("Программа");
        JButton startButton = new JButton("Начать");
        JButton exitButton = new JButton("Выйти");

        JLabel cardLabel = new JLabel("", SwingConstants.CENTER);
        cardLabel.setVisible(false);
        frame.add(cardLabel, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3));
        JButton yesButton = new JButton("Да");  // Переименовали кнопку
        panel.add(yesButton);                  // Добавили новую кнопку
        JButton checkButton = new JButton("Проверить");
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
                if (currentLine >= linesCount) {  // Если достигнут конец файла, переходим на первую строку
                    currentLine = 0;
                }
                checkButton.doClick();  // Вызываем проверку текущей строки
            }
        });
        panel.add(noButton);
        yesButton.addActionListener(new ActionListener() {  //Добавили обработчик для новой кнопки
            public void actionPerformed(ActionEvent e) {
                currentLine++;
                if (currentLine >= linesCount) {  // Если достигнут конец файла, переходим на первую строку
                    currentLine = 0;
                }
                checkButton.doClick();  // Вызываем проверку текущей строки
            }
        });

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.add(exitButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        JPanel startButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        startButtonPanel.add(startButton);
        frame.add(startButtonPanel, BorderLayout.SOUTH);

        frame.add(panel, BorderLayout.SOUTH);
        panel.setVisible(false);
        frame.add(startButton, BorderLayout.WEST);
        frame.add(exitButton, BorderLayout.EAST);
        frame.setSize(500, 150);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}