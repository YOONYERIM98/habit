package grape;

import ResultFrame.ResultFrame;
import models.CusMysql;
import reward.RewardPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainGrape extends JPanel implements ActionListener {

  int[] xPoint;


  static final ImageIcon rootImage = new ImageIcon(new ImageIcon("./image/image3.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));//가지
  JButton[] grapeButtons;
  SeparateGrape[] separateGrapes;
  JLabel[] treeGrapes;

  private String title;
  private boolean isFinish = false;
  private int days;
  private int withOutDays;
  final private String create_at;
  final private Object[][] rewardItems;

  final private int habitIdx;

  static final ImageIcon tree = new ImageIcon("./image/tree.png");
  static final ImageIcon farmer = new ImageIcon("./image/farmer.png");
  static final ImageIcon success = new ImageIcon("./image/성공.png");
  static final ImageIcon fail = new ImageIcon("./image/실패.png");
  static final ImageIcon treeGrape = new ImageIcon(new ImageIcon("./image/grape.png").getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));

  // 새로 생성하는 MainGrape 패널을 위한 생성자.
  public MainGrape(String title, int days, int withOutDays, Object[][] rewardItems) {
    this.title = title;
    this.days = days;
    this.withOutDays = withOutDays;
    this.rewardItems = rewardItems;
    this.habitIdx = CusMysql.createHabits(title, days, withOutDays, rewardItems);
    // 리워드 보상창을 붙인다.
    attachReward();
    // 지정된 날짜에 따라, 달리 호출되어야 하는 동작들을 호출 및 변수를 초기화한다.
    if (days == 30) {
      xPoint = get30Data();
      treeGrapes = new JLabel[days / 6];
      separateGrapes = new SeparateGrape[days];
      grapeButtons = new JButton[days];
    } else {
      xPoint = get66Data();
      treeGrapes = new JLabel[days / 6];
      separateGrapes = new SeparateGrape[days];
      grapeButtons = new JButton[days];
    }
    // 각 이미지들의 위치를 직접 지정해 주기 위해, layout 을 null로 지정해준다.
    setLayout(null);
    // 포도송이의 줄기에 해당하는 부분 현재 패널에 추가한다.
    JLabel label = new JLabel(rootImage);
    label.setBounds(385, 45, 150, 100);
    add(label);

    // days에 따른 포도알을 붙인다.
    if (days == 30) {
      fill30GrapeButton();
    } else {
      fill66GrapeButton();
    }

    // 포도가 맺힐 포도나무를 패널에 추가한다.
    JPanel innerPanel = new JPanel() { 
      public void paintComponent(Graphics g) {
        g.drawImage(tree.getImage(), 0, 0, 400, 500, null);
      }
    };
    innerPanel.setBounds(900, 400, 400, 500);

    // 포도나무에 맺히는 포도들의 위치를 미리 지정 및 추가한다.
    if (days == 30) {
      batch30TreeGrape();
    } else {
      batch66TreeGrape();
    }
    add(innerPanel);//나무 해당하는 부분 현재 패널에 추가한다.

    // 농부 이미지를 현재 패널에 추가한다.
    JPanel farmerPanel = new JPanel() {
      public void paintComponent(Graphics g) {
        g.drawImage(farmer.getImage(), 0, 0, 400, 300, null);
      }
    };
    farmerPanel.setBounds(1200, 600, 400, 300);
    add(farmerPanel);

    //만든 날짜를 저장한다.
    SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
    Date time = new Date();
    create_at = format2.format(time);

    // 계획 일수 및 빠져도 되는 일수에 대한 정보를 현재 패널에 추가한다.
    daysPanel();
  }

  // 데이터를 DB에서 불러올경우 호출되는 생성자. 기본적인 틀은 위의 생성자와 비슷하다.
  // 아래 생성자에선 다른 부분만 주석을 추가함.
  public MainGrape(int habitIdx, String title, int days, int withOutDays, SeparateGrape[] grapes, Object[][] rewardItems, String create_at) {
    this.title =title;
    this.days = days;
    this.withOutDays = withOutDays;
    this.rewardItems = rewardItems;
    this.habitIdx = habitIdx;
    this.create_at = create_at;
    attachReward();// 리워드 보상창을 붙인다.
    if (days == 30) {
      xPoint = get30Data();//포도알 위치 저장
      treeGrapes = new JLabel[days / 6];
      separateGrapes = grapes;//포도알 이미지
      grapeButtons = new JButton[days];//30포도알 버튼 생성
    } else {
      xPoint = get66Data();//포도알 위치 저장
      treeGrapes = new JLabel[days / 6];
      separateGrapes = grapes;//포도알 이미지
      grapeButtons = new JButton[days];//66포도알 버튼 생성
    }
    setLayout(null);
    // 포도송이의 줄기에 해당하는 부분 현재 패널에 추가한다.
    JLabel label = new JLabel(rootImage);
    label.setBounds(385, 45, 150, 100);
    add(label);//패널에 줄기 추가
    if (days == 30) {
      fill30GrapeButton();//fill30GrapeButton()메소드 포도알 그리기
    } else {
      fill66GrapeButton();//fill66GrapeButton()메소드 포도알 그리기
    }
    // 포도가 맺힐 포도나무를 패널에 추가한다.
    JPanel innerPanel = new JPanel() {
      public void paintComponent(Graphics g) {
        g.drawImage(tree.getImage(), 0, 0, 400, 500, null);
      }
    };

    innerPanel.setBounds(900, 400, 400, 500);
    //포도나무에 포도의 위치를 미리 지정한다.
    if (days == 30) {
      batch30TreeGrape();
    } else {
      batch66TreeGrape();
    }
    add(innerPanel);
    
    // 농부 이미지를 현재 패널에 추가한다.
    JPanel farmerPanel = new JPanel() {
      public void paintComponent(Graphics g) {
        g.drawImage(farmer.getImage(), 0, 0, 400, 300, null);
      }
    };
    farmerPanel.setBounds(1200, 600, 400, 300);
    add(farmerPanel);

    try {
      // 현재 프로그램을 껏다 켠 상태이므로, 실패한 일수가 늘어나 있을 수 있으므로, 실패한 날짜에 대한 포도알정보를 추가한다.
      failGrapeProcess();
    } catch (Exception e) {
      e.printStackTrace();
    }
    // 현재 트리가 성공한 트리인지 체크하고, 해당 패널을 부착하는 메서드를 호출한다.
    treeGrape();
    // 현재 트리가 실패한 것인지 체크하고, 실패 패널을 부착하는 메서드를 호출한다.
    checkFail();
    daysPanel();
  }

  public int getHabitIdx() {
    return habitIdx;
  }

  // 보상 정보에 대한 것을 부착하는 메서드
  private void attachReward() {
    RewardPanel panel = new RewardPanel(rewardItems);
    panel.setBounds(940, 10, 400, 300);
    this.add(panel);
  }

  // 30일 계획에 대한 포도알의 위치를 지정 및 각각의 데이터를 선언 및 지정해준다.
  private void fill30GrapeButton() {
    for (int i = 0; i < 30; i++) {
      // 만약, 대상 포도알이 비었을 경우, 새로 만들어준다.
      // 만약, 대상 포도알이 이미 존재할 경우, 현재 객체를 대상의 parentMainGrape로 지정해준다.
      if (separateGrapes[i] == null) {
        separateGrapes[i] = new SeparateGrape(this.habitIdx, i, this);//SeparateGrape객체 생성
      } else {
        separateGrapes[i].setParentMainGrape(this);
      }
      SeparateGrape separateGrape = separateGrapes[i];
      grapeButtons[i] = new JButton(); //포도알 버튼 생성해서
      grapeButtons[i].setIcon(separateGrape.icon); //저장된 이미지를 붙여준다
      grapeButtons[i].setSize(115, 115);

      // 만약, 현재 포도송이의 상태가 fail일 경우, 액션 리스너를 부착하지 않고, 그 외 상태라면 액션 리스너를 부착한다.
      if (!separateGrape.state.equals("fail")) {//separateGrape의 state상태가 빠진날이 아니라면
        grapeButtons[i].addActionListener(separateGrape);
      }
      grapeButtons[i].setBorderPainted(false); // JButton의 Border(외곽선)을 없애준다.
      grapeButtons[i].setContentAreaFilled(false); // JButton의 내용영역 채루기 않함
      grapeButtons[i].setFocusPainted(false); // JButton이 선택(focus)되었을때 생기는 테두리 사용안함
      add(grapeButtons[i]);
      if (i < 4) {
        grapeButtons[i].setLocation(xPoint[i], 100);
      } else if (i < 9) {
        grapeButtons[i].setLocation(xPoint[i], 170);
      } else if (i < 15) {
        grapeButtons[i].setLocation(xPoint[i], 240);
      } else if (i < 20) {
        grapeButtons[i].setLocation(xPoint[i], 310);
      } else if (i < 24) {
        grapeButtons[i].setLocation(xPoint[i], 380);
      } else if (i < 27) {
        grapeButtons[i].setLocation(xPoint[i], 450);
      } else if (i < 29) {
        grapeButtons[i].setLocation(xPoint[i], 520);
      } else {
        grapeButtons[i].setLocation(xPoint[i], 590);
      }
    }
  }

  // 66일 계획에 대한 포도알의 위치를 지정 및 각각의 데이터를 선언 및 지정해준다.
  private void fill66GrapeButton() {
    for (int i = 0; i < 66; i++) {
      // 만약, 대상 포도알이 비었을 경우, 새로 만들어준다.
      // 만약, 대상 포도알이 이미 존재할 경우, 현재 객체를 대상의 parentMainGrape로 지정해준다.
      if (separateGrapes[i] == null) {
        separateGrapes[i] = new SeparateGrape(this.habitIdx, i, this);
      } else {
        separateGrapes[i].setParentMainGrape(this);
      }
      SeparateGrape separateGrape = separateGrapes[i];
      grapeButtons[i] = new JButton();
      grapeButtons[i].setIcon(separateGrape.icon);
      grapeButtons[i].setSize(115, 115);
      // 만약, 현재 포도송이의 상태가 fail일 경우, 액션 리스너를 부착하지 않고, 그 외 상태라면 액션 리스너를 부착한다.
      if (!separateGrape.state.equals("fail")) {
        grapeButtons[i].addActionListener(separateGrape);
      }
      grapeButtons[i].setBorderPainted(false); // JButton의 Border(외곽선)을 없애준다.
      grapeButtons[i].setContentAreaFilled(false); // JButton의 내용영역 채루기 않함
      grapeButtons[i].setFocusPainted(false); // JButton이 선택(focus)되었을때 생기는 테두리 사용안함
      add(grapeButtons[i]);
      if (i < 9)
        grapeButtons[i].setLocation(xPoint[i], 100);
      else if (i < 19)
        grapeButtons[i].setLocation(xPoint[i], 170);
      else if (i < 28)
        grapeButtons[i].setLocation(xPoint[i], 240);
      else if (i < 36)
        grapeButtons[i].setLocation(xPoint[i], 310);
      else if (i < 43)
        grapeButtons[i].setLocation(xPoint[i], 380);
      else if (i < 49)
        grapeButtons[i].setLocation(xPoint[i], 450);
      else if (i < 54)
        grapeButtons[i].setLocation(xPoint[i], 520);
      else if (i < 58)
        grapeButtons[i].setLocation(xPoint[i], 590);
      else if (i < 61)
        grapeButtons[i].setLocation(xPoint[i], 660);
      else if (i < 63)
        grapeButtons[i].setLocation(xPoint[i], 730);
      else
        grapeButtons[i].setLocation(xPoint[i], 800);
    }
  }

  // 30일 계획에 대한 포도나무에 포도의 위치를 미리 지정한다.
  private void batch30TreeGrape() {
    int i = 0;
    // 1
    treeGrapes[i] = new JLabel();
    treeGrapes[i].setBounds(990, 450, 300, 400);
    add(treeGrapes[i]);
    i++;
    // 2
    treeGrapes[i] = new JLabel();
    treeGrapes[i].setBounds(1010, 260, 400, 500);
    add(treeGrapes[i]);
    i++;
    // 3
    treeGrapes[i] = new JLabel();
    treeGrapes[i].setBounds(1060, 330, 400, 500);
    add(treeGrapes[i]);
    i++;
    // 4
    treeGrapes[i] = new JLabel();
    treeGrapes[i].setBounds(1110, 400, 400, 500);
    add(treeGrapes[i]);
    i++;
    // 5
    treeGrapes[i] = new JLabel();
    treeGrapes[i].setBounds(1140, 300, 400, 500);
    add(treeGrapes[i]);
  }

  // 66일 계획에 대한 포도나무에 포도의 위치를 미리 지정한다.
  private void batch66TreeGrape() {
    int i = 0;
    // 1
    treeGrapes[i] = new JLabel();
    treeGrapes[i].setBounds(1055, 270, 300, 400);
    add(treeGrapes[i]);
    i++;
    // 2
    treeGrapes[i] = new JLabel();
    treeGrapes[i].setBounds(1010, 270, 400, 500);
    add(treeGrapes[i]);
    i++;
    // 3
    treeGrapes[i] = new JLabel();
    treeGrapes[i].setBounds(1110, 270, 400, 500);
    add(treeGrapes[i]);
    i++;
    // 4
    treeGrapes[i] = new JLabel();
    treeGrapes[i].setBounds(990, 350, 400, 500);
    add(treeGrapes[i]);
    i++;
    // 5
    treeGrapes[i] = new JLabel();
    treeGrapes[i].setBounds(1050, 310, 400, 500);
    add(treeGrapes[i]);
    i++;
    // 6
    treeGrapes[i] = new JLabel();
    treeGrapes[i].setBounds(1110, 340, 400, 500);
    add(treeGrapes[i]);
    i++;
    // 7
    treeGrapes[i] = new JLabel();
    treeGrapes[i].setBounds(1160, 320, 400, 500);
    add(treeGrapes[i]);
    i++;
    // 8
    treeGrapes[i] = new JLabel();
    treeGrapes[i].setBounds(990, 430, 400, 500);
    add(treeGrapes[i]);
    i++;
    // 9
    treeGrapes[i] = new JLabel();
    treeGrapes[i].setBounds(1040, 380, 400, 500);
    add(treeGrapes[i]);
    i++;
    // 10
    treeGrapes[i] = new JLabel();
    treeGrapes[i].setBounds(1100, 400, 400, 500);
    add(treeGrapes[i]);
    i++;
    // 11
    treeGrapes[i] = new JLabel();
    treeGrapes[i].setBounds(1160, 430, 400, 500);
    add(treeGrapes[i]);
  }

  private int[] get30Data() {//포도알 위치
    return new int[]{
        300, 370, 440, 510,
        265, 335, 405, 475, 545,
        230, 300, 370, 440, 510, 580,
        265, 335, 405, 475, 545,
        300, 370, 440, 510,
        335, 405, 475,
        370, 440,
        405};
  }

  private int[] get66Data() {
    return new int[]{
        180, 250, 320, 390, 460, 530, 600, 670, 740,
        145, 215, 285, 355, 425, 495, 565, 635, 705, 775,
        180, 250, 320, 390, 460, 530, 600, 670, 740,
        210, 280, 350, 420, 490, 560, 630, 700,
        240, 310, 380, 450, 520, 590, 660,
        270, 340, 410, 480, 550, 620,
        300, 370, 440, 510, 580,
        330, 400, 470, 540,
        360, 430, 500,
        390, 460,
        420, 600, 700};
  }

  // 현재 몇칸을 채웠는지 체크하여, 6의 배수 칸에 대해 fillFarmer 아이콘으로 지정하거나, 마지막 칸이라면, fillLatest로 상태를 지정해주는 메서드.
  // 포도알을 채울 때, 어떤 상태로 채워야되는지 여기서 판단함.
  public String fillTreeAndReturnState(SeparateGrape clickedSeparateGrape) {
    int fillCount = 0;
    int currentGrapeIdx = 0;
    for (int i = 0; i < separateGrapes.length; i++) {
      if (separateGrapes[i].isFill()) {
        fillCount++;
      }
      if (separateGrapes[i] == clickedSeparateGrape) {
        currentGrapeIdx = i;
      }
    }
    if (separateGrapes[currentGrapeIdx].isFill()) {
      return separateGrapes[currentGrapeIdx].state;
    }
    String state = "fill";
    fillCount++;
    if (fillCount % 6 == 0) {
      state = "fillFarmer";
      treeGrapes[fillCount / 6 - 1].setIcon(treeGrape);
    }
    if (currentGrapeIdx == separateGrapes.length - 1) {
      state = "fillLatest";
    }
    return state;
  }

  // 현재 habit에 성공 이미지 패널을 부착시킨다.
  private void addSuccessPanel() {

    JPanel successPanel = new JPanel() {
      public void paintComponent(Graphics g) {
        g.drawImage(success.getImage(), 0, 0, 400, 500, null);
      }
    };
    successPanel.setBounds(1200, 200, 400, 500);
    add(successPanel);
  }

  // 현재 채워진 갯수를 가져와서, 포도나무에 포도를 추가해주는 메서드이다.
  // 만약, 포도나무가 전부 다 채워졌으면 성공 패널을 부착한다.
  private void treeGrape() {
    int fillCount = 0;
    for (int i = 0; i < separateGrapes.length; i++) {
      if (separateGrapes[i].isFill()) {
        fillCount++;
      }
    }

    int treeGrapeFillCount = 0;
    while (fillCount >= 6) {
      treeGrapes[fillCount / 6 - 1].setIcon(treeGrape);
      treeGrapeFillCount++;
      fillCount -= 6;
    }

    if (days == 30 && treeGrapeFillCount == 5 || days == 66 && treeGrapeFillCount == 11) {
      addSuccessPanel();
    }
  }

  // 포도알 클릭 이후, 아이콘을 변경시켜주는 메서드이다.
  // 다른 클래스에서 호출한다.
  public void clickedAfter(SeparateGrape clickedSeparateGrape) {
    for (int i = 0; i < separateGrapes.length; i++) {
      if (separateGrapes[i] == clickedSeparateGrape) {
        grapeButtons[i].setIcon(separateGrapes[i].icon);
      }
    }
    checkFinish();
  }

  // 실패 여부를 체크한다. 만약, 실패가 휴일보다 많다면, 실패로 간주하고 현재 habit을 finish 된것으로 지정한다.
  private void checkFail() {
    int fail = 0;
    for (int i = 0; i < separateGrapes.length; i++) {
      if (separateGrapes[i].state.equals("fail")) {
        fail++;
      }
    }
    if (fail > withOutDays) {
      isFinish = true;
    }
  }

  // 일수, 빠져도 되는 날 정보에 대한 패널을 부착하는 메서드
  private void daysPanel() {
    JPanel daysPanel = new JPanel(new GridLayout(1, 3));
    JLabel label = new JLabel(this.title);
    label.setFont(new Font(label.getFont().getName(), label.getFont().getStyle(), 20));
    daysPanel.add(label);
    label = new JLabel("총 일수:" + String.valueOf(this.days));
    label.setFont(new Font(label.getFont().getName(), label.getFont().getStyle(), 20));
    daysPanel.add(label);
    label = new JLabel("빠져도 되는 날:" + String.valueOf(this.withOutDays));
    label.setFont(new Font(label.getFont().getName(), label.getFont().getStyle(), 20));
    daysPanel.add(label);
    daysPanel.setBounds(0, 0, 1000, 40);
    add(daysPanel);
  }

  // 실패 이미지를 패널에 부착하는 메서드
  private void addFailPanel() {
    JPanel failPanel = new JPanel() {
      public void paintComponent(Graphics g) {
        g.drawImage(fail.getImage(), 0, 0, 400, 500, null);
      }
    };
    failPanel.setBounds(1200, 200, 400, 500);
    add(failPanel);
  }

  private void failGrapeProcess() throws ParseException {// 이해가 안감 혜정아 너가 해석해봐
    int fail = 0;
    int fillCnt = 0;
    // 포도알의 총 fail 갯수에 대해 가져온다.
    for (int i = 0; i < separateGrapes.length; i++) {
      if (separateGrapes[i].state.equals("fail")) {//separateGrape의 state상태가 fail이라면(빠진날)
        fail++;
      }
      if(separateGrapes[i].isFill()){//!!!!!!!!!!!!!!!!!!!!!!혜정아 이게 뭔지 모르겠어!!!!!!!!!!!!!!!!!!!!!!!!!
        fillCnt++;
      }
    }

    // 현재 날짜와 최초 생성일자 사이의 날짜차를 구한다.
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    Date currentDate = new Date();
    Date createAt = sdf.parse(create_at);//?? 이따 물어봐야지
    long diffInMillies = currentDate.getTime() - createAt.getTime();
    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

    // 만약, fail이 diff 보다 작다면, 그 차이만큼 포도알의 상태를 fail로 업데이트한다.
    if (fillCnt < diff) {
      for (int i = 0; i < separateGrapes.length; i++) {
        if (separateGrapes[i].state.equals("empty")) {
          fail++;
          fillCnt++;
          separateGrapes[i].setState("fail");
          grapeButtons[i].setIcon(separateGrapes[i].icon);
          grapeButtons[i].addActionListener((actionEvent) -> {
          });

          if (fillCnt >= diff) break;
        }
      }
    }
    // 만약, fail이 휴식일보다 더 많다면, failPaenl을 부착한다.
    if (fail > withOutDays) {
      isFinish = true;
      addFailPanel();
    }
  }

  // 현재 habit을 finish된 상태로 변경시키고, 결과물 프레임을 띄운다.
  private void finishDo(int failNum) {
    isFinish = true;
    new ResultFrame(days, failNum);
    addSuccessPanel();
  }

  // 모든 포도알이 채워졌는지 체크한다.
  private void checkFinish() {
    if (isFinish) {
      return;
    }

    int isEmpty = 0;
    int fail = 0;
    for (int i = 0; i < separateGrapes.length; i++) {
      if (separateGrapes[i].state.equals("empty")) {
        isEmpty++;
      } else if (separateGrapes[i].state.equals("fail")) {
        fail++;
      }
    }
    if (isEmpty == 0) {
      finishDo(fail);
    }
  }

  public boolean canFill(){
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    Date currentDate = new Date();
    Date createAt = null;
    try {
      createAt = sdf.parse(create_at);
    } catch (ParseException ex) {
      ex.printStackTrace();
    }
    long diffInMillies = currentDate.getTime() - createAt.getTime();
    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

    int currentFilled = 0;
    for (int i = 0; i < separateGrapes.length; i++) {
      if (separateGrapes[i].state.equals("fail")) {
        currentFilled++;
      }else if (separateGrapes[i].isFill()){
        currentFilled++;
      }
    }
    if(currentFilled <= diff){
      return true;
    }else{
      return false;
    }
  }


  @Override
  public void actionPerformed(ActionEvent e) {

  }
}
