var cadastroTipoNotaController = $.extend(true, {

	buscar: function() {
		var operacao = $("#operacaoID", this.worspace).val();
		var tipoNota = $("#tipoNota", this.workspace).val();
		$(".tiposNotasGrid", this.worspace).flexOptions({
			url: contextPath + '/administracao/naturezaOperacao/pesquisar',
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
					$(".grids", this.worspace).hide();
					return resultado;
				}
				$(".grids", this.worspace).show();
				return resultado;
			  },
		
			dataType : 'json',
			colModel : [ {
				display : 'Operacao',
				name : 'tipoAtividade',
				width : 138,
				sortable : true,
				align : 'left'
			},{
				display : 'Descrição',
				name : 'nopDescricao',
				width : 390,
				sortable : true,
				align : 'left'
			},{
				display : 'CFOP Dentro UF',
				name : 'cfopEstado',
				width : 80,
				sortable : true,
				align : 'center'
			},{
				display : 'CFOP Fora UF',
				name : 'cfopOutrosEstados',
				width : 80,
				sortable : true,
				align : 'center'
			}],
			sortname : "tipoAtividade",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 950,
			height : 'auto'
		});
	}, 
	bindButtons : function() {
		$("#btnPesquisar", this.workspace).click(function() {
			cadastroTipoNotaController.buscar();
			$(".grids", this.worspace).show();
		});
	},
	init : function() {
		this.initGrid();
		this.bindButtons();
		definirAcaoPesquisaTeclaEnter(this.worspace);
	}		
}, BaseController);
//@sourceURL=naturezaOperacao.js