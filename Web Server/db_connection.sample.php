<?PHP


function connect_to_db(){
  $db = "";
  $host = "";
  $user = "";
  $pass = "";

  $conn = mysql_connect($host,$user, $pass) or die("Could not connect: " . mysql_error());
  mysql_select_db($db) or die("Could not select the database");
  return $conn;
}

?>