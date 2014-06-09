-- Query OK, 18756 rows affected (0.97 sec)
-- Insere os moviemento de devolução para o fornecedor baseado no encalhe das cotas
-- Com exceção dos produtos que ainda estão do distribuidor (Esses produto serão excluídos após rollaut)
INSERT INTO movimento_estoque
(
 APROVADO_AUTOMATICAMENTE,
 DATA_APROVACAO,
 MOTIVO,
 STATUS,
 DATA,
 DATA_CRIACAO,
 APROVADOR_ID,
 TIPO_MOVIMENTO_ID,
 USUARIO_ID,
 QTDE,
 PRODUTO_EDICAO_ID,
 ESTOQUE_PRODUTO_ID,
 ITEM_REC_FISICO_ID,
 DATA_INTEGRACAO,
 STATUS_INTEGRACAO,
 COD_ORIGEM_MOTIVO,
 DAT_EMISSAO_DOC_ACERTO,
 NUM_DOC_ACERTO,
 ORIGEM)

(select 
	true,
	date(sysdate()),
	'CARGA',
	'APROVADO',
	me.data,
	date(sysdate()),
	null,
	66,
	1,
	me.QTDE,
	me.PRODUTO_EDICAO_ID,
	ep.ID,
 	null,
 	null,
	null,
 	null,
 	null,
	null,
	'CARGA_INICIAL'

    from movimento_estoque me,
	 estoque_produto ep,
	 hvnd h
 where me.tipo_movimento_id = 31
	and ep.produto_edicao_id = me.produto_edicao_id
	and h.produto_edicao_id = ep.produto_edicao_id
    and me.QTDE > 0
	and h.produto_edicao_id is not null

-- Robson Martins Retirado o not in - As devoluções de fornecedores devem ser contabilizadas no fornecedor
and me.produto_edicao_id in 
(select distinct(mec.produto_edicao_id) from movimento_estoque_cota mec, estoque_produto p
where mec.tipo_movimento_id = 26 
and mec.produto_edicao_id = p.produto_edicao_id 

-- Comentado - Estava gerando erro na quantidade - 108 e deveria ser 20170
-- and coalesce(p.qtde_devolucao_encalhe) > 0
) 
group by 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18
)
;

-- Atualiza o estoque do distribuidor com as quantidades enviadas p/ o fornecedor
update estoque_produto
set qtde_devolucao_fornecedor = coalesce(qtde_devolucao_fornecedor,0) + 
								coalesce((select sum(me.qtde) from movimento_estoque me 
										  where me.tipo_movimento_id = 66 
										  and me.produto_edicao_id = estoque_produto.produto_edicao_id), 
								0)
where produto_edicao_id in (select produto_edicao_id from movimento_estoque 
							where estoque_produto.produto_edicao_id = produto_edicao_id 
							and tipo_movimento_id = 66)
;


drop table temp_mvto_estq_sem_12;

create table temp_mvto_estq_sem_12 as (
select a.data, c.id as estoque_produto_id, f.nome_box,a.produto_edicao_id,d.nome,d.codigo,e.numero_edicao,
round(a.qtde) me,round(c.qtde_devolucao_encalhe) pe, round(sum(b.qtde)) mec
from movimento_estoque   a, 
     movimento_estoque_cota b, 
     estoque_produto   c,
  produto     d, 
  produto_edicao   e,
  estqbox    f
where a.tipo_movimento_id = 31 and b.tipo_movimento_id = 26
and a.produto_edicao_id = b.produto_edicao_id
and a.produto_edicao_id = c.produto_edicao_id
and c.produto_edicao_id = b.produto_edicao_id
and a.produto_edicao_id = e.id
and b.produto_edicao_id = e.id
and c.produto_edicao_id = e.id
and e.produto_id        = d.id
and a.produto_edicao_id = f.produto_edicao_id
and b.produto_edicao_id = f.produto_edicao_id
and c.produto_edicao_id = f.produto_edicao_id
and c.produto_edicao_id = e.id
-- and f.nome_box = 'ENCALHE'
and a.qtde  <> c.qtde_devolucao_encalhe  ## somente para 31 X 26
and   f.produto_edicao_id   not in (select produto_edicao_id from estqmov where tipo_movto = 12)
group by 1,2,3,4
limit 1000000)
;

