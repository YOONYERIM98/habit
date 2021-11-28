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

    JPanel panel1 = new JPanel(new BorderLayout()); //제목 패널
    JLabel title = new JLabel("습관 만들기");
    title.setFont(new Font("맑은고딕", Font.BOLD, 30));
    title.setHorizontalAlignment(JLabel.CENTER);
    panel1.add(title);

    setTitle("습관 설정 ");
    setSize(1280, 1280);

    String[] goalTarget = {"30", "66"};  // 목표 칸 수

    main = new JPanel(new GridLayout(0, 2));

    JPanel panel2 = new JPanel(); // 습관,목표 칸 수,자체 휴일 설정 패널

    panel2.add(main);
    JLabel goalName = new JLabel("습        관");
    habitTextField = new JTextField(11);
    main.add(goalName);
    main.add(habitTextField);

    JLabel goalRepeat = new JLabel("목표 칸 수");
    cb = new JComboBox(goalTarget);
    cb.addActionListener(this);
    main.add(goalRepeat);
    main.add(cb);

    JLabel goalHoliday = new JLabel("자체 휴일");
    cb2 = new JComboBox();
    main.add(goalHoliday);
    main.add(cb2);

    for (int i = 0; i < 5; i++) {
      String to = Integer.toString(i);
      cb2.addItem(to);
    }

    JPanel panel3 = new JPanel(); // 버튼 패널
    button = new JButton("다음");
    button.addActionListener(this);
    panel3.add(button);


    panel.add(panel1);
    panel.add(panel2);
    panel.add(panel3);
    add(panel);
    setVisible(true);

  }

  // 다음 프레임을 호출한다.
  private void nextFrame() {
    new RewardFrame(parentHabit, habitTextField.getText(), (int) Integer.valueOf((String) cb.getSelectedItem()), (int) Integer.valueOf((String) cb2.getSelectedItem()));
  }


  @Override
  public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub
    if (e.getActionCommand().equals("comboBoxChanged")) {
      // 콤보 박스 정보가 변경됐을 때, 계획 일수에 따른 최대 휴일 목록을 cb2콤보박스에 저장한다.
      JComboBox cb = (JComboBox) e.getSource();
      int index = cb.getSelectedIndex();
      for (int i = 0; i < 10; i++) {
        String to = Integer.toString(i);
        cb2.removeItem(to);
      }
      // 목표 30일 경우 휴일 4
      if (index == 0) {
        for (int i = 0; i < 5; i++) {
          String to = Integer.toString(i);
          cb2.addItem(to);
        }
      }

      //목표 66일 경우 휴일 9
      else {
        for (int i = 0; i < 10; i++) {
          String to = Integer.toString(i);
          cb2.addItem(to);
        }
      }
    } else if (e.getActionCommand().equals("다음")) {
      // 다음 버튼을 누르면, 현재 창은 종료하고, 다음 프레임을 띄운다.
      this.dispose();
      nextFrame();
    }
  }
}
