核心功能文件：
用户管理相关：
   UserSession.java - 用户会话管理
   UserData.java - 用户数据模型
   LoginController.java 和 LoginView.fxml - 登录界面
   SignUpController.java 和 SignUp.fxml - 注册界面
数据库相关：
   DatabaseService.java - 数据库服务核心类
   DatabaseInitializer.java - 数据库初始化
   database_zh.md 和 database_en.md - 数据库文档
主要界面：
   GamePlatformApp.java - 应用程序入口
   MainController.java 和 MainView.fxml - 主界面
   GameViewController.java 和 GameView.fxml - 游戏视图
游戏功能：
GameLauncher.java - 游戏启动器
反馈系统：
   BugReportController.java 和 BugReport.fxml - 问题反馈
   ReviewController.java 和 Review.fxml - 游戏评价
   BugData.java 和 ReviewData.java - 数据模型
6. 开发者工具：
   DeveloperLoginController.java 和 DeveloperLogin.fxml - 开发者登录
   DeveloperManageController.java 和 DeveloperManageView.fxml - 开发者管理界面
工具类：
   LanguageUtil.java - 语言切换工具
   run.bat - 启动脚本