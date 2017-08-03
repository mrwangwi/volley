<h1> 对volley的二次封装，支持http/https，支持添加headers</h1>
<h3> 使用方法：</h3>
<h4>1.将 library_volley 文件夹以 Module添加到自己的工程，并且确保</h4>
compile project(':library_volley')
<h4>2.添加网络权限</h4>
<h4>3.APP入口即extends Application并添加以下代码</h4>
RequestManager.getInstance().init(this);
<h4>4.以下是简单例子，具体使用详情前往MainActivity查看</h4>
a.接口返回：{"code":1,"message":"SUCCESS","data":{"timeStamp":1501750360}}
<p></p>
b.创建实体Test.class

c.直接请求获取对象

RequestManager.getInstance().get(url, new ReqListener<>(Test.class, new SuccessError<Test>() {

            @Override

            public void onSuccess(Test test) {

                //请求成功，test直接使用

                Log.e("MainActivity", "testBean.getData().getTimeStamp():" + test.getData().getTimeStamp());

            }


            @Override

            public void onError(String errorMsg) {

            }
        }));

