package org.liuhe.element.other;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.liuhe.background.pane.BackRoundPane;
import org.liuhe.element.combobox.PeriodComboBox;

public class PeriodDayPane extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private Calendar select = Calendar.getInstance();
	private final LabelManager dayManager = new LabelManager();
	private PeriodComboBox yearComboBox;
	private PeriodComboBox monthComboBox;
	private Item_Listener item_listener = new Item_Listener();
	private DateWeekBar weekBar;
	private DateMainPane mainPane;
	
	private boolean labelChange = false;
	
	public PeriodDayPane() {
        this(new Date());
    }
    public PeriodDayPane(Date date){
//    	select.setTime(date);
    	SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
    	Calendar cc = Calendar.getInstance();
		cc.setTime(date);
		cc.set(Calendar.DATE, cc.get(Calendar.DATE) - 1);
		try {
			Date endDate = dft.parse(dft.format(cc.getTime()));
			select.setTime(endDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			select.setTime(date);
		}
        initPanel();
    }
    
    public Date getDate(){
        return select.getTime();
    }
    public String getDateStr(){
    	return new SimpleDateFormat("yyyy-MM-dd").format(getDate());
    }
    
    private void initPanel(){
    	setOpaque(false);
    	setLayout(new BorderLayout(10,10));
    	
    	createRountPane();
    	
    	JPanel dayPane = new JPanel();
    	dayPane.setOpaque(false);
    	dayPane.setLayout(new BorderLayout());
    	dayPane.setBorder(BorderFactory.createLineBorder(new Color(160,160,160)));
    	add(dayPane,BorderLayout.CENTER);
    	
    	weekBar = new DateWeekBar();
    	weekBar.setPreferredSize(new Dimension(399,32));
    	dayPane.add(weekBar,BorderLayout.NORTH);
        
        mainPane = new DateMainPane();
        //mainPane.setBackground(Color.WHITE);
        mainPane.setPreferredSize(new Dimension(399,276));
        dayPane.add(mainPane,BorderLayout.CENTER);
    }
    
    private void createRountPane(){
    	BackRoundPane comboboxPane = new BackRoundPane();
    	comboboxPane.setDrawColor(new Color(215,217,218), new Color(180,180,180));
		comboboxPane.setPreferredSize(new Dimension(comboboxPane.getWidth(),60));
		comboboxPane.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
		add(comboboxPane,BorderLayout.NORTH);
		
		int year = select.get(Calendar.YEAR);
		String[] yearArr = {year-1+"",year+""};
		yearComboBox = new PeriodComboBox(yearArr);
		yearComboBox.setSelectedItem(year+"");
		yearComboBox.setPreferredSize(new Dimension(120,40));
		yearComboBox.addItemListener(item_listener);
		comboboxPane.add(yearComboBox);
		
		JLabel label_year = new JLabel("年");
		label_year.setFont(new Font("微软雅黑", Font.PLAIN+Font.BOLD, 24));
		label_year.setForeground(new Color(40,40,40));
		comboboxPane.add(label_year);
		
		String[] monthArr = {"1","2","3","4","5","6","7","8","9","10","11","12"};
		monthComboBox = new PeriodComboBox(monthArr);
		monthComboBox.setSelectedItem((select.get(Calendar.MONTH)+1)+"");
		monthComboBox.setPreferredSize(new Dimension(100,40));
		monthComboBox.addItemListener(item_listener);//setMaximumRowCount
		monthComboBox.setMaximumRowCount(12);
		comboboxPane.add(monthComboBox);
		
		JLabel label_month = new JLabel("月");
		label_month.setFont(new Font("微软雅黑", Font.PLAIN+Font.BOLD, 24));
		label_month.setForeground(new Color(40,40,40));
		comboboxPane.add(label_month);
		
    }
    
    private class DateWeekBar extends JPanel{
		private static final long serialVersionUID = 1L;
		private Font font = new Font("宋体",Font.PLAIN,20);//微软雅黑+Font.BOLD黑体
		private Color color_font = new Color(40,40,40);
		private Color color_line = new Color(230,230,230);
        protected void paintComponent(Graphics g){
        	Graphics2D g2 = (Graphics2D) g;
        	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        	g2.setColor(Color.WHITE);
        	g2.fillRect(0, 0, getWidth(), getHeight());
        	
        	g2.setColor(color_font);
            g2.setFont(font);
            g2.drawString("日 ",15,21);
            g2.drawString("一 ",75,21);
            g2.drawString("二 ",135-3,21);
            g2.drawString("三",195-5,21);
            g2.drawString("四",255-10,21);
            g2.drawString("五",315-13,21);
            g2.drawString("六",375-18,21);
            g2.setColor(color_line);
            g2.drawLine(0,28,getWidth(),28);
        }
    }
    
    private class DateMainPane extends JPanel{
		private static final long serialVersionUID = 1L;
        public DateMainPane(){
            super(new GridLayout(6,7));
            updateDate();
        }
        public void updateDate(){
            this.removeAll();
            dayManager.clear();
            Calendar calender = Calendar.getInstance();
            calender.setTime(select.getTime());
            calender.set(Calendar.DAY_OF_MONTH,1);
            int index = calender.get(Calendar.DAY_OF_WEEK);
            int sum = (index==1?8:index);
            calender.add(Calendar.DAY_OF_MONTH,0-sum);
            for(int i=0;i<42;i++){
            	calender.add(Calendar.DAY_OF_MONTH,1);
                dayManager.addLabel(new DayLabel(calender.get(Calendar.YEAR),calender.get(Calendar.MONTH),calender.get(Calendar.DAY_OF_MONTH)));
            }
            for(DayLabel my:dayManager.getLabels()){
                this.add(my);
            }
            SwingUtilities.updateComponentTreeUI(this);
        }
    }
    private class DayLabel extends JLabel implements Comparator<DayLabel>,MouseListener{
		private static final long serialVersionUID = 1L;
		private int year,month,day;
        private boolean isSelected;
        private boolean islight = false;
        private Font font = new Font("宋体",Font.PLAIN,20);
        public DayLabel(int year,int month,int day){
            super(""+day,JLabel.CENTER);
            this.year = year;
            this.day = day;
            this.month = month;
            this.addMouseListener(this);
            this.setFont(font);
            //当月为黑色字体重点显示
            if(month==select.get(Calendar.MONTH)){
                this.setForeground(Color.BLACK);
            }else{
                this.setForeground(Color.LIGHT_GRAY);
                islight = true;
            }
            if(year==select.get(Calendar.YEAR)&&month==select.get(Calendar.MONTH)&&day==select.get(Calendar.DAY_OF_MONTH)){
            	isSelected = true;
            }
        }
        public boolean isSelected(){
            return isSelected;
        }
        public boolean isLight(){
        	return islight;
        }
        public void setSelected(boolean b){
            isSelected = b;
            if(isSelected){
            	 select.set(year,month,day);
            }
            this.repaint();
        }
        public int getYear(){
        	return year;
        }
        public int getMonth(){
        	return month;
        }
        public int getDay(){
        	return day;
        }
        protected void paintComponent(Graphics g){
        	if(isSelected){
        		//g.setColor(new Color(160,185,215));
        		g.setColor(new Color(185,205,178));
                g.fillRect(0,0,getWidth(),getHeight());
        	}
        	/*if(year==select.get(Calendar.YEAR)&&month==select.get(Calendar.MONTH)&&day==select.get(Calendar.DAY_OF_MONTH)){
        		g.setColor(new Color(160,185,215));
                g.fillRect(0,0,getWidth(),getHeight());
        	}
            if(year==now.get(Calendar.YEAR)&&month==now.get(Calendar.MONTH)&&day==now.get(Calendar.DAY_OF_MONTH)){
                //如果日期和当前日期一样,则用红框
                Graphics2D gd=(Graphics2D)g;
                gd.setColor(Color.RED);
                Polygon p=new Polygon();
                p.addPoint(0,0);
                p.addPoint(getWidth()-1,0);
                p.addPoint(getWidth()-1,getHeight()-1);
                p.addPoint(0,getHeight()-1);
                gd.drawPolygon(p);
            }
            //如果被选中了就画出一个虚线框出来
            if(isSelected){
                Stroke s = new BasicStroke(1.0f,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_BEVEL,1.0f,new float[]{2.0f,2.0f},1.0f);
                Graphics2D gd = (Graphics2D)g;
                gd.setStroke(s);
                gd.setColor(Color.BLACK);
                Polygon p = new Polygon();
                p.addPoint(0,0);
                p.addPoint(getWidth()-1,0);
                p.addPoint(getWidth()-1,getHeight()-1);
                p.addPoint(0,getHeight()-1);
                gd.drawPolygon(p);
            }*/
            super.paintComponent(g);
        }
        public boolean contains(Point p){
            return this.getBounds().contains(p);
        }
        public void mousePressed(MouseEvent e) {
        	Point point = SwingUtilities.convertPoint(this,e.getPoint(),mainPane);
            dayManager.setSelect(point);
        }
        public void mouseReleased(MouseEvent e) {
        }
        public void mouseClicked(MouseEvent e) {
        }
        public void mouseEntered(MouseEvent e) {
        }
        public void mouseExited(MouseEvent e) {
        }
        public int compare(DayLabel o1, DayLabel o2) {
            Calendar c1=Calendar.getInstance();
            c1.set(o1.year,o2.month,o1.day);
            Calendar c2=Calendar.getInstance();
            c2.set(o2.year,o2.month,o2.day);
            return c1.compareTo(c2);
        }
    }
    
    private class LabelManager{
        private List<DayLabel> list;
        public LabelManager(){
            list = new ArrayList<DayLabel>();
        }
        public List<DayLabel> getLabels(){
            return list;
        }
        public void addLabel(DayLabel my){
            list.add(my);
        }
        public void clear(){
            list.clear();
        }
        public void setSelect(Point p){
            for(DayLabel label:list){
            	if(label.contains(p)){
            		if(!label.isSelected()){
            			//该按钮是否不为当月的日期
            			if(label.isLight()){
            				select.set(label.getYear(), label.getMonth(), label.getDay());
            				labelChange = true;
            				monthComboBox.setSelectedItem((label.getMonth()+1)+"");
            				yearComboBox.setSelectedItem(label.getYear()+"");
            				mainPane.updateDate();
            				return;
            			}else{
            				label.setSelected(true);
            			}
            		}
            	}else{
            		if(label.isSelected()){
            			label.setSelected(false);
            		}
            	}
            }
        }
    }
    
    private class Item_Listener implements ItemListener{
		public void itemStateChanged(ItemEvent e) {
			PeriodComboBox bobox = (PeriodComboBox) e.getSource();
			if(e.getStateChange() == ItemEvent.SELECTED){
				//如果为日期面板触发
				if(labelChange){
					labelChange = false;
					System.out.println("reset labelChange is false");
				}else{
					System.out.println("changed:"+bobox.getSelectedItem());
					select.set(Integer.parseInt(yearComboBox.getSelectedItem().toString()), 
							Integer.parseInt(monthComboBox.getSelectedItem().toString())-1, select.get(Calendar.DAY_OF_MONTH));
					mainPane.updateDate();
				}
			}
		}
	}
    
}