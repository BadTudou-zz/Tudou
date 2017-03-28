# Android联系人提供程序
https://developer.android.com/guide/topics/providers/contacts-provider.html

并不仅仅指“通讯录”中的联系人，而是指应用程序中的账户，如QQ、WeChat。
## 类型
保留了3种类型(3个表)的联系人数据。
ContactsContract.Contacts:表示不同联系人的行，基于聚合的原始联系人行
ContactsContract.RawContacts:包含联系人数据摘要的行，针对特定用户账户和类型。
ContactsContract.Data:包含原始联系人详细信息，针对特定用户账户和类型。
Data.RAW_CONTACT_ID，包含其父级RawContacts.ID值。
(RowContacts包含Data)

原始联系人：表示来自某一账户类型和账户名称、有关某个联系人的数据，存储于ContactsContract.Data的1行或多行。
（允许 1个联系人 对应 多个原始联系人）
Contacts(联络)
Contract(协议)

RawContacts:
ACCOUNT_NAME:账户名称，如’123’, ‘badtudou@google.com’
ACCOUNT_TYPE:账户类型，如’com.google’。(请务必使用您拥有或控制的域标识符限定您的账户类型。这可以确保您的账户类型具有唯一性。)
DELETED:”已删除”标志(客户端将其设置为t，代表删除，删除请求发送至服务器物理删除后，再在客户端将该记录真正删除)
 原始联系人的姓名存储在Data表的ContactsContract.CommonDataKinds.StructuredName行内。
若不在AccountManager中注册账户，联系人提供程序将自动删除您的原始联系人行。

Data:
RAW_CONTACT_ID:对应原始联系人_ID列
MIMETYPE：自定义MIME类型
IS_PRIMARY：主要

DATA1-DATA15, SYNC1-SYNC4(同步适配器)
DATA1索引列，DATA15预留列，存储二进制大型对象(BLOB)数据
包含：显示姓名、电话号码、电子邮件、邮政地址、照片以及网站。
列有通用名称与描述性名称。

## 类型专用列名称类
映射类	数据类型	说明
ContactsContract.CommonDataKinds.StructuredName	与该数据行关联的原始联系人的姓名数据。	一位原始联系人只有其中一行。
ContactsContract.CommonDataKinds.Photo	与该数据行关联的原始联系人的主要照片。	一位原始联系人只有其中一行。
ContactsContract.CommonDataKinds.Email	与该数据行关联的原始联系人的电子邮件地址。	一位原始联系人可有多个电子邮件地址。
ContactsContract.CommonDataKinds.StructuredPostal	与该数据行关联的原始联系人的邮政地址。	一位原始联系人可有多个邮政地址。
ContactsContract.CommonDataKinds.GroupMembership	将原始联系人链接到联系人提供程序内其中一组的标识符。	组是帐户类型和帐户名称的一项可选功能。 联系人组部分对其做了更详尽的描述。

系统不允许应用或同步适配器添加联系人。
合并了联系人 LOOKUP_KEY 的内容 URI CONTENT_LOOKUP_URI 仍将指向联系人行。

## 读取联系人所需权限
想要访问联系人提供程序的应用必须请求以下权限：

对一个或多个表的读取权限
READ_CONTACTS，在 AndroidManifest.xml 中指定，使用 <uses-permission> 元素作为 <uses-permission android:name="android.permission.READ_CONTACTS">。
对一个或多个表的写入权限
WRITE_CONTACTS，在 AndroidManifest.xml 中指定，使用 <uses-permission> 元素作为 <uses-permission android:name="android.permission.WRITE_CONTACTS">。

## 用户个人资料
ContactsContract.Contacts 表有一行包含设备用户的个人资料数据。
android.Manifest.permission.READ_PROFILE 和   android.Manifest.permission.WRITE_PROFILE 权限进行读取和写入访问。