select * from temp_mvto_estq_sem_12;

-- inserir movimento em estoque tipo 18, atribuir o valor ao atributo qtde o resultado da subtração de pe - me
INSERT INTO movimento_estoque
(
 APROVADO_AUTOMATICAMENTE, DATA_APROVACAO, MOTIVO, STATUS, DATA, DATA_CRIACAO, APROVADOR_ID, TIPO_MOVIMENTO_ID,
 USUARIO_ID, QTDE, PRODUTO_EDICAO_ID, ESTOQUE_PRODUTO_ID, ITEM_REC_FISICO_ID, DATA_INTEGRACAO, STATUS_INTEGRACAO,
 COD_ORIGEM_MOTIVO, DAT_EMISSAO_DOC_ACERTO, NUM_DOC_ACERTO, ORIGEM)
(
select true,	date(sysdate()),	'CARGA',	'APROVADO',	m.data,	date(sysdate()),	null,	18,	1,	(m.pe - m.me),
	m.PRODUTO_EDICAO_ID,	m.estoque_produto_id, 	null, 	null,	null, 	null, 	null,	null,	'CARGA_INICIAL'
from temp_mvto_estq_sem_12 m
where pe > me
)
;

-- inserir movimento em estoque tipo 15, atribuir o valor ao atributo qtde da tabela movimento_estoque o resultado 
-- da subtração de me - pe
INSERT INTO movimento_estoque
(
 APROVADO_AUTOMATICAMENTE, DATA_APROVACAO, MOTIVO, STATUS, DATA, DATA_CRIACAO, APROVADOR_ID, TIPO_MOVIMENTO_ID,
 USUARIO_ID, QTDE, PRODUTO_EDICAO_ID, ESTOQUE_PRODUTO_ID, ITEM_REC_FISICO_ID, DATA_INTEGRACAO, STATUS_INTEGRACAO,
 COD_ORIGEM_MOTIVO, DAT_EMISSAO_DOC_ACERTO, NUM_DOC_ACERTO, ORIGEM)
(
select true,	date(sysdate()),	'CARGA',	'APROVADO',	m.data,	date(sysdate()),	null,	15,	1,	(m.me - m.pe),
	m.PRODUTO_EDICAO_ID,	m.estoque_produto_id, 	null, 	null,	null, 	null, 	null,	null,	'CARGA_INICIAL'
from temp_mvto_estq_sem_12 m
where pe < me
)

;


-- Excluir os movimentos tipo 66 da movimento_estoque dos 47 registros da temp.
delete from movimento_estoque 
where PRODUTO_EDICAO_ID in (select t.PRODUTO_EDICAO_ID from temp_mvto_estq_sem_12 t 
							where t.PRODUTO_EDICAO_ID = movimento_estoque.PRODUTO_EDICAO_ID)
and tipo_movimento_id = 66
;

drop table temp_mvto_estq_sem_12;

create table temp_mvto_estq_qtde_igual(
select a.data, c.id as estoque_produto_id, f.nome_box,a.produto_edicao_id,d.nome,d.codigo,e.numero_edicao,
round(a.qtde) me,round(c.qtde_devolucao_encalhe) pe, round(sum(b.qtde)) mec
from movimento_estoque   a, 
     movimento_estoque_cota b, 
     estoque_produto   c,
  produto     d, 
  produto_edicao   e,
  estqbox    f
where a.tipo_movimento_id = 31 and b.tipo_movimento_id = 26
and a.produto_edicao_id = b.produto_edicao_id
and a.produto_edicao_id = c.produto_edicao_id
and c.produto_edicao_id = b.produto_edicao_id
and a.produto_edicao_id = e.id
and b.produto_edicao_id = e.id
and c.produto_edicao_id = e.id
and e.produto_id        = d.id
and a.produto_edicao_id = f.produto_edicao_id
and b.produto_edicao_id = f.produto_edicao_id
and c.produto_edicao_id = f.produto_edicao_id
and c.produto_edicao_id = e.id
-- and f.nome_box = 'ENCALHE'
and a.qtde  = c.qtde_devolucao_encalhe  ## somente para 31 X 26
and   f.produto_edicao_id   not in (select produto_edicao_id from estqmov where tipo_movto = 12)
group by 1,2,3,4
limit 1000000)
;

