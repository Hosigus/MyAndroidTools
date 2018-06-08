# Hosigus's Android Utils

##### ——个人开发Android项目的工具类集, 使用语言为Kotlin



### - Download

```
implementation 'com.hosigus:tools:1.3'
```



### - What it contains ?

- *DensityUtils*
- *DownloadManager*
- *Encryptor*
- *LogUtils*
- *NetUtils*
- *PermissionUtils*
- *ThreadUtils*
- *ToastUtils*





### - What is it used for ?

- to get screen size, dp2px/px2dp, make dialog full screen
- multithreaded section downloader
- encrypt md5 string
- manage logs
- use post/get to get JSONBean from internet
- easy to deal with permission after API 23
- manage threads , async / main thread
- easy to toast , avoid repetition show toast





###  - How to use it ? [[show me demo](https://github.com/Hosigus/MyToolsDemo)]

- *DownloadManager*
  1. init manager
    ```kotlin
    val option = DownloadOption(url,filePath,fileName)
    			  .listener(object : DownloadListener {
    				//there are other functions:
    				// 'onStart','onPaused','onCancel','onFailed(e: Exception)'
    				  override fun onDownloading(progress: Int, size: Int) {
    					TODO("update progress")
    				  }
    				  override fun onSuccess(download:File) {
    					TODO("use the file")
    				  }
    			  })
    val manager = DownloadManager(this, option)
    ```

  2. use manager to control download
    ```kotlin
    //start download
    manager.download()

    //pause download
    manager.pause()//use it if you want to keep cache for continue

    //continue download
    manager.download()

    //cancel download
    manager.cancel()//it will delete cache file,make sure you want to cancel download plan

    //delete the file which have download successfully
    manager.delete()

    //start print logs, you may need it when debug
    manager.openLog()

    //close print logs, I think it's useless,I'm considering about to delete it
    manager.closeLog()
    ```

- PermissionUtils

  1.  add option ,  it needs code(unique),permissions,callback

     ```kotlin
     addOption(PermissionOption(requestCode)
                     .permissions(arrayOf(Manifest.permission.XXX))
                     .callback(object : PermissionCallback {
                       //there is another function: 
                       // onDenied(DeniedPermissions: Array<String>){}
                         override fun onGranted() {
                             TODO("do something needs permissions")
                         }
                     }))
     ```

  2.  override fun , let me agent permission for you

     ```kotlin
     override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
             super.onRequestPermissionsResult(requestCode, permissions, grantResults)
             onPermissionRequest(requestCode,permissions,grantResults)
     }
     ```

  3. use option , execute the option/plan you made before

     ```kotlin
     executeOption(this@XXXActivity, requestCode)
     ```

- [update later]