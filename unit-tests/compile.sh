# source 
source ~/.bash_profile

# Important, to avoid compile errors
cd src/

### Classes
javac -d ../bin/ codingfactory/rpgconsole/enemy/*.java
javac -d ../bin/ codingfactory/rpgconsole/hero/*.java
javac -d ../bin/ codingfactory/rpgconsole/game/*.java

### Tests
javac -d ../bin/ test/HeroTest.java