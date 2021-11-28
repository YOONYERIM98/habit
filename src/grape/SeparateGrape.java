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

// ������ ������ ������ �����ϱ� ���� Ŭ����.
public class SeparateGrape implements ActionListener, Serializable {

  static final ImageIcon grapeEmpty = new ImageIcon(new ImageIcon("./image/grape_empty.png").getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT));//ä������ ���� ������
  static final ImageIcon grapeFill = new ImageIcon(new ImageIcon("./image/grape_fill.png").getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT));//ä���� ������
  static final ImageIcon grapeFail = new ImageIcon(new ImageIcon("./image/grape_fail.png").getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT));//������ ������
  static final ImageIcon grapeFarmer = new ImageIcon(new ImageIcon("./image/grape_farmer.png").getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT));//������ 6�� ä������ �� ������
  static final ImageIcon grapeLatest = new ImageIcon(new ImageIcon("./image/grape_latest.png").getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT));//������ ä������ �� ������

  private int grapeIdx;
  private int habitIdx;
  public String diaryContent;
  public String state;
  public ImageIcon icon;
  public String fillAt;
  private MainGrape parentMainGrape;

  // ���� �⺻���� �������� �����ϴ� ������
  // �⺻������ �� �����̴�.
  // �����ڸ� ȣ���ϸ�, db�� 'grape'�����͸� �Բ� �߰��Ѵ�.
  public SeparateGrape(int habitIdx, int grapeIdx, MainGrape parentMainGrape) {
    this.grapeIdx = grapeIdx;
    this.habitIdx = habitIdx;
    this.state = "empty";// �� ������ ���� ����
    internalSetIcon();
    this.diaryContent = "";
    this.parentMainGrape = parentMainGrape;

    // db insert
    CusMysql.createSeparateGrape(habitIdx, grapeIdx);
  }

  // �������� �����ϴ� ������
  // ���� �����͸� �ҷ����� ���, ȣ��Ǵ� �������̴�.
  public SeparateGrape(int habitIdx, int grapeIdx, String state, String diaryContent) {
    this.grapeIdx = grapeIdx;
    this.habitIdx = habitIdx;
    this.state = state;
    internalSetIcon();
    this.diaryContent = diaryContent;
  }

  // �����͸� �ҷ��� ���, �����ڿ��� parent�� �ش��ϴ� MainGrape Panel�� �������� ���ϱ� ������, ���� �Լ��� ����.
  public void setParentMainGrape(MainGrape parentMainGrape) {
    this.parentMainGrape = parentMainGrape;
  }

  // state �� ����, �������� ��������ִ� �޼����̴�.
  public void internalSetIcon() {
    if (this.state.equals("fill")) {//state���� fill�̶��
      icon = grapeFill;//ä���� ������
    } else if (this.state.equals("empty")) {//state���� empty�̶��
      icon = grapeEmpty;//�� ������
    } else if (this.state.equals("fillFarmer")) {//state���� fillFarmer�̶��
      icon = grapeFarmer;//������ 6�� ä������ �� ������
    } else if (this.state.equals("fail")) {//state���� fail�̶��
      icon = grapeFail;//������ ������
    } else if (this.state.equals("fillLatest")) {//state���� fillLatest�̶��
      icon = grapeLatest;//������ ä������ �� ������
    }
  }

  // ���� ������Ʈ�� ä���� �������� üũ�ϴ� �޼����̴�.
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

  // ������Ʈ�� �����ϴ� �޼����̴�.
  // ������Ʈ�� �����ϸ� ������ �� DB�� ������Ʈ ������ϱ� ������, �޼���� �����ߴ�.
  public void setState(String state) {
    this.state = state;
    internalSetIcon();
    CusMysql.updateSeparateGrape(habitIdx, grapeIdx, state, diaryContent);
  }

  // �� �Լ��� diary �����ӿ��� �����.
  // ���̾�� ������Ʈ �ϱ� ���� �Լ��̴�.
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
    // ������ ���ο� ���̾ �������� ȣ���Ѵ�.
    if (parentMainGrape.canFill() || !state.equals("empty")) {
      new DiaryWindow(this, diaryContent);
    } else {
      JOptionPane.showMessageDialog(null, "�Ϸ翡 �ϳ��� �ۼ��� �� �ֽ��ϴ�.", "����", JOptionPane.WARNING_MESSAGE);
    }
  }
}
