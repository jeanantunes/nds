<head>
	<title>NDS - Novo Distrib</title>

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

		for (i = 0 ; i < data.rows.length; i++) {

			var lastIndex = data.rows[i].cell.length;

			data.rows[i].cell[lastIndex] = getAction(data.rows[i].id);
		}

		if ($(".grids").css('display') == 'none') {

			$(".grids").show();
		}

		return data;
	}

	function getAction(idMovimento) {

		return '<a href="javascript:;" onclick="editarMovimento(' + idMovimento + ')" ' +
				' style="cursor:pointer;border:0px;margin:5px" title="Editar endereço">' +
				'<img src="${pageContext.request.contextPath}/images/ico_editar.gif" border="0px"/>' +
				'</a>' +
				'<a href="javascript:;" onclick="confirmarExclusaoMovimento(' + idMovimento + ')" ' +
				' style="cursor:pointer;border:0px;margin:5px" title="Excluir endereço">' +
				'<img src="${pageContext.request.contextPath}/images/ico_excluir.gif" border="0px"/>' +
				'</a>';
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
			params: formData
		});

		$(".debitosCreditosGrid").flexReload();
	}

	function popup() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:450,
			width:780,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
					$(".grids").show();
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};
	
	function editarMovimento(idMovimento) {

		$.postJSON(
			'<c:url value="/financeiro/debitoCreditoCota/prepararMovimentoFinanceiroCotaParaEdicao" />',
			{ "idMovimento" : idMovimento },
			function(result) {

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
					salvarMovimentoFinanceiro();
					$( this ).dialog( "close" );
					$("#effect").hide("highlight", {}, 1000, callback);
					$( "#abaPdv" ).show( );
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});	
		      
	};
	
	function salvarMovimentoFinanceiro() {

		var formData = $("#formEdicaoMovimentoFinanceiro").serializeArray();

		$.postJSON(
			'<c:url value="/financeiro/debitoCreditoCota/cadastrarMovimentoFincanceiroCota" />',
			formData,
			null,
			function(result) {
				
				processarResultadoConsultaDebitosCreditos(result);
			},
			true
		);
	}

	function removerMovimento(idMovimento) {

		$.postJSON(
			'<c:url value="/financeiro/debitoCreditoCota/removerMovimentoFinanceiroCota" />',
			{ "idMovimento" : idMovimento },
			null,
			function(result) {
				
				processarResultadoConsultaDebitosCreditos(result);
			},
			true
		);

		popularGridDebitosCreditos();
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
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};
	
	
 	function pesquisarCota() {
 		
 		var numeroCota = $("#numeroCota").val();
 		
 		$.postJSON(
			'<c:url value="/financeiro/debitoCreditoCota/buscarCotaPorNumero" />',
			{ "numeroCota": numeroCota },
			function(result) {
				$("#nomeCota").html(result);
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
		$( "#edicaoDataLancamento" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$( "#edicaoDataVencimento" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
			buttonImageOnly: true
		});
		
	});

	$(function() {
		var availableTags = [
			"344 - Crédito ao Cota",
			"344 - Crédito ao Cota Ausente"			
		];
		$( "#tipoMovimento" ).autocomplete({
			source: availableTags
		});
		var availableTags_1 = [
			"344 - Crédito ao Cota",
			"344 - Crédito ao Cota Ausente"			
		];
		$( "#tipoMovimento_1" ).autocomplete({
			source: availableTags_1
		});
	});
	
</script>

</head>

<body>

<div id="dialog-excluir" title="Excluir Tipo de Movimento">
<p><strong>Confirma a exclusão deste Tipo de Movimento?</strong></p>
</div>

<div id="dialog-novo" title="Incluir Novo Tipo de Movimento">
    
<table width="650" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td width="119">Tipo de Lançamento:</td>
    <td width="517">
    <select name="select" id="select" style="width:300px;">
      <option>344 - Crédito a Cota</option>
	  <option>355 - Débito a Cota</option>
    </select>
    </td>
  </tr>
</table>
<br />
<table class="debitosCreditosGrid_1"></table>
</div>


<div id="dialog-editar" title="Editar Tipo de Movimento">
<form id="formEdicaoMovimentoFinanceiro">
<table width="450" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td width="126">Tipo de Movimento:</td>
    <td width="310">
    <select name="debitoCredito.tipoMovimentoFinanceiro" id="edicaoTipoMovimento" style="width:300px;">
  		<option selected="selected"> </option>
		<c:forEach items="${tiposMovimentoFinanceiro}" var="tipoMovimento">
			<option value="${tipoMovimento.id}">${tipoMovimento.descricao}</option>
		</c:forEach>
    </select>
    </td>
  </tr>
  <tr>
    <td width="126">Cota:</td>
    <td width="310">
    <input type="text" style="width:80px; float:left; margin-right:5px;" name="debitoCredito.numeroCota" id="edicaoNumeroCota"/>
   	<span class="classPesquisar"><a href="javascript:;" onclick="pesquisarCota();">&nbsp;</a></span>
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
    	<input type="text" name="debitoCredito.dataLancamento" id="edicaoDataLancamento" style="width:80px;" />
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
   			  name="filtroDebitoCredito.numeroCota" id="numeroCota"/>
	   <span class="classPesquisar"><a href="javascript:;" onclick="pesquisarCota();">&nbsp;</a></span>
    </td>
    <td width="38">Nome:</td>
    
    <td width="203"><span name="filtroDebitoCredito.nomeCota" id="nomeCota"></span></td>
    
    <td width="121">Tipo de Lançamento:</td>
    
    <td width="360">
	    <select name="filtroDebitoCredito.idTipoMovimento" id="idTipoMovimento" style="width:250px;">
		  <option selected="selected"> </option>
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
         
          <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>

<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
          <span style="float:right; margin-right:260px">Total: R$ 999.999,99</span>
 </div>
 <span class="bt_novos" title="Novo"><a href="javascript:;" onclick="popup();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>Novo</a></span>


      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      
    </div>
</div> 
<script>
		/*
		$(".debitosCreditosGrid_1").flexigrid({
			url : '../xml/debitosCreditos_1-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 120,
				sortable : true,
				align : 'left'
			},{
				display : 'Nome',
				name : 'nome',
				width : 185,
				sortable : true,
				align : 'left'
			}, {
				display : 'Data',
				name : 'data',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Valor R$',
				name : 'valor',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Observação',
				name : 'observacao',
				width : 220,
				sortable : true,
				align : 'left'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 760,
			height : 230
		});*/
	</script>
</body>
