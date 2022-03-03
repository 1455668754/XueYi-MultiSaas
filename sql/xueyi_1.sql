SET NAMES utf8mb4;

-- ----------------------------
-- 1、租户信息表
-- ----------------------------
drop table if exists te_tenant;
create table te_tenant (
  id		                bigint	            not null                                comment '租户Id',
  strategy_id		        bigint	            not null                                comment '策略Id',
  name		                varchar(50)	        not null unique	                        comment '租户账号',
  system_name		        varchar(50)	        not null 	                            comment '系统名称',
  nick		                varchar(50)	        not null 	                            comment '租户名称',
  logo		                varchar(200)	    default ''	                            comment '租户logo',
  name_frequency            tinyint             default 0                               comment '账号修改次数',
  is_lessor                 char(1)             not null default 'N'	                comment '超管租户（Y是 N否）',
  sort                      int unsigned        not null default 0                      comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  remark                    varchar(1000)       default null                            comment '备注',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  is_default                char(1)             not null default 'N'	                comment '默认租户（Y是 N否）',
  del_flag		            tinyint             not null default 0                      comment '删除标志（0正常 1删除）',
  primary key (id),
  unique key (name)
) engine=innodb comment = '租户信息表';

-- ----------------------------
-- 初始化-租户信息表数据
-- ----------------------------
insert into te_tenant (id, is_lessor, is_default, strategy_id,  name, system_name, nick, logo)
values (-1, 'Y', 'Y', 1, 'administrator', '租管租户', 'xueYi1', 'https://images.gitee.com/uploads/images/2021/1101/141601_d68e92a4_7382127.jpeg'),
       ( 1, 'N', 'N', 1, 'xueYi', '雪忆科技', 'xueYi1', 'https://images.gitee.com/uploads/images/2021/1101/141601_d68e92a4_7382127.jpeg');

-- ----------------------------
-- 2、策略信息表
-- ----------------------------
drop table if exists te_strategy;
create table te_strategy (
  id		                bigint	            not null                                comment '策略Id',
  name                      varchar(500)	    not null default ''	                    comment '策略名称',
  source_id                 bigint	            not null	                            comment '数据源Id',
  source_slave              varchar(500)	    not null	                            comment '数据源编码',
  sort                      int unsigned        not null default 0                      comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  remark                    varchar(1000)       default null                            comment '备注',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  is_default                char(1)             not null default 'N'	                comment '默认策略（Y是 N否）',
  del_flag		            tinyint             not null default 0                      comment '删除标志（0正常 1删除）',
primary key (id)
) engine=innodb comment = '数据源策略表';

-- ----------------------------
-- 初始化-策略信息表数据
-- ----------------------------
insert into te_strategy(id, name, is_default, source_id, source_slave, sort)
values (1, '默认注册策略', 'Y', 1, 'slave', 1);

-- ----------------------------
-- 4、数据源表|管理系统数据源信息 | 主库有且只能有一个，用途：主要用于存储公共数据，具体看后续文档或视频
-- ----------------------------
drop table if exists te_source;
create table te_source (
  id		                bigint	            not null                                comment '数据源Id',
  name		                varchar(50)	        not null                                comment '数据源名称',
  slave		                varchar(500)	    not null	                            comment '数据源编码',
  driver_class_name		    varchar(500)	    not null	                            comment '驱动',
  url_prepend	            varchar(500)	    not null	                            comment '连接地址',
  url_append	            varchar(500)	    not null default ''	                    comment '连接参数',
  username	                varchar(500)	    not null	                            comment '用户名',
  password	                varchar(500)	    not null default ''	                    comment '密码',
  sort                      int unsigned        not null default 0                      comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  remark                    varchar(1000)       default null                            comment '备注',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  is_default                char(1)             not null default 'N'	                comment '默认数据源（Y是 N否）',
  del_flag		            tinyint             not null default 0                      comment '删除标志（0正常 1删除）',
primary key (id),
unique key (name),
unique key (slave)
) engine=innodb comment = '数据源表';

-- ----------------------------
-- 初始化-数据源表数据 | 这条数据为我的基础库，实际使用时调整成自己的库即可
-- ----------------------------
insert into te_source(id, name, is_default, slave, driver_class_name, url_prepend, url_append, username, password)
values (1, '注册数据源', 'Y', 'slave', 'com.mysql.cj.jdbc.Driver', 'jdbc:mysql://localhost:3306/xy-cloud1', '?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8', 'root', 'password');

-- ----------------------------
-- 6、模块信息表
-- ----------------------------
drop table if exists sys_module;
create table sys_module (
  id		                bigint	            not null                                comment '模块Id',
  name		                varchar(50)	        not null	                            comment '模块名称',
  logo                      varchar(2000)	    default '' 	        	                comment '模块logo',
  path                      varchar(200)        not null default ''                     comment '路由地址',
  param_path                varchar(255)        default null                            comment '路由参数',
  type		                char(1)	            not null default '0'	                comment '模块类型（0常规 1内嵌 2外链）',
  hide_module		        char(1)	            not null default 'N'	                comment '模块显隐状态（0显示 1隐藏）',
  sort                      int unsigned        not null default 0                      comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  remark                    varchar(1000)       default null                            comment '备注',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  is_common                 char(1)             not null default 'N'	                comment '公共模块（0公共 1私有）',
  is_default                char(1)             not null default 'N'	                comment '默认模块（Y是 N否）',
  del_flag		            tinyint             not null default 0                      comment '删除标志（0正常 1删除）',
  tenant_id		            bigint	            not null                                comment '租户Id',
  primary key (id)
) engine=innodb comment = '模块信息表';

# ----------------------------
# 初始化-模块信息表数据
# ----------------------------
insert into sys_module (id, name, type, path, param_path, is_default, hide_module, logo, remark, is_common, tenant_id)
values (1, '基础平台' ,    '0', '', '', 'Y', '0', 'https://images.gitee.com/uploads/images/2021/1101/141155_f3dfce1d_7382127.jpeg', '基础平台', '0', 0),
       (2, '开发者平台' , '0', '', '', 'Y', '0', 'https://images.gitee.com/uploads/images/2021/1101/141601_d68e92a4_7382127.jpeg', '开发者平台', '1', -1);

-- ----------------------------
-- 7、菜单权限表
-- ----------------------------
drop table if exists sys_menu;
create table sys_menu (
  id                        bigint              not null                                comment '菜单Id',
  parent_id                 bigint              not null default 0                      comment '父菜单Id',
  name                      varchar(100)        not null                                comment '菜单名称',
  title                     varchar(50)         not null                                comment '菜单标题 | 多语言',
  ancestors                 varchar(2000)       default ''                              comment '祖级列表',
  path                      varchar(200)        default null                            comment '路由地址',
  frame_src                 varchar(200)        default null                            comment '外链地址 | 仅页面类型为外链时生效',
  component                 varchar(255)        default null                            comment '组件路径',
  param_path                varchar(255)        default null                            comment '路由参数',
  transition_name           varchar(255)        default null                            comment '路由切换动画',
  ignore_route              char(1)             not null default  'N'                   comment '是否忽略路由（Y是 N否）',
  is_cache                  char(1)             not null default  'N'                   comment '是否缓存（Y是 N否）',
  is_affix                  char(1)             not null default  'N'                   comment '是否固定标签（Y是 N否）',
  is_disabled               char(1)             not null default  'N'                   comment '是否禁用（Y是 N否）',
  frame_type                char(1)             not null default  '0'                   comment '页面类型（0常规 1内嵌 2外链）',
  menu_type                 char(1)             not null                                comment '菜单类型（M目录 C菜单 X详情 F按钮）',
  hide_tab                  char(1)             not null default  'N'                   comment '标签显隐状态（0显示 1隐藏）',
  hide_menu                 char(1)             not null default  'N'                   comment '菜单显隐状态（0显示 1隐藏）',
  hide_breadcrumb           char(1)             not null default  'N'                   comment '面包屑路由显隐状态（0显示 1隐藏）',
  hide_children             char(1)             not null default  'N'                   comment '子菜单显隐状态（0显示 1隐藏）',
  hide_path_for_children    char(1)             not null default  'N'                   comment '是否在子级菜单的完整path中忽略本级path（0显示 1隐藏）',
  dynamic_level             int                 not null default 1                      comment '详情页可打开Tab页数',
  real_path                 varchar(255)        default null                            comment '详情页的实际Path',
  perms                     varchar(100)        default null                            comment '权限标识',
  icon                      varchar(100)        default null                            comment '菜单图标',
  sort                      int unsigned        not null default  0                     comment '显示顺序',
  status                    char(1)             not null default  '0'                   comment '状态（0正常 1停用）',
  remark                    varchar(1000)       default null                            comment '备注',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  is_common                 char(1)             not null default  'N'                   comment '公共菜单（0公共 1私有）',
  is_default                char(1)             not null default  'N'                   comment '默认菜单（Y是 N否）',
  del_flag                  tinyint             not null default  0                     comment '删除标志(0正常 1删除)',
  module_id                 bigint              not null                                comment '模块Id',
  tenant_id                 bigint              not null                                comment '租户Id',
  primary key (id),
  unique (name)
) engine=innodb comment = '菜单权限表';

