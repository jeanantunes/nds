<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>NDS - Novo Distrib</title>

<link href="css/menu_superior.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="css/novo_distrib.css" />
<link rel="stylesheet" type="text/css" href="css/NDS.css" />
<link rel="stylesheet" href="scripts/jquery-ui-1.8.16.custom/development-bundle/themes/redmond/jquery.ui.all.css" />
<link rel="stylesheet" type="text/css" href="scripts/jquery-ui-1.8.16.custom/development-bundle/themes/redmond/jquery.ui.theme.css"/>
<link rel="stylesheet" type="text/css" href="scripts/flexigrid-1.1/css/flexigrid.pack.css" />
<link rel="stylesheet" type="text/css" href="scripts/tooltip/tipsy.css" />
<link rel="stylesheet" type="text/css" href="scripts/tooltip/tipsy-docs.css" />

<script language="javascript" type="text/javascript" src="scripts/jquery-ui-1.8.16.custom/js/jquery-1.7.1.min.js"></script>
<script language="javascript" type="text/javascript" src="scripts/jquery-ui-1.8.16.custom/js/jquery-ui-1.8.16.custom.min.js"></script>
<script language="javascript" type="text/javascript" src="scripts/NDS.js"></script>
<script language="javascript" type="text/javascript" src="scripts/utils.js"></script>
<script language="javascript" type="text/javascript" src="scripts/flexigrid-1.1/js/flexigrid.pack.js"></script>
<script language="javascript" type="text/javascript" src="scripts/tooltip/jquery.tipsy.js"></script>

<script language="javascript" type="text/javascript" src="scripts/jquery.json-2.3.min.js"></script>
<script language="javascript" type="text/javascript" src="scripts/flexigrid-1.1/js/flexigrid.js"></script>
<script language="javascript" type="text/javascript" src="scripts/jquery.ui.datepicker-pt-BR.js"></script>
<script language="javascript" type="text/javascript" src="scripts/jquery.maskmoney.js"></script>
<script language="javascript" type="text/javascript" src="scripts/jquery.maskedinput.js"></script>

<script type="text/javascript" src="scripts/tools-1.2.6/js/jquery.tools.min.js"></script>
<script type="text/javascript" src="scripts/jquery.formatCurrency-1.4.0.min.js"></script>
<script type="text/javascript" src="scripts/jquery.calculation.min.js"></script> 		
<script type="text/javascript" src="scripts/jquery.dateFormat-1.0.js"></script>

<link rel="stylesheet" type="text/css" href="scripts/tools-1.2.6/css/tools.css" />

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
															"pagina nÃ£o encontrada :</br> Mensagem de Erro: </br>xhr["
																	+ JSON
																			.stringify(xhr)
																	+ "] </br>status["
																	+ xhr.status
																	+ "] </br>index ["
																	+ index
																	+ "] </br>anchor ["
																	+ anchor);
										},
										complete : function(xhr, status, index,
												anchor) {
											focarPrimeiroElemento();
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
		
		
		$('#linkHome').click(function() {
				$('#workspace').tabs('addTab', $('#linkHome').html(),
						$('#linkHome').prop("href"));
				return false;
			});

		$('#linkHome').click();
	
	});
	
	
	var contextPath = "${pageContext.request.contextPath}";
	
	/*$(document).ready(function() {
		verificarMensagens();
	});


	function verificarMensagens() {
		if (${empty mensagens}) {
			return;
		}
		var jsonData = jQuery.toJSON(${mensagens});
		var mensagens = jQuery.evalJSON(jsonData);
		if (mensagens) {
			exibirMensagem(
				mensagens.tipoMensagem, 
				mensagens.listaMensagens
			);
		}	
	}*/
	
var pressedCtrl = false; 
var pressedAlt = false; 
var pressedShift = false;
$(document).keyup(function (e) {
	if(e.which == 17)
		pressedCtrl=false; 
	
	if (e.which == 18)
		pressedAlt=false;
	
	if (e.shiftKey==1)
		pressedShift = false;
})

