# Cursor Prompt: 创建一个 Android 记账本 App 项目（Java）

## 🎯 项目目标
开发一个 **Android 记账本 App**，用于记录每日收支和制定财务计划。应用需具有整洁美观的界面，并支持以下功能：

---

## 📱 功能需求

### 1. 主界面（MainActivity）
- 显示今日收支概况
- 导航按钮跳转到：
  - 添加记录页面
  - 收支明细列表
  - 财务统计页面

### 2. 添加记录页面（AddRecordActivity）
- 输入字段：
  - 金额（EditText）
  - 类型（收入 / 支出）（Spinner）
  - 类别（如餐饮、交通）（Spinner）
  - 日期选择器（DatePicker）
  - 备注（可选）
- 点击保存按钮后，将数据存入本地数据库（SQLite）

### 3. 收支明细页面（RecordListActivity）
- 列出所有收支记录（ListView 或 RecyclerView）
- 支持按日期或类型筛选
- 长按记录可删除

### 4. 财务统计页面（StatisticsActivity）
- 按日 / 月统计收入与支出
- 显示图表（可使用 MPAndroidChart）

---

## 💾 数据存储（SQLite）
- 表名：`record`
- 字段：
  - `id` INTEGER PRIMARY KEY
  - `amount` REAL
  - `type` TEXT
  - `category` TEXT
  - `date` TEXT
  - `remark` TEXT

---

## 🎨 布局与美化
- 所有页面使用 XML 编写，布局整齐
- 使用 Material Design 风格或 ConstraintLayout
- 使用图标、色彩分明的按钮提升用户体验

---

## ✅ 技术要求
- 使用 Java 编写
- 最低兼容 API 21+
- 使用多个 Activity 实现功能模块划分
- 使用 Intent 在 Activity 之间传值
- 使用 SQLiteOpenHelper 管理数据库

---

## 📦 项目打包提交要求
请生成以下文件并打包为压缩包：
- Java 源代码文件
- XML 布局文件
- 可安装 APK 文件
- Word 格式说明文档（作品简介 + 功能介绍 + 界面截图）

压缩包命名格式：**学号+姓名.zip**

---

## 📚 附加建议
- 使用 Git 进行版本管理
- 文档中请包含以下内容：
  - 应用功能介绍
  - 数据表设计图
  - 主要界面截图
  - 关键代码说明
  - 项目遇到的问题与解决方法

---

## 🛑 注意事项
- 独立完成，禁止抄袭或代做
- 功能完整性、界面美观性和文档质量均影响评分
