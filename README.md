# Html Battle Window

###Requirements

On Linux:

```
sudo apt-get install default-jdk
sudo apt-get install gradle
```

On Windows, install `chocolatey` and then in administrative command line:

```
#You may also need to install the appropriate jdk with choco install jdkX (with X being 7, 8, ...)
choco install gradle
```

###Build

On Linux:
```
./gradlew html:dist
```

On Windows, in **powershell**:
```
gradlew.bat html:dist
```

###Run

This is not a stand-alone repository. It is used as a submodule of `po-devs/po-web`.
