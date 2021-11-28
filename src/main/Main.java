package main;

import models.CusMysql;

public class Main {
  public static void main(String[] args) {
    // 데이터 베이스 최초 세팅
    CusMysql.getInstance();
    // 세팅 프레임 시작
    new SettingFrame();
  }
}
