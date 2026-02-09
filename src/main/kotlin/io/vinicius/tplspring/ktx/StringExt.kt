package io.vinicius.tplspring.ktx

fun String.capitalized() = this.replaceFirstChar { it.uppercase() }