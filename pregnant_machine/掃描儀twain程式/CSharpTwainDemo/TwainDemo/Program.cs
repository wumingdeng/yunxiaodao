using System;
using System.Collections.Generic;
using System.Linq;
//using System.Threading.Tasks;
using System.Windows.Forms;
using System.Drawing.Imaging;
using System.IO;
using Twain;
namespace TwainDemo
{
    static class Program
    {
        public static string[] inputParams;
        /// <summary>
        /// 应用程序的主入口点。
        /// </summary>
        [STAThread]
        static void Main(string [] args)
        {
            if (args.Length < 1)
            {
                MessageBox.Show("params lack");
                return;
            }
            Program.inputParams = args;
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new FrmDemo());
        }
    }
}
