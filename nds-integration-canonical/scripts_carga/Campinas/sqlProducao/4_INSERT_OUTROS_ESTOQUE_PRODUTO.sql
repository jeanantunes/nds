-- Insere edições de produtos que não existem na estoque produto mas existem no estoque de produto da cota.
-- Essa anomalia pode acontecer pq o arquivo HVND que popula o estoque produto pode não conter 
-- movimentos que estão na ESTQBOX (que insere movimentações de estoque baseados no PDV das cotas)
-- (1 min)
insert into estoque_produto 
(produto_edicao_id)
(select produto_edicao_id
from movimento_estoque_cota 
where produto_edicao_id not in (select produto_edicao_id from estoque_produto) 
group by 1);

