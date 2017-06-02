package org.liuhe.element.field;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import org.liuhe.common.ui.VGrayScrollBarUI;
import org.liuhe.common.util.ScanUtil;

/**
 * 文本框式弹出面板,JPanel为载体
 * 
 * */
public class SourceTextField extends JTextField{
	private static final long serialVersionUID = 1L;
	private JDialog dialog;
	private String[] arr_source;
	private String source;
	
	public boolean isShow = false;
    private Popup pop;
	private JPanel popoPane;
	
	private DefaultListModel model;
	private JList list;
	private JButton button_test;
	private JButton button_sure;
	
	private Font gen_font = new Font("微软雅黑", Font.PLAIN, 12);
	private Action_listener listener = new Action_listener();

	int width = 240;
	int height = 150;
	public SourceTextField(JDialog dialog,String source,String[] arr_source){
		this.dialog = dialog;
		this.source = source;
		this.arr_source = arr_source;
		initPane();
		initListener();
	}
	
	private void initPane(){
		popoPane = new JPanel();
    	popoPane.setPreferredSize(new Dimension(width,height));
    	popoPane.setLayout(new BorderLayout(0,0));
        popoPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        model = new DefaultListModel();
        int selectIndex = -1;
        if(arr_source!=null){
        	for(int i=0;i<arr_source.length;i++){
        		model.addElement((arr_source[i]));
        		if(selectIndex==-1&&arr_source[i].equals(source)){
        			selectIndex = i;
        		}
        	}        	
        }
        list = new JList(model);
        list.setFont(gen_font);
        if(selectIndex!=-1){
        	list.setSelectedIndex(selectIndex);
        }
        JScrollPane scroll_list_pane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll_list_pane.getVerticalScrollBar().setUI(new VGrayScrollBarUI());
        scroll_list_pane.setBorder(BorderFactory.createEmptyBorder());
        scroll_list_pane.setOpaque(false);
        scroll_list_pane.getViewport().setOpaque(false);
        scroll_list_pane.getViewport().setView(list);
        popoPane.add(scroll_list_pane,BorderLayout.CENTER);
        
        JPanel button_pane = new JPanel();
        button_pane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
        button_pane.setLayout(new FlowLayout(FlowLayout.RIGHT,8,6));
        popoPane.add(button_pane,BorderLayout.SOUTH);
        button_test = new JButton("扫 描 测 试");
        button_test.setFont(gen_font);
        button_test.setPreferredSize(new Dimension(100,22));
        button_test.addActionListener(listener);
        button_sure = new JButton("确 定");
        button_sure.setFont(gen_font);
        button_sure.setPreferredSize(new Dimension(65,22));
        button_sure.addActionListener(listener);
        button_pane.add(button_test);
        button_pane.add(button_sure);
	}
	
	private void initListener(){
		this.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				if(SourceTextField.this.isEnabled()){
                    if(isShow){
                        hide_pop();
                    }else{
                    	show_pop(SourceTextField.this);
                    }
                }
			}
		});
        this.addAncestorListener(new AncestorListener(){
            public void ancestorAdded(AncestorEvent event) {
            }
            public void ancestorRemoved(AncestorEvent event) {
            }
            public void ancestorMoved(AncestorEvent event) {
                hide_pop();
            }
        });
    	this.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent me){
            	if(SourceTextField.this.isEnabled()){
                    if(isShow){
                        hide_pop();
                    }else{
                    	show_pop(SourceTextField.this);
                    }
                }
            }
        });
    	/*this.addFocusListener(new FocusListener(){
  			public void focusGained(FocusEvent e){
  				if(isShow){
                    hide_pop();
                }else{
                	show_pop(SourceTextField.this);
                }
  			}
  			public void focusLost(FocusEvent e){
  				hide_pop();	
  			}
        });*/
	}
	
    public void hide_pop(){
        if(pop!=null){
            isShow = false;
            pop.hide();
            pop = null;
        }
    }
    
    private void show_pop(Component owner){
    	if(pop!=null){
            pop.hide();
        }
        Point show = new Point(0,this.getHeight());
        SwingUtilities.convertPointToScreen(show,this);
        int x = show.x;
        int y = show.y;
        pop = PopupFactory.getSharedInstance().getPopup(owner,popoPane,x,y);
        pop.show();
        isShow = true;
    }
	
	class Action_listener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==button_sure){
				SourceTextField.this.setText((String)list.getSelectedValue());
				hide_pop();
			}else if(e.getSource()==button_test){
				Thread thread = new Thread(){
					public void run(){
						boolean success = ScanUtil.testScan(null);
						//boolean success = ScanUtil.testScan((String)list.getSelectedValue());
						if(success){
							JOptionPane.showMessageDialog(dialog,"扫描测试成功。","提示",JOptionPane.INFORMATION_MESSAGE);
						}else{
							JOptionPane.showMessageDialog(dialog,"扫描测试失败！","提示",JOptionPane.WARNING_MESSAGE);
						}
					}
				};
				thread.start();
			}
		}
	}
//end...SourceTextField.java
}