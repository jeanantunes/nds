<head>
<title>Parâmetros do Distribuidor</title>
<style type="text/css">
	#dialog-confirm{display:none;}
	label{width:auto!important;}
	#dialog-pesq-fornecedor fieldset {width:450px!important;}
	.forncedoresSel{display:none;}
	#dialog-pesq-fornecedor{display:none;}
	.forncedores ul{margin:0px; padding:0px;}
	.forncedores li{display:inline;}
	.forncedoresSel, .editorSel {
	    padding: 0px!important;
	}
</style>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/editor/jquery.wysiwyg.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/editor/jquery.wysiwyg.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/editor/wysiwyg.image.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/editor/wysiwyg.link.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/editor/wysiwyg.table.js"></script>
<script language="javascript" type="text/javascript">

function gravarDiasDistribuidorFornecedor() {	
	
	var listaFornecedoresLancamento = [];
	var listaDiasLancamento         = [];
	var listaDiasRecolhimento       = [];

	$('#selectFornecedoresLancamento :selected').each(function(i, selected) {
		listaFornecedoresLancamento[i] = $(selected).val();
	    //textvalues[i] = $(selected).text();
	});

	$('#selectDiasLancamento :selected').each(function(i, selected) {
		listaDiasLancamento[i] = $(selected).val();
	});

	$('#selectDiasRecolhimento :selected').each(function(i, selected) {
		listaDiasRecolhimento[i] = $(selected).val();
	});
	
	var data = [{name: 'selectFornecedoresLancamento', value: listaFornecedoresLancamento},
	            {name: 'selectDiasLancamento', 		   value: listaDiasLancamento},
	            {name: 'selectDiasRecolhimento', 	   value: listaDiasRecolhimento}];

	$.postJSON("<c:url value='/administracao/parametrosDistribuidor/gravarDiasDistribuidorFornecedor'/>", 
			   data,
			   function (resultado) {
					exibirMensagem(resultado.tipoMensagem, 
								   resultado.listaMensagens);
					recarregarDiasDistribuidorFornecedorGrid();
			   });
}

function excluirDiasDistribuidorFornecedor(id) {
	var data = [{name: 'codigoFornecedor', value: id}];
	$.postJSON("<c:url value='/administracao/parametrosDistribuidor/excluirDiasDistribuicaoFornecedor'/>",
			   data,
			   function (resultado) {
				   exibirMensagem(resultado.tipoMensagem, 
				   				  resultado.listaMensagens);
					recarregarDiasDistribuidorFornecedorGrid();
			   });
}

function recarregarDiasDistribuidorFornecedorGrid() {
	$.postJSON("<c:url value='/administracao/parametrosDistribuidor/recarregarDiasDistribuidorFornecedorGrid'/>", 
			   null,
			   function (resultado) {
					if (resultado.mensagens) {
						exibirMensagem(
							resultado.tipoMensagem, 
							resultado.listaMensagens
						);
					}
					var startTag = '<table width="441" border="0" cellpadding="0" cellspacing="1" id="tableDiasDistribuidorFornecedor"><tr class="header_table"><td>Fornecedor</td><td align="center">Lançamento</td><td align="center">Recolhimento</td><td align="center">&nbsp;</td></tr>';
					var endTag = '</table>';
					var newTable = startTag;
					$.each(resultado.rows, function(index, row) {
						newTable += '<tr class="class_linha_1"><td width="139">' + row.cell.fornecedor.juridica.nomeFantasia + '</td><td width="144" align="center">' + row.cell.diasLancamento + '</td><td width="125" align="center">' + row.cell.diasRecolhimento + '</td><td width="28" align="center"><a href="javascript:;" onclick="excluirDiasDistribuidorFornecedor(' + row.cell.fornecedor.id + ')" ><img src="${pageContext.request.contextPath}/images/ico_excluir.gif" width="15" height="15" alt="Excluir" /></a></td></tr>';
					});
					newTable += endTag;
					document.getElementById('spanDiasDistribuidorFornecedor').innerHTML=newTable
			   });
}

