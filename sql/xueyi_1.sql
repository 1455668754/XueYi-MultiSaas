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
  slave		                varchar(500)	    not null default ''	                    comment '数据源编码',
  driver_class_name		    varchar(500)	    not null default ''	                    comment '驱动',
  url_prepend	            varchar(500)	    not null default ''	                    comment '连接地址',
  url_append	            varchar(500)	    not null default ''	                    comment '连接参数',
  username	                varchar(500)	    not null default ''	                    comment '用户名',
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
primary key (id)
) engine=innodb comment = '数据源表';

-- ----------------------------
-- 初始化-数据源表数据 | 这条数据为我的基础库，实际使用时调整成自己的库即可
-- ----------------------------
insert into te_source(id, name, is_default, slave, driver_class_name, url_prepend, url_append, username, password)
values (1, '注册数据源', 'Y', 'slave', 'com.mysql.cj.jdbc.Driver', 'jdbc:mysql://124.71.32.2:32001/xy-cloud1-v3', '?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8', 'root', 'password');

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
  hide_module		        char(1)	            not null default 'N'	                comment '模块显隐状态（Y隐藏 N显示）',
  sort                      int unsigned        not null default 0                      comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  remark                    varchar(1000)       default null                            comment '备注',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  is_common                 char(1)             not null default 'N'	                comment '公共模块（Y是 N否）',
  is_default                char(1)             not null default 'N'	                comment '默认模块（Y是 N否）',
  del_flag		            tinyint             not null default 0                      comment '删除标志（0正常 1删除）',
  tenant_id		            bigint	            not null                                comment '租户Id',
  primary key (id)
) engine=innodb comment = '模块信息表';

# ----------------------------
# 初始化-模块信息表数据
# ----------------------------
insert into sys_module (id, tenant_id, name, is_common, type, path, param_path, is_default, hide_module, logo, remark)
values (0, 0, '默认系统' ,    'Y', '0', '', '', 'Y', 'Y', 'https://images.gitee.com/uploads/images/2021/1101/141155_f3dfce1d_7382127.jpeg', '默认系统'),
       (1, -1, '租户管理系统' , 'N', '0', '', '', 'Y', 'N', 'https://images.gitee.com/uploads/images/2021/1101/141601_d68e92a4_7382127.jpeg', '租户管理系统');

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
  path                      varchar(200)        not null default ''                     comment '路由地址',
  frame_src                 varchar(200)        default null                            comment '外链地址 | 仅页面类型为外链时生效',
  component                 varchar(255)        default null                            comment '组件路径',
  param_path                varchar(255)        default null                            comment '路由参数',
  transition_name           varchar(255)        default null                            comment '路由切换动画',
  ignore_route              char(1)             not null default  'N'                   comment '是否忽略路由（Y是 N否）',
  is_cache                  char(1)             not null default  'N'                   comment '是否缓存（Y是 N否）',
  is_affix                  char(1)             not null default  'N'                   comment '是否固定标签（Y是 N否）',
  is_disabled               char(1)             not null default  'N'                   comment '是否禁用（Y是 N否）',
  frame_type                char(1)             not null default  '0'                   comment '页面类型（0常规 1内嵌 2外链）',
  menu_type                 char(1)             not null default  ''                    comment '菜单类型（M目录 C菜单 X详情 F按钮）',
  hide_tab                  char(1)             not null default  'N'                   comment '标签显隐状态（Y隐藏 N显示）',
  hide_menu                 char(1)             not null default  'N'                   comment '菜单显隐状态（Y隐藏 N显示）',
  hide_breadcrumb           char(1)             not null default  'N'                   comment '面包屑路由显隐状态（Y隐藏 N显示）',
  hide_children             char(1)             not null default  'N'                   comment '子菜单显隐状态（Y隐藏 N显示）',
  hide_path_for_children    char(1)             not null default  'N'                   comment '是否在子级菜单的完整path中忽略本级path（Y隐藏 N显示）',
  dynamic_level             int                 not null default 1                      comment '详情页可打开Tab页数',
  real_path                 varchar(255)        default null                            comment '详情页的实际Path',
  current_active_menu       varchar(255)        default null                            comment '详情页激活的菜单',
  perms                     varchar(100)        default null                            comment '权限标识',
  icon                      varchar(100)        not null default  '#'                   comment '菜单图标',
  sort                      int unsigned        not null default  0                     comment '显示顺序',
  status                    char(1)             not null default  '0'                   comment '状态（0正常 1停用）',
  remark                    varchar(1000)       default null                            comment '备注',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  is_common                 char(1)             not null default  'N'                   comment '公共菜单（Y是 N否）',
  is_default                char(1)             not null default  'N'                   comment '默认菜单（Y是 N否）',
  del_flag                  tinyint             not null default  0                     comment '删除标志(0正常 1删除)',
  module_id                 bigint              not null                                comment '模块Id',
  tenant_id                 bigint              not null                                comment '租户Id',
  primary key (id),
  unique (name)
) engine=innodb comment = '菜单权限表';

