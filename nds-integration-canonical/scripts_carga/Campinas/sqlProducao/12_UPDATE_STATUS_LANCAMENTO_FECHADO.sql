-- Query OK, 7990 rows affected, 7990 warnings (15 min 55.77 sec)
-- 
drop table temp_script_12_target;

create table temp_script_12_target
as(
select a.data_recolhimento, a.produto_edicao_id, a.status from hvnd a, lancamento b 
where  a.produto_edicao_id = b.produto_edicao_id
and    a.status = 'F' and    a.data_recolhimento <> b.data_rec_distrib);

ALTER TABLE `temp_script_12_target` ADD INDEX `ndx_produto_edicao_id` (`produto_edicao_id` ASC);
ALTER TABLE `temp_script_12_target` ADD INDEX `ndx_status` (`status` ASC);
optimize table temp_script_12_target;

-- Executar esse update pelo prompt de comando do mysql por ser lento, evita perda de conexões 
-- ou qqr outra interferência no client que faça parar o processo
UPDATE lancamento set status = 'FECHADO', 
DATA_REC_DISTRIB = (select t.data_recolhimento from temp_script_12_target t
where t.produto_edicao_id = lancamento.produto_edicao_id group by 1)
where produto_edicao_id in (select produto_edicao_id from temp_script_12_target group by 1);

-- ---- ##### -------

drop table temp_script_12_target_2;

create table temp_script_12_target_2
as(
select a.data_lancamento, a.produto_edicao_id from hvnd a, lancamento b 
where  a.produto_edicao_id = b.produto_edicao_id
and    a.data_lancamento <> b.data_lcto_distribuidor);

ALTER TABLE `temp_script_12_target_2` ADD INDEX `ndx_produto_edicao_id` (`produto_edicao_id` ASC);
ALTER TABLE `temp_script_12_target_2` ADD INDEX `ndx_data_lancamento` (`data_lancamento` ASC);
optimize table temp_script_12_target_2;


-- Executar esse update pelo prompt de comando do mysql por ser lento, evita perda de conexões 
-- ou qqr outra interferência no client que faça parar o processo
UPDATE lancamento SET
DATA_LCTO_DISTRIBUIDOR = (select h.data_lancamento from temp_script_12_target_2 h
where h.produto_edicao_id = lancamento.produto_edicao_id order by 1 limit 1)
where produto_edicao_id in (select h.produto_edicao_id from temp_script_12_target_2 h group by 1);


-- ====================######## ABAIXO Scripts Tests ###############============================


#UPDATE lancamento set DATA_REC_DISTRIB = DATA_REC_PREVISTA where DATA_REC_DISTRIB = '0000-00-00';

-- Verificar após a execução desse script se existe DATA_REC_DISTRIB e DATA_LCTO_DISTRIBUIDOR = '0000-00-00'
-- Caso exista verificaar o que fazer, discutir.


explain select h.data_recolhimento from hvnd h, lancamento 
                        where h.status = 'F' 
                            and h.produto_edicao_id = lancamento.produto_edicao_id 
                            and h.produto_edicao_id is not null group by 1;

desc hvnd;

select count(1) from hvnd h 
where h.status = 'F'
and h.produto_edicao_id is not null;


show processlist;


#UPDATE lancamento set status = 'FECHADO', DATA_REC_DISTRIB = (select r.data_real_recolto_rcpr from rcpr r where 
#r.tipo_status_recolto_rcpr = 'REC' and r.produto_edicao_id = lancamento.produto_edicao_id and r.produto_edicao_id is not null group by 1)
#where produto_edicao_id in 
#(select r.produto_edicao_id
#from rcpr r 
#where r.tipo_status_recolto_rcpr = 'REC' and r.produto_edicao_id is not null group by 1);

#UPDATE lancamento set DATA_REC_DISTRIB = DATA_REC_PREVISTA where DATA_REC_DISTRIB = '0000-00-00';

select count(1) from temp_script_12 where DATA_LANCAMENTO between '2011-01-01' and '2011-03-31';


select count(1) from temp_script_12 t, lancamento l
where t.produto_edicao_id = l.produto_edicao_id
and t.data_recolhimento = '0000-00-00';


select a.data_recolhimento, a.produto_edicao_id from hvnd a, lancamento b 
where  a.produto_edicao_id = b.produto_edicao_id
and    a.status = 'F' and    a.data_recolhimento <> b.data_rec_distrib;

select count(1) from hvnd a, lancamento b 
where  a.produto_edicao_id = b.produto_edicao_id
and    a.status = 'F' and    a.data_recolhimento <> b.DATA_REC_DISTRIB
;

select count(1) from hvnd a, lancamento b 
where  a.produto_edicao_id = b.produto_edicao_id
and    a.data_lancamento <> b.data_lcto_distribuidor;

desc lancamento;


select count(1) from temp_script_12_target;


explain UPDATE lancamento set status = 'FECHADO', 
    DATA_REC_DISTRIB = (select t.data_recolhimento from temp_script_12_target t
                        where t.produto_edicao_id = lancamento.produto_edicao_id group by 1)
where produto_edicao_id in (select produto_edicao_id from temp_script_12_target group by 1);


SELECT * FROM INFORMATION_SCHEMA.PROCESSLIST where id = 90111;

select count(1) from lancamento 
where exists (select t.produto_edicao_id from temp_script_12_target t 
							where t.produto_edicao_id = lancamento.produto_edicao_id 
							group by 1);

select count(1) from lancamento 
where produto_edicao_id in (select t.produto_edicao_id from temp_script_12_target t 
							where t.produto_edicao_id = lancamento.produto_edicao_id 
							group by 1)
and status != 'FECHADO'
;

select t.produto_edicao_id from lancamento l, temp_script_12_target t
where t.produto_edicao_id = l.produto_edicao_id
group by 1
limit 10000000000;

desc temp_script_12_target;


select a.data_lancamento, a.produto_edicao_id from hvnd a, lancamento b 
where  a.produto_edicao_id = b.produto_edicao_id
and    a.data_lancamento <> b.data_lcto_distribuidor;