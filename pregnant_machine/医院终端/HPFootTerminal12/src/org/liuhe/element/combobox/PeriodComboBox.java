package org.liuhe.element.combobox;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ScrollPaneConstants;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

import org.liuhe.common.ui.VGrayScrollBarUI;

public class PeriodComboBox extends JComboBox{
	private static final long serialVersionUID = 1L;

	public PeriodComboBox(Object[] items){
		super(items);
		setBorder(BorderFactory.createLineBorder(new Color(171, 173, 179)));
		setUI(new IComboBoxUI());
		setRenderer(new IComboBoxRenderer());
		setFont(new Font("΢���ź�", Font.PLAIN, 20));
	}
	
	private class IComboBoxRenderer implements ListCellRenderer {
		private DefaultListCellRenderer defaultCellRenderer = new DefaultListCellRenderer();
		private Color selectBackColor = Color.WHITE;
		private Color selectForeColor = new Color(40,40,40);
		private int align = JLabel.LEFT;
		public IComboBoxRenderer() {
			super();
		}
		public Component getListCellRendererComponent(JList list, Object value,int index, boolean isSelected, boolean cellHasFocus) {
			 JLabel cell = (JLabel)defaultCellRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			 if(isSelected){
				 //cell.setBackground(new Color(51,153,255));	//�㿪�����б�����ƶ�ʱ��ָ����ı�����ɫ������ɫ
				 cell.setBackground(new Color(144,197,72));
				 cell.setForeground(Color.WHITE);
			 }else{
				 cell.setBackground(Color.WHITE);
				 cell.setForeground(new Color(40,40,40));
			 }
			 cell.setBorder(BorderFactory.createEmptyBorder(6, 20, 6, 5));
//			 cell.setFont(new Font("΢���ź�", Font.PLAIN, 20));
			 cell.setHorizontalAlignment(align);				//�������б��еĶ��뷽ʽ
			 //��comboBox��ý���ʱ��
			 list.setSelectionBackground(selectBackColor);		//JComBoBox��ǰ��ѡ��ı�����ɫ
			 list.setSelectionForeground(selectForeColor);		//JComBoBox��ǰ��ѡ�������ǰ����ɫ
			 list.setFont(new Font("΢���ź�", Font.PLAIN, 20));	//���������б����������ʽ
			 return cell;
		}
	}
	
	private class IComboBoxUI extends BasicComboBoxUI {
		 private Arrow arrow;
		 private boolean boundsLight = false;
		 private Color border_color = null;
		 private boolean isbar = false;
		 
		 public IComboBoxUI() {
			 super();
			 setArrowButton();
		 }
		 protected JButton createArrowButton() {
			 return arrow;
		 }
		 public void setArrowButton(){
			 arrow = new Arrow();
			 border_color = new Color(171, 173, 179);
			 isbar = true;
		 }
		 
		 public void paint(Graphics g, JComponent c) {
			 hasFocus = comboBox.hasFocus();						//comboBox�Ƿ��ý���
			 Graphics2D g2 = (Graphics2D)g;
			 if (!comboBox.isEditable()) {							//comboBoxĬ��Ϊfalse�����ɱ༭�¡�
				 Rectangle r = rectangleForCurrentValue();
				 //�ص�:JComboBox��textfield �Ļ��� �����ǿ�Renderer������
				 paintCurrentValueBackground(g2, r, hasFocus); 		//����ͨ��paintCurrentValueBackground�����Ʊ���
				 paintCurrentValue(g2, r, hasFocus);				//Ȼ��ͨ��paintCurrentValue();ȥ����JComboBox����ʾ��ֵ
			 }
		 }
		 
		 //�ػ�JComboBox�е�����
		 public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
			 super.paintCurrentValue(g, bounds, hasFocus);
		 }
		 public Dimension getPreferredSize(JComponent c) {
			 return super.getPreferredSize(c);
		 }
		 public boolean isBoundsLight() {
			 return boundsLight;
		 }
		 public void setBoundsLight(boolean boundsLight) {
			 this.boundsLight = boundsLight;
		 }

		 protected ComboPopup createPopup() {
			 ComboPopup popup = new BasicComboPopup(comboBox) {
				private static final long serialVersionUID = 1L;
				protected JScrollPane createScroller() {
					if(isbar){
						JScrollPane sp = new JScrollPane(list,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
						sp.setHorizontalScrollBar(null);
						JScrollBar v_bar = new JScrollBar();
				    	v_bar.setUnitIncrement(80);
						v_bar.setUI(new VGrayScrollBarUI());
						sp.setVerticalScrollBar(v_bar);
						return sp;
					}
					return super.createScroller();
				 }
				 //����paintBorder���� ������������Ҫ�ı߿�..
				 public void paintBorder(Graphics g){
					 Graphics2D g2 = (Graphics2D) g;
					 g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
					 g2.setColor(border_color);
					 g2.drawRoundRect(0,-arrow.getHeight(),getWidth()-1,getHeight()+arrow.getHeight()-1,0,0);
				 }
			 };
			 return popup;
		 }
	}
	
	private class Arrow extends JButton{
		private static final long serialVersionUID = 1L;
		private boolean pressed = false;
		private int differ_y = 7;
		private int differ_x = 5;
		public Arrow(){
			setMargin(new Insets(0,0,0,0));
		    setHideActionText(true);
		    setBorderPainted(false);
		    setFocusPainted(false);
		    setContentAreaFilled(false);
		    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		    addMouseListener(new MouseAdapter() {
		    	public void mousePressed(MouseEvent e){
	            	pressed = true;
	                repaint();
	            }
		    	public void mouseReleased(MouseEvent e) {
	            	pressed = false;
	                repaint();
	            }
	        });
		}
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g.create();
			int height = this.getHeight();
			int width = this.getWidth();  
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
	        g2.setPaint(new GradientPaint(0, 0, new Color(144,197,72), width/2, height/2,new Color(160,234,57),true));
	        //g2.setPaint(new GradientPaint(0, 0, new Color(0,111,228), width/2, height/2,new Color(0,185,255),true));
	        /*GeneralPath path = new GeneralPath();
	        path.moveTo(0,height-1);
	        path.moveTo(width-1,height-1);
	        if(pressed){
	        	path.moveTo((width-1)/2.0f,1);
	        }else{
	        	path.moveTo((width-1)/2.0f,0);
	        }
	        path.closePath();
	        g2.fill(path);*/
	        Polygon path = new Polygon();
        	path.addPoint(0+differ_x,0+differ_y);
            path.addPoint(width-1-differ_x,0+differ_y);
            if(pressed){
            	path.addPoint((width-1)/2,height-2-differ_y+1);
            	g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
            }else{
            	path.addPoint((width-1)/2,height-1-differ_y+1);
            	g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            }
	        g2.fillPolygon(path);
		}
	}

}