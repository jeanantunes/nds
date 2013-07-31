INSERT INTO CONTROLE_CONFERENCIA_ENCALHE_COTA
(data_fim, data_inicio, data_operacao, box_id, ctrl_conf_encalhe_id, 
cota_id, usuario_id)
(select data_recolhimento, data_recolhimento, data_recolhimento,
105, cce.id, m.cota_id, 1
from 
movimento_estoque_cota m, 
chamada_encalhe ce,
controle_conferencia_encalhe cce
where 
tipo_movimento_id = 26
and ce.produto_edicao_id = m.produto_edicao_id
and cce.data = m.data
group by 1,2,3,4,5,6,7);


-- ====================######## ABAIXO Scripts Tests ###############============================

/* ponto de atenção 
create table tmpEstudo (estudo_id int);

insert into tmpEstudo 
(select estudo.id
from estudo, lancamento
where lancamento.PRODUTO_EDICAO_ID = estudo.PRODUTO_EDICAO_ID
and lancamento.DATA_LCTO_PREVISTA = estudo.DATA_LANCAMENTO
group by estudo.PRODUTO_EDICAO_ID, estudo.DATA_LANCAMENTO having count(*) > 1);

update nota_envio_item set estudo_cota_id = null
where estudo_cota_id in
(select id from estudo_cota where estudo_id in (select estudo_id from tmpEstudo));

delete from estudo_cota where estudo_id in (select estudo_id from tmpEstudo);

delete from estudo where id in (select estudo_id from tmpEstudo);

drop table tmpEstudo;

*/


