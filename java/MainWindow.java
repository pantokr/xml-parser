package xml_gui;

import javax.swing.*;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame {
	boolean isSaved = false;
	File loadFile = null;

	XMLFunctions func = new XMLFunctions();
	Document doc = null;

	JTextArea statusTextField = new JTextArea();
	JButton[] optionButtons = new JButton[9];
	JLabel statusLabel = new JLabel();

	String[] status = new String[] { "Load", "Make", "Find", "Save", "Print", "Insert", "Update", "Delete", "Exit" };

	public MainWindow() {
		MainFrame();
	}

	public void MainFrame() {
		setTitle("Menu");
		setSize(400, 400);
		setLayout(new BorderLayout());

		ButtonPanel();
		TextPanel();
		BottomPanel();

		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	public void ButtonPanel() {
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new GridLayout(3, 3, 1, 1));

		for (int btn = 0; btn < 9; btn++) {
			optionButtons[btn] = new JButton(status[btn]);
		}

		for (int btn = 0; btn < 9; btn++) {
			optionButtons[btn].addMouseListener(new ButtonMouseAdapter(btn));
		}

		for (int btn = 0; btn < 9; btn++) {
			optionButtons[btn].addActionListener(new ButtonActionListener(btn));
		}

		for (JButton btn : optionButtons) {
			jPanel.add(btn);
		}

		add(jPanel, BorderLayout.NORTH);

	}

	public void TextPanel() {
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new FlowLayout());

		statusTextField.setText("");
		statusTextField.setPreferredSize(new Dimension(380, 200));

		jPanel.add(statusTextField);

		add(jPanel, BorderLayout.CENTER);
	}

	public void BottomPanel() {
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new FlowLayout());

		statusLabel.setText("파일이 로드되지 않았습니다.");

		jPanel.add(statusLabel);

		add(jPanel, BorderLayout.SOUTH);
	}

	public static void main(String[] args) {
		MainWindow mw = new MainWindow();
	}

	class ButtonMouseAdapter extends MouseAdapter {

		int btn_n;
		String[] taText = new String[] { "Load", "Make", "Find", "Save", "Print", "Insert", "Update", "Delete",
				"Exit" };

		ButtonMouseAdapter(int btn_n) {
			this.btn_n = btn_n;
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

			statusTextField.setText(taText[btn_n]);
		}
	}

	class ButtonActionListener extends JFrame implements ActionListener {
		int btn_n;

		public ButtonActionListener(int btn_n) {
			// TODO Auto-generated constructor stub
			this.btn_n = btn_n;

			addWindowListener(new java.awt.event.WindowAdapter() {
				@Override
				public void windowClosing(java.awt.event.WindowEvent windowEvent) {
					getContentPane().removeAll();
				}
			});
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

			if (btn_n == 0) {
				load();
			} else if (btn_n == 1) {
				make();
			} else if (btn_n == 2) {
				find();
			} else if (btn_n == 3) {
				save();
			} else if (btn_n == 4) {
				print();
			} else if (btn_n == 5) {
				insert();
			} else if (btn_n == 6) {
				update();
			} else if (btn_n == 7) {
				delete();
			}
			if (btn_n == 8) {
				exit();
			}
		}

		void load() {
			setTitle("Load");
			setSize(400, 150);
			setLayout(new BorderLayout());

			// Now Files
			File folder = new File("./");
			String[] xmlNames = new String[100];

			int xmls = 0;
			for (File file : folder.listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					// TODO Auto-generated method stub
					return name.endsWith(".xml");
				}
			})) {
				if (!file.isDirectory()) {
					System.out.println(file.getPath());
					xmlNames[xmls++] = file.getPath();
				}
			}

			String[] xsdNames = new String[100];

			int xsds = 0;
			for (File file : folder.listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					// TODO Auto-generated method stub
					return name.endsWith(".xsd");
				}
			})) {
				if (!file.isDirectory()) {
					System.out.println(file.getPath());
					xsdNames[xsds++] = file.getPath();
				}
			}

			// jpanels

			JPanel jPanel1 = new JPanel();
			JLabel explainLabel = new JLabel("로딩할 xml 파일과 xsd 파일을 선택하고,\n 버튼을 눌러주세요.");

			JPanel jPanel2 = new JPanel();
			jPanel2.setLayout(new FlowLayout());
			JComboBox<String> xmlsComboBox = new JComboBox<String>(Arrays.copyOfRange(xmlNames, 0, xmls));
			JComboBox<String> xsdsComboBox = new JComboBox<String>(Arrays.copyOfRange(xsdNames, 0, xsds));
			//
			xmlsComboBox.setPreferredSize(new Dimension(100, xmlsComboBox.getPreferredSize().height));
			xsdsComboBox.setPreferredSize(new Dimension(100, xsdsComboBox.getPreferredSize().height));

			JButton selectButton = new JButton("Load");

			JPanel jPanel3 = new JPanel();
			JLabel pathLabel = new JLabel(folder.getPath());
			// pathLabel.setText(folder.getAbsolutePath());
			// panel1

			explainLabel.setHorizontalAlignment(JLabel.CENTER);
			jPanel1.add(explainLabel);

			// panel2

			selectButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					String xml = (String) xmlsComboBox.getSelectedItem();
					String xsd = (String) xsdsComboBox.getSelectedItem();
					loadFile = new File(xml);
					File loadFile_XSD = new File(xsd);

					pathLabel.setText(loadFile.getPath());

					doc = func.isValid(xml, xsd);
					if (doc != null) {
						statusLabel.setText("로드된 파일 : " + loadFile.getPath());
						JOptionPane.showMessageDialog(null, "유효성 검사 완료\n성공적으로 로딩하였습니다.");

						dispose();
					} else {
						JOptionPane.showMessageDialog(null, "로딩에 실패하였습니다.");
					}
				}
			});

			jPanel2.add(xmlsComboBox);
			jPanel2.add(xsdsComboBox);
			jPanel2.add(selectButton);

			// panel3
			pathLabel.setHorizontalAlignment(JLabel.CENTER);
			jPanel3.add(pathLabel);

			add(jPanel1, BorderLayout.NORTH);
			add(jPanel2, BorderLayout.CENTER);
			add(jPanel3, BorderLayout.SOUTH);

			setLocationRelativeTo(null);
			setVisible(true);
		}

		void make() {
			setTitle("Make");
			setSize(400, 150);
			setLayout(new BorderLayout());

			// jpanels
			JPanel jPanel1 = new JPanel();
			JLabel explainLabel = new JLabel("생성할 파일명을 입력해주세요.");

			JPanel jPanel2 = new JPanel();
			JTextField fileNameTextField = new JTextField(10);
			JButton selectButton = new JButton("Make");

			JPanel jPanel3 = new JPanel();
			JLabel completeLabel = new JLabel("");
			// pathLabel.setText(folder.getAbsolutePath());
			// panel1

			explainLabel.setHorizontalAlignment(JLabel.CENTER);
			jPanel1.add(explainLabel);

			// panel2

			jPanel2.setLayout(new FlowLayout());
			selectButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					String fn = fileNameTextField.getText();
					System.out.println(fn);

					File newFile = new File(fn);
					try {
						newFile.createNewFile();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					completeLabel.setText(newFile.getPath());
					JOptionPane.showMessageDialog(null, newFile.getName() + "을 성공적으로 생성하였습니다.");

					loadFile = newFile;
					statusLabel.setText("로드된 파일 : " + newFile.getPath());
					doc = func.make(newFile.getName());

				}
			});

			jPanel2.add(fileNameTextField);
			jPanel2.add(selectButton);

			// panel3
			completeLabel.setHorizontalAlignment(JLabel.CENTER);
			jPanel3.add(completeLabel);

			add(jPanel1, BorderLayout.NORTH);
			add(jPanel2, BorderLayout.CENTER);
			add(jPanel3, BorderLayout.SOUTH);

			setLocationRelativeTo(null);
			setVisible(true);
		}

		void find() {

			if (reject()) {
				return;
			}

			setTitle("Find");
			setSize(400, 150);
			setLayout(new BorderLayout());

			// jpanels
			JPanel jPanel1 = new JPanel();
			JLabel explainLabel = new JLabel("키워드를 입력해주세요.");

			JPanel jPanel2 = new JPanel();
			JButton selectButton = new JButton("Find");

			String[] nodeListString = func.find_getNodeNameList(doc);
			JComboBox<String> nodeListComboBox = new JComboBox<String>(nodeListString);
			nodeListComboBox.setPreferredSize(new Dimension(200, nodeListComboBox.getPreferredSize().height));

			JPanel jPanel3 = new JPanel();
			// panel1

			explainLabel.setHorizontalAlignment(JLabel.CENTER);
			jPanel1.add(explainLabel);

			// panel2

			jPanel2.setLayout(new FlowLayout());
			selectButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					int index = nodeListComboBox.getSelectedIndex();
					String info = func.find(doc, index);

					JOptionPane.showMessageDialog(null, info);

					getContentPane().removeAll();
					dispose();
					find();
				}
			});
			jPanel2.add(nodeListComboBox);
			jPanel2.add(selectButton);

			// panel3
