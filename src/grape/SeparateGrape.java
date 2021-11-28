package grape;

import diary.DiaryWindow;
import models.CusMysql;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

// 각각의 포도알 정보를 저장하기 위한 클래스.
public class SeparateGrape implements ActionListener, Serializable {

  static final ImageIcon grapeEmpty = new ImageIcon(new ImageIcon("./image/grape_empty.png").getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT));//채워지지 않은 포도알
  static final ImageIcon grapeFill = new ImageIcon(new ImageIcon("./image/grape_fill.png").getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT));//채워진 포도알
  static final ImageIcon grapeFail = new ImageIcon(new ImageIcon("./image/grape_fail.png").getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT));//빠진날 포도알
  static final ImageIcon grapeFarmer = new ImageIcon(new ImageIcon("./image/grape_farmer.png").getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT));//포도알 6개 채워지는 날 포도알
  static final ImageIcon grapeLatest = new ImageIcon(new ImageIcon("./image/grape_latest.png").getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT));//마지막 채워지는 날 포도알

  private int grapeIdx;
  private int habitIdx;
  public String diaryContent;
  public String state;
  public ImageIcon icon;
  public String fillAt;
  private MainGrape parentMainGrape;

  // 아주 기본적인 포도알을 생성하는 생성자
  // 기본적으로 빈 상태이다.
  // 생성자를 호출하면, db에 'grape'데이터를 함께 추가한다.
  public SeparateGrape(int habitIdx, int grapeIdx, MainGrape parentMainGrape) {
    this.grapeIdx = grapeIdx;
    this.habitIdx = habitIdx;
    this.state = "empty";// 빈 포도알 상태 저장
    internalSetIcon();
    this.diaryContent = "";
    this.parentMainGrape = parentMainGrape;

    // db insert
    CusMysql.createSeparateGrape(habitIdx, grapeIdx);
  }

  // 포도알을 생성하는 생성자
  // 기존 데이터를 불러왔을 경우, 호출되는 생성자이다.
  public SeparateGrape(int habitIdx, int grapeIdx, String state, String diaryContent) {
    this.grapeIdx = grapeIdx;
    this.habitIdx = habitIdx;
    this.state = state;
    internalSetIcon();
    this.diaryContent = diaryContent;
  }

  // 데이터를 불러올 경우, 생성자에서 parent에 해당하는 MainGrape Panel을 지정하지 못하기 때문에, 따로 함수로 구현.
  public void setParentMainGrape(MainGrape parentMainGrape) {
    this.parentMainGrape = parentMainGrape;
  }

  // state 에 따라, 아이콘을 변경시켜주는 메서드이다.
  public void internalSetIcon() {
    if (this.state.equals("fill")) {//state값이 fill이라면
      icon = grapeFill;//채워진 포도알
    } else if (this.state.equals("empty")) {//state값이 empty이라면
      icon = grapeEmpty;//빈 포도알
    } else if (this.state.equals("fillFarmer")) {//state값이 fillFarmer이라면
      icon = grapeFarmer;//포도알 6개 채워지는 날 포도알
    } else if (this.state.equals("fail")) {//state값이 fail이라면
      icon = grapeFail;//빠진날 포도알
    } else if (this.state.equals("fillLatest")) {//state값이 fillLatest이라면
      icon = grapeLatest;//마지막 채워지는 날 포도알
    }
  }

  // 현재 스테이트가 채워진 상태인지 체크하는 메서드이다.
  public boolean isFill() {
    if (this.state.equals("fill")) {
      return true;
    } else if (this.state.equals("fillFarmer")) {
      return true;
    } else if (this.state.equals("fillLatest")) {
      return true;
    }
    return false;
  }

  // 스테이트를 변경하는 메서드이다.
  // 스테이트를 변경하면 아이콘 및 DB도 업데이트 해줘야하기 때문에, 메서드로 구현했다.
  public void setState(String state) {
    this.state = state;
    internalSetIcon();
    CusMysql.updateSeparateGrape(habitIdx, grapeIdx, state, diaryContent);
  }

  // 이 함수는 diary 프레임에서 사용함.
  // 다이어리를 업데이트 하기 위한 함수이다.
  public void saveDiary(String content) {
    this.diaryContent = content;
    this.state = parentMainGrape.fillTreeAndReturnState(this);
    internalSetIcon();
    parentMainGrape.clickedAfter(this);

    // db update
    CusMysql.updateSeparateGrape(habitIdx, grapeIdx, state, diaryContent);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // 누르면 새로운 다이어리 프레임을 호출한다.
    if (parentMainGrape.canFill() || !state.equals("empty")) {
      new DiaryWindow(this, diaryContent);
    } else {
      JOptionPane.showMessageDialog(null, "하루에 하나만 작성할 수 있습니다.", "실패", JOptionPane.WARNING_MESSAGE);
    }
  }
}
