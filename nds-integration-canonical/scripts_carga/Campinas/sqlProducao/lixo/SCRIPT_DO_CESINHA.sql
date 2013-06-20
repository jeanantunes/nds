
select codigo, numero_edicao from produto p, produto_edicao pe where 
p.id = pe.produto_id
and pe.id = 138;

create table tmpTable (produto_edicao_id bigint)
insert into tmpTable (select produto_edicao_id 
        from 
    movimento_estoque_cota 
        where tipo_movimento_id = 26 group by 1);

insert into tmpTable (select produto_edicao_id 
        from 
    movimento_estoque_cota 
        where tipo_movimento_id = 26 group by 1);

select * from tmpTable

create table tmpTable2 (produto_edicao_id bigint);

insert into tmpTable2
 (select produto_edicao_id  
from movimento_estoque_cota mec  
where mec.tipo_movimento_id = 21  
and mec.produto_edicao_id not in  
(select produto_edicao_id from tmpTable) group by 1);

select 
    cod_produto, 
    num_edicao, 
    cod_cota_hvct, 
    QTDE_REPARTE_HVCT, 
    qtde_encalhe_hvct  
from CSVImport 
where produto_edicao_id in (select produto_edicao_id from tmpTable2) 
order by 1, 2

show processList

select count(*)
from movimento_estoque_cota mec
where mec.tipo_movimento_id = 21
and mec.produto_edicao_id not in 
    (select produto_edicao_id 
        from 
    tmpTable);

select * from movimento_estoque_cota where data = '2012-10-23';
select * from movimento_estoque_cota where produto_edicao_id = 138 and tipo_movimento_id = 26

select * from CSVImport where cod_produto = '27698030' and num_edicao = 1210;

-- Isso mostra que o arquivo veio sem chamada de encalhes!
select       cod_produto,       num_edicao,       sum(QTDE_REPARTE_HVCT),       sum(qtde_encalhe_hvct),      produto_edicao_id  from CSVImport   where produto_edicao_id in (select produto_edicao_id from tmpTable2)   group by 1, 2, 5
