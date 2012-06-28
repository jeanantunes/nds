function Balanceamento(pathTela, descInstancia) {
	
	var T = this;
	
	this.tiposMovimento = []; 
	this.tipoMovimento = null;
	this.instancia = descInstancia;
	this.linhasDestacadas = [];
	this.isCliquePesquisar;
	this.lancamentos = [];
	
	this.pesquisar = function() {
		
		$("#resumoPeriodo").show();				
		
		var data = [];
		
		data.push({name:'dataLancamento', value: $("#datepickerDe").val()});
		
		$("input[name='checkgroup_menu']:checked").each(function(i) {
			data.push({name:'idsFornecedores', value: $(this).val()});
		});
		
		isCliquePesquisar = true;
		
		$.postJSON(
			pathTela + "/matrizLancamento/obterMatrizLancamento", 
			data,
			function(result) {
				
				T.popularResumoPeriodo(result);
				
				T.carregarGrid();
			},
			function() {

				$("#resumoPeriodo").hide();
			}
		);
	},
	


	this.verificarBalanceamentosAlterados = function(funcao) {

		$.postJSON(
			pathTela + "/matrizLancamento/verificarBalanceamentosAlterados",
			null,
			function(result){
				
				if (result == "true") 
					T.retornoVerificarBalanceamentosAlterados(funcao);
				else
					funcao();
			}
		);
	},
			
	this.carregarGrid = function() {		
				
		$(".grids").show();		
		
		T.linhasDestacadas = [];		
		lancamentosSelecionados = [];		
		$('#selTodos').uncheck();	
				
		$(".lancamentosProgramadosGrid").flexOptions({			
			url : pathTela + "/matrizLancamento/obterGridMatrizLancamento",
			dataType : 'json',		
			autoload: false,
			singleSelect: true,
			preProcess: T.processaRetornoPesquisa,
			onSuccess: T.onSuccessPesquisa,
			onSubmit: T.confirmarPaginacao
		});
		
		$(".lancamentosProgramadosGrid").flexReload();
	},


	this.confirmarPaginacao = function() {
		
		var noSelect = $('[name=checkgroup]:checked').size() == 0;
		
		if(isCliquePesquisar || noSelect ) {
			isCliquePesquisar = false;
			return true;
		}
		
		return confirm('As seleções de lançamentos não serão salvas,deseja continuar?');
	},
	
	this.processaRetornoPesquisa = function(data) {
		
		if(data.mensagens) {
			exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);
			return data.rows;
		}
		
		//$("#tableResumoPeriodo").clear();
		$("#valorTotal").clear();
		
		T.linhasDestacadas = [];
		T.lancamentos = [];
		
		$("#valorTotal").html(data[1]);
		$.each(data[0].rows, function(index,row){ T.processarLinha(index, row);});
		return data[0];
	},
		
	this.popularResumoPeriodo = function(data) {
	
		$("#tableResumoPeriodo").clear();
		
		var rows='<tr>';
		$.each(data.listaResumoPeriodoBalanceamento, function(index, resumo){
			  rows+='<td>';
			  rows+='<div class="box_resumo">';
			  rows+='<label>'+ resumo.dataFormatada +'</label>';
			  rows+='<span class="span_1">Qtde. Títulos:</span>';	 
			  rows+='<span class="span_2">'+ resumo.qtdeTitulos +'</span>';
			  
			  if (resumo.excedeCapacidadeDistribuidor) {
				  
				  rows+='<span class="span_1">Qtde. Exempl.:</span>';
				  rows+='<span name="qtdeExemplares" class="span_2 redLabel"';
				  rows+='title="A quantidade de exemplares excede a capacidade de manuseio ';
				  rows+=data.capacidadeRecolhimentoDistribuidor + ' do distribuidor">';
				  rows+=resumo.qtdeExemplaresFormatada + '</span>';
			  
			  } else {
				  
				  rows+='<span class="span_1">Qtde. Exempl.:</span>';	
				  rows+='<span class="span_2">' + resumo.qtdeExemplaresFormatada + '</span>';
			  }
			  
			  rows+='<span class="span_1">Qtde. Parciais:</span>';
			  rows+='<span class="span_2">' + resumo.qtdeTitulosParciais + '</span>';
			  rows+='<span class="span_1">Peso Total:</span>';
			  rows+='<span class="span_2">'+ resumo.pesoTotalFormatado +'</span>';
			  rows+='<span class="span_1">Valor Total:</span>';
			  rows+='<span class="span_2">'+ resumo.valorTotalFormatado +'</span>';
			  rows+='</div>';
			  rows+='</td>';					  
	    });	
	    
		rows+="</tr>";
	    
	    $("#tableResumoPeriodo").append(rows);
	    
	    $("span[name='qtdeExemplares']").tooltip();
	},
	
	
	this.processarLinha = function(i,row) {
		
		var linkDescProduto = T.getLinkProduto(row.cell.idProdutoEdicao,row.cell.nomeProduto);
		T.lancamentos.push({
			id:				row.cell.id, 
			numEdicao:		row.cell.numEdicao,
			nomeProduto:	row.cell.nomeProduto
		});
		row.cell.nomeProduto = linkDescProduto;
		
		row.cell.novaData = T.gerarInputDataDistrib(row.cell.novaData, row.cell.bloquearData, i);
		row.cell.reprogramar = T.gerarCheckReprogramar(row.cell.id.toString(), row.cell.bloquearData,i);
				
		if (!row.cell.possuiRecebimentoFisico || row.cell.cancelamentoGD || (row.cell.dataPrevisto!=row.cell.dataLancamentoDistribuidor) ) {
			T.linhasDestacadas.push(i+1);
		}
		
	},
	
	this.gerarInputDataDistrib = function(dataMatrizDistrib, isBloqueado, index) {
		
		return '<input onblur="B.alterarData(this,\'' + index + '\');" type="text" name="dataNova" style="width:80px; float:left;" value="' + dataMatrizDistrib + '" ' + 
			   (isBloqueado? ' disabled="disabled" ' : '') +  
			   '/>' +
		       '<span class="bt_atualizarIco" title="Atualizar Datas" ' +
		       (isBloqueado? ' style="opacity:0.5;" ' : '') + 
		       '>' +
		       '<a href="javascript:;" ' + 
		       (isBloqueado? '' : ' onclick="B.reprogramarLancamentoUnico(' + index + ');') +
		       '">&nbsp;</a></span>';
		
	},
	
	this.reprogramarLancamentoUnico = function(index) {
		
		var data = [];
		
		data.push({name: 'produtoLancamento.id', 			value: T.lancamentos[index].id});
		data.push({name: 'produtoLancamento.novaData', 		value: T.lancamentos[index].novaData});
		data.push({name: 'produtoLancamento.nomeProduto', 	value: T.lancamentos[index].nomeProduto});
		data.push({name: 'produtoLancamento.numEdicao', 	value: T.lancamentos[index].numEdicao});
		
		
		$.postJSON(
				pathTela + "/matrizLancamento/reprogramarLancamentoUnico",
				data,
				null
			);
	},
	
	this.alterarData = function(input, index) {
		T.lancamentos[index].novaData = input.value;
	},
	
	this.gerarCheckReprogramar = function(id,isBloqueado,index) { 
		return '<input type="checkbox" value="'+id+'" name="checkgroup" ' +
			   (isBloqueado? ' disabled="disabled" ' : ' onclick="B.selecionarCheck(this,\'' + index + '\');" ') + 
			   ' />';	
	},
	
	this.selecionarCheck = function(check, index) {
		
		T.lancamentos[index].selecionado = check.checked;
		
		$("#selTodos").uncheck();
	},
	
	this.onSuccessPesquisa = function() {
		
		$(T.linhasDestacadas).each(function(i, item){
			 id = '#row' + item;			    	
			 $(id).removeClass("erow").addClass("gridLinhaDestacada");
			 $(id).children("td").removeClass("sorted");
		});
		 

		$("input[name='dataNova']").datepicker({
			dateFormat: 'dd/mm/yy'
		});
		
		$("input[name='dataNova']").mask("99/99/9999");
	},
	
	/**
	 * Obtém link para detalhes do produto
	 * OBS: Específico para matrizLancamento\index.jsp
	 * @param idProdutoEdicao
	 * @param idProdutoEdicao
	 * @return String: link para função de busca de detalhes
	 */
	this.getLinkProduto = function(idProdutoEdicao,nomeProduto) {
		return '<a href="javascript:;" onclick="' + T.instancia +'.obterDetalheProduto('+idProdutoEdicao+');">'+nomeProduto+'</a>';
	},

	
    /**
     * Obtém detalhes do produto
     * OBS: Específico para matrizLancamento\index.jsp
     * @param idProdutoEdicao
     */
	this.obterDetalheProduto = function (idProdutoEdicao){
		var data = [];
		data.push({name:'idProdutoEdicao', value: idProdutoEdicao});
		
		$.postJSON(
			pathTela + "/cadastro/edicao/obterDetalheProduto.json", 
			data,
			function(result) {
				T.popularDetalheProduto(result);
				T.popup_detalhes_prod( "#dialog-detalhe-produto" );
			},
			function() {
				$("#dialog-detalhe-produto").hide();
			}
		);
	},
	
	
	/**
	 * Retorna componente 'img' com imagem default para produtos sem imagem
	 * @param w: propriedade width
	 * @param h: propriedade height
	 * @param a: propriedade alt
	 */
	this.carregarImagemCapaDefault = function(w,h,a) {
		
		var imgDefault = $("<img />")
		.attr('src', contextPath + "/capas/capa_sem_imagem.jpg")
		.attr('width', w)
		.attr('height', h)
		.attr('alt', a);
		
		return imgDefault;
	},
	

	/**
	 * Carrega imagem de Produto Edição
	 * @param idProdutoEdicao
	 * @param w: propriedade width
	 * @param h: propriedade height
	 * @param a: propriedade alt
	 * @param recipiente: Componente html onde aparecerá a imagem
	 */
	this.carregarImagemCapa = function(idProdutoEdicao,w,h,a,recipiente) {

		$("#"+recipiente).empty();
		
		var img = $("<img />")
		.load(
		    function() {						
		    	$("#"+recipiente).append(img);
		    }
		)
		.error(
		    function() {
		    	$("#"+recipiente).append(T.carregarImagemCapaDefault(w,h,a));
		    }
		)
		.attr('src', contextPath + "/capa/" + idProdutoEdicao)
		.attr('width', w)
		.attr('height', h)
		.attr('alt', a);
	},
	
	
	/**
	 * Popula Popup de detalhes do produto.
	 * OBS: Específico para matrizLancamento\index.jsp
	 * @param result
	 */
	this.popularDetalheProduto = function(result){
		$("#detalheNome").html(result.nomeProduto);
		$("#detalhePreco").html(result.precoCapa);
		$("#detalheCCapa").html(result.chamadaCapa);
		$("#detalhePrecoDesc").html(result.precoComDesconto);
		$("#detalheFornecedor").html(result.fornecedor);
		$("#detalheBrinde").html(result.possuiBrinde);
		$("#detalheEditor").html(result.codigoEditor+"-"+result.nomeEditor);
		$("#detalhePacote").html(result.pacotePadrao);

		T.carregarImagemCapa(result.idProdutoEdicao,'129','170','Capa',"td_imagem_capa");
	},

	
	/**
     * Obtém tela de confirmação de Balanceamento
     * OBS: Específico para matrizLancamento\index.jsp
     * @param codigoProduto
     */
	this.obterConfirmacaoBalanceamento = function (){
		$.postJSON(
			pathTela + "/matrizLancamento/obterAgrupamentoDiarioBalanceamento", 
			null,
			function(result) {
				T.popularConfirmacaoBalanceamento(result);
				T.popup_confirmar_balanceamento( "#dialog-confirm-balanceamento" );
			},
			function() {
				$("#dialog-confirm-balanceamento").hide();
			}
		);
	},
	
	
	/**
	 * Popula Popup de confirmação de Balanceamento.
	 * OBS: Específico para matrizLancamento\index.jsp
	 * @param result
	 */
	this.popularConfirmacaoBalanceamento = function(result){
		
		$("#tableConfirmaBalanceamento").clear();
		
		var conteudo = '';
		
		$.each(result.rows, function(index,row){

			if (row.cell.confirmado){
			    
				conteudo += '<tr class="class_linha_1"><td>';
				conteudo += row.cell.mensagem;
				conteudo += '</td>';
				conteudo += '<td align="center">Confirmada</td>';
				conteudo += '<td align="center"><img src="images/bt_check.gif" width="22" height="22" alt="Confirmado" /></td>';
				conteudo += '</tr>';
			}    
			else{
	
				conteudo += '<tr class="class_linha_1"><td id=dataConfirmar_'+index+' name=dataConfirmar_'+index+' >';
				conteudo += row.cell.mensagem;
				conteudo += '</td>';
				conteudo += '<td align="center"><input id=checkConfirmar_'+index+' name=checkConfirmar_'+index+' type="checkbox" value="" /></td>';
				conteudo += '<td align="center">&nbsp;</td>';
				conteudo += '</tr>';
			}
			
		});
		
		$("#tableConfirmaBalanceamento").append(conteudo);
	},
	
	
	/**
	 * Obtém datas marcadas da confirmação do balanceamento
	 * @returns dividasMarcadas
	 */
	this.obterDatasMarcadasConfirmacao = function(){

		var datasConfirmadas='';
		var table = document.getElementById("tableConfirmaBalanceamento");
		
		for(var i = 0; i < table.rows.length; i++){   
			
			if(document.getElementById("checkConfirmar_"+i)!=null){
				
				if (document.getElementById("checkConfirmar_"+i).checked){
				    table.rows[i].cells[0].textContent; 
				    datasConfirmadas+='datasConfirmadas='+ table.rows[i].cells[0].textContent + '&';
			    }
				
		    }

		} 
		
		return datasConfirmadas;
	},
	
	
	/**
     * Confirma a matriz de lançamento
     * OBS: Específico para matrizLancamento\index.jsp
     */
	this.confirmarMatrizLancamento = function (){

		$.postJSON(
			pathTela + "/matrizLancamento/confirmarMatrizLancamento", 
			T.obterDatasMarcadasConfirmacao(),
			function(mensagens) {
				
	           $("#dialog-confirm-balanceamento").dialog("close");

			   if (mensagens){
				   var tipoMensagem = mensagens.tipoMensagem;
				   var listaMensagens = mensagens.listaMensagens;
				   if (tipoMensagem && listaMensagens) {
				       exibirMensagem(tipoMensagem, listaMensagens);
			       }
        	   }
			   
            },
			null,
			true,
			"dialog-confirmar"
		);
	},
	
	/**
	 * Exibe popup de detalhes do produto
	 * @param dialog: Nome do dialog
	 */
	this.popup_detalhes_prod = function(dialog){
		$( dialog ).dialog({
			resizable: false,
			height:300,
			width:760,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
				},
			}
		});
	},
	
	
	/**
	 * Exibe popup de confirmação de balanceamento
	 * @param dialog: Nome do dialog
	 */
	this.popup_confirmar_balanceamento = function(dialog){
		$( dialog ).dialog({
			resizable: false,
			height:'auto',
			width:300,
			modal: true,
			buttons: {
				"Confirmar": function() {
					T.confirmarMatrizLancamento();
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				},
			},
			beforeClose: function() {
				clearMessageDialogTimeout(dialog);
		    }
		});
	},

	this.voltarConfiguracaoOriginal = function() {
		var selecionado = verifyAtLeastOneChecked('checkgroup');
		if (selecionado) {
			popup_reprogramar();
		} else {
			mensagens = new Array();
			mensagens.push('Nenhum registro selecionado.');
			exibirMensagem('ERROR', mensagens);
		}
	},
	
	this.retornoVerificarBalanceamentosAlterados = function(funcao) {
			
		$("#dialog-confirm").dialog({
			resizable: false,
			height:'auto',
			width:600,
			modal: true,
			buttons: {
				"Confirmar": function() {
					
					funcao();
					
					$(this).dialog("close");
				},
				"Cancelar": function() {
					
					$(this).dialog("close");
				}
			}
		});	
	};
}
