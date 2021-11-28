package diary;

import grape.SeparateGrape;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class DiaryWindow extends JFrame implements ActionListener {

  JButton button;
  JTextArea info;
  String time2;
  final SeparateGrape parentGrape;

  public DiaryWindow(SeparateGrape parentGrape, String diaryContent) {
    this.parentGrape =parentGrape;
    setSize(500, 400);
//    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle("�ۼ��ʵ�");

    JPanel panelDay = new JPanel();
    JPanel panelText = new JPanel();
    JPanel panelButton = new JPanel();

    SimpleDateFormat format2 = new SimpleDateFormat("yyyy�� MM�� dd��");

    Date time = new Date();

    time2 = format2.format(time);

    JLabel label1 = new JLabel(time2);

    panelDay.add(label1);

    info = new JTextArea("", 40, 40);
    info.setText(diaryContent);
    info.setLineWrap(true);
    info.setWrapStyleWord(true);
    panelText.add(info);

    button = new JButton("�ۼ�");
    button.addActionListener(this);
    JButton button2 = new JButton("���");
    button2.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if (e.getSource() == button2)
          dispose();
      }
    });
    panelButton.add(button);
    panelButton.add(button2);

    setLayout(new BorderLayout());
    add(panelDay, BorderLayout.NORTH);
    add(panelText, BorderLayout.CENTER);
    add(panelButton, BorderLayout.SOUTH);
    setVisible(true);
  }


  public void actionPerformed(ActionEvent e) {

    if (e.getSource() == button) {
      Object obj = e.getSource();

      try {

        // �ۼ��� �Ϸ��ϸ�, ���̾�� ����Ǵ� separateGrape�� ���̾ ������ �����Ѵ�.
        parentGrape.saveDiary(info.getText());
        this.dispose();
      } catch (Exception e1) {

        // TODO Auto-generated catch block

        e1.printStackTrace();

      }

    }
  }


}


