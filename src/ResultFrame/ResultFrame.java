package ResultFrame;

import javax.swing.*;
import java.awt.*;

// ������� ������ִ� ������.
public class ResultFrame extends JFrame {

  JPanel panel;
  JButton bt = new JButton(new ImageIcon("./image/�ͼ���.gif"));

  private int days;
  private int failDays;

  public ResultFrame(int days, int failDays) {
    this.days = days;
    this.failDays = failDays;

    panel = new JPanel(new BorderLayout());

    this.setTitle("���");
    this.setSize(500, 600);
    this.setResizable(false);


    bt.setBorderPainted(false);
    bt.setFocusPainted(false);
    bt.setContentAreaFilled(false);

    panel.add(bt, BorderLayout.CENTER);
    this.add(panel);
    setVisible(true);

    grapeChanger();
  }

  // ���� gif�� ���̰� �� 8������ �ǹǷ�, 8�� ���ο� �����带 �������, 8�� �ڿ� �̹����� �ٲٵ��� �ϴ� �޼����̴�.
  public void grapeChanger() {
    // 8�ʰ� gif ȭ���� ����, �����ֽ� �̹��� ���.
    new Thread(() -> {
      try {
        Thread.sleep(8000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      // �����ֽ� �ۼ�Ʈ�� ���� �б�ó���ϱ�
      if (days == 30) {
        if (failDays == 0) {
          bt.setIcon(new ImageIcon("./image/�����ֽ�100.jpg"));
        } else {
          bt.setIcon(new ImageIcon("./image/�����ֽ�80.jpg"));
        }
      } else if (days == 66) {
        if (failDays == 0) {
          bt.setIcon(new ImageIcon("./image/�����ֽ�100.jpg"));
        } else if (failDays <= 6) {
          bt.setIcon(new ImageIcon("./image/�����ֽ�91.jpg"));
        } else {
          bt.setIcon(new ImageIcon("./image/�����ֽ�82.jpg"));
        }
      }
    }).start();
  }

  public static void main(String[] args) {
    new ResultFrame(66, 11);
  }
}
