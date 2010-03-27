<?php
// ============================================================================
//
//  File: upload_results.php
//  Parameters: lot_id, img_url, avail_spots
//  Purpose: Upload the results (available spot data) from image processor
//
// ===========================================================================

require_once("db_connection.php");
$conn = connect_to_db();

$lot_id = isset($_REQUEST['lot_id'])? mysql_real_escape_string(strip_tags($_REQUEST['lot_id'])): die('fail');
$img_file = isset($_REQUEST['img_url'])? basename(strip_tags($_REQUEST['img_url'])): die('fail');
$avail_spots = is_numeric($_REQUEST['avail_spots'])? $_REQUEST['avail_spots']: die('fail');


// Move the file to the processed folder
if(!file_exists($img_file)) die('error: no such image is waiting to be processed');
rename($img_file, 'processed/'.$img_file);


$query = "UPDATE lots SET img='{$img_file}', avail_spots='{$avail_spots}', img_timestamp='".time()."' WHERE lot_id='$lot_id'";

$rs = mysql_query($query);

if(mysql_affected_rows() > 0){
  echo 'success';
}


?>