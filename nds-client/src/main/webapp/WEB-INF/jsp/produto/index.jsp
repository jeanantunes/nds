<head>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/produto.js"></script>
	
	<script type="text/javascript">
		var PesquisaProduto = {
				
			pesquisarProdutosSuccessCallBack:function() {
				
				PesquisaProduto.pesquisarBox(PesquisaProduto.getCodigoProdutoPesquisa());
				PesquisaProduto.pesquisarFornecedor(PesquisaProduto.getCodigoProdutoPesquisa());
				
				$("#dataProgramada").val("");
				
			},
			
			pesquisarProdutosErrorCallBack: function() {
					
				PesquisaProduto.pesquisarBox(PesquisaProduto.getCodigoProdutoPesquisa());
				PesquisaProduto.pesquisarFornecedor(PesquisaProduto.getCodigoProdutoPesquisa());
				
				$("#dataProgramada").val("");
				
			},

			getCodigoProdutoPesquisa: function () {
				return  "codigoProduto=" + $("#codigoProduto").val();
			},
			
			pesquisarBox:function(data) {
				
				$.postJSON("<c:url value='/devolucao/chamadaEncalheAntecipada/pesquisarBox' />",
						   data, PesquisaProduto.montarComboBoxs);
			},		

			montarComboBoxs:function(result) {
				var comboBoxes = "<option selected='selected'  value='-1'></option>";  
				
				comboBoxes = comboBoxes + montarComboBox(result, true);
				
				$("#box").html(comboBoxes);
			}, 

			pesquisarFornecedor:function(data){
			
				$.postJSON("<c:url value='/devolucao/chamadaEncalheAntecipada/pesquisarFornecedor' />",
						   data, PesquisaProduto.montarComboFornecedores);
			},

			montarComboFornecedores:function(result) {
				var comboFornecedores =  montarComboBox(result, true);
				
				$("#fornecedor").html(comboFornecedores);
			},
			
			validarEdicaoSuccessCallBack : function(){
				
				 var data = [{name:"codigoProduto",value:$("#codigoProduto").val()},
	             			 {name:"numeroEdicao",value:$("#edicao").val()},
							];
				
				 $.postJSON("<c:url value='/devolucao/chamadaEncalheAntecipada/pesquisarDataProgramada' />",
						   data, function(result) {
					 $("#dataProgramada").val(result);
				 });
			},
			
			validarEdicaoErrorCallBack: function() {
				 $("#dataProgramada").val("");
			},
		};

		$(function() {
			
			inicializar();
		});
		
		function iniciarGrid() {
			$(".produtosGrid").flexigrid({
				preProcess: executarPreProcessamento,
				dataType : 'json',
				colModel : [ {
					display : 'C&oacute;digo',
					name : 'codigo',
					width : 60,
					sortable : true,
					align : 'left'
				}, {
					display : 'Produto',
					name : 'produtoDescricao',
					width : 180,
					sortable : true,
					align : 'left'
				}, {
					display : 'Tipo Produto',
					name : 'tipoProdutoDescricao',
					width : 80,
					sortable : true,
					align : 'left'
				}, {
					display : 'Editor',
					name : 'nomeEditor',
					width : 190,
					sortable : true,
					align : 'left'
				}, {
					display : 'Fornecedor',
					name : 'tipoContratoFornecedor',
					width : 150,
					sortable : true,
					align : 'left'
				}, {
					display : 'PEB',
					name : 'peb',
					width : 60,
					sortable : true,
					align : 'center'
				}, {
					display : 'Situa&ccedil;&atilde;o',
					name : 'situacao',
					width : 60,
					sortable : true,
					align : 'center'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 60,
					sortable : false,
					align : 'center'
				}],
				sortname : "codigo",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 255,
				singleSelect : true
			});
		}

		function inicializar() {

			iniciarGrid();
		}

		function pesquisar() {
			
			var codigo = $("#codigoProduto").val();
			var produto = $("#produto").val();
			var periodicidade = $("#periodicidade").val();
			var fornecedor = $("#fornecedor").val();
			var editor = $("#edicao").val();
			var codigoTipoProduto = $("#comboTipoProduto").val();
			
			$(".produtosGrid").flexOptions({
				url: "<c:url value='/produto/pesquisarProdutos' />",
				params: [{name:'codigo', value: codigo },
					     {name:'produto', value: produto },
					     {name:'fornecedor', value: fornecedor },
					     {name:'editor', value: editor },
					     {name:'codigoTipoProduto', value : codigoTipoProduto}],
				newp: 1,
			});
			
			$(".produtosGrid").flexReload();
		}

		function editarProduto(id) {

		}

		function removerProduto(id) {
			
			$("#dialog-excluir").dialog( {
				resizable : false,
				height : 'auto',
				width : 450,
				modal : true,
				buttons : {
					"Confirmar" : function() {
						
						$.postJSON("<c:url value='/produto/removerProduto' />", 
								   "id=" + id,
								   function(result) {
								   		
								   		$("#dialog-excluir").dialog("close");
								   		
										var tipoMensagem = result.tipoMensagem;
										var listaMensagens = result.listaMensagens;
										
										if (tipoMensagem && listaMensagens) {
											
											exibirMensagem(tipoMensagem, listaMensagens);
										}
												
										$(".produtosGrid").flexReload();
								   },
								   null,
								   true
						);
					},
					"Cancelar" : function() {
						$(this).dialog("close");
					}
				},
				beforeClose: function() {
					clearMessageDialogTimeout();
				}
			});
		}
		
		function executarPreProcessamento(resultado) {

			$.each(resultado.rows, function(index, row) {
				
				var linkAprovar = '<a href="javascript:;" onclick="editarProduto(' + row.cell.id + ');" style="cursor:pointer">' +
						     	  	'<img title="Editar" src="${pageContext.request.contextPath}/images/ico_editar.gif" hspace="5" border="0px" />' +
						  		  '</a>';
				
				var linkExcluir = '<a href="javascript:;" onclick="removerProduto(' + row.cell.id + ');" style="cursor:pointer">' +
								   	 '<img title="Excluir" src="${pageContext.request.contextPath}/images/ico_excluir.gif" hspace="5" border="0px" />' +
								   '</a>';
				
				row.cell.acao = linkAprovar + linkExcluir;
			});
				
			$(".grids").show();
			
			return resultado;
		}
		
	</script>

	<style>
		label { 
			vertical-align:super; 
		}
		
		#dialog-novo label { 
			width:370px; margin-bottom:10px; float:left; font-weight:bold; line-height:26px; 
		}
		
		.ui-tabs .ui-tabs-panel {
		   padding: 6px!important;
		}
	</style>

