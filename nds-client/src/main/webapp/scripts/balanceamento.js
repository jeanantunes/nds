function Balanceamento(pathTela, descInstancia) {
	
	var T = this;
	
	this.tiposMovimento = []; 
	this.tipoMovimento = null;
	this.instancia = descInstancia;
	this.linhasDestacadas = [];
	this.isCliquePesquisar;
	
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
			onSuccess: T.destacarLinhas,
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
			  rows+='<span class="span_1">Qtde. Exempl.:</span>';	
			  rows+='<span class="span_2">'+ resumo.qtdeExemplaresFormatada +'</span>';	
			  rows+='<span class="span_1">Peso Total:</span>';
			  rows+='<span class="span_2">'+ resumo.pesoTotalFormatado +'</span>';
			  rows+='<span class="span_1">Valor Total:</span>';
			  rows+='<span class="span_2">'+ resumo.valorTotalFormatado +'</span>';
			  rows+='</div>';
			  rows+='</td>';					  
	    });	
	    rows+="</tr>";
	    $("#tableResumoPeriodo").append(rows);	   
	
	},
	
	
	this.processarLinha = function(i,row) {
		
		var linkDescProduto = T.getLinkProduto(row.cell.codigoProduto,row.cell.nomeProduto);
		row.cell.nomeProduto = linkDescProduto;
		
		var isBloqueado = row.cell.bloquearData;
		
		row.cell.novaData = T.gerarInputDataDistrib(row.cell.novaData, isBloqueado);
		row.cell.reprogramar = T.gerarCheckReprogramar(row.cell.id.toString(), isBloqueado);
				
		if (!row.cell.possuiRecebimentoFisico || row.cell.cancelamentoGD || (row.cell.dataPrevisto!=row.cell.dataLancamentoDistribuidor) ) {
			T.linhasDestacadas.push(i+1);
		}
		
	},
	
	this.gerarInputDataDistrib = function(dataMatrizDistrib, isBloqueado) {
		return '<input type="text" name="datepickerDe10" style="width:80px; float:left;" value="' + dataMatrizDistrib + '" ' + 
			   (isBloqueado? ' disabled="disabled" ' : '') +  
			   '/>' +
		       '<span class="bt_atualizarIco" title="Atualizar Datas" ' +
		       (isBloqueado? ' style="opacity:0.5;" ' : '') + 
		       '>' +
		       '<a href="javascript:;">&nbsp;</a></span>';
	},
	
	this.gerarCheckReprogramar = function(id,isBloqueado) { 
		return '<input type="checkbox" value="'+id+'" name="checkgroup" ' +
			   (isBloqueado? ' disabled="disabled" ' : ' onclick="verifyCheck($(\'#selRep\'));" ') + 
			   ' />';	
	},
	
	this.destacarLinhas = function() {
		
		 $(T.linhasDestacadas).each(function(i, item){
			 id = '#row' + item;			    	
			 $(id).removeClass("erow").addClass("gridLinhaDestacada");
			 $(id).children("td").removeClass("sorted");
		   });
	},
	
	/**
	 * Obtém link para detalhes do produto
	 * OBS: Específico para matrizLancamento\index.jsp
	 * @param codigoProduto
	 * @param nomeProduto
	 * @return String: link para função de busca de detalhes
	 */
	this.getLinkProduto = function(codigoProduto,nomeProduto) {
		return '<a href="javascript:;" onclick="' + T.instancia +'.obterDetalheProduto('+codigoProduto+');">'+nomeProduto+'</a>';
	},

	
    /**
     * Obtém detalhes do produto
     * OBS: Específico para matrizLancamento\index.jsp
     * @param codigoProduto
     */
	this.obterDetalheProduto = function (codigoProduto){
		var data = [];
		data.push({name:'codigoProduto', value: codigoProduto});
		
		$.postJSON(
			pathTela + "/matrizLancamento/obterDetalheProduto", 
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
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				},
			}
		});
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
}
