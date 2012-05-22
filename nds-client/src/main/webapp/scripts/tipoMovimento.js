function TipoMovimento(pathTela,obj) {
	
	var T = this;
	
	this.tiposMovimento = []; 
	this.tipoMovimento = null;
	
	this.cliquePesquisar = function() {
		
		$(".movimentosGrid").flexOptions({			
			url : pathTela + "/tipoMovimento/pesquisarTipoMovimento",
			dataType : 'json',
			preProcess: T.processaRetornoPesquisa,
			params:T.getDadosFiltro()
		});
		
		$(".movimentosGrid").flexReload();
	},
	
	this.processaRetornoPesquisa = function(result) {
		
		$.each(result.rows, function(index,row){T.processarLinha(index,row.cell);} );
		
		return result;
	},
	
	this.processarLinha = function(index,cell) {
		
		T.tiposMovimento.push(cell);
		
		cell.acao = 			
			'<a href="javascript:;" onclick="'+ obj +'.carregarAlteracao(\''+ index + '\')">' +
			'<img src="'+pathTela+'/images/ico_editar.gif" border="0" hspace="5" />' +
			'</a>'+
			'<a href="javascript:;" onclick="'+ obj +'.carregarExclusao(\''+ index + '\')">' +
			'<img src="'+pathTela+'/images/ico_excluir.gif" hspace="5" border="0" />' +
			'</a>';
	},
	
	this.carregarAlteracao = function(index) {
		
		T.tipoMovimento = T.tiposMovimento[index];
		
		T.set('codigoModal', T.tipoMovimento.codigo);
		T.set('descricaoModal', T.tipoMovimento.descricao);
		T.set('grupoOperacaoModal', T.tipoMovimento.grupoOperacao);
		T.set('operacaoModal', T.tipoMovimento.operacao);
		T.set('aprovacaoModal', T.tipoMovimento.aprovacao);
		T.set('incideDividaModal', T.tipoMovimento.incideDivida);
				
		popup_alterar();
		
	},
	
	this.carregarNovo = function() {
		popup();
	},
	
	this.carregarExclusao = function(index) {
		popup_excluir();
	},
	
	
	/**
	 * Retorna todos os dados do filtro no padrão utilizado pelo VRaptor
	 * @return Espelho de FiltroTipoMovimentoDTO (br.com.abril.nds.dto) 
	 */
	this.getDadosFiltro = function() {
	
		var data = [];
		
		data.push({name:'filtro.codigo',		value: T.get("codigo")});
		data.push({name:'filtro.descricao',		value: T.get("descricao")});
		
		return data;
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

	});
}
