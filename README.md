# remote-deploy
用于远程部署项目，通过xml配置，上传文件并执行指定命令。

**最终构建目录**
```
成功构建后配置remote-config.xml，并执行start.bat即可完成部署操作。
日志配置文件内置在remote-deploy-*.jar中，默认输出到控制台。

├─bin
│      start.bat
│
├─config
│      remote-config.xml
│
└─lib
        dom4j-1.6.1.jar
        remote-deploy-1.0-SNAPSHOT.jar
        slf4j-api-1.7.25.jar
        slf4j-simple-1.7.25.jar
        sshd-core-1.7.0.jar
        xml-apis-1.0.b2.jar
```

**remote-config.xml使用说明**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <!--
    <remote/>：远端操控配置，可以配置多个
    active：是否激活当前节点，若active="false"则表示不激活，不配或其他值均表示激活
    user：远端SSH登录帐号
    password：远端SSH登录密码
    ip：远端连接IP
    port：远端连接端口，默认“22”，可不配置
    -->
    <remote active="true" user="root" password="12345678" ip="127.0.0.1">
        <!--
        <update/>：上传文件配置，可配置多个
        active：同remote.active
        -->
        <update active="false">
            <!--
            <files/>：上传文件列表配置
            local：本地文件路径，可文件夹可文件，若为文件夹则上传文件夹下的所有文件
            remote：远端存放路径（文件夹级别）
            remoteName：上传后的文件名，若无则同本地文件名称
            remoteBakPath：文件备份目录，将检查所有上传的文件，若远端存在文件则进行 mv -f 操作
            isCleanUp：是否 rm -rf 操作
            -->
            <files local="F:\\test.txt" remote="" remoteName="" remoteBakPath="" isCleanUp="true" >
                <!--
                <file/>：上传文件，可配置多个，属性值及含义同 <files/>
                注意：若配置了files.*属性，则files下的file可不配置对应的属性，自动继承files.*，若file也配置了，则覆盖files.*
                -->
                <file remote="/root/test/"/>
            </files>
        </update>
        <!--
         <update-after/>：文件上传后需要执行的命令集
         -->
        <update-after>
            <!--
            <command/>：远端执行命令，可配置多个
            -->
            <command>ls /root/test</command>
        </update-after>
    </remote>

</configuration>
```
**Code目录结构**
```text
├─main
│  ├─java
│  │  └─com
│  │      └─clyy
│  │          │  DeployApplication.java // 启动类，负责解析xml并执行任务
│  │          │  RemoteCommand.java // 命令执行器
│  │          │  RemoteConnection.java // 远端链接器
│  │          │  RemoteUpdate.java // 文件上传器
│  │          │
│  │          └─entity
│  │                  Remote.java // 装载xml<remote/>数据
│  │                  RemoteLink.java // 装载xml<remote/>属性数据
│  │                  UpdateFile.java // 装载xml<update/>数据
│  │
│  └─resources
│          remote-config.xml    // 数据配置文件模版
│          simplelogger.properties  // 日志配置
│
└─test
    ├─java
    │  └─com
    │      └─clyy
    │              DeployApplicationTest.java   // 测试类 
    │
    └─resources
            simplelogger.properties // c

```