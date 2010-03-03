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


// Adam: do we really need this.  lots.php has all of this info?
// Hamy: I think it's debatable. I see this as a tiny, very low computational
//       way to access the number of spots in a single lot. This would be polled
//       by the phone when it was sitting on a single lot's details page. If
//       we include this with lots.php, I would not want to send along all the 
//       info that lots.php has (such as lot pretty name, lot id, etc), only a 
//       subset of that info. Therefore, I added this file, as I favor more files
//       and fewer URL parameters

?>