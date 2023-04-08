Lombok annotations :
-------------------
@Builder - Lombok annotation for Builder methods
@RequiredArgsConstructor - Constructor injection
@Slf4j - Logging

TestContainer library is used to write Integration Tests :
--------------------------------------------------------

Dependencies  added -

<dependencyManagement>
<dependencies>
<dependency>
<groupId>org.testcontainers</groupId>
<artifactId>testcontainers-bom</artifactId>
<version>1.18.0</version>
<type>pom</type>
<scope>import</scope>
</dependency>
</dependencies>
</dependencyManagement>

<dependency>
<groupId>org.testcontainers</groupId>
<artifactId>mysql</artifactId>
<scope>test</scope>
</dependency>

<dependency>
<groupId>org.testcontainers</groupId>
<artifactId>junit-jupiter</artifactId>
<scope>test</scope>
</dependency>

Key changes -

@Testcontainers - to be added at the top of the test class
@AutoConfigureMockMvc - Used to test rest endpoints
@Container
static MySQLContainer mySQLContainer = new MySQLContainer("mysql:5.5"); - Used to define the container

@DynamicPropertySource
static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
dynamicPropertyRegistry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
dynamicPropertyRegistry.add("spring.datasource.username", mySQLContainer::getUsername);
dynamicPropertyRegistry.add("spring.datasource.password", mySQLContainer::getPassword);
dynamicPropertyRegistry.add("spring.datasource.driver-class-name", mySQLContainer::getDriverClassName);
} - Sets the container properties before executing the test cases.
