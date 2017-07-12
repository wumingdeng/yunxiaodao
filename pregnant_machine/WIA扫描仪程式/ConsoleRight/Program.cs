using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
//using System.Threading.Tasks;
using WIA;
using System.IO;
using System.Runtime.InteropServices;
namespace ConsoleApp1
{
    class Program
    {
        /// <summary>
        /// Adjusts the settings of the scanner with the providen parameters.
        /// </summary>
        /// <param name="scannnerItem">Scanner Item</param>
        /// <param name="scanResolutionDPI">Provide the DPI resolution that should be used e.g 150</param>
        /// <param name="scanStartLeftPixel"></param>
        /// <param name="scanStartTopPixel"></param>
        /// <param name="scanWidthPixels"></param>
        /// <param name="scanHeightPixels"></param>
        /// <param name="brightnessPercents"></param>
        /// <param name="contrastPercents">Modify the contrast percent</param>
        /// <param name="colorMode">Set the color mode</param>
        private static void AdjustScannerSettings(IItem scannnerItem, int scanResolutionDPI, int scanStartLeftPixel, int scanStartTopPixel, int scanWidthPixels, int scanHeightPixels, int brightnessPercents, int contrastPercents, int colorMode)
        {
            const string WIA_SCAN_COLOR_MODE = "6146";
            const string WIA_HORIZONTAL_SCAN_RESOLUTION_DPI = "6147";
            const string WIA_VERTICAL_SCAN_RESOLUTION_DPI = "6148";
            const string WIA_HORIZONTAL_SCAN_START_PIXEL = "6149";
            const string WIA_VERTICAL_SCAN_START_PIXEL = "6150";
            const string WIA_HORIZONTAL_SCAN_SIZE_PIXELS = "6151";
            const string WIA_VERTICAL_SCAN_SIZE_PIXELS = "6152";
            const string WIA_SCAN_BRIGHTNESS_PERCENTS = "6154";
            const string WIA_SCAN_CONTRAST_PERCENTS = "6155";
            SetWIAProperty(scannnerItem.Properties, WIA_HORIZONTAL_SCAN_RESOLUTION_DPI, scanResolutionDPI);
            SetWIAProperty(scannnerItem.Properties, WIA_VERTICAL_SCAN_RESOLUTION_DPI, scanResolutionDPI);
            SetWIAProperty(scannnerItem.Properties, WIA_HORIZONTAL_SCAN_START_PIXEL, scanStartLeftPixel);
            SetWIAProperty(scannnerItem.Properties, WIA_VERTICAL_SCAN_START_PIXEL, scanStartTopPixel);
            SetWIAProperty(scannnerItem.Properties, WIA_HORIZONTAL_SCAN_SIZE_PIXELS, scanWidthPixels);
            SetWIAProperty(scannnerItem.Properties, WIA_VERTICAL_SCAN_SIZE_PIXELS, scanHeightPixels);
            SetWIAProperty(scannnerItem.Properties, WIA_SCAN_BRIGHTNESS_PERCENTS, brightnessPercents);
            SetWIAProperty(scannnerItem.Properties, WIA_SCAN_CONTRAST_PERCENTS, contrastPercents);
            SetWIAProperty(scannnerItem.Properties, WIA_SCAN_COLOR_MODE, colorMode);
        }

        /// <summary>
        /// Modify a WIA property
        /// </summary>
        /// <param name="properties"></param>
        /// <param name="propName"></param>
        /// <param name="propValue"></param>
        private static void SetWIAProperty(IProperties properties, object propName, object propValue)
        {
            Property prop = properties.get_Item(ref propName);
            prop.set_Value(ref propValue);
        }

        /// <summary>  
        /// 获取时间戳  
        /// </summary>  
        /// <returns></returns>  
        public static string GetTimeStamp()
        {
            TimeSpan ts = DateTime.UtcNow - new DateTime(1970, 1, 1, 0, 0, 0, 0);
            return Convert.ToInt64(ts.TotalSeconds).ToString();
        }

        public static void Scan2(DeviceInfo dei,string saveName)
        {
            try
            {
                var firstScannerAvailable = dei;// Program.infos[i];//deviceManager.DeviceInfos[i];
                var device = firstScannerAvailable.Connect();
                // Select the scanner
                var scannerItem = device.Items[1];
                int resolution = 100;
                int width_pixel = 850;
                int height_pixel = 1169;
                int color_mode = 1;
                AdjustScannerSettings(scannerItem, resolution, 0, 0, width_pixel, height_pixel, 0, 0, color_mode);
                // Retrieve a image in JPEG format and store it into a variable
                var imageFile = (ImageFile)scannerItem.Transfer(FormatID.wiaFormatJPEG);

                //save the image in some path with filename
                string str = String.Format("./Image/Image_{0}.jpg", saveName);
                //var path = @"d:/1.jpg";

                if (File.Exists(str))
                {
                    File.Delete(str);
                }

                //save image !
                imageFile.SaveFile(str);
            }
            catch (COMException e)
            {
                // Convert the error code to UINT
                uint errorCode = (uint)e.ErrorCode;

                // See the error codes
                if (errorCode == 0x80210006)
                {
                    Console.WriteLine("The scanner is busy or isn't ready");
                }
                else if (errorCode == 0x80210064)
                {
                    Console.WriteLine("The scanning process has been cancelled.");
                }
                else if (errorCode == 0x8021000C)
                {
                    Console.WriteLine("There is an incorrect setting on the WIA device.");
                }
                else if (errorCode == 0x80210005)
                {
                    Console.WriteLine("The device is offline. Make sure the device is powered on and connected to the PC.");
                }
                else if (errorCode == 0x80210001)
                {
                    Console.WriteLine("An unknown error has occurred with the WIA device.");
                }
            }
        }

        [DllImport("kernel32.dll")]
        static extern IntPtr GetConsoleWindow();

        [DllImport("user32.dll")]
        static extern bool ShowWindow(IntPtr hWnd, int nCmdShow);

        const int SW_HIDE = 0;
        const int SW_SHOW = 5;

        static void Main(string[] args)
        {
            if (args.Length < 2)
            {
                Console.WriteLine("param wrong");
                return;
            }
            var handle = GetConsoleWindow();
            // Hide
            ShowWindow(handle, SW_HIDE);
            var deviceManager = new DeviceManager();
            //for (int i = 1; i <= deviceManager.DeviceInfos.Count; i++) {
                //Console.WriteLine(deviceManager.DeviceInfos[i].Properties["Name"].get_Value());
            //}
                
                for (int i = 1; i <= deviceManager.DeviceInfos.Count; i++)
                {
                    if (deviceManager.DeviceInfos[i].Type != WiaDeviceType.ScannerDeviceType)
                    {
                        continue;
                    }
                    String devicename = deviceManager.DeviceInfos[i].Properties["Name"].get_Value() as string;
                    if (!devicename.Equals(args[0]))
                    {
                        continue;
                    }
                    //Console.WriteLine(deviceManager.DeviceInfos[i].Properties["Name"].get_Value());
                    //Console.WriteLine(deviceManager.DeviceInfos[i].Properties["Unique Device ID"].get_Value());
                    Scan2(deviceManager.DeviceInfos[i], args[1]);
                    //break;
                }
                //foreach (string c in args)
                //{
                //    Console.WriteLine(c);
                //}
                //Console.ReadKey();
        }
    }
}
