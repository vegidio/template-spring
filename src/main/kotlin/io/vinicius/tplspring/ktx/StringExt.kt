package io.vinicius.tplspring.ktx

fun String.capitalize() = this.replaceFirstChar { it.uppercase() }