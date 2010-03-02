<?php

// ============================================================================
//
//  File: images_to_process.php
//  Parameters: none
//  Purpose: Lists all uploaded images waiting to be processed
//
//           This feed is meant to be polled by the image processing system
// ===========================================================================

set_time_limit(0);

$images = scandir(dirname(realpath(__FILE__)), 1);
$base_url = 'http://' . $_SERVER["HTTP_HOST"] . dirname($_SERVER["PATH_INFO"]) . '/';

header("Content-type: text/xml");

echo "<images>\n";

foreach($images as $im){
	if(strpos($im,'jpg') != 0){
    $lot_id = 'towers'; //todo  parse this from the filename
    echo '<image';
		echo ' lot_id="' . $lot_id . '"';
    echo ' lot_layout="' . $base_url . 'layout.php?lot=' . $lot_id . '"';
    echo ' img_url="' . $base_url . $im . '"';
    echo ' />';
	}
}

echo '</images>';


?>