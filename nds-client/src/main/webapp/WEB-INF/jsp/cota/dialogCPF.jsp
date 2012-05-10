<div id="dialog-cpf" title="Nova Cota">

<div id="tabpf">
	<ul>
	<li><a href="#tabpf-1">Dados Cadastrais</a></li>
	<li><a href="#tabpf-2" onclick="MANTER_COTA.carregarEnderecos()">Endere�os</a></li>			
	<li><a href="#tabpf-3" onclick="MANTER_COTA.carregarTelefones()">Telefones</a></li>
	<li><a href="#tabpf-4" onclick="MANTER_COTA.carregarPDV()">PDV</a></li>
	<li><a href="#tabpf-5" onclick="MANTER_COTA.carregaFinanceiroCota();">Garantia</a></li>
	<li><a href="#tabpf-6">Fornecedor</a></li>
	<li><a href="#tabpf-7">Desconto</a></li>
	<li><a href="#tabpf-8">Financeiro</a></li>
	<li><a href="#tabpf-9">Distribui��o</a></li>
	</ul>

	<div id="tabpf-1"> 
		<jsp:include page="dadosBasicoCPF.jsp"></jsp:include>
	</div>
	
	<div id="tabpf-2">
		<jsp:include page="../endereco/index.jsp">
			<jsp:param value="ENDERECO_COTA" name="telaEndereco"/>
		</jsp:include>
	</div>
	
	<div id="tabpf-3">
		<jsp:include page="../telefone/index.jsp">
			<jsp:param value="COTA" name="tela"/>
		</jsp:include>
	</div>

	<div id="tabpf-4">
		 <jsp:include page="../pdv/index.jsp"></jsp:include>
	</div> 

	<div id="tabpf-5"> 
		Garantia!!	
	</div>
	
	<div id="tabpf-6"> 
	     <jsp:include page="fornecedor.jsp">
	     	<jsp:param value="cpf" name="paramFornecedores"/>
	     </jsp:include>
	</div>
	
	<div id="tabpf-7"> 
		<jsp:include page="desconto.jsp"></jsp:include>
	</div>
	
	<div id="tabpf-8"> 
		<jsp:include page="../financeiro/index.jsp"></jsp:include>
	</div>

	<div id="tabpf-9"> 
		Distribui��o
	</div>
</div>

</div>