<?php
// ============================================================================
//
//  File: latest.php
//  Parameters: lot, html=1
//  Purpose: Shows the most recent image. 
//            - If passed html=0, then returns the image data itself, rather 
//              than printing out an <img /> tag. 
//            - If passed lot id, then the most recent image whose title contains 
//              the lot id is selected and returned
//  ToDo: Setup the lot and pretty params
//
// ===========================================================================


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