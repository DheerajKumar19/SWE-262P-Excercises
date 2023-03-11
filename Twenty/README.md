## Build Commands

```bash
# run this from root
$ javac .\MainFramework\*.java .\Plugins\*.java

# Creates the main (executable) framework jar
$ jar cvmf .\Build\framework.jar .\MainFramework\manifest.mf .\MainFramework\Framework.class .\MainFramework\Counter.class .\MainFramewor
k\ExtractWords.class

$ cd Plugins
$ jar cvf ..\Build\normal_plugin.jar .\NormalWordCounter.class .\ExtractWordsNormal.class ..\MainFramework\Counter.class ..\MainFramework\ExtractWords.class
$ jar cvf ..\Build\abnormal_plugin.jar .\CounterBasedOnFirstLetter.class .\ExtractWordsZWords.class ..\MainFramework\Counter.class ..\MainFramework\ExtractWords.class       

#To run the program
$ cd Build
$ java -jar .\framework.jar .\pride-and-prejudice.txt
```