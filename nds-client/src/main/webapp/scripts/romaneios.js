var romaneiosController = $.extend(true, {
	
	init : function () {
		$(".romaneiosGrid", romaneiosController.workspace).flexigrid({
			preProcess: romaneiosController.executarPreProcessamento,
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 180,
				sortable : true,
				align : 'left'
			}, {
				display : 'Endereço',
				name : 'logradouro',
				width : 260,
				sortable : true,
				align : 'left'
			}, {
				display : 'Bairro',
				name : 'bairro',
				width : 135,
				sortable : true,
				align : 'left'
			}, {
				display : 'Cidade',
				name : 'cidade',
				width : 90,
				sortable : true,
				align : 'left'
			}, {
				display : 'UF',
				name : 'uf',
				width : 30,
				sortable : true,
				align : 'center'
			}, {
				display : 'Telefone',
				name : 'numeroTelefone',
				width : 100,
				sortable : true,
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
		$(".romaneiosGrid").flexOptions({
			url: contextPath + "/romaneio/pesquisarRomaneio",
			dataType : 'json',
			params: [
						{name:'filtro.idBox', value:$('#idBox', romaneiosController.workspace).val()},
						{name:'filtro.idRoteiro', value:$('#idRoteiro', romaneiosController.workspace).val()},
						{name:'filtro.idRota', value:$('#idRota', romaneiosController.workspace).val()},
						{name:'filtro.nomeRota',	value: $('#idRota option:selected', romaneiosController.workspace).text()}
						]
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
		
		$(".grids", romaneiosController.workspace).show();
		
		return resultado;
	}
		
}, BaseController);
