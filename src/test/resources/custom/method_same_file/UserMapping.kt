package custom.method_in_class

public fun User.toClient(): Client =
    Client().also {
        it.age = abs(age)
    }