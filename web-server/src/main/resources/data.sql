INSERT INTO tbl_user (user_id, first_name, last_name, email, password, is_enable, validation_code)
VALUES ('7f000001-88ea-1791-8188-eab8dc090000', 'Test_0', 'Duper', 'test@email.com',
        '$2a$10$rnyh.E9fcfW1mSI9tiQ6L.sFXD3WAh7znN1/WAl9uEqxzCOO72pIK', true, null);

INSERT INTO tbl_role (role_id, role_name)
VALUES ('7f000001-88ea-1791-8188-eab8dc097001', 'Video Creator');
INSERT INTO tbl_role (role_id, role_name)
VALUES ('7f000001-88ea-1791-8188-eab8dc097002', 'Administrator & Video Creator');

INSERT INTO tbl_privilege (privilege_id, privilege_name, privilege_system_name)
VALUES ('7f000001-88ea-1791-8188-eab8dc091000', 'List Role', 'LIST_ROLE');
INSERT INTO tbl_privilege (privilege_id, privilege_name, privilege_system_name)
VALUES ('7f000001-88ea-1791-8188-eab8dc091001', 'View Role', 'VIEW_ROLE');
INSERT INTO tbl_privilege (privilege_id, privilege_name, privilege_system_name)
VALUES ('7f000001-88ea-1791-8188-eab8dc091002', 'Create Role', 'CREATE_ROLE');
INSERT INTO tbl_privilege (privilege_id, privilege_name, privilege_system_name)
VALUES ('7f000001-88ea-1791-8188-eab8dc091003', 'Edit Role', 'EDIT_ROLE');
INSERT INTO tbl_privilege (privilege_id, privilege_name, privilege_system_name)
VALUES ('7f000001-88ea-1791-8188-eab8dc091004', 'Delete Role', 'DELETE_ROLE');

INSERT INTO tbl_privilege (privilege_id, privilege_name, privilege_system_name)
VALUES ('7f000001-88ea-1791-8188-eab8dc092000', 'List Video', 'LIST_VIDEO');
INSERT INTO tbl_privilege (privilege_id, privilege_name, privilege_system_name)
VALUES ('7f000001-88ea-1791-8188-eab8dc092001', 'View Video', 'VIEW_VIDEO');
INSERT INTO tbl_privilege (privilege_id, privilege_name, privilege_system_name)
VALUES ('7f000001-88ea-1791-8188-eab8dc092002', 'Create Video', 'CREATE_VIDEO');
INSERT INTO tbl_privilege (privilege_id, privilege_name, privilege_system_name)
VALUES ('7f000001-88ea-1791-8188-eab8dc092003', 'Edit Video', 'EDIT_VIDEO');
INSERT INTO tbl_privilege (privilege_id, privilege_name, privilege_system_name)
VALUES ('7f000001-88ea-1791-8188-eab8dc092004', 'Delete Video', 'DELETE_VIDEO');

INSERT INTO tbl_privilege (privilege_id, privilege_name, privilege_system_name)
VALUES ('7f000001-88ea-1791-8188-eab8dc093000', 'List User', 'LIST_USER');
INSERT INTO tbl_privilege (privilege_id, privilege_name, privilege_system_name)
VALUES ('7f000001-88ea-1791-8188-eab8dc093001', 'View User', 'VIEW_USER');
INSERT INTO tbl_privilege (privilege_id, privilege_name, privilege_system_name)
VALUES ('7f000001-88ea-1791-8188-eab8dc093002', 'Create User', 'CREATE_USER');
INSERT INTO tbl_privilege (privilege_id, privilege_name, privilege_system_name)
VALUES ('7f000001-88ea-1791-8188-eab8dc093003', 'Edit User', 'EDIT_USER');
INSERT INTO tbl_privilege (privilege_id, privilege_name, privilege_system_name)
VALUES ('7f000001-88ea-1791-8188-eab8dc093004', 'Delete User', 'DELETE_USER');


INSERT INTO tbl_role_privilege (role_privilege_id, role_id, privilege_id)
VALUES ('7f000001-88ea-1791-8188-eab8dc099000', '7f000001-88ea-1791-8188-eab8dc097001', '7f000001-88ea-1791-8188-eab8dc091000');
INSERT INTO tbl_role_privilege (role_privilege_id, role_id, privilege_id)
VALUES ('7f000001-88ea-1791-8188-eab8dc099001', '7f000001-88ea-1791-8188-eab8dc097001', '7f000001-88ea-1791-8188-eab8dc091000');