function gravar() {
	var data = [
		{name:'parametrosDistribuidor.relancamentoParciaisEmDias', value: $('#relancamentoParciaisEmDias').val()},
		{name:'parametrosDistribuidor.aceitaEncalheJuramentada', value: $('#aceitaEncalheJuramentada').is(':checked')},
		{name:'parametrosDistribuidor.diaRecolhimentoPrimeiro', value: $('#diaRecolhimentoPrimeiro').is(':checked')},
		{name:'parametrosDistribuidor.diaRecolhimentoSegundo', value: $('#diaRecolhimentoSegundo').is(':checked')},
		{name:'parametrosDistribuidor.diaRecolhimentoTerceiro', value: $('#diaRecolhimentoTerceiro').is(':checked')},
		{name:'parametrosDistribuidor.diaRecolhimentoQuarto', value: $('#diaRecolhimentoQuarto').is(':checked')},
		{name:'parametrosDistribuidor.diaRecolhimentoQuinto', value: $('#diaRecolhimentoQuinto').is(':checked')},
		{name:'parametrosDistribuidor.limiteCEProximaSemana', value: $('#limiteCEProximaSemana').is(':checked')},
		{name:'parametrosDistribuidor.conferenciaCegaRecebimento', value: $('#conferenciaCegaRecebimento').is(':checked')},
		{name:'parametrosDistribuidor.conferenciaCegaEncalhe', value: $('#conferenciaCegaEncalhe').is(':checked')},
		{name:'parametrosDistribuidor.capacidadeManuseioHomemHoraLancamento', value: $('#capacidadeManuseioHomemHoraLancamento').val()},
		{name:'parametrosDistribuidor.capacidadeManuseioHomemHoraRecolhimento', value: $('#capacidadeManuseioHomemHoraRecolhimento').val()},
		{name:'parametrosDistribuidor.reutilizacaoCodigoCotaInativa', value: $('#reutilizacaoCodigoCotaInativa').val()},
		{name:'parametrosDistribuidor.obrigacaoFiscao', value: $('#obrigacaoFiscao').is(':checked')},
		{name:'parametrosDistribuidor.regimeEspecial', value: $('#regimeEspecial').is(':checked')},
		{name:'parametrosDistribuidor.distribuidor', value: $("[name='distribuidor']").val()},
		{name:'parametrosDistribuidor.slipImpressao', value: $('#slipImpressao').is(':checked')},
		{name:'parametrosDistribuidor.slipEmail', value: $('#slipEmail').is(':checked')},
		{name:'parametrosDistribuidor.boletoImpressao', value: $('#boletoImpressao').is(':checked')},
		{name:'parametrosDistribuidor.boletoEmail', value: $('#boletoEmail').is(':checked')},
		{name:'parametrosDistribuidor.boletoSlipImpressao', value: $('#boletoSlipImpressao').is(':checked')},
		{name:'parametrosDistribuidor.boletoSlipEmail', value: $('#boletoSlipEmail').is(':checked')},
		{name:'parametrosDistribuidor.reciboImpressao', value: $('#reciboImpressao').is(':checked')},
		{name:'parametrosDistribuidor.reciboEmail', value: $('#reciboEmail').is(':checked')},
		{name:'parametrosDistribuidor.notaEnvioImpressao', value: $('#notaEnvioImpressao').is(':checked')},
		{name:'parametrosDistribuidor.notaEnvioEmail', value: $('#notaEnvioEmail').is(':checked')},
		{name:'parametrosDistribuidor.chamadaEncalheImpressao', value: $('#chamadaEncalheImpressao').is(':checked')},
		{name:'parametrosDistribuidor.chamadaEncalheEmail', value: $('#chamadaEncalheEmail').is(':checked')},
		{name:'parametrosDistribuidor.impressaoNE', value: $("input[name='impressaoNE']:checked").val()},
		{name:'parametrosDistribuidor.impressaoNEFaltaDe', value: $('#impressaoNEFaltaDe').is(':checked')},
		{name:'parametrosDistribuidor.impressaoNEFaltaEm', value: $('#impressaoNEFaltaEm').is(':checked')},
		{name:'parametrosDistribuidor.impressaoNESobraDe', value: $('#impressaoNESobraDe').is(':checked')},
		{name:'parametrosDistribuidor.impressaoNESobraEm', value: $('#impressaoNESobraEm').is(':checked')},
		{name:'parametrosDistribuidor.impressaoNECADANFE', value: $("input[name='impressaoNECADANFE']:checked").val()},
		{name:'parametrosDistribuidor.impressaoNECADANFEFaltaDe', value: $('#impressaoNECADANFEFaltaDe').is(':checked')},
		{name:'parametrosDistribuidor.impressaoNECADANFEFaltaEm', value: $('#impressaoNECADANFEFaltaEm').is(':checked')},
		{name:'parametrosDistribuidor.impressaoNECADANFESobraDe', value: $('#impressaoNECADANFESobraDe').is(':checked')},
		{name:'parametrosDistribuidor.impressaoNECADANFESobraEm', value: $('#impressaoNECADANFESobraEm').is(':checked')},
		{name:'parametrosDistribuidor.impressaoCE', value: $("input[name='impressaoCE']:checked").val()},
		{name:'parametrosDistribuidor.impressaoCEFaltaDe', value: $('#impressaoCEFaltaDe').is(':checked')},
		{name:'parametrosDistribuidor.impressaoCEFaltaEm', value: $('#impressaoCEFaltaEm').is(':checked')},
		{name:'parametrosDistribuidor.impressaoCESobraDe', value: $('#impressaoCESobraDe').is(':checked')},
		{name:'parametrosDistribuidor.impressaoCESobraEm', value: $('#impressaoCESobraEm').is(':checked')},
		{name:'parametrosDistribuidor.utilizaContratoComCotas', value: $('#utilizaContratoComCotas').is(':checked')},
		{name:'parametrosDistribuidor.prazoContrato', value: $('#prazoContrato').val()},
		{name:'parametrosDistribuidor.informacoesComplementaresContrato', value: $('#informacoesComplementaresContrato').val()},
		{name:'parametrosDistribuidor.utilizaProcuracaoEntregadores', value: $('#utilizaProcuracaoEntregadores').is(':checked')},
		{name:'parametrosDistribuidor.informacoesComplementaresProcuracao', value: $('#informacoesComplementaresProcuracao').val()},
		{name:'parametrosDistribuidor.utilizaGarantiaPdv', value: $('#utilizaGarantiaPdv').is(':checked')},
		{name:'parametrosDistribuidor.chequeCalcao', value: $('#chequeCalcao').is(':checked')},
		{name:'parametrosDistribuidor.chequeCalcaoValor', value: $('#chequeCalcaoValor').val()},
		{name:'parametrosDistribuidor.fiador', value: $('#fiador').is(':checked')},
		{name:'parametrosDistribuidor.fiadorValor', value: $('#fiadorValor').val()},
		{name:'parametrosDistribuidor.imovel', value: $('#imovel').is(':checked')},
		{name:'parametrosDistribuidor.imovelValor', value: $('#imovelValor').val()},
		{name:'parametrosDistribuidor.caucaoLiquida', value: $('#caucaoLiquida').is(':checked')},
		{name:'parametrosDistribuidor.caucaoLiquidaValor', value: $('#caucaoLiquidaValor').val()},
		{name:'parametrosDistribuidor.notaPromissoria', value: $('#notaPromissoria').is(':checked')},
		{name:'parametrosDistribuidor.notaPromissoriaValor', value: $('#notaPromissoriaValor').val()},
		{name:'parametrosDistribuidor.antecedenciaValidade', value: $('#antecedenciaValidade').is(':checked')},
		{name:'parametrosDistribuidor.antecedenciaValidadeValor', value: $('#antecedenciaValidadeValor').val()},
		{name:'parametrosDistribuidor.indicadorReajusteCaucaoLiquida', value: $('#indicadorReajusteCaucaoLiquida').is(':checked')},
		{name:'parametrosDistribuidor.indicadorReajusteCaucaoLiquidaValor', value: $('#indicadorReajusteCaucaoLiquidaValor').val()},
		{name:'parametrosDistribuidor.sugereSuspensaoQuandoAtingirBoletos', value: $('#sugereSuspensaoQuandoAtingirBoletos').val()},
		{name:'parametrosDistribuidor.sugereSuspensaoQuandoAtingirReais', value: $('#sugereSuspensaoQuandoAtingirReais').val()},
		{name:'parametrosDistribuidor.parcelamentoDividas', value: $('#parcelamentoDividas').is(':checked')},
		{name:'parametrosDistribuidor.negociacaoAteParcelas', value: $('#negociacaoAteParcelas').val()},
		{name:'parametrosDistribuidor.permitePagamentoDividasDivergentes', value: $('#permitePagamentoDividasDivergentes').is(':checked')},
		{name:'parametrosDistribuidor.utilizaControleAprovacao', value: $('#utilizaControleAprovacao').is(':checked')},
		{name:'parametrosDistribuidor.paraDebitosCreditos', value: $('#paraDebitosCreditos').is(':checked')},
		{name:'parametrosDistribuidor.negociacao', value: $('#negociacao').is(':checked')},
		{name:'parametrosDistribuidor.ajusteEstoque', value: $('#ajusteEstoque').is(':checked')},
		{name:'parametrosDistribuidor.postergacaoCobranca', value: $('#postergacaoCobranca').is(':checked')},
		{name:'parametrosDistribuidor.devolucaoFornecedor', value: $('#devolucaoFornecedor').is(':checked')},
		{name:'parametrosDistribuidor.recibo', value: $('#recibo').is(':checked')},
		{name:'parametrosDistribuidor.faltasSobras', value: $('#faltasSobras').is(':checked')},
		{name:'parametrosDistribuidor.aprovacaoFaltaDe', value: $('#aprovacaoFaltaDe').is(':checked')},
		{name:'parametrosDistribuidor.aprovacaoSobraDe', value: $('#aprovacaoSobraDe').is(':checked')},
		{name:'parametrosDistribuidor.aprovacaoFaltaEm', value: $('#aprovacaoFaltaEm').is(':checked')},
		{name:'parametrosDistribuidor.aprovacaoSobraEm', value: $('#aprovacaoSobraEm').is(':checked')},
		{name:'parametrosDistribuidor.prazoFollowUp', value: $('#prazoFollowUp').val()},
		{name:'parametrosDistribuidor.prazoFollowUpFaltaDe', value: $('#prazoFollowUpFaltaDe').is(':checked')},
		{name:'parametrosDistribuidor.prazoFollowUpSobraDe', value: $('#prazoFollowUpSobraDe').is(':checked')},
		{name:'parametrosDistribuidor.prazoFollowUpFaltaEm', value: $('#prazoFollowUpFaltaEm').is(':checked')},
		{name:'parametrosDistribuidor.prazoFollowUpSobraEm', value: $('#prazoFollowUpSobraEm').is(':checked')},
		{name:'parametrosDistribuidor.prazoAvisoPrevioValidadeGarantia', value: $('#prazoAvisoPrevioValidadeGarantia').val()},
		{name:'parametrosDistribuidor.prazoAvisoPrevioValidadeGarantiaFaltaDe', value: $('#prazoAvisoPrevioValidadeGarantiaFaltaDe').is(':checked')},
		{name:'parametrosDistribuidor.prazoAvisoPrevioValidadeGarantiaSobraDe', value: $('#prazoAvisoPrevioValidadeGarantiaSobraDe').is(':checked')},
		{name:'parametrosDistribuidor.prazoAvisoPrevioValidadeGarantiaFaltaEm', value: $('#prazoAvisoPrevioValidadeGarantiaFaltaEm').is(':checked')},
		{name:'parametrosDistribuidor.prazoAvisoPrevioValidadeGarantiaSobraEm', value: $('#prazoAvisoPrevioValidadeGarantiaSobraEm').is(':checked')}
	];
	
	$.postJSON("<c:url value='/administracao/parametrosDistribuidor/gravar'/>",
			   data,
			   function (resultado) {
				   exibirMensagem(resultado.tipoMensagem, 
				   				  resultado.listaMensagens);
					recarregarDiasDistribuidorFornecedorGrid();
			   });
}

