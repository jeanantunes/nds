-- Query OK, 19056 rows affected (6.18 sec)
insert into CHAMADA_ENCALHE (
data_recolhimento, tipo_chamada_encalhe, produto_edicao_id, sequencia
)

(select data, 'MATRIZ_RECOLHIMENTO', m.produto_edicao_id, null
    from 
        movimento_estoque_cota m, 
        cota c
    where 
        c.id = m.cota_id
    and tipo_movimento_id = 26 group by m.produto_edicao_id);
