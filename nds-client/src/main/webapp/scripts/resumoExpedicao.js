var resumoExpedicaoController = $.extend(true, {
	
	_codigoBox : "",
	_dataLancamento : "",
	
	inicializar : function() {
		
		$("#dataLancamentoResumo", this.workspace).datepicker({
			showOn : "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly : true,
			dateFormat: 'dd/mm/yy',
			defaultDate: new Date()
		});
		
		$("#dataLancamentoResumo", this.workspace).mask("99/99/9999");
		$("#dataLancamentoResumo", this.workspace).focus();
		
		this.inicializarGridResumoExpedicaoBox();
		this.inicializarGridResumoExpedicaoProduto();
		this.inicializarGridVendaEncalhe();
	},
	
	inicializarGridResumoExpedicaoBox : function() {
		
		$("#resumoExpedicaoGridBox", this.workspace).flexigrid({
			preProcess: this.executarPreProcessamento,
			dataType : 'json',
			colModel : [ 
			{
				display : 'Data Lançamento',
				name : 'dataLancamento',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Box',
				name : 'codigoBox',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome do Box',
				name : 'descricaoBox',
				width : 240,
				sortable : true,
				align : 'left'
			}, {
				display : 'Qtde Produto',
				name : 'qntProduto',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 90,
				sortable : true,
				align : 'center'
			},{
				display : 'Diferença',
				name : 'qntDiferenca',
				width : 90,
				sortable : true,
				align : 'center'
			},{
				display : 'Valor Faturado R$',
				name : 'valorFaturado',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 40,
				sortable : false,
				align : 'center'
			}],
			sortname : "codigoBox",
			sortorder : "asc",
			scroll:true,
			width : 960,
			height : 150
		});
	},
	
	inicializarGridResumoExpedicaoProduto : function() {
		
		$("#resumoExpedicaoGridProduto", this.workspace).flexigrid({
			preProcess: this.executarPreProcessamento,
			dataType : 'json',
			colModel : [
			{
				display : 'Código',
				name : 'codigoProduto',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'descricaoProduto',
				width : 330,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicaoProduto',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Preço Venda R$',
				name : 'precoCapa',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 90,
				sortable : true,
				align : 'center'
			},{
				display : 'Diferença',
				name : 'qntDiferenca',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Valor Faturado R$',
				name : 'valorFaturado',
				width : 90,
				sortable : true,
				align : 'right'
			}],
			sortname : "codigoProduto",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 150
		});
	},
	
	inicializarGridVendaEncalhe : function() {
		
		$("#venda-encalhe-grid", this.workspace).flexigrid({
			preProcess: this.executarPreProcessamentoDetalheResumoExpedicao,
			dataType : 'json',
			colModel : [
			{
				display : 'Código',
				name : 'codigoProduto',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'descricaoProduto',
				width : 160,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicaoProduto',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Preço Capa R$',
				name : 'precoCapa',
				width : 110,
				sortable : true,
				align : 'right'
			}, {
				display : 'Preço Desconto R$',
				name : 'precoDesconto',
				width : 110,
				sortable : true,
				align : 'right'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 70,
				sortable : true,
				align : 'center'
			},{
				display : 'Diferença',
				name : 'qntDiferenca',
				width : 70,
				sortable : true,
				align : 'center'
			},{
				display : 'Total R$',
				name : 'valorFaturado',
				width : 90,
				sortable : true,
				align : 'center'
			},{
				display : 'Fornecedor',
				name : 'nomeFornecedor',
				width : 90,
				sortable : true,
				align : 'center'
			}],
			sortname : "codigoProduto",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 150
		});
	},
	
	/*
	 * Executa o pré processamento das informações retornadas da requisição de pesquisa.
	 */
	executarPreProcessamento : function(resultado) {
		
		if (resultado.mensagens) {
	
			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$("#grid", this.workspace).hide();
	
			return resultado.tableModel;
		}
		
		$("#totalReparte", this.workspace).html(resultado.qtdeTotalReparte);
		
		$("#totalValorFaturado", this.workspace).html(resultado.valorTotalFaturado);
		
		$("#grid", this.workspace).show();
		
		resumoExpedicaoController.mudarLegendaFielsSet('idFiledResultResumo','resumo');
		
		$("#dataLancamentoResumo", this.workspace).focus();
		
		$.each(resultado.tableModel.rows, function(index, row) {
			
			row.cell.codigoBox = "<input type='hidden' id='codigoBox"+ index +"' value='"+ row.cell.codigoBox +"'/>"+ row.cell.codigoBox;
			row.cell.descricaoBox = "<input type='hidden' id='descricaoBox"+ index +"' value='"+ row.cell.descricaoBox +"'/>"+ row.cell.descricaoBox;
			var dataLan = "";
			
			if (!row.cell.dataLancamento){
				
				dataLan = "";
				
			} else {
				
				dataLan = row.cell.dataLancamento;
			}
			
			row.cell.dataLancamento = "<span id='dataLanc"+ index +"'>"+ dataLan +"</span>";
			
			row.cell.acao = "<span style='text-align: center;'>" +
				"<a href='javascript:;' onclick='resumoExpedicaoController.detalharResumoExpedicao("+ index +");'>" +
				"<img border='0' alt='Detalhes' src=\'" + contextPath + "/images/ico_detalhes.png\'></a></span>";
		});
		
		return resultado.tableModel;
	},
	
	/*
	 * Executa o pré processamento detalhes resumo expedicao
	 */
	executarPreProcessamentoDetalheResumoExpedicao : function(resultado) {
		
		if (resultado.mensagens) {
	
			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
	
			return resultado;
		}
		
		$("#valorTotal", this.workspace).html(resultado.somaTotal);
		
		return resultado.resultado;
	},
	
	detalharResumoExpedicao : function(index) { 
		
		this._codigoBox = $("#codigoBox" + index, this.workspace).val();
		this._dataLancamento = $("#dataLanc" + index, this.workspace).text();
		
		var originalCodigoBox = this._codigoBox.split('-')[0];
		
		$("#box-resumo-expedicao", this.workspace).html(originalCodigoBox);
		
		$("#nome-box-resumo-expedicao", this.workspace).html($("#descricaoBox"+ index, this.workspace).val());
		
		$("#venda-encalhe-grid", this.workspace).flexOptions({
			
			url: contextPath + '/expedicao/resumo/pesquisar/detalhe',
			dataType : 'json',
			
			params:[{name:'codigoBox', value: originalCodigoBox},
			        
			        {name:'dataLancamento', value: this._dataLancamento}]
		
		});
		
		$("#venda-encalhe-grid", this.workspace).flexReload();
		
		$("#dialog-venda-encalhe", this.workspace).dialog({
			resizable: false,
			width:1000,
			modal: true,
			buttons: {
				"Fechar": function() {
					$(this, this.workspace).dialog("close");
				}
			},
			form: $("#dialog-venda-encalhe", this.workspace).parents("form")
		});
	},
	
	/*
	 * Efetua a pesquisa de resumo de expedições, conforme tipo de pesquisa selecionado.
	 */
	pesquisar : function() {
		
		var dataLancamento = $('#dataLancamentoResumo', this.workspace).val();
		var tipoPesquisa = $('#tipoPesquisa', this.workspace).val();
		
		var formData = [
            {name:"dataLancamento",value:dataLancamento},
            {name:"tipoPesquisa",value:tipoPesquisa}
		];
		
		if (tipoPesquisa === 'PRODUTO') {
			this.carregarGridProduto(formData);
			$("#gridBox", this.workspace).hide();
		} else if (tipoPesquisa === 'BOX') {
			this.carregarGridbox(formData);
			$("#gridProduto", this.workspace).hide();
		} else {
			var mensagens = new Array('O preenchimento do campo \'Tipo de Consulta\' é obrigatório.') ;
			exibirMensagem('WARNING',mensagens);
		}
		
		$("#dataLancamentoResumo", this.workspace).focus();
	},
	
	/*
	 * Efetua a busca das informações referente a Produto, e monta grid
	 */
	carregarGridProduto : function(formData) {
	
		$("#resumoExpedicaoGridProduto", this.workspace).flexOptions({
			url: contextPath + "/expedicao/resumo/pesquisar/produto",
			params: formData,
			newp: 1
		});
		
		$("#resumoExpedicaoGridProduto", this.workspace).flexReload();
		
		$("#gridProduto", this.workspace).show();
	},
	
	/*
	 * Efetua a busca das informações referente a Box, e monta o grid
	 */
	carregarGridbox : function(formData) {
	
		$("#resumoExpedicaoGridBox", this.workspace).flexOptions({
			url: contextPath + "/expedicao/resumo/pesquisar/box",
			params: formData,
			newp: 1
		});
		
		$("#resumoExpedicaoGridBox", this.workspace).flexReload();
		
		$("#gridBox", this.workspace).show();
	},
	
	/*
	 * Altera o titulo do fieldset conforme tipo de pesquisa selecionado
	 */
	mudarLegendaFielsSet : function(id, tipo) {
		
		 if (tipo === "pesquisar") {
			 $(id, this.workspace).html(this.getTituloFieldSetPesquisa());
		 } else {
			 $(id, this.workspace).html(this.getTituloFieldSet());
		 }
	},
	
	/*
	 * Retorna o tirulo do fieldset conforme tipo de pesquisa selecionado
	 */
	getTituloFieldSetPesquisa : function() {
		
		return ($('#tipoPesquisa', this.workspace).val() === 'BOX')
				? 'Pesquisar Expedição por Box'
						:'Pesquisar Expedição por Produto';
	},
	
	/*
	 * Retorna o tirulo do fieldset conforme tipo de pesquisa selecionado
	 */
	getTituloFieldSet : function() {
		
		return ($('#tipoPesquisa', this.workspace).val() === 'BOX')
				? 'Resumo  Expedição por Box'
						:'Resumo  Expedição por Produto';
	},
	
	/*
  	 * Efetua a exporta��o dos dados da pesquisa.
  	 */
	exportar : function(fileType) {

		var tipoPesquisa = $("#tipoPesquisa", this.workspace).val();

		if (!tipoPesquisa || !fileType) {

			return;
		}

		window.location = 
			contextPath + "/expedicao/resumo/exportar?tipoConsulta=" + tipoPesquisa + "&fileType=" + fileType;
	},
	
	/*
	 * Exporta os detalhes.
	 */
	exportarDetalhes : function(fileType) {

		var originalCodigoBox = this._codigoBox.split('-')[0];
		
		window.location = contextPath + "/expedicao/resumo/exportarDetalhes?fileType=" + fileType + "&codigoBox=" + originalCodigoBox + "&dataLancamento=" + this._dataLancamento;
		
	}
	
}, BaseController);