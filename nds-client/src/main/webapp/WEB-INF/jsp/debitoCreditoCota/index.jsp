<head>

	<title>NDS - Novo Distrib</title>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/cota.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	
<script>

	function processarResultadoConsultaDebitosCreditos(data) {
		
		if (data.mensagens) {

			exibirMensagem(
				data.mensagens.tipoMensagem, 
				data.mensagens.listaMensagens
			);

			$(".grids").hide();

			return;
		}

		var i;

		for (i = 0 ; i < data.tableModel.rows.length; i++) {

			var lastIndex = data.tableModel.rows[i].cell.length;

			var isLancamentoManual = data.tableModel.rows[i].cell[lastIndex - 1];
			
			data.tableModel.rows[i].cell[lastIndex - 1] = getAction(data.tableModel.rows[i].id, isLancamentoManual);
		}

		if ($(".grids").css('display') == 'none') {

			$(".grids").show();
		}

		$("#footerValorTotal").html("Total: R$ " + data.valorTotal);
		
		return data.tableModel;
	}

	function getAction(idMovimento, isLancamentoManual) {

		var acoes;
		
		if (isLancamentoManual == "true") {
			
			acoes = 
				'<a href="javascript:;" onclick="editarMovimento(' + idMovimento + ')" ' +
				' style="cursor:pointer;border:0px;margin:5px" title="Editar endereço">' +
				'<img src="${pageContext.request.contextPath}/images/ico_editar.gif" border="0px"/>' +
				'</a>' +
				'<a href="javascript:;" onclick="confirmarExclusaoMovimento(' + idMovimento + ')" ' +
				' style="cursor:pointer;border:0px;margin:5px" title="Excluir endereço">' +
				'<img src="${pageContext.request.contextPath}/images/ico_excluir.gif" border="0px"/>' +
				'</a>';
			
		} else {
			
			acoes = 
				'<span style="border:0px;margin:5px">' +
				'<img src="${pageContext.request.contextPath}/images/ico_editar.gif" border="0px" style="opacity:0.4"/>' +
				'</span>' +
				'<span style="border:0px;margin:5px">' +
				'<img src="${pageContext.request.contextPath}/images/ico_excluir.gif" border="0px" style="opacity:0.4"/>' +
				'</span>';
		}
		
		return acoes;		
	}
	
	function popularGridDebitosCreditos() {

		var formData = $("#formPesquisaDebitosCreditos").serializeArray();

		$(".debitosCreditosGrid").flexigrid({
			url : '<c:url value="/financeiro/debitoCreditoCota/pesquisarDebitoCredito" />',
			dataType : 'json',
			preProcess: processarResultadoConsultaDebitosCreditos,
			colModel : [  {
				display : 'Data de Lançamento',
				name : 'dataLancamento',
				width : 115,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Vencimento',
				name : 'dataVencimento',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Cota',
				name : 'numeroCota',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeCota',
				width : 140,
				sortable : true,
				align : 'left'
			},{
				display : 'Tipo de Lançamento',
				name : 'tipoLancamento',
				width : 140,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor R$',
				name : 'valor',
				width : 60,
				sortable : true,
				align : 'right'
			},{
				display : 'Observação',
				name : 'observacao',
				width : 190,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 60,
				sortable : false,
				align : 'center'
			}],
			sortname : "tipoLancamento",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			params: formData,
			rp : 15,
			singleSelect: true,
			showTableToggleBtn : true,
			width : 960,
			height : 180
		});

		$(".debitosCreditosGrid").flexOptions({
			url : '<c:url value="/financeiro/debitoCreditoCota/pesquisarDebitoCredito" />',
			preProcess: processarResultadoConsultaDebitosCreditos,
			params: formData
		});

		$(".debitosCreditosGrid").flexReload();
	}

	function editarMovimento(idMovimento) {

		$.postJSON(
			'<c:url value="/financeiro/debitoCreditoCota/prepararMovimentoFinanceiroCotaParaEdicao" />',
			{ "idMovimento" : idMovimento },
			function(result) {

				$("#edicaoId").val(result.id);
				$("#edicaoTipoMovimento").val(result.tipoMovimentoFinanceiro.id);
				$("#edicaoNumeroCota").val(result.numeroCota);
				$("#edicaoNomeCota").val(result.nomeCota);
				$("#edicaoDataLancamento").val(result.dataLancamento);
				$("#edicaoDataVencimento").val(result.dataVencimento);
				$("#edicaoValor").val(result.valor);
				$("#edicaoObservacao").val(result.observacao);
			},
			function(result) {
				
				processarResultadoConsultaDebitosCreditos(result);
			},
			true
		);
		
		popup_alterar();
	}
	
	function popup_alterar() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-editar" ).dialog({
			resizable: false,
			height:340,
			width:500,
			modal: true,
			buttons: {
				"Confirmar": function() {
					salvarMovimentoFinanceiro(true);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});	
	};
	
	function salvarMovimentoFinanceiro(isEdicao) {

		var url;
		var formData;
		var dialogId = isEdicao ? "#dialog-editar" : "#dialog-novo";
		
		if (isEdicao) {
			
			formData = $("#formEdicaoMovimentoFinanceiro").serializeArray();
			url = '<c:url value="/financeiro/debitoCreditoCota/editarMovimentoFincanceiroCota" />';
		
		} else {
			
			formData = obterListaMovimentos() + 'idTipoMovimento=' + $("#novoTipoMovimento").val();
			url = '<c:url value="/financeiro/debitoCreditoCota/criarMovimentoFincanceiroCota" />';
		}

		$.postJSON(
			url,
			formData,
			function(result) {

				popularGridDebitosCreditos();

				$(dialogId).dialog( "close" );
				$(".grids").show();
				
				exibirMensagem(
					result.tipoMensagem, 
					result.listaMensagens
				);

			},
			function(result) {

				exibirMensagemDialog(
					result.mensagens.tipoMensagem, 
					result.mensagens.listaMensagens
				);

			},
			true
		);
	}

	function removerMovimento(idMovimento) {

		$.postJSON(
			'<c:url value="/financeiro/debitoCreditoCota/removerMovimentoFinanceiroCota" />',
			{ "idMovimento" : idMovimento },
			function(result) {

				popularGridDebitosCreditos();

				$("#dialog-excluir").dialog( "close" );
				
				exibirMensagem(
					result.tipoMensagem, 
					result.listaMensagens
				);
			},
			function(result) {

				$("#dialog-excluir").dialog( "close" );
				
				exibirMensagemDialog(
					result.mensagens.tipoMensagem, 
					result.mensagens.listaMensagens
				);
			}
		);
	}
	
	function confirmarExclusaoMovimento(idMovimento) {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-excluir" ).dialog({
			resizable: false,
			height:170,
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					removerMovimento(idMovimento);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};
	
	
 	function pesquisarCota(isModalAlteracao) {
 		
 		var numeroCota;
 		
 		if (isModalAlteracao) {
 			
 			numeroCota = $("#edicaoNumeroCota").val();
 		
 		} else {
 			
 			numeroCota = $("#numeroCota").val();
 		}
 		
 		$.postJSON(
			'<c:url value="/financeiro/debitoCreditoCota/buscarCotaPorNumero" />',
			{ "numeroCota": numeroCota },
			function(result) {

				if (isModalAlteracao) {

					$("#edicaoNomeCota").val(result);
					
				} else {

					$("#nomeCota").html(result);
				}
			},
			null,
			true
		);
 	}
	
	$(function() {
		$( "#datepickerDe" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#datepickerAte" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#datepickerDeVenc" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#datepickerAteVenc" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#datepickerData" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$( "#datepickerDe" ).mask("99/99/9999");
		$( "#datepickerAte" ).mask("99/99/9999");
		$( "#datepickerDeVenc" ).mask("99/99/9999");
		$( "#datepickerAteVenc" ).mask("99/99/9999");
		$( "#datepickerData" ).mask("99/99/9999");
		
		$('#edicaoDataLancamento').datepicker("option", {minDate:-1,maxDate:-2})
		
		$( "#edicaoDataVencimento" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
			buttonImageOnly: true
		});
		
	});
