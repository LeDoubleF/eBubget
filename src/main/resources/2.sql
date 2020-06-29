/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

DELETE FROM `category`;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` (`NAME`) VALUES
	('agios'),
	('alimentation'),
	('assurance enfant'),
	('assurance habitation'),
	('autre revenu'),
	('cadeaux'),
	('caf'),
	('coiffure'),
	('cpam'),
	('crédit'),
	('divers'),
	('edf'),
	('électroménager'),
	('emprunt'),
	('épargne'),
	('formule compte'),
	('frais bancaire'),
	('frais de garde'),
	('hygiène'),
	('impôt sur revenu'),
	('indemnité'),
	('jeux'),
	('laboratoire'),
	('loisirs'),
	('lokesh'),
	('loyer'),
	('médecin'),
	('mobile'),
	('mutuelle'),
	('navigo'),
	('net'),
	('pharmacie'),
	('psy'),
	('reliquat'),
	('réparation moto'),
	('salaire fanny'),
	('salaire oumar'),
	('taxe habitation'),
	('taxi'),
	('ticket'),
	('vacances'),
	('vêtements');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
