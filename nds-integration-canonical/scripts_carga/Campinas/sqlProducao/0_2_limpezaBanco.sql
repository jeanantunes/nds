delete from nota_envio_item;
delete from nota_envio;
delete  from fechamento_encalhe_box;
delete  from fechamento_encalhe;
delete  from controle_fechamento_encalhe;
delete  from conferencia_encalhe;
delete  from cobranca_controle_conferencia_encalhe_cota;
delete  from controle_conferencia_encalhe_cota;
delete  from controle_conferencia_encalhe;
delete  from conferencia_enc_parcial;
delete  from conferencia_encalhe;
delete  from chamada_encalhe_cota;
delete  from chamada_encalhe_lancamento;
delete  from chamada_encalhe;
delete  from negociacao_cobranca_originaria;

update movimento_financeiro_cota set baixa_cobranca_id = null;

delete from baixa_cobranca;
delete  from cobranca;
update divida set DIVIDA_RAIZ_ID = null;

delete from historico_acumulo_divida;

delete  from divida;
delete  from consolidado_mvto_financeiro_cota;
delete  from consolidado_financeiro_cota;
delete  from historico_movto_financeiro_cota;
delete  from venda_produto_movimento_financeiro;
delete  from venda_produto_movimento_estoque;
delete  from venda_produto;
delete  from nota_fiscal_produto_servico_movimento_estoque_cota;
delete  from lancamento_diferenca_movimento_estoque_cota;
delete  from parcela_negociacao;
delete  from cota_ausente_movimento_estoque_cota;
delete  from movimento_estoque_cota;
delete  from movimento_financeiro_cota;
delete  from rateio_cota_ausente;
delete  from cota_ausente;
delete  from estoque_produto_cota;

delete from lancamento_item_receb_fisico;
delete from rateio_diferenca;
update movimento_estoque set item_rec_fisico_id = null;
update item_receb_fisico set diferenca_id = null;
delete from diferenca;
delete from item_receb_fisico;
delete from lancamento_diferenca;
delete from movimento_estoque;
delete from estoque_produto;

update nota_fiscal_entrada  set nota_fiscal_entrada.STATUS_NOTA_FISCAL = 'NAO_RECEBIDA'  
where nota_fiscal_entrada.ID IN (select recebimento_fisico.NOTA_FISCAL_ID from recebimento_fisico);

update lancamento  set  lancamento.`STATUS` = 'BALANCEADO'
where lancamento.PRODUTO_EDICAO_ID IN (
	select item_nota_fiscal_entrada.PRODUTO_EDICAO_ID from nota_fiscal_entrada 
	join recebimento_fisico on recebimento_fisico.NOTA_FISCAL_ID = nota_fiscal_entrada.ID 
	join item_nota_fiscal_entrada on item_nota_fiscal_entrada.NOTA_FISCAL_ID = nota_fiscal_entrada.ID
);

update diferenca set diferenca.itemRecebimentoFisico_ID = null, diferenca.LANCAMENTO_DIFERENCA_ID = null;
delete from lancamento_item_receb_fisico;
delete from lancamento_diferenca;
delete from movimento_estoque;
delete from item_receb_fisico;
delete from rateio_diferenca;
delete from diferenca;
delete from recebimento_fisico;
delete from movimento_estoque;

update lancamento set lancamento.EXPEDICAO_ID = null;
delete  from expedicao;

delete from estoque_produto;
