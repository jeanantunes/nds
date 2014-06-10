-- Garante que não tenha nenhum movimento para as cotas / distribuidor sem lançamento
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
date(sysdate()),
data_lancamento,
data_lancamento,
data_recolhimento,
data_recolhimento,
date(sysdate()),
'EXPEDIDO',
'LANCAMENTO',
produto_edicao_id
from hvnd 
where produto_edicao_id not in (select produto_edicao_id from lancamento) 
and data_recolhimento <> '0000-00-00' 
and data_recolhimento is not null
and produto_edicao_id is not null

);

-- ====================######## ABAIXO Scripts Tests ###############============================

select date(sysdate());

desc lancamento;