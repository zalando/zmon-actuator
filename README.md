![Maven Central](https://img.shields.io/maven-central/v/org.zalando.zmon/zmon-actuator.svg)

## Usage

Add the following dependency to your spring-boot project.

## Security

Add security to your management endpoints or move management endpoints to a different non-public port.

```
management:
  port: 7979
```

## Maven Dependency

```
<dependency>
   <groupId>org.zalando.zmon</groupId>
   <artifactId>zmon-actuator</artifactId>
   <version>${version}</version>
</dependency>
```

You need to have the base actuator dependency in place, too.

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

## Spring Boot with Jersey vs SpringMVC

See the [Jersey module provided](https://github.com/zalando/zmon-actuator/tree/master/zmon-actuator-jaxrs-jersey)
and the [example-application](https://github.com/zalando/zmon-actuator/tree/master/samples/zmon-actuator-jetty-jersey).

Important is to use at least the specified Jersey versions as defined in the POM. These will be included in spring boot 1.3+

## Result on /metrics endpoint

```
{  
  "zmon.response.200.GET.rest.api.v1.checks.all-active-check-definitions.count": 10,
  "zmon.response.200.GET.rest.api.v1.checks.all-active-check-definitions.fifteenMinuteRate": 0.18076110580284566,
  "zmon.response.200.GET.rest.api.v1.checks.all-active-check-definitions.fiveMinuteRate": 0.1518180485219247,
  "zmon.response.200.GET.rest.api.v1.checks.all-active-check-definitions.meanRate": 0.06792011610723951,
  "zmon.response.200.GET.rest.api.v1.checks.all-active-check-definitions.oneMinuteRate": 0.10512398137982051,
  "zmon.response.200.GET.rest.api.v1.checks.all-active-check-definitions.snapshot.75thPercentile": 1173,
  "zmon.response.200.GET.rest.api.v1.checks.all-active-check-definitions.snapshot.95thPercentile": 1233,
  "zmon.response.200.GET.rest.api.v1.checks.all-active-check-definitions.snapshot.98thPercentile": 1282,
  "zmon.response.200.GET.rest.api.v1.checks.all-active-check-definitions.snapshot.999thPercentile": 1282,
  "zmon.response.200.GET.rest.api.v1.checks.all-active-check-definitions.snapshot.99thPercentile": 1282,
  "zmon.response.200.GET.rest.api.v1.checks.all-active-check-definitions.snapshot.max": 1282,
  "zmon.response.200.GET.rest.api.v1.checks.all-active-check-definitions.snapshot.mean": 1170,
  "zmon.response.200.GET.rest.api.v1.checks.all-active-check-definitions.snapshot.median": 1161,
  "zmon.response.200.GET.rest.api.v1.checks.all-active-check-definitions.snapshot.min": 1114,
  "zmon.response.200.GET.rest.api.v1.checks.all-active-check-definitions.snapshot.stdDev": 42,
}
```

## Release to Maven Central

    mvn clean release:prepare -Dresume=false

    mvn release:perform

## License

Copyright © 2015 Zalando SE

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
