function Parciais(pathTela) {
	
	var T = this;
		
	this.idProdutoEdicao = null;
	this.dataLancamento = null;
	this.dataRecolhimento = null;
	this.codigoProduto = null;
	this.nomeProduto = null;
	this.numEdicao = null;
	this.nomeFornecedor = null;
	this.precoCapa = null;
	this.statusParcial = null;
	
	this.idLancamento = null;
	
	/**
	 * Ação de clique do botão pesquisar
	 */
	this.cliquePesquisar = function() {
		
		if(T.get('codigoProduto').length!=0 && T.get('edicaoProduto').length!=0) {
			$('#painelLancamentos').hide();
			$('#painelPeriodos').show();	
			
			
			T.codigoProduto = T.get('codigoProduto');
			T.numEdicao = T.get('edicaoProduto');
			
			T.pesquisarPeriodosParciais();
		} else {
			$('#painelPeriodos').hide();
			$('#painelLancamentos').show();
			T.pesquisarLancamentosParciais();
		}		
	},
	
	this.pesquisarLancamentosParciais = function() {
		
		$(".parciaisGrid").flexOptions({			
			url : pathTela + "/parciais/pesquisarParciais",
			dataType : 'json',
			preProcess: T.processaRetornoPesquisaParciais,
			params:T.getDados()
		});
		
		$(".parciaisGrid").flexReload();
	},
	
	this.pesquisarPeriodosParciais = function() {
		
		$(".periodosGrid").flexOptions({			
			url : pathTela + "/parciais/pesquisarPeriodosParciais",
			dataType : 'json',
			preProcess: T.processaRetornoPeriodosParciais,
			params:T.getDados()
		});
		
		$(".periodosGrid").flexReload();
		
	},

	this.pesquisarPeriodosParciaisModal = function(codigoProduto,numEdicao) {
		
		var data = [];
		
		data.push({name: 'filtro.codigoProduto',	value: codigoProduto});
		data.push({name: 'filtro.edicaoProduto',	value: numEdicao});
		
		$(".parciaisPopGrid").flexOptions({			
			url : pathTela + "/parciais/pesquisarPeriodosParciais",
			dataType : 'json',
			preProcess: T.processaRetornoPeriodosParciaisModal,
			params:data
		});
		
		$(".parciaisPopGrid").flexReload();
		
	},

	this.inserirPeriodos = function(modal) {
		$.postJSON(contextPath + "/parciais/inserirPeriodos",
				T.getDadosNovosPeriodo(),
				function(result){
					if(modal)
						$(".parciaisPopGrid").flexReload();
					else
						$(".periodosGrid").flexReload();
				},
				null, 
				true,
				"dialog-detalhes");
		
		
	},	
	
	
	this.processaRetornoPesquisaParciais = function(result) {
		
		if(result.mensagens) 
			exibirMensagem(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
		
		if(result.rows.length==0) {
			$("#exportacao").hide();
		} else {
			$("#exportacao").show();
		}
		
		$.each(result.rows, function(index,row){T.gerarAcaoPrincipal(index,row);} );
				
		return result;
	},
	
	this.processaRetornoPeriodosParciais = function(result) {
		
		if(result.mensagens) 
			exibirMensagem(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
			
		
		if(result.rows.length==0) {
			$('#exportacaoPeriodos').hide();
		} else {
			$('#exportacaoPeriodos').show();
			T.idProdutoEdicao = result.rows[0].cell.idProdutoEdicao;
		}
		
		if(result.rows[0].cell.geradoPorInterface==true)
			$("#btnIncluirPeriodos").hide();
		else
			$("#btnIncluirPeriodos").show();		
		
		$.each(result.rows, function(index,row){T.gerarAcaoDetalhes(index,row);} );
				
		return result;
	},
	
	this.processaRetornoPeriodosParciaisModal = function(result) {
		
		if(result.mensagens)
				exibirMensagemDialog(result.mensagens.tipoMensagem, result.mensagens.listaMensagens, "dialog-detalhes");
					
		if(result.rows.length==0) {
			$('#exportacaoPeriodosModal').hide();
		} else {
			$('#exportacaoPeriodosModal').show();
		}
		
		if(result.rows[0].cell.geradoPorInterface==true)
			$("#btnIncluirPeriodosModal").hide();
		else
			$("#btnIncluirPeriodosModal").show();		
		
		$.each(result.rows, function(index,row){T.gerarAcaoDetalhes(index,row);} );
				
		return result;
	},
		
		
	
	/**
	 * Retorna todos os dados da tela principal no padrão utilizado pelo VRaptor
	 * @return Espelho de FiltroParciaisDTO (br.com.abril.nds.dto) 
	 */
	this.getDados = function() {
	
		var data = [];
		
		data.push({name:'filtro.codigoProduto',		value: T.get("codigoProduto")});
		data.push({name:'filtro.nomeProduto',		value: T.get("nomeProduto")});
		data.push({name:'filtro.edicaoProduto',		value: T.get("edicaoProduto")});
		data.push({name:'filtro.idFornecedor',		value: T.get("idFornecedor")});
		data.push({name:'filtro.dataInicial',		value: T.get("dataInicial")});
		data.push({name:'filtro.dataFinal',			value: T.get("dataFinal")});
		data.push({name:'filtro.status',			value: T.get("status")});
		
		data.push({name:'filtro.nomeFornecedor',	value: $('#idFornecedor option:selected').text()});
		
		return data;
	},
	
	this.getDadosNovosPeriodo = function() {
		
		var data = [];
		
		data.push({name:'peb',				value: T.get("peb")});
		data.push({name:'qtde',				value: T.get("qtde")});
		data.push({name:'idProdutoEdicao',	value: T.idProdutoEdicao});
		
		return data;
	},
	
	this.getDadosParaPeb = function() {
		
		var data = [];
		
		data.push({name:'codigoProduto',		value: T.codigoProduto});
		data.push({name:'edicaoProduto',		value: T.numEdicao});
		
		return data;
	},
	
	this.getDadosEdicaoPeriodo = function() {
		
		var data = [];
		
		data.push({name:'dataLancamento',		value: T.get('dataLancamentoEd')});
		data.push({name:'dataRecolhimento',		value: T.get('dataRecolhimentoEd')});
		data.push({name:'idLancamento',			value: T.idLancamento});	
		
		return data;
	},
	
	this.carregaPeb = function() {
		
		T.set('peb','');
		T.set('qtde','');
		
		$.postJSON(contextPath + "/parciais/obterPebDoProduto",
				T.getDadosParaPeb(),
				function(result){T.set('peb',result);},
				null, 
				true,
				"dialog-novo");
		
	},
	
	this.gerarAcaoPrincipal = function(index,row) {
		row.cell.acao = 
			'<a href="javascript:;" onclick="PARCIAIS.carregarDetalhes(\''+ 
				row.cell.idProdutoEdicao +'\', \''+
				row.cell.dataLancamento +'\', \''+
				row.cell.dataRecolhimento +'\', \''+
				row.cell.codigoProduto +'\', \''+
				row.cell.nomeProduto +'\', \''+
				row.cell.numEdicao +'\', \''+
				row.cell.nomeFornecedor +'\', \''+ 
				row.cell.precoCapa +'\', \''+ 
				row.cell.statusParcial + '\')">' +
				'<img src="'+pathTela+'/images/ico_detalhes.png" border="0" /></a>';
	},
	
	this.gerarAcaoDetalhes = function(index, row) {
		
		row.cell.acao = 
			'<a href="javascript:;" ' +
			(row.cell.geradoPorInterface==true?'style="opacity: 0.5;"':'onclick="PARCIAIS.carregarEdicaoDetalhes(\''+ 
					row.cell.idLancamento +'\', \''+
					row.cell.dataLancamento +'\', \''+
					row.cell.dataRecolhimento +
			        ' \')"')+
			        
			' ><img src="'+pathTela+'/images/ico_editar.gif" border="0" hspace="5" /></a>' +
			'<a href="javascript:;" '+
			(row.cell.geradoPorInterface==true?'style="opacity: 0.5;"':' onclick="PARCIAIS.carregarExclusaoPeriodo(\'' + row.cell.idLancamento+ '\');" ')+
			'><img src="'+pathTela+'/images/ico_excluir.gif" hspace="5" border="0" /></a>';
	},
	
	this.carregarDetalhes = function(idProdutoEdicao , dataLancamento, dataRecolhimento, codigoProduto, 
			nomeProduto, numEdicao, nomeFornecedor, precoCapa, statusParcial) {
				
		T.idProdutoEdicao = idProdutoEdicao;
		T.dataLancamento = dataLancamento;
		T.dataRecolhimento = dataRecolhimento;
		T.codigoProduto = codigoProduto;
		T.nomeProduto = nomeProduto;
		T.numEdicao = numEdicao;
		T.nomeFornecedor = nomeFornecedor;
		T.precoCapa = precoCapa;
		T.statusParcial = statusParcial;
		
		
		$('#codigoProdutoM').text(codigoProduto);
		$('#nomeProdutoM').text(nomeProduto);
		$('#numEdicaoM').text(numEdicao);
		$('#nomeFornecedorM').text(nomeFornecedor);
		$('#dataLancamentoM').text(dataLancamento);
		$('#dataRecolhimentoM').text(dataRecolhimento);
		
		T.pesquisarPeriodosParciaisModal(codigoProduto,numEdicao);
		
		popup_detalhes();
	},
	
	this.carregarEdicaoDetalhes = function(idLancamento, dataLancamento,dataRecolhimento) {
		
		T.idLancamento = idLancamento;
		
		$('#codigoProdutoEd').val(T.codigoProduto);
		$('#nomeProdutoEd').val(T.nomeProduto);
		$('#numEdicaoEd').val(T.numEdicao);
		$('#nomeFornecedorEd').val(T.nomeFornecedor);
		$('#dataLancamentoEd').val(dataLancamento);
		$('#dataRecolhimentoEd').val(dataRecolhimento);
		$('#precoCapaEd').val(T.precoCapa);
		
		popup_edit_produto();
	},
	
	this.carregarExclusaoPeriodo = function(idLancamento) {
		
		T.idLancamento = idLancamento;
		
		popup_excluir();
	},
	
	this.editarPeriodoParcial = function() {
		
		$.postJSON(contextPath + "/parciais/editarPeriodoParcial",
				T.getDadosEdicaoPeriodo(),
				function(result){

					$( "#dialog-edit-produto" ).dialog( "close" );
					
					if($('#painelPeriodos').css('display')=='none') {
						exibirMensagemDialog('SUCCESS', ['Período alterado com sucesso.'], "dialog-detalhes");			
						$(".parciaisPopGrid").flexReload();
					} else {
						$(".parciaisGrid").flexReload();
						exibirMensagem('SUCCESS', ['Período alterado com sucesso.']);
					}
					
				},	
				null,
				true,
				'dialog-edit-produto');		
	},
	
	this.excluirPeriodoParcial = function() {
		
		var data = [];		
		data.push({name:'idLancamento',			value: T.idLancamento});	
		
		$.postJSON(contextPath + "/parciais/excluirPeriodoParcial",
				data,
				function(result){
			
					if($('#painelPeriodos').css('display')=='none') {
						exibirMensagemDialog('SUCCESS', ['Período excluido com sucesso.'], "dialog-detalhes");			
						$(".parciaisPopGrid").flexReload();
					} else {
						$(".periodosGrid").flexReload();
						exibirMensagem('SUCCESS', ['Período excluido com sucesso.']);
					}
					
				},	
				null,
				true,
				'dialog-detalhes');	
		
		$( "#dialog-excluir" ).dialog( "close" );
		
	},
	
	/**
	 * Atribui valor a um campo da tela
	 * Obs: Checkboxs devem ser atribuidos com o valor de true ou false
	 * 
	 * @param campo - Campo a ser alterado
	 * @param value - valor
	 */
	this.set = function(campo,value) {
				
		var elemento = $("#" + campo);
		
		if(elemento.attr('type') == 'checkbox') {
			
			if(value) {
				elemento.attr('checked','checked');
			} else {
				elemento.removeAttr('checked');
			}
						
		} else {
			elemento.val(value);
		}
	},
	
	/**
	 * Obtém valor de elemento da tela
	 * @param campo - de onde o valor será obtido
	 */
	this.get = function(campo) {
		
		var elemento = $("#" + campo);
		
		if(elemento.attr('type') == 'checkbox') {
			return (elemento.attr('checked') == 'checked') ;
		} else {
			return elemento.val();
		}
		
	};
	
	$(function() {
		$("#dataInicial").mask("99/99/9999");
		$("#dataFinal").mask("99/99/9999");
		
		$("#dataLancamentoEd").mask("99/99/9999");
		$("#dataRecolhimentoEd").mask("99/99/9999");
		
		$("#edicaoProduto").numeric();

		$("#peb").numeric();

		$("#qtde").numeric();
	});
}