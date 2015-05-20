<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- %@taglib tagdir="/WEB-INF/tags" prefix="nds" % -->

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>STG-Sistema Treelog de GestÃ£o</title>

<base href="<c:url value="/"/>" />

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/NDS.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/themes/redmond/jquery.ui.all.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/scripts/flexigrid-1.1/css/flexigrid.pack.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/tooltip/tipsy.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/tooltip/tipsy-docs.css" />
<script language="javascript" type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/js/jquery-1.7.1.min.js"></script>
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
	src="${pageContext.request.contextPath}/scripts/jquery.json-2.3.min.js"></script>
<script language="javascript" type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/NDS.js"></script>
<script language="javascript" type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/utils.js"></script>
<!-- TODO: Posteriormente incluir os métodos 'personalizados': 
<script language="javascript" type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/flexigrid-1.1/js/flexigrid.pack.js"></script>
 -->
<script language="javascript" type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/flexigrid-1.1/js/flexigrid.js"></script>
<script language="javascript" type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.datepicker.js"></script>
<script language="javascript" type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/jquery.ui.datepicker-pt-BR.js"></script>
<script language="javascript" type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.autocomplete.js"></script>

<script language="javascript" type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/jquery.maskmoney.js"></script>

<script language="javascript" type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/jquery.maskedinput.js"></script>

<link rel="stylesheet" type="text/css" 
	href="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/themes/redmond/jquery.ui.theme.css"/>
	
<script type="text/javascript"
 		src="${pageContext.request.contextPath}/scripts/tools-1.2.6/js/jquery.tools.min.js"></script>

<script type="text/javascript"
 		src="${pageContext.request.contextPath}/scripts/jquery.formatCurrency-1.4.0.min.js"></script>

<script type="text/javascript"
 		src="${pageContext.request.contextPath}/scripts/jquery.calculation.min.js"></script> 		

<script type="text/javascript"
 		src="${pageContext.request.contextPath}/scripts/jquery.dateFormat-1.0.js"></script>

<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/tooltip/jquery.tipsy.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonsbehaviour.js"></script>

<link rel="stylesheet" type="text/css"
	  href="${pageContext.request.contextPath}/scripts/tools-1.2.6/css/tools.css" />

<script language="javascript" type="text/javascript">
	$(function() {
		$( "#tabs-cadastros" ).tabs();
		$( "#tabs-pessoas" ).tabs();
	});
	
	var contextPath = "${pageContext.request.contextPath}";
	
	$(document).ready(function() {

		$("#ajaxLoading").ajaxStart(function() {
			
			$(this).fadeIn(200);
		});
		 
		$("#ajaxLoading").ajaxStop(function() {
			
			$(this).fadeOut(200);
		});
		
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
	}

</script>



<style type="text/css">

	fieldset label {
		width: auto !important;
		margin-left: 10px;
	}
	
	.ui-datepicker-today a {	display:block !important; }
	
	#ui-datepicker-div { z-index: 99999 !important; }

	#ajaxLoading {
		position: absolute;	z-index: 99999;
		left: 0px; top: 0px; width: 100%; height: 100%; margin: 0;
	}
	#ajaxLoading #shadow {
		background: #D0D0D0;
		position: fixed; z-index: 1;
		filter: alpha(opacity=50); opacity: 0.5;
		left: 0px; top: 0px; width: 100%; height: 100%; margin: 0;
	}
	#ajaxLoading #panel {
		position: fixed; z-index: 2;
		top: 50%; left: 50%; width: 100px; height: 100px;
		margin-top: -50px; margin-left: -50px;
		text-align: center; vertical-align: 50%;
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
		
		<jsp:include page="/WEB-INF/jsp/commons/loading.jsp" />
		
		<div class="menu_superior">
			<div class="itensMenu">
				<ul class="nav" id="nav-one">
					<li><span class="classHome">&nbsp;</span><a href="index.htm">Home</a>
					</li>
					
					<li><span class="classAdministracao">&nbsp;</span><a
						href="javascript:;">Administração</a>
						<ul>
							<li>
								<a href='<c:url value="/administracao/controleAprovacao"/>'>
									Controle Aprovação
								</a>
							</li>
							<li><a href='<c:url value="/painelOperacional/"/>'>Painel Operacional</a></li>
							<li><a href='<c:url value="/administracao/cadastroCalendario"/>'>Calendário</a></li>
							<li><a href='<c:url value="/administracao/tipoDescontoCota"/>'>Tipo de Desconto Cota</a></li>
							
							<li><a href="<c:url value="/tipoMovimento/index"/>">Tipo de Movimento</a></li>

							<li><a href="<c:url value="/administracao/cadastroTipoNota"/>">Tipo de Nota</a></li>
							
							<li><a href="Administracao/iniciar_dia.htm">Iniciar o
									Dia</a>
							</li>
							<li><a href="Administracao/fechar_dia.htm">Fechar o Dia</a>
							</li>
							<li><a href='<c:url value="/servico/cadastroServico"/>'>Serviço de Entrega</a>
							</li>
							<li><a href='<c:url value="/administracao/tipoProduto"/>'>Tipo de Produto</a>
							</li>
							<li><a href="Administracao/help_administracao.htm">Help</a>
							</li>
							<li><a href='<c:url value="/distribuidor/parametroCobranca/index"/>'>Parâmetros de Cobrança</a>
							</li>
							<li><a href='<c:url value="administracao/parametrosSistema"/>'>Par&acirc;metros do Sistema</a></li>
							<li><a href='<c:url value="/administracao/parametrosDistribuidor"/>'>Par&acirc;metros do Distribuidor</a></li>
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
			
			<div id="effectSuccess" class="ui-state-default ui-corner-all" 
					style="display: none; position: absolute; width: 980px; z-index: 10;">
				<p>
					<span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
					<b id="idTextSuccess"></b>
				</p>
			</div>
			<div id="effectWarning" class="ui-state-highlight ui-corner-all" 
					style="display: none; position: absolute; width: 980px; z-index: 10;">
				<p>
					<span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
					<b id="idTextWarning"></b>
				</p>
			</div>
			<div id="effectError" class="ui-state-error ui-corner-all" 
					style="display: none; position: absolute; width: 980px; z-index: 10;">
				<p>
					<span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
					<b id="idTextError"></b>
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
