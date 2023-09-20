package custom.method_in_class

public fun Client.toUser(): User =
    User().also {
        it.age = abs(age)
    }