INSERT INTO tbl_tenant (tenant_id, tenant_name, entity_status)
VALUES ('7f000001-88ea-1791-8188-eab8dc098000', 'R1VS', 'ACTIVE');
INSERT INTO tbl_tenant (tenant_id, tenant_name, entity_status)
VALUES ('7f000001-88ea-1791-8188-eab8dc098001', 'Super tenant', 'ACTIVE');

INSERT INTO tbl_tenant_parameter (tenant_parameter_id, parameter_name, parameter_value, tenant_id)
VALUES ('7f000001-88ea-1791-8188-eab8dc098100', 'color', 'red', '7f000001-88ea-1791-8188-eab8dc098000');

INSERT INTO tbl_user_role (user_role_id, user_id, role_id, tenant_id, entity_status, is_default)
VALUES ('7f000001-88ea-1791-8188-eab8dc096000', '7f000001-88ea-1791-8188-eab8dc090000',
        '7f000001-88ea-1791-8188-eab8dc097001', '7f000001-88ea-1791-8188-eab8dc098000', 'ACTIVE', true);

INSERT INTO tbl_tenant_invitation_code (tenant_invitation_code_id, code, entity_status, tenant_id)
VALUES ('7f000001-88ea-1791-8188-eab8dc098000', 'ABC123', 'ACTIVE', '7f000001-88ea-1791-8188-eab8dc098000');

INSERT INTO tbl_client (client_id, company_name, unique_taxpayer_identification, business, address, commune, city, type_of_purchase)
VALUES ('7f000001-88ea-1791-8188-eab8dc0c0000', 'Tech Solutions Ltda', '76.543.210-1', 'Servicios Informáticos', 'Av. Providencia 1234', 'Providencia', 'Santiago', 'DEL_GIRO');
INSERT INTO tbl_client (client_id, company_name, unique_taxpayer_identification, business, address, commune, city, type_of_purchase)
VALUES ('7f000001-88ea-1791-8188-eab8dc0c0001', 'Construcciones Andes S.A.', '96.123.456-7', 'Construcción', 'Camino a Melipilla Km 12', 'Maipú', 'Santiago', 'DEL_GIRO');
INSERT INTO tbl_client (client_id, company_name, unique_taxpayer_identification, business, address, commune, city, type_of_purchase)
VALUES ('7f000001-88ea-1791-8188-eab8dc0c0002', 'AgroExport Chile', '88.765.432-1', 'Exportación de Frutas', 'Ruta 5 Sur Km 320', 'San Fernando', 'O’Higgins', 'DEL_GIRO');
INSERT INTO tbl_client (client_id, company_name, unique_taxpayer_identification, business, address, commune, city, type_of_purchase)
VALUES ('7f000001-88ea-1791-8188-eab8dc0c0003', 'Medicina Integral SpA', '77.234.567-8', 'Clínica y Servicios Médicos', 'Av. La Florida 4500', 'La Florida', 'Santiago', 'DEL_GIRO');
INSERT INTO tbl_client (client_id, company_name, unique_taxpayer_identification, business, address, commune, city, type_of_purchase)
VALUES ('7f000001-88ea-1791-8188-eab8dc0c0004', 'Transporte Express Limitada', '79.987.654-3', 'Transporte y Logística', 'Av. Las Industrias 980', 'Quilicura', 'Santiago', 'DEL_GIRO');

INSERT INTO tbl_branch (id_branch, branch_name, address, commune, client_id)
VALUES ('7f000001-88ea-1791-8188-eab8dc0d0000', 'Sucursal Providencia', 'Av. Providencia 1250', 'Providencia',  '7f000001-88ea-1791-8188-eab8dc0c0000');
INSERT INTO tbl_branch (id_branch, branch_name, address, commune, client_id)
VALUES ('7f000001-88ea-1791-8188-eab8dc0d0001', 'Sucursal Maipú', 'Camino a Melipilla Km 14', 'Maipú', '7f000001-88ea-1791-8188-eab8dc0c0001');
INSERT INTO tbl_branch (id_branch, branch_name, address, commune, client_id)
VALUES ('7f000001-88ea-1791-8188-eab8dc0d0002', 'Sucursal San Fernando', 'Ruta 5 Sur Km 322', 'San Fernando', '7f000001-88ea-1791-8188-eab8dc0c0002');
INSERT INTO tbl_branch (id_branch, branch_name, address, commune, client_id)
VALUES ('7f000001-88ea-1791-8188-eab8dc0d0003', 'Sucursal La Florida', 'Av. La Florida 4520', 'La Florida', '7f000001-88ea-1791-8188-eab8dc0c0003');
INSERT INTO tbl_branch (id_branch, branch_name, address, commune, client_id)
VALUES ('7f000001-88ea-1791-8188-eab8dc0d0004', 'Sucursal Quilicura', 'Av. Las Industrias 990', 'Quilicura', '7f000001-88ea-1791-8188-eab8dc0c0004');

