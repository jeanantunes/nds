<head>
	
	
	<script type="text/javascript">

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
					name : 'produto',
					width : 180,
					sortable : true,
					align : 'left'
				}, {
					display : 'Tipo Produto',
					name : 'tipoProduto',
					width : 80,
					sortable : true,
					align : 'left'
				}, {
					display : 'Editor',
					name : 'editor',
					width : 190,
					sortable : true,
					align : 'left'
				}, {
					display : 'Fornecedor',
					name : 'fornecedor',
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
					display : 'Situa&ccedil;�o',
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
			
			var codigo = $("#codigo").val();
			var produto = $("#produto").val();
			var periodicidade = $("#periodicidade").val();
			var fornecedor = $("#fornecedor").val();
			var editor = $("#editor").val();
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
				<td width="123" ><input type="text" id="codigo" style="width:100px;"/></td>
				<td width="55">Produto:</td>
				<td width="237"><input type="text" id="produto" style="width:222px;"/></td>
				<td width="99">Fornecedor:</td>
				<td width="251"><input type="text" id="fornecedor" style="width:200px;"/></td>
				<td width="106">&nbsp;</td>
			</tr>
			<tr>
				<td>Editor:</td>
				<td colspan="3" ><input type="text" id="editor" style="width:410px;"/></td>
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
				<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>
				Novo
			</a>
		</span>
	</fieldset>
	
	<div class="linha_separa_fields">&nbsp;</div>

</body>