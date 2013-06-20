--Query OK, 1288970 rows affected (1 min 25.32 sec)
update movimento_estoque_cota set
data = 
(select min(l.data_lancamento)
from 
hvnd l
where 
l.produto_edicao_id = movimento_estoque_cota.produto_edicao_id
group by
l.produto_edicao_id
)
where 
tipo_movimento_id = 21;
