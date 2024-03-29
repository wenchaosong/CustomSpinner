# 自定义 Spinner
[![](https://jitpack.io/v/wenchaosong/CustomSpinner.svg)](https://jitpack.io/#wenchaosong/CustomSpinner)

自定义继承TextView,效果类似 spinner
在[nice-spinner](https://github.com/arcadefire/nice-spinner)的基础上增加了设置 layout 的构造方法，感谢作者

## 例子
![image](/gifs/nice-spinner.gif )  

## 使用

- Step 1. 把 JitPack repository 添加到build.gradle文件中 repositories的末尾:
```
repositories {
    maven { url "https://jitpack.io" }
}
```
- Step 2. 在你的app build.gradle 的 dependencies 中添加依赖
```
dependencies {
	compile 'com.github.wenchaosong:CustomSpinner:1.4.0'
}
```
- Step 3

```
xml

	<lib.spinner.CustomSpinner
        android:id="@+id/edit_spinner"
        android:layout_width="200dp"
        android:layout_height="wrap_content"
        android:layout_centerInParent="true"
        android:gravity="center"
        android:hint="提示"
        android:textColor="#0000ff"
        app:cs_arrow="@drawable/arrow"
        app:cs_arrow_height="10dp"
        app:cs_arrow_width="15dp"
        app:cs_elevation="15dp"
        app:cs_list_height="400dp"
        app:cs_hide_arrow="false"
        app:cs_popup_background="@color/bg" />

    <rotate xmlns:android="http://schemas.android.com/apk/res/android"
            android:drawable="@drawable/arrow"
            android:duration="200"
            android:fromDegrees="0"
            android:pivotX="50%"
            android:pivotY="50%"
            android:toDegrees="180" />
```

```
代码

        List<String> dataset = new LinkedList<>(Arrays.asList("One", "Two", "Three", "Four", "Five"));
        MaterialSpinner mEditSpinner = (MaterialSpinner) findViewById(R.id.spinner);
        mEditSpinner.setItems(dataset);
```
    
