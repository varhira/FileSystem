/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package filesystem;

import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
*　GUI上でCSVファイルの読み書きを行うプログラム
* @author y_hiraba
*/
/**
* @param args the command line arguments
*/

// 透かし文字の処理　普通に入力するときにも文字が透けてしまうのでいったんコメントアウト
//    class JTextFt extends JTextField implements FocusListener {
//      private static final long serialVersionUID = 1L;
//      String helpmsg;
//      String bakstr="";
//      JTextFt(String msg){
//          helpmsg=msg;
//          addFocusListener(this);
//          drawmsg();
//        }
//        void drawmsg() {
//            setForeground(Color.LIGHT_GRAY);
//            setText(helpmsg);
//          }
//        @Override
//        public void focusGained(FocusEvent arg0) {
//            setForeground(Color.BLACK);
//            setText(bakstr);
//        }
//        @Override
//        public void focusLost(FocusEvent arg0) {
//            bakstr=getText();
//            if (bakstr.equals("")) {
//                drawmsg();
//            }
//        }
//    }

// イベントを発生させるボタンを生成
class FileSystem extends JFrame {
    // Button
    private JButton selectButton;
    private JButton load;
    private JButton save;
    private JButton clear;
    private JButton add;
    private JButton update;
    //    private JButton infoButton;

    // Item
    private JTextField id;
    private JTextField name;
    private String sex = "";
    private JTextField birthday;
    private JTextField age;
    public String filePath = "";
    public int row;
    public int comboData;

    // Label
    public JLabel patientInformation;
    public JLabel label;

    // For dispaly
    public JTextField selected;
    public JTextArea loadField;
    public JTextField selectField;
    public JRadioButton men;
    public JRadioButton woman;

    // Panel
    public JPanel TopPanenl;
    public JPanel CenterPanel;
    public JPanel patientPanel;

    // Array
    public ArrayList<String> patientInformationArrTmp;
    public ArrayList<String> patientInformationArr;

    // Table
    public JTable loadFieldTable;

    // Model
    public DefaultTableModel patientInformationModel;

    public static void main(String[] args) {
        FileSystem f = new FileSystem();
    }

    // 未入力の場合のエラーダイアログの表示
    public void error() {
        // 条件が良くないので適切な状態で判定ができていない。
        if(patientInformationArrTmp.indexOf("") >= 0){
            JOptionPane.showMessageDialog(this, "記入漏れがあります。");
            patientInformationArrTmp.clear();
        }
    }

    public void add(){

        JPanel DialogPanelBase = createDialog();

        int r = JOptionPane.showConfirmDialog(
            FileSystem.this, // オーナーウィンドウ
            DialogPanelBase, // メッセージ
            "追加", // タイトル
            JOptionPane.OK_CANCEL_OPTION,	// オプション（ボタンの種類）
            JOptionPane.QUESTION_MESSAGE	// メッセージタイプ（アイコンの種類）
        );

        dataSet(true);

        }

    public void update(){
        JPanel DialogPanelBase = createDialog();

        //PatientInformation info = getSelectedTableRow();
        int selectedRow = getSelectedRow();
        PatientInformation info = this.getTabledRowInfo(selectedRow);

        System.out.println(info.toString());

        if(info == null) {
            JOptionPane.showMessageDialog(FileSystem.this, "選択されていません。");
            return;
        }else{

            id.setText(info.getId());
            name.setText(info.getName());
            if(info.getSex().equals("男")){
                men.setSelected(true);
                woman.setSelected(false);
            }else if(info.getSex().equals("女")){
                men.setSelected(false);
                woman.setSelected(true);
            }
            birthday.setText(info.getBirthday());
            age.setText(info.getAge());
        }

        // JFrame, JDialog
        int r = JOptionPane.showConfirmDialog(
            FileSystem.this, // オーナウィンドウ
            DialogPanelBase, // メッセージ
            "編集", // タイトル
            JOptionPane.OK_CANCEL_OPTION,	// オプション（ボタンの種類）
            JOptionPane.QUESTION_MESSAGE	// メッセージタイプ（アイコンの種類）
        );

        dataSet(false);

    }

