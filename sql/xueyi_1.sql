# DROP DATABASE IF EXISTS `xy-cloud`;
#
# CREATE DATABASE  `xy-cloud` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
#
# SET NAMES utf8mb4;
# SET FOREIGN_KEY_CHECKS = 0;
#
# USE `xy-cloud`;

-- ----------------------------
-- 1、租户信息表
-- ----------------------------
drop table if exists te_tenant;
create table te_tenant (
  id		                bigint	            not null                                comment '租户Id',
  strategy_id		        bigint	            not null                                comment '策略Id',
  name		                varchar(50)	        not null	                            comment '租户账号',
  system_name		        varchar(50)	        not null 	                            comment '系统名称',
  nick		                varchar(50)	        not null 	                            comment '租户名称',
  domain_name               varchar(100)        default null                            comment '租户域名',
  logo		                varchar(200)	    default ''	                            comment '租户logo',
  name_frequency            tinyint             default 0                               comment '账号修改次数',
  is_lessor                 char(1)             not null default 'N'	                comment '超管租户（Y是 N否）',
  sort                      int unsigned        default 0                               comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  remark                    varchar(200)        default null                            comment '备注',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  is_default                char(1)             not null default 'N'	                comment '默认租户（Y是 N否）',
  del_flag		            tinyint             not null default 0                      comment '删除标志（0正常 1删除）',
  primary key (id)
) engine = innodb comment = '租户信息表';

-- ----------------------------
-- 初始化-租户信息表数据
-- ----------------------------
insert into te_tenant (id, is_lessor, is_default, strategy_id,  name, system_name, nick, logo)
values (1, 'Y', 'Y', 1, 'administrator', '租管租户', 'xueYi1', 'https://images.gitee.com/uploads/images/2021/1101/141601_d68e92a4_7382127.jpeg'),
       (2, 'N', 'N', 1, 'xueYi', '雪忆科技', 'xueYi1', 'https://images.gitee.com/uploads/images/2021/1101/141601_d68e92a4_7382127.jpeg');

-- ----------------------------
-- 2、策略信息表
-- ----------------------------
drop table if exists te_strategy;
create table te_strategy (
  id		                bigint	            not null                                comment '策略Id',
  name                      varchar(100)	    not null default ''	                    comment '策略名称',
  source_id                 bigint	            not null	                            comment '数据源Id',
  source_slave              varchar(200)	    not null	                            comment '数据源编码',
  sort                      int unsigned        default 0                               comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  remark                    varchar(200)        default null                            comment '备注',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  is_default                char(1)             not null default 'N'	                comment '默认策略（Y是 N否）',
  del_flag		            tinyint             not null default 0                      comment '删除标志（0正常 1删除）',
  primary key (id)
) engine = innodb comment = '数据源策略表';

-- ----------------------------
-- 初始化-策略信息表数据
-- ----------------------------
insert into te_strategy(id, name, is_default, source_id, source_slave, sort)
values (1, '默认注册策略', 'Y', 1, 'slave', 1);

-- ----------------------------
-- 3、数据源信息表|管理系统数据源信息 | 主库有且只能有一个，用途：主要用于存储公共数据，具体看后续文档或视频
-- ----------------------------
drop table if exists te_source;
create table te_source (
  id		                bigint	            not null                                comment '数据源Id',
  name		                varchar(50)	        not null                                comment '数据源名称',
  slave		                varchar(200)	    not null	                            comment '数据源编码',
  driver_class_name		    varchar(200)	    not null	                            comment '驱动',
  url_prepend	            varchar(200)	    not null	                            comment '连接地址',
  url_append	            varchar(200)	    not null default ''	                    comment '连接参数',
  user_name	                varchar(100) 	    not null	                            comment '用户名',
  password	                varchar(100) 	    not null default ''	                    comment '密码',
  sort                      int unsigned        default 0                               comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  remark                    varchar(200)        default null                            comment '备注',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  is_default                char(1)             not null default 'N'	                comment '默认数据源（Y是 N否）',
  del_flag		            tinyint             not null default 0                      comment '删除标志（0正常 1删除）',
  primary key (id)
) engine = innodb comment = '数据源信息表';

-- ----------------------------
-- 初始化-数据源信息表数据 | 这条数据为我的基础库，实际使用时调整成自己的库即可
-- ----------------------------
insert into te_source(id, name, is_default, slave, driver_class_name, url_prepend, url_append, user_name, password)
values (1, '注册数据源', 'Y', 'slave', 'com.mysql.cj.jdbc.Driver', 'jdbc:mysql://localhost:3306/xy-cloud1', '?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&allowMultiQueries=true&serverTimezone=GMT%2B8', 'root', 'password');

-- ----------------------------
-- 4、模块信息表
-- ----------------------------
drop table if exists sys_module;
create table sys_module (
  id		                bigint	            not null                                comment '模块Id',
  name		                varchar(50)	        not null	                            comment '模块名称',
  logo                      varchar(200)	    default null 	        	            comment '模块logo',
  path                      varchar(200)        default null                            comment '路由地址',
  param_path                varchar(255)        default null                            comment '路由参数',
  type		                char(1)	            not null default '0'	                comment '模块类型（0常规 1内嵌 2外链）',
  hide_module		        char(1)	            not null default '0'	                comment '模块显隐状态（0显示 1隐藏）',
  sort                      int unsigned        default 0                               comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  remark                    varchar(200)        default null                            comment '备注',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  is_common                 char(1)             not null default '1'	                comment '公共模块（0公共 1私有）',
  is_default                char(1)             not null default 'N'	                comment '默认模块（Y是 N否）',
  del_flag		            tinyint             not null default 0                      comment '删除标志（0正常 1删除）',
  tenant_id		            bigint	            not null                                comment '租户Id',
  primary key (id)
) engine = innodb comment = '模块信息表';

# ----------------------------
# 初始化-模块信息表数据
# ----------------------------
insert into sys_module (id, name, type, path, param_path, is_default, hide_module, logo, remark, is_common, tenant_id)
values (1, '基础平台' ,    '0', '', '', 'Y', '0', 'https://images.gitee.com/uploads/images/2021/1101/141155_f3dfce1d_7382127.jpeg', '基础平台', '0', 0),
       (2, '开发者平台' , '0', '', '', 'Y', '0', 'https://images.gitee.com/uploads/images/2021/1101/141601_d68e92a4_7382127.jpeg', '开发者平台', '0', 0);

-- ----------------------------
-- 5、菜单权限表
-- ----------------------------
drop table if exists sys_menu;
create table sys_menu (
  id                        bigint              not null                                comment '菜单Id',
  parent_id                 bigint              not null default 0                      comment '父菜单Id',
  name                      varchar(100)        not null                                comment '菜单名称',
  title                     varchar(50)         not null                                comment '菜单标题 | 多语言',
  level                     int                 not null                                comment '树层级',
  ancestors                 varchar(500)        default ''                              comment '祖级列表',
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
  hide_tab                  char(1)             not null default  '0'                   comment '标签显隐状态（0显示 1隐藏）',
  hide_menu                 char(1)             not null default  '0'                   comment '菜单显隐状态（0显示 1隐藏）',
  hide_breadcrumb           char(1)             not null default  '0'                   comment '面包屑路由显隐状态（0显示 1隐藏）',
  hide_children             char(1)             not null default  '0'                   comment '子菜单显隐状态（0显示 1隐藏）',
  hide_path_for_children    char(1)             not null default  '0'                   comment '是否在子级菜单的完整path中忽略本级path（0显示 1隐藏）',
  dynamic_level             int                 not null default 1                      comment '详情页可打开Tab页数',
  real_path                 varchar(255)        default null                            comment '详情页的实际Path',
  perms                     varchar(255)        default null                            comment '权限标识',
  icon                      varchar(100)        default null                            comment '菜单图标',
  sort                      int unsigned        not null default  0                     comment '显示顺序',
  status                    char(1)             not null default  '0'                   comment '状态（0正常 1停用）',
  remark                    varchar(200)        default null                            comment '备注',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  is_common                 char(1)             not null default  '1'                   comment '公共菜单（0公共 1私有）',
  is_default                char(1)             not null default  'N'                   comment '默认菜单（Y是 N否）',
  del_flag                  tinyint             not null default  0                     comment '删除标志(0正常 1删除)',
  module_id                 bigint              not null                                comment '模块Id',
  tenant_id                 bigint              not null                                comment '租户Id',
  primary key (id),
  unique (name)
) engine = innodb comment = '菜单权限表';

