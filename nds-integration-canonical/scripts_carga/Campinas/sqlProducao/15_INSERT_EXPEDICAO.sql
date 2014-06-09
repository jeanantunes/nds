insert into expedicao (data_expedicao, usuario)
(select data,1 from movimento_estoque where tipo_movimento_id=13 group by 1);

alter table `expedicao` add index `ndx_data_expedicao` (`data_expedicao` desc);
alter table `lancamento` add index `ndx_data_lcto_distribuidor` (`data_lcto_distribuidor` desc);
optimize table expedicao;
optimize table lancamento;

update lancamento 
set expedicao_id = 
(select e.id 
from expedicao e 
where e.data_expedicao = lancamento.data_lcto_distribuidor)
where 
produto_edicao_id 
in (select 
me.produto_edicao_id  
from movimento_estoque me
where me.tipo_movimento_id=13
group by me.produto_edicao_id);

-- ====================######## ABAIXO Scripts Tests ###############============================

explain select count(1) from lancamento 
where 
    produto_edicao_id 
        in (select 
                me.produto_edicao_id  
            from temp_script_13_target_2 me
            group by me.produto_edicao_id  );

explain select count(1) from lancamento 
where 
    produto_edicao_id 
        in (select 
                me.produto_edicao_id  
            from movimento_estoque me
            where me.tipo_movimento_id=13
            group by me.produto_edicao_id  );

select count(1)
    from movimento_estoque me, lancamento l
    where me.tipo_movimento_id=13
	and me.produto_edicao_id != l.produto_edicao_id;


drop table temp_script_13_target;

alter table `expedicao` add index `ndx_data_expedicao` (`data_expedicao` desc);
alter table `lancamento` add index `ndx_data_lcto_distribuidor` (`data_lcto_distribuidor` desc);
optimize table expedicao;
optimize table lancamento;

create table temp_script_13_target
as( select e.id as expedicao_id, l.id as id_lancamento
    from expedicao e, lancamento l
    where e.data_expedicao = l.data_lcto_distribuidor
	and e.id <> l.expedicao_id
);

alter table `temp_script_13_target` add index `ndx_expedicao_id` (`expedicao_id` desc);
alter table `temp_script_13_target` add index `ndx_data_expedicao` (`id_lancamento` desc);
optimize table temp_script_13_target;

create table temp_script_13_target_2
as( select me.produto_edicao_id
    from movimento_estoque me, lancamento l
    where me.tipo_movimento_id=13
	and me.produto_edicao_id = l.produto_edicao_id
);

alter table `temp_script_13_target_2` add index `ndx_produto_edicao_id` (`produto_edicao_id` desc);
optimize table temp_script_13_target;