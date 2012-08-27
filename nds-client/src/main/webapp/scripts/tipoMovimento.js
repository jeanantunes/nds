function TipoMovimento(pathTela,obj) {
	
	var T = this;
	
	this.tiposMovimento = []; 
	this.tipoMovimento = null;
	
	this.cliquePesquisar = function() {
		
		var data = [];
		
		data.push({name:'filtro.codigo',		value: T.get("codigo")});
		data.push({name:'filtro.descricao',		value: T.get("descricao")});
		
		$(".movimentosGrid").flexOptions({			
			url : pathTela + "/tipoMovimento/pesquisarTipoMovimento",
			dataType : 'json',
			preProcess: T.processaRetornoPesquisa,
			params:data
		});
		
		$(".movimentosGrid").flexReload();
	},
	
	this.processaRetornoPesquisa = function(result) {
		
		T.tiposMovimento = [];
		
		$.each(result.rows, function(index,row){T.processarLinha(index,row.cell);} );
		
		return result;
	},
	
	this.processarLinha = function(index,cell) {
		
		T.tiposMovimento.push(cell);
		
		var onClickAlteracao = cell.permiteAlteracao == false ? '' : (obj +'.carregarAlteracao(\''+ index + '\')');
		var onClickExclusao =  cell.permiteAlteracao == false ? '' : (obj +'.carregarExclusao(\''+ index + '\')');
		var styleOpacity = cell.permiteAlteracao == false 	  ? ('style="opacity: 0.5"') : '';
		
		cell.acao = 			
			'<a href="javascript:;" onclick="'+ onClickAlteracao +'" style="margin-right:10px;">' +
			'<img src="'+pathTela+'/images/ico_editar.gif" border="0" hspace="5" ' + styleOpacity + ' />' +
			'</a>'+
			'<a href="javascript:;" onclick="' + onClickExclusao + '">' +
			'<img src="'+pathTela+'/images/ico_excluir.gif" hspace="5" border="0" ' + styleOpacity + ' />' +
			'</a>';
	},
	
	this.carregarAlteracao = function(index) {
		
		T.tipoMovimento = T.tiposMovimento[index];
		
		T.preencherModal(T.tipoMovimento.codigo, 
						 T.tipoMovimento.descricao, 
						 T.tipoMovimento.grupoOperacaoValue, 
						 T.tipoMovimento.operacaoValue, 
						 T.tipoMovimento.aprovacaoValue, 
						 T.tipoMovimento.incideDividaValue);
		
		popup_alterar();
		
	},
		
	this.carregarNovo = function() {
		
		T.preencherModal(null,'','FINANCEIRO','CREDITO','SIM','SIM');
		
		popup();
	},
	
	this.excluirTipoMovimento = function() {
		
		var data = [];		
		data.push({name:'codigo',		value: T.tipoMovimento.codigo});

		$.postJSON(contextPath + "/tipoMovimento/excluirTipoMovimento",
				data,
				function(result){										
					$(".movimentosGrid").flexReload();
					exibirMensagem('SUCCESS', ['Tipo de Movimento excluido com sucesso.']);										
				});		
		
		$( "#dialog-excluir" ).dialog( "close" );
	},
	
	this.salvarTipoMovimento = function() {
		
		var data = [];		
		data.push({name:'tipoMovimentoDTO.codigo',		value: T.get('codigoModal')});
		data.push({name:'tipoMovimentoDTO.descricao',		value: T.get('descricaoModal')});
		data.push({name:'tipoMovimentoDTO.grupoOperacaoValue',		value: T.get('grupoOperacaoModal')});
		data.push({name:'tipoMovimentoDTO.operacaoValue',		value: T.get('operacaoModal')});
		data.push({name:'tipoMovimentoDTO.aprovacaoValue',		value: T.get('aprovacaoModal')});
		data.push({name:'tipoMovimentoDTO.incideDividaValue',		value: T.get('incideDividaModal')});
		
		$.postJSON(contextPath + "/tipoMovimento/salvarTipoMovimento",
				data,
				function(result){
					$( "#dialog-novo" ).dialog( "close" );					
					$(".movimentosGrid").flexReload();
					exibirMensagem('SUCCESS', ['Tipo de Movimento salvo com sucesso.']);										
				},	
				null, 
				true,
				"dialog-novo");		
		
	},
	
	this.alterarTipoMovimento = function() {
		
		var data = [];		
		data.push({name:'tipoMovimentoDTO.codigo',		value: T.get('codigoModal')});
		data.push({name:'tipoMovimentoDTO.descricao',		value: T.get('descricaoModal')});
		data.push({name:'tipoMovimentoDTO.grupoOperacaoValue',		value: T.get('grupoOperacaoModal')});
		data.push({name:'tipoMovimentoDTO.operacaoValue',		value: T.get('operacaoModal')});
		data.push({name:'tipoMovimentoDTO.aprovacaoValue',		value: T.get('aprovacaoModal')});
		data.push({name:'tipoMovimentoDTO.incideDividaValue',		value: T.get('incideDividaModal')});
		
		$.postJSON(contextPath + "/tipoMovimento/alterarTipoMovimento",
				data,
				function(result){		
					$( "#dialog-novo" ).dialog( "close" );
					$(".movimentosGrid").flexReload();
					exibirMensagem('SUCCESS', ['Tipo de Movimento alterado com sucesso.']);										
				},
				null, 
				true,
				"dialog-novo");		
	},
	
	this.preencherModal = function(codigo, descricao, grupoOperacao, operacao, aprovacao, incideDivida) {
		
		if(codigo!=null) {
			$('#grupoOperacaoModal').disable();
		} else {
			$('#grupoOperacaoModal').enable();
		}
		
		T.atualizarCombosPorGrupoOperacao(grupoOperacao);
		
		T.set('codigoModal', codigo);
		T.set('descricaoModal', descricao);
		T.set('grupoOperacaoModal', grupoOperacao);
		T.set('operacaoModal', operacao);
		T.set('aprovacaoModal', aprovacao);
		T.set('incideDividaModal', incideDivida == ('NAO_SE_APLICA') ? 'SIM' : incideDivida);
		
	},
	
	this.atualizarCombosPorGrupoOperacao = function(grupoOperacao) {
		
		var opcoes = [];
				
		if(grupoOperacao == 'FINANCEIRO') {
			opcoes.push({value:{$:'Crédito'},key:{$:'CREDITO'}});
			opcoes.push({value:{$:'Débito'},key:{$:'DEBITO'}});
			
			T.set('incideDividaModal','SIM');
			$('#incideDividaModal').disable();
			
		} else {
			opcoes.push({value:{$:'Entrada'},key:{$:'ENTRADA'}});
			opcoes.push({value:{$:'Saída'},key:{$:'SAIDA'}});
			
			$('#incideDividaModal').enable();
		}
		
		var combo =  montarComboBox(opcoes, false);
		$("#operacaoModal").html(combo);
	},
		
	this.carregarExclusao = function(index) {
		T.tipoMovimento = T.tiposMovimento[index];
		popup_excluir();
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
		
		$("#codigo").numeric();

	});
}
