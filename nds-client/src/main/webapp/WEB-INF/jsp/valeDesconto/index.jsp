<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">

<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/valeDesconto.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/produto.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaProduto.js"></script>
	<script type="text/javascript">
		var pesquisaProduto = new PesquisaProduto(produtoController.workspace);
	</script>
</head>
<body>

	<div class="areaBts">
		<div class="area">
			<span class="bt_novos">
				<a href="javascript:;" onclick="valeDescontoController.novoValeDesconto();" rel="tipsy" title="Novo Vale Desconto">
					<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0" />
				</a>
			</span>
			
			<span class="bt_arq">
				<a href="${pageContext.request.contextPath}/cadastro/valeDesconto/exportar?fileType=XLS" rel="tipsy" title="Gerar Arquivo">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
				</a>
			</span>
			
			<span class="bt_arq">
				<a href="${pageContext.request.contextPath}/cadastro/valeDesconto/exportar?fileType=PDF" rel="tipsy" title="Imprimir">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
				</a>
			</span>
			
		</div>
	</div>
	
	<div class="linha_separa_fields">&nbsp;</div>
	
	<fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
		<legend> Pesquisar Vales Desconto </legend>
		<form id="filtroValeDesconto">
			<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
				<tr>
					<td width="43">C&oacute;digo:</td>
					<td width="123" >
				    	<input type="text" name="filtro.codigo" id="codigoValeDesconto"
							   style="width: 80px; float: left; margin-right: 5px;" maxlength="10"
							   onchange="valeDescontoController.obterPorCodigo();" />
					</td>
					
					<td width="55">Nome:</td>
					<td width="237">
						<input type="text" name="filtro.nome" id="nomeValeDesconto" style="width: 222px;" maxlength="255" />
					</td>
					<td width="99">Edi&ccedil;&atilde;o:</td>
					<td width="251">
						<input type="text" id="numeroEdicao" name="filtro.numeroEdicao" style="width:200px;" />
					
					</td>
					<td width="106">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="3">Produto/Edi&ccedil;&atilde;o Cuponada:</td>
					<td colspan="3">
						<input type="hidden" name="filtro.edicaoCuponada" id="edicaoCuponada" />
						<input type="text" style="width:550px;" id="inputEdicaoCuponada" maxlength="20"/>
					</td>
					<td>
						<span class="bt_novos">
							<a href="javascript:;" onclick="valeDescontoController.pesquisar();"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a>
						</span>
					</td>
				</tr>
			</table>
		</form>
	</fieldset>
	
	<div class="linha_separa_fields">&nbsp;</div>
	
	<div class="grids" style="display:none;">
		<fieldset class="fieldGrid">
			<legend>Vales Desconto Cadastrados</legend>
			<table class="valesDescontoGrid"></table>
		</fieldset>
	</div>
	
	<form action="/produto" id="excluir_form">
		<div id="dialog-excluir" title="Excluir Vale Desconto">
			<p>Confirma a exclus&atilde;o deste Vale Desconto?</p>
		</div>
	</form>
	
	<jsp:include page="./novoValeDesconto.jsp"></jsp:include>

</body>