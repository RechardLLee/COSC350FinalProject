# 游戏平台 (Game Platform)

这是一个基于JavaFX开发的多语言游戏平台系统，支持中文和英文界面切换。

## 功能特性

### 1. 用户系统
- 用户登录/注册
- 开发者登录入口
- 语言切换（中文/英文）

### 2. 游戏管理
- 游戏展示（包括贪吃蛇等多个游戏）
- 游戏购买
- 游戏下载
- 游戏启动

### 3. 反馈系统
- 问题反馈（Bug Report）
- 游戏评价（1-5星评分）
- 用户评论

## 技术架构

- 开发语言：Java
- UI框架：JavaFX
- 项目结构：MVC模式

### 主要组件

1. **控制器类**
   - MainController：主界面控制器
   - GameViewController：游戏视图控制器
   - SignUpController：注册控制器
   - ReviewController：评价控制器
   - BugReportController：问题反馈控制器
   - DeveloperLoginController：开发者登录控制器

2. **工具类**
   - LanguageUtil：语言工具类，管理中英文切换

3. **视图文件（FXML）**
   - MainView.fxml：主界面
   - GameView.fxml：游戏界面
   - SignUp.fxml：注册界面
   - Review.fxml：评价界面
   - BugReport.fxml：问题反馈界面
   - DeveloperLogin.fxml：开发者登录界面

## 界面预览

### 主界面
- 顶部工具栏：用户登录、注册、语言切换
- 中央区域：游戏展示网格
- 右侧边栏：问题反馈、评价、开发者入口

### 游戏界面
- 游戏图标展示
- 游戏描述
- 购买/下载/启动按钮

## 运行说明

### 系统要求
- JDK 8或更高版本
- JavaFX运行环境

### 启动方式
1. Windows环境下双击运行`run.bat`
2. 或通过命令行：
