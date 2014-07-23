insert into chamada_encalhe_lancamento
(select ce.id, l.id from chamada_encalhe ce, lancamento l
where
ce.data_recolhimento = l.data_rec_distrib
and ce.produto_edicao_id = l.produto_edicao_id);
