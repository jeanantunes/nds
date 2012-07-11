<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>NDS - Novo Distrib</title>

<link href="css/menu_superior.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="css/novo_distrib.css" />
<link rel="stylesheet" type="text/css" href="css/NDS.css" />
<link rel="stylesheet"
	href="scripts/jquery-ui-1.8.16.custom/development-bundle/themes/redmond/jquery.ui.all.css" />
<link rel="stylesheet" type="text/css"
	href="scripts/flexigrid-1.1/css/flexigrid.pack.css" />

<script language="javascript" type="text/javascript"
	src="scripts/jquery-ui-1.8.16.custom/development-bundle/jquery-1.6.2.js"></script>
<script language="javascript" type="text/javascript"
	src="scripts/jquery-ui-1.8.16.custom/js/jquery-ui-1.8.16.custom.min.js"></script>
<script language="javascript" type="text/javascript"
	src="scripts/NDS.js"></script>
<script language="javascript" type="text/javascript"
	src="scripts/flexigrid-1.1/js/flexigrid.pack.js"></script>

<script type="text/javascript">
	(function($) {
		$.fn
				.extend(
						$.ui.tabs.prototype,
						{
							_original_init : $.ui.tabs.prototype._init,
							_init : function() {
								var self = this.element;

								this._original_init();
								this.element
										.children('.ui-tabs-nav')
										.after(
												'<div class="ui-tabs-strip-spacer"></div>');

								var tabOpt = {
									tabTemplate : "<li><a href='#\{href\}'>#\{label\}</a> <span class='ui-icon ui-icon-close'>Remove Tab</span></li>",
									add : function(event, ui) {
										self.tabs('select', '#' + ui.panel.id);
									},
									ajaxOptions : {
										error : function(xhr, status, index,
												anchor) {
											$(anchor.hash)
													.html(
															"pagina não encontrada :</br> Mensagem de Erro: </br>xhr["
																	+ JSON
																			.stringify(xhr)
																	+ "] </br>status["
																	+ xhr.status
																	+ "] </br>index ["
																	+ index
																	+ "] </br>anchor ["
																	+ anchor);
										}
									},
									cache : true
								};

								$.fn.extend(this.options, tabOpt);
								this.addCloseTab();
							},
							addTab : function(title, url) {
								var self = this.element, o = this.options, add = true;
								$("li", self).each(function() {
									if ($("a", this).html() == title) {
										var index = $("li", self).index(this);
										self.tabs('select', index);
										add = false;
									}
								});
								if (add) {
									self.tabs('add', url, title);
								}
							},
							addCloseTab : function() {
								var self = this.element, o = this.options;
								$("span.ui-icon-close", self).live(
										'click',
										function() {
											var index = $("li", $(self)).index(
													$(this).parent());
											if (index > -1)
												$(self).tabs("remove", index);
										});
							}
						});
	})(jQuery);

	$(function() {

		$('#workspace').tabs();

		// Dinamicaly add tabs from menu
		$("#menu_principal ul li ul li").click(
				function() {
					$('#workspace').tabs('addTab', $("a", this).html(),
							$("a", this).prop("href"));
					return false;
				});

	});
</script>

