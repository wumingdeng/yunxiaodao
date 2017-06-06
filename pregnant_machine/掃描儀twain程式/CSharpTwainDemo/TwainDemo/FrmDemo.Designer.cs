namespace TwainDemo
{
    partial class FrmDemo
    {
        /// <summary>
        /// 必需的设计器变量。
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// 清理所有正在使用的资源。
        /// </summary>
        /// <param name="disposing">如果应释放托管资源，为 true；否则为 false。</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows 窗体设计器生成的代码

        /// <summary>
        /// 设计器支持所需的方法 - 不要
        /// 使用代码编辑器修改此方法的内容。
        /// </summary>
        private void InitializeComponent()
        {
            this.combo_Dev = new System.Windows.Forms.ComboBox();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.groupBox2 = new System.Windows.Forms.GroupBox();
            this.rb_RGB = new System.Windows.Forms.RadioButton();
            this.rb_BW = new System.Windows.Forms.RadioButton();
            this.groupBox3 = new System.Windows.Forms.GroupBox();
            this.combo_DPI = new System.Windows.Forms.ComboBox();
            this.btnScan = new System.Windows.Forms.Button();
            this.chk_ShowUI = new System.Windows.Forms.CheckBox();
            this.rb_Gray = new System.Windows.Forms.RadioButton();
            this.groupBox1.SuspendLayout();
            this.groupBox2.SuspendLayout();
            this.groupBox3.SuspendLayout();
            this.SuspendLayout();
            // 
            // combo_Dev
            // 
            this.combo_Dev.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.combo_Dev.FormattingEnabled = true;
            this.combo_Dev.Location = new System.Drawing.Point(19, 27);
            this.combo_Dev.Name = "combo_Dev";
            this.combo_Dev.Size = new System.Drawing.Size(631, 20);
            this.combo_Dev.TabIndex = 0;
            this.combo_Dev.SelectedIndexChanged += new System.EventHandler(this.combo_Dev_SelectedIndexChanged);
            // 
            // groupBox1
            // 
            this.groupBox1.Controls.Add(this.combo_Dev);
            this.groupBox1.Location = new System.Drawing.Point(12, 12);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(666, 70);
            this.groupBox1.TabIndex = 1;
            this.groupBox1.TabStop = false;
            this.groupBox1.Text = "扫描源";
            // 
            // groupBox2
            // 
            this.groupBox2.Controls.Add(this.rb_Gray);
            this.groupBox2.Controls.Add(this.rb_RGB);
            this.groupBox2.Controls.Add(this.rb_BW);
            this.groupBox2.Location = new System.Drawing.Point(12, 88);
            this.groupBox2.Name = "groupBox2";
            this.groupBox2.Size = new System.Drawing.Size(179, 60);
            this.groupBox2.TabIndex = 2;
            this.groupBox2.TabStop = false;
            this.groupBox2.Text = "图像类型";
            // 
            // rb_RGB
            // 
            this.rb_RGB.AutoSize = true;
            this.rb_RGB.Location = new System.Drawing.Point(120, 26);
            this.rb_RGB.Name = "rb_RGB";
            this.rb_RGB.Size = new System.Drawing.Size(47, 16);
            this.rb_RGB.TabIndex = 1;
            this.rb_RGB.Text = "彩色";
            this.rb_RGB.UseVisualStyleBackColor = true;
            // 
            // rb_BW
            // 
            this.rb_BW.AutoSize = true;
            this.rb_BW.Checked = true;
            this.rb_BW.Location = new System.Drawing.Point(12, 26);
            this.rb_BW.Name = "rb_BW";
            this.rb_BW.Size = new System.Drawing.Size(47, 16);
            this.rb_BW.TabIndex = 0;
            this.rb_BW.TabStop = true;
            this.rb_BW.Text = "黑白";
            this.rb_BW.UseVisualStyleBackColor = true;
            // 
            // groupBox3
            // 
            this.groupBox3.Controls.Add(this.combo_DPI);
            this.groupBox3.Location = new System.Drawing.Point(197, 88);
            this.groupBox3.Name = "groupBox3";
            this.groupBox3.Size = new System.Drawing.Size(179, 60);
            this.groupBox3.TabIndex = 3;
            this.groupBox3.TabStop = false;
            this.groupBox3.Text = "扫描质量";
            // 
            // combo_DPI
            // 
            this.combo_DPI.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.combo_DPI.FormattingEnabled = true;
            this.combo_DPI.Items.AddRange(new object[] {
            "300DPI",
            "200DPI",
            "100DPI"});
            this.combo_DPI.Location = new System.Drawing.Point(13, 26);
            this.combo_DPI.Name = "combo_DPI";
            this.combo_DPI.Size = new System.Drawing.Size(152, 20);
            this.combo_DPI.TabIndex = 0;
            // 
            // btnScan
            // 
            this.btnScan.Location = new System.Drawing.Point(587, 114);
            this.btnScan.Name = "btnScan";
            this.btnScan.Size = new System.Drawing.Size(75, 23);
            this.btnScan.TabIndex = 4;
            this.btnScan.Text = "扫描";
            this.btnScan.UseVisualStyleBackColor = true;
            this.btnScan.Click += new System.EventHandler(this.btnScan_Click);
            // 
            // chk_ShowUI
            // 
            this.chk_ShowUI.AutoSize = true;
            this.chk_ShowUI.Location = new System.Drawing.Point(400, 114);
            this.chk_ShowUI.Name = "chk_ShowUI";
            this.chk_ShowUI.Size = new System.Drawing.Size(96, 16);
            this.chk_ShowUI.TabIndex = 5;
            this.chk_ShowUI.Text = "显示扫描界面";
            this.chk_ShowUI.UseVisualStyleBackColor = true;
            // 
            // rb_Gray
            // 
            this.rb_Gray.AutoSize = true;
            this.rb_Gray.Location = new System.Drawing.Point(66, 26);
            this.rb_Gray.Name = "rb_Gray";
            this.rb_Gray.Size = new System.Drawing.Size(47, 16);
            this.rb_Gray.TabIndex = 2;
            this.rb_Gray.Text = "灰度";
            this.rb_Gray.UseVisualStyleBackColor = true;
            // 
            // FrmDemo
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(690, 180);
            this.Controls.Add(this.chk_ShowUI);
            this.Controls.Add(this.btnScan);
            this.Controls.Add(this.groupBox3);
            this.Controls.Add(this.groupBox2);
            this.Controls.Add(this.groupBox1);
            this.MaximizeBox = false;
            this.Name = "FrmDemo";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "TWAINDemo";
            this.Load += new System.EventHandler(this.FrmDemo_Load);
            this.groupBox1.ResumeLayout(false);
            this.groupBox2.ResumeLayout(false);
            this.groupBox2.PerformLayout();
            this.groupBox3.ResumeLayout(false);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.ComboBox combo_Dev;
        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.GroupBox groupBox2;
        private System.Windows.Forms.RadioButton rb_RGB;
        private System.Windows.Forms.RadioButton rb_BW;
        private System.Windows.Forms.GroupBox groupBox3;
        private System.Windows.Forms.ComboBox combo_DPI;
        private System.Windows.Forms.Button btnScan;
        private System.Windows.Forms.CheckBox chk_ShowUI;
        private System.Windows.Forms.RadioButton rb_Gray;
    }
}

