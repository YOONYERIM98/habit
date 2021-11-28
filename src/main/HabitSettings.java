package main;

import reward.RewardFrame;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class HabitSettings extends JFrame implements ActionListener {

  JPanel panel, next, main;
  JButton button;
  JComboBox cb, cb2;
  JTextField habitTextField;

  TabbedPaneFrame parentHabit;


  public HabitSettings(TabbedPaneFrame newHabit) {
    parentHabit = newHabit;

    panel = new JPanel(new GridLayout(3, 1));

    JPanel panel1 = new JPanel(new BorderLayout()); //���� �г�
    JLabel title = new JLabel("���� �����");
    title.setFont(new Font("�������", Font.BOLD, 30));
    title.setHorizontalAlignment(JLabel.CENTER);
    panel1.add(title);

    setTitle("���� ���� ");
    setSize(1280, 1280);

    String[] goalTarget = {"30", "66"};  // ��ǥ ĭ ��

    main = new JPanel(new GridLayout(0, 2));

    JPanel panel2 = new JPanel(); // ����,��ǥ ĭ ��,��ü ���� ���� �г�

    panel2.add(main);
    JLabel goalName = new JLabel("��        ��");
    habitTextField = new JTextField(11);
    main.add(goalName);
    main.add(habitTextField);

    JLabel goalRepeat = new JLabel("��ǥ ĭ ��");
    cb = new JComboBox(goalTarget);
    cb.addActionListener(this);
    main.add(goalRepeat);
    main.add(cb);

    JLabel goalHoliday = new JLabel("��ü ����");
    cb2 = new JComboBox();
    main.add(goalHoliday);
    main.add(cb2);

    for (int i = 0; i < 5; i++) {
      String to = Integer.toString(i);
      cb2.addItem(to);
    }

    JPanel panel3 = new JPanel(); // ��ư �г�
    button = new JButton("����");
    button.addActionListener(this);
    panel3.add(button);


    panel.add(panel1);
    panel.add(panel2);
    panel.add(panel3);
    add(panel);
    setVisible(true);

  }

  // ���� �������� ȣ���Ѵ�.
  private void nextFrame() {
    new RewardFrame(parentHabit, habitTextField.getText(), (int) Integer.valueOf((String) cb.getSelectedItem()), (int) Integer.valueOf((String) cb2.getSelectedItem()));
  }


  @Override
  public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub
    if (e.getActionCommand().equals("comboBoxChanged")) {
      // �޺� �ڽ� ������ ������� ��, ��ȹ �ϼ��� ���� �ִ� ���� ����� cb2�޺��ڽ��� �����Ѵ�.
      JComboBox cb = (JComboBox) e.getSource();
      int index = cb.getSelectedIndex();
      for (int i = 0; i < 10; i++) {
        String to = Integer.toString(i);
        cb2.removeItem(to);
      }
      // ��ǥ 30�� ��� ���� 4
      if (index == 0) {
        for (int i = 0; i < 5; i++) {
          String to = Integer.toString(i);
          cb2.addItem(to);
        }
      }

      //��ǥ 66�� ��� ���� 9
      else {
        for (int i = 0; i < 10; i++) {
          String to = Integer.toString(i);
          cb2.addItem(to);
        }
      }
    } else if (e.getActionCommand().equals("����")) {
      // ���� ��ư�� ������, ���� â�� �����ϰ�, ���� �������� ����.
      this.dispose();
      nextFrame();
    }
  }
}
