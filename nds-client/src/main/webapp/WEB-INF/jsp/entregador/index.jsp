<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/entregador.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pessoa.js"></script>
<script type="text/javascript">
	$(function() {
		entregadorController.init();
	});
</script>
<style>
#dialog-pdv{display:none!important;}
.diasFunc label, .finceiro label, .materialPromocional label{ vertical-align:super;}
.complementar label{ vertical-align:super; margin-right:5px; margin-left:5px;}
</style>
<script>
	
</script>
</head>
<body>

	<div id="dialog-excluir-entregador" title="Excluir Entregador" style="display:none">
		<p>Confirma a exclusão deste Entregador?</p>
	</div>


		<div class="areaBts">
			<div class="area">
				<span class="bt_novos">
		           	<a href="javascript:;" id="linkBtnNovoEntregadorPF" rel="tipsy" title="Incluir Novo Entregador Pessoa Física">
		           		<img src="${pageContext.request.contextPath}/images/ico_jornaleiro.gif" hspace="5" border="0"/>
		           	</a>
		        </span>

		        <span class="bt_novos">
		        	<a href="javascript:;" id="linkBtnNovoEntregadorPJ" rel="tipsy" title="Incluir Novo Entregador Pessoa Juridica">
		        		<img src="${pageContext.request.contextPath}/images/ico_usuarios1.gif" hspace="5" border="0"/>
		        	</a>
		        </span>
			</div>
		</div>
    <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="fieldFiltro" title="Capataz, Entregador, Mula, Carreteiro">
   	    <legend> Pesquisar Entregador</legend>
   	    
   	    <form id="formularioPesquisaEntregadores">
   	    
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="117">Nome/Razão Social:</td>
              <td colspan="3">
              	<input type="text" name="filtroEntregador.nomeRazaoSocial" id="filtroEntregador-nomeRazaoSocial" style="width:160px;" 
              	onkeyup='PESSOA.autoCompletarPorNomeEntregador("#filtroEntregador-nomeRazaoSocial",entregadorController.workspace)'/>
              </td>
                <td width="146">Apelido / Nome Fantasia:</td>
              <td width="145">
              	<input type="text" name="filtroEntregador.apelidoNomeFantasia" id="filtroEntregador-apelidoNomeFantasia" style="width:130px;"
              	onkeyup='PESSOA.autoCompletarPorApelidoEntregador("#filtroEntregador-apelidoNomeFantasia",entregadorController.workspace)'/>
              </td>
              <td width="79">CPF / CNPJ:</td>
              <td width="152">
              	<input type="text" name="filtroEntregador.cpfCnpj" id="textfield" style="width:150px;"/>
              </td>
              <td width="106">
              	<span class="bt_novos" id="btnPesquisar">
              		<a href="javascript:;" id="linkBtnPesquisar"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a>
              	</span>
              </td>
            </tr>
          </table>
         </form>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      
      <div class="grids" style="display:none;">
	      <fieldset class="fieldGrid">
	       	  <legend title="Capataz, Entregador, Mula, Carreteiro">Entregadores Cadastrados</legend>
	        	<table class="pessoasGrid"></table>
	      </fieldset>
      </div>

	
	<jsp:include page="novoEntregador.jsp" />
</body>