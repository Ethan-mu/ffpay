-- Create table
create table T_MCH_INFO
(
  MCHID      VARCHAR2(30) not null,
  NAME       VARCHAR2(30) not null,
  TYPE       VARCHAR2(24) not null,
  REQKEY     VARCHAR2(128) not null,
  RESKEY     VARCHAR2(128) not null,
  STATE      NUMBER not null,
  CREATETIME DATE not null,
  UPDATETIME DATE not null
)
tablespace FFPAY_TEST
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table
comment on table T_MCH_INFO
  is '商户信息表';
-- Add comments to the columns
comment on column T_MCH_INFO.MCHID
  is '商户ID';
comment on column T_MCH_INFO.NAME
  is '名称';
comment on column T_MCH_INFO.TYPE
  is '类型';
comment on column T_MCH_INFO.REQKEY
  is '请求私钥';
comment on column T_MCH_INFO.RESKEY
  is '响应私钥';
comment on column T_MCH_INFO.STATE
  is '商户状态,0-停止使用,1-使用中';
comment on column T_MCH_INFO.CREATETIME
  is '创建时间';
comment on column T_MCH_INFO.UPDATETIME
  is '更新时间';
-- Create/Recreate primary, unique and foreign key constraints
alter table T_MCH_INFO
  add constraint T_MCH_INFO_PK primary key (MCHID)
  using index
  tablespace FFPAY
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );


-- Create table
create table T_PAY_CHANNEL
(
  ID           VARCHAR2(24) not null,
  CHANNELID    VARCHAR2(24) not null,
  CHANNELNAME  VARCHAR2(30) not null,
  CHANNELMCHID VARCHAR2(32) not null,
  MCHID        VARCHAR2(30) not null,
  STATE        NUMBER(4) not null,
  PARAM        VARCHAR2(4000) not null,
  REMARK       VARCHAR2(240),
  CREATETIME   DATE not null,
  UPDATETIME   DATE not null
)
tablespace ffpay_test
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table
comment on table T_PAY_CHANNEL
  is '支付渠道表';
-- Add comments to the columns
comment on column T_PAY_CHANNEL.ID
  is '渠道主键ID';
comment on column T_PAY_CHANNEL.CHANNELID
  is '渠道ID';
comment on column T_PAY_CHANNEL.CHANNELNAME
  is '渠道名称,如:alipay,wechat';
comment on column T_PAY_CHANNEL.CHANNELMCHID
  is '渠道商户ID';
comment on column T_PAY_CHANNEL.MCHID
  is '商户ID';
comment on column T_PAY_CHANNEL.STATE
  is '渠道状态,0-停止使用,1-使用中';
comment on column T_PAY_CHANNEL.PARAM
  is '配置参数,json字符串';
comment on column T_PAY_CHANNEL.REMARK
  is '备注';
comment on column T_PAY_CHANNEL.CREATETIME
  is '创建时间';
comment on column T_PAY_CHANNEL.UPDATETIME
  is '更新时间';
-- Create/Recreate primary, unique and foreign key constraints
alter table T_PAY_CHANNEL
  add constraint T_PAY_CHANNEL_PK primary key (ID)
  using index
  tablespace ffpay_test
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );



-- Create table
create table T_PAY_LOG
(
  OUT_TRADE_NO   VARCHAR2(50) not null,
  CREATE_TIME    DATE,
  PAY_TIME       DATE,
  TOTAL_FEE      NUMBER,
  USER_ID        VARCHAR2(50),
  TRANSACTION_ID VARCHAR2(30),
  TRADE_STATE    VARCHAR2(240),
  ORDER_LIST     VARCHAR2(200),
  PAY_TYPE       VARCHAR2(240),
  REQUEST_PARAM  VARCHAR2(2000),
  REQUEST_URL    VARCHAR2(240),
  RESPONSE_PARAM VARCHAR2(2000),
  REQUEST_TIME   DATE,
  REPONSE_TIME   DATE
)
tablespace ffpay_test
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Add comments to the columns
comment on column T_PAY_LOG.OUT_TRADE_NO
  is '支付订单号';
comment on column T_PAY_LOG.CREATE_TIME
  is '创建日期';
comment on column T_PAY_LOG.PAY_TIME
  is '支付完成时间';
comment on column T_PAY_LOG.TOTAL_FEE
  is '支付金额（分）';
comment on column T_PAY_LOG.USER_ID
  is '用户ID';
comment on column T_PAY_LOG.TRANSACTION_ID
  is '交易号码';
comment on column T_PAY_LOG.TRADE_STATE
  is '交易状态';
comment on column T_PAY_LOG.ORDER_LIST
  is '订单编号列表';
comment on column T_PAY_LOG.PAY_TYPE
  is '支付类型';
comment on column T_PAY_LOG.REQUEST_PARAM
  is '请求参数';
comment on column T_PAY_LOG.REQUEST_URL
  is '请求url';
comment on column T_PAY_LOG.RESPONSE_PARAM
  is '响应参数';
comment on column T_PAY_LOG.REQUEST_TIME
  is '请求时间';
comment on column T_PAY_LOG.REPONSE_TIME
  is '响应时间';
