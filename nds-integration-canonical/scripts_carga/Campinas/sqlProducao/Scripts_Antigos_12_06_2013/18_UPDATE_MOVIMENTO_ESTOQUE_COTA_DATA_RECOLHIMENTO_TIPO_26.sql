--Query OK, 1288886 rows affected, 65535 warnings (1 min 23.08 sec)
update movimento_estoque_cota set
data = 
(select min(l.data_recolhimento)
from 
hvnd l
where 
l.produto_edicao_id = movimento_estoque_cota.produto_edicao_id
group by
l.produto_edicao_id
)
where 
tipo_movimento_id = 26;
