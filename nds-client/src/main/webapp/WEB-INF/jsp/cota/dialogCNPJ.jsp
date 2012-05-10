
<div id="dialog-cnpj" title="Nova Cota">
	
	<div id="tabpj">
            <ul>
                <li><a href="#tabpj-1">Dados Cadastrais</a></li>
                <li><a href="#tabpj-2">Endereços</a></li>
                <li><a href="#tabpj-3">Telefones</a></li>
                <li><a href="#tabpj-4">PDV</a></li>
                <li><a href="#tabpj-5">Garantia</a></li>
                <li><a href="#tabpj-6" onclick="COTA_CNPJ.carregarFornecedores()">Fornecedores</a></li>
                <li><a href="#tabpj-7">Desconto</a></li>
                <li><a href="#tabpj-8">Financeiro</a></li>
                <li><a href="#tabpj-9">Distribuição</a></li>
                <li><a href="#tabpj-10">Sócios</a></li>
          </ul>
        
        
        <div id="tabpj-1">
      		<jsp:include page="dadosBasicoCNPJ.jsp"/>
		</div>
		
		<div id="tabpj-2">
			<jsp:include page="../endereco/index.jsp">
				<jsp:param value="ENDERECO_COTA" name="telaEndereco"/>
			</jsp:include>
		</div>
		
		<div id="tabpj-3">
			<jsp:include page="../telefone/index.jsp">
				<jsp:param value="COTA" name="tela"/>
			</jsp:include>
		</div>
		
		<div id="tabpj-4">
			<jsp:include page="../pdv/index.jsp"></jsp:include>
		</div>
		
		<div id="tabpj-5">
		</div>
		
		<div id="tabpj-6">
			 <jsp:include page="fornecedor.jsp">
			 	<jsp:param value="cnpj" name="paramFornecedores"/>
			 </jsp:include> 
		</div>
		
		<div id="tabpj-7">
			<jsp:include page="desconto.jsp"></jsp:include>
		</div>
		
		<div id="tabpj-8">
			 <jsp:include page="../financeiro/index.jsp"></jsp:include> 
		</div>
		
		<div id="tabpj-9">
		</div>
	
	
		<div id="tabpj-10">
		</div>
	
	
</div>


