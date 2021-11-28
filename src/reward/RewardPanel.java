package reward;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

// 보상 패널에 대한 정보를 정의한 클래스.
// MainGrape Panel에서 사용하기 위해 정의하였다.
// 코드 자체는 RewardFrame과 거의 동일하다.
public class RewardPanel extends JScrollPane {
  String columnNames[] = {"", "보상명"};
  Object[][] rowData;

  public RewardPanel(Object[][] rowData) {
    super();
    this.rowData = rowData;
    JTable jTable;
    //가운데정렬
    jTable = new JTable(rowData, columnNames);
    DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
    dtcr.setHorizontalAlignment(SwingConstants.CENTER);
    for (int i = 0; i < jTable.getColumnCount(); i++) {
      jTable.getColumnModel().getColumn(i).setCellRenderer(dtcr);
    }

    jTable.getColumnModel().getColumn(0).setPreferredWidth(50); //번호셀 크기
    jTable.getColumnModel().getColumn(1).setPreferredWidth(200); //보상명셀 크기

    setViewportView(jTable);
  }
}
