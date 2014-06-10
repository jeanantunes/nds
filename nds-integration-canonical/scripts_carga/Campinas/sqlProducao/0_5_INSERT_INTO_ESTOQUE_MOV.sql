drop table estqmov;

create table estqmov (
    linha_vazia varchar(1),
    tipo int,
	produto varchar(8),
	edicao int,
	DATA date,
	NRO_DOCTO int,
	TIPO_MOVTO int,
	ORIGEM int,
	DESTINO int,
	QUANTIDADE int,
	PRECO_CAPA decimal(18,2),
	PERC_DESCTO decimal(18,2),
	DOCTO_ORIGEM int,
	FLAG_ESTORNO varchar(1),
    produto_edicao_id int);

-- deve ser executado pela console pois o arquivo nao esta local, está no servidor
LOAD DATA LOCAL INFILE 'ESTQMOV.NEW' INTO TABLE estqmov COLUMNS TERMINATED BY '|' LINES TERMINATED BY '\n';
-- LOAD DATA LOCAL INFILE 'C:\\Carga\\TXT\\10_07\\ESTQMOV.NEW' INTO TABLE estqmov COLUMNS TERMINATED BY '|' LINES TERMINATED BY '\n';

update estqmov set produto_edicao_id = (select pe.id from produto_edicao pe, produto p 
										where p.id = pe.produto_id 
										and p.codigo = produto 
										and pe.numero_edicao = edicao)
;


-- ====================######## ABAIXO Scripts Tests ###############============================
delete from estqmov where produto_edicao_id is null;

select * from estqmov where (produto = '' or produto is null) or (edicao = 0 or edicao ='' or edicao is null);

select count(1) from estqmov where produto_edicao_id is null;

select * from estqbox box, estqmov mov 
where box.produto = mov.produto
and box.edicao = mov.edicao
and mov.produto_edicao_id is null 

;



