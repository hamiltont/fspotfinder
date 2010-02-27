<?php
set_time_limit(0);

header("Content-type: image/jpeg");
									
$path = dirname(realpath(__FILE__));
$images = scandir($path,1);
$count = 20;
foreach($images as $im){
	if(($count--) > 0 && strpos($im,'jpg') != 0){
    $im_path = $path . '/' .$im;
    readfile($im_path);
    exit(0);
	}
}

?>