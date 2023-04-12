package io.vinicius.tplspring.feat.user

import io.vinicius.tplspring.exception.NotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepo: UserRepository) {
    fun findById(id: Int): User =
        userRepo.findByIdOrNull(id) ?: throw NotFoundException(detail = "No user found with this id")

    fun findByEmail(email: String): User =
        userRepo.findByEmail(email) ?: throw NotFoundException(detail = "No user found with this id")
}