delete from movimento_estoque 
where PRODUTO_EDICAO_ID in (select t.PRODUTO_EDICAO_ID from temp_mvto_estq_qtde_igual t 
							where t.PRODUTO_EDICAO_ID = movimento_estoque.PRODUTO_EDICAO_ID)
and tipo_movimento_id = 66
;


-- ====================######## ABAIXO Scripts Tests ###############============================

select true, date(sysdate()),'CARGA','APROVADO',min(h.data_lancamento),date(sysdate()),	null,66,1,me.QTDE,me.PRODUTO_EDICAO_ID,ep.ID,
 	null,null,null,null,null,null, 'CARGA_INICIAL' 
from movimento_estoque me,
	 estoque_produto ep,
	 hvnd h
 where me.tipo_movimento_id = 31
	and ep.produto_edicao_id = me.produto_edicao_id
	and h.produto_edicao_id = ep.produto_edicao_id
    and me.QTDE > 0
	and h.produto_edicao_id is not null


limit 1000000000
;

select true, date(sysdate()),'CARGA','APROVADO',min(h.data_lancamento),date(sysdate()),	null,66,1,me.QTDE,me.PRODUTO_EDICAO_ID,ep.ID,
 	null,null,null,null,null,null, 'CARGA_INICIAL' 
from movimento_estoque me,
	 estoque_produto ep,
	 hvnd h
 where me.tipo_movimento_id = 31
	and ep.produto_edicao_id = me.produto_edicao_id
	and h.produto_edicao_id = ep.produto_edicao_id
    and me.QTDE > 0
	and h.produto_edicao_id is not null
and me.produto_edicao_id in 
(select distinct(mec.produto_edicao_id) from movimento_estoque_cota mec, estoque_produto p
where mec.tipo_movimento_id = 26 
and mec.produto_edicao_id = p.produto_edicao_id 
) 
group by 1,2,3,4,6,7,8,9,10,11,12,13,14,15,16,17,18
limit 1000000000
;


select sum(qtde) from movimento_estoque_cota where produto_edicao_id = 149751 and tipo_movimento_id = 26;

select distinct(mec.produto_edicao_id) from movimento_estoque_cota mec, estoque_produto p
where mec.tipo_movimento_id = 26 
and mec.produto_edicao_id = p.produto_edicao_id 

;

select * from movimento_estoque me, estoque_produto
where me.tipo_movimento_id = 66 
and me.produto_edicao_id = estoque_produto.produto_edicao_id;

select count(1) from movimento_estoque me
where TIPO_MOVIMENTO_ID = 31
and me.produto_edicao_id in 
							(select distinct(mec.produto_edicao_id) from movimento_estoque_cota mec, estoque_produto p where mec.tipo_movimento_id = 26 
							and mec.produto_edicao_id = p.produto_edicao_id 
							and coalesce(p.qtde_devolucao_encalhe) > 0) 
and not exists ( select 1 from estqbox where produto_edicao_id = me.produto_edicao_id and nome_box = 'ENCALHE') 
;

select * from estqbox;

select * from estoque_produto
where produto_edicao_id in (select produto_edicao_id from movimento_estoque where estoque_produto.produto_edicao_id = produto_edicao_id and tipo_movimento_id = 66);

select * from movimento_estoque where TIPO_MOVIMENTO_ID = 66;
select * from estoque_produto where id = 16531;

-- Robson Martins- Limpeza de tabela caso movimento precise ser reprocessado
/*
*/

delete from movimento_estoque 
where TIPO_MOVIMENTO_ID = 66;


