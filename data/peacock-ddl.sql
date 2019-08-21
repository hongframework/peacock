
/*Table structure for table `cfg_api_conf` */

CREATE TABLE `cfg_api_conf` (
  `id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `path` varchar(256) NOT NULL COMMENT 'API路径',
  `version` varchar(64) NOT NULL COMMENT 'API版本',
  `prop_key` varchar(64) NOT NULL COMMENT '配置Key',
  `prop_value` text NOT NULL COMMENT '配置Value',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `ctime` int(11) NOT NULL COMMENT '创建时间',
  `mtime` int(11) DEFAULT NULL COMMENT '修改时间',
  `valid_time` int(11) DEFAULT NULL COMMENT '生效时间',
  `invalid_time` int(11) DEFAULT NULL COMMENT '失效时间',
  `state` tinyint(2) NOT NULL COMMENT '状态：0-待生效，1-已生效，-1-已失效',
  `invalid_letime` int(11) DEFAULT NULL COMMENT '失效时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='API配置表';

/*Table structure for table `cfg_api_def` */

CREATE TABLE `cfg_api_def` (
  `id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `path` varchar(256) NOT NULL COMMENT 'API路径',
  `name` varchar(256) NOT NULL COMMENT 'API名称',
  `version` varchar(64) NOT NULL COMMENT 'API版本',
  `prop_key` varchar(64) NOT NULL COMMENT '属性Key',
  `prop_type` varchar(64) NOT NULL COMMENT '属性类型：integer-整数型，string-字符型',
  `prop_options` varchar(512) DEFAULT NULL COMMENT '属性支持选项,通过json存储，例如：{1:''A'',2:''B''}',
  `prop_description` varchar(512) DEFAULT NULL COMMENT '对属性的一些说明，例如: 满足正则表达式：^(d{16}|d{19})$',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `ctime` int(11) NOT NULL COMMENT '创建时间',
  `mtime` int(11) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='API定义表';

/*Table structure for table `cfg_consumer` */

CREATE TABLE `cfg_consumer` (
  `id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `client_id` varchar(64) NOT NULL COMMENT '客户端访问Id',
  `secret_key` varchar(128) NOT NULL COMMENT '客户端访问秘钥',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注，记录消费者信息',
  `ctime` int(11) NOT NULL COMMENT '创建时间',
  `mtime` int(11) DEFAULT NULL COMMENT '修改时间',
  `state` tinyint(2) NOT NULL COMMENT '状态：0-失效，1-有效',
  PRIMARY KEY (`id`),
  UNIQUE KEY `client_id` (`client_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='API消费者';

/*Table structure for table `cfg_consumer_auth` */

CREATE TABLE `cfg_consumer_auth` (
  `id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `client_id` varchar(64) NOT NULL COMMENT '客户端访问Id',
  `auth_path` varchar(256) NOT NULL COMMENT 'API路径，支持*匹配',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `ctime` int(11) NOT NULL COMMENT '创建时间',
  `mtime` int(11) DEFAULT NULL COMMENT '修改时间',
  `state` tinyint(2) NOT NULL COMMENT '状态：0-失效，1-有效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='API消费者授权';

/*Table structure for table `cfg_datasouce_hbase` */

CREATE TABLE `cfg_datasouce_hbase` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `zklist` varchar(256) NOT NULL COMMENT 'ZKLIST',
  `zkport` int(11) NOT NULL COMMENT 'ZKPROT',
  `state` tinyint(2) NOT NULL COMMENT '状态：0-未生效，1-已生效',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `ctime` int(11) NOT NULL COMMENT '创建时间',
  `mtime` int(11) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='HBASE';

/*Table structure for table `cfg_datasouce_mysql` */

CREATE TABLE `cfg_datasouce_mysql` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `host` varchar(256) NOT NULL COMMENT 'HOST',
  `port` int(11) NOT NULL COMMENT 'PROT',
  `database` varchar(256) NOT NULL COMMENT 'DATABASE',
  `username` varchar(256) NOT NULL COMMENT 'USERNAME',
  `password` varchar(256) DEFAULT NULL COMMENT 'PASSWORD',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `state` tinyint(2) NOT NULL COMMENT '状态：0-未生效，1-已生效',
  `ctime` int(11) NOT NULL COMMENT '创建时间',
  `mtime` int(11) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='MYSQL';

/*Table structure for table `cfg_datasouce_redis` */

CREATE TABLE `cfg_datasouce_redis` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `host` varchar(256) NOT NULL COMMENT 'HOST',
  `port` int(11) NOT NULL COMMENT 'PROT',
  `auth` varchar(256) DEFAULT NULL COMMENT 'AUTH',
  `db` varchar(256) NOT NULL COMMENT 'DB',
  `state` tinyint(2) NOT NULL COMMENT '状态：0-未生效，1-已生效',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `ctime` int(11) NOT NULL COMMENT '创建时间',
  `mtime` int(11) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='REDIS';

/*Table structure for table `cfg_feature` */

CREATE TABLE `cfg_feature` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(128) NOT NULL COMMENT '特征名称',
  `TYPE` tinyint(2) DEFAULT NULL COMMENT '特征类型',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `ctime` int(11) NOT NULL COMMENT '创建时间',
  `mtime` int(11) DEFAULT NULL COMMENT '修改时间',
  `state` tinyint(2) NOT NULL COMMENT '状态：0-待生效，1-已生效，-1-已失效',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='特征';

/*Table structure for table `cfg_index` */

CREATE TABLE `cfg_index` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `code` varchar(128) NOT NULL COMMENT '指标编码',
  `name` varchar(128) NOT NULL COMMENT '指标名称',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `features` varchar(64) NOT NULL COMMENT '特征',
  `ctime` int(11) NOT NULL COMMENT '创建时间',
  `mtime` int(11) DEFAULT NULL COMMENT '修改时间',
  `state` tinyint(2) NOT NULL COMMENT '状态：0-待生效，1-已生效，-1-已失效',
  `edit_features` varchar(64) DEFAULT NULL COMMENT '编辑特征',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COMMENT='指标';

/*Table structure for table `cfg_index_hbase` */

CREATE TABLE `cfg_index_hbase` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `index_id` int(11) NOT NULL COMMENT '指标ID',
  `hbase_id` int(11) NOT NULL COMMENT 'HBASE ID',
  `table` varchar(128) DEFAULT NULL COMMENT '表名',
  `family` varchar(128) DEFAULT NULL COMMENT 'FAMILY',
  `qualifier` varchar(128) DEFAULT NULL COMMENT 'QUALIFIER',
  `rowkey_converter` tinyint(2) DEFAULT NULL COMMENT 'ROWKEY CONVERTER',
  `ctime` int(11) NOT NULL COMMENT '创建时间',
  `mtime` int(11) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='HBASE指标';

/*Table structure for table `cfg_index_mysql` */

CREATE TABLE `cfg_index_mysql` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `index_id` int(11) NOT NULL COMMENT '指标ID',
  `mysql_id` int(11) NOT NULL COMMENT 'MYSQL ID',
  `table` varchar(128) DEFAULT NULL COMMENT '表名',
  `primary_key` varchar(128) DEFAULT NULL COMMENT '主键',
  `column` varchar(128) DEFAULT NULL COMMENT '属性列',
  `sql` text COMMENT 'SQL语句',
  `ctime` int(11) NOT NULL COMMENT '创建时间',
  `mtime` int(11) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='MYSQL指标';

/*Table structure for table `cfg_index_redis` */

CREATE TABLE `cfg_index_redis` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `index_id` int(11) NOT NULL COMMENT '指标ID',
  `redis_id` int(11) NOT NULL COMMENT 'REDIS ID',
  `data_type` tinyint(2) DEFAULT NULL COMMENT '数据类型',
  `key_converter` tinyint(2) DEFAULT NULL COMMENT 'KEY CONVERTER',
  `key_parts` varchar(128) DEFAULT NULL COMMENT 'KEY PARTS',
  `method` tinyint(2) DEFAULT NULL COMMENT '判断依据：contain',
  `ctime` int(11) NOT NULL COMMENT '创建时间',
  `mtime` int(11) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='REDIS指标';

/*Table structure for table `cfg_mgr_module` */

CREATE TABLE `cfg_mgr_module` (
  `id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '模块id,ID',
  `program_id` bigint(12) NOT NULL COMMENT '项目id,ID',
  `code` varchar(64) NOT NULL COMMENT '模块编码,编码',
  `name` varchar(128) NOT NULL COMMENT '模块名称,名称',
  `description` varchar(128) DEFAULT NULL COMMENT '模块描述,描述',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `creator_id` bigint(12) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modifier_id` bigint(12) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `type` tinyint(4) DEFAULT NULL COMMENT '模块类型,类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COMMENT='模块';

/*Table structure for table `cfg_mgr_program` */

CREATE TABLE `cfg_mgr_program` (
  `id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '项目id,ID',
  `code` varchar(64) NOT NULL COMMENT '项目编码,编码',
  `name` varchar(128) NOT NULL COMMENT '项目名称,名称',
  `description` varchar(128) DEFAULT NULL COMMENT '项目描述,描述',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `creator_id` bigint(12) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modifier_id` bigint(12) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='项目';

/*Table structure for table `cfg_mgr_version` */

CREATE TABLE `cfg_mgr_version` (
  `id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '版本id,ID',
  `code` varchar(64) NOT NULL COMMENT '版本编码,编码',
  `description` varchar(128) DEFAULT NULL COMMENT '版本描述,描述',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `creator_id` bigint(12) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modifier_id` bigint(12) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `program_id` bigint(12) NOT NULL COMMENT '项目id,ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='版本';

/*Table structure for table `cfg_node` */

CREATE TABLE `cfg_node` (
  `id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '节点id,ID',
  `code` varchar(64) NOT NULL COMMENT '节点编码,编码',
  `name` varchar(128) NOT NULL COMMENT '节点名称,名称',
  `type` tinyint(4) DEFAULT NULL COMMENT '节点类型,类型',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `program_id` varchar(128) DEFAULT NULL COMMENT '项目id,ID',
  `creator_id` bigint(12) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modifier_id` bigint(12) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `domain` varchar(128) DEFAULT NULL COMMENT '节点访问地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='节点';

/*Table structure for table `cfg_runtime_api` */

CREATE TABLE `cfg_runtime_api` (
  `id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `module` varchar(256) DEFAULT NULL COMMENT 'API归属模块',
  `name` varchar(256) NOT NULL COMMENT 'API名称',
  `title` varchar(256) NOT NULL COMMENT 'API标题',
  `description` varchar(256) DEFAULT NULL COMMENT 'API描述',
  `path` varchar(256) DEFAULT NULL COMMENT 'API路径',
  `version` varchar(64) DEFAULT NULL COMMENT 'API版本',
  `content` text COMMENT '配置内容',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `ctime` int(11) NOT NULL COMMENT '创建时间',
  `mtime` int(11) DEFAULT NULL COMMENT '修改时间',
  `valid_time` int(11) DEFAULT NULL COMMENT '生效时间',
  `invalid_time` int(11) DEFAULT NULL COMMENT '失效时间',
  `state` tinyint(2) NOT NULL DEFAULT '0' COMMENT '状态：0-开发中,1-测试中,2-运行中,3-已过时,4-已失效',
  `module_id` bigint(12) NOT NULL COMMENT '模块id,ID',
  `program_id` bigint(12) NOT NULL COMMENT '项目id,ID',
  `tags` varchar(128) DEFAULT NULL COMMENT '标签',
  `version_id` bigint(12) DEFAULT NULL COMMENT '版本id,ID',
  `examples` varchar(1024) DEFAULT NULL COMMENT '请求样例',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 COMMENT='API动态配置表';

/*Table structure for table `cfg_runtime_dictionary` */

CREATE TABLE `cfg_runtime_dictionary` (
  `id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '字典id,ID',
  `program_id` bigint(12) NOT NULL COMMENT '项目id,ID',
  `code` varchar(64) NOT NULL COMMENT '字典编码,编码',
  `name` varchar(128) NOT NULL COMMENT '字典名称,名称',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `creator_id` bigint(12) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modifier_id` bigint(12) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='API字典';

/*Table structure for table `cfg_runtime_dictionary_items` */

CREATE TABLE `cfg_runtime_dictionary_items` (
  `id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '字典项id,ID',
  `dictionary_id` bigint(12) NOT NULL COMMENT '字典id,ID',
  `code` varchar(64) NOT NULL COMMENT '字典项编码,编码',
  `name` varchar(128) NOT NULL COMMENT '字典项名称,名称',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `creator_id` bigint(12) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modifier_id` bigint(12) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='API字典项';

/*Table structure for table `cfg_runtime_handler` */

CREATE TABLE `cfg_runtime_handler` (
  `id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `module` varchar(256) DEFAULT NULL COMMENT 'API归属模块',
  `name` varchar(256) NOT NULL COMMENT 'API名称',
  `title` varchar(256) NOT NULL COMMENT 'API标题',
  `description` varchar(256) DEFAULT NULL COMMENT 'API描述',
  `path` varchar(256) DEFAULT NULL COMMENT 'API路径',
  `version` varchar(64) NOT NULL COMMENT 'API版本',
  `content` text COMMENT '配置内容',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `ctime` int(11) NOT NULL COMMENT '创建时间',
  `mtime` int(11) DEFAULT NULL COMMENT '修改时间',
  `valid_time` int(11) DEFAULT NULL COMMENT '生效时间',
  `invalid_time` int(11) DEFAULT NULL COMMENT '失效时间',
  `state` tinyint(2) NOT NULL DEFAULT '0' COMMENT '状态：0-开发中,1-测试中,2-运行中,3-已过时,4-已失效',
  `module_id` bigint(12) NOT NULL COMMENT '模块id,ID',
  `program_id` bigint(12) NOT NULL COMMENT '项目id,ID',
  `tags` varchar(128) DEFAULT NULL COMMENT '标签',
  `version_id` bigint(12) DEFAULT NULL COMMENT '版本id,ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=93 DEFAULT CHARSET=utf8 COMMENT='动态Handler';

/*Table structure for table `cfg_runtime_parameter` */

CREATE TABLE `cfg_runtime_parameter` (
  `id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '参数id,ID',
  `program_id` bigint(12) NOT NULL COMMENT '项目id,ID',
  `name` varchar(128) NOT NULL COMMENT '参数名称,名称',
  `type` varchar(32) DEFAULT NULL COMMENT '参数类型,类型',
  `description` varchar(128) DEFAULT NULL COMMENT '参数描述,描述',
  `default_val` varchar(128) DEFAULT NULL COMMENT '默认值',
  `min_val` bigint(20) DEFAULT NULL COMMENT '最小值',
  `max_val` bigint(20) DEFAULT NULL COMMENT '最大值',
  `required` tinyint(4) DEFAULT NULL COMMENT '是否必须',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `creator_id` bigint(12) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modifier_id` bigint(12) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `expanders` varchar(128) NOT NULL COMMENT '扩展器id,ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='参数';

/*Table structure for table `cfg_runtime_response` */

CREATE TABLE `cfg_runtime_response` (
  `id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '参数id,ID',
  `program_id` bigint(12) NOT NULL COMMENT '项目id,ID',
  `code` varchar(64) NOT NULL COMMENT '参数编码,编码',
  `name` varchar(128) NOT NULL COMMENT '参数名称,名称',
  `value` varchar(128) DEFAULT NULL COMMENT '参数取值',
  `type` tinyint(4) DEFAULT NULL COMMENT '参数类型,类型',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `creator_id` bigint(12) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modifier_id` bigint(12) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `description` varchar(128) DEFAULT NULL COMMENT '参数描述,描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='响应参数';

/*Table structure for table `cfg_runtime_rule` */

CREATE TABLE `cfg_runtime_rule` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `code` varchar(128) NOT NULL COMMENT '规则编码',
  `name` varchar(128) NOT NULL COMMENT '规则名称',
  `version` varchar(128) NOT NULL COMMENT '版本',
  `expresssion` text COMMENT '表达式',
  `description` varchar(256) DEFAULT NULL COMMENT '描述',
  `return_type` tinyint(2) DEFAULT NULL COMMENT '返回类型',
  `features` int(11) NOT NULL COMMENT '特征',
  `ctime` int(11) NOT NULL COMMENT '创建时间',
  `mtime` int(11) DEFAULT NULL COMMENT '修改时间',
  `state` tinyint(2) NOT NULL COMMENT '状态：0-待生效，1-已生效，-1-已失效',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='动态规则';

/*Table structure for table `cfg_runtime_trace` */

CREATE TABLE `cfg_runtime_trace` (
  `id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '跟踪配置id,ID',
  `program_id` bigint(12) NOT NULL COMMENT '项目id,ID',
  `node_id` varchar(12) NOT NULL COMMENT '节点id,ID',
  `name` varchar(128) NOT NULL COMMENT '配置名称,名称',
  `content` text COMMENT '配置内容',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `creator_id` bigint(12) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modifier_id` bigint(12) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='跟踪配置';

/*Table structure for table `cfg_static_expander` */

CREATE TABLE `cfg_static_expander` (
  `id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '扩展器id,ID',
  `type` varchar(64) DEFAULT NULL COMMENT '扩展器类型,类型',
  `name` varchar(128) NOT NULL COMMENT '扩展器名称,名称',
  `trigger_data_type` varchar(64) DEFAULT NULL COMMENT '触发数据类型',
  `expander_class` varchar(256) NOT NULL COMMENT '扩展器CLASS',
  `description` varchar(128) DEFAULT NULL COMMENT '静态扩展器描述,描述',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `creator_id` bigint(12) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modifier_id` bigint(12) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COMMENT='静态扩展器';

/*Table structure for table `cfg_static_expander_parameter` */

CREATE TABLE `cfg_static_expander_parameter` (
  `id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '参数id,ID',
  `name` varchar(128) NOT NULL COMMENT '参数名称,名称',
  `value` varchar(128) NOT NULL COMMENT '参数取值',
  `description` varchar(128) DEFAULT NULL COMMENT '静态扩展器参数描述,描述',
  `expander_id` bigint(12) NOT NULL COMMENT '扩展器id,ID',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `creator_id` bigint(12) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modifier_id` bigint(12) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8 COMMENT='静态扩展器参数';

/*Table structure for table `cfg_test_case` */

CREATE TABLE `cfg_test_case` (
  `id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(128) NOT NULL COMMENT '名称',
  `path` varchar(128) NOT NULL COMMENT '访问路径',
  `parameter_str` varchar(512) DEFAULT NULL COMMENT '访问参数',
  `request_body` varchar(1024) DEFAULT NULL COMMENT '请求报文',
  `response_body` text COMMENT '响应报文',
  `method` varchar(32) NOT NULL COMMENT '请求方式',
  `is_store` tinyint(4) DEFAULT NULL COMMENT '是否收藏',
  `is_pub` tinyint(4) DEFAULT NULL COMMENT '是否公开',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `creator_id` bigint(12) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modifier_id` bigint(12) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `description` varchar(128) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=utf8 COMMENT='测试CASE';

/*Table structure for table `dictionary` */

CREATE TABLE `dictionary` (
  `dictionary_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典ID',
  `dictionary_name` varchar(32) DEFAULT NULL COMMENT '字典名称',
  `dictionary_code` varchar(64) DEFAULT NULL COMMENT '字典编码',
  `dictionary_desc` varchar(128) DEFAULT NULL COMMENT '字典描述',
  `ext1` varchar(128) DEFAULT NULL COMMENT '扩展字段1',
  `ext2` varchar(128) DEFAULT NULL COMMENT '扩展字段2',
  `op_id` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_op_id` bigint(20) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` int(2) DEFAULT NULL COMMENT '删除标识',
  PRIMARY KEY (`dictionary_id`)
) ENGINE=InnoDB AUTO_INCREMENT=170 DEFAULT CHARSET=utf8 COMMENT='字典';

/*Table structure for table `dictionary_item` */

CREATE TABLE `dictionary_item` (
  `dictionary_item_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典项ID',
  `value` varchar(32) DEFAULT NULL COMMENT '字典项值',
  `text` varchar(32) DEFAULT NULL COMMENT '字典项文本',
  `desc` varchar(128) DEFAULT NULL COMMENT '字典项描述',
  `is_default` int(2) DEFAULT NULL COMMENT '是否默认',
  `pri` decimal(4,2) DEFAULT NULL COMMENT '优先级',
  `ext1` varchar(128) DEFAULT NULL COMMENT '扩展字段1',
  `ext2` varchar(128) DEFAULT NULL COMMENT '扩展字段2',
  `dictionary_id` bigint(20) DEFAULT NULL COMMENT '字典ID',
  `dictionary_code` varchar(32) DEFAULT NULL,
  `op_id` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_op_id` bigint(20) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` int(2) DEFAULT NULL COMMENT '删除标识',
  PRIMARY KEY (`dictionary_item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=132 DEFAULT CHARSET=utf8 COMMENT='字典项';

/*Table structure for table `menu` */

CREATE TABLE `menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_code` varchar(64) DEFAULT NULL COMMENT '菜单编码',
  `menu_name` varchar(128) DEFAULT NULL COMMENT '菜单名称',
  `menu_desc` varchar(128) DEFAULT NULL COMMENT '菜单描述',
  `menu_level` int(2) DEFAULT NULL COMMENT '菜单级别',
  `icon` varchar(64) DEFAULT NULL COMMENT '图标',
  `url` varchar(128) DEFAULT NULL COMMENT '地址',
  `parent_menu_id` bigint(20) DEFAULT NULL COMMENT '父级菜单ID',
  `pri` decimal(4,2) DEFAULT NULL COMMENT '优先级',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modifier_id` bigint(20) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` int(2) DEFAULT NULL COMMENT '删除标识',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8 COMMENT='菜单';

/*Table structure for table `organize` */

CREATE TABLE `organize` (
  `organize_id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '组织id',
  `organize_code` varchar(64) DEFAULT NULL COMMENT '组织编码',
  `organize_name` varchar(128) DEFAULT NULL COMMENT '组织名称',
  `organize_type` tinyint(4) DEFAULT NULL COMMENT '组织类型',
  `organize_level` tinyint(4) DEFAULT NULL COMMENT '组织级别',
  `parent_organize_id` bigint(12) DEFAULT NULL COMMENT '上级组织id',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `creator_id` bigint(12) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modifier_id` bigint(12) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`organize_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='组织';

/*Table structure for table `role` */

CREATE TABLE `role` (
  `role_id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_code` varchar(64) DEFAULT NULL COMMENT '角色编码',
  `role_name` varchar(128) DEFAULT NULL COMMENT '角色名称',
  `role_type` tinyint(4) DEFAULT NULL COMMENT '角色类型',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `creator_id` bigint(12) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modifier_id` bigint(12) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='角色';

/*Table structure for table `role_authorize` */

CREATE TABLE `role_authorize` (
  `role_authorize_id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '角色授权id',
  `role_authorize_type` tinyint(4) DEFAULT NULL COMMENT '角色授权类型',
  `role_id` bigint(12) DEFAULT NULL COMMENT '角色id',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单ID',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `creator_id` bigint(12) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modifier_id` bigint(12) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`role_authorize_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色授权';

/*Table structure for table `third_api` */

CREATE TABLE `third_api` (
  `id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT 'API ID,ID',
  `domain_id` bigint(12) NOT NULL COMMENT '访问域id,ID',
  `api_type` tinyint(4) NOT NULL COMMENT 'api类型,类型',
  `name` varchar(128) NOT NULL COMMENT 'api名称,名称',
  `path` varchar(128) NOT NULL COMMENT '请求路径,编码',
  `method` varchar(64) NOT NULL COMMENT '请求方式',
  `tags` varchar(128) DEFAULT NULL COMMENT '标签',
  `request_type` varchar(64) NOT NULL COMMENT '请求类型,类型',
  `response_type` varchar(64) NOT NULL COMMENT '响应类型',
  `content` text COMMENT '配置报文,',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `creator_id` bigint(12) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modifier_id` bigint(12) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='访问API';

/*Table structure for table `third_common_parameter` */

CREATE TABLE `third_common_parameter` (
  `id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '参数id,ID',
  `domain_id` bigint(12) NOT NULL COMMENT '访问域id,ID',
  `name` varchar(128) NOT NULL COMMENT '参数名称,名称',
  `path` varchar(64) NOT NULL COMMENT '参数路径,编码',
  `type` tinyint(4) DEFAULT NULL COMMENT '参数类型,类型',
  `required` tinyint(4) DEFAULT NULL COMMENT '是否必填',
  `check_rule` varchar(128) DEFAULT NULL COMMENT '检查规则',
  `value` varchar(128) DEFAULT NULL COMMENT '参数取值',
  `creator_id` bigint(12) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modifier_id` bigint(12) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='域通用参数';

/*Table structure for table `third_domain` */

CREATE TABLE `third_domain` (
  `id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '访问域id,ID',
  `name` varchar(128) NOT NULL COMMENT '域名称,名称',
  `url` varchar(512) NOT NULL COMMENT 'URL',
  `description` varchar(128) DEFAULT NULL COMMENT '域描述,描述',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `creator_id` bigint(12) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modifier_id` bigint(12) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `protocol` varchar(64) DEFAULT NULL COMMENT '协议',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='访问域';

/*Table structure for table `third_domain_parameter` */

CREATE TABLE `third_domain_parameter` (
  `id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '访问域参数id,ID',
  `domain_id` bigint(12) NOT NULL COMMENT '访问域id,ID',
  `name` varchar(128) NOT NULL COMMENT '访问域参数名称,名称',
  `code` varchar(64) NOT NULL COMMENT '访问域参数编码,编码',
  `value` varchar(256) NOT NULL COMMENT '访问域参数取值,取值',
  `visiable` tinyint(4) NOT NULL COMMENT '是否可见',
  `creator_id` bigint(12) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modifier_id` bigint(12) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='访问域参数';

/*Table structure for table `third_help_parameter` */

CREATE TABLE `third_help_parameter` (
  `id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '访问域常规参数id,ID',
  `name` varchar(128) NOT NULL COMMENT '访问域常规参数名称,名称',
  `code` varchar(64) NOT NULL COMMENT '访问域常规参数编码,编码',
  `value` varchar(256) NOT NULL COMMENT '访问域常规参数取值,取值',
  `visiable` tinyint(4) NOT NULL COMMENT '是否可见',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `creator_id` bigint(12) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modifier_id` bigint(12) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='访问域常规参数';

/*Table structure for table `third_pub_request` */

CREATE TABLE `third_pub_request` (
  `id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '参数id,ID',
  `domain_id` bigint(12) NOT NULL COMMENT '访问域id,ID',
  `name` varchar(128) NOT NULL COMMENT '参数名称,名称',
  `path` varchar(64) NOT NULL COMMENT '参数路径,编码',
  `type` tinyint(4) DEFAULT NULL COMMENT '参数类型,类型',
  `required` tinyint(4) DEFAULT NULL COMMENT '是否必填',
  `check_rule` varchar(128) DEFAULT NULL COMMENT '检查规则',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creator_id` bigint(12) DEFAULT NULL COMMENT '创建人',
  `modifier_id` bigint(12) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `value` varchar(128) DEFAULT NULL COMMENT '参数取值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COMMENT='公共请求参数';

/*Table structure for table `third_pub_response` */

CREATE TABLE `third_pub_response` (
  `id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '公共响应参数id,ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creator_id` bigint(12) DEFAULT NULL COMMENT '创建人',
  `domain_id` bigint(12) NOT NULL COMMENT '访问域id,ID',
  `modifier_id` bigint(12) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `name` varchar(128) NOT NULL COMMENT '参数名称,名称',
  `path` varchar(64) NOT NULL COMMENT '参数路径,编码',
  `type` tinyint(4) DEFAULT NULL COMMENT '参数类型,类型',
  `value` varchar(128) DEFAULT NULL COMMENT '参数取值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='公共响应参数';

/*Table structure for table `third_public_rule` */

CREATE TABLE `third_public_rule` (
  `id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '规则id,ID',
  `domain_id` bigint(12) NOT NULL COMMENT '访问域id,ID',
  `name` varchar(128) NOT NULL COMMENT '规则名称,名称',
  `code` varchar(64) NOT NULL COMMENT '规则编码,编码',
  `expression` varchar(128) NOT NULL COMMENT '规则表达式,描述',
  `creator_id` bigint(12) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modifier_id` bigint(12) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='公共规则';

/*Table structure for table `user` */

CREATE TABLE `user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_name` varchar(64) DEFAULT NULL COMMENT '用户名称',
  `account` varchar(64) DEFAULT NULL COMMENT '用户账号',
  `password` varchar(128) DEFAULT NULL COMMENT '用户密码',
  `gender` int(2) DEFAULT NULL COMMENT '性别',
  `mobile` varchar(6) DEFAULT NULL COMMENT '手机号',
  `email` int(2) DEFAULT NULL COMMENT '邮箱',
  `addr` int(2) DEFAULT NULL COMMENT '地址',
  `avatar` varchar(512) DEFAULT NULL COMMENT '头像',
  `last_login_time` datetime DEFAULT NULL COMMENT '上次登录时间',
  `status` int(2) DEFAULT NULL COMMENT '状态',
  `organize_id` bigint(20) DEFAULT NULL COMMENT '组织ID',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modifier_id` bigint(20) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` int(2) DEFAULT NULL COMMENT '删除标识',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户';

/*Table structure for table `user_authorize` */

CREATE TABLE `user_authorize` (
  `user_authorize_id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '用户授权id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `organize_id` bigint(12) DEFAULT NULL COMMENT '组织id',
  `role_id` bigint(12) DEFAULT NULL COMMENT '角色id',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `creator_id` bigint(12) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modifier_id` bigint(12) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`user_authorize_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户授权';
