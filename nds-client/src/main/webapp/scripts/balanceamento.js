function Balanceamento(pathTela, descInstancia) {
	
	var T = this;
	
	this.tiposMovimento = []; 
	this.tipoMovimento = null;
	this.instancia = descInstancia;
	
	this.pesquisar = function() {
		
		$("#resumoPeriodo").show();				
		
		var data = [];
		
		data.push({name:'dataLancamento', value: $("#datepickerDe").val()});
		
		$("input[name='checkgroup_menu']:checked").each(function(i) {
			data.push({name:'idsFornecedores', value: $(this).val()});
		});
		
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
		
		linhasDestacadas = [];		
		lancamentosSelecionados = [];		
		$('#selTodos').uncheck();	
				
		$(".lancamentosProgramadosGrid").flexOptions({			
			url : pathTela + "/matrizLancamento/obterGridMatrizLancamento",
			dataType : 'json',		
			autoload: false,
			singleSelect: true,
			preProcess: T.processaRetornoPesquisa
		});
		
		$(".lancamentosProgramadosGrid").flexReload();
	},

	this.processaRetornoPesquisa = function(data) {
		
		if(data.mensagens) {
			exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);
			return data.rows;
		}
		
		//$("#tableResumoPeriodo").clear();
		$("#valorTotal").clear();
		
		linhasDestacadas = [];
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
		
		var emEstudoExpedido = row.cell.estudoFechado || row.cell.expedido;
		
		var linkDescProduto = T.getLinkProduto(row.cell.codigoProduto,row.cell.nomeProduto);
		row.cell.nomeProduto = linkDescProduto;
			
		if (!emEstudoExpedido) {
			var dataDistrib = '<input type="text" name="datepickerDe10" id="datepickerDe10" style="width:70px; float:left;" value="'+row.cell.dataMatrizDistrib+'"/>';
			dataDistrib+='<span class="bt_atualizarIco" title="Atualizar Datas">';
			dataDistrib+='<a href="javascript:;">&nbsp;</a></span>';
			row.cell.dataMatrizDistrib = dataDistrib;
			var id = row.cell.id.toString(); 					
			var reprogramar = '<input type="checkbox" value="'+id+'" name="checkgroup" ';
			if ($.inArray(id, lancamentosSelecionados) != -1) {
				reprogramar+='checked="checked" ';					
			}
			reprogramar+='onclick="verifyCheck($(\'#selRep\'));" />';	
			row.cell.reprogramar=reprogramar;
		} else {
			var dataDistrib = '<input type="text" disabled="disabled" style="width:70px; float:left;" value="'+row.cell.dataMatrizDistrib+'"/>';
			row.cell.dataMatrizDistrib = dataDistrib;
			row.cell.reprogramar='<input type="checkbox" name="checkgroup" value="'+row.cell.id+'" disabled="disabled" onclick="verifyCheck($(\'#selRep\'));" />';
		}
		if (row.cell.semFisico || row.cell.cancelamentoGD || row.cell.furo ) {
			linhasDestacadas.push(i+1);
		}
		
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
	 * Carrega imagem default para produtos sem imagem
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
	 * @returns
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
