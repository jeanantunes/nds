var movimentoFinanceiroCotaController = $.extend(true, {
	
	dataOperacaoDistribuidor: null,
	
	init : function() {
		
		$("#filtroNumCota", movimentoFinanceiroCotaController.workspace).numeric();
		
		$("#descricaoCota", movimentoFinanceiroCotaController.workspace).autocomplete({source: ""});

		$( "#dtPostergada", movimentoFinanceiroCotaController.workspace ).datepicker({
			
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		movimentoFinanceiroCotaController.dataOperacaoDistribuidor = $("#dataBaixa", movimentoFinanceiroCotaController.workspace).datepicker("getDate");
		
		movimentoFinanceiroCotaController.iniciarGridCotasAVista();
		
		//Era chamado essa função assim que carregava a tela, mas como agora vai ter filtros não será mais necessário chama-lá aqui.
		//movimentoFinanceiroCotaController.buscarCotas();
	},
	
	iniciarGridCotasAVista : function() {
		
		$(".gridCotas").flexigrid({
			preProcess: movimentoFinanceiroCotaController.getDataFromResult,
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 100,
				sortable : true,
				align : 'left'
			},{
				display : 'Nome',
				name : 'nomeCota',
				width : 250,
				sortable : true,
				align : 'left'
			},{
				display : 'Valor Consignado',
				name : 'valorConsignado',
				width : 100,
				sortable : false,
				align : 'left'
			},{
				display : 'Valor À Vista',
				name : 'valorAVista',
				width : 100,
				sortable : false,
				align : 'left'
			},{
				display : 'Débitos',
				name : 'debitos',
				width : 95,
				sortable : false,
				align : 'left'
			},{
				display : 'Créditos',
				name : 'creditos',
				width : 95,
				sortable : false,
				align : 'left'
			},{
				display : 'Saldo',
				name : 'saldo',
				width : 100,
				sortable : false,
				align : 'left'
			},{
				display : '',
				name : 'check',
				width : 20,
				sortable : false,
				align : 'left'
			} ],
			sortname : "numeroCota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 980,
			height : 440
		});
	},
	
	getDataFromResult : function(resultado) {	

		if (resultado.mensagens) {
			
			exibirMensagem(
					
				resultado.mensagens.tipoMensagem,
				
				resultado.mensagens.listaMensagens
			);
			
			$(".area", movimentoFinanceiroCotaController.workspace).hide();
			
			$(".grids", movimentoFinanceiroCotaController.workspace).hide();
			
			return resultado;
		}
		
		$("#selTodos", movimentoFinanceiroCotaController.workspace).attr("checked", false);
		
		movimentoFinanceiroCotaController.selecionarTodos(false);
		
		var totalDividas=0;
		
		$.each(resultado.rows, function(index, row) {
			
			valorSaldo = removeMascaraPriceFormat(row.cell.saldo);
			
			totalDividas = intValue(totalDividas) + intValue(valorSaldo);

			var checkBox;
			
			if (row.cell.check){
				
			    checkBox = '<input isEdicao="true" checked="' + row.cell.check + '" type="checkbox" name="checkboxGrid" id="checkbox_'+ row.cell.numeroCota +'" onchange="movimentoFinanceiroCotaController.calculaSelecionados(this.checked,'+ valorSaldo +'); dataHolder.hold(\'baixaManual\', '+ row.cell.numeroCota +', \'checado\', this.checked); " />';	
			    
			    movimentoFinanceiroCotaController.calculaSelecionados(true, valorSaldo);
			}
			else{
				
			    checkBox = '<input isEdicao="true" title="Selecionar Dívida" type="checkbox" name="checkboxGrid" id="checkbox_'+ row.cell.numeroCota +'" onchange="movimentoFinanceiroCotaController.calculaSelecionados(this.checked,'+ valorSaldo +'); dataHolder.hold(\'baixaManual\', '+ row.cell.numeroCota +', \'checado\', this.checked); " />';
			}
			
			if (valorSaldo!=0){
	
		        row.cell.check = checkBox;
			}
			else{
				
				row.cell.check = '<img title="Financeiro Processado" src="' + contextPath + '/images/ico_check.gif" border="0px" />'
			}
		});

		$("#totalDividasHidden", movimentoFinanceiroCotaController.workspace).val(totalDividas);
		
		$('#totalDividasHidden', movimentoFinanceiroCotaController.workspace).priceFormat({
			
			allowNegative: true,
			centsSeparator: ',',
		    thousandsSeparator: '.',
			centsLimit: 2
		});
		
		$("#totalDividas", movimentoFinanceiroCotaController.workspace).html($("#totalDividasHidden", movimentoFinanceiroCotaController.workspace).val());
		
		$(".area", movimentoFinanceiroCotaController.workspace).show();
		
		$(".grids", movimentoFinanceiroCotaController.workspace).show();
		
		return resultado;
	},
	
    buscarCotas : function(){
		
		var numeroCota = $("#filtroNumCota", movimentoFinanceiroCotaController.workspace).val();
		var codigoProduto = $("#processamentoFinanceiro-codigoProdutoEd", movimentoFinanceiroCotaController.workspace).val();
		var numeroEdicao = $("#processamentoFinanceiro-numEdicaoEd", movimentoFinanceiroCotaController.workspace).val();
					
		$(".gridCotas", movimentoFinanceiroCotaController.workspace).flexOptions({
			url: contextPath + "/financeiro/movimentoFinanceiroCota/buscarCotas",
			params: [
			         {name:'numeroCota', value: numeroCota},
			         {name:'codigoProduto', value: codigoProduto},
			         {name:'numeroEdicao', value: numeroEdicao}
			        ] ,
			        newp: 1
		});
		
		$(".gridCotas", movimentoFinanceiroCotaController.workspace).flexReload();
		
		$(".grids", movimentoFinanceiroCotaController.workspace).show();
	},
	
    calculaSelecionados : function(checked, valor) {
		
		var totalSelecionado = removeMascaraPriceFormat($("#totalDividasSelecionadasHidden", movimentoFinanceiroCotaController.workspace).val());
	    
		if(checked){
			
			totalSelecionado = intValue(totalSelecionado) + intValue(valor);
		}
		else{
			
			totalSelecionado = intValue(totalSelecionado) - intValue(valor);
		}

		$("#totalDividasSelecionadasHidden", movimentoFinanceiroCotaController.workspace).val(totalSelecionado);
		
		$("#totalDividasSelecionadasHidden", movimentoFinanceiroCotaController.workspace).priceFormat({
			allowNegative: true,
			centsSeparator: ',',
		    thousandsSeparator: '.',
			centsLimit: 2
		});
		
		$("#totalDividasSelecionadas", movimentoFinanceiroCotaController.workspace).html($("#totalDividasSelecionadasHidden", movimentoFinanceiroCotaController.workspace).val());
	},
	
    selecionarTodos : function(checked){
		
		for (var i=0;i<document.formularioListaCotas.elements.length;i++) {
		     var x = document.formularioListaCotas.elements[i];
		     if (x.name == 'checkboxGrid') {
		    	 x.checked = checked;
		     }    
		}
		
		if (checked){
			
			var elem = $("#textoSelTodos", movimentoFinanceiroCotaController.workspace);
			elem.innerHTML = "Desmarcar todos";
			
	        $("#totalDividasSelecionadas", movimentoFinanceiroCotaController.workspace).html($("#totalDividas").html());
		    $("#totalDividasSelecionadasHidden", movimentoFinanceiroCotaController.workspace).val($("#totalDividasHidden").val());
        }
		
		else{
			
			var elem = document.getElementById("textoSelTodos");
			elem.innerHTML = "Marcar todos";
			
			$("#totalDividasSelecionadas").html("0,00");
			$("#totalDividasSelecionadasHidden").val("0,00");
		}
	},
	
	obterCotasSelecionadas : function(){
		
		var cotasMarcadas=new Array();
		
		var table = $("#tabelaCotas tr", movimentoFinanceiroCotaController.workspace);
		
		for(var i = 0; i < table.length; i++){   
			
			if ($("#checkbox_"+table[i].cells[0].textContent, movimentoFinanceiroCotaController.workspace).attr("checked") == "checked") {	
				
				cotasMarcadas.push(table[i].cells[0].textContent);
			}
		} 		
		
		return cotasMarcadas;
	},
	
	processarFinanceiroCota : function(){

		var param = serializeArrayToPost('numerosCota',movimentoFinanceiroCotaController.obterCotasSelecionadas());
		
    	$.postJSON(contextPath + "/financeiro/movimentoFinanceiroCota/processarFinanceiroCota",param,
				   function(mensagens) {

					   if (mensagens){
						   
						   var tipoMensagem = mensagens.tipoMensagem;
						   
						   var listaMensagens = mensagens.listaMensagens;
						   
						   if (tipoMensagem && listaMensagens) {
							   
						       exibirMensagem(tipoMensagem, listaMensagens);
					       }
		        	   }
			           
					   movimentoFinanceiroCotaController.buscarCotas();
	               },
	               null,
	               true);
	},
	
	postergarFinanceiroCota : function(){

		var param = serializeArrayToPost('numerosCota',movimentoFinanceiroCotaController.obterCotasSelecionadas());
		
    	$.postJSON(contextPath + "/financeiro/movimentoFinanceiroCota/postergarFinanceiroCota",param,
				   function(mensagens) {

					   if (mensagens){
						   
						   var tipoMensagem = mensagens.tipoMensagem;
						   
						   var listaMensagens = mensagens.listaMensagens;
						   
						   if (tipoMensagem && listaMensagens) {
							   
						       exibirMensagem(tipoMensagem, listaMensagens);
					       }
		        	   }
			           
					   movimentoFinanceiroCotaController.buscarCotas();
	               },
	               null,
	               true);
	}
},

BaseController);

//@ sourceURL=movimentoFinanceiroCota.js