$(document).keydown(function (e) {
	if(e.which == 17) 
		pressedCtrl = true; 

	if (e.shiftKey==1)
		pressedShift = true;

	if (e.which==18)
		pressedAlt=true;

	if(e.which == 38 && pressedCtrl == true) { 
		//Aqui vai o cÃ³digo e chamadas de funÃ§Ãµes para o ctrl+s
		escondeHeader();
	}
	else if(e.which == 40 && pressedCtrl == true) { 
		//Aqui vai o cÃ³digo e chamadas de funÃ§Ãµes para o ctrl+s
		mostraHeader();
	} else if (e.which == 69 && pressedShift == true) {
		// CÃ³digo para shift+e (XLS)
		if ($('#bt_xls_excell').length != 0) {
			$('#bt_xls_excell')[0].click();
		}
		
	} else if (e.which == 73 && pressedShift == true) {
		// CÃ³digo para shift+i (Imprimir PDF)
		if ($('#bt_imprimir_pdf').length != 0) {
			$('#bt_imprimir_pdf')[0].click();
		}
		
	} else if(e.which == 13) {
		// CÃ³digo para Enter (Pesquisas e Novo)
		if ($('#bt_pesquisar_cadastrar').length != 0) {
			$('#bt_pesquisar_cadastrar')[0].click();
		} else if ($('#btnPesquisar').length != 0) {
			$('#btnPesquisar')[0].click();
		}
	}
});

$(function() {
		$('.sobeHeader a').tipsy({gravity: 'ne'}); 
	});
$(function() {
	$('.headerEsconde a').tipsy({gravity: 'ne'}); 
}); 

</script>

<style>
#workspace {
	margin-top: 0px;
}
input:focus, textarea:focus{
background-color: #5C9CCC;
color:#fff;
font-weight:bold;
}

#effectSuccess, .ui-state-highlight, .ui-state-error{padding:5px; position:absolute; width:95%; line-height:30px; right:10px; top:0px; z-index:1;}


.ui-icon-info{margin-top:7px;}
</style>
</head>
<body>

<div class="header_fino">
	<img src="images/logo_treelog_fixo.png" alt="Treelog" align="left" />
	
    <ul>
    	<li><strong>UsuÃ¡rio:</strong> Junior Fonseca &nbsp;</li>
        <li><script type="text/javascript"
					language="JavaScript">
							diaSemana();
			</script>
        </li>
        <li><a href="javascript:;" title="Sair do Sistema" class="sair">Sair</a></li>
        <li><a href="javascript:;" onclick="mostraHeader();"><img src="images/seta_desce.gif" width="15" height="15" border="0" /></a></li>
    </ul>
	
