-- phpMyAdmin SQL Dump
-- version 4.0.10deb1
-- http://www.phpmyadmin.net
--
-- Servidor: localhost
-- Tiempo de generación: 15-10-2016 a las 19:07:50
-- Versión del servidor: 5.5.50-0ubuntu0.14.04.1
-- Versión de PHP: 5.5.9-1ubuntu4.19

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de datos: `portal`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `enlaces`
--

CREATE TABLE IF NOT EXISTS `enlaces` (
  `id_enlace` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(30) DEFAULT NULL,
  `id_url` int(11) DEFAULT NULL,
  `id_menu` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_enlace`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `idiomas`
--

CREATE TABLE IF NOT EXISTS `idiomas` (
  `id_idioma` int(11) NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(100) NOT NULL,
  PRIMARY KEY (`id_idioma`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `menu`
--

CREATE TABLE IF NOT EXISTS `menu` (
  `id_menu` int(11) NOT NULL AUTO_INCREMENT,
  `id_padre` int(11) DEFAULT NULL,
  `id_raiz` int(11) DEFAULT NULL,
  `nivel` int(11) DEFAULT NULL,
  `nombre` varchar(50) NOT NULL,
  PRIMARY KEY (`id_menu`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rel_urls_xsl`
--

CREATE TABLE IF NOT EXISTS `rel_urls_xsl` (
  `id_url` int(11) NOT NULL,
  `id_xsl` int(11) NOT NULL,
  PRIMARY KEY (`id_url`,`id_xsl`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `urls`
--

CREATE TABLE IF NOT EXISTS `urls` (
  `id_url` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(100) NOT NULL,
  PRIMARY KEY (`id_url`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `xsl`
--

CREATE TABLE IF NOT EXISTS `xsl` (
  `id_xsl` int(11) NOT NULL AUTO_INCREMENT,
  `contenido` text,
  `nombre_ejb` varchar(100) DEFAULT NULL,
  `id_idioma` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_xsl`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `xsls_principales`
--

CREATE TABLE IF NOT EXISTS `xsls_principales` (
  `id_xsl` int(11) NOT NULL AUTO_INCREMENT,
  `contenido` longtext NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `id_idioma` int(11) NOT NULL,
  PRIMARY KEY (`id_xsl`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
