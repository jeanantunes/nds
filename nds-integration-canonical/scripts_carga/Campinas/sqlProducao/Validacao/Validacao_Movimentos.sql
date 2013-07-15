### validacao CE X Conferencia CE X Fechamento

drop table CE;
CREATE TEMPORARY TABLE CE AS
(
SELECT   
   d.id       id_edicao   ,
            e.nome            ,
   e.codigo           ,
            d.numero_edicao          ,
   c.data_recolhimento         ,
      d.id            ,
   ROUND(sum(a.qtde_prevista))    qtde_chamada   ,
            ROUND(sum(f.qtde_recebida))    reparte_est_prod_cota,
   ROUND(sum(f.qtde_devolvida))   encalhe_est_prod_cota

FROM   CHAMADA_ENCALHE_COTA a, 
   cota      b,
   chamada_encalhe   c,
            produto_edicao    d,
   produto     e,
   estoque_produto_cota f

where   a.cota_id     = b.id 
and   a.CHAMADA_ENCALHE_id = c.id 
and   c.produto_edicao_id  = d.id
and         d.produto_id         = e.id 
and   c.produto_edicao_id  = f.produto_edicao_id
and   f.produto_edicao_id  = d.id
and         f.cota_id    = b.id
group by 1,2,3,4,5,6
limit 100000000);


-- drop table confce;
CREATE TEMPORARY TABLE CONFCE AS
(

select  d.produto_edicao_id,round(sum(d.qtde_informada)) qtd_confce 

from 
              controle_conferencia_encalhe_cota a,
              cota         b,
     controle_conferencia_encalhe      c,
              conferencia_encalhe    d,
              produto        e,
     produto_edicao     f
where  a.cota_id                     = b.id
and    a.ctrl_conf_encalhe_id         = c.id
and    d.controle_conferencia_encalhe_cota_id = a.id
and    d.produto_edicao_id        = f.id
and    f.produto_id         = e.id
group by 1) ;

-- drop table fechce;
CREATE TEMPORARY TABLE FECHCE AS
(
select  f.id, round(sum(a.quantidade)) qtd_fech
from 
              fechamento_encalhe    a,
              produto     e,
     produto_edicao  f
where   f.produto_id     = e.id
and  f.id             = a.produto_edicao_id
group by 1
);

select c.codigo,c.nome,c.numero_edicao,c.data_recolhimento,a.*,b.*,encalhe_est_prod_cota 
from confce a left outer join fechce  b on a.produto_edicao_id = b.id,  ce c
where c.id_edicao = a.produto_edicao_id 
and a.qtd_confce <> b.qtd_fech;

select false, false, sum(qtde), ce.id, m.cota_id
from 
	movimento_estoque_cota m, 
	chamada_encalhe ce
where 
	tipo_movimento_id = 21
and ce.produto_edicao_id = m.produto_edicao_id
and ce.produto_edicao_id = 387

;

select sum(QTDE_PREVISTA) 
from chamada_encalhe c, CHAMADA_ENCALHE_COTA cc 
where c.id = cc.chamada_encalhe_id 
and  produto_edicao_id = 387;

desc movimento_estoque_cota;


select * from conferencia_encalhe where QTDE <> null ;

select * from tipo_movimento where GRUPO_MOVIMENTO_ESTOQUE = 'RECEBIMENTO_REPARTE';

ALTER TABLE `pagamento_caucao_liquida`
	ADD COLUMN `BANCO_ID` BIGINT(20) NULL AFTER `FORMA_COBRANCA_CAUCAO_LIQUIDA_ID`;