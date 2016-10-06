var novoDialogDebitoCreditoCotaController = $.extend(true, {
	pesquisaCotaDebitoCreditoCota : null,
    init : function(varPesquisaCotaDebitoCreditoCota) {
    	pesquisaCotaDebitoCreditoCota = varPesquisaCotaDebitoCreditoCota;
    	$( "#novoDataPeriodoDe", novoDialogDebitoCreditoCotaController.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#novoDataPeriodoAte", novoDialogDebitoCreditoCotaController.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#novoDataVencimento", novoDialogDebitoCreditoCotaController.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$("#novoPercentual", novoDialogDebitoCreditoCotaController.workspace).numeric();

		$("input[id^='novoValor']", novoDialogDebitoCreditoCotaController.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		});

		$("#novoDataPeriodoDe", novoDialogDebitoCreditoCotaController.workspace).mask("99/99/9999");
		$("#novoDataPeriodoAte", novoDialogDebitoCreditoCotaController.workspace).mask("99/99/9999");
		$("#novoDataVencimento", novoDialogDebitoCreditoCotaController.workspace).mask("99/99/9999");
		
		novoDialogDebitoCreditoCotaController.carregarRoteiros(undefined,0);
		novoDialogDebitoCreditoCotaController.carregarRotas(0,0);

		$(".debitosCreditosGrid_1", novoDialogDebitoCreditoCotaController.workspace).flexigrid({
			preProcess: novoDialogDebitoCreditoCotaController.executarPreProcessamentoMovimento,
			onSuccess: novoDialogDebitoCreditoCotaController.configurarCampos,
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
				width : 260,
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
			width : 800,
			height : 220
		});

    },

	configurarCampos : function() {
		
		$("input[id^='data']", novoDialogDebitoCreditoCotaController.workspace).datepicker();
		
		$("input[id^='data']", novoDialogDebitoCreditoCotaController.workspace).mask("99/99/9999");
		
		$("input[id^='valor']", novoDialogDebitoCreditoCotaController.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		});
		
		$("input[id^='numeroCota']", novoDialogDebitoCreditoCotaController.workspace).numeric();
		
		$("input[name='debitoCredito.nomeCota']", novoDialogDebitoCreditoCotaController.workspace).autocomplete({source: ""});
		
	},

	popupNovoDialog : function() {
		
		$('#tabelaFaturamento', novoDialogDebitoCreditoCotaController.workspace).hide();
		$('#tituloNovoValor', novoDialogDebitoCreditoCotaController.workspace).show();
		$('#novoValor', novoDialogDebitoCreditoCotaController.workspace).show();
		
		$("#novoTipoMovimento", novoDialogDebitoCreditoCotaController.workspace).val(0);
		$("#grupoMovimentoHidden", novoDialogDebitoCreditoCotaController.workspace).val('');
		$("#novoBaseCalculo", novoDialogDebitoCreditoCotaController.workspace).val(0);
		$("#novoBox", novoDialogDebitoCreditoCotaController.workspace).val(0);
		$("#novoRoteiro", novoDialogDebitoCreditoCotaController.workspace).val(0);
		$("#novoRota", novoDialogDebitoCreditoCotaController.workspace).val(0);
		
		$("#novoPercentual", novoDialogDebitoCreditoCotaController.workspace).val('');
		$("#novoDataVencimento", novoDialogDebitoCreditoCotaController.workspace).val('');
		$("#novoDataPeriodoDe", novoDialogDebitoCreditoCotaController.workspace).val('');
		$("#novoDataPeriodoAte", novoDialogDebitoCreditoCotaController.workspace).val('');
		$("#novoValor", novoDialogDebitoCreditoCotaController.workspace).val('');
		$("#novoObservacao", novoDialogDebitoCreditoCotaController.workspace).val('');

		$("#debitosCreditosGrid_1", novoDialogDebitoCreditoCotaController.workspace).flexOptions({
			url : contextPath + '/financeiro/debitoCreditoCota/novoMovimento', 
		});
		
		$("#debitosCreditosGrid_1", novoDialogDebitoCreditoCotaController.workspace).flexReload();
		
		var closeEnable = false;
		
		$("#dialog-novo", novoDialogDebitoCreditoCotaController.workspace).dialog({
			resizable: false,
			height:570,
			width:880,
			modal: true,
			buttons:[ 
			          {
				           id:"bt_confirmar",
				           text:"Confirmar", 
				           click: function() {
				        	   closeEnable = true;
				        	   debitoCreditoCotaController.salvarMovimentoFinanceiro();
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
				    return !novoDialogDebitoCreditoCotaController.existMovimentos();
				}
				
				clearMessageDialogTimeout();
			},
			form: $("#form-dialog-novo", novoDialogDebitoCreditoCotaController.workspace)
		});     
	},
	
	obterVencimentoLancamentoAnterior : function(index){
		if (index > 0){
			$("#data" + (index), novoDialogDebitoCreditoCotaController.workspace).val($("#data" + (index-1), novoDialogDebitoCreditoCotaController.workspace).val());
		}    
	},
	
	executarPreProcessamentoMovimento : function(resultado) {

		if (resultado.mensagens) {

			exibirMensagemDialog(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens,
				""
			);

			return resultado;
		}

		var grupoMovimento = $("#grupoMovimentoHidden", novoDialogDebitoCreditoCotaController.workspace).val();
		
		$.each(resultado.rows, function(index, row) {

			var vencimento = $("#novoDataVencimento", novoDialogDebitoCreditoCotaController.workspace).val();
			var observacao = $("#novoObservacao", novoDialogDebitoCreditoCotaController.workspace).val();
			var numCota='';
			if (row.cell.numeroCota!=null){
				numCota = row.cell.numeroCota;
			}
			var nomeCota='';
			if (row.cell.nomeCota!=null){
				nomeCota = row.cell.nomeCota;
			}
			var valor = $("#novoValor", novoDialogDebitoCreditoCotaController.workspace).val();
			if (row.cell.valor!=null){
				valor = row.cell.valor;
			}
			
			var hiddenId = '<input type="hidden" name="idMovimento" value="' + index + '" />';
			
			var parametroPesquisaCota = '\'#numeroCota' + index + '\', \'#nomeCota' + index + '\', true';

			var parametroAutoCompleteCota = '\'#nomeCota' + index + '\', true';
			
			var inputNumeroCota = '<input id="numeroCota' + index + '" value="' + numCota + '" onkeypress="novoDialogDebitoCreditoCotaController.checarCheckbox(' + index + ')" maxlength="9" name="debitoCredito.numeroCota" type="text" style="width:60px; float:left; margin-right:5px;" onchange="pesquisaCotaDebitoCreditoCota.pesquisarPorNumeroCota(' + parametroPesquisaCota + '); novoDialogDebitoCreditoCotaController.obterInformacoesParaLancamentoIndividual(this.value, '+index+'); novoDialogDebitoCreditoCotaController.obterVencimentoLancamentoAnterior('+index+'); " />';

			var inputNomeCota = '<input id="nomeCota' + index + '" value="' + nomeCota+ '" onkeypress="novoDialogDebitoCreditoCotaController.checarCheckbox(' + index + ')" name="debitoCredito.nomeCota" type="text" style="width:180px;" onkeyup="pesquisaCotaDebitoCreditoCota.autoCompletarPorNome(' + parametroAutoCompleteCota + ');" onchange="pesquisaCotaDebitoCreditoCota.pesquisarPorNomeCota(' + parametroPesquisaCota + ')" />';
			
			var inputData = '<input id="data' + index + '" value="' + vencimento + '" onkeypress="novoDialogDebitoCreditoCotaController.checarCheckbox(' + index + ')" onchange="novoDialogDebitoCreditoCotaController.checarCheckbox(' + index + ')" name="debitoCredito.dataVencimento" type="text" style="width:70px;" />';
			
			var inputValor = '<input id="valor' + index + '" value="' + valor + '" onkeypress="novoDialogDebitoCreditoCotaController.checarCheckbox(' + index + ')" name="debitoCredito.valor" type="text" style="width:80px;" />';
			if (grupoMovimento=="DEBITO_SOBRE_FATURAMENTO"){
				inputValor = '<input readonly="readonly" id="valor' + index + '" value="' + valor + '" name="debitoCredito.valor" type="text" style="width:80px;" />';
			}
			
			var inputObservacao = '<input id="observacao' + index + '" value="' + observacao + '" onkeypress="novoDialogDebitoCreditoCotaController.checarCheckbox(' + index + ')" name="debitoCredito.observacao" type="text" style="width:220px;" />';

			var checkBox = '<input id="checkbox'+ index +'" title="Selecionar Lançamento"'+ 
			(row.cell.valor ? ' checked="checked" ' : '')
			+' type="checkbox" name="debitoCredito.checkboxGrid" />';

			row.cell[0] = hiddenId + inputNumeroCota;
			row.cell[1] = inputNomeCota;
			row.cell[2] = inputData;
			row.cell[3] = inputValor;
			row.cell[4] = inputObservacao;
			row.cell[5] = checkBox;
			
		});
		
		$('selTodos', novoDialogDebitoCreditoCotaController.workspace).attr('checked', false);
		//document.getElementById("selTodos").checked = false;
		novoDialogDebitoCreditoCotaController.selecionarTodos(false);
		
		return resultado;
	},

	checarCheckbox : function(index) {
		$("#checkbox" + index, novoDialogDebitoCreditoCotaController.workspace).attr("checked", true);
	},
	
	tratarErroCadastroNovosMovimentos : function(jsonData) {

		if (!jsonData || !jsonData.mensagens) {

			return;
		}

		var dadosValidacao = jsonData.mensagens.dados;
		
		var linhasDaGrid = $(".debitosCreditosGrid_1 tr", novoDialogDebitoCreditoCotaController.workspace);

		$.each(linhasDaGrid, function(index, value) {

			var linha = $(value);

			if (dadosValidacao 
					&& ($.inArray(index, dadosValidacao) > -1)) {

				linha.removeClass('erow').addClass('linhaComErro');
				
			} else {

				linha.removeClass('linhaComErro');					
			}
		});
	},
	
	obterListaMovimentos : function() {


		var linhasDaGrid = $(".debitosCreditosGrid_1 tr", novoDialogDebitoCreditoCotaController.workspace);

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
				$(colunaNumeroCota).find("div").find('input[name="debitoCredito.numeroCota"]', novoDialogDebitoCreditoCotaController.workspace).val();
			
			var nomeCota = 
				$(colunaNomeCota).find("div").find('input[name="debitoCredito.nomeCota"]', novoDialogDebitoCreditoCotaController.workspace).val();

			var data = 
				$(colunaData).find("div").find('input[name="debitoCredito.dataVencimento"]', novoDialogDebitoCreditoCotaController.workspace).val();

			var valor =
				$(colunaValor).find("div").find('input[name="debitoCredito.valor"]', novoDialogDebitoCreditoCotaController.workspace).val();
			
			var observacao =
				$(colunaObservacao).find("div").find('input[name="debitoCredito.observacao"]', novoDialogDebitoCreditoCotaController.workspace).val();
			
			var idMovimento =
				$(colunaNumeroCota).find("div").find('input[name="idMovimento"]', novoDialogDebitoCreditoCotaController.workspace).val();
			
			if (novoDialogDebitoCreditoCotaController.isAtributosMovimentoVazios(numeroCota, nomeCota, data, valor, observacao)) {

				return true;
			}

			var checked = $("#checkbox"+index, novoDialogDebitoCreditoCotaController.workspace).is(':checked');
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
	},

	existMovimentos : function() {

		var linhasDaGrid = $(".debitosCreditosGrid_1 tr", novoDialogDebitoCreditoCotaController.workspace);
		
		var isMovimentos = false;

		$.each(linhasDaGrid, function(index, value) {

			var linha = $(value);
			
			var colunaNumeroCota = linha.find("td")[0];
			var colunaNomeCota = linha.find("td")[1];
			var colunaData = linha.find("td")[2];
			var colunaValor = linha.find("td")[3];
			var colunaObservacao = linha.find("td")[4];
			
			var numeroCota = 
				$(colunaNumeroCota).find("div").find('input[name="debitoCredito.numeroCota"]', novoDialogDebitoCreditoCotaController.workspace).val();
			
			var nomeCota = 
				$(colunaNomeCota).find("div").find('input[name="debitoCredito.nomeCota"]', novoDialogDebitoCreditoCotaController.workspace).val();

			var data = 
				$(colunaData).find("div").find('input[name="debitoCredito.dataVencimento"]', novoDialogDebitoCreditoCotaController.workspace).val();

			var valor =
				$(colunaValor).find("div").find('input[name="debitoCredito.valor"]', novoDialogDebitoCreditoCotaController.workspace).val();
			
			var observacao =
				$(colunaObservacao).find("div").find('input[name="debitoCredito.observacao"]', novoDialogDebitoCreditoCotaController.workspace).val();

			
			if (!novoDialogDebitoCreditoCotaController.isAtributosMovimentoVazios(numeroCota, nomeCota, data, valor, observacao)) {
				isMovimentos = true;
				return isMovimentos;
			}

		});

		return isMovimentos;
	},
	
	habilitaValor : function(habilita){
		
        var linhasDaGrid = $(".debitosCreditosGrid_1 tr", novoDialogDebitoCreditoCotaController.workspace);
        
		$.each(linhasDaGrid, function(index, value) {
			if (!habilita){
			    $("#valor"+index, novoDialogDebitoCreditoCotaController.workspace).attr('readonly','readonly');
			}
			else{
				$('#valor'+index, novoDialogDebitoCreditoCotaController.workspace).removeAttr("readonly"); 
			}
		});	
		
	},

	isAtributosMovimentoVazios : function(numeroCota, nomeCota, data, valor, observacao) {

		if (!$.trim(numeroCota) 
				&& !$.trim(nomeCota)
				&& !$.trim(data) 
				&& !$.trim(valor)
				&& !$.trim(observacao)) {

			return true;
		}
	},
	
	obterInformacoesParaLancamento : function(){

		var idBox = $("#novoBox", novoDialogDebitoCreditoCotaController.workspace).val();
		var idRoteiro = $("#novoRoteiro", novoDialogDebitoCreditoCotaController.workspace).val();
		var idRota = $("#novoRota", novoDialogDebitoCreditoCotaController.workspace).val();
		var grupoMovimento = $("#grupoMovimentoHidden", novoDialogDebitoCreditoCotaController.workspace).val();
		var percentual = $("#novoPercentual", novoDialogDebitoCreditoCotaController.workspace).val();
		var baseCalculo = $("#novoBaseCalculo", novoDialogDebitoCreditoCotaController.workspace).val();
		var dataPeriodoInicial = $("#novoDataPeriodoDe", novoDialogDebitoCreditoCotaController.workspace).val();
		var dataPeriodoFinal = $("#novoDataPeriodoAte", novoDialogDebitoCreditoCotaController.workspace).val();
		
		$(".debitosCreditosGrid_1", novoDialogDebitoCreditoCotaController.workspace).flexOptions({
			url: contextPath + "/financeiro/debitoCreditoCota/obterInformacoesParaLancamento",
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
		$(".debitosCreditosGrid_1", novoDialogDebitoCreditoCotaController.workspace).flexReload();

		$(".grids", novoDialogDebitoCreditoCotaController.workspace).show();
	},

	obterInformacoesParaLancamentoIndividual : function(numeroCota,indexValor){

		var grupoMovimento = $("#grupoMovimentoHidden", novoDialogDebitoCreditoCotaController.workspace).val();
		var percentual = $("#novoPercentual", novoDialogDebitoCreditoCotaController.workspace).val();
		var baseCalculo = $("#novoBaseCalculo", novoDialogDebitoCreditoCotaController.workspace).val();
		var dataPeriodoInicial = $("#novoDataPeriodoDe", novoDialogDebitoCreditoCotaController.workspace).val();
		var dataPeriodoFinal = $("#novoDataPeriodoAte", novoDialogDebitoCreditoCotaController.workspace).val();

		if (grupoMovimento == "DEBITO_SOBRE_FATURAMENTO"){

			var data = [{name:'numeroCota', value:numeroCota},
				        {name:'grupoMovimento', value:grupoMovimento},
				        {name:'percentual', value:percentual},
				        {name:'baseCalculo', value:baseCalculo},
				        {name:'dataPeriodoInicial', value:dataPeriodoInicial},
				        {name:'dataPeriodoFinal', value:dataPeriodoFinal},
				        {name:'index', value:indexValor}];
			
			$.postJSON(contextPath + "/financeiro/debitoCreditoCota/obterInformacoesParaLancamentoIndividual",
					    data,
						function(result){
							$("#valor"+indexValor, novoDialogDebitoCreditoCotaController.workspace).val(result.valor);
						},
						null,
						true);
			
		}	
	},
	
	//VERIFICA SE O TIPO DE LANÇAMENTO CONSIDERA FATURAMENTO DA COTA
	configuraTelaLancamento : function(idTipoLancamento){
		var data = [{name: 'idTipoMovimento', value: idTipoLancamento}];
		$.postJSON(contextPath + "/financeiro/debitoCreditoCota/obterGrupoFaturamento",
				   data,
				   novoDialogDebitoCreditoCotaController.sucessCallbackConfiguraTelaLancamento,
				   null,
				   true);
	},
	
	sucessCallbackConfiguraTelaLancamento : function(result){
		
		$("#grupoMovimentoHidden", novoDialogDebitoCreditoCotaController.workspace).val(result);
		
		if (result=='DEBITO_SOBRE_FATURAMENTO' || result=='CREDITO_SOBRE_FATURAMENTO'){
			$('#tabelaFaturamento', novoDialogDebitoCreditoCotaController.workspace).show();
			$("#novoValor", novoDialogDebitoCreditoCotaController.workspace).val('');
			$('#tituloNovoValor', novoDialogDebitoCreditoCotaController.workspace).hide();
			$('#novoValor', novoDialogDebitoCreditoCotaController.workspace).hide();
			
			novoDialogDebitoCreditoCotaController.habilitaValor(false); 
		}
		else{
			$('#tabelaFaturamento', novoDialogDebitoCreditoCotaController.workspace).hide();
			$('#tituloNovoValor', novoDialogDebitoCreditoCotaController.workspace).show();
			$('#novoValor', novoDialogDebitoCreditoCotaController.workspace).show();
			
			$("#novoBaseCalculo", novoDialogDebitoCreditoCotaController.workspace).val(0);
			$("#novoPercentual", novoDialogDebitoCreditoCotaController.workspace).val('');
			$("#novoDataPeriodoDe", novoDialogDebitoCreditoCotaController.workspace).val('');
			$("#novoDataPeriodoAte", novoDialogDebitoCreditoCotaController.workspace).val('');
			
			novoDialogDebitoCreditoCotaController.habilitaValor(true);
		}

	},
	
	montarComboBox : function(result,name,onChange,selected) {

		var options = "";
		
		options += "<select name='"+name+"' id='"+name+"' style='width:150px;' onchange='"+onChange+"'>";
		options += "<option value=''>Todos</option>";
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
	},
	
	carregarRoteiros : function(idBox,selected){
		var data = [{name: 'idBox', value: idBox}];
		$.postJSON(contextPath + "/financeiro/debitoCreditoCota/obterRoteirosBox",
				   data,
				   function(resultado){
				   		novoDialogDebitoCreditoCotaController.criaComponenteRoteiros(resultado,selected)
		           }
				   ,
				   null,
				   true);
	},
	
	criaComponenteRoteiros : function(result,selected) {
	    var comboRoteiros =  novoDialogDebitoCreditoCotaController.montarComboBox(result,"novoRoteiro","novoDialogDebitoCreditoCotaController.carregarRotas(this.value,0);",selected);
		$("#roteirosBox", novoDialogDebitoCreditoCotaController.workspace).html(comboRoteiros);
	},
	
	carregarRotas : function(idRoteiro,selected){
		var data = [{name: 'idRoteiro', value: idRoteiro}];
		$.postJSON(contextPath + "/financeiro/debitoCreditoCota/obterRotasRoteiro",
				   data,
				   function(resultado){
				   		novoDialogDebitoCreditoCotaController.criaComponenteRotas(resultado,selected)
		           }
				   ,
				   null,
				   true);
	},
	
	criaComponenteRotas : function(result,selected) {
	    var comboRotas =  novoDialogDebitoCreditoCotaController.montarComboBox(result,"novoRota","",selected);
		$("#rotasRoteiro", novoDialogDebitoCreditoCotaController.workspace).html(comboRotas);
	},

	//SELECIONAR TODOS OS LANÇAMENTOS
	selecionarTodos : function(checked){
		
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

}, BaseController);
//@ sourceURL=novoDialogDebitoCreditoCota.js
