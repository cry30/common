IMAGE_ID=amazoncorretto:17.0.6-al2

build:
	docker run --rm -it -v "${PWD}":/java-common -w /java-common $(IMAGE_ID)  javac src/main/java/ph/rye/common/io/ConsoleInputReader.java

install:  # Makes it available locally to other projects via maven.
	mvn install