INSERT INTO tbl_equipment_type (equipment_type_id, type_name)
VALUES ('7f000001-88ea-1791-8188-eab8dc0e0000', 'Equipo de Rayos X Digital');
INSERT INTO tbl_equipment_type (equipment_type_id, type_name)
VALUES ('7f000001-88ea-1791-8188-eab8dc0e0001', 'Tomógrafo Computarizado (CT Scan)');
INSERT INTO tbl_equipment_type (equipment_type_id, type_name)
VALUES ('7f000001-88ea-1791-8188-eab8dc0e0002', 'Resonador Magnético (MRI)');
INSERT INTO tbl_equipment_type (equipment_type_id, type_name)
VALUES ('7f000001-88ea-1791-8188-eab8dc0e0003', 'Ecógrafo Portátil');

INSERT INTO tbl_equipment (equipment_id, equipment_model, equipment_brand, serial_number, equipment_type_id, branch_id)
VALUES ('7f000001-88ea-1791-8188-eab8dc0f0000', 'RX-5000D', 'Siemens', 'RXD123456', '7f000001-88ea-1791-8188-eab8dc0e0000', '7f000001-88ea-1791-8188-eab8dc0d0000');
INSERT INTO tbl_equipment (equipment_id, equipment_model, equipment_brand, serial_number, equipment_type_id, branch_id)
VALUES ('7f000001-88ea-1791-8188-eab8dc0f0001', 'CT-64Slice', 'Philips', 'CT987654', '7f000001-88ea-1791-8188-eab8dc0e0001', '7f000001-88ea-1791-8188-eab8dc0d0001');
INSERT INTO tbl_equipment (equipment_id, equipment_model, equipment_brand, serial_number, equipment_type_id, branch_id)
VALUES ('7f000001-88ea-1791-8188-eab8dc0f0002', 'MRI-3T', 'GE Healthcare', 'MRI456789', '7f000001-88ea-1791-8188-eab8dc0e0002', '7f000001-88ea-1791-8188-eab8dc0d0002');
INSERT INTO tbl_equipment (equipment_id, equipment_model, equipment_brand, serial_number, equipment_type_id, branch_id)
VALUES ('7f000001-88ea-1791-8188-eab8dc0f0003', 'ECO-PRO', 'Mindray', 'ECO112233', '7f000001-88ea-1791-8188-eab8dc0e0003', '7f000001-88ea-1791-8188-eab8dc0d0003');
















INSERT INTO tbl_country (country_id, name)
VALUES ('264850c6-54b0-4801-be5b-23bc239a99ec', 'Chile');

