package basic.multiple_properties_same_name

public fun User.toClient(): Client =
    Client().also {
        it.age = age
        it.name = name
        it.hobbies = hobbies
    }