/*
select '  Desconto : ',count(*) from desconto_logistica;
select '    Editor : ',count(*) from editor;
select '   Produto : ',count(*) from produto;
select '    Edição : ',count(*) from produto_edicao;
select 'Lançamento : ',count(*) from lancamento;
select 'Consignado : ', sum(preco * qtde_reparte_hvct) from hvnd where status = 'A';

select 'Produtos:';
select h.cod_produto,ppe.codigo
from (select distinct cod_produto from hvnd where status = 'A') h
left join produto ppe
on (h.cod_produto = ppe.codigo) 
where ppe.codigo is null;

select 'Edicoes:';
select h.cod_produto,h.num_edicao
from (select distinct cod_produto,num_edicao from hvnd where status = 'A') h
left join (select p.codigo,pe.numero_edicao from produto p,produto_edicao pe where p.id = pe.produto_id) ppe
on (h.cod_produto = ppe.codigo and h.num_edicao = ppe.numero_edicao) 
where (ppe.codigo is null or ppe.numero_edicao is null);

select 'Cotas:';
select distinct cod_cota_hvct from HVND where cod_cota_hvct not in (select numero_cota from cota);

select 'Segmento:';
select codigoProduto,codigoEdicao,segmento from carga_prodin_prd 
where segmento not in (select id from tipo_segmento_produto);



select '  Desconto : ',count(*) from desconto_logistica union all
select '    Editor : ',count(*) from editor union all
select '   Produto : ',count(*) from produto union all
select '    Edição : ',count(*) from produto_edicao union all
select 'Lançamento : ',count(*) from lancamento union all
select 'Consignado : ', sum(preco * qtde_reparte_hvct) from hvnd where status = 'A';


select * from hvnd

select * from lancamento
select count(*) from lancamento where DATA_LCTO_DISTRIBUIDOR is null union all
select count(*) from lancamento where DATA_LCTO_PREVISTA is null union all
select count(*) from lancamento where DATA_REC_DISTRIB is null union all
select count(*) from lancamento where DATA_REC_PREVISTA is null union all

select * from hvnd

select 'Produtos Não encontrados:';
select h.cod_produto,ppe.codigo
from (select distinct cod_produto from hvnd where status = 'A') h
left join produto ppe
on (h.cod_produto = ppe.codigo) 
where ppe.codigo is null;

select 'Edicoes não encontradas:';
select h.cod_produto,h.num_edicao
from (select distinct cod_produto,num_edicao from hvnd where status = 'A') h
left join (select p.codigo,pe.numero_edicao from produto p,produto_edicao pe where p.id = pe.produto_id) ppe
on (h.cod_produto = ppe.codigo and h.num_edicao = ppe.numero_edicao) 
where (ppe.codigo is null or ppe.numero_edicao is null);

select 'Cotas: não encontradas:';
select distinct cod_cota_hvct from HVND where cod_cota_hvct not in (select numero_cota from cota);

select 'Segmentos não encontrados (PUB):';
select codigoProduto,segmento from carga_prodin_pub 
where segmento not in (select DESCRICAO from tipo_segmento_produto);


select 'Segmentos não encontrados (PRD):';
select codigoProduto,codigoEdicao,segmento from carga_prodin_prd 
where segmento not in (select id from tipo_segmento_produto);

select 'Classificação não encontrados:';
select codigoProduto,codigoEdicao,segmento from carga_prodin_prd 
where segmento not in (select id from tipo_segmento_produto);

select * from carga_prodin_lan
select * from carga_mdc_matriz



update HVND set 
cod_produto=substring(COD_PRODUTO_HVCT, -12, 8), 
num_edicao=substring(COD_PRODUTO_HVCT, -4),
DATA_LANCAMENTO = STR_TO_DATE(DATA_LANCAMENTO_STR, '%d/%m/%Y'),
DATA_RECOLHIMENTO = STR_TO_DATE(DATA_RECOLHIMENTO_STR, '%d/%m/%Y');

ALTER TABLE HVND 
DROP COD_PRODUTO_HVCT, 
-- DROP PRECO, 
DROP DATA_LANCAMENTO_STR,
DROP DATA_RECOLHIMENTO_STR;

update HVND h,produto_edicao pe, produto p 
set h.produto_edicao_id = pe.id
where p.id = pe.produto_id 
and p.codigo = h.cod_produto 
and pe.numero_edicao = h.num_edicao;


update HVND set cota_id = (select c.id from cota c where c.numero_cota = HVND.COD_COTA_HVCT);

ALTER TABLE `HVND` ADD INDEX `hvnd_cota_id` (`COD_COTA_HVCT` ASC, `cod_produto` ASC, `num_edicao` ASC, `produto_edicao_id` ASC) ;

ALTER TABLE `HVND` ADD INDEX `hvnd_cota_status` (`STATUS` ASC) ;

ALTER TABLE `HVND` ADD INDEX `cota_id` (`cota_id` ASC);

select 'HVND: ',sysdate(); -- Log 

select * from produto where codigo = '15188001'


select * from HVND

select * from cota


select * from produto_edicao


select distinct FORMA_COMERCIALIZACAO from produto


*/
