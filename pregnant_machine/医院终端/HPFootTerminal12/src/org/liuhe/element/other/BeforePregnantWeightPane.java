package org.liuhe.element.other;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.liuhe.background.pane.BackRoundPane;
import org.liuhe.element.button.DeleteButton;
import org.liuhe.element.button.NumButton;
import org.liuhe.element.field.RoundTextField;

public class BeforePregnantWeightPane extends JPanel{
	private static final long serialVersionUID = 1L;
	private ArrayList<FastLabel> fastList = null;
	private RoundTextField stature_text = null;
	private DeleteButton deleteBut = null;
	private Action_listener listener = new Action_listener();
	
	public BeforePregnantWeightPane(){
		initPanel();
	}
	
	public String getStature(){
		return stature_text.getText();
	}
	public void initStature(){
		stature_text.setText("");
		for(int i=0;i<fastList.size();i++){
			fastList.get(i).setSelected(false);
		}
	}
	
	private void initPanel(){
		setOpaque(false);
		setLayout(new BorderLayout(10,10));
		
		//绘制顶部身高文本输入框
		BackRoundPane textPane = new BackRoundPane();
		textPane.setDrawColor(new Color(215,217,218), new Color(180,180,180));
		textPane.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
		add(textPane,BorderLayout.NORTH);
		
//		JLabel label_stature = new JLabel("身高：");
//		label_stature.setFont(new Font("微软雅黑", Font.PLAIN, 24));
//		label_stature.setForeground(new Color(40,40,40));
//		textPane.add(label_stature);
		
		stature_text = new RoundTextField();
		stature_text.setEditable(false);
		stature_text.setBackground(Color.WHITE);
		stature_text.setForeground(new Color(40,40,40));
		stature_text.setLeft(10);
		stature_text.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		stature_text.setPreferredSize(new Dimension(200,40));
		textPane.add(stature_text);
		
		deleteBut = new DeleteButton();
		deleteBut.setPreferredSize(new Dimension(50,40));
		textPane.add(deleteBut);
		deleteBut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(!stature_text.getText().equals("")){
					stature_text.setText(stature_text.getText().substring(0,stature_text.getText().length()-1));
					for(int i=0;i<fastList.size();i++){
						fastList.get(i).setSelected(false);
					}
				}
			}
		});
		
		//绘制中间层面板
		JPanel mainPane = new JPanel();
		mainPane.setOpaque(false);
		mainPane.setLayout(new BorderLayout(0,0));
		add(mainPane,BorderLayout.CENTER);
		
		//快速选择面板
		JPanel westPane = new JPanel();
		westPane.setOpaque(false);
		westPane.setLayout(new BorderLayout(0,0));
		mainPane.add(westPane,BorderLayout.WEST);
		
		JLabel fastTitle = new JLabel("快 速 选 择");
		fastTitle.setHorizontalAlignment(JLabel.CENTER);
		fastTitle.setFont(new Font("黑体", Font.PLAIN, 20));
		fastTitle.setForeground(new Color(120,120,120));
		westPane.add(fastTitle,BorderLayout.NORTH);
		
		JPanel fastPane = new JPanel();
		fastPane.setBackground(new Color(160,160,160));
		fastPane.setLayout(new GridLayout(4,5,1,1));
		fastPane.setBorder(BorderFactory.createLineBorder(new Color(160,160,160)));
		fastPane.setPreferredSize(new Dimension(300-4,200-3));
		westPane.add(fastPane,BorderLayout.CENTER);
		
		String[][] fastArr = new String[][]{
				{"45","46","47","48","49"},
				{"50","51","52","53","54"},
				{"55","56","57","58","59"},
				{"60","61","62","63","64"}};
		fastList = new ArrayList<FastLabel>();
		for(int i=0;i<fastArr.length;i++){
			for(int j=0;j<fastArr[0].length;j++){
				FastLabel fastLabel = new FastLabel(fastArr[i][j]);
				fastPane.add(fastLabel);
				fastList.add(fastLabel);
			}
		}
		
		//快速面板和自定义面板的中间分割线
		JPanel space = new JPanel();
		space.setBackground(Color.WHITE);
		space.setPreferredSize(new Dimension(10,space.getHeight()));
		mainPane.add(space,BorderLayout.CENTER);
		
		//自定义输入面板
		JPanel eastPane = new JPanel();
		eastPane.setOpaque(false);
		eastPane.setLayout(new BorderLayout(0,0));
		mainPane.add(eastPane,BorderLayout.EAST);
		
		JLabel selfTitle = new JLabel("自 定 义 选 择");
		selfTitle.setHorizontalAlignment(JLabel.CENTER);
		selfTitle.setFont(new Font("黑体", Font.PLAIN, 20));
		selfTitle.setForeground(new Color(120,120,120)); 
		eastPane.add(selfTitle,BorderLayout.NORTH);
		
		JPanel selfPane = new JPanel();
		selfPane.setBackground(new Color(240,240,240));
		selfPane.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
		selfPane.setBorder(BorderFactory.createLineBorder(new Color(160,160,160)));
		selfPane.setPreferredSize(new Dimension(300-48,200-3));
		eastPane.add(selfPane,BorderLayout.CENTER);
		
		//String[] selfArr = new String[]{"7","8","9","4","5","6","1","2","3","0","."};
		String[] selfArr = new String[]{"1","2","3","4","5","6","7","8","9","0","."};
		for(int i=0;i<selfArr.length;i++){
			NumButton numButton = new NumButton(selfArr[i]);
			numButton.addActionListener(listener);
			numButton.setPreferredSize(new Dimension(50,50));
			selfPane.add(numButton);
		}
	}
	
	private class FastLabel extends JLabel implements MouseListener{
		private static final long serialVersionUID = 1L;
		private Font font = new Font("宋体",Font.PLAIN,20);
		private boolean isSelected;
		public FastLabel(String text){
			super(text, JLabel.CENTER);
			this.addMouseListener(this);
            this.setFont(font);
            this.setForeground(Color.BLACK);
		}
		public boolean isSelected(){
            return isSelected;
        }
		public void setSelected(boolean b){
            isSelected = b;
            this.repaint();
        }
		protected void paintComponent(Graphics g){
        	if(isSelected){
        		g.setColor(new Color(160,185,215));
        	}else{
        		g.setColor(new Color(240,240,240));
        	}
        	g.fillRect(0,0,getWidth(),getHeight());
        	super.paintComponent(g);
        }
		public void mousePressed(MouseEvent e) {
			for(int i=0;i<fastList.size();i++){
				fastList.get(i).setSelected(false);
			}
			setSelected(true);
			stature_text.setText(this.getText());
        }
        public void mouseReleased(MouseEvent e) {
        }
        public void mouseClicked(MouseEvent e) {
        }
        public void mouseEntered(MouseEvent e) {
        }
        public void mouseExited(MouseEvent e) {
        }
	}
	
	private class Action_listener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			NumButton button = (NumButton) e.getSource();
			stature_text.setText(stature_text.getText()+button.getTitle());
			for(int i=0;i<fastList.size();i++){
				fastList.get(i).setSelected(false);
			}
		}
	}

}