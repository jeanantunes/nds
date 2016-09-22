var GerarBoletoAvulsoController = $.extend(true, {
	
	itensBoletoAvulso : [],
	
	pesquisaCotaDebitoCreditoCota : null,
	
	init : function() {
		var params = [];
		this.initInputs();
		this.initGrids();
		this.initFiltroDatas();
		this.pesquisaCota();
	},
	
	initInputs : function() {
		
		$( "#boleto-avulso-dataVencimento", GerarBoletoAvulsoController.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$("#boleto-avulso-dataVencimento", GerarBoletoAvulsoController.workspace).mask("99/99/9999");

		$("#boleto-avulso-valor", GerarBoletoAvulsoController.workspace).val(0);
		$("input[id^='boleto-avulso-valor']", GerarBoletoAvulsoController.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		});
	},
	
	initGrids : function() {
		$(".boleto-avulso-Grid_1", GerarBoletoAvulsoController.workspace).flexigrid({
			preProcess: GerarBoletoAvulsoController.executarPreProcessamentoGrid,
			onSuccess: GerarBoletoAvulsoController.configurarCampos,
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
				display : 'Banco',
				name : 'data',
				width : 220,
				sortable : false,
				align : 'center'
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
				width : 150,
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
			width : 950,
			height : 220
		});
		
		$("#boleto-avulso-Grid_1", GerarBoletoAvulsoController.workspace).flexReload();
	},
	
	configurarCampos : function() {
		

		$("input[id^='data']", GerarBoletoAvulsoController.workspace).datepicker();
		
		$("input[id^='data']", GerarBoletoAvulsoController.workspace).mask("99/99/9999");
		
		$("input[id^='valor']", GerarBoletoAvulsoController.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		});
		
		$("input[id^='numeroCota']", GerarBoletoAvulsoController.workspace).numeric();
		
		$("input[name='boletoAvulso.nomeCota']", GerarBoletoAvulsoController.workspace).autocomplete({source: ""});
		
	},
	
	initFiltroDatas : function(){
		
	    $.postJSON(contextPath + '/cadastro/distribuidor/obterDataDistribuidor',
				null, 
				function(result) {
					$("#boleto-avulso-dataVencimento", this.workspace).val(result);
		        }
		); 
	},
	
	pesquisaCota : function(varPesquisaCotaDebitoCreditoCota) {
    	pesquisaCotaDebitoCreditoCota = varPesquisaCotaDebitoCreditoCota;
	},	
	
	executarPreProcessamentoGrid : function(resultado) {

		if (resultado.mensagens) {

			exibirMensagemDialog(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens,
				""
			);

			return resultado;
		}
		
		$.each(resultado.rows, function(index, row) {

			var vencimento = $("#boleto-avulso-dataVencimento", GerarBoletoAvulsoController.workspace).val();
			
			var observacao = $("#boleto-avulso-observacao", GerarBoletoAvulsoController.workspace).val();
			
			var numCota='';
			if (row.cell.numeroCota!=null){
				numCota = row.cell.numeroCota;
			}
			
			var nomeCota='';
			if (row.cell.nomeCota!=null){
				nomeCota = row.cell.nomeCota;
			}
			
			var valor = $("#boleto-avulso-valor", GerarBoletoAvulsoController.workspace).val();
			if (row.cell.valor!=null){
				valor = row.cell.valor;
			}
			
			var optionBanco = "";
			
			if (row.cell.bancos!=null){
				$.each(row.cell.bancos, function(index, ban) {
					optionBanco += '<option value="'+ban.key.$+'">'+ban.value.$+'</option>'
				});
			}
			
			var dataVencimento = "";
			if (row.cell.dataVencimento!=null){
				vencimento = row.cell.dataVencimento;
			}
			
			if (row.cell.observacao!=null){
				observacao = row.cell.observacao;
			}
			
			var hiddenId = '<input type="hidden" name="idMovimento" value="' + index + '" />';
			
			var parametroPesquisaCota = '\'#numeroCota' + index + '\', \'#nomeCota' + index + '\', true';

			var parametroAutoCompleteCota = '\'#nomeCota' + index + '\', true';
			
			var inputNumeroCota = '<input id="numeroCota' + index + '" value="' + numCota + '" onkeypress="GerarBoletoAvulsoController.checarCheckbox(' + index + ')" maxlength="9" name="boletoAvulso.numeroCota" type="text" style="width:60px; float:left; margin-right:5px;" onchange="pesquisaCotaDebitoCreditoCota.pesquisarPorNumeroCota(' + parametroPesquisaCota + ');" />';

			var inputNomeCota = '<input id="nomeCota' + index + '" value="' + nomeCota+ '" name="boletoAvulso.nomeCota" onkeypress="GerarBoletoAvulsoController.checarCheckbox(' + index + ')" name="boletoAvulso.nomeCota" type="text" style="width:180px;" onkeyup="pesquisaCotaDebitoCreditoCota.autoCompletarPorNome(' + parametroAutoCompleteCota + ');" onblur="pesquisaCotaDebitoCreditoCota.pesquisarPorNomeCota(' + parametroPesquisaCota + ')" />';
			
			var comboBanco = '<select id="comboBanco'+ index +'" name="boletoAvulso.comboBanco">'+ optionBanco +'</select>'
			
			var inputData = '<input id="data' + index + '" value="' + vencimento + '" onkeypress="GerarBoletoAvulsoController.checarCheckbox(' + index + ')" onchange="GerarBoletoAvulsoController.checarCheckbox(' + index + ')" name="boletoAvulso.dataVencimento" type="text" style="width:70px;" />';
			
			var inputValor = '<input id="valor' + index + '" value="' + valor + '" onkeypress="GerarBoletoAvulsoController.checarCheckbox(' + index + ')" name="boletoAvulso.valor" type="text" style="width:80px;" />';
			
			var inputObservacao = '<input id="observacao' + index + '" value="' + observacao + '" onkeypress="GerarBoletoAvulsoController.checarCheckbox(' + index + ')" name="boletoAvulso.observacao" type="text" style="width:150px;" />';

			var checkBox = '<input id="checkbox'+ index +'" title="Selecionar Itens"'+(row.cell.valor ? '' : '')+' type="checkbox" name="boletoAvulso.checkboxAvulsoGrid" />';
			
			row.cell[0] = hiddenId + inputNumeroCota;
			row.cell[1] = inputNomeCota;
			row.cell[2] = comboBanco;
			row.cell[3] = inputData;
			row.cell[4] = inputValor;
			row.cell[5] = inputObservacao;
			row.cell[6] = checkBox;
			
		});
		
		$('boleto-avulso-selTodos', GerarBoletoAvulsoController.workspace).attr('checked', false);
		GerarBoletoAvulsoController.selecionarTodos(false);
		
		return resultado;
	},
	
	checarCheckbox : function(index) {
		$("#checkbox" + index, GerarBoletoAvulsoController.workspace).attr("checked", true);
	},
	
	obterInformacoesParaBoleto : function(){
		
		var parametros = [{name:'filtro.numeroCota', value:$('#boletoAvulsoEdicaoNumeroCota', GerarBoletoAvulsoController.workspace).val()},
		                  {name:'filtro.idRegiao', value:$('#boleto-avulso-idRegiao', GerarBoletoAvulsoController.workspace).val()},
				          {name:'filtro.idBox', value:$('#boleto-avulso-idBox option:selected', GerarBoletoAvulsoController.workspace).val()},
				          {name:'filtro.idRoteiro', value:$('#boleto-avulso-idRoteiro option:selected', GerarBoletoAvulsoController.workspace).val()},
				          {name:'filtro.idRota', value:$('#boleto-avulso-idRota option:selected', GerarBoletoAvulsoController.workspace).val()},
				          {name:'filtro.idBanco', value:$('#boleto-avulso-idBanco', GerarBoletoAvulsoController.workspace).val()},
				          {name:'filtro.dataVencimento', value:$('#boleto-avulso-dataVencimento', GerarBoletoAvulsoController.workspace).val()},
				          {name:'filtro.valor', value:$('#boleto-avulso-valor', GerarBoletoAvulsoController.workspace).val()},
				          {name:'filtro.observacao', value:$('#boleto-avulso-observacao', GerarBoletoAvulsoController.workspace).val()}
				          ];
		
		$(".boleto-avulso-Grid_1", GerarBoletoAvulsoController.workspace).flexOptions({
			url: contextPath + "/financeiro/boletoAvulso/obterInformacoesParaBoleto",
			params: parametros
				, newp: 1
		},
		
		null,
		null,
		true);

		/*RECARREGA GRID CONFORME A EXECUCAO DO METODO COM OS PARAMETROS PASSADOS*/
		$(".boleto-avulso-Grid_1", GerarBoletoAvulsoController.workspace).flexReload();

		$(".grids", GerarBoletoAvulsoController.workspace).show();
	},
	
	/**
	 * Recarregar combos por Box
	 */
    changeBox : function() {
		
    	var boxDe = $("#boleto-avulso-idBox").val();
    	
    	var boxAte = $("#boleto-avulso-idBox").val();
    	
    	var idRota = $("#boleto-avulso-idRota").val();
    	
    	var idRoteiro = $("#boleto-avulso-idRoteiro").val();
    	
    	var params = [{
			            name : "codigoBoxDe",
			            value : boxDe	
					  },{
						name : "codigoBoxAte",
						value : boxAte
					  }];
    	
    	$.postJSON(contextPath + '/cadastro/roteirizacao/carregarCombosPorBox', params, 
			function(result) {
    		
    		    var listaRota = result[0];
    		    
    		    var listaRoteiro = result[1];
    		    
    		    var listaBox = result[2];
    		
    		    GerarBoletoAvulsoController.recarregarCombo($("#boleto-avulso-idRota", GerarBoletoAvulsoController.workspace), listaRota ,idRota);
 		    
    		    GerarBoletoAvulsoController.recarregarCombo($("#boleto-avulso-idRoteiro", GerarBoletoAvulsoController.workspace), listaRoteiro ,idRoteiro); 
    		    
    		    GerarBoletoAvulsoController.recarregarCombo($("#boleto-avulso-idBox", GerarBoletoAvulsoController.workspace), listaBox ,boxDe);
    		    
    	    }    
		);
	},
	
	/**
	 * Recarregar combos por Rota
	 */
    changeRota : function() {
    	
        var boxDe = $("#boleto-avulso-idBox").val();
    	
    	var boxAte = $("#boleto-avulso-idBox").val();
    	
    	var idRota = $("#boleto-avulso-idRota").val();
    	
    	var idRoteiro = $("#boleto-avulso-idRoteiro").val();
    	
    	var params = [{
			            name : "idRota",
			            value : idRota	
					  }];
	    
    	$.postJSON(contextPath + '/cadastro/roteirizacao/carregarCombosPorRota', params, 
			function(result) {
    		
    		    var listaRoteiro = result[0];
    		 
    		    var listaBox = result[1];
    		    
    		    var listaRota = result[2];

    		    GerarBoletoAvulsoController.recarregarCombo($("#boleto-avulso-idBox", GerarBoletoAvulsoController.workspace), listaBox, boxDe);
 		    
    		    GerarBoletoAvulsoController.recarregarCombo($("#boleto-avulso-idBox", GerarBoletoAvulsoController.workspace), listaBox, boxAte);
 		    
    		    GerarBoletoAvulsoController.recarregarCombo($("#boleto-avulso-idRoteiro", GerarBoletoAvulsoController.workspace), listaRoteiro, idRoteiro); 
    		    
    		    GerarBoletoAvulsoController.recarregarCombo($("#boleto-avulso-idRota", GerarBoletoAvulsoController.workspace), listaRota, idRota);
    	    }    
		);
	},
	
	/**
	 * Recarregar combos por Roteiro
	 */
    changeRoteiro : function() {
    	
        var boxDe = $("#boleto-avulso-idBox").val();
    	
    	var boxAte = $("#boleto-avulso-idBox").val();
    	
    	var idRota = $("#boleto-avulso-idRota").val();
    	
    	var idRoteiro = $("#boleto-avulso-idRoteiro").val();
     	
     	var params = [{
				            name : "idRoteiro",
				            value : idRoteiro	
						  }];
     	
     	$.postJSON(contextPath + '/cadastro/roteirizacao/carregarCombosPorRoteiro', params, 
			function(result) {
    		
    		    var listaRota = result[0];
    		 
    		    var listaBox = result[1];
    		    
    		    var listaRoteiro = result[2];
 		    
    		    GerarBoletoAvulsoController.recarregarCombo($("#boleto-avulso-idRota", GerarBoletoAvulsoController.workspace), listaRota, idRota);  
    		    
    		    GerarBoletoAvulsoController.recarregarCombo($("#boleto-avulso-idBox", GerarBoletoAvulsoController.workspace), listaBox, boxDe);
     		    
    		    GerarBoletoAvulsoController.recarregarCombo($("#boleto-avulso-idBox", GerarBoletoAvulsoController.workspace), listaBox, boxAte);
    		    
    		    GerarBoletoAvulsoController.recarregarCombo($("#boleto-avulso-idRoteiro", GerarBoletoAvulsoController.workspace), listaRoteiro, idRoteiro); 
    	    }    
		);
	},
	
	/**
	 * Recarregar combo
	 */
	recarregarCombo : function (comboNameComponent, content, valSelected) {
		
		comboNameComponent.empty();

		comboNameComponent.append(new Option('Selecione...', '', true, true));
		
	    $.each(content, function(index, row) {
		    	
	    	comboNameComponent.append(new Option(row.value.$, row.key.$, true, true));
		});

	    if (valSelected) {
	    	
	        $(comboNameComponent).val(valSelected);
	    } else {
	    	
	        $(comboNameComponent).val('');
	    }
	},
	
	pesquisarCota : function(isModalAlteracao) {
 		
 		var numeroCota;
 		
 		if (isModalAlteracao) {
 			
 			numeroCota = $("#boletoAvulsoEdicaoNumeroCota", GerarBoletoAvulsoController.workspace).val();
 		
 		} else {
 			
 			numeroCota = $("#boleto-avulso-numeroCota", GerarBoletoAvulsoController.workspace).val();
 		}
 		
 		if(numeroCota == '') {
 			$("#edicaoNomeCota", GerarBoletoAvulsoController.workspace).val("");
 		} else {
 			$.postJSON(
 					contextPath + '/financeiro/debitoCreditoCota/buscarCotaPorNumero',
 					{ "numeroCota": numeroCota },
 					function(result) {

 						if (isModalAlteracao) {

 							$("#edicaoNomeCota", GerarBoletoAvulsoController.workspace).val(result);
 							
 						} else {

 							$("#nomeCota", GerarBoletoAvulsoController.workspace).html(result);
 						}
 					},
 					null,
 					true
 				);
 		}
 	},
	
	confirmar : function() {
		var parametros = []; 
		
		var itens = GerarBoletoAvulsoController.obterCotasSelecionadas();
		
		$.postJSON(contextPath + '/financeiro/boletoAvulso/gerarBoletoAvulsoCota',
			itens,
			 function(resultado) {
				$.fileDownload(contextPath +'/financeiro/boletoAvulso/imprimirBoleto', {
					httpMethod : "POST",
					data : itens,
					
					successCallback: function (url) {
				    	console.log('success');
				    	alert("caiu aqui");
				    },
				    failCallback: function (responseHtml, url) {
				        preparingFileModal.dialog('close');
				        $("#error-modal").dialog({ modal: true });
				    }
				});
				
			 },
			null,
			true
		);
		
	},
	
	//OBTEM OS CODIGOS DAS DIVIDAS MARCADAS
	obterCotasSelecionadas : function(){
			
		var linhasDaGrid = $(".boleto-avulso-Grid_1 tr", GerarBoletoAvulsoController.workspace);
		
		var listaCotaSelecionadas = [];
		
		$.each(linhasDaGrid, function(index, value) {

			var linha = $(value);
			
			var colunaNumeroCota = linha.find("td")[0];
			var colunaNomeCota = linha.find("td")[1];
			var colunaComboBanco = linha.find("td")[2];
			var colunaData = linha.find("td")[3];
			var colunaValor = linha.find("td")[4];
			var colunaObservacao = linha.find("td")[5];
			var colunaChecked = linha.find("td")[6];
			var colunaIdMovimento = linha.find("td")[7];
			
			var numeroCota = 
				$(colunaNumeroCota).find("div").find('input[name="boletoAvulso.numeroCota"]', GerarBoletoAvulsoController.workspace).val();
			
			var nomeCota = 
				$(colunaNomeCota).find("div").find('input[name="boletoAvulso.nomeCota"]', GerarBoletoAvulsoController.workspace).val();
			
			var idBanco = $('#comboBanco'+index, GerarBoletoAvulsoController.workspace).val();
			
			var data = 
				$(colunaData).find("div").find('input[name="boletoAvulso.dataVencimento"]', GerarBoletoAvulsoController.workspace).val();

			var valor =
				$(colunaValor).find("div").find('input[name="boletoAvulso.valor"]', GerarBoletoAvulsoController.workspace).val();
			
			var observacao =
				$(colunaObservacao).find("div").find('input[name="boletoAvulso.observacao"]', GerarBoletoAvulsoController.workspace).val();
			
			var idMovimento =
				$(colunaNumeroCota).find("div").find('input[name="idMovimento"]', GerarBoletoAvulsoController.workspace).val();
			
			var checked = $("#checkbox"+index, GerarBoletoAvulsoController.workspace).is(':checked');
			
			if (checked == true) {
				
				var movimento = 'listaBoletosAvulso[' + index + '].numeroCota=' + numeroCota + '&';

				movimento += 'listaBoletosAvulso[' + index + '].nomeCota=' + nomeCota + '&';
	
				movimento += 'listaBoletosAvulso[' + index + '].idBanco=' + idBanco + '&';
				
				movimento += 'listaBoletosAvulso[' + index + '].dataVencimento=' + data + '&';
	
				movimento += 'listaBoletosAvulso[' + index + '].valor=' + valor + '&';
				
				movimento += 'listaBoletosAvulso[' + index + '].observacao=' + observacao + '&';

				movimento += 'listaBoletosAvulso[' + index + '].id=' + idMovimento + '&';

				listaCotaSelecionadas = (listaCotaSelecionadas + movimento);
			}
		
		});

		return listaCotaSelecionadas;
	},
	
	//SELECIONAR TODOS OS ITENS DA GRID
	selecionarTodos : function(checked){
		
		for (var i=0;i<document.listaCotasBoletos.elements.length;i++) {
		     var x = document.listaCotasBoletos.elements[i];
		     if (x.name == 'boletoAvulso.checkboxAvulsoGrid') {
		    	 x.checked = checked;
		     }    
		}
		
		if (checked){
			var elem = document.getElementById("boleto-avulso-textoSelTodos");
			elem.innerHTML = "Desmarcar todos";
        } else {
			var elem = document.getElementById("boleto-avulso-textoSelTodos");
			elem.innerHTML = "Marcar todos";
		}
	},
	
	addLoteMix:function(){

		GerarBoletoAvulsoController.initGrids();
		
		$("#modalUploadArquivoMix").dialog({
			resizable: false,
			height:'auto',
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {
					GerarBoletoAvulsoController.executarSubmitArquivo();
					
				},
				"Cancelar": function() {
					$("#excelFile").val("");
					$(this).dialog("close");
				}
			},
		});
		
		/*RECARREGA GRID CONFORME A EXECUCAO DO METODO COM OS PARAMETROS PASSADOS*/
		$(".boleto-avulso-Grid_1", GerarBoletoAvulsoController.workspace).flexReload();

		$(".grids", GerarBoletoAvulsoController.workspace).show();
		
	},
	
	//submit do arquivo adicionar em lote
	executarSubmitArquivo:function(){
		
		var banco = $("#idBanco").val();
		
		var data = [];
		data.push({name: 'idBanco', value: banco});
		
		var fileName = $("#excelFile").val();
	      
	    var ext = fileName.substr(fileName.lastIndexOf(".")+1).toLowerCase();
	    
	    if(ext!="xls" & ext!="xlsx"){
	       exibirMensagem("WARNING", ["Somente arquivos com extensão .XLS ou .XLSX são permitidos."]);
	       $(this).val('');
	       return;
	    }else{
	    	   
	       $("#formUploadLoteMix").ajaxSubmit({
				
	    	   success: function(responseText, statusText, xhr, $form, result, data)  { 
					
					if(responseText === null) {
						exibirMensagemDialog("SUCCESS", ["Todo o arquivo foi importado com sucesso!"],"");
					}
						
					$('#modalUploadArquivoMix').dialog('close');
						
					/*RECARREGA GRID CONFORME A EXECUCAO DO METODO COM OS PARAMETROS PASSADOS*/
						
					$(".boleto-avulso-Grid_1").flexAddData(responseText);
						
					$(".boleto-avulso-Grid_1", GerarBoletoAvulsoController.workspace).flexReload();
						
				},
				
				type: 'POST',
				dataType: 'json',
				data : data,
			});
       }
	},
}, BaseController);
//@ sourceURL=boletoAvulso.js