<div id="fornecedorController-dialogNovoFornecedor" title="Novo Fornecedor" style="display:none">

	<jsp:include page="../messagesDialog.jsp" />

	<div id="fornecedorController-tabFornecedores">
	
		<ul>
			<li><a href="#fornecedorController-dadosCadastrais">Dados Cadastrais</a></li>
			
			<li><a href="#fornecedorController-manutencaoEnderecos" onclick="ENDERECO_FORNECEDOR.popularGridEnderecos()">Endereços</a></li>
			
			<li><a href="#fornecedorController-manutencaoTelefones" onclick="FORNECEDOR.carregarTelefones()">Telefones</a></li>
		</ul>
	
		<div id="fornecedorController-dadosCadastrais">
			<jsp:include page="dadosCadastrais.jsp" />
		</div>
		
		<div id="fornecedorController-manutencaoEnderecos">
			<jsp:include page="../endereco/index.jsp">
				<jsp:param value="ENDERECO_FORNECEDOR" name="telaEndereco"/>
			</jsp:include>
		</div>
		
		<div id="fornecedorController-manutencaoTelefones">
			<jsp:include page="../telefone/index.jsp">
				<jsp:param value="FORNECEDOR" name="tela"/>
			</jsp:include>
		</div>
	</div>

</div>