<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- %@taglib tagdir="/WEB-INF/tags" prefix="nds" % -->

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>NDS - Novo Distrib</title>

<base href="<c:url value="/"/>" />

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/NDS.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/themes/redmond/jquery.ui.all.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/scripts/flexigrid-1.1/css/flexigrid.pack.css" />

<script language="javascript" type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/jquery-1.6.2.js"></script>
<script language="javascript" type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.core.js"></script>
<script language="javascript" type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.effects.core.js"></script>
<script language="javascript" type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.effects.highlight.js"></script>
<script language="javascript" type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.widget.js"></script>
<script language="javascript" type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.position.js"></script>
<script language="javascript" type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.accordion.js"></script>
<script language="javascript" type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.dialog.js"></script>
<script language="javascript" type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.tabs.js"></script>
<script language="javascript" type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/NDS.js"></script>
<script language="javascript" type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/utils.js"></script>
<script language="javascript" type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/flexigrid-1.1/js/flexigrid.pack.js"></script>
	
<script language="javascript" type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.datepicker.js"></script>

<script language="javascript" type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.autocomplete.js"></script>

<script language="javascript" type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/jquery.maskmoney.js"></script>

<script language="javascript" type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/jquery.maskedinput.js"></script>

<link rel="stylesheet" type="text/css" 
	href="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/themes/redmond/jquery.ui.theme.css"/>

<script language="javascript" type="text/javascript">
	$(function() {
		$( "#tabs-cadastros" ).tabs();
		$( "#tabs-pessoas" ).tabs();
	});
</script>



<style type="text/css">

fieldset label {
	width: auto !important;
	margin-left: 10px;
}
</style>

<!-- DECORATOR HEAD -->
<decorator:head />
<!-- DECORATOR HEAD -->
</head>

