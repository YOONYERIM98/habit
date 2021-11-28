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


  static final ImageIcon rootImage = new ImageIcon(new ImageIcon("./image/image3.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));//����
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
  static final ImageIcon success = new ImageIcon("./image/����.png");
  static final ImageIcon fail = new ImageIcon("./image/����.png");
  static final ImageIcon treeGrape = new ImageIcon(new ImageIcon("./image/grape.png").getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));

  // ���� �����ϴ� MainGrape �г��� ���� ������.
  public MainGrape(String title, int days, int withOutDays, Object[][] rewardItems) {
    this.title = title;
    this.days = days;
    this.withOutDays = withOutDays;
    this.rewardItems = rewardItems;
    this.habitIdx = CusMysql.createHabits(title, days, withOutDays, rewardItems);
    // ������ ����â�� ���δ�.
    attachReward();
    // ������ ��¥�� ����, �޸� ȣ��Ǿ�� �ϴ� ���۵��� ȣ�� �� ������ �ʱ�ȭ�Ѵ�.
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
    // �� �̹������� ��ġ�� ���� ������ �ֱ� ����, layout �� null�� �������ش�.
    setLayout(null);
    // ���������� �ٱ⿡ �ش��ϴ� �κ� ���� �гο� �߰��Ѵ�.
    JLabel label = new JLabel(rootImage);
    label.setBounds(385, 45, 150, 100);
    add(label);

    // days�� ���� �������� ���δ�.
    if (days == 30) {
      fill30GrapeButton();
    } else {
      fill66GrapeButton();
    }

    // ������ ���� ���������� �гο� �߰��Ѵ�.
    JPanel innerPanel = new JPanel() { 
      public void paintComponent(Graphics g) {
        g.drawImage(tree.getImage(), 0, 0, 400, 500, null);
      }
    };
    innerPanel.setBounds(900, 400, 400, 500);

    // ���������� ������ �������� ��ġ�� �̸� ���� �� �߰��Ѵ�.
    if (days == 30) {
      batch30TreeGrape();
    } else {
      batch66TreeGrape();
    }
    add(innerPanel);//���� �ش��ϴ� �κ� ���� �гο� �߰��Ѵ�.

    // ��� �̹����� ���� �гο� �߰��Ѵ�.
    JPanel farmerPanel = new JPanel() {
      public void paintComponent(Graphics g) {
        g.drawImage(farmer.getImage(), 0, 0, 400, 300, null);
      }
    };
    farmerPanel.setBounds(1200, 600, 400, 300);
    add(farmerPanel);

    //���� ��¥�� �����Ѵ�.
    SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
    Date time = new Date();
    create_at = format2.format(time);

    // ��ȹ �ϼ� �� ������ �Ǵ� �ϼ��� ���� ������ ���� �гο� �߰��Ѵ�.
    daysPanel();
  }

  // �����͸� DB���� �ҷ��ð�� ȣ��Ǵ� ������. �⺻���� Ʋ�� ���� �����ڿ� ����ϴ�.
  // �Ʒ� �����ڿ��� �ٸ� �κи� �ּ��� �߰���.
  public MainGrape(int habitIdx, String title, int days, int withOutDays, SeparateGrape[] grapes, Object[][] rewardItems, String create_at) {
    this.title =title;
    this.days = days;
    this.withOutDays = withOutDays;
    this.rewardItems = rewardItems;
    this.habitIdx = habitIdx;
    this.create_at = create_at;
    attachReward();// ������ ����â�� ���δ�.
    if (days == 30) {
      xPoint = get30Data();//������ ��ġ ����
      treeGrapes = new JLabel[days / 6];
      separateGrapes = grapes;//������ �̹���
      grapeButtons = new JButton[days];//30������ ��ư ����
    } else {
      xPoint = get66Data();//������ ��ġ ����
      treeGrapes = new JLabel[days / 6];
      separateGrapes = grapes;//������ �̹���
      grapeButtons = new JButton[days];//66������ ��ư ����
    }
    setLayout(null);
    // ���������� �ٱ⿡ �ش��ϴ� �κ� ���� �гο� �߰��Ѵ�.
    JLabel label = new JLabel(rootImage);
    label.setBounds(385, 45, 150, 100);
    add(label);//�гο� �ٱ� �߰�
    if (days == 30) {
      fill30GrapeButton();//fill30GrapeButton()�޼ҵ� ������ �׸���
    } else {
      fill66GrapeButton();//fill66GrapeButton()�޼ҵ� ������ �׸���
    }
    // ������ ���� ���������� �гο� �߰��Ѵ�.
    JPanel innerPanel = new JPanel() {
      public void paintComponent(Graphics g) {
        g.drawImage(tree.getImage(), 0, 0, 400, 500, null);
      }
    };

    innerPanel.setBounds(900, 400, 400, 500);
    //���������� ������ ��ġ�� �̸� �����Ѵ�.
    if (days == 30) {
      batch30TreeGrape();
    } else {
      batch66TreeGrape();
    }
    add(innerPanel);
    
    // ��� �̹����� ���� �гο� �߰��Ѵ�.
    JPanel farmerPanel = new JPanel() {
      public void paintComponent(Graphics g) {
        g.drawImage(farmer.getImage(), 0, 0, 400, 300, null);
      }
    };
    farmerPanel.setBounds(1200, 600, 400, 300);
    add(farmerPanel);

    try {
      // ���� ���α׷��� ���� �� �����̹Ƿ�, ������ �ϼ��� �þ ���� �� �����Ƿ�, ������ ��¥�� ���� ������������ �߰��Ѵ�.
      failGrapeProcess();
    } catch (Exception e) {
      e.printStackTrace();
    }
    // ���� Ʈ���� ������ Ʈ������ üũ�ϰ�, �ش� �г��� �����ϴ� �޼��带 ȣ���Ѵ�.
    treeGrape();
    // ���� Ʈ���� ������ ������ üũ�ϰ�, ���� �г��� �����ϴ� �޼��带 ȣ���Ѵ�.
    checkFail();
    daysPanel();
  }

  public int getHabitIdx() {
    return habitIdx;
  }

  // ���� ������ ���� ���� �����ϴ� �޼���
  private void attachReward() {
    RewardPanel panel = new RewardPanel(rewardItems);
    panel.setBounds(940, 10, 400, 300);
    this.add(panel);
  }

  // 30�� ��ȹ�� ���� �������� ��ġ�� ���� �� ������ �����͸� ���� �� �������ش�.
  private void fill30GrapeButton() {
    for (int i = 0; i < 30; i++) {
      // ����, ��� �������� ����� ���, ���� ������ش�.
      // ����, ��� �������� �̹� ������ ���, ���� ��ü�� ����� parentMainGrape�� �������ش�.
      if (separateGrapes[i] == null) {
        separateGrapes[i] = new SeparateGrape(this.habitIdx, i, this);//SeparateGrape��ü ����
      } else {
        separateGrapes[i].setParentMainGrape(this);
      }
      SeparateGrape separateGrape = separateGrapes[i];
      grapeButtons[i] = new JButton(); //������ ��ư �����ؼ�
      grapeButtons[i].setIcon(separateGrape.icon); //����� �̹����� �ٿ��ش�
      grapeButtons[i].setSize(115, 115);

      // ����, ���� ���������� ���°� fail�� ���, �׼� �����ʸ� �������� �ʰ�, �� �� ���¶�� �׼� �����ʸ� �����Ѵ�.
      if (!separateGrape.state.equals("fail")) {//separateGrape�� state���°� �������� �ƴ϶��
        grapeButtons[i].addActionListener(separateGrape);
      }
      grapeButtons[i].setBorderPainted(false); // JButton�� Border(�ܰ���)�� �����ش�.
      grapeButtons[i].setContentAreaFilled(false); // JButton�� ���뿵�� ä��� ����
      grapeButtons[i].setFocusPainted(false); // JButton�� ����(focus)�Ǿ����� ����� �׵θ� ������
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

  // 66�� ��ȹ�� ���� �������� ��ġ�� ���� �� ������ �����͸� ���� �� �������ش�.
  private void fill66GrapeButton() {
    for (int i = 0; i < 66; i++) {
      // ����, ��� �������� ����� ���, ���� ������ش�.
      // ����, ��� �������� �̹� ������ ���, ���� ��ü�� ����� parentMainGrape�� �������ش�.
      if (separateGrapes[i] == null) {
        separateGrapes[i] = new SeparateGrape(this.habitIdx, i, this);
      } else {
        separateGrapes[i].setParentMainGrape(this);
      }
      SeparateGrape separateGrape = separateGrapes[i];
      grapeButtons[i] = new JButton();
      grapeButtons[i].setIcon(separateGrape.icon);
      grapeButtons[i].setSize(115, 115);
      // ����, ���� ���������� ���°� fail�� ���, �׼� �����ʸ� �������� �ʰ�, �� �� ���¶�� �׼� �����ʸ� �����Ѵ�.
      if (!separateGrape.state.equals("fail")) {
        grapeButtons[i].addActionListener(separateGrape);
      }
      grapeButtons[i].setBorderPainted(false); // JButton�� Border(�ܰ���)�� �����ش�.
      grapeButtons[i].setContentAreaFilled(false); // JButton�� ���뿵�� ä��� ����
      grapeButtons[i].setFocusPainted(false); // JButton�� ����(focus)�Ǿ����� ����� �׵θ� ������
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

  // 30�� ��ȹ�� ���� ���������� ������ ��ġ�� �̸� �����Ѵ�.
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

  // 66�� ��ȹ�� ���� ���������� ������ ��ġ�� �̸� �����Ѵ�.
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

  private int[] get30Data() {//������ ��ġ
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

  // ���� ��ĭ�� ä������ üũ�Ͽ�, 6�� ��� ĭ�� ���� fillFarmer ���������� �����ϰų�, ������ ĭ�̶��, fillLatest�� ���¸� �������ִ� �޼���.
  // �������� ä�� ��, � ���·� ä���ߵǴ��� ���⼭ �Ǵ���.
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

  // ���� habit�� ���� �̹��� �г��� ������Ų��.
  private void addSuccessPanel() {

    JPanel successPanel = new JPanel() {
      public void paintComponent(Graphics g) {
        g.drawImage(success.getImage(), 0, 0, 400, 500, null);
      }
    };
    successPanel.setBounds(1200, 200, 400, 500);
    add(successPanel);
  }

  // ���� ä���� ������ �����ͼ�, ���������� ������ �߰����ִ� �޼����̴�.
  // ����, ���������� ���� �� ä�������� ���� �г��� �����Ѵ�.
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

  // ������ Ŭ�� ����, �������� ��������ִ� �޼����̴�.
  // �ٸ� Ŭ�������� ȣ���Ѵ�.
  public void clickedAfter(SeparateGrape clickedSeparateGrape) {
    for (int i = 0; i < separateGrapes.length; i++) {
      if (separateGrapes[i] == clickedSeparateGrape) {
        grapeButtons[i].setIcon(separateGrapes[i].icon);
      }
    }
    checkFinish();
  }

  // ���� ���θ� üũ�Ѵ�. ����, ���а� ���Ϻ��� ���ٸ�, ���з� �����ϰ� ���� habit�� finish �Ȱ����� �����Ѵ�.
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

  // �ϼ�, ������ �Ǵ� �� ������ ���� �г��� �����ϴ� �޼���
  private void daysPanel() {
    JPanel daysPanel = new JPanel(new GridLayout(1, 3));
    JLabel label = new JLabel(this.title);
    label.setFont(new Font(label.getFont().getName(), label.getFont().getStyle(), 20));
    daysPanel.add(label);
    label = new JLabel("�� �ϼ�:" + String.valueOf(this.days));
    label.setFont(new Font(label.getFont().getName(), label.getFont().getStyle(), 20));
    daysPanel.add(label);
    label = new JLabel("������ �Ǵ� ��:" + String.valueOf(this.withOutDays));
    label.setFont(new Font(label.getFont().getName(), label.getFont().getStyle(), 20));
    daysPanel.add(label);
    daysPanel.setBounds(0, 0, 1000, 40);
    add(daysPanel);
  }

  // ���� �̹����� �гο� �����ϴ� �޼���
  private void addFailPanel() {
    JPanel failPanel = new JPanel() {
      public void paintComponent(Graphics g) {
        g.drawImage(fail.getImage(), 0, 0, 400, 500, null);
      }
    };
    failPanel.setBounds(1200, 200, 400, 500);
    add(failPanel);
  }

  private void failGrapeProcess() throws ParseException {// ���ذ� �Ȱ� ������ �ʰ� �ؼ��غ�
    int fail = 0;
    int fillCnt = 0;
    // �������� �� fail ������ ���� �����´�.
    for (int i = 0; i < separateGrapes.length; i++) {
      if (separateGrapes[i].state.equals("fail")) {//separateGrape�� state���°� fail�̶��(������)
        fail++;
      }
      if(separateGrapes[i].isFill()){//!!!!!!!!!!!!!!!!!!!!!!������ �̰� ���� �𸣰ھ�!!!!!!!!!!!!!!!!!!!!!!!!!
        fillCnt++;
      }
    }

    // ���� ��¥�� ���� �������� ������ ��¥���� ���Ѵ�.
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    Date currentDate = new Date();
    Date createAt = sdf.parse(create_at);//?? �̵� ���������
    long diffInMillies = currentDate.getTime() - createAt.getTime();
    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

    // ����, fail�� diff ���� �۴ٸ�, �� ���̸�ŭ �������� ���¸� fail�� ������Ʈ�Ѵ�.
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
    // ����, fail�� �޽��Ϻ��� �� ���ٸ�, failPaenl�� �����Ѵ�.
    if (fail > withOutDays) {
      isFinish = true;
      addFailPanel();
    }
  }

  // ���� habit�� finish�� ���·� �����Ű��, ����� �������� ����.
  private void finishDo(int failNum) {
    isFinish = true;
    new ResultFrame(days, failNum);
    addSuccessPanel();
  }

  // ��� �������� ä�������� üũ�Ѵ�.
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
