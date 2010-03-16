--
-- Database: `vandy`
--

-- --------------------------------------------------------

--
-- Table structure for table `lots`
--

CREATE TABLE `lots` (
  `lot_id` varchar(128) NOT NULL default '',
  `name` varchar(128) default NULL,
  `img_timestamp` varchar(128) default NULL,
  `img` varchar(128) NOT NULL,
  `avail_spots` int(11) NOT NULL default '0',
  `layout` longtext,
  PRIMARY KEY  (`lot_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
