# RemoveNote - 注释清理工具

**RemoveNote** 是一个用于批量清理代码文件中注释的工具，适用于交付项目时清除冗余注释，支持多种文件类型（如 Java、HTML、YAML、JavaScript、TypeScript 等）拥有 URL 跳过选项。提供用户友好的图形界面和多种配置选项，开箱即用，支持跨平台。

## 功能特性

- **支持文件类型**：支持 `.java`、`.html`、`.yaml`、`.yml`、`.js`、`.ts` 文件中的注释清理。
- **清理选项**：
  - 删除单行注释（`//`）。
  - 删除多行注释（`/* */` 和 `/** */`）。
  - 可选择跳过清理注释中的 URL。
- **跨平台**：支持通过 JAR 包运行，也提供 Windows 平台的可执行文件。
- **简单易用**：提供图形化用户界面（GUI）。

---

## 安装与运行

### 1. 下载

从 [Releases 页面](https://github.com/RL1127/RemoveNote/releases/tag/untagged-06184f08df174be885a2) 下载适合的版本：

- **Windows EXE 文件**：`RemoveNote.exe`
- **跨平台 JAR 文件**：`RemoveNote-1.0-SNAPSHOT.jar`

---

### 2. 系统要求

- **Windows**：适用于 EXE 文件运行的操作系统。
- **Java 环境**（运行 JAR 文件时需要）：
  - Java 11 或更高版本（推荐使用最新的 LTS 版本）。

---

### 3. 运行程序

#### Windows 用户
直接双击运行下载的 `RemoveNote.exe` 文件。

#### 其他平台用户
运行下载的 JAR 文件：
```bash
java -jar RemoveNote-1.0-SNAPSHOT.jar
