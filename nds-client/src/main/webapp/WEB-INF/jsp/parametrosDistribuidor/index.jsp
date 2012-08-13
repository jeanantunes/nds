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
		{name:'parametrosDistribuidor.tipoContabilizacaoCE', value: $('input[name=parametrosDistribuidor.radioTipoContabilizacaoCE]:checked').val()},
		{name:'parametrosDistribuidor.supervisionaVendaNegativa', value: $('#supervisionaVendaNegativa').is(':checked')},
		{name:'parametrosDistribuidor.chamadaoDiasSuspensao', value: $('#chamadaoDiasSuspensao').val()},
		{name:'parametrosDistribuidor.chamadaoValorConsignado', value: $('#chamadaoValorConsignado').val()},
		{name:'parametrosDistribuidor.aceitaEncalheJuramentada', value: $('#aceitaEncalheJuramentada').attr('checked')},
		{name:'parametrosDistribuidor.diaRecolhimentoPrimeiro', value: $('#diaRecolhimentoPrimeiro').attr('checked')},
		{name:'parametrosDistribuidor.diaRecolhimentoSegundo', value: $('#diaRecolhimentoSegundo').attr('checked')},
		{name:'parametrosDistribuidor.diaRecolhimentoTerceiro', value: $('#diaRecolhimentoTerceiro').attr('checked')},
		{name:'parametrosDistribuidor.diaRecolhimentoQuarto', value: $('#diaRecolhimentoQuarto').attr('checked')},
		{name:'parametrosDistribuidor.diaRecolhimentoQuinto', value: $('#diaRecolhimentoQuinto').attr('checked')},
		{name:'parametrosDistribuidor.limiteCEProximaSemana', value: $('#limiteCEProximaSemana').attr('checked')},
		{name:'parametrosDistribuidor.conferenciaCegaRecebimento', value: $('#conferenciaCegaRecebimento').attr('checked')},
		{name:'parametrosDistribuidor.conferenciaCegaEncalhe', value: $('#conferenciaCegaEncalhe').attr('checked')},
		{name:'parametrosDistribuidor.capacidadeManuseioHomemHoraLancamento', value: $('#capacidadeManuseioHomemHoraLancamento').val()},
		{name:'parametrosDistribuidor.capacidadeManuseioHomemHoraRecolhimento', value: $('#capacidadeManuseioHomemHoraRecolhimento').val()},
		{name:'parametrosDistribuidor.reutilizacaoCodigoCotaInativa', value: $('#reutilizacaoCodigoCotaInativa').val()},
		{name:'parametrosDistribuidor.obrigacaoFiscao', value: $('#obrigacaoFiscao').attr('checked')},
		{name:'parametrosDistribuidor.regimeEspecial', value: $('#regimeEspecial').attr('checked')},
		{name:'parametrosDistribuidor.distribuidor', value: $("input[name='distribuidor']:checked").val()},
		{name:'parametrosDistribuidor.slipImpressao', value: $('#slipImpressao').attr('checked')},
		{name:'parametrosDistribuidor.slipEmail', value: $('#slipEmail').attr('checked')},
		{name:'parametrosDistribuidor.boletoImpressao', value: $('#boletoImpressao').attr('checked')},
		{name:'parametrosDistribuidor.boletoEmail', value: $('#boletoEmail').attr('checked')},
		{name:'parametrosDistribuidor.boletoSlipImpressao', value: $('#boletoSlipImpressao').attr('checked')},
		{name:'parametrosDistribuidor.boletoSlipEmail', value: $('#boletoSlipEmail').attr('checked')},
		{name:'parametrosDistribuidor.reciboImpressao', value: $('#reciboImpressao').attr('checked')},
		{name:'parametrosDistribuidor.reciboEmail', value: $('#reciboEmail').attr('checked')},
		{name:'parametrosDistribuidor.notaEnvioImpressao', value: $('#notaEnvioImpressao').attr('checked')},
		{name:'parametrosDistribuidor.notaEnvioEmail', value: $('#notaEnvioEmail').attr('checked')},
		{name:'parametrosDistribuidor.chamadaEncalheImpressao', value: $('#chamadaEncalheImpressao').attr('checked')},
		{name:'parametrosDistribuidor.chamadaEncalheEmail', value: $('#chamadaEncalheEmail').attr('checked')},
		{name:'parametrosDistribuidor.impressaoNE', value: $("input[name='impressaoNE']:checked").val()},
		{name:'parametrosDistribuidor.impressaoNEFaltaDe', value: $('#impressaoNEFaltaDe').attr('checked')},
		{name:'parametrosDistribuidor.impressaoNEFaltaEm', value: $('#impressaoNEFaltaEm').attr('checked')},
		{name:'parametrosDistribuidor.impressaoNESobraDe', value: $('#impressaoNESobraDe').attr('checked')},
		{name:'parametrosDistribuidor.impressaoNESobraEm', value: $('#impressaoNESobraEm').attr('checked')},
		{name:'parametrosDistribuidor.impressaoNECADANFE', value: $("input[name='impressaoNECADANFE']:checked").val()},
		{name:'parametrosDistribuidor.impressaoNECADANFEFaltaDe', value: $('#impressaoNECADANFEFaltaDe').attr('checked')},
		{name:'parametrosDistribuidor.impressaoNECADANFEFaltaEm', value: $('#impressaoNECADANFEFaltaEm').attr('checked')},
		{name:'parametrosDistribuidor.impressaoNECADANFESobraDe', value: $('#impressaoNECADANFESobraDe').attr('checked')},
		{name:'parametrosDistribuidor.impressaoNECADANFESobraEm', value: $('#impressaoNECADANFESobraEm').attr('checked')},
		{name:'parametrosDistribuidor.impressaoCE', value: $("input[name='impressaoCE']:checked").val()},
		{name:'parametrosDistribuidor.impressaoCEFaltaDe', value: $('#impressaoCEFaltaDe').attr('checked')},
		{name:'parametrosDistribuidor.impressaoCEFaltaEm', value: $('#impressaoCEFaltaEm').attr('checked')},
		{name:'parametrosDistribuidor.impressaoCESobraDe', value: $('#impressaoCESobraDe').attr('checked')},
		{name:'parametrosDistribuidor.impressaoCESobraEm', value: $('#impressaoCESobraEm').attr('checked')},
		{name:'parametrosDistribuidor.utilizaContratoComCotas', value: $('#utilizaContratoComCotas').attr('checked')},
		{name:'parametrosDistribuidor.prazoContrato', value: $('#prazoContrato').val()},
		{name:'parametrosDistribuidor.informacoesComplementaresContrato', value: $('#informacoesComplementaresContrato').val()},
		{name:'parametrosDistribuidor.utilizaProcuracaoEntregadores', value: $('#utilizaProcuracaoEntregadores').attr('checked')},
		{name:'parametrosDistribuidor.informacoesComplementaresProcuracao', value: $('#informacoesComplementaresProcuracao').val()},
		{name:'parametrosDistribuidor.utilizaGarantiaPdv', value: $('#utilizaGarantiaPdv').attr('checked')},
		{name:'parametrosDistribuidor.chequeCalcao', value: $('#chequeCalcao').attr('checked')},
		{name:'parametrosDistribuidor.chequeCalcaoValor', value: $('#chequeCalcaoValor').val()},
		{name:'parametrosDistribuidor.fiador', value: $('#fiador').attr('checked')},
		{name:'parametrosDistribuidor.fiadorValor', value: $('#fiadorValor').val()},
		{name:'parametrosDistribuidor.imovel', value: $('#imovel').attr('checked')},
		{name:'parametrosDistribuidor.imovelValor', value: $('#imovelValor').val()},
		{name:'parametrosDistribuidor.caucaoLiquida', value: $('#caucaoLiquida').attr('checked')},
		{name:'parametrosDistribuidor.caucaoLiquidaValor', value: $('#caucaoLiquidaValor').val()},
		{name:'parametrosDistribuidor.notaPromissoria', value: $('#notaPromissoria').attr('checked')},
		{name:'parametrosDistribuidor.notaPromissoriaValor', value: $('#notaPromissoriaValor').val()},
		{name:'parametrosDistribuidor.antecedenciaValidade', value: $('#antecedenciaValidade').attr('checked')},
		{name:'parametrosDistribuidor.antecedenciaValidadeValor', value: $('#antecedenciaValidadeValor').val()},
		{name:'parametrosDistribuidor.indicadorReajusteCaucaoLiquida', value: $('#indicadorReajusteCaucaoLiquida').attr('checked')},
		{name:'parametrosDistribuidor.indicadorReajusteCaucaoLiquidaValor', value: $('#indicadorReajusteCaucaoLiquidaValor').val()},
		{name:'parametrosDistribuidor.sugereSuspensaoQuandoAtingirBoletos', value: $('#sugereSuspensaoQuandoAtingirBoletos').val()},
		{name:'parametrosDistribuidor.sugereSuspensaoQuandoAtingirReais', value: $('#sugereSuspensaoQuandoAtingirReais').val()},
		{name:'parametrosDistribuidor.parcelamentoDividas', value: $('#parcelamentoDividas').attr('checked')},
		{name:'parametrosDistribuidor.negociacaoAteParcelas', value: $('#negociacaoAteParcelas').val()},
		{name:'parametrosDistribuidor.permitePagamentoDividasDivergentes', value: $('#permitePagamentoDividasDivergentes').attr('checked')},
		{name:'parametrosDistribuidor.utilizaControleAprovacao', value: $('#utilizaControleAprovacao').attr('checked')},
		{name:'parametrosDistribuidor.paraDebitosCreditos', value: $('#paraDebitosCreditos').attr('checked')},
		{name:'parametrosDistribuidor.negociacao', value: $('#negociacao').attr('checked')},
		{name:'parametrosDistribuidor.ajusteEstoque', value: $('#ajusteEstoque').attr('checked')},
		{name:'parametrosDistribuidor.postergacaoCobranca', value: $('#postergacaoCobranca').attr('checked')},
		{name:'parametrosDistribuidor.devolucaoFornecedor', value: $('#devolucaoFornecedor').attr('checked')},
		{name:'parametrosDistribuidor.recibo', value: $('#recibo').attr('checked')},
		{name:'parametrosDistribuidor.faltasSobras', value: $('#faltasSobras').attr('checked')},
		{name:'parametrosDistribuidor.aprovacaoFaltaDe', value: $('#aprovacaoFaltaDe').attr('checked')},
		{name:'parametrosDistribuidor.aprovacaoSobraDe', value: $('#aprovacaoSobraDe').attr('checked')},
		{name:'parametrosDistribuidor.aprovacaoFaltaEm', value: $('#aprovacaoFaltaEm').attr('checked')},
		{name:'parametrosDistribuidor.aprovacaoSobraEm', value: $('#aprovacaoSobraEm').attr('checked')},
		{name:'parametrosDistribuidor.prazoFollowUp', value: $('#prazoFollowUp').val()},
		{name:'parametrosDistribuidor.prazoFollowUpFaltaDe', value: $('#prazoFollowUpFaltaDe').attr('checked')},
		{name:'parametrosDistribuidor.prazoFollowUpSobraDe', value: $('#prazoFollowUpSobraDe').attr('checked')},
		{name:'parametrosDistribuidor.prazoFollowUpFaltaEm', value: $('#prazoFollowUpFaltaEm').attr('checked')},
		{name:'parametrosDistribuidor.prazoFollowUpSobraEm', value: $('#prazoFollowUpSobraEm').attr('checked')},
		{name:'parametrosDistribuidor.prazoAvisoPrevioValidadeGarantia', value: $('#prazoAvisoPrevioValidadeGarantia').val()},
		{name:'parametrosDistribuidor.prazoAvisoPrevioValidadeGarantiaFaltaDe', value: $('#prazoAvisoPrevioValidadeGarantiaFaltaDe').attr('checked')},
		{name:'parametrosDistribuidor.prazoAvisoPrevioValidadeGarantiaSobraDe', value: $('#prazoAvisoPrevioValidadeGarantiaSobraDe').attr('checked')},
		{name:'parametrosDistribuidor.prazoAvisoPrevioValidadeGarantiaFaltaEm', value: $('#prazoAvisoPrevioValidadeGarantiaFaltaEm').attr('checked')},
		{name:'parametrosDistribuidor.prazoAvisoPrevioValidadeGarantiaSobraEm', value: $('#prazoAvisoPrevioValidadeGarantiaSobraEm').attr('checked')}
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
				<li><a href="#tabOperacao">Operação</a></li>
				<li><a href="#tabEmissao">Fiscal / Emissão de Documentos</a></li>
				<li><a href="#tabContratos">Contratos e Garantias</a></li>
			    <li><a href="#tabNegociacao">Negociação</a></li>
			     <li><a href="#tabAprovacao">Aprovação</a></li>
			</ul>
			
            <!--  Aba Operação --> 
            <jsp:include page="tabOperacao.jsp"/>
      
			<div id="tabEmissao">
				<fieldset style="width:450px!important; margin-bottom:5px;">
		              <legend>Fiscal</legend>
		              <table width="426" border="0" cellspacing="0" cellpadding="0">
		                <tr>
		                  <td>&nbsp;</td>
		                  <td>&nbsp;</td>
		                  <td align="right">&nbsp;</td>
		                  <td align="right">&nbsp;</td>
		                  <td colspan="4"><strong>Distribuidor</strong></td>
		                </tr>
		                <tr>
		                  <td width="20"><input name="parametrosDistribuidor.obrigacaoFiscao" id="obrigacaoFiscao" type="checkbox"  ${parametrosDistribuidor.obrigacaoFiscao}/></td>
		                  <td width="97"> Obrigação Fiscal</td>
		                  <td width="20"><input name="parametrosDistribuidor.regimeEspecial" id="regimeEspecial" type="checkbox"  ${parametrosDistribuidor.regimeEspecial}/></td>
		                  <td width="98">Regime Especial</td>
		                  <td width="20"><input type="radio" name="distribuidor" id="radioPrestadorServico" value="PRESTADOR_SERVICO" /> </td>
		                  <td width="101">Prestador Serviço</td>
		                  <td width="20"><input type="radio" name="distribuidor" id="radioMercantil" value="MERCANTIL" /></td>
		                  <td width="50">Mercantil</td>
		                </tr>
		                <tr>
		                  <td>&nbsp;</td>
		                  <td>&nbsp;</td>
		                  <td>&nbsp;</td>
		                  <td>&nbsp;</td>
		                  <td>&nbsp;</td>
		                  <td colspan="3">&nbsp;</td>
		                </tr>
		              </table>
	            </fieldset>
	            <fieldset style="width:450px!important;">
		           	<legend>Emissão de Documentos</legend>
		                <table width="430" border="0" cellspacing="0" cellpadding="0">
		                  <tr>
		                    <td>Utiliza:</td>
		                    <td align="center">Impressão</td>
		                    <td align="center">E-mail</td>
		                  </tr>
		                  <tr>
		                    <td width="203">Slip</td>
		                    <td width="120" align="center"><input name="parametrosDistribuidor.slipImpressao" id="slipImpressao" type="checkbox"  ${parametrosDistribuidor.slipImpressao}/></td>
		                    <td width="107" align="center"><input name="parametrosDistribuidor.slipEmail" id="slipEmail" type="checkbox"  ${parametrosDistribuidor.slipEmail}/></td>
		                  </tr>
		                  <tr>
		                    <td>Boleto</td>
		                    <td align="center"><input name="parametrosDistribuidor.boletoImpressao" id="boletoImpressao" type="checkbox"  ${parametrosDistribuidor.boletoImpressao}/></td>
		                    <td align="center"><input name="parametrosDistribuidor.boletoEmail" id="boletoEmail" type="checkbox"  ${parametrosDistribuidor.boletoEmail}/></td>
		                  </tr>
		                  <tr>
		                    <td>Boleto + Slip</td>
		                    <td align="center"><input name="parametrosDistribuidor.boletoSlipImpressao" id="boletoSlipImpressao" type="checkbox"  ${parametrosDistribuidor.boletoSlipImpressao}/></td>
		                    <td align="center"><input name="parametrosDistribuidor.boletoSlipEmail" id="boletoSlipEmail" type="checkbox"  ${parametrosDistribuidor.boletoSlipEmail}/></td>
		                  </tr>
		                  <tr>
		                    <td>Recibo</td>
		                    <td align="center"><input name="parametrosDistribuidor.reciboImpressao" id="reciboImpressao" type="checkbox"  ${parametrosDistribuidor.reciboImpressao}/></td>
		                    <td align="center"><input name="parametrosDistribuidor.reciboEmail" id="reciboEmail" type="checkbox"  ${parametrosDistribuidor.reciboEmail}/></td>
		                  </tr>
		                  <tr>
		                    <td>Nota de Envio</td>
		                    <td align="center"><input name="parametrosDistribuidor.notaEnvioImpressao" id="notaEnvioImpressao" type="checkbox"  ${parametrosDistribuidor.notaEnvioImpressao}/></td>
		                    <td align="center"><input name="parametrosDistribuidor.notaEnvioEmail" id="notaEnvioEmail" type="checkbox"  ${parametrosDistribuidor.notaEnvioEmail}/></td>
		                  </tr>
		                  <tr>
		                    <td>Chamada de Encalhe</td>
		                    <td align="center"><input name="parametrosDistribuidor.chamadaEncalheImpressao" id="chamadaEncalheImpressao" type="checkbox"  ${parametrosDistribuidor.chamadaEncalheImpressao}/></td>
		                    <td align="center"><input name="parametrosDistribuidor.chamadaEncalheEmail" id="chamadaEncalheEmail" type="checkbox"  ${parametrosDistribuidor.chamadaEncalheEmail}/></td>
		                  </tr>
		                </table>
	            </fieldset>
	            <fieldset style="width:320px!important; margin-bottom:5px;">
		           	  <legend>Impressão NE</legend>
		                <table width="325" border="0" cellspacing="0" cellpadding="0">
		                      <tr>
		                        <td width="26"><input type="radio" name="impressaoNE" id="impressaoNEModelo1" value="MODELO_1" /></td>
		                        <td width="93"><a href="${pageContext.request.contextPath}/modelos/ce_modelo_1.htm" target="_blank">Modelo 1</a></td>
		                        <td width="20"><input type="radio" name="impressaoNE" id="impressaoNEModelo2" value="MODELO_2" /></td>
		                        <td width="287"><a href="${pageContext.request.contextPath}/modelos/ce_modelo_2.html" target="_blank">Modelo 2</a></td>
		                      </tr>
		                      <tr>
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
		                        <td width="23"><input name="parametrosDistribuidor.impressaoNEFaltaDe" id="impressaoNEFaltaDe" type="checkbox"  ${parametrosDistribuidor.impressaoNEFaltaDe}/></td>
		                        <td width="123">Falta de</td>
		                        <td width="20"><input name="parametrosDistribuidor.impressaoNESobraDe" id="impressaoNESobraDe" type="checkbox"  ${parametrosDistribuidor.impressaoNESobraDe}/></td>
		                        <td width="108">Sobra de</td>
		                      </tr>
		                      <tr>
		                        <td><input name="parametrosDistribuidor.impressaoNEFaltaEm" id="impressaoNEFaltaEm" type="checkbox"  ${parametrosDistribuidor.impressaoNEFaltaEm}/></td>
		                        <td>Falta em</td>
		                        <td><input name="parametrosDistribuidor.impressaoNESobraEm" id="impressaoNESobraEm" type="checkbox"  ${parametrosDistribuidor.impressaoNESobraEm}/></td>
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
	            <fieldset style="width:320px!important; margin-bottom:5px;">
		               	<legend>Impressão NECA / Danfe</legend>
		                <table width="325" border="0" cellspacing="0" cellpadding="0">
		                      <tr>
		                        <td width="20"><input type="radio" name="impressaoNECADANFE" id="impressaoNECADANFEMODELO1" value="MODELO_1" /></td>
		                        <td width="79"><a href="${pageContext.request.contextPath}/modelos/ce_modelo_1.htm" target="_blank">Modelo 1</a></td>
		                        <td width="20"><input type="radio" name="impressaoNECADANFE" id="impressaoNECADANFEMODELO2" value="MODELO_2" /></td>
		                        <td width="75"><a href="${pageContext.request.contextPath}/modelos/ce_modelo_3.htm" target="_blank">Modelo 2</a></td>
		                        <td width="20"><input type="radio" name="impressaoNECADANFE" id="impressaoNECADANFE" value="DANFE" /></td>
		                        <td width="111"><a href="javascript:;" target="_blank">Danfe</a></td>
		                      </tr>
		                      <tr>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
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
		                        <td width="23"><input name="parametrosDistribuidor.impressaoNECADANFEFaltaDe" id="impressaoNECADANFEFaltaDe" type="checkbox"  ${parametrosDistribuidor.impressaoNECADANFEFaltaDe}/></td>
		                        <td width="123">Falta de</td>
		                        <td width="20"><input name="parametrosDistribuidor.impressaoNECADANFESobraDe" id="impressaoNECADANFESobraDe" type="checkbox"  ${parametrosDistribuidor.impressaoNECADANFESobraDe}/></td>
		                        <td width="108">Sobra de</td>
		                      </tr>
		                      <tr>
		                        <td><input name="parametrosDistribuidor.impressaoNECADANFEFaltaEm" id="impressaoNECADANFEFaltaEm" type="checkbox"  ${parametrosDistribuidor.impressaoNECADANFEFaltaEm}/></td>
		                        <td>Falta em</td>
		                        <td><input name="parametrosDistribuidor.impressaoNECADANFESobraEm" id="impressaoNECADANFESobraEm" type="checkbox"  ${parametrosDistribuidor.impressaoNECADANFESobraEm}/></td>
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
	              <fieldset style="width:320px!important; margin-bottom:5px;">
		           	  	<legend>Impressão CE</legend>
		                <table width="325" border="0" cellspacing="0" cellpadding="0">
		                      <tr>
		                        <td width="26"><input type="radio" name="impressaoCE" id="impressaoCEModelo2" value="MODELO_1" /></td>
		                        <td width="93"><a href="${pageContext.request.contextPath}/modelos/ce_modelo_1.htm" target="_blank">Modelo 1</a></td>
		                        <td width="20"><input type="radio" name="impressaoCE" id="impressaoCEModelo1" value="MODELO_2" /></td>
		                        <td width="287"><a href="${pageContext.request.contextPath}/modelos/ce_modelo_2.html" target="_blank">Modelo 2</a></td>
		                      </tr>
		                      <tr>
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
		                        <td width="23"><input name="parametrosDistribuidor.impressaoCEFaltaDe" id="impressaoCEFaltaDe" type="checkbox"  ${parametrosDistribuidor.impressaoCEFaltaDe}/></td>
		                        <td width="123">Falta de</td>
		                        <td width="20"><input name="parametrosDistribuidor.impressaoCESobraDe" id="impressaoCESobraDe" type="checkbox"  ${parametrosDistribuidor.impressaoCESobraDe}/></td>
		                        <td width="108">Sobra de</td>
		                      </tr>
		                      <tr>
		                        <td><input name="parametrosDistribuidor.impressaoCEFaltaEm" id="impressaoCEFaltaEm" type="checkbox"  ${parametrosDistribuidor.impressaoCEFaltaEm}/></td>
		                        <td>Falta em</td>
		                        <td><input name="parametrosDistribuidor.impressaoCESobraEm" id="impressaoCESobraEm" type="checkbox"  ${parametrosDistribuidor.impressaoCESobraEm}/></td>
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
	              <br clear="all"  />
			</div>
		    <!-- Aba Contratos e Garantias  -->	
            <jsp:include page="tabContratosGarantias.jsp"></jsp:include>
            			
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
