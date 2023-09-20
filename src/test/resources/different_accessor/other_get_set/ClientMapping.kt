package different_accessor.other_get_set

public fun Client.toUser(): User =
    User().also {
        it.age = age
        it.name = name
    }