</div>

		<div class="header">
			<div class="sub-header">
				<div class="logo">&nbsp;</div>

				<div class="titAplicacao">
					<h1>Treelog S/A. LogÃ­stica e DistribuiÃ§Ã£o - SP</h1>
					<h2>CNPJ: 00.000.000/00001-00</h2>
					<h3>Distrib vs.1</h3>
				</div>

				<div class="usuario">
					<div class="bt_novos">

						<label title="UsuÃ¡rio Logado no Sistema">UsuÃ¡rio: Junior
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

		<jsp:include page="/WEB-INF/jsp/commons/loading.jsp" />
		<div id="menu_principal">
        	<ul style="float:left!important;">
				<li class="headerEsconde"><img src="images/logo_treelog_fixo.png" alt="Treelog" align="left" /></li>
                <li><a id="linkHome" href='<c:url value="/index"/>'><span class="classHome">&nbsp;</span>Home</a>
				</li>
				<li><a href="javascript:;" class="trigger"><span
						class="classCadastros">&nbsp;</span>Cadastro</a>
					<ul>
						<li>
							<a href='<c:url value="/produto"/>'>Produtos</a>
						</li>
						<li><a href='<c:url value="/cadastro/edicao"/>'>Edi&ccedil;&atilde;o</a></li>
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
						<li><a href="<c:url value="/cadastro/roteirizacao"/>">RoteirizaÃ§Ã£o</a>
						</li>
						<li><a href='<c:url value="/cadastro/transportador/"/>'>Transportador</a>
						</li>
						<li><a href="Cadastro/help_cadastros.htm">Help</a>
						</li>
					</ul>
				</li>
				<li><a href="javascript:;" class="trigger"><span
						class="classFinanceiro">&nbsp;</span>Financeiro</a>
					<ul>
						<li><a href='<c:url value="/financeiro/baixa"/>'>Baixa
								BancÃ¡ria</a>
						</li>
						<li><a href="Financeiro/baixa_bancaria_manual.htm">Baixa
								BancÃ¡ria Manual</a>
						</li>
						<li><a href="Financeiro/baixa_manual_divida.htm">Baixa
								Manual de DÃ­vidas</a>
						</li>
						<li><a href='<c:url value="/financeiro/boletos/consulta"/>'>Consulta
								Boletos por Cota</a>
						</li>
						<li><a href='<c:url value="/financeiro/contaCorrenteCota/index"/>'>Conta
								Corrente</a>
						</li>
						<li><a href='<c:url value="/financeiro/debitoCreditoCota"/>'>DÃ©bitos /
								CrÃ©ditos Cota</a>
						</li>

						<li><a href="Financeiro/geracao_cobranca.htm">GeraÃ§Ã£o
								CobranÃ§a</a>
						</li>
						<li> <a href='<c:url value="/inadimplencia/index"/>'>HistÃ³rico
								de InadimplÃªncia</a>
						</li>
						<li><a href='<c:url value="/financeiro/impressaoBoletos"/>'>ImpressÃ£o
								de Boletos</a>
						</li>
						<li><a href='<c:url value="/financeiro/manutencaoStatusCota"/>'>ManutenÃ§Ã£o
								de Status Cota</a>
						</li>
						<li><a href="Financeiro/parametros_cobranca.htm">ParÃ¢metros
								de CobranÃ§a</a>
						</li>
						<li><a href='<c:url value="/suspensaoCota/index"/>'>SuspensÃ£o
								Cota</a>
						</li>
						<li><a href="Financeiro/workflow_aprovacao.htm">Work
								Flow de AprovaÃ§Ã£o</a>
						</li>
						<li><a href='<c:url value="/financeiro/consultaConsignadoCota"/>'>Consignado Cota</a>
						</li>
						<li><a href="Financeiro/help_financeiro.htm">Help</a>
						</li>
					</ul>
				</li>
				<li><a href="javascript:;" class="trigger"><span
						class="classLancamento">&nbsp;</span>LanÃ§amento</a>
					<ul>
						<li><a href="matrizLancamento">Balanceamento
								da Matriz</a>
						</li>
						<li><a href="Lancamento/consulta_consignado_jornaleiro.htm">Consulta
								Reparte Cota</a>
						</li>
						<li><a href="<c:url value='/lancamento/furoProduto'/>">Furo de
								Produto</a>
						</li>
						<li><a href="<c:url value='/lancamento/relatorioVendas'/>">RelatÃ³rio de Vendas</a>
						</li>
						<li><a href="<c:url value='/lancamento/vendaProduto'/>">Venda por Produto</a></li>
						<li><a href="Lancamento/help_lancamento.htm">Help</a>
						</li>
					</ul>
				</li>
				<li><a href="javascript:;" class="trigger"><span
						class="classEstoque">&nbsp;</span>Estoque</a>
					<ul>
						<li><a href="<c:url value="/estoque/consultaNotas"/>">Consulta
								de Notas</a>
						</li>
						<li><a href='<c:url value="/estoque/diferenca/consulta"/>'>Consulta
								Faltas e Sobras</a>
						</li>
						<li><a href='<c:url value="/estoque/extratoEdicao/index"/>'>Extrato de
								EdiÃ§Ã£o</a>
						</li>
						<li>
							<a href='<c:url value="/estoque/diferenca/lancamento"/>'>LanÃ§amento de Faltas e Sobras</a>
						</li>
						<li><a href='<c:url value="/estoque/recebimentoFisico/index"/>'>Recebimento
								Fisico</a>
						</li>
						<li><a href='<c:url value="/estoque/edicoesFechadas"/>'>EdiÃ§Ãµes Fechadas com Saldo</a>
						</li>
						<li><a href="Estoque/help_estoque.htm">Help</a>
						</li>
					</ul>
				</li>
				<li><a href="javascript:;" class="trigger"><span
						class="classExpedicao">&nbsp;</span>ExpediÃ§Ã£o</a>
					<ul>
						<li><a href="<c:url value="/mapaAbastecimento/index"/>">Mapa Abastecimento</a></li>
						<li><a href='<c:url value="/confirmacaoExpedicao/index"/>'>Confirma
								ExpediÃ§Ã£o</a>
						</li>
						<li><a
							href="Expedicao/consulta_resumos_nfe_geradas_retornadas.htm">Consulta
								Resumos das NF-e Geradas e Retornadas</a>
						</li>
						<li><a href="Expedicao/geracao_arquivo_nfe.htm">GeraÃ§Ã£o
								arquivos NF-e</a>
						</li>
						<li><a href='<c:url value="/expedicao/geracaoNFe"/>'>GeraÃ§Ã£o de NF-e</a>
						</li>
						<li><a href='<c:url value="/cotaAusente/index"/>'>Cota
								Ausente</a>
						</li>
						<li><a href="Expedicao/painel_monitor_nfe.htm">Painel
								Monitor NF-e</a>
						</li>
						<li><a href='<c:url value="/expedicao/resumo"/>'>Resumo
								de ExpediÃ§Ã£o</a>
						</li>
						<li><a href='<c:url value="/romaneio"/>'>Romaneios</a>
						</li>
						<li><a href="Expedicao/tratamento_arquivo_retorno_nfe.htm">IntegraÃ§Ã£o
								do Arquivo de Retorno NF-e</a>
						</li>
						<li><a href="Expedicao/help_expedicao.htm">Help</a>
						</li>
					</ul>
				</li>
				<li><a href="javascript:;" class="trigger"><span
						class="classRecolhimento">&nbsp;</span>Recolhimento</a>
					<ul>
						<li><a
							href="<c:url value="/devolucao/balanceamentoMatriz"/>">Balanceamento
								da Matriz</a>
						</li>
						<li><a href='<c:url value="/devolucao/chamadaEncalheAntecipada"/>'>CE
								Antecipada - Produto</a>
						</li>
						<li>
							<a href='<c:url value="/devolucao/chamadao"/>'>ChamadÃ£o</a>
						</li>
						<li>
							<a href='<c:url value="/devolucao/conferenciaEncalhe/"/>'>
								ConferÃªncia de Encalhe Cota
							</a>
						</li>
						<li>
							<a href='<c:url value="/devolucao/conferenciaEncalheContingencia/"/>'>
								ConferÃªncia de Encalhe Cota ContingÃªncia
							</a>
						</li>
						<li>
							<a href='<c:url value="/devolucao/consultaEncalhe"/>'>Consulta
								Encalhe Cota</a>
						</li>

						<li><a href='<c:url value="/devolucao/informeEncalhe/"/>'>Consulta
								de Informe de Encalhe</a>
								
						</li>
						<li><a href="Recolhimento/edicoes_chamada.htm">Consulta
								CE</a>
						</li>
						<li><a href="Recolhimento/liberacao_encalhe_conferido.htm">LiberaÃ§Ã£o
								do Encalhe Conferido</a>
						</li>
						<li><a href='<c:url value="/devolucao/vendaEncalhe/"/>'>Venda de
								Encalhe</a>
						</li>
						<li><a href="Recolhimento/help_recolhimento.htm">Help</a>
						</li>
					</ul>
				</li>
				<li><a href="javascript:;" class="trigger"><span
						class="classDevolucao">&nbsp;</span>DevoluÃ§Ã£o</a>
					<ul>
						<li><a href="<c:url value="/emissaoCE/index"/>">EmissÃ£o CE</a></li>
						<li><a href='<c:url value="/devolucao/digitacao/contagem/"/>'>DigitaÃ§Ã£o
								de Contagem para DevoluÃ§Ã£o</a>
						</li>
						<li><a href='<c:url value="devolucao/fechamentoEncalhe/"/>'>Fechamento Encalhe</a>
						</li>
						<li><a href="Devolucao/help_devolucao.htm">Help</a>
						</li>
					</ul>
				</li>
				<li>
				<li><a href="javascript:;" class="trigger"><span
						class="classNFe">&nbsp;</span>NF-e</a>
					<ul>
						<li>
							<a href="nfe/retornoNFe/">Retorno NF-e</a>
						</li>
						<li>
							<a href="nfe/consultaNFEEncalheTratamento/">Consulta NFE Encalhe Tratamento</a>
						</li>
						<li>
							<a href="nfe/painelMonitorNFe/">Painel Monitor NF-e</a>
						</li>
					</ul>
				</li>
				<li><a href="javascript:;" class="trigger"><span
						class="classAdministracao">&nbsp;</span>AdministraÃ§Ã£o</a>
					<ul>
						<li>
							<a href='<c:url value="/administracao/controleAprovacao"/>'>
								Controle AprovaÃ§Ã£o
							</a>
						</li>
						<li><a href='<c:url value="/followup"/>'>Follow Up do Sistema</a></li>
						<li><a href='<c:url value="/administracao/painelProcessamento"/>'>Painel de Processamento</a></li>
						<li><a href='<c:url value="/administracao/cadastroCalendario"/>'>CalendÃ¡rio</a></li>
						<li><a href='<c:url value="/administracao/tipoDescontoCota"/>'>Tipo de Desconto Cota</a></li>
						
						<li><a href="<c:url value="/tipoMovimento/index"/>">Tipo de Movimento</a></li>
						<li><a href="<c:url value="/administracao/cadastroTipoNota"/>">Tipo de Nota</a></li>
						
						<li><a href="Administracao/iniciar_dia.htm">Iniciar o
								Dia</a>
						</li>
						<li><a href="Administracao/fechar_dia.htm">Fechar o Dia</a>
						</li>
						<li><a href='<c:url value="/administracao/tipoProduto"/>'>Tipo de Produto</a>
						</li>
						<li><a href="Administracao/help_administracao.htm">Help</a>
						</li>
						<li><a href='<c:url value="/distribuidor/parametroCobranca/index"/>'>ParÃ¢metros de CobranÃ§a</a>
						</li>
						<li><a href='<c:url value="administracao/parametrosSistema"/>'>Par&acirc;metros do Sistema</a></li>
						<li><a href='<c:url value="/administracao/parametrosDistribuidor"/>'>Par&acirc;metros do Distribuidor</a></li>
					</ul>
				</li>
                <li><a href='help.htm'><span class="classHelp">&nbsp;</span>Help</a>
				</li>
				<li class="headerEsconde"><a href="javascript:;" title="Sair do Sistema" class="sair">Sair</a></li>
				<li id="desceHeader" class="headerEsconde"><a href="javascript:;" onclick="mostraHeader();" rel="tipsy" title="Use o atalho CRTL + Seta para baixo" style="padding-right:0px!important;"><img src="images/seta_desce.gif"  border="0" /></a></li>
			</ul>
			<div id="sobeHeader" class="sobeHeader"><a href="javascript:;" onclick="escondeHeader();" rel="tipsy" title=" Use o atalho CRTL + Seta para cima"><img src="images/seta_sobe.gif" border="0" /></a></div>
			<br clear="all"/>
			
			<div class="container">
				<div id="notify" style="display: none;"></div>
				<div id="effectSuccess" class="ui-state-default ui-corner-all" style="display: none;">
					<p>
						<span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
						<b id="idTextSuccess"></b>
					</p>
				</div>
				<div id="effectWarning" class="ui-state-highlight ui-corner-all" style="display: none;">
					<p>
						<span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
						<b id="idTextWarning"></b>
					</p>
				</div>
				<div id="effectError" class="ui-state-error ui-corner-all" style="display: none;">
					<p>
						<span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
						<b id="idTextError"></b>
					</p>
				</div>
			</div>			
			
		</div>
		
		
		<div id="workspace">
			<ul></ul>
		</div>
		
		
</body>
</html>
