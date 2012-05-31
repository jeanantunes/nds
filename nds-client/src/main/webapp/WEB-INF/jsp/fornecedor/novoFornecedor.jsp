<div id="dialogNovoFornecedor" title="Novo Fornecedor" style="display:none">

	<jsp:include page="../messagesDialog.jsp" />

	<div id="tabFornecedores">
	
		<ul>
			<li><a href="#dadosCadastrais">Dados Cadastrais</a></li>
			
			<li><a href="#manutencaoEnderecos" onclick="ENDERECO_FORNECEDOR.popularGridEnderecos()">Endereços</a></li>
			
			<li><a href="#manutencaoTelefones" onclick="FORNECEDOR.carregarTelefones()">Telefones</a></li>
		</ul>
	
		<div id="dadosCadastrais">
			<jsp:include page="dadosCadastrais.jsp" />
		</div>
		
		<div id="manutencaoEnderecos">
			<jsp:include page="../endereco/index.jsp">
				<jsp:param value="ENDERECO_FORNECEDOR" name="telaEndereco"/>
			</jsp:include>
		</div>
		
		<div id="manutencaoTelefones">
			<jsp:include page="../telefone/index.jsp">
				<jsp:param value="FORNECEDOR" name="tela"/>
			</jsp:include>
		</div>
	</div>

</div>