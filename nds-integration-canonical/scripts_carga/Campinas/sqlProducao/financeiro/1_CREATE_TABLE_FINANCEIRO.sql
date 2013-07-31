drop table mov_cred;
drop table mov_deb;

create table mov_cred (linha_inicial varchar(1), 
                       data varchar(8),
                       numero_cota int,
                       tipo_credito int,
                       desc_credito varchar(255),
                       valor varchar(11),
                       valor_decimal decimal(11,2),
                       data_real date,
                       cota_id int);

create table mov_deb (linha_inicial varchar(1),
                      data varchar(8),
                      numero_cota int,
                      tipo_debito int,
                      desc_debito varchar(255),
                      valor varchar(11),
                      valor_decimal decimal(11,2),
                      data_real date,
                      cota_id int);

LOAD DATA LOCAL INFILE 'ARQCD08.NEW' INTO TABLE mov_cred COLUMNS TERMINATED BY '|' LINES TERMINATED BY '\n';

LOAD DATA LOCAL INFILE 'ARQDB97.NEW' INTO TABLE mov_deb COLUMNS TERMINATED BY '|' LINES TERMINATED BY '\n';

update mov_cred set valor_decimal = (cast(valor as decimal(12.2))/100), data_real = str_to_date(data, '%Y%m%d'), cota_id = (select c.id from cota c where c.numero_cota = mov_cred.numero_cota);
update mov_deb set valor_decimal = (cast(valor as decimal(12.2))/100), data_real = str_to_date(data, '%Y%m%d'), cota_id = (select c.id from cota c where c.numero_cota = mov_deb.numero_cota);