insert into sys_menu (id, parent_id, name, title, ancestors, path, frame_src, component, param_path, transition_name, ignore_route, is_cache, is_affix, is_disabled, frame_type, menu_type, hide_tab, hide_menu, hide_breadcrumb, hide_children, hide_path_for_children, dynamic_level, real_path, perms, icon, sort, remark, is_common, is_default, module_id, tenant_id)
values (0, 0, 'd346c7dbf265416086dbe2951a7465b2', '默认菜单', '', 'default', null, null, null, null, 'N', 'N', 'N', 'N', '0', 'M', '0', '0', '0', '0', '0', 1, null, null, '', 1, '目录:默认菜单', '0', 'Y', 1, 0),
       (13000000, 0, '4be0456e05a7422d9f1c82fb7bf19377', '组织管理', '0', 'organize', null, '', null, null, 'N', 'N', 'N', 'N', '0', 'M', '0', '0', '0', '0', '0', 1, null, '', 'ant-design:apartment-outlined', 4, '目录:组织管理', '0', 'Y', 1, 0),
           (13010000, 13000000, '0a612ebdfaa64ea4b8fd8cfd787042ea', '部门管理', '0,13000000', 'dept', null, 'system/organize/dept/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, 'organize:dept:list', 'ant-design:partition-outlined', 1, '菜单:部门管理', '0', 'Y', 1, 0),
               (13010100, 13010000, 'c67c9520af2c48d89e1bae0d2a0342d1', '部门详情', '0,13000000,13010000', 'deptDetail/:id', null, 'system/organize/dept/DeptDetail', null, null, 'N', 'Y', 'N', 'N', '0', 'X', '0', '1', '0', '0', '0', 5, null, 'organize:dept:single', null, 2, '详情:部门详情', '0', 'Y', 1, 0),
               (13010200, 13010000, '5e50660b195a49c4860607aec14fc330', '部门新增', '0,13000000,13010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'organize:dept:add', null, 3, '按钮:部门新增', '0', 'Y', 1, 0),
               (13010300, 13010000, 'ad0a108f2c35433daf1864cf4cf93b64', '部门修改', '0,13000000,13010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'organize:dept:edit', null, 4, '按钮:部门修改', '0', 'Y', 1, 0),
               (13010400, 13010000, 'df7b77ef91c8433f97a9848ebe9b714b', '部门修改状态', '0,13000000,13010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'organize:dept:editStatus', null, 5, '按钮:部门修改状态', '0', 'Y', 1, 0),
               (13010500, 13010000, '83a7e4bafb154a938b2c67d7f3e540d5', '部门删除', '0,13000000,13010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'organize:dept:delete', null, 6, '按钮:部门删除', '0', 'Y', 1, 0),
               (13010600, 13010000, 'ba6fe94f191649468a35d1799efedd56', '部门导入', '0,13000000,13010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'organize:dept:import', null, 7, '按钮:部门导入', '0', 'Y', 1, 0),
               (13010700, 13010000, 'd3ae0d58b37d4bd6bd38db6a5ccefe7e', '部门导出', '0,13000000,13010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'organize:dept:export', null, 8, '按钮:部门导出', '0', 'Y', 1, 0),
           (13020000, 13000000, 'ecdcabe9c00a408796626a83d0129fdb', '岗位管理', '0,13000000', 'post', null, 'system/organize/post/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, 'organize:post:list', 'ant-design:idcard-outlined', 1, '菜单:岗位管理', '0', 'Y', 1, 0),
               (13020100, 13020000, '270eab9abe6a4e58b8c06d1339b46009', '岗位详情', '0,13000000,13020000', 'postDetail/:id', null, 'system/organize/post/PostDetail', null, null, 'N', 'Y', 'N', 'N', '0', 'X', '0', '1', '0', '0', '0', 5, null, 'organize:post:single', null, 2, '详情:岗位详情', '0', 'Y', 1, 0),
               (13020200, 13020000, '5ee95c2e5af8481ca6346faa82974251', '岗位新增', '0,13000000,13020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'organize:post:add', null, 3, '按钮:岗位新增', '0', 'Y', 1, 0),
               (13020300, 13020000, '082b9043d201490badbb403e2de9d0e1', '岗位修改', '0,13000000,13020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'organize:post:edit', null, 4, '按钮:岗位修改', '0', 'Y', 1, 0),
               (13020400, 13020000, '6a9ffc830afd4f3d8f74a047d0619e0f', '岗位修改状态', '0,13000000,13020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'organize:post:editStatus', null, 5, '按钮:岗位修改状态', '0', 'Y', 1, 0),
               (13020500, 13020000, 'baf3aa4d771a4ac5b80c0192ae52964b', '岗位删除', '0,13000000,13020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'organize:post:delete', null, 6, '按钮:岗位删除', '0', 'Y', 1, 0),
               (13020600, 13020000, 'f41ed2a5ba68445f9084bb6f2dee5db6', '岗位导入', '0,13000000,13020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'organize:post:import', null, 7, '按钮:岗位导入', '0', 'Y', 1, 0),
               (13020700, 13020000, 'a5c252aace4243eea2c5102ea14e347a', '岗位导出', '0,13000000,13020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'organize:post:export', null, 8, '按钮:岗位导出', '0', 'Y', 1, 0),
           (13030000, 13000000, 'f450bb13df9f42618ecbe62c46346d3e', '用户管理', '0,13000000', 'user', null, 'system/organize/user/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, 'organize:user:list', 'ant-design:user-outlined', 1, '菜单:用户管理', '0', 'Y', 1, 0),
               (13030100, 13030000, '3584b9c409ab49b79b2888936de01a2d', '用户详情', '0,13000000,13030000', 'userDetail/:id', null, 'system/organize/user/UserDetail', null, null, 'N', 'Y', 'N', 'N', '0', 'X', '0', '1', '0', '0', '0', 5, null, 'organize:user:single', null, 2, '详情:用户详情', '0', 'Y', 1, 0),
               (13030200, 13030000, '679d5eccf32d43639898d6d5a347d643', '用户新增', '0,13000000,13030000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'organize:user:add', null, 3, '按钮:用户新增', '0', 'Y', 1, 0),
               (13030300, 13030000, 'bc20a8b4c70f42ada4666269cc6a7624', '用户修改', '0,13000000,13030000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'organize:user:edit', null, 4, '按钮:用户修改', '0', 'Y', 1, 0),
               (13030400, 13030000, '5fb9a4cbaa1f4059aff2f22ff02a78f0', '用户修改状态', '0,13000000,13030000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'organize:user:editStatus', null, 5, '按钮:用户修改状态', '0', 'Y', 1, 0),
               (13030500, 13030000, '6669f111d1974ec28b82e830ad2195c2', '用户删除', '0,13000000,13030000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'organize:user:delete', null, 6, '按钮:用户删除', '0', 'Y', 1, 0),
               (13030600, 13030000, '2527782114544c53acdff12dc8e09a34', '用户导入', '0,13000000,13030000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'organize:user:import', null, 7, '按钮:用户导入', '0', 'Y', 1, 0),
               (13030700, 13030000, 'abc1c9ec3a73477184384bbfc37cf8a6', '用户导出', '0,13000000,13030000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'organize:user:export', null, 8, '按钮:用户导出', '0', 'Y', 1, 0),
       (14000000, 0, '61ac2678fa5a4cd7977c118114ff1828', '权限管理', '0', 'authority', null, '', null, null, 'N', 'N', 'N', 'N', '0', 'M', '0', '0', '0', '0', '0', 1, null, '', 'ant-design:safety-certificate-outlined', 4, '目录:权限管理', '0', 'Y', 1, 0),
           (14010000, 14000000, '7fdc19cde2494105b5b1ee951a560904', '模块管理', '0,14000000', 'module', null, 'system/authority/module/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, 'authority:module:list', 'ant-design:appstore-add-outlined', 1, '菜单:模块管理', '0', 'Y', 1, 0),
               (14010100, 14010000, 'b6aeef72800e4082ace3673e454a66b6', '模块详情', '0,14000000,14010000', 'moduleDetail/:id', null, 'system/authority/module/ModuleDetail', null, null, 'N', 'Y', 'N', 'N', '0', 'X', '0', '1', '0', '0', '0', 5, null, 'authority:module:single', null, 2, '详情:模块详情', '0', 'Y', 1, 0),
               (14010200, 14010000, '96047ce6aead414bab788070a65abab9', '模块新增', '0,14000000,14010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'authority:module:add', null, 3, '按钮:模块新增', '0', 'Y', 1, 0),
               (14010300, 14010000, 'ec5c9fc0b5ba4c9794f5b87b031c3104', '模块修改', '0,14000000,14010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'authority:module:edit', null, 4, '按钮:模块修改', '0', 'Y', 1, 0),
               (14010400, 14010000, 'ca53bb0ac4524d58baf1e647e0d0da68', '模块修改状态', '0,14000000,14010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'authority:module:editStatus', null, 5, '按钮:模块修改状态', '0', 'Y', 1, 0),
               (14010500, 14010000, '37a9b9cc2bcf4e7a98ea535526b3a597', '模块删除', '0,14000000,14010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'authority:module:delete', null, 6, '按钮:模块删除', '0', 'Y', 1, 0),
               (14010600, 14010000, '01e246c784d04ef1951b2cade43e104b', '模块导入', '0,14000000,14010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'authority:module:import', null, 7, '按钮:模块导入', '0', 'Y', 1, 0),
               (14010700, 14010000, '559dae9283fb41178ee16ca5f7838ac7', '模块导出', '0,14000000,14010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'authority:module:export', null, 8, '按钮:模块导出', '0', 'Y', 1, 0),
           (14020000, 14000000, 'bba8d25857ec45f38a8946c8c74182c2', '菜单管理', '0,14000000', 'menu', null, 'system/authority/menu/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, 'authority:menu:list', 'ant-design:bars-outlined', 1, '菜单:菜单管理', '0', 'Y', 1, 0),
               (14020100, 14020000, '5e4b17aeae124b91b7bad8f4dd11b7b4', '菜单详情', '0,14000000,14020000', 'menuDetail/:id', null, 'system/authority/menu/MenuDetail', null, null, 'N', 'Y', 'N', 'N', '0', 'X', '0', '1', '0', '0', '0', 5, null, 'authority:menu:single', null, 2, '详情:菜单详情', '0', 'Y', 1, 0),
               (14020200, 14020000, 'a1ea4b626a104a5380a6a2ba3eddcd2e', '菜单新增', '0,14000000,14020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'authority:menu:add', null, 3, '按钮:菜单新增', '0', 'Y', 1, 0),
               (14020300, 14020000, 'af5e2c3864eb4ef3a602f3d5619a53d4', '菜单修改', '0,14000000,14020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'authority:menu:edit', null, 4, '按钮:菜单修改', '0', 'Y', 1, 0),
               (14020400, 14020000, '69ebe38822d54e81b828c36a8dfdb479', '菜单修改状态', '0,14000000,14020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'authority:menu:editStatus', null, 5, '按钮:菜单修改状态', '0', 'Y', 1, 0),
               (14020500, 14020000, 'ed7bdf2e22244a72b3963e9750d2df98', '菜单删除', '0,14000000,14020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'authority:menu:delete', null, 6, '按钮:菜单删除', '0', 'Y', 1, 0),
               (14020600, 14020000, 'ae18f8022bf74bf991c0711db16e3acc', '菜单导入', '0,14000000,14020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'authority:menu:import', null, 7, '按钮:菜单导入', '0', 'Y', 1, 0),
               (14020700, 14020000, '1c08bd6fa572486f805d7cea86d1d644', '菜单导出', '0,14000000,14020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'authority:menu:export', null, 8, '按钮:菜单导出', '0', 'Y', 1, 0),
           (14030000, 14000000, '88c136711d98441699a6013ef27a356a', '角色管理', '0,14000000', 'role', null, 'system/authority/role/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, 'authority:role:list', 'ant-design:team-outlined', 1, '菜单:角色管理', '0', 'Y', 1, 0),
               (14030100, 14030000, '458c8e2ae43e47059b978504dd363a94', '角色详情', '0,14000000,14030000', 'roleDetail/:id', null, 'system/authority/role/RoleDetail', null, null, 'N', 'Y', 'N', 'N', '0', 'X', '0', '1', '0', '0', '0', 5, null, 'authority:role:single', null, 2, '详情:角色详情', '0', 'Y', 1, 0),
               (14030200, 14030000, '949497ca68724eb18f43b550c4c4bf5d', '角色新增', '0,14000000,14030000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'authority:role:add', null, 3, '按钮:角色新增', '0', 'Y', 1, 0),
               (14030300, 14030000, '77604dc9a47b4cc296e9f41cb76f81f5', '角色修改', '0,14000000,14030000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'authority:role:edit', null, 4, '按钮:角色修改', '0', 'Y', 1, 0),
               (14030400, 14030000, 'ed3d7dfa06424f1abe271058aa3abbd9', '角色修改状态', '0,14000000,14030000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'authority:role:editStatus', null, 5, '按钮:角色修改状态', '0', 'Y', 1, 0),
               (14030500, 14030000, '8bd13d9c71624e60a288d424a6a96670', '角色权限分配', '0,14000000,14030000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'authority:role:auth', null, 5, '按钮:角色权限分配', '0', 'Y', 1, 0),
               (14030600, 14030000, '54dc750a6076494ebc40e62b5d775db7', '角色删除', '0,14000000,14030000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'authority:role:delete', null, 6, '按钮:角色删除', '0', 'Y', 1, 0),
               (14030700, 14030000, '0cc43f7717ca4e2a9b0d8e4631fe1451', '角色导入', '0,14000000,14030000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'authority:role:import', null, 7, '按钮:角色导入', '0', 'Y', 1, 0),
               (14030800, 14030000, '8982c4c0b99f45b1a34c1cd146e6a116', '角色导出', '0,14000000,14030000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'authority:role:export', null, 8, '按钮:角色导出', '0', 'Y', 1, 0),
       (15000000, 0, 'de6e93785ba944e1bde5538e92bd7b91', '公告管理', '0', 'notice', null, '', null, null, 'N', 'N', 'N', 'N', '0', 'M', '0', '0', '0', '0', '0', 1, null, '', 'ant-design:schedule-outlined', 4, '目录:公告管理', '0', 'Y', 1, 0),
           (15010000, 15000000, 'b7214522d55c4f1ebd381859dc6774bb', '通知公告', '0,15000000', 'notice', null, 'system/notice/notice/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, 'system:notice:list', 'ant-design:notification-outlined', 1, '菜单:通知公告管理', '0', 'Y', 1, 0),
               (15010100, 15010000, '3b77bf3ae1264e5a960262cd898a9279', '通知公告详情', '0,15000000,15010000', 'noticeDetail/:id', null, 'system/notice/notice/NoticeDetail', null, null, 'N', 'Y', 'N', 'N', '0', 'X', '0', '1', '0', '0', '0', 5, null, 'system:notice:single', null, 2, '详情:通知公告详情', '0', 'Y', 1, 0),
               (15010200, 15010000, '9d21b1e6c2134ac9943db2d1ffa4d61f', '通知公告新增', '0,15000000,15010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'system:notice:add', null, 3, '按钮:通知公告新增', '0', 'Y', 1, 0),
               (15010300, 15010000, '04b84309c6544e20bf40df26349b4212', '通知公告修改', '0,15000000,15010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'system:notice:edit', null, 4, '按钮:通知公告修改', '0', 'Y', 1, 0),
               (15010400, 15010000, '4de8d945df96492ea79bcc1d98100051', '通知公告修改状态', '0,15000000,15010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'system:notice:editStatus', null, 5, '按钮:通知公告修改状态', '0', 'Y', 1, 0),
               (15010500, 15010000, '010e949049864492b046883b8571063d', '通知公告删除', '0,15000000,15010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'system:notice:delete', null, 6, '按钮:通知公告删除', '0', 'Y', 1, 0),
               (15010600, 15010000, 'fb58b0e282154fe8802342fd4c0e14cf', '通知公告导入', '0,15000000,15010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'system:notice:import', null, 7, '按钮:通知公告导入', '0', 'Y', 1, 0),
               (15010700, 15010000, '257064a1e547431bbfec47d5be281e67', '通知公告导出', '0,15000000,15010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'system:notice:export', null, 8, '按钮:通知公告导出', '0', 'Y', 1, 0),
       (16000000, 0, 'fa8965418a3540a99a62c805a4616e59', '系统管理', '0', 'system', null, '', null, null, 'N', 'N', 'N', 'N', '0', 'M', '0', '0', '0', '0', '0', 1, null, '', 'ant-design:control-outlined', 4, '目录:系统管理', '0', 'Y', 1, 0),
       (17000000, 0, 'e24afc855afc485d961b865d5d999811', '系统监控', '0', 'monitor', null, '', null, null, 'N', 'N', 'N', 'N', '0', 'M', '0', '0', '0', '0', '0', 1, null, '', 'ant-design:eye-outlined', 4, '目录:系统监控', '0', 'Y', 1, 0),
           (17010000, 17000000, 'f4a053ab18c84a82be6b2f04d3adaaac', '在线用户', '0,17000000', 'online', null, 'system/monitor/online/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, 'monitor:online:list', 'ant-design:contacts-outlined', 1, '菜单:在线用户管理', '0', 'Y', 1, 0),
               (17010100, 17010000, '6829ff5c29894306b2d4fa0d6769e6e6', '在线用户强退', '0,17000000,17010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'monitor:online:forceLogout', null, 6, '按钮:在线用户强退', '0', 'Y', 1, 0),
           (17020000, 17000000, '693be0ee3df14643ba93acf1b0e2df97', '日志管理', '0,17000000', 'log', null, '', null, null, 'N', 'N', 'N', 'N', '0', 'M', '0', '0', '0', '0', '0', 1, null, '', 'ant-design:file-text-outlined', 4, '目录:日志管理', '0', 'Y', 1, 0),
               (17020100, 17020000, '89d2de0ab59248a59f41d097a41517b5', '登录日志', '0,17000000,17020000', 'loginLog', null, 'system/monitor/loginLog/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, 'monitor:loginLog:list', 'xy_organization', 1, '菜单:登录日志', '0', 'Y', 1, 0),
                   (17020101, 17020100, '10afaa52d5be4a9ca9de5da3567d3d47', '登录日志删除', '0,17000000,17020000,17020100', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'monitor:loginLog:delete', null, 6, '按钮:登录日志删除', '0', 'Y', 1, 0),
                   (17020102, 17020100, 'f29ae2f1ec794de992109776f46743cc', '登录日志导出', '0,17000000,17020000,17020100', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'monitor:loginLog:export', null, 8, '按钮:登录日志导出', '0', 'Y', 1, 0),
               (17020200, 17020000, '7a3b5c396f8647499a9c9cd97712324a', '操作日志', '0,17000000,17020000', 'operateLog', null, 'system/monitor/operateLog/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, 'monitor:operateLog:list', 'xy_organization', 1, '菜单:操作日志管理', '0', 'Y', 1, 0),
                   (17020201, 17020000, '8b4266f509f748baac4f86739d7bb8c9', '操作日志详情', '0,17000000,17020000,17020200', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'monitor:operateLog:add', null, 2, '按钮:操作日志详情', '0', 'Y', 1, 0),
                   (17020202, 17020000, '8340af3b5dcc44e2837ef92e7de4da9d', '操作日志删除', '0,17000000,17020000,17020200', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'monitor:operateLog:delete', null, 3, '按钮:操作日志删除', '0', 'Y', 1, 0),
                   (17020203, 17020000, '05598058a8794251b454025d863c6daa', '操作日志导出', '0,17000000,17020000,17020200', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'monitor:operateLog:export', null, 4, '按钮:操作日志导出', '0', 'Y', 1, 0),
       (23000000, 0, 'fbaad9604c74427592126a9a0e756067', '系统工具', '0', 'generate', null, '', null, null, 'N', 'N', 'N', 'N', '0', 'M', '0', '0', '0', '0', '0', 1, null, '', 'xy_organization', 3, '目录:系统工具', '0', 'Y', 2, 0),
           (23010000, 23000000, 'ebd46c2fd3c3429896de95a82bcf1d8b', '代码生成', '0,23000000', 'gen', null, 'gen/generate/gen/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, 'generate:gen:list', 'xy_organization', 1, '菜单:代码生成', '0', 'Y', 2, 0),
               (23010100, 23010000, 'b489b7b0e645471eb42ed8b1f0365d32', '代码生成配置', '0,23000000,23010000', 'generateDetail/:id', null, 'gen/generate/gen/GenDetail', null, null, 'N', 'Y', 'N', 'N', '0', 'X', '0', '1', '0', '0', '0', 5, null, 'generate:gen:edit', 'xy_organization', 2, '详情:代码生成配置', '0', 'Y', 2, 0),
               (23010200, 23010000, 'c49d2b1d7d6640e7bd331aa494b05e58', '代码生成预览', '0,23000000,23010000', 'codeDetail/:id', null, 'gen/generate/gen/CodeDetail', null, null, 'N', 'Y', 'N', 'N', '0', 'X', '0', '1', '0', '0', '0', 5, null, 'generate:gen:preview', 'xy_organization', 3, '详情:代码生成预览', '0', 'Y', 2, 0),
               (23010300, 23010000, 'f264257d3d4948bd9204ee5ba39b1661', '代码生成导入', '0,23000000,23010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'generate:gen:import', null, 4, '按钮:代码生成导入', '0', 'Y', 2, 0),
               (23010400, 23010000, '828aa317e06b48a2adc9d783ee968d99', '代码生成下载', '0,23000000,23010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'generate:gen:code', null, 5, '按钮:代码生成下载', '0', 'Y', 2, 0),
               (23010500, 23010000, '29db55acc054471f8b68411f99be1dee', '代码生成删除', '0,23000000,23010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'generate:gen:delete', null, 6, '按钮:代码生成删除', '0', 'Y', 2, 0),
       (21000000, 0, '65a261f21077447fa28db33268f5ae82', '租户管理', '0', 'tenantManager', null, '', null, null, 'N', 'N', 'N', 'N', '0', 'M', '0', '0', '0', '0', '0', 1, null, '', 'xy_organization', 3, '目录:租户管理', '0', 'Y', 2, 0),
           (21010000, 21000000, '0109cdb290144529a5153737dc5e93d6', '租户管理', '0,21000000', 'tenant', null, 'tenant/tenant/tenant/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, 'tenant:tenant:list', 'xy_organization', 1, '菜单:租户管理', '0', 'Y', 2, 0),
               (21010100, 21010000, 'a535eb84b07049f685005bb1c83a0981', '租户详情', '0,21000000,21010000', 'tenantDetail/:id', null, 'tenant/tenant/tenant/TenantDetail', null, null, 'N', 'Y', 'N', 'N', '0', 'X', '0', '1', '0', '0', '0', 5, null, 'tenant:tenant:single', null, 2, '详情:租户详情', '0', 'Y', 2, 0),
               (21010200, 21010000, '942dfd9e742c4392beae3e5618769d5d', '租户新增', '0,21000000,21010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'tenant:tenant:add', null, 3, '按钮:租户新增', '0', 'Y', 2, 0),
               (21010300, 21010000, '9c081b0a1fea467fb2349145fa6b49fd', '租户修改', '0,21000000,21010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'tenant:tenant:edit', null, 4, '按钮:租户修改', '0', 'Y', 2, 0),
               (21010400, 21010000, '2ee5caad992d4a73b402931c038e21cf', '租户修改状态', '0,21000000,21010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'tenant:tenant:editStatus', null, 5, '按钮:租户修改状态', '0', 'Y', 2, 0),
               (21010500, 21010000, 'dfd3f200a5c145c88b90c78bb91312a3', '租户权限分配', '0,21000000,21010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'tenant:tenant:auth', null, 5, '按钮:租户权限分配', '0', 'Y', 2, 0),
               (21010600, 21010000, '9398fdb1f5e34d52ba0df19a4ac66a5c', '租户删除', '0,21000000,21010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'tenant:tenant:delete', null, 6, '按钮:租户删除', '0', 'Y', 2, 0),
               (21010700, 21010000, '77eab2f22c0944e1a3e544787e21c874', '租户导入', '0,21000000,21010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'tenant:tenant:import', null, 7, '按钮:租户导入', '0', 'Y', 2, 0),
               (21010800, 21010000, '72dd64db42fb42d8ac4851d28a4d4946', '租户导出', '0,21000000,21010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'tenant:tenant:export', null, 8, '按钮:租户导出', '0', 'Y', 2, 0),
           (21020000, 21000000, '6b1f5a23273f4be2a7aeaaec16d5a972', '源策略管理', '0,21000000', 'strategy', null, 'tenant/tenant/strategy/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, 'tenant:strategy:list', 'xy_organization', 1, '菜单:数据源策略管理', '0', 'Y', 2, 0),
               (21020100, 21020000, 'bb6ecc8a720a4793bd93c6d060abd4d0', '源策略详情', '0,21000000,21020000', 'strategyDetail/:id', null, 'tenant/tenant/strategy/StrategyDetail', null, null, 'N', 'Y', 'N', 'N', '0', 'X', '0', '1', '0', '0', '0', 5, null, 'tenant:strategy:single', null, 2, '详情:数据源策略详情', '0', 'Y', 2, 0),
               (21020200, 21020000, 'eb999cfc3b924c5d9973cfd3d24ee221', '源策略新增', '0,21000000,21020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'tenant:strategy:add', null, 3, '按钮:数据源策略新增', '0', 'Y', 2, 0),
               (21020300, 21020000, '7063e7b32edb49769dbf4a66c69470c6', '源策略修改', '0,21000000,21020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'tenant:strategy:edit', null, 4, '按钮:数据源策略修改', '0', 'Y', 2, 0),
               (21020400, 21020000, '303eb6f1190e4f288f6019a5197f0de6', '源策略修改状态', '0,21000000,21020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'tenant:strategy:editStatus', null, 5, '按钮:数据源策略修改状态', '0', 'Y', 2, 0),
               (21020500, 21020000, '4866eca79112480c8791ab3b01acc15a', '源策略删除', '0,21000000,21020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'tenant:strategy:delete', null, 6, '按钮:数据源策略删除', '0', 'Y', 2, 0),
               (21020600, 21020000, '7fc8f11b95cd497ab8b93f1d93107124', '源策略导入', '0,21000000,21020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'tenant:strategy:import', null, 7, '按钮:数据源策略导入', '0', 'Y', 2, 0),
               (21020700, 21020000, '6fe711d2c5b648149f52e5e8a1be809a', '源策略导出', '0,21000000,21020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'tenant:strategy:export', null, 8, '按钮:数据源策略导出', '0', 'Y', 2, 0),
           (21030000, 21000000, 'eebe7073cbe54614baec1507c90795a5', '数据源管理', '0,21000000', 'source', null, 'tenant/source/source/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, 'tenant:source:list', 'xy_organization', 1, '菜单:数据源管理', '0', 'Y', 2, 0),
               (21030100, 21030000, '55b4eab094a540a6b0ad4584108566ef', '数据源详情', '0,21000000,21030000', 'sourceDetail/:id', null, 'tenant/source/source/SourceDetail', null, null, 'N', 'Y', 'N', 'N', '0', 'X', '0', '1', '0', '0', '0', 5, null, 'tenant:source:single', null, 2, '详情:数据源详情', '0', 'Y', 2, 0),
               (21030200, 21030000, '2f8484ccb4944f5780535c5a6835fe65', '数据源新增', '0,21000000,21030000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'tenant:source:add', null, 3, '按钮:数据源新增', '0', 'Y', 2, 0),
               (21030300, 21030000, '558308f42a1e4a269fa9fa33b294d730', '数据源修改', '0,21000000,21030000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'tenant:source:edit', null, 4, '按钮:数据源修改', '0', 'Y', 2, 0),
               (21030400, 21030000, '7a4d22dd4a1a4a51818408d516ca6b04', '数据源修改状态', '0,21000000,21030000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'tenant:source:editStatus', null, 5, '按钮:数据源修改状态', '0', 'Y', 2, 0),
               (21030500, 21030000, '6d55a92a567c423cb6efc7dcba120821', '数据源删除', '0,21000000,21030000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'tenant:source:delete', null, 6, '按钮:数据源删除', '0', 'Y', 2, 0),
               (21030600, 21030000, 'c06e8f136c704ca686527d9f1ad6b983', '数据源导入', '0,21000000,21030000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'tenant:source:import', null, 7, '按钮:数据源导入', '0', 'Y', 2, 0),
               (21030700, 21030000, '0ce47238e4794f709985a0f8505756a7', '数据源导出', '0,21000000,21030000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'tenant:source:export', null, 8, '按钮:数据源导出', '0', 'Y', 2, 0),
       (22000000, 0, 'fe8bad86fdf34ed7ab584887e1e0f786', '系统管理', '0', 'system', null, '', null, null, 'N', 'N', 'N', 'N', '0', 'M', '0', '0', '0', '0', '0', 1, null, '', 'ant-design:key-outlined', 4, '目录:系统管理', '0', 'Y', 2, 0),
           (22010000, 22000000, 'b08569b9c6044608913cae26f427f842', '字典管理', '0,22000000', 'dictType', null, 'tenant/dict/dictType/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, 'dict:dict:list', 'xy_organization', 1, '菜单:字典管理', '0', 'Y', 2, 0),
               (22010100, 22010000, 'bf7ca66a708f4ee88d5e58df65b558fb', '字典详情', '0,22000000,22010000', 'dictTypeDetail/:id', null, 'tenant/dict/dictType/DictTypeDetail', null, null, 'N', 'Y', 'N', 'N', '0', 'X', '0', '1', '0', '0', '0', 5, null, 'dict:dict:single', null, 2, '详情:字典详情', '0', 'Y', 2, 0),
               (22010200, 22010000, '71b7dcc4d4a74f5fb78b1d4457c6829e', '字典新增', '0,22000000,22010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'dict:dict:add', null, 3, '按钮:字典新增', '0', 'Y', 2, 0),
               (22010300, 22010000, '72f4012cdc744c048367c50de2bf603b', '字典数据', '0,22000000,22010000', 'dictDataDetail/:code', null, 'tenant/dict/dictData/index', null, null, 'N', 'Y', 'N', 'N', '0', 'X', '0', '1', '0', '0', '0', 1, null, 'dict:dict:dict', null, 4, '按钮:字典数据', '0', 'Y', 2, 0),
               (22010400, 22010000, '2df41ed5aed44909ab4ea3f66ea5f4d4', '字典修改', '0,22000000,22010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'dict:dict:edit', null, 4, '按钮:字典修改', '0', 'Y', 2, 0),
               (22010500, 22010000, 'bfa0a7b8d5be4189acf8933b4dd9bf94', '字典修改状态', '0,22000000,22010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'dict:dict:editStatus', null, 5, '按钮:字典修改状态', '0', 'Y', 2, 0),
               (22010600, 22010000, '8808bad6108546439e560ead38715aba', '字典删除', '0,22000000,22010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'dict:dict:delete', null, 6, '按钮:字典删除', '0', 'Y', 2, 0),
               (22010700, 22010000, 'ab20e0bfafc441fc90409e1096d3eebd', '字典导入', '0,22000000,22010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'dict:dict:import', null, 7, '按钮:字典导入', '0', 'Y', 2, 0),
               (22010800, 22010000, 'e50e7bbf131f413ebf0c4ec09cfa3b70', '字典导出', '0,22000000,22010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'dict:dict:export', null, 8, '按钮:字典导出', '0', 'Y', 2, 0),
           (22020000, 22000000, '71c9c40055b44052a27c5b3775c42054', '参数管理', '0,22000000', 'config', null, 'tenant/dict/config/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, 'dict:config:list', 'xy_organization', 1, '菜单:参数管理', '0', 'Y', 2, 0),
               (22020100, 22020000, '9fdfef2871974adcb8dcd546af0f4aa2', '参数详情', '0,22000000,22020000', 'configDetail/:id', null, 'tenant/dict/config/ConfigDetail', null, null, 'N', 'Y', 'N', 'N', '0', 'X', '0', '1', '0', '0', '0', 5, null, 'dict:config:single', null, 2, '详情:参数详情', '0', 'Y', 2, 0),
               (22020200, 22020000, '504ce298004f4a7fa93794ea974500cf', '参数新增', '0,22000000,22020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'dict:config:add', null, 3, '按钮:参数新增', '0', 'Y', 2, 0),
               (22020300, 22020000, 'aa46bd42a43b4921afa392107b062749', '参数修改', '0,22000000,22020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'dict:config:edit', null, 4, '按钮:参数修改', '0', 'Y', 2, 0),
               (22020400, 22020000, '25c63be4b66e4b05b40c7393ac773fd3', '参数删除', '0,22000000,22020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'dict:config:delete', null, 5, '按钮:参数删除', '0', 'Y', 2, 0),
               (22020500, 22020000, '0f86293fc6904e26bc22410611faaad5', '参数强制删除', '0,22000000,22020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'dict:config:delForce', null, 6, '按钮:参数强制删除', '0', 'Y', 2, 0),
               (22020600, 22020000, 'b03f82fb6e1148329c03816a1a892b32', '参数导入', '0,22000000,22020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'dict:config:import', null, 7, '按钮:参数导入', '0', 'Y', 2, 0),
               (22020700, 22020000, '72db9651b40e4bd6bc6ff5e3f6dcc1d1', '参数导出', '0,22000000,22020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'dict:config:export', null, 8, '按钮:参数导出', '0', 'Y', 2, 0);
# (10000000, 0, 'SysOrganize', '组织管理', '0', 'organize', null, null, null, null, 'N', 'N', 'N', 'N', '0', 'M', '0', '0', '0', '0', '0', 1, null, null, null, 'xy_organization', 1, '目录:组织管理', '0', 'Y', 0, 0),
# (10010000, 10000000, 'SysDept', '部门管理', '0,10000000', 'dept', null, 'system/organize/dept/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, null, 'system:dept:list', 'xy_organization', 1, '菜单:部门管理', '0', 'Y', 0, 0),
# (10020000, 10000000, 'SysPost', '岗位管理', '0,10000000', 'post', null, 'system/organize/post/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, null, 'system:post:list', 'xy_organization', 2, '菜单:岗位管理', '0', 'Y', 0, 0),
# (10030000, 10000000, 'SysUser', '用户管理', '0,10000000', 'user', null, 'system/organize/user/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, null, 'system:user:list', 'xy_organization', 3, '菜单:用户管理', '0', 'Y', 0, 0),
# -- ----------------------------
# -- 初始化-菜单信息表数据
# -- ----------------------------
# insert into sys_menu (id, module_id, tenant_id, parent_id, name, path, component, query, is_common, is_default, is_frame, is_cache, menu_type, visible, perms, icon, sort, create_by, remark)
# values
# -- 系统Id 0 租户Id 0(公用)
#        -- 一级菜单
#        (10100, 0, 0, 0, '组织管理', 'organize',   null, '', 'Y', 'N', 'N', 'N', 'M', 'Y', '', 'xy_organization', 1, 0, '组织管理目录'),
#               -- 二级菜单
#               (10110, 0, 0, 10100, '部门管理', 'dept',   'system/organize/dept/index',     '', 'Y', 'N', 'N', 'N', 'C', 'Y',  'system:dept:list',      'xy_dept',   1, 0, '部门管理菜单'),
#                                    -- 部门管理按钮
#                                    (10111, 0, 0, 10110, '部门查询', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:dept:query',           '#', 1, 0, ''),
#                                    (10112, 0, 0, 10110, '部门新增', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:dept:add',             '#', 2, 0, ''),
#                                    (10113, 0, 0, 10110, '部门修改', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:dept:edit',            '#', 3, 0, ''),
#                                    (10114, 0, 0, 10110, '部门删除', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:dept:remove',          '#', 4, 0, ''),
#               -- 二级菜单
#               (10120, 0, 0, 10100, '岗位管理', 'post',   'system/organize/post/index',     '', 'Y', 'N', 'N', 'N', 'C', 'Y',  'system:post:list',      'xy_post',   2, 0, '岗位管理菜单'),
#                                    -- 岗位管理按钮
#                                    (10121, 0, 0, 10120, '岗位查询', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:post:query',           '#', 1, 0, ''),
#                                    (10122, 0, 0, 10120, '岗位新增', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:post:add',             '#', 2, 0, ''),
#                                    (10123, 0, 0, 10120, '岗位修改', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:post:edit',            '#', 3, 0, ''),
#                                    (10124, 0, 0, 10120, '岗位删除', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:post:remove',          '#', 4, 0, ''),
#                                    (10125, 0, 0, 10120, '岗位导出', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:post:export',          '#', 5, 0, ''),
#               -- 二级菜单
#               (10130, 0, 0, 10100, '用户管理', 'user',   'system/organize/user/index',     '', 'Y', 'N', 'N', 'N', 'C', 'Y',  'system:user:list',      'xy_user',   3, 0, '用户管理菜单'),
#                                    -- 用户管理按钮
#                                    (10131, 0, 0, 10130, '用户查询', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:user:query',           '#', 1, 0, ''),
#                                    (10132, 0, 0, 10130, '用户新增', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:user:add',             '#', 2, 0, ''),
#                                    (10133, 0, 0, 10130, '用户修改', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:user:edit',            '#', 3, 0, ''),
#                                    (10134, 0, 0, 10130, '用户删除', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:user:remove',          '#', 4, 0, ''),
#                                    (10135, 0, 0, 10130, '用户导出', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:user:export',          '#', 5, 0, ''),
#                                    (10136, 0, 0, 10130, '用户导入', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:user:import',          '#', 6, 0, ''),
#                                    (10137, 0, 0, 10130, '重置密码', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:user:resetPwd',        '#', 7, 0, ''),
#        -- 一级菜单
#        (10200, 0, 0, 0, '企业账户', 'enterprise', null, '', 'Y', 'N', 'N', 'N', 'M', 'Y', '', 'xy_enterprise',   2, 0, '企业账户目录'),
#               (10210, 0, 0, 10200, '资料管理', 'dict',   'system/dataSetting/enterprise/profile/index',        '', 'Y', 'N', 'N', 'N', 'C', 'Y',  'system:enterprise:list',      'xy_dict',   1, 0, '资料管理菜单'),
#                                    -- 字典管理按钮
#                                    (10211, 0, 0, 10210, '资料查看权限', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:enterprise:list',      '#', 1, 0, ''),
#                                    (10212, 0, 0, 10210, '普通操作权限', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:enterprise:edit',      '#', 2, 0, ''),
#                                    (10213, 0, 0, 10210, '超管操作权限', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:enterpriseAdmin:edit', '#', 3, 0, ''),
#        -- 一级菜单
#        (10300, 0, 0, 0, '系统管理', 'system',     null, '', 'Y', 'N', 'N', 'N', 'M', 'Y', '', 'xy_setting',      3, 0, '系统管理目录'),
#               -- 二级菜单
#               (10310, 0, 0, 10300, '通知公告', 'notice', 'system/system/notice/index',     '', 'Y', 'N', 'N', 'N', 'C', 'Y',  'system:notice:list',    'xy_message', 3, 0, '通知公告菜单'),
#                                    -- 通知公告按钮
#                                    (10311, 0, 0, 10310, '公告查询', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:notice:query',        '#', 1, 0, ''),
#                                    (10312, 0, 0, 10310, '公告新增', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:notice:add',          '#', 2, 0, ''),
#                                    (10313, 0, 0, 10310, '公告修改', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:notice:edit',         '#', 3, 0, ''),
#                                    (10314, 0, 0, 10310, '公告删除', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:notice:remove',       '#', 4, 0, ''),
#               -- 二级菜单
#               (10320, 0, 0, 10300, '日志管理', 'log',    '',                                '', 'Y', 'N', 'N', 'N', 'M', 'Y',  '',                      'xy_log',    4, 0, '日志管理菜单'),
#                      -- 三级菜单
#                      (10321, 0, 0, 10320, '操作日志', 'operlog',   'system/system/journal/operlog/index',    '', 'Y', 'N', 'N', 'N', 'C', 'Y', 'system:operlog:list',   'xy_log_operation',  1, 0, '操作日志菜单'),
#                                    -- 操作日志按钮
#                                    (10322, 0, 0, 10321, '操作查询', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:operlog:query',       '#', 1, 0, ''),
#                                    (10323, 0, 0, 10321, '操作删除', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:operlog:remove',      '#', 2, 0, ''),
#                                    (10324, 0, 0, 10321, '日志导出', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:operlog:export',      '#', 3, 0, ''),
#                      -- 三级菜单
#                      (10325, 0, 0, 10320, '登录日志', 'loginInfo', 'system/system/journal/loginInfo/index',  '', 'Y', 'N', 'N', 'N', 'C', 'Y', 'system:loginInfo:list', 'xy_log_loginInfo',  2, 0, '登录日志菜单'),
#                                    -- 登录日志按钮
#                                    (10326, 0, 0, 10325, '登录查询', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:loginInfo:query',     '#', 1, 0, ''),
#                                    (10327, 0, 0, 10325, '登录删除', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:loginInfo:remove',    '#', 2, 0, ''),
#                                    (10328, 0, 0, 10325, '日志导出', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:loginInfo:export',    '#', 3, 0, ''),
#        -- 一级菜单
#        (10400, 0, 0, 0, '权限管理', 'authority',  null, '', 'Y', 'N', 'N', 'N', 'M', 'Y', '', 'xy_authority',    3, 0, '权限管理目录'),
#               -- 二级菜单
#               (10410, 0, 0, 10400, '角色管理', 'role',   'system/authority/role/index',     '', 'Y', 'N', 'N', 'N', 'C', 'Y', 'system:role:list',      'xy_role',    1, 0, '角色管理菜单'),
#                                    -- 角色管理按钮
#                                    (10411, 0, 0, 10410, '角色查询', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:role:query',           '#', 1, 0, ''),
#                                    (10412, 0, 0, 10410, '角色授权', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:role:set',             '#', 2, 0, ''),
#                                    (10413, 0, 0, 10410, '角色新增', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:role:add',             '#', 3, 0, ''),
#                                    (10414, 0, 0, 10410, '角色修改', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:role:edit',            '#', 4, 0, ''),
#                                    (10415, 0, 0, 10410, '角色删除', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:role:remove',          '#', 5, 0, ''),
#                                    (10416, 0, 0, 10410, '角色导出', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:role:export',          '#', 6, 0, ''),
#               -- 二级菜单
#               (10420, 0, 0, 10400, '菜单管理', 'menu',   'system/authority/menu/index',     '', 'Y', 'N', 'N', 'N', 'C', 'Y', 'system:menu:list',      'xy_menu',    2, 0, '菜单管理菜单'),
#                                    -- 菜单管理按钮
#                                    (10421, 0, 0, 10420, '菜单查询', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:menu:query',           '#', 1, 0, ''),
#                                    (10422, 0, 0, 10420, '菜单新增', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:menu:add',             '#', 2, 0, ''),
#                                    (10423, 0, 0, 10420, '菜单修改', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:menu:edit',            '#', 3, 0, ''),
#                                    (10424, 0, 0, 10420, '菜单删除', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:menu:remove',          '#', 4, 0, ''),
#               -- 二级菜单
#               (10430, 0, 0, 10400, '模块管理', 'system', 'system/authority/system/index',   '', 'Y', 'N', 'N', 'N', 'C', 'Y', 'system:system:list',    'xy_system',  3, 0, '模块管理菜单'),
#                                    -- 模块管理按钮
#                                    (10431, 0, 0, 10430, '模块查询', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:system:query',    '#', 1, 0, ''),
#                                    (10432, 0, 0, 10430, '模块新增', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:system:add',      '#', 2, 0, ''),
#                                    (10433, 0, 0, 10430, '模块修改', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:system:edit',     '#', 3, 0, ''),
#                                    (10434, 0, 0, 10430, '模块删除', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:system:remove',   '#', 4, 0, ''),
#        -- 一级菜单
#        (10500, 0, 0, 0, '系统监控', 'monitor',    null, '', 'Y', 'N', 'N', 'N', 'M', 'Y', '', 'xy_monitor',         4, 0, '系统监控目录'),
#               -- 二级菜单
#               (10510, 0, 0, 10500, '在线用户', 'online', 'system/monitor/online/index',     '', 'Y', 'N', 'N', 'N', 'C', 'Y', 'monitor:online:list',   'xy_online',  1, 0, '在线用户菜单'),
#                                    -- 在线用户按钮
#                                    (10511, 0, 0, 10510, '在线查询', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'monitor:online:query',       '#', 1, 0, ''),
#                                    (10512, 0, 0, 10510, '批量强退', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'monitor:online:batchLogout', '#', 2, 0, ''),
#                                    (10513, 0, 0, 10510, '单条强退', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'monitor:online:forceLogout', '#', 3, 0, ''),
#
#               -- 二级菜单
#               (10520, 0, 0, 10500, '定时任务', 'job',    'system/monitor/job/index',        '', 'Y', 'N', 'N', 'N', 'C', 'Y', 'monitor:job:list',      'xy_job',     2, 0, '定时任务菜单'),
#                                    -- 定时任务按钮
#                                    (10521, 0, 0, 10520, '任务查询', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'monitor:job:query',          '#', 1, 0, ''),
#                                    (10522, 0, 0, 10520, '任务新增', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'monitor:job:add',            '#', 2, 0, ''),
#                                    (10523, 0, 0, 10520, '任务修改', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'monitor:job:edit',           '#', 3, 0, ''),
#                                    (10524, 0, 0, 10520, '任务删除', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'monitor:job:remove',         '#', 4, 0, ''),
#                                    (10525, 0, 0, 10520, '状态修改', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'monitor:job:changeStatus',   '#', 5, 0, ''),
#                                    (10526, 0, 0, 10520, '任务导出', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'monitor:job:export',         '#', 6, 0, ''),
#        -- 一级菜单
#        (10600, 0, 0, 0, '素材管理', null,       null, '', 'Y', 'N', 'N', 'N', 'M', 'N', '', 'xy_tool',     9, 0, '素材管理目录'),
#                                    -- 素材模块按钮
#                                    (10601, 0, 0, 10600, '素材查询', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:material:query',        '#', 1, 0, ''),
#                                    (10602, 0, 0, 10600, '素材新增', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:material:add',          '#', 2, 0, ''),
#                                    (10603, 0, 0, 10600, '素材编辑', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:material:edit',         '#', 3, 0, ''),
#                                    (10604, 0, 0, 10600, '素材删除', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:material:remove',       '#', 4, 0, ''),
#
# -- 系统Id 2 租户Id -1(超管)
#        -- 一级菜单
#        (20000, 2, -1, 0, '租户管理', 'tenant',      null, '', 'N', 'N', 'N', 'N', 'M', 'Y', '', 'xy_tenant',     6, 0, '租户管理目录'),
#               -- 二级菜单
#               (20010, 2, -1, 20000, '租户管理', 'tenant',    'tenant/tenant/index',  '', 'N', 'N', 'N', 'N', 'C', 'Y', 'tenant:tenant:list',    'xy_tenant',    1, 0, '租户管理菜单'),
#                                    -- 租户管理按钮
#                                    (20011, 2, -1, 20010, '租户查询', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tenant:tenant:query',          '#', 1, 0, ''),
#                                    (20012, 2, -1, 20010, '租户新增', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tenant:tenant:add',            '#', 2, 0, ''),
#                                    (20013, 2, -1, 20010, '租户修改', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tenant:tenant:edit',           '#', 3, 0, ''),
#                                    (20014, 2, -1, 20010, '菜单配置', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tenant:tenant:role',           '#', 4, 0, ''),
#                                    (20015, 2, -1, 20010, '租户删除', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tenant:tenant:remove',         '#', 5, 0, ''),
#                      -- 二级菜单
#               (20050, 2, -1, 20000, '策略管理', 'strategy',   'tenant/strategy/index',     '', 'N', 'N', 'N', 'N', 'C', 'Y',  'tenant:strategy:list',      'xy_strategy',   1, 0, '数据源策略菜单'),
#                                    -- 数据源策略按钮
#                                    (20051, 2, -1, 20050, '策略查询', '', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tenant:strategy:query',           '#', 1, 0, ''),
#                                    (20052, 2, -1, 20050, '策略新增', '', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tenant:strategy:add',             '#', 2, 0, ''),
#                                    (20053, 2, -1, 20050, '策略修改', '', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tenant:strategy:edit',            '#', 3, 0, ''),
#                                    (20054, 2, -1, 20050, '策略删除', '', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tenant:strategy:remove',          '#', 4, 0, ''),
#                                    (20055, 2, -1, 20050, '策略导出', '', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tenant:strategy:export',          '#', 5, 0, ''),
#               -- 二级菜单
#               (20020, 2, -1, 20000, '数据源管理', 'source',   'tenant/source/index',     '', 'N', 'N', 'N', 'N', 'C', 'Y',  'tenant:source:list',      'xy_source',   1, 0, '数据源菜单'),
#                                    -- 数据源管理按钮
#                                    (20021, 2, -1, 20020, '数据源查询', '', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tenant:source:query',           '#', 1, 0, ''),
#                                    (20022, 2, -1, 20020, '数据源新增', '', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tenant:source:add',             '#', 2, 0, ''),
#                                    (20023, 2, -1, 20020, '数据源修改', '', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tenant:source:edit',            '#', 3, 0, ''),
#                                    (20024, 2, -1, 20020, '数据源配置', '', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tenant:separation:edit',        '#', 4, 0, ''),
#                                    (20025, 2, -1, 20020, '数据源删除', '', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tenant:source:remove',          '#', 5, 0, ''),
#        -- 一级菜单
#        (20100, 2, -1, 0, '系统管理', 'system',     null, '', 'N', 'N', 'N', 'N', 'M', 'Y', '', 'xy_setting',      3, 0, '系统管理目录'),
#               (20110, 2, -1, 20100, '字典管理', 'dict',   'system/system/dict/index',        '', 'N', 'N', 'N', 'N', 'C', 'Y',  'system:dict:list',      'xy_dict',   1, 0, '字典管理菜单'),
#                                    -- 字典管理按钮
#                                    (20111, 2, -1, 20110, '字典查询', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'system:dict:query',          '#', 1, 0, ''),
#                                    (20112, 2, -1, 20110, '字典新增', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'system:dict:add',            '#', 2, 0, ''),
#                                    (20113, 2, -1, 20110, '字典修改', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'system:dict:edit',           '#', 3, 0, ''),
#                                    (20114, 2, -1, 20110, '字典删除', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'system:dict:remove',         '#', 4, 0, ''),
#                                    (20115, 2, -1, 20110, '字典导出', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'system:dict:export',         '#', 5, 0, ''),
#               -- 二级菜单
#               (20120, 2, -1, 20100, '参数管理', 'config', 'system/system/config/index',     '', 'N', 'N', 'N', 'N', 'C', 'Y',  'system:config:list',    'xy_config', 2, 0, '参数管理菜单'),
#                                    -- 参数设置按钮
#                                    (20121, 2, -1, 20120, '参数查询', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'system:config:query',        '#', 1, 0, ''),
#                                    (20122, 2, -1, 20120, '参数新增', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'system:config:add',          '#', 2, 0, ''),
#                                    (20123, 2, -1, 20120, '参数修改', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'system:config:edit',         '#', 3, 0, ''),
#                                    (20124, 2, -1, 20120, '参数删除', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'system:config:remove',       '#', 4, 0, ''),
#                                    (20125, 2, -1, 20120, '参数导出', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'system:config:export',       '#', 5, 0, ''),
#        -- 一级菜单
#        (20200, 2, -1, 0, '系统监控', 'monitor',    null, '', 'N', 'N', 'N', 'N', 'M', 'Y', '', 'xy_monitor',         4, 0, '系统监控目录'),
#               -- 二级菜单
#               (20210, 2, -1, 20200, 'Sentinel控制台', 'http://localhost:8718',      '',     '', 'N', 'N', 'Y', 'N', 'C', 'Y', 'monitor:sentinel:list', 'xy_sentinel', 1, 0, '流量控制菜单'),
#               -- 二级菜单
#               (20220, 2, -1, 20200, 'Nacos控制台',    'http://localhost:8848/nacos', '',     '', 'N', 'N', 'Y', 'N', 'C', 'Y', 'monitor:nacos:list',    'xy_nacos',    2, 0, '服务治理菜单'),
#               -- 二级菜单
#               (20230, 2, -1, 20200, 'Admin控制台',    'http://localhost:9100/login', '',     '', 'N', 'N', 'Y', 'N', 'C', 'Y', 'monitor:server:list',   'xy_server',   3, 0, '服务监控菜单'),
#               -- 二级菜单
#               (20240, 2, -1, 20200, 'rabbit控制台',   'http://localhost:15672/#/', '',       '', 'N', 'N', 'Y', 'N', 'C', 'Y', 'monitor:rabbitmq:list',  'xy_rabbit',  4, 0, '消息队列菜单'),
#        -- 一级菜单
#        (20300, 2, -1, 0, '系统工具', 'tool',       null, '', 'N', 'N', 'N', 'N', 'M', 'Y', '', 'xy_tool',     5, 0, '系统工具目录'),
#               -- 二级菜单
#               (20310, 2, -1, 20300, '表单构建', 'build',  'tool/build/index',                '', 'N', 'N', 'N', 'N', 'C', 'Y', 'tool:build:list',       'xy_build',   1, 0, '表单构建菜单'),
#               -- 二级菜单
#               (20320, 2, -1, 20300, '代码生成', 'gen',    'tool/gen/index',                  '', 'N', 'N', 'N', 'N', 'C', 'Y', 'tool:gen:list',         'xy_code',    2, 0, '代码生成菜单'),
#                                    -- 代码生成按钮
#                                    (20321, 2, -1, 20320, '生成查询', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tool:gen:query',             '#', 1, 0, ''),
#                                    (20322, 2, -1, 20320, '生成修改', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tool:gen:edit',              '#', 2, 0, ''),
#                                    (20323, 2, -1, 20320, '生成删除', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tool:gen:remove',            '#', 3, 0, ''),
#                                    (20324, 2, -1, 20320, '导入代码', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tool:gen:import',            '#', 4, 0, ''),
#                                    (20325, 2, -1, 20320, '预览代码', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tool:gen:preview',           '#', 5, 0, ''),
#                                    (20326, 2, -1, 20320, '生成代码', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tool:gen:code',              '#', 6, 0, ''),
#               -- 二级菜单
#               (20330, 2, -1, 20300, '系统接口', 'http://localhost:8080/swagger-ui/index.html', '', '', 'N', 'N', 'Y', 'N', 'C', 'Y', 'tool:swagger:list',     'xy_swagger', 3, 0, '系统接口菜单');

-- ----------------------------
-- 8、字典类型表
-- ----------------------------
drop table if exists sys_dict_type;
create table sys_dict_type (
  id                        bigint              not null auto_increment                 comment '字典主键',
  name                      varchar(100)        default ''                              comment '字典名称',
  code                      varchar(100)        default ''                              comment '字典类型',
  sort                      int unsigned        not null default 0                      comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  remark                    varchar(1000)       default null                            comment '备注',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  primary key (id),
  unique (code)
) engine=innodb auto_increment=100 comment = '字典类型表';

insert into sys_dict_type (name, code, remark)
values ( '用户性别', 'sys_user_sex', '用户性别列表'),
       ( '常规：显隐', 'sys_show_hide', '常规：显隐列表'),
       ( '常规：公共私有', 'sys_common_private', '常规：公共私有列表'),
       ( '系统开关', 'sys_normal_disable', '系统开关列表'),
       ( '任务状态', 'sys_job_status', '任务状态列表'),
       ( '任务分组', 'sys_job_group', '任务分组列表'),
       ( '常规-是否', 'sys_yes_no', '常规：是否列表'),
       ( '通知类型', 'sys_notice_type', '通知：类型列表'),
       ( '通知状态', 'sys_notice_status', '通知：状态列表'),
       ( '日志：操作类型', 'sys_operate_business', '日志：操作类型列表'),
       ( '日志：操作类别', 'sys_operate_type', '日志：操作类别列表'),
       ( '日志：操作状态', 'sys_operate_status', '日志：操作状态列表'),
       ( '状态：成功失败', 'sys_message_status', '状态：成功失败列表'),
       ( '字典：颜色', 'sys_dict_color', '字典：颜色列表'),
       ( '授权类型', 'sys_grant_type', '授权类型列表'),
       ( '权限：页面类型', 'auth_frame_type', '权限：页面类型列表'),
       ( '权限：菜单类型', 'auth_menu_type', '权限：菜单类型列表'),
       ( '权限：数据范围', 'auth_data_scope', '权限：数据范围列表'),
       ( '读写类型', 'sys_source_type', '读写类型列表'),
       ( '数据源类型', 'sys_database_type', '数据源类型列表'),
       ( '配置类型', 'te_configuration_type', '配置类型列表'),
       ( '代码生成：模板类型', 'gen_template_type', '代码生成：模板类型列表'),
       ( '代码生成：属性类型', 'gen_java_type', '代码生成：属性类型列表'),
       ( '代码生成：查询类型', 'gen_query_type', '代码生成：查询类型列表'),
       ( '代码生成：显示类型', 'gen_display_type', '代码生成：显示类型列表'),
       ( '代码生成：生成方式', 'gen_generation_mode', '代码生成：生成方式列表'),
       ( '代码生成：源策略模式', 'gen_source_mode', '代码生成：源策略模式列表');

-- ----------------------------
-- 9、字典数据表
-- ----------------------------
drop table if exists sys_dict_data;
create table sys_dict_data (
  id                        bigint              not null auto_increment                 comment '数据Id',
  code                      varchar(100)        default ''                              comment '字典编码',
  value                     varchar(100)        default ''                              comment '数据键值',
  label                     varchar(100)        default ''                              comment '数据标签',
  sort                      int unsigned        not null default 0                      comment '显示顺序',
  is_default                char(1)             default 'N'                             comment '是否默认（Y是 N否）',
  css_class                 varchar(100)        default null                            comment '样式属性（其他样式扩展）',
  list_class                varchar(100)        default null                            comment '表格回显样式',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  remark                    varchar(1000)       default null                            comment '备注',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  primary key (id)
) engine=innodb auto_increment=1 comment = '字典数据表';

insert into sys_dict_data (sort, label, value, code, css_class, list_class, is_default, remark)
values (1, '男', '0', 'sys_user_sex', '', '', 'Y', '性别男'),
       (2, '女', '1', 'sys_user_sex', '', '', 'N', '性别女'),
       (3, '未知', '2', 'sys_user_sex', '', '', 'N', '性别未知'),
       (0, '公共', '0', 'sys_common_private', '', 'cyan', 'N', '常规-公共私有：公共'),
       (1, '私有', '1', 'sys_common_private', '', 'purple', 'N', '常规-公共私有：私有'),
       (1, '显示', '0', 'sys_show_hide', '', 'blue', 'Y', '常规-显隐：显示'),
       (2, '隐藏', '1', 'sys_show_hide', '', 'red', 'N', '常规-显隐：隐藏'),
       (1, '正常', '0', 'sys_normal_disable', '', 'blue', 'Y', '正常状态'),
       (2, '停用', '1', 'sys_normal_disable', '', 'red', 'N', '停用状态'),
       (1, '正常', '0', 'sys_job_status', '', 'blue', 'Y', '正常状态'),
       (2, '暂停', '1', 'sys_job_status', '', 'red', 'N', '停用状态'),
       (1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '默认分组'),
       (2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '系统分组'),
       (1, '是', 'Y', 'sys_yes_no', '', 'blue', 'Y', '常规-是否：是'),
       (2, '否', 'N', 'sys_yes_no', '', 'red', 'N', '常规-是否：否'),
       (1, '通知', '0', 'sys_notice_type', '', 'blue', 'Y', '通知'),
       (2, '公告', '1', 'sys_notice_type', '', 'green', 'N', '公告'),
       (1, '待发送', '0', 'sys_notice_status', '', 'blue', 'Y', '待发送'),
       (2, '已发送', '1', 'sys_notice_status', '', 'green', 'N', '已发送'),
       (3, '已关闭', '2', 'sys_notice_status', '', 'cyan', 'N', '已关闭'),
       (4, '发送失败', '3', 'sys_notice_status', '', 'orange', 'N', '发送失败'),
       (5, '发送异常', '4', 'sys_notice_status', '', 'red', 'N', '发送异常'),
       (1, '其它', '00', 'sys_operate_business', '', 'orange', 'N', '操作类型：其它'),
       (2, '新增', '01', 'sys_operate_business', '', 'blue', 'N', '操作类型：新增'),
       (3, '强制新增', '02', 'sys_operate_business', '', 'red', 'N', '操作类型：强制新增'),
       (4, '修改', '03', 'sys_operate_business', '', 'green', 'N', '操作类型：修改'),
       (5, '强制修改', '04', 'sys_operate_business', '', 'red', 'N', '操作类型：强制修改'),
       (6, '权限控制', '05', 'sys_operate_business', '', 'purple', 'N', '操作类型：权限控制'),
       (7, '修改状态', '06', 'sys_operate_business', '', 'purple', 'N', '操作类型：修改状态'),
       (8, '强制修改状态', '07', 'sys_operate_business', '', 'red', 'N', '操作类型：强制修改状态'),
       (9, '删除', '08', 'sys_operate_business', '', 'orange', 'N', '操作类型：删除'),
       (10, '强制删除', '09', 'sys_operate_business', '', 'red', 'N', '操作类型：强制删除'),
       (11, '授权', '10', 'sys_operate_business', '', 'orange', 'N', '操作类型：授权'),
       (12, '导出', '11', 'sys_operate_business', '', 'pink', 'N', '操作类型：导出'),
       (13, '导入', '12', 'sys_operate_business', '', 'cyan', 'N', '操作类型：导入'),
       (14, '强退', '13', 'sys_operate_business', '', 'orange', 'N', '操作类型：强退'),
       (15, '生成代码', '14', 'sys_operate_business', '', 'orange', 'N', '操作类型：生成代码'),
       (16, '清空数据', '15', 'sys_operate_business', '', 'orange', 'N', '操作类型：清空数据'),
       (17, '更新缓存', '16', 'sys_operate_business', '', 'orange', 'N', '操作类型：更新缓存'),
       (1, '其它', '00', 'sys_operate_type', '', 'green', 'N', '操作类别：其它'),
       (2, '后台用户', '01', 'sys_operate_type', '', 'blue', 'N', '操作类别：后台用户'),
       (3, '手机端用户', '02', 'sys_operate_type', '', 'red', 'N', '操作类别：手机端用户'),
       (1, '正常', '0', 'sys_operate_status', '', 'green', 'N', '操作状态：正常'),
       (2, '异常', '1', 'sys_operate_status', '', 'red', 'N', '操作状态：异常'),
       (1, '成功', '0', 'sys_message_status', '', 'blue', 'N', '状态：成功'),
       (2, '失败', '1', 'sys_message_status', '', 'red', 'N', '状态：失败'),
       (1, 'none', '', 'sys_dict_color', '', '', 'N', '字典颜色：none'),
       (2, 'red', 'red', 'sys_dict_color', '', 'red', 'N', '字典颜色：red'),
       (3, 'pink', 'pink', 'sys_dict_color', '', 'pink', 'N', '字典颜色：pink'),
       (4, 'orange', 'orange', 'sys_dict_color', '', 'orange', 'N', '字典颜色：orange'),
       (5, 'green', 'green', 'sys_dict_color', '', 'green', 'N', '字典颜色：green'),
       (6, 'blue', 'blue', 'sys_dict_color', '', 'blue', 'N', '字典颜色：blue'),
       (7, 'purple', 'purple', 'sys_dict_color', '', 'purple', 'N', '字典颜色：purple'),
       (8, 'cyan', 'cyan', 'sys_dict_color', '', 'cyan', 'N', '字典颜色：cyan'),
       (1, '授权码模式', 'authorization_code', 'sys_grant_type', '', '', 'N', '授权码模式'),
       (2, '密码模式', 'password', 'sys_grant_type', '', '', 'N', '密码模式'),
       (3, '客户端模式', 'client_credentials', 'sys_grant_type', '', '', 'N', '客户端模式'),
       (4, '简化模式', 'implicit', 'sys_grant_type', '', '', 'N', '简化模式'),
       (5, '刷新模式', 'refresh_token', 'sys_grant_type', '', '', 'N', '刷新模式'),
       (1, '常规', '0', 'auth_frame_type', '', 'pink', 'Y', '权限-页面类型：常规'),
       (2, '内嵌', '1', 'auth_frame_type', '', 'cyan', 'N', '权限-页面类型：内嵌'),
       (3, '外链', '2', 'auth_frame_type', '', 'green', 'N', '权限-页面类型：外链'),
       (1, '目录', 'M', 'auth_menu_type', '', 'pink', 'Y', '权限-菜单类型：目录'),
       (2, '菜单', 'C', 'auth_menu_type', '', 'cyan', 'N', '权限-菜单类型：菜单'),
       (3, '详情', 'X', 'auth_menu_type', '', 'purple', 'N', '权限-菜单类型：详情'),
       (4, '按钮', 'F', 'auth_menu_type', '', 'green', 'N', '权限-菜单类型：按钮'),
       (0, '全部数据权限', '1', 'auth_data_scope', '', '', 'Y', '权限-数据范围：全部数据权限'),
       (1, '自定数据权限', '2', 'auth_data_scope', '', '', 'N', '权限-数据范围：自定数据权限'),
       (2, '本部门数据权限', '3', 'auth_data_scope', '', '', 'N', '权限-数据范围：本部门数据权限'),
       (3, '本部门及以下数据权限', '4', 'auth_data_scope', '', '', 'N', '权限-数据范围：本部门及以下数据权限'),
       (4, '本岗位数据权限', '5', 'auth_data_scope', '', '', 'N', '权限-数据范围：本岗位数据权限'),
       (5, '仅本人数据权限', '6', 'auth_data_scope', '', '', 'N', '权限-数据范围：仅本人数据权限'),
       (1, '读&写', '0', 'sys_source_type', '', 'blue', 'Y', '读&写'),
       (2, '只读', '1', 'sys_source_type', '', 'green', 'N', '只读'),
       (3, '只写', '2', 'sys_source_type', '', 'red', 'N', '只写'),
       (1, '子数据源', '0', 'sys_database_type', '', 'green', 'Y', '子数据源'),
       (2, '主数据源', '1', 'sys_database_type', '', 'red', 'N', '主数据源'),
       (1, '自动配置', '0', 'te_configuration_type', '', '', 'N', '配置类型：自动配置'),
       (2, '手动配置', '1', 'te_configuration_type', '', '', 'N', '配置类型：手动配置'),
       (1, '单表', 'base', 'gen_template_type', '', '', 'Y', '代码生成-模板类型：单表'),
       (2, '树表', 'tree', 'gen_template_type', '', '', 'N', '代码生成-模板类型：树表'),
       (3, '主子表', 'subBase', 'gen_template_type', '', '', 'N', '代码生成-模板类型：主子表'),
       (4, '主子树表', 'subTree', 'gen_template_type', '', '', 'N', '代码生成-模板类型：主子树表'),
       (5, '关联表', 'merge', 'gen_template_type', '', '', 'N', '代码生成-模板类型：关联表'),
       (0, 'Long', 'Long', 'gen_java_type', '', '', 'N', '代码生成-属性类型：Long'),
       (1, 'String', 'String', 'gen_java_type', '', '', 'N', '代码生成-属性类型：String'),
       (2, 'Integer', 'Integer', 'gen_java_type', '', '', 'N', '代码生成-属性类型：Integer'),
       (3, 'Double', 'Double', 'gen_java_type', '', '', 'N', '代码生成-属性类型：Double'),
       (4, 'BigDecimal', 'BigDecimal', 'gen_java_type', '', '', 'N', '代码生成-属性类型：BigDecimal'),
       (5, 'Date', 'Date', 'gen_java_type', '', '', 'N', '代码生成-属性类型：Date'),
       (0, '=', 'eq', 'gen_query_type', '', '', 'Y', '代码生成-查询类型：='),
       (1, '!=', 'ne', 'gen_query_type', '', '', 'N', '代码生成-查询类型：!='),
       (2, '>', 'gt', 'gen_query_type', '', '', 'N', '代码生成-查询类型：>'),
       (3, '>=', 'ge', 'gen_query_type', '', '', 'N', '代码生成-查询类型：>='),
       (4, '<', 'lt', 'gen_query_type', '', '', 'N', '代码生成-查询类型：<'),
       (5, '<=', 'le', 'gen_query_type', '', '', 'N', '代码生成-查询类型：<='),
       (6, 'LIKE', 'like', 'gen_query_type', '', '', 'N', '代码生成-查询类型：LIKE'),
       (7, 'NOT_LIKE', 'notLike', 'gen_query_type', '', '', 'N', '代码生成-查询类型：NOT_LIKE'),
       (8, 'LIKE_LEFT', 'likeLeft', 'gen_query_type', '', '', 'N', '代码生成-查询类型：LIKE_LEFT'),
       (9, 'LIKE_RIGHT', 'likeRight', 'gen_query_type', '', '', 'N', '代码生成-查询类型：LIKE_RIGHT'),
       (10, 'BETWEEN', 'between', 'gen_query_type', '', '', 'N', '代码生成-查询类型：BETWEEN'),
       (11, 'NOT_BETWEEN', 'notBetween', 'gen_query_type', '', '', 'N', '代码生成-查询类型：NOT_BETWEEN'),
       (12, 'ISNULL', 'isNull', 'gen_query_type', '', '', 'N', '代码生成-查询类型：ISNULL'),
       (13, 'IS_NOT_NULL', 'isNotNull', 'gen_query_type', '', '', 'N', '代码生成-查询类型：IS_NOT_NULL'),
       (0, '文本框', 'Input', 'gen_display_type', '', '', 'Y', '代码生成-显示类型：文本框'),
       (1, '密码框', 'InputPassword', 'gen_display_type', '', '', 'Y', '代码生成-显示类型：密码框'),
       (2, '数字输入框', 'InputNumber', 'gen_display_type', '', '', 'Y', '代码生成-显示类型：数字输入框'),
       (3, '文本域', 'InputTextArea', 'gen_display_type', '', '', 'N', '代码生成-显示类型：文本域'),
       (4, '下拉框', 'Select', 'gen_display_type', '', '', 'N', '代码生成-显示类型：下拉框'),
       (5, '单选框', 'CheckboxGroup', 'gen_display_type', '', '', 'N', '代码生成-显示类型：单选框'),
       (6, '按钮风格单选框', 'RadioButtonGroup', 'gen_display_type', '', '', 'N', '代码生成-显示类型：按钮风格单选框'),
       (7, '复选框', 'RadioGroup', 'gen_display_type', '', '', 'N', '代码生成-显示类型：复选框'),
       (8, '日期控件', 'DatePicker', 'gen_display_type', '', '', 'N', '代码生成-显示类型：日期控件'),
       (9, '时间控件', 'TimePicker', 'gen_display_type', '', '', 'N', '代码生成-显示类型：时间控件'),
       (10, '图片上传', 'imageUpload', 'gen_display_type', '', '', 'N', '代码生成-显示类型：图片上传'),
       (11, '文件上传', 'Upload', 'gen_display_type', '', '', 'N', '代码生成-显示类型：文件上传'),
       (12, '富文本控件', 'tinymce', 'gen_display_type', '', '', 'N', '代码生成-显示类型：富文本控件'),
       (13, 'MarkDown编辑器控件', 'markdown', 'gen_display_type', '', '', 'N', '代码生成-显示类型：MarkDown编辑器控件'),
       (0, 'zip压缩包', '0', 'gen_generation_mode', '', '', 'Y', '代码生成-生成方式：zip压缩包'),
       (1, '自定义路径', '1', 'gen_generation_mode', '', '', 'N', '代码生成-生成方式：自定义路径'),
       (0, '策略源', 'ISOLATE', 'gen_source_mode', '', '', 'Y', '代码生成-源策略模式：策略源'),
       (1, '主数据源', 'MASTER', 'gen_source_mode', '', '', 'N', '代码生成-源策略模式：主数据源');

-- ----------------------------
-- 10、参数配置表
-- ----------------------------
drop table if exists sys_config;
create table sys_config (
  id                        bigint              not null                                comment '参数主键',
  name                      varchar(100)        default ''                              comment '参数名称',
  code                      varchar(100)        default ''                              comment '参数编码',
  value                     varchar(500)        default ''                              comment '参数键值',
  type                      char(1)             default 'N'                             comment '系统内置（Y是 N否）',
  sort                      int unsigned        not null default 0                      comment '显示顺序',
  remark                    varchar(1000)       default null                            comment '备注',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  primary key (id)
) engine=innodb comment = '参数配置表';

insert into sys_config (id, name, code, value, type, remark)
values (1, '主框架页-默认皮肤样式名称',           'sys.index.skinName',               'skin-blue',       'Y',    '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow'),
       (2, '用户管理-账号初始密码',              'sys.user.initPassword',            '123456',          'Y',    '初始化密码 123456'),
       (3, '主框架页-侧边栏主题',                'sys.index.sideTheme',              'theme-dark',      'Y',    '深色主题theme-dark，浅色主题theme-light'),
       (4, '账号自助-是否开启租户注册功能',        'sys.account.registerTenant',       'false',           'Y',    '是否开启注册租户功能（true开启，false关闭）');

-- ----------------------------
-- 11、定时任务调度表
-- ----------------------------
drop table if exists sys_job;
create table sys_job (
  job_id                    bigint              not null                                comment '任务Id',
  job_name                  varchar(64)         default ''                              comment '任务名称',
  job_group                 varchar(64)         default 'DEFAULT'                       comment '任务组名',
  invoke_target             varchar(500)        not null                                comment '调用目标字符串',
  cron_expression           varchar(255)        default ''                              comment 'cron执行表达式',
  misfire_policy            varchar(20)         default '3'                             comment '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  concurrent                char(1)             default '1'                             comment '是否并发执行（0允许 1禁止）',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1暂停）',
  remark                    varchar(1000)       default null                            comment '备注',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
  tenant_id		            bigint	            not null                                comment '租户Id',
  primary key (job_id, job_name, job_group)
) engine=innodb auto_increment=100 comment = '定时任务调度表';

insert into sys_job (job_id, job_name, job_group, invoke_target, cron_expression, misfire_policy, concurrent, status, tenant_id)
values (1, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams',        '0/10 * * * * ?', '3', '1', '1', 0),
       (2, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')',  '0/15 * * * * ?', '3', '1', '1', 0),
       (3, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)',  '0/20 * * * * ?', '3', '1', '1', 0);

-- ----------------------------
-- 12、定时任务调度日志表
-- ----------------------------
drop table if exists sys_job_log;
create table sys_job_log (
  job_log_id                bigint              not null auto_increment                 comment '任务日志Id',
  job_name                  varchar(64)         not null                                comment '任务名称',
  job_group                 varchar(64)         not null                                comment '任务组名',
  invoke_target             varchar(500)        not null                                comment '调用目标字符串',
  job_message               varchar(500)                                                comment '日志信息',
  status                    char(1)             not null default '0'                    comment '执行状态（0正常 1失败）',
  exception_info            varchar(2000)       default ''                              comment '异常信息',
  create_time               datetime            default current_timestamp               comment '创建时间',
  del_time                  datetime            on update current_timestamp             comment '删除时间',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
  tenant_id		            bigint	            not null                                comment '租户Id',
  primary key (job_log_id)
) engine=innodb comment = '定时任务调度日志表';

-- ----------------------------
-- 13、代码生成业务表
-- ----------------------------
drop table if exists gen_table;
create table gen_table (
  id                        bigint              not null auto_increment                 comment '表Id',
  name                      varchar(200)        default ''                              comment '表名称',
  comment                   varchar(500)        default ''                              comment '表描述',
  class_name                varchar(100)        default ''                              comment '实体类名称',
  prefix                    varchar(100)        default ''                              comment '前缀名称',
  tpl_category              varchar(200)        not null default 'base'                 comment '使用的模板（base单表 tree树表 subBase主子单表 subTree主子树表 merge关联表）',
  package_name              varchar(100)                                                comment '生成包路径',
  authority_name            varchar(30)                                                 comment '生成权限名',
  module_name               varchar(30)                                                 comment '生成模块名',
  business_name             varchar(30)                                                 comment '生成业务名',
  function_name             varchar(50)                                                 comment '生成功能名',
  function_author           varchar(50)                                                 comment '生成功能作者',
  gen_type                  char(1)             not null default '0'                    comment '生成代码方式（0zip压缩包 1自定义路径）',
  gen_path                  varchar(200)        default '/'                             comment '后端生成路径（不填默认项目路径）',
  ui_path                   varchar(200)        default '/'                             comment '前端生成路径（不填默认项目路径）',
  options                   varchar(1000)                                               comment '其它生成选项',
  remark                    varchar(500)        default null                            comment '备注',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  primary key (id)
) engine=innodb auto_increment=1 comment = '代码生成业务表';

-- ----------------------------
-- 14、代码生成业务表字段
-- ----------------------------
drop table if exists gen_table_column;
create table gen_table_column (
  id                        bigint              not null auto_increment                 comment '字段Id',
  table_id                  varchar(64)         not null                                comment '归属表Id',
  name                      varchar(200)                                                comment '列名称',
  comment                   varchar(500)                                                comment '列描述',
  type                      varchar(100)                                                comment '列类型',
  java_type                 varchar(500)                                                comment 'JAVA类型',
  java_field                varchar(200)                                                comment 'JAVA字段名',
  is_pk                     tinyint(1)          not null default 0                      comment '主键字段（1是 0否）',
  is_increment              tinyint(1)          not null default 0                      comment '自增字段（1是 0否）',
  is_required               tinyint(1)          not null default 0                      comment '必填字段（1是 0否）',
  is_view                   tinyint(1)          not null default 0                      comment '查看字段（1是 0否）',
  is_insert                 tinyint(1)          not null default 0                      comment '新增字段（1是 0否）',
  is_edit                   tinyint(1)          not null default 0                      comment '编辑字段（1是 0否）',
  is_list                   tinyint(1)          not null default 0                      comment '列表字段（1是 0否）',
  is_query                  tinyint(1)          not null default 0                      comment '查询字段（1是 0否）',
  is_unique                 tinyint(1)          not null default 0                      comment '唯一字段（1是 0否）',
  is_import                 tinyint(1)          not null default 0                      comment '导入字段（1是 0否）',
  is_export                 tinyint(1)          not null default 0                      comment '导出字段（1是 0否）',
  is_hide                   tinyint(1)          not null default 0                      comment '隐藏字段（1是 0否）',
  is_cover                  tinyint(1)          not null default 0                      comment '覆盖字段（1是 0否）',
  query_type                varchar(200)        default 'EQ'                            comment '查询方式（等于、不等于、大于、小于、范围）',
  html_type                 varchar(200)                                                comment '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  dict_type                 varchar(200)        default ''                              comment '字典类型',
  sort                      int unsigned        not null default 0                      comment '显示顺序',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  primary key (id)
) engine=innodb auto_increment=1 comment = '代码生成业务表字段';