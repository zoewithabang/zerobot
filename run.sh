mvn clean install -q -Dmaven.test.skip=true
chmod +x target/zerobot-1.11-jar-with-dependencies.jar
java -jar target/zerobot-1.11-jar-with-dependencies.jar