package different_accessor.other_get

public fun Client.toUser(): User =
    User().also {
        it.theAge(age)
        it.name = name
    }