<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/entregador.js"></script>
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

	<div class="container">

		<div id="effect" style="padding: 0 .7em;"
			class="ui-state-highlight ui-corner-all">
			<p>
				<span style="float: left; margin-right: .3em;"
					class="ui-icon ui-icon-info">
				</span> 
			</p>
		</div>
    
      <fieldset class="classFieldset" title="Capataz, Entregador, Mula, Carreteiro">
   	    <legend> Pesquisar Entregador</legend>
   	    
   	    <form id="formularioPesquisaEntregadores">
   	    
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="117">Nome/Razão Social:</td>
              <td colspan="3">
              	<input type="text" name="filtroEntregador.nomeRazaoSocial" id="textfield2" style="width:160px;"/>
              </td>
                <td width="146">Apelido / Nome Fantasia:</td>
              <td width="145">
              	<input type="text" name="filtroEntregador.apelidoNomeFantasia" id="textfield" style="width:130px;"/>
              </td>
              <td width="79">CPF / CNPJ:</td>
              <td width="152">
              	<input type="text" name="filtroEntregador.cpfCnpj" id="textfield" style="width:150px;"/>
              </td>
              <td width="106">
              	<span class="bt_pesquisar" id="btnPesquisar">
              		<a href="javascript:;" id="linkBtnPesquisar">Pesquisar</a>
              	</span>
              </td>
            </tr>
          </table>
         </form>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="classFieldset">
       	  <legend title="Capataz, Entregador, Mula, Carreteiro">Entregadores Cadastrados</legend>
        <div class="grids" style="display:none;">
        	<table class="pessoasGrid"></table>
        </div>
        <span class="bt_novos" title="Novo">
           	<a href="javascript:;" id="linkBtnNovoEntregadorPF">
           		<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>CPF
           	</a>
        </span>

        <span class="bt_novos" title="Novo">
        	<a href="javascript:;" id="linkBtnNovoEntregadorPJ">
        		<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>CNPJ
        	</a>
        </span>
           
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
	</div>
	
	<jsp:include page="novoEntregador.jsp" />
</body>