insert into fechamento_encalhe_box
(select 
qtde,data,produto_edicao_id,105
from movimento_estoque
where tipo_movimento_id = 31);
