package different_accessor.other_get_set_java

public fun User.toClient(): Client =
    Client().also {
        it.age = theAge()
        it.name = name
    }