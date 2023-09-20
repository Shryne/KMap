package different_accessor.other_set

public fun User.toClient(): Client =
    Client().also {
        it.age = age
        it.name = name
    }