package ResultFrame;

import javax.swing.*;
import java.awt.*;

// 결과값을 출력해주는 프레임.
public class ResultFrame extends JFrame {

  JPanel panel;
  JButton bt = new JButton(new ImageIcon("./image/믹서기.gif"));

  private int days;
  private int failDays;

  public ResultFrame(int days, int failDays) {
    this.days = days;
    this.failDays = failDays;

    panel = new JPanel(new BorderLayout());

    this.setTitle("결과");
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

  // 포도 gif의 길이가 약 8초정도 되므로, 8초 새로운 스레드를 실행시켜, 8초 뒤에 이미지를 바꾸도록 하는 메서드이다.
  public void grapeChanger() {
    // 8초간 gif 화면을 본뒤, 포도주스 이미지 띄움.
    new Thread(() -> {
      try {
        Thread.sleep(8000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      // 포도주스 퍼센트에 따라 분기처리하기
      if (days == 30) {
        if (failDays == 0) {
          bt.setIcon(new ImageIcon("./image/포도주스100.jpg"));
        } else {
          bt.setIcon(new ImageIcon("./image/포도주스80.jpg"));
        }
      } else if (days == 66) {
        if (failDays == 0) {
          bt.setIcon(new ImageIcon("./image/포도주스100.jpg"));
        } else if (failDays <= 6) {
          bt.setIcon(new ImageIcon("./image/포도주스91.jpg"));
        } else {
          bt.setIcon(new ImageIcon("./image/포도주스82.jpg"));
        }
      }
    }).start();
  }

  public static void main(String[] args) {
    new ResultFrame(66, 11);
  }
}
