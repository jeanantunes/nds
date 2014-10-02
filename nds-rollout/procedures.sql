-- 1) Salva um backup
-- 2) Gera uma estrutura limpa

-- ###############################################################

-- LIMPAR_BASE
DROP PROCEDURE IF EXISTS LIMPAR_BASE;

delimiter //
CREATE PROCEDURE LIMPAR_BASE (IN p_in_cod_distribuidor VARCHAR(8), OUT p_out_saida TEXT)
BEGIN

  -- 1) Limpar tabelas
END//
delimiter ;

-- ###############################################################

-- CARGA_TIPOS
DROP PROCEDURE IF EXISTS CARGA_TIPOS;

delimiter //
CREATE PROCEDURE CARGA_TIPOS (IN p_in_cod_distribuidor VARCHAR(8), OUT p_out_saida TEXT)
BEGIN

  -- 1) 
END//
delimiter ;


-- ###############################################################

-- CARGA_DISTRIBUIDOR
DROP PROCEDURE IF EXISTS CARGA_DISTRIBUIDOR;

delimiter //
CREATE PROCEDURE CARGA_DISTRIBUIDOR (IN p_in_cod_distribuidor VARCHAR(8), OUT p_out_saida TEXT)
BEGIN

  -- 1) 
END//
delimiter ;

-- ###############################################################

-- CARGA_PARAMETROS_SISTEMA
DROP PROCEDURE IF EXISTS CARGA_PARAMETROS_SISTEMA;

delimiter //
CREATE PROCEDURE CARGA_PARAMETROS_SISTEMA (IN p_in_cod_distribuidor VARCHAR(8), OUT p_out_saida TEXT)
BEGIN

  -- 1) 
END//
delimiter ;

-- ###############################################################

-- ROLLOUT
DROP PROCEDURE IF EXISTS ROLLOUT;

delimiter //
CREATE PROCEDURE ROLLOUT (IN p_in_cod_distribuidor VARCHAR(8), OUT p_out_saida TEXT)

BEGIN

 CALL CARGA_PARAMETROS_SISTEMA(@p_in_cod_distribuidor,@p_out_saida);

 
 SET p_out_saida = CONCAT('Procedure Rollout executada com sucesso.',@p_out_saida);

 SELECT p_out_saida;

END//
delimiter ;

