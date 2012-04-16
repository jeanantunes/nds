<div id="dialog-novoEntregador" title="Novo Entregador" style="display:none">

	<div id="tabsNovoEntregador">

		<ul>
			<li>
				<a href="#dadosCadastrais">Dados Cadastrais</a>
			</li>
			<li>
				<a href="#manutencaoEnderecos" onclick="popularGridEnderecos()">Endereços</a>
			</li>
			<li>
				<a href="#manutencaoTelefones" onclick="carregarTelefones()">Telefones</a>
			</li>
		</ul>

		<div id="dadosCadastrais">
			<jsp:include page="dadosCadastraisPF.jsp" />
			<jsp:include page="dadosCadastraisPJ.jsp" />
		</div>
		
		<jsp:include page="../endereco/index.jsp" />
		<jsp:include page="../telefone/index.jsp" />

	</div>
	
</div>