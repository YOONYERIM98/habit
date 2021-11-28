package main;

import grape.MainGrape;
import grape.SeparateGrape;
import models.CusMysql;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;


public class TabbedPaneFrame extends JFrame implements ActionListener {

  // �� �߰� ��ư
  JButton insertbutton;
  // �� ���� ��ư
  JButton deletebutton;
  // ��
  JTabbedPane tabbedpane;
  // ���� ���� ����
  int num = 1;
  // ���� �ִ� ����
  int nextnum = 7;

  public TabbedPaneFrame() {


    setTitle("���������");
    setSize(1600, 1400);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPanel toppanel = new JPanel(new BorderLayout());

    JPanel buttonpanel = new JPanel();

    // ���� ��ư�� �Ҵ����ְ�, �׼� �����ʸ� �߰��Ѵ�.
    insertbutton = new JButton("�ε��� �߰�");
    insertbutton.addActionListener(this);
    deletebutton = new JButton("�ε��� ����");
    deletebutton.addActionListener(this);
    buttonpanel.add(insertbutton);
    buttonpanel.add(deletebutton);


    // ���� �߰���Ų��.
    tabbedpane = new JTabbedPane();
    toppanel.add(tabbedpane, BorderLayout.CENTER);
    toppanel.add(buttonpanel, BorderLayout.NORTH);

    add(toppanel);

    // ����, ������ ������� �ε����� �ִٸ� �ε��Ѵ�.
    loadData();

    setVisible(true);
  }

  private void loadData() {
	  
    // habit ������ ���� ���� DB���� �о�´�.
    ArrayList habits = CusMysql.getHabitList();
    for (int i = 0; i < habits.size(); i++) {//����Ǿ��ִ� �迭 ũ����� �ݺ�
      Map habit = (Map) habits.get(i);//�迭�� ����� ���� ����

      int habitIdx = (int) habit.get("habit_idx");//key�� ���� value���� ����
      String habitTitle = (String) habit.get("title");
      int days = (int) habit.get("days");
      int withoutDays = (int) habit.get("without_days");
      String createAt = (String) habit.get("create_at");

      // ���� ������ ���� ���� DB���� �о�´�.
      // �� ��, ���� ����ϴ� ������ ���¿� �°� ��ȯ�Ѵ�.
      ArrayList<Map> rewardList = CusMysql.getRewardByHabitIdx(habitIdx);//�ε��� ��ȣ ����
      Object[][] rewardItems = new Object[rewardList.size()][2]; //[reward���̺� ī��η�Ƽ][��Ʈ����Ʈ]

      for (int j = 0; j < rewardList.size(); j++) {//����Ǿ��ִ� �迭 ũ����� �ݺ�
        rewardItems[j][0] = j + 1;//1��
        rewardItems[j][1] = rewardList.get(j).get("content");//2��
      }

      // ������ ������ ���� ���� DB���� �о�´�.
      // �׵�, ������ ��ü�� �����Ѵ�.
      ArrayList<Map> grapeList = CusMysql.getGrapeByHabitIdx(habitIdx);//�ε��� ��ȣ ����
      SeparateGrape[] separateGrapes = new SeparateGrape[grapeList.size()];//[grape���̺� ī��η�Ƽ]

      for (int j = 0; j < grapeList.size(); j++) {//����Ǿ��ִ� �迭 ũ����� �ݺ�
        Map grape = grapeList.get(j);//�迭�� ����� ���� ����
        int grapeIdx = (int) grape.get("grape_idx");//key�� ���� value���� ����
        String state = (String) grape.get("state");
        String diaryContent = (String) grape.get("diary_content");
        separateGrapes[j] = new SeparateGrape(habitIdx, grapeIdx, state, diaryContent);//SeparateGrape��ü ����
      }
      // MainGrape �г��� ������ ��, �ǿ� �߰���Ų��.
      MainGrape mainpanel = new MainGrape(habitIdx, habitTitle, days, withoutDays, separateGrapes, rewardItems, createAt);
      tabbedpane.add(habitTitle, mainpanel);//�� �߰�(�� �̸�, �� ����)
      num++; //�� num���� 
    }
  }

  public boolean newHabit(String habitName, int days, int withoutDays, Object[][] rewardItems) {
    // TODO Auto-generated method stub
    if (num < nextnum) {// tabbedpane�� �ε��� �߰�
      tabbedpane.add(habitName, new MainGrape(habitName, days, withoutDays, rewardItems));
      num++;
    } else {
      // �ִ� ���������� ������ �Ѿ��� ���, ��� �޼��� ���.
      JOptionPane.showMessageDialog(null, "6�������� �ε����� ����� �ֽ��ϴ�.�ε����� �����ϰ� �ٽ� �����Ͻÿ�.", "����", JOptionPane.WARNING_MESSAGE);
    }
    return true;
  }


  @Override
  public void actionPerformed(ActionEvent e) {

    if (e.getSource() == insertbutton) {
      // �߰� ��ư�̸�, HabitSettings Frame�� ȣ���Ѵ�.
      // HabitSetting Frame���� newHabit �޼��带 ȣ���ؾ� �ϱ� ������, ���� ��ü�� �Ķ���ͷ� �־��ش�.
      new HabitSettings(this);

    } else if (e.getSource() == deletebutton) {
      // ������ư�̸�, �ش� �����Ǵ� �ǿ� �ش��ϴ� habit idx ���� �����ͼ� DB���� �����Ѵ�.
      // DB���� ���� �����ϸ�, �ǿ��� �����Ѵ�.
      MainGrape selected = (MainGrape) tabbedpane.getSelectedComponent();
      int habitIdx = selected.getHabitIdx();
      if(CusMysql.deleteHabitByIdx(habitIdx) == 0){
        tabbedpane.remove(selected);
        num--;
      }

    }
  }
}