    private void dataSet(boolean check) {
        if(men.isSelected() == true){
            sex = "男";
        }else if(woman.isSelected() == true){
            sex = "女";
        }else if(men.isSelected() == false || woman.isSelected() == false){
            JOptionPane.showMessageDialog(this, "性別を選択してください。");
            return;
        }

        int row = 0;
        if(check == false){ // update
            int selectedRow = loadFieldTable.getSelectedRow();
            // ﾃﾞｰﾀの反映
            loadFieldTable.setValueAt(id.getText(), selectedRow, 0);
            loadFieldTable.setValueAt(name.getText(), selectedRow, 1);
            loadFieldTable.setValueAt(sex, selectedRow, 2);
            loadFieldTable.setValueAt(birthday.getText().replaceAll("-", "/"), selectedRow, 3);
            loadFieldTable.setValueAt(age.getText(), selectedRow, 4);
        }else{ //add
            int rowCount = loadFieldTable.getRowCount();
            row = rowCount + 1;

            // ﾃﾞｰﾀの反映
            ArrayList<String> ret = new ArrayList<>();
            ret.add(id.getText());
            ret.add(name.getText());
            ret.add(sex);
            ret.add(birthday.getText().replaceAll("-", "/"));
            ret.add(age.getText());

            DefaultTableModel model = (DefaultTableModel)loadFieldTable.getModel();
            model.addRow(ret.toArray());

        }




    }

    private JPanel createDialog() {
        JPanel DialogPanelBase = new JPanel();
        JPanel DialogPanel = new JPanel();
        JPanel patientInformationPanelItem = new JPanel();
        JPanel patientInformationPanelField = new JPanel();

        DialogPanel.setLayout(new GridLayout(1, 2));
        patientInformationPanelItem.setLayout(new GridLayout(5,1));
        patientInformationPanelField.setLayout(new GridLayout(5,1));

        JLabel lId = new JLabel("患者ID");
        JLabel lName = new JLabel("氏名");
        JLabel lSex = new JLabel("性別");
        JLabel lBirthday = new JLabel("生年月日");
        JLabel lAge = new JLabel("年齢");

        lId.setHorizontalAlignment(SwingConstants.CENTER);
        lName.setHorizontalAlignment(SwingConstants.CENTER);
        lSex.setHorizontalAlignment(SwingConstants.CENTER);
        lBirthday.setHorizontalAlignment(SwingConstants.CENTER);
        lAge.setHorizontalAlignment(SwingConstants.CENTER);

        patientInformationPanelItem.add(lId);
        patientInformationPanelItem.add(lName);
        patientInformationPanelItem.add(lSex);
        patientInformationPanelItem.add(lBirthday);
        patientInformationPanelItem.add(lAge);

        id = new JTextField("", 20);
        patientInformationPanelField.add(id);
        name = new JTextField("", 20);
        patientInformationPanelField.add(name);
        JPanel SexPanel = new JPanel();
        SexPanel.setLayout(new GridLayout(1,2));
        ButtonGroup sexGroup = new ButtonGroup();
        men = new JRadioButton("男性");
        woman = new JRadioButton("女性");
        sexGroup.add(men);
        sexGroup.add(woman);
        SexPanel.add(men);
        SexPanel.add(woman);
        patientInformationPanelField.add(SexPanel);
        birthday = new JTextField("", 20);
        patientInformationPanelField.add(birthday);
        age = new JTextField("", 20);
        patientInformationPanelField.add(age);

        DialogPanel.add(patientInformationPanelItem);
        DialogPanel.add(patientInformationPanelField);
        DialogPanelBase.add(DialogPanel);

        return DialogPanelBase;
    }

