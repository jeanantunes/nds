--Rows matched: 17550  Changed: 17546  Warnings: 140
update movimento_estoque set
data = (select min(l.data_recolhimento)
from 
hvnd l
where 
l.produto_edicao_id = movimento_estoque.produto_edicao_id
group by
l.produto_edicao_id
)
where 
 tipo_movimento_id = 31;
