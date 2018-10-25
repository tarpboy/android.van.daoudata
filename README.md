# Android VAN Main-Application

Android vAN Main Application은 Payfun device(이하 EMV Reader)를 smartphone에 연동하여 결제할 수 있는 VAN사별 Android 결제 앱을 목적으로 한다.

이 프로젝트는 Android VAN Lib 모듈, Sub-Application 모듈을 import하여 사용한 소스이며 EMV Reader 하드웨어(DABxxx)에서 동작한다.

초기 버전:

H/W: DABxxxxx

Lib: android.van.lib (V1.0.x)

sub App: android.van.app (V1.0.x)

Android: 

1. DaouData: Android.van.daoudata(V1.0.x)



## Getting Started

먼저 repository에서 <u>lib modue</u> 과 <u>van sub application</u>, <u>project</u>를 다운 받는다.

<u>lib module</u> 과 <u>van sub application</u>는 동일한 directory내부에 위치하도록 하여야한다.

이유는 이 <u>project</u> 는 android.van.app, lib를 module로 import하고 있기 때문이다.

별도의 설정없이 개발환경에서 그냥 compile하면된다.

**[Build Gradle]**

1. settings.gradle

   import external modules: library, van_app_daou, profile, bbdevice, 'signpad', keyboard, receipt, print, etc.

   ```
   include ':AppPayFun', ':library', ':bbdevice', ':keyboard', ':signpad', 'receipt', 'print', 'van_app_daou', 'profile'
   
   project(':library').projectDir=new File('../android.van.lib/library')
   project(':bbdevice').projectDir=new File('../android.van.lib/bbdevice')
   project(':keyboard').projectDir=new File('../android.van.lib/keyboard')
   project(':signpad').projectDir=new File('../android.van.lib/signpad')
   project(':receipt').projectDir=new File('../android.van.lib/receipt')
   project(':print').projectDir=new File('../android.van.lib/print')
   project(':van_app_daou').projectDir=new File('../android.van.app/van_app_daou')
   project(':profile').projectDir=new File('../android.van.app/profile')
   ```

2. project build gradle

   export common declaration to compile.

   만약 이 정의에서 up version을 사용하게되면 application build gradle의 dependance를 version에 맞게 변경해줘야한다.

   ```
   ext {
       //  Common Declaration
       buildTools = '27.0.3'
       compileSdk = 27
       targetSdk = 27
       minSdk = 21
       minSdkInstant = 21
       versionCode = 1
       versionName = "1.0.0"
       glide = "3.7.0"
   
       //  Special Declaration for libs
   }
   ```

3. application build gradle

   A) using exported common declaration to compile.

   ```
   android {
       compileSdkVersion rootProject.compileSdk
   
       defaultConfig {
           applicationId "com.ginu.android.smartlink3.ibeacon"
           minSdkVersion rootProject.minSdk
           targetSdkVersion rootProject.targetSdk
           versionCode rootProject.versionCode
           versionName rootProject.versionName
           
           testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
       }
   ```

   B) setting module dependance

   * add implementation project(':library'), (':signpad'), ...

   * 

   ```
   dependencies {
       implementation fileTree(include: ['*.jar'], dir: 'libs')
       implementation 'com.android.support:appcompat-v7:27.1.1'
       //implementation 'com.android.support:appcompat-v7:xx.x.+'
       implementation 'com.android.support:design:27.1.1'
       implementation 'com.android.support.constraint:constraint-layout:1.1.2'
       implementation 'com.google.firebase:firebase-core:16.0.1'
       implementation 'com.google.firebase:firebase-messaging:17.0.0'
       implementation 'com.google.android.gms:play-services-base:15.0.1'
       implementation files('libs/jsoup-1.8.1.jar')
       testImplementation 'junit:junit:4.12'
       androidTestImplementation 'com.android.support.test:runner:1.0.2'
       androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.2'
       implementation project(':library')
       api project(':bbdevice')
       implementation project(':keyboard')
       implementation project(':signpad')
       implementation project(':receipt')
       implementation project(':van_app_daou')
       implementation project(':profile')
       implementation project(':print')
   }
   
   //  MUST ADD THIS AT THE BOTTOM
   apply plugin: 'com.google.gms.google-services'
   ```

4. Google GMS Service

   google gms serice를 위해서는 build.gradle파일에 위와 같이 제일 하단에 plugin을 정의해줘야한다.

   그리고 main application(AppPayFun) 디렉토리에 google-services.json을 다운받아 넣어 놔야한다. 

   ```
   $> AppPaFun/google-services.json
   ```



### Prerequisites

What things you need to install the software and how to install them

```
EDI: Android Studio Version 3.2.1
```

### Installing

A step by step series of examples that tell you have to get a development env running

Say what the step will be

```
1. downloading van Library 
git clone http://DavidSrv/david/android.van.lib
there is DavidSrc: 192.168.20.50 (local repository)
2. downloading sub app module
git clone http://DavidSrv/david/android.van.app
3. downloading project
git clone http://DavidSrv/david/android.van.daou

```

And repeat

```
until finished
```

End with an example of getting some data out of the system or using it for a little demo

## Running the tests

Explain how to run the automated tests for this system

### Break down into end to end tests

Explain what these tests test and why

```
Give an example
```

### And coding style tests

Explain what these tests test and why

```
Give an example
```

## Deployment

Add additional notes about how to deploy this on a live system

## Built With

* [Dropwizard](http://www.dropwizard.io/1.0.2/docs/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [ROME](https://rometools.github.io/rome/) - Used to generate RSS Feeds

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

1. **V1.0.0**: *daou van sub-application module*, 18/10/25




## Authors

* **David SH Kim** - *Initial work* - [david@ginu.co.kr](https://192.168.20.50/david)

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to anyone who's code was used
* Inspiration
* etc