</head>

<body>

	<div id="dialog-excluir" title="Excluir Produto">
		<p>Confirma a exclus&atilde;o deste Produto?</p>
	</div>

	<fieldset class="classFieldset">
		<legend> Pesquisar Produtos</legend>
		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
			<tr>
				<td width="43">C&oacute;digo:</td>
				<td width="123" >
					
			    	<input type="text" name="codigoProduto" id="codigoProduto"
						   style="width: 80px; float: left; margin-right: 5px;" maxlength="255"
						   onchange="produto.pesquisarPorCodigoProduto('#codigoProduto', '#produto', '#edicao', false,
								   									   PesquisaProduto.pesquisarProdutosSuccessCallBack,
								   									   PesquisaProduto.pesquisarProdutosErrorCallBack);" />
				</td>
				
				<td width="55">Produto:</td>
				<td width="237">
					<input type="text" name="produto" id="produto" style="width: 222px;" maxlength="255"
					       onkeyup="produto.autoCompletarPorNomeProduto('#produto', false);"
					       onblur="produto.pesquisarPorNomeProduto('#codigoProduto', '#produto', '#edicao', false,
														    	   PesquisaProduto.pesquisarProdutosSuccessCallBack,
														    	   PesquisaProduto.pesquisarProdutosErrorCallBack);"/>
				</td>
				<td width="99">Fornecedor:</td>
				<td width="251"><input type="text" id="fornecedor" style="width:200px;"/></td>
				<td width="106">&nbsp;</td>
			</tr>
			<tr>
				<td>Editor:</td>
				<td colspan="3" >
					<input type="text" style="width:410px;" name="edicao" id="edicao" maxlength="20" disabled="disabled"
							   onchange="produto.validarNumEdicao('#codigoProduto', '#edicao', false,
							   										PesquisaProduto.validarEdicaoSuccessCallBack,
						    	   									PesquisaProduto.validarEdicaoErrorCallBack);"/>
				</td>
				<td>Tipo de Produto:</td>
				<td>
					<select id="comboTipoProduto" style="width:207px;">
						<option value="-1">Selecione...</option>
						<c:forEach items="${listaTipoProduto}" var="tipoProduto" >
							<option value="${tipoProduto.id}">${tipoProduto.descricao}</option>
						</c:forEach>
					</select>
				</td>
				<td>
					<span class="bt_pesquisar">
						<a href="javascript:;" onclick="pesquisar();">Pesquisar</a>
					</span>
				</td>
			</tr>
		</table>
	</fieldset>

	<div class="linha_separa_fields">&nbsp;</div>
	
	<fieldset class="classFieldset">
		<legend>Produtos Cadastrados</legend>
			<div class="grids" style="display:none;">
				<table class="produtosGrid"></table>
			</div>
	
		<span class="bt_novos" title="Novo">
			<a href="javascript:;" onclick="popup();">
				<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0" />Novo
			</a>
		</span>
	</fieldset>
	
	<div class="linha_separa_fields">&nbsp;</div>

</body>