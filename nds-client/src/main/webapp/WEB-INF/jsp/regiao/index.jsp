<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>NDS - Novo Distrib</title>
<link rel="stylesheet" type="text/css" href="../css/NDS.css" />
<link rel="stylesheet" type="text/css"
	href="../scripts/jquery-ui-1.8.16.custom/development-bundle/themes/redmond/jquery.ui.all.css" />
<link rel="stylesheet" type="text/css" href="../css/menu_superior.css" />
<script language="javascript" type="text/javascript"
	src="../scripts/jquery-ui-1.8.16.custom/js/jquery-1_7_1_min.js"></script>
<script language="javascript" type="text/javascript"
	src="../scripts/jquery-ui-1.8.16.custom/js/jquery-ui-1.8.16.custom.min.js"></script>
<script language="javascript" type="text/javascript"
	src="../scripts/NDS.js"></script>
<script language="javascript" type="text/javascript"
	src="../scripts/acessoRapido.js"></script>
<script language="javascript" type="text/javascript"
	src="../scripts/flexigrid-1.1/js/flexigrid.pack.js"></script>
<link rel="stylesheet" type="text/css"
	href="../scripts/flexigrid-1.1/css/flexigrid.pack.css" />
<script language="javascript" type="text/javascript"
	src="../scripts/warpech-jquery-handsontable/jquery.handsontable.js"></script>

<link rel="stylesheet" media="screen"
	href="../scripts/warpech-jquery-handsontable/jquery.handsontable.css" />
<link rel="stylesheet" media="screen"
	href="../scripts/warpech-jquery-handsontable/lib/jQuery-contextMenu/jquery.contextMenu.css" />
<script language="javascript" type="text/javascript">


</script>
<style type="text/css">
.porCep,.porNMaiores,.porSegmento {
	display: none;
}

.gridfaixaCep,.gridNMaiores,.gridsegmentos {
	display: none;
}

#row5 {
	display: none;
}
</style>
</head>

