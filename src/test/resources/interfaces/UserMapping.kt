package interfaces

public fun User.toClient(): Client =
    Client().also {
        it.age = abs(age)
        it.id = id
    }