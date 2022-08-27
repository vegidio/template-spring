package io.vinicius.tplspring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TemplateSpringApplication

fun main(args: Array<String>) {
    runApplication<TemplateSpringApplication>(*args)

    println("Server running on http://localhost:8080")
}
