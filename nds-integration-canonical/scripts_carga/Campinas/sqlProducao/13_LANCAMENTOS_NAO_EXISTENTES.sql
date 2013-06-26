-- Garante que não tenha nenhum movimentos para as cotas / distribuidor sem lançamento
-- Atualizar as datas para data do rollout

insert into lancamento
(alterado_interface,
data_criacao,
data_lcto_distribuidor,
data_lcto_prevista,
data_rec_distrib,
data_rec_prevista,
data_status,
status,
tipo_lancamento,
produto_edicao_id)
(select 
true,
'2013-06-15',
data_lancamento,
data_lancamento,
data_recolhimento,
data_recolhimento,
'2013-06-15',
'EXPEDIDO',
'LANCAMENTO',
produto_edicao_id
from hvnd 
where produto_edicao_id not in (select produto_edicao_id from lancamento) 
and data_recolhimento != '0000-00-00' and data_recolhimento != '' and data_recolhimento is not null);
