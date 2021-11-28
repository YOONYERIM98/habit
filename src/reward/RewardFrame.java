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
  JFrame jFrame = new JFrame("����â");
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

    // ������ �ϼ��� ����, ������ �޶�����. 66���̸� 11��. 30���̸� 5���̴�.
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
    JButton ok = new JButton("Ȯ��");
    ok.addActionListener(this);
    bottomPanel.add(ok);
    JButton cancel = new JButton("���");
    cancel.addActionListener(this);
    bottomPanel.add(cancel);


    panel.add(bottomPanel, BorderLayout.SOUTH);
    jFrame.add(panel);
    jFrame.setSize(400, 300);
    jFrame.setVisible(true);

  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("���")) {
      jFrame.dispose();
    } else if (e.getActionCommand().equals("Ȯ��")) {
      // Ȯ���� ������, tabbedFrame.newHabit �� ȣ���Ͽ�, �����ǿ� ���� habit������ �߰��Ѵ�. �� ��, ����â�� �����Ѵ�.
      parentTabbedFrame.newHabit(habitTitle, days, withoutDays, rowData);
      jFrame.dispose();
    }
  }
}
