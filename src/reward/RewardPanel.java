package reward;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

// ���� �гο� ���� ������ ������ Ŭ����.
// MainGrape Panel���� ����ϱ� ���� �����Ͽ���.
// �ڵ� ��ü�� RewardFrame�� ���� �����ϴ�.
public class RewardPanel extends JScrollPane {
  String columnNames[] = {"", "�����"};
  Object[][] rowData;

  public RewardPanel(Object[][] rowData) {
    super();
    this.rowData = rowData;
    JTable jTable;
    //�������
    jTable = new JTable(rowData, columnNames);
    DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
    dtcr.setHorizontalAlignment(SwingConstants.CENTER);
    for (int i = 0; i < jTable.getColumnCount(); i++) {
      jTable.getColumnModel().getColumn(i).setCellRenderer(dtcr);
    }

    jTable.getColumnModel().getColumn(0).setPreferredWidth(50); //��ȣ�� ũ��
    jTable.getColumnModel().getColumn(1).setPreferredWidth(200); //����� ũ��

    setViewportView(jTable);
  }
}
