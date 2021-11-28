package models;

import java.sql.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CusMysql {
  private static Connection con;

  public static Connection getInstance() {//디비 연결
    if (con != null) {
      return con;
    }
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      String url = "jdbc:mysql://localhost/grape_db?characterEncoding=UTF-8&serverTimezone=UTC";
      con = DriverManager.getConnection(url, "root", "appserver");
      System.out.println("연결 성공");
    } catch (ClassNotFoundException e) {
      System.out.println("드라이버 로딩 실패");
    } catch (SQLException e) {
      System.out.println("에러: " + e);
    }
    return con;
  }

  // habit을 삭제하는 메서드.
  static public int deleteHabitByIdx(int habitIdx) {
    PreparedStatement statement = null;
    try {
      String sql = "DELETE FROM habit WHERE habit_idx = ? ";
      statement = getInstance().prepareStatement(sql);
      statement.setInt(1, habitIdx);
      statement.executeUpdate();
      statement.close();

      sql = "DELETE FROM reward WHERE habit_idx = ? ";
      statement = getInstance().prepareStatement(sql);
      statement.setInt(1, habitIdx);
      statement.executeUpdate();
      statement.close();


      sql = "DELETE FROM grape WHERE habit_idx = ? ";
      statement = getInstance().prepareStatement(sql);
      statement.setInt(1, habitIdx);
      statement.executeUpdate();
      statement.close();
    } catch (SQLException se1) {
      se1.printStackTrace();
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        if (statement != null)
          statement.close();
      } catch (SQLException se2) {
      }
    }
    return 0;
  }


  // 각각의 포도알을 업데이트하는 메서드.
  static public int updateSeparateGrape(int habitIdx, int grapeIdx, String state, String diaryContent) {
    PreparedStatement statement = null;
    try {

      String sql = "UPDATE grape SET state = ?, diary_content = ? WHERE habit_idx = ? AND grape_idx = ?";
      statement = getInstance().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      statement.setString(1, state);
      statement.setString(2, diaryContent);
      statement.setInt(3, habitIdx);
      statement.setInt(4, grapeIdx);

      final int separateGrapeIdx = statement.executeUpdate();

      statement.close();

      return separateGrapeIdx;
    } catch (SQLException se1) {
      se1.printStackTrace();
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        if (statement != null)
          statement.close();
      } catch (SQLException se2) {
      }
    }
    return 0;
  }

  // 각각의 포도알을 생성하는 메서드. 기본상태는 empty이다.
  static public int createSeparateGrape(int habitIdx, int grapeIdx) {
    PreparedStatement statement = null;
    try {

      String sql = "INSERT INTO grape (habit_idx, grape_idx, state, diary_content) VALUES (?, ?, \"empty\", \"\")";
      statement = getInstance().prepareStatement(sql);
      statement.setInt(1, habitIdx);
      statement.setInt(2, grapeIdx);
      statement.executeUpdate();

      statement.close();

      return 0;
    } catch (SQLException se1) {
      se1.printStackTrace();
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        if (statement != null)
          statement.close();
      } catch (SQLException se2) {
      }
    }
    return 0;
  }

  // habit 을 생성한다.
  // 생성 과정중, reward도 함께 생성한다.
  static public int createHabits(String habitTitle, int days, int withoutDays, Object[][] rewards) {
    PreparedStatement statement = null;
    try {

      String sql = "INSERT INTO habit (habit_idx, title, without_days, days) VALUES (null, ?, ?, ?)";
      statement = getInstance().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS );
      statement.setString(1, habitTitle);
      statement.setInt(2, withoutDays);
      statement.setInt(3, days);
      statement.executeUpdate();

      ResultSet rs = statement.getGeneratedKeys();
      int habitIdx = 0;
      if (rs.next()) {
        habitIdx = rs.getInt(1);
      }
      rs.close();

      statement.close();


      for (int i = 0; i < rewards.length; i++) {
        String currentReward = (String) rewards[i][1];
        sql = "INSERT INTO reward (habit_idx, reward_idx, content) VALUES (?, ?, ?)";
        statement = getInstance().prepareStatement(sql);
        statement.setInt(1, habitIdx);
        statement.setInt(2, i + 1);
        statement.setString(3, currentReward);
        statement.executeUpdate();
        statement.close();
      }
      return habitIdx;
    } catch (SQLException se1) {
      se1.printStackTrace();
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        if (statement != null)
          statement.close();
      } catch (SQLException se2) {
      }
    }
    return -1;
  }

  // 보상목록을 habit_idx로 조회한다.
  static public ArrayList<Map> getRewardByHabitIdx(int habitIdx) {
    ArrayList<Map> list = new ArrayList<>(); //제네릭 클래스 배열에 Map타입으로 저장
    Statement stmt = null; //sql문 실행하기 위해서
    try {

      String sql = "SELECT reward_idx, habit_idx, content, create_at FROM reward WHERE habit_idx = ?"; //prepareStatement문 떄문에 조건문에 '?'사용
      PreparedStatement statement = getInstance().prepareStatement(sql); //prepareStatement객체 생성(sql문을 인자로)
      statement.setInt(1, habitIdx); //'?'자리에 habitIdx사용
      ResultSet rs = statement.executeQuery(); //sql문 실행

      while (rs.next()) { //reward테이블의  문자열을 전부 입력받음(공백이 있기 전까지)
        int reward_idx = rs.getInt("reward_idx");
        int habit_idx = rs.getInt("habit_idx");
        String content = rs.getString("content");
        HashMap habitInfo = new HashMap();//(key, value)
        habitInfo.put("reward_idx", reward_idx);
        habitInfo.put("habit_idx", habit_idx);
        habitInfo.put("content", content);
        list.add(habitInfo); //제네릭 클래스에 저장
      }
      rs.close();
    } catch (SQLException se1) {
      se1.printStackTrace();
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (SQLException se2) {
      }
    }
    return list; //제네릭 클래스 반환
  }

  // separateGrape 목록을 habit_idx로 조회한다.
  static public ArrayList<Map> getGrapeByHabitIdx(int habitIdx) {
    ArrayList<Map> list = new ArrayList<>();
    Statement stmt = null;
    try {

      String sql;
      sql = "SELECT grape_idx, habit_idx, state, diary_content, create_at FROM grape WHERE habit_idx = ?"; //prepareStatement문 떄문에 조건문에 '?'사용
      PreparedStatement statement = getInstance().prepareStatement(sql);//prepareStatement객체 생성(sql문을 인자로)
      statement.setInt(1, habitIdx); //'?'자리에 habitIdx사용
      ResultSet rs = statement.executeQuery();//sql문 실행

      while (rs.next()) { //grape테이블의  문자열을 전부 입력받음(공백이 있기 전까지)
        int grape_idx = rs.getInt("grape_idx");
        int habit_idx = rs.getInt("habit_idx");
        String state = rs.getString("state");
        String diary_content = rs.getString("diary_content");
        HashMap habitInfo = new HashMap();//(key, value)
        habitInfo.put("grape_idx", grape_idx);
        habitInfo.put("habit_idx", habit_idx);
        habitInfo.put("state", state);
        habitInfo.put("diary_content", diary_content);
        list.add(habitInfo); //제네릭 클래스에 저장
      }
      rs.close();
    } catch (SQLException se1) {
      se1.printStackTrace();
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (SQLException se2) {
      }
    }
    return list; //제네릭 클래스 반환
  }

  // 모든 habit 목록을 가져온다.
  static public ArrayList<Map> getHabitList() {
    ArrayList<Map> list = new ArrayList<>(); //제네릭 클래스 배열에 Map타입으로 저장
    Statement stmt = null; //sql문 실행하기 위해서
    try {
      stmt = getInstance().createStatement();//문장 객체 생성

      String sql; 
      sql = "SELECT habit_idx,title,without_days, days, create_at FROM habit"; //sql문 생성
      ResultSet rs = stmt.executeQuery(sql); //sql문 실행

      while (rs.next()) { //habit테이블의  문자열을 전부 입력받음(공백이 있기 전까지)
        int habit_idx = rs.getInt("habit_idx");
        String title = rs.getString("title");
        int without_days = rs.getInt("without_days");
        int days = rs.getInt("days");
        String createAt = rs.getString("create_at").substring(0, 10);
        HashMap habitInfo = new HashMap(); //(key, value)
        habitInfo.put("habit_idx", habit_idx);
        habitInfo.put("title", title);
        habitInfo.put("without_days", without_days);
        habitInfo.put("days", days);
        habitInfo.put("create_at", createAt);
        list.add(habitInfo);//제네릭 클래스에 저장
      }
      rs.close();
      stmt.close();
    } catch (SQLException se1) {
      se1.printStackTrace();
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (SQLException se2) {
      }
    }
    return list; //제네릭 클래스 반환
  }

  // 생성자를 외부에서 호출하지 않게 하기 위해 생성자를 private으로 정의한다.
  private CusMysql() {
  }
}
