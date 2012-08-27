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
			<br clear="all"/>
			<fieldset style="width:770px; margin-left:10px; margin-bottom:5px;">
				<legend>Entregador - Pessoa Física</legend>
				<jsp:include page="dadosCadastraisPF.jsp" />
				<jsp:include page="dadosCadastraisPJ.jsp" />
			</fieldset>
			<br clear="all"/>
		</div>
		
		<div id="manutencaoEnderecos">
			<br clear="all"/>
			<fieldset style="width:770px; margin-left:10px; margin-bottom:5px;">
				<legend>Endereços</legend>
					<jsp:include page="../endereco/index.jsp">
					<jsp:param value="ENDERECO_ENTREGADOR" name="telaEndereco"/>
					</jsp:include>
			</fieldset>
			<br clear="all"/>
		</div>
		
		<div id="manutencaoTelefones">
			<br clear="all"/>
				<fieldset style="width:770px; margin-left:10px; margin-bottom:5px;">
					<legend>Endereços</legend>
					<jsp:include page="../telefone/index.jsp">
					<jsp:param value="ENTREGADOR" name="tela"/>
					</jsp:include>
				</fieldset>
			<br clear="all"/>
		</div>
		
		<div id="cotas">
			<br clear="all"/>
				<fieldset style="width:770px; margin-left:10px; margin-bottom:5px;">
					<legend>Endereços</legend>
					<jsp:include page="cotas.jsp" />
				</fieldset>
			<br clear="all"/>
		</div>

	</div>
	
</div>

<script type="text/javascript">
$(function(){
	ENDERECO_ENTREGADOR.init(entregadorController.workspace);
	ENTREGADOR.init(entregadorController.workspace);
});
</script>