-- Create/Recreate primary, unique and foreign key constraints

alter table T_PAY_LOG
  add constraint T_PAY_LOG_PK primary key (OUT_TRADE_NO)
  using index
  tablespace ffpay_test
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );




-- Create table
create table T_PAY_ORDER
(
  PAYORDERID     VARCHAR2(50) not null,
  MCHID          VARCHAR2(30) not null,
  MCHORDERNO     VARCHAR2(50) not null,
  CHANNELID      VARCHAR2(24) not null,
  AMOUNT         NUMBER not null,
  CURRENCY       VARCHAR2(3) not null,
  STATUS         NUMBER,
  CLIENTIP       VARCHAR2(32),
  DEVICE         VARCHAR2(64),
  SUBJECT        VARCHAR2(64) not null,
  BODY           VARCHAR2(256) not null,
  EXTRA          VARCHAR2(512),
  CHANNELMCHID   VARCHAR2(32) not null,
  CHANNELORDERNO VARCHAR2(64),
  ERRCODE        VARCHAR2(64),
  ERRMSG         VARCHAR2(128),
  PARAM1         VARCHAR2(64),
  PARAM2         VARCHAR2(64),
  NOTIFYURL      VARCHAR2(128) not null,
  NOTIFYCOUNT    NUMBER,
  LASTNOTIFYTIME NUMBER(20),
  EXPIRETIME     NUMBER(20),
  PAYSUCCTIME    NUMBER(20),
  CREATETIME     DATE not null,
  UPDATETIME     DATE not null
)
tablespace ffpay_test
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table
comment on table T_PAY_ORDER
  is '支付订单表';
-- Add comments to the columns
comment on column T_PAY_ORDER.PAYORDERID
  is '支付订单号';
comment on column T_PAY_ORDER.MCHID
  is '商户ID';
comment on column T_PAY_ORDER.MCHORDERNO
  is '商户订单号';
comment on column T_PAY_ORDER.CHANNELID
  is '渠道ID';
comment on column T_PAY_ORDER.AMOUNT
  is '支付金额,单位分';
comment on column T_PAY_ORDER.CURRENCY
  is '三位货币代码,人民币:cny';
comment on column T_PAY_ORDER.STATUS
  is '支付状态,0-订单生成,1-支付中(目前未使用),2-支付成功,3-业务处理完成';
comment on column T_PAY_ORDER.CLIENTIP
  is '客户端IP';
comment on column T_PAY_ORDER.DEVICE
  is '设备';
comment on column T_PAY_ORDER.SUBJECT
  is '商品标题';
comment on column T_PAY_ORDER.BODY
  is '商品描述信息';
comment on column T_PAY_ORDER.EXTRA
  is '特定渠道发起时额外参数';
comment on column T_PAY_ORDER.CHANNELMCHID
  is '渠道商户ID';
comment on column T_PAY_ORDER.CHANNELORDERNO
  is '渠道订单号';
comment on column T_PAY_ORDER.ERRCODE
  is '渠道支付错误码';
comment on column T_PAY_ORDER.ERRMSG
  is '渠道支付错误描述';
comment on column T_PAY_ORDER.PARAM1
  is '扩展参数1';
comment on column T_PAY_ORDER.PARAM2
  is '扩展参数2';
comment on column T_PAY_ORDER.NOTIFYURL
  is '通知地址';
comment on column T_PAY_ORDER.NOTIFYCOUNT
  is '通知次数';
comment on column T_PAY_ORDER.LASTNOTIFYTIME
  is '最后一次通知时间';
comment on column T_PAY_ORDER.EXPIRETIME
  is '订单失效时间';
comment on column T_PAY_ORDER.PAYSUCCTIME
  is '订单支付成功时间';
comment on column T_PAY_ORDER.CREATETIME
  is '创建时间';
comment on column T_PAY_ORDER.UPDATETIME
  is '更新时间';
-- Create/Recreate primary, unique and foreign key constraints
alter table T_PAY_ORDER
  add constraint T_PAY_ORDER_PK primary key (PAYORDERID)
  using index
  tablespace ffpay_test
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );


-- Create table
create table T_USER
(
  USERID        VARCHAR2(32) not null,
  NAME          VARCHAR2(32) not null,
  EMAIL         VARCHAR2(32) not null,
  PHONENUMBER   VARCHAR2(32) not null,
  PASSWORD      VARCHAR2(32) not null,
  STATUS        VARCHAR2(2) not null,
  CREATETIME    DATE not null,
  LASTLOGINTIME DATE not null
)
tablespace ffpay_test
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Create/Recreate primary, unique and foreign key constraints
alter table T_USER
  add primary key (USERID)
  using index
  tablespace ffpay_test
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

create sequence T_MCH_INFO_S
minvalue 1
maxvalue 9999999999999999999999999999
start with 1000
increment by 1
cache 20;

-- Create sequence
create sequence T_PAY_CHANNEL_S
minvalue 1
maxvalue 9999999999999999999999999999
start with 1000
increment by 1
cache 20;


-- Create sequence
create sequence T_PAY_ORDER_S
minvalue 1
maxvalue 9999999999999999999999999999
start with 1000
increment by 1
cache 20;