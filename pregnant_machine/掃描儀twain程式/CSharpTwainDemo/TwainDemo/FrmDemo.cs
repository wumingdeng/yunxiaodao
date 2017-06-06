using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Drawing.Imaging;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.IO;
using Twain;

namespace TwainDemo
{
    
    public partial class FrmDemo : Form
    {
        public FrmDemo()
        {
            InitializeComponent();
            this.WindowState = FormWindowState.Minimized;
            this.ShowInTaskbar = false;
            SetVisibleCore(false);
        }        
        private int mImageIndex = 1;
        private string mRunPath = "";
        private string mImagePath = "";
        private Twain32 mTwain = new Twain32();

        private void twEndXfer(object sender, Twain32.EndXferEventArgs e)
        {
            string FileNm = mImagePath + "Image_" + Program.inputParams[0] + ".jpg";
            //MessageBox.Show(FileNm);
            e.Image.Save(FileNm, ImageFormat.Jpeg);
            mImageIndex++;
        }
        private void FrmDemo_Load(object sender, EventArgs e)
        {
            mRunPath = System.IO.Directory.GetCurrentDirectory() + "\\";
            mImagePath = mRunPath + "Image\\";
            if (Directory.Exists(mImagePath) == false)
            {
                Directory.CreateDirectory(mImagePath);
            }
            mTwain.Language = TwLanguage.CHINESE_SINGAPORE;
            mTwain.IsTwain2Enable = false;
            mTwain.OpenDSM();
            List<string> srclst = new List<string>();
            for (int i = 0; i < mTwain.SourcesCount; i++)
            {
                srclst.Add(mTwain.GetSourceProductName(i));
            }
            combo_Dev.DataSource = srclst;
            mTwain.EndXfer += twEndXfer;

            this.btnScan_Click(null, null);
        }

        private void btnScan_Click(object sender, EventArgs e)
        {
            float val = 100;// Convert.ToSingle(combo_DPI.SelectedText);
            mTwain.Capabilities.XResolution.Set(val);
            mTwain.Capabilities.YResolution.Set(val);
            mTwain.Capabilities.PixelType.Set(TwPixelType.RGB);
            //if (rb_RGB.Checked)
            //{
            //    mTwain.Capabilities.PixelType.Set(TwPixelType.RGB);
            //}
            //if (rb_BW.Checked)
            //{
            //    mTwain.Capabilities.PixelType.Set(TwPixelType.BW);
            //}
            //if (rb_Gray.Checked)
            //{
            //    mTwain.Capabilities.PixelType.Set(TwPixelType.Gray);
            //}
            mTwain.ShowUI = false;
            mTwain.DisableAfterAcquire = true;
            mTwain.Acquire();
            mTwain.AcquireError += MTwain_AcquireError;
            mTwain.AcquireCompleted += MTwain_AcquireCompleted;
        }

        private void MTwain_AcquireCompleted(object sender, EventArgs e)
        {
            var t = new System.Timers.Timer(3700);
            t.Elapsed += new System.Timers.ElapsedEventHandler(theout);//到达时间的时候执行事件；
            t.AutoReset = false;//设置是执行一次（false）还是一直执行(true)
            t.Start();
        }

        private void theout(object sender, EventArgs e)
        {
            System.Environment.Exit(0);
        }

        private void MTwain_AcquireError(object sender, Twain32.AcquireErrorEventArgs e)
        {
            mTwain.Dispose();
        }

        private void button1_Click(object sender, EventArgs e)
        {
        
        }

        private void combo_Dev_SelectedIndexChanged(object sender, EventArgs e)
        {
            try
            {
                mTwain.SourceIndex = combo_Dev.SelectedIndex;
                mTwain.OpenDataSource();
                var _resolutions = mTwain.Capabilities.XResolution.Get();
                List<string> dpilst = new List<string>();
                for (var i = 0; i < _resolutions.Count; i++)
                {
                    dpilst.Add(_resolutions[i].ToString());
                }
                combo_DPI.DataSource = dpilst;
            }
            catch
            {
            }
        }
    }
}

