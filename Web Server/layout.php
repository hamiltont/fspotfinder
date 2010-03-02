<?php

// ============================================================================
//
//  File: layout.php
//  Parameters: lot
//  Purpose: Return the layout that the passed lot is following, using defined
// 			     XML standard
//
// ============================================================================

require_once("db_connection.php");
$conn = connect_to_db();

$lot = isset($_GET['lot'])? mysql_real_escape_string(strip_tags($_GET['lot'])): false;

if(!$lot) die("");

$query = "SELECT layout FROM lots WHERE name = '$lot'";

$rs = mysql_query($query);

if(mysql_num_rows($rs) > 0){
  $row = mysql_fetch_assoc($rs);

  header("Content-type: text/xml");
  echo $row["layout"];
}

?>