//			jPanel3.add(nodeNameTextField);

			add(jPanel1, BorderLayout.NORTH);
			add(jPanel2, BorderLayout.CENTER);
			add(jPanel3, BorderLayout.SOUTH);

			setLocationRelativeTo(null);
			setVisible(true);
		}

		void save() {

			if (reject()) {
				return;
			}
			setTitle("Save");
			setSize(400, 150);
			setLayout(new BorderLayout());

			// jpanels
			JPanel jPanel1 = new JPanel();
			JLabel explainLabel = new JLabel("저장될 파일명을 입력해주세요.");

			JPanel jPanel2 = new JPanel();
			JTextField fileNameTextField = new JTextField(10);
			JButton selectButton = new JButton("Save");

			JPanel jPanel3 = new JPanel();
			JLabel pathLabel = new JLabel("");
			// panel1

			explainLabel.setHorizontalAlignment(JLabel.CENTER);
			jPanel1.add(explainLabel);

			// panel2

			jPanel2.setLayout(new FlowLayout());
			selectButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					String fn = fileNameTextField.getText();

					// 2. FileInputStream, FileOutputStream 준비
					func.Save(doc, fn);

					JOptionPane.showMessageDialog(null, "저장이 완료되었습니다.");
					// TODO Auto-generated catch block
					dispose();
					isSaved = true;
				}

			});

			jPanel2.add(fileNameTextField);
			jPanel2.add(selectButton);

			// panel3
			pathLabel.setHorizontalAlignment(JLabel.CENTER);
			jPanel3.add(pathLabel);

			add(jPanel1, BorderLayout.NORTH);
			add(jPanel2, BorderLayout.CENTER);
			add(jPanel3, BorderLayout.SOUTH);

			setLocationRelativeTo(null);
			setVisible(true);

			return;
		}

		void print() {

			if (reject()) {
				return;
			}

			setTitle("Print");
			setSize(400, 800);
			setLayout(new BorderLayout());

			// jpanels
			JPanel jPanel1 = new JPanel();
			String outputString = "";
			JTextArea outputTextArea = new JTextArea();
			// outputTextArea.setPreferredSize(new Dimension(380, 200));
			jPanel1.add(outputTextArea);

			// string[] to string
			String content = func.print(doc);

			outputTextArea.setText(content);

			JScrollPane scrollPane = new JScrollPane(outputTextArea);
			add(scrollPane);

			// TODO Auto-generated catch block

			// 5. Stream close

			add(jPanel1, BorderLayout.NORTH);

			setLocationRelativeTo(null);
			setVisible(true);
		}

		void insert() {

			if (reject()) {
				return;
			}

			setTitle("Insert");
			setSize(400, 400);
			setLayout(new BorderLayout());

			JPanel jPanel1 = new JPanel();
			jPanel1.setLayout(new FlowLayout());

			JLabel explainLabel1 = new JLabel("추가할 노드 타입과 기준 Element 위치를 선택해주세요.");
			JLabel explainLabel2 = new JLabel(" (Element 추가 시 기준 Element 이전에 생성)");

			JPanel jPanel2 = new JPanel();
			jPanel2.setLayout(new FlowLayout());

			JPanel jPanel3 = new JPanel();
			jPanel2.setLayout(new FlowLayout());

			String[] types = new String[] { "Element", "Text", "Attribute", "Comment" };
			String[] elements = func.insert_getNodeNameList(doc);
			JComboBox<String> typeComboBox = new JComboBox<String>(types);
			JComboBox<String> elementComboBox = new JComboBox<String>(elements);
			//
			typeComboBox.setPreferredSize(new Dimension(100, typeComboBox.getPreferredSize().height));
			elementComboBox.setPreferredSize(new Dimension(300, elementComboBox.getPreferredSize().height));

			JButton selectButton = new JButton("Insert");

			explainLabel1.setHorizontalAlignment(JLabel.CENTER);
			jPanel1.add(explainLabel1);

			// panel2

			selectButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					int type = typeComboBox.getSelectedIndex();
					int element = elementComboBox.getSelectedIndex();

					if (element == 0) {
						JOptionPane.showMessageDialog(null, "최상위 노드는 변경할 수 없습니다.");
						return;
					}

					String str1, str2;
					if (type == 0) {
						str1 = JOptionPane.showInputDialog("Element 이름을 입력하세요");
						if (str1 == null || str1.length() == 0) {
							JOptionPane.showMessageDialog(null, "취소되었습니다.");
							return;
						}
						func.insert_element(doc, element, str1);
					} else if (type == 1) {
						str1 = JOptionPane.showInputDialog("Text 내용을 입력하세요");
						if (str1 == null || str1.length() == 0) {
							JOptionPane.showMessageDialog(null, "취소되었습니다.");
							return;
						}
						func.insert_text(doc, element, str1);
					} else if (type == 2) {
						str1 = JOptionPane.showInputDialog("Attribute 이름을 입력하세요");
						str2 = JOptionPane.showInputDialog("Attribute 내용을 입력하세요");

						if (str1 == null || str1.length() == 0) {
							JOptionPane.showMessageDialog(null, "취소되었습니다.");
							return;
						}
						if (str2 == null || str2.length() == 0) {
							JOptionPane.showMessageDialog(null, "취소되었습니다.");
							return;
						}

						func.insert_attribute(doc, element, str1, str2);
					} else if (type == 3) {
						str1 = JOptionPane.showInputDialog("Comment 내용을 입력하세요");

						if (str1 == null || str1.length() == 0) {
							JOptionPane.showMessageDialog(null, "취소되었습니다.");
							return;
						}

						func.insert_comment(doc, element, str1);
					}

					JOptionPane.showMessageDialog(null, "삽입에 성공했습니다.");

					getContentPane().removeAll();

					dispose();
					insert();
				}
			});

			jPanel2.add(typeComboBox);
			jPanel2.add(elementComboBox);
			jPanel2.add(selectButton);

			explainLabel2.setHorizontalAlignment(JLabel.CENTER);
			jPanel3.add(explainLabel2);

			//
			add(jPanel1, BorderLayout.NORTH);
			add(jPanel2, BorderLayout.CENTER);
			add(jPanel3, BorderLayout.SOUTH);

			setLocationRelativeTo(null);
			setVisible(true);
		}

		void update() {
			if (reject()) {
				return;
			}

			setTitle("Update");
			setSize(400, 400);
			setLayout(new BorderLayout());

			JPanel jPanel1 = new JPanel();
			jPanel1.setLayout(new FlowLayout());

			JLabel explainLabel1 = new JLabel("수정할 타입과 기준 Element 위치를 선택해주세요.");

			JPanel jPanel2 = new JPanel();
			jPanel2.setLayout(new FlowLayout());

			JPanel jPanel3 = new JPanel();
			jPanel2.setLayout(new FlowLayout());

			String[] types = new String[] { "Element", "Text", "Attribute", "Comment" };
			String[] elements = func.update_getNodeNameList(doc);
			JComboBox<String> typeComboBox = new JComboBox<String>(types);
			JComboBox<String> elementComboBox = new JComboBox<String>(elements);
			//
			typeComboBox.setPreferredSize(new Dimension(100, typeComboBox.getPreferredSize().height));
			elementComboBox.setPreferredSize(new Dimension(300, elementComboBox.getPreferredSize().height));

			JButton selectButton = new JButton("Update");

			explainLabel1.setHorizontalAlignment(JLabel.CENTER);
			jPanel1.add(explainLabel1);

			// panel2

			selectButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					int type = typeComboBox.getSelectedIndex();
					int element = elementComboBox.getSelectedIndex();

					String str1, str2;
					if (type == 0) {
						// element

						str1 = JOptionPane.showInputDialog("새 Element 이름을 입력하세요");
						if (str1 == null || str1.length() == 0) {
							JOptionPane.showMessageDialog(null, "취소되었습니다.");
							return;
						}
						func.update_element(doc, element, str1);
						JOptionPane.showMessageDialog(null, "수정에 성공했습니다.");

						getContentPane().removeAll();
						dispose();
						update();
					} else if (type == 1) {
						// text
						getContentPane().removeAll();
						dispose();
						update_(type, element);

					} else if (type == 2) {
						// attribute
						getContentPane().removeAll();
						dispose();
						update_(type, element);
					} else if (type == 3) {
						// comment
						getContentPane().removeAll();
						dispose();
						update_(type, element);
					}

				}
			});

			jPanel2.add(typeComboBox);
			jPanel2.add(elementComboBox);
			jPanel2.add(selectButton);

			add(jPanel1, BorderLayout.NORTH);
			add(jPanel2, BorderLayout.CENTER);
			add(jPanel3, BorderLayout.SOUTH);

			setLocationRelativeTo(null);
			setVisible(true);
		}

		void update_(int type, int element) {
			if (reject()) {
				return;
			}

			setTitle("Update");
			setSize(400, 400);
			setLayout(new BorderLayout());

			JPanel jPanel1 = new JPanel();
			jPanel1.setLayout(new FlowLayout());

			JLabel explainLabel1 = new JLabel("수정할 내용을 구체적으로 선택해주세요.");

			JPanel jPanel2 = new JPanel();
			jPanel2.setLayout(new FlowLayout());

			JPanel jPanel3 = new JPanel();
			jPanel2.setLayout(new FlowLayout());

			String[] elements = func.update_getContentNameList(doc, type, element);

			if (elements == null || elements.length == 0) {
				JOptionPane.showMessageDialog(null, "삭제할 내용이 해당 Element에 존재하지 않습니다.");

				getContentPane().removeAll();
				dispose();
				delete();
				return;
			}

			JComboBox<String> elementComboBox = new JComboBox<String>(elements);
			//
			elementComboBox.setPreferredSize(new Dimension(300, elementComboBox.getPreferredSize().height));

			JButton selectButton = new JButton("Update");

			explainLabel1.setHorizontalAlignment(JLabel.CENTER);
			jPanel1.add(explainLabel1);

			// panel2

			selectButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					int content = elementComboBox.getSelectedIndex();

					String str1, str2;
					if (type == 1) {
						// text
						str1 = JOptionPane.showInputDialog("새 Text 이름을 입력하세요");
						if (str1 == null || str1.length() == 0) {
							JOptionPane.showMessageDialog(null, "취소되었습니다.");
							return;
						}

						func.update_text(doc, element, content, str1);
						JOptionPane.showMessageDialog(null, "수정에 성공했습니다.");
						getContentPane().removeAll();
						dispose();
						update();
					} else if (type == 2) {
						str1 = JOptionPane.showInputDialog("새 Attribute 이름을 입력하세요");
						str2 = JOptionPane.showInputDialog("새 Attribute 내용을 입력하세요");

						if (str1 == null || str1.length() == 0) {
							JOptionPane.showMessageDialog(null, "취소되었습니다.");
							return;
						}
						if (str2 == null || str2.length() == 0) {
							JOptionPane.showMessageDialog(null, "취소되었습니다.");
							return;
						}

						func.update_attribute(doc, element, content, str1, str2);
						JOptionPane.showMessageDialog(null, "수정에 성공했습니다.");
						getContentPane().removeAll();
						dispose();
						update();
					} else if (type == 3) {
						str1 = JOptionPane.showInputDialog("새 Comment 이름을 입력하세요");
						if (str1 == null || str1.length() == 0) {
							JOptionPane.showMessageDialog(null, "취소되었습니다.");
							return;
						}

						func.update_comment(doc, element, content, str1);
						JOptionPane.showMessageDialog(null, "수정에 성공했습니다.");
						getContentPane().removeAll();
						dispose();
						update();
					}
				}
			});

			jPanel2.add(elementComboBox);
			jPanel2.add(selectButton);

			//
			add(jPanel1, BorderLayout.NORTH);
			add(jPanel2, BorderLayout.CENTER);
			add(jPanel3, BorderLayout.SOUTH);

			setLocationRelativeTo(null);
			setVisible(true);

		}

		void delete() {
			if (reject()) {
				return;
			}

			setTitle("Delete");
			setSize(400, 400);
			setLayout(new BorderLayout());

			JPanel jPanel1 = new JPanel();
			jPanel1.setLayout(new FlowLayout());

			JLabel explainLabel1 = new JLabel("삭제할 타입과 기준 Element 위치를 선택해주세요.");

			JPanel jPanel2 = new JPanel();
			jPanel2.setLayout(new FlowLayout());

			JPanel jPanel3 = new JPanel();
			jPanel2.setLayout(new FlowLayout());

			String[] types = new String[] { "Element", "Text", "Attribute", "Comment" };
			String[] elements = func.delete_getNodeNameList(doc);
			JComboBox<String> typeComboBox = new JComboBox<String>(types);
			JComboBox<String> elementComboBox = new JComboBox<String>(elements);
			//
			typeComboBox.setPreferredSize(new Dimension(100, typeComboBox.getPreferredSize().height));
			elementComboBox.setPreferredSize(new Dimension(300, elementComboBox.getPreferredSize().height));

			JButton selectButton = new JButton("Delete");

			explainLabel1.setHorizontalAlignment(JLabel.CENTER);
			jPanel1.add(explainLabel1);

			// panel2

			selectButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					int type = typeComboBox.getSelectedIndex();
					int element = elementComboBox.getSelectedIndex();

					if (type == 0) {
						// element
						func.delete_element(doc, element);
						JOptionPane.showMessageDialog(null, "삭제에 성공했습니다.");
						getContentPane().removeAll();
						dispose();
						delete();
					} else if (type == 1) {
						// text
						getContentPane().removeAll();
						dispose();
						delete_(type, element);

					} else if (type == 2) {
						// attribute
						getContentPane().removeAll();
						dispose();
						delete_(type, element);
					} else if (type == 3) {
						// comment
						getContentPane().removeAll();
						dispose();
						delete_(type, element);
					}
				}
			});

			jPanel2.add(typeComboBox);
			jPanel2.add(elementComboBox);
			jPanel2.add(selectButton);

			//
			add(jPanel1, BorderLayout.NORTH);
			add(jPanel2, BorderLayout.CENTER);
			add(jPanel3, BorderLayout.SOUTH);

			setLocationRelativeTo(null);
			setVisible(true);
		}

		void delete_(int type, int element) {
			if (reject()) {
				return;
			}

			setTitle("Delete");
			setSize(400, 400);
			setLayout(new BorderLayout());

			JPanel jPanel1 = new JPanel();
			jPanel1.setLayout(new FlowLayout());

			JLabel explainLabel1 = new JLabel("삭제할 내용을 구체적으로 선택해주세요.");

			JPanel jPanel2 = new JPanel();
			jPanel2.setLayout(new FlowLayout());

			JPanel jPanel3 = new JPanel();
			jPanel2.setLayout(new FlowLayout());

			String[] elements = func.delete_getContentNameList(doc, type, element);

			if (elements == null || elements.length == 0) {
				JOptionPane.showMessageDialog(null, "삭제할 타입이 해당 Element에 존재하지 않습니다.");

				getContentPane().removeAll();
				dispose();
				delete();
				return;
			}

			JComboBox<String> elementComboBox = new JComboBox<String>(elements);
			//
			elementComboBox.setPreferredSize(new Dimension(300, elementComboBox.getPreferredSize().height));

			JButton selectButton = new JButton("Delete");

			explainLabel1.setHorizontalAlignment(JLabel.CENTER);
			jPanel1.add(explainLabel1);

			// panel2

			selectButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					int content = elementComboBox.getSelectedIndex();

					if (type == 1) {
						// text
						func.delete_text(doc, element, content);
						JOptionPane.showMessageDialog(null, "삭제에 성공했습니다.");
						getContentPane().removeAll();
						dispose();
						delete();
					} else if (type == 2) {
						func.delete_attr(doc, element, content);
						JOptionPane.showMessageDialog(null, "삭제에 성공했습니다.");
						getContentPane().removeAll();
						dispose();
						delete();
					} else if (type == 3) {
						func.delete_comment(doc, element, content);
						JOptionPane.showMessageDialog(null, "삭제에 성공했습니다.");
						getContentPane().removeAll();
						dispose();
						delete();
					}
				}
			});

			jPanel2.add(elementComboBox);
			jPanel2.add(selectButton);

			//
			add(jPanel1, BorderLayout.NORTH);
			add(jPanel2, BorderLayout.CENTER);
			add(jPanel3, BorderLayout.SOUTH);

			setLocationRelativeTo(null);
			setVisible(true);

		}

		void exit() {
			if (!isSaved) {
				save();
			} else {
				System.exit(0);
			}
		}

		boolean reject() {
			if (loadFile == null) {
				JOptionPane.showMessageDialog(null, "Load나 Make를 먼저 실행해주세요.");
				return true;
			}
			return false;
		}
	}
}
