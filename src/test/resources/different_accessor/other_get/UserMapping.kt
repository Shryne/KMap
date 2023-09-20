package different_accessor.other_get

public fun User.toClient(): Client =
    Client().also {
        it.age = theAge()
        it.name = name
    }