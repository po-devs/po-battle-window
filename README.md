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
choco install wget gradle
#To extract in command line. Not needed if you want to extract manually.
choco install devbox-unzip
```

##Setup

This will fetch the sprites, which are not stored as is in the directory:

```
wget http://web.pokemon-online.eu/public/battle/oras.zip -P assets/
#To extract. Can also just go in assets/ and extract oras.zip manually
cd assets && unzip oras.zip && cd -
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

You can change `html/build.gradle` and replace `OBF` by `PRETTY` to have non-minified output, easier to debug.

###Run

This is not a stand-alone repository. It is used as a submodule of `po-devs/po-web`.
