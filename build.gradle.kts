plugins {
	java
	id("org.springframework.boot") version "3.3.5"
	id("io.spring.dependency-management") version "1.1.6"
}


// 실제 실행 환경의 Java 버전은 제어(x) 설정
java {
	sourceCompatibility = JavaVersion.VERSION_21
}


allprojects {
	group = "com.djukim"

	repositories {
		mavenCentral()
	}
}

subprojects {
	var lombokVersion = "1.18.30"

	apply(plugin = "idea")
	apply(plugin = "java")
	apply(plugin = "io.spring.dependency-management")

	dependencies {
		testImplementation("org.springframework.boot:spring-boot-starter-test")

		// lombok
		compileOnly("org.projectlombok:lombok")
		annotationProcessor("org.projectlombok:lombok")
	}

	tasks.test {
		useJUnitPlatform()
	}

	tasks.withType<JavaExec> {
		jvmArgs = listOf("--enable-preview")
	}
}

/* api, boot, batch 의존 분리 설정 */
var apiprojects = allprojects.filter {
	it.findProperty("type").toString().contains("api")
}
var bootprojects = allprojects.filter {
	it.findProperty("type").toString().contains("boot")
}
var batchprojects = allprojects.filter {
	it.findProperty("type").toString().contains("batch")
}

configure(apiprojects) {
	dependencies {
		implementation("org.springframework.boot:spring-boot-starter-web")
	}
}

configure(bootprojects) {
	apply(plugin = "org.springframework.boot")

	dependencies {
		implementation("org.springframework.boot:spring-boot-starter")
		annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
		implementation("org.springframework.boot:spring-boot-starter-aop")
	}
}






/*
withType 사용하여 test, integrationTest, unitTest 모두에 적용함
tasks.withType<Test> {
	useJUnitPlatform()
}
 */


