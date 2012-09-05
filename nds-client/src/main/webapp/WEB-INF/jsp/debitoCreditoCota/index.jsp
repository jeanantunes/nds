<head>

	<title>NDS - Novo Distrib</title>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	
<script>

	var pesquisaCotaDebitoCreditoCota = new PesquisaCota();

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
				' style="cursor:pointer;border:0px;margin:5px" title="Editar movimento">' +
				'<img src="${pageContext.request.contextPath}/images/ico_editar.gif" border="0px"/>' +
				'</a>' +
				'<a href="javascript:;" onclick="confirmarExclusaoMovimento(' + idMovimento + ')" ' +
				' style="cursor:pointer;border:0px;margin:5px" title="Excluir movimento">' +
				'<img src="${pageContext.request.contextPath}/images/ico_excluir.gif" border="0px"/>' +
				'</a>';	
			
		} else {
			
			acoes = 
				'<a href="javascript:;" onclick="detalheMovimento(' + idMovimento + ')" ' +
				' style="cursor:pointer;border:0px;margin:5px" title="Editar movimento">' +
				'<img src="${pageContext.request.contextPath}/images/ico_editar.gif" border="0px"/>' +
				'</a>' +
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
			sortname : "dataLancamento",
			sortorder : "desc",
			usepager : true,
			useRp : true,
			params: formData,
			rp : 15,
			singleSelect: true,
			showTableToggleBtn : true,
			width : 960,
			height : 'auto'
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
				
				configuraTelaEdicao(result.tipoMovimentoFinanceiro.id);
					
			},
			function(result) {
				
				processarResultadoConsultaDebitosCreditos(result);
			},
			true
		);
		
		popup_alterar();
	}
	
	function detalheMovimento(idMovimento) {

		$.postJSON(
			'<c:url value="/financeiro/debitoCreditoCota/prepararMovimentoFinanceiroCotaParaEdicao" />',
			{ "idMovimento" : idMovimento },
			function(result) {
				
				$("#detalheId").val(result.id);
				$("#detalheTipoMovimento").val(result.tipoMovimentoFinanceiro.descricao);
				$("#detalheNumeroCota").val(result.numeroCota);
				$("#detalheNomeCota").val(result.nomeCota);
				$("#detalheDataLancamento").val(result.dataLancamento);
				$("#detalheDataVencimento").val(result.dataVencimento);
				$("#detalheValor").val(result.valor);
				$("#detalheObservacao").val(result.observacao);
			},
			function(result) {
				
				processarResultadoConsultaDebitosCreditos(result);
			},
			true
		);
		
		popup_detalhe();
	}
	
	function popup_alterar() {
	
		$( "#dialog-editar" ).dialog({
			resizable: false,
			height:450,
			width:500,
			modal: true,
			buttons:[ 
			          {
				           id:"bt_confirmar",
				           text:"Confirmar", 
				           click: function() {
				        	   salvarMovimentoFinanceiro(true);
				           }
			           },
			           {
				           id:"bt_cancelar",
				           text:"Cancelar", 
				           click: function() {
				        	   $( this ).dialog( "close" );
				           }
			           }
	        ],
			beforeClose: function() {
				clearMessageDialogTimeout();
		    }
		});	
	};
	
	function popup_detalhe() {
	
		$( "#dialog-detalhe" ).dialog({
			resizable: false,
			height:350,
			width:500,
			modal: true,
			buttons:[ 
			           {
				           id:"bt_fechar",
				           text:"Fechar", 
				           click: function() {
				        	   $( this ).dialog( "close" );
				           }
			           }
	        ],
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
					result.mensagens.listaMensagens, ""
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
					result.mensagens.listaMensagens, ""
				);
			}
		);
	}
	
	function confirmarExclusaoMovimento(idMovimento) {
		
		$( "#dialog-excluir" ).dialog({
			resizable: false,
			height:170,
			width:380,
			modal: true,
			buttons:[ 
			          {
				           id:"bt_confirmar",
				           text:"Confirmar", 
				           click: function() {
				        	   removerMovimento(idMovimento);
				           }
			           },
			           {
				           id:"bt_cancelar",
				           text:"Cancelar", 
				           click: function() {
				        	   $( this ).dialog( "close" );
				           }
			           }
	        ],
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

 	//VERIFICA SE O TIPO DE LANÇAMENTO CONSIDERA FATURAMENTO DA COTA
	function configuraTelaEdicao(idTipoLancamento){
		var data = [{name: 'idTipoMovimento', value: idTipoLancamento}];
		$.postJSON("<c:url value='/financeiro/debitoCreditoCota/obterGrupoFaturamento' />",
				   data,
				   sucessCallbackConfiguraTelaEdicao,
				   null,
				   true);
	}
	
	function sucessCallbackConfiguraTelaEdicao(result){
		
		$("#grupoMovimentoHidden").val(result);
		
		if (result=='DEBITO_SOBRE_FATURAMENTO'){
			
			$('#edicaoBaseCalculo').show();
			$('#edicaoPercentual').show();
			$('#edicaoDataPeriodo').show();
			
			$('#tituloEdicaoBaseCalculo').show();
			$('#tituloEdicaoPercentual').show();
			$('#tituloEdicaoDataPeriodo').show();
			
			$("#edicaoValor").attr('readonly','readonly'); 
		}
		else{	
			
			$("#edicaoBaseCalculo").val(0);
			$("#edicaoPercentual").val('');
			$("#edicaoDataPeriodoDe").val('');
			$("#edicaoDataPeriodoAte").val('');
			
			$('#edicaoBaseCalculo').hide();
			$('#edicaoPercentual').hide();
			$('#edicaoDataPeriodo').hide();
			
			$('#tituloEdicaoBaseCalculo').hide();
			$('#tituloEdicaoPercentual').hide();
			$('#tituloEdicaoDataPeriodo').hide();
			
			$('#edicaoValor').removeAttr("readonly"); 
		}
	}
	
	function limparValor(){
		$("#edicaoValor").val('');
	}
 	
 	function obterInformacoesParaEdicao(){

		var grupoMovimento = $("#grupoMovimentoHidden").val();
		var percentual = $("#edicaoPercentual").val();
		var baseCalculo = $("#edicaoBaseCalculo").val();
		var dataPeriodoInicial = $("#edicaoDataPeriodoDe").val();
		var dataPeriodoFinal = $("#edicaoDataPeriodoAte").val();
        var numeroCota = $("#edicaoNumeroCota").val();
        var indexValor = 0;
		
		if ((grupoMovimento == "DEBITO_SOBRE_FATURAMENTO") && (percentual!='') && (baseCalculo!='') && (dataPeriodoInicial!='') && (dataPeriodoFinal!='')){

			var data = [{name:'numeroCota', value:numeroCota},
				        {name:'grupoMovimento', value:grupoMovimento},
				        {name:'percentual', value:percentual},
				        {name:'baseCalculo', value:baseCalculo},
				        {name:'dataPeriodoInicial', value:dataPeriodoInicial},
				        {name:'dataPeriodoFinal', value:dataPeriodoFinal},
				        {name:'index', value:indexValor}];
			
			$.postJSON("<c:url value='/financeiro/debitoCreditoCota/obterInformacoesParaLancamentoIndividual' />",
					    data,
						function(result){
							$("#edicaoValor").val(result.valor);
						},
						null,
						true);
			
		}	
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
		
		$( "#edicaoDataPeriodoDe" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$( "#edicaoDataPeriodoAte" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$("#edicaoNumeroCota").numeric();
		$("#edicaoPercentual").numeric();
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
    <select name="debitoCredito.tipoMovimentoFinanceiro.id" id="edicaoTipoMovimento" style="width:300px;" onchange="configuraTelaEdicao(this.value); limparValor();">
		<c:forEach items="${tiposMovimentoFinanceiro}" var="tipoMovimento">
			<option value="${tipoMovimento.id}">${tipoMovimento.id}-${tipoMovimento.descricao}</option>
		</c:forEach>
    </select>
    </td>
  </tr>

  <tr>
    <td width="126">Cota:</td>
    <td width="310">
    <input maxlength="11" type="text" style="width:80px; float:left; margin-right:5px;" 
    	   name="debitoCredito.numeroCota" id="edicaoNumeroCota" onblur="pesquisarCota(true);"/>
    </td>
  </tr>
  <tr>
    <td width="126">Nome:</td>
    <td width="310">
    <input maxlength="80" type="text" style="width:300px;" name="debitoCredito.nomeCota" id="edicaoNomeCota" /></td>
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
    <td width="126" id="tituloEdicaoPercentual" >Percentual(%)</td>
    <td width="310">
    	<input  maxlength="3" type="text" style="width:80px; text-align:right;" name="edicaoPercentual" id="edicaoPercentual" onchange="obterInformacoesParaEdicao();" />
    </td>
  </tr>
  
  <tr>
    <td width="80" id="tituloEdicaoBaseCalculo">Base de Cálculo:</td>
    <td width="120">
		<select name="edicaoBaseCalculo" id="edicaoBaseCalculo" style="width:120px;" onchange="obterInformacoesParaEdicao();" >
		
	  		<option selected="selected"></option>
			<c:forEach items="${basesCalculo}" var="base">
				<option value="${base}">${base.value}</option>
			</c:forEach>
			
	    </select>
    </td>
  </tr>
  
  <tr>
    <td width="100" id="tituloEdicaoDataPeriodo">Período para Cálculo:</td>
    <td width="180" id="edicaoDataPeriodo">
		<input type="text" name="edicaoDataPeriodoDe" id="edicaoDataPeriodoDe" style="width:80px;" onchange="obterInformacoesParaEdicao();" />
        até
		<input type="text" name="edicaoDataPeriodoAte" id="edicaoDataPeriodoAte" style="width:80px;" onchange="obterInformacoesParaEdicao();" />
    </td>
  </tr>

  <tr>
    <td width="126">Valor R$:</td>
    <td width="310">
    	<input  maxlength="16" type="text" style="width:80px; text-align:right;" name="debitoCredito.valor" id="edicaoValor" />
    </td>
  </tr>
  <tr>
    <td width="126">Observação:</td>
    <td width="310">
    	<input  maxlength="150" type="text" style="width:300px;" name="debitoCredito.observacao" id="edicaoObservacao" />
    </td>
  </tr>
</table>
</form>
</div>

<div id="dialog-detalhe" title="Tipo de Movimento" style="display: none;">

<jsp:include page="../messagesDialog.jsp" />

<form id="formDetalheMovimentoFinanceiro">
<input readonly="readonly" type="hidden" name="debitoCredito.id" id="detalheId" />
<table width="450" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td width="126">Tipo de Movimento:</td>
    <td width="310">
    <input readonly="readonly" name="debitoCredito.tipoMovimentoFinanceiro.id" id="detalheTipoMovimento" style="width:300px;">
    </td>
  </tr>
  <tr>
    <td width="126">Cota:</td>
    <td width="310">
    <input readonly="readonly" type="text" style="width:80px; float:left; margin-right:5px;" 
    	   name="debitoCredito.numeroCota" id="detalheNumeroCota"/>
    </td>
  </tr>
  <tr>
    <td width="126">Nome:</td>
    <td width="310">
    <input readonly="readonly" type="text" style="width:300px;" name="debitoCredito.nomeCota" id="detalheNomeCota" /></td>
  </tr>
  <tr>
    <td width="126">Data Lançamento:</td>
    <td width="310">
    	<input readonly="readonly" type="text" name="debitoCredito.dataLancamento" id="detalheDataLancamento" style="width:80px;" />
    </td>
  </tr>
  <tr>
    <td>Data Vencimento:</td>
    <td>
    	<input readonly="readonly" type="text" name="debitoCredito.dataVencimento" id="detalheDataVencimento" style="width:80px;" />
    </td>
  </tr>
  <tr>
    <td width="126">Valor R$:</td>
    <td width="310">
    	<input readonly="readonly" type="text" style="width:80px; text-align:right;" name="debitoCredito.valor" id="detalheValor" />
    </td>
  </tr>
  <tr>
    <td width="126">Observação:</td>
    <td width="310">
    	<input readonly="readonly" type="text" style="width:300px;" name="debitoCredito.observacao" id="detalheObservacao" />
    </td>
  </tr>
</table>
</form>
</div>


	<form id="formPesquisaDebitosCreditos">
	
	<div class="areaBts">
		<div class="area">
			<span class="bt_novos">
				<a href="javascript:;" onclick="popupNovoDialog();" rel="tipsy" title="Incluir Novo Tipo de Movimento">
					<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" 
 	   				hspace="5" border="0"/>
 	   			</a>
 	   		</span>
			
			<span class="bt_arq">
			<a href="${pageContext.request.contextPath}/financeiro/debitoCreditoCota/exportar?fileType=XLS" rel="tipsy" title="Gerar Arquivo">
				<img src="${pageContext.request.contextPath}/images/ico_excel.png"  hspace="5" border="0" />
			</a>
		</span>
		<span class="bt_arq">
			<a href="${pageContext.request.contextPath}/financeiro/debitoCreditoCota/exportar?fileType=PDF" rel="tipsy" title="Imprimir">
			<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
			</a>
		</span>
		</div>
	</div>
	
	<div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="fieldFiltro">
   	    <legend>Pesquisar Débitos / Créditos Cota</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
		  <tr>
		
		    <td width="33">Cota:</td>
		         <td width="85">
		         <input name="filtroDebitoCredito.numeroCota" 
		       		    id="numeroCota" 
		       		    type="text"
		       		    maxlength="11"
		       		    style="width:80px; 
		       		    float:left; margin-right:5px;"
		       		    onchange="pesquisaCotaDebitoCreditoCota.pesquisarPorNumeroCota('#numeroCota', '#nomeCota');" />
			</td>
			<td width="41">Nome:</td>
			<td colspan="3">
			     <input name="filtroDebitoCredito.nomeCota" 
			      	    id="nomeCota" 
			      		type="text" 
			      		class="nomeCota" 
			      		maxlength="255"
			      		style="width:250px;"
			      		onkeyup="pesquisaCotaDebitoCreditoCota.autoCompletarPorNome('#nomeCota');" 
			      		onblur="pesquisaCotaDebitoCreditoCota.pesquisarPorNomeCota('#numeroCota', '#nomeCota');" />
			</td>
		    
		    <td width="114">Tipo de Lançamento:</td>
		    
		    <td colspan="4">
			    <select name="filtroDebitoCredito.idTipoMovimento" id="idTipoMovimento" style="width:250px;">
				  <option selected="selected"></option>
				  <c:forEach items="${tiposMovimentoFinanceiro}" var="tipoMovimento">
				  	<option value="${tipoMovimento.id}">${tipoMovimento.descricao}</option>
				  </c:forEach>
				</select>
			</td>
		
		  </tr>
		  <tr>
		    <td colspan="3">Data Lançamento:</td>
		    <td width="104"><input type="text" id="datepickerDeVenc" name="filtroDebitoCredito.dataLancamentoInicio" style="width:80px;" /></td>
		    <td width="26">Até:</td>
		    <td width="137"><input id="datepickerAteVenc" type="text" name="filtroDebitoCredito.dataLancamentoFim" style="width:80px;" /></td>
		    <td>Data Vencimento:</td>
		    <td width="136"><input type="text" id="datepickerDe" name="filtroDebitoCredito.dataVencimentoInicio" style="width:80px;" /></td>
		    <td width="27">Até:</td>
		    <td width="113"><input id="datepickerAte" type="text" name="filtroDebitoCredito.dataVencimentoFim" style="width:80px;" /></td>
		    <td width="78"><span class="bt_novos">
            	<a href="javascript:;" onclick="popularGridDebitosCreditos();"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a></span></td>
  </tr>
  		</table>
      </fieldset>
     </form>
          <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="fieldGrid">
       	  <legend>Débitos / Créditos Cota Cadastrados</legend>
        <div class="grids" style="display:none;">
       	  <table class="debitosCreditosGrid"></table>
         
          <br />
	
		
         

          <span style="float:right; margin-right:260px" id="footerValorTotal"></span>
 		</div>
      </fieldset>
      


<jsp:include page="novoDialog.jsp" />

<script>

$("input[id^='edicaoValor']").maskMoney({
	 thousands:'.', 
	 decimal:',', 
	 precision:2
});
</script>

</body>