-- Regiones de Chile
INSERT INTO tbl_region (region_id, name, country_id)
VALUES
('e9dfcd5a-17d0-4e7f-8a33-d40233ea7342', 'Ñuble', '264850c6-54b0-4801-be5b-23bc239a99ec'),
('c49eb1b3-4b93-4f61-9db0-6c50c050a7c2', 'Arica y Parinacota', '264850c6-54b0-4801-be5b-23bc239a99ec'),
('f2db9b4c-9215-4b76-87a8-6b1e37c8e2bc', 'Tarapacá', '264850c6-54b0-4801-be5b-23bc239a99ec'),
('be84b230-c468-41f3-8a5a-64bb3b8bcd7e', 'Antofagasta', '264850c6-54b0-4801-be5b-23bc239a99ec'),
('9db92f42-56cc-4d6d-9eb9-bf5b0b78c5a8', 'Atacama', '264850c6-54b0-4801-be5b-23bc239a99ec'),
('ae2b6ae6-c67a-4b21-8891-8c3cdb6b02b7', 'Coquimbo', '264850c6-54b0-4801-be5b-23bc239a99ec'),
('c281c6e7-4e74-46a5-b45e-f0d5a8a8a4a6', 'Valparaíso', '264850c6-54b0-4801-be5b-23bc239a99ec'),
('f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b', 'Metropolitana de Santiago', '264850c6-54b0-4801-be5b-23bc239a99ec'),
('ceecfab0-b1f4-42a5-8a3a-d9f6a4e3cce1', 'O’Higgins', '264850c6-54b0-4801-be5b-23bc239a99ec'),
('d5f9b3a6-84e7-4e49-a563-6e5e4e9b4c2e', 'Maule', '264850c6-54b0-4801-be5b-23bc239a99ec'),
('e75f91bc-c171-48f3-94ab-4d9f1f6efc8e', 'Biobío', '264850c6-54b0-4801-be5b-23bc239a99ec'),
('a3d4e06e-c485-4e58-9d57-ef64d36a6278', 'Araucanía', '264850c6-54b0-4801-be5b-23bc239a99ec'),
('bf32a0f3-dc9e-4e9c-9b68-d0f8b0a65783', 'Los Ríos', '264850c6-54b0-4801-be5b-23bc239a99ec'),
('dd0c9fa4-cc21-42a1-8421-8eac7f707684', 'Los Lagos', '264850c6-54b0-4801-be5b-23bc239a99ec'),
('a9493a1e-2e2b-4504-8f1d-b7ae32ff1498', 'Aysén', '264850c6-54b0-4801-be5b-23bc239a99ec'),
('b7e5a65c-34f2-4a83-965d-cfd9b8c0d6e9', 'Magallanes', '264850c6-54b0-4801-be5b-23bc239a99ec');

-- Comunas de Ñuble
INSERT INTO tbl_commune (commune_id, name, region_id)
VALUES
('f02ca310-eb85-4d89-9992-674538049ac4', 'San Carlos', 'e9dfcd5a-17d0-4e7f-8a33-d40233ea7342'),
('4d5e0538-6b77-4d8d-97e4-b12f7a9c58a1', 'Chillán', 'e9dfcd5a-17d0-4e7f-8a33-d40233ea7342'),
('2b4e1c5b-bb0f-4ed0-8d7d-7690c78f3a10', 'Bulnes', 'e9dfcd5a-17d0-4e7f-8a33-d40233ea7342'),
('bfbc1f2e-5c61-4a4c-b0cf-f6d41ffb673e', 'Cobquecura', 'e9dfcd5a-17d0-4e7f-8a33-d40233ea7342'),
('d7e06585-f768-4d30-87c6-2dfe3d8bb6b7', 'Coelemu', 'e9dfcd5a-17d0-4e7f-8a33-d40233ea7342'),
('fbd24c5b-e93a-4e28-8b6b-1f6a9b0c58a6', 'Coihueco', 'e9dfcd5a-17d0-4e7f-8a33-d40233ea7342'),
('e88a4c50-5057-4c4c-9b58-98c6b49e3f48', 'El Carmen', 'e9dfcd5a-17d0-4e7f-8a33-d40233ea7342'),
('c3f408d8-cd4d-4096-8774-0a1bce64096c', 'Ninhue', 'e9dfcd5a-17d0-4e7f-8a33-d40233ea7342'),
('f2085dc8-9cc3-411d-9f98-8f8b9839ad63', 'Ñiquén', 'e9dfcd5a-17d0-4e7f-8a33-d40233ea7342'),
('a6b7d23c-85ff-4c25-80a6-5d9d7ffb9a68', 'Pemuco', 'e9dfcd5a-17d0-4e7f-8a33-d40233ea7342'),
('f4023dfc-7a84-47b8-9d39-c30b4c8b1b19', 'Pinto', 'e9dfcd5a-17d0-4e7f-8a33-d40233ea7342'),
('cdaecfe5-5697-41d1-9490-4f71b8c59b62', 'Portezuelo', 'e9dfcd5a-17d0-4e7f-8a33-d40233ea7342'),
('bf9c8c2d-08c7-4a3b-88b2-2bc7b1a48756', 'Quillón', 'e9dfcd5a-17d0-4e7f-8a33-d40233ea7342'),
('a5e7d3be-b3a3-4c8a-b8fc-98c9a482f08d', 'Quirihue', 'e9dfcd5a-17d0-4e7f-8a33-d40233ea7342'),
('fe3fc2dc-5081-43c9-97a7-8e2b3c48f1b4', 'Ránquil', 'e9dfcd5a-17d0-4e7f-8a33-d40233ea7342'),
('d8b6c0f1-df69-44a3-83d4-f8b9b6e7e7a4', 'San Fabián', 'e9dfcd5a-17d0-4e7f-8a33-d40233ea7342'),
('c91c1e3c-9c7e-439c-8b45-8f0e8c2c1b6e', 'San Ignacio', 'e9dfcd5a-17d0-4e7f-8a33-d40233ea7342'),
('abcf8f2a-3853-43a1-8a3a-9e1c8f8f7d6f', 'San Nicolás', 'e9dfcd5a-17d0-4e7f-8a33-d40233ea7342'),
('d7f1b9d8-6c4f-4f9e-8b45-7e3c6b2c8e5a', 'Treguaco', 'e9dfcd5a-17d0-4e7f-8a33-d40233ea7342'),
('fae3b8d1-3a48-4f9e-8b65-9e1d8c3a8e6a', 'Yungay', 'e9dfcd5a-17d0-4e7f-8a33-d40233ea7342');

