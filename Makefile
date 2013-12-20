tweeter:
	mvn clean install
run:
	mvn cargo:run
test:
	mvn test
clean:
	rm upload/*
	rm *~
	mvn clean

