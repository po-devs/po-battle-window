# Html Battle Window

###Requirements

On Linux:

```
sudo apt-get install unzip
sudo apt-get install default-jdk
sudo apt-get install gradle
```

On Windows, install `chocolatey` and then in administrative command line:

```
#You may also need to install the appropriate jdk with choco install jdkX (with X being 7, 8, ...)
choco install devbox-unzip gradle
```

You also need to install [git-lfs](https://git-lfs.github.com/).

##Setup

This will fetch the sprites, which are not stored as is in the directory:

```
git lfs fetch
unzip assets/oras.zip -d assets/
```

###Build

You can run the desktop test version from your IDE as per libgdx' guidelines.

You can also build it for use in the webclient.

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