<body>
	<div id="dialog-pesqCotas" title="Selecionar Cotas"
		style="display: none;">
		<fieldset style="width: 600px !important;">
			<legend>Pesquisar Cotas</legend>
			<table width="550" border="0" cellspacing="2" cellpadding="2">
				<tr>
					<td width="31">Cota:</td>
					<td width="99"><input type="text" name="textfield"
						id="textfield" style="width: 80px;" /></td>
					<td width="32">Nome:</td>
					<td width="250"><input type="text" name="textfield5"
						id="textfield6" style="width: 200px;" /></td>
					<td width="106"><span class="bt_pesquisar"><a
							href="javascript:;">Pesquisar</a> </span></td>
				</tr>
			</table>
		</fieldset>

		<fieldset
			style="width: 600px !important; margin-top: 10px !important;">
			<legend>Selecionar Cotas</legend>
			<table class="lstCotasGrid"></table>
			<span class="bt_sellAll" style="float: right;"><label
				for="sel">Selecionar Todos</label><input type="checkbox" id="sel"
				name="Todos" onclick="checkAll();"
				style="float: left; margin-right: 25px;" /> </span>
		</fieldset>
	</div>

	<div id="dialog-detalhes" title="Visualizando Produto">
		<img src="../capas/revista-nautica-11.jpg" width="235" height="314" />
	</div>

	<div id="dialog-addNMaiores" title="Adicionar Produtos"
		style="display: none;">
		<fieldset style="width: 600px !important;">
			<legend>Pesquisar Produtos</legend>
			<table width="588" border="0" cellspacing="2" cellpadding="2">
				<tr>
					<td width="36">Código:</td>
					<td width="77"><input type="text" name="textfield"
						id="textfield" style="width: 60px;" /></td>
					<td width="40">Produto:</td>
					<td width="129"><input type="text" name="textfield5"
						id="textfield6" style="width: 120px;" /></td>
					<td width="68">Classificação:</td>
					<td width="163"><select name="select" id="select"
						style="width: 140px;">
							<option selected="selected">Selecione...</option>
							<option>Classificação 1</option>
							<option>Classificação 2</option>
							<option>Classificação 3</option>
							<option>Classificação 4</option>
					</select></td>
					<td width="31"><span title="Pesquisar Produto"
						class="classPesquisar"><a href="javascript:;">&nbsp;</a> </span></td>
				</tr>
			</table>
		</fieldset>

		<fieldset
			style="width: 600px !important; margin-top: 10px !important;">
			<legend>Produtos</legend>
			<table class="lstProdutosGrid"></table>
			<span class="bt_sellAll" style="float: right;"><label
				for="sel">Selecionar Todos</label><input type="checkbox" id="sel"
				name="Todos" onclick="checkAll();"
				style="float: left; margin-right: 25px;" /> </span>
		</fieldset>

	</div>
	<div id="dialog-cotas" title="Montagem de Região Automática"
		style="display: none;">
		<fieldset style="width: 600px !important;">
			<legend>Montar Região por:</legend>

			<table width="550" border="0" cellspacing="2" cellpadding="2">
				<tr>
					<td width="20"><input type="radio" name="radio" id="radio"
						value="radio" onclick="filtroPorCep();" /></td>
					<td width="40">CEP</td>
					<td width="20"><input type="radio" name="radio" id="radio2"
						value="radio" onclick="filtroPorNMaiores();" /></td>
					<td width="74">N Maiores</td>
					<td width="20"><input type="radio" name="radio" id="radio3"
						value="radio" onclick="filtroPorSegmento();" /></td>
					<td width="336">Segmento</td>
				</tr>
			</table>

			<div class="porCep">
				<table width="550" border="0" cellspacing="2" cellpadding="2">
					<tr>
						<td width="86">Faixa de CEP:</td>
						<td width="133"><input type="text" name="textfield"
							id="textfield" style="width: 80px;" /> <input type="text"
							name="textfield3" id="textfield3" style="width: 30px;" /></td>
						<td width="26">Até:</td>
						<td width="186"><input type="text" name="textfield4"
							id="textfield4" style="width: 80px;" /> <input type="text"
							name="textfield4" id="textfield5" style="width: 30px;" />
						</td>
						<td width="87"><span class="bt_pesquisar"><a
								href="javascript:;" onclick="mostrarPorCep();">Pesquisar</a> </span></td>
					</tr>
				</table>


			</div>


			<div class="porSegmento">
				<table width="550" border="0" cellspacing="2" cellpadding="2">
					<tr>
						<td width="51">Segmento:</td>
						<td width="205"><select name="select" id="select"
							style="width: 180px;">
								<option>Selecione...</option>
						</select></td>
						<td width="92">Qtde de Cotas:</td>
						<td width="83"><input type="text" name="textfield2"
							id="textfield2" style="width: 80px;" /></td>
						<td width="87"><span class="bt_pesquisar"><a
								href="javascript:;" onclick="mostrarPorSegmento();">Pesquisar</a>
						</span></td>
					</tr>
				</table>
			</div>


		</fieldset>

		<br clear="all" />


		<fieldset style="width: 600px !important; margin-top: 10px;"
			class="gridfaixaCep">
			<legend>Faixa de Cep</legend>
			<table class="faixaGrid"></table>
			<span class="bt_sellAll" style="float: right;"><label
				for="sel">Selecionar Todos</label><input type="checkbox" id="sel"
				name="Todos" onclick="checkAll();"
				style="float: left; margin-right: 25px;" /> </span> <span class="bt_novos"><a
				href="javascript:;" onclick="add_cotas();"><img
					src="../images/ico_add.gif" hspace="5" border="0" />Incluir</a> </span> <span
				class="bt_novos"><a href="javascript:;"><img
					src="../images/ico_excluir.gif" hspace="5" border="0" />Cancelar</a> </span>
		</fieldset>


		<fieldset style="width: 600px !important; margin-top: 10px;"
			class="gridNMaiores">
			<legend>N Maiores</legend>
			<table class="nMaioresGrid"></table>

			<span class="bt_novos"><a href="javascript:;"
				onclick="add_produtos();"><img src="../images/ico_add.gif"
					hspace="5" border="0" />Incluir</a> </span> <span
				style="float: right; margin-top: 5px; margin-right: 50px;">Qtde
				de Cotas:&nbsp;&nbsp; <input name="" type="text"
				style="width: 60px;" /> <a href="javascript:;"
				onclick="add_cotas();"><img src="../images/ico_check.gif"
					border="0" /> </a> </span>
		</fieldset>


		<fieldset style="width: 600px !important; margin-top: 10px;"
			class="gridsegmentos">
			<legend>Segmento</legend>
			<table class="segmentosGrid"></table>
			<span class="bt_sellAll" style="float: right;"><label
				for="sel">Selecionar Todos</label><input type="checkbox" id="sel"
				name="Todos" onclick="checkAll();"
				style="float: left; margin-right: 25px;" /> </span> <span class="bt_novos"><a
				href="javascript:;" onclick="add_cotas();"><img
					src="../images/ico_add.gif" hspace="5" border="0" />Incluir</a> </span> <span
				class="bt_novos"><a href="javascript:;"><img
					src="../images/ico_excluir.gif" hspace="5" border="0" />Cancelar</a> </span>
		</fieldset>
	</div>





	<div id="dialog-lote" title="Adicionar em Lote" style="display: none;">
		<fieldset style="width: 125px;">
			<legend>Adicionar em Lote</legend>

			<div id="example2grid" class="dataTable" style="background: #FFF;"></div>



		</fieldset>
	</div>



	<div id="dialog-regiaoAutomatica" title="Montagem Região Automática"
		style="display: none;">
		<fieldset style="width: 400px !important;">
			<legend>Adicionar Cotas</legend>
			<table class="regioesCadastradasGrid"></table>
		</fieldset>

		<fieldset style="width: 400px !important; margin-top: 5px;">
			<legend>Selecionar Cotas</legend>
			<table class="addCotasGrid"></table>
			<span class="bt_novos"><a href="javascript:;"
				onclick="addNovaRegiao();"><img src="../images/ico_add.gif"
					hspace="5" border="0" />Nova Região</a> </span>
		</fieldset>
	</div>



	<div id="dialog-novo" title="Regiões Criadas" style="display: none;">
		<fieldset style="width: 600px !important;">
			<legend>Regiões Criadas</legend>
			<table class="regioesCadastradasGrid"></table>
			<!-- <span class="bt_novos"><a href="javascript:;" onclick="addNovaRegiao();"><img src="../images/ico_add.gif" hspace="5" border="0" />Nova Região</a></span>
