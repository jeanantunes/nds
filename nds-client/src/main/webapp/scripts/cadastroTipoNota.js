var cadastroTipoNotaController = $.extend(true, {

	buscar: function(){
		var operacao = $("#operacaoID", this.worspace).val();
		var tipoNota = $("#tipoNota", this.workspace).val();
		$(".tiposNotasGrid").flexOptions({
			url: contextPath + '/administracao/cadastroTipoNota/pesquisar',
			params: [
		         {name:'operacao', value: operacao},
		         {name:'tipoNota', value: tipoNota}
		    ],
		    newp: 1,
		});
		$(".tiposNotasGrid").flexReload();
	},
	initGrid: function() {
		$(".tiposNotasGrid", this.workspace).flexigrid({
			preProcess: function(resultado) {
				if (resultado.mensagens) {
					exibirMensagem(resultado.mensagens.tipoMensagem, resultado.mensagens.listaMensagens);
					$(".grids").hide();
					return resultado;
				}
				$(".grids").show();
				return resultado;
			  },
		
			dataType : 'json',
			colModel : [ {
				display : 'Operacao',
				name : 'tipoAtividade',
				width : 140,
				sortable : true,
				align : 'left'
			},{
				display : 'Processo',
				name : 'processo',
				width : 140,
				sortable : false,
				align : 'left'
			},{
				display : 'Tipo de Nota',
				name : 'nopDescricao',
				width : 400,
				sortable : true,
				align : 'left'
			},{
				display : 'CFOP Dentro UF',
				name : 'cfopEstado',
				width : 100,
				sortable : true,
				align : 'left'
			},{
				display : 'CFOP Fora UF',
				name : 'cfopOutrosEstados',
				width : 100,
				sortable : true,
				align : 'left'
			}],
			sortname : "tipoAtividade",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});
	}, 
	bindButtons : function() {
		$("#btnPesquisar", this.workspace).click(function() {
			cadastroTipoNotaController.buscar();
			$(".grids").show();
		});
	},
	init : function() {
		this.initGrid();
		this.bindButtons();
		definirAcaoPesquisaTeclaEnter();
	}		
}, BaseController);


