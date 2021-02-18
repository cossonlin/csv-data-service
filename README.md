# Project Title

CSV data service

## Getting Started

### Prerequisites

* [JDK 8](http://www.oracle.com/technetwork/pt/java/javase/overview/index.html) - Ensure JAVA_HOME environment variable
  is set and points to your JDK installation

* [Maven](https://maven.apache.org/) - Dependency Management Download from https://maven.apache.org/

### Installing & Running the tests

Run maven clean install command will install the dependencies, compile and run the tests

```
mvn clean install
```

### And coding style tests

Test cases cover the implementation methods for Command Interface

```
@Test
public void saveCsvFile_ShouldReturnTaskId_WhenStartSavingRecordsFromCsv() throws IOException {
	long expectedTaskId = 1;
	Task task = Task.builder().id(expectedTaskId).build();
	MultipartFile file = new MockMultipartFile("test-data.csv", new FileInputStream("src/test/resources/test-data.csv"));
	when(taskService.createTask(anyInt())).thenReturn(task);
	long returnTaskId = csvService.saveCsvFile(file);
	assertEquals(expectedTaskId, returnTaskId);
}
```

## Deployment

JAR package will be created under target/csv-data-service-{versionNo}.jar after packaging then you can run below command
to bring up the application

```
java -jar target/csv-data-service-{versionNo}.jar
```

## Usage

### Upload Csv file

```
POST http://localhost:8080/csvdata/file
```

File will be attached as MultipartFile

### Search Data

```
GET http://localhost:8080/csvdata/data/search
```

Search data by different criteria. If multiple criteria are provided, all of them must be fulfilled, meaning it's AND
for them.

### Task progress query

```
GET http://localhost:8080/task/{taskId}
```

Get the percentage of given task

## Built With

* [Maven](https://maven.apache.org/)

## Authors

* **Lin Lin**