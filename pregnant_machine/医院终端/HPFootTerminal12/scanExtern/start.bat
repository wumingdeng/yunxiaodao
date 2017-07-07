cd %1
::start TwainScanner.exe left
::TwainScanner.exe right

convert .\Image\Image_Right.jpg -rotate -180 .\Image\Image_Right_r.jpg
convert -size 2000x1370 -strip xc:none .\Image\Image_Left.jpg -geometry +0+100 -composite .\Image\Image_Right_r.jpg -geometry +1150+100 -composite .\Image\fin.jpg
::convert -size 2000x1170 -strip xc:none .\Image\Image_Left.jpg -geometry +0+0 -composite .\Image\Image_Right_r.jpg -geometry +1150+0 -composite .\Image\fin.jpg
::convert -size 2000x1170 -strip xc:none .\Image\Image_Left.jpg -geometry +0+0 -composite .\Image\Image_Right.jpg -geometry +1150+0 -composite .\Image\fin.jpg