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

  // 탭 추가 버튼
  JButton insertbutton;
  // 탭 삭제 버튼
  JButton deletebutton;
  // 탭
  JTabbedPane tabbedpane;
  // 탭의 현재 갯수
  int num = 1;
  // 탭의 최대 갯수
  int nextnum = 7;

  public TabbedPaneFrame() {


    setTitle("습관만들기");
    setSize(1600, 1400);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPanel toppanel = new JPanel(new BorderLayout());

    JPanel buttonpanel = new JPanel();

    // 각각 버튼을 할당해주고, 액션 리스너를 추가한다.
    insertbutton = new JButton("인덱스 추가");
    insertbutton.addActionListener(this);
    deletebutton = new JButton("인덱스 삭제");
    deletebutton.addActionListener(this);
    buttonpanel.add(insertbutton);
    buttonpanel.add(deletebutton);


    // 탭을 추가시킨다.
    tabbedpane = new JTabbedPane();
    toppanel.add(tabbedpane, BorderLayout.CENTER);
    toppanel.add(buttonpanel, BorderLayout.NORTH);

    add(toppanel);

    // 만약, 기존에 만들어진 인덱스가 있다면 로드한다.
    loadData();

    setVisible(true);
  }

  private void loadData() {
	  
    // habit 정보에 대한 것을 DB에서 읽어온다.
    ArrayList habits = CusMysql.getHabitList();
    for (int i = 0; i < habits.size(); i++) {//저장되어있는 배열 크기까지 반복
      Map habit = (Map) habits.get(i);//배열에 저장된 값을 얻어옴

      int habitIdx = (int) habit.get("habit_idx");//key에 대한 value값을 얻어옴
      String habitTitle = (String) habit.get("title");
      int days = (int) habit.get("days");
      int withoutDays = (int) habit.get("without_days");
      String createAt = (String) habit.get("create_at");

      // 보상 정보에 대한 것을 DB에서 읽어온다.
      // 그 뒤, 현재 사용하는 데이터 형태에 맞게 변환한다.
      ArrayList<Map> rewardList = CusMysql.getRewardByHabitIdx(habitIdx);//인덱스 번호 전달
      Object[][] rewardItems = new Object[rewardList.size()][2]; //[reward테이블 카디널러티][에트리뷰트]

      for (int j = 0; j < rewardList.size(); j++) {//저장되어있는 배열 크기까지 반복
        rewardItems[j][0] = j + 1;//1열
        rewardItems[j][1] = rewardList.get(j).get("content");//2열
      }

      // 포도알 정보에 대한 것을 DB에서 읽어온다.
      // 그뒤, 포도알 객체를 생성한다.
      ArrayList<Map> grapeList = CusMysql.getGrapeByHabitIdx(habitIdx);//인덱스 번호 전달
      SeparateGrape[] separateGrapes = new SeparateGrape[grapeList.size()];//[grape테이블 카디널러티]

      for (int j = 0; j < grapeList.size(); j++) {//저장되어있는 배열 크기까지 반복
        Map grape = grapeList.get(j);//배열에 저장된 값을 얻어옴
        int grapeIdx = (int) grape.get("grape_idx");//key에 대한 value값을 얻어옴
        String state = (String) grape.get("state");
        String diaryContent = (String) grape.get("diary_content");
        separateGrapes[j] = new SeparateGrape(habitIdx, grapeIdx, state, diaryContent);//SeparateGrape객체 생성
      }
      // MainGrape 패널을 생성한 뒤, 탭에 추가시킨다.
      MainGrape mainpanel = new MainGrape(habitIdx, habitTitle, days, withoutDays, separateGrapes, rewardItems, createAt);
      tabbedpane.add(habitTitle, mainpanel);//탭 추가(탭 이름, 탭 내용)
      num++; //탭 num증가 
    }
  }

  public boolean newHabit(String habitName, int days, int withoutDays, Object[][] rewardItems) {
    // TODO Auto-generated method stub
    if (num < nextnum) {// tabbedpane의 인덱스 추가
      tabbedpane.add(habitName, new MainGrape(habitName, days, withoutDays, rewardItems));
      num++;
    } else {
      // 최대 생성가능한 개수를 넘었을 경우, 경고 메세지 출력.
      JOptionPane.showMessageDialog(null, "6개까지의 인덱스를 만들수 있습니다.인덱스를 삭제하고 다시 생성하시요.", "실패", JOptionPane.WARNING_MESSAGE);
    }
    return true;
  }


  @Override
  public void actionPerformed(ActionEvent e) {

    if (e.getSource() == insertbutton) {
      // 추가 버튼이면, HabitSettings Frame을 호출한다.
      // HabitSetting Frame에서 newHabit 메서드를 호출해야 하기 때문에, 현재 객체를 파라미터로 넣어준다.
      new HabitSettings(this);

    } else if (e.getSource() == deletebutton) {
      // 삭제버튼이면, 해당 삭제되는 탭에 해당하는 habit idx 값을 가져와서 DB에서 삭제한다.
      // DB에서 삭제 성공하면, 탭에서 삭제한다.
      MainGrape selected = (MainGrape) tabbedpane.getSelectedComponent();
      int habitIdx = selected.getHabitIdx();
      if(CusMysql.deleteHabitByIdx(habitIdx) == 0){
        tabbedpane.remove(selected);
        num--;
      }

    }
  }
}