-->

		</fieldset>
	</div>

	<div id="dialog-addRegiao" title="Nova Região" style="display: none;">
		<fieldset style="width: 300px !important;">
			<legend>Região</legend>
			<table width="270" border="0" cellspacing="2" cellpadding="2">
				<tr>
					<td width="44">Nome:</td>
					<td width="212"><input name="" type="text"
						style="width: 200px;" /></td>
				</tr>
				<tr>
					<td align="right"><input name="" type="checkbox" value="" />
					</td>
					<td>Região Fixa</td>
				</tr>
			</table>
		</fieldset>
	</div>




	<div id="dialog-excluir" title="Excluir Região">
		<p>Confirma a exclusão desta Região?</p>
	</div>




	<div class="corpo">
		<div class="header">
			<div class="sub-header">
				<div class="logo">
					<img src="../images/logo_sistema.png" width="110" height="70"
						alt="Novo Distrib" />
				</div>

				<div class="titAplicacao">
					<h1>Treelog S/A. Logística e Distribuição - SP</h1>

					<h2>CNPJ: 00.000.000/00001-00</h2>
					<h3>Distrib vs.1</h3>
				</div>

				<div class="usuario">
					<a href="../login.htm"><img src="../images/bt_sair.jpg"
						alt="Sair do Sistema" title="Sair do Sistema" width="63"
						height="27" border="0" align="right" /> </a> <br clear="all" /> <span>Usuário:
						Junior Fonseca</span> <span> <script type="text/javascript"
							language="JavaScript">
		  diaSemana();
          </script> </span>
				</div>
				<div id="div_acessoRapido" class="box_acesso_rapido">
					<span class="titulo"><a href="javascript:;"
						onclick="acessoRapido();" style="float: left;">Acesso Rápido</a> </span>
					<a href="javascript:;" onclick="acessoRapidoFechar();"
						style="float: right;" class="fechar"><span
						class="ui-icon ui-icon-close">&nbsp;</span> </a>

					<div class="class_acessos">
						<ul id="acessoRapido"></ul>
					</div>
				</div>

			</div>

		</div>
		<div class="bg_menu">
			<div id="menu_principal">
				<ul>
					<li><a href="../index.htm"><span class="classHome">&nbsp;</span>Home</a>
					</li>
					<li><a href="javascript:;" class="trigger"><span
							class="classCadastros">&nbsp;</span>Cadastro</a>
						<ul>
							<li><a href="../Cadastro/cadastro_produtos.htm">Produto</a>
							</li>
							<li><a href="../Cadastro/cadastro_edicao.htm">Edição</a></li>
							<li><a href="../Cadastro/cadastro_cotas.htm">Cota</a></li>
							<li><a href="../Cadastro/jornaleiros_equivalentes.htm">Cotas
									Base</a></li>
							<li><a href="../Cadastro/cadastro_fiador.htm">Fiador</a></li>
							<li><a href="../Cadastro/cadastro_entregador.htm">Entregador</a>
							</li>
							<li><a href="../Cadastro/cadastro_transportador.htm">Transportador</a>
							</li>
							<li><a href="../Cadastro/cadastro_fornecedor.htm">Fornecedor</a>
							</li>
							<li><a href="../Cadastro/roteirizacao.htm">Roteirização</a>
							</li>

							<li><a href="../Cadastro/cadastro_box.htm">Box</a></li>
							<li><a href="../Cadastro/cadastro_bancos.htm">Banco</a></li>
							<li><a href="../Cadastro/alteracao_cotas.htm">Alteração
									/ Cota</a></li>
							<!--<li><a href="../Cadastro/consulta_geral_equivalentes.htm">Consulta Geral Equivalentes</a></li>-->
							<li><a href="../Cadastro/help_cadastros.htm">Help</a></li>
						</ul>
					</li>
					<li><a href="javascript:;" class="trigger"><span
							class="classLancamento">&nbsp;</span>Lançamento</a>
						<ul>
							<!--<li class="criando"><a href="javascript:;"  onclick="alert('Serviço em construção.');">Conectividade</a></li>-->
							<li><a href="../Lancamento/balanceamento_da_matriz.htm">Balanceamento
									da Matriz</a></li>
							<li><a href="../Lancamento/furo_publicacao.htm">Furo de
									Lançamento</a></li>
							<li><a href="../Lancamento/cadastro_parciais.htm">Parciais</a>
							</li>
							<li><a href="../Lancamento/relatorio_vendas.htm">Relatório
									de Vendas</a></li>
							<li><a href="../Lancamento/venda_produto.htm">Venda por
									Produto</a></li>
							<li><a href="../Lancamento/relatorio_tipos_produtos.htm">Relatório
									Tipos de Produtos</a></li>
							<li><a href="../Lancamento/help_lancamento.htm">Help</a></li>
						</ul>
					</li>
					<li><a href="javascript:;" class="trigger"><span
							class="classDistribuicao">&nbsp;</span>Distribuição</a>
						<ul>
							<li><a href="matriz_distribuicao.htm">Matriz de
									Distribuição</a></li>
							<li><a href="analise_estudo.htm">Análise de Estudos</a></li>
							<li><a href="mix_produto.htm">Mix de Produto</a></li>
							<li><a href="fixacao.htm">Fixação de Reparte</a></li>
							<li><a href="classificacao_nao_recebida.htm">Classificação
									Não Recebida</a></li>
							<li><a href="segmento_nao_recebido.htm">Segmento Não
									Recebido</a></li>
							<li><a href="tratamento_excessao.htm">Exceções de
									Segmentos e Parciais</a></li>
							<li><a href="ajustes_reparte.htm">Ajustes Reparte</a></li>
							<li><a href="engloba_desengloba.htm">Desenglobação</a></li>
							<li><a href="histograma.htm">Histograma de Venda</a></li>
							<li><a href="historico_venda.htm">Histórico de Venda</a></li>
							<li><a href="regiao.htm">Região</a></li>
							<li><a href="area_influencia.htm">Área de
									Influência/Gerador de Fluxo</a></li>
							<li><a href="informacoes_produtos.htm">Informações do
									Produto</a></li>
							<li><a href="caracteristica_distribuicao.htm">Caracteristicas
									de Distribuição</a></li>
							<li><a href="help_distribuicao.htm">Help</a></li>
						</ul>
					</li>
					<li><a href="javascript:;" class="trigger"><span
							class="classEstoque">&nbsp;</span>Estoque</a>
						<ul>
							<li><a href="../Estoque/recebimento_fisico.htm">Recebimento
									Fisico</a></li>
							<li><a href="../Estoque/lancamento_faltas_sobras.htm">Lançamento
									Faltas e Sobras</a></li>
							<!--
    <li><a href="../Estoque/relatorio_faltas_sobras.htm">Relatório Faltas e Sobras</a></li>-->
							<!--<li><a href="../Estoque/ajuste_estoque.htm">Ajuste Estoque - Inventário</a></li>-->
							<li><a href="../Estoque/consulta_notas_sem_fisico.htm">Consulta
									Notas</a></li>
							<li><a href="../Estoque/consulta_faltas_sobras.htm">Consulta
									Faltas e Sobras</a></li>
							<li><a href="../Estoque/extrato_edicao.htm">Extrato de
									Edição</a></li>
							<li><a href="../Estoque/visao_estoque.htm">Visão do
									Estoque</a></li>
							<li><a href="../Estoque/edicoes_fechadas.htm">Edições
									Fechadas com Saldo</a></li>
							<li><a href="../Estoque/help_estoque.htm">Help</a></li>
						</ul>
					</li>

					<li><a href="javascript:;" class="trigger"><span
							class="classExpedicao">&nbsp;</span>Expedição</a>
						<ul>
							<li><a href="../Expedicao/interface_picking.htm">Interface
									Picking</a></li>
							<li><a href="../Expedicao/mapa_abastecimento.htm">Mapa
									Abastecimento</a></li>
							<li><a href="../Expedicao/confirma_expedicao.htm">Confirma
									Expedição</a></li>
							<li><a href="../Expedicao/jornaleiro_ausente.htm">Cota
									Ausente - Reparte</a></li>
							<li><a href="../Expedicao/geracao_nfe.htm">Nota de Envio</a>
							</li>
							<li><a href="../Expedicao/resumo_expedicao_nota.htm">Resumo
									de Expedição</a></li>
							<li><a href="../Expedicao/romaneios.htm">Romaneios</a></li>
							<li><a href="../Expedicao/help_expedicao.htm">Help</a></li>
						</ul>
					</li>
					<li><a href="javascript:;" class="trigger"><span
							class="classDevolucao">&nbsp;</span>Devolução</a>
						<ul>
							<li><a
								href="../Devolucao/balanceamento_da_matriz_recolhimento.htm">Balanceamento
									da Matriz</a></li>
							<li><a href="../Devolucao/consulta_informe_encalhe.htm">Informe
									Recolhimento</a></li>
							<li><a href="../Devolucao/ce_antecipada.htm">CE
									Antecipada - Produto</a></li>
							<li><a href="../Devolucao/emissao_ce.htm">Emissão CE</a></li>
							<li><a
								href="../Devolucao/conferencia_encalhe_jornaleiro.htm">Conferência
									de Encalhe</a></li>
							<li><a href="../Devolucao/venda_encalhe.htm">Venda de
									Encalhe / Suplementar</a></li>
							<li><a href="../Devolucao/fechamento_fisico_logico.htm">Fechamento
									Encalhe</a></li>
							<li><a href="../Devolucao/fechamento_ce_integracao.htm">Fechamento
									CE - Integração</a></li>
							<li><a href="../Devolucao/devolucao_fornecedor.htm">Devolução
									ao Fornecedor</a></li>
							<!--<li><a href="../Devolucao/digitacao_contagem_devolucao.htm">Devolução Fornecedor</a></li>-->
							<li><a href="../Devolucao/emissao_bandeira.htm">Emissão
									das Bandeiras</a></li>
							<li><a href="../Devolucao/chamadao.htm">Chamadão</a></li>
							<li><a href="../Devolucao/edicoes_chamada.htm">Consulta
									Encalhe</a></li>
							<li><a href="../Devolucao/help_devolucao.htm">Help</a></li>
						</ul></li>
					<li><a href="javascript:;" class="trigger"><span
							class="classNFe">&nbsp;</span>NF-e</a>
						<ul>
							<li><a href="../NFE/tratamento_arquivo_retorno_nfe.htm">Retorno
									NF-e</a></li>
							<li><a href="../NFE/consulta_nfe_encalhe_tratamento.htm">Entrada
									NF-e Terceiros</a></li>
							<li><a href="../NFE/geracao_nfe_NFE.htm">Geração NF-e</a></li>
							<li><a href="../NFE/impressao_nfe_NFE.htm">Impressão
									NF-e</a></li>
							<!--<li><a href="../NFE/cancelamento_nfe.htm">Cancelamento NFE</a></li>-->
							<li><a href="../NFE/painel_monitor_nfe.htm">Painel
									Monitor NF-e</a></li>
							<li><a href="../NFE/help_nfe.htm">Help</a></li>
						</ul>
					</li>


					<li><a href="javascript:;" class="trigger"><span
							class="classFinanceiro">&nbsp;</span>Financeiro</a>
						<ul>
							<li><a href="../Financeiro/baixa_bancaria.htm">Baixa
									Financeira</a></li>
							<li><a href="../Financeiro/negociar_divida.htm">Negociação
									de Divida</a></li>
							<li><a href="../Financeiro/debitos_creditos.htm">Débitos
									/ Créditos Cota</a></li>
							<li><a href="../Financeiro/impressao_boletos.htm">Impressão
									de Boletos</a></li>
							<li><a href="../Financeiro/cadastro_manutencao_status.htm">Manutenção
									de Status Cota</a></li>
							<li><a href="../Financeiro/suspensao_jornaleiro.htm">Suspensão
									Cota</a></li>
							<li><a href="../Financeiro/consulta_boletos_jornaleiros.htm">Consulta
									Boletos por Cota</a></li>
							<li><a href="../Financeiro/conta_corrente.htm">Conta
									Corrente</a></li>
							<li><a href="../Financeiro/conta_pagar.htm">Contas a
									pagar</a></li>
							<li><a href="../Financeiro/historico_inadimplencia.htm">Inadimplência</a>
							</li>
							<li><a href="../Financeiro/consignado_cota.htm">Consulta
									Consignado</a></li>
							<li><a href="../Financeiro/cadastro_tipo_desconto.htm">Tipo
									de Desconto Cota</a></li>
							<li><a href="../Financeiro/relatorio_garantias.htm">Relatório
									de Garantias</a></li>
							<li><a href="../Financeiro/parametros_cobranca.htm">Parâmetros
									de Cobrança</a></li>
							<li><a href="../Financeiro/help_financeiro.htm">Help</a></li>
						</ul>
					</li>
					<li><a href="javascript:;" class="trigger"><span
							class="classAdministracao">&nbsp;</span>Administração</a>
						<ul>
							<li><a href="../Administracao/fechar_dia.htm">Fechamento
									Diário</a></li>
							<li><a href="../Administracao/workflow_aprovacao.htm">Controle
									Aprovação</a></li>
							<!-- <li><a href="painel_operacional.htm">Painel Operacional</a></li>-->
							<li><a href="../Administracao/painel_processamento.htm">Painel
									Processamento</a></li>
							<li><a href="../Administracao/fallowup_sistema.htm">Follow
									Up do Sistema</a></li>
							<li><a href="../Administracao/cadastro_usuario.htm">Grupos
									de Acesso</a></li>
							<li><a href="../Administracao/cadastro_calendario.htm">Calendário</a>
							</li>
							<!--<li><a href="../Administracao/cadastro_tipos_movimento.htm">Tipo de Movimento</a></li>-->
							<li><a href="../Administracao/faixa_reparte.htm">Faixa
									de Reparte</a></li>
							<li><a href="../Administracao/gerar_arquivo_jornaleiro.htm">Gerar
									Arquivo Jornaleiro</a></li>
							<li><a href="../Administracao/cadastro_tipo_nota.htm">Tipos
									de NF-e</a></li>

							<!--<li><a href="../Administracao/cadastro_servico_entrega.htm">Serviço de Entrega</a></li>-->

							<li><a href="../Administracao/relatorio_servico_entrega.htm">Relatório
									de Serviços de Entrega</a></li>

							<!--<li><a href="../Administracao/tipos_produtos.htm">Tipos de Produtos</a></li>-->
							<!--<li class="criando"><a href="javascript:;" onclick="alert('Serviço em construção.');">Plano de Contas</a></li>-->
							<li><a href="../Administracao/parametros_sistema.htm">Parâmetros
									de Sistema</a></li>
							<li><a href="../Administracao/parametros_distribuidor.htm">Parâmetros
									Distribuidor</a></li>
							<!--<li class="criando"><a href="javascript:;" onclick="alert('Serviço em construção.');">Histórico do PDV</a></li>-->
							<li><a href="../Administracao/help_administracao.htm">Help</a>
							</li>
						</ul></li>
					<li><a href="../help.htm" style="width: 14px !important;"><span
							class="classHelp">&nbsp;</span> </a></li>
				</ul>
				<br class="clearit" />
			</div>
		</div>
		<br clear="all" /> <br />

		<div class="container">

			<div id="effect" style="padding: 0 .7em;"
				class="ui-state-highlight ui-corner-all">
				<p>
					<span style="float: left; margin-right: .3em;"
						class="ui-icon ui-icon-info"></span> <b>Região < evento > com
						< status >.</b>
				</p>
			</div>

			<fieldset class="classFieldset">
				<legend> Pesquisar Região</legend>
				<table width="950" border="0" cellpadding="2" cellspacing="1"
					class="filtro">
					<tr>
						<td width="48">Região:</td>
						<td width="752"><select name="select" id="select"
							style="width: 250px;" onchange="$('.grids').toggle();">
								<option selected="selected">Selecione...</option>
								<option>Pinheiros</option>
						</select>
						</td>
						<td width="134"><span class="bt_novos" title="Nova Região"><a
								href="javascript:;" onclick="addNovaRegiao();"><img
									src="../images/ico_salvar.gif" hspace="5" border="0" />Nova
									Região</a> </span></td>
					</tr>
				</table>

			</fieldset>
			<div class="linha_separa_fields">&nbsp;</div>
			<div class="grids" style="display: none;">
				<fieldset class="classFieldset">
					<legend>Cotas por Região</legend>

					<table class="cotasRegiaoGrid"></table>
					<span class="bt_novos" title="Arquivo"><a
						href="javascript:;"><img src="../images/ico_excel.png"
							hspace="5" border="0" />Arquivo</a> </span> <span class="bt_novos"
						title="Imprimir"><a href="javascript:;"><img
							src="../images/ico_impressora.gif" alt="Imprimir" hspace="5"
							border="0" />Imprimir</a> </span>

					<!--<span class="bt_novos"><a href="javascript:;" onclick="addCotas();"><img src="../images/ico_add.gif" alt="Adicionar Cotas" hspace="5" border="0" />Adicionar Cotas</a></span>-->

					<span class="bt_novos" title="Região Automática"><a
						href="javascript:;" onclick="addCotas();"><img
							src="../images/ico_integrar.png" hspace="5" border="0" />Região
							Automática</a> </span> <span class="bt_novos" title="Adicionar em Lote"><a
						href="javascript:;" onclick="add_lote();"><img
							src="../images/ico_integrar.png" hspace="5" border="0" />Adicionar
							em Lote</a> </span> <span class="bt_novos" title="Manutenção"><a
						href="javascript:;" onclick="incluirNovo();"><img
							src="../images/bt_administracao.png" hspace="5" border="0" />Manutenção</a>
					</span> <span class="bt_novos" title="Adicionar Cota"><a
						href="javascript:;" onclick="add_cotas_grid();"><img
							src="../images/ico_add.gif" hspace="5" border="0" />Adicionar
							Cota</a> </span>



				</fieldset>
			</div>
		</div>
	</div>
	<script>
	$(".faixaGrid").flexigrid({
			url : '../xml/faixasB-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'Nome',
				width : 320,
				sortable : true,
				align : 'left'
			},  {
				display : 'Status',
				name : 'Status',
				width : 130,
				sortable : true,
				align : 'left'
			},  {
				display : ' ',
				name : 'sel',
				width : 20,
				sortable : true,
				align : 'center'
			}],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 600,
			height : 200
		});
		
		
		$(".lstCotasGrid").flexigrid({
			url : '../xml/cotasLst-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 60,
				sortable : true,
				align : 'left',
			}, {
				display : 'Nome',
				name : 'nome',
				width : 300,
				sortable : true,
				align : 'left'
			},  {
				display : 'Status',
				name : 'status',
				width : 150,
				sortable : true,
				align : 'left'
			},  {
				display : '',
				name : 'sel',
				width : 20,
				sortable : true,
				align : 'center'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 600,
			height : 200
		});
		
	$(".lstProdutosGrid").flexigrid({
			url : '../xml/nMaioresLst-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Edição',
				name : 'edicao',
				width : 80,
				sortable : true,
				align : 'left',
			}, {
				display : 'Data de Lançamento',
				name : 'dtLancamento',
				width : 130,
				sortable : true,
				align : 'left'
			},  {
				display : 'Status',
				name : 'status',
				width : 100,
				sortable : true,
				align : 'left'
			},  {
				display : 'Classificação',
				name : 'classificação',
				width : 120,
				sortable : true,
				align : 'left'
			},  {
				display : 'Capa',
				name : 'capa',
				width : 40,
				sortable : true,
				align : 'center'
			},  {
				display : '',
				name : 'sel',
				width : 30,
				sortable : true,
				align : 'center'
			}],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 600,
			height : 200
		});
	
	
	$(".nMaioresGrid").flexigrid({
			url : '../xml/nMaiores-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 60,
				sortable : true,
				align : 'left',
			}, {
				display : 'Produto',
				name : 'produto',
				width : 250,
				sortable : true,
				align : 'left'
			},  {
				display : 'Edição',
				name : 'edicao',
				width : 60,
				sortable : true,
				align : 'left'
			},  {
				display : 'Classificação',
				name : 'classificacao',
				width : 115,
				sortable : true,
				align : 'left'
			},  {
				display : 'Ação',
				name : 'acao',
				width : 30,
				sortable : true,
				align : 'center'
			}],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 600,
			height : 250
		});
	$(".segmentosGrid").flexigrid({
			url : '../xml/segmentoB-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'Nome',
				width : 220,
				sortable : true,
				align : 'left'
			}, {
				display : 'Tipo de PDV',
				name : 'tipoPdv',
				width : 100,
				sortable : true,
				align : 'left'
			},  {
				display : 'Status',
				name : 'Status',
				width : 120,
				sortable : true,
				align : 'left'
			},  {
				display : ' ',
				name : 'sel',
				width : 20,
				sortable : true,
				align : 'center'
			}],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 600,
			height : 200
		});
	
		
		
		
	
	
	
	
	$(".classificacaoGrid").flexigrid({
			url : '../xml/segmentos-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'Nome',
				width : 150,
				sortable : true,
				align : 'left'
			},  {
				display : '',
				name : 'sel',
				width : 30,
				sortable : true,
				align : 'center'
			}],
			width : 300,
			height : 235
		});

	$(".addCotasGrid").flexigrid({
			url : '../xml/addCotas-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Região',
				name : 'regiao',
				width : 280,
				sortable : true,
				align : 'left'
			}, {
				display : 'Fixa',
				name : 'fixa',
				width : 30,
				sortable : true,
				align : 'center'
			},  {
				display : 'Ação',
				name : 'acao',
				width : 30,
				sortable : true,
				align : 'center'
			}],
			sortname : "regiao",
			sortorder : "asc",
			useRp : true,
			rp : 15,
			width : 400,
			height : 200
		});

	$(".regioesCadastradasGrid").flexigrid({
			url : '../xml/regioesCadastradas-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Região',
				name : 'regiao',
				width : 210,
				sortable : true,
				align : 'left'
			}, {
				display : 'Usuário',
				name : 'usuario',
				width : 150,
				sortable : true,
				align : 'left'
			}, {
				display : 'Data',
				name : 'data',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Fixa',
				name : 'fixa',
				width : 30,
				sortable : true,
				align : 'center'
			},  {
				display : 'Ação',
				name : 'acao',
				width : 30,
				sortable : true,
				align : 'center'
			}],
			sortname : "regiao",
			sortorder : "asc",
			useRp : true,
			rp : 15,
			width : 600,
			height : 200
		});
	
	
	$(".cotasRegiaoGrid").flexigrid({
			url : '../xml/cotasRegiaoB-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'Nome',
				width : 130,
				sortable : true,
				align : 'left'
			},  {
				display : 'Tipo PDV',
				name : 'tipoPdv',
				width : 70,
				sortable : true,
				align : 'left'
			},  {
				display : 'Status',
				name : 'status',
				width : 50,
				sortable : true,
				align : 'left'
			},  {
				display : 'Bairro',
				name : 'Bairro',
				width : 120,
				sortable : true,
				align : 'left'
			},  {
				display : 'Cidade',
				name : 'cidade',
				width : 90,
				sortable : true,
				align : 'left'
			},  {
				display : 'Faturamento R$',
				name : 'faturamento',
				width : 90,
				sortable : true,
				align : 'right'
			},  {
				display : 'Usuário',
				name : 'usuario',
				width : 80,
				sortable : true,
				align : 'left'
			},  {
				display : 'Data',
				name : 'data',
				width : 60,
				sortable : true,
				align : 'center'
			},  {
				display : 'Hora',
				name : 'hora',
				width : 33,
				sortable : true,
				align : 'center'
			},  {
				display : 'Ação',
				name : '',
				width : 25,
				sortable : true,
				align : 'center'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});
		
		
	$("#example2grid").handsontable({
		rows: 8,
		cols: 1
	});

	  var data = [
		["Nº Cota"],
		[4.257],
		[2.325]
	  ];

	$("#example2grid").handsontable("loadData", data);

		
</script>
</body>
</html>
