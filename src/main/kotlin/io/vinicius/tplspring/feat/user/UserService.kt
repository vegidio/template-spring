package io.vinicius.tplspring.feat.user

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepo: UserRepository) {
    fun findByEmail(email: String): User {
        return userRepo.findByEmail(email) ?: throw NotFoundException()
    }
}