insert into sys_menu (id, parent_id, name, title, ancestors, path, frame_src, component, param_path, transition_name, ignore_route, is_cache, is_affix, is_disabled, frame_type, menu_type, hide_tab, hide_menu, hide_breadcrumb, hide_children, hide_path_for_children, dynamic_level, real_path, current_active_menu, perms, icon, sort, remark, is_common, is_default, module_id, tenant_id)
values (0, 0, 'Default', '默认菜单', '', 'default', null, null, null, null, 'N', 'N', 'N', 'N', '0', 'M', 'N', 'N', 'N', 'N', 'N', 1, null, null, null, 'xy_organization', 1, '目录:默认菜单', 'Y', 'Y', 0, 0),
       (23000000, 0, 'GenTool', '系统工具', '0', 'tool', null, '', null, null, 'N', 'N', 'N', 'N', '0', 'M', 'N', 'N', 'N', 'N', 'N', 1, null, null, '', 'xy_organization', 3, '目录:系统工具', 'Y', 'Y', 0, 0),
       (23010000, 23000000, 'GenGen', '代码生成', '0,23000000', 'gen', null, 'tool/gen/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', 'N', 'N', 'N', 'N', 'N', 1, null, null, 'system:gen:list', 'xy_organization', 1, '菜单:代码生成', 'Y', 'Y', 0, 0),
       (23010100, 23010000, 'GenGenDetail', '代码生成配置', '0,23010000', 'generate/:id', null, 'tool/gen/GenDetail', null, null, 'N', 'Y', 'N', 'N', '0', 'X', 'N', 'Y', 'N', 'N', 'N', 1, null, '/tool/gen', 'system:gen:edit', 'xy_organization', 2, '详情:代码生成配置', 'Y', 'Y', 0, 0),
       (23010200, 23010000, 'GenCodeDetail', '代码生成预览', '0,23010000', 'code/:id', null, 'tool/gen/CodeDetail', null, null, 'N', 'Y', 'N', 'N', '0', 'X', 'N', 'Y', 'N', 'N', 'N', 1, null, '/tool/gen', 'system:gen:preview', 'xy_organization', 2, '详情:代码生成预览', 'Y', 'Y', 0, 0);

# (10000000, 0, 'SysOrganize', '组织管理', '0', 'organize', null, null, null, null, 'N', 'N', 'N', 'N', '0', 'M', 'N', 'N', 'N', 'N', 'N', 1, null, null, null, 'xy_organization', 1, '目录:组织管理', 'Y', 'Y', 0, 0),
# (10010000, 10000000, 'SysDept', '部门管理', '0,10000000', 'dept', null, 'system/organize/dept/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', 'N', 'N', 'N', 'N', 'N', 1, null, null, 'system:dept:list', 'xy_organization', 1, '菜单:部门管理', 'Y', 'Y', 0, 0),
# (10020000, 10000000, 'SysPost', '岗位管理', '0,10000000', 'post', null, 'system/organize/post/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', 'N', 'N', 'N', 'N', 'N', 1, null, null, 'system:post:list', 'xy_organization', 2, '菜单:岗位管理', 'Y', 'Y', 0, 0),
# (10030000, 10000000, 'SysUser', '用户管理', '0,10000000', 'user', null, 'system/organize/user/index', null, null, 'N', 'N', 'N', 'N', '0', 'C', 'N', 'N', 'N', 'N', 'N', 1, null, null, 'system:user:list', 'xy_organization', 3, '菜单:用户管理', 'Y', 'Y', 0, 0),
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

