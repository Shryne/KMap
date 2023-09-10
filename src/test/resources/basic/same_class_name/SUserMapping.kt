package basic.same_name

public fun User.toUser(): basic.same_name.other.User =
    basic.same_name.other.User().also {
        it.age = age
        it.name = name
        it.hobbies = hobbies
    }