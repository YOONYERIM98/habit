package reward;

import main.TabbedPaneFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;


class Reward implements Serializable {
  public String[] rewardArr;

  Reward(int targetNum) {
    this.rewardArr = new String[targetNum];
  }

  Reward(String[] rewardArr) {
    this.rewardArr = rewardArr;
  }
}

public class RewardFrame implements ActionListener {
  JFrame jFrame = new JFrame("보상창");
  Object rowData[][];
  JPanel panel = new JPanel(new BorderLayout());
  final TabbedPaneFrame parentTabbedFrame;
  final String habitTitle;
  final int days;
  final int withoutDays;

  public RewardFrame(TabbedPaneFrame parentFrame, String habitTitle, int days, int withoutDays) {
    this.parentTabbedFrame = parentFrame;
    this.habitTitle = habitTitle;
    this.days = days;
    this.withoutDays = withoutDays;

    // 보상은 일수에 따라, 갯수가 달라진다. 66일이면 11번. 30일이면 5번이다.
    if (days == 66) {
      rowData = new Object[][]{{1, ""},
          {2, ""},
          {3, ""},
          {4, ""},
          {5, ""},
          {6, ""},
          {7, ""},
          {8, ""},
          {9, ""},
          {10, ""},
          {11, ""}};
    } else {
      rowData = new Object[][]{{1, ""},
          {2, ""},
          {3, ""},
          {4, ""},
          {5, ""}};
    }


    panel.add(new RewardPanel(rowData), BorderLayout.CENTER);
//    jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);



    final JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
    JButton ok = new JButton("확인");
    ok.addActionListener(this);
    bottomPanel.add(ok);
    JButton cancel = new JButton("취소");
    cancel.addActionListener(this);
    bottomPanel.add(cancel);


    panel.add(bottomPanel, BorderLayout.SOUTH);
    jFrame.add(panel);
    jFrame.setSize(400, 300);
    jFrame.setVisible(true);

  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("취소")) {
      jFrame.dispose();
    } else if (e.getActionCommand().equals("확인")) {
      // 확인을 누르면, tabbedFrame.newHabit 을 호출하여, 메인탭에 현재 habit정보를 추가한다. 그 뒤, 현재창을 종료한다.
      parentTabbedFrame.newHabit(habitTitle, days, withoutDays, rowData);
      jFrame.dispose();
    }
  }
}
