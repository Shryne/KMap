package different_accessor.other_set

public fun Client.toUser(): User =
    User().also {
        it.theAge(age)
        it.name = name
    }