
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/pdv.js"></script>
	
<label><strong>PDVs Cadastrados</strong></label>
<br />
<table class="PDVsGrid"></table>
<br />
<span class="bt_novo"><a href="javascript:;" onclick="PDV.poupNovoPDV();">Novo</a></span>

<br clear="all" />

<form id="idFormPDVExcluir">
	<div id="dialog-excluirPdv" title="Atenção" style="display:none">
		<p>Confirmar exclusão PDV ?</p>
	</div>
</form>

<form id="idFormPDVCancelar">
	<div id="dialog-cancelar-cadastro-pdv" title="PDV" style="display: none;">
			<p>Dados não salvos serão perdidos. Confirma o cancelamento?</p>
	</div>
</form>

<form id="idFormPDVDadosEdicao">
	<div id="dialog-pdv" title="PDV Cota">
		
		<jsp:include page="../messagesDialog.jsp">
			
			<jsp:param value="idModalPDV" name="messageDialog"/>
		
		</jsp:include>
		
		<input type="hidden" name="idPDV" id="idPDV" value=""/>
		
		<div id="tabpdv">
		    <ul>
		        <li><a href="#tabpdv-1">Dados Básicos</a></li>
		        <li><a href="#tabpdv-2" onclick="ENDERECO_PDV.popularGridEnderecos();">Endereços</a></li>
		        <li><a href="#tabpdv-3" onclick="TELEFONE_PDV.carregarTelefones();">Telefones</a></li>
		        <li><a href="#tabpdv-4">Caract. / Segmentação</a></li>
		        <li><a href="#tabpdv-6">Gerador de Fluxo</a></li>
		        <li><a href="#tabpdv-7">MAP</a></li>
		       
		  </ul>
		 	   <div id="tabpdv-1"> <jsp:include page="dadosBasico.jsp"/> </div>
			   
			   <div id="tabpdv-2"> <jsp:include page="endereco.jsp"/> </div>
			   
			   <div id="tabpdv-3"> 	<jsp:include page="telefone.jsp"/> 	</div>
			   
			   <div id="tabpdv-4"> <jsp:include page="caracteristica.jsp"/> </div>
			   
			   <div id="tabpdv-6"> <jsp:include page="geradorFluxo.jsp"/> </div>
			   
			   <div id="tabpdv-7"> <jsp:include page="map.jsp"/> </div>				
					 
			  <br clear="all" />
		</div>
		
	</div>
</form>

<form id="idFormPDVConfirmarPP">
	<div id="dialog-confirmaPontoPrincipal" title="Ponto Principal" style="display: none;">
		<p>Já existe um ponto principal, deseja substitui-lo por este?</p>
	</div>
</form>
