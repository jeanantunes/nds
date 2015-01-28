<div id="div-wrapper-dialog-novoEntregador">

	<div id="dialog-novoEntregador" title="Novo Entregador" style="display:none">
		
		<jsp:include page="../messagesDialog.jsp" />
	
		<div id="tabsNovoEntregador">
	
			<ul>
				<li>
					<a href="#dadosCadastrais" id="linkDadosCadastrais">Dados Cadastrais</a>
				</li>
				<li>
					<a href="#manutencaoEnderecos" id="linkEndereco">Endereços</a>
				</li>
				<li>
					<a href="#manutencaoTelefones" id="linkTelefone">Telefones</a>
				</li>
				<li>
					<a href="#cotas" id="linkCotas">Cotas</a>
				</li>
			</ul>
	
			<div id="dadosCadastrais">
				<fieldset style="width:765px; margin:5px;">
					<jsp:include page="dadosCadastraisPF.jsp" />
					<jsp:include page="dadosCadastraisPJ.jsp" />
				</fieldset>
				<br clear="all"/>
			</div>
			
			<div id="manutencaoEnderecos">
				<jsp:include page="../endereco/index.jsp">
				<jsp:param value="ENDERECO_ENTREGADOR" name="telaEndereco"/>
				</jsp:include>
			</div>
			
			<div id="manutencaoTelefones">
				<jsp:include page="../telefone/index.jsp">
				<jsp:param value="ENTREGADOR" name="tela"/>
				</jsp:include>
			</div>
			
			<div id="novo-entregador-cotas">
				<br clear="all"/>
					<fieldset style="width:765px; margin:5px;">
						<legend>Endereços</legend>
						<jsp:include page="cotas.jsp" />
					</fieldset>
				<br clear="all"/>
			</div>
	
		</div>
		
	</div>

</div>

<script type="text/javascript">
$(function(){
	ENDERECO_ENTREGADOR.init(entregadorController.workspace);
	ENTREGADOR.init(entregadorController.workspace);
});
</script>