<style>
#workspace {
	margin-top: 0px;
}
</style>
</head>
<body>

	<div class="corpo">
		<div class="header">
			<div class="sub-header">
				<div class="logo">&nbsp;</div>

				<div class="titAplicacao">
					<h1>Treelog S/A. Logística e Distribuição - SP</h1>
					<h2>CNPJ: 00.000.000/00001-00</h2>
					<h3>Distrib vs.1</h3>
				</div>

				<div class="usuario">
					<div class="bt_novos">

						<label title="Usuário Logado no Sistema">Usuário: Junior
							Fonseca</label>
					</div>
					<div class="bt_novos">
						<label> <script type="text/javascript"
								language="JavaScript">
							diaSemana();
						</script> </label>
					</div>
					<div class="bt_novos">
						<a href="javascript:;" title="Sair do Sistema" class="sair">Sair</a>
					</div>

				</div>
			</div>
		</div>


		<div id="menu_principal">
			<ul>
				<li><a href="index.htm"><span class="classHome">&nbsp;</span>Home</a>
				</li>
				<li><a href="javascript:;" class="trigger"><span
						class="classCadastros">&nbsp;</span>Cadastro</a>
					<ul>
						<li><a href='<c:url value="/produto"/>'>Produtos</a>
						</li>
						<li><a href='<c:url value="/cadastro/edicao"/>'>Edi&ccedil;&atilde;o</a>
						</li>
						<li><a href='<c:url value="/banco/bancos"/>'>Bancos</a>
						</li>
						<li><a href='<c:url value="/cadastro/box"/>'>Box</a>
						</li>
						<li><a href='<c:url value="/cadastro/cota"/>'>Cotas</a>
						</li>
						<li><a href="<c:url value="/cadastro/entregador"/>">Entregador</a>
						</li>
						<li><a href='<c:url value="/cadastro/fiador/"/>'>Fiador</a>
						</li>
						<li><a href='<c:url value="/cadastro/fornecedor"/>'>Fornecedor</a>
						</li>
						<li><a href="<c:url value="/parciais/index"/>">Parciais</a>
						</li>
						<li><a href="<c:url value="/cadastro/roteirizacao"/>">Roteirização</a>
						</li>
						<li><a href='<c:url value="/cadastro/transportador/"/>'>Transportador</a>
						</li>
						<li><a href="Cadastro/help_cadastros.htm">Help</a>
						</li>
					</ul>
				</li>
				<li><a href="javascript:;" class="trigger"><span
						class="classLancamento">&nbsp;</span>Lançamento</a>
					<ul>
						<li><a href="matrizLancamento">Balanceamento da Matriz</a></li>
						<li><a href="Lancamento/consulta_consignado_jornaleiro.htm">Consulta
								Reparte Cota</a></li>
						<li><a href="<c:url value='/lancamento/furoProduto'/>">Furo
								de Produto</a></li>
						<li><a href="<c:url value='/lancamento/relatorioVendas'/>">Relatório
								de Vendas</a></li>
						<li><a href="<c:url value='/lancamento/vendaProduto'/>">Venda
								por Produto</a>
						</li>
						<li><a href="Lancamento/help_lancamento.htm">Help</a></li>
					</ul>
				</li>
				<li><a href="javascript:;" class="trigger"><span
						class="classEstoque">&nbsp;</span>Estoque</a>
					<ul>
						<li><a href="<c:url value="/estoque/consultaNotas"/>">Consulta
								de Notas</a></li>
						<li><a href='<c:url value="/estoque/diferenca/consulta"/>'>Consulta
								Faltas e Sobras</a></li>
						<li><a href='<c:url value="/estoque/extratoEdicao/index"/>'>Extrato
								de Edição</a></li>
						<li><a href='<c:url value="/estoque/diferenca/lancamento"/>'>Lançamento
								de Faltas e Sobras</a></li>
						<li><a
							href='<c:url value="/estoque/recebimentoFisico/index"/>'>Recebimento
								Fisico</a></li>
						<li><a href='<c:url value="/estoque/edicoesFechadas"/>'>Edições
								Fechadas com Saldo</a></li>
						<li><a href="Estoque/help_estoque.htm">Help</a></li>
					</ul>
				</li>

				<li><a href="javascript:;" class="trigger"><span
						class="classExpedicao">&nbsp;</span>Expedição</a>
					<ul>
						<li><a href="<c:url value="/mapaAbastecimento/index"/>">Mapa
								Abastecimento</a>
						</li>
						<li><a href='<c:url value="/confirmacaoExpedicao/index"/>'>Confirma
								Expedição</a></li>
						<li><a
							href="Expedicao/consulta_resumos_nfe_geradas_retornadas.htm">Consulta
								Resumos das NF-e Geradas e Retornadas</a></li>
						<li><a href="Expedicao/geracao_arquivo_nfe.htm">Geração
								arquivos NF-e</a></li>
						<li><a href="Expedicao/geracao_nfe.htm">Geração de NF-e</a></li>
						<li><a href='<c:url value="/cotaAusente/index"/>'>Cota
								Ausente</a></li>
						<li><a href="Expedicao/painel_monitor_nfe.htm">Painel
								Monitor NF-e</a></li>
						<li><a href='<c:url value="/expedicao/resumo"/>'>Resumo
								de Expedição</a></li>
						<li><a href='<c:url value="/romaneio"/>'>Romaneios</a></li>
						<li><a href="Expedicao/tratamento_arquivo_retorno_nfe.htm">Integração
								do Arquivo de Retorno NF-e</a></li>
						<li><a href="Expedicao/help_expedicao.htm">Help</a></li>
					</ul>
				</li>
				<li><a href="javascript:;" class="trigger"><span
						class="classDevolucao">&nbsp;</span>Devolução</a>
					<ul>
						<li><a href='<c:url value="/devolucao/digitacao/contagem/"/>'>Digitação
								de Contagem para Devolução</a></li>
						<li><a href='<c:url value="devolucao/fechamentoEncalhe/"/>'>Fechamento
								Encalhe</a></li>
						<li><a href="Devolucao/help_devolucao.htm">Help</a></li>
					</ul></li>
				<li><a href="javascript:;" class="trigger"><span
						class="classNFe">&nbsp;</span>NF-e</a>
					<ul>
						<li><a href="nfe/retornoNFe/">Retorno NF-e</a></li>
						<li><a href="nfe/painelMonitorNFe/">Painel Monitor NF-e</a></li>
					</ul>
				</li>


				<li><a href="javascript:;" class="trigger"><span
						class="classFinanceiro">&nbsp;</span>Financeiro</a>
					<ul>
						<li><a href='<c:url value="/financeiro/baixa"/>'>Baixa
								Bancária</a></li>
						<li><a href="Financeiro/baixa_bancaria_manual.htm">Baixa
								Bancária Manual</a></li>
						<li><a href="Financeiro/baixa_manual_divida.htm">Baixa
								Manual de Dívidas</a></li>
						<li><a href='<c:url value="/financeiro/boletos/consulta"/>'>Consulta
								Boletos por Cota</a></li>
						<li><a
							href='<c:url value="/financeiro/contaCorrenteCota/index"/>'>Conta
								Corrente</a></li>
						<li><a href='<c:url value="/financeiro/debitoCreditoCota"/>'>Débitos
								/ Créditos Cota</a></li>

						<li><a href="Financeiro/geracao_cobranca.htm">Geração
								Cobrança</a></li>
						<li><a href='<c:url value="/inadimplencia/index"/>'>Histórico
								de Inadimplência</a></li>
						<li><a href='<c:url value="/financeiro/impressaoBoletos"/>'>Impressão
								de Boletos</a></li>
						<li><a
							href='<c:url value="/financeiro/manutencaoStatusCota"/>'>Manutenção
								de Status Cota</a></li>
						<li><a href="Financeiro/parametros_cobranca.htm">Parâmetros
								de Cobrança</a></li>
						<li><a href='<c:url value="/suspensaoCota/index"/>'>Suspensão
								Cota</a></li>
						<li><a href="Financeiro/workflow_aprovacao.htm">Work Flow
								de Aprovação</a></li>
						<li><a href="Financeiro/help_financeiro.htm">Help</a></li>
					</ul>
				</li>
				<li><a href="javascript:;" class="trigger"><span
						class="classAdministracao">&nbsp;</span>Administração</a>
					<ul>
						<li><a
							href='<c:url value="/administracao/controleAprovacao"/>'>
								Controle Aprovação </a></li>
						<li><a
							href='<c:url value="/administracao/cadastroCalendario"/>'>Calendário</a>
						</li>
						<li><a
							href='<c:url value="/administracao/tipoDescontoCota"/>'>Tipo
								de Desconto Cota</a>
						</li>

						<li><a href="<c:url value="/tipoMovimento/index"/>">Tipo
								de Movimento</a>
						</li>


						<li><a href="Administracao/iniciar_dia.htm">Iniciar o Dia</a>
						</li>
						<li><a href="Administracao/fechar_dia.htm">Fechar o Dia</a></li>
						<li><a href='<c:url value="/servico/cadastroServico"/>'>Serviço
								de Entrega</a></li>
						<li><a href='<c:url value="/administracao/tipoProduto"/>'>Tipo
								de Produto</a></li>
						<li><a href="Administracao/help_administracao.htm">Help</a></li>
						<li><a
							href='<c:url value="/distribuidor/parametroCobranca/index"/>'>Parâmetros
								de Cobrança</a></li>
						<li><a
							href='<c:url value="administracao/parametrosSistema"/>'>Par&acirc;metros
								do Sistema</a>
						</li>

					</ul></li>
				<li><a href="help.htm"><span class="classHelp">&nbsp;</span>Help</a>
				</li>
			</ul>
			<br class="clearit">
		</div>


		<div id="workspace">
			<ul></ul>
		</div>

	</div>

</body>
</html>