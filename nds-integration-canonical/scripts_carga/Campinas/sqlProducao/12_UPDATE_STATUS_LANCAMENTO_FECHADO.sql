-- Query OK, 7990 rows affected, 7990 warnings (15 min 55.77 sec)
#UPDATE lancamento set status = 'FECHADO', DATA_REC_DISTRIB = (select r.data_real_recolto_rcpr from rcpr r where r.tipo_status_recolto_rcpr = 'REC' and r.produto_edicao_id = lancamento.produto_edicao_id and r.produto_edicao_id is not null group by 1)
#where produto_edicao_id in 
#(select r.produto_edicao_id
#from rcpr r 
#where r.tipo_status_recolto_rcpr = 'REC' and r.produto_edicao_id is not null group by 1);

#UPDATE lancamento set DATA_REC_DISTRIB = DATA_REC_PREVISTA where DATA_REC_DISTRIB = '0000-00-00';

UPDATE lancamento set status = 'FECHADO', 
    DATA_REC_DISTRIB = (select h.data_recolhimento from hvnd h
                        where h.status = 'F' 
                            and h.produto_edicao_id = lancamento.produto_edicao_id 
                            and h.produto_edicao_id is not null group by 1)
where produto_edicao_id in (select h.produto_edicao_id from hvnd h 
                            where h.status = 'F'
                            and h.produto_edicao_id is not null group by 1);

UPDATE lancamento SET
    DATA_LCTO_DISTRIBUIDOR = (select h.data_lancamento from hvnd h
                        where h.produto_edicao_id = lancamento.produto_edicao_id 
                            and h.produto_edicao_id is not null group by 1 order by 1 limit 1)
where produto_edicao_id in (select h.produto_edicao_id from hvnd h 
                            where h.produto_edicao_id is not null group by 1);


#UPDATE lancamento set DATA_REC_DISTRIB = DATA_REC_PREVISTA where DATA_REC_DISTRIB = '0000-00-00';