</script>

</head>

<body>

<div id="dialog-excluir" title="Excluir Tipo de Movimento">
<p><strong>Confirma a exclusão deste Tipo de Movimento?</strong></p>
</div>

<div id="dialog-editar" title="Editar Tipo de Movimento">

<jsp:include page="../messagesDialog.jsp" />

<form id="formEdicaoMovimentoFinanceiro">
<input type="hidden" name="debitoCredito.id" id="edicaoId" />
<table width="450" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td width="126">Tipo de Movimento:</td>
    <td width="310">
    <select name="debitoCredito.tipoMovimentoFinanceiro.id" id="edicaoTipoMovimento" style="width:300px;">
  		<option selected="selected"></option>
		<c:forEach items="${tiposMovimentoFinanceiro}" var="tipoMovimento">
			<option value="${tipoMovimento.id}">${tipoMovimento.descricao}</option>
		</c:forEach>
    </select>
    </td>
  </tr>
  <tr>
    <td width="126">Cota:</td>
    <td width="310">
    <input type="text" style="width:80px; float:left; margin-right:5px;" 
    	   name="debitoCredito.numeroCota" id="edicaoNumeroCota" onblur="pesquisarCota(true);"/>
    </td>
  </tr>
  <tr>
    <td width="126">Nome:</td>
    <td width="310">
    <input type="text" style="width:300px;" name="debitoCredito.nomeCota" id="edicaoNomeCota" /></td>
  </tr>
  <tr>
    <td width="126">Data Lançamento:</td>
    <td width="310">
    	<input type="text" name="debitoCredito.dataLancamento" id="edicaoDataLancamento" readonly="readonly" style="width:80px;" />
    </td>
  </tr>
  <tr>
    <td>Data Vencimento:</td>
    <td>
    	<input type="text" name="debitoCredito.dataVencimento" id="edicaoDataVencimento" style="width:80px;" />
    </td>
  </tr>
  <tr>
    <td width="126">Valor R$:</td>
    <td width="310">
    	<input type="text" style="width:80px; text-align:right;" name="debitoCredito.valor" id="edicaoValor" />
    </td>
  </tr>
  <tr>
    <td width="126">Observação:</td>
    <td width="310">
    	<input type="text" style="width:300px;" name="debitoCredito.observacao" id="edicaoObservacao" />
    </td>
  </tr>
