--Query OK, 19056 rows affected, 149 warnings (0.65 sec)
update movimento_estoque set
data = (select min(l.data_lancamento)
from 
hvnd l
where 
l.produto_edicao_id = movimento_estoque.produto_edicao_id
group by
l.produto_edicao_id
)
where 
tipo_movimento_id = 13;
