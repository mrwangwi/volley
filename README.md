<h1> 对volley的二次封装，支持http/https，支持添加headers</h1>
<h3> 使用方法：</h3>
<h6>1.将 library_volley 文件夹以 Module添加到自己的工程，并且确保</h6>
compile project(':library_volley')
<h6>2.添加网络权限</h6>
\<uses-permission android:name="android.permission.INTERNET" /\>
<h6>3.APP入口即extends Application并添加以下代码</h6>
RequestManager.getInstance().init(this);
<h6>4.具体使用详情前往MainActivity查看</h6>

