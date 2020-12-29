all: run

# to get jaxb-api.jar, install libjaxb-api-java & libjaxb-java
%.class: %.java
	javac -classpath /usr/share/java/jaxb-api.jar:. $^

run: timepiece/GenAlg.class
	java -classpath /usr/share/java/jaxb-runtime.jar:. timepiece.GenAlg

clean:
	-rm timepiece/*.class
