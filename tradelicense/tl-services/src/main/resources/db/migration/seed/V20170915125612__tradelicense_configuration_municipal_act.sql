insert into egtl_configuration values (nextval('seq_egtl_configuration'), 'default.pdf.municipal.act.section', 'Value for Municipal Act Section in PDF', 1, now(), 1, now(), 'default');

insert into egtl_configurationvalues values (nextval('seq_egtl_configurationvalues'), (select id from egtl_configuration where keyname = 'default.pdf.municipal.act.section' and tenantid = 'default'), 'As per the Municipal act', now(), 1, now(), 1, now(), 'default');