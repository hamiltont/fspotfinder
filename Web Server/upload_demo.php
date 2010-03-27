<?php
// ============================================================================
//
//  File: upload_demo.php
//  Parameters: interval  [optional]   (secs between image refreshes)
//  Purpose: Mimics the upload behavior of the image processor to demo
//           the rest of the system
//
// ===========================================================================

set_time_limit(0);


$interval = 5; //seconds
if(isset($_REQUEST['interval']) && $_REQUEST['interval'] > 3) $interval = $_REQUEST['interval'];
echo 'Uploading image results every ' . $interval . ' seconds <br />';

$base_url = 'http://' . $_SERVER["HTTP_HOST"] . dirname($_SERVER["PATH_INFO"]) . '/';

// Grab the images_to_be_processed feed
$process_xml = file_get_contents($base_url . 'images_to_process.php');

// parse the XML
preg_match_all('/<image ([^<>]+)>/i', $process_xml, $result, PREG_PATTERN_ORDER);
$images = $result[1];

// upload results for each image in the queue
foreach($images as $image){
  $chunks = explode(' ', $image);
  $keys = array();
  
  foreach($chunks as $chunk){
    $attr = explode('=', $chunk);
    if(count($attr) == 2)
      $keys[$attr[0]] = trim($attr[1],'"');
  }
  
  if(!isset($keys['lot_id'])) continue;
  if(!isset($keys['img_url'])) continue;
  
  // use a random number for available spots
  $avail = rand(0,20);
  
  //Create url for processing
  $upload_url = $base_url . 'upload_results.php?lot_id='.$keys['lot_id'].'&img_url='.$keys['img_url'].'&avail_spots='. $avail;
  
  file_get_contents($upload_url);
  
  echo "*Updated lot_id = {$keys['lot_id']} with {$avail} spots<br />";

  sleep($interval);
}

?>