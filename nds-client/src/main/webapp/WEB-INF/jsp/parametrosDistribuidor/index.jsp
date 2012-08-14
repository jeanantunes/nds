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
	
	body{
	font-family: arial;
	font-size: 11px;
	color: #000;
	background-color: #FFF;
	margin: 0px;
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
		{name:'parametrosDistribuidor.impressaoInterfaceLED', value: $("input[name='interfaceLED']:checked").val()},
		{name:'parametrosDistribuidor.impressaoNECADANFE', value: $("input[name='impressaoNECADANFE']:checked").val()},
		{name:'parametrosDistribuidor.impressaoCE', value: $("input[name='impressaoCE']:checked").val()},
		{name:'parametrosDistribuidor.utilizaContratoComCotas', value: $('#utilizaContratoComCotas').is(':checked')},
		{name:'parametrosDistribuidor.prazoContrato', value: $('#prazoContrato').val()},
		{name:'parametrosDistribuidor.informacoesComplementaresContrato', value: $('#informacoesComplementaresContrato').val()},
		{name:'parametrosDistribuidor.utilizaProcuracaoEntregadores', value: $('#utilizaProcuracaoEntregadores').is(':checked')},
		{name:'parametrosDistribuidor.informacoesComplementaresProcuracao', value: $('#informacoesComplementaresProcuracao').val()},
		{name:'parametrosDistribuidor.utilizaGarantiaPdv', value: $('#utilizaGarantiaPdv').is(':checked')},
		{name:'parametrosDistribuidor.utilizaChequeCaucao', value: $('#utilizaChequeCaucao').is(':checked')},
		{name:'parametrosDistribuidor.validadeChequeCaucao', value: $('#validadeChequeCaucao').val()},
		{name:'parametrosDistribuidor.utilizaFiador', value: $('#utilizaFiador').is(':checked')},
		{name:'parametrosDistribuidor.validadeFiador', value: $('#validadeFiador').val()},
		{name:'parametrosDistribuidor.utilizaImovel', value: $('#utilizaImovel').is(':checked')},
		{name:'parametrosDistribuidor.validadeImovel', value: $('#validadeImovel').val()},
		{name:'parametrosDistribuidor.utilizaCaucaoLiquida', value: $('#utilizaCaucaoLiquida').is(':checked')},
		{name:'parametrosDistribuidor.validadeCaucaoLiquida', value: $('#validadeCaucaoLiquidaValor').val()},
		{name:'parametrosDistribuidor.utilizaNotaPromissoria', value: $('#utilizaNotaPromissoria').is(':checked')},
		{name:'parametrosDistribuidor.validadeNotaPromissoria', value: $('#validadeNotaPromissoriaValor').val()},
		{name:'parametrosDistribuidor.utilizaAntecedenciaValidade', value: $('#utilizaAntecedenciaValidade').is(':checked')},
		{name:'parametrosDistribuidor.validadeAntecedenciaValidade', value: $('#validadeAntecedenciaValidade').val()},
		{name:'parametrosDistribuidor.utilizaOutros', value: $('#utilizaOutros').is(':checked')},
		{name:'parametrosDistribuidor.validadeOutros', value: $('#validadeOutros').val()},
		{name:'parametrosDistribuidor.sugereSuspensaoQuandoAtingirBoletos', value: $('#sugereSuspensaoQuandoAtingirBoletos').val()},
		{name:'parametrosDistribuidor.sugereSuspensaoQuandoAtingirReais', value: $('#sugereSuspensaoQuandoAtingirReais').val()},
		{name:'parametrosDistribuidor.parcelamentoDividas', value: $('#parcelamentoDividas').is(':checked')},
		{name:'parametrosDistribuidor.negociacaoAteParcelas', value: $('#negociacaoAteParcelas').val()},		
		{name:'parametrosDistribuidor.utilizaDesconto', value: $('#utilizaDesconto').is(':checked')},
		{name:'parametrosDistribuidor.percentualDesconto', value: $('#percentualDesconto').val()},		
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

function removeFornecedor(){
	$( ".forncedoresSel" ).fadeOut('fast');
}

function habilitaPrazoContrato() {
	if ($('#utilizaContratoComCotas').is(':checked')) {
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
	$('#informacoesComplementaresTermoAdesaoEntregaBancas').wysiwyg();
	$('#informacoesComplementaresTermoAdesaoEntregaBancas').wysiwyg({controls:"font-family,italic,|,undo,redo"});
	$('#informacoesComplementaresTermoAdesaoEntregaBancas').wysiwyg('setContent','${parametrosDistribuidor.complementoTermoAdesaoEntregaBancas}');
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
		 thousands:'', 
		 decimal:'', 
		 precision:0
	});

	$("#validadeCaucaoLiquida").maskMoney({
		 thousands:'', 
		 decimal:'', 
		 precision:0
	});
	
	$("#validadeAntecedenciaValidade").maskMoney({
		 thousands:'', 
		 decimal:'', 
		 precision:0
	});
	
	$("#validadeFiador").maskMoney({
		 thousands:'', 
		 decimal:'', 
		 precision:0
	});
	
	$("#validadeImovel").maskMoney({
		 thousands:'', 
		 decimal:'', 
		 precision:0
	});
	
	$("#validadeChequeCaucao").maskMoney({
		 thousands:'', 
		 decimal:'', 
		 precision:0
	});
	
	$("#validadeNotaPromissoria").maskMoney({
		 thousands:'', 
		 decimal:'', 
		 precision:0
	});
	
	$("#validadeOutros").maskMoney({
		 thousands:'', 
		 decimal:'', 
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
	$('input:radio[name=interfaceLED][value=${parametrosDistribuidor.impressaoInterfaceLED}]').click();
	$('input:radio[name=impressaoNECADANFE][value=${parametrosDistribuidor.impressaoNECADANFE}]').click();
	$('input:radio[name=impressaoCE][value=${parametrosDistribuidor.impressaoCE}]').click();
	
	habilitaPrazoContrato();
	
	$("#tabDistribuidor").tabs();
});

function mostraTabelaGarantiasAceitas(){
	if ($('#checkUtilizaGarantiaPdv').is(':checked')) {
		$('#tabelaGarantiasAceitas').show();
	} else {
		$('#tabelaGarantiasAceitas').hide();
	}
}

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
			
            <!--  Aba Operação --> 
            <jsp:include page="tabOperacao.jsp"/>
			
			<jsp:include page="tabFiscal.jsp"></jsp:include>
			
      
			
			<jsp:include page="tabEmissao.jsp"></jsp:include>
			
		    <!-- Aba Contratos e Garantias  -->	
            <jsp:include page="tabContratosGarantias.jsp"></jsp:include>
            			
			<jsp:include page="tabNegociacao.jsp"></jsp:include>
		   
		    <jsp:include page="tabAprovacao.jsp"></jsp:include>
		    
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
