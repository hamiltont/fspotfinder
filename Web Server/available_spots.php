<?php
// ============================================================================
//
//  File: available_spots.php
//  Parameters: lot
//  Purpose: Return the number of spaces in a single lot, as well as just enough
//     	     info for the phone to know if it needs to request a new picture from
//	     		 latest.php. We want this to be small, so an optimal response would
// 			     be something like "4,1267314230" aka "spots,timestamp_of_some_sort"
//
//			Use the lot ID string as the parameter value.
//  ToDo: Create me (also, perhaps rename the file to something that matches it's
// 		  responsibilities more
//
// ===========================================================================

require_once("db_connection.php");
$conn = connect_to_db();

$lot = isset($_GET['lot'])? mysql_real_escape_string(strip_tags($_GET['lot'])): false;

if(!$lot) die("");

$query = "SELECT img_timestamp, avail_spots FROM lots WHERE lot_id = '$lot' LIMIT 1";

$rs = mysql_query($query);

if($row = mysql_fetch_assoc($rs)){
  echo $row['img_timestamp'] . ',' . $row['avail_spots'];
}


?>