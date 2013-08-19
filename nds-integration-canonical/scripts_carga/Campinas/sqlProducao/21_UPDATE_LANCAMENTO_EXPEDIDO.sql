-- Não Executar 

/*
create table tmpLancamento (lancamento_id int);

insert into tmpLancamento (select l.id from lancamento l, movimento_estoque m
where l.produto_edicao_id = m.produto_edicao_id
and l.data_lcto_distribuidor = m.data 
group by l.produto_edicao_id);

update lancamento set status = 'EXPEDIDO'
where id in (select lancamento_id from tmpLancamento);

drop table tmpLancamento;

*/