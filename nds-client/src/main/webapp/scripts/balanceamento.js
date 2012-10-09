function Balanceamento(pathTela, descInstancia) {
	
	var T = this;
	
	this.instancia = descInstancia;
	
	/**
	 * Monta a coluna de nome do produto
	 * 
	 * @param idProdutoEdicao
	 * @param idProdutoEdicao
	 * @param possuiFuro
	 * 
	 * @return Coluna de nome de produto
	 */
	this.getColunaProduto = function(idProdutoEdicao, nomeProduto, possuiFuro) {
		
		var colunaProduto = "";
		
		if (possuiFuro) {
		
			colunaProduto += '<img src="' + contextPath + '/images/ico_detalhes.png" title="Produto com furo" hspace="5" style="cursor:pointer;" />';
		}
		
		colunaProduto += '<a href="javascript:;" onclick="' + T.instancia +'.obterDetalheProduto('+idProdutoEdicao+');">'+nomeProduto+'</a>'; 
		
		return colunaProduto;
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
	 * Popula Popup de detalhes do produto.
	 * OBS: Específico para matrizLancamento\index.jsp
	 * @param result
	 */
	this.popularDetalheProduto = function(result){
		
		$("#produtoEdicaoController-detalheNome").html(result.nomeProduto);
		$("#produtoEdicaoController-detalhePreco").html(result.precoCapa);
		$("#produtoEdicaoController-detalheCCapa").html(result.chamadaCapa);
		$("#produtoEdicaoController-detalhePrecoDesc").html(result.precoComDesconto);
		$("#produtoEdicaoController-detalheFornecedor").html(result.fornecedor);
		$("#produtoEdicaoController-detalheBrinde").html(result.possuiBrinde);
		
		var codigoEditor = ((typeof result.codigoEditor == 'undefined') || result.codigoEditor == null) ? "" : result.codigoEditor;
		
		var nomeEditor = ((typeof result.nomeEditor == 'undefined') || result.nomeEditor == null) ? "" : result.nomeEditor;
		
		var codigo_nome = "";
		
		if(	codigoEditor != "" && nomeEditor != ""	) {
			codigo_nome = codigoEditor + "-" + nomeEditor;
		} else {
			
			if(codigoEditor!="") {
				codigo_nome = codigoEditor;
			}
			
			if(nomeEditor!="") {
				codigo_nome = codigoEditor;
			}
			
		}
			
		$("#produtoEdicaoController-detalheEditor").html(codigo_nome);
		
		$("#produtoEdicaoController-detalhePacote").html(result.pacotePadrao);

		T.carregarImagemCapa(result.idProdutoEdicao,'129','170','Capa',"produtoEdicaoController-td_imagem_capa");
	},
	
	/**
	 * Exibe popup de detalhes do produto
	 * @param dialog: Nome do dialog
	 */
	this.popup_detalhes_prod = function(dialog){
		$( dialog ).dialog({
			resizable: false,
			height:400,
			width:760,
			modal: true,
			buttons: [
			    {
			    	id: "dialogDetalheProdutosBtnFechar",
			    	text: "Fechar",
			    	click: function() {
			    
			    		$(this).dialog("close");
			    	}
				}
		    ]
		});
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
		
		var img = $("<img />");
		img.load(
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
	 * Popula Popup de confirmação de Balanceamento.
	 * OBS: Específico para matrizLancamento\index.jsp
	 * @param result
	 */
	this.popularConfirmacaoBalanceamento = function(result) {
		
		$("#tableConfirmaBalanceamento").clear();
		
		var conteudo = '';
		
		$.each(result, function(index, row) {

			if (row.confirmado) {
			    
				conteudo += '<tr class="class_linha_1"><td>';
				conteudo += row.mensagem;
				conteudo += '</td>';
				conteudo += '<td align="center">Confirmada</td>';
				conteudo += '<td align="center"><img src="' + contextPath + '/images/bt_check.gif" width="22" height="22" alt="Confirmado" /></td>';
				conteudo += '</tr>';
			
			} else {
	
				conteudo += '<tr class="class_linha_1"><td id=dataConfirmar_'+index+' name=dataConfirmar_'+index+' >';
				conteudo += row.mensagem;
				conteudo += '</td>';
				conteudo += '<td align="center"><input id=checkConfirmar_'+index+' name=checkConfirmar_'+index+' type="checkbox" value="" /></td>';
				conteudo += '<td align="center">&nbsp;</td>';
				conteudo += '</tr>';
			}
			
		});
		
		$("#tableConfirmaBalanceamento").append(conteudo);
	};
	
}
//@ sourceURL=balanceamento.js