-- Comunas de Arica y Parinacota
INSERT INTO tbl_commune (commune_id, name, region_id)
VALUES
('f0e9a0c1-3a1c-4c98-a2d4-5e8f6d9e0a1b', 'Arica', 'c49eb1b3-4b93-4f61-9db0-6c50c050a7c2'),
('be84b230-c468-41f3-8a5a-64bb3b8bcd7e', 'Camarones', 'c49eb1b3-4b93-4f61-9db0-6c50c050a7c2'),
('c2d3e4f5-6a7b-8c9d-0e1f-2a3b4c5d6e7f', 'General Lagos', 'c49eb1b3-4b93-4f61-9db0-6c50c050a7c2'),
('d3e4f5a6-7b8c-9d0e-1f2a-3b4c5d6e7f8a', 'Putre', 'c49eb1b3-4b93-4f61-9db0-6c50c050a7c2');

-- Comunas de Tarapacá
INSERT INTO tbl_commune (commune_id, name, region_id)
VALUES
('a1b2c3d4-5e6f-7a8b-9c0d-1e2f3a4b5c6d', 'Iquique', 'f2db9b4c-9215-4b76-87a8-6b1e37c8e2bc'),
('1d56e66a-1071-405a-83d2-4b1306f9f5c2', 'Alto Hospicio', 'f2db9b4c-9215-4b76-87a8-6b1e37c8e2bc'),
('446e34c9-9767-4d7e-b165-6186dc573b11', 'Pozo Almonte', 'f2db9b4c-9215-4b76-87a8-6b1e37c8e2bc'),
('8b6d5ae9-b987-4d46-9e38-842b7f1563da', 'Camiña', 'f2db9b4c-9215-4b76-87a8-6b1e37c8e2bc'),
('56b13646-25f4-48d7-917a-7d22a5079026', 'Colchane', 'f2db9b4c-9215-4b76-87a8-6b1e37c8e2bc'),
('62ddc2a0-f1a6-4b2d-ba9e-f6d2921ba4d3', 'Huara', 'f2db9b4c-9215-4b76-87a8-6b1e37c8e2bc'),
('9770e270-b635-40b6-8637-6817ea235bc4', 'Pica', 'f2db9b4c-9215-4b76-87a8-6b1e37c8e2bc');

-- Comunas de Antofagasta
INSERT INTO tbl_commune (commune_id, name, region_id)
VALUES
('6115aa8b-3383-48bd-b1dc-191f524bbc72', 'Antofagasta', 'be84b230-c468-41f3-8a5a-64bb3b8bcd7e'),
('60fa8528-0939-4544-a7ee-a5d1603d83b5', 'Mejillones', 'be84b230-c468-41f3-8a5a-64bb3b8bcd7e'),
('af097f65-df1a-4af0-8e74-b88b810fd0f9', 'Sierra Gorda', 'be84b230-c468-41f3-8a5a-64bb3b8bcd7e'),
('767e9b84-fff0-442a-afe3-b7597bc20179', 'Taltal', 'be84b230-c468-41f3-8a5a-64bb3b8bcd7e'),
('be610dc5-c989-469a-be73-661ebae5a1d0', 'Calama', 'be84b230-c468-41f3-8a5a-64bb3b8bcd7e'),
('23bb2e6f-2261-4691-8afd-cfc14406e355', 'Ollagüe', 'be84b230-c468-41f3-8a5a-64bb3b8bcd7e'),
('c2a5a70a-aca6-4f95-9d4a-69a5e8bbe510', 'San Pedro de Atacama', 'be84b230-c468-41f3-8a5a-64bb3b8bcd7e'),
('2c4704a6-842d-48c5-adaa-b55cae8d8ee2', 'Tocopilla', 'be84b230-c468-41f3-8a5a-64bb3b8bcd7e'),
('87027fa6-e454-4404-96eb-08036aa66d56', 'María Elena', 'be84b230-c468-41f3-8a5a-64bb3b8bcd7e');

