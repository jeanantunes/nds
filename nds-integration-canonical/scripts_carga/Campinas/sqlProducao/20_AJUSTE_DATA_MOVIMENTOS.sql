-- Query OK, 457668 rows affected (22.02 sec)
-- Não deve mais ocorrer Fazer com begin apenas para verificação, caso ocorra, verificar nos primeiros inserts dos movimento_estoque_cota e movimento_estoque o porquê.

select * from movimento_estoque_cota where data = '0000-00-00';

update movimento_estoque_cota set data = '2013-06-15' where data = '0000-00-00';


select * from movimento_estoque where data = '0000-00-00';
-- Query OK, 11074 rows affected (0.19 sec)
update movimento_estoque set data = '2013-06-15' where data = '0000-00-00';
