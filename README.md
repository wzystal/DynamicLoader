# DynamicLoader

基于 [任玉刚 dl-lib](https://github.com/DingMouRen/AndroidDynamicLoader) 的 Android 插件动态加载学习 Demo。扫描外置存储中的 APK，在桌面 Widget 上以网格展示，点击后通过 `DLPluginManager` 免安装启动插件 Activity。

## 项目背景

本项目是 Android 插件化技术的实践工程，核心目标是验证 **dl-lib 动态加载框架** 的可行性，并探索不同的插件展示与启动方式。经历了从 Activity 内嵌列表到桌面 Widget 的 UI 迁移，以及从底层 `DLClassLoader` 到高层 `DLPluginManager` 的 API 升级。

**这不是一个生产级插件分发平台。** NFS 挂载是作者开发机上的一次实验性改动，硬编码了内网 IP，并非项目的核心设计。

## 演进历程

| 阶段 | 时间 | 变化 |
|------|------|------|
| v1 | 2014.10 | `MainActivity` + `GridView` + `PluginAdapter`，扫描 `/sdcard/plugins/` |
| v2 | 2014.10 | 并行引入 `AppWidget` + `RemoteViewsService` 展示插件列表 |
| v3 | 2015.01.08 | 删除 `MainActivity`，升级 `dl-lib`，改用 `DLPluginManager` API |
| v4 | 2015.01.18 | 实验性加入 NFS 挂载（Root + libsuperuser），扫描路径改为 `/sdcard/apps` |

## 功能特性

- **桌面 Widget 插件墙**：4 列 GridView 展示 APK 图标与名称，支持主屏和锁屏（`widgetCategory="home_screen|keyguard"`）
- **动态加载启动**：未安装的插件通过 `DLPluginManager.loadApk()` + `DLProxyActivity` 代理启动
- **已安装优先**：包名已存在于系统中时，直接走系统 Launcher 启动
- **目录热更新**：`PluginsObserver` 监听插件目录文件增删，自动刷新 Widget 列表
- **NFS 实验**（可选）：Root 环境下挂载远程 NFS 目录到本地，用于开发调试

## 技术栈

| 类别 | 选型 |
|------|------|
| 语言 | Java |
| 平台 | Android API 14–21（`minSdkVersion=14`, `target=android-20`） |
| 构建 | Eclipse ADT + Ant |
| 插件框架 | `dl-lib.jar`（`com.ryg.dynamicload`，任玉刚插件化框架） |
| Root Shell | `libsuperuser.jar`（仅 NFS 实验使用） |
| 兼容库 | `android-support-v4.jar` |

## 项目结构

```
DynamicLoader/
├── AndroidManifest.xml              # 无 LAUNCHER Activity，仅 Widget + 代理 Activity
├── libs/
│   ├── dl-lib.jar                   # 动态加载核心（DLPluginManager / DLProxyActivity）
│   ├── libsuperuser.jar             # Root 命令（NFS 实验）
│   └── android-support-v4.jar
├── src/com/wzystal/dynamicloader/
│   ├── PluginsWidgetProvider.java   # Widget 入口：更新 UI、处理点击启动
│   ├── PluginsWidgetService.java    # RemoteViewsFactory：扫描 APK、渲染列表
│   ├── Plugin.java                  # 插件元数据（路径、包名、入口 Activity）
│   ├── PluginsObserver.java         # Widget 版 FileObserver → 刷新列表
│   ├── PluginObserver.java          # [遗留] MainActivity 时代的 FileObserver
│   └── util/
│       ├── Constant.java            # 路径常量、NFS 配置
│       ├── DLHelper.java            # 图标转换、已安装包名查询
│       └── LogHelper.java
└── res/
    ├── layout/widget_plugins.xml          # Widget 主布局
    ├── layout/gridview_item_plugins.xml   # 列表项布局
    ├── layout/activity_main.xml           # [遗留] 已删除的 MainActivity 布局
    └── xml/widget_provider_info.xml
```

## 工作原理

```
外置存储 APK 目录（/sdcard/apps 或 /sdcard/plugins/）
        │
        ▼
PluginsWidgetService.GridRemoteViewsFactory
  ├── [可选] Root 挂载 NFS → LOCAL_DIR
  └── 扫描 *.apk → 解析 Plugin 元数据
        │
        ▼
桌面 Widget GridView 展示插件图标
        │
   用户点击插件项
        │
   ┌────┴────┐
   ▼         ▼
 已安装    未安装
   │         │
   ▼         ▼
系统       DLPluginManager.loadApk(path)
Launcher      → DLIntent(package, activity)
启动            → DLProxyActivity 代理启动
```

### 动态加载关键代码

点击插件时的启动逻辑（`PluginsWidgetProvider.java`）：

```java
DLPluginManager pluginManager = DLPluginManager.getInstance(context);
pluginManager.loadApk(pluginPath);
DLIntent dlIntent = new DLIntent(packageName, launcherActivity);
dlIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
pluginManager.startPluginActivity(context, dlIntent);
```

Manifest 中必须注册 dl-lib 提供的代理 Activity：

- `com.ryg.dynamicload.DLProxyActivity`
- `com.ryg.dynamicload.DLProxyFragmentActivity`

## 使用方式

本应用 **没有桌面图标入口**（Manifest 中无 `MAIN` / `LAUNCHER` Activity），使用步骤：

1. 安装 APK 到设备
2. 长按桌面 → 添加小工具 → 选择 **DynamicLoader**
3. 将待加载的插件 APK 放入插件目录
4. 点击 Widget 中的插件图标即可启动

### 插件目录配置

定义在 `Constant.java`：

| 常量 | 路径 | 用途 |
|------|------|------|
| `DIR_PLUGINS` | `/sdcard/plugins/` | `PluginsObserver` 监听的目录（原始设计） |
| `LOCAL_DIR` | `/sdcard/apps` | Widget 实际扫描的目录（NFS 挂载点） |

> **注意**：v4 引入 NFS 后，扫描路径（`LOCAL_DIR`）与监听路径（`DIR_PLUGINS`）不一致，目录热更新可能对 NFS 挂载的插件不生效。这是未完成的重构，使用时建议将两处路径统一。

### NFS 配置（可选，需 Root）

```java
NFS_DIR   = "192.168.1.66:/home/wzystal/nfs/apps"  // 作者开发机，需按环境修改
LOCAL_DIR = "/sdcard/apps"                            // 挂载目标
```

## 构建

Eclipse ADT 时代的 Ant 工程：

```bash
# 需配置 ANDROID_HOME
android update project -p .
ant debug
adb install -r bin/DynamicLoader-debug.apk
```

或使用 Eclipse：Import → Existing Android Code Into Workspace → Run。

## 已知限制与遗留问题

| 问题 | 说明 |
|------|------|
| 无 Launcher 入口 | 只能通过 Widget 使用，应用列表中虽有安装记录但无法直接打开主界面 |
| 入口 Activity 简化 | `Plugin` 取 `PackageInfo.activities[0]` 作为入口，未解析真正的 LAUNCHER Intent |
| 路径不一致 | 扫描 `LOCAL_DIR`，监听 `DIR_PLUGINS`，热更新可能失效 |
| NFS 竞态 | `MountTask` 异步执行，首次 `initData()` 可能在挂载完成前运行，列表可能为空 |
| 遗留代码 | `PluginObserver`、`PluginsInitTask`、`activity_main.xml` 来自已删除的 MainActivity，未清理 |
| API 过时 | 目标 SDK 21，在现代 Android 上兼容性未验证 |

## 权限

```xml
INTERNET                          <!-- NFS 实验 -->
READ_EXTERNAL_STORAGE
WRITE_EXTERNAL_STORAGE
MOUNT_UNMOUNT_FILESYSTEMS         <!-- NFS 挂载 -->
```

## 相关资源

- [任玉刚 — Android 插件化动态加载（玉刚说）](https://github.com/DingMouRen/AndroidDynamicLoader)
- [libsuperuser](https://github.com/Chainfire/libsuperuser)
