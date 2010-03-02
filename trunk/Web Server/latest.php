<?php
// ============================================================================
//
//  File: latest.php
//  Parameters: lot, html=1
//  Purpose: Shows the most recent image. 
//            - If passed html=1, print out an <img /> tag rather than
//              return the image data itself
//            - If passed lot id, then the most recent image whose title contains 
//              the lot id is selected and returned
//
// ===========================================================================


set_time_limit(0);

$lot = isset($_GET['lot'])? strip_tags($_GET['lot']): false;

$path = dirname(realpath(__FILE__));
$images = scandir($path,1);

foreach($images as $im){

  // Scan all the images files (most recent file) looking for
  // an image from a particular lot (if specified)
	if(strpos($im,'jpg') != 0 && (!$lot || stripos($im,$lot) !== false) ){

    //HTML Mode?
    if(isset($_GET['html']) && $_GET['html'] == 1){
      echo '<img src="' . $im . '" />';
    }
    else {
      header("Content-type: image/jpeg");
      $im_path = $path . '/' .$im;
      readfile($im_path);
    }

    exit(0);
  }
}
?>