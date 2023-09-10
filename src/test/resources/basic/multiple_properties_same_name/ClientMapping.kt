package basic.multiple_properties_same_name

public fun Client.toUser(): User =
    User().also {
        it.age = age
        it.name = name
        it.hobbies = hobbies
    }