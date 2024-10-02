/* 회원 등록 */

INSERT INTO MEMBER (id, name) VALUES (1, 'Member1');
INSERT INTO MEMBER (id, name) VALUES (2, 'Member2');
INSERT INTO MEMBER (id, name) VALUES (3, 'Member3');
INSERT INTO MEMBER (id, name) VALUES (4, 'Member4');
INSERT INTO MEMBER (id, name) VALUES (5, 'Member5');
INSERT INTO MEMBER (id, name) VALUES (6, 'Member6');
INSERT INTO MEMBER (id, name) VALUES (7, 'Member7');
INSERT INTO MEMBER (id, name) VALUES (8, 'Member8');
INSERT INTO MEMBER (id, name) VALUES (9, 'Member9');
INSERT INTO MEMBER (id, name) VALUES (10, 'Member10');
INSERT INTO MEMBER (id, name) VALUES (11, 'Member11');
INSERT INTO MEMBER (id, name) VALUES (12, 'Member12');
INSERT INTO MEMBER (id, name) VALUES (13, 'Member13');
INSERT INTO MEMBER (id, name) VALUES (14, 'Member14');
INSERT INTO MEMBER (id, name) VALUES (15, 'Member15');
INSERT INTO MEMBER (id, name) VALUES (16, 'Member16');
INSERT INTO MEMBER (id, name) VALUES (17, 'Member17');
INSERT INTO MEMBER (id, name) VALUES (18, 'Member18');
INSERT INTO MEMBER (id, name) VALUES (19, 'Member19');
INSERT INTO MEMBER (id, name) VALUES (20, 'Member20');
INSERT INTO MEMBER (id, name) VALUES (21, 'Member21');
INSERT INTO MEMBER (id, name) VALUES (22, 'Member22');
INSERT INTO MEMBER (id, name) VALUES (23, 'Member23');
INSERT INTO MEMBER (id, name) VALUES (24, 'Member24');
INSERT INTO MEMBER (id, name) VALUES (25, 'Member25');
INSERT INTO MEMBER (id, name) VALUES (26, 'Member26');
INSERT INTO MEMBER (id, name) VALUES (27, 'Member27');
INSERT INTO MEMBER (id, name) VALUES (28, 'Member28');
INSERT INTO MEMBER (id, name) VALUES (29, 'Member29');
INSERT INTO MEMBER (id, name) VALUES (30, 'Member30');
INSERT INTO MEMBER (id, name) VALUES (31, 'Member31');
INSERT INTO MEMBER (id, name) VALUES (32, 'Member32');
INSERT INTO MEMBER (id, name) VALUES (33, 'Member33');
INSERT INTO MEMBER (id, name) VALUES (34, 'Member34');
INSERT INTO MEMBER (id, name) VALUES (35, 'Member35');
INSERT INTO MEMBER (id, name) VALUES (36, 'Member36');
INSERT INTO MEMBER (id, name) VALUES (37, 'Member37');
INSERT INTO MEMBER (id, name) VALUES (38, 'Member38');
INSERT INTO MEMBER (id, name) VALUES (39, 'Member39');
INSERT INTO MEMBER (id, name) VALUES (40, 'Member40');
INSERT INTO MEMBER (id, name) VALUES (41, 'Member41');
INSERT INTO MEMBER (id, name) VALUES (42, 'Member42');
INSERT INTO MEMBER (id, name) VALUES (43, 'Member43');
INSERT INTO MEMBER (id, name) VALUES (44, 'Member44');
INSERT INTO MEMBER (id, name) VALUES (45, 'Member45');
INSERT INTO MEMBER (id, name) VALUES (46, 'Member46');
INSERT INTO MEMBER (id, name) VALUES (47, 'Member47');
INSERT INTO MEMBER (id, name) VALUES (48, 'Member48');
INSERT INTO MEMBER (id, name) VALUES (49, 'Member49');
INSERT INTO MEMBER (id, name) VALUES (50, 'Member50');


/* 강의 등록 */
INSERT INTO LECTURE (id, title,  instructor) VALUES (1, 'Clean Architecture',  '허재');

INSERT INTO LECTURE (id, title,  instructor) VALUES (2, 'Domain-Driven Design',  '로이');

-- Dummy data for LECTURE_INSTANCE
INSERT INTO LECTURE_INSTANCE (start_date, end_date, max_participants, current_participants, lecture_id) VALUES
('2024-01-01', '2024-01-30', 30, 0, 1), -- Clean Architecture
('2024-02-01', '2024-02-28', 25, 0, 1), -- Clean Architecture
('2024-03-01', '2024-03-30', 20, 0, 2);  -- Domain-Driven Design
