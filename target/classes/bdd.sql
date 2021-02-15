/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

CREATE DATABASE IF NOT EXISTS `ebudget` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `ebudget`;

CREATE TABLE IF NOT EXISTS `account` (
  `NAME` varchar(100) NOT NULL,
  `accountType` varchar(255) DEFAULT NULL,
  `Description` varchar(100) NOT NULL,
  `finalAmount` double NOT NULL,
  `initialAmount` double NOT NULL,
  PRIMARY KEY (`NAME`),
  UNIQUE KEY `NAME` (`NAME`),
  UNIQUE KEY `NAME_2` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `budget` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `closed` bit(1) NOT NULL,
  `expectedExpense` double NOT NULL,
  `expectedFinalBalance` double NOT NULL,
  `expectedIncome` double NOT NULL,
  `expense` double NOT NULL,
  `finalBalance` double NOT NULL,
  `income` double NOT NULL,
  `annee` int(11) NOT NULL,
  `trimestre` int(11) NOT NULL,
  `mois` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID` (`ID`),
  UNIQUE KEY `ID_2` (`ID`),
  KEY `FK773F9B65754160A4` (`annee`,`trimestre`,`mois`),
  CONSTRAINT `FK773F9B65754160A4` FOREIGN KEY (`annee`, `trimestre`, `mois`) REFERENCES `periode` (`annee`, `mois`, `trimestre`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `category` (
  `NAME` varchar(100) NOT NULL,
  PRIMARY KEY (`NAME`),
  UNIQUE KEY `NAME` (`NAME`),
  UNIQUE KEY `NAME_2` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `distribution` (
  `availableAmount` double NOT NULL,
  `expectedExpense` double NOT NULL,
  `expense` double NOT NULL,
  `Category` varchar(100) NOT NULL,
  `budget` varchar(100) NOT NULL,
  PRIMARY KEY (`budget`,`Category`),
  KEY `FKAB93A2A429B2583B` (`Category`),
  KEY `FKAB93A2A4D48A4A82` (`budget`),
  CONSTRAINT `FKAB93A2A429B2583B` FOREIGN KEY (`Category`) REFERENCES `category` (`NAME`),
  CONSTRAINT `FKAB93A2A4D48A4A82` FOREIGN KEY (`budget`) REFERENCES `category` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `forecast` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `amount` double NOT NULL,
  `april` bit(1) NOT NULL,
  `august` bit(1) NOT NULL,
  `december` bit(1) NOT NULL,
  `Description` varchar(100) NOT NULL,
  `february` bit(1) NOT NULL,
  `january` bit(1) NOT NULL,
  `july` bit(1) NOT NULL,
  `june` bit(1) NOT NULL,
  `mandatory` bit(1) NOT NULL,
  `march` bit(1) NOT NULL,
  `may` bit(1) NOT NULL,
  `november` bit(1) NOT NULL,
  `october` bit(1) NOT NULL,
  `september` bit(1) NOT NULL,
  `variable` bit(1) NOT NULL,
  `Category` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID` (`ID`),
  UNIQUE KEY `ID_2` (`ID`),
  KEY `FK1FAC2E1B29B2583B` (`Category`),
  CONSTRAINT `FK1FAC2E1B29B2583B` FOREIGN KEY (`Category`) REFERENCES `category` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `periode` (
  `annee` int(11) NOT NULL,
  `mois` int(11) NOT NULL,
  `trimestre` int(11) NOT NULL,
  PRIMARY KEY (`annee`,`mois`,`trimestre`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `transaction` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `amount` double NOT NULL,
  `Date` varchar(255) NOT NULL,
  `Description` varchar(100) NOT NULL,
  `Payment` varchar(100) NOT NULL,
  `annee` int(11) NOT NULL,
  `trimestre` int(11) NOT NULL,
  `mois` int(11) NOT NULL,
  `Category` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID` (`ID`),
  UNIQUE KEY `ID_2` (`ID`),
  KEY `FKE30A7ABE419CDCEB` (`annee`,`trimestre`,`mois`),
  KEY `FKE30A7ABEBC113A3F` (`Category`),
  KEY `FKE30A7ABEA738F3A8` (`annee`,`trimestre`,`mois`),
  KEY `FKE30A7ABE754160A4` (`annee`,`trimestre`,`mois`),
  KEY `FKE30A7ABE29B2583B` (`Category`),
  CONSTRAINT `FKE30A7ABE29B2583B` FOREIGN KEY (`Category`) REFERENCES `category` (`NAME`),
  CONSTRAINT `FKE30A7ABE419CDCEB` FOREIGN KEY (`annee`, `trimestre`, `mois`) REFERENCES `periode` (`annee`, `mois`, `trimestre`),
  CONSTRAINT `FKE30A7ABE754160A4` FOREIGN KEY (`annee`, `trimestre`, `mois`) REFERENCES `periode` (`annee`, `mois`, `trimestre`),
  CONSTRAINT `FKE30A7ABEA738F3A8` FOREIGN KEY (`annee`, `trimestre`, `mois`) REFERENCES `periode` (`annee`, `mois`, `trimestre`),
  CONSTRAINT `FKE30A7ABEBC113A3F` FOREIGN KEY (`Category`) REFERENCES `category` (`NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=1689 DEFAULT CHARSET=latin1;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
