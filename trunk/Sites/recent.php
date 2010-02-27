<?php
set_time_limit(0);

$images = scandir(dirname(realpath(__FILE__)), 1);
$count = isset($_GET['limit'])? $_GET['limit']:20;
if(!is_numeric($count) || $count > 50) $count = 20;

foreach($images as $im){
	if(($count--) > 0 && strpos($im,'jpg') != 0){
		echo '<img src="'.$im.'" title="'.$im.'"/><br /><br />';
	}
}

?>