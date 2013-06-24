--Query OK, 457668 rows affected (22.02 sec)
update movimento_estoque_cota set data = '2013-06-15' where data = '0000-00-00';
--Query OK, 11074 rows affected (0.19 sec)
update movimento_estoque set data = '2013-06-15' where data = '0000-00-00';
