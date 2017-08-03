# 对volley的二次封装，支持http/https，支持添加headers

# 使用方法：

1.将 library_volley 文件夹以 Module添加到自己的工程，并且确保
  dependencies {
      compile project(':library_volley')
  }
  
2.添加网络权限
  <uses-permission android:name="android.permission.INTERNET" />

3.APP入口即extends Application并添加以下代码
  RequestManager.getInstance().init(this);
  
4.具体使用详情前往MainActivity查看
