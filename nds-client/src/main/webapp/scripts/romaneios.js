var romaneiosController = $.extend(true, {
	
	init : function () {
		$(".romaneiosGrid", romaneiosController.workspace).flexigrid({
			preProcess: romaneiosController.executarPreProcessamento,
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
				name : 'logradouro',
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
			buttonImageOnly: true
		});
		
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
		
		params.push({name: 'filtro.data',      value: $("#dataLancamento", romaneiosController.workspace).val()});
		params.push({name: 'filtro.idBox',     value:$('#idBox', romaneiosController.workspace).val()});
		params.push({name: 'filtro.idRoteiro', value:$('#idRoteiro', romaneiosController.workspace).val()});
		params.push({name: 'filtro.idRota',    value:$('#idRota', romaneiosController.workspace).val()});
		params.push({name: 'filtro.nomeRota',  value: $('#idRota option:selected', romaneiosController.workspace).text()});
		
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

			return resultado;
		}
		
		$.each(resultado.rows, function(index, value){
			
			if (!value.cell.numeroNotaEnvio){
				
				value.cell.numeroNotaEnvio = '';
			}
		});
		
		$(".grids", romaneiosController.workspace).show();
		
		return resultado;
	},
	
	pesquisarProdutos : function(){
		
		var data = $("#dataLancamento", romaneiosController.workspace).val();
		
		if (data){
		
			$.postJSON(contextPath + "/romaneio/carregarProdutosDataLancamento",
				"data=" + data,
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
	}
		
}, BaseController);

//@ sourceURL=romaneios.js