select * from movimento_estoque
where PRODUTO_EDICAO_ID in( select PRODUTO_EDICAO_ID from temp_mvto_estq_sem_12 t 
							where movimento_estoque.PRODUTO_EDICAO_ID = t.PRODUTO_EDICAO_ID group by 1)
and tipo_movimento_id = 66
;


select * from movimento_estoque 
where produto_edicao_id not in(
	select e.produto_edicao_id from estqmov e
	where movimento_estoque.produto_edicao_id = e.produto_edicao_id
	and e.tipo_movto =12
)
and tipo_movimento_id = 66
and produto_edicao_id in
	(142411,144811,145111,145121,145151,149751,152591,152721,152761,153221,153381,153391,153731,156911,157251,157281,157331,157381,157451,157561,157631,157671,157791,158251,
		158271,158291,158321,158351,158431,158461,158521,158541,158551,158681,159451,160831,161111,161601,162151,162171,162341,162501,162681,163451,165431,168301,168501,170361,177031
	)

;

select * from movimento_estoque
where tipo_movimento_id = 66
and produto_edicao_id in
	(142411,144811,145111,145121,145151,149751,152591,152721,152761,153221,153381,153391,153731,156911,157251,157281,157331,157381,157451,157561,157631,157671,157791,158251,
		158271,158291,158321,158351,158431,158461,158521,158541,158551,158681,159451,160831,161111,161601,162151,162171,162341,162501,162681,163451,165431,168301,168501,170361,177031
	)

;


select sum(coalesce(qtde_devolucao_fornecedor,0) + 
								coalesce((select sum(me.qtde) from movimento_estoque me 
										  where me.tipo_movimento_id = 66 
										  and me.produto_edicao_id = estoque_produto.produto_edicao_id), 
								0)) from estoque_produto

where produto_edicao_id in (select produto_edicao_id from movimento_estoque 
							where estoque_produto.produto_edicao_id = produto_edicao_id 
							and tipo_movimento_id = 66)
;

select sum(me.qtde) from movimento_estoque me 
where me.tipo_movimento_id = 66 
								
;

-- preco_venda com preco_venda
-- preco_custo colocar PRECO_COM_DESCONTO da movimento_estoque_cota

update movimento_estoque_cota 
set PRECO_COM_DESCONTO = (select p.preco_venda - (p.preco_venda*movimento_estoque_cota.valor_desconto/100) from produto_edicao p where p.id = movimento_estoque_cota.PRODUTO_EDICAO_ID)
where PRODUTO_EDICAO_ID in (select p.id from produto_edicao p where p.id = movimento_estoque_cota.PRODUTO_EDICAO_ID)
and PRECO_COM_DESCONTO = 0
and (TIPO_MOVIMENTO_ID = 21 or TIPO_MOVIMENTO_ID = 26)

;


select count(1) from movimento_estoque_cota 
where 
PRODUTO_EDICAO_ID in (select p.id from produto_edicao p where p.id = movimento_estoque_cota.PRODUTO_EDICAO_ID)
and PRECO_COM_DESCONTO = 0
and (TIPO_MOVIMENTO_ID = 21 or TIPO_MOVIMENTO_ID = 26)

;

select preco_venda, (select p.preco_venda - (p.preco_venda*movimento_estoque_cota.valor_desconto/100) 
			from produto_edicao p 
			where p.id = movimento_estoque_cota.PRODUTO_EDICAO_ID)
from movimento_estoque_cota 
where 
PRODUTO_EDICAO_ID in (select p.id from produto_edicao p where p.id = movimento_estoque_cota.PRODUTO_EDICAO_ID)
and PRECO_COM_DESCONTO = 0
and (TIPO_MOVIMENTO_ID = 21 or TIPO_MOVIMENTO_ID = 26)

;

desc movimento_estoque_cota;


update movimento_estoque 
set TIPO_MOVIMENTO_ID = 15
where produto_edicao_id in(
152591,
153731,
161111,
152761,
153221,
157451,
162681,
168301
)
and TIPO_MOVIMENTO_ID = 18
;