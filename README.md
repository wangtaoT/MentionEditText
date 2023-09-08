## Android 仿小红书 EditText 插入#话题、@用户



### 演示
<img src="https://github.com/wangtaoT/MentionEditText/blob/main/demo.jpeg" width="250" />  



### 使用说明

#### 监听#、@符号

```kotlin
binding.etContent.editDataListener = object : EditDataListener {
            override fun onEditAddAt(str: String, start: Int, length: Int) {
                //@符号
            }

            override fun onEditAddHashtag(start: Int) {
                //#符号
            }

            override fun onCloseEdit() {
                //结束输入 如：空格、换行等
            }
        }
```

#### 光标位置插入高亮区域

```kotlin
//插入话题
binding.etContent.insert(MentionTopic("11", "国庆快乐"))
//插入用户
binding.etContent.insert(MentionUser("22", "王王王"))
```

#### 普通文字转换为高亮区域

```kotlin
binding.etContent.insertConvert(
                MentionTopic(
                    "11",
                    "国庆快乐",
                ),
                0,
                6
            )
```

#### 光标位置插入文字

```kotlin
binding.etContent.insertText("@")
```

#### 获取话题列表

```kotlin
val list = binding.etContent.formatResult.topicList
```

#### 获取用户列表

```kotlin
val list = binding.etContent.formatResult.userList
```

#### 获取当前话题个数

```kotlin
binding.etContent.topicLength
```

#### 获取当前用户个数

```kotlin
binding.etContent.userLength
```

#### 清空

```kotlin
binding.etContent.clear()
```

