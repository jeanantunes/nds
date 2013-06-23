insert into expedicao (data_expedicao, usuario)
(select data,1 from movimento_estoque where tipo_movimento_id=13 group by 1);

update lancamento 
set expedicao_id = 
(   select e.id 
    from expedicao e 
    where e.data_expedicao = lancamento.data_lcto_distribuidor)
where 
    produto_edicao_id 
        in (select 
                me.produto_edicao_id  
            from movimento_estoque me
            where me.tipo_movimento_id=13
            group by me.produto_edicao_id  );
