-- phpMyAdmin SQL Dump
-- version 4.7.9
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le :  mar. 30 avr. 2019 à 07:32
-- Version du serveur :  5.7.25-log
-- Version de PHP :  5.6.35

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `xml`
--

-- --------------------------------------------------------

--
-- Structure de la table `arbitre`
--

DROP TABLE IF EXISTS `arbitre`;
CREATE TABLE IF NOT EXISTS `arbitre` (
  `numArbitre` varchar(11) NOT NULL,
  `nom_arbitre` varchar(50) DEFAULT NULL,
  `nationalite` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`numArbitre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `arbitre`
--

INSERT INTO `arbitre` (`numArbitre`, `nom_arbitre`, `nationalite`) VALUES
('1', 'Wayne Barnes', 'Angleterre'),
('10', 'Tony Spreadbury', 'Angleterre'),
('11', 'Steve Walsh', 'Nouvelle-Zelande'),
('12', 'Chris White', 'Angleterre'),
('2', 'Stuart Dickinson', 'Australie'),
('3', 'Paul Honiss', 'Nouvelle-Zelande'),
('4', 'Marius Jonker', 'Republique Sud-Africaine'),
('5', 'Joel Jutge', 'France'),
('6', 'Jonathan Kaplan', 'Republique Sud-Africaine'),
('7', 'Alan Lewis', 'Irelande'),
('8', 'Nigel Owens', 'Pays de Galles'),
('9', 'Alain Rolland', 'Irelande');

-- --------------------------------------------------------

--
-- Structure de la table `arbitrer`
--

DROP TABLE IF EXISTS `arbitrer`;
CREATE TABLE IF NOT EXISTS `arbitrer` (
  `numMatch` varchar(11) NOT NULL,
  `numArbitre` varchar(11) NOT NULL,
  PRIMARY KEY (`numMatch`,`numArbitre`),
  KEY `numArbitre` (`numArbitre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `arbitrer`
--

INSERT INTO `arbitrer` (`numMatch`, `numArbitre`) VALUES
('30', '1'),
('32', '1'),
('4', '1'),
('9', '1'),
('1', '10'),
('11', '10'),
('21', '10'),
('10', '11'),
('20', '11'),
('8', '11'),
('12', '12'),
('6', '12'),
('27', '2'),
('7', '2'),
('16', '3'),
('26', '3'),
('14', '4'),
('15', '4'),
('19', '4'),
('2', '5'),
('22', '5'),
('28', '5'),
('24', '6'),
('25', '6'),
('29', '6'),
('17', '7'),
('31', '7'),
('13', '8'),
('23', '8'),
('3', '8'),
('18', '9'),
('5', '9');

-- --------------------------------------------------------

--
-- Structure de la table `equipe`
--

DROP TABLE IF EXISTS `equipe`;
CREATE TABLE IF NOT EXISTS `equipe` (
  `codeEquipe` varchar(4) NOT NULL,
  `pays` varchar(50) DEFAULT NULL,
  `couleur` varchar(50) DEFAULT NULL,
  `entraineur` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`codeEquipe`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `equipe`
--

INSERT INTO `equipe` (`codeEquipe`, `pays`, `couleur`, `entraineur`) VALUES
('ARG', 'Argentine', 'Azur-Blanc', 'Marcelo Loffreda'),
('AUS', 'Australie', 'Jaune-Vert', 'John Connolly'),
('CAN', 'Canada', 'Rouge-Blanc', 'Ric Suggitt'),
('ENG', 'Angleterre', 'Blanc-Rouge', 'Brian Ashton'),
('FJI', 'Fidji', 'Blanc', 'Ilie Tabua'),
('FRA', 'France', 'Bleu', 'Bernard Laporte'),
('GEO', 'Georgie', 'Rouge-Blanc', 'Malkhaz Cheishvili'),
('IRL', 'Irlande', 'Vert', 'Eddie O\'Sullivan'),
('ITA', 'Italie', 'Azur', 'Pierre Berbizier'),
('JPN', 'Japon', 'Rouge-Blanc', 'John Kirwan'),
('NAM', 'Namibie', 'Bleu-Blanc', 'Hakkies Husselman'),
('NZL', 'Nouvelle-Zelande', 'Noir', 'Graham Henry'),
('POR', 'Portugal', 'Bordeaux-Vert', 'Tomaz Morais'),
('ROM', 'Roumanie', 'Jaune', 'Daniel Santamans'),
('RSA', 'Republique Sud-Africaine', 'Vert', 'Jake White'),
('SAM', 'Samoa', 'Bleu', 'Michael Jones'),
('SCO', 'Ecosse', 'Bleu-Blanc', 'Frank Hadden'),
('TGA', 'Tonga', 'Rouge-Blanc', 'Quddus Fielea'),
('USA', 'Etats-Unis', 'Rouge-Bleu', 'Peter Thorburn'),
('WAL', 'Pays de Galles', 'Rouge', 'Gareth Jenkins');

-- --------------------------------------------------------

--
-- Structure de la table `jouer`
--

DROP TABLE IF EXISTS `jouer`;
CREATE TABLE IF NOT EXISTS `jouer` (
  `numMatch` varchar(11) NOT NULL,
  `numJoueur` varchar(11) NOT NULL,
  `titulaire` varchar(2) DEFAULT NULL,
  `tpsJeu` varchar(11) DEFAULT NULL,
  `nbPoints` varchar(11) DEFAULT NULL,
  `nbEssais` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`numMatch`,`numJoueur`),
  KEY `numJoueur` (`numJoueur`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `jouer`
--

INSERT INTO `jouer` (`numMatch`, `numJoueur`, `titulaire`, `tpsJeu`, `nbPoints`, `nbEssais`) VALUES
('1', '31', 'X', '80', '0', '0'),
('1', '32', 'X', '60', '0', '0'),
('1', '33', 'X', '80', '0', '0'),
('1', '34', ' ', '20', '0', '0'),
('1', '35', 'X', '80', '0', '0'),
('1', '36', 'X', '80', '0', '0'),
('1', '38', ' ', '20', '0', '0'),
('1', '39', ' ', '7', '0', '0'),
('1', '40', ' ', '18', '0', '0'),
('1', '41', 'X', '80', '0', '0'),
('1', '42', 'X', '80', '0', '0'),
('1', '46', ' ', '20', '0', '0'),
('1', '51', 'X', '80', '0', '0'),
('1', '52', 'X', '80', '0', '0'),
('1', '54', 'X', '80', '0', '0'),
('1', '55', 'X', '80', '0', '0'),
('1', '56', 'X', '60', '0', '0'),
('1', '58', 'X', '73', '0', '0'),
('1', '59', 'X', '60', '0', '0'),
('1', '60', 'X', '62', '12', '0'),
('12', '1', ' ', '19', '0', '0'),
('12', '13', 'X', '7', '0', '0'),
('12', '15', ' ', '73', '5', '1'),
('12', '16', 'X', '80', '0', '0'),
('12', '17', 'X', '57', '5', '1'),
('12', '18', 'X', '61', '0', '0'),
('12', '19', ' ', '23', '0', '0'),
('12', '2', ' ', '16', '0', '0'),
('12', '20', 'X', '80', '5', '1'),
('12', '21', 'X', '80', '33', '1'),
('12', '22', 'X', '80', '10', '2'),
('12', '23', 'X', '80', '10', '2'),
('12', '24', ' ', '26', '5', '1'),
('12', '25', 'X', '80', '0', '0'),
('12', '28', 'X', '80', '5', '1'),
('12', '29', 'X', '80', '10', '2'),
('12', '3', ' ', '29', '5', '1'),
('12', '30', 'X', '54', '5', '1'),
('12', '4', 'X', '51', '0', '0'),
('12', '5', 'X', '54', '5', '1'),
('12', '6', 'X', '64', '5', '1'),
('12', '8', ' ', '26', '0', '0'),
('15', '1', 'X', '80', '0', '0'),
('15', '10', 'X', '80', '15', '1'),
('15', '11', 'X', '80', '0', '0'),
('15', '12', 'X', '80', '0', '0'),
('15', '14', 'X', '80', '10', '2'),
('15', '15', 'X', '21', '0', '0'),
('15', '16', ' ', '15', '0', '0'),
('15', '17', ' ', '21', '0', '0'),
('15', '19', 'X', '59', '0', '0'),
('15', '20', 'X', '80', '0', '0'),
('15', '21', ' ', '59', '0', '0'),
('15', '23', 'X', '65', '0', '0'),
('15', '25', ' ', '19', '0', '0'),
('15', '27', 'X', '80', '0', '0'),
('15', '28', ' ', '15', '0', '0'),
('15', '3', 'X', '65', '0', '0'),
('15', '30', ' ', '21', '0', '0'),
('15', '4', ' ', '14', '0', '0'),
('15', '5', 'X', '80', '5', '1'),
('15', '7', 'X', '61', '5', '1'),
('15', '8', 'X', '80', '0', '0'),
('15', '9', 'X', '59', '5', '1'),
('5', '32', ' ', '22', '5', '1'),
('5', '33', 'X', '41', '0', '0'),
('5', '34', 'X', '58', '10', '2'),
('5', '37', 'X', '80', '5', '1'),
('5', '38', 'X', '80', '5', '1'),
('5', '39', 'X', '80', '27', '1'),
('5', '40', 'X', '63', '0', '0'),
('5', '41', 'X', '80', '5', '1'),
('5', '42', 'X', '51', '0', '0'),
('5', '43', 'X', '80', '5', '1'),
('5', '44', 'X', '80', '15', '3'),
('5', '45', 'X', '65', '0', '0'),
('5', '46', 'X', '58', '0', '0'),
('5', '47', 'X', '80', '0', '0'),
('5', '48', 'X', '80', '10', '2'),
('5', '49', 'X', '54', '0', '0'),
('5', '50', ' ', '17', '0', '0'),
('5', '51', ' ', '29', '0', '0'),
('5', '52', ' ', '15', '0', '0'),
('5', '55', ' ', '26', '0', '0'),
('5', '57', ' ', '39', '0', '0'),
('5', '59', ' ', '22', '0', '0'),
('6', '31', 'X', '74', '0', '0'),
('6', '32', 'X', '57', '0', '0'),
('6', '33', 'X', '80', '0', '0'),
('6', '34', 'X', '46', '0', '0'),
('6', '35', 'X', '80', '0', '0'),
('6', '36', 'X', '62', '0', '0'),
('6', '37', 'X', '80', '0', '0'),
('6', '38', 'X', '80', '0', '0'),
('6', '39', 'X', '74', '15', '0'),
('6', '40', 'X', '80', '0', '0'),
('6', '41', 'X', '80', '0', '0'),
('6', '42', 'X', '76', '0', '0'),
('6', '43', 'X', '74', '0', '0'),
('6', '44', 'X', '80', '10', '2'),
('6', '45', 'X', '72', '0', '0'),
('6', '46', ' ', '23', '0', '0'),
('6', '47', ' ', '6', '0', '0'),
('6', '48', ' ', '34', '0', '0'),
('6', '49', ' ', '18', '0', '0'),
('6', '50', ' ', '6', '0', '0'),
('6', '51', ' ', '6', '0', '0'),
('6', '52', ' ', '8', '0', '0'),
('9', '1', 'X', '55', '0', '0'),
('9', '10', 'X', '60', '17', '0'),
('9', '11', 'X', '80', '10', '2'),
('9', '12', 'X', '80', '4', '0'),
('9', '13', 'X', '53', '5', '1'),
('9', '14', 'X', '80', '15', '3'),
('9', '15', 'X', '80', '0', '0'),
('9', '16', ' ', '25', '0', '0'),
('9', '19', ' ', '28', '0', '0'),
('9', '2', 'X', '52', '0', '0'),
('9', '20', ' ', '20', '0', '0'),
('9', '25', ' ', '9', '0', '0'),
('9', '28', ' ', '27', '0', '0'),
('9', '29', ' ', '20', '0', '0'),
('9', '3', 'X', '80', '0', '0'),
('9', '30', ' ', '28', '0', '0'),
('9', '4', 'X', '80', '5', '1'),
('9', '5', 'X', '71', '0', '0'),
('9', '6', 'X', '80', '10', '2'),
('9', '7', 'X', '60', '10', '2'),
('9', '8', 'X', '80', '0', '0'),
('9', '9', 'X', '52', '0', '0');

-- --------------------------------------------------------

--
-- Structure de la table `joueur`
--

DROP TABLE IF EXISTS `joueur`;
CREATE TABLE IF NOT EXISTS `joueur` (
  `numJoueur` varchar(11) NOT NULL,
  `prenom_joueur` varchar(50) DEFAULT NULL,
  `nom_joueur` varchar(50) DEFAULT NULL,
  `num_poste` varchar(11) DEFAULT NULL,
  `equipe` varchar(4) DEFAULT NULL,
  PRIMARY KEY (`numJoueur`),
  KEY `num_poste` (`num_poste`),
  KEY `equipe` (`equipe`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `joueur`
--

INSERT INTO `joueur` (`numJoueur`, `prenom_joueur`, `nom_joueur`, `num_poste`, `equipe`) VALUES
('1', 'Tony', 'Woodcock', '1', 'NZL'),
('10', 'Dan', 'Carter', '10', 'NZL'),
('100', 'Dan', 'Parks', '10', 'SCO'),
('101', 'Chris', 'Paterson', '11', 'SCO'),
('102', 'Rob', 'Dewey', '12', 'SCO'),
('103', 'Simon', 'Webster', '13', 'SCO'),
('104', 'Sean', 'Lamont', '14', 'SCO'),
('105', 'Rory', 'Lamont', '15', 'SCO'),
('106', 'Scott', 'Lawson', '1', 'SCO'),
('107', 'Craig', 'Smith', '1', 'SCO'),
('108', 'Scott', 'MacLeod', '5', 'SCO'),
('109', 'Kelly', 'Brown', '6', 'SCO'),
('11', 'Sitiveni', 'Sivivatu', '11', 'NZL'),
('110', 'Chris', 'Cusiter', '9', 'SCO'),
('111', 'Hugo', 'Southwell', '13', 'SCO'),
('112', 'Nikki', 'Walker', '10', 'SCO'),
('113', 'John', 'Barclay', '7', 'SCO'),
('114', 'David', 'Callam', '8', 'SCO'),
('115', 'Marcus', 'Di Rollo', '13', 'SCO'),
('116', 'Alasdair', 'Dickinson', '1', 'SCO'),
('117', 'Andrew', 'Henderson', '12', 'SCO'),
('118', 'Allan', 'Jacobsen', '1', 'SCO'),
('119', 'Rory', 'Lawson', '9', 'SCO'),
('12', 'Luke', 'McAlister', '12', 'NZL'),
('120', 'Euan', 'Murray', '5', 'SCO'),
('121', 'Fergus', 'Thomson', '2', 'SCO'),
('13', 'Mils', 'Muliaina', '13', 'NZL'),
('14', 'Doug', 'Howlett', '14', 'NZL'),
('15', 'Leon', 'MacDonald', '15', 'NZL'),
('16', 'Neemia', 'Tialata', '1', 'NZL'),
('17', 'Andrew', 'Hore', '2', 'NZL'),
('18', 'Greg', 'Somerville', '3', 'NZL'),
('19', 'Anton', 'Oliver', '2', 'NZL'),
('2', 'Keven', 'Mealamu', '2', 'NZL'),
('20', 'Chris', 'Masoe', '7', 'NZL'),
('21', 'Nick', 'Evans', '10', 'NZL'),
('22', 'Joe', 'Rokocoko', '11', 'NZL'),
('23', 'Conrad', 'Smith', '13', 'NZL'),
('24', 'Andrew', 'Ellis', '9', 'NZL'),
('25', 'Sione', 'Lauaki', '5', 'NZL'),
('26', 'Keith', 'Robinson', '1', 'NZL'),
('27', 'Reuben', 'Thorne', '4', 'NZL'),
('28', 'Isaia', 'Toeava', '14', 'NZL'),
('29', 'Aaron', 'Mauger', '10', 'NZL'),
('3', 'Carl', 'Hayman', '3', 'NZL'),
('30', 'Brendon', 'Leonard', '9', 'NZL'),
('31', 'Olivier', 'Milloud', '1', 'FRA'),
('32', 'Raphael', 'Ibanez', '2', 'FRA'),
('33', 'Pieter', 'de Villiers', '3', 'FRA'),
('34', 'Sebastien', 'Chabal', '4', 'FRA'),
('35', 'Jerome', 'Thion', '5', 'FRA'),
('36', 'Serge', 'Betsen', '6', 'FRA'),
('37', 'Thierry', 'Dusautoir', '7', 'FRA'),
('38', 'Julien', 'Bonnaire', '8', 'FRA'),
('39', 'Jean-Baptiste', 'Elissalde', '9', 'FRA'),
('4', 'Chris', 'Jack', '4', 'NZL'),
('40', 'Frederic', 'Michalak', '10', 'FRA'),
('41', 'Cedric', 'Heymans', '11', 'FRA'),
('42', 'Damien', 'Traille', '12', 'FRA'),
('43', 'David', 'Marty', '13', 'FRA'),
('44', 'Vincent', 'Clerc', '14', 'FRA'),
('45', 'Clement', 'Poitrenaud', '15', 'FRA'),
('46', 'Dimitri', 'Szarzewski', '2', 'FRA'),
('47', 'Jean-Baptiste', 'Poux', '1', 'FRA'),
('48', 'Lionel', 'Nallet', '5', 'FRA'),
('49', 'Yannick', 'Nyanga', '6', 'FRA'),
('5', 'Ali', 'Williams', '5', 'NZL'),
('50', 'Lionel', 'Beauxis', '10', 'FRA'),
('51', 'Yannick', 'Jauzion', '12', 'FRA'),
('52', 'Aurelien', 'Rougerie', '15', 'FRA'),
('53', 'Sebastien', 'Bruno', '1', 'FRA'),
('54', 'Christophe', 'Dominici', '11', 'FRA'),
('55', 'Imanol', 'Harinordoquy', '8', 'FRA'),
('56', 'Remy', 'Martin', '7', 'FRA'),
('57', 'Nicolas', 'Mas', '3', 'FRA'),
('58', 'Pierre', 'Mignoni', '9', 'FRA'),
('59', 'Fabien', 'Pelous', '4', 'FRA'),
('6', 'Jerry', 'Collins', '6', 'NZL'),
('60', 'David', 'Skrela', '10', 'FRA'),
('61', 'Matt', 'Dunning', '1', 'AUS'),
('62', 'Stephen', 'Moore', '2', 'AUS'),
('63', 'Al', 'Baxter', '3', 'AUS'),
('64', 'Nathan', 'Sharpe', '4', 'AUS'),
('65', 'Daniel', 'Vickerman', '5', 'AUS'),
('66', 'Rocky', 'Elsom', '6', 'AUS'),
('67', 'George', 'Smith', '7', 'AUS'),
('68', 'Wycliff', 'Palu', '8', 'AUS'),
('69', 'George', 'Gregan', '9', 'AUS'),
('7', 'Richie', 'McCaw', '7', 'NZL'),
('70', 'Stephen', 'Larkham', '10', 'AUS'),
('71', 'Lote', 'Tuqiri', '11', 'AUS'),
('72', 'Matt', 'Giteau', '12', 'AUS'),
('73', 'Stirling', 'Mortlock', '13', 'AUS'),
('74', 'Adam', 'Ashley-Cooper', '14', 'AUS'),
('75', 'Chris', 'Latham', '15', 'AUS'),
('76', 'Adam', 'Freier', '2', 'AUS'),
('77', 'Guy', 'Shepherdson', '3', 'AUS'),
('78', 'Hugh', 'McMeniman', '4', 'AUS'),
('79', 'Stephen', 'Hoiles', '8', 'AUS'),
('8', 'Rodney', 'So\'oialo', '8', 'NZL'),
('80', 'Berrick', 'Barnes', '10', 'AUS'),
('81', 'Drew', 'Mitchell', '14', 'AUS'),
('82', 'Mark', 'Gerrard', '13', 'AUS'),
('83', 'Mark', 'Chisholm', '6', 'AUS'),
('84', 'Sam', 'Cordingley', '8', 'AUS'),
('85', 'Sean', 'Hardman', '4', 'AUS'),
('86', 'Greg', 'Holmes', '1', 'AUS'),
('87', 'Julian', 'Huxley', '10', 'AUS'),
('88', 'David', 'Lyons', '3', 'AUS'),
('89', 'Cameron', 'Shepherd', '12', 'AUS'),
('9', 'Byron', 'Kelleher', '9', 'NZL'),
('90', 'Scott', 'Staniforth', '13', 'AUS'),
('91', 'Gavin', 'Kerr', '1', 'SCO'),
('92', 'Ross', 'Ford', '2', 'SCO'),
('93', 'Euan', 'Murray', '3', 'SCO'),
('94', 'Nathan', 'Hines', '4', 'SCO'),
('95', 'Jim', 'Hamilton', '5', 'SCO'),
('96', 'Jason', 'White', '6', 'SCO'),
('97', 'Allister', 'Hogg', '7', 'SCO'),
('98', 'Simon', 'Taylor', '8', 'SCO'),
('99', 'Mike', 'Blair', '9', 'SCO');

-- --------------------------------------------------------

--
-- Structure de la table `matchs`
--

DROP TABLE IF EXISTS `matchs`;
CREATE TABLE IF NOT EXISTS `matchs` (
  `numMatch` varchar(11) NOT NULL,
  `dateMatch` date DEFAULT NULL,
  `nbSpect` varchar(11) DEFAULT NULL,
  `numStade` varchar(11) DEFAULT NULL,
  `codeEquipe_r` varchar(4) DEFAULT NULL,
  `score_r` varchar(11) DEFAULT NULL,
  `nbEssais_r` varchar(11) DEFAULT NULL,
  `codeEquipe_d` varchar(4) DEFAULT NULL,
  `score_d` varchar(11) DEFAULT NULL,
  `nbEssais_d` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`numMatch`),
  KEY `numStade` (`numStade`),
  KEY `codeEquipe_r` (`codeEquipe_r`),
  KEY `codeEquipe_d` (`codeEquipe_d`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `matchs`
--

INSERT INTO `matchs` (`numMatch`, `dateMatch`, `nbSpect`, `numStade`, `codeEquipe_r`, `score_r`, `nbEssais_r`, `codeEquipe_d`, `score_d`, `nbEssais_d`) VALUES
('1', '2010-09-07', '30000', '3', 'FRA', '12', '0', 'ARG', '17', '1'),
('10', '2010-09-09', '30000', '9', 'SCO', '56', '3', 'POR', '10', '0'),
('11', '2010-09-12', '30000', '6', 'ITA', '24', '3', 'ROM', '18', '0'),
('12', '2010-09-15', '30000', '5', 'NZL', '108', '16', 'POR', '13', '0'),
('13', '2010-09-18', '30000', '2', 'SCO', '42', '3', 'ROM', '0', '0'),
('14', '2010-09-19', '30000', '2', 'ITA', '31', '3', 'POR', '5', '0'),
('15', '2010-09-23', '30000', '4', 'SCO', '0', '3', 'NZL', '40', '0'),
('16', '2010-09-25', '30000', '10', 'ROM', '14', '3', 'POR', '10', '0'),
('17', '2010-09-08', '30000', '5', 'AUS', '91', '13', 'JPN', '3', '0'),
('18', '2010-09-09', '30000', '8', 'WAL', '42', '5', 'CAN', '17', '3'),
('19', '2010-09-12', '30000', '10', 'JPN', '31', '3', 'FJI', '35', '4'),
('2', '2010-09-09', '30000', '4', 'IRL', '32', '5', 'NAM', '17', '2'),
('20', '2010-09-15', '30000', '7', 'WAL', '20', '2', 'AUS', '32', '4'),
('21', '2010-09-16', '30000', '7', 'FJI', '29', '4', 'CAN', '16', '1'),
('22', '2010-09-20', '30000', '7', 'WAL', '72', '11', 'JPN', '18', '2'),
('23', '2010-09-23', '30000', '2', 'AUS', '55', '7', 'JPN', '12', '2'),
('24', '2010-09-25', '30000', '4', 'CAN', '12', '2', 'JPN', '12', '2'),
('25', '2010-09-08', '30000', '1', 'ENG', '28', '3', 'USA', '10', '1'),
('26', '2010-09-09', '30000', '2', 'RSA', '59', '8', 'SAM', '7', '1'),
('27', '2010-09-12', '30000', '7', 'USA', '15', '2', 'TGA', '25', '3'),
('28', '2010-09-14', '30000', '3', 'ENG', '0', '0', 'RSA', '36', '3'),
('29', '2010-09-16', '30000', '7', 'SAM', '15', '0', 'TGA', '19', '1'),
('3', '2010-09-11', '30000', '5', 'ARG', '33', '4', 'GEO', '3', '0'),
('30', '2010-09-22', '30000', '1', 'RSA', '30', '4', 'TGA', '25', '3'),
('31', '2010-09-22', '30000', '8', 'ENG', '44', '4', 'SAM', '22', '1'),
('32', '2010-09-26', '30000', '9', 'SAM', '25', '3', 'USA', '21', '2'),
('4', '2010-09-15', '30000', '4', 'IRL', '14', '2', 'GEO', '10', '1'),
('5', '2010-09-16', '30000', '10', 'FRA', '87', '13', 'NAM', '10', '1'),
('6', '2010-09-21', '30000', '3', 'FRA', '25', '2', 'IRL', '3', '0'),
('7', '2010-09-22', '30000', '6', 'ARG', '63', '9', 'NAM', '3', '0'),
('8', '2010-09-26', '30000', '1', 'GEO', '30', '3', 'NAM', '0', '0'),
('9', '2010-09-08', '30000', '6', 'NZL', '76', '3', 'ITA', '14', '0');

-- --------------------------------------------------------

--
-- Structure de la table `poste`
--

DROP TABLE IF EXISTS `poste`;
CREATE TABLE IF NOT EXISTS `poste` (
  `numero` varchar(11) NOT NULL,
  `libelle` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`numero`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `poste`
--

INSERT INTO `poste` (`numero`, `libelle`) VALUES
('1', 'Premiere ligne - Pilier gauche'),
('10', 'Demi d\'ouverture'),
('11', 'Trois-quarts aile gauche'),
('12', 'Trois-quarts centre gauche'),
('13', 'Trois-quarts centre droit'),
('14', 'Trois-quarts aile droit'),
('15', 'Arriere'),
('2', 'Premiere ligne - Talonneur'),
('3', 'Premiere ligne - Pilier droit'),
('4', 'Deuxieme ligne'),
('5', 'Deuxieme ligne'),
('6', 'Troisieme ligne - Gauche'),
('7', 'Troisieme ligne - Droit'),
('8', 'Troisieme ligne - Centre'),
('9', 'Demi de melee');

-- --------------------------------------------------------

--
-- Structure de la table `stade`
--

DROP TABLE IF EXISTS `stade`;
CREATE TABLE IF NOT EXISTS `stade` (
  `numStade` varchar(11) NOT NULL,
  `ville` varchar(50) DEFAULT NULL,
  `nomStade` varchar(30) DEFAULT NULL,
  `capacite` int(11) DEFAULT NULL,
  PRIMARY KEY (`numStade`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `stade`
--

INSERT INTO `stade` (`numStade`, `ville`, `nomStade`, `capacite`) VALUES
('1', 'Lens', 'Felix Bollaert', 30000),
('10', 'Toulouse', 'Stadium', 35000),
('2', 'Paris', 'Parc des Princes', 60000),
('3', 'Saint-Denis', 'Stade de France', 80000),
('4', 'Bordeaux', 'Chaban-Delmas', 40000),
('5', 'Lyon', 'Gerland', 40000),
('6', 'Marseille', 'Velodrome', 50000),
('7', 'Montpellier', 'Mosson', 35000),
('8', 'Nantes', 'Beaujoire', 35000),
('9', 'Saint-Etienne', 'Geoffroy-Guichard', 37000);

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `arbitrer`
--
ALTER TABLE `arbitrer`
  ADD CONSTRAINT `arbitrer_ibfk_1` FOREIGN KEY (`numMatch`) REFERENCES `matchs` (`numMatch`),
  ADD CONSTRAINT `arbitrer_ibfk_2` FOREIGN KEY (`numArbitre`) REFERENCES `arbitre` (`numArbitre`);

--
-- Contraintes pour la table `jouer`
--
ALTER TABLE `jouer`
  ADD CONSTRAINT `jouer_ibfk_1` FOREIGN KEY (`numMatch`) REFERENCES `matchs` (`numMatch`),
  ADD CONSTRAINT `jouer_ibfk_2` FOREIGN KEY (`numJoueur`) REFERENCES `joueur` (`numJoueur`);

--
-- Contraintes pour la table `joueur`
--
ALTER TABLE `joueur`
  ADD CONSTRAINT `joueur_ibfk_1` FOREIGN KEY (`num_poste`) REFERENCES `poste` (`numero`),
  ADD CONSTRAINT `joueur_ibfk_2` FOREIGN KEY (`equipe`) REFERENCES `equipe` (`codeEquipe`);

--
-- Contraintes pour la table `matchs`
--
ALTER TABLE `matchs`
  ADD CONSTRAINT `matchs_ibfk_1` FOREIGN KEY (`numStade`) REFERENCES `stade` (`numStade`),
  ADD CONSTRAINT `matchs_ibfk_2` FOREIGN KEY (`codeEquipe_r`) REFERENCES `equipe` (`codeEquipe`),
  ADD CONSTRAINT `matchs_ibfk_3` FOREIGN KEY (`codeEquipe_d`) REFERENCES `equipe` (`codeEquipe`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
