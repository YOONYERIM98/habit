package main;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class SettingFrame extends JFrame {

  JPanel panel;

  SettingFrame() {

    panel = new JPanel(new BorderLayout());

    this.setTitle("���������");
    this.setSize(500, 600);
    this.setResizable(false);

    // �����ӿ� ��ư�� �߰��Ѵ�. ��ư�� ������, ���� â�� �����ϰ�, TabbedPaneFrame â�� ����.
    JButton bt = new JButton(new ImageIcon("./image/����.jpg"));

    bt.setBorderPainted(false);
    bt.setFocusPainted(false);
    bt.setContentAreaFilled(false);

    bt.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        new TabbedPaneFrame();
        dispose();
      }
    });
    panel.add(bt, BorderLayout.CENTER);
    this.add(panel);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }
}