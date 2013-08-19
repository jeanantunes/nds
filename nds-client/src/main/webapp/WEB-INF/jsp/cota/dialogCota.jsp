
<div id="dialog-cancelar-cadastro-cota" title="Cota" style="display: none;">
	<p>Informações das abas não salvas serão perdidas. Confirma ?</p>
</div>

<div id="workspaceCota">
<div id="dialog-cota" title="Nova Cota" style="display: none">
	
	<jsp:include page="../messagesDialog.jsp">
		<jsp:param value="dialog-cota" name="messageDialog"/>
	</jsp:include>
	
	<div id="tabCota">
           
            <ul>
                <li><a href="#tabCota-1">Dados Cadastrais</a></li>
                <li><a href="#tabCota-2">Endereços</a></li>
                <li><a href="#tabCota-3">Telefones</a></li>
                <li><a href="#tabCota-4">PDV</a></li>
                <li><a href="#tabCota-5">Garantia</a></li>
                <li><a href="#tabCota-6">Fornecedores</a></li>
                <li><a href="#tabCota-7">Desconto</a></li>
                <li><a href="#tabCota-8">Financeiro</a></li>
                <li><a href="#tabCota-9" onclick="MANTER_COTA.verificarEntregador();">Distribuição</a></li>
                <li><a href="#tabCota-10" id="idTabSocio">Sócios</a></li>
          </ul>
        
        <div id="tabCota-1">
      		<jsp:include page="dadosBasicoCNPJ.jsp"/>
      		<jsp:include page="dadosBasicoCPF.jsp"/>
            
            <div id="dialog-titular" title="Alterar Titularidade" style="display:none;">
              <fieldset>
                  <legend>Novo Titular</legend>
                  <span class="bt_novos">
                      <a href="javascript:;" onclick="MANTER_COTA.alterarTitular(true);" rel="tipsy" title="Novo Titular Pessoa Física">
                        <img src="${pageContext.request.contextPath}/images/ico_jornaleiro.gif" hspace="5" border="0"/>
                      </a>
                  </span>
                  <span class="bt_novos" style="margin-left:50px;">
                    <a href="javascript:;" onclick="MANTER_COTA.alterarTitular()" rel="tipsy" title="Novo Titular Pessoa Jurídica">
                      <img src="${pageContext.request.contextPath}/images/ico_usuarios.gif" border="0"/>
                     </a>
                   </span>
              </fieldset>
            </div>
		</div>
		
		<div id="tabCota-2">
			<jsp:include page="../endereco/index.jsp">
				<jsp:param value="ENDERECO_COTA" name="telaEndereco"/>
				<jsp:param value="dialog-cota" name="message"/>
			</jsp:include>
		</div>
		
		<div id="tabCota-3">
			<jsp:include page="../telefone/index.jsp">
				<jsp:param value="COTA" name="tela"/>
				<jsp:param value="dialog-cota" name="message"/>
			</jsp:include>
		</div>
		
		<div id="tabCota-4">
			<jsp:include page="../pdv/index.jsp"></jsp:include>
		</div>
		
		<div id="tabCota-5">
			<jsp:include page="../cotaGarantia/index.jsp"></jsp:include>
		</div>
		
		<div id="tabCota-6">
			 <jsp:include page="fornecedor.jsp">
			 	<jsp:param value="cnpj" name="paramFornecedores"/>
			 </jsp:include> 
		</div>
		
		<div id="tabCota-7">
			<jsp:include page="desconto.jsp"></jsp:include>
		</div>
		
		<div id="tabCota-8">
			 <jsp:include page="../parametroCobrancaCota/index.jsp"></jsp:include> 
		</div>
		
		<div id="tabCota-9">
			<jsp:include page="../distribuicao/distribuicao.jsp">
				<jsp:param value="DISTRIB_COTA" name="tela"/>
			</jsp:include>
		</div>
		
		<div id="tabCota-10">
			<jsp:include page="socio.jsp"/>
		</div>
		
	</div>

</div>
</div>

<script type="text/javascript">
$(function(){
	ENDERECO_COTA.init(MANTER_COTA.workspace);
	COTA.init(MANTER_COTA.workspace);
});
</script>

