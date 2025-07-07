CREATE TABLE carrier_template
(
    config_info    JSON NULL COMMENT '载体配置模板',
    name           VARCHAR(255) NOT NULL COMMENT '载体配置模板名称',
    ##
                   id VARCHAR(255) NOT NULL PRIMARY KEY COMMENT '主键',
    institution_id VARCHAR(255) default '01' COMMENT '所属单位的ID',
    description    VARCHAR(255) NULL COMMENT '对记录的描述或说明',
    status         VARCHAR(255) default '0' COMMENT '记录的状态',
    create_by      VARCHAR(255) default '01' COMMENT '创建记录的用户ID',
    create_time    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    update_by      VARCHAR(255) default '01' COMMENT '最后更新记录的用户ID',
    update_time    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后更新时间',
    extension      JSON NULL COMMENT '扩展字段'
);

CREATE TABLE carrier
(
    carrier_template_id VARCHAR(255) NOT NULL COMMENT '载体模板id',
    config_info         JSON NULL COMMENT '载体配置信息',
    name                VARCHAR(255) NOT NULL COMMENT '载体名称',
    ##
                        id VARCHAR(255) NOT NULL PRIMARY KEY COMMENT '主键',
    institution_id      VARCHAR(255) default '01' COMMENT '所属单位的ID',
    description         VARCHAR(255) NULL COMMENT '对记录的描述或说明',
    status              VARCHAR(255) default '0' COMMENT '记录的状态',
    create_by           VARCHAR(255) default '01' COMMENT '创建记录的用户ID',
    create_time         TIMESTAMP    DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    update_by           VARCHAR(255) default '01' COMMENT '最后更新记录的用户ID',
    update_time         TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后更新时间',
    extension           JSON NULL COMMENT '扩展字段'
);

CREATE TABLE template
(
    type           varchar(255) default 'notification' COMMENT '消息类型, eg: 通知、提醒、请求、反馈',
    content        TEXT         NOT NULL COMMENT '模板内容，通过${sample}$标注替换内容',
    callback_url   VARCHAR(255) NULL COMMENT '回调数据地址',
    name           VARCHAR(255) NOT NULL COMMENT '模板名称',
    ##
                   id VARCHAR(255) NOT NULL PRIMARY KEY COMMENT '主键',
    institution_id VARCHAR(255) default '01' COMMENT '所属单位的ID',
    description    VARCHAR(255) NULL COMMENT '对记录的描述或说明',
    status         VARCHAR(255) default '0' COMMENT '记录的状态',
    create_by      VARCHAR(255) default '01' COMMENT '创建记录的用户ID',
    create_time    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    update_by      VARCHAR(255) default '01' COMMENT '最后更新记录的用户ID',
    update_time    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后更新时间',
    extension      JSON NULL COMMENT '扩展字段'
);

CREATE TABLE strategy
(
    cron           VARCHAR(255) NULL COMMENT 'cron表达式',
    #              receiver JSON NOT NULL COMMENT '接受方，可能是群发使用JSON',
    type           varchar(255) default 'immediately-single' COMMENT '策略类型 {即时，周期，定时} * {单发，群发}',
    ##
                   id VARCHAR(255) NOT NULL PRIMARY KEY COMMENT '主键',
    institution_id VARCHAR(255) default '01' COMMENT '所属单位的ID',
    description    VARCHAR(255) NULL COMMENT '对记录的描述或说明',
    status         VARCHAR(255) default '0' COMMENT '记录的状态',
    create_by      VARCHAR(255) default '01' COMMENT '创建记录的用户ID',
    create_time    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    update_by      VARCHAR(255) default '01' COMMENT '最后更新记录的用户ID',
    update_time    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后更新时间',
    extension      JSON NULL COMMENT '扩展字段'
);

CREATE TABLE message
(
    carrier_id     VARCHAR(255) NULL COMMENT '载体id',
    template_id    VARCHAR(255) NULL COMMENT '模板id',
    template_fill  JSON NULL COMMENT '模板填充内容，回调的数据等',
    strategy_name  VARCHAR(255) NOT NULL COMMENT '策略名称',
    receiver       json         NOT NULL COMMENT '接受方，可能是群发',
    send_time      BIGINT       NOT NULL COMMENT '发送时间',
    ##
    id VARCHAR(255) NOT NULL PRIMARY KEY COMMENT '主键',
    institution_id VARCHAR(255) default '01' COMMENT '所属单位的ID',
    description    VARCHAR(255) NULL COMMENT '对记录的描述或说明',
    status         VARCHAR(255) default '0' COMMENT '记录的状态',
    create_by      VARCHAR(255) default '01' COMMENT '创建记录的用户ID',
    create_time    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    update_by      VARCHAR(255) default '01' COMMENT '最后更新记录的用户ID',
    update_time    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后更新时间',
    extension      JSON NULL COMMENT '扩展字段'
);