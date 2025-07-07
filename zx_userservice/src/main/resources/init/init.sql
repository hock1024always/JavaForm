CREATE TABLE `user`
(
    id             VARCHAR(255) NOT NULL COMMENT '主键',
    username       VARCHAR(255) NOT NULL COMMENT '用户的登录名称',
    password       VARCHAR(255) NOT NULL COMMENT '用户登录密码，存储时加盐加密',
    email          VARCHAR(255)          DEFAULT NULL COMMENT '用户的电子邮箱地址',
    phone          VARCHAR(255)          DEFAULT NULL COMMENT '用户的手机号码',
    bio            VARCHAR(255)          DEFAULT NULL COMMENT '用户的个人简介',
    institution_id VARCHAR(255) NOT NULL COMMENT '所属单位的ID',
    description    VARCHAR(255)          DEFAULT NULL COMMENT '对记录的描述或说明',
    status         VARCHAR(255) NOT NULL COMMENT '记录的状态',
    create_by      VARCHAR(255)          COMMENT '创建记录的用户ID',
    create_time    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    update_by      VARCHAR(255)          COMMENT '最后更新记录的用户ID',
    update_time    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后更新时间',
    extension      JSON                  DEFAULT NULL COMMENT '扩展字段',
    PRIMARY KEY (id)
);


CREATE TABLE `group`
(
    id             VARCHAR(255) NOT NULL COMMENT '主键',
    group_name     VARCHAR(255) NOT NULL COMMENT '分组名称',
    institution_id VARCHAR(255) NOT NULL COMMENT '所属单位的ID',
    description    VARCHAR(255)          DEFAULT NULL COMMENT '对记录的描述或说明',
    status         VARCHAR(255) NOT NULL COMMENT '记录的状态',
    create_by      VARCHAR(255)  COMMENT '创建记录的用户ID',
    create_time    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    update_by      VARCHAR(255)  COMMENT '最后更新记录的用户ID',
    update_time    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后更新时间',
    extension      JSON                  DEFAULT NULL COMMENT '扩展字段',
    PRIMARY KEY (id)
);

CREATE TABLE group_and_user
(
    id             VARCHAR(255) NOT NULL COMMENT '主键',
    user_id        VARCHAR(255) NOT NULL COMMENT '用户ID',
    group_id       VARCHAR(255) NOT NULL COMMENT '分组ID',
    institution_id VARCHAR(255) NOT NULL COMMENT '所属单位的ID',
    relation_name  VARCHAR(255) NOT NULL COMMENT '描述关系的名称',
    description    VARCHAR(255)          DEFAULT NULL COMMENT '对记录的描述或说明',
    status         VARCHAR(255) NOT NULL COMMENT '记录的状态',
    create_by      VARCHAR(255)  COMMENT '创建记录的用户ID',
    create_time    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    update_by      VARCHAR(255)  COMMENT '最后更新记录的用户ID',
    update_time    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后更新时间',
    extension      JSON                  DEFAULT NULL COMMENT '扩展字段',
    PRIMARY KEY (id)
);


CREATE TABLE group_and_group
(
    id              VARCHAR(255) NOT NULL COMMENT '主键',
    parent_group_id VARCHAR(255) NOT NULL COMMENT '父分组ID',
    child_group_id  VARCHAR(255) NOT NULL COMMENT '子分组ID',
    tier            INT          NOT NULL COMMENT '层级',
    institution_id  VARCHAR(255) NOT NULL COMMENT '所属单位的ID',
    relation_name   VARCHAR(255) NOT NULL COMMENT '描述关系的名称',
    description     VARCHAR(255)          DEFAULT NULL COMMENT '对记录的描述或说明',
    status          VARCHAR(255) NOT NULL COMMENT '记录的状态',
    create_by       VARCHAR(255)  COMMENT '创建记录的用户ID',
    create_time     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    update_by       VARCHAR(255)  COMMENT '最后更新记录的用户ID',
    update_time     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后更新时间',
    extension       JSON                  DEFAULT NULL COMMENT '扩展字段',
    PRIMARY KEY (id)
);

CREATE TABLE permission
(
    id              VARCHAR(255) NOT NULL COMMENT '主键',
    permission_type VARCHAR(50)  NOT NULL COMMENT '权限类型名称',
    institution_id  VARCHAR(255) NOT NULL COMMENT '所属单位的ID',
    description     VARCHAR(255)          DEFAULT NULL COMMENT '对记录的描述或说明',
    status          VARCHAR(255) NOT NULL COMMENT '记录的状态',
    create_by       VARCHAR(255)  COMMENT '创建记录的用户ID',
    create_time     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    update_by       VARCHAR(255)  COMMENT '最后更新记录的用户ID',
    update_time     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后更新时间',
    extension       JSON                  DEFAULT NULL COMMENT '扩展字段',
    PRIMARY KEY (id)
);

CREATE TABLE permission_and_user
(
    id             VARCHAR(255) NOT NULL COMMENT '主键',
    user_id        INT          NOT NULL COMMENT '用户唯一标识',
    permission_id  INT          NOT NULL COMMENT '权限唯一标识',
    institution_id VARCHAR(255) NOT NULL COMMENT '所属单位的ID',
    relation_name  VARCHAR(255) NOT NULL COMMENT '描述关系的名称',
    description    VARCHAR(255)          DEFAULT NULL COMMENT '对记录的描述或说明',
    status         VARCHAR(255) NOT NULL COMMENT '记录的状态',
    create_by      VARCHAR(255)  COMMENT '创建记录的用户ID',
    create_time    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    update_by      VARCHAR(255) COMMENT '最后更新记录的用户ID',
    update_time    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后更新时间',
    extension      JSON                  DEFAULT NULL COMMENT '扩展字段',
    PRIMARY KEY (id)
);

CREATE TABLE permission_and_group
(
    id             VARCHAR(255) NOT NULL COMMENT '主键',
    group_id       INT          NOT NULL COMMENT '分组唯一标识',
    permission_id  INT          NOT NULL COMMENT '权限唯一标识',
    institution_id VARCHAR(255) NOT NULL COMMENT '所属单位的ID',
    relation_name  VARCHAR(255) NOT NULL COMMENT '描述关系的名称',
    description    VARCHAR(255)          DEFAULT NULL COMMENT '对记录的描述或说明',
    status         VARCHAR(255) NOT NULL COMMENT '记录的状态',
    create_by      VARCHAR(255)  COMMENT '创建记录的用户ID',
    create_time    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    update_by      VARCHAR(255)  COMMENT '最后更新记录的用户ID',
    update_time    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后更新时间',
    extension      JSON                  DEFAULT NULL COMMENT '扩展字段',
    PRIMARY KEY (id)
);