function popup_confirm() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-confirm" ).dialog({
			resizable: false,
			height:160,
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {
					gravar();
					$( this ).dialog( "close" );
					//$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
};
	
function popup_pesq_fornecedor() {
	
		$( "#dialog-pesq-fornecedor" ).dialog({
			resizable: false,
			height:300,
			width:490,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$( ".forncedoresSel" ).css('display','table-cell');
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
};
	
  //callback function to bring a hidden box back
function callback() {
	setTimeout(function() {
		$( "#effect:visible").removeAttr( "style" ).fadeOut();

	}, 1000 );
};	

function mostra_funcionalidades(){
	if ($('#faltasSobras').attr('checked') == 'checked') {
		$('.funcionalidades').show();
	} else {
		$('.funcionalidades').hide();
	}
}

function removeFornecedor(){
	$( ".forncedoresSel" ).fadeOut('fast');
}

function habilitaPrazoContrato() {
	if ($('#utilizaContratoComCotas').attr('checked') == "checked") {
		$('#prazoContrato').attr("disabled", false);
	} else {
		$('#prazoContrato').attr("disabled", true);
	}
}

$(document).ready(function() {
	$('#informacoesComplementaresContrato').wysiwyg();
	$('#informacoesComplementaresContrato').wysiwyg({controls:"font-family,italic,|,undo,redo"});
	$('#informacoesComplementaresContrato').wysiwyg('setContent','${parametrosDistribuidor.informacoesComplementaresContrato}');
	$('#informacoesComplementaresProcuracao').wysiwyg();
	$('#informacoesComplementaresProcuracao').wysiwyg({controls:"font-family,italic,|,undo,redo"});
	$('#informacoesComplementaresProcuracao').wysiwyg('setContent','${parametrosDistribuidor.informacoesComplementaresProcuracao}');
});

$(function() {
	
	$("input[id^='reutilizacaoCodigoCotaInativa']").maskMoney({
		 thousands:'.', 
		 decimal:',', 
		 precision:0
	});

	$("input[id^='capacidadeManuseioHomemHoraLancamento']").maskMoney({
		 thousands:'.', 
		 decimal:',', 
		 precision:0
	});

	$("input[id^='capacidadeManuseioHomemHoraRecolhimento']").maskMoney({
		 thousands:'.', 
		 decimal:',', 
		 precision:0
	});

	$("input[id^='prazoContrato']").maskMoney({
		 thousands:'.', 
		 decimal:',', 
		 precision:0
	});

	$("input[id^='caucaoLiquidaValor']").maskMoney({
		 thousands:'.', 
		 decimal:',', 
		 precision:0
	});
	
	$("input[id^='antecedenciaValidadeValor']").maskMoney({
		 thousands:'.', 
		 decimal:',', 
		 precision:0
	});
	
	$("input[id^='fiadorValor']").maskMoney({
		 thousands:'.', 
		 decimal:',', 
		 precision:0
	});
	
	$("input[id^='imovelValor']").maskMoney({
		 thousands:'.', 
		 decimal:',', 
		 precision:0
	});
	
	$("input[id^='chequeCalcaoValor']").maskMoney({
		 thousands:'.', 
		 decimal:',', 
		 precision:0
	});
	
	$("input[id^='indicadorReajusteCaucaoLiquidaValor']").maskMoney({
		 thousands:'.', 
		 decimal:',', 
		 precision:0
	});
	
	$("input[id^='notaPromissoriaValor']").maskMoney({
		 thousands:'.', 
		 decimal:',', 
		 precision:0
	});
	
	$("input[id^='sugereSuspensaoQuandoAtingirBoletos']").maskMoney({
		 thousands:'.', 
		 decimal:',', 
		 precision:0
	});	
	
	$("input[id^='sugereSuspensaoQuandoAtingirReais']").maskMoney({
		 thousands:'.', 
		 decimal:',', 
		 precision:2
	});

	$("input[id^='negociacaoAteParcelas']").maskMoney({
		 thousands:'.', 
		 decimal:',', 
		 precision:0
	});	
	
	$("input[id^='prazoAvisoPrevioValidadeGarantia']").maskMoney({
		 thousands:'.', 
		 decimal:',', 
		 precision:0
	});	
	
	$("input[id^='prazoFollowUp']").maskMoney({
		 thousands:'.', 
		 decimal:',', 
		 precision:0
	});	
	
	$("#chamadaoValorConsignado").maskMoney({
		 thousands:'.', 
		 decimal:',', 
	});
	
	$("#chamadaoDiasSuspensao").maskMoney({
		 thousands:'', 
		 decimal:'', 
		 precision:0
	});
	
	$("#relancamentoParciaisEmDias").val(${parametrosDistribuidor.relancamentoParciaisEmDias});
	
	$('input:radio[name=distribuidor][value=${parametrosDistribuidor.distribuidor}]').click();
	$('input:radio[name=impressaoNE][value=${parametrosDistribuidor.impressaoNE}]').click();
	$('input:radio[name=impressaoNECADANFE][value=${parametrosDistribuidor.impressaoNECADANFE}]').click();
	$('input:radio[name=impressaoCE][value=${parametrosDistribuidor.impressaoCE}]').click();
	
	mostra_funcionalidades();
	habilitaPrazoContrato();
	
	$("#tabDistribuidor").tabs();
});

</script>
</head>

<body>

<form name="form" id="form" method="post">

<div id="dialog-pesq-fornecedor" title="Selecione os Fornecedores">
	<fieldset>
		<legend>Selecione um ou mais Fornecedores</legend>
	    <select name="" size="1" multiple="multiple" style="width:440px; height:150px;" >
          <c:forEach items="${fornecedores}" var="fornecedor">
          	<option value="${fornecedor.id}">${fornecedor.juridica.nomeFantasia}</option>
     	  </c:forEach>
	    </select>
	</fieldset>
</div>

<div id="dialog-confirm" title="Salvar Parâmetro do Distribuidor">
	<p>Confirma os Parâmetros do Distribuidor?</p>
</div>
<div class="corpo">
    <div class="container">	
    <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Parâmetros do Distribuidor < evento > com < status >.</b></p>
	</div>
    <fieldset class="classFieldset">
   	    <legend>Parâmetros do Distribuidor</legend>
        <div id="tabDistribuidor">
			<ul>
				<li><a href="#tabFiscal">Cadastro / Fiscal</a></li>
				<li><a href="#tabOperacao">Operação</a></li>
				<li><a href="#tabEmissao">Documentos</a></li>
				<li><a href="#tabContratos">Contratos e Garantias</a></li>
			       <li><a href="#tabNegociacao">Negociação</a></li>
			       <li><a href="#tabAprovacao">Aprovação</a></li>
			</ul>
			
			<jsp:include page="tabFiscal.jsp"></jsp:include>
			
			<div id="tabOperacao">
		      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td valign="top">		
                    <fieldset style="width:440px!important; margin-bottom:5px; float:left;">
		                <legend>Dias de Operação:</legend>
		                <table width="441" border="0" cellpadding="0" cellspacing="1">
		                  <tr class="header_table">
		                    <td>Fornecedor</td>
		                    <td align="center">Lançamento</td>
		                    <td align="center">Recolhimento</td>
		                  </tr>
		                  <tr class="class_linha_1">
		                    <td width="141">
		                    	<select name="selectFornecedoresLancamento[]" size="5" multiple="multiple" id="selectFornecedoresLancamento" style="width:130px; height:100px">
                    		  		<c:forEach items="${fornecedores}" var="fornecedor">
		                      			<option value="${fornecedor.id}">${fornecedor.juridica.nomeFantasia}</option>
		                      		</c:forEach>
		                    	</select>
		                    </td>
		                    <td width="157" align="center">
		                    	<select name="selectDiasLancamento[]" size="5" multiple="multiple" id="selectDiasLancamento" style="width:130px; height:100px">
			                      <option value="1">Domingo</option>
			                      <option value="2">Segunda-feira</option>
			                      <option value="3">Terça-feira</option>
			                      <option value="4">Quarta-feira</option>
			                      <option value="5">Quinta-feira</option>
			                      <option value="6">Sexta-feira</option>
			                      <option value="7">Sábado</option>
		                    	</select>
		                    </td>
		                    <td width="139" align="center">
		                    	<select name="selectDiasRecolhimento[]" size="5" multiple="multiple" id="selectDiasRecolhimento" style="width:130px; height:100px">
			                      <option value="1">Domingo</option>
			                      <option value="2">Segunda-feira</option>
			                      <option value="3">Terça-feira</option>
			                      <option value="4">Quarta-feira</option>
			                      <option value="5">Quinta-feira</option>
			                      <option value="6">Sexta-feira</option>
			                      <option value="7">Sábado</option>
		                    	</select>
		                    </td>
		                  </tr>
		                  <tr>
		                    <td>&nbsp;</td>
		                    <td align="center">&nbsp;</td>
		                    <td width="139" align="center"><span class="bt_add"><a href="javascript:;" onclick="gravarDiasDistribuidorFornecedor()" >Incluir Novo</a></span></td>
		                  </tr>
		                </table>
		                <br />
		                <span id="spanDiasDistribuidorFornecedor">
			                <table width="441" border="0" cellpadding="0" cellspacing="1">
			                  <tr class="header_table">
			                    <td>Fornecedor</td>
			                    <td align="center">Lançamento</td>
			                    <td align="center">Recolhimento</td>
			                    <td align="center">&nbsp;</td>
			                  </tr>
	                		  <c:forEach items="${listaDiaOperacaoFornecedor}" var="registroDiaOperacaoFornecedor">
				                  <tr class="class_linha_1">
				                    <td width="139">${registroDiaOperacaoFornecedor.fornecedor.juridica.nomeFantasia}</td>
				                    <td width="144" align="center">${registroDiaOperacaoFornecedor.diasLancamento}</td>
				                    <td width="125" align="center">${registroDiaOperacaoFornecedor.diasRecolhimento}</td>
				                    <td width="28" align="center"><a href="javascript:;" onclick="excluirDiasDistribuidorFornecedor(${registroDiaOperacaoFornecedor.fornecedor.id})" ><img src="${pageContext.request.contextPath}/images/ico_excluir.gif" width="15" height="15" alt="Excluir" /></a></td>
				                  </tr>
	                   		  </c:forEach>
			                </table>
		                </span>
		              </fieldset> 
                  <fieldset style="width:440px!important; margin-bottom:5px;">
                    <legend>Parciais / Matriz de Lançamento</legend>
                    <label>Relançamento de Parciais em D+: </label>
                    <select name="parametrosDistribuidor.relancamentoParciaisEmDias" size="1" id="relancamentoParciaisEmDias" style="width:50px; height:19px;">
                      <option value="2" selected="selected">2</option>
                      <option value="3">3</option>
                      <option value="4">4</option>
                      <option value="5">5</option>
                    </select>
                  </fieldset>
                   <fieldset style="width:440px!important; margin-bottom:5px;">
                  <legend>Reutilização de Código de Cota</legend>
                  <table width="390" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                      <td width="222" align="left">Reutilização de Código de Cota Inativa:</td>
                      <td width="40" align="center"><input name="parametrosDistribuidor.reutilizacaoCodigoCotaInativa" value="${parametrosDistribuidor.reutilizacaoCodigoCotaInativa}" type="text" id="reutilizacaoCodigoCotaInativa" style="width:40px; text-align:center;" value="06" /></td>
                      <td width="124" align="left"> &nbsp;( meses )</td>
                    </tr>
                  </table>
                </fieldset>
                  </td>
                  <td style="width:10px;">&nbsp;</td>
                  <td valign="top"> 
			          <fieldset style="width:420px!important; margin-bottom:5px;">
			                <legend>Recolhimento</legend>
			            <table width="398" border="0" cellspacing="0" cellpadding="0">
			              <tr>
			                <td width="197">&nbsp;</td>
			                <td colspan="11">&nbsp;</td>
			              </tr>
			             
                         <tr>
                            <td>Na CE, deseja utilizar:</td>
                            <td>
                             <c:if test="${parametrosDistribuidor.tipoContabilizacaoCE eq 'VALOR'}">
								<input type="radio" name="parametrosDistribuidor.tipoContabilizacaoCE" 
									   id="radioTipoContabilizacaoCEValor" 
									   checked="checked"
                                       value="VALOR" />
							</c:if>		
							<c:if test="${empty parametrosDistribuidor.tipoContabilizacaoCE or 
								(not (parametrosDistribuidor.tipoContabilizacaoCE eq 'VALOR'))}">
								<input type="radio" name="parametrosDistribuidor.tipoContabilizacaoCE" 
									   id="radioTipoContabilizacaoCEValor" 
                                       value="VALOR" />
							</c:if>									
                            </td>
                            <td colspan="3">Valor</td>
                            <td>
							  <c:if test="${parametrosDistribuidor.tipoContabilizacaoCE eq 'EXEMPLARES'}">
								<input type="radio" name="parametrosDistribuidor.tipoContabilizacaoCE" 
                                     id="radioTipoContabilizacaoCEExemplares" 
									 checked="checked"	
                                     value="EXEMPLARES" />
							  </c:if>
							  <c:if test="${empty parametrosDistribuidor.tipoContabilizacaoCE or 
									(not (parametrosDistribuidor.tipoContabilizacaoCE eq 'EXEMPLARES'))}">
									<input type="radio" name="parametrosDistribuidor.tipoContabilizacaoCE" 
                                     id="radioTipoContabilizacaoCEExamplares" 
			                         value="EXEMPLARES" />
							  </c:if>
							 </td>
                            <td colspan="6">Exemplares</td>
                          </tr>
                    
                         <tr>
			                <td>Aceita Encalhe Juramentada:</td>
			                <td width="22"><input name="parametrosDistribuidor.aceitaEncalheJuramentada" type="checkbox" id="aceitaEncalheJuramentada" ${parametrosDistribuidor.aceitaEncalheJuramentada} /></td>
			                <td width="15">&nbsp;</td>
			                <td width="21">&nbsp;</td>
			                <td width="16">&nbsp;</td>
			                <td width="20">&nbsp;</td>
			                <td width="16">&nbsp;</td>
			                <td width="21">&nbsp;</td>
			                <td width="13">&nbsp;</td>
			                <td width="20">&nbsp;</td>
			                <td width="15">&nbsp;</td>
			                <td width="22">&nbsp;</td>
			              </tr>
			              <tr>
			                <td>Dias de Recolhimento:</td>
			                <td><input name="parametrosDistribuidor.diaRecolhimentoPrimeiro" type="checkbox" id="diaRecolhimentoPrimeiro" ${parametrosDistribuidor.diaRecolhimentoPrimeiro} /></td>
			                <td>1º</td>
			                <td><input name="parametrosDistribuidor.diaRecolhimentoSegundo" type="checkbox" id="diaRecolhimentoSegundo" ${parametrosDistribuidor.diaRecolhimentoSegundo} /></td>
			                <td>2º</td>
			                <td><input name="parametrosDistribuidor.diaRecolhimentoTerceiro" type="checkbox" id="diaRecolhimentoTerceiro" ${parametrosDistribuidor.diaRecolhimentoTerceiro} /></td>
			                <td>3º</td>
			                <td><input name="parametrosDistribuidor.diaRecolhimentoQuarto" type="checkbox" id="diaRecolhimentoQuarto" ${parametrosDistribuidor.diaRecolhimentoQuarto} /></td>
			                <td>4º</td>
			                <td><input name="parametrosDistribuidor.diaRecolhimentoQuinto" type="checkbox" id="diaRecolhimentoQuinto" ${parametrosDistribuidor.diaRecolhimentoQuinto} /></td>
			                <td>5º</td>
			                <td>Dias</td>
			              </tr>
			              <tr>
			                <td>Aceita devolução antecipada cota:</td>
			                <td><input name="parametrosDistribuidor.limiteCEProximaSemana" type="checkbox" id="limiteCEProximaSemana" ${parametrosDistribuidor.limiteCEProximaSemana} /></td>
			                <td colspan="10">Limite CE Próxima Semana</td>
			              </tr>
                          <tr>
                            <td colspan="11">Em casos de Venda Negativa, solicita a senha de aprovação do Supervisor?</td>
                            <td>
                              <c:if test="${parametrosDistribuidor.supervisionaVendaNegativa}">
                                <input name="parametrosDistribuidor.supervisionaVendaNegativa" id="supervisionaVendaNegativa" type="checkbox" checked="checked" />
                              </c:if>
                              <c:if test="${empty parametrosDistribuidor.supervisionaVendaNegativa or (not parametrosDistribuidor.supervisionaVendaNegativa)}">
                                <input name="parametrosDistribuidor.supervisionaVendaNegativa" id="supervisionaVendaNegativa" type="checkbox"/>
                              </c:if>
                            </td>
                          </tr>
                    <tr>
			              <tr>
			                <td>&nbsp;</td>
			                <td>&nbsp;</td>
			                <td>&nbsp;</td>
			                <td>&nbsp;</td>
			                <td>&nbsp;</td>
			                <td>&nbsp;</td>
			                <td>&nbsp;</td>
			                <td>&nbsp;</td>
			                <td>&nbsp;</td>
			                <td>&nbsp;</td>
			                <td>&nbsp;</td>
			                <td>&nbsp;</td>
			              </tr>
			            </table>
			            <table width="390" border="0" cellspacing="1" cellpadding="0">
			              <tr class="header_table">
			                <td align="center">&nbsp;</td>
			                <td align="center">Recebimento</td>
			                <td align="center">Encalhe</td>
			              </tr>
			              <tr>
			                <td width="123" align="center" class="class_linha_1">Conferência Cega</td>
			                <td width="115" align="center"><input name="parametrosDistribuidor.conferenciaCegaRecebimento" type="checkbox" id="conferenciaCegaRecebimento" ${parametrosDistribuidor.conferenciaCegaRecebimento} /></td>
			                <td width="148" align="center"><input name="parametrosDistribuidor.conferenciaCegaEncalhe" type="checkbox" id="conferenciaCegaEncalhe" ${parametrosDistribuidor.conferenciaCegaEncalhe} /></td>
			              </tr>
			            </table>
			          </fieldset>
		      		  <fieldset style="width:420px!important; margin-bottom:5px;">
                          <legend>Chamadão</legend>
                          <table width="387" border="0" cellspacing="1" cellpadding="0">
                            <tr>
                              <td width="221" align="right">Avisar quando a Cota permanecer por &nbsp;</td>
                              <td width="70"><input name="parametrosDistribuidor.chamadaoDiasSuspensao" 
                                type="text" id="chamadaoDiasSuspensao" style="width:20px; text-align:center;" value="${parametrosDistribuidor.chamadaoDiasSuspensao}" /></td>
                              <td width="92" align="left">dias suspensos</td>
                            </tr>
                            <tr>
                              <td align="right"> Ou atingir R$&nbsp; </td>
                              <td><input name="parametrosDistribuidor.chamadaoValorConsignado" 
                                type="text" id="chamadaoValorConsignado" style="width:50px; text-align:right;" value="${parametrosDistribuidor.chamadaoValorConsignado}" /></td>
                              <td align="left">de consignado</td>
                            </tr>
                         </table>
                      </fieldset>
                      <fieldset style="width:420px!important; margin-bottom:5px;">
		                <legend>Capacidade de Manuseio </legend>
		                <table width="390" border="0" cellspacing="1" cellpadding="0">
		                  <tr class="header_table">
		                    <td align="center">&nbsp;</td>
		                    <td align="center">Lançamento</td>
		                    <td align="center">Recolhimento</td>
		                  </tr>
		                  <tr>
		                    <td width="123" align="center" class="class_linha_1">Exes. Homem/ Hora</td>
		                    <td width="115" align="center"><input type="text" name="parametrosDistribuidor.capacidadeManuseioHomemHoraLancamento" id="capacidadeManuseioHomemHoraLancamento" value="${parametrosDistribuidor.capacidadeManuseioHomemHoraLancamento}" style="width:40px; text-align:center;" /></td>
		                    <td width="148" align="center"><input type="text" name="parametrosDistribuidor.capacidadeManuseioHomemHoraRecolhimento" id="capacidadeManuseioHomemHoraRecolhimento" value="${parametrosDistribuidor.capacidadeManuseioHomemHoraRecolhimento}" style="width:40px; text-align:center;" /></td>
		                  </tr>
		                </table>
		              </fieldset>     
			         
		              
                  </td>
                  </tr>
                 </table> 
       
			</div>
			
			<jsp:include page="tabEmissao.jsp"></jsp:include>
			
			<div id="tabContratos">
				<fieldset style="width:420px!important; margin-bottom:5px;">
		               	<legend>Condições de Contratação:</legend>
		                <table width="392" border="0" cellspacing="1" cellpadding="1">
		                  <tr>
		                    <td>Utiliza Contrato com as Cotas?</td>
		                    <td><input name="parametrosDistribuidor.utilizaContratoComCotas" onclick="habilitaPrazoContrato()" id="utilizaContratoComCotas" type="checkbox"  ${parametrosDistribuidor.utilizaContratoComCotas}/></td>
		                  </tr>
		                  <tr>
		                    <td width="190">Prazo do Contrato (em meses ):</td>
		                    <td width="195"><input type="text" name="parametrosDistribuidor.prazoContrato" id="prazoContrato"  style="width:80px;" disabled="disabled" value="${parametrosDistribuidor.prazoContrato}" /></td>
		                  </tr>
		                  <tr>
		                    <td colspan="2">&nbsp;</td>
		                  </tr>
		                  <tr>
		                    <td colspan="2">Informações complementares do Contrato:</td>
		                  </tr>
		                  <tr>
		                    <td colspan="2"><textarea name="parametrosDistribuidor.informacoesComplementaresContrato" rows="4" id="informacoesComplementaresContrato" style="width:350px;" ></textarea></td>
		                  </tr>
		                </table>
                </fieldset>
                <fieldset style="width:420px!important; margin-bottom:5px;">
		                <legend>Procuração</legend>
		                <table width="393" border="0" cellspacing="1" cellpadding="1">
		                  <tr>
		                    <td><input name="parametrosDistribuidor.utilizaProcuracaoEntregadores" id="utilizaProcuracaoEntregadores" type="checkbox"  ${parametrosDistribuidor.utilizaProcuracaoEntregadores}/></td>
		                    <td width="386" align="left">Utiliza procuração para  Entregadores?</td>
		                  </tr>
		                  <tr>
		                    <td colspan="2">&nbsp;</td>
		                  </tr>
		                  <tr>
		                    <td colspan="2">Informações complementares da Procuração:</td>
		                  </tr>
		                  <tr>
		                    <td colspan="2"><textarea name="parametrosDistribuidor.informacoesComplementaresProcuracao" rows="4" id="informacoesComplementaresProcuracao" style="width:150px;" ></textarea></td>
		                  </tr>
		                </table>
                </fieldset>
                <br clear="all" />
	            <fieldset style="width:870px!important; margin-bottom:5px;">
		               	<legend>Garantia</legend>
		                    <table width="300" border="0" cellspacing="0" cellpadding="0">
		                      <tr>
		                        <td width="170">Utiliza Garantia para PDVs?</td>
		                        <td width="25"><input name="parametrosDistribuidor.utilizaGarantiaPdv" id="utilizaGarantiaPdv" type="checkbox"  ${parametrosDistribuidor.utilizaGarantiaPdv}/></td>
		                        <td width="35">&nbsp;</td>
		                        <td width="20">&nbsp;</td>
		                        <td width="50">&nbsp;</td>
		                      </tr>
		                    </table>
		               <table width="780" border="0" cellspacing="1" cellpadding="1">
		                 <tr>
		                   <td width="20"><input name="parametrosDistribuidor.chequeCalcao" id="chequeCalcao" type="checkbox"  ${parametrosDistribuidor.chequeCalcao}/></td>
		                   <td width="201"> Cheque Caução</td>
		                   <td width="104"><input name="parametrosDistribuidor.chequeCalcaoValor" type="text" style="float:left; width:60px;" value="${parametrosDistribuidor.chequeCalcaoValor}" id="chequeCalcaoValor" /></td>
		                        <td width="20"><input name="parametrosDistribuidor.fiador" id="fiador" type="checkbox" ${parametrosDistribuidor.fiador}/></td>
		                        <td width="105">Fiador</td>
		                        <td width="78"><input name="parametrosDistribuidor.fiadorValor" type="text" style="float:left; width:60px;" id="fiadorValor" value="${parametrosDistribuidor.fiadorValor}" /></td>
		                        <td width="20"><input name="parametrosDistribuidor.imovel" id="imovel" type="checkbox" ${parametrosDistribuidor.imovel}/></td>
		                        <td width="144">Imóvel</td>
		                        <td width="60"><input name="parametrosDistribuidor.imovelValor" type="text" style="float:left; width:60px;" id="imovelValor" value="${parametrosDistribuidor.imovelValor}" /></td>
		                 </tr>
		                 <tr>
		                   <td><input name="parametrosDistribuidor.caucaoLiquida" id="caucaoLiquida" type="checkbox" ${parametrosDistribuidor.caucaoLiquida}/></td>
		                   <td>Caução Líquida</td>
		                   <td><input name="parametrosDistribuidor.caucaoLiquidaValor" type="text" style="float:left; width:60px;" id="caucaoLiquidaValor" value="${parametrosDistribuidor.caucaoLiquidaValor}" /></td>
		                        <td><input name="parametrosDistribuidor.notaPromissoria" id="notaPromissoria" type="checkbox" ${parametrosDistribuidor.notaPromissoria}/></td>
		                        <td>Nota Promissória</td>
		                        <td><input name="parametrosDistribuidor.notaPromissoriaValor" type="text" style="float:left; width:60px;" id="notaPromissoriaValor" value="${parametrosDistribuidor.notaPromissoriaValor}" /></td>
		                        <td><input name="parametrosDistribuidor.antecedenciaValidade" id="antecedenciaValidade" type="checkbox" ${parametrosDistribuidor.antecedenciaValidade}/></td>
		                        <td>Antecedência da Validade</td>
		                        <td><input name="parametrosDistribuidor.antecedenciaValidadeValor" type="text" style="float:left; width:60px;" value="${parametrosDistribuidor.antecedenciaValidadeValor}" id="antecedenciaValidadeValor" /></td>
		                 </tr>
		                 <tr>
		                   <td><input name="parametrosDistribuidor.indicadorReajusteCaucaoLiquida" id="indicadorReajusteCaucaoLiquida" type="checkbox" ${parametrosDistribuidor.indicadorReajusteCaucaoLiquida}/></td>
		                   <td>Indicador reajuste de caução líquida</td>
		                   <td><input name="parametrosDistribuidor.indicadorReajusteCaucaoLiquidaValor" id="indicadorReajusteCaucaoLiquidaValor" type="text" style="width:60px;" value="${parametrosDistribuidor.indicadorReajusteCaucaoLiquidaValor}" /></td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                 </tr>
		            </table>
		
	            </fieldset> 
			</div>
			
			<jsp:include page="tabNegociacao.jsp"></jsp:include>
		   
		    <div id="tabAprovacao">
				<fieldset style="width:470px!important; margin-bottom:5px;">
		               	<legend>Aprovação</legend>
		                    <table width="450" border="0" cellspacing="0" cellpadding="0">
		                      <tr>
		                        <td width="23"><input name="parametrosDistribuidor.utilizaControleAprovacao" id="utilizaControleAprovacao" type="checkbox" ${parametrosDistribuidor.utilizaControleAprovacao} /></td>
		                        <td width="190">Utiliza Controle de Aprovação?</td>
		                        <td width="20">&nbsp;</td>
		                        <td width="178">&nbsp;</td>
		                      </tr>
		                      <tr>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                      </tr>
		                    </table>
		                    <table width="451" border="0" cellspacing="0" cellpadding="0">
		                      <tr>
		                        <td colspan="6">Para as Funcionalidades:</td>
		                      </tr>
		                      <tr>
		                        <td width="20"><input name="parametrosDistribuidor.paraDebitosCreditos" id="paraDebitosCreditos" type="checkbox" ${parametrosDistribuidor.paraDebitosCreditos} /></td>
		                        <td width="143">Débitos e Créditos</td>
		                        <td width="20"><input name="parametrosDistribuidor.negociacao" id="negociacao" type="checkbox" ${parametrosDistribuidor.negociacao} /></td>
		                        <td width="133">Negociação</td>
		                        <td width="20"><input name="parametrosDistribuidor.ajusteEstoque" id="ajusteEstoque" type="checkbox" ${parametrosDistribuidor.ajusteEstoque} /></td>
		                        <td width="115">Ajuste de Estoque</td>
		                      </tr>
		                      <tr>
		                        <td><input name="parametrosDistribuidor.postergacaoCobranca" type="checkbox" id="postergacaoCobranca" ${parametrosDistribuidor.postergacaoCobranca} /></td>
		                        <td>Postergação de Cobrança</td>
		                        <td><input name="parametrosDistribuidor.devolucaoFornecedor" type="checkbox" id="devolucaoFornecedor" ${parametrosDistribuidor.devolucaoFornecedor} /></td>
		                        <td>Devolução Fornecedor</td>
		                        <td><input name="parametrosDistribuidor.recibo" type="checkbox" id="recibo" ${parametrosDistribuidor.recibo} /></td>
		                        <td>Recibo</td>
		                      </tr>
		                      <tr>
		                        <td><input name="parametrosDistribuidor.faltasSobras" type="checkbox" id="faltasSobras" ${parametrosDistribuidor.faltasSobras} onchange="mostra_funcionalidades();" /></td>
		                        <td>Faltas e Sobras</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                      </tr>
		                    </table>
		                    <table width="274" border="0" cellspacing="0" cellpadding="0" class="funcionalidades" style="display:none;">
		                      <tr>
		                        <td colspan="4">&nbsp;</td>
		                      </tr>
		                      <tr>
		                        <td colspan="4">Para as Funcionalidades:</td>
		                      </tr>
		                      <tr>
		                        <td width="23"><input name="parametrosDistribuidor.aprovacaoFaltaDe" type="checkbox" id="aprovacaoFaltaDe" ${parametrosDistribuidor.aprovacaoFaltaDe} /></td>
		                        <td width="123">Falta de</td>
		                        <td width="20"><input name="parametrosDistribuidor.aprovacaoSobraDe" type="checkbox" id="aprovacaoSobraDe" ${parametrosDistribuidor.aprovacaoSobraDe} /></td>
		                        <td width="108">Sobra de</td>
		                      </tr>
		                      <tr>
		                        <td><input name="parametrosDistribuidor.aprovacaoFaltaEm" type="checkbox" id="aprovacaoFaltaEm" ${parametrosDistribuidor.aprovacaoFaltaEm} /></td>
		                        <td>Falta em</td>
		                        <td><input name="parametrosDistribuidor.aprovacaoSobraEm" type="checkbox" id="aprovacaoSobraEm" ${parametrosDistribuidor.aprovacaoSobraEm} /></td>
		                        <td>Sobra em</td>
		                      </tr>
		                      <tr>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                      </tr>
		                    </table>
                </fieldset>
	            <fieldset style="width:300px!important; margin-bottom:5px;">
		              <legend>Prazo de Follow up</legend>
		           	  <table width="280" border="0" cellspacing="0" cellpadding="0">
		                <tr>
		                  <td width="240">Aviso prévio para vencímento de contratos (dias)</td>
		                  <td width="40"><input name="parametrosDistribuidor.prazoFollowUp" type="text" id="prazoFollowUp" style="float:left; width:40px;" value="${parametrosDistribuidor.prazoFollowUp}" /></td>
		                </tr>
		              </table>
		              <table width="274" border="0" cellspacing="0" cellpadding="0" class="funcionalidades" style="display:none;">
		                	  <tr>
		                        <td colspan="4">&nbsp;</td>
		                      </tr>
		                      <tr>
		                        <td colspan="4">Para as Funcionalidades:</td>
		                      </tr>
		                      <tr>
		                        <td width="23"><input name="parametrosDistribuidor.prazoFollowUpFaltaDe" type="checkbox" id="prazoFollowUpFaltaDe" ${parametrosDistribuidor.prazoFollowUpFaltaDe} /></td>
		                        <td width="123">Falta de</td>
		                        <td width="20"><input name="parametrosDistribuidor.prazoFollowUpSobraDe" type="checkbox" id="prazoFollowUpSobraDe" ${parametrosDistribuidor.prazoFollowUpSobraDe} /></td>
		                        <td width="108">Sobra de</td>
		                      </tr>
		                      <tr>
		                        <td><input name="parametrosDistribuidor.prazoFollowUpFaltaEm" type="checkbox" id="prazoFollowUpFaltaEm" ${parametrosDistribuidor.prazoFollowUpFaltaEm} /></td>
		                        <td>Falta em</td>
		                        <td><input name="parametrosDistribuidor.prazoFollowUpSobraEm" type="checkbox" id="prazoFollowUpSobraEm" ${parametrosDistribuidor.prazoFollowUpSobraEm} /></td>
		                        <td>Sobra em</td>
		                      </tr>
		                      <tr>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                      </tr>
	                    </table>
                </fieldset>
                <fieldset style="width:300px!important; margin-bottom:5px;">
	               	<legend>Aviso Prévio para Validade de Garantia</legend>
		           	  <table width="280" border="0" cellspacing="0" cellpadding="0">
		                <tr>
		                  <td width="240">Aviso prévio para vencimento de garantias (dias).</td>
		                  <td width="40"><input name="parametrosDistribuidor.prazoAvisoPrevioValidadeGarantia" type="text" id="prazoAvisoPrevioValidadeGarantia" value="${parametrosDistribuidor.prazoAvisoPrevioValidadeGarantia}" style="float:left; width:40px;" value="15" /></td>
		                </tr>
		              </table>
		              <table width="274" border="0" cellspacing="0" cellpadding="0" class="funcionalidades" style="display:none;">
		                  <tr>
	                        <td colspan="4">&nbsp;</td>
	                      </tr>
	                      <tr>
	                        <td colspan="4">Para as Funcionalidades:</td>
	                      </tr>
	                      <tr>
	                        <td width="23"><input name="parametrosDistribuidor.prazoAvisoPrevioValidadeGarantiaFaltaDe" type="checkbox" id="prazoAvisoPrevioValidadeGarantiaFaltaDe" ${parametrosDistribuidor.prazoAvisoPrevioValidadeGarantiaFaltaDe} /></td>
	                        <td width="123">Falta de</td>
	                        <td width="20"><input name="parametrosDistribuidor.prazoAvisoPrevioValidadeGarantiaSobraDe" type="checkbox" id="prazoAvisoPrevioValidadeGarantiaSobraDe" ${parametrosDistribuidor.prazoAvisoPrevioValidadeGarantiaSobraDe} /></td>
	                        <td width="108">Sobra de</td>
	                      </tr>
	                      <tr>
	                        <td><input name="parametrosDistribuidor.prazoAvisoPrevioValidadeGarantiaFaltaEm" type="checkbox" id="prazoAvisoPrevioValidadeGarantiaFaltaEm" ${parametrosDistribuidor.prazoAvisoPrevioValidadeGarantiaFaltaEm} /></td>
	                        <td>Falta em</td>
	                        <td><input name="parametrosDistribuidor.prazoAvisoPrevioValidadeGarantiaSobraEm" type="checkbox" id="prazoAvisoPrevioValidadeGarantiaSobraEm" ${parametrosDistribuidor.prazoAvisoPrevioValidadeGarantiaSobraEm} /></td>
	                        <td>Sobra em</td>
	                      </tr>
	                      <tr>
	                        <td>&nbsp;</td>
	                        <td>&nbsp;</td>
	                        <td>&nbsp;</td>
	                        <td>&nbsp;</td>
	                      </tr>
	                  </table>
                </fieldset>
			</div>
	   		<br clear="all" />
		</div>
	</fieldset>
        <span class="bt_novos" title="Novo"><a href="javascript:;" onclick="popup_confirm();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>Salvar</a></span>
      <div class="linha_separa_fields">&nbsp;</div>
    </div>
</div> 

</form>

</body>
</html>
