--Query OK, 19056 rows affected, 149 warnings (0.62 sec)
#update movimento_estoque set
#data = (select min(l.data_lcto_distribuidor)
#from 
#lancamento l
#where 
#l.produto_edicao_id = movimento_estoque.produto_edicao_id
#group by
#l.produto_edicao_id
#)
#where 
#tipo_movimento_id = 20;