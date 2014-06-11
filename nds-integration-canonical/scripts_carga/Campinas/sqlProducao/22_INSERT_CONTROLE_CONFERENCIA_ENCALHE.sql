-- Query OK, 1007 rows affected (0.14 sec)
insert into CONTROLE_CONFERENCIA_ENCALHE 
(data, status) 
(select 
data,
'CONCLUIDO'
from movimento_estoque
where tipo_movimento_id = 31
group by 1, 2);


