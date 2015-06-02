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
│  │              │      ActivityController.java  可以用作管理系统控制器
│  │              │
│  │              ├─dao  主要为mybatis工具自动生成的dao层代码
│  │              │  │  ActivityMapper.java 活动相关表获取sequence接口
│  │              │  │  ......
│  │              │  └─mapping
│  │              │          ActivityMapper.xml
│  │              │          ......
│  │              ├─daowrapper  封装了mybatis自动生成的dao层代码
│  │              │  └─impl
│  │              │
│  │              ├─dto  mybatis自动生成的数据库--java对象映射
│  │              │
│  │              ├─enums  存放enums类型
│  │              │      QualifyType.java  资格类型，目前共9种
│  │              │      RewardLogStatus.java  奖励日志状态
│  │              │      RewardSituation.java  定义合适进行奖励
│  │              │      RewardType.java  奖励类型，目前共7种
│  │              │
│  │              ├─model  java模型
│  │              │      Activity.java  活动对象
│  │              │      EsOrderInfoBean.java  订单对象（历史代码）
│  │              │      HavePriority.java  拥有优先级接口
│  │              │      RewardLog.java  奖励日志对象
│  │              │      Rule.java  规则对象（定义了奖励资格）
│  │              │      RuleReward.java  奖励对象
│  │              │      SaleProduct.java  换购商品对象
│  │              │      SaleProductInfo.java  换购商品集合对象
│  │              │
│  │              ├─qualify  资格计算相关代码包
│  │              │  │  AbsQualify.java  资格抽象基类，使用桥接模式，组合不同资格算法和资格类型
│  │              │  │  OnceQualify.java  一次性资格
│  │              │  │  Qualify.java  获取资格接口
│  │              │  │  QualifyFactory.java  资格工程
│  │              │  │  SaleQualify.java  换购资格
│  │              │  │  TotalQualify.java  活动期间累积资格
│  │              │  │
│  │              │  ├─calculator  资格算法
│  │              │  │      CountCalculator.java  按购买个数计算
│  │              │  │      NominalCalculator.java  按面额计算
│  │              │  │      PaymentCalculator.java  按付款金额计算
│  │              │  │      QualifyCalculator.java  算法接口
│  │              │  │
│  │              │  └─strategy  资格策略
│  │              │          CountStrategy.java  按购买个数策略工厂实现
│  │              │          NominalStrategy.java  按面额策略工厂实现
│  │              │          PaymentStrategy.java  按支付金额策略工厂实现
│  │              │          QualifyStrategy.java  策略抽象工厂
│  │              │
│  │              ├─rewards  奖励相关代码包
│  │              │      AbsDiscountReward.java  满减奖励抽象基类
│  │              │      AbsNewCardReward.java  奖励卡抽象基类
│  │              │      AbsReward.java  实现奖励接口的抽象基类，主要用于参数双重校验，奖励日志记录
│  │              │      CashierRechargeReward.java  收银员奖励
│  │              │      DiscountReward.java  满减奖励
│  │              │      ExtraCardReward.java  实现类似“买三送一”奖励
│  │              │      NewCardReward.java  满额送卡奖励
│  │              │      PercentageReward.java  满额打折奖励
│  │              │      RechargePCardReward.java  充值主卡奖励
│  │              │      Rewardable.java  奖励接口
│  │              │      RewardFactory.java  奖励工厂
│  │              │      SaleReward.java  换购奖励
│  │              │
│  │              ├─service  服务层包
│  │              │  │  ActivityFactory.java  活动工厂，可以获取不同的活动信息来源，从而兼容其他活动系统
│  │              │  │  ActivityService.java  定义了活动所提供的服务
│  │              │  │  WapPayService.java  历史代码，主要用于根据订单号获取订单对象
│  │              │  │
│  │              │  └─impl
│  │              │          ActivityServiceImpl.java  活动具体实现
│  │              │
│  │              └─utils 工具包
│  │                      ListUtils.java  列表工具类
│  │                      PriorityComparator.java  优先级比较器，倒序排序
│  │                      SysPara.java  历史代码
└─test
    └─java
        └─com
            └─opvita
                └─activity  活动相关测试代码所在的包
                        ActivityServiceTests.java  测试类，（！！！ 建议从测试类开始阅读 ！！！）
                        RollbackableTest.java  测试基类
