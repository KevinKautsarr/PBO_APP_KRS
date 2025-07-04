-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 18, 2025 at 04:19 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `dbuniv`
--

-- --------------------------------------------------------

--
-- Table structure for table `dosen`
--

CREATE TABLE `dosen` (
  `kode_dsn` varchar(10) NOT NULL,
  `nama_dsn` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `dosen`
--

INSERT INTO `dosen` (`kode_dsn`, `nama_dsn`) VALUES
('101', 'Dr. Amanda'),
('102', 'Dr. Septiya'),
('103', 'Haris , M.Kom'),
('104', 'Budiman,M.Si'),
('105', 'Herlambang,MT'),
('106', 'Dr. Aris Salaman,M.Kom');

-- --------------------------------------------------------

--
-- Table structure for table `jadwal`
--

CREATE TABLE `jadwal` (
  `kode_mk` char(5) NOT NULL,
  `kelas` varchar(10) NOT NULL,
  `hari` varchar(6) DEFAULT NULL,
  `jam` varchar(11) DEFAULT NULL,
  `ruang` varchar(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `jadwal`
--

INSERT INTO `jadwal` (`kode_mk`, `kelas`, `hari`, `jam`, `ruang`) VALUES
('101', '001', 'Senin', '07.00-08.40', 'H.3.1'),
('101', '002', 'Senin', '07.00-08.40', 'H.3.2'),
('101', '003', 'Rabu', '07.00-08.40', 'H.3.1'),
('102', '001', 'Senin', '08.40-10.20', 'H.3.1'),
('102', '002', 'Kamis', '08.40-10.20', 'H.3.2'),
('103', '001', 'Senin', '10.20-12.00', 'H.3.1'),
('103', '002', 'Senin', '10.20-12.00', 'H.3.2'),
('104', '001', 'Selasa', '07.00-08.40', 'H.3.1'),
('104', '002', 'Selasa', '07.00-08.40', 'H.3.2'),
('105', '001', 'Selasa', '08.40-10.20', 'H.3.1'),
('105', '002', 'Selasa', '08.40-10.20', 'H.3.2'),
('201', '001', 'Selasa', '10.20-12.00', 'H.3.1'),
('201', '002', 'Selasa', '10.20-12.00', 'H.3.2'),
('201', '003', 'Selasa', '10.20-12.00', 'H.3.3');

-- --------------------------------------------------------

--
-- Table structure for table `krs`
--

CREATE TABLE `krs` (
  `kode_mk` char(5) NOT NULL,
  `kelas` varchar(10) NOT NULL,
  `nim` varchar(14) NOT NULL,
  `status` varchar(10) DEFAULT 'baru' COMMENT 'baru/ulang'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `krs`
--

INSERT INTO `krs` (`kode_mk`, `kelas`, `nim`, `status`) VALUES
('101', '001', 'A11.2023.00001', 'baru'),
('101', '001', 'A11.2023.00002', 'baru'),
('101', '001', 'A11.2023.00003', 'baru'),
('101', '001', 'A11.2023.00004', 'baru'),
('101', '002', 'A11.2023.00005', 'baru'),
('101', '002', 'A11.2023.00006', 'baru'),
('101', '002', 'A11.2023.00007', 'baru'),
('101', '002', 'A11.2023.00008', 'baru'),
('102', '001', 'A11.2023.00001', 'baru'),
('102', '001', 'A11.2023.00002', 'baru'),
('102', '001', 'A11.2023.00003', 'baru'),
('102', '002', 'A11.2023.00004', 'baru'),
('102', '002', 'A11.2023.00005', 'baru');

-- --------------------------------------------------------

--
-- Table structure for table `matakuliah`
--

CREATE TABLE `matakuliah` (
  `kode_mk` char(5) NOT NULL,
  `nama_mk` varchar(30) DEFAULT NULL,
  `sks` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `matakuliah`
--

INSERT INTO `matakuliah` (`kode_mk`, `nama_mk`, `sks`) VALUES
('101', 'Kalkulus', 3),
('102', 'Matematika', 3),
('103', 'Bhs Inggris', 3),
('104', 'Dasar Kewirausahaan', 3),
('105', 'Dasar Pemrograman', 3),
('201', 'Algoritma Pemrograman', 3),
('202', 'Matematika Diskrit', 3),
('203', 'Fisika', 3),
('204', 'Pemrograman Berbasis Web', 3);

-- --------------------------------------------------------

--
-- Table structure for table `mhs`
--

CREATE TABLE `mhs` (
  `nim` varchar(14) NOT NULL,
  `nama` varchar(30) DEFAULT NULL,
  `alamat` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `mhs`
--

INSERT INTO `mhs` (`nim`, `nama`, `alamat`) VALUES
('A11.2023.00001', 'Melati Putri Sekar', 'Jl. Kawi 1, no.26'),
('A11.2023.00002', 'Siva', 'Jl. Nakula 1'),
('A11.2023.00003', 'Rima', 'Jl. Dewi Kunti 22 '),
('A11.2023.00004', 'Roy', 'Jl. Sadewa 2 no 56'),
('A11.2023.00005', 'Roni Patinasatan', 'Jl. Abimanyu, no.60'),
('A11.2023.00006', 'Jaka Sembuh', 'Jl. Werkodoro no.226'),
('A11.2023.00007', 'Budi', 'Jl. Bima Barat 26'),
('A11.2023.00008', 'Umar', 'Jl. Sengkuni 123'),
('A11.2023.00009', 'Hasan', 'Jl. Werkodoro no.226'),
('A11.2023.00010', 'Ibnu', 'Jl. Bulu Lor no.11'),
('A11.2023.00011', 'Sofii', 'Jl. Bendungan Hilir ,12');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `iduser` int(11) NOT NULL,
  `username` varchar(30) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`iduser`, `username`, `password`) VALUES
(1, 'yogi', '123'),
(2, 'adam', '123'),
(7, 'ari', '123');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `dosen`
--
ALTER TABLE `dosen`
  ADD PRIMARY KEY (`kode_dsn`);

--
-- Indexes for table `jadwal`
--
ALTER TABLE `jadwal`
  ADD PRIMARY KEY (`kode_mk`,`kelas`);

--
-- Indexes for table `krs`
--
ALTER TABLE `krs`
  ADD PRIMARY KEY (`kode_mk`,`kelas`,`nim`),
  ADD KEY `nim` (`nim`);

--
-- Indexes for table `matakuliah`
--
ALTER TABLE `matakuliah`
  ADD PRIMARY KEY (`kode_mk`);

--
-- Indexes for table `mhs`
--
ALTER TABLE `mhs`
  ADD PRIMARY KEY (`nim`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`iduser`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `iduser` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `jadwal`
--
ALTER TABLE `jadwal`
  ADD CONSTRAINT `jadwal_ibfk_2` FOREIGN KEY (`kode_mk`) REFERENCES `matakuliah` (`kode_mk`);

--
-- Constraints for table `krs`
--
ALTER TABLE `krs`
  ADD CONSTRAINT `krs_ibfk_2` FOREIGN KEY (`kode_mk`) REFERENCES `matakuliah` (`kode_mk`),
  ADD CONSTRAINT `krs_ibfk_3` FOREIGN KEY (`nim`) REFERENCES `mhs` (`nim`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
