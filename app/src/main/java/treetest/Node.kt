package com.example.treetest

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

data class Node(
    val id: String = generateId(),
    val parent: Node? = null,
    val children: MutableList<Node> = mutableListOf()
) {
    val name: String = generateName(id)

    companion object {
        fun generateId(): String = System.currentTimeMillis().toString()

        fun generateName(input: String): String {
            return try {
                val digest = MessageDigest.getInstance("SHA-256")
                val hash = digest.digest(input.toByteArray())
                val last20Bytes = hash.takeLast(20).toByteArray()
                last20Bytes.joinToString("") { "%02x".format(it) }
            } catch (e: NoSuchAlgorithmException) {

                input.takeLast(20)
            }
        }
    }
}


