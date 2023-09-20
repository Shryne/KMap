package different_accessor.other_get_set_java

public fun Client.toUser(): User =
    User().also {
        it.setAge(age)
        it.name = name
    }