-- Comunas de la Región Metropolitana de Santiago con UUID estáticos
INSERT INTO tbl_commune (commune_id, name, region_id)
VALUES
('7688c8af-561e-4c85-8e71-08968c35117b', 'Santiago', 'f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b'),
('dcc5842e-7b08-4cb1-aff0-ad8401477912', 'Cerrillos', 'f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b'),
('462f3a5b-1378-4354-9030-0bd743fe447a', 'Cerro Navia', 'f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b'),
('b4f778b7-59f4-4c85-8291-5201b9a94db9', 'Conchalí', 'f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b'),
('1c7d11bf-366c-44ff-a2c4-0fb3852a9a8c', 'El Bosque', 'f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b'),
('837fb475-aab1-4d9b-86e8-18be0c8a5cfc', 'Estación Central', 'f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b'),
('592fc76c-8784-4536-b20d-382354c1cea4', 'Huechuraba', 'f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b'),
('c290c476-ecdc-4d4a-b960-c2977d8daf92', 'Independencia', 'f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b'),
('6931b6f6-ab7d-43d2-b7c6-b97d72f75c4b', 'La Cisterna', 'f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b'),
('daec949d-6119-4704-8542-1d9698aed0e2', 'La Florida', 'f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b'),
('67553fbe-2d4b-4986-8056-eae9e6cebc0f', 'La Granja', 'f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b'),
('05b577f4-8fa5-4f94-96ce-4c670a233709', 'La Pintana', 'f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b'),
('31d6ce65-7932-4e7e-9f8c-99516437a9ed', 'La Reina', 'f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b'),
('f1bfcebd-601c-4ba7-babe-6669214c9245', 'Las Condes', 'f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b'),
('2697519b-18ff-47e8-ac5c-8dbefdc5359f', 'Lo Barnechea', 'f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b'),
('a43f97b3-0593-4900-8be5-7bf003c590cd', 'Lo Espejo', 'f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b'),
('cb8d6c02-f4f0-4c13-b053-84afd9b9a4d5', 'Lo Prado', 'f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b'),
('83e3dd76-53ca-45c7-8f77-988bd524f11d', 'Macul', 'f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b'),
('aa84bfd1-0751-46d6-b489-8aa9cabb5a75', 'Maipú', 'f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b'),
('e954aff1-9aff-4f0d-b5a2-14dc75c68e7c', 'Ñuñoa', 'f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b'),
('b6e81c19-72e2-4324-a256-76275c31c8f4', 'Pedro Aguirre Cerda', 'f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b'),
('a899a3fc-9e02-4600-a1a5-19059d1cc0e8', 'Peñalolén', 'f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b'),
('41807c50-bf3b-4eea-a83d-621e11c96daf', 'Providencia', 'f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b'),
('e77c0c11-766e-4952-84a3-786bd2e86ab4', 'Pudahuel', 'f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b'),
('21077de3-5648-4d32-ba50-346e7530a151', 'Quilicura', 'f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b'),
('dbc50298-67cd-407c-a0b0-07f0cfc50176', 'Quinta Normal', 'f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b'),
('c02d664c-d7cf-4d06-ac24-194c78ff5817', 'Recoleta', 'f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b'),
('8b22a6d4-5134-4272-8abb-bc2870ef1fdc', 'Renca', 'f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b'),
('f280560e-cb9a-4883-b875-90b327988d7c', 'San Joaquín', 'f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b'),
('233619a6-239c-47e4-b804-e1238bc2c6ed', 'San Miguel', 'f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b'),
('669e3b9f-cbff-4f46-b2c0-4f45202ecbf0', 'San Ramón', 'f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b'),
('a7a4f233-86bc-453b-bcc4-936f2fff4526', 'Vitacura', 'f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b'),
('cdff45b9-adf9-4712-8839-58f653070bf6', 'Colina', 'f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b'),
('4bfa9aaf-8073-44ad-9830-52e9b3d7042f', 'Lampa', 'f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b'),
('74ce8fad-6e80-4fab-8908-77276905d2e3', 'Tiltil', 'f02bfb10-1d1b-46e3-9c5f-2c83b0f0844b');


