<div id="dialog-novo" title="Incluir Novo Tipo de Movimento" style="display: none;">

	<jsp:include page="../messagesDialog.jsp" />
	
	<input type="hidden" id="grupoMovimentoHidden" />

	<table width="760" border="0" cellspacing="2" cellpadding="2">
	  <tr>
	    <td width="119">Tipo de Lançamento:</td>
	    <td width="517">
			<select onchange="configuraTelaLancamento(this.value);" name="debitoCredito.tipoMovimentoFinanceiro.id" id="novoTipoMovimento" style="width:300px;">
		  		<option selected="selected"></option>
				<c:forEach items="${tiposMovimentoFinanceiro}" var="tipoMovimento">
					<option value="${tipoMovimento.id}">${tipoMovimento.descricao}</option>
				</c:forEach>
		    </select>
	    </td>
	  </tr>
	</table>   
	  
	<table name="tabelaFaturamento" id="tabelaFaturamento" width="760" border="0">  
	  <tr>
	    <td width="40">Percentual(%):</td>
	    <td width="30">
			 <input type="text" maxlength="3" style="width:30px;height:15px;" name="debitoCredito.percentual" id="novoPercentual" />
	    </td>
	    
	    <td width="80">Base de Cálculo:</td>
	    <td width="120">
			<select name="debitoCredito.baseCalculo.id" id="novoBaseCalculo" style="width:120px;">
			
		  		<option selected="selected"></option>
				<c:forEach items="${basesCalculo}" var="base">
					<option value="${base}">${base.value}</option>
				</c:forEach>
				
		    </select>
	    </td>
	    
	    <td width="100">Período para Cálculo:</td>
	    <td width="70">
			<input type="text" name="debitoCredito.dataPeriodoDe" id="novoDataPeriodoDe" style="width:70px;height:15px;" />
	    </td>
	    
	    <td width="20">até</td>
	    <td width="70">
			<input type="text" name="debitoCredito.dataPeriodoAte" id="novoDataPeriodoAte" style="width:70px;height:15px;" />
	    </td>
	  </tr>
   </table>	  
	  
   <table width="760" border="0">  	  
	  <tr>
	    <td width="20">Box:</td>
	    <td width="120">
			<select name="debitoCredito.box.id" id="novoBox" onchange="carregarRoteiros(this.value,0);carregarRotas(0,0);" style="width:120px;">
			
		  		<option value="0" selected="selected"></option>
				<c:forEach items="${boxes}" var="box">
					<option value="${box.id}">${box.nome}</option>
				</c:forEach>
				
		    </select>
	    </td>
	    
	    <td width="40" id="tituloNovoRoteiro">Roteiro:</td>
	    <td width="180">
		    <div id="roteirosBox"/>
	    </td>
	    
	    <td width="30" id="tituloNovoRota">Rota:</td>
	    <td width="180">
			<div id="rotasRoteiro"/>
	    </td>
	  </tr>
   </table>	  
	  
   <table width="760" border="0">   	  
	  <tr>
	    <td width="100">Data Vencimento:</td>
	    <td width="100">
			<input type="text" name="debitoCredito.dataVencimento" id="novoDataVencimento" style="width:70px;height:15px;" />
	    </td>
	    
	    <td width="40" id="tituloNovoValor">Valor(R$):</td>
	    <td width="70">
			 <input maxlength="16" type="text" style="width:70px;height:15px;" name="debitoCredito.valor" id="novoValor" />
	    </td>
	    
	    <td width="30">Observação:</td>
	    <td width="300">
			 <input maxlength="150" type="text" style="width:300px;height:15px;" name="debitoCredito.observacao" id="novoObservacao" />
	    </td>
	    
	    <td width="20">
	        <span>
	            <a href="javascript:;" onclick="obterInformacoesParaLancamento();" style="width:20px;">
	                <img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" hspace="5" border="0" />
	            </a>
	        </span>
	    </td>
	    
	  </tr>
	</table>
	
	
	
	
	<br />
	
	<form name="formularioListaLancamentos" id="formularioListaLancamentos">

		<table class="debitosCreditosGrid_1" id="debitosCreditosGrid_1"></table>
		
		<table  width="100%" border="0" cellspacing="2" cellpadding="2" >
		    <tr>
			    <td width="80%"></td>    
			    
				<td width="20%">    
			        <span class="checar">
			            <label for="textoSelTodos" id="textoSelTodos" style="float:left;">Marcar Todos</label>
			            <input title="Selecionar todos os lançamentos" type="checkbox" id="selTodos" name="selTodos" onclick="selecionarTodos(this.checked);" style="float:left;"/>
			        </span>
			    </td>
            </tr> 
	    </table>
	
	</form>
	
	<script language="javascript" type="text/javascript">
	
	    
	    $(function(){
	    	$( "#novoDataPeriodoDe" ).datepicker({
				showOn: "button",
				buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
				buttonImageOnly: true
			});
			$( "#novoDataPeriodoAte" ).datepicker({
				showOn: "button",
				buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
				buttonImageOnly: true
			});
			$( "#novoDataVencimento" ).datepicker({
				showOn: "button",
				buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
				buttonImageOnly: true
			});
			
			$("#novoPercentual").numeric();
			$("#novoValor").numeric();
			$("#novoDataPeriodoDe").mask("99/99/9999");
			$("#novoDataPeriodoAte").mask("99/99/9999");
			$("#novoDataVencimento").mask("99/99/9999");
			
			carregarRoteiros(0,0);
			carregarRotas(0,0);
			
	    });
	    

		$(".debitosCreditosGrid_1").flexigrid({
			preProcess: executarPreProcessamentoMovimento,
			onSuccess: configurarCampos,
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 70,
				sortable : false,
				align : 'left'
			},{
				display : 'Nome',
				name : 'nome',
				width : 185,
				sortable : false,
				align : 'left'
			}, {
				display : 'Data',
				name : 'data',
				width : 80,
				sortable : false,
				align : 'center'
			}, {
				display : 'Valor R$',
				name : 'valor',
				width : 90,
				sortable : false,
				align : 'center'
			}, {
				display : 'Observação',
				name : 'observacao',
				width : 230,
				sortable : false,
				align : 'left'
			}, {
				display : '',
				name : 'check',
				width : 20,
				sortable : false,
				align : 'center',
			}],
			disableSelect : true,
			width : 770,
			height : 300
		});
		
		function configurarCampos() {
			
			$("input[id^='data']").datepicker();
			
			$("input[id^='data']").mask("99/99/9999");
			
			$("input[id^='valor']").maskMoney({
				 thousands:'.', 
				 decimal:',', 
				 precision:2
			});
			
			$("input[id^='numeroCota']").numeric();
			
			$("input[name='debitoCredito.nomeCota']").autocomplete({source: ""});
			
		}

