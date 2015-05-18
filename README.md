# activity
线上支付满减，满额折扣，满额送商品促销活动解决方案

├─main
│  ├─java
│  │  └─com
│  │      └─opvita
│  │          └─activity
│  │              ├─common  历史代码
│  │              │
│  │              ├─controller
│  │              │      ActivityController.java  用作管理系统控制器
│  │              │
│  │              ├─dao  主要为mybatis工具自动生成的dao层代码
│  │              │  │  ActivityMapper.java 活动相关表获取sequence接口
│  │              │  │  ......
│  │              │  │
│  │              │  └─mapping
│  │              │          ActivityMapper.xml
│  │              │          ......
│  │              │
│  │              ├─daowrapper  封装了mybatis自动生成的dao层代码
│  │              │  └─impl
│  │              │
│  │              ├─dto  mybatis自动生成的数据库--java对象映射
│  │              │
│  │              ├─enums  存放enums类型
│  │              │      RewardLogStatus.java  奖励日志状态
│  │              │      RewardSituation.java  定义合适进行奖励
│  │              │
│  │              ├─model  java模型
│  │              │      Activity.java         活动对象
│  │              │      EsOrderInfoBean.java  历史订单对象
│  │              │      RewardLog.java        奖励日志对象
│  │              │      Rule.java             规则对象（定义了奖励资格）
│  │              │      RuleReward.java       奖励对象
│  │              │
│  │              ├─qualify  资格计算相关代码包
│  │              │      NominalQualify.java   按照面额计算资格
│  │              │      PaymentQualify.java   按照支付金额计算资格
│  │              │      Qualify.java          资格接口
│  │              │      QualifyFactory.java   资格工厂
│  │              │
│  │              ├─rewards  奖励相关代码包
│  │              │      AbsDiscountReward.java  满减奖励抽象基类，子类有满减奖励和满额打折奖励
│  │              │      AbsReward.java          实现奖励接口的抽象基类，主要用于参数双重校验
│  │              │      DiscountReward.java     满减奖励
│  │              │      NewCardReward.java      满额开新卡奖励
│  │              │      PercentageReward.java   满额打折奖励
│  │              │      Rewardable.java         奖励接口
│  │              │      RewardFactory.java      奖励工厂
│  │              │
│  │              ├─service  服务层包
│  │              │      ActivityService.java      定义了活动所提供的服务
│  │              │      ActivityServiceImpl.java  活动具体实现
│  │              │      WapPayService.java        历史代码，主要用于根据订单号获取订单对象
│  │              │
│  │              └─utils  工具包
│  │                      ListUtils.java  列表工具类
│  │                      SysPara.java    历史代码
│
└─test
    └─java
        └─com
            └─opvita
                └─activity  活动相关测试代码所在的包
                        ActivityServiceTests.java  测试类，（建议从测试类开始阅读）
                        RollbackableTest.java      测试基类
