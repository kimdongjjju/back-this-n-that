plugins {
	java
	`java-library`
	id("org.springframework.boot") version "3.3.5" apply false
	id("io.spring.dependency-management") version "1.1.6" apply false
}

allprojects {
	group = "com.djukim"

	repositories {
		mavenCentral()
	}
}

subprojects {
	val lombokVersion = "1.18.30"

	apply(plugin = "idea")
	apply(plugin = "java")
	apply(plugin = "io.spring.dependency-management")

	configure<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension> {
		imports {
			mavenBom("org.springframework.boot:spring-boot-dependencies:3.3.5")
		}
	}

	dependencies {

		testImplementation("org.springframework.boot:spring-boot-starter-test")

		// lombok
		compileOnly("org.projectlombok:lombok")
		annotationProcessor("org.projectlombok:lombok")
		testAnnotationProcessor("org.projectlombok:lombok:$lombokVersion")
		testImplementation("org.projectlombok:lombok:$lombokVersion")
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
var libprojects = allprojects.filter {
	it.findProperty("type").toString().contains("lib")
}


configure(apiprojects) {
	dependencies {
		implementation("org.springframework.boot:spring-boot-starter-web")
	}
}

configure(bootprojects) {
	apply(plugin = "org.springframework.boot")

	tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
		enabled = false
		mainClass.set("com.djukim.thisnthat.ThisnthatApiApplication")
	}

	tasks.getByName<Jar>("jar") {
		enabled = true
	}

	dependencies {
		implementation("org.springframework.boot:spring-boot-starter")
		annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
		implementation("org.springframework.boot:spring-boot-starter-aop")
	}
}

configure(libprojects) {
	apply(plugin = "org.springframework.boot")
	apply(plugin = "java-library")


	dependencies {
		implementation("org.springframework.boot:spring-boot-starter-web")
	}

	val bootJar: org.springframework.boot.gradle.tasks.bundling.BootJar by tasks
	bootJar.enabled = false
	val jar: Jar by tasks
	jar.enabled = true
}




/*
withType 사용하여 test, integrationTest, unitTest 모두에 적용함
tasks.withType<Test> {
	useJUnitPlatform()
}
 */


