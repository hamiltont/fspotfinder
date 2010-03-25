<?php


// ============================================================================
//
//  File: lots.php
//  Parameters: mobile=false
//  Purpose: List all available lots, and describe the state of each lot. 
// 			     These names are the lot ids that are passed into latest.php.
//
//			 
//           Returns XML, with a Lot Name, Lot ID,
//           Available Spaces, Time each available space was calculated, 
//      	 the URI to the latest photo, the timestamp on the photo, and
//           any other params
//           If mobile is set in the GET params, then only returns minimal info
//
// ===========================================================================

require_once("db_connection.php");
$conn = connect_to_db();
$img_url = 'http://' . $_SERVER["HTTP_HOST"] . dirname($_SERVER["PATH_INFO"]) . '/processed/';

$is_mobile = isset($_GET['mobile']) ? true: false;

header("Content-type: text/xml");

$query = "SELECT * FROM lots";

echo "<lots>\n";
$rs = mysql_query($query);

while($row = mysql_fetch_assoc($rs)){
  echo '<lot';
  echo ' id="' . $row['lot_id'] . '"';
  echo ' spots="' . $row['avail_spots'] . '"';

  if ($is_mobile == false)
  {  
    echo ' name="' . $row['name'] . '"';
    echo ' img_timestamp="' . $row['img_timestamp']  . '"';
    echo ' img_url="' . $img_url . $row['img'] . '"';
  }
  
  echo " />\n";
}

echo '</lots>';


?>