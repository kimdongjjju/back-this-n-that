rootProject.name = "thisnthat"
include(
    "thisnthat-api",
    "thisnthat-service",
    "thisnthat-core",
    "thisnthat-domain"
)

project(":thisnthat-api").projectDir = file("presentation/thisnthat-api")
project(":thisnthat-service").projectDir = file("application/thisnthat-service")
project(":thisnthat-core").projectDir = file("core/thisnthat-core")
project(":thisnthat-domain").projectDir = file("domain/thisnthat-domain")