<body>
	<div class="corpo">
		<div class="header">
			<div class="sub-header">
				<div class="logo">
					<img src="${pageContext.request.contextPath}/images/logo_sistema.png" width="109" height="67"
						alt="Picking Eletrônico" />
				</div>

				<div class="titAplicacao">
					<h1>Treelog S/A. Logística e Distribuição - SP</h1>

					<h2>CNPJ: 00.000.000/00001-00</h2>
					<h3>Distrib vs.1</h3>
				</div>

				<div class="usuario">
					<a href="index.htm"><img src="${pageContext.request.contextPath}/images/bt_sair.jpg"
						alt="Sair do Sistema" title="Sair do Sistema" width="63"
						height="27" border="0" align="right" />
					</a> <br clear="all" /> <span>Usuário: Junior Fonseca</span> <span>
						<script type="text/javascript" language="JavaScript">
		  	diaSemana();
		  </script> </span>
				</div>

			</div>

		</div>
		<div class="menu_superior">
			<div class="itensMenu">
				<ul class="nav" id="nav-one">
					<li><span class="classHome">&nbsp;</span><a href="index.htm">Home</a>
					</li>
					<li><span class="classCadastros">&nbsp;</span><a
						href="javascript:;">Cadastro</a>
						<ul>
							<li><a href="banco">Bancos</a>
							</li>
							<li><a href="Cadastro/cadastro_box.htm">Box</a>
							</li>
							<li><a href="Cadastro/cadastro_cotas.htm">Cotas</a>
							</li>
							<li><a href="Cadastro/cadastro_entregador.htm">Entregador</a>
							</li>
							<li><a href="Cadastro/cadastro_fiador.htm">Fiador</a>
							</li>
							<li><a href="Cadastro/cadastro_fornecedor.htm">Fornecedor</a>
							</li>
							<li><a href="Cadastro/cadastro_parciais.htm">Parciais</a>
							</li>
							<li><a href="Cadastro/cadastro_transportador.htm">Transportador</a>
							</li>
							<li><a href="Cadastro/help_cadastros.htm">Help</a>
							</li>
						</ul></li>
					<li><span class="classFinanceiro">&nbsp;</span><a
						href="javascript:;">Financeiro</a>
						<ul>
							<li><a href="Financeiro/baixa_bancaria.htm">Baixa
									Bancária</a>
							</li>
							<li><a href="Financeiro/baixa_bancaria_manual.htm">Baixa
									Bancária Manual</a>
							</li>
							<li><a href="Financeiro/baixa_manual_divida.htm">Baixa
									Manual de Dívidas</a>
							</li>
							<li><a href="Financeiro/consulta_boletos_jornaleiros.htm">Consulta
									Boletos por Cota</a>
							</li>
							<li><a href="Financeiro/conta_corrente.htm">Conta
									Corrente</a>
							</li>
							<li><a href="Financeiro/debitos_creditos.htm">Débitos /
									Créditos Cota</a>
							</li>

							<li><a href="Financeiro/geracao_cobranca.htm">Geração
									Cobrança</a>
							</li>
							<li><a href="Financeiro/historico_inadimplencia.htm">Histórico
									de Inadimplência</a>
							</li>
							<li><a href="Financeiro/impressao_boletos.htm">Impressão
									de Boletos</a>
							</li>
							<li><a href="Financeiro/cadastro_manutencao_status.htm">Manutenção
									de Status Cota</a>
							</li>
							<li><a href="Financeiro/parametros_cobranca.htm">Parâmetros
									de Cobrança</a>
							</li>
							<li><a href="Financeiro/suspensao_jornaleiro.htm">Suspensão
									Cota</a>
							</li>
							<li><a href="Financeiro/workflow_aprovacao.htm">Work
									Flow de Aprovação</a>
							</li>
							<li><a href="Financeiro/help_financeiro.htm">Help</a>
							</li>
						</ul></li>
					<li><span class="classEstoque">&nbsp;</span><a
						href="javascript:;">Estoque</a>
						<ul>
							<li><a href="Estoque/consulta_notas_sem_fisico.htm">Consulta
									de Notas</a>
							</li>
							<li><a href="estoque/diferenca/consulta">Consulta
									Faltas e Sobras</a>
							</li>
							<li><a href="Estoque/extrato_edicao.htm">Extrato de
									Edição</a>
							</li>
							<li><a href="Estoque/lancamento_faltas_sobras.htm">Lançamento
									de Faltas e Sobras</a>
							</li>
							<li><a href="Estoque/recebimento_fisico.htm">Recebimento
									Fisico</a>
							</li>
							<li><a href="Estoque/help_estoque.htm">Help</a>
							</li>
						</ul></li>
					<li><span class="classLancamento">&nbsp;</span><a
						href="javascript:;">Lançamento</a>
						<ul>
							<li><a href="matrizLancamento">Balanceamento
									da Matriz</a>
							</li>
							<li><a href="Lancamento/consulta_consignado_jornaleiro.htm">Consulta
									Reparte Cota</a>
							</li>
							<li><a href="lancamento/furoProduto">Furo de
									Produto</a>
							</li>
							<li><a href="Lancamento/help_lancamento.htm">Help</a>
							</li>
						</ul></li>
					<li><span class="classExpedicao">&nbsp;</span><a
						href="javascript:;">Expedição</a>
						<ul>
							<li><a href="Expedicao/confirma_expedicao.htm">Confirma
									Expedição</a>
							</li>
							<li><a
								href="Expedicao/consulta_resumos_nfe_geradas_retornadas.htm">Consulta
									Resumos das NF-e Geradas e Retornadas</a>
							</li>
							<li><a href="Expedicao/geracao_arquivo_nfe.htm">Geração
									arquivos NF-e</a>
							</li>
							<li><a href="Expedicao/geracao_nfe.htm">Geração de NF-e</a>
							</li>
							<li><a href="Expedicao/jornaleiro_ausente.htm">Cota
									Ausente</a>
							</li>
							<li><a href="Expedicao/painel_monitor_nfe.htm">Painel
									Monitor NF-e</a>
							</li>
							<li><a href="Expedicao/resumo_expedicao_nota.htm">Resumo
									de Expedição</a>
							</li>
							<li><a href="Expedicao/tratamento_arquivo_retorno_nfe.htm">Integração
									do Arquivo de Retorno NF-e</a>
							</li>
							<li><a href="Expedicao/help_expedicao.htm">Help</a>
							</li>
						</ul></li>
					<li><span class="classRecolhimento">&nbsp;</span><a
						href="javascript:;">Recolhimento</a>
						<ul>
							<li><a
								href="Recolhimento/balanceamento_da_matriz_recolhimento.htm">Balanceamento
									da Matriz</a>
							</li>
							<li><a href="Recolhimento/ce_antecipada.htm">CE
									Antecipada</a>
							</li>
							<li><a href="Recolhimento/chamadao.htm">Chamadão</a>
							</li>
							<li><a
								href="Recolhimento/conferencia_encalhe_jornaleiro.htm">Conferência
									de Encalhe Cota</a>
							</li>
							<li><a
								href="Recolhimento/conferencia_encalhe_jornaleiro_contingencia.htm">Conferência
									de Encalhe Cota Contingência</a>
							</li>
							<li><a href="Recolhimento/consulta_encalhe_jornaleiro.htm">Consulta
									Encalhe Cota</a>
							</li>

							<li><a href="Recolhimento/consulta_informe_encalhe.htm">Consulta
									de Informe de Encalhe</a>
							</li>
							<li><a href="Recolhimento/edicoes_chamada.htm">Consulta
									CE</a>
							</li>
							<li><a href="Recolhimento/liberacao_encalhe_conferido.htm">Liberação
									do Encalhe Conferido</a>
							</li>
							<li><a href="Recolhimento/venda_encalhe.htm">Venda de
									Encalhe</a>
							</li>
							<li><a href="Recolhimento/help_recolhimento.htm">Help</a>
							</li>
						</ul></li>
					<li><span class="classDevolucao">&nbsp;</span><a
						href="javascript:;">Devolução</a>
						<ul>
							<li><a href="Devolucao/digitacao_contagem_devolucao.htm">Digitação
									de Contagem para Devolução</a>
							</li>
							<li><a href="Devolucao/help_devolucao.htm">Help</a>
							</li>
						</ul>
					</li>
					<li><span class="classAdministracao">&nbsp;</span><a
						href="javascript:;">Administração</a>
						<ul>
							<li><a href="Administracao/cadastro_tipos_movimento.htm">Cadastrar
									Tipos de Movimento</a>
							</li>
							<li><a href="Administracao/iniciar_dia.htm">Iniciar o
									Dia</a>
							</li>
							<li><a href="Administracao/fechar_dia.htm">Fechar o Dia</a>
							</li>
							<li><a href="Administracao/help_administracao.htm">Help</a>
							</li>
						</ul>
					</li>
					<li><span class="classHelp">&nbsp;</span><a href="help.htm">Help</a>
					</li>
				</ul>
			</div>
		</div>
		<br clear="all" />

		<br />

		<div class="container">
			
			<div id="notify" style="display: none;"></div>
			<%@include file="../components/error.jsp"%>
			
			<div id="effect" class="ui-state-highlight ui-corner-all" style="display: none; position: absolute;">
				<p>
					<span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
					<b id="idTextoMensagem"></b>
				</p>
			</div>
			
			<!-- DECORATOR BODY -->
			<decorator:body />
			<!-- DECORATOR BODY -->
		</div>
		<br clear="all" />


	</div>

</body>
</html>