insert into sys_dict_type (id, name, code, remark)
values (1, '用户性别', 'sys_user_sex', '用户性别列表'),
       (2, '常规：显隐', 'sys_show_hide', '常规：显隐列表'),
       (3, '系统开关', 'sys_normal_disable', '系统开关列表'),
       (4, '任务状态', 'sys_job_status', '任务状态列表'),
       (5, '任务分组', 'sys_job_group', '任务分组列表'),
       (6, '常规-是否', 'sys_yes_no', '常规：是否列表'),
       (7, '通知类型', 'sys_notice_type', '通知类型列表'),
       (8, '通知状态', 'sys_notice_status', '通知状态列表'),
       (9, '操作类型', 'sys_oper_type', '操作类型列表'),
       (10, '系统状态', 'sys_common_status', '登录状态列表'),
       (11, '授权类型', 'sys_grant_type', '授权类型列表'),
       (12, '权限模块：页面类型', 'sys_frame_type', '权限模块：页面类型列表'),
       (13, '读写类型', 'sys_source_type', '读写类型列表'),
       (14, '数据源类型', 'sys_database_type', '数据源类型列表'),
       (15, '配置类型', 'te_configuration_type', '配置类型列表'),
       (16, '常规：公共私有', 'sys_common_type', '常规：公共私有列表'),
       (17, '数据范围', 'sys_data_scope', '数据范围列表'),
       (18, '代码生成：模板类型', 'gen_template_type', '代码生成：模板类型列表'),
       (19, '代码生成：属性类型', 'gen_java_type', '代码生成：属性类型列表'),
       (20, '代码生成：查询类型', 'gen_query_type', '代码生成：查询类型列表'),
       (21, '代码生成：显示类型', 'gen_display_type', '代码生成：显示类型列表'),
       (22, '代码生成：生成方式', 'gen_generation_mode', '代码生成：生成方式列表'),
       (23, '代码生成：源策略模式', 'gen_source_mode', '代码生成：源策略模式列表');

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
       (1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '常规-显隐：显示'),
       (2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '常规-显隐：隐藏'),
       (1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '正常状态'),
       (2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '停用状态'),
       (1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '正常状态'),
       (2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '停用状态'),
       (1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '默认分组'),
       (2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '系统分组'),
       (1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '常规-是否：是'),
       (2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '常规-是否：否'),
       (1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '通知'),
       (2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '公告'),
       (1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '正常状态'),
       (2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '关闭状态'),
       (1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '新增操作'),
       (2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '修改操作'),
       (3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '删除操作'),
       (4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '授权操作'),
       (5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '导出操作'),
       (6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '导入操作'),
       (7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '强退操作'),
       (8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '生成操作'),
       (9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '清空操作'),
       (1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '正常状态'),
       (2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '停用状态'),
       (1, '授权码模式', 'authorization_code', 'sys_grant_type', '', '', 'N', '授权码模式'),
       (2, '密码模式', 'password', 'sys_grant_type', '', '', 'N', '密码模式'),
       (3, '客户端模式', 'client_credentials', 'sys_grant_type', '', '', 'N', '客户端模式'),
       (4, '简化模式', 'implicit', 'sys_grant_type', '', '', 'N', '简化模式'),
       (5, '刷新模式', 'refresh_token', 'sys_grant_type', '', '', 'N', '刷新模式'),
       (1, '常规', '0', 'sys_frame_type', '', '', 'Y', '权限模块-页面类型：常规'),
       (2, '内嵌', '1', 'sys_frame_type', '', '', 'N', '权限模块-页面类型：内嵌'),
       (2, '外链', '2', 'sys_frame_type', '', '', 'N', '权限模块-页面类型：外链'),
       (1, '读&写', '0', 'sys_source_type', '', 'primary', 'Y', '读&写'),
       (2, '只读', '1', 'sys_source_type', '', 'success', 'N', '只读'),
       (3, '只写', '2', 'sys_source_type', '', 'warning', 'N', '只写'),
       (1, '子数据源', '0', 'sys_database_type', '', 'success', 'Y', '子数据源'),
       (2, '主数据源', '1', 'sys_database_type', '', 'danger', 'N', '主数据源'),
       (1, '自动配置', '0', 'te_configuration_type', '', '', 'N', '配置类型：自动配置'),
       (2, '手动配置', '1', 'te_configuration_type', '', '', 'N', '配置类型：手动配置'),
       (0, '公共', 'Y', 'sys_common_type', '', '', 'N', '常规-公共私有：公共'),
       (1, '私有', 'N', 'sys_common_type', '', '', 'N', '常规-公共私有：私有'),
       (0, '全部数据权限', '1', 'sys_data_scope', '', '', 'Y', '数据范围：全部数据权限'),
       (1, '自定数据权限', '2', 'sys_data_scope', '', '', 'N', '数据范围：自定数据权限'),
       (2, '本部门数据权限', '3', 'sys_data_scope', '', '', 'N', '数据范围：本部门数据权限'),
       (3, '本部门及以下数据权限', '4', 'sys_data_scope', '', '', 'N', '数据范围：本部门及以下数据权限'),
       (4, '本岗位数据权限', '5', 'sys_data_scope', '', '', 'N', '数据范围：本岗位数据权限'),
       (5, '仅本人数据权限', '6', 'sys_data_scope', '', '', 'N', '数据范围：仅本人数据权限'),
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
  front_package_name        varchar(100)                                                comment '生成前端包路径',
  authority_name            varchar(30)                                                 comment '生成权限名',
  module_name               varchar(30)                                                 comment '生成模块名',
  business_name             varchar(30)                                                 comment '生成业务名',
  function_name             varchar(50)                                                 comment '生成功能名',
  function_author           varchar(50)                                                 comment '生成功能作者',
  gen_type                  char(1)             not null default '0'                    comment '生成代码方式（0zip压缩包 1自定义路径）',
  gen_path                  varchar(200)        default '/'                             comment '生成路径（不填默认项目路径）',
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