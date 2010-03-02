<?php


// ============================================================================
//
//  File: lots.php
//  Parameters: none
//  Purpose: List all available lots, and describe the state of each lot. 
// 			     These names are the lot ids that are passed into latest.php.
//
//			     Returns XML, with a Lot Name, Lot ID,
//           Available Spaces, Time each available space was calculated, 
//  		     the URI to the latest photo, the timestamp on the photo, and
//   		     any other params
//
// ===========================================================================

require_once("db_connection.php");
$conn = connect_to_db();
$img_url = 'http://' . $_SERVER["HTTP_HOST"] . '/processed/';

header("Content-type: text/xml");

$query = "SELECT * FROM lots";

echo "<lots>\n";
$rs = mysql_query($query);

while($row = mysql_fetch_assoc($rs)){
  echo '<lot';
  echo ' name="' . $row['name'] . '"';
  echo ' id="' . $row['lot_id'] . '"';
  echo ' img_timestamp="' . $row['img_timestamp']  . '"';
  echo ' img_url="' . $img_url . $row['img'] . '"';
  // ....
  
  echo " />\n";
}

echo '</lots>';


?>