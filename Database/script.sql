CREATE DATABASE `health` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_roman_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
countys_seqmedics_appointmentsCREATE TABLE `accounts` (
  `id` int NOT NULL,
  `activated` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT 'No',
  `password` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  `role` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  `username` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_k8h1bgqoplx0rkngj01pm1rgp` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_roman_ci;

CREATE TABLE `analysis` (
  `id` int NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `analysis_category_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrvtg27dxki6vjnx0462vox7vq` (`analysis_category_id`),
  CONSTRAINT `FKrvtg27dxki6vjnx0462vox7vq` FOREIGN KEY (`analysis_category_id`) REFERENCES `analysis_category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `analysis_category` (
  `id` int NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `accounts_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `analysis_category_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `analysis_document` (
  `id` int NOT NULL,
  `completed` tinyint(1) DEFAULT '0',
  `data` mediumblob,
  `doc_name` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  `doc_type` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  `done_at` time DEFAULT NULL,
  `analysis_result_document_id` int DEFAULT NULL,
  `appointment_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKlwo1qt39aahtklaeq3lmj9ytg` (`analysis_result_document_id`),
  KEY `FKeu85qip3lvm2aa7osh9ryubn8` (`appointment_id`),
  CONSTRAINT `FKeu85qip3lvm2aa7osh9ryubn8` FOREIGN KEY (`appointment_id`) REFERENCES `appointments` (`id`),
  CONSTRAINT `FKlwo1qt39aahtklaeq3lmj9ytg` FOREIGN KEY (`analysis_result_document_id`) REFERENCES `analysis_result_documents` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_roman_ci;

CREATE TABLE `analysis_document_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `analysis_document_required` (
  `analysis_document_id` int NOT NULL,
  `required_id` int NOT NULL,
  KEY `FKcd8uxyh6qfyh2by0gobymmwa8` (`required_id`),
  KEY `FKl6ikplee43yu4j831pegdmgye` (`analysis_document_id`),
  CONSTRAINT `FKcd8uxyh6qfyh2by0gobymmwa8` FOREIGN KEY (`required_id`) REFERENCES `analysis` (`id`),
  CONSTRAINT `FKl6ikplee43yu4j831pegdmgye` FOREIGN KEY (`analysis_document_id`) REFERENCES `analysis_document` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_roman_ci;

CREATE TABLE `analysis_result_documents` (
  `id` int NOT NULL,
  `completed` tinyint(1) DEFAULT '0',
  `data` mediumblob,
  `doc_name` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  `doc_type` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_roman_ci;

CREATE TABLE `analysis_result_documents_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_roman_ci;

CREATE TABLE `analysis_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `appointments` (
  `id` int NOT NULL,
  `completed` tinyint(1) DEFAULT '0',
  `day` int DEFAULT NULL,
  `ensured` bit(1) DEFAULT NULL,
  `specialization` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  `start_hour` time DEFAULT NULL,
  `analysis_document_id` int DEFAULT NULL,
  `consultation_id` int DEFAULT NULL,
  `medic_id` int DEFAULT NULL,
  `patient_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKc2tw6ef5a4jlfhgcelq9259ku` (`analysis_document_id`),
  KEY `FKtoc9asmdq1ckjce620ou0b2uq` (`consultation_id`),
  KEY `FKl0a42v2uisxbwovhl4yedxgl` (`medic_id`),
  KEY `FK8exap5wmg8kmb1g1rx3by21yt` (`patient_id`),
  CONSTRAINT `FK8exap5wmg8kmb1g1rx3by21yt` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`),
  CONSTRAINT `FKc2tw6ef5a4jlfhgcelq9259ku` FOREIGN KEY (`analysis_document_id`) REFERENCES `analysis_document` (`id`),
  CONSTRAINT `FKl0a42v2uisxbwovhl4yedxgl` FOREIGN KEY (`medic_id`) REFERENCES `medics` (`id`),
  CONSTRAINT `FKtoc9asmdq1ckjce620ou0b2uq` FOREIGN KEY (`consultation_id`) REFERENCES `consultation` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_roman_ci;

CREATE TABLE `appointments_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `confirmation tokens` (
  `id` int NOT NULL,
  `confirmed_at` datetime(6) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `expires_at` datetime(6) NOT NULL,
  `token` varchar(255) COLLATE utf8mb4_roman_ci NOT NULL,
  `account_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_4ts1sbv94p4f4y0m2g0wpfm4g` (`token`),
  KEY `FKkb29tgyjl5nb3tldhuktk1gxr` (`account_id`),
  CONSTRAINT `FKkb29tgyjl5nb3tldhuktk1gxr` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_roman_ci;

CREATE TABLE `confirmation tokens_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `consultation` (
  `id` int NOT NULL,
  `initial_diagnosis` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  `reason` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  `recommendations` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  `symptoms` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  `appointment_id` int DEFAULT NULL,
  `consultation_document_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhjs2k3tklvmvteokipbnf2xy7` (`appointment_id`),
  KEY `FKt2bfxb9bmgh6glukiioefhr1b` (`consultation_document_id`),
  CONSTRAINT `FKhjs2k3tklvmvteokipbnf2xy7` FOREIGN KEY (`appointment_id`) REFERENCES `appointments` (`id`),
  CONSTRAINT `FKt2bfxb9bmgh6glukiioefhr1b` FOREIGN KEY (`consultation_document_id`) REFERENCES `consultation_document` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_roman_ci;

CREATE TABLE `consultation_document` (
  `id` int NOT NULL,
  `data` mediumblob,
  `doc_name` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  `doc_type` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_roman_ci;

CREATE TABLE `consultation_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `consultation_document_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `countys` (
  `id` int NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `countys_prefixes` (
  `county_id` int NOT NULL,
  `prefixes_id` int NOT NULL,
  UNIQUE KEY `UK_tepjb0un5fahgv80n2by557ml` (`prefixes_id`),
  KEY `FKhmi1ee1hn5qkkju2oyha153e0` (`county_id`),
  CONSTRAINT `FKhmi1ee1hn5qkkju2oyha153e0` FOREIGN KEY (`county_id`) REFERENCES `countys` (`id`),
  CONSTRAINT `FKhyyu472k0ba9781wb4vdagwpf` FOREIGN KEY (`prefixes_id`) REFERENCES `prefixes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `countys_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `documents` (
  `id` int NOT NULL,
  `data` mediumblob,
  `doc_name` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  `doc_type` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  `account_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK61p4ibldm3a7jajha6t76c5f2` (`account_id`),
  CONSTRAINT `FK61p4ibldm3a7jajha6t76c5f2` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_roman_ci;

CREATE TABLE `documents_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `medic_schedule` (
  `id` int NOT NULL,
  `day` int DEFAULT NULL,
  `end_hour` time DEFAULT NULL,
  `specialization` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  `start_hour` time DEFAULT NULL,
  `medic_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKah2ie4gt2hc1gdxtbfl9gu7yl` (`medic_id`),
  CONSTRAINT `FKah2ie4gt2hc1gdxtbfl9gu7yl` FOREIGN KEY (`medic_id`) REFERENCES `medics` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_roman_ci;

CREATE TABLE `medic_schedule_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `medics` (
  `id` int NOT NULL,
  `age` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  `gender` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  `license_id` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  `specialization` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  `surname` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  `account_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_eh45f3951vg5qprg4gyfa1yw8` (`license_id`),
  KEY `FKgcqsah4vnkbclc69faleb5eff` (`account_id`),
  CONSTRAINT `FKgcqsah4vnkbclc69faleb5eff` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_roman_ci;

CREATE TABLE `medics_appointments` (
  `medic_id` int NOT NULL,
  `appointments_id` int NOT NULL,
  UNIQUE KEY `UK_htw3bbpgmcy2aeehgvwim149j` (`appointments_id`),
  KEY `FK31dlu4swexedjv2h8olmmg85k` (`medic_id`),
  CONSTRAINT `FK31dlu4swexedjv2h8olmmg85k` FOREIGN KEY (`medic_id`) REFERENCES `medics` (`id`),
  CONSTRAINT `FKmibwrd7vfamutub4u9u5ed5e0` FOREIGN KEY (`appointments_id`) REFERENCES `appointments` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_roman_ci;

CREATE TABLE `medics_patients` (
  `id` int NOT NULL,
  `medic_id` int DEFAULT NULL,
  `patient_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKd7uh032onr5sijb0oo1k1stue` (`medic_id`),
  KEY `FK9c90e7pxqsq1t80glvant2tbt` (`patient_id`),
  CONSTRAINT `FK9c90e7pxqsq1t80glvant2tbt` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`),
  CONSTRAINT `FKd7uh032onr5sijb0oo1k1stue` FOREIGN KEY (`medic_id`) REFERENCES `medics` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_roman_ci;

CREATE TABLE `medics_patients_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `medics_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `nurse` (
  `id` int NOT NULL,
  `age` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  `gender` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  `license_id` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  `specialization` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  `surname` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  `account_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_kkj5enk21gqf1wmy109i5pwl6` (`license_id`),
  KEY `FKqrh48frpmkhhla4wwxowel5ex` (`account_id`),
  CONSTRAINT `FKqrh48frpmkhhla4wwxowel5ex` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_roman_ci;

CREATE TABLE `nurse_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `patients` (
  `id` int NOT NULL,
  `age` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  `gender` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  `social_id` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  `surname` varchar(255) COLLATE utf8mb4_roman_ci DEFAULT NULL,
  `account_id` int DEFAULT NULL,
  `county_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_a370hmxgv0l5c9panryr1ji7d` (`email`),
  UNIQUE KEY `UK_28x7tjguxicswtcqe25079bqb` (`social_id`),
  KEY `FKflgrec6bbs3jrbf93o9fixjma` (`account_id`),
  KEY `FKh6533ngwxcq89blambfgrd0cj` (`county_id`),
  CONSTRAINT `FKflgrec6bbs3jrbf93o9fixjma` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`),
  CONSTRAINT `FKh6533ngwxcq89blambfgrd0cj` FOREIGN KEY (`county_id`) REFERENCES `countys` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_roman_ci;

CREATE TABLE `patients_appointments` (
  `patient_id` int NOT NULL,
  `appointments_id` int NOT NULL,
  UNIQUE KEY `UK_h9yb7tfxm3xxovgu685wwx233` (`appointments_id`),
  KEY `FKt0uhagt1ck6bh802nhsk40yts` (`patient_id`),
  CONSTRAINT `FK1t048t48byllr9h8f02f7qrfn` FOREIGN KEY (`appointments_id`) REFERENCES `appointments` (`id`),
  CONSTRAINT `FKt0uhagt1ck6bh802nhsk40yts` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_roman_ci;

CREATE TABLE `prefixes` (
  `id` int NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `patients_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `prefixes_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