-- Comunas del Maule
INSERT INTO tbl_commune (commune_id, name, region_id)
VALUES
('f7e0b6e1-1f3d-4f8c-9e4f-6e7e7e3e7e6f', 'Talca', 'd5f9b3a6-84e7-4e49-a563-6e5e4e9b4c2e'),
('a6e7e8f2-2d4e-4f7d-9f5f-7e7e7d4f7e7f', 'Curicó', 'd5f9b3a6-84e7-4e49-a563-6e5e4e9b4c2e'),
('c8f8e9f3-3d5e-4f8e-9f6f-7e7e7e5f7e8f', 'Linares', 'd5f9b3a6-84e7-4e49-a563-6e5e4e9b4c2e'),
('d9f0f1e5-4d6e-4f9f-9f7f-7e7e7d6f7e9f', 'Molina', 'd5f9b3a6-84e7-4e49-a563-6e5e4e9b4c2e'),
('e0f1f2e7-5d7e-4fae-9f8f-7e7e7e7f7e0f', 'San Clemente', 'd5f9b3a6-84e7-4e49-a563-6e5e4e9b4c2e'),
('f1f2f3e9-6d8e-4fbe-9f9f-7e7e7d8f7f1f', 'San Javier', 'd5f9b3a6-84e7-4e49-a563-6e5e4e9b4c2e'),
('a2f3f4e1-7d9e-4fce-9f0f-7e7e7e9f7e2f', 'Teno', 'd5f9b3a6-84e7-4e49-a563-6e5e4e9b4c2e'),
('b3f4f5e3-8d0e-4fde-9f1f-7e7e7d0f7f3f', 'Villa Alegre', 'd5f9b3a6-84e7-4e49-a563-6e5e4e9b4c2e'),
('c4f5f6e5-9d1e-4fee-9f2f-7e7e7e1f7e4f', 'Yerbas Buenas', 'd5f9b3a6-84e7-4e49-a563-6e5e4e9b4c2e'),
('d5f6f7e7-0d2f-4ffe-9f3f-7e7e7e2f7e5f', 'Colbún', 'd5f9b3a6-84e7-4e49-a563-6e5e4e9b4c2e'),
('e6f7f8e9-1d3f-4e0e-9f4f-7e7e7e3f7e6f', 'Constitución', 'd5f9b3a6-84e7-4e49-a563-6e5e4e9b4c2e'),
('f7f8f9e1-2d4f-4e1e-9f5f-7e7e7e4f7e8f', 'Curepto', 'd5f9b3a6-84e7-4e49-a563-6e5e4e9b4c2e'),
('a8f9f0e3-3d5f-4e2e-9f6f-7e7e7e5f7e9f', 'Empedrado', 'd5f9b3a6-84e7-4e49-a563-6e5e4e9b4c2e'),
('b9f0f1e5-4d6f-4e3e-9f7f-7e7e7e6f7e0f', 'Maule', 'd5f9b3a6-84e7-4e49-a563-6e5e4e9b4c2e'),
('c0f1f2e7-5d7f-4e4e-9f8f-7e7e7e7f7e1f', 'Pelarco', 'd5f9b3a6-84e7-4e49-a563-6e5e4e9b4c2e'),
('d1f2f3e9-6d8f-4e5e-9f9f-7e7e7d8f7f2f', 'Pencahue', 'd5f9b3a6-84e7-4e49-a563-6e5e4e9b4c2e'),
('e2f3f4e1-7d9f-4e6e-9f0f-7e7e7e9f7e3f', 'Río Claro', 'd5f9b3a6-84e7-4e49-a563-6e5e4e9b4c2e'),
('f3f4f5e3-8d0f-4e7e-9f1f-7e7e7d0f7f4f', 'San Rafael', 'd5f9b3a6-84e7-4e49-a563-6e5e4e9b4c2e');
