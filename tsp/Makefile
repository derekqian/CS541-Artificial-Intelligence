all : tsp.java
	javac tsp.java
	javac tsp_node.java
	javac tsp_edge.java

test :
#	@java -ea tsp oregon-mileage.txt test-instance.txt
	@echo processing small-instance.txt with the first algorithm ...
	@java -ea tsp -n oregon-mileage.txt small-instance.txt > small-result-1.txt
	@echo processing small-instance.txt with the second algorithm ...
	@java -ea tsp oregon-mileage.txt small-instance.txt > small-result-2.txt
	@echo processing medium-instance.txt with the first algorithm ...
	@java -ea tsp -n oregon-mileage.txt medium-instance.txt > medium-result-1.txt
	@echo processing medium-instance.txt with the second algorithm ...
	@java -ea tsp oregon-mileage.txt medium-instance.txt > medium-result-2.txt
	@echo processing large-instance.txt with the first algorithm ...
	@java -ea tsp -n oregon-mileage.txt large-instance.txt > large-result-1.txt
	@echo processing large-instance.txt with the second algorithm ...
	@java -ea tsp oregon-mileage.txt large-instance.txt > large-result-2.txt

clean :
	rm *.class
	rm *result*.txt

