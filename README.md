# 记账本App项目文档

## 目录
- [1. 作品简介](#1-作品简介)
  - [1.1 项目概述](#11-项目概述)
  - [1.2 功能特点](#12-功能特点)
  - [1.3 技术特点](#13-技术特点)
- [2. 界面设计与实现](#2-界面设计与实现)
  - [2.1 整体架构](#21-整体架构)
  - [2.2 界面展示](#22-界面展示)
  - [2.3 数据库设计](#23-数据库设计)
- [3. 实现步骤](#3-实现步骤)
  - [3.1 环境搭建](#31-环境搭建)
  - [3.2 功能实现](#32-功能实现)
  - [3.3 关键代码说明](#33-关键代码说明)
- [4. 编译与运行](#4-编译与运行)
  - [4.1 开发环境要求](#41-开发环境要求)
  - [4.2 运行步骤](#42-运行步骤)
  - [4.3 注意事项](#43-注意事项)
- [5. 界面展示](#5-界面展示)
  - [5.1 主要界面](#51-主要界面)
  - [5.2 功能演示](#52-功能演示)

## 1. 作品简介

### 1.1 项目概述
本项目是一个基于Android平台开发的个人记账本应用，采用Material Design设计风格，为用户提供简洁直观的记账体验。应用支持收入支出记录、数据统计分析等功能，帮助用户更好地管理个人财务。

### 1.2 功能特点
- **收支记录管理**
  - 添加收入/支出记录
  - 支持多种收支类别
  - 备注功能
  - 日期选择
- **数据统计与分析**
  - 日/月收支统计
  - 分类支出占比分析
  - 饼图可视化展示
- **收支明细查看**
  - 列表展示所有记录
  - 按类型筛选
  - 支持记录删除

### 1.3 技术特点
- 采用SQLite数据库存储数据
- 使用Material Design组件
- 实现MVP架构设计模式
- 集成MPAndroidChart图表库

## 2. 界面设计与实现

### 2.1 整体架构
```
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/tallybook/
│   │   │   ├── MainActivity.java
│   │   │   ├── AddRecordActivity.java
│   │   │   ├── RecordListActivity.java
│   │   │   ├── StatisticsActivity.java
│   │   │   ├── db/
│   │   │   │   └── DatabaseHelper.java
│   │   │   └── model/
│   │   │       └── Record.java
│   │   └── res/
│   │       ├── layout/
│   │       │   ├── activity_main.xml
│   │       │   ├── activity_add_record.xml
│   │       │   ├── activity_record_list.xml
│   │       │   ├── activity_statistics.xml
│   │       │   └── item_record.xml
│   │       └── values/
│   │           ├── colors.xml
│   │           ├── strings.xml
│   │           └── themes.xml
└── build.gradle.kts
```

### 2.2 界面展示

#### 主界面
主界面采用卡片式设计，顶部显示今日收支概况，下方为功能导航按钮。

#### 添加记录界面
使用Material Design的输入控件，包括：
- 金额输入框（数字键盘）
- 类型选择（下拉菜单）
- 类别选择（下拉菜单）
- 日期选择（日期选择器）
- 备注输入（多行文本）

#### 收支明细界面
使用RecyclerView实现列表展示，每个条目包含：
- 类别
- 金额（收入绿色，支出红色）
- 日期
- 备注信息

#### 统计界面
- 使用TabLayout实现日/月统计切换
- MPAndroidChart饼图展示支出分布
- 列表显示详细统计数据

### 2.3 数据库设计

#### Record表结构
```sql
CREATE TABLE record (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    amount REAL NOT NULL,
    type TEXT NOT NULL,
    category TEXT NOT NULL,
    date TEXT NOT NULL,
    remark TEXT
);
```

## 3. 实现步骤

### 3.1 环境搭建
1. 创建Android项目
2. 配置build.gradle.kts文件
3. 添加必要的依赖：
```kotlin
dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
}
```

### 3.2 功能实现

#### 1. 数据库实现
创建`DatabaseHelper`类继承`SQLiteOpenHelper`，实现数据库的创建和升级。

#### 2. 主界面实现
- 实现今日收支统计查询
- 添加功能导航按钮
- 设置页面跳转

#### 3. 添加记录功能
- 实现表单验证
- 实现日期选择器
- 实现数据保存

#### 4. 收支明细功能
- 实现RecyclerView适配器
- 实现列表项布局
- 实现数据筛选功能

#### 5. 统计功能
- 实现饼图统计
- 实现日/月切换
- 实现详细数据展示

### 3.3 关键代码说明

#### 数据库操作
```java
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tallybook.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_RECORD = "record";
    // ... 字段定义

    private static final String DATABASE_CREATE = "create table "
            + TABLE_RECORD + "(" 
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_AMOUNT + " real not null, "
            + COLUMN_TYPE + " text not null, "
            + COLUMN_CATEGORY + " text not null, "
            + COLUMN_DATE + " text not null, "
            + COLUMN_REMARK + " text);";

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }
}
```

#### 添加记录
```java
private void saveRecord() {
    // 获取输入数据
    String amountStr = etAmount.getText().toString();
    String type = spinnerType.getText().toString();
    String category = spinnerCategory.getText().toString();
    String date = etDate.getText().toString();
    String remark = etRemark.getText().toString();

    // 数据验证
    if (amountStr.isEmpty()) {
        Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
        return;
    }

    try {
        // 保存到数据库
        double amount = Double.parseDouble(amountStr);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_AMOUNT, amount);
        values.put(DatabaseHelper.COLUMN_TYPE, type);
        values.put(DatabaseHelper.COLUMN_CATEGORY, category);
        values.put(DatabaseHelper.COLUMN_DATE, date);
        values.put(DatabaseHelper.COLUMN_REMARK, remark);

        long newRowId = db.insert(DatabaseHelper.TABLE_RECORD, null, values);
        // ... 处理结果
    } catch (NumberFormatException e) {
        Toast.makeText(this, "请输入有效的金额", Toast.LENGTH_SHORT).show();
    }
}
```

#### 统计功能
```java
private void loadStatistics(String datePattern) {
    SQLiteDatabase db = dbHelper.getReadableDatabase();
    Map<String, Double> categorySum = new HashMap<>();
    double totalExpense = 0;

    // 查询支出数据
    Cursor cursor = db.rawQuery(
        "SELECT " + DatabaseHelper.COLUMN_CATEGORY + ", SUM(" + DatabaseHelper.COLUMN_AMOUNT + ") " +
        "FROM " + DatabaseHelper.TABLE_RECORD + " WHERE " + DatabaseHelper.COLUMN_TYPE + "=? " +
        "AND " + DatabaseHelper.COLUMN_DATE + " LIKE ? GROUP BY " + DatabaseHelper.COLUMN_CATEGORY,
        new String[]{"支出", datePattern}
    );

    // ... 处理数据并更新UI
}
```

## 4. 编译与运行

### 4.1 开发环境要求
- Android Studio Hedgehog | 2023.1.1 或更高版本
- JDK 17 或更高版本
- Android SDK Platform 34 (Android 14.0)
- Gradle 8.0 或更高版本
- 最低支持 Android API 21 (Android 5.0)

### 4.2 运行步骤
1. **克隆项目**
   ```bash
   git clone https://github.com/yourusername/Tallybook.git
   ```

2. **打开项目**
   - 启动Android Studio
   - 选择"Open an existing Android Studio project"
   - 导航到项目目录并打开

3. **配置项目**
   - 等待Gradle同步完成
   - 确保所有依赖项都已下载完成

4. **运行应用**
   - 连接Android设备或启动模拟器
   - 点击工具栏的"Run"按钮或按下Shift + F10
   - 选择目标设备并等待应用安装完成

### 4.3 注意事项
1. **权限要求**
   - 应用需要以下权限：
     - 读写外部存储权限（用于数据备份，可选）
     - 网络权限（用于后续在线功能，可选）

2. **数据存储**
   - 所有数据存储在本地SQLite数据库中
   - 数据库文件位置：`/data/data/com.example.tallybook/databases/tallybook.db`
   - 卸载应用会清除所有数据，请注意备份

3. **性能优化**
   - 大量数据时建议使用分页加载
   - 图表统计时注意内存占用

## 5. 界面展示

### 5.1 主要界面

#### 主界面
主界面采用Material Design风格，包含以下元素：
- 顶部卡片显示今日收支概况
- 三个主要功能按钮采用MaterialButton样式
- 配色方案采用紫色主题

界面布局代码：
```xml
<androidx.cardview.widget.CardView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="16dp"
    app:cardCornerRadius="8dp"
    app:cardElevation="4dp">
    <!-- 内容布局 -->
</androidx.cardview.widget.CardView>
```

#### 添加记录界面
表单设计特点：
- 使用TextInputLayout提供更好的输入体验
- 下拉选择器使用AutoCompleteTextView实现
- 日期选择器使用MaterialDatePicker
- 备注框支持多行输入和中文输入法

#### 收支明细界面
列表实现特点：
- 使用RecyclerView实现高效滚动
- 列表项使用卡片式设计
- 收支金额使用不同颜色区分
- 支持按类型筛选功能

#### 统计界面
图表展示特点：
- 使用MPAndroidChart实现饼图统计
- 支持图表动画效果
- 可切换日/月统计视图
- 底部显示详细数据列表

### 5.2 功能演示

#### 添加记录流程
1. 点击"添加记录"按钮
2. 输入金额（支持小数点）
3. 选择收支类型（收入/支出）
4. 选择具体类别
5. 选择或修改日期
6. 添加备注信息（可选）
7. 点击保存完成记录

#### 数据统计流程
1. 进入统计界面
2. 选择统计周期（日/月）
3. 查看饼图展示的支出分布
4. 查看底部详细数据列表
5. 可以点击图例查看具体类别数据

## 6. 开发心得

### 6.1 技术难点
1. **数据库设计与优化**
   - 合理设计表结构
   - 优化查询性能
   - 处理并发访问

2. **UI实现**
   - Material Design组件的使用
   - 自定义控件的开发
   - 动画效果的实现

3. **数据统计**
   - 数据聚合计算
   - 图表展示
   - 性能优化

### 6.2 解决方案
1. **数据库优化**
   - 使用索引提高查询速度
   - 采用事务处理保证数据一致性
   - 使用ContentValues简化数据操作

2. **UI优化**
   - 使用ConstraintLayout提高布局效率
   - 实现ViewHolder模式提高列表性能
   - 使用主题定制统一界面风格

3. **统计功能优化**
   - 使用HashMap缓存统计数据
   - 采用异步加载减少主线程压力
   - 实现数据分页加载

### 6.3 项目亮点
1. **用户体验**
   - 简洁直观的界面设计
   - 流畅的操作响应
   - 合理的数据展示方式

2. **技术实现**
   - 规范的代码结构
   - 完善的注释文档
   - 合理的架构设计

3. **扩展性**
   - 预留功能扩展接口
   - 模块化的代码设计
   - 灵活的配置管理

## 7. 未来展望

### 7.1 功能扩展
1. 添加数据导出功能
2. 实现预算管理功能
3. 添加账单提醒功能
4. 支持多账户管理

### 7.2 技术改进
1. 引入MVVM架构
2. 使用Room数据库
3. 添加单元测试
4. 优化应用性能

### 7.3 用户体验提升
1. 支持自定义主题
2. 添加手势操作
3. 优化数据展示方式
4. 增加数据分析功能 