// 		function popupNovoDialog() {
// 			$( "#dialog-novo" ).dialog({
// 				resizable: false,
// 				height:450,
// 				width:780,
// 				modal: true,
// 				buttons: {
// 					"Confirmar": function() {
// 						$( this ).dialog( "close" );
// 						$("#effect").show("highlight", {}, 1000, callback);
// 						$(".grids").show();
// 					},
// 					"Cancelar": function() {
// 						$( this ).dialog( "close" );
// 					}
// 				}
// 			});
// 		};

		function popupNovoDialog() {
			
			$('#tabelaFaturamento').hide();
			$('#tituloNovoValor').show();
			$('#novoValor').show();
			
			$("#novoTipoMovimento").val(0);
			$("#grupoMovimentoHidden").val('');
			$("#novoBaseCalculo").val(0);
			$("#novoBox").val(0);
			$("#novoRoteiro").val(0);
			$("#novoRota").val(0);
			
			$("#novoPercentual").val('');
			$("#novoDataVencimento").val('');
			$("#novoDataPeriodoDe").val('');
			$("#novoDataPeriodoAte").val('');
			$("#novoValor").val('');
			$("#novoObservacao").val('');

			$("#debitosCreditosGrid_1").flexOptions({
				url : '<c:url value="/financeiro/debitoCreditoCota/novoMovimento" />', 
			});
			
			$("#debitosCreditosGrid_1").flexReload();
			
			var closeEnable = false;
			
			$("#dialog-novo").dialog({
				resizable: false,
				height:600,
				width:800,
				modal: true,
				buttons:[ 
				          {
					           id:"bt_confirmar",
					           text:"Confirmar", 
					           click: function() {
					        	   closeEnable = true;
								   salvarMovimentoFinanceiro();
					           }
				           },
				           {
					           id:"bt_cancelar",
					           text:"Cancelar", 
					           click: function() {
					        	   closeEnable = true;
								   $(this).dialog("close");
					           }
				           }
		        ],
				beforeClose: function() {
					
					if (closeEnable!=true){
					    return !existMovimentos();
					}
					
					clearMessageDialogTimeout();
				}
			});     
		}
		
		function obterVencimentoLancamentoAnterior(index){
			if (index > 0){
				$("#data" + (index)).val($("#data" + (index-1)).val());
			}    
		}
		
		function executarPreProcessamentoMovimento(resultado) {

			if (resultado.mensagens) {

				exibirMensagemDialog(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens,
					""
				);

				return resultado;
			}

			var grupoMovimento = $("#grupoMovimentoHidden").val();
			
			$.each(resultado.rows, function(index, row) {

				var vencimento = $("#novoDataVencimento").val();
				var observacao = $("#novoObservacao").val();
				var numCota='';
				if (row.cell.numeroCota!=null){
					numCota = row.cell.numeroCota;
				}
				var nomeCota='';
				if (row.cell.nomeCota!=null){
					nomeCota = row.cell.nomeCota;
				}
				var valor = $("#novoValor").val();
				if (row.cell.valor!=null){
					valor = row.cell.valor;
				}
				
				var hiddenId = '<input type="hidden" name="idMovimento" value="' + index + '" />';
				
				var parametroPesquisaCota = '\'#numeroCota' + index + '\', \'#nomeCota' + index + '\', true';

				var parametroAutoCompleteCota = '\'#nomeCota' + index + '\', true';
				
				var inputNumeroCota = '<input id="numeroCota' + index + '" value="' + numCota + '" maxlength="9" name="debitoCredito.numeroCota" type="text" style="width:60px; float:left; margin-right:5px;" onchange="cota.pesquisarPorNumeroCota(' + parametroPesquisaCota + '); obterInformacoesParaLancamentoIndividual(this.value, '+index+'); obterVencimentoLancamentoAnterior('+index+'); " />';

				var inputNomeCota = '<input id="nomeCota' + index + '" value="' + nomeCota+ '" name="debitoCredito.nomeCota" type="text" style="width:180px;" onkeyup="cota.autoCompletarPorNome(' + parametroAutoCompleteCota + ');" onblur="cota.pesquisarPorNomeCota(' + parametroPesquisaCota + ')" />';
				
				var inputData = '<input id="data' + index + '" value="' + vencimento + '" name="debitoCredito.dataVencimento" type="text" style="width:70px;" />';
				
				var inputValor = '<input id="valor' + index + '" value="' + valor + '" name="debitoCredito.valor" type="text" style="width:80px;" />';
				if (grupoMovimento=="DEBITO_SOBRE_FATURAMENTO"){
					inputValor = '<input readonly="readonly" id="valor' + index + '" value="' + valor + '" name="debitoCredito.valor" type="text" style="width:80px;" />';
				}
				
				var inputObservacao = '<input id="observacao' + index + '" value="' + observacao + '" name="debitoCredito.observacao" type="text" style="width:220px;" />';

				var checkBox = '<input id="checkbox'+ index +'" title="Selecionar Lançamento" type="checkbox" name="debitoCredito.checkboxGrid" />';

				row.cell[0] = hiddenId + inputNumeroCota;
				row.cell[1] = inputNomeCota;
				row.cell[2] = inputData;
				row.cell[3] = inputValor;
				row.cell[4] = inputObservacao;
				row.cell[5] = checkBox;
				
			});
			
			document.getElementById("selTodos").checked = false;
			selecionarTodos(false);
			
			return resultado;
		}

		function tratarErroCadastroNovosMovimentos(jsonData) {

			if (!jsonData || !jsonData.mensagens) {

				return;
			}

			var dadosValidacao = jsonData.mensagens.dados;
			
			var linhasDaGrid = $(".debitosCreditosGrid_1 tr");

			$.each(linhasDaGrid, function(index, value) {

				var linha = $(value);

				if (dadosValidacao 
						&& ($.inArray(index, dadosValidacao) > -1)) {

					linha.removeClass('erow').addClass('linhaComErro');
					
				} else {

					linha.removeClass('linhaComErro');					
				}
			});
		}
		
		function obterListaMovimentos() {

			var linhasDaGrid = $(".debitosCreditosGrid_1 tr");

			var listaMovimentos = "";

			$.each(linhasDaGrid, function(index, value) {

				var linha = $(value);
				
				var colunaNumeroCota = linha.find("td")[0];
				var colunaNomeCota = linha.find("td")[1];
				var colunaData = linha.find("td")[2];
				var colunaValor = linha.find("td")[3];
				var colunaObservacao = linha.find("td")[4];
				var colunaChecked = linha.find("td")[5];
				var colunaIdMovimento = linha.find("td")[6];
				
				var numeroCota = 
					$(colunaNumeroCota).find("div").find('input[name="debitoCredito.numeroCota"]').val();
				
				var nomeCota = 
					$(colunaNomeCota).find("div").find('input[name="debitoCredito.nomeCota"]').val();

				var data = 
					$(colunaData).find("div").find('input[name="debitoCredito.dataVencimento"]').val();

				var valor =
					$(colunaValor).find("div").find('input[name="debitoCredito.valor"]').val();
				
				var observacao =
					$(colunaObservacao).find("div").find('input[name="debitoCredito.observacao"]').val();
				
				var idMovimento =
					$(colunaNumeroCota).find("div").find('input[name="idMovimento"]').val();
				
				if (isAtributosMovimentoVazios(numeroCota, nomeCota, data, valor, observacao)) {

					return true;
				}
	
				var checked = document.getElementById("checkbox"+index).checked;
				if (checked == true) {
					
					var movimento = 'listaNovosDebitoCredito[' + index + '].numeroCota=' + numeroCota + '&';
	
					movimento += 'listaNovosDebitoCredito[' + index + '].nomeCota=' + nomeCota + '&';
		
					movimento += 'listaNovosDebitoCredito[' + index + '].dataVencimento=' + data + '&';
		
					movimento += 'listaNovosDebitoCredito[' + index + '].valor=' + valor + '&';
					
					movimento += 'listaNovosDebitoCredito[' + index + '].observacao=' + observacao + '&';
	
					movimento += 'listaNovosDebitoCredito[' + index + '].id=' + idMovimento + '&';

					listaMovimentos = (listaMovimentos + movimento);

				}

			});

			return listaMovimentos;
		}

		function existMovimentos() {

			var linhasDaGrid = $(".debitosCreditosGrid_1 tr");
			
			var isMovimentos = false;

			$.each(linhasDaGrid, function(index, value) {

				var linha = $(value);
				
				var colunaNumeroCota = linha.find("td")[0];
				var colunaNomeCota = linha.find("td")[1];
				var colunaData = linha.find("td")[2];
				var colunaValor = linha.find("td")[3];
				var colunaObservacao = linha.find("td")[4];
				
				var numeroCota = 
					$(colunaNumeroCota).find("div").find('input[name="debitoCredito.numeroCota"]').val();
				
				var nomeCota = 
					$(colunaNomeCota).find("div").find('input[name="debitoCredito.nomeCota"]').val();

				var data = 
					$(colunaData).find("div").find('input[name="debitoCredito.dataVencimento"]').val();

				var valor =
					$(colunaValor).find("div").find('input[name="debitoCredito.valor"]').val();
				
				var observacao =
					$(colunaObservacao).find("div").find('input[name="debitoCredito.observacao"]').val();

				
				if (!isAtributosMovimentoVazios(numeroCota, nomeCota, data, valor, observacao)) {
					isMovimentos = true;
					return isMovimentos;
				}

			});

			return isMovimentos;
		}
		
		function habilitaValor(habilita){
			
            var linhasDaGrid = $(".debitosCreditosGrid_1 tr");
            
			$.each(linhasDaGrid, function(index, value) {
				$("#valor"+index).val('');
				if (!habilita){
				    $("#valor"+index).attr('readonly','readonly');
				}
				else{
					$('#valor'+index).removeAttr("readonly"); 
				}
			});	
			
		}

		function isAtributosMovimentoVazios(numeroCota, nomeCota, data, valor, observacao) {

			if (!$.trim(numeroCota) 
					&& !$.trim(nomeCota)
					&& !$.trim(data) 
					&& !$.trim(valor)
					&& !$.trim(observacao)) {

				return true;
			}
		}
		
		function obterInformacoesParaLancamento(){

			var idBox = $("#novoBox").val();
			var idRoteiro = $("#novoRoteiro").val();
			var idRota = $("#novoRota").val();
			var grupoMovimento = $("#grupoMovimentoHidden").val();
			var percentual = $("#novoPercentual").val();
			var baseCalculo = $("#novoBaseCalculo").val();
			var dataPeriodoInicial = $("#novoDataPeriodoDe").val();
			var dataPeriodoFinal = $("#novoDataPeriodoAte").val();
			
			$(".debitosCreditosGrid_1").flexOptions({
				url: "<c:url value='/financeiro/debitoCreditoCota/obterInformacoesParaLancamento' />",
				params: [{name:'idBox', value:idBox},
				         {name:'idRoteiro', value:idRoteiro},
				         {name:'idRota', value:idRota},
				         {name:'grupoMovimento', value:grupoMovimento},
				         {name:'percentual', value:percentual},
				         {name:'baseCalculo', value:baseCalculo},
				         {name:'dataPeriodoInicial', value:dataPeriodoInicial},
				         {name:'dataPeriodoFinal', value:dataPeriodoFinal}],
				        newp: 1
			},
			null,
			null,
			true);

			/*RECARREGA GRID CONFORME A EXECUCAO DO METODO COM OS PARAMETROS PASSADOS*/
			$(".debitosCreditosGrid_1").flexReload();

			$(".grids").show();
		}

		function obterInformacoesParaLancamentoIndividual(numeroCota,indexValor){

			var grupoMovimento = $("#grupoMovimentoHidden").val();
			var percentual = $("#novoPercentual").val();
			var baseCalculo = $("#novoBaseCalculo").val();
			var dataPeriodoInicial = $("#novoDataPeriodoDe").val();
			var dataPeriodoFinal = $("#novoDataPeriodoAte").val();

			if (grupoMovimento == "DEBITO_SOBRE_FATURAMENTO"){

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
								$("#valor"+indexValor).val(result.valor);
							},
							null,
							true);
				
			}	
		}
		
		//VERIFICA SE O TIPO DE LANÇAMENTO CONSIDERA FATURAMENTO DA COTA
		function configuraTelaLancamento(idTipoLancamento){
			var data = [{name: 'idTipoMovimento', value: idTipoLancamento}];
			$.postJSON("<c:url value='/financeiro/debitoCreditoCota/obterGrupoFaturamento' />",
					   data,
					   sucessCallbackConfiguraTelaLancamento,
					   null,
					   true);
		}
		
		function sucessCallbackConfiguraTelaLancamento(result){
			
			$("#grupoMovimentoHidden").val(result);
			
			if (result=='DEBITO_SOBRE_FATURAMENTO'){
				$('#tabelaFaturamento').show();
				$("#novoValor").val('');
				$('#tituloNovoValor').hide();
				$('#novoValor').hide();
				
				habilitaValor(false); 
			}
			else{
				$('#tabelaFaturamento').hide();
				$('#tituloNovoValor').show();
				$('#novoValor').show();
				
				$("#novoBaseCalculo").val(0);
				$("#novoPercentual").val('');
				$("#novoDataPeriodoDe").val('');
				$("#novoDataPeriodoAte").val('');
				
				habilitaValor(true);
			}

		}
		
		function montarComboBox(result,name,onChange,selected) {

			var options = "";
			
			options += "<select name='"+name+"' id='"+name+"' style='width:150px;' onchange='"+onChange+"'>";
			options += "<option value='0'></option>";
			$.each(result, function(index, row) {
				if (selected == row.key.$){
				    options += "<option selected='true' value='" + row.key.$ + "'>"+ row.value.$ +"</option>";	
				}
				else{
					options += "<option value='" + row.key.$ + "'>"+ row.value.$ +"</option>";	
				}
			});
			options += "</select>";
			
			return options;
		}
		
		function carregarRoteiros(idBox,selected){
			var data = [{name: 'idBox', value: idBox}];
			$.postJSON("<c:url value='/financeiro/debitoCreditoCota/obterRoteirosBox' />",
					   data,
					   function(resultado){
				           criaComponenteRoteiros(resultado,selected)
			           }
					   ,
					   null,
					   true);
		}
		
		function criaComponenteRoteiros(result,selected) {
		    var comboRoteiros =  montarComboBox(result,"novoRoteiro","carregarRotas(this.value,0);",selected);
			$("#roteirosBox").html(comboRoteiros);
		}
		
		function carregarRotas(idRoteiro,selected){
			var data = [{name: 'idRoteiro', value: idRoteiro}];
			$.postJSON("<c:url value='/financeiro/debitoCreditoCota/obterRotasRoteiro' />",
					   data,
					   function(resultado){
				           criaComponenteRotas(resultado,selected)
			           }
					   ,
					   null,
					   true);
		}
		
		function criaComponenteRotas(result,selected) {
		    var comboRotas =  montarComboBox(result,"novoRota","",selected);
			$("#rotasRoteiro").html(comboRotas);
		}

		//SELECIONAR TODOS OS LANÇAMENTOS
		function selecionarTodos(checked){
			
			for (var i=0;i<document.formularioListaLancamentos.elements.length;i++) {
			     var x = document.formularioListaLancamentos.elements[i];
			     if (x.name == 'debitoCredito.checkboxGrid') {
			    	 x.checked = checked;
			     }    
			}
			
			if (checked){
				var elem = document.getElementById("textoSelTodos");
				elem.innerHTML = "Desmarcar todos";
            }
			
			else{
				var elem = document.getElementById("textoSelTodos");
				elem.innerHTML = "Marcar todos";
			}
		}
	
	</script>
	
</div>