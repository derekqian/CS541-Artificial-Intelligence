all : heart_anomalies.java
	javac heart_anomalies.java

test : 
	@java -ea heart_anomalies spect-orig.train.csv spect-orig.test.csv
	@java -ea heart_anomalies spect-itg.train.csv spect-itg.test.csv
	@java -ea heart_anomalies spect-resplit.train.csv spect-resplit.test.csv

clean : 
	rm *.class