    public FileSystem(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("ファイルの読み書き");
        setBounds(200, 100, 600, 700); // 出力場所 (x, y, width, heigft);

        patientInformationArr = new ArrayList();

        // Top Panelの設定-------------------------------------------------------
        JPanel TopPanel = new JPanel();
        patientPanel = new JPanel();
        JPanel ButtonPanel1 = new JPanel();
        JPanel ButtonPanel2 = new JPanel();
        TopPanel.setPreferredSize(new Dimension(600, 150));
        patientPanel.setPreferredSize(new Dimension(500, 80));
        patientPanel.setBackground(new Color(232,226,232)); // set Color

//        ButtonPanel1.setBackground(new Color(140,140,140)); // set Color
//        ButtonPanel2.setBackground(new Color(140,140,140)); // set Color
//        TopPanel.setBackground(new Color(30,232,203)); // set Color

        selectField = new JTextField(filePath, 30);
        TopPanel.add(selectField);
        selectButton = new JButton("ファイルの選択");
        TopPanel.add(selectButton);

        String[] charComboData = {"SJIS", "UTF-8"};
        JComboBox charCode = new JComboBox(charComboData);
        TopPanel.add(charCode);
        TopPanel.add(patientPanel);

        // First Button
        load = new JButton("読み込み");
        save = new JButton("保存");
        ButtonPanel1.add(load);
        ButtonPanel1.add(save);
//        // Second Button
//        add = new JButton("追加");
//        update = new JButton("編集");
//        clear = new JButton("削除");
//        ButtonPanel2.add(add);
//        ButtonPanel2.add(update);
//        ButtonPanel2.add(clear);

        TopPanel.add(ButtonPanel1);
//        TopPanel.add(ButtonPanel2);
        // Top Panelの設定-------------------------------------------------------

        // Center Panelの設定----------------------------------------------------
        CenterPanel = new JPanel();
        loadFieldTable = new JTable();
        loadFieldTable.setDefaultEditor(Object.class, null); // セルの編集不可
//        loadFieldTable.setAutoCreateRowSorter(true); // ソート機能 int型に対応していないのでまだ使えない。
        loadFieldTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane loadFieldTableScollpane = new JScrollPane(loadFieldTable);
        loadFieldTableScollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        CenterPanel.add(loadFieldTableScollpane);
//        CenterPanel.setBackground(new Color(30,232,203)); // set Color

        JPopupMenu popup;
        popup = new JPopupMenu();
        JMenuItem addMenuItem = new JMenuItem("追加");
//        JMenuItem copyMenuItem = new JMenuItem("コピー");
        JMenuItem updateMenuItem = new JMenuItem("編集");
        JMenuItem deleteMenuItem = new JMenuItem("削除");

        popup.add(addMenuItem);
//        popup.add(copyMenuItem);
        popup.add(updateMenuItem);
        popup.add(deleteMenuItem);
        // Center Panelの設定----------------------------------------------------

        // BottomPanelの設定-----------------------------------------------------
        JPanel BottomPanel = new JPanel();
//        BottomPanel.setBackground(new Color(30,232,203)); // set Color
        // BottomPanelの設定-----------------------------------------------------

        // 各Panelの表示---------------------------------------------------------
        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.add(TopPanel);
        contentPane.add(CenterPanel);
        contentPane.add(popup);
        contentPane.add(BottomPanel);
        // 各Panelの表示---------------------------------------------------------

        // カラム設定------------------------------------------------------------
        final String[] columnNames = {"患者ID", "氏名", "性別", "生年月日", "年齢"};
        patientInformationModel = new DefaultTableModel(columnNames, 0);
        loadFieldTable.setModel(patientInformationModel);
        // カラム設定------------------------------------------------------------

        // ボタン設定------------------------------------------------------------
        FileAccess fa = new FileAccess();
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser filechooser = new JFileChooser();
                filechooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int selected = filechooser.showOpenDialog(FileSystem.this);

                if (selected == JFileChooser.APPROVE_OPTION) {
                    File file = filechooser.getSelectedFile();
                    JLabel selectedLabel = new JLabel();
                    selectedLabel.setText(file.getAbsolutePath());
                    filePath = file.getAbsolutePath();
                    selectField.setText(filePath);
                }else if (selected == JFileChooser.CANCEL_OPTION){
//                    selectField.setToolTipText("キャンセルされました");
                    BottomPanel.add(new JLabel("キャンセルされました"));
                }else if (selected == JFileChooser.ERROR_OPTION){
//                    selectField.setText("エラー又は取消しがありました");
                    BottomPanel.add(new JLabel("エラー又は取消しがありました"));
                }
            }
        });

        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(filePath == ""){
                    JOptionPane.showMessageDialog(FileSystem.this, "ファイルが指定されていません。");
                }else{
                    // File select dialog
                    ArrayList<PatientInformation> data;

                    comboData = charCode.getSelectedIndex();
                    System.out.println(comboData);

                    data = new ArrayList();
                    data = fa.readFile(filePath, comboData); // filePathを入れるとcolumsArrが返ってくる

                    if(data == null) {
                        // File access error.
                        return;
                    }
                    // data ...
                    for (int j = 0; j < data.size(); j++) {
                        PatientInformation info = data.get(j);
                        int rc = patientInformationModel.getRowCount();
                        patientInformationModel.addRow(new Object[] {rc});
                        patientInformationModel.setValueAt(info.getId(), j, PatientInformation.COLUMN_ID);
                        patientInformationModel.setValueAt(info.getName(), j, PatientInformation.COLUMN_NAME);
                        patientInformationModel.setValueAt(info.getSex(), j, PatientInformation.COLUMN_SEX);
                        patientInformationModel.setValueAt(info.getBirthday(), j, PatientInformation.COLUMN_BIRTHDAY);
                        patientInformationModel.setValueAt(info.getAge(), j, PatientInformation.COLUMN_AGE);
                    }
                    loadFieldTable.setModel(patientInformationModel);
                    JOptionPane.showMessageDialog(FileSystem.this, "ファイルの読み込みが完了しました。");
                }
            }
        });

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(filePath.equals("")){
                    JOptionPane.showMessageDialog(FileSystem.this, "ファイルが指定されていません。");
                }else{

                    ArrayList dataList = new ArrayList<>();
                    ArrayList<PatientInformation> infoList = getTableItems();
                    for(int i = 0; i < infoList.size(); i++) {
                        PatientInformation info = infoList.get(i);
                        dataList.add(info.convertCsvFormat());
                    }
                    fa.writeFile(filePath, dataList);
                    System.out.println("出力しました。");
                    JOptionPane.showMessageDialog(FileSystem.this, "ファイルの出力が完了しました。");
                }
            }
        });

        addMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                add();
            }
        });

        deleteMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectvalues[] = {"選択した行を削除","すべてを削除","取消し"};
                int option = JOptionPane.showOptionDialog(FileSystem.this,
                    "削除しますか？",
                    "警告",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    selectvalues,
                    selectvalues[0]
                );

                if (option == JOptionPane.YES_OPTION) {
                    // 選択している行のみ削除
                    int[] selection = loadFieldTable.getSelectedRows();
                    int selectionRow = loadFieldTable.getSelectedColumn();
                    System.out.print(selectionRow);
                    if( selectionRow < 0 ) {
                        JOptionPane.showMessageDialog(FileSystem.this, "選択されていません。");
                        return;
                    }
                    for (int i = selection.length - 1; i >= 0; i--) {
                        patientInformationModel.removeRow(loadFieldTable.convertRowIndexToModel(selection[i]));
                    }
                }else if(option == JOptionPane.NO_OPTION) {
                    // すべての列を削除
                    int loadFieldTableNUM = loadFieldTable.getRowCount();
                    for (int i = loadFieldTableNUM - 1; i >= 0; i--) {
                        patientInformationModel.removeRow(i);
                    }
                }else if (option == JOptionPane.CLOSED_OPTION){
                    // 選択なしで×を押してダイアログ終了の処理
                }
            }
        });

        updateMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
            }
        });
        // ボタン設定------------------------------------------------------------


        loadFieldTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int btn = e.getButton();
                if (btn == MouseEvent.BUTTON1){
                    System.out.println("左ボタンクリック");
                    if (e.getClickCount() == 1) {
                        patientPanel.removeAll();
                        PatientInformation info = getSelectedTableRow();
                        JLabel label = new JLabel();
                        label.setText(info.patientToString());
                        label.setOpaque(false);
                        patientPanel.add(label);
                        // patientPanel.repaint();
                        patientPanel.updateUI();
                    }else if(e.getClickCount() == 2){
                        update();
                    }
                }
            }
            public void mouseReleased(MouseEvent e){
              showPopup(e);
            }

            public void mousePressed(MouseEvent e){
              showPopup(e);
            }

            private void showPopup(MouseEvent e){
              if (e.isPopupTrigger()) {
                /* ポップアップメニューを表示させる */
                popup.show(e.getComponent(), e.getX(), e.getY());
              }
            }
        });

        setVisible(true);

    }

    private int getSelectedRow() {
        return loadFieldTable.getSelectedRow();
    }

    private PatientInformation getTabledRowInfo(int row) {
        PatientInformation ret = null;

        if(row >= 0) {
            ret = new PatientInformation();
            ret.setId((String)loadFieldTable.getValueAt(row, PatientInformation.COLUMN_ID));
            ret.setName((String)loadFieldTable.getValueAt(row, PatientInformation.COLUMN_NAME));
            ret.setSex((String)loadFieldTable.getValueAt(row, PatientInformation.COLUMN_SEX));
            ret.setBirthday((String)loadFieldTable.getValueAt(row, PatientInformation.COLUMN_BIRTHDAY));
            ret.setAge((String)loadFieldTable.getValueAt(row, PatientInformation.COLUMN_AGE));
        }

        return ret;
    }

    // 選択している行の値を返す
    private PatientInformation getSelectedTableRow() {
//        PatientInformation ret = null;
//
//        int selectedRow = loadFieldTable.getSelectedRow();
//        if(selectedRow >= 0) {
//            ret = new PatientInformation();
//            ret.setId((String)loadFieldTable.getValueAt(selectedRow, PatientInformation.COLUMN_ID));
//            ret.setName((String)loadFieldTable.getValueAt(selectedRow, PatientInformation.COLUMN_NAME));
//            ret.setSex((String)loadFieldTable.getValueAt(selectedRow, PatientInformation.COLUMN_SEX));
//            ret.setBirthday((String)loadFieldTable.getValueAt(selectedRow, PatientInformation.COLUMN_BIRTHDAY));
//            ret.setAge((String)loadFieldTable.getValueAt(selectedRow, PatientInformation.COLUMN_AGE));
//        }
//
//        return ret;
        return getTabledRowInfo(getSelectedRow());
    }

    // ﾃｰﾌﾞﾙにあるすべての値を行ごとに取得して返す
    private ArrayList<PatientInformation> getTableItems() {
        ArrayList<PatientInformation> ret = new ArrayList<>();
        for(int row = 0; row < loadFieldTable.getRowCount(); row++) {
//            PatientInformation info = new PatientInformation();
//            info.setId((String)loadFieldTable.getValueAt(row, PatientInformation.COLUMN_ID));
//            info.setName((String)loadFieldTable.getValueAt(row, PatientInformation.COLUMN_NAME));
//            info.setSex((String)loadFieldTable.getValueAt(row, PatientInformation.COLUMN_SEX));
//            info.setBirthday((String)loadFieldTable.getValueAt(row, PatientInformation.COLUMN_BIRTHDAY));
//            info.setAge((String)loadFieldTable.getValueAt(row, PatientInformation.COLUMN_AGE));
            PatientInformation info = getTabledRowInfo(row);
            ret.add(info);
        }
        return ret;
    }
}