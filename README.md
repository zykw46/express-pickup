# 快递代取平台

一个基于 SpringBoot3 + Vue3 的快递代取平台，支持管理员、配送员、普通用户三种角色。

## 技术栈

### 后端
- SpringBoot 3.2.0
- Sa-Token 1.37.0 (权限认证)
- MyBatis-Plus 3.5.5 (ORM框架)
- Druid 1.2.20 (连接池)
- MySQL 8.0.12
- Redis
- JWT

### 前端
- Vue 3.4
- Element Plus 2.5
- Vue Router 4
- Pinia (状态管理)
- Axios
- ECharts

## 项目结构

```
express-pickup/
├── backend/          # 后端项目
│   ├── src/main/java/com/express/pickup/
│   │   ├── common/   # 通用类
│   │   ├── config/   # 配置类
│   │   ├── controller/ # 控制器
│   │   ├── entity/   # 实体类
│   │   ├── mapper/   # Mapper接口
│   │   ├── service/  # 服务类
│   │   ├── utils/    # 工具类
│   │   └── vo/       # 视图对象
│   └── src/main/resources/
│       ├── mapper/   # XML映射文件
│       └── application.yml # 配置文件
├── frontend/         # 前端项目
│   ├── src/
│   │   ├── api/      # API接口
│   │   ├── components/ # 组件
│   │   ├── layouts/  # 布局
│   │   ├── router/   # 路由
│   │   ├── store/    # 状态管理
│   │   ├── utils/    # 工具
│   │   └── views/    # 页面
│   └── package.json
└── sql/              # 数据库脚本
    └── init.sql
```

## 功能模块

### 管理员模块
- 登录功能
- 首页数据统计（订单趋势图、状态分布图）
- 用户管理
- 角色管理
- 快递站点管理
- 包裹规格管理
- 宿舍楼管理
- 订单管理
- 配送员审核
- 通知公告管理

### 配送员模块
- 登录/注册
- 接单大厅
- 接单记录
- 我的收益

### 用户端模块
- 登录/注册
- 首页
- 快递代取下单
- 我的订单
- 账户充值
- 配送员认证
- 个人中心

## 运行步骤

### 1. 数据库准备

1. 安装 MySQL 8.0 和 Redis
2. 创建数据库：
```sql
CREATE DATABASE express_pickup DEFAULT CHARACTER SET utf8mb4;
```
3. 执行初始化脚本：
```bash
mysql -u root -p express_pickup < sql/init.sql
```

### 2. 后端运行

```bash
cd backend

# 使用 Maven 运行
mvn spring-boot:run

# 或者打包后运行
mvn clean package
java -jar target/express-pickup-1.0.0.jar
```

后端服务默认运行在 http://localhost:8080

### 3. 前端运行

```bash
cd frontend

# 安装依赖
npm install

# 开发模式运行
npm run dev

# 打包
npm run build
```

前端服务默认运行在 http://localhost:3000

## 默认账号

- 管理员：admin / admin123
- 普通用户：需要注册

## 配置说明

### 后端配置 (application.yml)

```yaml
# 数据库配置
spring.datasource.url: jdbc:mysql://localhost:3306/express_pickup
spring.datasource.username: root
spring.datasource.password: 123456

# Redis配置
spring.redis.host: localhost
spring.redis.port: 6379

# 订单配置
express.order.base-price: 2.00      # 基础价格
express.order.delivery-rate: 0.70   # 配送员分成比例(70%)
```

## API 文档

后端 API 接口前缀: `/api`

主要接口：
- 认证: `/auth/*`
- 用户: `/user/*`
- 角色: `/role/*`
- 订单: `/order/*`
- 快递站点: `/station/*`
- 包裹规格: `/spec/*`
- 宿舍楼: `/building/*`
- 配送员认证: `/certification/*`
- 账户: `/account/*`
- 通知公告: `/notice/*`
- 统计: `/statistics/*`

## 注意事项

1. 确保 MySQL 和 Redis 服务已启动
2. 修改数据库连接配置为你的实际配置
3. 前端开发时代理已配置，会自动转发到后端
4. 生产环境需要配置 Nginx 反向代理

## 部署建议

### 生产环境部署

1. 后端打包部署到服务器
2. 前端打包后将 dist 目录部署到 Nginx
3. 配置 Nginx 反向代理到后端服务
4. 配置 SSL 证书启用 HTTPS

### Nginx 配置示例

```nginx
server {
    listen 80;
    server_name your-domain.com;
    
    location / {
        root /path/to/frontend/dist;
        index index.html;
        try_files $uri $uri/ /index.html;
    }
    
    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

## 许可证

MIT License
