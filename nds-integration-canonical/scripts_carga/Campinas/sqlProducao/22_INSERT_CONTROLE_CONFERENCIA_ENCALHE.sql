-- Query OK, 1007 rows affected (0.14 sec)
insert into CONTROLE_CONFERENCIA_ENCALHE 
(data, status) 
(select 
data,
'CONCLUIDO'
from movimento_estoque
where tipo_movimento_id = 31
group by 1, 2);


select * from CONTROLE_CONFERENCIA_ENCALHE

;

-- Scrip de limpeza p/ reprocessamento do 22 até o 32
delete from fechamento_encalhe_box;
delete from conferencia_encalhe;
delete from fechamento_encalhe;
delete from controle_conferencia_encalhe_cota;
delete from chamada_encalhe_cota;
delete from chamada_encalhe_lancamento;
delete from chamada_encalhe;
delete from controle_conferencia_encalhe;

truncate controle_fechamento_encalhe;
truncate cobranca_controle_conferencia_encalhe_cota;
truncate conferencia_enc_parcial;
truncate conferencia_encalhe;
