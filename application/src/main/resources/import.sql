INSERT INTO tb_user (id, first_name, last_name, email, password, enabled) VALUES (1, 'Alex', 'Brow', 'alex@gmail.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', true);
INSERT INTO tb_user (id, first_name, last_name, email, password, enabled) VALUES (2, 'Ian', 'Brow', 'ian@gmail.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', true);

INSERT INTO tb_role (id, authority) VALUES (1, 'ROLE_CLIENT');
INSERT INTO tb_role (id, authority) VALUES (2, 'ROLE_ADMIN');

INSERT INTO tb_user_role(user_id, role_id) values (1, 1);
INSERT INTO tb_user_role(user_id, role_id) values (2, 1);
INSERT INTO tb_user_role(user_id, role_id) values (2, 2);