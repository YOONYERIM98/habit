package models;

import java.sql.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CusMysql {
  private static Connection con;

  public static Connection getInstance() {//��� ����
    if (con != null) {
      return con;
    }
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      String url = "jdbc:mysql://localhost/grape_db?characterEncoding=UTF-8&serverTimezone=UTC";
      con = DriverManager.getConnection(url, "root", "appserver");
      System.out.println("���� ����");
    } catch (ClassNotFoundException e) {
      System.out.println("����̹� �ε� ����");
    } catch (SQLException e) {
      System.out.println("����: " + e);
    }
    return con;
  }

  // habit�� �����ϴ� �޼���.
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


  // ������ �������� ������Ʈ�ϴ� �޼���.
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

  // ������ �������� �����ϴ� �޼���. �⺻���´� empty�̴�.
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

  // habit �� �����Ѵ�.
  // ���� ������, reward�� �Բ� �����Ѵ�.
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

  // �������� habit_idx�� ��ȸ�Ѵ�.
  static public ArrayList<Map> getRewardByHabitIdx(int habitIdx) {
    ArrayList<Map> list = new ArrayList<>(); //���׸� Ŭ���� �迭�� MapŸ������ ����
    Statement stmt = null; //sql�� �����ϱ� ���ؼ�
    try {

      String sql = "SELECT reward_idx, habit_idx, content, create_at FROM reward WHERE habit_idx = ?"; //prepareStatement�� ������ ���ǹ��� '?'���
      PreparedStatement statement = getInstance().prepareStatement(sql); //prepareStatement��ü ����(sql���� ���ڷ�)
      statement.setInt(1, habitIdx); //'?'�ڸ��� habitIdx���
      ResultSet rs = statement.executeQuery(); //sql�� ����

      while (rs.next()) { //reward���̺���  ���ڿ��� ���� �Է¹���(������ �ֱ� ������)
        int reward_idx = rs.getInt("reward_idx");
        int habit_idx = rs.getInt("habit_idx");
        String content = rs.getString("content");
        HashMap habitInfo = new HashMap();//(key, value)
        habitInfo.put("reward_idx", reward_idx);
        habitInfo.put("habit_idx", habit_idx);
        habitInfo.put("content", content);
        list.add(habitInfo); //���׸� Ŭ������ ����
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
    return list; //���׸� Ŭ���� ��ȯ
  }

  // separateGrape ����� habit_idx�� ��ȸ�Ѵ�.
  static public ArrayList<Map> getGrapeByHabitIdx(int habitIdx) {
    ArrayList<Map> list = new ArrayList<>();
    Statement stmt = null;
    try {

      String sql;
      sql = "SELECT grape_idx, habit_idx, state, diary_content, create_at FROM grape WHERE habit_idx = ?"; //prepareStatement�� ������ ���ǹ��� '?'���
      PreparedStatement statement = getInstance().prepareStatement(sql);//prepareStatement��ü ����(sql���� ���ڷ�)
      statement.setInt(1, habitIdx); //'?'�ڸ��� habitIdx���
      ResultSet rs = statement.executeQuery();//sql�� ����

      while (rs.next()) { //grape���̺���  ���ڿ��� ���� �Է¹���(������ �ֱ� ������)
        int grape_idx = rs.getInt("grape_idx");
        int habit_idx = rs.getInt("habit_idx");
        String state = rs.getString("state");
        String diary_content = rs.getString("diary_content");
        HashMap habitInfo = new HashMap();//(key, value)
        habitInfo.put("grape_idx", grape_idx);
        habitInfo.put("habit_idx", habit_idx);
        habitInfo.put("state", state);
        habitInfo.put("diary_content", diary_content);
        list.add(habitInfo); //���׸� Ŭ������ ����
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
    return list; //���׸� Ŭ���� ��ȯ
  }

  // ��� habit ����� �����´�.
  static public ArrayList<Map> getHabitList() {
    ArrayList<Map> list = new ArrayList<>(); //���׸� Ŭ���� �迭�� MapŸ������ ����
    Statement stmt = null; //sql�� �����ϱ� ���ؼ�
    try {
      stmt = getInstance().createStatement();//���� ��ü ����

      String sql; 
      sql = "SELECT habit_idx,title,without_days, days, create_at FROM habit"; //sql�� ����
      ResultSet rs = stmt.executeQuery(sql); //sql�� ����

      while (rs.next()) { //habit���̺���  ���ڿ��� ���� �Է¹���(������ �ֱ� ������)
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
        list.add(habitInfo);//���׸� Ŭ������ ����
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
    return list; //���׸� Ŭ���� ��ȯ
  }

  // �����ڸ� �ܺο��� ȣ������ �ʰ� �ϱ� ���� �����ڸ� private���� �����Ѵ�.
  private CusMysql() {
  }
}