</table>
</form>
</div>

<br clear="all"/>
    <br />
   
    <div class="container">	
    <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Débito / Crédito < evento > com < status >.</b></p>
	</div>
<form id="formPesquisaDebitosCreditos">
      <fieldset class="classFieldset">
   	    <legend>Pesquisar Débitos / Créditos Cota
        </legend><table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
  <tr>
    <td width="105">Cota:</td>
    
    <td width="92">
	   <input type="text" style="width:60px; float:left; margin-right:5px;" 
   			  name="filtroDebitoCredito.numeroCota" id="numeroCota" onblur="pesquisarCota();"/>
    </td>
    <td width="38">Nome:</td>
    
    <td width="203"><span name="filtroDebitoCredito.nomeCota" id="nomeCota"></span></td>
    
    <td width="121">Tipo de Lançamento:</td>
    
    <td width="360">
	    <select name="filtroDebitoCredito.idTipoMovimento" id="idTipoMovimento" style="width:250px;">
		  <option selected="selected"></option>
		  <c:forEach items="${tiposMovimentoFinanceiro}" var="tipoMovimento">
		  	<option value="${tipoMovimento.id}">${tipoMovimento.descricao}</option>
		  </c:forEach>
		</select>
	</td>

  </tr>
  </table>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
          <tr>
            <td width="105">Data Lançamento:</td>
            <td width="115">
            	<input type="text" id="datepickerDeVenc" name="filtroDebitoCredito.dataLancamentoInicio" style="width:80px;" />
            </td>
            <td width="30">Até:</td>
            <td width="190">
            	<input id="datepickerAteVenc" type="text" name="filtroDebitoCredito.dataLancamentoFim" style="width:80px;" />
            </td>
            <td width="121">Data Vencimento:</td>
            <td width="106">
            	<input type="text" id="datepickerDe" name="filtroDebitoCredito.dataVencimentoInicio" style="width:80px;" />
            </td>
            <td width="29" align="center">Até:</td>
            <td colspan="2">
            	<input id="datepickerAte" type="text" name="filtroDebitoCredito.dataVencimentoFim" style="width:80px;" />
            </td>
            <td width="104"><span class="bt_pesquisar">
            	<a href="javascript:;" onclick="popularGridDebitosCreditos();">Pesquisar</a></span>
            </td>
          </tr>
        </table>
      </fieldset>
     </form>
          <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="classFieldset">
       	  <legend>Débitos / Créditos Cota Cadastrados</legend>
        <div class="grids" style="display:none;">
       	  <table class="debitosCreditosGrid"></table>
         
          <br />
	
		<span class="bt_novos" title="Gerar Arquivo">
			<a href="${pageContext.request.contextPath}/financeiro/debitoCreditoCota/exportar?fileType=XLS">
				<img src="${pageContext.request.contextPath}/images/ico_excel.png"  hspace="5" border="0" />
					Arquivo
			</a>
		</span>
		<span class="bt_novos" title="Imprimir">
			<a href="${pageContext.request.contextPath}/financeiro/debitoCreditoCota/exportar?fileType=PDF">
			<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
				Imprimir
			</a>
		</span>
         

          <span style="float:right; margin-right:260px" id="footerValorTotal"></span>
 </div>
 <span class="bt_novos" title="Novo"><a href="javascript:;" onclick="popupNovoDialog();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" 
 	   hspace="5" border="0"/>Novo</a></span>


      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      
    </div>
</div> 

<jsp:include page="novoDialog.jsp" />

<script>

$("input[id^='edicaoValor']").maskMoney({
	 thousands:'.', 
	 decimal:',', 
	 precision:2
});
</script>

</body>
