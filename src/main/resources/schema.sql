-- LECTURE_APPLY 테이블 드랍 후 생성
DROP TABLE IF EXISTS LECTURE_APPLY;

-- LECTURE_INSTANCE 테이블 드랍 후 생성 (LECTURE 테이블을 참조하므로 먼저 삭제)
DROP TABLE IF EXISTS LECTURE_INSTANCE;

-- LECTURE 테이블 드랍 후 생성
DROP TABLE IF EXISTS LECTURE;

-- MEMBER 테이블 드랍 후 생성
DROP TABLE IF EXISTS MEMBER;

-- MEMBER 테이블 생성
CREATE TABLE MEMBER
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- LECTURE 테이블 생성
CREATE TABLE LECTURE
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    title            VARCHAR(255) NOT NULL,
    instructor          VARCHAR(50)  NOT NULL
);

-- LECTURE_INSTANCE 테이블 생성
CREATE TABLE LECTURE_INSTANCE
(
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    start_date           DATE NOT NULL,
    end_date             DATE NOT NULL,
    max_participants     INT NOT NULL,
    status               VARCHAR(255),
    current_participants INT NOT NULL,
    lecture_id           BIGINT,
    FOREIGN KEY (lecture_id) REFERENCES LECTURE(id)
);

-- LECTURE_APPLY 테이블 생성
CREATE TABLE LECTURE_APPLY
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id       BIGINT,
    lecture_instance_id BIGINT,
    FOREIGN KEY (member_id) REFERENCES MEMBER(id),
    FOREIGN KEY (lecture_instance_id) REFERENCES LECTURE_INSTANCE(id)
);