insert into sys_menu (id, parent_id, name, title, level, ancestors, path, frame_src, component, param_path, transition_name, ignore_route, is_cache, is_affix, is_disabled, frame_type, menu_type, hide_tab, hide_menu, hide_breadcrumb, hide_children, hide_path_for_children, dynamic_level, real_path, perms, icon, sort, remark, is_common, is_default, module_id, tenant_id)
values (13000000, 0, '4be0456e05a7422d9f1c82fb7bf19377', '组织管理', 1, '0', 'organize', null, '', null, null, 'N', 'N', 'N', 'N', '0', 'M', '0', '0', '0', '0', '0', 1, null, '', 'ant-design:apartment-outlined', 1, '目录:组织管理', '0', 'Y', 1, 0),
           (13010000, 13000000, '0a612ebdfaa64ea4b8fd8cfd787042ea', '部门管理', 2, '0,13000000', 'dept', null, 'system/organize/dept/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, 'FE:system:organize:dept:list, RD:system:organize:dept:list', 'ant-design:partition-outlined', 1, '菜单:系统服务 | 组织模块 | 部门管理 | 部门管理', '0', 'Y', 1, 0),
               (13010100, 13010000, 'c67c9520af2c48d89e1bae0d2a0342d1', '部门详情', 3, '0,13000000,13010000', 'deptDetail/:id', null, 'system/organize/dept/DeptDetail', null, null, 'N', 'Y', 'N', 'N', '0', 'X', '0', '1', '0', '0', '0', 5, null, 'FE:system:organize:dept:single, RD:system:organize:dept:single', null, 1, '详情:系统服务 | 组织模块 | 部门管理 | 部门详情', '0', 'Y', 1, 0),
               (13010200, 13010000, '5e50660b195a49c4860607aec14fc330', '部门新增', 3, '0,13000000,13010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:organize:dept:add, RD:system:organize:dept:add', null, 2, '按钮:系统服务 | 组织模块 | 部门管理 | 部门新增', '0', 'Y', 1, 0),
               (13010300, 13010000, 'ad0a108f2c35433daf1864cf4cf93b64', '部门修改', 3, '0,13000000,13010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:organize:dept:edit, RD:system:organize:dept:edit, RD:system:organize:dept:single', null, 3, '按钮:系统服务 | 组织模块 | 部门管理 | 部门修改', '0', 'Y', 1, 0),
               (13010400, 13010000, 'df7b77ef91c8433f97a9848ebe9b714b', '部门状态修改', 3, '0,13000000,13010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:organize:dept:es, RD:system:organize:dept:es', null, 3, '按钮:系统服务 | 组织模块 | 部门管理 | 部门状态修改', '0', 'Y', 1, 0),
               (13010500, 13010000, 'a4c9cb1f239744b6bc7e6c5f11ba3268', '部门权限分配', 3, '0,13000000,13010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:organize:dept:auth, RD:system:organize:dept:auth, RD:system:authority:role:list', null, 5, '按钮:系统服务 | 组织模块 | 部门管理 | 部门权限分配', '0', 'Y', 1, 0),
               (13010600, 13010000, '83a7e4bafb154a938b2c67d7f3e540d5', '部门删除', 3, '0,13000000,13010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:organize:dept:del, RD:system:organize:dept:del', null, 6, '按钮:系统服务 | 组织模块 | 部门管理 | 部门删除', '0', 'Y', 1, 0),
           (13020000, 13000000, 'ecdcabe9c00a408796626a83d0129fdb', '岗位管理', 2, '0,13000000', 'post', null, 'system/organize/post/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, 'FE:system:organize:post:list, RD:system:organize:post:list, RD:system:organize:dept:list', 'ant-design:idcard-outlined', 2, '菜单:系统服务 | 组织模块 | 岗位管理 | 岗位管理', '0', 'Y', 1, 0),
               (13020100, 13020000, '270eab9abe6a4e58b8c06d1339b46009', '岗位详情', 3, '0,13000000,13020000', 'postDetail/:id', null, 'system/organize/post/PostDetail', null, null, 'N', 'Y', 'N', 'N', '0', 'X', '0', '1', '0', '0', '0', 5, null, 'FE:system:organize:post:single, RD:system:organize:post:single', null, 1, '详情:系统服务 | 组织模块 | 岗位管理 | 岗位详情', '0', 'Y', 1, 0),
               (13020200, 13020000, '5ee95c2e5af8481ca6346faa82974251', '岗位新增', 3, '0,13000000,13020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:organize:post:add, RD:system:organize:post:add, RD:system:organize:dept:list', null, 3, '按钮:系统服务 | 组织模块 | 岗位管理 | 岗位新增', '0', 'Y', 1, 0),
               (13020300, 13020000, '082b9043d201490badbb403e2de9d0e1', '岗位修改', 3, '0,13000000,13020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:organize:post:edit, RD:system:organize:post:edit, RD:system:organize:post:single, RD:system:organize:dept:list', null, 4, '按钮:系统服务 | 组织模块 | 岗位管理 | 岗位修改', '0', 'Y', 1, 0),
               (13020400, 13020000, '6a9ffc830afd4f3d8f74a047d0619e0f', '岗位状态修改', 3, '0,13000000,13020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:organize:post:es, RD:system:organize:post:es', null, 5, '按钮:系统服务 | 组织模块 | 岗位管理 | 岗位状态修改', '0', 'Y', 1, 0),
               (13020500, 13020000, '1af7167de8ba4f819149956fe5f166e9', '岗位权限分配', 3, '0,13000000,13020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:organize:post:auth, RD:system:organize:post:auth, RD:system:authority:role:list', null, 5, '按钮:系统服务 | 组织模块 | 岗位管理 | 岗位权限分配', '0', 'Y', 1, 0),
               (13020600, 13020000, 'baf3aa4d771a4ac5b80c0192ae52964b', '岗位删除', 3, '0,13000000,13020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:organize:post:del, RD:system:organize:post:del', null, 6, '按钮:系统服务 | 组织模块 | 岗位管理 | 岗位删除', '0', 'Y', 1, 0),
           (13030000, 13000000, 'f450bb13df9f42618ecbe62c46346d3e', '用户管理', 2, '0,13000000', 'user', null, 'system/organize/user/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, 'FE:system:organize:user:list, RD:system:organize:user:list', 'ant-design:user-outlined', 3, '菜单:系统服务 | 组织模块 | 用户管理 | 用户管理', '0', 'Y', 1, 0),
               (13030100, 13030000, '3584b9c409ab49b79b2888936de01a2d', '用户详情', 3, '0,13000000,13030000', 'userDetail/:id', null, 'system/organize/user/UserDetail', null, null, 'N', 'Y', 'N', 'N', '0', 'X', '0', '1', '0', '0', '0', 5, null, 'FE:system:organize:user:single, RD:system:organize:user:single', null, 1, '详情:系统服务 | 组织模块 | 用户管理 | 用户详情', '0', 'Y', 1, 0),
               (13030200, 13030000, '679d5eccf32d43639898d6d5a347d643', '用户新增', 3, '0,13000000,13030000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:organize:user:add, RD:system:organize:user:add', null, 3, '按钮:系统服务 | 组织模块 | 用户管理 | 用户新增', '0', 'Y', 1, 0),
               (13030300, 13030000, 'bc20a8b4c70f42ada4666269cc6a7624', '用户修改', 3, '0,13000000,13030000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:organize:user:edit, RD:system:organize:user:edit, RD:system:organize:user:single', null, 4, '按钮:系统服务 | 组织模块 | 用户管理 | 用户修改', '0', 'Y', 1, 0),
               (13030400, 13030000, '5fb9a4cbaa1f4059aff2f22ff02a78f0', '用户状态修改', 3, '0,13000000,13030000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:organize:user:es, RD:system:organize:user:es', null, 5, '按钮:系统服务 | 组织模块 | 用户管理 | 用户状态修改', '0', 'Y', 1, 0),
               (13030500, 13030000, 'b0d4e749c5eb49bdb3634fc5d4e57ecc', '用户权限分配', 3, '0,13000000,13030000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:organize:user:auth, RD:system:organize:user:auth, RD:system:authority:role:list', null, 5, '按钮:系统服务 | 组织模块 | 用户管理 | 用户权限分配', '0', 'Y', 1, 0),
               (13030600, 13030000, '6669f111d1974ec28b82e830ad2195c2', '用户删除', 3, '0,13000000,13030000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:organize:user:del, RD:system:organize:user:del', null, 6, '按钮:系统服务 | 组织模块 | 用户管理 | 用户删除', '0', 'Y', 1, 0),
               (13030700, 13030000, '2527782114544c53acdff12dc8e09a34', '用户导入', 3, '0,13000000,13030000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:organize:user:import, RD:system:organize:user:import', null, 7, '按钮:系统服务 | 组织模块 | 用户管理 | 用户导入', '0', 'Y', 1, 0),
               (13030800, 13030000, 'abc1c9ec3a73477184384bbfc37cf8a6', '用户导出', 3, '0,13000000,13030000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:organize:user:export, RD:system:organize:user:export', null, 8, '按钮:系统服务 | 组织模块 | 用户管理 | 用户导出', '0', 'Y', 1, 0),
               (13030900, 13030000, 'a79774cd4205485c9e5ea9c36be41573', '用户密码重置', 3, '0,13000000,13030000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:organize:user:rp, RD:system:organize:user:rp', null, 5, '按钮:系统服务 | 组织模块 | 用户管理 | 用户密码修改', '0', 'Y', 1, 0),
       (14000000, 0, '61ac2678fa5a4cd7977c118114ff1828', '权限管理', 1, '0', 'authority', null, '', null, null, 'N', 'N', 'N', 'N', '0', 'M', '0', '0', '0', '0', '0', 1, null, '', 'ant-design:safety-certificate-outlined', 2, '目录:权限管理', '0', 'Y', 1, 0),
           (14010000, 14000000, '88c136711d98441699a6013ef27a356a', '角色管理', 2, '0,14000000', 'role', null, 'system/authority/role/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, 'FE:system:authority:role:list, RD:system:authority:role:list', 'ant-design:team-outlined', 3, '菜单:系统服务 | 权限模块 | 角色管理 | 角色管理', '0', 'Y', 1, 0),
               (14010100, 14010000, '458c8e2ae43e47059b978504dd363a94', '角色详情', 3, '0,14000000,14010000', 'roleDetail/:id', null, 'system/authority/role/RoleDetail', null, null, 'N', 'Y', 'N', 'N', '0', 'X', '0', '1', '0', '0', '0', 5, null, 'FE:system:authority:role:single, RD:system:authority:role:single', null, 1, '详情:系统服务 | 权限模块 | 角色管理 | 角色详情', '0', 'Y', 1, 0),
               (14010200, 14010000, '949497ca68724eb18f43b550c4c4bf5d', '角色新增', 3, '0,14000000,14010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:authority:role:add, RD:system:authority:role:add', null, 2, '按钮:系统服务 | 权限模块 | 角色管理 | 角色新增', '0', 'Y', 1, 0),
               (14010300, 14010000, '77604dc9a47b4cc296e9f41cb76f81f5', '角色修改', 3, '0,14000000,14010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:authority:role:edit, RD:system:authority:role:edit, RD:system:authority:role:single', null, 3, '按钮:系统服务 | 权限模块 | 角色管理 | 角色修改', '0', 'Y', 1, 0),
               (14010400, 14010000, 'ed3d7dfa06424f1abe271058aa3abbd9', '角色状态修改', 3, '0,14000000,14010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:authority:role:es, RD:system:authority:role:es', null, 4, '按钮:系统服务 | 权限模块 | 角色管理 | 角色状态修改', '0', 'Y', 1, 0),
               (14010500, 14010000, '8bd13d9c71624e60a288d424a6a96670', '角色权限分配', 3, '0,14000000,14010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:authority:role:auth, RD:system:authority:role:auth', null, 5, '按钮:系统服务 | 权限模块 | 角色管理 | 角色权限分配', '0', 'Y', 1, 0),
               (14010600, 14010000, '54dc750a6076494ebc40e62b5d775db7', '角色删除', 3, '0,14000000,14010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:authority:role:del, RD:system:authority:role:del', null, 6, '按钮:系统服务 | 权限模块 | 角色管理 | 角色删除', '0', 'Y', 1, 0),
       (15000000, 0, 'de6e93785ba944e1bde5538e92bd7b91', '公告管理', 1, '0', 'notice', null, '', null, null, 'N', 'N', 'N', 'N', '0', 'M', '0', '0', '0', '0', '0', 1, null, '', 'ant-design:schedule-outlined', 3, '目录:公告管理', '0', 'Y', 1, 0),
           (15010000, 15000000, 'b7214522d55c4f1ebd381859dc6774bb', '通知公告', 2, '0,15000000', 'notice', null, 'system/notice/notice/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, 'FE:system:notice:notice:list, RD:system:notice:notice:list', 'ant-design:notification-outlined', 1, '菜单:系统服务 | 消息模块 | 通知公告管理 | 通知公告管理', '0', 'Y', 1, 0),
               (15010100, 15010000, '3b77bf3ae1264e5a960262cd898a9279', '通知公告详情', 3, '0,15000000,15010000', 'noticeDetail/:id', null, 'system/notice/notice/NoticeDetail', null, null, 'N', 'Y', 'N', 'N', '0', 'X', '0', '1', '0', '0', '0', 5, null, 'FE:system:notice:notice:single, RD:system:notice:notice:single', null, 1, '详情:系统服务 | 消息模块 | 通知公告管理 | 通知公告详情', '0', 'Y', 1, 0),
               (15010200, 15010000, '9d21b1e6c2134ac9943db2d1ffa4d61f', '通知公告新增', 3, '0,15000000,15010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:notice:notice:add, RD:system:notice:notice:add', null, 2, '按钮:系统服务 | 消息模块 | 通知公告管理 | 通知公告新增', '0', 'Y', 1, 0),
               (15010300, 15010000, '04b84309c6544e20bf40df26349b4212', '通知公告修改', 3, '0,15000000,15010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:notice:notice:edit, RD:system:notice:notice:edit', null, 3, '按钮:系统服务 | 消息模块 | 通知公告管理 | 通知公告修改', '0', 'Y', 1, 0),
               (15010400, 15010000, '010e949049864492b046883b8571063d', '通知公告删除', 3, '0,15000000,15010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:notice:notice:del, RD:system:notice:notice:del', null, 4, '按钮:系统服务 | 消息模块 | 通知公告管理 | 通知公告删除', '0', 'Y', 1, 0),
       (16000000, 0, 'fa8965418a3540a99a62c805a4616e59', '系统管理', 1, '0', 'system', null, '', null, null, 'N', 'N', 'N', 'N', '0', 'M', '0', '0', '0', '0', '0', 1, null, '', 'ant-design:control-outlined', 4, '目录:系统管理', '0', 'Y', 1, 0),
           (16010000, 16000000, '60a02b15ddcf45eab582a0c57af6ae62', '定时任务', 2, '0,16000000', 'job', null, 'system/system/job/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, 'FE:job:schedule:job:list, RD:job:schedule:job:list', 'ant-design:field-time-outlined', 1, '菜单:定时任务 | 调度任务管理 | 定时任务管理', '0', 'Y', 1, 0),
               (16010100, 16010000, '101aeea275ce4c61b0652491f75125c0', '任务详情', 3, '0,16000000,16010000', 'jobDetail/:id', null, 'system/system/job/JobDetail', null, null, 'N', 'Y', 'N', 'N', '0', 'X', '0', '1', '0', '0', '0', 5, null, 'FE:job:schedule:job:single, RD:job:schedule:job:single', null, 1, '详情:定时任务 | 调度任务管理 | 定时任务详情', '0', 'Y', 1, 0),
               (16010200, 16010000, 'c48c23456ed144719fe6d0e35d9e8ddf', '调度日志', 3, '0,16000000,16010000', 'jobLogDetail/:id', null, 'system/system/jobLog/index', null, null, 'N', 'Y', 'N', 'N', '0', 'X', '0', '1', '0', '0', '0', 5, null, 'FE:job:schedule:job:log, RD:job:schedule:job:log, RD:job:schedule:job:list', null, 2, '详情:定时任务 | 调度任务管理 | 调度日志', '0', 'Y', 1, 0),
               (16010300, 16010000, '099a0d943a604d0198516e5f6766ac04', '任务新增', 3, '0,16000000,16010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:job:schedule:job:add, RD:job:schedule:job:add', null, 3, '按钮:定时任务 | 调度任务管理 | 定时任务新增', '0', 'Y', 1, 0),
               (16010400, 16010000, '0acf6076acca4ff689f3df52161099e7', '任务修改', 3, '0,16000000,16010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:job:schedule:job:edit, RD:job:schedule:job:edit, RD:job:schedule:job:single', null, 4, '按钮:定时任务 | 调度任务管理 | 定时任务修改', '0', 'Y', 1, 0),
               (16010500, 16010000, '83398a4cd3ce4aa1a26a00b0b8a7612b', '任务状态修改', 3, '0,16000000,16010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:job:schedule:job:es, RD:job:schedule:job:es', null, 5, '按钮:定时任务 | 调度任务管理 | 定时任务状态修改', '0', 'Y', 1, 0),
               (16010600, 16010000, '96bdff8d31be422b89cafb64d22ffd2c', '任务删除', 3, '0,16000000,16010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:job:schedule:job:del, RD:job:schedule:job:del', null, 6, '按钮:定时任务 | 调度任务管理 | 定时任务删除', '0', 'Y', 1, 0),
       (17000000, 0, 'e24afc855afc485d961b865d5d999811', '系统监控', 1, '0', 'monitor', null, '', null, null, 'N', 'N', 'N', 'N', '0', 'M', '0', '0', '0', '0', '0', 1, null, '', 'ant-design:eye-outlined', 5, '目录:系统监控', '0', 'Y', 1, 0),
           (17010000, 17000000, 'f4a053ab18c84a82be6b2f04d3adaaac', '在线用户', 2, '0,17000000', 'online', null, 'system/monitor/online/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, 'FE:system:monitor:online:list, RD:system:monitor:online:list', 'ant-design:contacts-outlined', 1, '菜单:系统服务 | 监控模块 | 在线用户管理 | 在线用户管理', '0', 'Y', 1, 0),
               (17010100, 17010000, '6829ff5c29894306b2d4fa0d6769e6e6', '在线用户强退', 3, '0,17000000,17010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:monitor:online:forceLogout, RD:system:monitor:online:forceLogout', null, 1, '按钮:-系统服务 | 监控模块 | 在线用户管理 | 在线用户强退', '0', 'Y', 1, 0),
           (17020000, 17000000, '693be0ee3df14643ba93acf1b0e2df97', '日志管理', 2, '0,17000000', 'log', null, '', null, null, 'N', 'N', 'N', 'N', '0', 'M', '0', '0', '0', '0', '0', 1, null, '', 'ant-design:file-text-outlined', 2, '目录:日志管理', '0', 'Y', 1, 0),
               (17020100, 17020000, '89d2de0ab59248a59f41d097a41517b5', '登录日志', 3, '0,17000000,17020000', 'loginLog', null, 'system/monitor/loginLog/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, 'FE:system:monitor:loginLog:list, RD:system:monitor:loginLog:list', 'ant-design:contacts-outlined', 3, '菜单:系统服务 | 监控模块 | 访问日志管理 | 登录日志', '0', 'Y', 1, 0),
                   (17020101, 17020100, '10afaa52d5be4a9ca9de5da3567d3d47', '登录日志删除', 4, '0,17000000,17020000,17020100', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:monitor:loginLog:del, RD:system:monitor:loginLog:del', null, 1, '按钮:系统服务 | 监控模块 | 访问日志管理 | 登录日志删除', '0', 'Y', 1, 0),
                   (17020102, 17020100, 'f29ae2f1ec794de992109776f46743cc', '登录日志导出', 4, '0,17000000,17020000,17020100', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:monitor:loginLog:export, RD:system:monitor:loginLog:export', null, 2, '按钮:系统服务 | 监控模块 | 访问日志管理 | 登录日志导出', '0', 'Y', 1, 0),
               (17020200, 17020000, '7a3b5c396f8647499a9c9cd97712324a', '操作日志', 3, '0,17000000,17020000', 'operateLog', null, 'system/monitor/operateLog/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, 'FE:system:monitor:operateLog:list, RD:system:monitor:operateLog:list', 'ant-design:contacts-twotone', 4, '菜单:系统服务 | 监控模块 | 操作日志管理 | 操作日志管理', '0', 'Y', 1, 0),
                   (17020201, 17020000, '8b4266f509f748baac4f86739d7bb8c9', '操作日志详情', 4, '0,17000000,17020000,17020200', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:monitor:operateLog:add, RD:system:monitor:operateLog:add', null, 1, '按钮:系统服务 | 监控模块 | 操作日志管理 | 操作日志详情', '0', 'Y', 1, 0),
                   (17020202, 17020000, '8340af3b5dcc44e2837ef92e7de4da9d', '操作日志删除', 4, '0,17000000,17020000,17020200', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:monitor:operateLog:del, RD:system:monitor:operateLog:del', null, 2, '按钮:系统服务 | 监控模块 | 操作日志管理 | 操作日志删除', '0', 'Y', 1, 0),
                   (17020203, 17020000, '05598058a8794251b454025d863c6daa', '操作日志导出', 4, '0,17000000,17020000,17020200', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:monitor:operateLog:export, RD:system:monitor:operateLog:export', null, 3, '按钮:系统服务 | 监控模块 | 操作日志管理 | 操作日志导出', '0', 'Y', 1, 0),
       (25000000, 0, 'fbaad9604c74427592126a9a0e756067', '系统工具', 1, '0', 'generate', null, '', null, null, 'N', 'N', 'N', 'N', '0', 'M', '0', '0', '0', '0', '0', 1, null, '', 'ant-design:setting-outlined', 1, '目录:系统工具', '0', 'Y', 2, 0),
           (25010000, 25000000, 'ebd46c2fd3c3429896de95a82bcf1d8b', '代码生成', 2, '0,25000000', 'gen', null, 'gen/generate/gen/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, 'FE:gen:generate:gen:list, RD:gen:generate:gen:list', 'ant-design:experiment-outlined', 1, '菜单:代码生成 | 代码生成管理 | 代码生成', '0', 'Y', 2, 0),
               (25010100, 25010000, 'b489b7b0e645471eb42ed8b1f0365d32', '代码生成配置', 3, '0,25000000,25010000', 'generateDetail/:id', null, 'gen/generate/gen/GenDetail', null, null, 'N', 'Y', 'N', 'N', '0', 'X', '0', '1', '0', '0', '0', 5, null, 'FE:gen:generate:gen:edit, RD:gen:generate:gen:edit, RD:gen:generate:gen:single, RD:system:authority:module:list, RD:system:authority:menu:list', null, 1, '详情:代码生成 | 代码生成管理 | 代码生成配置', '0', 'Y', 2, 0),
               (25010200, 25010000, 'c49d2b1d7d6640e7bd331aa494b05e58', '代码生成预览', 3, '0,25000000,25010000', 'codeDetail/:id', null, 'gen/generate/gen/CodeDetail', null, null, 'N', 'Y', 'N', 'N', '0', 'X', '0', '1', '0', '0', '0', 5, null, 'FE:gen:generate:gen:preview, RD:gen:generate:gen:preview, RD:gen:generate:gen:single', null, 2, '详情:代码生成 | 代码生成管理 | 代码生成预览', '0', 'Y', 2, 0),
               (25010300, 25010000, 'f264257d3d4948bd9204ee5ba39b1661', '代码生成导入', 3, '0,25000000,25010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:gen:generate:gen:import, RD:gen:generate:gen:import, RD:tenant:tenant:source:list, RD:gen:generate:gen:list', null, 3, '按钮:代码生成 | 代码生成管理 | 代码生成导入', '0', 'Y', 2, 0),
               (25010400, 25010000, '828aa317e06b48a2adc9d783ee968d99', '代码生成下载', 3, '0,25000000,25010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:gen:generate:gen:code, RD:gen:generate:gen:code', null, 4, '按钮:代码生成 | 代码生成管理 | 代码生成下载', '0', 'Y', 2, 0),
               (25010500, 25010000, '29db55acc054471f8b68411f99be1dee', '代码生成删除', 3, '0,25000000,25010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:gen:generate:gen:del, RD:gen:generate:gen:del', null, 5, '按钮:代码生成 | 代码生成管理 | 代码生成删除', '0', 'Y', 2, 0),
           (25020000, 25000000, '8fa8a4e494594496a865ab028483d28b', '系统接口', 2, '0,25000000', 'swagger', 'http://localhost:8080/webjars/swagger-ui/index.html', null, null, null, 'N', 'N', 'N', 'N', '1', 'C', '0', '0', '0', '0', '0', 1, null, 'monitor:swagger:list', 'ant-design:api-twotone', 2, '菜单:系统接口', '0', 'Y', 2, 0),
       (21000000, 0, '65a261f21077447fa28db33268f5ae82', '租户管理', 1, '0', 'tenant', null, '', null, null, 'N', 'N', 'N', 'N', '0', 'M', '0', '0', '0', '0', '0', 1, null, '', 'ant-design:bank-outlined', 2, '目录:租户管理', '0', 'Y', 2, 0),
           (21010000, 21000000, '0109cdb290144529a5153737dc5e93d6', '租户管理', 2, '0,21000000', 'tenant', null, 'tenant/tenant/tenant/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, 'FE:tenant:tenant:tenant:list, RD:tenant:tenant:tenant:list, RD:tenant:tenant:strategy:list', 'ant-design:shop-twotone', 1, '菜单:租户服务 | 租户模块 | 租户管理 | 租户管理', '0', 'Y', 2, 0),
               (21010100, 21010000, 'a535eb84b07049f685005bb1c83a0981', '租户详情', 3, '0,21000000,21010000', 'tenantDetail/:id', null, 'tenant/tenant/tenant/TenantDetail', null, null, 'N', 'Y', 'N', 'N', '0', 'X', '0', '1', '0', '0', '0', 5, null, 'FE:tenant:tenant:tenant:single, RD:tenant:tenant:tenant:single', null, 1, '详情:租户服务 | 租户模块 | 租户管理 | 租户详情', '0', 'Y', 2, 0),
               (21010200, 21010000, '942dfd9e742c4392beae3e5618769d5d', '租户新增', 3, '0,21000000,21010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:tenant:tenant:tenant:add, RD:tenant:tenant:tenant:add, RD:tenant:tenant:strategy:list', null, 2, '按钮:租户服务 | 租户模块 | 租户管理 | 租户新增', '0', 'Y', 2, 0),
               (21010300, 21010000, '9c081b0a1fea467fb2349145fa6b49fd', '租户修改', 3, '0,21000000,21010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:tenant:tenant:tenant:edit, RD:tenant:tenant:tenant:edit, RD:tenant:tenant:tenant:single, RD:tenant:tenant:strategy:list', null, 3, '按钮:租户服务 | 租户模块 | 租户管理 | 租户修改', '0', 'Y', 2, 0),
               (21010400, 21010000, '2ee5caad992d4a73b402931c038e21cf', '租户状态修改', 3, '0,21000000,21010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:tenant:tenant:tenant:es, RD:tenant:tenant:tenant:es', null, 4, '按钮:租户服务 | 租户模块 | 租户管理 | 租户状态修改', '0', 'Y', 2, 0),
               (21010500, 21010000, 'dfd3f200a5c145c88b90c78bb91312a3', '租户权限分配', 3, '0,21000000,21010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:tenant:tenant:tenant:auth, RD:tenant:tenant:tenant:auth, RD:system:authority:authGroup:list', null, 5, '按钮:租户服务 | 租户模块 | 租户管理 | 租户权限分配', '0', 'Y', 2, 0),
               (21010600, 21010000, '9398fdb1f5e34d52ba0df19a4ac66a5c', '租户删除', 3, '0,21000000,21010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:tenant:tenant:tenant:del, RD:tenant:tenant:tenant:del', null, 6, '按钮:租户服务 | 租户模块 | 租户管理 | 租户删除', '0', 'Y', 2, 0),
           (21020000, 21000000, '6b1f5a23273f4be2a7aeaaec16d5a972', '源策略管理', 2, '0,21000000', 'strategy', null, 'tenant/source/strategy/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, 'FE:tenant:tenant:strategy:list, RD:tenant:tenant:strategy:list, RD:tenant:tenant:source:list', 'ant-design:code-sandbox-outlined', 2, '菜单:租户服务 | 策略模块 | 数据源策略管理 | 数据源策略管理', '0', 'Y', 2, 0),
               (21020100, 21020000, 'bb6ecc8a720a4793bd93c6d060abd4d0', '源策略详情', 3, '0,21000000,21020000', 'strategyDetail/:id', null, 'tenant/source/strategy/StrategyDetail', null, null, 'N', 'Y', 'N', 'N', '0', 'X', '0', '1', '0', '0', '0', 5, null, 'FE:tenant:tenant:strategy:single, RD:tenant:tenant:strategy:single', null, 1, '详情:租户服务 | 策略模块 | 数据源策略管理 | 数据源策略详情', '0', 'Y', 2, 0),
               (21020200, 21020000, 'eb999cfc3b924c5d9973cfd3d24ee221', '源策略新增', 3, '0,21000000,21020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:tenant:tenant:strategy:add, RD:tenant:tenant:strategy:add, RD:tenant:tenant:source:list', null, 2, '按钮:租户服务 | 策略模块 | 数据源策略管理 | 数据源策略新增', '0', 'Y', 2, 0),
               (21020300, 21020000, '7063e7b32edb49769dbf4a66c69470c6', '源策略修改', 3, '0,21000000,21020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:tenant:tenant:strategy:edit, RD:tenant:tenant:strategy:edit, RD:tenant:tenant:strategy:single, RD:tenant:tenant:source:list', null, 3, '按钮:租户服务 | 策略模块 | 数据源策略管理 | 数据源策略修改', '0', 'Y', 2, 0),
               (21020400, 21020000, '303eb6f1190e4f288f6019a5197f0de6', '源策略状态修改', 3, '0,21000000,21020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:tenant:tenant:strategy:es, RD:tenant:tenant:strategy:es', null, 4, '按钮:租户服务 | 策略模块 | 数据源策略管理 | 数据源策略状态修改', '0', 'Y', 2, 0),
               (21020500, 21020000, '4866eca79112480c8791ab3b01acc15a', '源策略删除', 3, '0,21000000,21020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:tenant:tenant:strategy:del, RD:tenant:tenant:strategy:del', null, 5, '按钮:租户服务 | 策略模块 | 数据源策略管理 | 数据源策略删除', '0', 'Y', 2, 0),
           (21030000, 21000000, 'eebe7073cbe54614baec1507c90795a5', '数据源管理', 2, '0,21000000', 'source', null, 'tenant/source/source/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, 'FE:tenant:tenant:source:list, RD:tenant:tenant:source:list', 'ant-design:link-outlined', 3, '菜单:租户服务 | 策略模块 | 数据源管理 | 数据源管理', '0', 'Y', 2, 0),
               (21030100, 21030000, '55b4eab094a540a6b0ad4584108566ef', '数据源详情', 3, '0,21000000,21030000', 'sourceDetail/:id', null, 'tenant/source/source/SourceDetail', null, null, 'N', 'Y', 'N', 'N', '0', 'X', '0', '1', '0', '0', '0', 5, null, 'FE:tenant:tenant:source:single, RD:tenant:tenant:source:single', null, 1, '详情:租户服务 | 策略模块 | 数据源管理 | 数据源详情', '0', 'Y', 2, 0),
               (21030200, 21030000, '2f8484ccb4944f5780535c5a6835fe65', '数据源新增', 3, '0,21000000,21030000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:tenant:tenant:source:add, RD:tenant:tenant:source:add', null, 2, '按钮:租户服务 | 策略模块 | 数据源管理 | 数据源新增', '0', 'Y', 2, 0),
               (21030300, 21030000, '558308f42a1e4a269fa9fa33b294d730', '数据源修改', 3, '0,21000000,21030000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:tenant:tenant:source:edit, RD:tenant:tenant:source:edit, RD:tenant:tenant:source:single', null, 3, '按钮:租户服务 | 策略模块 | 数据源管理 | 数据源修改', '0', 'Y', 2, 0),
               (21030400, 21030000, '7a4d22dd4a1a4a51818408d516ca6b04', '数据源状态修改', 3, '0,21000000,21030000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:tenant:tenant:source:es, RD:tenant:tenant:source:es', null, 4, '按钮:租户服务 | 策略模块 | 数据源管理 | 数据源状态修改', '0', 'Y', 2, 0),
               (21030500, 21030000, '6d55a92a567c423cb6efc7dcba120821', '数据源删除', 3, '0,21000000,21030000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:tenant:tenant:source:del, RD:tenant:tenant:source:del', null, 5, '按钮:租户服务 | 策略模块 | 数据源管理 | 数据源删除', '0', 'Y', 2, 0),
       (22000000, 0, '0d7114083f3e4c169e29bb7c4c2394d1', '权限管理', 1, '0', 'authority', null, '', null, null, 'N', 'N', 'N', 'N', '0', 'M', '0', '0', '0', '0', '0', 1, null, '', 'ant-design:safety-certificate-outlined', 3, '目录:权限管理', '0', 'Y', 2, 0),
           (22010000, 22000000, '7fdc19cde2494105b5b1ee951a560904', '模块管理', 2, '0,22000000', 'module', null, 'system/authority/module/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, 'FE:system:authority:module:list, RD:system:authority:module:list, RD:tenant:tenant:tenant:list', 'ant-design:appstore-add-outlined', 1, '菜单:系统服务 | 权限模块 | 模块管理 | 模块管理', '0', 'Y', 2, 0),
               (22010100, 22010000, 'b6aeef72800e4082ace3673e454a66b6', '模块详情', 3, '0,22000000,22010000', 'moduleDetail/:id', null, 'system/authority/module/ModuleDetail', null, null, 'N', 'Y', 'N', 'N', '0', 'X', '0', '1', '0', '0', '0', 5, null, 'FE:system:authority:module:single, RD:system:authority:module:single', null, 1, '详情:系统服务 | 权限模块 | 模块管理 | 模块详情', '0', 'Y', 2, 0),
               (22010200, 22010000, '96047ce6aead414bab788070a65abab9', '模块新增', 3, '0,22000000,22010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:authority:module:add, RD:system:authority:module:add, RD:tenant:tenant:tenant:list', null, 2, '按钮:系统服务 | 权限模块 | 模块管理 | 模块新增', '0', 'Y', 2, 0),
               (22010300, 22010000, 'ec5c9fc0b5ba4c9794f5b87b031c3104', '模块修改', 3, '0,22000000,22010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:authority:module:edit, RD:system:authority:module:edit, RD:system:authority:module:single, RD:tenant:tenant:tenant:list', null, 3, '按钮:系统服务 | 权限模块 | 模块管理 | 模块修改', '0', 'Y', 2, 0),
               (22010400, 22010000, 'ca53bb0ac4524d58baf1e647e0d0da68', '模块状态修改', 3, '0,22000000,22010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:authority:module:es, RD:system:authority:module:es', null, 4, '按钮:系统服务 | 权限模块 | 模块管理 | 模块状态修改', '0', 'Y', 2, 0),
               (22010500, 22010000, '37a9b9cc2bcf4e7a98ea535526b3a597', '模块删除', 3, '0,22000000,22010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:authority:module:del, RD:system:authority:module:del', null, 5, '按钮:系统服务 | 权限模块 | 模块管理 | 模块删除', '0', 'Y', 2, 0),
           (22020000, 22000000, 'bba8d25857ec45f38a8946c8c74182c2', '菜单管理', 2, '0,22000000', 'menu', null, 'system/authority/menu/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, 'FE:system:authority:menu:list, RD:system:authority:menu:list, RD:tenant:tenant:tenant:list, RD:system:authority:module:list', 'ant-design:bars-outlined', 2, '菜单:系统服务 | 权限模块 | 菜单管理 | 菜单管理', '0', 'Y', 2, 0),
               (22020100, 22020000, '5e4b17aeae124b91b7bad8f4dd11b7b4', '菜单详情', 3, '0,22000000,22020000', 'menuDetail/:id', null, 'system/authority/menu/MenuDetail', null, null, 'N', 'Y', 'N', 'N', '0', 'X', '0', '1', '0', '0', '0', 5, null, 'FE:system:authority:menu:single, RD:system:authority:menu:single', null, 1, '详情:系统服务 | 权限模块 | 菜单管理 | 菜单详情', '0', 'Y', 2, 0),
               (22020200, 22020000, 'a1ea4b626a104a5380a6a2ba3eddcd2e', '菜单新增', 3, '0,22000000,22020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:authority:menu:add, RD:system:authority:menu:add, RD:tenant:tenant:tenant:list, RD:system:authority:module:list', null, 2, '按钮:系统服务 | 权限模块 | 菜单管理 | 菜单新增', '0', 'Y', 2, 0),
               (22020300, 22020000, 'af5e2c3864eb4ef3a602f3d5619a53d4', '菜单修改', 3, '0,22000000,22020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:authority:menu:edit, RD:system:authority:menu:edit, RD:system:authority:menu:single, RD:tenant:tenant:tenant:list, RD:system:authority:module:list', null, 3, '按钮:系统服务 | 权限模块 | 菜单管理 | 菜单修改', '0', 'Y', 2, 0),
               (22020400, 22020000, '69ebe38822d54e81b828c36a8dfdb479', '菜单状态修改', 3, '0,22000000,22020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:authority:menu:es, RD:system:authority:menu:es', null, 4, '按钮:系统服务 | 权限模块 | 菜单管理 | 菜单状态修改', '0', 'Y', 2, 0),
               (22020500, 22020000, 'ed7bdf2e22244a72b3963e9750d2df98', '菜单删除', 3, '0,22000000,22020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:authority:menu:del, RD:system:authority:menu:del', null, 5, '按钮:系统服务 | 权限模块 | 菜单管理 | 菜单删除', '0', 'Y', 2, 0),
           (22030000, 22000000, '1915bdb53937486a82d9ce9d380d41b1', '权限组管理', 2, '0,22000000', 'authGroup', null, 'system/authority/authGroup/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, 'FE:system:authority:authGroup:list, RD:system:authority:authGroup:list', 'ant-design:property-safety-outlined', 1, '菜单:系统服务 | 权限模块 | 租户权限组管理 | 权限组管理', '0', 'Y', 2, 0),
               (22030100, 22030000, '8630a790fd5c40c4b21f4236d60f7e75', '权限组详情', 3, '0,22000000,22030000', 'authGroupDetail/:id', null, 'system/authority/authGroup/AuthGroupDetail', null, null, 'N', 'Y', 'N', 'N', '0', 'X', '0', '1', '0', '0', '0', 5, null, 'FE:system:authority:authGroup:single, RD:system:authority:authGroup:single', null, 2, '详情:系统服务 | 权限模块 | 租户权限组管理 | 权限组详情', '0', 'Y', 2, 0),
               (22030200, 22030000, 'a681492a05154ac1b066d6bce0ef4dd1', '权限组新增', 3, '0,22000000,22030000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:authority:authGroup:add, RD:system:authority:authGroup:add', null, 3, '按钮:系统服务 | 权限模块 | 租户权限组管理 | 权限组新增', '0', 'Y', 2, 0),
               (22030300, 22030000, '127e52a2169443bbaffddbbf409ef322', '权限组修改', 3, '0,22000000,22030000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:authority:authGroup:edit, RD:system:authority:authGroup:edit, RD:system:authority:authGroup:single', null, 4, '按钮:系统服务 | 权限模块 | 租户权限组管理 | 权限组修改', '0', 'Y', 2, 0),
               (22030400, 22030000, '77a7e74eb44a4c2fb1fd27e6d2dab94c', '权限组状态修改', 3, '0,22000000,22030000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:authority:authGroup:es, RD:system:authority:authGroup:es', null, 5, '按钮:系统服务 | 权限模块 | 租户权限组管理 | 权限组状态修改', '0', 'Y', 2, 0),
               (22030500, 22030000, '78f27eab2bf240cdbbb6dc33a11e9730', '权限组删除', 3, '0,22000000,22030000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:authority:authGroup:del, RD:system:authority:authGroup:del', null, 6, '按钮:系统服务 | 权限模块 | 租户权限组管理 | 权限组删除', '0', 'Y', 2, 0),
       (23000000, 0, 'fe8bad86fdf34ed7ab584887e1e0f786', '系统管理', 1, '0', 'system', null, '', null, null, 'N', 'N', 'N', 'N', '0', 'M', '0', '0', '0', '0', '0', 1, null, '', 'ant-design:key-outlined', 4, '目录:系统管理', '0', 'Y', 2, 0),
           (23010000, 23000000, 'b08569b9c6044608913cae26f427f842', '字典管理', 2, '0,23000000', 'dict', null, 'system/dict/dict/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, 'FE:system:dict:dict:list, RD:system:dict:dict:list, RD:tenant:tenant:tenant:list', 'ant-design:file-text-outlined', 1, '菜单:系统服务 | 字典模块 | 字典管理 | 字典管理', '0', 'Y', 2, 0),
               (23010100, 23010000, 'bf7ca66a708f4ee88d5e58df65b558fb', '字典详情', 3, '0,23000000,23010000', 'dictDetail/:id', null, 'system/dict/dict/DictDetail', null, null, 'N', 'Y', 'N', 'N', '0', 'X', '0', '1', '0', '0', '0', 5, null, 'FE:system:dict:dict:single, RD:system:dict:dict:single', null, 1, '详情:系统服务 | 字典模块 | 字典管理 | 字典详情', '0', 'Y', 2, 0),
               (23010200, 23010000, '71b7dcc4d4a74f5fb78b1d4457c6829e', '字典新增', 3, '0,23000000,23010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:dict:dict:add, RD:system:dict:dict:add, RD:tenant:tenant:tenant:list', null, 2, '按钮:系统服务 | 字典模块 | 字典管理 | 字典新增', '0', 'Y', 2, 0),
               (23010300, 23010000, '72f4012cdc744c048367c50de2bf603b', '字典数据', 3, '0,23000000,23010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:dict:dict:dict, RD:system:dict:dict:dict, RD:tenant:tenant:tenant:list, RD:system:dict:dict:list', null, 3, '按钮:系统服务 | 字典模块 | 字典管理 | 字典数据', '0', 'Y', 2, 0),
               (23010400, 23010000, '2df41ed5aed44909ab4ea3f66ea5f4d4', '字典修改', 3, '0,23000000,23010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:dict:dict:edit, RD:system:dict:dict:edit, RD:system:dict:dict:single', null, 4, '按钮:系统服务 | 字典模块 | 字典管理 | 字典修改', '0', 'Y', 2, 0),
               (23010500, 23010000, 'bfa0a7b8d5be4189acf8933b4dd9bf94', '字典状态修改', 3, '0,23000000,23010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:dict:dict:es, RD:system:dict:dict:es', null, 5, '按钮:系统服务 | 字典模块 | 字典管理 | 字典状态修改', '0', 'Y', 2, 0),
               (23010600, 23010000, '8808bad6108546439e560ead38715aba', '字典删除', 3, '0,23000000,23010000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:dict:dict:del, RD:system:dict:dict:del', null, 6, '按钮:系统服务 | 字典模块 | 字典管理 | 字典删除', '0', 'Y', 2, 0),
           (23020000, 23000000, '71c9c40055b44052a27c5b3775c42054', '参数管理', 2, '0,23000000', 'config', null, 'system/dict/config/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', '0', '0', '0', '0', '0', 1, null, 'FE:system:dict:config:list, RD:system:dict:config:list, RD:tenant:tenant:tenant:list', 'ant-design:function-outlined', 2, '菜单:系统服务 | 字典模块 | 参数管理 | 参数管理', '0', 'Y', 2, 0),
               (23020100, 23020000, '9fdfef2871974adcb8dcd546af0f4aa2', '参数详情', 3, '0,23000000,23020000', 'configDetail/:id', null, 'system/dict/config/ConfigDetail', null, null, 'N', 'Y', 'N', 'N', '0', 'X', '0', '1', '0', '0', '0', 5, null, 'FE:system:dict:config:single, RD:system:dict:config:single', null, 1, '详情:系统服务 | 字典模块 | 参数管理 | 参数详情', '0', 'Y', 2, 0),
               (23020200, 23020000, '504ce298004f4a7fa93794ea974500cf', '参数新增', 3, '0,23000000,23020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:dict:config:add, RD:system:dict:config:add, RD:tenant:tenant:tenant:list', null, 2, '按钮:系统服务 | 字典模块 | 参数管理 | 参数新增', '0', 'Y', 2, 0),
               (23020300, 23020000, 'aa46bd42a43b4921afa392107b062749', '参数修改', 3, '0,23000000,23020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:dict:config:edit, RD:system:dict:config:edit, RD:system:dict:config:single, RD:tenant:tenant:tenant:list', null, 3, '按钮:系统服务 | 字典模块 | 参数管理 | 参数修改', '0', 'Y', 2, 0),
               (23020400, 23020000, '25c63be4b66e4b05b40c7393ac773fd3', '参数删除', 3, '0,23000000,23020000', null, null, null, null, null, 'N', 'N', 'N', 'N', '0', 'F', '0', '0', '0', '0', '0', 1, null, 'FE:system:dict:config:del, RD:system:dict:config:del', null, 4, '按钮:系统服务 | 字典模块 | 参数管理 | 参数删除', '0', 'Y', 2, 0),
       (24000000, 0, 'e24afc855afc485d96135999811', '系统监控', 1, '0', 'monitor', null, '', null, null, 'N', 'N', 'N', 'N', '0', 'M', '0', '0', '0', '0', '0', 1, null, '', 'ant-design:eye-outlined', 5, '目录:系统监控', '0', 'Y', 2, 0),
           (24010000, 24000000, '8fa8a4e494594496a89635028483d28b', '流量监控', 2, '0,24000000', 'sentinel', 'http://localhost:8718', null, null, null, 'N', 'N', 'N', 'N', '1', 'C', '0', '0', '0', '0', '0', 1, null, 'monitor:sentinel:list', 'ant-design:fund-projection-screen-outlined', 1, '菜单:流量监控', '0', 'Y', 2, 0),
           (24020000, 24000000, '8fa8a4e494594496a865a6428483d28b', '服务治理', 2, '0,24000000', null, 'http://127.0.0.1:8848/nacos/#/', null, null, null, 'N', 'N', 'N', 'N', '2', 'C', '0', '0', '0', '0', '0', 1, null, 'monitor:nacos:list', 'ant-design:control-outlined', 2, '菜单:服务治理', '0', 'Y', 2, 0),
           (24030000, 24000000, '8fa8a4e494594496a32586534583d28b', '服务监控', 2, '0,24000000', 'server', 'http://localhost:9100/applications', null, null, null, 'N', 'N', 'N', 'N', '1', 'C', '0', '0', '0', '0', '0', 1, null, 'monitor:server:list', 'ant-design:radar-chart-outlined', 3, '菜单:服务监控', '0', 'Y', 2, 0);

-- ----------------------------
-- 6、租户权限组信息表
-- ----------------------------
drop table if exists sys_auth_group;
create table sys_auth_group (
  id                        bigint	            not null                                comment '权限组Id',
  code                      varchar(64)         default null                            comment '权限组编码',
  name                      varchar(30)         not null                                comment '权限组名称',
  auth_key                  varchar(100)        default null                            comment '权限组权限字符串',
  sort                      int unsigned        default 0                               comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  remark                    varchar(200)        default null                            comment '备注',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  del_flag		            tinyint             not null default 0                      comment '删除标志（0正常 1删除）',
  primary key (id)
) engine = innodb comment = '租户权限组信息表';

-- ----------------------------
-- 初始化-租户权限组信息表数据
-- ----------------------------
insert into sys_auth_group (id, code, name, auth_key, remark)
values (1, '001', '默认权限组', 'A', '默认权限组');

-- ----------------------------
-- 7、租户和租户权限组关联表
-- ----------------------------
drop table if exists sys_tenant_auth_group_merge;
create table sys_tenant_auth_group_merge (
  id                        bigint              not null                                comment 'id',
  auth_group_id             bigint              not null                                comment '租户权限组Id',
  tenant_id		            bigint	            not null                                comment '租户Id',
  primary key (id),
  unique (auth_group_id, tenant_id)
) engine = innodb comment = '租户和租户权限组关联表';

-- ----------------------------
-- 8、租户权限组和模块关联表
-- ----------------------------
drop table if exists sys_auth_group_module_merge;
create table sys_auth_group_module_merge (
  id                        bigint              not null                                comment 'id',
  module_id                 bigint              not null                                comment '模块Id',
  auth_group_id		        bigint	            not null                                comment '租户权限组Id',
  primary key (id),
  unique (module_id, auth_group_id)
) engine = innodb comment = '租户权限组和模块关联表';

-- ----------------------------
-- 9、租户权限组和菜单关联表
-- ----------------------------
drop table if exists sys_auth_group_menu_merge;
create table sys_auth_group_menu_merge (
  id                        bigint              not null                                comment 'id',
  menu_id                   bigint              not null                                comment '菜单Id',
  auth_group_id		        bigint	            not null                                comment '租户权限组Id',
  primary key (id),
  unique (menu_id, auth_group_id)
) engine = innodb comment = '租户权限组和菜单关联表';

-- ----------------------------
-- 10、字典类型表
-- ----------------------------
drop table if exists sys_dict_type;
create table sys_dict_type (
  id                        bigint              not null                                comment '字典主键',
  name                      varchar(100)        default ''                              comment '字典名称',
  code                      varchar(100)        default ''                              comment '字典类型',
  data_type		            char(1)	            not null	                            comment '数据类型（0默认 1只增 2只减 3只读）',
  cache_type		        char(1)	            not null	                            comment '缓存类型（0租户 1全局）',
  sort                      int unsigned        default 0                               comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  remark                    varchar(200)        default null                            comment '备注',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  del_flag		            tinyint             not null default 0                      comment '删除标志（0正常 1删除）',
  tenant_id		            bigint	            not null                                comment '租户Id',
  primary key (id)
) engine = innodb comment = '字典类型表';

insert into sys_dict_type (id, name, code, remark, data_type, cache_type, tenant_id)
values (10001, '常规|性别', 'sys_user_sex', '常规|性别列表', '0', '1', 0),
       (10002, '常规|显隐', 'sys_show_hide', '常规|显隐列表', '0', '1', 0),
       (10003, '常规|公共私有', 'sys_common_private', '常规|公共私有列表', '0', '1', 0),
       (10004, '常规|状态', 'sys_normal_disable', '常规|状态列表', '0', '1', 0),
       (10005, '定时任务|任务状态', 'sys_job_status', '定时任务|任务状态列表', '0', '1', 0),
       (10006, '定时任务|任务策略', 'sys_job_policy', '定时任务|任务策略列表', '0', '1', 0),
       (10007, '定时任务|任务并发', 'sys_job_concurrent', '定时任务|任务并发列表', '0', '1', 0),
       (10008, '定时任务|任务分组', 'sys_job_group', '定时任务|任务分组列表', '0', '1', 0),
       (10009, '常规|是否', 'sys_yes_no', '常规|是否列表', '0', '1', 0),
       (10010, '系统服务|消息模块|通知类型', 'sys_notice_type', '系统服务|消息模块|通知类型列表', '0', '1', 0),
       (10011, '系统服务|消息模块|通知状态', 'sys_notice_status', '系统服务|消息模块|通知状态列表', '0', '1', 0),
       (10012, '系统服务|监控模块|操作类型', 'sys_operate_business', '系统服务|监控模块|操作类型列表', '0', '1', 0),
       (10013, '系统服务|监控模块|操作用户类别', 'sys_operate_type', '系统服务|监控模块|操作用户类别列表', '0', '1', 0),
       (10014, '系统服务|监控模块|操作日志状态', 'sys_operate_status', '系统服务|监控模块|操作日志状态列表', '0', '1', 0),
       (10015, '常规|消息状态', 'sys_message_status', '常规|消息状态列表', '0', '1', 0),
       (10016, '系统服务|字典模块|字典颜色', 'sys_dict_color', '系统服务|字典模块|字典颜色列表', '0', '1', 0),
       (10017, '系统服务|权限模块|授权类型', 'sys_grant_type', '系统服务|权限模块|授权类型列表', '0', '1', 0),
       (10018, '系统服务|权限模块|页面类型', 'auth_frame_type', '系统服务|权限模块|页面类型列表', '0', '1', 0),
       (10019, '系统服务|权限模块|菜单类型', 'auth_menu_type', '系统服务|权限模块|菜单类型列表', '0', '1', 0),
       (10020, '系统服务|权限模块|数据范围', 'auth_data_scope', '系统服务|权限模块|数据范围列表', '0', '1', 0),
       (10021, '代码生成|读写类型', 'sys_source_type', '代码生成|读写类型列表', '0', '1', 0),
       (10022, '代码生成|数据源类型', 'sys_database_type', '代码生成|数据源类型列表', '0', '1', 0),
       (10023, '代码生成|配置类型', 'te_configuration_type', '代码生成|配置类型列表', '0', '1', 0),
       (10024, '代码生成|模板类型', 'gen_template_type', '代码生成|模板类型列表', '0', '1', 0),
       (10025, '代码生成|属性类型', 'gen_java_type', '代码生成|属性类型列表', '0', '1', 0),
       (10026, '代码生成|查询类型', 'gen_query_type', '代码生成|查询类型列表', '0', '1', 0),
       (10027, '代码生成|显示类型', 'gen_display_type', '代码生成|显示类型列表', '0', '1', 0),
       (10028, '代码生成|生成路径', 'gen_generation_mode', '代码生成|生成路径列表', '0', '1', 0),
       (10029, '代码生成|源策略模式', 'gen_source_mode', '代码生成|源策略模式列表', '0', '1', 0),
       (10030, '系统服务|字典模块|数据类型', 'sys_dict_data_type', '系统服务|字典模块|数据类型列表', '0', '1', 0),
       (10031, '系统服务|字典模块|缓存类型', 'sys_dict_cache_type', '系统服务|字典模块|缓存类型列表', '0', '1', 0),
       (10032, '常规|功能状态', 'sys_function_status', '常规|功能状态列表', '3', '1', 0);

-- ----------------------------
-- 11、字典数据表
-- ----------------------------
drop table if exists sys_dict_data;
create table sys_dict_data (
  id                        bigint              not null                                comment '数据Id',
  code                      varchar(100)        default ''                              comment '字典编码',
  value                     varchar(100)        default ''                              comment '数据键值',
  label                     varchar(100)        default ''                              comment '数据标签',
  additional_a              varchar(100)        default null                            comment '附加数据1',
  additional_b              varchar(100)        default null                            comment '附加数据2',
  additional_c              varchar(100)        default null                            comment '附加数据3',
  sort                      int unsigned        default 0                               comment '显示顺序',
  is_default                char(1)             default 'N'                             comment '是否默认（Y是 N否）',
  css_class                 varchar(100)        default null                            comment '样式属性（其他样式扩展）',
  list_class                varchar(100)        default null                            comment '表格回显样式',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  remark                    varchar(200)        default null                            comment '备注',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  del_flag		            tinyint             not null default 0                      comment '删除标志（0正常 1删除）',
  tenant_id		            bigint	            not null                                comment '租户Id',
  primary key (id)
) engine = innodb comment = '字典数据表';

insert into sys_dict_data (id, sort, label, value, code, css_class, list_class, is_default, remark, tenant_id)
values (1000101, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '常规|性别：男', 0),
       (1000102, 2, '女', '1', 'sys_user_sex', '', '', 'N', '常规|性别：女', 0),
       (1000103, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '常规|性别：未知', 0),
       (1000201, 1, '显示', '0', 'sys_show_hide', '', 'blue', 'Y', '常规|显隐：显示', 0),
       (1000202, 2, '隐藏', '1', 'sys_show_hide', '', 'red', 'N', '常规|显隐：隐藏', 0),
       (1000301, 1, '公共', '0', 'sys_common_private', '', 'cyan', 'N', '常规|公共私有：公共', 0),
       (1000302, 2, '私有', '1', 'sys_common_private', '', 'purple', 'N', '常规|公共私有：私有', 0),
       (1000401, 1, '正常', '0', 'sys_normal_disable', '', 'blue', 'Y', '常规|状态：正常', 0),
       (1000402, 2, '停用', '1', 'sys_normal_disable', '', 'red', 'N', '常规|状态：停用', 0),
       (1000501, 1, '正常', '0', 'sys_job_status', '', 'blue', 'Y', '定时任务|任务状态：正常', 0),
       (1000502, 2, '暂停', '1', 'sys_job_status', '', 'red', 'N', '定时任务|任务状态：停用', 0),
       (1000601, 1, '默认', '0', 'sys_job_policy', '', 'blue', 'N', '定时任务|任务策略：默认', 0),
       (1000602, 2, '立即执行', '1', 'sys_job_policy', '', 'green', 'N', '定时任务|任务策略：立即执行', 0),
       (1000603, 3, '执行一次', '2', 'sys_job_policy', '', 'cyan', 'N', '定时任务|任务策略：执行一次', 0),
       (1000604, 4, '放弃执行', '3', 'sys_job_policy', '', 'purple', 'N', '定时任务|任务策略：放弃执行', 0),
       (1000701, 1, '允许', '0', 'sys_job_concurrent', '', 'blue', 'N', '定时任务|任务并发：允许', 0),
       (1000702, 2, '禁止', '1', 'sys_job_concurrent', '', 'red', 'N', '定时任务|任务并发：禁止', 0),
       (1000801, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '定时任务|任务分组：默认', 0),
       (1000802, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '定时任务|任务分组：系统', 0),
       (1000901, 1, '是', 'Y', 'sys_yes_no', '', 'blue', 'Y', '常规|是否：是', 0),
       (1000902, 2, '否', 'N', 'sys_yes_no', '', 'red', 'N', '常规|是否：否', 0),
       (1001001, 1, '通知', '0', 'sys_notice_type', '', 'blue', 'Y', '系统服务|消息模块|通知类型：通知', 0),
       (1001002, 2, '公告', '1', 'sys_notice_type', '', 'green', 'N', '系统服务|消息模块|通知类型：公告', 0),
       (1001101, 1, '待发送', '0', 'sys_notice_status', '', 'blue', 'Y', '系统服务|消息模块|通知状态：待发送', 0),
       (1001102, 2, '已发送', '1', 'sys_notice_status', '', 'green', 'N', '系统服务|消息模块|通知状态：已发送', 0),
       (1001103, 3, '已关闭', '2', 'sys_notice_status', '', 'cyan', 'N', '系统服务|消息模块|通知状态：已关闭', 0),
       (1001104, 4, '发送失败', '3', 'sys_notice_status', '', 'orange', 'N', '系统服务|消息模块|通知状态：发送失败', 0),
       (1001105, 5, '发送异常', '4', 'sys_notice_status', '', 'red', 'N', '系统服务|消息模块|通知状态：发送异常', 0),
       (1001201, 1, '其它', '00', 'sys_operate_business', '', 'orange', 'N', '系统服务|监控模块|操作类型：其它', 0),
       (1001202, 2, '新增', '01', 'sys_operate_business', '', 'blue', 'N', '系统服务|监控模块|操作类型：新增', 0),
       (1001203, 3, '强制新增', '02', 'sys_operate_business', '', 'red', 'N', '系统服务|监控模块|操作类型：强制新增', 0),
       (1001204, 4, '修改', '03', 'sys_operate_business', '', 'green', 'N', '系统服务|监控模块|操作类型：修改', 0),
       (1001205, 5, '强制修改', '04', 'sys_operate_business', '', 'red', 'N', '系统服务|监控模块|操作类型：强制修改', 0),
       (1001206, 6, '权限控制', '05', 'sys_operate_business', '', 'purple', 'N', '系统服务|监控模块|操作类型：权限控制', 0),
       (1001207, 7, '修改状态', '06', 'sys_operate_business', '', 'purple', 'N', '系统服务|监控模块|操作类型：修改状态', 0),
       (1001208, 8, '强制修改状态', '07', 'sys_operate_business', '', 'red', 'N', '系统服务|监控模块|操作类型：强制修改状态', 0),
       (1001209, 9, '删除', '08', 'sys_operate_business', '', 'orange', 'N', '系统服务|监控模块|操作类型：删除', 0),
       (1001210, 10, '强制删除', '09', 'sys_operate_business', '', 'red', 'N', '系统服务|监控模块|操作类型：强制删除', 0),
       (1001211, 11, '授权', '10', 'sys_operate_business', '', 'orange', 'N', '系统服务|监控模块|操作类型：授权', 0),
       (1001212, 12, '导出', '11', 'sys_operate_business', '', 'pink', 'N', '系统服务|监控模块|操作类型：导出', 0),
       (1001213, 13, '导入', '12', 'sys_operate_business', '', 'cyan', 'N', '系统服务|监控模块|操作类型：导入', 0),
       (1001214, 14, '强退', '13', 'sys_operate_business', '', 'orange', 'N', '系统服务|监控模块|操作类型：强退', 0),
       (1001215, 15, '生成代码', '14', 'sys_operate_business', '', 'orange', 'N', '系统服务|监控模块|操作类型：生成代码', 0),
       (1001216, 16, '清空数据', '15', 'sys_operate_business', '', 'orange', 'N', '系统服务|监控模块|操作类型：清空数据', 0),
       (1001217, 17, '更新缓存', '16', 'sys_operate_business', '', 'orange', 'N', '系统服务|监控模块|操作类型：更新缓存', 0),
       (1001301, 1, '其它', '00', 'sys_operate_type', '', 'green', 'N', '系统服务|监控模块|操作用户类别：其它', 0),
       (1001302, 2, '后台用户', '01', 'sys_operate_type', '', 'blue', 'N', '系统服务|监控模块|操作用户类别：后台用户', 0),
       (1001303, 3, '手机端用户', '02', 'sys_operate_type', '', 'red', 'N', '系统服务|监控模块|操作用户类别：手机端用户', 0),
       (1001401, 1, '正常', '0', 'sys_operate_status', '', 'green', 'N', '系统服务|监控模块|操作日志状态：正常', 0),
       (1001402, 2, '异常', '1', 'sys_operate_status', '', 'red', 'N', '系统服务|监控模块|操作日志状态：异常', 0),
       (1001501, 1, '成功', '0', 'sys_message_status', '', 'blue', 'N', '常规|消息状态：成功', 0),
       (1001502, 2, '失败', '1', 'sys_message_status', '', 'red', 'N', '常规|消息状态：失败', 0),
       (1001601, 1, 'none', '', 'sys_dict_color', '', '', 'N', '系统服务|字典模块|字典颜色：none', 0),
       (1001602, 2, 'red', 'red', 'sys_dict_color', '', 'red', 'N', '系统服务|字典模块|字典颜色：red', 0),
       (1001603, 3, 'pink', 'pink', 'sys_dict_color', '', 'pink', 'N', '系统服务|字典模块|字典颜色：pink', 0),
       (1001604, 4, 'orange', 'orange', 'sys_dict_color', '', 'orange', 'N', '系统服务|字典模块|字典颜色：orange', 0),
       (1001605, 5, 'green', 'green', 'sys_dict_color', '', 'green', 'N', '系统服务|字典模块|字典颜色：green', 0),
       (1001606, 6, 'blue', 'blue', 'sys_dict_color', '', 'blue', 'N', '系统服务|字典模块|字典颜色：blue', 0),
       (1001607, 7, 'purple', 'purple', 'sys_dict_color', '', 'purple', 'N', '系统服务|字典模块|字典颜色：purple', 0),
       (1001608, 8, 'cyan', 'cyan', 'sys_dict_color', '', 'cyan', 'N', '系统服务|字典模块|字典颜色：cyan', 0),
       (1001701, 1, '授权码模式', 'authorization_code', 'sys_grant_type', '', '', 'N', '系统服务|权限模块|授权类型：授权码模式', 0),
       (1001702, 2, '密码模式', 'password', 'sys_grant_type', '', '', 'N', '系统服务|权限模块|授权类型：密码模式', 0),
       (1001703, 3, '客户端模式', 'client_credentials', 'sys_grant_type', '', '', 'N', '系统服务|权限模块|授权类型：客户端模式', 0),
       (1001704, 4, '简化模式', 'implicit', 'sys_grant_type', '', '', 'N', '系统服务|权限模块|授权类型：简化模式', 0),
       (1001705, 5, '刷新模式', 'refresh_token', 'sys_grant_type', '', '', 'N', '系统服务|权限模块|授权类型：刷新模式', 0),
       (1001801, 1, '常规', '0', 'auth_frame_type', '', 'pink', 'Y', '系统服务|权限模块|页面类型：常规', 0),
       (1001802, 2, '内嵌', '1', 'auth_frame_type', '', 'cyan', 'N', '系统服务|权限模块|页面类型：内嵌', 0),
       (1001803, 3, '外链', '2', 'auth_frame_type', '', 'green', 'N', '系统服务|权限模块|页面类型：外链', 0),
       (1001901, 1, '目录', 'M', 'auth_menu_type', '', 'pink', 'Y', '系统服务|权限模块|菜单类型：目录', 0),
       (1001902, 2, '菜单', 'C', 'auth_menu_type', '', 'cyan', 'N', '系统服务|权限模块|菜单类型：菜单', 0),
       (1001903, 3, '详情', 'X', 'auth_menu_type', '', 'purple', 'N', '系统服务|权限模块|菜单类型：详情', 0),
       (1001904, 4, '按钮', 'F', 'auth_menu_type', '', 'green', 'N', '系统服务|权限模块|菜单类型：按钮', 0),
       (1002001, 1, '全部数据权限', '1', 'auth_data_scope', '', '', 'Y', '系统服务|权限模块|数据范围：全部数据权限', 0),
       (1002002, 2, '自定数据权限', '2', 'auth_data_scope', '', '', 'N', '系统服务|权限模块|数据范围：自定数据权限', 0),
       (1002003, 3, '本部门数据权限', '3', 'auth_data_scope', '', '', 'N', '系统服务|权限模块|数据范围：本部门数据权限', 0),
       (1002004, 4, '本部门及以下数据权限', '4', 'auth_data_scope', '', '', 'N', '系统服务|权限模块|数据范围：本部门及以下数据权限', 0),
       (1002005, 5, '本岗位数据权限', '5', 'auth_data_scope', '', '', 'N', '系统服务|权限模块|数据范围：本岗位数据权限', 0),
       (1002006, 6, '仅本人数据权限', '6', 'auth_data_scope', '', '', 'N', '系统服务|权限模块|数据范围：仅本人数据权限', 0),
       (1002101, 1, '读&写', '0', 'sys_source_type', '', 'blue', 'Y', '代码生成|读写类型：读&写', 0),
       (1002102, 2, '只读', '1', 'sys_source_type', '', 'green', 'N', '代码生成|读写类型：只读', 0),
       (1002103, 3, '只写', '2', 'sys_source_type', '', 'red', 'N', '代码生成|读写类型：只写', 0),
       (1002201, 1, '子数据源', '0', 'sys_database_type', '', 'green', 'Y', '代码生成|数据源类型：子数据源', 0),
       (1002202, 2, '主数据源', '1', 'sys_database_type', '', 'red', 'N', '代码生成|数据源类型：主数据源', 0),
       (1002301, 1, '自动配置', '0', 'te_configuration_type', '', '', 'N', '代码生成|配置类型：自动配置', 0),
       (1002302, 2, '手动配置', '1', 'te_configuration_type', '', '', 'N', '代码生成|配置类型：手动配置', 0),
       (1002401, 1, '单表', 'base', 'gen_template_type', '', '', 'Y', '代码生成|模板类型：单表', 0),
       (1002402, 2, '树表', 'tree', 'gen_template_type', '', '', 'N', '代码生成|模板类型：树表', 0),
       (1002403, 3, '关联表', 'merge', 'gen_template_type', '', '', 'N', '代码生成|模板类型：关联表', 0),
       (1002501, 1, 'Long', 'Long', 'gen_java_type', '', '', 'N', '代码生成|属性类型：Long', 0),
       (1002502, 2, 'String', 'String', 'gen_java_type', '', '', 'N', '代码生成|属性类型：String', 0),
       (1002503, 3, 'Integer', 'Integer', 'gen_java_type', '', '', 'N', '代码生成|属性类型：Integer', 0),
       (1002504, 4, 'Double', 'Double', 'gen_java_type', '', '', 'N', '代码生成|属性类型：Double', 0),
       (1002505, 5, 'BigDecimal', 'BigDecimal', 'gen_java_type', '', '', 'N', '代码生成|属性类型：BigDecimal', 0),
       (1002506, 6, 'LocalDateTime', 'LocalDateTime', 'gen_java_type', '', '', 'N', '代码生成|属性类型：LocalDateTime', 0),
       (1002601, 1, '=', 'eq', 'gen_query_type', '', '', 'Y', '代码生成|查询类型：=', 0),
       (1002602, 2, '!=', 'ne', 'gen_query_type', '', '', 'N', '代码生成|查询类型：!=', 0),
       (1002603, 3, '>', 'gt', 'gen_query_type', '', '', 'N', '代码生成|查询类型：>', 0),
       (1002604, 4, '>=', 'ge', 'gen_query_type', '', '', 'N', '代码生成|查询类型：>=', 0),
       (1002605, 5, '<', 'lt', 'gen_query_type', '', '', 'N', '代码生成|查询类型：<', 0),
       (1002606, 6, '<=', 'le', 'gen_query_type', '', '', 'N', '代码生成|查询类型：<=', 0),
       (1002607, 7, 'LIKE', 'like', 'gen_query_type', '', '', 'N', '代码生成|查询类型：LIKE', 0),
       (1002608, 8, 'NOT_LIKE', 'notLike', 'gen_query_type', '', '', 'N', '代码生成|查询类型：NOT_LIKE', 0),
       (1002609, 9, 'LIKE_LEFT', 'likeLeft', 'gen_query_type', '', '', 'N', '代码生成|查询类型：LIKE_LEFT', 0),
       (1002610, 10, 'LIKE_RIGHT', 'likeRight', 'gen_query_type', '', '', 'N', '代码生成|查询类型：LIKE_RIGHT', 0),
       (1002611, 11, 'BETWEEN', 'between', 'gen_query_type', '', '', 'N', '代码生成|查询类型：BETWEEN', 0),
       (1002612, 12, 'NOT_BETWEEN', 'notBetween', 'gen_query_type', '', '', 'N', '代码生成|查询类型：NOT_BETWEEN', 0),
       (1002613, 13, 'ISNULL', 'isNull', 'gen_query_type', '', '', 'N', '代码生成|查询类型：ISNULL', 0),
       (1002614, 14, 'IS_NOT_NULL', 'isNotNull', 'gen_query_type', '', '', 'N', '代码生成|查询类型：IS_NOT_NULL', 0),
       (1002701, 1, '文本框', 'Input', 'gen_display_type', '', '', 'Y', '代码生成|显示类型：文本框', 0),
       (1002702, 2, '密码框', 'InputPassword', 'gen_display_type', '', '', 'Y', '代码生成|显示类型：密码框', 0),
       (1002703, 3, '数字输入框', 'InputNumber', 'gen_display_type', '', '', 'Y', '代码生成|显示类型：数字输入框', 0),
       (1002704, 4, '文本域', 'InputTextArea', 'gen_display_type', '', '', 'N', '代码生成|显示类型：文本域', 0),
       (1002705, 5, '下拉框', 'Select', 'gen_display_type', '', '', 'N', '代码生成|显示类型：下拉框', 0),
       (1002706, 6, '单选框', 'CheckboxGroup', 'gen_display_type', '', '', 'N', '代码生成|显示类型：单选框', 0),
       (1002707, 7, '按钮风格单选框', 'RadioButtonGroup', 'gen_display_type', '', '', 'N', '代码生成|显示类型：按钮风格单选框', 0),
       (1002708, 8, '复选框', 'RadioGroup', 'gen_display_type', '', '', 'N', '代码生成|显示类型：复选框', 0),
       (1002709, 9, '日期控件', 'DatePicker', 'gen_display_type', '', '', 'N', '代码生成|显示类型：日期控件', 0),
       (1002710, 10, '时间控件', 'TimePicker', 'gen_display_type', '', '', 'N', '代码生成|显示类型：时间控件', 0),
       (1002711, 11, '图片上传', 'ImageUpload', 'gen_display_type', '', '', 'N', '代码生成|显示类型：图片上传', 0),
       (1002712, 12, '文件上传', 'Upload', 'gen_display_type', '', '', 'N', '代码生成|显示类型：文件上传', 0),
       (1002713, 13, '富文本控件', 'tinymce', 'gen_display_type', '', '', 'N', '代码生成|显示类型：富文本控件', 0),
       (1002714, 14, 'MarkDown编辑器控件', 'markdown', 'gen_display_type', '', '', 'N', '代码生成|显示类型：MarkDown编辑器控件', 0),
       (1002801, 1, '默认路径', '0', 'gen_generation_mode', '', '', 'Y', '代码生成|生成路径：默认路径', 0),
       (1002802, 2, '自定义路径', '1', 'gen_generation_mode', '', '', 'N', '代码生成|生成路径：自定义路径', 0),
       (1002901, 1, '策略源', 'Isolate', 'gen_source_mode', '', '', 'Y', '代码生成|源策略模式：策略源', 0),
       (1002902, 2, '主数据源', 'Master', 'gen_source_mode', '', '', 'N', '代码生成|源策略模式：主数据源', 0),
       (1003001, 1, '默认', '0', 'sys_dict_data_type', '', 'blue', 'N', '系统服务|字典模块|数据类型：默认', 0),
       (1003002, 2, '只增', '1', 'sys_dict_data_type', '', 'green', 'N', '系统服务|字典模块|数据类型：只增', 0),
       (1003003, 3, '只减', '2', 'sys_dict_data_type', '', 'cyan', 'N', '系统服务|字典模块|数据类型：只减', 0),
       (1003004, 4, '只读', '3', 'sys_dict_data_type', '', 'purple', 'N', '系统服务|字典模块|数据类型：只读', 0),
       (1003101, 1, '租户', '0', 'sys_dict_cache_type', '', 'purple', 'N', '系统服务|字典模块|缓存类型：租户', 0),
       (1003102, 2, '全局', '1', 'sys_dict_cache_type', '', 'blue', 'N', '系统服务|字典模块|缓存类型：全局', 0),
       (1003201, 1, '开启', 'Y', 'sys_function_status', '', 'blue', 'N', '常规|功能状态: 开启', 0),
       (1003202, 2, '关闭', 'N', 'sys_function_status', '', 'orange', 'Y', '常规|功能状态: 关闭', 0);

-- ----------------------------
-- 12、参数配置表
-- ----------------------------
drop table if exists sys_config;
create table sys_config (
  id                        bigint              not null                                comment '参数Id',
  name                      varchar(100)        default ''                              comment '参数名称',
  code                      varchar(100)        default ''                              comment '参数编码',
  value                     text                                                        comment '参数键值',
  data_type		            char(1)	            not null	                            comment '数据类型（0默认 1只读）',
  cache_type		        char(1)	            not null	                            comment '缓存类型（0租户 1全局）',
  type                      char(1)             default 'N'                             comment '系统内置（Y是 N否）',
  sort                      int unsigned        default 0                               comment '显示顺序',
  remark                    varchar(200)        default null                            comment '备注',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  del_flag		            tinyint             not null default 0                      comment '删除标志（0正常 1删除）',
  tenant_id		            bigint	            not null                                comment '租户Id',
  primary key (id)
) engine = innodb comment = '参数配置表';

insert into sys_config (id, name, code, value, type, data_type, cache_type, remark, tenant_id)
values (1, '主框架页-默认皮肤样式名称',           'sys.index.skinName',          'skin-blue',      'Y',  '0',  '0',    '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow', 0),
       (2, '用户管理-账号初始密码',              'sys.user.initPassword',         '123456',       'Y',  '0',  '0',    '初始化密码 123456', 0),
       (3, '主框架页-侧边栏主题',                'sys.index.sideTheme',           'theme-dark',   'Y',  '0',  '0',    '深色主题theme-dark，浅色主题theme-light', 0),
       (4, '账号自助-是否开启租户注册功能',        'sys.account.registerTenant',    'false',        'Y',  '0',  '0',    '是否开启注册租户功能（true开启，false关闭）', 0),
       (10001, '系统模块:数据编码配置:功能开关',   'sys.code.show', 'Y', 'Y', '0', '0', '系统模块:数据编码配置:功能开关（Y启用 N禁用）', 0),
       (10002, '系统模块:数据编码配置:必须字段',   'sys.code.must', 'N', 'Y', '0', '0', '系统模块:数据编码配置:必须字段（Y是 N否）', 0);

-- ----------------------------
-- 13、定时任务调度表
-- ----------------------------
drop table if exists sys_job;
create table sys_job (
  id                        bigint              not null                                comment '任务Id',
  name                      varchar(64)         default ''                              comment '任务名称',
  job_group                 varchar(64)         default 'DEFAULT'                       comment '任务组名',
  invoke_target             varchar(500)        not null                                comment '调用目标字符串',
  invoke_tenant             varchar(500)        not null                                comment '调用租户字符串',
  cron_expression           varchar(255)        default ''                              comment 'cron执行表达式',
  misfire_policy            varchar(20)         default '3'                             comment '计划执行错误策略（0默认 1立即执行 2执行一次 3放弃执行）',
  concurrent                char(1)             default '1'                             comment '是否并发执行（0允许 1禁止）',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1暂停）',
  remark                    varchar(200)        default null                            comment '备注',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
  tenant_id		            bigint	            not null                                comment '租户Id',
  primary key (id)
) engine = innodb auto_increment = 100 comment = '定时任务调度表';

insert into sys_job (id, name, job_group, invoke_target, invoke_tenant, cron_expression, misfire_policy, concurrent, status, tenant_id)
values (1, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams',   '1L, \'Y\', \'slave\'',     '0/10 * * * * ?', '3', '1', '1', 1),
       (2, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')',   '1L, \'Y\', \'slave\'',  '0/15 * * * * ?', '3', '1', '1', 1),
       (3, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)',   '1L, \'Y\', \'slave\'',  '0/20 * * * * ?', '3', '1', '1', 1);

-- ----------------------------
-- 14、定时任务调度日志表
-- ----------------------------
drop table if exists sys_job_log;
create table sys_job_log (
  id                        bigint              not null auto_increment                 comment '任务日志Id',
  job_id                    bigint              not null                                comment '任务Id',
  name                      varchar(64)         not null                                comment '任务名称',
  job_group                 varchar(64)         not null                                comment '任务组名',
  invoke_target             varchar(500)        not null                                comment '调用目标字符串',
  invoke_tenant             varchar(500)        not null                                comment '调用租户字符串',
  job_message               varchar(500)                                                comment '日志信息',
  status                    char(1)             not null default '0'                    comment '执行状态（0正常 1失败）',
  exception_info            varchar(2000)       default ''                              comment '异常信息',
  create_time               datetime            default current_timestamp               comment '创建时间',
  del_time                  datetime            on update current_timestamp             comment '删除时间',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
  tenant_id		            bigint	            not null                                comment '租户Id',
  primary key (id)
) engine = innodb comment = '定时任务调度日志表';

-- ----------------------------
-- 15、代码生成业务表
-- ----------------------------
drop table if exists gen_table;
create table gen_table (
  id                        bigint              not null auto_increment                 comment '表Id',
  name                      varchar(200)        default ''                              comment '表名称',
  comment                   varchar(200)        default ''                              comment '表描述',
  class_name                varchar(50)         default ''                              comment '实体类名称',
  prefix                    varchar(50)         default ''                              comment '前缀名称',
  tpl_category              varchar(10)         not null default 'base'                 comment '使用的模板（base单表 tree树表 merge关联表）',
  rd_package_name           varchar(100)                                                comment '生成后端包路径',
  fe_package_name           varchar(100)                                                comment '生成前端包路径',
  authority_name            varchar(30)                                                 comment '生成权限标识',
  module_name               varchar(30)                                                 comment '生成模块名',
  business_name             varchar(30)                                                 comment '生成业务名',
  function_name             varchar(50)                                                 comment '生成功能名',
  function_author           varchar(50)                                                 comment '生成功能作者',
  gen_type                  char(1)             default '0'                             comment '生成路径类型（0默认路径 1自定义路径）',
  gen_path                  varchar(200)        default '/'                             comment '后端生成路径（不填默认项目路径）',
  ui_path                   varchar(200)        default '/'                             comment '前端生成路径（不填默认项目路径）',
  options                   varchar(1000)                                               comment '其它生成选项',
  remark                    varchar(200)        default null                            comment '备注',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  primary key (id)
) engine=innodb auto_increment=1 comment = '代码生成业务表';

-- ----------------------------
-- 16、代码生成业务表字段
-- ----------------------------
drop table if exists gen_table_column;
create table gen_table_column (
  id                        bigint              not null auto_increment                 comment '字段Id',
  table_id                  bigint              not null                                comment '表Id',
  name                      varchar(100)                                                comment '列名称',
  comment                   varchar(100)                                                comment '列描述',
  type                      varchar(20)                                                 comment '列类型',
  java_type                 varchar(20)                                                 comment 'JAVA类型',
  java_field                varchar(100)                                                comment 'JAVA字段名',
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
  sort                      int unsigned        default 0                               comment '显示顺序',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  primary key (id)
) engine=innodb auto_increment=1 comment = '代码生成业务表字段';

-- ----------------------------
-- 17、终端信息表
-- ----------------------------
drop table if exists sys_oauth_client;
create table sys_oauth_client (
  id		                bigint	            not null                                comment '租户Id',
  client_id                 varchar(32)         not null                                comment '客户端Id',
  resource_ids              varchar(256)        default null                            comment '资源列表',
  client_secret             varchar(256)        default null                            comment '客户端密钥',
  scope                     varchar(256)        default null                            comment '域',
  authorized_grant_types    varchar(256)        default null                            comment '认证类型',
  web_server_redirect_uri   varchar(256)        default null                            comment '重定向地址',
  authorities               varchar(256)        default null                            comment '角色列表',
  access_token_validity     int                 default null                            comment 'token 有效期',
  refresh_token_validity    int                 default null                            comment '刷新令牌有效期',
  additional_information    varchar(4096)       default null                            comment '令牌扩展字段JSON',
  auto_approve              varchar(256)        default null                            comment '是否自动放行',
  sort                      int unsigned        default 0                               comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  remark                    varchar(200)        default null                            comment '备注',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  del_flag		            tinyint             not null default 0                      comment '删除标志（0正常 1删除）',
  primary key (id),
  unique (client_id)
) engine=innodb comment = '终端信息表';

insert into sys_oauth_client (id, client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, auto_approve, sort, status)
values (1, 'app',       null, 'app', 'server', 'app,refresh_token', null, null, null, null, null, 'true', 1, '0'),
       (2, 'client',    null, 'client', 'server', 'client_credentials', null, null, null, null, null, 'true', 1, '0'),
       (3, 'daemon',    null, 'daemon', 'server', 'password,refresh_token', null, null, null, null, null, 'true', 1, '0'),
       (4, 'xueyi',     null, 'xueyi', 'server', 'password,app,refresh_token,authorization_code,client_credentials', 'https://xueyitt.cn', null, null, null, null, 'true', 1, '0'),
       (5, 'gen',       null, 'gen', 'server', 'password,refresh_token', null, null, null, null, null, 'true', 1, '0');

-- ----------------------------
-- 18、导入导出配置信息表
-- ----------------------------
drop table if exists sys_im_ex;
create table sys_im_ex (
  id                        bigint              not null                                comment '配置Id',
  code                      varchar(100)        not null                                comment '配置编码',
  name                      varchar(100)        not null                                comment '配置名称',
  import_config             text                                                        comment '导入配置',
  export_config             text                                                        comment '导出配置',
  data_type		            char(1)	            not null	                            comment '数据类型（0默认 1只读）',
  cache_type		        char(1)	            not null	                            comment '缓存类型（0租户 1全局）',
  sort                      int unsigned        default 0                               comment '显示顺序',
  remark                    varchar(200)                                                comment '备注',
  create_by                 bigint                                                      comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint                                                      comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  del_flag		            tinyint             not null default 0                      comment '删除标志（0正常 1删除）',
  tenant_id		            bigint	            not null                                comment '租户Id',
  primary key (id)
) engine = innodb comment = '导入导出配置信息表';

INSERT INTO sys_im_ex (id, code, name, import_config, export_config, data_type, cache_type, sort, remark, create_by, create_time, update_by, update_time, del_flag, tenant_id)
VALUES (1, 'IE0001', '导入导出配置demo',
        '{	"sheetName": "数据表",	"fieldList": [		{			"name": "资产编号",			"sort": 1,			"field": "assetCode"		},		{			"name": "财务编码",			"sort": 2,			"field": "financeCode"		},		{			"name": "设备名称",			"sort": 3,			"field": "assetName"		},		{			"name": "使用部门",			"sort": 4,			"field": "deptName"		},		{			"name": "使用年限",			"sort": 5,			"field": "assetLife"		},		{			"name": "建筑面积或工程量",			"sort": 6,			"field": "cusKey9"		},		{			"name": "施工单位",			"sort": 7,			"field": "cusKey3"		},		{			"name": "出厂年月",			"sort": 8,			"field": "manufactureTime"		},		{			"name": "结构形式",			"sort": 9,			"field": "cusKey11"		},		{			"name": "建设地点",			"sort": 10,			"field": "cusKey8"		},		{			"name": "立卡年月",			"sort": 11,			"field": "tentCardTime"		},		{			"name": "开工日期",			"sort": 12,			"field": "cusKey10"		},		{			"name": "竣工日期",			"sort": 13,			"field": "cusKey5"		},		{			"name": "原值(元)",			"sort": 14,			"field": "currentPrice"		},		{			"name": "当前管理状态",			"sort": 15,			"field": "status"		},		{			"name": "外形尺寸",			"sort": 16,			"field": "cusKey7"		},		{			"name": "随机附件",			"sort": 17,			"field": "cusKey12"		},		{			"name": "备注",			"sort": 18,			"field": "remark"		},		{			"name": "财务账上编号",			"sort": 19,			"field": "cusKey13"		},		{			"name": "报废日期",			"sort": 20,			"field": "realityScrapTime"		}	]}',
        '{	"sheetName": "数据表",	"fieldList": [		{			"name": "变更前价值",			"sort": 1,			"field": "beforePrice"		},		{			"name": "变更价值",			"sort": 1,			"field": "modifyPrice"		},		{			"name": "变更后价值",			"sort": 1,			"field": "afterPrice"		},		{			"field": "assetInfo",			"children": [				{					"name": "资产编号",					"sort": 1,					"field": "assetCode"				}			]		}	]}',
        '3', '1', 0, null, null, '2024-01-23 21:04:08', null, '2024-01-25 11:02:49', 0, 0);
