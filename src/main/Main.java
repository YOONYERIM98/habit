package main;

import models.CusMysql;

public class Main {
  public static void main(String[] args) {
    // ������ ���̽� ���� ����
    CusMysql.getInstance();
    // ���� ������ ����
    new SettingFrame();
  }
}
