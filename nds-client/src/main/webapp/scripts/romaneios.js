var romaneiosController = $.extend(true, {
	
	init : function () {
		$(".romaneiosGrid", romaneiosController.workspace).flexigrid({
			preProcess: romaneiosController.executarPreProcessamento,
			onSuccess: romaneiosController.carregarQuantidadeCotas,
			dataType : 'json',
			colModel : [ {
				display : 'Nº. NE',
				name : 'numeroNotaEnvio',
				width : 80,
				sortable : true,
				align : 'left'
			},{
				display : 'Cota',
				name : 'numeroCota',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 310,
				sortable : true,
				align : 'left'
			}, {
				display : 'Endereço',
				name : 'endereco',
				width : 450,
				sortable : false,
				align : 'left'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 150
		});
		
		$( "#dataLancamento", romaneiosController.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath +"/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true,
			dateFormat : 'dd/mm/yy'
		});
		
		$("#dataLancamento", romaneiosController.workspace).mask("99/99/9999");
		
		$("#selectProdutos", romaneiosController.workspace).multiselect({
			selectedList : 6
		});
	},
	
	mostrar : function(){
		$(".grids", romaneiosController.workspace).show();
	},

	confirmar : function(){
			$(".dados", romaneiosController.workspace).show();
	},
	
	pesqEncalhe : function(){
			$(".dadosFiltro", romaneiosController.workspace).show();		
	},

	pesquisar : function(){
		
		var params = [];
		
		params.push({name: 'filtro.data',      	value: $("#dataLancamento", romaneiosController.workspace).val()});
		params.push({name: 'filtro.codigoBox', 	value:$('#codigoBox', romaneiosController.workspace).val()});
		params.push({name: 'filtro.idRoteiro', 	value:$('#idRoteiro', romaneiosController.workspace).val()});
		params.push({name: 'filtro.idRota',    	value:$('#idRota', romaneiosController.workspace).val()});
		params.push({name: 'filtro.nomeRota',	value: $('#idRota option:selected', romaneiosController.workspace).text()});
		params.push({name: 'filtro.nomeRoteiro',value: $('#idRoteiro option:selected', romaneiosController.workspace).text()});
		params.push({name: 'filtro.nomeBox',  	value: $('#codigoBox option:selected', romaneiosController.workspace).text()});
		
		var produtos = $("#selectProdutos", romaneiosController.workspace).val();
		
		if (produtos){
			$.each(produtos, function(index, value){
				params.push({name: 'filtro.produtos['+index+']', value: value});
			});
		}
		
		$(".romaneiosGrid", romaneiosController.workspace).flexOptions({
			url: contextPath + "/romaneio/pesquisarRomaneio",
			dataType : 'json',
			params: params
		});
		
		$(".romaneiosGrid", romaneiosController.workspace).flexReload();
	},

	executarPreProcessamento : function(resultado) {
		
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".grids", romaneiosController.workspace).hide();
			
			romaneiosController.esconderBotoes();

			return resultado;
		}
		
		$.each(resultado.rows, function(index, value){
			
			if (!value.cell.numeroNotaEnvio){
				
				value.cell.numeroNotaEnvio = '';
			}
		});
		
		$(".grids", romaneiosController.workspace).show();
		
		romaneiosController.mostrarBotoes();
		
		return resultado;
	},
	
	carregarQuantidadeCotas : function(){
		
		$.postJSON(contextPath + "/romaneio/pesquisarQuantidadeCotasEntrega",
			null,
			function(result) {
				
				if (result.mensagens) {
	
					exibirMensagem(
						result.mensagens.tipoMensagem, 
						result.mensagens.listaMensagens
					);
				}
				
				$("#totalCotas", romaneiosController.workspace).text(result ? result.int : "");
			}
		);
	},
	
	pesquisarProdutos : function(){
		
		var data = $("#dataLancamento", romaneiosController.workspace).val();
		
		if (data){
		
			$.postJSON(contextPath + "/romaneio/carregarProdutosDataLancamento",
				{data:data},
				function(result) {
					
					if (result.mensagens) {
		
						exibirMensagem(
							result.mensagens.tipoMensagem, 
							result.mensagens.listaMensagens
						);
						
						$(".grids", romaneiosController.workspace).hide();
					}
					
					var itens = '';
					
					if (result){
						$.each(result, function(index, result) {
							itens += '<option value="'+ result.key.$ +'">'+ (result.value ? result.value.$ : '' ) +'</option>';
						});
					}
					
					$("#selectProdutos", romaneiosController.workspace).multiselect("destroy");
					$("#selectProdutos", romaneiosController.workspace).html("");
					$("#selectProdutos", romaneiosController.workspace).append(itens).multiselect();
				}
			);
		} else {
			
			$("#selectProdutos", romaneiosController.workspace).multiselect("destroy");
			$("#selectProdutos", romaneiosController.workspace).html("");
			$("#selectProdutos", romaneiosController.workspace).multiselect();
		}
	},
	
	/**
	 * Recarregar combos por Box
	 */
    changeBox : function(){
		
    	var codigoBox = $("#codigoBox").val();
    	
    	var idRoteiro = $("#idRoteiro").val();
    	
    	var idRota = $("#idRota").val();
    	
    	var params = [{
			            name : "codigoBoxDe",
			            value : codigoBox	
					  }];
    	
    	$.postJSON(contextPath + '/cadastro/roteirizacao/carregarCombosPorBox', params, 
			function(result) {
    		
    		    var listaRota = result[0];
    		    
    		    var listaRoteiro = result[1];
    		    
    		    var listaBox = result[2];
    		
    		    romaneiosController.recarregarCombo($("#idRota", romaneiosController.workspace), listaRota, idRota);
 		    
    		    romaneiosController.recarregarCombo($("#idRoteiro", romaneiosController.workspace), listaRoteiro, idRoteiro); 
    		    
    		    romaneiosController.recarregarCombo($("#codigoBox", romaneiosController.workspace), listaBox, codigoBox);
    	    }    
		);
	},
	
	/**
	 * Recarregar combos por Rota
	 */
    changeRota : function(){
    	
        var codigoBox = $("#codigoBox").val();
    	
        var idRoteiro = $("#idRoteiro").val();
        
    	var idRota = $("#idRota").val();
    	
    	var params = [{
			            name : "idRota",
			            value : idRota	
					  }];
	    
    	$.postJSON(contextPath + '/cadastro/roteirizacao/carregarCombosPorRota', params, 
			function(result) {
    		
    		    var listaRoteiro = result[0];
    		 
    		    var listaBox = result[1];
    		    
    		    var listaRota = result[2];

    		    romaneiosController.recarregarCombo($("#codigoBox", romaneiosController.workspace), listaBox, codigoBox);
 		    
    		    romaneiosController.recarregarCombo($("#idRoteiro", romaneiosController.workspace), listaRoteiro, idRoteiro); 
    		    
    		    romaneiosController.recarregarCombo($("#idRota", romaneiosController.workspace), listaRota, idRota);
    	    }    
		);
	},
	
	/**
	 * Recarregar combos por Roteiro
	 */
    changeRoteiro : function(){
    	
        var codigoBox = $("#codigoBox").val();
    	
        var idRoteiro = $("#idRoteiro").val();
        
    	var idRota = $("#idRota").val();
    	
     	var params = [{
			            name : "idRoteiro",
			            value : idRoteiro	
					  }];
     	
     	$.postJSON(contextPath + '/cadastro/roteirizacao/carregarCombosPorRoteiro', params, 
			function(result) {
    		
    		    var listaRota = result[0];
    		 
    		    var listaBox = result[1];
    		    
    		    var listaRoteiro = result[2];
 		    
    		    romaneiosController.recarregarCombo($("#idRota", romaneiosController.workspace), listaRota, idRota);  
    		    
    		    romaneiosController.recarregarCombo($("#codigoBox", romaneiosController.workspace), listaBox, codigoBox);
     		    
    		    romaneiosController.recarregarCombo($("#idRoteiro", romaneiosController.workspace), listaRoteiro, idRoteiro); 
    	    }    
		);
	},
	
	/**
	 * Recarregar combo
	 */
	recarregarCombo : function (comboNameComponent, content, valSelected){
		
		comboNameComponent.empty();

		comboNameComponent.append(new Option('Todos', '', true, true));
		
	    $.each(content, function(index, row) {
		    	
	    	comboNameComponent.append(new Option(row.value.$, row.key.$, true, true));
		});

	    if (valSelected) {
	    	
	        $(comboNameComponent).val(valSelected);
	    } else {
	    	
	        $(comboNameComponent).val('');
	    }
	},
	
	mostrarBotoes : function() {
		
		$(".areaBts", romaneiosController.workspace).find("span").show();
	},
	
	esconderBotoes : function() {
		
		$(".areaBts", romaneiosController.workspace).find("span").hide();
	}
		
}, BaseController);

//@ sourceURL=romaneios.js
