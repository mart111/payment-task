
# Payment System Task

Simple payment system task.

Project consists of 2 modules.

`be-payment` and
`fe-payment` (`BE`  stands for Back-End, `FE` stands for Front-End)



## Installation

Make sure you have docker installed and JDK 17.

NOTE: If you don't have <b>GraalVM</b> installed, run the below command with `-DSkipTests`.

```bash
  mvn clean package
```

This will build both modules and create docker images.

Then run.

``` docker-compose up ```

## API documentation

API documentation available under `./swagger` folder.